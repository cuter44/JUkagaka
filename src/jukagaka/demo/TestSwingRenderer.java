package jukagaka.demo;

import jukagaka.UkaDaemon;
import jukagaka.shell.UkaWindow;
import jukagaka.shell.renderer.SwingRenderer;

public class TestSwingRenderer
{
    public static void main(String[] args)
    {
        // To enable using this statment, UkaWindow.UkaWindow() must be changed to public.
        UkaWindow window = new UkaWindow();
        SwingRenderer renderer = new SwingRenderer();

        renderer.loadImage(
            UkaDaemon.getProgramDir()+"/res/shell/StubShell/StubXML.xml",
            "/root/image/img"
        );

        window.setContentPane(renderer);
        window.setRenderData("cyau_st0");

        System.out.println(renderer.getImage("cyau_st0"));

        // Currently window must be manually set to at least 1x1 size to ensure paintCompnent() will be invoked.
        // A 0x0(default value) size window never need repaint so paintCompnent() will not be called.
        window.setSize(1,1);
        window.setVisible(true);

        return;
    }
}
