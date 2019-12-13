package com.active.services.cart.util;

import com.active.services.cart.model.v1.BaseTree;

import java.util.ArrayList;
import java.util.List;

public class TreeBuilder {

    private List<? extends BaseTree> baseTreeList = new ArrayList<BaseTree>();

    public TreeBuilder(List<? extends BaseTree> baseTreeList) {
        super();
        this.baseTreeList = baseTreeList;
    }

    /**
     *
     * build tree structure
     *
     * @return
     */

    public List<BaseTree> buildTree() {
        List<BaseTree> treeBaseTrees = new ArrayList<BaseTree>();
        List<BaseTree> rootBaseTrees = getRootBaseTrees();
        for (BaseTree rootBaseTree : rootBaseTrees) {
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
            baseTree.setChildren(children);
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
        List<BaseTree> childBaseTrees = new ArrayList<BaseTree>();
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

    public List<BaseTree> getRootBaseTrees() {
        List<BaseTree> rootBaseTrees = new ArrayList<BaseTree>();
        for (BaseTree n : baseTreeList) {
            if (rootBaseTree(n)) {
                rootBaseTrees.add(n);
            }
        }
        return rootBaseTrees;
    }
}
