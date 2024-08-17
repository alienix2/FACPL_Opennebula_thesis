package facpl_things;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.opennebula.client.Client;

import it.unifi.facpl.lib.interfaces.IContextStub;
import it.unifi.facpl.lib.util.AttributeName;
import opennnebula_api.VMsInfo;

public class OpennebulaContextStub implements IContextStub {

	private static OpennebulaContextStub instance;
	private static Client oneClient;
	private static Logger clientLogger;
	
	public static OpennebulaContextStub getInstance(Client oneClient, Logger logger) {
		if (instance == null) {
			inizializeStub(oneClient, logger);
			instance = new OpennebulaContextStub();
		}
		return instance;
	}
	
	private static void inizializeStub(Client oneClient, Logger clientLogger) {
		OpennebulaContextStub.oneClient = oneClient;
		OpennebulaContextStub.clientLogger = clientLogger;
	}

	private OpennebulaContextStub() {
	}
	
	@Override
	public Object getContextValues(AttributeName attribute) {
		// Context Time Value
				if (attribute.getCategory().equals("environment") && attribute.getIDAttribute().equals("time")) {
					return new Date();
				}
				if (attribute.getCategory().equals("environment") && attribute.getIDAttribute().equals("date")) {
					return new Date();
				}
				// True and False constant
				if (attribute.getCategory().equals("environment") && attribute.getIDAttribute().equals("true")) {
					return true;
				}
				if (attribute.getCategory().equals("environment") && attribute.getIDAttribute().equals("false")) {
					return false;
				}
				if (attribute.getCategory().equals("system") && attribute.getIDAttribute().equals("hyper2.vm-ids")) {
					Set<String> runningVMs = new HashSet<String>();
					runningVMs.add("100");
					runningVMs.add("200");
					return runningVMs;
							
					
				}
				// TO Implement your own context here. For example
				/*
				 * if (attribute.getCategory().equals("foo") &&
				 * attribute.getIDAttribute().equals("id")){ return "foo"; }else{ return
				 * null; }
				 */
				return null;
	}
	
	public static Client getOneClient() {
		return oneClient;
	}
	
	public static Logger getLogger() {
		return clientLogger;
	}

}
