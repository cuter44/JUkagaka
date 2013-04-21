package jukagaka.demo;

import javax.swing.JDialog;
import javax.swing.JPanel;

import java.awt.event.HierarchyListener;
import java.awt.event.HierarchyEvent;

public class TestHierarchyChange
{
    public static void main(String[] args)
    {
        HierarchySupportPanel panel = new HierarchySupportPanel();
        panel.addHierarchyListener(panel);

        JDialog dialog = new JDialog();
        dialog.setContentPane(panel);

        return;
    }
}

class HierarchySupportPanel extends JPanel implements HierarchyListener
{
    @Override
    public void hierarchyChanged(HierarchyEvent ev)
    {
        System.out.println(ev);
    }
}

// 结论:
// 可以用以上方法监听组件层次变化