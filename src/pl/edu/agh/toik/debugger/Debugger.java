package pl.edu.agh.toik.debugger;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;

import pl.edu.agh.toik.gui.BreakpointFrame;
import pl.edu.agh.toik.gui.MenuFrame;

public class Debugger {

	private static Debugger instance = null;
	private DebuggerInterface debuggerInterface;
	
	private List<String> breakpointSignatures = new ArrayList<String>();
	private BreakpointFrame breakpointFrame;
	private DebuggerMode mode;
	private boolean stopped;
	
	public static Debugger getInstance() {
		if(instance == null) {
			instance = new Debugger();
		}
		return instance;
	}
	
	private Debugger() {
		mode = DebuggerMode.INCLUSIVE;
	}
	
	public void setInterface(DebuggerInterface debuggerInterface) {
		this.debuggerInterface = debuggerInterface;
	}
	
//	public DebuggerInterface getInterface() {
//		return this.debuggerInterface;
//	}
	
	public synchronized void pauseExecution(JoinPoint joinpoint) {
		this.debuggerInterface.takeCommand(joinpoint);
//		stopped = true;
//		while(stopped) {
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
	
	public void unpause() {
		stopped = false;
	}
	
	public void addBreakpoint(String signature) {
		breakpointSignatures.add(signature);
	}
	
	public void removeBreakpoint(String signature) {
		breakpointSignatures.remove(signature);
	}

	public List<String> getBreakpointSignatrues() {
		return breakpointSignatures;
	}
	
	public void setBreakpointFrame(BreakpointFrame breakpointFrame) {
		this.breakpointFrame = breakpointFrame;
		this.breakpointFrame.setVisible(true);
	}
	
	public DebuggerMode getMode() {
		return mode;
	}
	
	public void setMode(DebuggerMode mode) {
		this.mode = mode;
	}
}
