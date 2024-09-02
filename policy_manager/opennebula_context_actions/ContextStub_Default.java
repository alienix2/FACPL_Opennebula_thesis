import java.util.Date;
import java.util.UUID;

import it.unifi.facpl.lib.util.Set;
import opennebula_api.HostInfo;
import opennebula_api.OpenNebulaActionContext;
import opennebula_api.OpenNebulaVMService;
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
	            ContextStub_Default.oneClient = new Client();
	            Logger logger = FileLoggerFactory.make("logs/virtualMachines.log");
	            initializeStub(oneClient, logger);
	            instance = new ContextStub_Default();
	        } catch (ClientConfigurationException e) {
	            throw new RuntimeException("Failed to initialize Client: " + e.getMessage(), e);
	        } catch (Exception e) {
	            throw new RuntimeException("Unexpected error during ContextStub_Default initialization: " + e.getMessage(), e);
	        }
	    }
	    return instance;
	}
	
	private static void inizializeStub(Client oneClient, Logger clientLogger) {
		ContextStub_Default.oneClient = oneClient;
		ContextStub_Default.clientLogger = clientLogger;
		ContextStub_Default.vmsInfo = VMsInfo.withCustomLogger(new OpenNebulaVMService(oneClient), clientLogger);
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
						.getRunningVMsByHost("0")
						.forEach(vm -> runningHyper1VMs.addValue(vm.getVmName()));
					return runningHyper1VMs;
				}
				if (attribute.getCategory().equals("system") && attribute.getIDAttribute().equals("hyper2.vm-names")) {
					Set runningHyper2VMs = new Set();
					vmsInfo
						.getRunningVMsByHost("6")
						.forEach(vm -> runningHyper2VMs.addValue(vm.getVmName()));
					return runningHyper2VMs;
				}
				if (attribute.getCategory().equals("system") && attribute.getIDAttribute().equals("hyper1.vm1-counter")) {
					return vmsInfo.countRunningVMsByHost("0").doubleValue();
				}
				if (attribute.getCategory().equals("system") && attribute.getIDAttribute().equals("hyper2.vm1-counter")) {
					return vmsInfo.countRunningVMsByHost("6").doubleValue();
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
	
	public static OpenNebulaActionContext getONContext() {
		return new OpenNebulaActionContext(oneClient, clientLogger);
	}
}
