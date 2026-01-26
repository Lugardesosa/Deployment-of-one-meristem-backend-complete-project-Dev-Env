package org.meristem.oneapp.usersservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.meristem.oneapp.usersservice.domains.requests.CreateUserRequest;
import org.meristem.oneapp.usersservice.services.implementations.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasItem;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RequiredArgsConstructor
@AutoConfigureMockMvc
class UsersControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    private UsersService usersService;

//    @Container
//    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.8");

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    static void beforeAll() {
    }

    @AfterAll
    static void afterAll() {

    }

//    @DynamicPropertySource
//    static void setProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
//        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
//    }

    @DisplayName("Create a user with invalid details.")
    @Test
    void createUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/base")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(
                        CreateUserRequest.builder().firstName("").lastName("").middleName("").phoneNumber("").build())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasItem("email: cannot be null")));
    }

    @Test
    void getUser() {
    }
}