package org.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.web.UserSessionBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthFilterTest {

    @Mock
    private UserSessionBean userSessionBean;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthFilter authFilter;

    private static final String CONTEXT_PATH = "/app";
    private static final String LOGIN_PAGE = "/login.xhtml";

    /*
    @BeforeEach
    void setUp() {
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);
    }
    */

    @Test
    void doFilter_WhenUserLoggedInAndSessionExists_ShouldContinueChain() throws IOException, ServletException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(userSessionBean.isLoggedIn()).thenReturn(true);

        // Act
        authFilter.doFilter(request, response, chain);

        // Assert
        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void doFilter_WhenUserNotLoggedIn_ShouldRedirectToLogin() throws IOException, ServletException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(userSessionBean.isLoggedIn()).thenReturn(false);
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);

        // Act
        authFilter.doFilter(request, response, chain);

        // Assert
        verify(response).sendRedirect(CONTEXT_PATH + LOGIN_PAGE);
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    void doFilter_WhenSessionIsNull_ShouldRedirectToLogin() throws IOException, ServletException {
        // Arrange
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);

        // Act
        authFilter.doFilter(request, response, chain);

        // Assert
        verify(response).sendRedirect(CONTEXT_PATH + LOGIN_PAGE);
        verify(chain, never()).doFilter(any(), any());
    }
}