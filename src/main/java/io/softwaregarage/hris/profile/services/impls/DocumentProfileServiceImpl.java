package io.softwaregarage.hris.profile.services.impls;

import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.profile.dtos.DocumentProfileDTO;
import io.softwaregarage.hris.profile.entities.EmployeeProfile;
import io.softwaregarage.hris.profile.entities.DocumentProfile;
import io.softwaregarage.hris.profile.repositories.EmployeeProfileRepository;
import io.softwaregarage.hris.profile.repositories.DocumentProfileRepository;
import io.softwaregarage.hris.profile.services.DocumentProfileService;
import io.softwaregarage.hris.profile.services.EmployeeProfileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentProfileServiceImpl implements DocumentProfileService {
    private final Logger logger = LoggerFactory.getLogger(DocumentProfileServiceImpl.class);

    private final DocumentProfileRepository documentProfileRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    public DocumentProfileServiceImpl(DocumentProfileRepository documentProfileRepository,
                                      EmployeeProfileRepository employeeProfileRepository) {
        this.documentProfileRepository = documentProfileRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    @Override
    public void saveOrUpdate(DocumentProfileDTO object) {
        DocumentProfile documentProfile;
        String logMessage;

        if (object.getId() != null) {
            documentProfile = documentProfileRepository.getReferenceById(object.getId());
            logMessage = "Employee's requirement record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            documentProfile = new DocumentProfile();
            documentProfile.setCreatedBy(object.getCreatedBy());
            documentProfile.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's requirement record is successfully created.";
        }

        documentProfile.setEmployee(employeeProfileRepository.getReferenceById(object.getEmployeeDTO().getId()));
        documentProfile.setDocumentType(object.getDocumentType());
        documentProfile.setFileName(object.getFileName());
        documentProfile.setFileData(object.getFileData());
        documentProfile.setFileType(object.getFileType());
        documentProfile.setRemarks(object.getRemarks());
        documentProfile.setExpirationDate(object.getExpirationDate());
        documentProfile.setUpdatedBy(object.getUpdatedBy());
        documentProfile.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        documentProfileRepository.save(documentProfile);
    }

    @Override
    public DocumentProfileDTO getById(UUID id) {
        logger.info("Retrieving employee's requirement record with UUID ".concat(id.toString()));

        DocumentProfile documentProfile = documentProfileRepository.getReferenceById(id);
        DocumentProfileDTO documentProfileDTO = new DocumentProfileDTO();

        documentProfileDTO.setId(documentProfile.getId());
        documentProfileDTO.setEmployeeDTO(new EmployeeProfileServiceImpl(employeeProfileRepository).getById(documentProfile.getEmployee().getId()));
        documentProfileDTO.setDocumentType(documentProfile.getDocumentType());
        documentProfileDTO.setFileName(documentProfile.getFileName());
        documentProfileDTO.setFileData(documentProfile.getFileData());
        documentProfileDTO.setFileType(documentProfile.getFileType());
        documentProfileDTO.setRemarks(documentProfile.getRemarks());
        documentProfileDTO.setExpirationDate(documentProfile.getExpirationDate());
        documentProfileDTO.setCreatedBy(documentProfile.getCreatedBy());
        documentProfileDTO.setDateAndTimeCreated(documentProfile.getDateAndTimeCreated());
        documentProfileDTO.setUpdatedBy(documentProfile.getUpdatedBy());
        documentProfileDTO.setDateAndTimeUpdated(documentProfile.getDateAndTimeUpdated());

        logger.info("Employee's requirement record with id ".concat(id.toString()).concat(" is successfully retrieved."));

        return documentProfileDTO;
    }

    @Override
    public void delete(DocumentProfileDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the employee's requirement record permanently.");

            String id = object.getId().toString();
            DocumentProfile documentProfile = documentProfileRepository.getReferenceById(object.getId());
            documentProfileRepository.delete(documentProfile);

            logger.info("Employee's requirement record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<DocumentProfileDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee's requirement records from the database.");
        List<DocumentProfile> documentProfileList = documentProfileRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee's requirements records successfully retrieved.");
        List<DocumentProfileDTO> documentProfileDTOList = new ArrayList<>();

        if (!documentProfileList.isEmpty()) {
            EmployeeProfileService employeeProfileService = new EmployeeProfileServiceImpl(employeeProfileRepository);

            for (DocumentProfile documentProfile : documentProfileList) {
                DocumentProfileDTO documentProfileDTO = new DocumentProfileDTO();

                documentProfileDTO.setId(documentProfile.getId());
                documentProfileDTO.setEmployeeDTO(employeeProfileService.getById(documentProfile.getEmployee().getId()));
                documentProfileDTO.setDocumentType(documentProfile.getDocumentType());
                documentProfileDTO.setFileName(documentProfile.getFileName());
                documentProfileDTO.setFileData(documentProfile.getFileData());
                documentProfileDTO.setFileType(documentProfile.getFileType());
                documentProfileDTO.setRemarks(documentProfile.getRemarks());
                documentProfileDTO.setExpirationDate(documentProfile.getExpirationDate());
                documentProfileDTO.setCreatedBy(documentProfile.getCreatedBy());
                documentProfileDTO.setDateAndTimeCreated(documentProfile.getDateAndTimeCreated());
                documentProfileDTO.setUpdatedBy(documentProfile.getUpdatedBy());
                documentProfileDTO.setDateAndTimeUpdated(documentProfile.getDateAndTimeUpdated());

                documentProfileDTOList.add(documentProfileDTO);
            }

            logger.info(String.valueOf(documentProfileList.size()).concat(" record(s) found."));
        }

        return documentProfileDTOList;
    }

    @Override
    public List<DocumentProfileDTO> findByParameter(String param) {
        return List.of();
    }

    @Override
    public List<DocumentProfileDTO> getByEmployeeDTO(EmployeeProfileDTO employeeProfileDTO) {
        logger.info("Retrieving requirement's records with employee UUID ".concat(employeeProfileDTO.getId().toString()));

        EmployeeProfile employeeProfile = employeeProfileRepository.getReferenceById(employeeProfileDTO.getId());

        List<DocumentProfile> documentProfileList = documentProfileRepository.getByEmployee(employeeProfile);
        List<DocumentProfileDTO> documentProfileDTOList = new ArrayList<>();

        if (!documentProfileList.isEmpty()) {
            EmployeeProfileService employeeProfileService = new EmployeeProfileServiceImpl(employeeProfileRepository);

            for (DocumentProfile documentProfile : documentProfileList) {
                DocumentProfileDTO documentProfileDTO = new DocumentProfileDTO();

                documentProfileDTO.setId(documentProfile.getId());
                documentProfileDTO.setEmployeeDTO(employeeProfileService.getById(documentProfile.getEmployee().getId()));
                documentProfileDTO.setDocumentType(documentProfile.getDocumentType());
                documentProfileDTO.setFileName(documentProfile.getFileName());
                documentProfileDTO.setFileData(documentProfile.getFileData());
                documentProfileDTO.setFileType(documentProfile.getFileType());
                documentProfileDTO.setRemarks(documentProfile.getRemarks());
                documentProfileDTO.setExpirationDate(documentProfile.getExpirationDate());
                documentProfileDTO.setCreatedBy(documentProfile.getCreatedBy());
                documentProfileDTO.setDateAndTimeCreated(documentProfile.getDateAndTimeCreated());
                documentProfileDTO.setUpdatedBy(documentProfile.getUpdatedBy());
                documentProfileDTO.setDateAndTimeUpdated(documentProfile.getDateAndTimeUpdated());

                documentProfileDTOList.add(documentProfileDTO);
            }

            logger.info(String.valueOf(documentProfileList.size()).concat(" record(s) found."));
        }

        return documentProfileDTOList;
    }

    @Override
    public DocumentProfileDTO getIDPictureByEmployeeDTO(EmployeeProfileDTO employeeProfileDTO) {
        DocumentProfile documentProfile = documentProfileRepository.getIDPictureByEmployeeProfile(employeeProfileRepository.getReferenceById(employeeProfileDTO.getId()));
        DocumentProfileDTO documentProfileDTO;

        if (documentProfile != null) {
            documentProfileDTO = new  DocumentProfileDTO();
            documentProfileDTO.setId(documentProfile.getId());
            documentProfileDTO.setDocumentType(documentProfile.getDocumentType());
            documentProfileDTO.setFileName(documentProfile.getFileName());
            documentProfileDTO.setFileData(documentProfile.getFileData());
            documentProfileDTO.setFileType(documentProfile.getFileType());
            documentProfileDTO.setRemarks(documentProfile.getRemarks());
            documentProfileDTO.setExpirationDate(documentProfile.getExpirationDate());
            documentProfileDTO.setCreatedBy(documentProfile.getCreatedBy());
            documentProfileDTO.setDateAndTimeCreated(documentProfile.getDateAndTimeCreated());
            documentProfileDTO.setUpdatedBy(documentProfile.getUpdatedBy());
            documentProfileDTO.setDateAndTimeUpdated(documentProfile.getDateAndTimeUpdated());
        } else {
            documentProfileDTO = null;
        }

        return documentProfileDTO;
    }
}
