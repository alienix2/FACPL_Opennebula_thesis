package facpl_things;

import java.util.HashMap;
import java.util.logging.Logger;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;

import it.unifi.facpl.lib.interfaces.*;
import opennnebula_api.HostInfo;
import opennnebula_api.ShutdownVM;
import utilities.FileLoggerFactory;

@SuppressWarnings("all")
public class PEPAction{

	public static HashMap<String, IPepAction> getPepActions() {
		Client oneClient = OpennebulaContextStub.getOneClient();
		Logger clientLogger = OpennebulaContextStub.getLogger();
		/*
		* Set your own pep action e.g. HashMap<String,new ***** class Action extending IPepAction***()
		* 
		* pepAction = new HashMap<String,IPepAction>(); 
		* pepAction.put("action", Action.class); return
		* pepAction;
		*/
		HashMap<String, IPepAction> pepAction = new HashMap<String, IPepAction>();
		pepAction.put("release", new ShutdownVM(oneClient, clientLogger));
		
		return null;
   	}
	
}
