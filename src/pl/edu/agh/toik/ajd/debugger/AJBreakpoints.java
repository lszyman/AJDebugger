package pl.edu.agh.toik.ajd.debugger;

import java.awt.EventQueue;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import pl.edu.agh.toik.ajd.gui.MenuFrame;

@Aspect
public class AJBreakpoints {
	
	@Pointcut("within(pl.edu.agh.toik.ajd..*) || call(* pl.edu.agh.toik.ajd.debugger.Debugger.*(..))")
	void debuggerContext() {}
	
	@Pointcut("call(* *(..))")
	void allCalls() {}
	
	@Pointcut("execution(* *(..))")
	void allExecutions() {}
	
	@Pointcut("execution(public static void main(String[]))")
	void init() {}
	
	@Before("init()")
	public void beforeInit(JoinPoint joinPoint) {
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
		debugger.pauseExecution(joinPoint);
	}
	
	@Around("allCalls() && !debuggerContext()")
	public Object aroundAllCalls(ProceedingJoinPoint pjp) {
		
		StringBuilder pointcutInfo = new StringBuilder();
		pointcutInfo.append("JoinPoint " + pjp.toShortString()+"\n");
		pointcutInfo.append("\tKind: " + pjp.getKind()+"\n");
		pointcutInfo.append("\tSignature: " + pjp.getSignature()+"\n");
		pointcutInfo.append("\tSourceLocation: " + pjp.getSourceLocation()+"\n");
		pointcutInfo.append("\tArgs:\n");
		for(Object arg : pjp.getArgs())
			pointcutInfo.append("\tClass: " + arg.getClass().getName() + ",\tValue: " + arg.toString()+"\n");
		
		System.out.println(pointcutInfo.toString());
		
		Debugger debugger = Debugger.getInstance();
		List<String> signatures = debugger.getBreakpointSignatrues();
		
		String signature = pjp.getSignature().toLongString();
		signature = signature.substring(signature.indexOf(" ")+1);
		
		if( ( debugger.getAction().equals(DebuggerAction.STEP_INTO) )
			|| ( debugger.getAction().equals(DebuggerAction.STEP_OVER) && debugger.isWantedInside() )
			|| ( debugger.getAction().equals(DebuggerAction.STEP_OUT) && debugger.isWantedDepth() )
			|| ( debugger.getMode().equals(DebuggerMode.INCLUSIVE) && signatures.contains(signature) ) 
			|| ( debugger.getMode().equals(DebuggerMode.EXCLUSIVE) && !signatures.contains(signature) )
		) {
			debugger.setAction(DebuggerAction.NONE);
			debugger.pauseExecution(pjp);
		}
		
		Object[] args = changeArgs(pjp.getArgs(), debugger.getCustomArgs()); //podmiana argumentow funkcji
		
		debugger.increaseInside();	//potrzebne do step_over
		debugger.increaseDepth();	//potrzebne do step_out
		Object obj = null;
		try {
			obj = pjp.proceed(args);
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
