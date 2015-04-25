package pl.edu.agh.toik.debugger;

import java.util.ArrayList;
import java.util.List;

public class Debugger {

	private static Debugger instance = null;
	private DebuggerInterface debuggerInterface;
	
	private List<String> breakpointSignatures = new ArrayList<String>();
	
	public static Debugger getInstance() {
		if(instance == null)
			instance = new Debugger();
		return instance;
	}
	
	public void setInterface(DebuggerInterface debuggerInterface) {
		this.debuggerInterface = debuggerInterface;
	}
	
	public synchronized void pauseExecution() {
		System.out.println("PAUSED");
		this.debuggerInterface.takeCommand();
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
}
