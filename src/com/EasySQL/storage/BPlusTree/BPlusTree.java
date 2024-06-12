package com.EasySQL.storage.BPlusTree;

import com.EasySQL.conf.EasySQLConfig;

import java.util.LinkedList;
import java.util.Queue;

/**
 * B+树 唯一索引
 * @param <T>
 */
public class BPlusTree<T extends Comparable<T>> {
    int degree; // 度数/阶数
    BPlusTreeNodePage<T> rootPage; // 根页面

    public BPlusTree() {
        this.degree = EasySQLConfig.BTREE_DEGREE;
        this.rootPage = new BPlusTreeNodePage<T>(this.degree, true);
    }

    public boolean insert(T key, Object value){
        return rootPage.treeInsert(key, value);
    }

    public BPlusTreeNodePage<T> searchPage(T key){
        return rootPage.treeSearchPage(key);
    }

    public Object searchValue(T key){
        return rootPage.treeSearchValue(key);
    }

    public void delete(T key){

    }


    /**
     * 广度优先遍历, 输出 Tree
     */
    public void out() {
        Queue<BPlusTreeNodePage<T>> queue = new LinkedList<>();
        queue.add(rootPage);

        while(!queue.isEmpty()){
            BPlusTreeNodePage<T> first = queue.poll();
            System.out.print(first);
            queue.addAll(first.getChildren());
        }

        System.out.println("");
    }
}
