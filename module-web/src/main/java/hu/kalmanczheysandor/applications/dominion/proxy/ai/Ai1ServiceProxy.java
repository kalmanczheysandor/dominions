package hu.kalmanczheysandor.applications.dominion.proxy.ai;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


//@FeignClient(name="currency-exchange", url="localhost:8000")
@FeignClient(name="ai1")
public interface Ai1ServiceProxy {

	@GetMapping("/calculate")
	public CalculationResponse calculate();
}
