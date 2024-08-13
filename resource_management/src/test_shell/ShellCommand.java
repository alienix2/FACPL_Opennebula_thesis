package test_shell;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import it.unifi.facpl.lib.interfaces.*;

public class ShellCommand implements IPepAction{
	@Override
	public void eval(List<Object> args) throws Throwable { //Method executed when the function gets called
		//ToDo a better way to deal with the fact that the commands might not be string
		ArrayList<String> commands = new ArrayList<String>();
		args.forEach(x -> commands.add(x.toString()));
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("bash", "-c", String.join(" && ", commands));
		
		String fileName = "commandsOutput.txt"; 
		
		try {
		    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
			
			Process process = processBuilder.start();
			StringBuilder output = new StringBuilder();
			StringBuilder error = new StringBuilder();
			BufferedReader output_reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			BufferedReader error_reader = new BufferedReader(
					new InputStreamReader(process.getErrorStream()));
			
			String line;
			
			while ((line = output_reader.readLine()) != null) {
				output.append(line + "\n");
			}
			
			while ((line = error_reader.readLine()) != null) {
				error.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				writer.append("success!\n");
				writer.append(output);
				writer.close();
			} else {
				writer.append("fail!\n");
				writer.append(error);
				writer.close();
			}
		}catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}