package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.service;


import com.fasterxml.jackson.databind.ObjectMapper;

import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.variant.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.variant.option.LizVariantConceptOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralConcept;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.LizVariant;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.LizNeuralConceptAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.LizNeuralConceptNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.LizNeuralConceptNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.variant.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.LizNeuralConceptRepository;
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
public class LizVariantService extends TLizService {


    @Autowired
    private LizVariantRepository lizVariantRepository;
    @Autowired
    private LizNeuralConceptRepository lizConceptRepository;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationConfig applicationConfig;


    public LizVariantAccessResponse access(@NotBlank String recordUuid) {

        // Access entity via repository
        LizVariant recordToAccess = lizVariantRepository.findByUuid(recordUuid);
        if (recordToAccess == null) {
            throw new LizVariantNotFoundByUuidException(recordUuid);
        }

        // Generate response
        return modelMapper.map(recordToAccess, LizVariantAccessResponse.class);
    }

    public List<LizVariantItemResponse> listAll() {

        // Access entity via repository
        List<LizVariant> recordList = lizVariantRepository.findAll();

        // Generate response
        return recordList.stream()
                .map(item -> modelMapper.map(item, LizVariantItemResponse.class))
                .collect(Collectors.toList());
    }

    public LizVariantCreateResponse save(@NotNull LizVariantCreateRequest request) {

        // Checking: Whether the new name is reserved
        if (lizVariantRepository.isNameReserved(request.getName())) {
            throw new LizVariantNameIsReservedException(request.getName());
        }

        // Checking: Whether concept is exist
        LizNeuralConcept lizNeuralConcept = lizConceptRepository.findByUuid(request.getConceptUuid());
        if (lizNeuralConcept == null) {
            throw new LizNeuralConceptNotFoundByUuidException(request.getConceptUuid());
        }

        // Checking: Whether concept is enabled
        if (!lizNeuralConcept.isEnabled()) {
            throw new LizNeuralConceptAssociationRestrictedException(lizNeuralConcept.getId(), lizNeuralConcept.getName());
        }

        // Save lizPersonnel
        LizVariant recordToSave = new LizVariant();
        recordToSave.setName(request.getName());
        recordToSave.setConfSearchDepth(request.getConfSearchDepth());
        recordToSave.setConcept(lizNeuralConcept);
        recordToSave.setEnabled(request.isEnabled());
        recordToSave.setDateCreated(LocalDateTime.now());
        LizVariant recordSaved = uuidGenerator.saveWithRetry(lizVariantRepository, recordToSave);

        // Generate response
        return modelMapper.map(recordSaved, LizVariantCreateResponse.class);
    }

    public LizVariantUpdateResponse update(@NotBlank String recordUuid, @NotNull LizVariantUpdateRequest request) {

        // Access entity via repository
        LizVariant recordToModify = lizVariantRepository.findByUuid(recordUuid);
        if (recordToModify == null) {
            throw new LizVariantNotFoundByUuidException(recordUuid);
        }
        int recordId = recordToModify.getId();

        // Checking: Whether the new name is reserved
        if (lizVariantRepository.isNameReserved(request.getName(), recordId)) {
            throw new LizVariantNameIsReservedException(request.getName());
        }

        // Determine which concept is to use: current or the new one.
        LizNeuralConcept lizNeuralConcept = recordToModify.getConcept();
        if (lizNeuralConcept == null) {
            throw new LizNeuralConceptNotFoundException();
        }
        if (!lizNeuralConcept.getUuid().equals(request.getConceptUuid())) {// If new value is sent from frontend

            // Access instance
            lizNeuralConcept = lizConceptRepository.findByUuid(request.getConceptUuid());
            if (lizNeuralConcept == null) {
                throw new LizNeuralConceptNotFoundByUuidException(request.getConceptUuid());
            }

            // Checking: Whether variant is enabled
            if (!lizNeuralConcept.isEnabled()) {
                throw new LizNeuralConceptAssociationRestrictedException(lizNeuralConcept.getId(), lizNeuralConcept.getName());
            }
        }

        // Modify lizPersonnel
        recordToModify.setName(request.getName());
        recordToModify.setConfSearchDepth(request.getConfSearchDepth());
        recordToModify.setConcept(lizNeuralConcept);
        recordToModify.setEnabled(request.isEnabled());
        recordToModify.setDateModified(LocalDateTime.now());
        LizVariant recordModified = uuidGenerator.saveWithRetry(lizVariantRepository, recordToModify);

        // Generate response
        return modelMapper.map(recordModified, LizVariantUpdateResponse.class);
    }

    public void deleteOneLizPersonnel(@NotBlank String recordUuid) {

        // Execution
        try {
            this.deleteOneRow(recordUuid);
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    public void deleteMultiple(@NotNull LizVariantDeleteRequest request) {

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
        LizVariant recordToDelete = lizVariantRepository.findByUuid(recordUuid);
        if (recordToDelete == null) {
            throw new LizVariantNotFoundByUuidException(recordUuid);
        }
        int recordId = recordToDelete.getId();

        // Check: whether any foreign key referencing this record
        if (lizVariantRepository.isReferencedElsewhere(recordId)) {
            throw new LizVariantReferencedElsewhereException(recordId, recordToDelete.getName());
        }

        // Delete item from db
        try {
            lizVariantRepository.deleteById(recordId);
        } catch (EntityNotFoundException e) {
            throw new LizVariantNotFoundException(recordId);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////// OPTIONS /////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<LizVariantConceptOptionResponse> atSaveListConcept() {
        List<LizNeuralConcept> conceptList = lizConceptRepository.listAllEnabled();
        return conceptList.stream()
                .map(item -> modelMapper.map(item, LizVariantConceptOptionResponse.class))
                .collect(Collectors.toList());
    }

    public List<LizVariantConceptOptionResponse> atUpdateListVariant(@NotBlank String recordUuid) {

        // Access  entity via repository
        LizVariant recordToFind = lizVariantRepository.findByUuid(recordUuid);
        if (recordToFind == null) {
            throw new LizVariantNotFoundByUuidException(recordUuid);
        }
        int conceptIdToExclude = recordToFind.getConcept().getId();

        // Generate response
        List<LizNeuralConcept> variantList = lizConceptRepository.listAllEnabledWithOneExcludedId(conceptIdToExclude);
        return variantList.stream()
                .map(item -> modelMapper.map(item, LizVariantConceptOptionResponse.class))
                .collect(Collectors.toList());
    }


}
