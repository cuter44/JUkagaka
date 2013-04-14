package jukagaka.demo;

import jukagaka.shell.Ukagaka;
import jukagaka.shell.renderer.SwingRenderer;
import jukagaka.UkaDaemon;

public class TestRenderer
{
    public static void main(String[] args)
    {
        SwingRenderer renderer = new SwingRenderer();
        Ukagaka ukagaka = new Ukagaka();
        ukagaka.setRenderer(renderer);

        renderer.loadImage(
            UkaDaemon.getProgramDir()+"/res/shell/StubShell/StubXML.xml",
            "/root/image/img",
            SwingRenderer.DEFAULT_FILTER
        );
        System.out.println(ukagaka.getLayers().cacheImage = renderer.getImage("cyau_st0"));
        System.out.println(ukagaka.getLayers().cacheMask = renderer.getMask("cyau_st0"));

        ukagaka.fireLayerChange();
        //firePropertyChange("layer", 0, 1);
        System.out.println(ukagaka.getLayers().cacheImage.getWidth(ukagaka));
        System.out.println(ukagaka.getLayers().cacheImage.getHeight(ukagaka));
        ukagaka.setBounds(
            320,
            240,
            //120,
            //93
            ukagaka.getLayers().cacheImage.getWidth(ukagaka),
            ukagaka.getLayers().cacheImage.getHeight(ukagaka)
        );
        System.out.println(ukagaka.getContentPane().toString());
        ukagaka.setVisible(true);
        System.out.println("Shown");
        System.out.println(ukagaka.getLayers().cacheMask.getBounds());
        System.out.println(ukagaka.getLayers().cacheImage.getWidth(ukagaka));
        System.out.println(ukagaka.getLayers().cacheImage.getHeight(ukagaka));
        ukagaka.repaint();
    }
}

/*
 * 结论
 * firePropertyChange() 的后两个参数必须不同才能触发事件响应
 */