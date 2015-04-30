package pl.edu.agh.toik.debugger;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.toik.gui.BreakpointFrame;
import pl.edu.agh.toik.gui.MenuFrame;

public class Debugger {

	private static Debugger instance = null;
	private DebuggerInterface debuggerInterface;
	
	private List<String> breakpointSignatures = new ArrayList<String>();
	private BreakpointFrame breakpointFrame;
	
	public static Debugger getInstance() {
		if(instance == null) {
			instance = new Debugger();
		}
		return instance;
	}
	
	public void setInterface(DebuggerInterface debuggerInterface) {
		this.debuggerInterface = debuggerInterface;
		this.debuggerInterface.setBreakpointFrame(breakpointFrame);
	}
	
//	public DebuggerInterface getInterface() {
//		return this.debuggerInterface;
//	}
	
	public synchronized void pauseExecution(String pointcutInfo) {
		breakpointFrame.getTextArea().setText(pointcutInfo+"\nPAUSED\n");
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
	
	public void setBreakpointFrame(BreakpointFrame breakpointFrame) {
		this.breakpointFrame = breakpointFrame;
		this.breakpointFrame.setVisible(true);
		this.debuggerInterface.setBreakpointFrame(breakpointFrame);
	}
}
