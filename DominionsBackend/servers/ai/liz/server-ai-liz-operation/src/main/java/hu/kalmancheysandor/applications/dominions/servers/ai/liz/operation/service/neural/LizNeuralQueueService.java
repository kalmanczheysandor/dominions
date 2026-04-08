package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.service.neural;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizInputData;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizNeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizNeuralNetworkTrainingConfiguration;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralInputData;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.NeuralNetworkTrainingResult;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistory;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralExecution;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingResult;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingSnapshot;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingTask;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural.LizNeuralTrainingResultRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural.LizNeuralTrainingSnapshotRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.service.TLizService;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.etc.PlayerDecision;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


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
