package askme.dto.response;

import lombok.Builder;

import java.time.LocalDate;


@Builder
public record UserProfileResponse(
        //User data
        Long userId,
        Long postId,
        String username,
        String displayName,
        String bio,
        // Question data
        String question,
        String answer,
        Boolean isAnswered,
        LocalDate createdAt

) {}