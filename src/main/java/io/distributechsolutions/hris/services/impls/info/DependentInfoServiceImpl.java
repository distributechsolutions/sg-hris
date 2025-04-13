package io.distributechsolutions.hris.services.impls.info;

import io.distributechsolutions.hris.dtos.info.DependentInfoDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.entities.info.DependentInfo;
import io.distributechsolutions.hris.entities.profile.Employee;
import io.distributechsolutions.hris.repositories.info.DependentInfoRepository;
import io.distributechsolutions.hris.repositories.profile.EmployeeRepository;
import io.distributechsolutions.hris.services.impls.profile.EmployeeServiceImpl;
import io.distributechsolutions.hris.services.info.DependentInfoService;
import io.distributechsolutions.hris.services.profile.EmployeeService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DependentInfoServiceImpl implements DependentInfoService {
    private final Logger logger = LoggerFactory.getLogger(DependentInfoService.class);

    private final DependentInfoRepository dependentInfoRepository;
    private final EmployeeRepository employeeRepository;

    public DependentInfoServiceImpl(DependentInfoRepository dependentInfoRepository,
                                    EmployeeRepository employeeRepository) {
        this.dependentInfoRepository = dependentInfoRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void saveOrUpdate(DependentInfoDTO object) {
        DependentInfo dependentInfo;
        String logMessage;

        if (object.getId() != null) {
            dependentInfo = dependentInfoRepository.getReferenceById(object.getId());
            logMessage = "Dependent record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            dependentInfo = new DependentInfo();
            dependentInfo.setCreatedBy(object.getCreatedBy());
            dependentInfo.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Dependent record is successfully created.";
        }

        dependentInfo.setEmployee(employeeRepository.getReferenceById(object.getEmployeeDTO().getId()));
        dependentInfo.setFullName(object.getFullName());
        dependentInfo.setDateOfBirth(object.getDateOfBirth());
        dependentInfo.setAge(object.getAge());
        dependentInfo.setRelationship(object.getRelationship());
        dependentInfo.setUpdatedBy(object.getUpdatedBy());
        dependentInfo.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        dependentInfoRepository.save(dependentInfo);
        logger.info(logMessage);
    }

    @Override
    public DependentInfoDTO getById(UUID id) {
        logger.info("Retrieving personnel dependent record with UUID ".concat(id.toString()));

        DependentInfo dependentInfo = dependentInfoRepository.getReferenceById(id);
        DependentInfoDTO dependentInfoDTO = new DependentInfoDTO();

        dependentInfoDTO.setId(dependentInfo.getId());
        dependentInfoDTO.setEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(dependentInfo.getEmployee().getId()));
        dependentInfoDTO.setFullName(dependentInfo.getFullName());
        dependentInfoDTO.setDateOfBirth(dependentInfo.getDateOfBirth());
        dependentInfoDTO.setAge(dependentInfo.getAge());
        dependentInfoDTO.setRelationship(dependentInfo.getRelationship());
        dependentInfoDTO.setUpdatedBy(dependentInfo.getUpdatedBy());
        dependentInfoDTO.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        return dependentInfoDTO;
    }

    @Override
    public void delete(DependentInfoDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the dependent record permanently.");

            String id = object.getId().toString();
            DependentInfo dependentInfo = dependentInfoRepository.getReferenceById(object.getId());
            dependentInfoRepository.delete(dependentInfo);

            logger.info("Dependent record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<DependentInfoDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving dependent records from the database.");
        List<DependentInfo> dependentInfoList = dependentInfoRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Dependent records successfully retrieved.");
        List<DependentInfoDTO> dependentInfoDTOList = new ArrayList<>();

        if (!dependentInfoList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (DependentInfo dependentInfo : dependentInfoList) {
                DependentInfoDTO dependentInfoDTO = new DependentInfoDTO();

                dependentInfoDTO.setId(dependentInfo.getId());
                dependentInfoDTO.setEmployeeDTO(employeeService.getById(dependentInfo.getEmployee().getId()));
                dependentInfoDTO.setFullName(dependentInfo.getFullName());
                dependentInfoDTO.setDateOfBirth(dependentInfo.getDateOfBirth());
                dependentInfoDTO.setAge(dependentInfo.getAge());
                dependentInfoDTO.setRelationship(dependentInfo.getRelationship());

                dependentInfoDTOList.add(dependentInfoDTO);
            }

            logger.info(String.valueOf(dependentInfoList.size()).concat(" record(s) found."));
        }

        return dependentInfoDTOList;
    }

    @Override
    public List<DependentInfoDTO> findByParameter(String param) {
        return List.of();
    }

    @Override
    public List<DependentInfoDTO> getByEmployeeDTO(EmployeeDTO employeeDTO) {
        logger.info("Retrieving dependent records with employee UUID ".concat(employeeDTO.getId().toString()));

        Employee employee = employeeRepository.getReferenceById(employeeDTO.getId());

        List<DependentInfo> dependentInfoList = dependentInfoRepository.findByEmployee(employee);
        List<DependentInfoDTO> dependentInfoDTOList = new ArrayList<>();

        if (!dependentInfoList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (DependentInfo dependentInfo : dependentInfoList) {
                DependentInfoDTO dependentInfoDTO = new DependentInfoDTO();

                dependentInfoDTO.setId(dependentInfo.getId());
                dependentInfoDTO.setEmployeeDTO(employeeService.getById(dependentInfo.getEmployee().getId()));
                dependentInfoDTO.setFullName(dependentInfo.getFullName());
                dependentInfoDTO.setDateOfBirth(dependentInfo.getDateOfBirth());
                dependentInfoDTO.setAge(dependentInfo.getAge());
                dependentInfoDTO.setRelationship(dependentInfo.getRelationship());

                dependentInfoDTOList.add(dependentInfoDTO);
            }

            logger.info(String.valueOf(dependentInfoList.size()).concat(" record(s) found."));
        }

        return dependentInfoDTOList;
    }
}
