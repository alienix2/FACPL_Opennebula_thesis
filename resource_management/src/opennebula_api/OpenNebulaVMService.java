package opennebula_api;

import org.opennebula.client.Client;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;
import org.w3c.dom.Document;

import utilities.XmlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OpenNebulaVMService implements VirtualMachineService {

    private VirtualMachinePool vmPool;

    public OpenNebulaVMService(Client client) {
        this.vmPool = new VirtualMachinePool(client);
    }
    
    public OpenNebulaVMService(VirtualMachinePool vmPool) {
    	this.vmPool = vmPool;
    }

    @Override
	public List<VMDescriptor> getVirtualMachinesInfo() {
        vmPool.info();

        List<VMDescriptor> vmInfoList = new ArrayList<>();
        for (VirtualMachine vm : vmPool) {
            String xmlData = vm.info().getMessage();
            Document document = XmlUtils.parseXml(xmlData);

            String vmId = vm.getId() + "";
            String vmName = vm.getName();
            String templateId = document.getElementsByTagName("TEMPLATE_ID").item(0).getTextContent();
            String hostId = document.getElementsByTagName("HID").item(0).getTextContent();
            String lcmState = vm.lcmStateStr();
            String state = vm.stateStr();

            VMDescriptor vmInfo = new VMDescriptor(vmId, vmName, templateId, hostId, state, lcmState);
            vmInfoList.add(vmInfo);
        }

        return vmInfoList;
    }

    @Override
    public List<VMDescriptor> getRunningVirtualMachineInfo() {
        return getVirtualMachinesInfo().stream()
                .filter(x -> x.getState().equals("ACTIVE"))
                .filter(x ->x.getLcmState().equals("RUNNING"))
                .collect(Collectors.toList());
    }
}

