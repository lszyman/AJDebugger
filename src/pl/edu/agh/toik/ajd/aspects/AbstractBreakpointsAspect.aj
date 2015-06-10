package pl.edu.agh.toik.ajd.aspects;

import org.aspectj.lang.JoinPoint;


public abstract aspect AbstractBreakpointsAspect {
	
	abstract pointcut allCalls();
	
	pointcut debuggerContext(): within(pl.edu.agh.toik.ajd..*) || call(* pl.edu.agh.toik.ajd.debugger.Debugger.*(..));
	
	pointcut init(): execution(public static void main(String[]));
	
	pointcut mainPointcut(): !ajdebugWithout() && allCalls() && !debuggerContext();
	
	pointcut ajdebugWithout(): within(@pl.edu.agh.toik.ajd.annotation.AJDebugWithout *) || execution(@pl.edu.agh.toik.ajd.annotation.AJDebugWithout * *()) || call(@pl.edu.agh.toik.ajd.annotation.AJDebugWithout * *(..));
	
//	pointcut ajdebugOnly(): within(@pl.edu.agh.toik.ajd.annotation.AJDebugOnly *) || execution(@pl.edu.agh.toik.ajd.annotation.AJDebugOnly * *()) || call(@pl.edu.agh.toik.ajd.annotation.AJDebugOnly * *(..));
//	
//	pointcut allExecutionsWithOnlyAnnotation(): ajdebugOnly() && allCalls() && !debuggerContext();
	
	protected void printConsoleJoinPoint(JoinPoint pjp) {
		StringBuilder pointcutInfo = new StringBuilder();
		pointcutInfo.append("JoinPoint " + pjp.toShortString()+"\n");
		pointcutInfo.append("\tKind: " + pjp.getKind()+"\n");
		pointcutInfo.append("\tSignature: " + pjp.getSignature()+"\n");
		pointcutInfo.append("\tSourceLocation: " + pjp.getSourceLocation()+"\n");
		pointcutInfo.append("\tArgs:\n");
		for(Object arg : pjp.getArgs())
			pointcutInfo.append("\tClass: " + arg.getClass().getName() + ",\tValue: " + arg.toString()+"\n");
		
		System.out.println(pointcutInfo.toString());
	}
	
	protected Object[] changeArgs(Object[] args, Object[] customArgs) {
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