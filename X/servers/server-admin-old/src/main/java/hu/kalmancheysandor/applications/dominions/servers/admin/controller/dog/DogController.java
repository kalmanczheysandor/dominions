package hu.kalmancheysandor.applications.dominions.servers.admin.controller.dog;

import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.dog.*;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.dog.galery.DogGalleryResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.dog.option.DogBreedOptionResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.dog.option.DogSiteOptionResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.dog.DogService;
import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission.SecurityAuthorisation;
import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission.SecurityPermission;
import hu.kalmancheysandor.applications.dominions.servers.admin.utils.filehandler.FileHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/data/dog")
@Slf4j
public class DogController {

    @Autowired
    private DogService dogService;

    private final Path fileStorageLocation = Paths.get("/path/to/your/uploads");


    //////////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public DogResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().dog());

        return dogService.accessDog(uuid);
    }

    //////////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<DogResponse> listAllDog() {
        // Check permission
        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().dog());

        return dogService.listAllDog();
    }

    //////////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public DogCreateResponse addDog(@Valid @RequestBody DogCreateRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasAddPermission(SecurityPermission.generator().dog());

        return dogService.saveDog(request);
    }


    @GetMapping("/add/options/breed")
    public List<DogBreedOptionResponse> atAddListBreed() {
        // Check permission
        SecurityAuthorisation.assertHasAddPermission(SecurityPermission.generator().dog());

        return dogService.atSaveListBreed();
    }

    @GetMapping("/add/options/site")
    public List<DogSiteOptionResponse> atAddListSite() {
        // Check permission
        SecurityAuthorisation.assertHasAddPermission(SecurityPermission.generator().dog());

        return dogService.atSaveListSite();
    }
    //////////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public DogUpdateResponse editDog(@PathVariable("uuid") String uuid, @Valid @RequestBody DogUpdateRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasEditPermission(SecurityPermission.generator().dog());

        return dogService.updateDog(uuid, request);
    }


    @GetMapping("/{uuid}/edit/options/breed")
    public List<DogBreedOptionResponse> atEditListBreed(@PathVariable("uuid") String uuid) {
        // Check permission
        SecurityAuthorisation.assertHasEditPermission(SecurityPermission.generator().dog());

        return dogService.atUpdateListBreed(uuid);
    }

    @GetMapping("/{uuid}/edit/options/site")
    public List<DogSiteOptionResponse> atEditListSite(@PathVariable("uuid") String uuid) {
        // Check permission
        SecurityAuthorisation.assertHasEditPermission(SecurityPermission.generator().dog());

        return dogService.atUpdateListSite(uuid);
    }
    //////////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        SecurityAuthorisation.assertHasDeletePermission(SecurityPermission.generator().dog());

        dogService.deleteOneDog(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody DogDeleteRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasDeletePermission(SecurityPermission.generator().dog());

        dogService.deleteMultipleDog(request);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////// IMAGES /////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}/image/main")
    public ResponseEntity<Resource> image(@PathVariable("uuid") String uuid, HttpServletRequest request) {

        // Check permission
        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().dog());


        FileHandler.Result result = dogService.image(uuid);
        Resource resource = result.getResource();
        String etag = result.getEtag();
        long lastModified = result.getLastModified();

//        System.out.println("ETAG:" + request.getHeader(HttpHeaders.IF_NONE_MATCH));
//        System.out.println("lastModified:" + request.getHeader(HttpHeaders.IF_MODIFIED_SINCE));


        // Response: If the file has not been changed, the cached version will be used on the frontend
        if (request.getHeader(HttpHeaders.IF_NONE_MATCH) != null && etag.equals(request.getHeader(HttpHeaders.IF_NONE_MATCH))) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        if (request.getHeader(HttpHeaders.IF_MODIFIED_SINCE) != null && lastModified <= Long.parseLong(request.getHeader(HttpHeaders.IF_MODIFIED_SINCE))) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        // Response: If the file has been changed or has not been cached
        return ResponseEntity.ok()
            .eTag(etag)
            .lastModified(lastModified)
            .contentType(MediaType.IMAGE_JPEG)
            .body(resource);
    }


    @GetMapping("/{uuid}/gallery/{filename}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Resource> access(@PathVariable("uuid") String uuid, @PathVariable("filename") String filename, HttpServletRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().dog());

        FileHandler.Result result = dogService.accessDogGalleryItem(uuid, filename);
        Resource resource = result.getResource();
        String etag = result.getEtag();
        long lastModified = result.getLastModified();

        // Response: If the file has not been changed, the cached version will be used on the frontend
        if (request.getHeader(HttpHeaders.IF_NONE_MATCH) != null && etag.equals(request.getHeader(HttpHeaders.IF_NONE_MATCH))) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        if (request.getHeader(HttpHeaders.IF_MODIFIED_SINCE) != null && lastModified <= Long.parseLong(request.getHeader(HttpHeaders.IF_MODIFIED_SINCE))) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        // Response: If the file has been changed or has not been cached
        return ResponseEntity.ok()
            .eTag(etag)
            .lastModified(lastModified)
            .contentType(MediaType.IMAGE_JPEG)
            .body(resource);
    }

    //
    @PostMapping("/{uuid}/gallery/upload")
    @ResponseStatus(HttpStatus.OK)
    public void uploadGalleryFiles(@PathVariable("uuid") String uuid, @RequestParam("files") MultipartFile[] files) {
        // Check permission
        SecurityAuthorisation.assertHasEditPermission(SecurityPermission.generator().dog());

        dogService.uploadDogGallery(uuid, files);
    }

    @GetMapping("/{uuid}/gallery/list")
    public List<DogGalleryResponse> listAllGalleryItems(@PathVariable("uuid") String uuid) {
        // Check permission
        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().dog());

        return dogService.listDogGallery(uuid);
    }

    @DeleteMapping("/{uuid}/gallery/{filename}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteOneGalleryItem(@PathVariable("uuid") String uuid, @PathVariable("filename") String filename) {
        // Check permission
        SecurityAuthorisation.assertHasDeletePermission(SecurityPermission.generator().dog());

        dogService.deleteDogGalleryItem(uuid, filename);
    }

}
