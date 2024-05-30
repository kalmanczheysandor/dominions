package hu.kalmancheysandor.application.dominion.server.game.proxy;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="ai-liz")
public interface LizAiServerProxy {
	@PostMapping("/ai-liz/call")
	public AiResponse generateResponse(@RequestBody AiRequest request);
}
