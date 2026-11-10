package askme.dto.response;

import lombok.Builder;

import java.time.LocalDate;


@Builder
public record QuestionResponse(
            // Question data
            Long questionId,
            String textQuestion,
            String answer,
            Boolean isAnswered,
            LocalDate createdAt
) {}


