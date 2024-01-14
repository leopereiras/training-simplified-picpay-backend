package com.picpaysimplificado.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import javax.swing.text.html.parser.Entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import com.picpaysimplificado.domain.dtos.UserDTO;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get user sucess from DB")
    void testFindUserByDocumentCase1() {
        String document = "1234567891";
        UserDTO data = new UserDTO("Leo","Santos",document, new BigDecimal(10), "teste@example.com", "4444",UserType.COMMON);
        this.createUser(data);

        Optional<User> result = this.userRepository.findUserByDocument(document);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get user sucess from DB when user not exists")
    void testFindUserByDocumentCase2() {
        String document = "1234567891";

        Optional<User> result = this.userRepository.findUserByDocument(document);

        assertThat(result.isEmpty()).isTrue();
    }

    private User createUser(UserDTO data){
        User newUser = new User(data);
        this.entityManager.persist(newUser);
        return newUser;
    }
}
