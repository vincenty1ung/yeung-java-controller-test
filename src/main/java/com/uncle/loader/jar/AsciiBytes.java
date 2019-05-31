package com.uncle.loader.jar;

import java.nio.charset.Charset;

/**
 * @author 杨戬
 * @className AsciiBytes
 * @email yangb@chaosource.com
 * @date 2019/5/30 17:16
 */

final class AsciiBytes {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final byte[] bytes;
    private final int offset;
    private final int length;
    private String string;
    private int hash;

    AsciiBytes(String string) {
        this(string.getBytes(UTF_8));
        this.string = string;
    }

    AsciiBytes(byte[] bytes) {
        this(bytes, 0, bytes.length);
    }

    AsciiBytes(byte[] bytes, int offset, int length) {
        if ((offset < 0) || (length < 0) || (offset + length > bytes.length)) {
            throw new IndexOutOfBoundsException();
        }
        this.bytes = bytes;
        this.offset = offset;
        this.length = length;
    }

    public int length() {
        return this.length;
    }

    public boolean startsWith(AsciiBytes prefix) {
        if (this == prefix) {
            return true;
        }
        if (prefix.length > this.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (this.bytes[(i + this.offset)] != prefix.bytes[(i + prefix.offset)]) {
                return false;
            }
        }
        return true;
    }

    public boolean endsWith(AsciiBytes postfix) {
        if (this == postfix) {
            return true;
        }
        if (postfix.length > this.length) {
            return false;
        }
        for (int i = 0; i < postfix.length; i++) {
            if (this.bytes[(this.offset + (this.length - 1) - i)] != postfix.bytes[(postfix.offset + (postfix.length - 1) - i)]) {
                return false;
            }
        }
        return true;
    }

    public AsciiBytes substring(int beginIndex) {
        return substring(beginIndex, this.length);
    }

    public AsciiBytes substring(int beginIndex, int endIndex) {
        int length = endIndex - beginIndex;
        if (this.offset + length > this.bytes.length) {
            throw new IndexOutOfBoundsException();
        }
        return new AsciiBytes(this.bytes, this.offset + beginIndex, length);
    }

    public AsciiBytes append(String string) {
        if ((string == null) || (string.isEmpty())) {
            return this;
        }
        return append(string.getBytes(UTF_8));
    }

    public AsciiBytes append(AsciiBytes asciiBytes) {
        if ((asciiBytes == null) || (asciiBytes.length() == 0)) {
            return this;
        }
        return append(asciiBytes.bytes);
    }

    public AsciiBytes append(byte[] bytes) {
        if ((bytes == null) || (bytes.length == 0)) {
            return this;
        }
        byte[] combined = new byte[this.length + bytes.length];
        System.arraycopy(this.bytes, this.offset, combined, 0, this.length);
        System.arraycopy(bytes, 0, combined, this.length, bytes.length);
        return new AsciiBytes(combined);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj.getClass().equals(AsciiBytes.class)) {
            AsciiBytes other = (AsciiBytes) obj;
            if (this.length == other.length) {
                for (int i = 0; i < this.length; i++) {
                    if (this.bytes[(this.offset + i)] != other.bytes[(other.offset + i)]) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int hash = this.hash;
        if ((hash == 0) && (this.bytes.length > 0)) {
            for (int i = this.offset; i < this.offset + this.length; i++) {
                int b = this.bytes[i];
                if (b < 0) {
                    b &= 0x7F;

                    int excess = 128;
                    int limit;
                    if (b < 96) {
                        limit = 1;
                        excess += 4096;
                    } else if (b < 112) {
                        limit = 2;
                        excess += 401408;
                    } else {
                        limit = 3;
                        excess += 29892608;
                    }
                    for (int j = 0; j < limit; j++) {
                        b = (b << 6) + (this.bytes[(++i)] & 0xFF);
                    }
                    b -= excess;
                }
                if (b <= 65535) {
                    hash = 31 * hash + b;
                } else {
                    hash = 31 * hash + ((b >> 10) + 55232);
                    hash = 31 * hash + ((b & 0x3FF) + 56320);
                }
            }
            this.hash = hash;
        }
        return hash;
    }

    @Override
    public String toString() {
        if (this.string == null) {
            this.string = new String(this.bytes, this.offset, this.length, UTF_8);
        }
        return this.string;
    }

    static String toString(byte[] bytes) {
        return new String(bytes, UTF_8);
    }

    public static int hashCode(String string) {
        return string.hashCode();
    }

    public static int hashCode(int hash, String string) {
        for (int i = 0; i < string.length(); i++) {
            hash = 31 * hash + string.charAt(i);
        }
        return hash;
    }
}
