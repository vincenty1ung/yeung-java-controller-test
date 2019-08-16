package com.uncle.loader.jar;

/**
 * @author 杨戬
 * @className FileHeader
 * @email uncle.yeung.bo@gmail.com
 * @date 2019/5/30 17:23
 */
interface FileHeader {
    boolean hasName(String paramString1, String paramString2);

    long getLocalHeaderOffset();

    long getCompressedSize();

    long getSize();

    int getMethod();
}
