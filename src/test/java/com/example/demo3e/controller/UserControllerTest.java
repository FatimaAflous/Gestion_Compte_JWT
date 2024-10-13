package com.example.demo3e.controller;

import com.example.demo3e.Controller.UserController;
import com.example.demo3e.entity.UserEntity;
import com.example.demo3e.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        // Créer un utilisateur fictif
        UserEntity user = new UserEntity();
        user.setUsername("fatima");
        user.setPassword("password123");
        user.setRole("USER");

        // Simuler le comportement du PasswordEncoder
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        // Afficher le mot de passe encodé pour vérifier
        System.out.println("Mot de passe encodé: " + passwordEncoder.encode(user.getPassword()));

        // Simuler le comportement du userRepository
        when(userRepository.save(any(UserEntity.class))).thenReturn(user); // Simuler le retour de la méthode save

        // Appeler la méthode d'enregistrement
        ResponseEntity<String> response = userController.registerUser(user);

        // Afficher la réponse pour voir ce qui est retourné
        System.out.println("Réponse: " + response.getBody());

        // Vérifier les résultats
        assertEquals("Utilisateur enregistré avec succès", response.getBody());

        // Vérifier que save() a été appelé une fois et imprimer les arguments
        verify(userRepository, times(1)).save(any(UserEntity.class));

        // Imprimer les arguments utilisés lors de l'appel de save
        System.out.println("Utilisateur enregistré: " + user);
    }

}
