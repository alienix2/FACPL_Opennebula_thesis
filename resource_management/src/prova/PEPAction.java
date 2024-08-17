package prova;

import java.util.HashMap;
import it.unifi.facpl.lib.interfaces.*;
import opennebula_api.CreateVM;
import opennebula_api.ReleaseVM;
import opennebula_api.ShutdownVM;

@SuppressWarnings("all")
public class PEPAction{

	public static HashMap<String, IPepAction> getPepActions() {
		ContextStub_Default.getInstance();
		/*
		* Set your own pep action e.g. HashMap<String,new ***** class Action extending IPepAction***()
		* 
		* pepAction = new HashMap<String,IPepAction>(); 
		* pepAction.put("action", Action.class); return
		* pepAction;
		*/
		HashMap<String, IPepAction> pepAction = new HashMap<String, IPepAction>();
		pepAction.put("release", 
				new ReleaseVM(ContextStub_Default.getOneClient(), ContextStub_Default.getLogger()));
		pepAction.put("create", 
				new CreateVM(ContextStub_Default.getOneClient(), ContextStub_Default.getLogger()));
		pepAction.put("freeze", 
				new ShutdownVM(ContextStub_Default.getOneClient(), ContextStub_Default.getLogger()));
		
		return pepAction;
   	}
	
}
