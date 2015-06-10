package pl.edu.agh.toik.ajd.aspects;

import java.awt.EventQueue;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import pl.edu.agh.toik.ajd.debugger.Debugger;
import pl.edu.agh.toik.ajd.debugger.FrameDebuggerInterfaceImpl;
import pl.edu.agh.toik.ajd.gui.MenuFrame;

@Aspect
public class ChangeArgsAspect extends AbstractBreakpointsAspect {
	
	@Pointcut("call(* *(..))")
	void allCalls() {}
	
	@Around("init()")
	public Object aroundInit(ProceedingJoinPoint pjp) {
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
		debugger.pauseExecution(pjp);
		
		debugger.increaseInside();	//potrzebne do step_over
		debugger.increaseDepth();	//potrzebne do step_out
		Object obj = null;
		try {
			obj = pjp.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		debugger.reduceDepth();
		debugger.reduceInside();
		
		return obj;
	}
	
	@Around("mainPointcut()")
	public Object aroundAllCalls(ProceedingJoinPoint pjp) {
		
		return wrapProceed(pjp);
	}
	
	private Object wrapProceed(ProceedingJoinPoint pjp) {
		Debugger debugger = Debugger.getInstance();
		Object[] args = debugger.getCustomArgs(); //podmiana argumentow funkcji
		debugger.setCustomArgs(new Object[0]);	//czyszczenie argumentow funkcji z poprzedniego wywolania
		
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
		
		return obj;
	}
}
