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

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import it.unifi.facpl.lib.interfaces.IContextStub;
import it.unifi.facpl.lib.util.AttributeName;

public class ContextStub_Default implements IContextStub {

    private static final String CONFIG_FILE = "config.properties";
	private static ContextStub_Default instance;
	private static Client oneClient;
	private static Logger clientLogger;
	private static VMsInfo vmsInfo;
	private static HostInfo hostInfo;
	private static String hyper1HostId;
	private static String hyper2HostId;
	private static String type1Template;
	private static String type2Template;

	public static ContextStub_Default getInstance() {
		if (instance == null) {
			try {
	            Configuration config = new Configurations().properties(CONFIG_FILE);
	            hyper1HostId = config.getString("hyper1.host.id");
	            hyper2HostId = config.getString("hyper2.host.id");
	            type1Template = config.getString("type1.template.id");
	            type2Template = config.getString("type2.template.id");
				ContextStub_Default.oneClient = new Client();
				Logger logger = FileLoggerFactory.make("logs/virtualMachines.log");
				inizializeStub(oneClient, logger);
				instance = new ContextStub_Default();
			} catch (ClientConfigurationException e) {
				throw new RuntimeException("Failed to initialize Client: " + e.getMessage(), e);
			} catch (ConfigurationException e) {
				throw new RuntimeException(
						"Errors in the config gile: " + e.getMessage(), e);
			} catch (Exception e) {
				throw new RuntimeException(
						"Unexpected error during ContextStub_Default initialization: " + e.getMessage(), e);
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
			vmsInfo.getRunningVMsByHost(hyper1HostId).forEach(vm -> runningHyper1VMs.addValue(vm.getVmName()));
			return runningHyper1VMs;
		}
		if (attribute.getCategory().equals("system") && attribute.getIDAttribute().equals("hyper2.vm-names")) {
			Set runningHyper2VMs = new Set();
			vmsInfo.getRunningVMsByHost(hyper2HostId).forEach(vm -> runningHyper2VMs.addValue(vm.getVmName()));
			return runningHyper2VMs;
		}
		if (attribute.getCategory().equals("system") && attribute.getIDAttribute().equals("hyper1.vm1-counter")) {
			return vmsInfo.getRunningVMsByHostTemplate(hyper1HostId, type1Template).size();
	    }
		if (attribute.getCategory().equals("system") && attribute.getIDAttribute().equals("hyper2.vm1-counter")) {
			return vmsInfo.getRunningVMsByHostTemplate(hyper2HostId, type1Template).size();
		}
		if (attribute.getCategory().equals("system")
				&& attribute.getIDAttribute().equals("hyper1.availableResources")) {
			return hostInfo.getAvailableCpu(hyper1HostId);
		}
		if (attribute.getCategory().equals("system")
				&& attribute.getIDAttribute().equals("hyper2.availableResources")) {
			return hostInfo.getAvailableCpu(hyper2HostId);
		}
		return null;
	}

	public static OpenNebulaActionContext getONContext() {
		return new OpenNebulaActionContext(oneClient, clientLogger);
	}
}
