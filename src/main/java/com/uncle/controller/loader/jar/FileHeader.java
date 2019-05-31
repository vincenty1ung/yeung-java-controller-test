package com.uncle.controller.loader.jar;

/**
 * @author 杨戬
 * @className FileHeader
 * @email yangb@chaosource.com
 * @date 2019/5/30 17:23
 */
interface FileHeader {
    boolean hasName(String paramString1, String paramString2);

    long getLocalHeaderOffset();

    long getCompressedSize();

    long getSize();

    int getMethod();
}
