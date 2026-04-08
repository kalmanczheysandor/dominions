package hu.kalmancheysandor.applications.dominions.service.breed;


import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.breed.*;
import hu.kalmancheysandor.applications.dominions.servers.admin.entity.breed.Breed;
import hu.kalmancheysandor.applications.dominions.servers.admin.repository.breed.BreedRepository;
import hu.kalmancheysandor.applications.dominions.service.breed.exception.BreedNameIsReservedException;
import hu.kalmancheysandor.applications.dominions.service.breed.exception.BreedNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.service.breed.exception.BreedNotFoundException;

import hu.kalmancheysandor.applications.dominions.service.breed.exception.BreedReferencedElsewhereException;
import hu.kalmancheysandor.applications.dominions.utils.uuid.UUIDGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BreedService {

    @Autowired
    private BreedRepository breedRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UUIDGenerator uuidGenerator;


    public BreedResponse accessBreed(@NotBlank String uuid) {

        // Access entity via repository
        Breed breed = breedRepository.findByUuid(uuid);
        if (breed == null) {
            throw new BreedNotFoundByUuidException(uuid);
        }

        // Generate response
        BreedResponse response = modelMapper.map(breed, BreedResponse.class);
        return response;
    }

    public BreedCreateResponse saveBreed(@NotNull BreedCreateRequest request) {

        // Checking: Whether the new name field is reserved
        if (breedRepository.isNameReserved(request.getName())) {
            throw new BreedNameIsReservedException(request.getName());
        }

        // Save breed
        Breed breedToSave = new Breed();
        breedToSave.setName(request.getName());
        breedToSave.setEnabled(request.isEnabled());
        Breed breedSaved = uuidGenerator.saveWithRetry(breedRepository, breedToSave);

        // Generate response
        return modelMapper.map(breedSaved, BreedCreateResponse.class);
    }

    public BreedUpdateResponse updateBreed(@NotBlank String uuid, @NotNull BreedUpdateRequest request) {

        // Access entity via repository
        Breed breedToModify = breedRepository.findByUuid(uuid);
        if (breedToModify == null) {
            throw new BreedNotFoundByUuidException(uuid);
        }
        int breedId = breedToModify.getId();

        // Checking: Whether the new identifier is reserved
        if (breedRepository.isNameReserved(request.getName(), breedId)) {
            throw new BreedNameIsReservedException(request.getName());
        }


        // Set simple properties
        breedToModify.setName(request.getName());
        breedToModify.setEnabled(request.isEnabled());
        Breed breedModified = uuidGenerator.saveWithRetry(breedRepository, breedToModify);

        // Generate response
        return modelMapper.map(breedModified, BreedUpdateResponse.class);
    }

    public List<BreedResponse> listAllBreed() {

        // Access entity via repository
        List<Breed> breedList = breedRepository.findAll();

        // Generate response
        return breedList.stream()
            .map(item -> modelMapper.map(item, BreedResponse.class))
            .collect(Collectors.toList());
    }

    public void deleteOneBreed(@NotBlank String uuid) {

        // Execution
        try {
            this.deleteOneRow(uuid);
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    public void deleteMultipleBreed(@NotNull BreedDeleteRequest request) {

        // Execution
        try {
            for (String uuid : request.getItems()) {
                this.deleteOneRow(uuid);
            }
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    private void deleteOneRow(@NotBlank String uuid) {

        // Access entity via repository
        Breed breedToDelete = breedRepository.findByUuid(uuid);
        if (breedToDelete == null) {
            throw new BreedNotFoundByUuidException(uuid);
        }
        int breedId = breedToDelete.getId();

        // Check: whether any foreign key still referencing this record
        if (breedRepository.isReferencedElsewhere(breedId)) {
            throw new BreedReferencedElsewhereException(breedId, breedToDelete.getName());
        }

        // Delete item from db
        try {
            breedRepository.deleteById(breedId);
        } catch (EntityNotFoundException e) {
            throw new BreedNotFoundException(breedId);
        }
    }
}