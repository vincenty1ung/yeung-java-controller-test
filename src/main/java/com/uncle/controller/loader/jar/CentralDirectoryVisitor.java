package com.uncle.controller.loader.jar;

import com.uncle.controller.loader.data.RandomAccessData;

/**
 * @author 杨戬
 * @className CentralDirectoryVisitor
 * @email yangb@chaosource.com
 * @date 2019/5/30 17:19
 */
interface CentralDirectoryVisitor {
    public abstract void visitStart(CentralDirectoryEndRecord paramCentralDirectoryEndRecord, RandomAccessData paramRandomAccessData);

    public abstract void visitFileHeader(CentralDirectoryFileHeader paramCentralDirectoryFileHeader, int paramInt);

    public abstract void visitEnd();
}

