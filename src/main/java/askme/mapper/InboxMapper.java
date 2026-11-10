package askme.mapper;

import askme.dto.response.InboxQuestionResponse;
import askme.entity.Question;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class InboxMapper {
    public InboxQuestionResponse toInboxResponse(Question question){
        if (question == null) return null;

        return new InboxQuestionResponse(question.getId(), question.getText_question());
    }

    public List<InboxQuestionResponse> toInboxPostResponseList(List<Question> posts) {
        if (posts == null || posts.isEmpty()) {
            return Collections.emptyList();
        }

        List<InboxQuestionResponse> inboxResponses = new ArrayList<>();
        for ( Question post : posts) {

            InboxQuestionResponse inboxResponse = toInboxResponse(post);
            inboxResponses.add(inboxResponse);
        }
        return inboxResponses;
    }
}
