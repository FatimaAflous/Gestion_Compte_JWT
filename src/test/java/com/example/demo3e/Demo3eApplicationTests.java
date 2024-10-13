package com.example.demo3e;

import com.example.demo3e.Controller.UserController;
import com.example.demo3e.entity.UserEntity;
import com.example.demo3e.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class Demo3eApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        // Si vous souhaitez initialiser quelque chose avant chaque test, faites-le ici
    }

    @Test

    /*
    void testRetrieveUsers() {
        // Créer et enregistrer des utilisateurs
        UserEntity user1 = new UserEntity();
        user1.setUsername("fatima");
        user1.setPassword(passwordEncoder.encode("password123"));
        user1.setRole("USER");
        userRepository.save(user1);

        UserEntity user2 = new UserEntity();
        user2.setUsername("ahmed");
        user2.setPassword(passwordEncoder.encode("password456"));
        user2.setRole("ADMIN");
        userRepository.save(user2);

        // Récupérer tous les utilisateurs de la base de données
        List<UserEntity> users = userRepository.findAll();

        // Vérifier que deux utilisateurs ont été enregistrés
        assertThat(users).hasSize(2);
        assertThat(users).extracting(UserEntity::getUsername).containsExactlyInAnyOrder("fatima", "ahmed");
    }
*/
    void contextLoads() {
    }
}
