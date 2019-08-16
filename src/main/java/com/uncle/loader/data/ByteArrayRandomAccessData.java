package com.uncle.loader.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author 杨戬
 * @className ByteArrayRandomAccessData
 * @email uncle.yeung.bo@gmail.com
 * @date 2019/5/30 17:11
 */
public class ByteArrayRandomAccessData implements RandomAccessData {
    private final byte[] bytes;
    private final long offset;
    private final long length;

    public ByteArrayRandomAccessData(byte[] bytes) {
        this(bytes, 0L, bytes != null ? bytes.length : 0L);
    }

    public ByteArrayRandomAccessData(byte[] bytes, long offset, long length) {
        this.bytes = (bytes != null ? bytes : new byte[0]);
        this.offset = offset;
        this.length = length;
    }

    @Override
    public InputStream getInputStream(RandomAccessData.ResourceAccess access) {
        return new ByteArrayInputStream(this.bytes, (int) this.offset, (int) this.length);
    }

    @Override
    public RandomAccessData getSubsection(long offset, long length) {
        return new ByteArrayRandomAccessData(this.bytes, this.offset + offset, length);
    }

    @Override
    public long getSize() {
        return this.length;
    }
}
