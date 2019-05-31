package com.uncle.controller.loader;

import com.uncle.controller.loader.archive.Archive;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

/**
 * @author 杨戬
 * @className ExecutableArchiveLauncher
 * @email yangb@chaosource.com
 * @date 2019/5/31 10:00
 */
public abstract class ExecutableArchiveLauncher
        extends Launcher {
    private final Archive archive;

    public ExecutableArchiveLauncher() {
        try {
            this.archive = createArchive();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    protected ExecutableArchiveLauncher(Archive archive) {
        this.archive = archive;
    }

    protected final Archive getArchive() {
        return this.archive;
    }

    @Override
    protected String getMainClass()
            throws Exception {
        Manifest manifest = this.archive.getManifest();
        String mainClass = null;
        if (manifest != null) {
            mainClass = manifest.getMainAttributes().getValue("Start-Class");
        }
        if (mainClass == null) {
            throw new IllegalStateException("No 'Start-Class' manifest entry specified in " + this);
        }
        return mainClass;
    }

    @Override
    protected List<Archive> getClassPathArchives()
            throws Exception {
        List<Archive> archives = new ArrayList(this.archive.getNestedArchives(new Archive.EntryFilter() {
            @Override
            public boolean matches(Archive.Entry entry) {
                return ExecutableArchiveLauncher.this.isNestedArchive(entry);
            }
        }));
        postProcessClassPathArchives(archives);
        return archives;
    }


    protected abstract boolean isNestedArchive(Archive.Entry paramEntry);

    protected void postProcessClassPathArchives(List<Archive> archives)
            throws Exception {
    }
}
