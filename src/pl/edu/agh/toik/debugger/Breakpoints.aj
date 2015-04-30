package pl.edu.agh.toik.debugger;

import java.util.List;

import pl.edu.agh.toik.gui.BreakpointFrame;

public aspect Breakpoints {
	
	pointcut debuggerContext(): within(pl.edu.agh.toik.debugger.*) || call(* pl.edu.agh.toik.debugger.Debugger.*(..));
	pointcut allCalls(): call(* *(..));
	pointcut allExecutions(): execution(* *(..));
	
	Object around(): allCalls() && !debuggerContext() {
		
		Debugger debugger = Debugger.getInstance();
		List<String> signatures = debugger.getBreakpointSignatrues();
		
		String signature = thisJoinPoint.getSignature().toLongString();
		signature = signature.substring(signature.indexOf(" ")+1);
		
		if(signatures.contains(signature)) {
		
//			System.out.println("JoinPoint " + thisJoinPoint.toShortString());
//			System.out.println("\tKind: " + thisJoinPoint.getKind());
//			System.out.println("\tSignature: " + thisJoinPoint.getSignature());
//			System.out.println("\tSourceLocation: " + thisJoinPoint.getSourceLocation());
//			System.out.println("\tArgs: ");
//			for(Object arg : thisJoinPoint.getArgs())
//				System.out.println("\t\tClass: " + arg.getClass().getName() + ",\tName: " + arg.toString());
			
			debugger.setBreakpointFrame(new BreakpointFrame());
			
			StringBuilder pointcutInfo = new StringBuilder();
			pointcutInfo.append("JoinPoint " + thisJoinPoint.toShortString()+"\n");
			pointcutInfo.append("\tKind: " + thisJoinPoint.getKind()+"\n");
			pointcutInfo.append("\tSignature: " + thisJoinPoint.getSignature()+"\n");
			pointcutInfo.append("\tSourceLocation: " + thisJoinPoint.getSourceLocation()+"\n");
			pointcutInfo.append("\tArgs:\n");
			for(Object arg : thisJoinPoint.getArgs())
				pointcutInfo.append("\tClass: " + arg.getClass().getName() + ",\tValue: " + arg.toString()+"\n");
			
			
			debugger.pauseExecution(pointcutInfo.toString());
			
		}
		
		Object obj = proceed();
		return obj;
	}
}
