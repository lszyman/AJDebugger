package pl.edu.agh.toik.interfaces;

import org.aspectj.lang.JoinPoint;

public interface DebuggerInterface {

	void takeCommand(JoinPoint joinpoint);
}
