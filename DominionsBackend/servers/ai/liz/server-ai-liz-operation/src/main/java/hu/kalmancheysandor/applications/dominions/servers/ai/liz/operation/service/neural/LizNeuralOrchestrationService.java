package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.service.neural;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizNeuralNetworkTrainingConfiguration;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.PlayState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.player.PlayerData;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.action.GameAction;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.exception.GeneralGameStateException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.queue.AiHistoryQueueItem;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistory;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistorySession;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTraining;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingTask;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.execution.LizNeuralConceptExecutionConflictException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.execution.LizNeuralConceptExecutionNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.queue.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryPlayer;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryScenario;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralExecution;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralConcept;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.LizNeuralConceptNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.LizNeuralConceptNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.neural.training.LizNeuralTrainingTaskNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryPlayerRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryScenarioRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistorySessionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.LizNeuralConceptRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.service.TLizService;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity.history.History;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.etc.PlayerDecision;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.MimeType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;


@Slf4j
@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class LizNeuralOrchestrationService extends TLizService {

    @Autowired
    private LizHistoryRepository lizHistoryRepository;

    @Autowired
    private LizHistorySessionRepository lizHistorySessionRepository;
    @Autowired
    private LizHistoryPlayerRepository lizHistoryPlayerRepository;

    @Autowired
    private LizHistoryScenarioRepository lizHistoryScenarioRepository;

    @Autowired
    private LizNeuralConceptRepository lizNeuralConceptRepository;

    @Autowired
    private LizNeuralExecutionRepository lizNeuralExecutionRepository;

    @Autowired
    private LizNeuralTrainingRepository lizNeuralTrainingRepository;

    @Autowired
    private LizNeuralTrainingTaskRepository lizNeuralTrainingTaskRepository;

    @Autowired
    private LizNeuralTrainingService lizNeuralTrainingService;

    @Autowired
    private LizNeuralQueueService lizNeuralQueueService;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    private StreamBridge streamBridge;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private ObjectMapper objectMapper;

    /// ///////////////////////////////////////////////////////////////////////////////////////////////////
    /// EVENT METHODS /////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////

    @Async
    public void eventExecutionStart(String conceptUuid) {

        // Access entity: Toplevel record
        LizNeuralConcept conceptToAccess = accessConceptRecordByUuid(conceptUuid);

        //
        if (!isExecutionCreationAllowed(conceptToAccess)) {
            throw new RuntimeException("New execution is not allowed");
        }

        //
        LizNeuralExecution execution = atomicCreateStructure(conceptToAccess);

        //
        execution = accessAnExecutionRecordAndLockIt(execution.getId());

        //
        //
        collectAllCreatedTasksAndRunThem(execution);

        //
        execution.setProcessPhase(LizNeuralExecution.ProcessPhase.RUNNING);
        execution.setDateModified(LocalDateTime.now());
    }

    @Async
    public void eventExecutionContinue(String conceptUuid) {
        // Access entity: Toplevel record
        LizNeuralConcept conceptToAccess = accessConceptRecordByUuid(conceptUuid);

        //
        if (!isExecutionContinuingAllowed(conceptToAccess)) {
            throw new RuntimeException("Execution continuing is not allowed!");
        }

        //
        LizNeuralExecution execution = lizNeuralExecutionRepository.findFirstPaused(conceptToAccess.getId());
        if (execution == null) {
            throw new RuntimeException("Execution(paused) is not found!");
        }

        //
        execution = accessAnExecutionRecordAndLockIt(execution.getId());

        //
        atomicContinueExecution(execution);
    }

    @Async
    public void eventExecutionPause(String conceptUuid) {
        System.out.println("EVENT:Pause");
        // Access entity: Toplevel record
        LizNeuralConcept conceptToAccess = accessConceptRecordByUuid(conceptUuid);

        //
        if (!isExecutionPausingAllowed(conceptToAccess)) {
            throw new RuntimeException("Execution pausing is not allowed!");
        }

        //
        LizNeuralExecution execution = lizNeuralExecutionRepository.findFirstRunning(conceptToAccess.getId());
        if (execution == null) {
            throw new RuntimeException("Execution(running) is not found!");
        }

        //
        execution = accessAnExecutionRecordAndLockIt(execution.getId());

        //
        atomicPauseExecution(execution);
    }

    @Async
    public void eventExecutionCancel(String conceptUuid) {
        // Access entity: Toplevel record
        LizNeuralConcept conceptToAccess = accessConceptRecordByUuid(conceptUuid);

        //
        if (!isExecutionCancellationAllowed(conceptToAccess)) {
            throw new RuntimeException("Execution cancellation is not allowed!");
        }

        //
        LizNeuralExecution execution = lizNeuralExecutionRepository.findFirstUnfinished(conceptToAccess.getId());
        if (execution == null) {
            throw new RuntimeException("Execution(unfinished) is not found!");
        }

        //
        execution = accessAnExecutionRecordAndLockIt(execution.getId());

        //
        atomicCancelExecution(execution);
    }

    private LizNeuralTrainingTask.TaskResult learn(LizNeuralConcept concept, LizNeuralExecution execution, LizNeuralTrainingTask task) {

        // Set training configuration
        LizNeuralNetworkTrainingConfiguration trainingConfig = convertConceptToTrainingConfiguration(concept);

        LizNeuralTrainingTask.TaskResult resultCode;

        try {
            //
            lizNeuralTrainingService.trainAPlayer(task, trainingConfig);

            //
            resultCode = LizNeuralTrainingTask.TaskResult.SUCCEEDED;

            System.out.println("XXXX____DONE " + execution.getTaskAllCount() + "-" + execution.getTaskPhaseFinishedCount());
        } catch (Exception e) {
            resultCode = LizNeuralTrainingTask.TaskResult.FAILED;

            System.out.println("AAA____FAILED");
        }
        return resultCode;
    }


    private void delay(LocalDateTime itemCreatedTime, long waitingTime) {
        LocalDateTime now = LocalDateTime.now();

        long secondsSinceCreation = Duration.between(itemCreatedTime, now).getSeconds();

        long remainingTime = waitingTime - secondsSinceCreation;

        if (remainingTime > 0) {
            try {
                Thread.sleep(remainingTime * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }


    @Transactional(propagation = Propagation.NEVER)
    public void eventTaskProcessingQueueItemReceived(LizNeuralConceptTaskProcessingQueueItem queueItem) {

        System.out.println("QUEUE-ITEM:" + queueItem);
        // Defining block level type/class
        class ExecutionResult {
            LizNeuralConcept concept;
            LizNeuralExecution execution;
            LizNeuralTrainingTask task;

            public ExecutionResult(LizNeuralConcept concept, LizNeuralExecution execution, LizNeuralTrainingTask task) {
                this.concept = concept;
                this.execution = execution;
                this.task = task;
            }
        }

        // Step 1
        ExecutionResult executionResult = transactionTemplate.execute(status -> {

            //
            delay(queueItem.getDateCreated(),10);

            //
            LizNeuralExecution execution = accessExecutionRecordById(queueItem.getExecutionId());
            if (execution == null) {
                throw new LizNeuralConceptExecutionNotFoundException(queueItem.getExecutionId());
            }

            if(!LizNeuralExecution.ProcessPhase.RUNNING.equals(execution.getProcessPhase())) {
                System.out.println("Execution is not in running phase, cancel processing");
                System.out.println("BECAUSE:" + execution.getProcessPhase());
                return null;
            }

            //
            LizNeuralTrainingTask taskToProcess = lizNeuralTrainingTaskRepository.findByCompositeKey(
                    queueItem.getConceptId(),
                    queueItem.getExecutionId(),
                    queueItem.getScenarioId(),
                    queueItem.getPlayerId()
            );
            if (taskToProcess == null) {
                throw new LizNeuralTrainingTaskNotFoundException();
            }


            //
            if (!isTaskAllowedToBeProcessed(taskToProcess)) {
                System.out.println("Task not allowed to be processed - in step 1");
                System.out.println("BECAUSE:" + taskToProcess.getTaskPhase());
                System.out.println("TheTask:"+taskToProcess);
                return null;
            }

            // Access entity: Toplevel record
            LizNeuralConcept concept = accessConceptRecordById(taskToProcess.getConceptId());



            return new ExecutionResult(concept, execution, taskToProcess);
        });
        if (executionResult == null) {
            //throw new RuntimeException("ExecutionResult is null");
            System.out.println("TASK PROCESSING - CANCELLED");
            return; // ?????? broker fuggetlen: ez kavzi feldolgozvat jelent. ha exceptiont dobnal akkor nemely broker ujra pobalkozna
        }


        // Step 2 - Long running section, therefore no transaction allowed
        LocalDateTime dateNow = LocalDateTime.now();
        TransactionTemplate neverTransactionTemplate = new TransactionTemplate(transactionManager);
        neverTransactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_NEVER);
        final LizNeuralTrainingTask.TaskResult taskResultCode = neverTransactionTemplate.execute(status -> {
            return learn(executionResult.concept, executionResult.execution, executionResult.task);
        });


        // Step 3
        transactionTemplate.executeWithoutResult(status -> {
            LizNeuralTrainingTask taskToModify = lizNeuralTrainingTaskRepository.findByCompositeKey(
                    queueItem.getConceptId(),
                    queueItem.getExecutionId(),
                    queueItem.getScenarioId(),
                    queueItem.getPlayerId()
            );
            if (taskToModify == null) {
                throw new LizNeuralTrainingTaskNotFoundException();
            }

            // Access entity: Toplevel record
            LizNeuralConcept concept = accessConceptRecordById(taskToModify.getConceptId());

            //
            LizNeuralExecution execution = accessExecutionRecordById(taskToModify.getExecutionId());

            //
            taskToModify.setTaskPhase(LizNeuralTrainingTask.TaskPhase.FINISHED);
            taskToModify.setTaskResult(taskResultCode);
            taskToModify.setDateModified(dateNow);
            taskToModify.setDateFinished(dateNow);

            //
            adjustExecutionRecordCountersOnTaskPhaseChangeFromQueuedToFinished(execution, LizNeuralTrainingTask.TaskResult.SUCCEEDED);

            // Execution
            execution.setDateModified(dateNow);

            //
            if (execution.getTaskAllCount() == execution.getTaskPhaseFinishedCount()) {
                execution.setProcessPhase(LizNeuralExecution.ProcessPhase.FINISHED);
                execution.setDateFinished(dateNow);
            }

            entityManager.flush();
        });

    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////////
    /// ATOMIC METHODS ////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////

    private LizNeuralExecution atomicCreateStructure(LizNeuralConcept concept) {

        // if no player and/or no scenario exists than no task will be created.
        if (lizHistoryScenarioRepository.countAll() == 0) {
            throw new RuntimeException("No scenarios found!");
        }

        if (lizHistoryPlayerRepository.countAll() == 0) {
            throw new RuntimeException("No player found!");
        }

        // Create and lock exclusively an execution record
        LizNeuralExecution execution = createANewExecutionRecord(concept);

        // Generate scenario based tests and its tasks (by memory saving solution)
        try (Stream<LizHistoryScenario> stream = lizHistoryScenarioRepository.streamAll()) {
            stream.forEach(scenario -> {
                createATrainingRecordAndBelongingTasks(execution, scenario.getId());
                entityManager.flush();
                entityManager.detach(scenario);
            });
        }

        //
        execution.setProcessPhase(LizNeuralExecution.ProcessPhase.INITIALISED);
        execution.setDateModified(LocalDateTime.now());
        return execution;
    }

    private void atomicStartExecution(LizNeuralExecution execution) {
        //
        collectAllCreatedTasksAndRunThem(execution);

        //
        execution.setProcessPhase(LizNeuralExecution.ProcessPhase.RUNNING);
        execution.setDateModified(LocalDateTime.now());
    }

    private void atomicPauseExecution(LizNeuralExecution execution) {
        //
        collectAllRunningTasksAndPauseThem(execution);
        System.out.println("GGGG-atomicPauseExecution");
        //
        execution.setProcessPhase(LizNeuralExecution.ProcessPhase.PAUSED);
        execution.setDateModified(LocalDateTime.now());
    }

    private void atomicContinueExecution(LizNeuralExecution execution) {
        //
        collectAllPausedTasksAndRunThem(execution);

        //
        execution.setProcessPhase(LizNeuralExecution.ProcessPhase.RUNNING);
        execution.setDateModified(LocalDateTime.now());
    }

    private void atomicCancelExecution(LizNeuralExecution execution) {

        //
        collectAllUnfinishedTasksAndCancelThem(execution);

        //
        LocalDateTime dateNow = LocalDateTime.now();
        execution.setProcessPhase(LizNeuralExecution.ProcessPhase.FINISHED);
        execution.setDateModified(dateNow);
        execution.setDateFinished(dateNow);
    }
//
//    private void atomicTaskProcessing(LizNeuralTrainingTask task) {
//        // Access entity: Toplevel record
//        LizNeuralConcept concept = accessConceptRecordById(task.getConceptId());
//
//        //
//        LizNeuralExecution execution = accessExecutionRecordById(task.getExecutionId());
//        LocalDateTime dateNow = LocalDateTime.now();
//
//        // Set training configuration
//        LizNeuralNetworkTrainingConfiguration trainingConfig = convertConceptToTrainingConfiguration(concept);
//
//        LizNeuralTrainingTask.TaskResult resultCode;
//        try {
//            //
//            lizNeuralTrainingService.trainAPlayer(task, trainingConfig);
//
//            //
//            resultCode = LizNeuralTrainingTask.TaskResult.SUCCEEDED;
//
//            System.out.println("XXXX____DONE " + execution.getTaskAllCount() + "-" + execution.getTaskPhaseFinishedCount());
//        } catch (Exception e) {
//            resultCode = LizNeuralTrainingTask.TaskResult.FAILED;
//
//            System.out.println("AAA____FAILED");
//        }
//
//        // Task
//        task.setTaskPhase(LizNeuralTrainingTask.TaskPhase.FINISHED);
//        task.setTaskResult(resultCode);
//        task.setDateModified(dateNow);
//        task.setDateFinished(dateNow);
//
//        //
//        adjustExecutionRecordCountersOnTaskPhaseChangeFromQueuedToFinished(execution, LizNeuralTrainingTask.TaskResult.SUCCEEDED);
//
//        // Execution
//        execution.setDateModified(dateNow);
//
//        //
//        if (execution.getTaskAllCount() == execution.getTaskPhaseFinishedCount()) {
//            execution.setProcessPhase(LizNeuralExecution.ProcessPhase.FINISHED);
//            execution.setDateFinished(dateNow);
//        }
//
//
//        entityManager.flush();
//
//    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONCEPT-RECORD METHODS ////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////

    private LizNeuralConcept accessConceptRecordByUuid(String conceptUuid) {
        LizNeuralConcept conceptToAccess = lizNeuralConceptRepository.findByUuid(conceptUuid);
        if (conceptToAccess == null) {
            throw new LizNeuralConceptNotFoundByUuidException(conceptUuid);
        }
        return conceptToAccess;
    }

    private LizNeuralConcept accessConceptRecordById(int conceptId) {
        LizNeuralConcept conceptToAccess = lizNeuralConceptRepository.findById(conceptId);
        if (conceptToAccess == null) {
            throw new LizNeuralConceptNotFoundException(conceptId);
        }
        return conceptToAccess;
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////////
    /// EXECUTION-RECORD METHODS //////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////

    private LizNeuralExecution createANewExecutionRecord(LizNeuralConcept concept) {

        Integer executionId = null;

        // Attempt to create it
        try {

            //
            LocalDateTime creationDate = LocalDateTime.now();

            // Set execution
            LizNeuralExecution executionSaved = uuidGenerator.saveWithRetry(lizNeuralExecutionRepository, LizNeuralExecution.builder()
                    .name("Liz Build[" + concept.getId() + "] " + creationDate.toString())
                    .concept(concept)
                    .processPhase(LizNeuralExecution.ProcessPhase.INITIALISED)
                    .dateCreated(creationDate)
                    .build()
            );

//            LizNeuralExecution executionToSave = new LizNeuralExecution();
//            executionToSave.setName("Liz Build[" + concept.getId() + "] " + creationDate.toString());
//            executionToSave.setConcept(concept);
//            executionToSave.setProcessPhase(LizNeuralExecution.ProcessPhase.INITIALISED);
//            executionToSave.setDateCreated(creationDate);
//            LizNeuralExecution executionSaved = uuidGenerator.saveWithRetry(lizNeuralExecutionRepository, executionToSave);
            entityManager.flush(); // Important in order to trigger database level unique conflict which occurs even before transaction commit.

            if (executionSaved == null) {
                throw new RuntimeException("Unexpected case is found!");
            }
            executionId = executionSaved.getId();

        } catch (DataIntegrityViolationException ex) {

            Throwable cause = ex.getCause();
            if (cause instanceof org.hibernate.exception.ConstraintViolationException cve) {
                String constraint = cve.getConstraintName();

                if ("CHK_is_unfinished_01".equalsIgnoreCase(constraint)) {
                    throw new LizNeuralConceptExecutionConflictException(concept.getId());
                }
            }

            // Other execeptions
            throw ex;
        }

        // Attempt to access it and lock it
        try {
            LizNeuralExecution executionToLock = lizNeuralExecutionRepository.accessByIdWithExclusiveLock(executionId);
            if (executionToLock == null) {
                throw new LizNeuralConceptNotFoundException(concept.getId());
            }
            if (!LizNeuralExecution.ProcessPhase.INITIALISED.equals(executionToLock.getProcessPhase())) {
                throw new RuntimeException("Unexpected case is found! Wrong process phase:" + executionToLock.getProcessPhase());
            }

            return executionToLock;
        } catch (PessimisticLockException |
                 LockTimeoutException e) {   // ha a konkurens folyamat meg nem zarult le, es a db dob vissza
            throw new LizNeuralConceptExecutionConflictException(concept.getId());
        }
    }

    private LizNeuralExecution accessExecutionRecordById(int executionId) {
        LizNeuralExecution executionToAccess = lizNeuralExecutionRepository.findById(executionId);
        if (executionToAccess == null) {
            throw new LizNeuralConceptNotFoundException(executionId);
        }
        return executionToAccess;
    }

    private LizNeuralExecution accessAnExecutionRecordAndLockIt(int executionId) {
        // Attempt to access it and lock it
        try {
            LizNeuralExecution executionToLock = lizNeuralExecutionRepository.accessByIdWithExclusiveLock(executionId);
            if (executionToLock == null) {
                throw new RuntimeException("execution is not found!");
            }
            return executionToLock;
        } catch (PessimisticLockException |
                 LockTimeoutException e) {   // ha a konkurens folyamat meg nem zarult le, es a db dob vissza
            throw new RuntimeException("Unable to lock execution!");
        }
    }

    private boolean isExecutionCreationAllowed(LizNeuralConcept concept) {
        if (lizNeuralExecutionRepository.isAnyUnfinished(concept.getId())) {
            return false;
        }
        return true;
    }

    private boolean isExecutionContinuingAllowed(LizNeuralConcept concept) {
        if (lizNeuralExecutionRepository.isAnyPaused(concept.getId())) {
            return true;
        }
        return false;
    }

    private boolean isExecutionPausingAllowed(LizNeuralConcept concept) {
        System.out.println("isExecutionPausingAllowed(conceptId:" + concept.getId() + ")");

        if (lizNeuralExecutionRepository.isAnyRunning(concept.getId())) {
            System.out.println("-Yes");
            return true;
        }
        System.out.println("-No");
        return false;
    }

    private boolean isExecutionCancellationAllowed(LizNeuralConcept concept) {
        if (lizNeuralExecutionRepository.isAnyUnfinished(concept.getId())) {
            return true;
        }
        return false;
    }


    private void adjustExecutionRecordCountersOnTaskCreate(LizNeuralExecution execution) {
        System.out.println("ADJUST executionRecord:" + execution.getId());

        // Increment/decrement phase counters
        entityManager.flush();  // Because avoid automatic flush of hibernate du to dirty entities which may have been existing
        lizNeuralExecutionRepository.adjustTaskPhaseCounters(execution.getId(), 1, 1, 0, 0, 0);
        lizNeuralExecutionRepository.adjustTaskResultCounters(execution.getId(), +1, 0, 0, 0);
        entityManager.refresh(execution);   // Because of previous bulk-updates
    }

    private void adjustExecutionRecordCountersOnTaskPhaseChangeFromCreatedToQueued(LizNeuralExecution execution) {
        System.out.println("ADJUST executionRecord:" + execution.getId());

        // Increment/decrement phase counters
        entityManager.flush();  // Because avoid automatic flush of hibernate du to dirty entities which may have been existing
        lizNeuralExecutionRepository.adjustTaskPhaseCounters(execution.getId(), 0, -1, +1, 0, 0);
        entityManager.refresh(execution);   // Because of previous bul updates
    }

    private void adjustExecutionRecordCountersOnTaskPhaseChangeFromQueuedToPaused(LizNeuralExecution execution) {
        System.out.println("ADJUST executionRecord:" + execution.getId());

        // Increment/decrement phase counters
        entityManager.flush();  // Because avoid automatic flush of hibernate du to dirty entities which may have been existing
        lizNeuralExecutionRepository.adjustTaskPhaseCounters(execution.getId(), 0, 0, -1, +1, 0);
        entityManager.refresh(execution);   // Because of previous bul updates
    }

    private void adjustExecutionRecordCountersOnTaskPhaseChangeFromPausedToQueued(LizNeuralExecution execution) {
        System.out.println("ADJUST executionRecord:" + execution.getId());

        // Increment/decrement phase counters
        entityManager.flush();  // Because avoid automatic flush of hibernate du to dirty entities which may have been existing
        lizNeuralExecutionRepository.adjustTaskPhaseCounters(execution.getId(), 0, 0, +1, -1, 0);
        entityManager.refresh(execution);   // Because of previous bul updates
    }

    private void adjustExecutionRecordCountersOnTaskPhaseChangeFromQueuedToFinished(LizNeuralExecution execution, LizNeuralTrainingTask.TaskResult taskResult) {
        System.out.println("ADJUST executionRecord:" + execution.getId());

        // Increment/decrement phase counters
        entityManager.flush();  // Because avoid automatic flush of hibernate du to dirty entities which may have been existing
        lizNeuralExecutionRepository.adjustTaskPhaseCounters(execution.getId(), 0, 0, -1, 0, 1);

        // Increment result counters
        if (LizNeuralTrainingTask.TaskResult.SUCCEEDED == taskResult) {
            lizNeuralExecutionRepository.adjustTaskResultCounters(execution.getId(), -1, 0, 0, +1); // Pending to succeeded
        } else if (LizNeuralTrainingTask.TaskResult.CANCELED == taskResult) {
            lizNeuralExecutionRepository.adjustTaskResultCounters(execution.getId(), -1, +1, 0, 0); // pending to cancelled
        } else if (LizNeuralTrainingTask.TaskResult.FAILED == taskResult) {
            lizNeuralExecutionRepository.adjustTaskResultCounters(execution.getId(), -1, 0, +1, 0); // pending to failed
        } else {
            throw new RuntimeException("Unsupported task result:" + taskResult);
        }
        entityManager.refresh(execution);   // Because of previous bul updates
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////////
    /// TASK METHODS //////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////


    public void collectAllUnfinishedTasksAndCancelThem(LizNeuralExecution execution) {

        LocalDateTime dateNow = LocalDateTime.now();
        try (Stream<LizNeuralTrainingTask> stream = lizNeuralTrainingTaskRepository.streamAllTaskInUnfinishedPhasesAtExecutionId(execution.getConcept().getId(), execution.getId())) {
            stream.forEach(task -> {
                System.out.println("CCCC:" + task);
                if (LizNeuralTrainingTask.TaskPhase.FINISHED != task.getTaskPhase()) {
                    System.out.println("----UNFINISHED");
                    task.setTaskPhase(LizNeuralTrainingTask.TaskPhase.FINISHED);
                    task.setTaskResult(LizNeuralTrainingTask.TaskResult.CANCELED);
                    task.setDateModified(dateNow);
                    task.setDateFinished(dateNow);
                    entityManager.flush();

                    //
                    adjustExecutionRecordCountersOnTaskPhaseChangeFromQueuedToFinished(execution, LizNeuralTrainingTask.TaskResult.CANCELED);
                }
                entityManager.detach(task);
            });
        }
    }

    public void collectAllRunningTasksAndPauseThem(LizNeuralExecution execution) {
        LocalDateTime dateNow = LocalDateTime.now();
        try (Stream<LizNeuralTrainingTask> stream = lizNeuralTrainingTaskRepository.streamAllTaskInQueuedPhaseAtExecutionId(execution.getConcept().getId(), execution.getId())) {
            stream.forEach(task -> {
                if (LizNeuralTrainingTask.TaskPhase.QUEUED == task.getTaskPhase()) {
                    System.out.println("collect to: PAUSE");
                    task.setTaskPhase(LizNeuralTrainingTask.TaskPhase.PAUSED);
                    task.setTaskResult(LizNeuralTrainingTask.TaskResult.PENDING);
                    task.setDateModified(dateNow);
                    entityManager.flush();
                }
                entityManager.detach(task);

                //
                adjustExecutionRecordCountersOnTaskPhaseChangeFromQueuedToPaused(execution);
            });
        }
    }

    public void collectAllPausedTasksAndRunThem(LizNeuralExecution execution) {
        LocalDateTime dateNow = LocalDateTime.now();
        try (Stream<LizNeuralTrainingTask> stream = lizNeuralTrainingTaskRepository.streamAllTaskInPausedPhaseAtExecutionId(execution.getConcept().getId(), execution.getId())) {
            stream.forEach(task -> {
                if (LizNeuralTrainingTask.TaskPhase.PAUSED == task.getTaskPhase()) {
                    System.out.println("collect to: RUN");

                    //
                    if (!isTaskAllowedToBeQueued(task)) {
                        throw new RuntimeException("Task is not allowed to be queued!");
                    }

                    //
                    task.setTaskPhase(LizNeuralTrainingTask.TaskPhase.QUEUED);
                    task.setTaskResult(LizNeuralTrainingTask.TaskResult.PENDING);
                    task.setDateModified(dateNow);
                    entityManager.flush();

                    //
                    registerATaskInQueue(task);
                }
                entityManager.detach(task);

                //
                adjustExecutionRecordCountersOnTaskPhaseChangeFromPausedToQueued(execution);
            });
        }
    }

    public void collectAllCreatedTasksAndRunThem(LizNeuralExecution execution) {

        LocalDateTime dateNow = LocalDateTime.now();
        try (Stream<LizNeuralTrainingTask> stream = lizNeuralTrainingTaskRepository.streamAllTaskInCreatedPhaseAtExecutionId(execution.getConcept().getId(), execution.getId())) {
            stream.forEach(task -> {
                if (LizNeuralTrainingTask.TaskPhase.CREATED == task.getTaskPhase()) {
                    //
                    if (!isTaskAllowedToBeQueued(task)) {
                        throw new RuntimeException("Task is not allowed to be queued!");
                    }

                    //
                    registerATaskInQueue(task);

                    //
                    task.setTaskPhase(LizNeuralTrainingTask.TaskPhase.QUEUED);
                    task.setTaskResult(LizNeuralTrainingTask.TaskResult.PENDING);
                    task.setDateModified(dateNow);
                    entityManager.flush();
                }
                entityManager.detach(task);

                adjustExecutionRecordCountersOnTaskPhaseChangeFromCreatedToQueued(execution);
            });
        }
    }


    private void createATrainingRecordAndBelongingTasks(LizNeuralExecution execution, int scenarioId) {
        //
        LocalDateTime dateNow = execution.getDateCreated();
        int conceptId = execution.getConcept().getId();
        int executionId = execution.getId();

        // Create a new training record in db
        LizNeuralTraining trainingSaved = lizNeuralTrainingRepository.save(LizNeuralTraining.builder()
                .conceptId(conceptId)
                .executionId(execution.getId())
                .scenarioId(scenarioId)
                .dateCreated(dateNow)
                .build()
        );

        //
//        List<LizHistoryPlayer> playersList = lizHistoryPlayerRepository.findAll();
//        for (LizHistoryPlayer player : playersList) {
//            lizNeuralTrainingTaskRepository.save(LizNeuralTrainingTask.builder()
//                    .conceptId(conceptId)
//                    .executionId(executionId)
//                    .scenarioId(trainingSaved.getScenarioId())
//                    .playerId(player.getId())
//                    .taskPhase(LizNeuralTrainingTask.TaskPhase.CREATED)
//                    .taskResult(LizNeuralTrainingTask.TaskResult.PENDING)
//                    .dateCreated(dateNow)
//                    .build()
//            );
//
//            adjustExecutionRecordCountersOnTaskCreate(execution);
//        }


        try (Stream<LizHistoryPlayer> stream = lizHistoryPlayerRepository.streamAll()) {
            stream.forEach(player -> {
                lizNeuralTrainingTaskRepository.save(LizNeuralTrainingTask.builder()
                        .conceptId(conceptId)
                        .executionId(executionId)
                        .scenarioId(trainingSaved.getScenarioId())
                        .playerId(player.getId())
                        .taskPhase(LizNeuralTrainingTask.TaskPhase.CREATED)
                        .taskResult(LizNeuralTrainingTask.TaskResult.PENDING)
                        .dateCreated(dateNow)
                        .build()
                );

                adjustExecutionRecordCountersOnTaskCreate(execution);

                entityManager.flush();
                entityManager.detach(player);
            });
        }
    }


    private boolean isTaskAllowedToBeProcessed(LizNeuralTrainingTask task) {
        if (LizNeuralTrainingTask.TaskPhase.QUEUED == task.getTaskPhase()) {
            return true;
        }
        return false;
    }

    private boolean isTaskAllowedToBeQueued(LizNeuralTrainingTask task) {
        if (LizNeuralTrainingTask.TaskPhase.CREATED == task.getTaskPhase()) {
            return true;
        } else if (LizNeuralTrainingTask.TaskPhase.PAUSED == task.getTaskPhase()) {
            return true;
        }
        return false;
    }

    private void registerATaskInQueue(LizNeuralTrainingTask task) {

        //
        streamBridge.send("queueExecutionTaskProcessingItemRead-out-0", LizNeuralConceptTaskProcessingQueueItem.builder()
                        .conceptId(task.getConceptId())
                        .executionId(task.getExecutionId())
                        .scenarioId(task.getScenarioId())
                        .playerId(task.getPlayerId())
                        .dateCreated(LocalDateTime.now())
                        .build(),
                MimeType.valueOf("application/json")
        );


    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////////
    /// QUEUE-CONSUMER METHODS ////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////
//    private void queueTaskProcessingItemRead(LizNeuralConceptTaskProcessingQueueItem queueItem) {
//        System.out.println("TASK_READ: " + queueItem.toString());
//        int conceptId = queueItem.getConceptId();
//        int executionId = queueItem.getExecutionId();
//        int scenarioId = queueItem.getScenarioId();
//        int playerId = queueItem.getPlayerId();
//
//        //
//        LizNeuralTrainingTask task = lizNeuralTrainingTaskRepository.findByCompositeKey(conceptId, executionId, scenarioId, playerId);
//        if (task == null) {
//            throw new LizNeuralTrainingTaskNotFoundException();
//        }
//
//        //
//        if (!isTaskAllowedToBeProcessed(task)) { // broker fuggetlen: ez kavzi feldolgozvat jelent. ha exceptiont dobnal akkor nemely broker ujra pobalkozna
//            return;
//        }
//
//
//        //
//        atomicTaskProcessing(task);
//    }
//

    /// ///////////////////////////////////////////////////////////////////////////////////////////////////
    /// HELPER METHODS ////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////

    private static LizNeuralNetworkTrainingConfiguration convertConceptToTrainingConfiguration(LizNeuralConcept concept) {
        return LizNeuralNetworkTrainingConfiguration.builder()
                .maxIterationsPerTurn(concept.getConfMaxIterationsPerTurn())
                .maxTurn(concept.getConfMaxTurn())
                .learningRate(concept.getConfLearningRate())
                .build();
    }

    public void eventHistoryQueueItemReceived(AiHistoryQueueItem queueItem) {
        log.info("QUEUE[-History-].Reader: {}\n", queueItem);

        // Initialisation
        PlayState playState = queueItem.getPlayState();
        GameState gameState = playState.getGameState();
        String sessionUuid = queueItem.getSessionUuid();
        String scenarioUuid = queueItem.getScenarioUuid();
        String scenarioName = queueItem.getScenarioName();
        int turn = playState.getTurn();

        // Register local representations if not exists
        registerHistorySessionIfNotExists(sessionUuid);
        registerHistoryScenarioIfNotExists(scenarioUuid, scenarioName);

        //
        Set<GameAction> intentions = new HashSet<>();
        for (Map.Entry<Integer, PlayerData> playerEntry : playState.getPlayers().entrySet()) {

            // Initialisation
            Integer playerIndex = playerEntry.getKey();
            PlayerData playerData = playerEntry.getValue();
            String userUuid = playerData.getUserUuid();
            String userName = playerData.getName();

            // Not to store data of dead player
            if (!gameState.getOpponent(playerIndex).isAlive()) {
                continue;
            }

            if (!playerData.isIntentionAlreadyGiven()) {
                throw new GeneralGameStateException("No intention is present for player! Player index:" + playerData.getIndex());
            }

            //
            GameAction playerIntention = playerData.getIntention();
            int reserveSize = gameState.getOpponents()[playerIndex].getReserveSize();
            int enemiesCount = gameState.getOpponents().length - 1;
            String playerNameCode = playerData.getName();

            //
            saveAHistory(sessionUuid, scenarioUuid, turn, userUuid, userName, PlayerDecision.create(
                            playerIndex,
                            playerIntention.getTargetCellKey(),
                            playerIntention.getAttackingTroopSize(),
                            reserveSize,
                            enemiesCount,
                            gameState
                    )
            );
        }


    }


    private void saveAHistory(String sessionUuid, String scenarioUuid, int turn, String userUuid, String userName, PlayerDecision playerDecision) {

        // Register local representation if not exists
        registerHistoryPlayerIfNotExists(userUuid, userName);

        // Access local representations
        LizHistorySession lizHistorySession = accessHistorySession(sessionUuid);
        int lizHistorySessionId = lizHistorySession.getId();
        LizHistoryScenario lizHistoryScenario = accessHistoryScenario(scenarioUuid);
        int lizHistoryScenarioId = lizHistoryScenario.getId();
        LizHistoryPlayer lizHistoryPlayer = accessHistoryPlayer(userUuid);
        int lizHistoryPlayerId = lizHistoryPlayer.getId();

        // Save history
        lizHistoryRepository.save(LizHistory.builder()
                .sessionId(lizHistorySessionId)
                .scenarioId(lizHistoryScenarioId)
                .playerId(lizHistoryPlayer.getId())
                .turn(turn)
                .decision(convertPlayerDecisionObjToJson(playerDecision))
                .dateCreated(LocalDateTime.now())
                .build()
        );
    }

//
//    // TODO: ismetlodik
//    // TODO: Tedd megosztott helyre
//    private LizHistoryPlayer findHistoryPlayerAndRegisterIfNotExists(String userUuid) {
//        // Determine current record and its id
//        LizHistoryPlayer lizHistoryPlayer = lizHistoryPlayerRepository.findByUserUuid(userUuid);
//        if (lizHistoryPlayer == null) { // Register it if it was not
//            lizHistoryPlayer = lizHistoryPlayerRepository.save(new LizHistoryPlayer(userUuid));
//        }
//        return lizHistoryPlayer;
//    }
//
//    // TODO: Tedd megosztott helyre
//    private LizHistorySession findHistorySessionAndRegisterIfNotExists(String sessionUuid) {
//        // Determine current record and its id
//        LizHistorySession lizHistorySession = lizHistorySessionRepository.findBySessionUuid(sessionUuid);
//        if (lizHistorySession == null) { // Register it if it was not
//            lizHistorySession = lizHistorySessionRepository.save(new LizHistorySession(sessionUuid));
//        }
//        return lizHistorySession;
//    }
//    // TODO: Tedd megosztott helyre
//    private LizHistoryScenario findHistoryScenarioAndRegisterIfNotExists(String scenarioUuid) {
//        // Determine current record and its id
//        LizHistoryScenario lizHistoryScenario = lizHistoryScenarioRepository.findByScenarioUuid(scenarioUuid);
//        if (lizHistoryScenario == null) { // Register it if it was not
//            lizHistoryScenario = lizHistoryScenarioRepository.save(new LizHistoryScenario(scenarioUuid));
//        }
//        return lizHistoryScenario;
//    }


    // TODO: ?? ismetlodo??
    // TODO: Tedd megosztott helyre
    private String convertPlayerDecisionObjToJson(PlayerDecision playerDecision) {
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            return objectMapper.writeValueAsString(playerDecision);
        } catch (JsonProcessingException e) {
            throw new GeneralGameStateException("Error at parsing");
        }
    }
}
