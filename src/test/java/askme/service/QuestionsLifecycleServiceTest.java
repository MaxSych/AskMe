package askme.service;

import askme.data.QuestionRepository;
import askme.data.UserRepository;
import askme.entity.Question;
import askme.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionsLifecycleServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionsLifecycleService questionsLifecycleService;

    @Test
    void getUnansweredQuestions_WhenUserExists_ReturnsOnlyUnanswered() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");

        Question unanswered = new Question();
        unanswered.setTextQuestion("Question 1");
        unanswered.setIsAnswered(false);

        Question answered = new Question();
        answered.setTextQuestion("Question 2");
        answered.setIsAnswered(true);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(questionRepository.findByUserId(1L)).thenReturn(List.of(unanswered, answered));

        List<Question> result = questionsLifecycleService.getUnansweredQuestions("user");

        assertThat(result)
                .hasSize(1)
                .containsExactly(unanswered);
    }

    @Test
    void getUnansweredQuestions_WhenUserNotFound_ThrowsException() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> questionsLifecycleService.getUnansweredQuestions("unknown"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");

        verifyNoInteractions(questionRepository);
    }

    @Test
    void deleteQuestion_WhenOwnerAndExists_DeletesAndReturnsTrue() {
        User owner = new User();
        owner.setId(1L);

        Question post = new Question();
        post.setId(10L);
        post.setUser(owner);

        when(questionRepository.findById(10L)).thenReturn(Optional.of(post));
        when(questionRepository.existsById(10L)).thenReturn(true);

        boolean result = questionsLifecycleService.deleteQuestion(10L, 1L);

        assertThat(result).isTrue();
        verify(questionRepository).deleteById(10L);
    }

    @Test
    void deleteQuestion_WhenPostNotFound_ThrowsException() {
        when(questionRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> questionsLifecycleService.deleteQuestion(10L, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Post not found with id: 10");

        verify(questionRepository, never()).deleteById(any());
    }

    @Test
    void deleteQuestion_WhenUserIsNotOwner_ThrowsAccessDeniedException() {
        User owner = new User();
        owner.setId(1L);

        Question post = new Question();
        post.setId(10L);
        post.setUser(owner);

        when(questionRepository.findById(10L)).thenReturn(Optional.of(post));

        assertThatThrownBy(() -> questionsLifecycleService.deleteQuestion(10L, 2L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("You are not allowed to delete this post");

        verify(questionRepository, never()).deleteById(any());
    }

    @Test
    void deleteQuestion_WhenDoesNotExistAfterFind_ReturnsFalse() {
        User owner = new User();
        owner.setId(1L);

        Question post = new Question();
        post.setId(10L);
        post.setUser(owner);

        when(questionRepository.findById(10L)).thenReturn(Optional.of(post));
        when(questionRepository.existsById(10L)).thenReturn(false);

        boolean result = questionsLifecycleService.deleteQuestion(10L, 1L);

        assertThat(result).isFalse();
        verify(questionRepository, never()).deleteById(any());
    }

    @Test
    void answerQuestion_WhenPostExists_SetsAnswerAndFlag() {
        Question post = new Question();
        post.setId(10L);
        post.setIsAnswered(false);

        when(questionRepository.findById(10L)).thenReturn(Optional.of(post));

        questionsLifecycleService.answerQuestion("My answer", 10L);

        assertThat(post.getAnswer()).isEqualTo("My answer");
        assertThat(post.getIsAnswered()).isTrue();
    }

    @Test
    void answerQuestion_WhenPostNotFound_ThrowsException() {
        when(questionRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> questionsLifecycleService.answerQuestion("My answer", 10L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Post not found with id: 10");
    }
}