package com.uncle.controller.loader.jar;

import com.uncle.controller.loader.data.RandomAccessData;

import java.io.IOException;

/**
 * @author 杨戬
 * @className CentralDirectoryEndRecord
 * @email yangb@chaosource.com
 * @date 2019/5/30 17:20
 */
public class CentralDirectoryEndRecord {
    private static final int MINIMUM_SIZE = 22;
    private static final int MAXIMUM_COMMENT_LENGTH = 65535;
    private static final int MAXIMUM_SIZE = 65557;
    private static final int SIGNATURE = 101010256;
    private static final int COMMENT_LENGTH_OFFSET = 20;
    private static final int READ_BLOCK_SIZE = 256;
    private byte[] block;
    private int offset;
    private int size;

    CentralDirectoryEndRecord(RandomAccessData data)
            throws IOException {
        this.block = createBlockFromEndOfData(data, 256);
        this.size = 22;
        this.offset = (this.block.length - this.size);
        while (!isValid()) {
            this.size += 1;
            if (this.size > this.block.length) {
                if ((this.size >= 65557) || (this.size > data.getSize())) {
                    throw new IOException("Unable to find ZIP central directory records after reading " + this.size + " bytes");
                }
                this.block = createBlockFromEndOfData(data, this.size + 256);
            }
            this.offset = (this.block.length - this.size);
        }
    }

    private byte[] createBlockFromEndOfData(RandomAccessData data, int size)
            throws IOException {
        int length = (int) Math.min(data.getSize(), size);
        return Bytes.get(data.getSubsection(data.getSize() - length, length));
    }

    private boolean isValid() {
        if ((this.block.length < 22) ||
                (Bytes.littleEndianValue(this.block, this.offset + 0, 4) != 101010256L)) {
            return false;
        }
        long commentLength = Bytes.littleEndianValue(this.block, this.offset + 20, 2);

        return this.size == 22L + commentLength;
    }

    public long getStartOfArchive(RandomAccessData data) {
        long length = Bytes.littleEndianValue(this.block, this.offset + 12, 4);
        long specifiedOffset = Bytes.littleEndianValue(this.block, this.offset + 16, 4);
        long actualOffset = data.getSize() - this.size - length;
        return actualOffset - specifiedOffset;
    }

    public RandomAccessData getCentralDirectory(RandomAccessData data) {
        long offset = Bytes.littleEndianValue(this.block, this.offset + 16, 4);
        long length = Bytes.littleEndianValue(this.block, this.offset + 12, 4);
        return data.getSubsection(offset, length);
    }

    public int getNumberOfRecords() {
        long numberOfRecords = Bytes.littleEndianValue(this.block, this.offset + 10, 2);
        if (numberOfRecords == 65535L) {
            throw new IllegalStateException("Zip64 archives are not supported");
        }
        return (int) numberOfRecords;
    }
}

