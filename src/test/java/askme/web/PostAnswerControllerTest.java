package askme.web;

import askme.service.QuestionsLifecycleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostAnswerController.class)
class PostAnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private QuestionsLifecycleService questionsLifecycleService;

    @Test
    void answerQuestion_WithValidRequest_ShouldReturn200() throws Exception {
        Long postId = 1L;
        String answerText = "This is a good question!";

        String jsonBody = objectMapper.writeValueAsString(Map.of("answer", answerText));

        mockMvc.perform(post("/posts/{postId}/answer", postId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());

        verify(questionsLifecycleService, times(1))
                .answerQuestion(eq(answerText), eq(postId));
    }

    @Test
    void answerQuestion_WhenServiceThrowsException_ShouldReturn500() throws Exception {
        Long postId = 5L;
        String answerText = "This is a good question!";

        String jsonBody = objectMapper.writeValueAsString(Map.of("answer", answerText));

        doThrow(new RuntimeException("Question not found"))
                .when(questionsLifecycleService).answerQuestion(answerText, postId );


        mockMvc.perform(post("/posts/{postId}/answer", postId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isInternalServerError());

        verify(questionsLifecycleService, times(1)).answerQuestion(anyString(), anyLong());
    }

    @Test
    void answerQuestion_WithEmptyAnswer_ShouldReturn400() throws Exception {
        Long postId = 42L;
        String answerText = " ";

        String jsonBody = objectMapper.writeValueAsString(Map.of("answer", answerText));

        mockMvc.perform(post("/posts/{postId}/answer", postId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(questionsLifecycleService);
    }
}