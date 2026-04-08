package hu.kalmancheysandor.applications.dominions.servers.admin.controller.site;


import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.site.*;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.site.SiteService;
import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission.SecurityAuthorisation;
import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission.SecurityPermission;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data/site")
public class SiteController {

    @Autowired
    private SiteService siteService;

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public SiteResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().site());

        return siteService.accessSite(uuid);
    }

    @GetMapping("/list")
    public List<SiteResponse> list() {
        // Check permission
        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().site());

        return siteService.listAllSite();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public SiteCreateResponse add(@Valid @RequestBody SiteCreateRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasAddPermission(SecurityPermission.generator().site());

        return siteService.saveSite(request);
    }

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public SiteUpdateResponse update(@PathVariable("uuid") String uuid, @Valid @RequestBody SiteUpdateRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasEditPermission(SecurityPermission.generator().site());

        return siteService.updateSite(uuid, request);
    }

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        SecurityAuthorisation.assertHasDeletePermission(SecurityPermission.generator().site());

        siteService.deleteOneSite(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody SiteDeleteRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasDeletePermission(SecurityPermission.generator().site());

        siteService.deleteMultipleSite(request);
    }

}
