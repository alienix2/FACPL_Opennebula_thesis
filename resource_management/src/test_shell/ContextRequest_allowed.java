package test_shell;

import java.util.HashMap;
import it.unifi.facpl.lib.context.*;
import it.unifi.facpl.lib.util.*;

@SuppressWarnings("all")		
public class ContextRequest_allowed {

	private static ContextRequest CxtReq;


	public static ContextRequest getContextReq(){
	if (CxtReq != null){
		return CxtReq;
	}
	//create map for each category
	HashMap<String, Object> req_action = new HashMap<String, Object>();
	HashMap<String, Object> req_resource = new HashMap<String, Object>();
	//add attribute's values
	req_action.put("id","READ");
	req_action.put("text","allowed");
	req_action.put("command1","cowsay I was allowed hurray");
	req_action.put("command2",":");
	req_resource.put("boss_email","mattemoni@gmail.com");
	//add attributes to request
	Request req = new Request("allowed");
	req.addAttribute("action",req_action);
	req.addAttribute("resource",req_resource);
	//context stub: default-one
	CxtReq =  new ContextRequest(req, ContextStub_Default.getInstance());
	return CxtReq;
	}
}
