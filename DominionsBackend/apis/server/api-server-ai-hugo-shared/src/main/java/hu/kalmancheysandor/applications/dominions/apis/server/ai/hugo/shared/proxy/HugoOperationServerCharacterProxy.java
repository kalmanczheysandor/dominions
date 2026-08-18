package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.proxy;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.character.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.character.option.HugoCharacterVariantOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.feign.PrimaryFeignProxyConfig;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "server-ai-hugo-operation",
        contextId = "HugoOperationServerCharacterProxy",
//        configuration = PrimaryFeignProxyConfig.class,
        path = "/character"
)
public interface HugoOperationServerCharacterProxy {


    @GetMapping("/{uuid}")
    public HugoCharacterAccessResponse access(@PathVariable("uuid") String uuid);

    @GetMapping("/list")
    public List<HugoCharacterItemResponse> listAll();
    @PostMapping("/add")
    public HugoCharacterCreateResponse add(@Valid @RequestBody HugoCharacterCreateRequest request);

    @GetMapping("/add/options/variant")
    public List<HugoCharacterVariantOptionResponse> atAddListVariant();

    @PostMapping("/{uuid}/edit")
    public HugoCharacterUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody HugoCharacterUpdateRequest request);

    @GetMapping("/{uuid}/edit/options/variant")
    public List<HugoCharacterVariantOptionResponse> atEditListVariant(@PathVariable("uuid") String uuid);

    @DeleteMapping("/{uuid}/delete")
    public void delete(@PathVariable("uuid") String uuid);

    @DeleteMapping("/delete")
    public void delete(@Valid @RequestBody HugoCharacterDeleteRequest request);
}
