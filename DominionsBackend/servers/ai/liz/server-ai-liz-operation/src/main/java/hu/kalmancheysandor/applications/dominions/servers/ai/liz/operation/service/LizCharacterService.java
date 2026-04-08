package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.character.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.character.option.LizCharacterVariantOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.LizCharacter;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.LizVariant;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.character.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.variant.LizVariantAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.variant.LizVariantNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.variant.LizVariantNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.LizCharacterRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.LizVariantRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.service.TLizService;
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
public class LizCharacterService extends TLizService {


    @Autowired
    private LizCharacterRepository lizCharacterRepository;
    
    @Autowired
    private LizVariantRepository lizVariantRepository;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationConfig applicationConfig;


    public LizCharacterAccessResponse access(@NotBlank String recordUuid) {

        // Access entity via repository
        LizCharacter recordToAccess = lizCharacterRepository.findByUuid(recordUuid);
        if (recordToAccess == null) {
            throw new LizCharacterNotFoundByUuidException(recordUuid);
        }

        // Generate response
        return modelMapper.map(recordToAccess, LizCharacterAccessResponse.class);
    }

    public List<LizCharacterItemResponse> listAll() {

        // Access entity via repository
        List<LizCharacter> recordList = lizCharacterRepository.findAll();

        // Generate response
        return recordList.stream()
                .map(item -> modelMapper.map(item, LizCharacterItemResponse.class))
                .collect(Collectors.toList());
    }

    public LizCharacterCreateResponse save(@NotNull LizCharacterCreateRequest request) {

        // Checking: Whether the new name is reserved
        if (lizCharacterRepository.isNameReserved(request.getName())) {
            throw new LizCharacterNameIsReservedException(request.getName());
        }

        // Checking: Whether the new code is reserved
        if (lizCharacterRepository.isCodeReserved(request.getCode())) {
            throw new LizCharacterCodeIsReservedException(request.getCode());
        }


        // Checking: Whether variant is exist
        LizVariant lizVariant = lizVariantRepository.findByUuid(request.getVariantUuid());
        if (lizVariant == null) {
            throw new LizVariantNotFoundByUuidException(request.getVariantUuid());
        }

        // Checking: Whether variant is enabled
        if (!lizVariant.isEnabled()) {
            throw new LizVariantAssociationRestrictedException(lizVariant.getId(), lizVariant.getName());
        }
        
        
        // Save lizCharacter
        LizCharacter recordToSave = new LizCharacter();
        recordToSave.setName(request.getName());
        recordToSave.setCode(request.getCode());
        recordToSave.setVariant(lizVariant);
        recordToSave.setEnabled(request.isEnabled());
        recordToSave.setDateCreated(LocalDateTime.now());
        LizCharacter recordSaved = uuidGenerator.saveWithRetry(lizCharacterRepository, recordToSave);

        // Generate response
        return modelMapper.map(recordSaved, LizCharacterCreateResponse.class);
    }

    public LizCharacterUpdateResponse update(@NotBlank String recordUuid, @NotNull LizCharacterUpdateRequest request) {

        // Access entity via repository
        LizCharacter recordToModify = lizCharacterRepository.findByUuid(recordUuid);
        if (recordToModify == null) {
            throw new LizCharacterNotFoundByUuidException(recordUuid);
        }
        int recordId = recordToModify.getId();

        // Checking: Whether the new name is reserved
        if (lizCharacterRepository.isNameReserved(request.getName(), recordId)) {
            throw new LizCharacterNameIsReservedException(request.getName());
        }
        // Checking: Whether the new code is reserved
        if (lizCharacterRepository.isCodeReserved(request.getCode(), recordId)) {
            throw new LizCharacterCodeIsReservedException(request.getCode());
        }

        // Determine which variant is to use: current or the new one.
        LizVariant lizVariant = recordToModify.getVariant();
        if (lizVariant == null) {
            throw new LizVariantNotFoundException();
        }
        if (!lizVariant.getUuid().equals(request.getVariantUuid())) {// If new value is sent from frontend

            // Access instance
            lizVariant = lizVariantRepository.findByUuid(request.getVariantUuid());
            if (lizVariant == null) {
                throw new LizVariantNotFoundByUuidException(request.getVariantUuid());
            }

            // Checking: Whether variant is enabled
            if (!lizVariant.isEnabled()) {
                throw new LizVariantAssociationRestrictedException(lizVariant.getId(), lizVariant.getName());
            }
        }

        // Modify lizCharacter
        recordToModify.setName(request.getName());
        recordToModify.setCode(request.getCode());
        recordToModify.setVariant(lizVariant);
        recordToModify.setEnabled(request.isEnabled());
        recordToModify.setDateModified(LocalDateTime.now());
        LizCharacter recordModified = uuidGenerator.saveWithRetry(lizCharacterRepository, recordToModify);

        // Generate response
        return modelMapper.map(recordModified, LizCharacterUpdateResponse.class);
    }

    public void deleteOneLizPersonnel(@NotBlank String recordUuid) {

        // Execution
        try {
            this.deleteOneRow(recordUuid);
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    public void deleteMultiple(@NotNull LizCharacterDeleteRequest request) {

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
        LizCharacter recordToDelete = lizCharacterRepository.findByUuid(recordUuid);
        if (recordToDelete == null) {
            throw new LizCharacterNotFoundByUuidException(recordUuid);
        }
        int recordId = recordToDelete.getId();

        // Check: whether any foreign key referencing this record
        if (lizCharacterRepository.isReferencedElsewhere(recordId)) {
            throw new LizCharacterReferencedElsewhereException(recordId, recordToDelete.getName());
        }

        // Delete item from db
        try {
            lizCharacterRepository.deleteById(recordId);
        } catch (EntityNotFoundException e) {
            throw new LizCharacterNotFoundException(recordId);
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////// OPTIONS /////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<LizCharacterVariantOptionResponse> atSaveListVariant() {
        List<LizVariant> variantList = lizVariantRepository.listAllEnabled();
        return variantList.stream()
                .map(item -> modelMapper.map(item, LizCharacterVariantOptionResponse.class))
                .collect(Collectors.toList());
    }

    public List<LizCharacterVariantOptionResponse> atUpdateListVariant(@NotBlank String recordUuid) {

        // Access entity via repository
        LizCharacter recordToFind = lizCharacterRepository.findByUuid(recordUuid);
        if (recordToFind == null) {
            throw new LizCharacterNotFoundByUuidException(recordUuid);
        }
        int variantIdToExclude = recordToFind.getVariant().getId();

        // Generate response
        List<LizVariant> variantList = lizVariantRepository.listAllEnabledWithOneExcludedId(variantIdToExclude);
        return variantList.stream()
                .map(item -> modelMapper.map(item, LizCharacterVariantOptionResponse.class))
                .collect(Collectors.toList());
    }

}
