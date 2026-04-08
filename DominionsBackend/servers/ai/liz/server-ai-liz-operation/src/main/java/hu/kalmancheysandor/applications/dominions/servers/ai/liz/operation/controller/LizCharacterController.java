package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.controller;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.character.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.character.option.LizCharacterVariantOptionResponse;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.service.LizCharacterService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/character")
@Slf4j
public class LizCharacterController {
    @Autowired
    private LizCharacterService lizCharacterService;

    /// ///////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public LizCharacterAccessResponse access(@PathVariable("uuid") String uuid) {
        return lizCharacterService.access(uuid);
    }

    /// ///////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<LizCharacterItemResponse> listAll() {

        return lizCharacterService.listAll();
    }

    /// ///////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public LizCharacterCreateResponse add(@Valid @RequestBody LizCharacterCreateRequest request) {
        return lizCharacterService.save(request);
    }

    @GetMapping("/add/options/variant")
    public List<LizCharacterVariantOptionResponse> atAddListVariant() {
        return lizCharacterService.atSaveListVariant();
    }


    /// ///////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public LizCharacterUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody LizCharacterUpdateRequest request) {
        return lizCharacterService.update(uuid, request);
    }

    @GetMapping("/{uuid}/edit/options/variant")
    public List<LizCharacterVariantOptionResponse> atEditListVariant(@PathVariable("uuid") String uuid) {


        return lizCharacterService.atUpdateListVariant(uuid);
    }

    /// ///////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        lizCharacterService.deleteOneLizPersonnel(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody LizCharacterDeleteRequest request) {
        lizCharacterService.deleteMultiple(request);
    }

}
