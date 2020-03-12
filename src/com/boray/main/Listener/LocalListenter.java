package com.boray.main.Listener;

import com.boray.Data.Data;
import com.boray.Utils.Util;
import com.boray.entity.FileOrFolder;
import com.boray.entity.Users;
import com.boray.main.Util.CustomTreeCellRenderer;
import com.boray.main.Util.CustomTreeNode;
import com.boray.main.Util.TreeUtil;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class LocalListenter implements ActionListener {

    private JTree tree;

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        tree = (JTree) MainUi.map.get("LocalTree");
        if (button.getText().equals("刷新")) {
            refresh();
        }
    }

    /**
     * 刷新
     */
    private void refresh() {
        tree.removeAll();
        File file = new File("C://Boray");
        if (!file.exists()) {//判断该文件夹是否存在 并创建
            file.mkdirs();
        }
        DefaultMutableTreeNode root = Util.traverseFolder(file);
        DefaultTreeModel model = new DefaultTreeModel(root);
        tree.setModel(model);
        tree.setCellRenderer(new CustomTreeCellRenderer());
        //默认展开全部节点
        TreeUtil util = new TreeUtil();
        util.expandAll(tree, new TreePath(root), true);
    }
}
