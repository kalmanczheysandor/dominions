package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.proxy;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.character.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.character.option.LizCharacterVariantOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.variant.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.variant.option.LizVariantConceptOptionResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "server-ai-liz-operation",
        contextId = "LizOperationServerVariantProxy",
        path = "/variant"
)
public interface LizOperationServerVariantProxy {

    @GetMapping("/{uuid}")
    public LizVariantAccessResponse access(@PathVariable("uuid") String uuid);

    @GetMapping("/list")
    public List<LizVariantItemResponse> listAll();
    @PostMapping("/add")
    public LizVariantCreateResponse add(@Valid @RequestBody LizVariantCreateRequest request);

    @GetMapping("/add/options/concept")
    public List<LizVariantConceptOptionResponse> atAddListConcept();

    @PostMapping("/{uuid}/edit")
    public LizVariantUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody LizVariantUpdateRequest request);

    @GetMapping("/{uuid}/edit/options/concept")
    public List<LizVariantConceptOptionResponse> atEditListConcept(@PathVariable("uuid") String uuid);

    @DeleteMapping("/{uuid}/delete")
    public void delete(@PathVariable("uuid") String uuid);

    @DeleteMapping("/delete")
    public void delete(@Valid @RequestBody LizVariantDeleteRequest request);
}
