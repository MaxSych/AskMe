package askme.service;

import askme.data.UserRepository;
import askme.dto.request.RegistrationRequest;
import askme.entity.User;
import askme.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_WhenPasswordsDontMatch_ThrowException() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@gg.com");
        request.setPassword("password");
        request.setConfirmPassword("differentPassword");

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Passwords do not match");

        verifyNoInteractions(userRepository);
    }

    @Test
    void register_WhenEmailAlreadyExists_ThrowException() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("exists@gg.com");
        request.setPassword("password123");
        request.setConfirmPassword("password123");

        when(userRepository.existsByEmail("exists@gg.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User already exists with email: " + request.getEmail());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_WithValidData_ShouldSaveUserWithEncodedPassword() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("new@test.com");
        request.setPassword("password123");
        request.setConfirmPassword("password123");

        User fakeUser = new User();
        fakeUser.setEmail("new@test.com");


        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(fakeUser);
        when(passwordEncoder.encode("password123")).thenReturn("hashed_secret");

        authService.register(request);

        assertThat(fakeUser.getPassword()).isEqualTo("hashed_secret");
        assertThat(fakeUser.getRole()).isEqualTo("ROLE_USER");

        verify(userRepository).saveAndFlush(any(User.class));
    }
}