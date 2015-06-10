package pl.edu.agh.toik.ajd.aspects;



public aspect BreakpointsAspect extends AbstractDefaultBreakpointsAspect {
	
	pointcut allCalls(): call(* *(..));
	
}
