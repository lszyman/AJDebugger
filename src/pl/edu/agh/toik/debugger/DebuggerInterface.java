package pl.edu.agh.toik.debugger;

import org.aspectj.lang.JoinPoint;

public interface DebuggerInterface {

	void takeCommand(JoinPoint joinpoint);
}
