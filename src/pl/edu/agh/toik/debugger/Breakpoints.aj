package pl.edu.agh.toik.debugger;

import java.util.List;


public aspect Breakpoints {
	
	pointcut debuggerContext(): within(pl.edu.agh.toik.gui.BreakpointFrame) || within(pl.edu.agh.toik.debugger.*) || call(* pl.edu.agh.toik.debugger.Debugger.*(..));
	pointcut allCalls(): call(* *(..));
	pointcut allExecutions(): execution(* *(..));
	
	Object around(): allCalls() && !debuggerContext() {
		
		Debugger debugger = Debugger.getInstance();
		List<String> signatures = debugger.getBreakpointSignatrues();
		
		String signature = thisJoinPoint.getSignature().toLongString();
		signature = signature.substring(signature.indexOf(" ")+1);
		
		if( ( debugger.getAction() == DebuggerAction.NEXT_JOINPOINT )
			|| ( debugger.getMode().equals(DebuggerMode.INCLUSIVE) && signatures.contains(signature) ) 
			|| ( debugger.getMode().equals(DebuggerMode.EXCLUSIVE) && !signatures.contains(signature) )
		) {
			debugger.setAction(DebuggerAction.NONE);
			debugger.pauseExecution(thisJoinPoint);
		}
		
		Object obj = proceed();
		return obj;
	}
}
