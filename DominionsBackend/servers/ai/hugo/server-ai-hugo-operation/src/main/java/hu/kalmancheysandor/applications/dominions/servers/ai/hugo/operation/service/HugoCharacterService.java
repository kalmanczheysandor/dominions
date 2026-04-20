package hu.kalmancheysandor.applications.dominions.servers.ai.hugo.operation.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.character.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.character.option.HugoCharacterVariantOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.training.HugoCharacter;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.training.HugoVariant;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.character.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.variant.HugoVariantAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.variant.HugoVariantNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.variant.HugoVariantNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.repository.training.HugoCharacterRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.repository.training.HugoVariantRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.service.THugoService;
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
public class HugoCharacterService extends THugoService {


    @Autowired
    private HugoCharacterRepository hugoCharacterRepository;
    
    @Autowired
    private HugoVariantRepository hugoVariantRepository;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationConfig applicationConfig;


    public HugoCharacterAccessResponse access(@NotBlank String recordUuid) {

        // Access entity via repository
        HugoCharacter recordToAccess = hugoCharacterRepository.findByUuid(recordUuid);
        if (recordToAccess == null) {
            throw new HugoCharacterNotFoundByUuidException(recordUuid);
        }

        // Generate response
        return modelMapper.map(recordToAccess, HugoCharacterAccessResponse.class);
    }

    public List<HugoCharacterItemResponse> listAll() {

        // Access entity via repository
        List<HugoCharacter> recordList = hugoCharacterRepository.findAll();

        // Generate response
        return recordList.stream()
                .map(item -> modelMapper.map(item, HugoCharacterItemResponse.class))
                .collect(Collectors.toList());
    }

    public HugoCharacterCreateResponse save(@NotNull HugoCharacterCreateRequest request) {

        // Checking: Whether the new name is reserved
        if (hugoCharacterRepository.isNameReserved(request.getName())) {
            throw new HugoCharacterNameIsReservedException(request.getName());
        }

        // Checking: Whether the new code is reserved
        if (hugoCharacterRepository.isCodeReserved(request.getCode())) {
            throw new HugoCharacterCodeIsReservedException(request.getCode());
        }


        // Checking: Whether variant is exist
        HugoVariant hugoVariant = hugoVariantRepository.findByUuid(request.getVariantUuid());
        if (hugoVariant == null) {
            throw new HugoVariantNotFoundByUuidException(request.getVariantUuid());
        }

        // Checking: Whether variant is enabled
        if (!hugoVariant.isEnabled()) {
            throw new HugoVariantAssociationRestrictedException(hugoVariant.getId(), hugoVariant.getName());
        }
        
        
        // Save lizCharacter
        HugoCharacter recordToSave = new HugoCharacter();
        recordToSave.setName(request.getName());
        recordToSave.setCode(request.getCode());
        recordToSave.setVariant(hugoVariant);
        recordToSave.setEnabled(request.isEnabled());
        recordToSave.setDateCreated(LocalDateTime.now());
        HugoCharacter recordSaved = uuidGenerator.saveWithRetry(hugoCharacterRepository, recordToSave);

        // Generate response
        return modelMapper.map(recordSaved, HugoCharacterCreateResponse.class);
    }

    public HugoCharacterUpdateResponse update(@NotBlank String recordUuid, @NotNull HugoCharacterUpdateRequest request) {

        // Access entity via repository
        HugoCharacter recordToModify = hugoCharacterRepository.findByUuid(recordUuid);
        if (recordToModify == null) {
            throw new HugoCharacterNotFoundByUuidException(recordUuid);
        }
        int recordId = recordToModify.getId();

        // Checking: Whether the new name is reserved
        if (hugoCharacterRepository.isNameReserved(request.getName(), recordId)) {
            throw new HugoCharacterNameIsReservedException(request.getName());
        }
        // Checking: Whether the new code is reserved
        if (hugoCharacterRepository.isCodeReserved(request.getCode(), recordId)) {
            throw new HugoCharacterCodeIsReservedException(request.getCode());
        }

        // Determine which variant is to use: current or the new one.
        HugoVariant hugoVariant = recordToModify.getVariant();
        if (hugoVariant == null) {
            throw new HugoVariantNotFoundException();
        }
        if (!hugoVariant.getUuid().equals(request.getVariantUuid())) {// If new value is sent from frontend

            // Access instance
            hugoVariant = hugoVariantRepository.findByUuid(request.getVariantUuid());
            if (hugoVariant == null) {
                throw new HugoVariantNotFoundByUuidException(request.getVariantUuid());
            }

            // Checking: Whether variant is enabled
            if (!hugoVariant.isEnabled()) {
                throw new HugoVariantAssociationRestrictedException(hugoVariant.getId(), hugoVariant.getName());
            }
        }

        // Modify lizCharacter
        recordToModify.setName(request.getName());
        recordToModify.setCode(request.getCode());
        recordToModify.setVariant(hugoVariant);
        recordToModify.setEnabled(request.isEnabled());
        recordToModify.setDateModified(LocalDateTime.now());
        HugoCharacter recordModified = uuidGenerator.saveWithRetry(hugoCharacterRepository, recordToModify);

        // Generate response
        return modelMapper.map(recordModified, HugoCharacterUpdateResponse.class);
    }

    public void deleteOneHugoCharacter(@NotBlank String recordUuid) {

        // Execution
        try {
            this.deleteOneRow(recordUuid);
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    public void deleteMultiple(@NotNull HugoCharacterDeleteRequest request) {

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
        HugoCharacter recordToDelete = hugoCharacterRepository.findByUuid(recordUuid);
        if (recordToDelete == null) {
            throw new HugoCharacterNotFoundByUuidException(recordUuid);
        }
        int recordId = recordToDelete.getId();

        // Check: whether any foreign key referencing this record
        if (hugoCharacterRepository.isReferencedElsewhere(recordId)) {
            throw new HugoCharacterReferencedElsewhereException(recordId, recordToDelete.getName());
        }

        // Delete item from db
        try {
            hugoCharacterRepository.deleteById(recordId);
        } catch (EntityNotFoundException e) {
            throw new HugoCharacterNotFoundException(recordId);
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////// OPTIONS /////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<HugoCharacterVariantOptionResponse> atSaveListVariant() {
        List<HugoVariant> variantList = hugoVariantRepository.listAllEnabled();
        return variantList.stream()
                .map(item -> modelMapper.map(item, HugoCharacterVariantOptionResponse.class))
                .collect(Collectors.toList());
    }

    public List<HugoCharacterVariantOptionResponse> atUpdateListVariant(@NotBlank String recordUuid) {

        // Access entity via repository
        HugoCharacter recordToFind = hugoCharacterRepository.findByUuid(recordUuid);
        if (recordToFind == null) {
            throw new HugoCharacterNotFoundByUuidException(recordUuid);
        }
        int variantIdToExclude = recordToFind.getVariant().getId();

        // Generate response
        List<HugoVariant> variantList = hugoVariantRepository.listAllEnabledWithOneExcludedId(variantIdToExclude);
        return variantList.stream()
                .map(item -> modelMapper.map(item, HugoCharacterVariantOptionResponse.class))
                .collect(Collectors.toList());
    }

}
