package hu.kalmancheysandor.applications.dominions.servers.admin.controller.training.liz;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.solution.*;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.AdminPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.authentication.AdminAuthenticationService;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.proxy.LizTrainingServerPersonnelSolutionProxy;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

//                    /ai/liz/personnel/0              /solution/'+uuid+'/training'
//@RequestMapping("/ai/liz/personnel/0              /solution/{solutionUuid}/training")
@RequestMapping(  "/ai/liz/personnel/{personnelUuid}/solution")
@Slf4j
public class LizPersonnelSolutionController {
    @Autowired
    private LizTrainingServerPersonnelSolutionProxy lizTrainingServerPersonnelSolutionProxy;

    @Autowired
    private AdminAuthenticationService authenticationService;

    /// ///////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public LizPersonnelSolutionAccessResponse access(@PathVariable("personnelUuid") String personnelUuid, @PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().liz().character().variant());
        System.out.println("PERSONNEL SOLUTION ACCESS:[personnelUuid"+personnelUuid+",uuid"+uuid+"]");
        return lizTrainingServerPersonnelSolutionProxy.access(personnelUuid,uuid);
    }

    /// ///////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<LizPersonnelSolutionItemResponse> listAll(@PathVariable("personnelUuid") String personnelUuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().liz().character().variant());

        System.out.println("LLLLLLLLIST");
        return lizTrainingServerPersonnelSolutionProxy.listAll(personnelUuid);
    }

    /// ///////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public LizPersonnelSolutionCreateResponse add(@PathVariable("personnelUuid") String personnelUuid, @Valid @RequestBody LizPersonnelSolutionCreateRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().ai().liz().character().variant());

        return lizTrainingServerPersonnelSolutionProxy.add(personnelUuid,request);
    }


    /// ///////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public LizPersonnelSolutionUpdateResponse edit(@PathVariable("personnelUuid") String personnelUuid, @PathVariable("uuid") String uuid, @Valid @RequestBody LizPersonnelSolutionUpdateRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().ai().liz().character().variant());

        return lizTrainingServerPersonnelSolutionProxy.edit(personnelUuid,uuid, request);
    }


    /// ///////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("personnelUuid") String personnelUuid,@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().liz().character().variant());

        lizTrainingServerPersonnelSolutionProxy.delete(personnelUuid,uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("personnelUuid") String personnelUuid,@Valid @RequestBody LizPersonnelSolutionDeleteRequest request) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().liz().character().variant());

        lizTrainingServerPersonnelSolutionProxy.delete(personnelUuid,request);
    }

}
