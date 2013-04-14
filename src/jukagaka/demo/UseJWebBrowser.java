package jukagaka.demo;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;

import jukagaka.shell.renderer.SwingRenderer;
import jukagaka.shell.Ukagaka;
import jukagaka.UkaDaemon;

import javax.swing.SwingUtilities;
import java.lang.Runnable;

public class UseJWebBrowser
{
    public static void main(String[] args)
    {
        NativeInterface.open();
        // Here goes the rest of the program initialization
        NativeInterface.runEventPump();

        SwingRenderer renderer = new SwingRenderer();
        Ukagaka ukagaka = new Ukagaka();
        final JWebBrowser browser = new JWebBrowser();

        ukagaka.setRenderer(renderer);

        renderer.loadImage(
            UkaDaemon.getProgramDir()+"/res/shell/StubShell/StubXML.xml",
            "/root/image/img",
            SwingRenderer.DEFAULT_FILTER
        );
        ukagaka.getLayers().cacheImage = renderer.getImage("balloonbk");
        ukagaka.getLayers().cacheMask = renderer.getMask("balloonbk");
        ukagaka.setBounds(
            260,
            160,
            ukagaka.getLayers().cacheImage.getWidth(ukagaka),
            ukagaka.getLayers().cacheImage.getHeight(ukagaka)
        );
        ukagaka.add(browser) ;
        browser.setBarsVisible(false);
        browser.setBounds(0,0,150,100);
        System.out.println(browser.getBounds());
        System.out.println(browser.isVisible());
        System.out.println(ukagaka.getLayout());


        ukagaka.setVisible(true);

        SwingUtilities.invokeLater(
            new Runnable()
            {
                public void run()
                {
                    System.out.println(browser.getBrowserType());
                    browser.navigate("http://jukagaka.diandian.com/");
                }
            }
        );

        return;
    }
}
