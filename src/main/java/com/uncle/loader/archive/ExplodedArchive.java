package com.uncle.loader.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.Manifest;

/**
 * @author 杨戬
 * @className ExplodedArchive
 * @email yangb@chaosource.com
 * @date 2019/5/30 17:02
 */
public class ExplodedArchive implements Archive {
    private static final Set<String> SKIPPED_NAMES = new HashSet(Arrays.asList(".", ".."));
    private final File root;
    private final boolean recursive;
    private File manifestFile;
    private Manifest manifest;

    public ExplodedArchive(File root) {
        this(root, true);
    }

    public ExplodedArchive(File root, boolean recursive) {
        if (root.exists() && root.isDirectory()) {
            this.root = root;
            this.recursive = recursive;
            this.manifestFile = this.getManifestFile(root);
        } else {
            throw new IllegalArgumentException("Invalid source folder " + root);
        }
    }

    private File getManifestFile(File root) {
        File metaInf = new File(root, "META-INF");
        return new File(metaInf, "MANIFEST.MF");
    }

    @Override
    public URL getUrl() throws MalformedURLException {
        return this.root.toURI().toURL();
    }

    @Override
    public Manifest getManifest() throws IOException {
        if (this.manifest == null && this.manifestFile.exists()) {
            FileInputStream inputStream = new FileInputStream(this.manifestFile);

            try {
                this.manifest = new Manifest(inputStream);
            } finally {
                inputStream.close();
            }
        }

        return this.manifest;
    }

    @Override
    public List<Archive> getNestedArchives(EntryFilter filter) throws IOException {
        List<Archive> nestedArchives = new ArrayList();
        Iterator var3 = this.iterator();

        while (var3.hasNext()) {
            Entry entry = (Entry) var3.next();
            if (filter.matches(entry)) {
                nestedArchives.add(this.getNestedArchive(entry));
            }
        }

        return Collections.unmodifiableList(nestedArchives);
    }

    @Override
    public Iterator<Entry> iterator() {
        return new ExplodedArchive.FileEntryIterator(this.root, this.recursive);
    }

    protected Archive getNestedArchive(Entry entry) throws IOException {
        File file = ((ExplodedArchive.FileEntry) entry).getFile();
        return (Archive) (file.isDirectory() ? new ExplodedArchive(file) : new JarFileArchive(file));
    }

    @Override
    public String toString() {
        try {
            return this.getUrl().toString();
        } catch (Exception var2) {
            return "exploded archive";
        }
    }

    private static class FileEntry implements Entry {
        private final String name;
        private final File file;

        FileEntry(String name, File file) {
            this.name = name;
            this.file = file;
        }

        public File getFile() {
            return this.file;
        }

        @Override
        public boolean isDirectory() {
            return this.file.isDirectory();
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    private static class FileEntryIterator implements Iterator<Entry> {
        private final Comparator<File> entryComparator = new ExplodedArchive.FileEntryIterator.EntryComparator();
        private final File root;
        private final boolean recursive;
        private final Deque<Iterator<File>> stack = new LinkedList();
        private File current;

        FileEntryIterator(File root, boolean recursive) {
            this.root = root;
            this.recursive = recursive;
            this.stack.add(this.listFiles(root));
            this.current = this.poll();
        }

        @Override
        public boolean hasNext() {
            return this.current != null;
        }

        @Override
        public Entry next() {
            if (this.current == null) {
                throw new NoSuchElementException();
            } else {
                File file = this.current;
                if (file.isDirectory() && (this.recursive || file.getParentFile().equals(this.root))) {
                    this.stack.addFirst(this.listFiles(file));
                }

                this.current = this.poll();
                String name = file.toURI().getPath().substring(this.root.toURI().getPath().length());
                return new ExplodedArchive.FileEntry(name, file);
            }
        }

        private Iterator<File> listFiles(File file) {
            File[] files = file.listFiles();
            if (files == null) {
                return null;
            } else {
                Arrays.sort(files, this.entryComparator);
                return Arrays.asList(files).iterator();
            }
        }

        private File poll() {
            label17:
            while (true) {
                if (!this.stack.isEmpty()) {
                    File file;
                    do {
                        if (!((Iterator) this.stack.peek()).hasNext()) {
                            this.stack.poll();
                            continue label17;
                        }

                        file = (File) ((Iterator) this.stack.peek()).next();
                    } while (ExplodedArchive.SKIPPED_NAMES.contains(file.getName()));

                    return file;
                }

                return null;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }

        private static class EntryComparator implements Comparator<File> {
            private EntryComparator() {
            }

            @Override
            public int compare(File o1, File o2) {
                return o1.getAbsolutePath().compareTo(o2.getAbsolutePath());
            }
        }
    }
}

