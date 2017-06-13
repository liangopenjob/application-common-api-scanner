package com.jwliang.application.api.annotation.scanner.utils;

//import javassist.ClassClassPath;
//import javassist.ClassPool;
//import javassist.CtClass;
//import javassist.CtMethod;
//import javassist.Modifier;
//import javassist.NotFoundException;
//import javassist.bytecode.CodeAttribute;
//import javassist.bytecode.LocalVariableAttribute;
//import javassist.bytecode.MethodInfo;

public class JavassistUtil {
//	private static ClassPool pool;
//	
//	static{
//		pool = ClassPool.getDefault();
//		ClassClassPath classPath = new ClassClassPath(JavassistUtil.class);
//		pool.insertClassPath(classPath);
//	}
//	
//	public static String[] getParameterNames(String className, String methodName) {
//		String[] parameterNames = new String[]{};
//		try {
//			CtClass cc = JavassistUtil.pool.get(className);
//			CtMethod cm = cc.getDeclaredMethod(methodName);
//			MethodInfo info = cm.getMethodInfo();
//			CodeAttribute codeAttribute = info.getCodeAttribute();
//			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
//			int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
//			parameterNames = new String[cm.getParameterTypes().length];
//			for (int i = 0; i < parameterNames.length; i++) {
//				parameterNames[i] = attr.variableName(i + pos);
//			}
//		} catch (NotFoundException e) {
//			e.printStackTrace();
//		}
//		return parameterNames;
//	}
//	
}
