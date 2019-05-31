package com.uncle.loader;

import com.uncle.loader.archive.Archive;
import lombok.NoArgsConstructor;

/**
 * @author 杨戬
 * @className JarLauncher
 * @email yangb@chaosource.com
 * @date 2019/5/31 10:01
 */
@NoArgsConstructor
public class JarLauncher
        extends ExecutableArchiveLauncher {
    static final String BOOT_INF_CLASSES = "BOOT-INF/classes/";
    static final String BOOT_INF_LIB = "BOOT-INF/lib/";


    protected JarLauncher(Archive archive) {
        super(archive);
    }

    @Override
    protected boolean isNestedArchive(Archive.Entry entry) {
        if (entry.isDirectory()) {
            return entry.getName().equals("BOOT-INF/classes/");
        }
        return entry.getName().startsWith("BOOT-INF/lib/");
    }

    public static void main(String[] args) throws Exception {
        JarLauncher jarLauncher = new JarLauncher();
        jarLauncher.launch(args);
    }
}

