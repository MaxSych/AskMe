package askme.web;


import askme.User;
import askme.service.AskingQuestionsService;
import askme.service.QuestionsLifecycleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostAnswerController.class)
class QuestionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AskingQuestionsService askingQuestionsService;

    @MockitoBean
    private QuestionsLifecycleService questionsLifecycleService;

    @Test
    void createQuestion_WhenUserExists_RedirectsToProfileAndSavesQuestion() throws Exception {
        String userName = "Anna";
        String questionText = "Hi its a test question!";

        mockMvc.perform(post("/add")
                        .param("text", questionText)
                        .param("userName", userName))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/" + userName));

        verify(askingQuestionsService).addQuestion(questionText, userName);

    }

    @Test
    void createPost_WhenUserDoesNotExist_Returns404() throws Exception {
        String userName = "NonExistentUser";
        String questionText = "Hi its a test question!";

        doThrow(new UsernameNotFoundException("User not found")).when(askingQuestionsService).addQuestion(questionText, userName);

        mockMvc.perform(post("/add")
                .param("text", questionText)
                .param("userName", userName))
                .andExpect(status().isNotFound());
    }

    @Test
    void addQuestion_WhenTextTooShort_Redirects() throws Exception {
        String userName = "Michael";
        String questionText = "Hi";

        mockMvc.perform(post("/add")
                        .param("text", questionText)
                        .param("userName", userName))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/" + userName))
                .andExpect(flash().attribute("errorMessage", "Text is too short!"));

        verify(askingQuestionsService, never()).addQuestion(questionText, userName);
    }

    @Test
    void deleteQuestion_Success() throws Exception {
        Long questionId = 1L;
        User mockUser = mock(User.class);

        when(mockUser.getId()).thenReturn(1L);

        mockMvc.perform(post("/questions/{id}/delete", questionId)
                    .with(user(mockUser)))
                .andExpect(status().isNoContent());

        verify(questionsLifecycleService).deleteQuestion(questionId, 1L);

    }
}