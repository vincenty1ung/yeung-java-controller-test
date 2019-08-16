package com.uncle.loader.jar;

/**
 * @author 杨戬
 * @className JarEntryFilter
 * @email uncle.yeung.bo@gmail.com
 * @date 2019/5/30 17:24
 */
interface JarEntryFilter {
    public abstract AsciiBytes apply(AsciiBytes paramAsciiBytes);
}
