package opennnebula_api;

import java.util.Arrays;
import java.util.logging.Logger;

import utilities.FileLoggerFactory;

public class Main {
	public static void main(String args[]) throws Exception {
		
		HostInfo.initializeClient();
		
		
		VMsInfo.initializeClient();
		Logger logger = FileLoggerFactory.make("commandOutput.log");
		HostInfo.setLogger(logger);
		System.out.println(HostInfo.calculateAvailableCpu(0));
		
		CreateVM vm_creator = new CreateVM("oneadmin:Panzerotto", "http://localhost:2633/RPC2", logger);
//			try {
//				vm_creator.eval(Arrays.asList(0, "nome4", 4));
//			} catch (Throwable e) {
//				e.printStackTrace();
//			}
//			try {
//				vm_creator.eval(Arrays.asList(0, "nome2", 4));
//			} catch (Throwable e) {
//				e.printStackTrace();
//			}
//			try {
//				vm_creator.eval(Arrays.asList(0, "nome3", 4));
//			} catch (Throwable e) {
//				e.printStackTrace();
//			}
		
		
		System.out.println(VMsInfo.getRunningVMsByHost("localhost"));
//		System.out.println(VMsInfo.getVMsInfoByHostTemplate("localhost", "4", 1).toString());
//			TerminateVMsByHostTemplate vm_terminator = new TerminateVMsByHostTemplate("oneadmin:Panzerotto", "http://localhost:2633/RPC2", logger);
//			try {
//				vm_terminator.eval(Arrays.asList("localhost", "4", 2));
//			} catch (Throwable e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
		System.out.println(HostInfo.calculateAvailableCpu(0));

	}
	
}
