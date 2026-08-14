package com.nexora.user.controller;

import com.nexora.user.dto.UpdateUserProfileRequest;
import com.nexora.user.dto.UserProfileDto;
import com.nexora.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Profil utilisateur")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Mon profil")
    public UserProfileDto getMyProfile() {
        return userService.getMyProfile();
    }

    @PutMapping("/me")
    @Operation(summary = "Modifier mon profil")
    public UserProfileDto updateMyProfile(@Valid @RequestBody UpdateUserProfileRequest request) {
        return userService.updateMyProfile(request);
    }
}
