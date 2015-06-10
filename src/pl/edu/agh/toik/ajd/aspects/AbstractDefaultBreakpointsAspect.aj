package pl.edu.agh.toik.ajd.aspects;

import java.util.List;

import pl.edu.agh.toik.ajd.debugger.Debugger;
import pl.edu.agh.toik.ajd.debugger.DebuggerAction;
import pl.edu.agh.toik.ajd.debugger.DebuggerMode;

public abstract aspect AbstractDefaultBreakpointsAspect extends AbstractBreakpointsAspect {
	
	
	before(): mainPointcut() {
		
		printConsoleJoinPoint(thisJoinPoint);
		
		Debugger debugger = Debugger.getInstance();
		List<String> signatures = debugger.getBreakpointSignatrues();
		
		String signature = thisJoinPoint.getSignature().toLongString();
		signature = signature.substring(signature.indexOf(" ")+1);
		
		if( ( debugger.getAction().equals(DebuggerAction.STEP_INTO) )
			|| ( debugger.getAction().equals(DebuggerAction.STEP_OVER) && debugger.isWantedInside() )
			|| ( debugger.getAction().equals(DebuggerAction.STEP_OUT) && debugger.isWantedDepth() )
			|| ( debugger.getMode().equals(DebuggerMode.INCLUSIVE) && signatures.contains(signature) ) 
			|| ( debugger.getMode().equals(DebuggerMode.EXCLUSIVE) && !signatures.contains(signature) )
		) {
//			System.out.println(debugger.getAction()+" +"+debugger.isWantedInside()+"+"+debugger.isWantedDepth()+"+ "+debugger.getMode()+" -- "+ signatures.contains(signature));
//			System.out.println(debugger.insideDepth+"="+debugger.wantedInside);
//			System.out.println(debugger.depth+"="+debugger.wantedDepth);
			debugger.setAction(DebuggerAction.NONE);
			debugger.resetInsideAndDepth();
			debugger.pauseExecution(thisJoinPoint);
		}
		
		debugger.setCustomArgs(changeArgs(thisJoinPoint.getArgs(), debugger.getCustomArgs())); //podmiana argumentow funkcji
	}
}
