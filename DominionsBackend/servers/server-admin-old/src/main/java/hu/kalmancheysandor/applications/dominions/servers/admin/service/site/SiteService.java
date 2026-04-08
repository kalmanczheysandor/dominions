package hu.kalmancheysandor.applications.dominions.service.site;


import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.site.*;
import hu.kalmancheysandor.applications.dominions.servers.admin.entity.site.Site;
import hu.kalmancheysandor.applications.dominions.servers.admin.repository.site.SiteRepository;
import hu.kalmancheysandor.applications.dominions.service.site.exception.SiteNameIsReservedException;
import hu.kalmancheysandor.applications.dominions.service.site.exception.SiteNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.service.site.exception.SiteNotFoundException;
import hu.kalmancheysandor.applications.dominions.service.site.exception.SiteReferencedElsewhereException;
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
public class SiteService {

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UUIDGenerator uuidGenerator;


    public SiteResponse accessSite(@NotBlank String uuid) {

        // Access entity via repository
        Site site = siteRepository.findByUuid(uuid);
        if (site == null) {
            throw new SiteNotFoundByUuidException(uuid);
        }

        // Generate response
        SiteResponse response = modelMapper.map(site, SiteResponse.class);
        return response;
    }

    public SiteCreateResponse saveSite(@NotNull SiteCreateRequest request) {

        // Checking: Whether the new name field is reserved
        if (siteRepository.isNameReserved(request.getName())) {
            throw new SiteNameIsReservedException(request.getName());
        }

        // Save site
        Site siteToSave = new Site();
        siteToSave.setName(request.getName());
        siteToSave.setAddress(request.getAddress());
        siteToSave.setNote(request.getNote());
        siteToSave.setEnabled(request.isEnabled());
        Site siteSaved = uuidGenerator.saveWithRetry(siteRepository, siteToSave);

        // Generate response
        return modelMapper.map(siteSaved, SiteCreateResponse.class);
    }

    public SiteUpdateResponse updateSite(@NotBlank String uuid, @NotNull SiteUpdateRequest request) {

        // Access entity via repository
        Site siteToModify = siteRepository.findByUuid(uuid);
        if (siteToModify == null) {
            throw new SiteNotFoundByUuidException(uuid);
        }
        int siteId = siteToModify.getId();

        // Checking: Whether the new identifier is reserved
        if (siteRepository.isNameReserved(request.getName(), siteId)) {
            throw new SiteNameIsReservedException(request.getName());
        }


        // Set simple properties
        siteToModify.setName(request.getName());
        siteToModify.setAddress(request.getAddress());
        siteToModify.setNote(request.getNote());
        siteToModify.setEnabled(request.isEnabled());
        Site siteModified = uuidGenerator.saveWithRetry(siteRepository, siteToModify);

        // Generate response
        return modelMapper.map(siteModified, SiteUpdateResponse.class);
    }

    public List<SiteResponse> listAllSite() {

        // Access entity via repository
        List<Site> siteList = siteRepository.findAll();

        // Generate response
        return siteList.stream()
            .map(item -> modelMapper.map(item, SiteResponse.class))
            .collect(Collectors.toList());
    }

    public void deleteOneSite(@NotBlank String uuid) {

        // Execution
        try {
            this.deleteOneRow(uuid);
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    public void deleteMultipleSite(@NotNull SiteDeleteRequest request) {

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
        Site siteToDelete = siteRepository.findByUuid(uuid);
        if (siteToDelete == null) {
            throw new SiteNotFoundByUuidException(uuid);
        }
        int siteId = siteToDelete.getId();

        // Check: whether any foreign key still referencing this record
        if (siteRepository.isReferencedElsewhere(siteId)) {
            throw new SiteReferencedElsewhereException(siteId, siteToDelete.getName());
        }

        // Delete item from db
        try {
            siteRepository.deleteById(siteId);
        } catch (EntityNotFoundException e) {
            throw new SiteNotFoundException(siteId);
        }
    }
}