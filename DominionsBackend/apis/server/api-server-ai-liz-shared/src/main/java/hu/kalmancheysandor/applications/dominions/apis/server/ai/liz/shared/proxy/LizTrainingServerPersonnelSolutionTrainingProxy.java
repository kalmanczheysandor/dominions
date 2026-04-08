package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.proxy;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.xxx.*;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "server-ai-liz-training",
        contextId = "LizTrainingServerPersonnelSolutionTrainingProxy",
        path = "/personnel/0/solution"
)
public interface LizTrainingServerPersonnelSolutionTrainingProxy {

    @GetMapping("/{solutionUuid}/training/{uuid}")
    public LizPersonnelSolutionTrainingAccessResponse access(@PathVariable("solutionUuid") String solutionUuid, @PathVariable("uuid") String uuid);

    @GetMapping("/{solutionUuid}/training/list")
    public List<LizPersonnelSolutionTrainingItemResponse> listAll(@PathVariable("solutionUuid") String solutionUuid);

    @PostMapping("/{solutionUuid}/training/add")
    public LizPersonnelSolutionTrainingCreateResponse add(@PathVariable("solutionUuid") String solutionUuid, @Valid @RequestBody LizPersonnelSolutionTrainingCreateRequest request);

    @PostMapping("/{solutionUuid}/training/{uuid}/edit")
    public LizPersonnelSolutionTrainingUpdateResponse edit(@PathVariable("solutionUuid") String solutionUuid, @PathVariable("uuid") String uuid, @Valid @RequestBody LizPersonnelSolutionTrainingUpdateRequest request);

    @DeleteMapping("/{solutionUuid}/training/{uuid}/delete")
    public void delete(@PathVariable("solutionUuid") String solutionUuid, @PathVariable("uuid") String uuid);

    @DeleteMapping("/{solutionUuid}/training/delete")
    public void delete(@PathVariable("solutionUuid") String solutionUuid, @Valid @RequestBody LizPersonnelSolutionTrainingDeleteRequest request);

}
