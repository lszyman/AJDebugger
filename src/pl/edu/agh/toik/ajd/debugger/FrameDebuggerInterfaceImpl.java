package pl.edu.agh.toik.ajd.debugger;


import org.aspectj.lang.JoinPoint;

import pl.edu.agh.toik.ajd.gui.BreakpointFrame;
import pl.edu.agh.toik.ajd.interfaces.Command;
import pl.edu.agh.toik.ajd.interfaces.DebuggerInterface;


public class FrameDebuggerInterfaceImpl implements DebuggerInterface {
	
	private boolean stop = true;
	
	@Override
	public void takeCommand(JoinPoint joinpoint) {
	
		Command command = new Command() {
			public void nextBreakpoint() {
				stop = false;
				Debugger.getInstance().setAction(DebuggerAction.NEXT_BREAKPOINT);
			};
			public void stepInto() {
				stop = false;
				Debugger.getInstance().setAction(DebuggerAction.STEP_INTO);
			}
			public void stepOver() {
				stop = false;
				Debugger debugger = Debugger.getInstance();
				debugger.setWantedInside();
				debugger.setAction(DebuggerAction.STEP_OVER);
			}
			public void stepOut() {
				stop = false;
				Debugger debugger = Debugger.getInstance();
				debugger.setWantedDepth();
				debugger.setAction(DebuggerAction.STEP_OUT);
			}
		};

		new BreakpointFrame(joinpoint, command);

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