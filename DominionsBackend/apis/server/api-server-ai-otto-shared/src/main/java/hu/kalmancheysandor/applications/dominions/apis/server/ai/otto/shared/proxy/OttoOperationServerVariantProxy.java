package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.proxy;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.dto.variant.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.dto.variant.option.OttoVariantEvaluatorOptionResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "server-ai-otto-operation",
        contextId = "OttoOperationServerVariantProxy",
//        configuration = PrimaryFeignProxyConfig.class,
        path = "/variant"
)
public interface OttoOperationServerVariantProxy {

    @GetMapping("/{uuid}")
    public OttoVariantAccessResponse access(@PathVariable("uuid") String uuid);

    @GetMapping("/list")
    public List<OttoVariantItemResponse> listAll();

    @PostMapping("/add")
    public OttoVariantCreateResponse add(@Valid @RequestBody OttoVariantCreateRequest request);

    @GetMapping("/add/options/evaluator")
    public List<OttoVariantEvaluatorOptionResponse> atAddListEvaluator();

    @PostMapping("/{uuid}/edit")
    public OttoVariantUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody OttoVariantUpdateRequest request);

    @GetMapping("/{uuid}/edit/options/evaluator")
    public List<OttoVariantEvaluatorOptionResponse> atEditListEvaluator(@PathVariable("uuid") String uuid);

    @DeleteMapping("/{uuid}/delete")
    public void delete(@PathVariable("uuid") String uuid);

    @DeleteMapping("/delete")
    public void delete(@Valid @RequestBody OttoVariantDeleteRequest request);
}
