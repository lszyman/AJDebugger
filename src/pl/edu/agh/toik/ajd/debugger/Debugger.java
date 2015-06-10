package pl.edu.agh.toik.ajd.debugger;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;

import pl.edu.agh.toik.ajd.interfaces.DebuggerInterface;


public class Debugger {

	private static Debugger instance = null;
	private DebuggerInterface debuggerInterface;
	
	private List<String> breakpointSignatures = new ArrayList<String>();
	private DebuggerMode mode;
	private DebuggerAction action;
	private int insideDepth = 0;
	private int wantedInside = -1;
	private int depth = 0;
	private int wantedDepth = -1;
	
	private Object[] customArgs = new Object[0];
	
	private boolean debugWithAJDebugOnly = false;

	public static Debugger getInstance() {
		if(instance == null) {
			instance = new Debugger();
		}
		return instance;
	}
	
	private Debugger() {
		mode = DebuggerMode.INCLUSIVE;
		action = DebuggerAction.NEXT_BREAKPOINT;
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
	
	public DebuggerAction getAction() {
		return action;
	}

	public void setAction(DebuggerAction action) {
		this.action = action;
	}
	
	public void increaseInside() {
		this.insideDepth++;
	}
	
	public void reduceInside() {
		this.insideDepth--;
	}
	
	public void setWantedInside() {
		wantedInside = insideDepth;
	}

	public boolean isWantedInside() {
		if(wantedInside == insideDepth)
			return true;
		return false;
	}

	public void increaseDepth() {
		depth++;		
	}

	public void reduceDepth() {
		depth--;
	}

	public void setWantedDepth() {
		wantedDepth = depth-1;
	}
	
	public boolean isWantedDepth() {
		if(wantedDepth==depth)
			return true;
		return false;
	}
	
	public void resetInsideAndDepth() {
		wantedInside = -1;
		wantedDepth = -1;
	}

	public Object[] getCustomArgs() {
		return customArgs;
	}

	public void setCustomArgs(Object[] customArgs) {
		this.customArgs = customArgs;
	}
	
	public boolean isDebugWithAJDebugOnly() {
		return debugWithAJDebugOnly;
	}

	public void setDebugWithAJDebugOnly(boolean debugWithAJDebugOnly) {
		this.debugWithAJDebugOnly = debugWithAJDebugOnly;
	}
}
