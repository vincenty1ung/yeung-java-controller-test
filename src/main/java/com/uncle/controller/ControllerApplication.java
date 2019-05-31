package com.uncle.controller;

import com.uncle.loader.archive.Archive;
import com.uncle.loader.archive.ExplodedArchive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;

/**
 * @author 杨戬
 * @email yangb@email.com
 */
@SpringBootApplication
@Slf4j
public class ControllerApplication {

    public static void main(String[] args) throws IOException, URISyntaxException {
        classLoaderTest(args);
        SpringApplication.run(ControllerApplication.class, args);
    }

    static boolean isNestedArchive(Archive.Entry entry) {
        if (entry.isDirectory()) {
            return entry.getName().equals("BOOT-INF/classes/");
        }
        return entry.getName().startsWith("BOOT-INF/lib/");
    }

    static void classLoaderTest(String[] args) throws IOException, URISyntaxException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println("classLoader = " + classLoader);

        Enumeration<URL> resources = classLoader.getResources("META-INF/spring.factories");
        ProtectionDomain protectionDomain = ControllerApplication.class.getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        URI uri = codeSource.getLocation().toURI();

        String schemeSpecificPart = uri.getSchemeSpecificPart();
        File file = new File(schemeSpecificPart);
        if (file.isDirectory()) {
            System.out.println("file = " + file);
        }
        ExplodedArchive entries = new ExplodedArchive(file);
        entries.forEach(name -> System.out.println("名称 = " + name.getName()+"是否是文件夹:" + name.isDirectory()));

        List<Archive> archives = new ArrayList(entries.getNestedArchives(new Archive.EntryFilter() {
            @Override
            public boolean matches(Archive.Entry entry) {
                return isNestedArchive(entry);
            }
        }));

        System.out.println("archives = " + archives);

        List<Properties> result = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            System.out.println("url = " + url);
            Properties properties = PropertiesLoaderUtils.loadProperties(new UrlResource(url));
            result.add(properties);
        }
        System.out.println("result = " + result);

    }
}


