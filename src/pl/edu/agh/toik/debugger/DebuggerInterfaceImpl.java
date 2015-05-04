package pl.edu.agh.toik.debugger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.aspectj.lang.JoinPoint;

// simple console interface

public class DebuggerInterfaceImpl implements DebuggerInterface {
	
	private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	@Override
	public void takeCommand(JoinPoint joinpoint) {
		
		StringBuilder pointcutInfo = new StringBuilder();
		pointcutInfo.append("JoinPoint " + joinpoint.toShortString()+"\n");
		pointcutInfo.append("\tKind: " + joinpoint.getKind()+"\n");
		pointcutInfo.append("\tSignature: " + joinpoint.getSignature()+"\n");
		pointcutInfo.append("\tSourceLocation: " + joinpoint.getSourceLocation()+"\n");
		pointcutInfo.append("\tArgs:\n");
		for(Object arg : joinpoint.getArgs())
			pointcutInfo.append("\tClass: " + arg.getClass().getName() + ",\tValue: " + arg.toString()+"\n");
		System.out.print(pointcutInfo);
		
		try {
			boolean stopped = true;
			while(stopped) {
				System.out.println("Read line from console:\n");
				String [] commands = input.readLine().split(" ");
				for(String command : commands) {
					if("continue".startsWith(command)) {
						stopped = false;
					}
					else if("exclude".startsWith(command)) {
						String signature = joinpoint.getSignature().toLongString();
						signature = signature.substring(signature.indexOf(" ") + 1);
						Debugger.getInstance().addBreakpoint(signature);
					}
					else if("help".startsWith(command)) {
						System.out.println("Available commands:\n"
								+ "\tcontinue - resume instruction flow\n"
								+ "\thelp - display this information\n");
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Error while reading from System.in\n");
		}
	}

}
