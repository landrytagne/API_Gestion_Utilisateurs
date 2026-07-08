package com.tland.API_Gestion_Utilisateurs.mapper;

import com.tland.API_Gestion_Utilisateurs.dto.UserRequestDTO;
import com.tland.API_Gestion_Utilisateurs.dto.UserResponseDTO;
import com.tland.API_Gestion_Utilisateurs.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .build();
    }

    public UserResponseDTO toResponseDTO(User entity) {
        if (entity == null) return null;
        return UserResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .role(entity.getRole())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}