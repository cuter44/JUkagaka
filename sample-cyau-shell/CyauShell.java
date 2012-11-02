/**
 * @version v120826
 * @author "Galin"<cuter44@qq.com>
 */
package jukagaka.shell.cyaushell;

import jukagaka.*;
import jukagaka.shell.*;

import java.util.Hashtable;
import java.awt.Image;
import java.awt.geom.Area;

public class CyauShell extends JUkaShell
{

    public static final String DEFAULT_INI = JUkaUtility.getProgramDir() + "/shell/cyaushell/SampleShell.ini";
    public static Hashtable<String, Image> hashImages = null;
    public static Hashtable<String, Area> hashMasks = null;

  // Install/Uninstall | ��װ/ж��
    /**
     * <p>(ģ��)��װ Shell</p>
     * <p>Shell �ڿ��Ա�ʹ��֮ǰ, ������ JUkaStage ֪ͨ�����<br>
     * (!) �������չ��Ӧ<b>����</b>����������� TODO �������׫�Ĵ���,
     *     JUkaShell.java ��ԭ�еĲ��ֲ��ܱ�ɾ��. ������ܵ�������ʧ��.<br>
     * </p>
     * @return <ul>����ֵ, ���ڵ���0����ʾִ�гɹ�, ��֮��ʾʧ��
     *  <li>0=����
     *  <li>1=������Ѱ�װ
     *  <li>-1=(δ�������)
     *  <li>-2=����ջ����
     *  <li>-3=��д����
     * </ul>
     */
    public static int install()
    {
        // TODO �ڴ˴����밲װǰ�Ĵ���
        // ���� �������/��ͻ��ϵ, EULA, ʹ��˵��, ȷ�϶Ի���, �����û����� �ȵ�.
        return(JUkaShellCtrl.installComponent());
    }

    /**
     * <p>(ģ��)ж�� Shell</p>
     * <p>Ҫ���ټ���ĳ�� Shell, ������ JUkaStage ֪ͨע��<br>
     * (!) �������չ��Ӧ<b>����</b>����������� TODO �������׫�Ĵ���,
     *     JUkaShell.java ��ԭ�еĲ��ֲ��ܱ�ɾ��. ������ܵ�������ʧ��.<br>
     * </p>
     * @return <ul>����ֵ, ���ڵ���0����ʾִ�гɹ�, ��֮��ʾʧ��
     *  <li>0=����
     *  <li>1=����������б���
     *  <li>-1=(δ�������)
     *  <li>-2=����ջ����
     *  <li>-3=��д����
     * </ul>
     */
    public static int uninstall()
    {
        // TODO �ڴ˴�����ж��ǰ�Ĵ���
        // ���� ȷ�϶Ի���, �����û�����, ����ж�صȵ�.
        return(JUkaShellCtrl.uninstallComponent());
    }

  // Trigger | ������
    /**
     * �˷����� JUkagaka ����ʱ����������ɳ�ʼ��
     */
    public static void onLoad()
    {
        JUkaShell.onLoad();

        // ����ͼ����Դ
        try
        {
            Class classOfSampleShell = Class.forName("jukagaka.shell.cyaushell.CyauShell");
            JUkaShell.prefetchImageResource(classOfSampleShell, CyauShell.DEFAULT_INI, "images", true);
        }
        catch (Exception ex)
        {
            System.err.println(ex);
            return;
        }

        //System.out.println("onLoad() was invoked successfully");
        return;
    }

  // Shell Constructor & Destructor | Shell ����/����
    public static CyauShell createShell()
    {
        CyauShell newShell = new CyauShell();

        newShell.initalize(
            CyauShell.DEFAULT_INI,
            CyauShell.hashImages,
            CyauShell.hashMasks
        );

        return(newShell);
    }

    /**
     * <p>Ĭ�ϵĹ��췽��...ʲôҲ����</p>
     */
    public CyauShell()
    {
    }

  // Balloon | ����
    //@Override
    // �˷�������Override
    public BalloonWin createBalloon()
    {
        return(super.createBalloon(CyauShell.DEFAULT_INI, CyauShell.hashImages, CyauShell.hashMasks));
    }

  // Other | ����
    /**
     * @deprecated �˷���Ŀǰ�����ڵ���
     */
    public static void main(String[] args)
    {
        // ���ڲ��� Shell װ/ж�� �� ͼ�����ʱ��Ĵ���.
        System.out.println(CyauShell.install());

        CyauShell.onLoad();

        // ���ڲ��Դ��� Shell / Ukagaka / Balloon
        CyauShell testShell = CyauShell.createShell();
        testShell.setWinVisible(0, true);
        testShell.fireClip(0);
        testShell.setWinVisible(1, true);
        testShell.fireClip(1);

        //System.out.println(SampleShell.hashImages.size());
        //System.out.println(SampleShell.hashMasks.size());
        //System.out.println(SampleShell.uninstallComponent());

        //// ���ڲ���ͼ���Ƿ���صĴ���
        //JWindow tmpJDialog = new JWindow();
        //tmpJDialog.setSize(320, 320);
        //tmpJDialog.setVisible(true);
        //tmpJDialog.getGraphics().drawImage(SampleShell.hashImages.get("shell/SampleShell/surface0000.png"),
            //0, 0,
            //JUkaStage.eventListener
        //);

        // ���ڲ��� UkagagkaWin �ܷ���ȷ����ķ���
        //UkagakaWin testUkaWin = UkagakaWin.createUkagaka(SampleShell.DEFAULT_INI, hashImages, hashMasks);
        //testUkaWin.setVisible(true);
        ////testUkaWin.repaint();
        //testUkaWin.clip();
        //testUkaWin.setLocation(1250, 625);

        //BalloonWin testBallWin = BalloonWin.createBalloon(SampleShell.DEFAULT_INI, hashImages, hashMasks);
        //testBallWin.setVisible(true);
        //testBallWin.repaint();
        //testBallWin.clip();

        //testBallWin = BalloonWin.createBalloon(SampleShell.DEFAULT_INI, hashImages, hashMasks);
        //testBallWin.setSize(140, 100);
        //testBallWin.setVisible(true);
        //testBallWin.repaint();
        //testBallWin.clip();


        return;
    }
}
