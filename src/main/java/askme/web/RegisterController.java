package askme.web;

import askme.dto.request.RegistrationRequest;
import askme.service.AuthService;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class RegisterController {
    private final AuthService authService;

    public RegisterController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String getLogin() {
        return "register";
    }

    @GetMapping("/main")
    public String getMain(){
        return "main";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegistrationRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            authService.register(request);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
        return "redirect:/main";
}
}
