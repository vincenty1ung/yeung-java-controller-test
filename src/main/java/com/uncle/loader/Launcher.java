package com.uncle.loader;

import com.uncle.loader.archive.Archive;
import com.uncle.loader.archive.ExplodedArchive;
import com.uncle.loader.archive.JarFileArchive;
import com.uncle.loader.jar.JarFile;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 杨戬
 * @className Launcher
 * @email yangb@chaosource.com
 * @date 2019/5/31 09:56
 */
public abstract class Launcher {
    protected void launch(String[] args)
            throws Exception {
        JarFile.registerUrlProtocolHandler();
        ClassLoader classLoader = createClassLoader(getClassPathArchives());
        launch(args, getMainClass(), classLoader);
    }

    protected ClassLoader createClassLoader(List<Archive> archives)
            throws Exception {
        List<URL> urls = new ArrayList(archives.size());
        for (Archive archive : archives) {
            urls.add(archive.getUrl());
        }
        return createClassLoader((URL[]) urls.toArray(new URL[urls.size()]));
    }

    protected ClassLoader createClassLoader(URL[] urls)
            throws Exception {
        return new LaunchedURLClassLoader(urls, getClass().getClassLoader());
    }

    protected void launch(String[] args, String mainClass, ClassLoader classLoader)
            throws Exception {
        Thread.currentThread().setContextClassLoader(classLoader);
        createMainMethodRunner(mainClass, args, classLoader).run();
    }

    protected MainMethodRunner createMainMethodRunner(String mainClass, String[] args, ClassLoader classLoader) {
        return new MainMethodRunner(mainClass, args);
    }

    protected abstract String getMainClass()
            throws Exception;

    protected abstract List<Archive> getClassPathArchives()
            throws Exception;

    protected final Archive createArchive()
            throws Exception {
        ProtectionDomain protectionDomain = getClass().getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        URI location = codeSource != null ? codeSource.getLocation().toURI() : null;
        String path = location != null ? location.getSchemeSpecificPart() : null;
        if (path == null) {
            throw new IllegalStateException("Unable to determine code source archive");
        }
        File root = new File(path);
        if (!root.exists()) {
            throw new IllegalStateException("Unable to determine code source archive from " + root);
        }
        return root.isDirectory() ? new ExplodedArchive(root) : new JarFileArchive(root);
    }
}
