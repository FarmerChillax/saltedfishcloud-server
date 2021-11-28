package com.xiaotao.saltedfishcloud.compress.filesystem;

import lombok.Getter;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;

import java.io.IOException;
import java.io.InputStream;

public class ArchiveEntryInputStream extends InputStream {
    private final ArchiveInputStream inputStream;
    private long readCnt = 0;
    private long targetSize;
    @Getter
    private final ArchiveEntry entry;

    public ArchiveEntryInputStream(ArchiveEntry entry, ArchiveInputStream inputStream) {
        this.inputStream = inputStream;
        targetSize = entry.getSize();
        this.entry = entry;
    }

    @Override
    public int read() throws IOException {
        if (targetSize == -1) return -1;
        readCnt++;
        return inputStream.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (atEnd()) return -1;
        long canRead = canReadSize();
        int read = inputStream.read(b, off, Math.toIntExact(len > canRead ? canRead : len));
        if (read > 0) readCnt += read;
        return read;
    }

    @Override
    public long skip(long n) throws IOException {
        long canRead = canReadSize();
        return super.skip(Math.min(n, canRead));
    }

    /**
     * 跳过当前实体的流长度
     * @return  跳过的字节数
     */
    public long skipThisEntry() throws IOException {
        return skip(getEntry().getSize());
    }

    @Override
    public int available() throws IOException {
        if (targetSize < 0 || readCnt == targetSize) return 0;
        int s = Math.toIntExact(targetSize - readCnt);
        return Math.min(inputStream.available(), s);
    }

    public long canReadSize() {
        if (atEnd()) return 0;
        return targetSize - readCnt;
    }

    public boolean atEnd() {
        return targetSize == -1 || targetSize == readCnt;
    }
}
