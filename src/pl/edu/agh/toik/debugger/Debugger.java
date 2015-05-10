package pl.edu.agh.toik.debugger;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;

import pl.edu.agh.toik.interfaces.DebuggerInterface;


public class Debugger {

	private static Debugger instance = null;
	private DebuggerInterface debuggerInterface;
	
	private List<String> breakpointSignatures = new ArrayList<String>();
	private DebuggerMode mode;
	
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
	
	public synchronized void pauseExecution(JoinPoint joinpoint) {
		this.debuggerInterface.takeCommand(joinpoint);
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
	
	public DebuggerMode getMode() {
		return mode;
	}
	
	public void setMode(DebuggerMode mode) {
		this.mode = mode;
	}
}
