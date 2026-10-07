package askme.mapper;

import askme.dto.response.UserProfileResponse;
import askme.entity.Question;
import askme.User;
import org.springframework.stereotype.Component;

@Component
public class UserPublicProfileMapper {
    public UserProfileResponse toResponse(User user) {
        return UserProfileResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .bio(user.getBio())
                .build();
    }

    public UserProfileResponse toResponse(Question question) {
        return UserProfileResponse.builder()
                .postId(question.getId())
                .question(question.getQuestion())
                .answer(question.getAnswer())
                .createdAt(question.getCreatedAt())
                .isAnswered(question.getIsAnswered())
                .build();
    }
}