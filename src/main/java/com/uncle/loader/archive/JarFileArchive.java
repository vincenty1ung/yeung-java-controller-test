package com.uncle.loader.archive;

import com.uncle.loader.data.RandomAccessData;
import com.uncle.loader.jar.JarFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.Manifest;

/**
 * @author 杨戬
 * @className JarFileArchive
 * @email yangb@chaosource.com
 * @date 2019/5/30 17:04
 */
public class JarFileArchive implements Archive {
    private static final String UNPACK_MARKER = "UNPACK:";
    private static final int BUFFER_SIZE = 32768;
    private final com.uncle.loader.jar.JarFile jarFile;
    private URL url;
    private File tempUnpackFolder;

    public JarFileArchive(File file) throws IOException {
        this(file, (URL) null);
    }

    public JarFileArchive(File file, URL url) throws IOException {
        this(new com.uncle.loader.jar.JarFile(file));
        this.url = url;
    }

    public JarFileArchive(com.uncle.loader.jar.JarFile jarFile) {
        this.jarFile = jarFile;
    }

    @Override
    public URL getUrl() throws MalformedURLException {
        return this.url != null ? this.url : this.jarFile.getUrl();
    }

    @Override
    public Manifest getManifest() throws IOException {
        return this.jarFile.getManifest();
    }

    @Override
    public List<Archive> getNestedArchives(EntryFilter filter) throws IOException {
        List<Archive> nestedArchives = new ArrayList();

        for (Entry entry : this) {
            if (filter.matches(entry)) {
                nestedArchives.add(this.getNestedArchive(entry));
            }
        }

        return Collections.unmodifiableList(nestedArchives);
    }

    @Override
    public Iterator<Entry> iterator() {
        return new JarFileArchive.EntryIterator(this.jarFile.entries());
    }

    protected Archive getNestedArchive(Entry entry) throws IOException {
        JarEntry jarEntry = ((JarFileArchive.JarFileEntry) entry).getJarEntry();
        if (jarEntry.getComment().startsWith("UNPACK:")) {
            return this.getUnpackedNestedArchive(jarEntry);
        } else {
            try {
                JarFile jarFile = this.jarFile.getNestedJarFile(jarEntry);
                return new JarFileArchive(jarFile);
            } catch (Exception var4) {
                throw new IllegalStateException("Failed to get nested archive for entry " + entry.getName(), var4);
            }
        }
    }

    private Archive getUnpackedNestedArchive(JarEntry jarEntry) throws IOException {
        String name = jarEntry.getName();
        if (name.lastIndexOf("/") != -1) {
            name = name.substring(name.lastIndexOf("/") + 1);
        }

        File file = new File(this.getTempUnpackFolder(), name);
        if (!file.exists() || file.length() != jarEntry.getSize()) {
            this.unpack(jarEntry, file);
        }

        return new JarFileArchive(file, file.toURI().toURL());
    }

    private File getTempUnpackFolder() {
        if (this.tempUnpackFolder == null) {
            File tempFolder = new File(System.getProperty("java.io.tmpdir"));
            this.tempUnpackFolder = this.createUnpackFolder(tempFolder);
        }

        return this.tempUnpackFolder;
    }

    private File createUnpackFolder(File parent) {
        int var2 = 0;

        File unpackFolder;
        do {
            if (var2++ >= 1000) {
                throw new IllegalStateException("Failed to create unpack folder in directory '" + parent + "'");
            }

            String fileName = (new File(this.jarFile.getName())).getName();
            unpackFolder = new File(parent, fileName + "-spring-boot-libs-" + UUID.randomUUID());
        } while (!unpackFolder.mkdirs());

        return unpackFolder;
    }

    private void unpack(JarEntry entry, File file) throws IOException {
        InputStream inputStream = this.jarFile.getInputStream(entry, RandomAccessData.ResourceAccess.ONCE);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);

            try {
                byte[] buffer = new byte['耀'];

                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();
            } finally {
                outputStream.close();
            }
        } finally {
            inputStream.close();
        }
    }

    @Override
    public String toString() {
        try {
            return this.getUrl().toString();
        } catch (Exception var2) {
            return "jar archive";
        }
    }

    private static class JarFileEntry implements Entry {
        private final JarEntry jarEntry;

        JarFileEntry(JarEntry jarEntry) {
            this.jarEntry = jarEntry;
        }

        public JarEntry getJarEntry() {
            return this.jarEntry;
        }

        @Override
        public boolean isDirectory() {
            return this.jarEntry.isDirectory();
        }

        @Override
        public String getName() {
            return this.jarEntry.getName();
        }
    }

    private static class EntryIterator implements Iterator<Entry> {
        private final Enumeration<JarEntry> enumeration;

        EntryIterator(Enumeration<JarEntry> enumeration) {
            this.enumeration = enumeration;
        }

        @Override
        public boolean hasNext() {
            return this.enumeration.hasMoreElements();
        }

        @Override
        public Entry next() {
            return new JarFileArchive.JarFileEntry((JarEntry) this.enumeration.nextElement());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }
}
