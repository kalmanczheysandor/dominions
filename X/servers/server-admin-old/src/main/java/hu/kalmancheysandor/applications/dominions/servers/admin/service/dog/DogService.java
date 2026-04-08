package hu.kalmancheysandor.applications.dominions.service.dog;


import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.dog.*;
import hu.kalmancheysandor.applications.dominions.service.breed.exception.BreedAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.service.breed.exception.BreedNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.service.breed.exception.BreedReferencedElsewhereException;
import hu.kalmancheysandor.applications.dominions.service.site.exception.SiteAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.dog.galery.DogGalleryResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.dog.option.DogBreedOptionResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.dog.option.DogSiteOptionResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.entity.breed.Breed;
import hu.kalmancheysandor.applications.dominions.servers.admin.entity.dog.Dog;
import hu.kalmancheysandor.applications.dominions.servers.admin.entity.site.Site;
import hu.kalmancheysandor.applications.dominions.servers.admin.repository.breed.BreedRepository;
import hu.kalmancheysandor.applications.dominions.servers.admin.repository.dog.DogRepository;
import hu.kalmancheysandor.applications.dominions.servers.admin.repository.site.SiteRepository;
import hu.kalmancheysandor.applications.dominions.service.dog.exception.DogNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.service.dog.exception.DogNotFoundException;
import hu.kalmancheysandor.applications.dominions.service.dog.exception.DogPrnIsReservedException;
import hu.kalmancheysandor.applications.dominions.service.site.exception.SiteNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.utils.filehandler.FileHandler;
import hu.kalmancheysandor.applications.dominions.utils.filehandler.GeneralFileException;
import hu.kalmancheysandor.applications.dominions.utils.uuid.UUIDGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class DogService {

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private BreedRepository breedRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    private FileHandler fileHandler;

    public FileHandler.Result image(@NotBlank String dogUuid) {

        // Access entity via repository
        Dog dog = dogRepository.findByUuid(dogUuid);
        if (dog == null) {
            throw new DogNotFoundByUuidException(dogUuid);
        }


        // Access image file
        int dogId = dog.getId();
        String fullPath = "d:/dogs/" + dogId + "/main.jpg";
        return fileHandler.findFile(fullPath);
    }

    public DogResponse accessDog(@NotBlank String dogUuid) {

        // Access entity via repository
        Dog dog = dogRepository.findByUuid(dogUuid);
        if (dog == null) {
            throw new DogNotFoundByUuidException(dogUuid);
        }

        // Generate response
        DogResponse response = modelMapper.map(dog, DogResponse.class);
        return response;
    }

    public List<DogResponse> listAllDog() {

        // Access entity via repository
        List<Dog> dogList = dogRepository.findAll();

        // Generate response
        return dogList.stream()
            .map(item -> modelMapper.map(item, DogResponse.class))
            .collect(Collectors.toList());
    }

    public DogCreateResponse saveDog(@NotNull DogCreateRequest request) {

        // Checking: Whether the new prn is reserved
        if (dogRepository.isPrnReserved(request.getPrn())) {
            throw new DogPrnIsReservedException(request.getPrn());
        }

        // Checking: Whether breed is exist
        Breed breed = breedRepository.findByUuid(request.getBreedUuid());
        if (breed == null) {
            throw new BreedNotFoundByUuidException(request.getBreedUuid());
        }

        // Checking: Whether breed is enabled
        if (!breed.isEnabled()) {
            throw new BreedAssociationRestrictedException(breed.getId(), breed.getName());
        }


        // Checking: Whether site is exist
        Site site = siteRepository.findByUuid(request.getSiteUuid());
        if (site == null) {
            throw new SiteNotFoundByUuidException(request.getSiteUuid());
        }

        // Checking: Whether site is enabled
        if (!site.isEnabled()) {
            throw new SiteAssociationRestrictedException(site.getId(), site.getName());
        }


        // Save dog
        Dog dogToSave = new Dog();
        dogToSave.setPrn(request.getPrn());
        dogToSave.setName(request.getName());
        dogToSave.setBreed(breed);
        dogToSave.setSite(site);
        dogToSave.setNote(request.getNote());
        dogToSave.setEnabled(request.isEnabled());
        Dog dogSaved = uuidGenerator.saveWithRetry(dogRepository, dogToSave);

        // Save image
        String filename = "d:/dogs/" + dogSaved.getId() + "/main.jpg";
        fileHandler.saveBase64Image(filename, request.getImageBase64());

        // Generate response
        return modelMapper.map(dogSaved, DogCreateResponse.class);
    }

    public DogUpdateResponse updateDog(@NotBlank String dogUuid, @NotNull DogUpdateRequest request) {

        // Access entity via repository
        Dog dogToModify = dogRepository.findByUuid(dogUuid);
        if (dogToModify == null) {
            throw new DogNotFoundByUuidException(dogUuid);
        }
        int dogId = dogToModify.getId();

        // Checking: Whether the new prn is reserved
        if (dogRepository.isPrnReserved(request.getPrn(), dogId)) {
            throw new DogPrnIsReservedException(request.getPrn());
        }

        // Determine which breed is to use: current or the new one.
        Breed breed = dogToModify.getBreed();
        if (!breed.getUuid().equals(request.getBreedUuid())) {
            breed = breedRepository.findByUuid(request.getBreedUuid());
            if (breed == null) {
                throw new BreedNotFoundByUuidException(request.getBreedUuid());
            }

            // Checking: Whether breed is enabled
            if (!breed.isEnabled()) {
                throw new BreedAssociationRestrictedException(breed.getId(), breed.getName());
            }
        }

        // Determine which site is to use: current or the new one.
        Site site = dogToModify.getSite();
        if (!site.getUuid().equals(request.getSiteUuid())) {
            site = siteRepository.findByUuid(request.getSiteUuid());
            if (site == null) {
                throw new SiteNotFoundByUuidException(request.getSiteUuid());
            }

            // Checking: Whether site is enabled
            if (!site.isEnabled()) {
                throw new SiteAssociationRestrictedException(site.getId(), site.getName());
            }
        }
        
        // Modify dog
        dogToModify.setPrn(request.getPrn());
        dogToModify.setName(request.getName());
        dogToModify.setBreed(breed);
        dogToModify.setSite(site);
        dogToModify.setNote(request.getNote());
        dogToModify.setEnabled(request.isEnabled());
        Dog dogModified = uuidGenerator.saveWithRetry(dogRepository, dogToModify);

        // Modify image
        String filename = "d:/dogs/" + dogModified.getId() + "/main.jpg";
        fileHandler.updateBase64Image(filename, request.getImageBase64());

        // Generate response
        return modelMapper.map(dogModified, DogUpdateResponse.class);
    }

    public void deleteOneDog(@NotBlank String uuid) {

        // Execution
        try {
            this.deleteOneRow(uuid);
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    public void deleteMultipleDog(@NotNull DogDeleteRequest request) {

        // Execution
        try {
            for (String uuid : request.getItems()) {
                this.deleteOneRow(uuid);
            }
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    private void deleteOneRow(@NotBlank String dogUuid) {

        // Access entity via repository
        Dog dogToDelete = dogRepository.findByUuid(dogUuid);
        if (dogToDelete == null) {
            throw new DogNotFoundByUuidException(dogUuid);
        }
        int dogId = dogToDelete.getId();

        // Check: whether any foreign key still referencing this record
        if (dogRepository.isReferencedElsewhere(dogId)) {
            throw new BreedReferencedElsewhereException(dogId, dogToDelete.getName());
        }

        // Delete item from db
        try {
            dogRepository.deleteById(dogId);
        } catch (EntityNotFoundException e) {
            throw new DogNotFoundException(dogId);
        }

        // Delete belonging files
        String folderPath = "d:/dogs/" + dogId;
        fileHandler.deleteDirectoryRecursively(folderPath);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////// GALLERY ////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public FileHandler.Result accessDogGalleryItem(@NotBlank String dogUuid, @NotBlank String filename) {


        // Access entity via repository
        Dog dogToFind = dogRepository.findByUuid(dogUuid);
        if (dogToFind == null) {
            throw new DogNotFoundByUuidException(dogUuid);
        }

        // Access
        String fullPath = "d:/dogs/" + dogToFind.getId() + "/gallery/" + filename;
        return fileHandler.findFile(fullPath);
    }

    public void uploadDogGallery(@NotBlank String dogUuid, @NotNull @NotEmpty MultipartFile[] files) {

        //
        if (files.length == 0) {
            throw new GeneralFileException("No files were uploaded");
        }


        // Access entity via repository
        Dog dogToFind = dogRepository.findByUuid(dogUuid);
        if (dogToFind == null) {
            throw new DogNotFoundByUuidException(dogUuid);
        }

        // Save file
        String folderPath = "d:/dogs/" + dogToFind.getId() + "/gallery";
        for (MultipartFile file : files) {
            fileHandler.updateMultipartFile(folderPath, file);
        }
    }

    public List<DogGalleryResponse> listDogGallery(@NotBlank String dogUuid) {


        // Access entity via repository
        Dog dogToFind = dogRepository.findByUuid(dogUuid);
        if (dogToFind == null) {
            throw new DogNotFoundByUuidException(dogUuid);
        }

        // List files in folder
        String folderPath = "d:/dogs/" + dogToFind.getId() + "/gallery";
        List<String> filenamesList = fileHandler.listFilesOfFolder(folderPath, true);

        // Generate response
        List<DogGalleryResponse> response = new ArrayList<>();
        for (String filename : filenamesList) {
            DogGalleryResponse item = new DogGalleryResponse();
            item.setFilename(filename);
            item.setUuid(dogUuid);

            response.add(item);
        }
        return response;
    }

    public void deleteDogGalleryItem(@NotBlank String dogUuid, @NotBlank String filename) {

        // Access entity via repository
        Dog dogToFind = dogRepository.findByUuid(dogUuid);
        if (dogToFind == null) {
            throw new DogNotFoundByUuidException(dogUuid);
        }
        int dogId = dogToFind.getId();

        // Access
        String fullPath = "d:/dogs/" + dogToFind.getId() + "/gallery/" + filename;
        fileHandler.deleteFileIfExists(fullPath);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////// OPTIONS /////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<DogBreedOptionResponse> atSaveListBreed() {

        List<Breed> breedList = breedRepository.listAllEnabled();
        return breedList.stream()
            .map(item -> modelMapper.map(item, DogBreedOptionResponse.class))
            .collect(Collectors.toList());
    }


    public List<DogBreedOptionResponse> atUpdateListBreed(@NotBlank String dogUuid) {

        // Access entity via repository
        Dog dogToFind = dogRepository.findByUuid(dogUuid);
        if (dogToFind == null) {
            throw new DogNotFoundByUuidException(dogUuid);
        }
        int breedIdToExclude = dogToFind.getBreed().getId();

        // Generate response
        List<Breed> breedList = breedRepository.listAllEnabledWithOneExcludedId(breedIdToExclude);
        return breedList.stream()
            .map(item -> modelMapper.map(item, DogBreedOptionResponse.class))
            .collect(Collectors.toList());
    }



    public List<DogSiteOptionResponse> atSaveListSite() {

        List<Site> siteList = siteRepository.listAllEnabled();
        return siteList.stream()
            .map(item -> modelMapper.map(item, DogSiteOptionResponse.class))
            .collect(Collectors.toList());
    }


    public List<DogSiteOptionResponse> atUpdateListSite(@NotBlank String dogUuid) {

        // Access entity via repository
        Dog dogToFind = dogRepository.findByUuid(dogUuid);
        if (dogToFind == null) {
            throw new DogNotFoundByUuidException(dogUuid);
        }
        int siteIdToExclude = dogToFind.getSite().getId();

        // Generate response
        List<Site> siteList = siteRepository.listAllEnabledWithOneExcludedId(siteIdToExclude);
        return siteList.stream()
            .map(item -> modelMapper.map(item, DogSiteOptionResponse.class))
            .collect(Collectors.toList());
    }



}