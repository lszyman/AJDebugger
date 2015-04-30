package pl.edu.agh.toik.debugger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pl.edu.agh.toik.gui.BreakpointFrame;

// simple console interface

public class DebuggerInterfaceImpl implements DebuggerInterface {
	
	private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	
	private BreakpointFrame breakpointFrame;

	@Override
	public void takeCommand() {
//		try {
//			boolean stopped = true;
//			while(stopped) {
//				breakpointFrame.getTextArea().append("Read line from console:\n");
//				String [] commands = input.readLine().split(" ");
//				for(String command : commands) {
//					if("continue".startsWith(command)) {
//						stopped = false;
//					}
//					else if("help".startsWith(command)) {
//						breakpointFrame.getTextArea().setText("Available commands:\n"
//								+ "\tcontinue - resume instruction flow\n"
//								+ "\thelp - display this information\n");
//					}
//				}
//			}
//		} catch (IOException e) {
//			breakpointFrame.getTextArea().setText("Error while reading from System.in\n");
//		}
	}
	
	@Override
	public void setBreakpointFrame(BreakpointFrame bf) {
		this.breakpointFrame = bf;
	}

}
