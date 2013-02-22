package jukagaka.demo;

import javax.swing.JFrame;
import javax.swing.JDialog;

public class TestChildDialog
{
    public static void main(String[] args)
    {
        JFrame parent1 = new JFrame("parent1");
        JDialog child1 = new JDialog(parent1,"child1");

        child1.setVisible(true);
        parent1.setVisible(true);

        JDialog parent2 = new JDialog();
        JDialog child2 = new JDialog(parent2,"child2");

        child2.setVisible(true);
        parent2.setVisible(true);

        try
        {
            Thread.sleep(10000);
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }

        parent2.setVisible(false);
        parent1.setVisible(false);

        try
        {
            Thread.sleep(10000);
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }

        JDialog child3 = new JDialog(parent2,"child3");
        parent2.setVisible(true);

        return;
    }

    // 结论:
    // 1. 子窗体始终位于父窗体之上
    // 2. 父窗体在响应最小化/还原/显示/隐藏事件时 子窗体也响应
    // 3. 父窗体在 隐藏->显示 后 原来不可见的子窗体依然是不可见的 反之亦然
}
