package hu.kalmancheysandor.applications.dominions.servers.admin.controller.game.scenario;

import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.AdminPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.authentication.AdminAuthenticationService;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.game.scenario.*;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.game.scenario.GameScenarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
@RequestMapping("/game/scenario")
@Slf4j
public class GameScenarioController {

    @Autowired
    private GameScenarioService gameScenarioService;

    @Autowired
    private AdminAuthenticationService authenticationService;


    /// ///////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public GameScenarioAccessResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().game().scenario());

        return gameScenarioService.accessGameScenario(uuid);
    }

    /// ///////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<GameScenarioItemResponse> listAllGameScenario() {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().game().scenario());

        return gameScenarioService.listAllGameScenario();
    }

    /// ///////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public GameScenarioCreateResponse addGameScenario(@Valid @RequestBody GameScenarioCreateRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().game().scenario());

        return gameScenarioService.saveGameScenario(request);
    }


    /// ///////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public GameScenarioUpdateResponse editGameScenario(@PathVariable("uuid") String uuid, @Valid @RequestBody GameScenarioUpdateRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().game().scenario());

        return gameScenarioService.updateGameScenario(uuid, request);
    }


    /// ///////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().game().scenario());

        gameScenarioService.deleteOneGameScenario(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody GameScenarioDeleteRequest request) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().game().scenario());

        gameScenarioService.deleteMultipleGameScenario(request);
    }

    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////// IMAGES /////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}/image/main")
    public ResponseEntity<Resource> image(@PathVariable("uuid") String uuid, HttpServletRequest request) {

        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().game().scenario());


        FileHandler.Result result = gameScenarioService.image(uuid);
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
