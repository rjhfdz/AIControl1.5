package com.boray.main.TreeUtil;

import com.boray.entity.FileOrFolder;
import resource.Resources;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //定义图标和要显示的字符串
    private static final ImageIcon root = (ImageIcon) Resources.getInstance().createIcon(Resources.getInstance().createImage("product_32.png"), 16, 16);
    private static final ImageIcon main = (ImageIcon) Resources.getInstance().createIcon(Resources.getInstance().createImage("prfs_32.png"), 16, 16);
    private static final ImageIcon company = (ImageIcon) Resources.getInstance().createIcon(Resources.getInstance().createImage("prfrs_32.png"), 16, 16);
    private static final ImageIcon file = (ImageIcon) Resources.getInstance().createIcon(Resources.getInstance().createImage("FileIcon.png"), 16, 16);

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object obj = node.getUserObject();
        if (node.isRoot()) {
            setIcon(root);
        } else if (obj != null && obj instanceof FileOrFolder) {
            FileOrFolder folder = (FileOrFolder) obj;
            if(folder.getXmtype()==1){
                setIcon(company);
            }else if(folder.getXmtype()==0){
                setIcon(main);
            }
            setText(folder.getXmname());
        }
        return this;
    }

    private static ImageIcon getIcon(String icon_name) {
        java.net.URL imageURL = Resources.class.getResource(icon_name);
        Image image = Toolkit.getDefaultToolkit().getImage(imageURL);
        ImageIcon imageIcon = new ImageIcon(image);
        return imageIcon;
    }
}
