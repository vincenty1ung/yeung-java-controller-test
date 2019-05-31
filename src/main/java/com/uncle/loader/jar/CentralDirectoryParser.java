package com.uncle.loader.jar;

import com.uncle.loader.data.RandomAccessData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 杨戬
 * @className CentralDirectoryParser
 * @email yangb@chaosource.com
 * @date 2019/5/30 17:26
 */
public class CentralDirectoryParser {
    private int CENTRAL_DIRECTORY_HEADER_BASE_SIZE = 46;
    private final List<CentralDirectoryVisitor> visitors = new ArrayList();

    public <T extends CentralDirectoryVisitor> T addVisitor(T visitor) {
        this.visitors.add(visitor);
        return visitor;
    }

    public RandomAccessData parse(RandomAccessData data, boolean skipPrefixBytes)
            throws IOException {
        CentralDirectoryEndRecord endRecord = new CentralDirectoryEndRecord(data);
        if (skipPrefixBytes) {
            data = getArchiveData(endRecord, data);
        }
        RandomAccessData centralDirectoryData = endRecord.getCentralDirectory(data);
        visitStart(endRecord, centralDirectoryData);
        parseEntries(endRecord, centralDirectoryData);
        visitEnd();
        return data;
    }

    private void parseEntries(CentralDirectoryEndRecord endRecord, RandomAccessData centralDirectoryData)
            throws IOException {
        byte[] bytes = Bytes.get(centralDirectoryData);
        CentralDirectoryFileHeader fileHeader = new CentralDirectoryFileHeader();
        int dataOffset = 0;
        for (int i = 0; i < endRecord.getNumberOfRecords(); i++) {
            fileHeader.load(bytes, dataOffset, null, 0, null);
            visitFileHeader(dataOffset, fileHeader);

            dataOffset = dataOffset + (this.CENTRAL_DIRECTORY_HEADER_BASE_SIZE + fileHeader.getName().length() + fileHeader.getComment().length() + fileHeader.getExtra().length);
        }
    }

    private RandomAccessData getArchiveData(CentralDirectoryEndRecord endRecord, RandomAccessData data) {
        long offset = endRecord.getStartOfArchive(data);
        if (offset == 0L) {
            return data;
        }
        return data.getSubsection(offset, data.getSize() - offset);
    }

    private void visitStart(CentralDirectoryEndRecord endRecord, RandomAccessData centralDirectoryData) {
        for (CentralDirectoryVisitor visitor : this.visitors) {
            visitor.visitStart(endRecord, centralDirectoryData);
        }
    }

    private void visitFileHeader(int dataOffset, CentralDirectoryFileHeader fileHeader) {
        for (CentralDirectoryVisitor visitor : this.visitors) {
            visitor.visitFileHeader(fileHeader, dataOffset);
        }
    }

    private void visitEnd() {
        for (CentralDirectoryVisitor visitor : this.visitors) {
            visitor.visitEnd();
        }
    }
}

