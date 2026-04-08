package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.proxy;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.character.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.character.option.LizCharacterVariantOptionResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "server-ai-liz-operation",
        contextId = "LizOperationServerCharacterProxy",
        path = "/character"
)
public interface LizOperationServerCharacterProxy {


    @GetMapping("/{uuid}")
    public LizCharacterAccessResponse access(@PathVariable("uuid") String uuid);

    @GetMapping("/list")
    public List<LizCharacterItemResponse> listAll();
    @PostMapping("/add")
    public LizCharacterCreateResponse add(@Valid @RequestBody LizCharacterCreateRequest request);

    @GetMapping("/add/options/variant")
    public List<LizCharacterVariantOptionResponse> atAddListVariant();

    @PostMapping("/{uuid}/edit")
    public LizCharacterUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody LizCharacterUpdateRequest request);

    @GetMapping("/{uuid}/edit/options/variant")
    public List<LizCharacterVariantOptionResponse> atEditListVariant(@PathVariable("uuid") String uuid);

    @DeleteMapping("/{uuid}/delete")
    public void delete(@PathVariable("uuid") String uuid);

    @DeleteMapping("/delete")
    public void delete(@Valid @RequestBody LizCharacterDeleteRequest request);
}
