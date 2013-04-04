package jukagaka.demo;

/* jukagaka */
import jukagaka.shell.renderer.SwingRenderer;
import jukagaka.UkaDaemon;
/* swing */
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
/* awt */
import java.awt.Graphics;
import java.awt.Image;

public class DrawContentPane
{
    private static Image img = null;
    private static JDialog dialog = null;

    public static void main(String[] args)
    {
        dialog = new JDialog();
        SwingRenderer imgBase = new SwingRenderer();
        imgBase.loadImage(
            UkaDaemon.getProgramDir()+"/res/shell/StubShell/StubXML.xml",
            "/root/image/img"
        );
        img = imgBase.getImage("cyau_st0");

        dialog.setContentPane(
            new JPanel()
            {
                public void paintComponent(Graphics g)
                {
                    //super.paint(g);
                    g.drawImage(DrawContentPane.img, 0, 0, DrawContentPane.dialog);

                    return;
                }
            }
        );

        dialog.setVisible(true);
        dialog.setSize(320, 320);
        dialog.add(new JButton("button"));

        return;
    }
}
