package com.whizservices.hris.services.admin;

import com.whizservices.hris.dtos.admin.UserDTO;
import com.whizservices.hris.services.BaseService;
import org.springframework.transaction.annotation.Transactional;

public interface UserService extends BaseService<UserDTO> {
    @Transactional
    UserDTO getByUsername(String username);
}
