package com.sophony.flow.commons.tree;


import java.util.List;
import java.util.function.Function;

/**
 * @author : yzm
 * @date : 2021-09-26 08:46
 * description:<p></p>
 **/
public abstract class AbsTree<T, R>{

    /**
     * 根据等级区分转换树
     * @param list
     * @return
     */
    abstract List<T> getTreeByLevel(List<T> list);

    /**
     * 找到自己的树
     * @param selfId
     * @param treeNode
     * @param <T>
     * @return
     */
    abstract  T getSelf(R selfId, T t);

    /**
     * 自定义树的扁平化操作
     * @param treeNode
     * @param func
     * @param <T>
     * @return
     */
    abstract List<T> mapToList(TreeNode treeNode,Function<TreeNode, List<T>> func);

    /**
     * 默认树的扁平化
     * @param treeNode
     * @param
     * @return
     */
    abstract  List<T> mapToList(T treeNode);

    /**
     * 找到根
     * @param selfId
     * @param t
     * @return
     */
    abstract T getParents(R selfId, T t);
}
