package com.policyManager.utils;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.util.CancelIndicator;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

import java.util.List;

public class FACPLFileValidator {
	
	static FACPLFileValidator instance;

    @Inject IResourceValidator validator;

	@Inject Provider<ResourceSet> resourceSetProvider;
    
	private static void getInstance() {
		Injector injector = new it.unifi.xtext.facpl.Facpl2StandaloneSetupGenerated()
				.createInjectorAndDoEMFRegistration();
		instance = injector.getInstance(FACPLFileValidator.class);

	}

    public static boolean validate(String filePath) {	
		if (instance == null) {
			getInstance();
		}		
		return instance.validateFile(filePath);
    }
    
    private boolean validateFile(String filePath) {
    	ResourceSet set = resourceSetProvider.get();
		Resource resource = set.getResource(URI.createURI(filePath), true);
		
		List<Issue> issues = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);

		boolean hasErrors = issues.stream()
                .anyMatch(issue -> issue.getSeverity() == Severity.ERROR);
       
        return !hasErrors;
    }
}
