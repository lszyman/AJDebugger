package pl.edu.agh.toik.debugger;


import org.aspectj.lang.JoinPoint;

import pl.edu.agh.toik.gui.BreakpointFrame;
import pl.edu.agh.toik.interfaces.Command;
import pl.edu.agh.toik.interfaces.DebuggerInterface;


public class FrameDebuggerInterfaceImpl implements DebuggerInterface {
	
	private boolean stop = true;
	
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
		
		Command command = new Command() {
			public void nextBreakpoint() {
				stop = false;
				Debugger.getInstance().setAction(DebuggerAction.NEXT_BREAKPOINT);
			};
			public void nextJoinpoint() {
				stop = false;
				Debugger.getInstance().setAction(DebuggerAction.NEXT_JOINPOINT);
			}
		};

		new BreakpointFrame(pointcutInfo.toString()+"\nPAUSED\n", command);

		while(stop == true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		stop = true;
	}

}