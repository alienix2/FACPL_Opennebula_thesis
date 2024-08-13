package opennebula_stub;

import java.util.Date;

import it.unifi.facpl.lib.interfaces.IContextStub;
import it.unifi.facpl.lib.util.AttributeName;

public class OpennebulaContextStub implements IContextStub {

	private static OpennebulaContextStub instance;
	
	public static OpennebulaContextStub getInstance() {
		if (instance == null) {
			instance = new OpennebulaContextStub();
		}
		return instance;
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
				
				if (attribute.getCategory().equals("prova") && attribute.getIDAttribute().equals("hello")) {
					return "Sono riuscito a modificare";
				}
				// TO Implement your own context here. For example
				/*
				 * if (attribute.getCategory().equals("foo") &&
				 * attribute.getIDAttribute().equals("id")){ return "foo"; }else{ return
				 * null; }
				 */
				return null;
	}

}
