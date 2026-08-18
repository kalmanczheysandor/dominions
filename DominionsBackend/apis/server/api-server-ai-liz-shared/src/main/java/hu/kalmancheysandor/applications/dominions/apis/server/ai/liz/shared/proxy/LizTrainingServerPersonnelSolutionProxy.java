package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.proxy;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.solution.*;
import hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.feign.PrimaryFeignProxyConfig;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "server-ai-liz-training",
        contextId = "LizTrainingServerPersonnelSolutionProxy",
        configuration = PrimaryFeignProxyConfig.class,
        path = "/personnel"
)
public interface LizTrainingServerPersonnelSolutionProxy {
    @GetMapping("/{personnelUuid}/solution/{uuid}")
    public LizPersonnelSolutionAccessResponse access(@PathVariable("personnelUuid") String personnelUuid, @PathVariable("uuid") String uuid) ;

    @GetMapping("/{personnelUuid}/solution/list")
    public List<LizPersonnelSolutionItemResponse> listAll(@PathVariable("personnelUuid") String personnelUuid) ;
    @PostMapping("/{personnelUuid}/solution/add")
    public LizPersonnelSolutionCreateResponse add(@PathVariable("personnelUuid") String personnelUuid, @Valid @RequestBody LizPersonnelSolutionCreateRequest request);
    @PostMapping("/{personnelUuid}/solution/{uuid}/edit")
    public LizPersonnelSolutionUpdateResponse edit(@PathVariable("personnelUuid") String personnelUuid, @PathVariable("uuid") String uuid, @Valid @RequestBody LizPersonnelSolutionUpdateRequest request);

    @DeleteMapping("/{personnelUuid}/solution/{uuid}/delete")
    public void delete(@PathVariable("personnelUuid") String personnelUuid,@PathVariable("uuid") String uuid);

    @DeleteMapping("/{personnelUuid}/solution/delete")
    public void delete(@PathVariable("personnelUuid") String personnelUuid,@Valid @RequestBody LizPersonnelSolutionDeleteRequest request);
}
