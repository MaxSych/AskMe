package askme.service;

import askme.User;
import askme.data.UserRepository;
import askme.data.QuestionRepository;
import askme.entity.Question;
import askme.mapper.UserPublicProfileMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import askme.dto.response.UserProfileResponse;

import java.util.List;

@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final UserPublicProfileMapper userPublicProfileMapper;

    public ProfileService(UserRepository userRepository, QuestionRepository questionRepository, UserPublicProfileMapper userPublicProfileMapper) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.userPublicProfileMapper = userPublicProfileMapper;
    }

    public UserProfileResponse getUserInformation(@PathVariable String username){
        User userFromDb = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileResponse mappedUserFromBd = userPublicProfileMapper.toResponse(userFromDb);

        return mappedUserFromBd;
    }

    public List<Question> getPostInformation(@PathVariable String username){
        User userFromDb = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Question> unansweredQuestions = questionRepository.findByUserIdAndIsAnsweredTrue(userFromDb.getId());

        return unansweredQuestions;
    }
}
