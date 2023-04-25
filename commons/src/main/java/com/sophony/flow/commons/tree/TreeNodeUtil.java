package com.sophony.flow.commons.tree;


import com.google.common.collect.Lists;
import com.sophony.flow.commons.utils.CollectionUtils;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author : yzm
 * @date : 2021-09-26 08:58
 * description:<p></p>
 **/
public class TreeNodeUtil<T extends TreeNode, R> extends AbsTree<T, R>{

    @Override
    public List<T> getTreeByLevel(List<T> list) {
        if(list == null || list.size() == 0) return null;
        Map<Integer, List<T>> levelCollect = list.stream().collect(Collectors.groupingBy(it -> it.getLevel()));
        Optional<Integer> min = levelCollect.keySet().stream().min(Comparator.comparing(Function.identity()));
        List<T> roots = levelCollect.get(min.get());
        for(int i = 0; i < roots.size(); i++) {
            addChildren(levelCollect, roots.get(i));
        }
        return roots;
    }

    @Override
    public List<T> mapToList(TreeNode treeNode,Function<TreeNode, List<T>> func) {
        Objects.requireNonNull(func);
        Objects.requireNonNull(treeNode);
        return func.apply(treeNode);
    }



    @Override
    public List<T> mapToList(T treeNode) {
        Objects.requireNonNull(treeNode);
        List<T> childNodes = treeNode.getChildren();
        treeNode.setChildren(null);
        List<T> treeNodes = Lists.newArrayList((T)treeNode);
        while (CollectionUtils.isNotEmpty(childNodes)){
            List<T> children = Lists.newArrayList();
            for(int i = 0; i < childNodes.size(); i++){
                T node = childNodes.get(i);
                if(CollectionUtils.isNotEmpty(node.getChildren())){
                    children.addAll(node.getChildren());
                }
                node.setChildren(null);
                treeNodes.add(node);
            }
            childNodes = children;
        }
        return treeNodes;
    }


    //寻找根源，此方法未测试
    @Override
    T getParents(R selfId, T treeNode) {
        if(selfId == null || Objects.isNull(treeNode)) return null;
        if(Objects.equals(treeNode.getId(), selfId)) return treeNode;
        Map<R, T> trees = mapToList(treeNode).stream().collect(Collectors.toMap(k -> (R)k.getId(), v -> v));
        T temp = trees.get(selfId);
        if(Objects.isNull(temp)) return null;
        if(CollectionUtils.isNotEmpty(temp.getChildren())) temp.getChildren().clear();
        while(Objects.nonNull(temp.parentId) && Objects.nonNull(temp.getParentId()) && temp.getParentId().toString().length() != 0){
            T parent = trees.get(temp.getParentId());
            parent.getChildren().clear();
            parent.getChildren().add(temp);
            temp = parent;
        }
        return temp;
    }

    @Override
    public T getSelf(R selfId, T treeNode) {
        if(selfId == null || Objects.isNull(treeNode)) return null;
        if(Objects.equals(treeNode.getId(), selfId)) return treeNode;
        List<T> treeNodes = treeNode.getChildren();
        boolean flag = false;
        while (CollectionUtils.isNotEmpty(treeNodes)){
            List<T> childTreeNodes = new ArrayList<>();

            T temp;
            for(int i = 0; i < treeNodes.size(); i++){
                temp = treeNodes.get(i);
                if(Objects.equals(temp.getId(), selfId)){
                    treeNode = temp;
                    flag = true;
                    break;
                }
                List<T> children = temp.getChildren();
                if(CollectionUtils.isNotEmpty(children)){
                    childTreeNodes.addAll(children);
                }
            }
            treeNodes = childTreeNodes;
            if(flag) break;
        }
        return flag ? treeNode : null;
    }


    private void addChildren(Map<Integer, List<T>> levelCollect, T root){
        List<T> list = levelCollect.get(root.getLevel() + 1);
        if(CollectionUtils.isEmpty(list)) return;
        Map<R, List<T>> childrenMap = list.stream().collect(Collectors.groupingBy(it -> (R)it.getParentId()));
        List<T> selfChildren = childrenMap.get(root.getId());
        if(CollectionUtils.isEmpty(selfChildren)) return;
        root.setChildren(new ArrayList());
        for(int i = 0; i < selfChildren.size(); i++){
            T treeNode = selfChildren.get(i);
            root.getChildren().add(treeNode);
            addChildren(levelCollect, treeNode);
        }
    }

}
