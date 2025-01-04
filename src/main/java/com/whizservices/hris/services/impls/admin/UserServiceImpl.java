package com.whizservices.hris.services.impls.admin;

import com.whizservices.hris.dtos.admin.UserDTO;
import com.whizservices.hris.entities.admin.User;
import com.whizservices.hris.repositories.admin.UserRepository;
import com.whizservices.hris.repositories.profile.EmployeeRepository;
import com.whizservices.hris.services.admin.UserService;
import com.whizservices.hris.services.impls.profile.EmployeeServiceImpl;
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
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    public UserServiceImpl(UserRepository userRepository,
                           EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void saveOrUpdate(UserDTO object) {
        User user;
        String logMessage;

        if (object.getId() != null) {
            user = userRepository.getReferenceById(object.getId());
            logMessage = "User record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            user = new User();
            user.setCreatedBy(object.getCreatedBy());
            user.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "User record is successfully created.";
        }

        user.setEmployee(employeeRepository.getReferenceById(object.getEmployeeDTO().getId()));
        user.setUsername(object.getUsername());
        user.setPassword(object.getPassword());
        user.setRole(object.getRole());
        user.setEmailAddress(object.getEmailAddress());
        user.setAccountActive(object.isAccountActive());
        user.setPasswordChanged(object.isPasswordChanged());
        user.setUpdatedBy(object.getUpdatedBy());
        user.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        userRepository.save(user);
        logger.info(logMessage);
    }

    @Override
    public UserDTO getById(UUID id) {
        logger.info("Retrieving user record with UUID ".concat(id.toString()));

        User user = userRepository.getReferenceById(id);
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(user.getEmployee().getId()));
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        userDTO.setEmailAddress(user.getEmailAddress());
        userDTO.setAccountActive(user.isAccountActive());
        userDTO.setPasswordChanged(user.isPasswordChanged());
        userDTO.setCreatedBy(user.getCreatedBy());
        userDTO.setDateAndTimeCreated(user.getDateAndTimeCreated());
        userDTO.setUpdatedBy(user.getUpdatedBy());
        userDTO.setDateAndTimeUpdated(user.getDateAndTimeUpdated());

        logger.info("User record with id ".concat(id.toString()).concat(" is successfully retrieved."));

        return userDTO;
    }

    @Override
    public void delete(UserDTO object) {
        if (object != null) {
            logger.warn("You are about to delete a user record permanently.");

            String id = object.getId().toString();
            User user = userRepository.getReferenceById(object.getId());
            userRepository.delete(user);

            logger.info("User record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<UserDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving user records from the database.");
        List<User> userList = userRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("User records successfully retrieved.");
        List<UserDTO> userDTOList = new ArrayList<>();

        if (!userList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (User user : userList) {
                UserDTO userDTO = new UserDTO();

                userDTO.setId(user.getId());
                userDTO.setEmployeeDTO(employeeService.getById(user.getEmployee().getId()));
                userDTO.setUsername(user.getUsername());
                userDTO.setPassword(user.getPassword());
                userDTO.setRole(user.getRole());
                userDTO.setEmailAddress(user.getEmailAddress());
                userDTO.setAccountActive(user.isAccountActive());
                userDTO.setPasswordChanged(user.isPasswordChanged());
                userDTO.setCreatedBy(user.getCreatedBy());
                userDTO.setDateAndTimeCreated(user.getDateAndTimeCreated());
                userDTO.setUpdatedBy(user.getUpdatedBy());
                userDTO.setDateAndTimeUpdated(user.getDateAndTimeUpdated());

                userDTOList.add(userDTO);
            }

            logger.info(String.valueOf(userList.size()).concat(" record(s) found."));
        }

        return userDTOList;
    }

    @Override
    public List<UserDTO> findByParameter(String param) {
        List<UserDTO> userDTOList = new ArrayList<>();
        List<User> userList = null;

        logger.info("Retrieving user records with search parameter '%".concat(param).concat("%' from the database."));

        if (param.equalsIgnoreCase("Yes") || param.equalsIgnoreCase("No")) {
            userList = userRepository.findByBooleanParameter(param.equalsIgnoreCase("Yes"));
        } else {
            userList = userRepository.findByStringParameter(param);
        }

        if (!userList.isEmpty()) {
            logger.info("User records with parameter '%".concat(param).concat("%' has successfully retrieved."));

            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (User user : userList) {
                UserDTO userDTO = new UserDTO();

                userDTO.setId(user.getId());
                userDTO.setEmployeeDTO(employeeService.getById(user.getEmployee().getId()));
                userDTO.setUsername(user.getUsername());
                userDTO.setPassword(user.getPassword());
                userDTO.setRole(user.getRole());
                userDTO.setEmailAddress(user.getEmailAddress());
                userDTO.setAccountActive(user.isAccountActive());
                userDTO.setPasswordChanged(user.isPasswordChanged());
                userDTO.setCreatedBy(user.getCreatedBy());
                userDTO.setDateAndTimeCreated(user.getDateAndTimeCreated());
                userDTO.setUpdatedBy(user.getUpdatedBy());
                userDTO.setDateAndTimeUpdated(user.getDateAndTimeUpdated());

                userDTOList.add(userDTO);
            }
        }

        return userDTOList;
    }

    @Override
    public UserDTO getByUsername(String username) {
        logger.info("Retrieving user record with username ".concat(username));

        User user = userRepository.findByUsername(username);
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(user.getEmployee().getId()));
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        userDTO.setEmailAddress(user.getEmailAddress());
        userDTO.setAccountActive(user.isAccountActive());
        userDTO.setPasswordChanged(user.isPasswordChanged());
        userDTO.setCreatedBy(user.getCreatedBy());
        userDTO.setDateAndTimeCreated(user.getDateAndTimeCreated());
        userDTO.setUpdatedBy(user.getUpdatedBy());
        userDTO.setDateAndTimeUpdated(user.getDateAndTimeUpdated());

        logger.info("User record with username ".concat(username).concat(" is successfully retrieved."));

        return userDTO;
    }
}
