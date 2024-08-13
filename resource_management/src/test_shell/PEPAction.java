package test_shell;

import java.util.HashMap;
import java.util.logging.Logger;

import it.unifi.facpl.lib.interfaces.*;
import opennebula_shell.CreateVM;
import opennebula_shell.ShutdownVM;
import opennebula_shell.SuspendVM;
import utilities.FileLoggerFactory;

@SuppressWarnings("all")
public class PEPAction{

	public static HashMap<String, IPepAction> getPepActions() {
		Logger logger = FileLoggerFactory.make("commandOutput.txt");
		/*
		* Set your own pep action e.g. HashMap<String,new ***** class Action extending IPepAction***()
		* 
		* pepAction = new HashMap<String,IPepAction>(); 
		* pepAction.put("action", Action.class); return
		* pepAction;
		*/
		HashMap<String, IPepAction> pepAction = new HashMap<String, IPepAction>();
		pepAction.put("create", new CreateVM(logger));
		pepAction.put("freeze", new SuspendVM(logger));
		pepAction.put("release", new ShutdownVM(logger));
		pepAction.put("shellCommand", new ShellCommand());
		return pepAction;
   	}
	
}
