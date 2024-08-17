package prova;

import java.util.Date;
import java.util.HashSet;
import it.unifi.facpl.lib.util.Set;
import java.util.logging.Logger;

import org.opennebula.client.Client;

import it.unifi.facpl.lib.interfaces.IContextStub;
import it.unifi.facpl.lib.util.AttributeName;

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
				if (attribute.getCategory().equals("system") && attribute.getIDAttribute().equals("hyper1.vm-ids")) {
					Set runningVMs = new Set();
					System.out.println((Object)runningVMs instanceof Set);
					runningVMs.addValue("nome2");
					runningVMs.addValue("200");
					return runningVMs;
				}
				if (attribute.getCategory().equals("system") && attribute.getIDAttribute().equals("hyper2.vm-ids")) {
					Set runningVMs = new Set();
					System.out.println((Object)runningVMs instanceof Set);
					runningVMs.addValue("100");
					runningVMs.addValue("200");
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
