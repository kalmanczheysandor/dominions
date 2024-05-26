package hu.kalmancheysandor.application.dominion.server.game.proxy;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="ai-2")
public interface AiPlayer2ServerProxy {
	@PostMapping("/ai-2/call")
	public AiResponse generateResponse(@RequestBody AiRequest request);
}
