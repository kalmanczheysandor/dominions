package hu.kalmanczheysandor.applications.dominion.proxy.game;

import hu.kalmanczheysandor.application.dominion.game.server.controller.GameJoinResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="game")
public interface GameServiceProxy {
	@GetMapping("/game/join/{key}")
	public GameJoinResponse joinToAGame(@PathVariable String key);
}
