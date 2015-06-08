//package pl.edu.agh.toik.ajd.debugger;
//
//import java.util.List;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//
//public aspect Breakpoints extends AbstractBreakpoints {
//	
//	pointcut allCalls(): call(* *(..));
//
//	//@Around("allCalls() && !debuggerContext()")
//	//public Object aroundAllCalls(ProceedingJoinPoint pjp) {
//	
//}
