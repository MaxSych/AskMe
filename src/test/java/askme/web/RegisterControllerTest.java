package askme.web;

import askme.dto.request.RegistrationRequest;
import askme.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RegisterController.class)
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void getRegisterPage_ShouldReturnRegisterView() throws Exception{
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void getMainPage_ShouldReturnMainView() throws Exception{
        mockMvc.perform(get("/main"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"));
    }

    @Test
    void register_WithInvalidData_ShouldReturnFormWithErrors() throws Exception {

        mockMvc.perform(post("/register")
                        .param("username", "testuser")
                        .param("email", "")
                        .param("password", "123")
                        .param("confirmPassword", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("registrationRequest", "email"));

        verifyNoInteractions(authService);
    }

    @Test
    void register_WhenServiceThrowsException_ShouldReturnFormWithErrorMessage() throws Exception {
        doThrow(new IllegalArgumentException("Passwords do not match"))
                .when(authService).register(any(RegistrationRequest.class));

        mockMvc.perform(post("/register")
                        .param("username", "testuser")
                        .param("email", "test@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "differentPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("errorMessage", "Passwords do not match"));

        verify(authService, times(1)).register(any(RegistrationRequest.class));
    }

    @Test
    void register_WithValidData_ShouldRedirectToMain() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "testuser")
                        .param("email", "test@gg.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/main"));

        verify(authService, times(1)).register(any(RegistrationRequest.class));
    }
}
