package hu.kalmancheysandor.applications.dominions.apis.util.configuration;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LizServer {
        private NeuralNetwork neuralNetwork;
        @Data
        @NoArgsConstructor
        public static class NeuralNetwork {
            @NotBlank
            private String baseFolder;
        }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }
}
