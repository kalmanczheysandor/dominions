package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.service.neural;


import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.execution.LizNeuralConceptExecutionStatusResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.LizNeuralConceptExecutionChartDataItemResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.LizNeuralConceptSnapshotChartDataItemResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.option.LizNeuralConceptResultHistoryPlayerOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.option.LizNeuralConceptResultHistoryScenarioOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryPlayer;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryScenario;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralConcept;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralExecution;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingResult;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingSnapshot;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryPlayerRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryScenarioRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.LizNeuralConceptRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural.LizNeuralExecutionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural.LizNeuralTrainingResultRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural.LizNeuralTrainingSnapshotRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.service.TLizService;
import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
public class LizNeuralConceptService extends TLizService {

    @Autowired
    private LizNeuralConceptRepository lizNeuralConceptRepository;

    @Autowired
    private LizNeuralOrchestrationService lizNeuralOrchestrationService;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    LizHistoryScenarioRepository lizHistoryScenarioRepository;


    @Autowired
    LizHistoryPlayerRepository lizHistoryPlayerRepository;

    @Autowired
    LizNeuralExecutionRepository lizNeuralExecutionRepository;

    @Autowired
    LizNeuralTrainingResultRepository lizNeuralTrainingResultRepository;

    @Autowired
    LizNeuralTrainingSnapshotRepository lizNeuralTrainingSnapshotRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationConfig applicationConfig;

    public LizNeuralConceptAccessResponse access(@NotBlank String recordUuid) {

        // Access entity via repository
        LizNeuralConcept recordToAccess = lizNeuralConceptRepository.findByUuid(recordUuid);
        if (recordToAccess == null) {
            throw new LizNeuralConceptNotFoundByUuidException(recordUuid);
        }

        // Generate response
        return modelMapper.map(recordToAccess, LizNeuralConceptAccessResponse.class);
    }

    public List<LizNeuralConceptItemResponse> listAll() {

        // Access entity via repository
        List<LizNeuralConcept> recordList = lizNeuralConceptRepository.findAll();

        // Generate response
        return recordList.stream()
                .map(item -> modelMapper.map(item, LizNeuralConceptItemResponse.class))
                .collect(Collectors.toList());
    }

    public LizNeuralConceptCreateResponse save(@NotNull LizNeuralConceptCreateRequest request) {

        // Checking: Whether the new name is reserved
        if (lizNeuralConceptRepository.isNameReserved(request.getName())) {
            throw new LizNeuralConceptNameIsReservedException(request.getName());
        }

        // Checking: Whether the new code is reserved
       /* if (lizNeuralConceptRepository.isCodeReserved(request.getCode())) {
            throw new LizNeuralConceptCodeIsReservedException(request.getCode());
        }*/

        // Save lizPersonnel
        LizNeuralConcept recordToSave = new LizNeuralConcept();
        recordToSave.setName(request.getName());
        recordToSave.setConfMaxIterationsPerTurn(request.getConfMaxIterationsPerTurn());
        recordToSave.setConfLearningRate(request.getConfLearningRate());
        recordToSave.setConfMaxTurn(request.getConfMaxTurn());
        recordToSave.setEnabled(request.isEnabled());
        recordToSave.setDateCreated(LocalDateTime.now());
        LizNeuralConcept recordSaved = uuidGenerator.saveWithRetry(lizNeuralConceptRepository, recordToSave);

        // Generate response
        return modelMapper.map(recordSaved, LizNeuralConceptCreateResponse.class);
    }

    public LizNeuralConceptUpdateResponse update(@NotBlank String recordUuid, @NotNull LizNeuralConceptUpdateRequest request) {

        // Access entity via repository
        LizNeuralConcept recordToModify = lizNeuralConceptRepository.findByUuid(recordUuid);
        if (recordToModify == null) {
            throw new LizNeuralConceptNotFoundByUuidException(recordUuid);
        }
        int recordId = recordToModify.getId();

        // Checking: Whether the new name is reserved
        if (lizNeuralConceptRepository.isNameReserved(request.getName(), recordId)) {
            throw new LizNeuralConceptNameIsReservedException(request.getName());
        }
        // Checking: Whether the new name is reserved
        /*if (lizNeuralConceptRepository.isCodeReserved(request.getCode(), recordId)) {
            throw new LizNeuralConceptCodeIsReservedException(request.getCode());
        }*/

        // Modify lizPersonnel
        recordToModify.setName(request.getName());
        recordToModify.setConfMaxIterationsPerTurn(request.getConfMaxIterationsPerTurn());
        recordToModify.setConfLearningRate(request.getConfLearningRate());
        recordToModify.setConfMaxTurn(request.getConfMaxTurn());
        recordToModify.setEnabled(request.isEnabled());
        recordToModify.setDateModified(LocalDateTime.now());
        LizNeuralConcept recordModified = uuidGenerator.saveWithRetry(lizNeuralConceptRepository, recordToModify);

        // Generate response
        return modelMapper.map(recordModified, LizNeuralConceptUpdateResponse.class);
    }

    public void deleteOneLizPersonnel(@NotBlank String recordUuid) {

        // Execution
        try {
            this.deleteOneRow(recordUuid);
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    public void deleteMultiple(@NotNull LizNeuralConceptDeleteRequest request) {

        // Execution
        try {
            for (String uuid : request.getItems()) {
                this.deleteOneRow(uuid);
            }
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    private void deleteOneRow(@NotBlank String recordUuid) {

        // Access entity via repository
        LizNeuralConcept recordToDelete = lizNeuralConceptRepository.findByUuid(recordUuid);
        if (recordToDelete == null) {
            throw new LizNeuralConceptNotFoundByUuidException(recordUuid);
        }
        int recordId = recordToDelete.getId();

        // Check: whether any foreign key referencing this record
        if (lizNeuralConceptRepository.isReferencedElsewhere(recordId)) {
            throw new LizNeuralConceptReferencedElsewhereException(recordId, recordToDelete.getName());
        }

        // Delete item from db
        try {
            lizNeuralConceptRepository.deleteById(recordId);
        } catch (EntityNotFoundException e) {
            throw new LizNeuralConceptNotFoundException(recordId);
        }
    }


    /// /////////////////////////////////////////////////////////////////////////////////////////////////
    /// //// RESULT METHODS /////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////////////////

    public LizNeuralConceptSnapshotChartDataItemResponse snapshotChartData(@NotBlank String conceptUuid, @NotBlank String historyScenarioUuid, @NotBlank String historyPlayerUuid) {

        // Access entity(s) via repository
        LizNeuralConcept conceptRecord = lizNeuralConceptRepository.findByUuid(conceptUuid);
        if (conceptRecord == null) {
            throw new LizNeuralConceptNotFoundByUuidException(conceptUuid);
        }

        LizHistoryPlayer historyPlayerRecord = lizHistoryPlayerRepository.findByUserUuid(historyPlayerUuid);
        if (historyPlayerRecord == null) {
            throw new LizNeuralConceptHistoryPlayerNotFoundByUuidException(conceptUuid);
        }

        LizHistoryScenario historyScenarioRecord = lizHistoryScenarioRepository.findByScenarioUuid(historyScenarioUuid);
        if (historyScenarioRecord == null) {
            throw new LizNeuralConceptHistoryScenarioNotFoundByUuidException(conceptUuid);
        }

        System.out.println("Before latest execution");

        // Find latest finished execution
        LizNeuralExecution latestExecution = lizNeuralExecutionRepository.findLatestFinished(conceptRecord.getId());
        if (latestExecution == null) {    // When there is no finished execution found
            System.out.println("NULL latest execution");
            return LizNeuralConceptSnapshotChartDataItemResponse
                    .builder()
                    .bestPrecision(null)
                    .turnCount(0)
                    .precisions(List.of())
                    .build();
        }

        System.out.println("latest execution: " + latestExecution.getId());
        LizNeuralTrainingResult trainingResult = lizNeuralTrainingResultRepository.findAScenarioResultOfAPlayer(
                conceptRecord.getId(),
                latestExecution.getId(),
                historyScenarioRecord.getId(),
                historyPlayerRecord.getId()
        );

        List<BigDecimal> precisions = new ArrayList<>();
        try (Stream<LizNeuralTrainingSnapshot> stream = lizNeuralTrainingSnapshotRepository.streamAllWhereConceptIdAndExecutionIdAndScenarioIdAndPlayerId(
                conceptRecord.getId(),
                latestExecution.getId(),
                historyScenarioRecord.getId(),
                historyPlayerRecord.getId()
        )) {
            stream.forEach(snapshot -> {
                precisions.add(snapshot.getPrecisionRate());
                entityManager.flush();
                entityManager.detach(snapshot);
            });
        }

        return LizNeuralConceptSnapshotChartDataItemResponse
                .builder()
                .bestPrecision(trainingResult.getPrecisionRate())
                .turnCount(trainingResult.getTurnCount())
                .turnBest(trainingResult.getTurnBest())
                .precisions(precisions)
                .build();
    }

    public LizNeuralConceptExecutionChartDataItemResponse executionChartData(@NotBlank String conceptUuid, @NotBlank String historyScenarioUuid, @NotBlank String historyPlayerUuid) {

        // Access entity(s) via repository
        LizNeuralConcept conceptRecord = lizNeuralConceptRepository.findByUuid(conceptUuid);
        if (conceptRecord == null) {
            throw new LizNeuralConceptNotFoundByUuidException(conceptUuid);
        }

        LizHistoryPlayer historyPlayerRecord = lizHistoryPlayerRepository.findByUserUuid(historyPlayerUuid);
        if (historyPlayerRecord == null) {
            throw new LizNeuralConceptHistoryPlayerNotFoundByUuidException(conceptUuid);
        }

        LizHistoryScenario historyScenarioRecord = lizHistoryScenarioRepository.findByScenarioUuid(historyScenarioUuid);
        if (historyScenarioRecord == null) {
            throw new LizNeuralConceptHistoryScenarioNotFoundByUuidException(conceptUuid);
        }

        // Collecting precision rate of all finished executions. Do not forget that these values are stored in test result table, and not in execution table, however the relation is 1:1
        List<BigDecimal> precisions = new ArrayList<>();
        try (Stream<LizNeuralTrainingResult> stream = lizNeuralTrainingResultRepository.streamResultOfAllExecution(
                conceptRecord.getId(),
                historyScenarioRecord.getId(),
                historyPlayerRecord.getId()
        )) {

            stream.forEach(trainingResultItem -> {
                precisions.add(trainingResultItem.getPrecisionRate()); // The first key is: 1

                entityManager.flush();
                entityManager.detach(trainingResultItem);
            });
        }


        // Counting values and finding the best value and its key
        // TODO: Find a better solution
        int executionCount = precisions.size();
        int executionBest = -1;
        BigDecimal bestPrecision = null;
        for (int i = 0; i < precisions.size(); i++) {
            BigDecimal current = precisions.get(i);

            if (bestPrecision == null || bestPrecision.compareTo(current) < 0) {
                bestPrecision = current;
                executionBest = i;
            }
        }


        // When there is no execution result exists. It may mean that there is one ongoing/paused one which is not finished yet.
        if (executionCount == 0) {
            return LizNeuralConceptExecutionChartDataItemResponse
                    .builder()
                    .bestPrecision(null)
                    .executionCount(0)
                    .executionKeyBest(null)
                    .precisions(List.of())
                    .build();
        }

        // When there is at leat one finished execution.
        return LizNeuralConceptExecutionChartDataItemResponse
                .builder()
                .bestPrecision(bestPrecision)
                .executionCount(executionCount)
                .executionKeyBest(executionBest)
                .precisions(precisions)
                .build();
    }

    public List<LizNeuralConceptResultHistoryScenarioOptionResponse> atResultListHistoryScenario(@NotBlank String recordUuid) {

        // Access entity via repository
        LizNeuralConcept conceptRecord = lizNeuralConceptRepository.findByUuid(recordUuid);
        if (conceptRecord == null) {
            throw new LizNeuralConceptNotFoundByUuidException(recordUuid);
        }

        // Find latest finished execution
        LizNeuralExecution latestExecution = lizNeuralExecutionRepository.findLatestFinished(conceptRecord.getId());
        if (latestExecution == null) {    // When there is no finished execution found
            return List.of();
        }

        // Generate response
        List<LizHistoryScenario> historyScenarioList = lizNeuralTrainingResultRepository.collectAllHistoryScenario(conceptRecord.getId(), latestExecution.getId());
        return historyScenarioList.stream()
                .map(item -> modelMapper.map(item, LizNeuralConceptResultHistoryScenarioOptionResponse.class))
                .collect(Collectors.toList());
    }

    public List<LizNeuralConceptResultHistoryPlayerOptionResponse> atResultListHistoryPlayer(@NotBlank String recordUuid, @NotBlank String historyScenarioUuid) {
        // Access entity(s) via repository
        LizNeuralConcept conceptRecord = lizNeuralConceptRepository.findByUuid(recordUuid);
        if (conceptRecord == null) {
            throw new LizNeuralConceptNotFoundByUuidException(recordUuid);
        }

        LizHistoryScenario historyScenarioRecord = lizHistoryScenarioRepository.findByScenarioUuid(historyScenarioUuid);
        if (historyScenarioRecord == null) {
            throw new LizNeuralConceptHistoryScenarioNotFoundByUuidException(historyScenarioUuid);
        }

        // Find latest finished execution
        LizNeuralExecution latestExecution = lizNeuralExecutionRepository.findLatestFinished(conceptRecord.getId());
        if (latestExecution == null) {    // When there is no finished execution found
            return List.of();
        }

        // Generate response
        List<LizHistoryPlayer> historyPlayerList = lizNeuralTrainingResultRepository.collectAllHistoryPlayer(conceptRecord.getId(), latestExecution.getId(), historyScenarioRecord.getId());
        List<LizNeuralConceptResultHistoryPlayerOptionResponse> outputList = new ArrayList<>();
        long snapshotCount = 0;
        for (LizHistoryPlayer historyPlayer : historyPlayerList) {

            snapshotCount = lizNeuralTrainingSnapshotRepository.countAllWhereConceptIdAndExecutionIdAndScenarioIdAndPlayerId(
                    conceptRecord.getId(),
                    latestExecution.getId(),
                    historyScenarioRecord.getId(),
                    historyPlayer.getId()
            );
            outputList.add(new LizNeuralConceptResultHistoryPlayerOptionResponse(
                            historyPlayer.getUserUuid(),
                            historyPlayer.getCaption(),
                            snapshotCount
                    )
            );
        }

        return outputList;


//        return historyPlayerList.stream()
//                .map(item -> modelMapper.map(item, LizNeuralConceptResultHistoryPlayerOptionResponse.class))
//                .collect(Collectors.toList());
    }

    /// /////////////////////////////////////////////////////////////////////////////////////////////////
    /// //// EXECUTION METHODS //////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////////////////

    public LizNeuralConceptExecutionStatusResponse executionStatus(String conceptUuid) {
        System.out.println("executionStatus: conceptUuid:"+conceptUuid);
        // Access entity(s) via repository
        LizNeuralConcept conceptRecord = lizNeuralConceptRepository.findByUuid(conceptUuid);
        if (conceptRecord == null) {
            throw new LizNeuralConceptNotFoundByUuidException(conceptUuid);
        }

        // Find latest finished execution
        LizNeuralExecution latestExecution = lizNeuralExecutionRepository.findLatestExisting(conceptRecord.getId());
        if (latestExecution == null) {    // When there is no finished execution found
            System.out.println("NULL latest execution");
            return LizNeuralConceptExecutionStatusResponse
                    .builder()
                    .taskAllCount(0)
                    .taskPhaseCreatedCount(0)
                    .taskPhaseQueuedCount(0)
                    .taskPhasePausedCount(0)
                    .taskPhaseFinishedCount(0)
                    .taskResultPendingCount(0)
                    .taskResultFailedCount(0)
                    .taskResultSucceededCount(0)
                    .taskResultCanceledCount(0)
                    .processPhase(null)
                    .build();
        }

        return LizNeuralConceptExecutionStatusResponse
                .builder()
                .taskAllCount(latestExecution.getTaskAllCount())
                .taskPhaseCreatedCount(latestExecution.getTaskPhaseCreatedCount())
                .taskPhaseQueuedCount(latestExecution.getTaskPhaseQueuedCount())
                .taskPhasePausedCount(latestExecution.getTaskPhasePausedCount())
                .taskPhaseFinishedCount(latestExecution.getTaskPhaseFinishedCount())
                .taskResultPendingCount(latestExecution.getTaskResultPendingCount())
                .taskResultFailedCount(latestExecution.getTaskResultFailedCount())
                .taskResultSucceededCount(latestExecution.getTaskResultSucceededCount())
                .taskResultCanceledCount(latestExecution.getTaskResultCanceledCount())
                .processPhase(latestExecution.getProcessPhase())
                .build();
    }


    public void eventExecutionStart(String conceptUuid) {
        lizNeuralOrchestrationService.eventExecutionStart(conceptUuid);
    }

    public void eventExecutionPause(String conceptUuid) {
        lizNeuralOrchestrationService.eventExecutionPause(conceptUuid);
    }

    public void eventExecutionContinue(String conceptUuid) {
        lizNeuralOrchestrationService.eventExecutionContinue(conceptUuid);
    }

    public void eventExecutionCancel(String conceptUuid) {
        lizNeuralOrchestrationService.eventExecutionCancel(conceptUuid);
    }
}
