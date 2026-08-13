package com.marketplace.user.mapper;

import com.marketplace.user.dto.UserResponse;
import com.marketplace.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getRole(), user.getFirstName(),
                user.getLastName(), user.getPhone());
    }
}
