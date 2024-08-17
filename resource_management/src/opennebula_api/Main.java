package opennebula_api;

import java.util.Arrays;
import java.util.logging.Logger;

import org.opennebula.client.Client;

import utilities.FileLoggerFactory;

public class Main {
	public static void main(String args[]) throws Exception {
		
		Client oneClient = new Client("oneadmin:Panzerotto", "http://localhost:2633/RPC2");

		Logger logger = FileLoggerFactory.make("commandOutput.log");
		HostInfo hostInfo = HostInfo.withClientAndLogger(oneClient, logger);
		System.out.println(hostInfo.getAvailableCpu(0));
		
		CreateVM vm_creator = new CreateVM(oneClient, logger);
			try {
				vm_creator.eval(Arrays.asList(0, "nome4", 4));
			} catch (Throwable e) {
				e.printStackTrace();
			}
			try {
				vm_creator.eval(Arrays.asList(0, "nome2", 4));
			} catch (Throwable e) {
				e.printStackTrace();
			}
//			try {
//				vm_creator.eval(Arrays.asList(0, "nome3", 4));
//			} catch (Throwable e) {
//				e.printStackTrace();
//			}
		
			VMsInfo.withCustomLogger("oneadmin:Panzerotto", "http://localhost:2633/RPC2", logger).countRunningVMsByHost("localhost");
//		System.out.println(VMsInfo.getVMsInfoByHostTemplate("localhost", "4", 1).toString());
		TerminateVMByName vm_terminator = new TerminateVMByName(oneClient, logger);
			try {
				vm_terminator.eval(Arrays.asList("localhost", "nome3"));
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		System.out.println(hostInfo.getAvailableCpu(0));

	}
	
}
