package com.sophony.flow.commons.tree;

import java.io.Serializable;
import java.util.List;

/**
 * @author : yzm
 * @date : 2021-09-26 08:50
 * description:<p></p>
 * version: 1.0
 **/
public class TreeNode<T extends TreeNode, R>  implements Serializable {

    R id;
    Integer level;
    String name;
    R parentId;
    List<T> children;

    public R getId() {
        return id;
    }

    public void setId(R id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public R getParentId() {
        return parentId;
    }

    public void setParentId(R parentId) {
        this.parentId = parentId;
    }



    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }


}
