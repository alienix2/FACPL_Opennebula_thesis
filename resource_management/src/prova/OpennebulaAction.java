package prova;

import java.util.HashMap;
import java.util.logging.Logger;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;

import it.unifi.facpl.lib.interfaces.*;
import opennnebula_api.HostInfo;
import opennnebula_api.ReleaseVM;
import opennnebula_api.ShutdownVM;
import utilities.FileLoggerFactory;

@SuppressWarnings("all")
public class OpennebulaAction{

	public static HashMap<String, IPepAction> getPepActions() {
		Client oneClient = null;
		try {
			oneClient = new Client("oneadmin:Panzerotto", "http://localhost:2633/RPC2");
		} catch (ClientConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger clientLogger = FileLoggerFactory.make("commandOutput.log");
		/*
		* Set your own pep action e.g. HashMap<String,new ***** class Action extending IPepAction***()
		* 
		* pepAction = new HashMap<String,IPepAction>(); 
		* pepAction.put("action", Action.class); return
		* pepAction;
		*/
		HashMap<String, IPepAction> pepAction = new HashMap<String, IPepAction>();
		pepAction.put("release", new ReleaseVM(oneClient, clientLogger));
		
		return pepAction;
   	}
	
}
