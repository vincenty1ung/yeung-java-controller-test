package com.uncle.loader.archive;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.jar.Manifest;

/**
 * @author 杨戬
 * @className Archive
 * @email uncle.yeung.bo@gmail.com
 * @date 2019/5/30 17:00
 */
public interface Archive extends Iterable<Archive.Entry> {
    URL getUrl() throws MalformedURLException;

    Manifest getManifest() throws IOException;

    List<Archive> getNestedArchives(Archive.EntryFilter entryFilter) throws IOException;

    interface EntryFilter {
        /**
         * 类型过滤
         *
         * @param entry
         * @return
         */
        boolean matches(Archive.Entry entry);
    }

    interface Entry {
        /**
         * 是否是文件夹
         *
         * @return 是否
         */
        boolean isDirectory();

        String getName();
    }
}
