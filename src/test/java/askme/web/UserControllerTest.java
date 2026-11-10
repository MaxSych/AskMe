package askme.web;

import askme.dto.response.InboxQuestionResponse;
import askme.dto.response.QuestionResponse;
import askme.dto.response.UserProfileResponse;
import askme.entity.Question;
import askme.mapper.InboxMapper;
import askme.service.ProfileService;
import askme.service.QuestionsLifecycleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfileService profileService;

    @MockitoBean
    private QuestionsLifecycleService answeringService;

    @MockitoBean
    private InboxMapper inboxMapper;

    @Test
    void showProfile_ShouldReturnProfileViewWithData() throws Exception {

        String username = "test";

        List<Question> fakeQuestions = List.of(new Question(), new Question());
        List<QuestionResponse> posts = List.of(new QuestionResponse(42L, "A question?", "An answer", true, LocalDate.of(2026, 1, 2)));
        UserProfileResponse fakeUser = new UserProfileResponse(1L, username, null);
        List<InboxQuestionResponse> fakeUnanswered = List.of(new InboxQuestionResponse(1l, "A"),new InboxQuestionResponse(2L, "A") );

        when(profileService.getPostInformation(username)).thenReturn(posts);
        when(profileService.getUserInformation(username)).thenReturn(fakeUser);
        when(answeringService.getUnansweredQuestions(username)).thenReturn(fakeQuestions);
        when(inboxMapper.toInboxPostResponseList(fakeQuestions)).thenReturn(fakeUnanswered);

        mockMvc.perform(get("/profile/" + username).with(user(username))
                        .requestAttr("_csrf", new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", "token")))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("user", fakeUser))
                .andExpect(model().attribute("posts", posts))
                .andExpect(model().attribute("unanswered", fakeUnanswered))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("A question?")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("An answer")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("data-id=\"42\"")));

        verify(profileService).getPostInformation(username);
        verify(profileService).getUserInformation(username);
        verify(inboxMapper).toInboxPostResponseList(fakeQuestions);
    }

  }