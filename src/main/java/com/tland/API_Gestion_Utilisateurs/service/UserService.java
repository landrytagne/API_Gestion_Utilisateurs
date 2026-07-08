package com.tland.API_Gestion_Utilisateurs.service;

import com.tland.API_Gestion_Utilisateurs.entity.Role;
import com.tland.API_Gestion_Utilisateurs.entity.User;
import com.tland.API_Gestion_Utilisateurs.exception.UserNotFoundException;
import com.tland.API_Gestion_Utilisateurs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 1. Créer un utilisateur
    public User createUser(User user) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }

        // Crypter le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Par défaut, rôle USER
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        return userRepository.save(user);
    }

    // 2. Récupérer tous les utilisateurs
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 3. Récupérer un utilisateur par ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable avec l'ID : " + id));
    }

    // 4. Mettre à jour un utilisateur
    public User updateUser(Long id, User userDetails) {
        User existingUser = getUserById(id);

        if (userDetails.getName() != null) {
            existingUser.setName(userDetails.getName());
        }
        if (userDetails.getEmail() != null) {
            existingUser.setEmail(userDetails.getEmail());
        }
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        if (userDetails.getRole() != null) {
            existingUser.setRole(userDetails.getRole());
        }

        return userRepository.save(existingUser);
    }

    // 5. Supprimer un utilisateur
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Utilisateur introuvable avec l'ID : " + id);
        }
        userRepository.deleteById(id);
    }
}