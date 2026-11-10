package askme.web;

import askme.User;
import askme.service.AskingQuestionsService;
import askme.service.QuestionsLifecycleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class QuestionsController {
    private final QuestionsLifecycleService questionsLifecycleService;
    private final AskingQuestionsService askingQuestionsService;

    public QuestionsController(AskingQuestionsService questionsService, QuestionsLifecycleService questionsLifecycleService){
        this.askingQuestionsService = questionsService;
        this.questionsLifecycleService = questionsLifecycleService;
    }

    @PostMapping("/add")
    public String addQuestion(@RequestParam("text") String questionText, @RequestParam("userName") String userName, RedirectAttributes redirectAttributes) {
        if (questionText.trim().length() < 5) {
            redirectAttributes.addFlashAttribute("errorMessage", "Text is too short!");
            return "redirect:/profile/" + userName;
        }
        try {
            askingQuestionsService.addQuestion( questionText, userName);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userName);
        }


        return "redirect:/profile/" + userName;
    }

    @PostMapping("/questions/{id}/delete")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id, @AuthenticationPrincipal User user){
        questionsLifecycleService.deleteQuestion(id, user.getId());
        return ResponseEntity.noContent().build();

    }
}