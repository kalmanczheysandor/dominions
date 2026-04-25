package hu.kalmancheysandor.applications.dominions.servers.ai.hugo.operation.controller;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.variant.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.variant.option.HugoVariantHeuristicOptionResponse;
import hu.kalmancheysandor.applications.dominions.servers.ai.hugo.operation.service.HugoVariantService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/variant")
@Slf4j
public class HugoVariantController {
    @Autowired
    private HugoVariantService hugoVariantService;

    /// ///////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public HugoVariantAccessResponse access(@PathVariable("uuid") String uuid) {
        return hugoVariantService.access(uuid);
    }

    /// ///////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<HugoVariantItemResponse> listAll() {

        return hugoVariantService.listAll();
    }

    /// ///////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public HugoVariantCreateResponse add(@Valid @RequestBody HugoVariantCreateRequest request) {
        return hugoVariantService.save(request);
    }

    @GetMapping("/add/options/heuristic")
    public List<HugoVariantHeuristicOptionResponse> atAddListHeuristic() {
        return hugoVariantService.atSaveListHeuristic();
    }

    /// ///////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public HugoVariantUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody HugoVariantUpdateRequest request) {
        return hugoVariantService.update(uuid, request);
    }


    @GetMapping("/{uuid}/edit/options/heuristic")
    public List<HugoVariantHeuristicOptionResponse> atEditListHeuristic(@PathVariable("uuid") String uuid) {
        return hugoVariantService.atUpdateListHeuristic(uuid);
    }
    /// ///////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        hugoVariantService.deleteOneHugoPersonnel(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody HugoVariantDeleteRequest request) {
        hugoVariantService.deleteMultiple(request);
    }

}
