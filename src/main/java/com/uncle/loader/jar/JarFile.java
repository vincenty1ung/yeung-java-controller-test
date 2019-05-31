package com.uncle.loader.jar;

import com.uncle.loader.data.RandomAccessData;
import com.uncle.loader.data.RandomAccessData.ResourceAccess;
import com.uncle.loader.data.RandomAccessDataFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

/**
 * @author 杨戬
 * @className JarFile
 * @email yangb@chaosource.com
 * @date 2019/5/30 17:07
 */
public class JarFile extends java.util.jar.JarFile {
    private static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";
    private static final String PROTOCOL_HANDLER = "java.protocol.handler.pkgs";
    private static final String HANDLERS_PACKAGE = "org.springframework.boot.loader";
    private static final AsciiBytes META_INF = new AsciiBytes("META-INF/");
    private static final AsciiBytes SIGNATURE_FILE_EXTENSION = new AsciiBytes(".SF");
    private final RandomAccessDataFile rootFile;
    private final String pathFromRoot;
    private final RandomAccessData data;
    private final JarFile.JarFileType type;
    private URL url;
    private JarFileEntries entries;
    private SoftReference<Manifest> manifest;
    private boolean signed;

    public JarFile(File file) throws IOException {
        this(new RandomAccessDataFile(file));
    }

    JarFile(RandomAccessDataFile file) throws IOException {
        this(file, "", file, JarFile.JarFileType.DIRECT);
    }

    private JarFile(RandomAccessDataFile rootFile, String pathFromRoot, RandomAccessData data, JarFile.JarFileType type) throws IOException {
        this(rootFile, pathFromRoot, data, (JarEntryFilter) null, type);
    }

    private JarFile(RandomAccessDataFile rootFile, String pathFromRoot, RandomAccessData data, JarEntryFilter filter, JarFile.JarFileType type) throws IOException {
        super(rootFile.getFile());
        this.rootFile = rootFile;
        this.pathFromRoot = pathFromRoot;
        CentralDirectoryParser parser = new CentralDirectoryParser();
        this.entries = (JarFileEntries) parser.addVisitor(new JarFileEntries(this, filter));
        parser.addVisitor(this.centralDirectoryVisitor());
        this.data = parser.parse(data, filter == null);
        this.type = type;
    }

    private CentralDirectoryVisitor centralDirectoryVisitor() {
        return new CentralDirectoryVisitor() {
            @Override
            public void visitStart(CentralDirectoryEndRecord endRecord, RandomAccessData centralDirectoryData) {
            }

            @Override
            public void visitFileHeader(CentralDirectoryFileHeader fileHeader, int dataOffset) {
                AsciiBytes name = fileHeader.getName();
                if (name.startsWith(JarFile.META_INF) && name.endsWith(JarFile.SIGNATURE_FILE_EXTENSION)) {
                    JarFile.this.signed = true;
                }

            }

            @Override
            public void visitEnd() {
            }
        };
    }

    protected final RandomAccessDataFile getRootJarFile() {
        return this.rootFile;
    }

    RandomAccessData getData() {
        return this.data;
    }

    @Override
    public Manifest getManifest() throws IOException {
        Manifest manifest = this.manifest != null ? (Manifest) this.manifest.get() : null;
        if (manifest == null) {
            if (this.type == JarFile.JarFileType.NESTED_DIRECTORY) {
                manifest = (new JarFile(this.getRootJarFile())).getManifest();
            } else {
                InputStream inputStream = this.getInputStream("META-INF/MANIFEST.MF", RandomAccessData.ResourceAccess.ONCE);
                if (inputStream == null) {
                    return null;
                }

                try {
                    manifest = new Manifest(inputStream);
                } finally {
                    inputStream.close();
                }
            }

            this.manifest = new SoftReference(manifest);
        }

        return manifest;
    }

    @Override
    public Enumeration<JarEntry> entries() {
        final Iterator<JarEntry> iterator = this.entries.iterator();
        return new Enumeration<JarEntry>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public JarEntry nextElement() {
                return (JarEntry) iterator.next();
            }
        };
    }

    @Override
    public com.uncle.loader.jar.JarEntry getJarEntry(String name) {
        return (com.uncle.loader.jar.JarEntry) this.getEntry(name);
    }

    public boolean containsEntry(String name) {
        return this.entries.containsEntry(name);
    }

    @Override
    public ZipEntry getEntry(String name) {
        return this.entries.getEntry(name);
    }

    @Override
    public synchronized InputStream getInputStream(ZipEntry ze) throws IOException {
        return this.getInputStream(ze, ResourceAccess.PER_READ);
    }

    public InputStream getInputStream(ZipEntry ze, ResourceAccess access) throws IOException {
        return ze instanceof com.uncle.loader.jar.JarEntry ? this.entries.getInputStream((com.uncle.loader.jar.JarEntry) ze, access) : this.getInputStream(ze != null ? ze.getName() : null, access);
    }

    InputStream getInputStream(String name, ResourceAccess access) throws IOException {
        return this.entries.getInputStream(name, access);
    }

    public synchronized JarFile getNestedJarFile(ZipEntry entry) throws IOException {
        return this.getNestedJarFile((com.uncle.loader.jar.JarEntry) entry);
    }

    public synchronized JarFile getNestedJarFile(com.uncle.loader.jar.JarEntry entry) throws IOException {
        try {
            return this.createJarFileFromEntry(entry);
        } catch (Exception var3) {
            throw new IOException("Unable to open nested jar file '" + entry.getName() + "'", var3);
        }
    }

    private JarFile createJarFileFromEntry(com.uncle.loader.jar.JarEntry entry) throws IOException {
        return entry.isDirectory() ? this.createJarFileFromDirectoryEntry(entry) : this.createJarFileFromFileEntry(entry);
    }

    private JarFile createJarFileFromDirectoryEntry(com.uncle.loader.jar.JarEntry entry) throws IOException {
        final AsciiBytes sourceName = new AsciiBytes(entry.getName());
        JarEntryFilter filter = new JarEntryFilter() {
            @Override
            public AsciiBytes apply(AsciiBytes name) {
                return name.startsWith(sourceName) && !name.equals(sourceName) ? name.substring(sourceName.length()) : null;
            }
        };
        return new JarFile(this.rootFile, this.pathFromRoot + "!/" + entry.getName().substring(0, sourceName.length() - 1), this.data, filter, JarFile.JarFileType.NESTED_DIRECTORY);
    }

    private JarFile createJarFileFromFileEntry(com.uncle.loader.jar.JarEntry entry) throws IOException {
        if (entry.getMethod() != 0) {
            throw new IllegalStateException("Unable to open nested entry '" + entry.getName() + "'. It has been compressed and nested jar files must be stored without compression. Please check the mechanism used to create your executable jar file");
        } else {
            RandomAccessData entryData = this.entries.getEntryData(entry.getName());
            return new JarFile(this.rootFile, this.pathFromRoot + "!/" + entry.getName(), entryData, JarFile.JarFileType.NESTED_JAR);
        }
    }

    public int size() {
        return this.entries.getSize();
    }

    public void close() throws IOException {
        super.close();
        this.rootFile.close();
    }

    public URL getUrl() throws MalformedURLException {
        if (this.url == null) {
            Handler handler = new Handler(this);
            String file = this.rootFile.getFile().toURI() + this.pathFromRoot + "!/";
            file = file.replace("file:////", "file://");
            this.url = new URL("jar", "", -1, file, handler);
        }

        return this.url;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public String getName() {
        return this.rootFile.getFile() + this.pathFromRoot;
    }

    boolean isSigned() {
        return this.signed;
    }

    void setupEntryCertificates(com.uncle.loader.jar.JarEntry entry) {
        try {
            JarInputStream inputStream = new JarInputStream(this.getData().getInputStream(ResourceAccess.ONCE));

            try {
                for (JarEntry certEntry = inputStream.getNextJarEntry(); certEntry != null; certEntry = inputStream.getNextJarEntry()) {
                    inputStream.closeEntry();
                    if (entry.getName().equals(certEntry.getName())) {
                        this.setCertificates(entry, certEntry);
                    }

                    this.setCertificates(this.getJarEntry(certEntry.getName()), certEntry);
                }
            } finally {
                inputStream.close();
            }

        } catch (IOException var8) {
            throw new IllegalStateException(var8);
        }
    }

    private void setCertificates(com.uncle.loader.jar.JarEntry entry, JarEntry certEntry) {
        if (entry != null) {
            entry.setCertificates(certEntry);
        }

    }

    public void clearCache() {
        this.entries.clearCache();
    }

    protected String getPathFromRoot() {
        return this.pathFromRoot;
    }

    JarFile.JarFileType getType() {
        return this.type;
    }

    public static void registerUrlProtocolHandler() {
        String handlers = System.getProperty("java.protocol.handler.pkgs", "");
        System.setProperty("java.protocol.handler.pkgs", "".equals(handlers) ? "org.springframework.boot.loader" : handlers + "|" + "org.springframework.boot.loader");
        resetCachedUrlHandlers();
    }

    private static void resetCachedUrlHandlers() {
        try {
            URL.setURLStreamHandlerFactory((URLStreamHandlerFactory) null);
        } catch (Error var1) {
        }

    }

    static enum JarFileType {
        DIRECT,
        NESTED_DIRECTORY,
        NESTED_JAR;

        private JarFileType() {
        }
    }
}
