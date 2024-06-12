package com.EasySQL.storage.BPlusTree;

import com.EasySQL.conf.EasySQLConfig;

import java.io.*;

/**
 * 分页管理器
 */
public class PageManager {
    private RandomAccessFile file;
    private int pageSize;

    public PageManager(String filePath) throws IOException {
        this.file = new RandomAccessFile(filePath, "rw");
        this.pageSize = EasySQLConfig.PAGE_SIZE;
    }

    public byte[] readPage(long pageOffset) throws IOException {
        byte[] data = new byte[pageSize];
        file.seek(pageOffset * pageSize);
        file.readFully(data);
        return data;
    }

    public void writePage(byte[] data, long pageOffset) throws IOException {
        file.seek(pageOffset * pageSize);
        file.write(data);
    }

    public void close() throws IOException {
        file.close();
    }
}
