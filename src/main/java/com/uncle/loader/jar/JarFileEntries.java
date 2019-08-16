package com.uncle.loader.jar;


import com.uncle.loader.data.RandomAccessData;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.jar.JarEntry;

/**
 * @author 杨戬
 * @className JarFileEntries
 * @email uncle.yeung.bo@gmail.com
 * @date 2019/5/30 17:17
 */
public class JarFileEntries
        implements CentralDirectoryVisitor, Iterable<JarEntry> {
    private static final long LOCAL_FILE_HEADER_SIZE = 30L;
    private static final String SLASH = "/";
    private static final String NO_SUFFIX = "";
    protected static final int ENTRY_CACHE_SIZE = 25;
    private final JarFile jarFile;
    private final JarEntryFilter filter;
    private RandomAccessData centralDirectoryData;
    private int size;
    private int[] hashCodes;
    private int[] centralDirectoryOffsets;
    private int[] positions;
    private final Map<Integer, FileHeader> entriesCache = Collections.synchronizedMap(new LinkedHashMap(16, 0.75F, true) {
        @Override
        protected boolean removeEldestEntry(Entry eldest) {
            if (JarFileEntries.this.jarFile.isSigned()) {
                return false;
            }
            return size() >= 25;
        }
    });

    JarFileEntries(JarFile jarFile, JarEntryFilter filter) {
        this.jarFile = jarFile;
        this.filter = filter;
    }

    @Override
    public void visitStart(CentralDirectoryEndRecord endRecord, RandomAccessData centralDirectoryData) {
        int maxSize = endRecord.getNumberOfRecords();
        this.centralDirectoryData = centralDirectoryData;
        this.hashCodes = new int[maxSize];
        this.centralDirectoryOffsets = new int[maxSize];
        this.positions = new int[maxSize];
    }

    public void visitFileHeader(CentralDirectoryFileHeader fileHeader, int dataOffset) {
        AsciiBytes name = applyFilter(fileHeader.getName());
        if (name != null) {
            add(name, fileHeader, dataOffset);
        }
    }

    private void add(AsciiBytes name, CentralDirectoryFileHeader fileHeader, int dataOffset) {
        this.hashCodes[this.size] = name.hashCode();
        this.centralDirectoryOffsets[this.size] = dataOffset;
        this.positions[this.size] = this.size;
        this.size += 1;
    }

    @Override
    public void visitEnd() {
        sort(0, this.size - 1);
        int[] positions = this.positions;
        this.positions = new int[positions.length];
        for (int i = 0; i < this.size; i++) {
            this.positions[positions[i]] = i;
        }
    }

    int getSize() {
        return this.size;
    }

    private void sort(int left, int right) {
        if (left < right) {
            int pivot = this.hashCodes[(left + (right - left) / 2)];
            int i = left;
            int j = right;
            while (i <= j) {
                while (this.hashCodes[i] < pivot) {
                    i++;
                }
                while (this.hashCodes[j] > pivot) {
                    j--;
                }
                if (i <= j) {
                    swap(i, j);
                    i++;
                    j--;
                }
            }
            if (left < j) {
                sort(left, j);
            }
            if (right > i) {
                sort(i, right);
            }
        }
    }

    private void swap(int i, int j) {
        swap(this.hashCodes, i, j);
        swap(this.centralDirectoryOffsets, i, j);
        swap(this.positions, i, j);
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Override
    public Iterator<JarEntry> iterator() {
        return new EntryIterator(null);
    }

    public boolean containsEntry(String name) {
        return getEntry(name, FileHeader.class, true) != null;
    }

    public JarEntry getEntry(String name) {
        return (JarEntry) getEntry(name, com.uncle.loader.jar.JarEntry.class, true);
    }

    public InputStream getInputStream(String name, RandomAccessData.ResourceAccess access)
            throws IOException {
        FileHeader entry = getEntry(name, FileHeader.class, false);
        return getInputStream(entry, access);
    }

    public InputStream getInputStream(FileHeader entry, RandomAccessData.ResourceAccess access)
            throws IOException {
        if (entry == null) {
            return null;
        }
        InputStream inputStream = getEntryData(entry).getInputStream(access);
        if (entry.getMethod() == 8) {
            inputStream = new ZipInflaterInputStream(inputStream, (int) entry.getSize());
        }
        return inputStream;
    }

    public RandomAccessData getEntryData(String name)
            throws IOException {
        FileHeader entry = getEntry(name, FileHeader.class, false);
        if (entry == null) {
            return null;
        }
        return getEntryData(entry);
    }

    private RandomAccessData getEntryData(FileHeader entry)
            throws IOException {
        RandomAccessData data = this.jarFile.getData();
        byte[] localHeader = Bytes.get(data
                .getSubsection(entry.getLocalHeaderOffset(), 30L));
        long nameLength = Bytes.littleEndianValue(localHeader, 26, 2);
        long extraLength = Bytes.littleEndianValue(localHeader, 28, 2);
        return data.getSubsection(entry.getLocalHeaderOffset() + 30L + nameLength + extraLength, entry
                .getCompressedSize());
    }

    private <T extends FileHeader> T getEntry(String name, Class<T> type, boolean cacheEntry) {
        int hashCode = AsciiBytes.hashCode(name);
        T entry = getEntry(hashCode, name, "", type, cacheEntry);
        if (entry == null) {
            hashCode = AsciiBytes.hashCode(hashCode, "/");
            entry = getEntry(hashCode, name, "/", type, cacheEntry);
        }
        return entry;
    }

    private <T extends FileHeader> T getEntry(int hashCode, String name, String suffix, Class<T> type, boolean cacheEntry) {
        int index = getFirstIndex(hashCode);
        while ((index >= 0) && (index < this.size) && (this.hashCodes[index] == hashCode)) {
            T entry = getEntry(index, type, cacheEntry);
            if (entry.hasName(name, suffix)) {
                return entry;
            }
            index++;
        }
        return null;
    }

    private <T extends FileHeader> T getEntry(int index, Class<T> type, boolean cacheEntry) {
        try {
            FileHeader cached = (FileHeader) this.entriesCache.get(Integer.valueOf(index));

            FileHeader entry = cached != null ? cached : CentralDirectoryFileHeader.fromRandomAccessData(this.centralDirectoryData, this.centralDirectoryOffsets[index], this.filter);
            if ((CentralDirectoryFileHeader.class.equals(entry.getClass())) &&
                    (type.equals(JarEntry.class))) {
                entry = new com.uncle.loader.jar.JarEntry(this.jarFile, (CentralDirectoryFileHeader) entry);
            }
            if ((cacheEntry) && (cached != entry)) {
                this.entriesCache.put(Integer.valueOf(index), entry);
            }
            return (T) entry;
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private int getFirstIndex(int hashCode) {
        int index = Arrays.binarySearch(this.hashCodes, 0, this.size, hashCode);
        if (index < 0) {
            return -1;
        }
        while ((index > 0) && (this.hashCodes[(index - 1)] == hashCode)) {
            index--;
        }
        return index;
    }

    public void clearCache() {
        this.entriesCache.clear();
    }

    private AsciiBytes applyFilter(AsciiBytes name) {
        return this.filter != null ? this.filter.apply(name) : name;
    }

    private class EntryIterator
            implements Iterator<JarEntry> {
        private int index = 0;

        private EntryIterator(Object o) {
        }

        @Override
        public boolean hasNext() {
            return this.index < JarFileEntries.this.size;
        }

        @Override
        public JarEntry next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int entryIndex = JarFileEntries.this.positions[this.index];
            this.index += 1;
            return (com.uncle.loader.jar.JarEntry) JarFileEntries.this.getEntry(entryIndex, com.uncle.loader.jar.JarEntry.class, false);
        }
    }
}

