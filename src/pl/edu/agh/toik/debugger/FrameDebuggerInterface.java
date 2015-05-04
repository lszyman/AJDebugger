package pl.edu.agh.toik.debugger;

import org.aspectj.lang.JoinPoint;

import pl.edu.agh.toik.gui.BreakpointFrame;

public class FrameDebuggerInterface implements DebuggerInterface{

	@Override
	public void takeCommand(JoinPoint joinpoint) {
		BreakpointFrame breakpointFrame = new BreakpointFrame();
		Debugger.getInstance().setBreakpointFrame(breakpointFrame);
		
		StringBuilder pointcutInfo = new StringBuilder();
		pointcutInfo.append("JoinPoint " + joinpoint.toShortString()+"\n");
		pointcutInfo.append("\tKind: " + joinpoint.getKind()+"\n");
		pointcutInfo.append("\tSignature: " + joinpoint.getSignature()+"\n");
		pointcutInfo.append("\tSourceLocation: " + joinpoint.getSourceLocation()+"\n");
		pointcutInfo.append("\tArgs:\n");
		for(Object arg : joinpoint.getArgs())
			pointcutInfo.append("\tClass: " + arg.getClass().getName() + ",\tValue: " + arg.toString()+"\n");
		
		breakpointFrame.getTextArea().setText(pointcutInfo+"\nPAUSED\n");
		// TODO Auto-generated method stub
		
	}

}
