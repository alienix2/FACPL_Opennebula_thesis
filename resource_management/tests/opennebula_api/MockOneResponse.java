package opennebula_api;

import org.opennebula.client.OneResponse;

public class MockOneResponse extends OneResponse {

	private boolean error;
    private String message;
    private String errorMessage;
    
    public MockOneResponse(boolean error, String message, String errorMessage) {
    	super(error, message);
        this.error = error;
        this.message = message;
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean isError() {
        return error;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}