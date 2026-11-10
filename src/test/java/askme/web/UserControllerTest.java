package askme.web;

import askme.User;
import askme.data.QuestionRepository;
import askme.data.UserRepository;
import askme.dto.response.InboxQuestionResponse;
import askme.dto.response.UserProfileResponse;
import askme.entity.Question;
import askme.mapper.InboxMapper;
import askme.service.ProfileService;
import askme.service.QuestionsLifecycleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        UserProfileResponse fakeUser = new UserProfileResponse(1L, 1L, "Mike", null, null, null, null, null, null);
        List<InboxQuestionResponse> fakeUnanswered = List.of(new InboxQuestionResponse(1l, "A"),new InboxQuestionResponse(2L, "A") );

        when(profileService.getPostInformation(username)).thenReturn(fakeQuestions);
        when(profileService.getUserInformation(username)).thenReturn(fakeUser);
        when(inboxMapper.toInboxPostResponseList(fakeQuestions)).thenReturn(fakeUnanswered);

        mockMvc.perform(get("/profile/" + username))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("user", fakeUser))
                .andExpect(model().attribute("posts", fakeQuestions))
                .andExpect(model().attribute("unanswered", fakeUnanswered));

        verify(profileService).getPostInformation(username);
        verify(profileService).getUserInformation(username);
        verify(inboxMapper).toInboxPostResponseList(fakeQuestions);
    }

  }