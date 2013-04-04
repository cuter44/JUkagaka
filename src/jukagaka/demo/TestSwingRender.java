package jukagaka.demo;

/* jukagaka */
import jukagaka.shell.renderer.SwingRenderer;
import jukagaka.shell.renderer.SwingRenderer.PixelFilter;
import jukagaka.UkaDaemon;
/* swing */
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
/* swing */
import java.awt.Image;
import java.awt.Graphics;

import java.awt.Color;
/* ---- */

public class TestSwingRender
{
    private Image image = null;

    public void runTest()
    {
        JDialog dialog1 = new JDialog()
        {
            @Override
            public void paint(Graphics g)
            {
                super.paint(g);
                g.drawImage(TestSwingRender.this.image, 0, 0, this);
            }
        };
        JDialog dialog2 = new JDialog()
        {
            //@Override
            //public void paint(Graphics g)
            //{
                //super.paint(g);
                //g.drawImage(TestSwingRender.this.image, 0, 0, this);
            //}
        };
        dialog1.setLayout(null);
        dialog2.setLayout(null);
        dialog1.setBackground(Color.WHITE);
        dialog2.setBackground(Color.WHITE);
        dialog1.setSize(300,300);
        dialog2.setSize(300,300);
        dialog2.setLocation(300,0);
        //dialog1.setUndecorated(true);
        //dialog2.setUndecorated(true);
        dialog1.setVisible(true);
        dialog2.setVisible(true);

        JLabel label1 = new JLabel("label1");
        JPanel panel2 = new JPanel()
        {
            @Override
            public void paintComponent(Graphics g)
            {
                g.drawImage(TestSwingRender.this.image, 0, 0, this);
            }
        };
        JLabel label2 = new JLabel("label2");
        dialog1.add(label1);
        dialog2.add(panel2);
        panel2.add(label2);
        System.out.println(label1.isOpaque());
        System.out.println(panel2.isOpaque());
        System.out.println(label2.isOpaque());
        dialog1.setBounds(0, 0, 300, 300);
        dialog2.setBounds(300, 0, 300, 300);
        label1.setBounds(0, 0, 300, 300);
        panel2.setBounds(0, 0, 300, 300);
        label2.setBounds(0, 0, 300, 300);

        //JLabel label12 = new JLabel("blablablabla");
        //dialog1.add(label12);
        //label12.setBounds(20, 20, 100, 100);


        try
        {
            Thread.sleep(3000);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        label1.setText("label1++++++++++++++++++++++++++++++++++++++");
        label2.setText("label2++++++++++++++++++++++++++++++++++++++");
        //dialog1.repaint();
        //dialog2.repaint();
    }

    public void runTest2()
    {
        JDialog dialog3 = new JDialog();
        dialog3.setBounds(600, 0, 300, 300);
        dialog3.setUndecorated(true);
        dialog3.setVisible(true);

        JPanel panel3 = new JPanel()
        {
            @Override
            public void paintComponent(Graphics g)
            {
                TestSwingRender.this.render(g, this);
            }
        };

        dialog3.add(panel3);
        panel3.setBounds(0,0,300,300);

    }

    public void render(Graphics g, JPanel panel)
    {
        //Graphics g = panel.getGraphics();
        g.drawImage(this.image, 0, 0, panel);
        return;
    }

    public static void main(String[] args)
    {
        SwingRenderer renderer = new SwingRenderer();
        TestSwingRender testRender = new TestSwingRender();
        renderer.loadImage(
            UkaDaemon.getProgramDir()+"/res/shell/StubShell/StubXML.xml",
            "/root/image/img",
            new PixelFilter()
            {
                public boolean isTransparent(int pixel)
                {
                    if ((pixel & 0xff000000) == 0)
                        return(true);
                    else
                        return(false);
                }
            }
        );

        System.out.println(renderer.getImageContent());

        testRender.image = renderer.getImage("balloonbk");
        testRender.runTest();
        testRender.runTest2();


        return;
    }
}
// 结论:
// 1. 需要在一个 JPanel 上撰写绘制代码, 并将其作为子元件 才能够正确地绘制.
// 2. 关于用于绘画的 JPanel 与其兄弟组件的互相遮盖问题未试验.
// 3. 之前一直无法解决的绘制不能问题是因为 JDialog 继承了重量级控件的绘制机制(虽然它属于swing包)
//    轻量级组件在重量级组件之后绘制所以有可能因此而遮盖了背景.
