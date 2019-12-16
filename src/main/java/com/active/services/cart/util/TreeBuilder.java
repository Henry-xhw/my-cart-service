package com.active.services.cart.util;

import com.active.services.cart.domain.BaseTree;

import java.util.ArrayList;
import java.util.List;

public class TreeBuilder<T extends BaseTree> {

    private List<T> baseTreeList = new ArrayList<>();

    public TreeBuilder(List baseTreeList) {
        super();
        this.baseTreeList = baseTreeList;
    }

    /**
     *
     * build tree structure
     *
     * @return
     */

    public List<T> buildTree() {
        List<T> treeBaseTrees = new ArrayList<>();
        List<T> rootBaseTrees = getRootBaseTrees();
        for (T rootBaseTree : rootBaseTrees) {
            buildChildBaseTrees(rootBaseTree);
            treeBaseTrees.add(rootBaseTree);
        }
        return treeBaseTrees;
    }

    /**
     *
     * recursion tree
     *
     * @param baseTree
     */

    public void buildChildBaseTrees(BaseTree baseTree) {
        List<BaseTree> children = getChildBaseTrees(baseTree);
        if (!children.isEmpty()) {
            for (BaseTree child : children) {
                buildChildBaseTrees(child);
            }
            baseTree.setSubItems(children);
        }

    }

    /**
     *
     * get all children
     *
     * @param baseTree
     *
     * @return
     */

    public List<BaseTree> getChildBaseTrees(BaseTree baseTree) {
        List<BaseTree> childBaseTrees = new ArrayList<>();
        for (BaseTree n : baseTreeList) {
            if (baseTree.getId().equals(n.getPid())) {
                childBaseTrees.add(n);
            }
        }
        return childBaseTrees;
    }

    /**
     *
     * check the node is the root node
     *
     * @param baseTree
     *
     * @return
     */

    public boolean rootBaseTree(BaseTree baseTree) {
        boolean isRootBaseTree = true;
        for (BaseTree n : baseTreeList) {
            if (baseTree.getPid().equals(n.getId())) {
                isRootBaseTree = false;
                break;
            }
        }
        return isRootBaseTree;
    }

    /**
     *
     * get all root nodes
     *
     * @return
     */

    public List<T> getRootBaseTrees() {
        List<T> rootBaseTrees = new ArrayList<>();
        for (T n : baseTreeList) {
            if (rootBaseTree(n)) {
                rootBaseTrees.add(n);
            }
        }
        return rootBaseTrees;
    }
}
