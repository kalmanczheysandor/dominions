package hu.kalmancheysandor.applications.dominions.servers.admin.controller.breed;


import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.breed.*;
import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission.SecurityAuthorisation;
import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission.SecurityPermission;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.breed.BreedService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data/breed")
public class BreedController {

    @Autowired
    private BreedService breedService;


    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public BreedResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().breed());

        return breedService.accessBreed(uuid);
    }


    @GetMapping("/list")
    public List<BreedResponse> list() {
        // Check permission
        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().breed());

        return breedService.listAllBreed();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public BreedCreateResponse add(@Valid @RequestBody BreedCreateRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasAddPermission(SecurityPermission.generator().breed());

        return breedService.saveBreed(request);
    }

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public BreedUpdateResponse update(@PathVariable("uuid") String uuid, @Valid @RequestBody BreedUpdateRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasEditPermission(SecurityPermission.generator().breed());

        return breedService.updateBreed(uuid, request);
    }

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        SecurityAuthorisation.assertHasDeletePermission(SecurityPermission.generator().breed());

        breedService.deleteOneBreed(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody BreedDeleteRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasDeletePermission(SecurityPermission.generator().breed());

        breedService.deleteMultipleBreed(request);
    }

}
