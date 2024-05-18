package hu.kalmancheysandor.applications.dominion.server.web.proxy.game;

import hu.kalmancheysandor.application.dominion.server.game.controller.GameJoinResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="game")
public interface GameServiceProxy {
	@GetMapping("/game/join/{key}")
	public GameJoinResponse joinToAGame(@PathVariable String key);
}
