package com.EasySQL.storage.BPlusTree;

import lombok.Data;
import lombok.ToString;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据页面为一组Node的集合
 *  T: 索引(键) 类型
 *  非叶子节点里存的值是子节点里的最大值
 *  这里实现唯一索引 即 key 唯一
 */
@Data
@ToString(exclude = "parent")
public class BPlusTreeNodePage<T extends Comparable<T>> implements Serializable{
    int degree; // 阶数/度数
    List<T> keys; // 键数组
    boolean leaf; // 是否为叶子页面
    List<BPlusTreeNodePage<T>> children; // 子页面的数组
    List<Object> data; // 数据, 为叶子时才有
    BPlusTreeNodePage<T> parent; // 父节点


    public BPlusTreeNodePage(int degree, boolean leaf) {
        this.degree = degree;
        this.keys = new ArrayList<>();
        this.leaf = leaf;
        this.children = new ArrayList<>();
        this.data = new ArrayList<>();
        this.parent = null;
    }

    /**
     * 对象转成 byte[] 便于持久化的序列化方法
     */
    public byte[] serialize() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(this);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  反序列化, byte[] 构造对象
     *  serialize() 的反函数
     */
    public static <T extends Comparable<T>> BPlusTreeNodePage<T> deserialize(byte[] data) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (BPlusTreeNodePage<T>)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查找第一个 >= key 的元素的位置
     * @return 第一个 >= key 的元素的位置
     */
    private int findFirstLEIndex(T key) {
        int low = 0;
        int high = keys.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            T midKey = keys.get(mid);

            int cmp = key.compareTo(midKey);
            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return low;
    }

    /**
     * 查找当前是否包含 key
     * @param key key
     */
    public boolean containKey(T key) {
        if (keys == null || keys.isEmpty()) {
            return false;
        }

        int left = 0;
        int right = keys.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            T midValue = keys.get(mid);

            int compareResult = midValue.compareTo(key);
            if (compareResult == 0) {
                return true;
            } else if (compareResult < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return false;
    }

    /**
     * 当前数据页插入数据, 保证 key 有序
     * @param key 索引
     * @param value 值
     * @return 插入成功时为 true
     */
    public boolean addData(T key, Object value) {
        int insertIndex = findFirstLEIndex(key);
        if(insertIndex < keys.size() && key.compareTo(keys.get(insertIndex)) == 0){
            return false;
        }
        keys.add(insertIndex, key);
        data.add(insertIndex, value);
        return true;
    }

    /**
     * B+ 树里查找数据页
     * @param key 索引
     */
    public BPlusTreeNodePage<T> treeSearchPage(T key) {
        // 找到第一个大于或等于key的位置
        int leI = findFirstLEIndex(key);

        if(leI >= keys.size()){
            // 表示当前页面最大的节点索引都没有 key 大, 即要找的元素不存在
            return null;
        }

        // 判断是不是叶子页
        if(leaf){
            if(key.compareTo(keys.get(leI)) == 0){
                return this;
            } else {
                return null;
            }
        }

        // 表示 key 在当前页面第 leI 个节点的范围里, 继续去子页面里搜索,
        return children.get(leI).treeSearchPage(key);
    }

    /**
     * B+ 树里查找数据
     * @param key 索引
     */
    public Object treeSearchValue(T key) {
        // 找到第一个大于或等于key的位置
        int leI = findFirstLEIndex(key);

        if(leI > keys.size()){
            // 表示当前页面最大的节点索引都没有 key 大, 即要找的元素不存在
            return null;
        }

        // 判断是不是叶子页
        if(leaf){
            if(key.compareTo(keys.get(leI)) == 0){
                return this.data.get(leI);
            } else {
                return null;
            }
        }

        // 表示 key 在当前页面第 leI 个节点的范围里, 继续去子页面里搜索,
        return children.get(leI).treeSearchPage(key);
    }

    /**
     * B+ 树里插入数据
     * @param key 索引
     * @param value 值
     * @return 插入成功返回 true
     */
    public boolean treeInsert(T key, Object value) {
        // 1) 如果当前节点是根节点，并且插入后节点关键字数目小于等于m，则算法结束；
        if(leaf && keys.size() < degree){
            return addData(key, value);
        }
        // 2) 如果当前节点是非根节点， 并且插入后节点关键字数目小于等于m，
        if(!leaf && keys.size() < degree){
            // 则判断若a是新索引值时转步骤4)后结束，若a不是新索引值，则直接结束；
            int le = findFirstLEIndex(key);
            if(key.compareTo(keys.get(le)) == 0){
                return false;
            }
            // TODO
        }
        //3) 如果插入后关键字数目大于m，则先分裂成两个节点X和Y，并且他们各自所含的关键字个数分别为： u=⌈(m+1)/2⌉, v=⌊(m+1)/2⌋。由于索引值位于节点的最左端或最右端，不妨假设索引值位于节点的最右端（即非终端节点含有其子树中的最大关键字）， 有如下操作：
        //如果当前分裂成的X节点和Y节点原来所属的节点是根节点，则从X节点和Y节点中取出索引关键字，将这两个关键字组成新的根节点，并且这个根节点指向X和Y，算法结束；
        //如果当前分裂成的X节点和Y节点原来所属的节点是非根节点，依据假设条件判断，如果a成为Y节点的新索引值，则转步骤4)得到Y双亲节点P；如果a不是Y节点的新索引值，则求出X节点和Y节点的双亲节点P, 然后提取X节点的新索引值a'，在P中插入关键字a'，继续进行插入算法；
        return false;
    }
}
