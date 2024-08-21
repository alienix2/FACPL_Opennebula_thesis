package opennebula_api;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class VMsInfo {

    private final Logger logger;
    private final VirtualMachineService vmService;

    private VMsInfo(VirtualMachineService vmService, Logger logger) {
        this.vmService = vmService;
        this.logger = logger;
    }

    public static VMsInfo withDefaultLogger(VirtualMachineService vmService) {
        return new VMsInfo(vmService, Logger.getLogger(VMsInfo.class.getName()));
    }

    public static VMsInfo withCustomLogger(VirtualMachineService vmService, Logger logger) {
        return new VMsInfo(vmService, logger);
    }
    
    private List<VMDescriptor> getAndLogVMs() {
        List<VMDescriptor> vmDescriptors = vmService.getRunningVirtualMachineInfo();
        logger.info("The VMs running are: " + vmDescriptors.toString());
        return vmDescriptors;
    }

    public List<VMDescriptor> getRunningVMsByHostTemplate(String host, String templateId) {
        return getAndLogVMs()
        		.stream()
                .filter(x -> x.getHostId().equals(host) && x.getTemplateId().equals(templateId))
        		.collect(Collectors.toList());
    }
    
    public List<VMDescriptor> getRunningVMsByHost(String host) {
        return getAndLogVMs()
        		.stream()
                .filter(x -> x.getHostId().equals(host))
        		.collect(Collectors.toList());
    }

    public VMDescriptor getRunningVMByName(String vmName) {
    	return getAndLogVMs()
        		.stream()
                .filter(x -> x.getVmName().equals(vmName))
                .filter(x -> x.getState().equals("ACTIVE"))
                .findFirst()
                .orElse(null);
    }

    public Long countRunningVMsByHost(String host) {
    	return getAndLogVMs()
        		.stream()
        		.filter(x -> x.getHostId().equals(host))
        		.count();
    }
}
