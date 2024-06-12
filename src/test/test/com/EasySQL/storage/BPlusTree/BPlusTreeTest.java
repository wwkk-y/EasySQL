package com.EasySQL.storage.BPlusTree;


import com.EasySQL.util.RandomGenerator;

import java.util.ArrayList;

public class BPlusTreeTest {
    public static void hr(int length) {
        for (int i = 0; i < length; i++) {
            System.out.print('-');
        }
        System.out.println("");
    }

    public static void hr() {
        hr(50);
    }

    public static void main(String[] args) {
        BPlusTree<Integer> bPlusTree = new BPlusTree<>();
        ArrayList<Integer> keys = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Integer key = RandomGenerator.generateRandomNumber(0, 100);
            String value = RandomGenerator.generateRandomString(10);
            keys.add(key);
            values.add(value);
            bPlusTree.insert(key, value);
        }
        System.out.println("keys: " + keys);
        System.out.println("values: " + values);
        hr();
        System.out.println("bPlusTree: ");
        bPlusTree.out();
        hr();
        Integer searchKey = keys.get(RandomGenerator.generateRandomNumber(0, keys.size() - 1));
        System.out.println("searchKey: " + searchKey);
        System.out.println(bPlusTree.searchPage(searchKey));
        System.out.println(bPlusTree.searchValue(searchKey));
        hr();

    }
}
