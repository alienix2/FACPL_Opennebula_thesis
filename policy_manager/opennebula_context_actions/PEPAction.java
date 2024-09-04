import java.util.HashMap;
import it.unifi.facpl.lib.interfaces.*;
import opennebula_api.CreateVM;
import opennebula_api.FreezeVM;
import opennebula_api.ReleaseVM;

@SuppressWarnings("all")
public class PEPAction{

	public static HashMap<String, IPepAction> getPepActions() {
		ContextStub_Default.getInstance();
		HashMap<String, IPepAction> pepAction = new HashMap<String, IPepAction>();
		pepAction.put("release", 
				new ReleaseVM(ContextStub_Default.getONContext()));
		pepAction.put("create", 
				new CreateVM(ContextStub_Default.getONContext()));
		pepAction.put("freeze", 
				new FreezeVM(ContextStub_Default.getONContext()));
		
		return pepAction;
   	}
}
