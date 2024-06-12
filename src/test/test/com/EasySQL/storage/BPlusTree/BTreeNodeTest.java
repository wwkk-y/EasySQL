package com.EasySQL.storage.BPlusTree;

import com.EasySQL.conf.EasySQLConfig;

public class BTreeNodeTest {
    public static void main(String[] args) {
        BPlusTreeNodePage<String> bPlusTreeNodePage = new BPlusTreeNodePage<>(EasySQLConfig.BTREE_DEGREE, true);
        bPlusTreeNodePage.getKeys().add("12132");
        byte[] serialize = bPlusTreeNodePage.serialize();
        BPlusTreeNodePage<Integer> bPlusTreeNodePage2 = BPlusTreeNodePage.deserialize(serialize);
        System.out.println(bPlusTreeNodePage2);
    }
}
