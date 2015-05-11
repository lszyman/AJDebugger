package pl.edu.agh.toik.ajd.interfaces;

import org.aspectj.lang.JoinPoint;

public interface DebuggerInterface {

	void takeCommand(JoinPoint joinpoint);
}
