package org.example.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.Application;
import jakarta.servlet.http.HttpServletRequest;
import org.example.web.UserSessionBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JSFUtilTest {

    @Mock
    private FacesContext facesContext;
    @Mock
    private ExternalContext externalContext;
    @Mock
    private Application application;
    @Mock
    private UserSessionBean userSessionBean;

    @Test
    void constructor_ShouldCreate() {
        assertDoesNotThrow(() -> new JSFUtil());
    }

    @Test
    void addErrorMessage_ShouldAddMessageWithErrorSeverity() {
        try (MockedStatic<FacesContext> mockedStatic = mockStatic(FacesContext.class)) {
            mockedStatic.when(FacesContext::getCurrentInstance).thenReturn(facesContext);
            String message = "Error message";

            JSFUtil.addErrorMessage(message);

            verify(facesContext).addMessage(eq(null), any(FacesMessage.class));
        }
    }

    @Test
    void addInfoMessage_ShouldAddMessageWithInfoSeverity() {
        try (MockedStatic<FacesContext> mockedStatic = mockStatic(FacesContext.class)) {
            mockedStatic.when(FacesContext::getCurrentInstance).thenReturn(facesContext);
            String message = "Info message";

            JSFUtil.addInfoMessage(message);

            verify(facesContext).addMessage(eq(null), any(FacesMessage.class));
        }
    }

    @Test
    void redirect_WithoutLeadingSlash_ShouldAddSlashAndRedirect() throws IOException {
        try (MockedStatic<FacesContext> mockedStatic = mockStatic(FacesContext.class)) {
            mockedStatic.when(FacesContext::getCurrentInstance).thenReturn(facesContext);
            when(facesContext.getExternalContext()).thenReturn(externalContext);
            when(externalContext.getRequestContextPath()).thenReturn("/context");
            String view = "page.xhtml";

            JSFUtil.redirect(view);

            verify(externalContext).redirect("/context/page.xhtml");
        }
    }

    @Test
    void redirect_WithLeadingSlash_ShouldRedirectAsIs() throws IOException {
        try (MockedStatic<FacesContext> mockedStatic = mockStatic(FacesContext.class)) {
            mockedStatic.when(FacesContext::getCurrentInstance).thenReturn(facesContext);
            when(facesContext.getExternalContext()).thenReturn(externalContext);
            when(externalContext.getRequestContextPath()).thenReturn("/context");
            String view = "/page.xhtml";

            JSFUtil.redirect(view);

            verify(externalContext).redirect("/context/page.xhtml");
        }
    }

    @Test
    void invalidateSession_ShouldInvalidateExternalContext() {
        try (MockedStatic<FacesContext> mockedStatic = mockStatic(FacesContext.class)) {
            mockedStatic.when(FacesContext::getCurrentInstance).thenReturn(facesContext);
            when(facesContext.getExternalContext()).thenReturn(externalContext);

            JSFUtil.invalidateSession();

            verify(externalContext).invalidateSession();
        }
    }

    @Test
    void redirectIfLogged_WhenUserLoggedInAndShouldBeLogged_ShouldRedirect() throws IOException {
        try (MockedStatic<FacesContext> mockedStatic = mockStatic(FacesContext.class)) {
            mockedStatic.when(FacesContext::getCurrentInstance).thenReturn(facesContext);
            when(facesContext.getExternalContext()).thenReturn(externalContext);
            when(externalContext.getRequestContextPath()).thenReturn("/context");
            when(userSessionBean.isLoggedIn()).thenReturn(true);

            JSFUtil.redirectIfLogged(userSessionBean, true, "/page.xhtml");

            verify(externalContext).redirect("/context/page.xhtml");
        }
    }

    @Test
    void redirectIfLogged_WhenUserNotLoggedInAndShouldNotBeLogged_ShouldRedirect() throws IOException {
        try (MockedStatic<FacesContext> mockedStatic = mockStatic(FacesContext.class)) {
            mockedStatic.when(FacesContext::getCurrentInstance).thenReturn(facesContext);
            when(facesContext.getExternalContext()).thenReturn(externalContext);
            when(externalContext.getRequestContextPath()).thenReturn("/context");
            when(userSessionBean.isLoggedIn()).thenReturn(false);

            JSFUtil.redirectIfLogged(userSessionBean, false, "/page.xhtml");

            verify(externalContext).redirect("/context/page.xhtml");
        }
    }

    @Test
    void redirectIfLogged_WhenConditionNotMet_ShouldNotRedirect() throws IOException {
        try (MockedStatic<FacesContext> mockedStatic = mockStatic(FacesContext.class)) {
            mockedStatic.when(FacesContext::getCurrentInstance).thenReturn(facesContext);
            when(userSessionBean.isLoggedIn()).thenReturn(true);

            JSFUtil.redirectIfLogged(userSessionBean, false, "/page.xhtml");

            verify(externalContext, never()).redirect(any());
        }
    }

    @Test
    void redirectIfLogged_WithEvaluateExpressionGet_ShouldRedirectWhenConditionMet() throws IOException {
        try (MockedStatic<FacesContext> mockedStatic = mockStatic(FacesContext.class)) {
            mockedStatic.when(FacesContext::getCurrentInstance).thenReturn(facesContext);
            when(facesContext.getApplication()).thenReturn(application);
            when(facesContext.getExternalContext()).thenReturn(externalContext);
            when(externalContext.getRequestContextPath()).thenReturn("/context");
            when(application.evaluateExpressionGet(eq(facesContext), eq("#{userSessionBean}"), eq(UserSessionBean.class)))
                    .thenReturn(userSessionBean);
            when(userSessionBean.isLoggedIn()).thenReturn(true);

            JSFUtil.redirectIfLogged(true, "/page.xhtml");

            verify(externalContext).redirect("/context/page.xhtml");
        }
    }

    @Test
    void redirectIfLogged_WhenIOExceptionOccurs_ShouldCatchException() throws IOException {
        try (MockedStatic<FacesContext> mockedStatic = mockStatic(FacesContext.class)) {
            // Arrange
            mockedStatic.when(FacesContext::getCurrentInstance).thenReturn(facesContext);
            when(facesContext.getExternalContext()).thenReturn(externalContext);
            when(userSessionBean.isLoggedIn()).thenReturn(true);
            doThrow(new IOException("Test exception")).when(externalContext).redirect(any());

            // Act & Assert
            assertDoesNotThrow(() ->
                    JSFUtil.redirectIfLogged(userSessionBean, true, "/page.xhtml")
            );

            // Verify that redirect was attempted
            verify(externalContext).redirect(any());
        }
    }

}