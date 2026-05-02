package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.service.neural;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.service.TLizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
public class LizNeuralQueueService extends TLizService {

//
//    public void collectAllUnfinishedTasksAndCancelThem(LizNeuralExecution execution) {
//
//        LocalDateTime dateNow = LocalDateTime.now();
//        try (Stream<LizNeuralTrainingTask> stream = lizNeuralTrainingTaskRepository.streamAllTaskInUnfinishedPhasesAtExecutionId(execution.getConcept().getId(), execution.getId())) {
//            stream.forEach(task -> {
//                System.out.println("CCCC:" + task);
//                if (LizNeuralTrainingTask.TaskPhase.FINISHED != task.getTaskPhase()) {
//                    System.out.println("----UNFINISHED");
//                    task.setTaskPhase(LizNeuralTrainingTask.TaskPhase.FINISHED);
//                    task.setTaskResult(LizNeuralTrainingTask.TaskResult.CANCELED);
//                    task.setDateModified(dateNow);
//                    task.setDateFinished(dateNow);
//                    entityManager.flush();
//
//                    //
//                    adjustExecutionRecordCountersOnTaskPhaseChangeFromQueuedToFinished(execution, LizNeuralTrainingTask.TaskResult.CANCELED);
//                }
//                entityManager.detach(task);
//            });
//        }
//    }
//
//    public void collectAllRunningTasksAndPauseThem(LizNeuralExecution execution) {
//        LocalDateTime dateNow = LocalDateTime.now();
//        try (Stream<LizNeuralTrainingTask> stream = lizNeuralTrainingTaskRepository.streamAllTaskInQueuedPhaseAtExecutionId(execution.getConcept().getId(), execution.getId())) {
//            stream.forEach(task -> {
//                System.out.println("CCCC:" + task);
//                if (LizNeuralTrainingTask.TaskPhase.QUEUED == task.getTaskPhase()) {
//                    System.out.println("----PAUSED");
//                    task.setTaskPhase(LizNeuralTrainingTask.TaskPhase.PAUSED);
//                    task.setTaskResult(LizNeuralTrainingTask.TaskResult.PENDING);
//                    task.setDateModified(dateNow);
//                    entityManager.flush();
//                }
//                entityManager.detach(task);
//
//                //
//                adjustExecutionRecordCountersOnTaskPhaseChangeFromQueuedToPaused(execution);
//            });
//        }
//    }
//
//    public void collectAllPausedTasksAndRunThem(LizNeuralExecution execution) {
//        LocalDateTime dateNow = LocalDateTime.now();
//        try (Stream<LizNeuralTrainingTask> stream = lizNeuralTrainingTaskRepository.streamAllTaskInPausedPhaseAtExecutionId(execution.getConcept().getId(), execution.getId())) {
//            stream.forEach(task -> {
//                if (LizNeuralTrainingTask.TaskPhase.PAUSED == task.getTaskPhase()) {
//                    registerATaskInQueue(task);
//                    task.setDateModified(dateNow);
//                    entityManager.flush();
//                }
//                entityManager.detach(task);
//
//                //
//                adjustExecutionRecordCountersOnTaskPhaseChangeFromPausedToQueued(execution);
//            });
//        }
//    }
//
//    public void collectAllCreatedTasksAndRunThem(LizNeuralExecution execution) {
//
//        LocalDateTime dateNow = LocalDateTime.now();
//        try (Stream<LizNeuralTrainingTask> stream = lizNeuralTrainingTaskRepository.streamAllTaskInCreatedPhaseAtExecutionId(execution.getConcept().getId(), execution.getId())) {
//            stream.forEach(task -> {
//                if (LizNeuralTrainingTask.TaskPhase.CREATED == task.getTaskPhase()) {
//                    registerATaskInQueue(task);
//                    task.setDateModified(dateNow);
//                    entityManager.flush();
//                }
//                entityManager.detach(task);
//
//                adjustExecutionRecordCountersOnTaskPhaseChangeFromCreatedToQueued(execution);
//            });
//        }
//    }
//
//



}
