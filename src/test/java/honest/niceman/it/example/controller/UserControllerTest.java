package honest.niceman.it.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import honest.niceman.it.example.model.User;
import honest.niceman.it.example.repo.UserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void save() throws Exception {
        //given
        Map<Object, Object> request = new HashMap<>();
        String username = "username";
        String createdAt = Instant.now()
                .toString();
        String createdBy = "admin";
        request.put("username", username);
        request.put("createdAt", createdAt);
        request.put("createdBy", createdBy);
        //when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.post("/rest/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
        );
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(username)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt", CoreMatchers.is(createdAt)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy", CoreMatchers.is(createdBy)));

        Assertions.assertTrue(userRepository.findByUsername(username).isPresent());
    }

    @Test
    public void patch() throws Exception {
        //given
        User user = new User();
        user.setCreatedAt(Instant.now());
        user.setCreatedBy("admin");
        user.setUsername("path_username");

        User savedUser = userRepository.save(user);

        Map<Object, Object> request = new HashMap<>();
        String updatedUsername = "new_username";
        request.put("username", updatedUsername);
        //when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.patch("/rest/users/" + savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
        );
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(updatedUsername)));

        Assertions.assertTrue(userRepository.findByUsername(updatedUsername).isPresent());
    }

    @Test
    public void delete() throws Exception {
        //given
        User user = new User();
        user.setCreatedAt(Instant.now());
        user.setCreatedBy("admin");
        user.setUsername("path_username");

        User savedUser = userRepository.save(user);
        //when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/rest/users/" + savedUser.getId())
        );
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(savedUser.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt", CoreMatchers.is(savedUser.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy", CoreMatchers.is(savedUser.getCreatedBy())));

        Assertions.assertFalse(userRepository.findByUsername(savedUser.getUsername()).isPresent());
    }

    @Test
    public void get() throws Exception {
        //given
        User user = new User();
        user.setCreatedAt(Instant.now());
        user.setCreatedBy("admin");
        user.setUsername("path_username");

        User savedUser = userRepository.save(user);
        //when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/rest/users/" + savedUser.getId())
        );
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(savedUser.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt", CoreMatchers.is(savedUser.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy", CoreMatchers.is(savedUser.getCreatedBy())));

        Assertions.assertTrue(userRepository.findByUsername(savedUser.getUsername()).isPresent());
    }
}
