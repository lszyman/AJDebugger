package pl.edu.agh.toik.debugger;

import java.util.List;

public aspect Breakpoints {
	
	pointcut debuggerContext(): within(pl.edu.agh.toik.debugger.*) || call(* pl.edu.agh.toik.debugger.Debugger.*(..));
	pointcut allCalls(): call(* *(..));
	pointcut allExecutions(): execution(* *(..));
	
	Object around(): allCalls() && !debuggerContext() {
		
		Debugger debugger = Debugger.getInstance();
		List<String> signatures = debugger.getBreakpointSignatrues();
		
		if(signatures.contains(thisJoinPoint.getSignature().toString())) {
		
			System.out.println("JoinPoint " + thisJoinPoint.toShortString());
			System.out.println("\tKind: " + thisJoinPoint.getKind());
			System.out.println("\tSignature: " + thisJoinPoint.getSignature());
			System.out.println("\tSourceLocation: " + thisJoinPoint.getSourceLocation());
			System.out.println("\tArgs: ");
			for(Object arg : thisJoinPoint.getArgs())
				System.out.println("\t\tClass: " + arg.getClass().getName() + ", toString(): " + arg.toString());
			
			debugger.pauseExecution();
			
		}
		
		Object obj = proceed();
		return obj;
	}
}
