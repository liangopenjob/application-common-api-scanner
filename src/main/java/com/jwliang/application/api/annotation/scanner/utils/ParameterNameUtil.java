package com.jwliang.application.api.annotation.scanner.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import aj.org.objectweb.asm.ClassReader;
import aj.org.objectweb.asm.ClassVisitor;
import aj.org.objectweb.asm.Label;
import aj.org.objectweb.asm.MethodVisitor;
import aj.org.objectweb.asm.Opcodes;
import aj.org.objectweb.asm.Type;
/**
 * 
 * @ClassName: ParameterNameUtil  
 * @Description: asm读取方法参数名称工具类
 * @author: liangjunwei5
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

public class ParameterNameUtil {

    /**
     * 获取指定类指定方法的参数名
     * @param clazz 要获取参数名的方法所属的类
     * @param method 要获取参数名的方法
     * @return 按参数顺序排列的参数名列表，如果没有参数，则返回null
     */
    public static String[] getParameterNames(Class<?> clazz, final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length == 0) {
            return null;
        }
        final Type[] types = new Type[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            types[i] = Type.getType(parameterTypes[i]);
        }
        final String[] parameterNames = new String[parameterTypes.length];
        final Map<String,Integer> indexMap = new HashMap<String,Integer>();
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(".");
        className = className.substring(lastDotIndex + 1) + ".class";
        InputStream is = clazz.getResourceAsStream(className);
        try {
            ClassReader classReader = new ClassReader(is);
            classReader.accept(new ClassVisitor(Opcodes.ASM4) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    // 只处理指定的方法
                    Type[] argumentTypes = Type.getArgumentTypes(desc);
                    if (!method.getName().equals(name) || !Arrays.equals(argumentTypes, types)) {
                        return null;
                    }
                    return new MethodVisitor(Opcodes.ASM4) {
                        @Override
                        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                        	//只考虑实例方法,不考虑static方法
                        	Integer nameIndex = indexMap.get("nameIndex");
                        	if(null==nameIndex){//第一个参数为this
                        		indexMap.put("nameIndex", 0);
                        		return;
                        	}else{
                        		if(nameIndex>=parameterNames.length){
                        			return;
                        		}
                        		parameterNames[nameIndex] = name;
                        		indexMap.put("nameIndex", nameIndex+1);
                        	}
                        }
                    };

                }
            }, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parameterNames;
    }
    
    public void method1(String param1, String param2) {  
        System.out.println(param1);  
    } 
    
    public void method2(String param1, Integer a, int b, ParameterNameUtil p) {  
        System.out.println(param1);  
    }  
  
    public static void main(String[] args) throws Exception {
        Class<?> clazz = ParameterNameUtil.class;  
        Method method1 = clazz.getDeclaredMethod("method1", String.class, String.class);  
        String[] parameterNames1 = ParameterNameUtil.getParameterNames(clazz, method1);  
        System.out.println("parameterNames1 = " + Arrays.toString(parameterNames1));
        System.out.println("=======================================");
        Method[] methods = ParameterNameUtil.class.getDeclaredMethods();
        for(Method m:methods){
        	if("method2".equals(m.getName())){
        		String[] parameterNames2 = ParameterNameUtil.getParameterNames(clazz, m);  
                System.out.println("parameterNames2 = " + Arrays.toString(parameterNames2));
        	}
        }
    }  

}