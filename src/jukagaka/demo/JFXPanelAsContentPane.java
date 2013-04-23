package jukagaka.demo;

import javax.swing.JDialog;

import java.awt.Graphics;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.scene.paint.Color;

public class JFXPanelAsContentPane
{
    public static void main(String[] args)
    {
        final JDialog dialog = new JDialog();
        final JFXPanel fxpanel = new JFXPanel()
        {
            public void paintComponent(Graphics g)
            {
                g.drawRect(64, 64, 64, 64);
            }
        };
        // ^ initalize

        Platform.runLater(
            new Runnable()
            {
                public void run()
                {
                    WebView webpanel = new WebView();

                    dialog.setContentPane(fxpanel);
                    fxpanel.setScene(new Scene(webpanel, Color.BLUE));

                    dialog.setVisible(true);

                    webpanel.getEngine().load("http://jukagaka.diandian.com/");
                }
            }
        );

        return;
    }
}

// 结论:
// 1. JFXPanel 通过 paintComponent 绘制它的子控件.
