package com.uncle.loader.jar;

import com.uncle.loader.data.RandomAccessData;

/**
 * @author 杨戬
 * @className CentralDirectoryVisitor
 * @email uncle.yeung.bo@gmail.com
 * @date 2019/5/30 17:19
 */
interface CentralDirectoryVisitor {
    public abstract void visitStart(CentralDirectoryEndRecord paramCentralDirectoryEndRecord, RandomAccessData paramRandomAccessData);

    public abstract void visitFileHeader(CentralDirectoryFileHeader paramCentralDirectoryFileHeader, int paramInt);

    public abstract void visitEnd();
}

