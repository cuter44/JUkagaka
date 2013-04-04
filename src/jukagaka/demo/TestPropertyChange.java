package jukagaka.demo;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JDialog;


public class TestPropertyChange
{
    public static PropertyChangeListener l = null;

    public static void main(String[] args)
    {
        JDialog dialog = new JDialog();

        dialog.addPropertyChangeListener(
            "layer",
            l = new PropertyChangeListener()
            {
                public void propertyChange(PropertyChangeEvent ev)
                {
                    System.out.println("layer property-change-listener activated.");

                    return;
                }

            }
        );

        dialog.firePropertyChange("layer",0,1);
        dialog.removePropertyChangeListener(l);
        dialog.firePropertyChange("layer",0,1);
        dialog.removePropertyChangeListener("layer",l);
        dialog.firePropertyChange("layer",0,1);

        return;
    }
}

/*
 * 结论:
 *
 * 1. 可以使用自定义字符串定义新的Property, 可以成功发效.
 * 2. 对于绑定到指定属性的PropertyChangeListener只能用同样的字符串解绑.
 */
