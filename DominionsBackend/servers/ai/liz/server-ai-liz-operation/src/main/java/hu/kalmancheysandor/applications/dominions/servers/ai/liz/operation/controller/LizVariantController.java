package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.controller;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.character.option.LizCharacterVariantOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.variant.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.variant.option.LizVariantConceptOptionResponse;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.service.LizVariantService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/variant")
@Slf4j
public class LizVariantController {
    @Autowired
    private LizVariantService lizVariantService;

    /// ///////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public LizVariantAccessResponse access(@PathVariable("uuid") String uuid) {
        return lizVariantService.access(uuid);
    }

    /// ///////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<LizVariantItemResponse> listAll() {

        return lizVariantService.listAll();
    }

    /// ///////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public LizVariantCreateResponse add(@Valid @RequestBody LizVariantCreateRequest request) {
        return lizVariantService.save(request);
    }

    @GetMapping("/add/options/concept")
    public List<LizVariantConceptOptionResponse> atAddListConcept() {
        return lizVariantService.atSaveListConcept();
    }

    /// ///////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public LizVariantUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody LizVariantUpdateRequest request) {
        return lizVariantService.update(uuid, request);
    }


    @GetMapping("/{uuid}/edit/options/concept")
    public List<LizVariantConceptOptionResponse> atEditListConcept(@PathVariable("uuid") String uuid) {


        return lizVariantService.atUpdateListVariant(uuid);
    }
    /// ///////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        lizVariantService.deleteOneLizPersonnel(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody LizVariantDeleteRequest request) {
        lizVariantService.deleteMultiple(request);
    }

}
