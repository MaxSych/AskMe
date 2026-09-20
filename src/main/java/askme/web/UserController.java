package askme.web;

import askme.dto.response.InboxResponse;
import askme.service.QuestionsLifecycleService;
import askme.mapper.InboxMapper;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import askme.service.ProfileService;

import java.util.List;

@Controller
public class UserController {
    private final QuestionsLifecycleService answeringService;
    private final ProfileService profileService;
    private final InboxMapper inboxMapper;

    UserController(ProfileService profileService, QuestionsLifecycleService answeringService, InboxMapper inboxMapper) {
       this.profileService = profileService;
        this.answeringService = answeringService;
        this.inboxMapper = inboxMapper;}

    @GetMapping("/profile/{username}")
    public String showProfile(Model model, @PathVariable String username) {
        model.addAttribute("posts", profileService.getPostInformation(username));
        model.addAttribute("user", profileService.getUserInformation(username));

        List<InboxResponse> unanswered = inboxMapper.toInboxPostResponseList(answeringService.getUnansweredPosts(username));
        model.addAttribute("unanswered", unanswered);

        return "profile";
    }
}
