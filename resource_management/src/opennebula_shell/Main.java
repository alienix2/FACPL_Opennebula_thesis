package opennebula_shell;

import java.util.Arrays;
import java.util.logging.Logger;
import utilities.FileLoggerFactory;

public class Main {
	public static void main(String args[]) throws Exception {
		
		Logger logger = FileLoggerFactory.make("commandOutput.log");
		HostInfo.setLogger(logger);
		CreateVM vm_creator = new CreateVM(logger);
//			try {
//				vm_creator.eval(Arrays.asList(0, "nome1", 4));
//			} catch (Throwable e) {
//				e.printStackTrace();
//			}
//			try {
//				vm_creator.eval(Arrays.asList(0, "nome2", 4));
//			} catch (Throwable e) {
//				e.printStackTrace();
//			}
//			try {
//				vm_creator.eval(Arrays.asList(5, "nome3", 4));
//			} catch (Throwable e) {
//				e.printStackTrace();
//			}

		TerminateVM vm_terminator = new TerminateVM(logger);
		try {
			vm_terminator.eval(Arrays.asList("localhost", "4", 2));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(HostInfo.getAvailableCpu(0));
		System.out.println(HostInfo.getAvailableMem(0));

	}
	
}
