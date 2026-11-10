package askme.dto.response;

import lombok.Builder;

import java.time.LocalDate;


@Builder
public record UserProfileResponse(
        //User data
        Long userId,
        String username,
        String displayName,
        String bio


) {}

