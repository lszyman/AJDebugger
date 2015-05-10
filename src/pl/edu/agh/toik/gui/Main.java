//package pl.edu.agh.toik.gui;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.lang.reflect.Method;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Set;
//
//import org.reflections.Reflections;
//import org.reflections.scanners.ResourcesScanner;
//import org.reflections.scanners.SubTypesScanner;
//import org.reflections.util.ClasspathHelper;
//import org.reflections.util.ConfigurationBuilder;
//import org.reflections.util.FilterBuilder;
//
//import pl.edu.agh.toik.debugger.Debugger;
//import pl.edu.agh.toik.debugger.DebuggerInterfaceImpl;
//
//public class Main {
//	private static String currentPackage = "pl.edu.agh.toik.example";
//	
//	public static void main(String[] args) {
//		
//		try{
//			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//	 
//			String input;
//			
//			Debugger debugger = Debugger.getInstance();
//			debugger.setInterface(new DebuggerInterfaceImpl());
//	 
//			while((input=br.readLine())!="") {
//				String [] command = input.split(" ");
//				
//				if(command[0].equals("-c")) {
//					
//					Set<Class<? extends Object>> allClasses = null;
//					
//					if(command.length >= 2)
//						allClasses = getAllClasses(command[1]);
//					if(allClasses == null)
//						allClasses = getAllClasses(currentPackage);
//					if(allClasses.size() != 0)
//						for(Class<? extends Object> clazz : allClasses)
//							System.out.println(clazz.getCanonicalName());
//					else
//						System.out.println("Error: Package not found.");
//					
//				} else if(command[0].equals("-m")) {
//					Method[] methods = null;
//					
//					if(command.length >= 2)
//						methods = getMethods(command[1]);
//					if(methods == null && command.length >=2)
//						methods = getMethods(currentPackage+"."+command[1]);
//					
//					if(methods != null)
//						for(Method method : methods) {
//							Class<?>[] paramTypes = method.getParameterTypes();
//							String params = "(";
//							for(Class<?> type : paramTypes){
//								params += type.getSimpleName();
//							}
//							
//							String returnType = method.getReturnType().getCanonicalName();
//							System.out.println(returnType+" "+method.getName()+params+")");
//						}
//					else
//						System.out.println("Error: Class not found.");
//					
//				} else {
//					
//					System.out.println("Command not found.");
//					
//				}
//			}
//		} catch(IOException io) {
//			io.printStackTrace();
//		}
//
//	}
//	
//	public static Set<Class<? extends Object>> getAllClasses(String packageName) {
//		List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
//		classLoadersList.add(ClasspathHelper.contextClassLoader());
//		classLoadersList.add(ClasspathHelper.staticClassLoader());
//		
//		Reflections reflections = new Reflections(new ConfigurationBuilder()
//	    .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
//	    .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
//	    .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));
//		
//		 Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);
//		 return allClasses;
//	}
//	
//	public static Method[] getMethods(String className) {
//		Class<? extends Object> aClass = null;
//		Method[] methods = null;
//		try {
//			aClass = Class.forName(className);
//			methods = aClass.getDeclaredMethods();
//		} catch (ClassNotFoundException e) {
//			return null;
//		}
//		
//		return methods;
//	}
//
//}
