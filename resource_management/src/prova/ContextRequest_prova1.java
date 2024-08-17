package prova;

import java.util.HashMap;
import java.util.logging.Logger;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;

import it.unifi.facpl.lib.context.*;
import it.unifi.facpl.lib.util.*;
import utilities.FileLoggerFactory;

@SuppressWarnings("all")		
public class ContextRequest_prova1 {

	private static ContextRequest CxtReq;


	public static ContextRequest getContextReq(){
		Client oneClient = null;
		try {
			oneClient = new Client("oneadmin:Panzerotto", "http://localhost:2633/RPC2");
		} catch (ClientConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger logger = FileLoggerFactory.make("commandOutput.log");
		
	if (CxtReq != null){
		return CxtReq;
	}
	//create map for each category
	HashMap<String, Object> req_action = new HashMap<String, Object>();
	HashMap<String, Object> req_subject = new HashMap<String, Object>();
	HashMap<String, Object> req_resource = new HashMap<String, Object>();
	//add attribute's values
	req_action.put("action-id","RELEASE");
	req_subject.put("profile-id","P_1");
	req_resource.put("vm-id","nome2");
	//add attributes to request
	Request req = new Request("prova1");
	req.addAttribute("action",req_action);
	req.addAttribute("subject",req_subject);
	req.addAttribute("resource",req_resource);
	//context stub: default-one
	CxtReq =  new ContextRequest(req, OpennebulaContextStub.getInstance(oneClient, logger));
	return CxtReq;
	}
}
