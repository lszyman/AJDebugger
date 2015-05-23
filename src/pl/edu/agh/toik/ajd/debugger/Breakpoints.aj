package pl.edu.agh.toik.ajd.debugger;

import java.awt.EventQueue;
import java.util.List;

import pl.edu.agh.toik.ajd.gui.MenuFrame;


public aspect Breakpoints {
	
	pointcut debuggerContext(): within(pl.edu.agh.toik.ajd..*) || call(* pl.edu.agh.toik.ajd.debugger.Debugger.*(..));
	pointcut allCalls(): call(* *(..));
	pointcut allExecutions(): execution(* *(..));
	pointcut init(): execution(public static void main(String[]));
	
	before(): init() {
		Debugger debugger = Debugger.getInstance();
		debugger.setInterface(new FrameDebuggerInterfaceImpl());
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuFrame frame = new MenuFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		debugger.pauseExecution(thisJoinPoint);
	}
	
	Object around(): allCalls() && !debuggerContext() {
		
		Debugger debugger = Debugger.getInstance();
		List<String> signatures = debugger.getBreakpointSignatrues();
		
		String signature = thisJoinPoint.getSignature().toLongString();
		signature = signature.substring(signature.indexOf(" ")+1);
		
		if( ( debugger.getAction().equals(DebuggerAction.NEXT_JOINPOINT) )
			|| ( debugger.getAction().equals(DebuggerAction.STEP_OVER) && !debugger.isInside() )
			|| ( debugger.getMode().equals(DebuggerMode.INCLUSIVE) && signatures.contains(signature) ) 
			|| ( debugger.getMode().equals(DebuggerMode.EXCLUSIVE) && !signatures.contains(signature) )
		) {
			debugger.setAction(DebuggerAction.NONE);
			debugger.pauseExecution(thisJoinPoint);
		}
		
		debugger.setInside(true);
		Object obj = proceed();
		debugger.setInside(false);
		
		return obj;
	}
}
