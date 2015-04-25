package pl.edu.agh.toik.debugger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// simple console interface

public class DebuggerInterfaceImpl implements DebuggerInterface {
	
	private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	@Override
	public void takeCommand() {
		try {
			boolean stopped = true;
			while(stopped) {
				System.out.println("Read line from console:");
				String [] commands = input.readLine().split(" ");
				for(String command : commands) {
					if("continue".startsWith(command)) {
						stopped = false;
					}
					else if("help".startsWith(command)) {
						System.out.println("Available commands:\n"
								+ "\tcontinue - resume instruction flow\n"
								+ "\thelp - display this information");
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Error while reading from System.in");
		}
	}

}
