package hu.kalmancheysandor.applications.dominions.servers.ai.otto.operation.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.dto.character.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.dto.character.option.OttoCharacterVariantOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.training.OttoCharacter;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.training.OttoVariant;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.character.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.variant.OttoVariantAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.variant.OttoVariantNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.variant.OttoVariantNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.repository.training.OttoCharacterRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.repository.training.OttoVariantRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.service.TOttoService;
import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class OttoCharacterService extends TOttoService {


    @Autowired
    private OttoCharacterRepository ottoCharacterRepository;
    
    @Autowired
    private OttoVariantRepository ottoVariantRepository;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationConfig applicationConfig;


    public OttoCharacterAccessResponse access(@NotBlank String recordUuid) {

        // Access entity via repository
        OttoCharacter recordToAccess = ottoCharacterRepository.findByUuid(recordUuid);
        if (recordToAccess == null) {
            throw new OttoCharacterNotFoundByUuidException(recordUuid);
        }

        // Generate response
        return modelMapper.map(recordToAccess, OttoCharacterAccessResponse.class);
    }

    public List<OttoCharacterItemResponse> listAll() {

        // Access entity via repository
        List<OttoCharacter> recordList = ottoCharacterRepository.findAll();

        // Generate response
        return recordList.stream()
                .map(item -> modelMapper.map(item, OttoCharacterItemResponse.class))
                .collect(Collectors.toList());
    }

    public OttoCharacterCreateResponse save(@NotNull OttoCharacterCreateRequest request) {

        // Checking: Whether the new name is reserved
        if (ottoCharacterRepository.isNameReserved(request.getName())) {
            throw new OttoCharacterNameIsReservedException(request.getName());
        }

        // Checking: Whether the new code is reserved
        if (ottoCharacterRepository.isCodeReserved(request.getCode())) {
            throw new OttoCharacterCodeIsReservedException(request.getCode());
        }


        // Checking: Whether variant is exist
        OttoVariant ottoVariant = ottoVariantRepository.findByUuid(request.getVariantUuid());
        if (ottoVariant == null) {
            throw new OttoVariantNotFoundByUuidException(request.getVariantUuid());
        }

        // Checking: Whether variant is enabled
        if (!ottoVariant.isEnabled()) {
            throw new OttoVariantAssociationRestrictedException(ottoVariant.getId(), ottoVariant.getName());
        }
        
        
        // Save lizCharacter
        OttoCharacter recordToSave = new OttoCharacter();
        recordToSave.setName(request.getName());
        recordToSave.setCode(request.getCode());
        recordToSave.setVariant(ottoVariant);
        recordToSave.setEnabled(request.isEnabled());
        recordToSave.setDateCreated(LocalDateTime.now());
        OttoCharacter recordSaved = uuidGenerator.saveWithRetry(ottoCharacterRepository, recordToSave);

        // Generate response
        return modelMapper.map(recordSaved, OttoCharacterCreateResponse.class);
    }

    public OttoCharacterUpdateResponse update(@NotBlank String recordUuid, @NotNull OttoCharacterUpdateRequest request) {

        // Access entity via repository
        OttoCharacter recordToModify = ottoCharacterRepository.findByUuid(recordUuid);
        if (recordToModify == null) {
            throw new OttoCharacterNotFoundByUuidException(recordUuid);
        }
        int recordId = recordToModify.getId();

        // Checking: Whether the new name is reserved
        if (ottoCharacterRepository.isNameReserved(request.getName(), recordId)) {
            throw new OttoCharacterNameIsReservedException(request.getName());
        }
        // Checking: Whether the new code is reserved
        if (ottoCharacterRepository.isCodeReserved(request.getCode(), recordId)) {
            throw new OttoCharacterCodeIsReservedException(request.getCode());
        }

        // Determine which variant is to use: current or the new one.
        OttoVariant ottoVariant = recordToModify.getVariant();
        if (ottoVariant == null) {
            throw new OttoVariantNotFoundException();
        }
        if (!ottoVariant.getUuid().equals(request.getVariantUuid())) {// If new value is sent from frontend

            // Access instance
            ottoVariant = ottoVariantRepository.findByUuid(request.getVariantUuid());
            if (ottoVariant == null) {
                throw new OttoVariantNotFoundByUuidException(request.getVariantUuid());
            }

            // Checking: Whether variant is enabled
            if (!ottoVariant.isEnabled()) {
                throw new OttoVariantAssociationRestrictedException(ottoVariant.getId(), ottoVariant.getName());
            }
        }

        // Modify lizCharacter
        recordToModify.setName(request.getName());
        recordToModify.setCode(request.getCode());
        recordToModify.setVariant(ottoVariant);
        recordToModify.setEnabled(request.isEnabled());
        recordToModify.setDateModified(LocalDateTime.now());
        OttoCharacter recordModified = uuidGenerator.saveWithRetry(ottoCharacterRepository, recordToModify);

        // Generate response
        return modelMapper.map(recordModified, OttoCharacterUpdateResponse.class);
    }

    public void deleteOneOttoCharacter(@NotBlank String recordUuid) {

        // Execution
        try {
            this.deleteOneRow(recordUuid);
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    public void deleteMultiple(@NotNull OttoCharacterDeleteRequest request) {

        // Execution
        try {
            for (String uuid : request.getItems()) {
                this.deleteOneRow(uuid);
            }
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    private void deleteOneRow(@NotBlank String recordUuid) {

        // Access entity via repository
        OttoCharacter recordToDelete = ottoCharacterRepository.findByUuid(recordUuid);
        if (recordToDelete == null) {
            throw new OttoCharacterNotFoundByUuidException(recordUuid);
        }
        int recordId = recordToDelete.getId();

        // Check: whether any foreign key referencing this record
        if (ottoCharacterRepository.isReferencedElsewhere(recordId)) {
            throw new OttoCharacterReferencedElsewhereException(recordId, recordToDelete.getName());
        }

        // Delete item from db
        try {
            ottoCharacterRepository.deleteById(recordId);
        } catch (EntityNotFoundException e) {
            throw new OttoCharacterNotFoundException(recordId);
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////// OPTIONS /////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<OttoCharacterVariantOptionResponse> atSaveListVariant() {
        List<OttoVariant> variantList = ottoVariantRepository.listAllEnabled();
        return variantList.stream()
                .map(item -> modelMapper.map(item, OttoCharacterVariantOptionResponse.class))
                .collect(Collectors.toList());
    }

    public List<OttoCharacterVariantOptionResponse> atUpdateListVariant(@NotBlank String recordUuid) {

        // Access entity via repository
        OttoCharacter recordToFind = ottoCharacterRepository.findByUuid(recordUuid);
        if (recordToFind == null) {
            throw new OttoCharacterNotFoundByUuidException(recordUuid);
        }
        int variantIdToExclude = recordToFind.getVariant().getId();

        // Generate response
        List<OttoVariant> variantList = ottoVariantRepository.listAllEnabledWithOneExcludedId(variantIdToExclude);
        return variantList.stream()
                .map(item -> modelMapper.map(item, OttoCharacterVariantOptionResponse.class))
                .collect(Collectors.toList());
    }

}
