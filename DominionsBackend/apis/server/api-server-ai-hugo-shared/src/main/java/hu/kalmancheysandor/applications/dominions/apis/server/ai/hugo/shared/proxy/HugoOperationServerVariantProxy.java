package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.proxy;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.variant.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.variant.option.HugoVariantHeuristicOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.feign.PrimaryFeignProxyConfig;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "server-ai-hugo-operation",
        contextId = "HugoOperationServerVariantProxy",
//        configuration = PrimaryFeignProxyConfig.class,
        path = "/variant"
)
public interface HugoOperationServerVariantProxy {

    @GetMapping("/{uuid}")
    public HugoVariantAccessResponse access(@PathVariable("uuid") String uuid);

    @GetMapping("/list")
    public List<HugoVariantItemResponse> listAll();
    @PostMapping("/add")
    public HugoVariantCreateResponse add(@Valid @RequestBody HugoVariantCreateRequest request);

    @GetMapping("/add/options/heuristic")
    public List<HugoVariantHeuristicOptionResponse> atAddListHeuristic();

    @PostMapping("/{uuid}/edit")
    public HugoVariantUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody HugoVariantUpdateRequest request);

    @GetMapping("/{uuid}/edit/options/heuristic")
    public List<HugoVariantHeuristicOptionResponse> atEditListHeuristic(@PathVariable("uuid") String uuid);

    @DeleteMapping("/{uuid}/delete")
    public void delete(@PathVariable("uuid") String uuid);

    @DeleteMapping("/delete")
    public void delete(@Valid @RequestBody HugoVariantDeleteRequest request);
}
