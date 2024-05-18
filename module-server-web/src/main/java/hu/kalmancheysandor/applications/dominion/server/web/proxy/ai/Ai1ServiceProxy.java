package hu.kalmancheysandor.applications.dominion.server.web.proxy.ai;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import hu.kalmancheysandor.application.dominion.service.CalculationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

//@FeignClient(name="currency-exchange", url="localhost:8000")
@FeignClient(name="ai1")
public interface Ai1ServiceProxy {

	@GetMapping("/ai1/calculate")
	public CalculationResponse calculate();

	@GetMapping("/ai1/attack")
	public AiResponse attack();
}
