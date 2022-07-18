package com.inside.messagesystem.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inside.messagesystem.entity.RoleEntity;
import com.inside.messagesystem.entity.UserEntity;
import com.inside.messagesystem.entity.UserRole;
import com.inside.messagesystem.repository.MessageRepository;
import com.inside.messagesystem.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

    @Value("${jwt.token.access}")
    private String accessSecretWord;

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected MessageRepository messageRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    protected UserEntity user;

    @BeforeEach
    void init() {
        user = new UserEntity();
        user.setId(1);
        user.setName("user");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt(12)));

        RoleEntity role = new RoleEntity();
        role.setId(2);
        role.setRoleName(UserRole.ADMIN);
        user.setRoles(Set.of(role));
    }

    protected <T> String toJson(final T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    protected String generateAccessTokenFromTest(UserEntity user, int timeInMinutes) {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("name", user.getName());
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .addClaims(claimsMap)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(timeInMinutes))))
                .signWith(SignatureAlgorithm.HS512, accessSecretWord)
                .compact();
    }

}
