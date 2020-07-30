package com.boray.main.Util;

import com.boray.entity.*;
import resource.Resources;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.io.File;

public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //定义图标和要显示的字符串
    private static final ImageIcon root = (ImageIcon) Resources.getInstance().createIcon(Resources.getInstance().createImage("product_32.png"), 16, 16);
    private static final ImageIcon main = (ImageIcon) Resources.getInstance().createIcon(Resources.getInstance().createImage("prfs_32.png"), 16, 16);
    private static final ImageIcon company = (ImageIcon) Resources.getInstance().createIcon(Resources.getInstance().createImage("prfrs_32.png"), 16, 16);
    private static final ImageIcon local = (ImageIcon) Resources.getInstance().createIcon(Resources.getInstance().createImage("MidType.png"), 16, 16);
    private static final ImageIcon file = (ImageIcon) Resources.getInstance().createIcon(Resources.getInstance().createImage("FileIcon.png"), 16, 16);
    private static final ImageIcon member = (ImageIcon) Resources.getInstance().createIcon(Resources.getInstance().createImage("Group.png"), 16, 16);

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object obj = node.getUserObject();
        if (node.isRoot()) {
            setIcon(root);
            if(obj != null && obj instanceof File){
                File obj1 = (File) obj;
                String[] strings = obj1.getAbsolutePath().split("\\\\");
                setText(strings[strings.length - 1]);
            }
        } else if (obj != null && obj instanceof FileOrFolder) {
            FileOrFolder folder = (FileOrFolder) obj;
            setIcon(company);
            setText(folder.getXmname());
        } else if (obj != null && obj instanceof ProjectFile) {
            ProjectFile folder = (ProjectFile) obj;
            setIcon(file);
            setText(folder.getGcname());
        } else if (obj != null && obj instanceof File) {
            File obj1 = (File) obj;
            if (obj1.isDirectory()) {
                setIcon(local);
            } else if (obj1.isFile()) {
                setIcon(file);
            }
            String[] strings = obj1.getAbsolutePath().split("\\\\");
            setText(strings[strings.length - 1]);
        } else if (obj != null && obj instanceof Member) {
            Member m = (Member) obj;
            setIcon(member);
            setText(m.getUsername());
        }else if (obj!=null && obj instanceof Offerentity) {
        	Offerentity o =(Offerentity) obj;
        	setIcon(member);
        	setText(o.getOfficename());
        }else if(obj!=null && obj instanceof SuCaiFile) {
        	SuCaiFile s = (SuCaiFile)obj;
        	setIcon(file);
        	setText(s.getFilename());
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
