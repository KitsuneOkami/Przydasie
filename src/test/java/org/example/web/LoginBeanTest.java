package org.example.web;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import org.example.model.User;
import org.example.service.UserService;
import org.example.util.JSFUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LoginBeanTest {

    private LoginBean loginBean;

    private UserSessionBean userSessionBean;
    private UserService userService;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() throws Exception {
        loginBean = new LoginBean();

        userSessionBean = mock(UserSessionBean.class);
        userService = mock(UserService.class);
        request = mock(HttpServletRequest.class);

        // Inject mocks via reflection
        injectField(loginBean, "userSessionBean", userSessionBean);
        injectField(loginBean, "userService", userService);
        injectField(loginBean, "request", request);
    }

    private void injectField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void init_ShouldCallRedirectIfLogged() {
        try (MockedStatic<JSFUtil> jsfUtilMock = Mockito.mockStatic(JSFUtil.class)) {
            loginBean.init();
            jsfUtilMock.verify(() -> JSFUtil.redirectIfLogged(true, "auctions.xhtml"));
        }
    }

    @Test
    void login_WhenUserAuthenticated_ShouldSetUserAndRedirect() {
        User user = new User();
        user.setName("testuser");

        loginBean.setUsername("testuser");
        loginBean.setPassword("password");

        when(userService.authenticate("testuser", "password")).thenReturn(Optional.of(user));

        String result = loginBean.login();

        verify(userSessionBean).setUser(user);
        assertEquals("/auctions.xhtml?faces-redirect=true", result);
    }

    @Test
    void login_WhenAuthenticationFails_ShouldAddErrorMessageAndReturnNull() {
        loginBean.setUsername("testuser");
        loginBean.setPassword("wrongpassword");

        when(userService.authenticate(anyString(), anyString())).thenReturn(Optional.empty());

        FacesContext facesContext = mock(FacesContext.class);
        MockedStatic<FacesContext> facesContextStatic = Mockito.mockStatic(FacesContext.class);
        facesContextStatic.when(FacesContext::getCurrentInstance).thenReturn(facesContext);

        String result = loginBean.login();

        verify(facesContext).addMessage(eq(null), argThat(msg ->
                msg.getSeverity() == FacesMessage.SEVERITY_ERROR &&
                        msg.getSummary().equals("Login failed") &&
                        msg.getDetail().equals("Invalid credentials")));

        assertNull(result);

        facesContextStatic.close();
    }

    @Test
    void logout_ShouldCallUserSessionLogoutAndRedirect() {
        String result = loginBean.logout();

        verify(userSessionBean).logout();
        assertEquals("/login.xhtml?faces-redirect=true", result);
    }
}
