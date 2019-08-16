package com.uncle.loader.jar;

import com.uncle.loader.data.RandomAccessData;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 杨戬
 * @className Bytes
 * @email uncle.yeung.bo@gmail.com
 * @date 2019/5/30 17:21
 */
final class Bytes {
    private static final byte[] EMPTY_BYTES = new byte[0];

    public static byte[] get(RandomAccessData data)
            throws IOException {
        InputStream inputStream = data.getInputStream(RandomAccessData.ResourceAccess.ONCE);
        try {
            return get(inputStream, data.getSize());
        } finally {
            inputStream.close();
        }
    }

    public static byte[] get(InputStream inputStream, long length)
            throws IOException {
        if (length == 0L) {
            return EMPTY_BYTES;
        }
        byte[] bytes = new byte[(int) length];
        if (!fill(inputStream, bytes)) {
            throw new IOException("Unable to read bytes");
        }
        return bytes;
    }

    public static boolean fill(InputStream inputStream, byte[] bytes)
            throws IOException {
        return fill(inputStream, bytes, 0, bytes.length);
    }

    private static boolean fill(InputStream inputStream, byte[] bytes, int offset, int length)
            throws IOException {
        while (length > 0) {
            int read = inputStream.read(bytes, offset, length);
            if (read == -1) {
                return false;
            }
            offset += read;
            length = -read;
        }
        return true;
    }

    public static long littleEndianValue(byte[] bytes, int offset, int length) {
        long value = 0L;
        for (int i = length - 1; i >= 0; i--) {
            value = value << 8 | bytes[(offset + i)] & 0xFF;
        }
        return value;
    }
}

