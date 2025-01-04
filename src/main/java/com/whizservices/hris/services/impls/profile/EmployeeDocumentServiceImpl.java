package com.whizservices.hris.services.impls.profile;

import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.dtos.profile.EmployeeDocumentDTO;
import com.whizservices.hris.entities.profile.Employee;
import com.whizservices.hris.entities.profile.EmployeeDocument;
import com.whizservices.hris.repositories.profile.EmployeeRepository;
import com.whizservices.hris.repositories.profile.EmployeeDocumentRepository;
import com.whizservices.hris.services.profile.EmployeeDocumentService;
import com.whizservices.hris.services.profile.EmployeeService;

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
public class EmployeeDocumentServiceImpl implements EmployeeDocumentService {
    private final Logger logger = LoggerFactory.getLogger(EmployeeDocumentServiceImpl.class);

    private final EmployeeDocumentRepository employeeDocumentRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeDocumentServiceImpl(EmployeeDocumentRepository employeeDocumentRepository,
                                       EmployeeRepository employeeRepository) {
        this.employeeDocumentRepository = employeeDocumentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void saveOrUpdate(EmployeeDocumentDTO object) {
        EmployeeDocument employeeDocument;
        String logMessage;

        if (object.getId() != null) {
            employeeDocument = employeeDocumentRepository.getReferenceById(object.getId());
            logMessage = "Employee's requirement record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            employeeDocument = new EmployeeDocument();
            employeeDocument.setCreatedBy(object.getCreatedBy());
            employeeDocument.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's requirement record is successfully created.";
        }

        employeeDocument.setEmployee(employeeRepository.getReferenceById(object.getEmployeeDTO().getId()));
        employeeDocument.setDocumentType(object.getDocumentType());
        employeeDocument.setFileName(object.getFileName());
        employeeDocument.setFileData(object.getFileData());
        employeeDocument.setFileType(object.getFileType());
        employeeDocument.setRemarks(object.getRemarks());
        employeeDocument.setExpirationDate(object.getExpirationDate());
        employeeDocument.setUpdatedBy(object.getUpdatedBy());
        employeeDocument.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        employeeDocumentRepository.save(employeeDocument);
    }

    @Override
    public EmployeeDocumentDTO getById(UUID id) {
        logger.info("Retrieving employee's requirement record with UUID ".concat(id.toString()));

        EmployeeDocument employeeDocument = employeeDocumentRepository.getReferenceById(id);
        EmployeeDocumentDTO employeeDocumentDTO = new EmployeeDocumentDTO();

        employeeDocumentDTO.setId(employeeDocument.getId());
        employeeDocumentDTO.setEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(employeeDocument.getEmployee().getId()));
        employeeDocumentDTO.setDocumentType(employeeDocument.getDocumentType());
        employeeDocumentDTO.setFileName(employeeDocument.getFileName());
        employeeDocumentDTO.setFileData(employeeDocument.getFileData());
        employeeDocumentDTO.setFileType(employeeDocument.getFileType());
        employeeDocumentDTO.setRemarks(employeeDocument.getRemarks());
        employeeDocumentDTO.setExpirationDate(employeeDocument.getExpirationDate());
        employeeDocumentDTO.setCreatedBy(employeeDocument.getCreatedBy());
        employeeDocumentDTO.setDateAndTimeCreated(employeeDocument.getDateAndTimeCreated());
        employeeDocumentDTO.setUpdatedBy(employeeDocument.getUpdatedBy());
        employeeDocumentDTO.setDateAndTimeUpdated(employeeDocument.getDateAndTimeUpdated());

        logger.info("Employee's requirement record with id ".concat(id.toString()).concat(" is successfully retrieved."));

        return employeeDocumentDTO;
    }

    @Override
    public void delete(EmployeeDocumentDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the employee's requirement record permanently.");

            String id = object.getId().toString();
            EmployeeDocument employeeDocument = employeeDocumentRepository.getReferenceById(object.getId());
            employeeDocumentRepository.delete(employeeDocument);

            logger.info("Employee's requirement record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<EmployeeDocumentDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee's requirement records from the database.");
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee's requirements records successfully retrieved.");
        List<EmployeeDocumentDTO> employeeDocumentDTOList = new ArrayList<>();

        if (!employeeDocumentList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (EmployeeDocument employeeDocument : employeeDocumentList) {
                EmployeeDocumentDTO employeeDocumentDTO = new EmployeeDocumentDTO();

                employeeDocumentDTO.setId(employeeDocument.getId());
                employeeDocumentDTO.setEmployeeDTO(employeeService.getById(employeeDocument.getEmployee().getId()));
                employeeDocumentDTO.setDocumentType(employeeDocument.getDocumentType());
                employeeDocumentDTO.setFileName(employeeDocument.getFileName());
                employeeDocumentDTO.setFileData(employeeDocument.getFileData());
                employeeDocumentDTO.setFileType(employeeDocument.getFileType());
                employeeDocumentDTO.setRemarks(employeeDocument.getRemarks());
                employeeDocumentDTO.setExpirationDate(employeeDocument.getExpirationDate());
                employeeDocumentDTO.setCreatedBy(employeeDocument.getCreatedBy());
                employeeDocumentDTO.setDateAndTimeCreated(employeeDocument.getDateAndTimeCreated());
                employeeDocumentDTO.setUpdatedBy(employeeDocument.getUpdatedBy());
                employeeDocumentDTO.setDateAndTimeUpdated(employeeDocument.getDateAndTimeUpdated());

                employeeDocumentDTOList.add(employeeDocumentDTO);
            }

            logger.info(String.valueOf(employeeDocumentList.size()).concat(" record(s) found."));
        }

        return employeeDocumentDTOList;
    }

    @Override
    public List<EmployeeDocumentDTO> findByParameter(String param) {
        return List.of();
    }

    @Override
    public List<EmployeeDocumentDTO> getByEmployeeDTO(EmployeeDTO employeeDTO) {
        logger.info("Retrieving requirement's records with employee UUID ".concat(employeeDTO.getId().toString()));

        Employee employee = employeeRepository.getReferenceById(employeeDTO.getId());

        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.getByEmployee(employee);
        List<EmployeeDocumentDTO> employeeDocumentDTOList = new ArrayList<>();

        if (!employeeDocumentList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (EmployeeDocument employeeDocument : employeeDocumentList) {
                EmployeeDocumentDTO employeeDocumentDTO = new EmployeeDocumentDTO();

                employeeDocumentDTO.setId(employeeDocument.getId());
                employeeDocumentDTO.setEmployeeDTO(employeeService.getById(employeeDocument.getEmployee().getId()));
                employeeDocumentDTO.setDocumentType(employeeDocument.getDocumentType());
                employeeDocumentDTO.setFileName(employeeDocument.getFileName());
                employeeDocumentDTO.setFileData(employeeDocument.getFileData());
                employeeDocumentDTO.setFileType(employeeDocument.getFileType());
                employeeDocumentDTO.setRemarks(employeeDocument.getRemarks());
                employeeDocumentDTO.setExpirationDate(employeeDocument.getExpirationDate());
                employeeDocumentDTO.setCreatedBy(employeeDocument.getCreatedBy());
                employeeDocumentDTO.setDateAndTimeCreated(employeeDocument.getDateAndTimeCreated());
                employeeDocumentDTO.setUpdatedBy(employeeDocument.getUpdatedBy());
                employeeDocumentDTO.setDateAndTimeUpdated(employeeDocument.getDateAndTimeUpdated());

                employeeDocumentDTOList.add(employeeDocumentDTO);
            }

            logger.info(String.valueOf(employeeDocumentList.size()).concat(" record(s) found."));
        }

        return employeeDocumentDTOList;
    }
}
