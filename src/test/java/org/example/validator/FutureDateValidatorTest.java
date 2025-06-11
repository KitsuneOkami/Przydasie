package org.example.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FutureDateValidatorTest {

    @Mock
    private FacesContext facesContext;

    @Mock
    private UIComponent uiComponent;

    private FutureDateValidator validator;
    private Map<String, Object> attributes;

    @BeforeEach
    void setUp() {
        validator = new FutureDateValidator();
        attributes = new HashMap<>();
        when(uiComponent.getAttributes()).thenReturn(attributes);
    }

    @Test
    void validate_WhenDateIsNull_ShouldThrowValidatorException() {
        // Arrange
        attributes.put("label", "Test Date");

        // Act & Assert
        ValidatorException exception = assertThrows(ValidatorException.class,
                () -> validator.validate(facesContext, uiComponent, null));

        FacesMessage message = exception.getFacesMessage();
        assertEquals(FacesMessage.SEVERITY_ERROR, message.getSeverity());
        assertEquals("Test Date: Data nie może być pusta", message.getSummary());
    }

    @Test
    void validate_WhenDateIsInPast_ShouldThrowValidatorException() {
        // Arrange
        attributes.put("label", "Test Date");
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

        // Act & Assert
        ValidatorException exception = assertThrows(ValidatorException.class,
                () -> validator.validate(facesContext, uiComponent, pastDate));

        FacesMessage message = exception.getFacesMessage();
        assertEquals(FacesMessage.SEVERITY_ERROR, message.getSeverity());
        assertEquals("Test Date: Podana data jest przeszła", message.getSummary());
    }

    @Test
    void validate_WhenDateIsInFuture_ShouldNotThrowException() {
        // Arrange
        attributes.put("label", "Test Date");
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // Act & Assert
        assertDoesNotThrow(() -> validator.validate(facesContext, uiComponent, futureDate));
    }

    @Test
    void validate_WhenLabelIsNull_ShouldUseEmptyPrefix() {
        // Act & Assert
        ValidatorException exception = assertThrows(ValidatorException.class,
                () -> validator.validate(facesContext, uiComponent, null));

        FacesMessage message = exception.getFacesMessage();
        assertEquals(FacesMessage.SEVERITY_ERROR, message.getSeverity());
        assertEquals("Data nie może być pusta", message.getSummary());
    }

    @Test
    void validate_WhenDateIsInPastAndLabelIsNull_ShouldThrowValidatorException() {
        // Arrange
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        // Not setting any label in attributes map

        // Act & Assert
        ValidatorException exception = assertThrows(ValidatorException.class,
                () -> validator.validate(facesContext, uiComponent, pastDate));

        FacesMessage message = exception.getFacesMessage();
        assertEquals(FacesMessage.SEVERITY_ERROR, message.getSeverity());
        assertEquals("Podana data jest przeszła", message.getSummary());
    }
}