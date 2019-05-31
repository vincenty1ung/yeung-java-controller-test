package com.uncle.loader;

import java.lang.reflect.Method;

/**
 * @author 杨戬
 * @className MainMethodRunner
 * @email yangb@chaosource.com
 * @date 2019/5/31 09:59
 */
public class MainMethodRunner {
    private final String mainClassName;
    private final String[] args;

    MainMethodRunner(String mainClass, String[] args) {
        this.mainClassName = mainClass;
        this.args = (args != null ? (String[]) args.clone() : null);
    }

    void run()
            throws Exception {
        Class<?> mainClass = Thread.currentThread().getContextClassLoader().loadClass(this.mainClassName);
        Method mainMethod = mainClass.getDeclaredMethod("main", new Class[]{String[].class});
        mainMethod.invoke(null, new Object[]{this.args});
    }
}