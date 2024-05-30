package hu.kalmancheysandor.application.dominion.server.ai.liz.service;

import hu.kalmancheysandor.application.dominion.api.ai.common.neural.NeuralTrainingData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiTrainingRequest {
    private List<NeuralTrainingData> trainingDataList;
}
