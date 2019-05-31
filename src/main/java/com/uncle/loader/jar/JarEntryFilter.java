package com.uncle.loader.jar;

/**
 * @author 杨戬
 * @className JarEntryFilter
 * @email yangb@chaosource.com
 * @date 2019/5/30 17:24
 */
interface JarEntryFilter {
    public abstract AsciiBytes apply(AsciiBytes paramAsciiBytes);
}
