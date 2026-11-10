package askme.mapper;

import askme.dto.response.QuestionResponse;
import askme.dto.response.UserProfileResponse;
import askme.entity.Question;
import askme.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserPublicProfileMapper {
    public UserProfileResponse toUserResponse(User user) {
        return UserProfileResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .bio(user.getBio())
                .build();
    }

    public QuestionResponse toQuestionResponse(Question question) {
        return QuestionResponse.builder()
                .questionId(question.getId())
                .textQuestion(question.getTextQuestion())
                .answer(question.getAnswer())
                .createdAt(question.getCreatedAt())
                .isAnswered(question.getIsAnswered())
                .build();
    }
}