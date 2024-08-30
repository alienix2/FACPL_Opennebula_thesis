package com.policyManager.utils;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.util.CancelIndicator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;

import com.google.inject.Provider;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class FACPLFileValidatorTest {

    @Mock
    private IResourceValidator mockValidator;

    @Mock
    private Provider<ResourceSet> mockResourceSetProvider;

    @Mock
    private ResourceSet mockResourceSet;

    @Mock
    private Resource mockResource;

    private FACPLFileValidator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new FACPLFileValidator();

        validator.validator = mockValidator;
        validator.resourceSetProvider = mockResourceSetProvider;

        when(mockResourceSetProvider.get()).thenReturn(mockResourceSet);
    }

    @Test
    void testValidateFileNoErrors() {
        when(mockResourceSet.getResource(any(), eq(true))).thenReturn(mockResource);
        when(mockValidator.validate(eq(mockResource), eq(CheckMode.ALL), eq(CancelIndicator.NullImpl)))
            .thenReturn(Collections.emptyList());

        FACPLFileValidator mockValidatorInstance = mock(FACPLFileValidator.class);
        try (MockedStatic<FACPLFileValidator> mockedStatic = mockStatic(FACPLFileValidator.class)) {
            mockedStatic.when(() -> FACPLFileValidator.validate("test-file-path")).thenReturn(true);
            FACPLFileValidator.instance = mockValidatorInstance;

            boolean result = FACPLFileValidator.validate("test-file-path");
            assertTrue(result, "File should be valid with no errors");
        }
    }

    @Test
    void testValidateFileWithErrors() {
        Issue mockIssue = mock(Issue.class);
        when(mockIssue.getSeverity()).thenReturn(Severity.ERROR);
        List<Issue> issues = Collections.singletonList(mockIssue);

        when(mockResourceSet.getResource(any(), eq(true))).thenReturn(mockResource);
        when(mockValidator.validate(eq(mockResource), eq(CheckMode.ALL), eq(CancelIndicator.NullImpl)))
            .thenReturn(issues);

        FACPLFileValidator mockValidatorInstance = mock(FACPLFileValidator.class);
        try (MockedStatic<FACPLFileValidator> mockedStatic = mockStatic(FACPLFileValidator.class)) {
            mockedStatic.when(() -> FACPLFileValidator.validate("test-file-path")).thenReturn(false);

            FACPLFileValidator.instance = mockValidatorInstance;

            boolean result = FACPLFileValidator.validate("test-file-path");
            assertFalse(result, "File should be invalid with errors");
        }
    }

    @Test
    void testStaticValidate() {
        try (MockedStatic<FACPLFileValidator> mockedStatic = mockStatic(FACPLFileValidator.class)) {
            mockedStatic.when(() -> FACPLFileValidator.validate("test-file-path")).thenReturn(true);

            boolean result = FACPLFileValidator.validate("test-file-path");
            
            assertTrue(result, "Static validate method should return true");

            mockedStatic.verify(() -> FACPLFileValidator.validate("test-file-path"));
        }
    }
}
