package askme.service;

import askme.data.QuestionRepository;
import askme.data.UserRepository;
import askme.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AskingQuestionsServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AskingQuestionsService askingQuestionsService;

    @Test
    void addQuestion_UserExists_SavesQuestion() {
        String username = "max";
        String text = "hello ?";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        askingQuestionsService.addQuestion(text, username);

        verify(questionRepository).save(argThat(question ->
                text.equals(question.getTextQuestion()) && user.equals(question.getUser())
        ));
    }

    @Test
    void addQuestion_UserNotFound_ThrowsException() {
        String username = "unknown";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> askingQuestionsService.addQuestion("hello", username))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found: " + username);

        verify(questionRepository, never()).save(any());
    }
}