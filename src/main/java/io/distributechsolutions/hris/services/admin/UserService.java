package io.distributechsolutions.hris.services.admin;

import io.distributechsolutions.hris.dtos.admin.UserDTO;
import io.distributechsolutions.hris.services.BaseService;
import org.springframework.transaction.annotation.Transactional;

public interface UserService extends BaseService<UserDTO> {
    @Transactional
    UserDTO getByUsername(String username);
}
