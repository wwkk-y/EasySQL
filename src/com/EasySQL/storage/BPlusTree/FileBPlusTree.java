package com.EasySQL.storage.BPlusTree;

import com.EasySQL.conf.EasySQLConfig;

import java.io.IOException;

public class FileBPlusTree {
    private BPlusTree btree;
    private PageManager pageManager;
    private int pageSize;

    public FileBPlusTree(String databaseName, String tableName) throws IOException {
        this.btree = new BPlusTree();
        this.pageManager = new PageManager(String.format("%s/%s/%s", EasySQLConfig.DAT_PATH, databaseName, tableName));
        this.pageSize = pageSize;
    }

    public void insert(Object key, Object data) throws IOException {
//        btree.treeInsert(key, data);
        // 处理分页和文件存储
    }

    public void delete(int key) throws IOException {
//        btree.delete(key);
        // 处理分页和文件存储
    }

    public BPlusTreeNodePage search(int key) throws IOException {
//        return btree.treeSearchPage(key);
        // 处理分页和文件读取
        return null;
    }

    public void close() throws IOException {
        pageManager.close();
    }
}

