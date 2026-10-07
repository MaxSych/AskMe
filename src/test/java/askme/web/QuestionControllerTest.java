package askme.web;

import askme.User;
import askme.data.QuestionRepository;
import askme.data.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostAnswerController.class)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private QuestionRepository postRepository;

    @Test
    void testAddPost_Success() throws Exception {
        Long userId = 1L;
        String postText = "Hi its a test post!";
        User mockUser = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        mockMvc.perform(post("/profile/{userId}", userId)
                        .param("text", postText))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/" + userId));

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testAddPost_UserNotFound() throws Exception {
        Long userId = 99L;
        
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/profile/{userId}", userId)
                        .param("text", "any text"))
                .andExpect(status().isInternalServerError());
    }
}