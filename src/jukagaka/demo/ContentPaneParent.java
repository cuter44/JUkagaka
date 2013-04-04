package jukagaka.demo;

import javax.swing.JDialog;

public class ContentPaneParent
{
    public static void main(String[] args)
    {
        JDialog dialog = new JDialog();
        System.out.println(dialog.getContentPane());
        System.out.println(dialog.getContentPane().getParent());
        System.out.println(dialog.getContentPane().getParent().getParent());
        System.out.println(dialog.getContentPane().getParent().getParent().getParent());

        return;
    }
}
