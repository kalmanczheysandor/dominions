package hu.kalmancheysandor.applications.dominion.server.web.proxy.game;

import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="game")
public interface GameServerProxy {
	@PostMapping("/game/create")
	public GameCreateResponse create(@RequestBody GameCreateRequest request );

	@PostMapping("/game/{sessionKey}/join")
	public GameJoinResponse join(@PathVariable String sessionKey,@RequestBody GameJoinRequest request );

	@PostMapping("/game/{sessionKey}/play/step")
	public GameStateResponse step(@PathVariable String sessionKey, @RequestBody GameStepRequest request );

	@GetMapping("/game/{sessionKey}/play/current")
	public GameStateResponse currentState(@PathVariable String sessionKey);
}
