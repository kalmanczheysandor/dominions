package hu.kalmancheysandor.applications.dominions.servers.ai.hugo.operation.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.variant.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.variant.option.HugoVariantHeuristicOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.heuristic.HugoHeuristicEvaluatorType;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.training.HugoVariant;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.heuristic.HugoHeuristicNotFoundByCodeException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.heuristic.HugoHeuristicNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.variant.HugoVariantNameIsReservedException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.variant.HugoVariantNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.variant.HugoVariantNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.variant.HugoVariantReferencedElsewhereException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.repository.training.HugoVariantRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.service.THugoService;
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
public class HugoVariantService extends THugoService {


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

    private List<HeuristicItem> heuristicList = List.of(
            new HeuristicItem(1, HugoHeuristicEvaluatorType.HUGO_FIRST_HEURISTIC_EVALUATOR.toString(), "00000-h1", HugoHeuristicEvaluatorType.HUGO_FIRST_HEURISTIC_EVALUATOR.toString()),
            new HeuristicItem(2, HugoHeuristicEvaluatorType.HUGO_SECOND_HEURISTIC_EVALUATOR.toString(), "00000-h2", HugoHeuristicEvaluatorType.HUGO_SECOND_HEURISTIC_EVALUATOR.toString())
    );

    public HugoVariantAccessResponse access(@NotBlank String recordUuid) {

        // Access entity via repository
        HugoVariant recordToAccess = hugoVariantRepository.findByUuid(recordUuid);
        if (recordToAccess == null) {
            throw new HugoVariantNotFoundByUuidException(recordUuid);
        }

        //
        HeuristicItem heuristicItem = findHeuristicItemByCode(heuristicList, recordToAccess.getHeuristicCode());
        if (heuristicItem == null) {
            throw new HugoHeuristicNotFoundByCodeException(recordToAccess.getHeuristicCode());
        }

        // Generate response
        return HugoVariantAccessResponse.builder()
                .uuid(recordToAccess.getUuid())
                .name(recordToAccess.getName())
                .confSearchDepth(recordToAccess.getConfSearchDepth())
                .heuristicUuid(heuristicItem.getUuid())
                .heuristicName(heuristicItem.getName())
                .enabled(recordToAccess.isEnabled())
                .dateCreated(recordToAccess.getDateCreated())
                .dateModified(recordToAccess.getDateModified())
                .build();
    }

    public List<HugoVariantItemResponse> listAll() {

        // Access entity via repository
        List<HugoVariant> recordList = hugoVariantRepository.findAll();

        // Generate response
        return recordList.stream()
                .map(item -> {

                            //
                            HeuristicItem heuristicItem = findHeuristicItemByCode(heuristicList, item.getHeuristicCode());
                            if (heuristicItem == null) {
                                throw new HugoHeuristicNotFoundByCodeException(item.getHeuristicCode());
                            }

                            //
                            return HugoVariantItemResponse.builder()
                                    .uuid(item.getUuid())
                                    .name(item.getName())
                                    .confSearchDepth(item.getConfSearchDepth())
                                    .heuristicUuid(item.getHeuristicCode())
                                    .heuristicName(heuristicItem.getName())
                                    .enabled(item.isEnabled())
                                    .dateCreated(item.getDateCreated())
                                    .dateModified(item.getDateModified())
                                    .build();
                        }


                )
                .collect(Collectors.toList());
    }

    public HugoVariantCreateResponse save(@NotNull HugoVariantCreateRequest request) {

        // Checking: Whether the new name is reserved
        if (hugoVariantRepository.isNameReserved(request.getName())) {
            throw new HugoVariantNameIsReservedException(request.getName());
        }

        //
        HeuristicItem heuristicItem = findHeuristicItemByUuid(heuristicList, request.getHeuristicUuid());
        if (heuristicItem == null) {
            throw new HugoHeuristicNotFoundByUuidException(request.getHeuristicUuid());
        }

        // Save hugoPersonnel
        HugoVariant recordToSave = new HugoVariant();
        recordToSave.setName(request.getName());
        recordToSave.setConfSearchDepth(request.getConfSearchDepth());
        recordToSave.setHeuristicCode(heuristicItem.getCode());
        recordToSave.setEnabled(request.isEnabled());
        recordToSave.setDateCreated(LocalDateTime.now());
        HugoVariant recordSaved = uuidGenerator.saveWithRetry(hugoVariantRepository, recordToSave);

        // Generate response
        return HugoVariantCreateResponse.builder()
                .uuid(recordSaved.getUuid())
                .name(recordSaved.getName())
                .confSearchDepth(recordSaved.getConfSearchDepth())
                .heuristicUuid(heuristicItem.getUuid())
                .heuristicName(heuristicItem.getName())
                .enabled(recordSaved.isEnabled())
                .dateCreated(recordSaved.getDateCreated())
                .build();
    }

    public HugoVariantUpdateResponse update(@NotBlank String recordUuid, @NotNull HugoVariantUpdateRequest request) {

        // Access entity via repository
        HugoVariant recordToModify = hugoVariantRepository.findByUuid(recordUuid);
        if (recordToModify == null) {
            throw new HugoVariantNotFoundByUuidException(recordUuid);
        }
        int recordId = recordToModify.getId();

        // Checking: Whether the new name is reserved
        if (hugoVariantRepository.isNameReserved(request.getName(), recordId)) {
            throw new HugoVariantNameIsReservedException(request.getName());
        }

        //
        HeuristicItem heuristicItem = findHeuristicItemByUuid(heuristicList, request.getHeuristicUuid());
        if (heuristicItem == null) {
            throw new HugoHeuristicNotFoundByUuidException(request.getHeuristicUuid());
        }


        // Modify hugoPersonnel
        recordToModify.setName(request.getName());
        recordToModify.setConfSearchDepth(request.getConfSearchDepth());
        recordToModify.setHeuristicCode(heuristicItem.getCode());
        recordToModify.setEnabled(request.isEnabled());
        recordToModify.setDateModified(LocalDateTime.now());
        HugoVariant recordModified = uuidGenerator.saveWithRetry(hugoVariantRepository, recordToModify);

        // Generate response
        return HugoVariantUpdateResponse.builder()
                .uuid(recordModified.getUuid())
                .name(recordModified.getName())
                .confSearchDepth(recordModified.getConfSearchDepth())
                .heuristicUuid(heuristicItem.getUuid())
                .heuristicName(heuristicItem.getName())
                .enabled(recordModified.isEnabled())
                .dateCreated(recordModified.getDateCreated())
                .dateModified(recordModified.getDateModified())
                .build();
    }

    public void deleteOneHugoPersonnel(@NotBlank String recordUuid) {

        // Execution
        try {
            this.deleteOneRow(recordUuid);
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    public void deleteMultiple(@NotNull HugoVariantDeleteRequest request) {

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
        HugoVariant recordToDelete = hugoVariantRepository.findByUuid(recordUuid);
        if (recordToDelete == null) {
            throw new HugoVariantNotFoundByUuidException(recordUuid);
        }
        int recordId = recordToDelete.getId();

        // Check: whether any foreign key referencing this record
        if (hugoVariantRepository.isReferencedElsewhere(recordId)) {
            throw new HugoVariantReferencedElsewhereException(recordId, recordToDelete.getName());
        }

        // Delete item from db
        try {
            hugoVariantRepository.deleteById(recordId);
        } catch (EntityNotFoundException e) {
            throw new HugoVariantNotFoundException(recordId);
        }
    }

    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////// OPTIONS /////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<HugoVariantHeuristicOptionResponse> atSaveListHeuristic() {

        // Generate response
        return heuristicList.stream()
                .map(item -> modelMapper.map(item, HugoVariantHeuristicOptionResponse.class))
                .collect(Collectors.toList());
    }

    public List<HugoVariantHeuristicOptionResponse> atUpdateListHeuristic(@NotBlank String recordUuid) {
        // Generate response
        return heuristicList.stream()
                .map(item -> modelMapper.map(item, HugoVariantHeuristicOptionResponse.class))
                .collect(Collectors.toList());
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class HeuristicItem {
        private int id;
        private String code;
        private String uuid;
        private String name;


        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            HeuristicItem that = (HeuristicItem) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }
    }


    private HeuristicItem findHeuristicItemByCode(List<HeuristicItem> heuristicList, String heuristicCode) {
        for (HeuristicItem heuristicItem : heuristicList) {
            if (heuristicItem.getCode().equals(heuristicCode)) {
                return heuristicItem;
            }
        }
        return null;
    }

    private HeuristicItem findHeuristicItemByUuid(List<HeuristicItem> heuristicList, String heuristicUuid) {
        for (HeuristicItem heuristicItem : heuristicList) {
            if (heuristicItem.getUuid().equals(heuristicUuid)) {
                return heuristicItem;
            }
        }
        return null;
    }

}
