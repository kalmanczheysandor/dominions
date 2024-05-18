package hu.kalmanczheysandor.applications.dominion.proxy.ai;

import hu.kalmanczheysandor.application.dominion.ai.common.AiResponse;
import hu.kalmanczheysandor.application.dominion.service.CalculationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name="currency-exchange", url="localhost:8000")
@FeignClient(name="ai1")
public interface Ai1ServiceProxy {

	@GetMapping("/ai1/calculate")
	public CalculationResponse calculate();

	@GetMapping("/ai1/attack")
	public AiResponse attack();
}
