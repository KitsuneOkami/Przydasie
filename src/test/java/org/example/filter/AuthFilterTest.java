package org.example.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private HttpSession session;

    private AuthFilter authFilter;

    @BeforeEach
    void setUp() {
        authFilter = new AuthFilter();
    }

    @Test
    void doFilter_WhenUserIsLoggedIn_ShouldProceedWithChain() throws IOException, ServletException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("testUser");

        // Act
        authFilter.doFilter(request, response, chain);

        // Assert
        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void doFilter_WhenSessionIsNull_ShouldRedirectToLogin() throws IOException, ServletException {
        // Arrange
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/app");

        // Act
        authFilter.doFilter(request, response, chain);

        // Assert
        verify(response).sendRedirect("/app/login.xhtml");
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    void doFilter_WhenSessionExistsButNoUsername_ShouldRedirectToLogin() throws IOException, ServletException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("username")).thenReturn(null);
        when(request.getContextPath()).thenReturn("/app");

        // Act
        authFilter.doFilter(request, response, chain);

        // Assert
        verify(response).sendRedirect("/app/login.xhtml");
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    void doFilter_WhenEmptyUsername_ShouldRedirectToLogin() throws IOException, ServletException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("");
        when(request.getContextPath()).thenReturn("/app");

        // Act
        authFilter.doFilter(request, response, chain);

        // Assert
        verify(response).sendRedirect("/app/login.xhtml");
        verify(chain, never()).doFilter(any(), any());
    }
}