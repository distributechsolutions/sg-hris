package io.softwaregarage.hris.profile.services.impls;

import io.softwaregarage.hris.admin.repositories.DepartmentRepository;
import io.softwaregarage.hris.admin.services.DepartmentService;
import io.softwaregarage.hris.profile.dtos.DepartmentProfileDTO;
import io.softwaregarage.hris.profile.entities.DepartmentProfile;
import io.softwaregarage.hris.profile.repositories.DepartmentProfileRepository;
import io.softwaregarage.hris.profile.repositories.EmployeeProfileRepository;
import io.softwaregarage.hris.profile.services.DepartmentProfileService;
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
public class DepartmentProfileServiceImpl implements DepartmentProfileService {
    private final Logger logger = LoggerFactory.getLogger(DepartmentProfileServiceImpl.class);

    private final DepartmentProfileRepository departmentProfileRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final DepartmentRepository departmentRepository;

    public DepartmentProfileServiceImpl(DepartmentProfileRepository employeeDepartmentProfileRepository,
                                        EmployeeProfileRepository employeeProfileRepository,
                                        io.softwaregarage.hris.admin.repositories.DepartmentRepository departmentRepository) {
        this.departmentProfileRepository = employeeDepartmentProfileRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void saveOrUpdate(DepartmentProfileDTO object) {
        DepartmentProfile departmentProfile;
        String logMessage;

        if (object.getId() != null) {
            departmentProfile = departmentProfileRepository.getReferenceById(object.getId());
            logMessage = "Employee's department record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            departmentProfile = new DepartmentProfile();
            departmentProfile.setCreatedBy(object.getCreatedBy());
            departmentProfile.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's department record is successfully created.";
        }

        departmentProfile.setEmployee(employeeProfileRepository.getReferenceById(object.getEmployeeDTO().getId()));
        departmentProfile.setDepartment(departmentRepository.getReferenceById(object.getDepartmentDTO().getId()));
        departmentProfile.setCurrentDepartment(object.isCurrentDepartment());
        departmentProfile.setUpdatedBy(object.getUpdatedBy());
        departmentProfile.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        departmentProfileRepository.save(departmentProfile);
        logger.info(logMessage);
    }

    @Override
    public DepartmentProfileDTO getById(UUID id) {
        logger.info("Retrieving employee's department record with UUID ".concat(id.toString()));

        DepartmentProfile departmentProfile = departmentProfileRepository.getReferenceById(id);
        DepartmentProfileDTO departmentProfileDTO = new DepartmentProfileDTO();

        departmentProfileDTO.setId(departmentProfile.getId());
        departmentProfileDTO.setEmployeeDTO(new EmployeeProfileServiceImpl(employeeProfileRepository).getById(departmentProfile.getEmployee().getId()));
        departmentProfileDTO.setDepartmentDTO(new io.softwaregarage.hris.admin.services.impls.DepartmentServiceImpl(departmentRepository).getById(departmentProfile.getDepartment().getId()));
        departmentProfileDTO.setCurrentDepartment(departmentProfile.isCurrentDepartment());
        departmentProfileDTO.setCreatedBy(departmentProfile.getCreatedBy());
        departmentProfileDTO.setDateAndTimeCreated(departmentProfile.getDateAndTimeCreated());
        departmentProfileDTO.setUpdatedBy(departmentProfile.getUpdatedBy());
        departmentProfileDTO.setDateAndTimeUpdated(departmentProfile.getDateAndTimeUpdated());

        logger.info("Employee's department record with id ".concat(id.toString()).concat(" is successfully retrieved."));

        return departmentProfileDTO;
    }

    @Override
    public void delete(DepartmentProfileDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the employee's department record permanently.");

            String id = object.getId().toString();
            DepartmentProfile departmentProfile = departmentProfileRepository.getReferenceById(object.getId());
            departmentProfileRepository.delete(departmentProfile);

            logger.info("Employee's department record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<DepartmentProfileDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee's department records from the database.");
        List<DepartmentProfile> departmentProfileList = departmentProfileRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee's department records successfully retrieved.");
        List<DepartmentProfileDTO> departmentProfileDTOList = new ArrayList<>();

        if (!departmentProfileList.isEmpty()) {
            EmployeeProfileService employeeProfileService = new EmployeeProfileServiceImpl(employeeProfileRepository);
            io.softwaregarage.hris.admin.services.DepartmentService positionService = new io.softwaregarage.hris.admin.services.impls.DepartmentServiceImpl(departmentRepository);

            for (DepartmentProfile departmentProfile : departmentProfileList) {
                DepartmentProfileDTO departmentProfileDTO = new DepartmentProfileDTO();

                departmentProfileDTO.setId(departmentProfile.getId());
                departmentProfileDTO.setEmployeeDTO(employeeProfileService.getById(departmentProfile.getEmployee().getId()));
                departmentProfileDTO.setDepartmentDTO(positionService.getById(departmentProfile.getDepartment().getId()));
                departmentProfileDTO.setCurrentDepartment(departmentProfile.isCurrentDepartment());
                departmentProfileDTO.setCreatedBy(departmentProfile.getCreatedBy());
                departmentProfileDTO.setDateAndTimeCreated(departmentProfile.getDateAndTimeCreated());
                departmentProfileDTO.setUpdatedBy(departmentProfile.getUpdatedBy());
                departmentProfileDTO.setDateAndTimeUpdated(departmentProfile.getDateAndTimeUpdated());

                departmentProfileDTOList.add(departmentProfileDTO);
            }

            logger.info(String.valueOf(departmentProfileList.size()).concat(" record(s) found."));
        }

        return departmentProfileDTOList;
    }

    @Override
    public List<DepartmentProfileDTO> findByParameter(String param) {
        List<DepartmentProfileDTO> departmentProfileDTOList = new ArrayList<>();
        List<DepartmentProfile> departmentProfileList;

        logger.info("Retrieving employee's department records with search parameter '%".concat(param).concat("%' from the database."));

        if (param.equalsIgnoreCase("Yes") || param.equalsIgnoreCase("No")) {
            departmentProfileList = departmentProfileRepository.findByBooleanParameter(param.equalsIgnoreCase("Yes"));
        } else {
            departmentProfileList = departmentProfileRepository.findByStringParameter(param);
        }

        if (!departmentProfileList.isEmpty()) {
            logger.info("Employee's department records with parameter '%".concat(param).concat("%' has successfully retrieved."));

            EmployeeProfileService employeeProfileService = new EmployeeProfileServiceImpl(employeeProfileRepository);
            DepartmentService positionService = new io.softwaregarage.hris.admin.services.impls.DepartmentServiceImpl(departmentRepository);

            for (DepartmentProfile departmentProfile : departmentProfileList) {
                DepartmentProfileDTO departmentProfileDTO = new DepartmentProfileDTO();

                departmentProfileDTO.setId(departmentProfile.getId());
                departmentProfileDTO.setEmployeeDTO(employeeProfileService.getById(departmentProfile.getEmployee().getId()));
                departmentProfileDTO.setDepartmentDTO(positionService.getById(departmentProfile.getDepartment().getId()));
                departmentProfileDTO.setCurrentDepartment(departmentProfile.isCurrentDepartment());
                departmentProfileDTO.setCreatedBy(departmentProfile.getCreatedBy());
                departmentProfileDTO.setDateAndTimeCreated(departmentProfile.getDateAndTimeCreated());
                departmentProfileDTO.setUpdatedBy(departmentProfile.getUpdatedBy());
                departmentProfileDTO.setDateAndTimeUpdated(departmentProfile.getDateAndTimeUpdated());

                departmentProfileDTOList.add(departmentProfileDTO);
            }
        }

        return departmentProfileDTOList;
    }
}
