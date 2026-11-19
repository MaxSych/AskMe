package askme.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnswerRequest(
        @NotBlank(message = "Answer cannot be empty")
        @Size(max = 200, message = "Answer is too long")
        String answer
) {}