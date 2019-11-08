package com.boray.main.Util;

import javax.swing.tree.DefaultMutableTreeNode;

public class CustomTreeNode extends DefaultMutableTreeNode {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    int level = 0;
    boolean expanded = false;

    public CustomTreeNode(){

    }

    public CustomTreeNode(Object userObject){
        super(userObject);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
