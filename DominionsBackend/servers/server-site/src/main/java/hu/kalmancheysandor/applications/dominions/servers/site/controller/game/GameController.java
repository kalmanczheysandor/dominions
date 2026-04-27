package hu.kalmancheysandor.applications.dominions.servers.site.controller.game;

import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.*;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.GamePlayAttackActionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.GamePlayReserveActionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.scenario.GameScenarioItemResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.SitePermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.authentication.SiteAuthenticationService;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;
import hu.kalmancheysandor.applications.dominions.servers.site.service.game.GameService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
@Slf4j
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private SiteAuthenticationService authenticationService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public GamePlayCreateResponse createGamePlay(@Valid @RequestBody GamePlayCreateRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(SitePermission.generator().game().create());

        return gameService.createGamePlay(request);
    }


    @PostMapping("/join")
    @ResponseStatus(HttpStatus.OK)
    public GamePlayJoinResponse joinToGamePlay(@NotNull @RequestBody GamePlayJoinRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(SitePermission.generator().game().lobby());

        return gameService.joinToGamePlay(request);
    }


    @PostMapping("/state")
    @ResponseStatus(HttpStatus.OK)
    public GamePlayStateResponse stateOfGamePlay(@NotNull @RequestBody GamePlayStateRequest request) {
        // Check permission
        authenticationService.assertHasAccessPermission(SitePermission.generator().game().play());

        return gameService.stateOfGamePlay(request);
    }


    @GetMapping("/list/recruiting")
    public List<GamePlayItemResponse> listAllRecruitingGamePlay() {
        // Check permission
        authenticationService.assertHasAccessPermission(SitePermission.generator().game().lobby());

        return gameService.listAllRecruitingGamePlay();
    }

    @GetMapping("/list/scenario")
    public List<GameScenarioItemResponse> listAllPublishedGameScenario() {
        // Check permission
        authenticationService.assertHasAccessPermission(SitePermission.generator().game().create());

        return gameService.listAllPublishedGameScenario();
    }




    @PostMapping("/action/attack")
    @ResponseStatus(HttpStatus.OK)
    public void sendAttackActionToGamePlay(@NotNull @RequestBody GamePlayAttackActionRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(SitePermission.generator().game().play());

        gameService.sendAttackActionToGamePlay(request);
    }

    @PostMapping("/action/reserve")
    @ResponseStatus(HttpStatus.OK)
    public void sendReserveActionToGamePlay(@NotNull @RequestBody GamePlayReserveActionRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(SitePermission.generator().game().play());

        gameService.sendReserveActionToGamePlay(request);
    }


    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////// IMAGES /////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/scenario/{uuid}/image/main")
    public ResponseEntity<Resource> image(@PathVariable("uuid") String uuid, HttpServletRequest request) {
        // Check permission
        authenticationService.assertHasAccessPermission(SitePermission.generator().game().create());

        //
        FileHandler.Result result = gameService.scenarioImage(uuid);
        if (result == null) {
            return ResponseEntity.noContent().build();
        }

        Resource resource = result.getResource();
        String etag = result.getEtag();
        long lastModified = result.getLastModified();

        // Response: If the file has not been changed, the cached version will be used on the frontend
        if (request.getHeader(HttpHeaders.IF_NONE_MATCH) != null && etag.equals(request.getHeader(HttpHeaders.IF_NONE_MATCH))) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        if (request.getHeader(HttpHeaders.IF_MODIFIED_SINCE) != null && lastModified <= Long.parseLong(request.getHeader(HttpHeaders.IF_MODIFIED_SINCE))) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        // Response: If the file has been changed or has not been cached
        return ResponseEntity.ok()
                .eTag(etag)
                .lastModified(lastModified)
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);

    }


}
