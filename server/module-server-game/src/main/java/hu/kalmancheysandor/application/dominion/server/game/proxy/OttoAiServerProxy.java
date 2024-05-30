package hu.kalmancheysandor.application.dominion.server.game.proxy;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="ai-otto")
public interface OttoAiServerProxy {
	@PostMapping("/ai-otto/call")
	public AiResponse generateResponse(@RequestBody AiRequest request);
}
