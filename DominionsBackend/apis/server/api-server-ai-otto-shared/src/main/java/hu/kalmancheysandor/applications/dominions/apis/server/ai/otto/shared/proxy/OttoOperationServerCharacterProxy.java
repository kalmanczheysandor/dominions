package hu.kalmancheysandor.applications.dominions.apis.server.ai. otto.shared.proxy;


import hu.kalmancheysandor.applications.dominions.apis.server.ai. otto.shared.dto.character.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai. otto.shared.dto.character.option.OttoCharacterVariantOptionResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "server-ai-otto-operation",
        contextId = "OttoOperationServerCharacterProxy",
//        configuration = PrimaryFeignProxyConfig.class,
        path = "/character"
)
public interface OttoOperationServerCharacterProxy {


    @GetMapping("/{uuid}")
    public  OttoCharacterAccessResponse access(@PathVariable("uuid") String uuid);

    @GetMapping("/list")
    public List<OttoCharacterItemResponse> listAll();
    @PostMapping("/add")
    public OttoCharacterCreateResponse add(@Valid @RequestBody OttoCharacterCreateRequest request);

    @GetMapping("/add/options/variant")
    public List<OttoCharacterVariantOptionResponse> atAddListVariant();

    @PostMapping("/{uuid}/edit")
    public OttoCharacterUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody OttoCharacterUpdateRequest request);

    @GetMapping("/{uuid}/edit/options/variant")
    public List<OttoCharacterVariantOptionResponse> atEditListVariant(@PathVariable("uuid") String uuid);

    @DeleteMapping("/{uuid}/delete")
    public void delete(@PathVariable("uuid") String uuid);

    @DeleteMapping("/delete")
    public void delete(@Valid @RequestBody OttoCharacterDeleteRequest request);
}
