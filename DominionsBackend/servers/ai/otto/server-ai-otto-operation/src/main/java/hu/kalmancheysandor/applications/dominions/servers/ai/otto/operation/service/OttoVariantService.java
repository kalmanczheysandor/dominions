package hu.kalmancheysandor.applications.dominions.servers.ai.otto.operation.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.dto.variant.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.dto.variant.option.OttoVariantEvaluatorOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.evaluator.OttoOperatorScoreEvaluatorType;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.training.OttoVariant;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.evaluator.OttoEvaluatorNotFoundByCodeException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.evaluator.OttoEvaluatorNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.variant.OttoVariantNameIsReservedException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.variant.OttoVariantNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.variant.OttoVariantNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.variant.OttoVariantReferencedElsewhereException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.repository.training.OttoVariantRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.service.TOttoService;
import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class OttoVariantService extends TOttoService {

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

    private List<EvaluatorItem> evaluatorList = List.of(
            new EvaluatorItem(1, OttoOperatorScoreEvaluatorType.R1.toString(), "00000-R1", OttoOperatorScoreEvaluatorType.R1.toString()),
            new EvaluatorItem(2, OttoOperatorScoreEvaluatorType.R2.toString(), "00000-R2", OttoOperatorScoreEvaluatorType.R2.toString()),
            new EvaluatorItem(3, OttoOperatorScoreEvaluatorType.R3.toString(), "00000-R3", OttoOperatorScoreEvaluatorType.R3.toString())
    );

    public OttoVariantAccessResponse access(@NotBlank String recordUuid) {

        // Access entity via repository
        OttoVariant recordToAccess = ottoVariantRepository.findByUuid(recordUuid);
        if (recordToAccess == null) {
            throw new OttoVariantNotFoundByUuidException(recordUuid);
        }

        //
        EvaluatorItem evaluatorItem = findEvaluatorItemByCode(evaluatorList, recordToAccess.getEvaluatorCode());
        if (evaluatorItem == null) {
            throw new OttoEvaluatorNotFoundByCodeException(recordToAccess.getEvaluatorCode());
        }

        // Generate response
        return OttoVariantAccessResponse.builder()
                .uuid(recordToAccess.getUuid())
                .name(recordToAccess.getName())
                .evaluatorUuid(evaluatorItem.getUuid())
                .evaluatorName(evaluatorItem.getName())
                .enabled(recordToAccess.isEnabled())
                .dateCreated(recordToAccess.getDateCreated())
                .dateModified(recordToAccess.getDateModified())
                .build();
    }

    public List<OttoVariantItemResponse> listAll() {

        // Access entity via repository
        List<OttoVariant> recordList = ottoVariantRepository.findAll();

        // Generate response
        return recordList.stream()
                .map(item -> {

                            //
                            EvaluatorItem evaluatorItem = findEvaluatorItemByCode(evaluatorList, item.getEvaluatorCode());
                            if (evaluatorItem == null) {
                                throw new OttoEvaluatorNotFoundByCodeException(item.getEvaluatorCode());
                            }

                            //
                            return OttoVariantItemResponse.builder()
                                    .uuid(item.getUuid())
                                    .name(item.getName())
                                    .evaluatorUuid(item.getEvaluatorCode())
                                    .evaluatorName(evaluatorItem.getName())
                                    .enabled(item.isEnabled())
                                    .dateCreated(item.getDateCreated())
                                    .dateModified(item.getDateModified())
                                    .build();
                        }


                )
                .collect(Collectors.toList());
    }

    public OttoVariantCreateResponse save(@NotNull OttoVariantCreateRequest request) {

        // Checking: Whether the new name is reserved
        if (ottoVariantRepository.isNameReserved(request.getName())) {
            throw new OttoVariantNameIsReservedException(request.getName());
        }

        //
        EvaluatorItem evaluatorItem = findEvaluatorItemByUuid(evaluatorList, request.getEvaluatorUuid());
        if (evaluatorItem == null) {
            throw new OttoEvaluatorNotFoundByUuidException(request.getEvaluatorUuid());
        }

        // Save ottoPersonnel
        OttoVariant recordToSave = new OttoVariant();
        recordToSave.setName(request.getName());
        recordToSave.setEvaluatorCode(evaluatorItem.getCode());
        recordToSave.setEnabled(request.isEnabled());
        recordToSave.setDateCreated(LocalDateTime.now());
        OttoVariant recordSaved = uuidGenerator.saveWithRetry(ottoVariantRepository, recordToSave);

        // Generate response
        return OttoVariantCreateResponse.builder()
                .uuid(recordSaved.getUuid())
                .name(recordSaved.getName())
                .evaluatorUuid(evaluatorItem.getUuid())
                .evaluatorName(evaluatorItem.getName())
                .enabled(recordSaved.isEnabled())
                .dateCreated(recordSaved.getDateCreated())
                .build();
    }

    public OttoVariantUpdateResponse update(@NotBlank String recordUuid, @NotNull OttoVariantUpdateRequest request) {

        // Access entity via repository
        OttoVariant recordToModify = ottoVariantRepository.findByUuid(recordUuid);
        if (recordToModify == null) {
            throw new OttoVariantNotFoundByUuidException(recordUuid);
        }
        int recordId = recordToModify.getId();

        // Checking: Whether the new name is reserved
        if (ottoVariantRepository.isNameReserved(request.getName(), recordId)) {
            throw new OttoVariantNameIsReservedException(request.getName());
        }

        //
        EvaluatorItem evaluatorItem = findEvaluatorItemByUuid(evaluatorList, request.getEvaluatorUuid());
        if (evaluatorItem == null) {
            throw new OttoEvaluatorNotFoundByUuidException(request.getEvaluatorUuid());
        }


        // Modify ottoPersonnel
        recordToModify.setName(request.getName());
        recordToModify.setEvaluatorCode(evaluatorItem.getCode());
        recordToModify.setEnabled(request.isEnabled());
        recordToModify.setDateModified(LocalDateTime.now());
        OttoVariant recordModified = uuidGenerator.saveWithRetry(ottoVariantRepository, recordToModify);

        // Generate response
        return OttoVariantUpdateResponse.builder()
                .uuid(recordModified.getUuid())
                .name(recordModified.getName())
                .evaluatorUuid(evaluatorItem.getUuid())
                .evaluatorName(evaluatorItem.getName())
                .enabled(recordModified.isEnabled())
                .dateCreated(recordModified.getDateCreated())
                .dateModified(recordModified.getDateModified())
                .build();
    }

    public void deleteOneOttoPersonnel(@NotBlank String recordUuid) {

        // Execution
        try {
            this.deleteOneRow(recordUuid);
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    public void deleteMultiple(@NotNull OttoVariantDeleteRequest request) {

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
        OttoVariant recordToDelete = ottoVariantRepository.findByUuid(recordUuid);
        if (recordToDelete == null) {
            throw new OttoVariantNotFoundByUuidException(recordUuid);
        }
        int recordId = recordToDelete.getId();

        // Check: whether any foreign key referencing this record
        if (ottoVariantRepository.isReferencedElsewhere(recordId)) {
            throw new OttoVariantReferencedElsewhereException(recordId, recordToDelete.getName());
        }

        // Delete item from db
        try {
            ottoVariantRepository.deleteById(recordId);
        } catch (EntityNotFoundException e) {
            throw new OttoVariantNotFoundException(recordId);
        }
    }

    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////// OPTIONS /////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<OttoVariantEvaluatorOptionResponse> atSaveListEvaluator() {

        // Generate response
        return evaluatorList.stream()
                .map(item -> modelMapper.map(item, OttoVariantEvaluatorOptionResponse.class))
                .collect(Collectors.toList());
    }

    public List<OttoVariantEvaluatorOptionResponse> atUpdateListEvaluator(@NotBlank String recordUuid) {
        // Generate response
        return evaluatorList.stream()
                .map(item -> modelMapper.map(item, OttoVariantEvaluatorOptionResponse.class))
                .collect(Collectors.toList());
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class EvaluatorItem {
        private int id;
        private String code;
        private String uuid;
        private String name;


        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            EvaluatorItem that = (EvaluatorItem) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }
    }


    private EvaluatorItem findEvaluatorItemByCode(List<EvaluatorItem> evaluatorList, String evaluatorCode) {
        for (EvaluatorItem evaluatorItem : evaluatorList) {
            if (evaluatorItem.getCode().equals(evaluatorCode)) {
                return evaluatorItem;
            }
        }
        return null;
    }

    private EvaluatorItem findEvaluatorItemByUuid(List<EvaluatorItem> evaluatorList, String evaluatorUuid) {
        for (EvaluatorItem evaluatorItem : evaluatorList) {
            if (evaluatorItem.getUuid().equals(evaluatorUuid)) {
                return evaluatorItem;
            }
        }
        return null;
    }

}
