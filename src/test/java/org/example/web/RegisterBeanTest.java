package org.example.web;

import org.example.model.User;
import org.example.model.User.Role;
import org.example.service.UserService;
import org.example.util.JSFUtil;
import org.example.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterBeanTest {

    private RegisterBean registerBean;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        registerBean = new RegisterBean();

        // Inject mock userService via reflection
        Field userServiceField = RegisterBean.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(registerBean, userService);
    }

    @Test
    void init_ShouldCallRedirectIfLogged() {
        RegisterBean registerBean = new RegisterBean();

        try (MockedStatic<JSFUtil> jsfUtilMock = Mockito.mockStatic(JSFUtil.class)) {
            registerBean.init();

            jsfUtilMock.verify(() -> JSFUtil.redirectIfLogged(true, "auctions.xhtml"));
        }
    }

    @Test
    void register_WhenPasswordsDoNotMatch_ShouldAddErrorMessageAndReturnNull() {
        registerBean.setUsername("user");
        registerBean.setEmail("user@example.com");
        registerBean.setPassword("password1");
        registerBean.setConfirmPassword("password2");

        try (MockedStatic<JSFUtil> jsfUtilMock = Mockito.mockStatic(JSFUtil.class)) {
            String result = registerBean.register();

            assertNull(result);
            jsfUtilMock.verify(() -> JSFUtil.addErrorMessage("Hasła są różne"));
            verifyNoInteractions(userService);
        }
    }

    @Test
    void register_WhenUsernameFieldIsNull_ShouldAddErrorMessageAndReturnNull() {
        // Test with username null
        registerBean.setUsername(null);
        registerBean.setEmail("email@example.com");
        registerBean.setPassword("password");
        registerBean.setConfirmPassword("password");

        try (MockedStatic<JSFUtil> jsfUtilMock = Mockito.mockStatic(JSFUtil.class)) {
            String result = registerBean.register();

            assertNull(result);
            jsfUtilMock.verify(() -> JSFUtil.addErrorMessage("Wszystkie pola są wymagane"));
            verifyNoInteractions(userService);
        }
    }

    @Test
    void register_WhenUsernameFieldIsEmpty_ShouldAddErrorMessageAndReturnNull() {
        // Test with username empty
        registerBean.setUsername("");
        registerBean.setEmail("email@example.com");
        registerBean.setPassword("password");
        registerBean.setConfirmPassword("password");

        try (MockedStatic<JSFUtil> jsfUtilMock = Mockito.mockStatic(JSFUtil.class)) {
            String result = registerBean.register();

            assertNull(result);
            jsfUtilMock.verify(() -> JSFUtil.addErrorMessage("Wszystkie pola są wymagane"));
            verifyNoInteractions(userService);
        }
    }

    @Test
    void register_WhenEmailFieldIsNull_ShouldAddErrorMessageAndReturnNull() {
        // Test with email null
        registerBean.setUsername("user");
        registerBean.setEmail(null);
        registerBean.setPassword("password");
        registerBean.setConfirmPassword("password");

        try (MockedStatic<JSFUtil> jsfUtilMock = Mockito.mockStatic(JSFUtil.class)) {
            String result = registerBean.register();

            assertNull(result);
            jsfUtilMock.verify(() -> JSFUtil.addErrorMessage("Wszystkie pola są wymagane"));
            verifyNoInteractions(userService);
        }
    }

    @Test
    void register_WhenEmailFieldIsEmpty_ShouldAddErrorMessageAndReturnNull() {
        // Test with email empty
        registerBean.setUsername("user");
        registerBean.setEmail("");
        registerBean.setPassword("password");
        registerBean.setConfirmPassword("password");

        try (MockedStatic<JSFUtil> jsfUtilMock = Mockito.mockStatic(JSFUtil.class)) {
            String result = registerBean.register();

            assertNull(result);
            jsfUtilMock.verify(() -> JSFUtil.addErrorMessage("Wszystkie pola są wymagane"));
            verifyNoInteractions(userService);
        }
    }

    @Test
    void register_WhenPasswordFieldIsEmpty_ShouldAddErrorMessageAndReturnNull() {
        // Test with password empty
        registerBean.setUsername("user");
        registerBean.setEmail("email@example.com");
        registerBean.setPassword("");
        registerBean.setConfirmPassword("");

        try (MockedStatic<JSFUtil> jsfUtilMock = Mockito.mockStatic(JSFUtil.class)) {
            String result = registerBean.register();

            assertNull(result);
            jsfUtilMock.verify(() -> JSFUtil.addErrorMessage("Wszystkie pola są wymagane"));
            verifyNoInteractions(userService);
        }
    }

    @Test
    void register_WhenUsernameOrEmailTaken_ShouldAddErrorMessageAndReturnNull() {
        registerBean.setUsername("user");
        registerBean.setEmail("user@example.com");
        registerBean.setPassword("password");
        registerBean.setConfirmPassword("password");

        when(userService.usernameOrEmailTaken("user", "user@example.com")).thenReturn(true);

        try (MockedStatic<JSFUtil> jsfUtilMock = Mockito.mockStatic(JSFUtil.class)) {
            String result = registerBean.register();

            assertNull(result);
            jsfUtilMock.verify(() -> JSFUtil.addErrorMessage("Użytkownik bądź email już istnieje"));
            verify(userService).usernameOrEmailTaken("user", "user@example.com");
            verify(userService, never()).saveUser(any());
        }
    }

    @Test
    void register_WhenValidInput_ShouldSaveUserAddInfoMessageAndReturnRedirect() {
        registerBean.setUsername("user");
        registerBean.setEmail("user@example.com");
        registerBean.setPassword("password");
        registerBean.setConfirmPassword("password");

        when(userService.usernameOrEmailTaken("user", "user@example.com")).thenReturn(false);

        try (MockedStatic<JSFUtil> jsfUtilMock = Mockito.mockStatic(JSFUtil.class);
             MockedStatic<PasswordUtil> passwordUtilMock = Mockito.mockStatic(PasswordUtil.class)) {

            passwordUtilMock.when(() -> PasswordUtil.hash("password")).thenReturn("hashedPassword");

            String result = registerBean.register();

            assertEquals("/login.xhtml?faces-redirect=true", result);

            verify(userService).usernameOrEmailTaken("user", "user@example.com");
            verify(userService).saveUser(argThat(user ->
                    user.getName().equals("user") &&
                            user.getEmail().equals("user@example.com") &&
                            user.getPassword().equals("hashedPassword") &&
                            user.getRole() == Role.USER
            ));

            jsfUtilMock.verify(() -> JSFUtil.addInfoMessage("Pomyślnie zarejestrowano użytkownika"));
        }
    }
}
