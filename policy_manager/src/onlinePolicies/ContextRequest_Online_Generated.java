
import java.util.HashMap;
import it.unifi.facpl.lib.context.*;
import it.unifi.facpl.lib.util.*;

@SuppressWarnings("all")		
public class ContextRequest_Online_Generated {

	private static ContextRequest CxtReq;


	public static ContextRequest getContextReq(){
	if (CxtReq != null){
		return CxtReq;
	}
	//create map for each category
	HashMap<String, Object> req_action = new HashMap<String, Object>();
	HashMap<String, Object> req_subject = new HashMap<String, Object>();
	HashMap<String, Object> req_resource = new HashMap<String, Object>();
	//add attribute's values
	req_action.put("action-id","CREATE");
	req_subject.put("profile-id","P_1");
	req_resource.put("vm-type","6");
	//add attributes to request
	Request req = new Request("Online_Generated");
	req.addAttribute("action",req_action);
	req.addAttribute("subject",req_subject);
	req.addAttribute("resource",req_resource);
	//context stub: default-one
	CxtReq =  new ContextRequest(req, ContextStub_Default.getInstance());
	return CxtReq;
	}
}
