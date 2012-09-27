// This demo is to test load image and JWindow.

import javax.swing.JWindow;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.MediaTracker;


public class Demo3
{
    public static void main(String[] args)
    {
        JWindow testWindow = new JWindow();
        testWindow.setSize(320, 320);
        Image backImage = Toolkit.getDefaultToolkit().getImage("surface0000.png");
        MediaTracker mt = new MediaTracker(testWindow);
        mt.addImage(backImage, 0);
        try
        {
            mt.waitForAll();
        }
        catch (Exception ex)
        {
        }
        testWindow.setVisible(true);
        testWindow.getGraphics().drawImage(backImage,0,0,testWindow);

        return;
    }
}
