package askme.service;

import askme.data.QuestionRepository;
import askme.data.UserRepository;
import askme.dto.response.QuestionResponse;
import askme.dto.response.UserProfileResponse;
import askme.entity.Question;
import askme.entity.User;
import askme.mapper.UserPublicProfileMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserPublicProfileMapper userPublicProfileMapper;

    @InjectMocks
    private ProfileService profileService;

    @Test
    void getUserInformation_shouldReturnProfile_whenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setBio("null");

        UserProfileResponse expected = new UserProfileResponse(1L, "user", null);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(userPublicProfileMapper.toUserResponse(user)).thenReturn(expected);

        UserProfileResponse actual = profileService.getUserInformation("user");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getUserInformation_shouldThrowNotFound_whenUserDoesNotExist() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> profileService.getUserInformation("user")
        );

        assertThat(HttpStatus.NOT_FOUND).isEqualTo(ex.getStatusCode());
        verifyNoInteractions(userPublicProfileMapper);
    }

    @Test
    void getPostInformation_shouldReturnAnsweredQuestions_whenUserExists() {
        User user = new User();
        user.setId(10L);
        user.setUsername("user");
        user.setBio("null");

        Question question = new Question();
        QuestionResponse dto = new QuestionResponse(1L, "Question", "Answer", true, null);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(questionRepository.findByUserIdAndIsAnsweredTrue(10L)).thenReturn(List.of(question));
        when(userPublicProfileMapper.toQuestionResponse(question)).thenReturn(dto);

        List<QuestionResponse> result = profileService.getPostInformation("user");

        assertThat(1).isEqualTo(result.size());
        assertThat(dto).isEqualTo(result.get(0));
    }

    @Test
    void getPostInformation_shouldThrowNotFound_whenUserDoesNotExist() {
        when(userRepository.findByUsername("user2")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.getPostInformation("user2"))
                .isInstanceOf(ResponseStatusException.class);

        verifyNoInteractions(questionRepository, userPublicProfileMapper);
    }
}