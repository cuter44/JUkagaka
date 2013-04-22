package jukagaka.demo;

import jukagaka.shell.UkaWindow;
import jukagaka.shell.renderer.WebViewRenderer;

public class TestWebViewRenderer
{
    public static void main(String[] args)
    {
        UkaWindow window = new UkaWindow();
        window.setSize(320, 240);
        WebViewRenderer renderer = new WebViewRenderer();

        window.setContentPane(renderer);

        window.setVisible(true);

        window.setRenderData("file://localhost/F:/doc/ref/Java/docs.oracle.com/javafx/2/api/index.html");

        return;
    }
}
