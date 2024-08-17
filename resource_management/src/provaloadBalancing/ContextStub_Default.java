package provaloadBalancing;

import java.util.Date;
import java.util.UUID;

import it.unifi.facpl.lib.util.Set;
import opennebula_api.HostInfo;
import opennebula_api.VMsInfo;
import utilities.FileLoggerFactory;

import java.util.logging.Logger;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;

import it.unifi.facpl.lib.interfaces.IContextStub;
import it.unifi.facpl.lib.util.AttributeName;

public class ContextStub_Default implements IContextStub {

	private static ContextStub_Default instance;
	private static Client oneClient;
	private static Logger clientLogger;
	private static VMsInfo vmsInfo;
	private static HostInfo hostInfo;
	
	public static ContextStub_Default getInstance() {
		if (instance == null) {
			try {
				ContextStub_Default.oneClient = new Client("oneadmin:Panzerotto", "http://localhost:2633/RPC2");
			} catch (ClientConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Logger logger = FileLoggerFactory.make("commandOutput.log");
			inizializeStub(oneClient, logger);
			instance = new ContextStub_Default();
		}
		return instance;
	}
	
	private static void inizializeStub(Client oneClient, Logger clientLogger) {
		ContextStub_Default.oneClient = oneClient;
		ContextStub_Default.clientLogger = clientLogger;
		ContextStub_Default.vmsInfo = VMsInfo.withClientAndLogger(oneClient, clientLogger);
		ContextStub_Default.hostInfo = HostInfo.withClientAndLogger(oneClient, clientLogger);
	}

	private ContextStub_Default() {
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
				
				if (attribute.getCategory().equals("system") && attribute.getIDAttribute().equals("vm-name")) {
					return UUID.randomUUID().toString();
				}
				
				if (attribute.getCategory().equals("system") && attribute.getIDAttribute().equals("hyper1.vm-names")) {
					Set runningHyper1VMs = new Set();
					vmsInfo
						.getRunningVMsByHost("localhost")
						.forEach(vm -> runningHyper1VMs.addValue(vm.getName()));
					return runningHyper1VMs;
				}
				if (attribute.getCategory().equals("system") && attribute.getIDAttribute().equals("hyper2.vm-names")) {
					Set runningHyper2VMs = new Set();
					vmsInfo
						.getRunningVMsByHost("192.168.1.11")
						.forEach(vm -> runningHyper2VMs.addValue(vm.getName()));
					return runningHyper2VMs;
				}
				if (attribute.getCategory().equals("system") && attribute.getIDAttribute().equals("hyper1.vm1-counter")) {
					return vmsInfo.countRunningVMsByHost("localhost").doubleValue();
				}
				if (attribute.getCategory().equals("system") && attribute.getIDAttribute().equals("hyper2.vm1-counter")) {
					return vmsInfo.countRunningVMsByHost("192.168.1.11").doubleValue();
				}
				if (attribute.getCategory().equals("system") && attribute.getIDAttribute().equals("hyper1.availableResources")) {
					return hostInfo.getAvailableCpu(0);
				}
				if (attribute.getCategory().equals("system") && attribute.getIDAttribute().equals("hyper2.availableResources")) {
					return hostInfo.getAvailableCpu(6);
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
