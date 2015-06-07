package pl.edu.agh.toik.ajd.debugger;

import java.awt.EventQueue;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

import pl.edu.agh.toik.ajd.gui.MenuFrame;

public abstract aspect AbstractBreakpoints {
	
	pointcut debuggerContext(): within(pl.edu.agh.toik.ajd..*) || call(* pl.edu.agh.toik.ajd.debugger.Debugger.*(..));

	//pointcut allCalls(): call(* *(..));
	abstract pointcut allCalls();
	
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
		
		//String signature = pjp.getSignature().toLongString();
		String signature = thisJoinPoint.getSignature().toLongString();
		signature = signature.substring(signature.indexOf(" ")+1);
		
		if( ( debugger.getAction().equals(DebuggerAction.STEP_INTO) )
			|| ( debugger.getAction().equals(DebuggerAction.STEP_OVER) && debugger.isWantedInside() )
			|| ( debugger.getAction().equals(DebuggerAction.STEP_OUT) && debugger.isWantedDepth() )
			|| ( debugger.getMode().equals(DebuggerMode.INCLUSIVE) && signatures.contains(signature) ) 
			|| ( debugger.getMode().equals(DebuggerMode.EXCLUSIVE) && !signatures.contains(signature) )
		) {
			debugger.setAction(DebuggerAction.NONE);
			//debugger.pauseExecution(pjp);
			debugger.pauseExecution(thisJoinPoint);
		}
		
	//	Object[] args = changeArgs(pjp.getArgs(), debugger.getCustomArgs()); //podmiana argumentow funkcji
		
		debugger.increaseInside();	//potrzebne do step_over
		debugger.increaseDepth();	//potrzebne do step_out
		Object obj = null;
		try {
		//	obj = pjp.proceed(args);
			obj = proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		debugger.reduceDepth();
		debugger.reduceInside();
		debugger.setCustomArgs(new Object[0]);	//czyszczenie argumentow funkcji z poprzedniego wywolania
		
		return obj;
	}
	
	private Object[] changeArgs(Object[] args, Object[] customArgs) {
		if(args.length == customArgs.length) {
			for (int i = 0; i < args.length; i++) {
				Class<? extends Object> clazz = args[i].getClass();
				if(clazz == String.class) {
					args[i] = new String(customArgs[i].toString());
					break;
				} else if(clazz == Integer.class || clazz == int.class) {
					args[i] = Integer.parseInt(customArgs[i].toString());
					break;
				} else if(clazz == Long.class || clazz == long.class) {
					args[i] = Long.parseLong((String)customArgs[i]);
					break;
				} else if(clazz == Float.class || clazz == float.class) {
					args[i] = Float.parseFloat(customArgs[i].toString());	
					break;
				} else if(clazz == Double.class || clazz == double.class) {
					args[i] = Double.parseDouble(customArgs[i].toString());
					break;
				}
			}
		}
		
		return args;
	}
}
