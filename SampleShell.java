/**
 * @version v120826
 * @author "Galin"<cuter44@qq.com>
 */
package jukagaka.shell;

import jukagaka.*;

import java.util.Hashtable;
import java.awt.Image;
import java.awt.geom.Area;
import javax.swing.JWindow;

public class SampleShell extends JUkaShell
{

    public static final String DEFAULT_INI = JUkaUtility.getProgramPath() + "shell/SampleShell/SampleShell.ini";
    public static Hashtable<String, Image> hashImages = null;
    public static Hashtable<String, Area> hashMasks = null;

  // Install/Uninstall | ��װ/ж��
    /**
     * <p>��װ Shell �ķ���</p>
     * <p>��ʹ��֮ǰ, Ӧ���ô˷������� JUkagaka ��ע���Ա�������ʱ����ȡ<br>
     * �����߿����� TODO �����밲װǰ���û������.<br>
     * (!) �˷����ᱻ�ص�, ������ı䷽��ǩ��<br>
     * (*) �����Զ��巵��ֵ &lt;0=ʧ��, &gt;0=�ɹ�</p>
     * @return <ul>����ֵ<li>0=�ɹ�<li>1=������Ѿ���װ<li>-1=(δ�������)
     * <li>-2=����ջ����<li>-3=��д����
     */
    public static int installComponent()
    {
        // TODO �ڴ˴����밲װǰ�Ĵ���
        // ���� EULA, ʹ��˵��, ȷ�϶Ի���ʲô��.
        return(JUkaShellCtrl.installComponent());
    }

    /**
     * <p>ж�� Shell �ķ���</p>
     * <p>��ɾ��֮ǰ, Ӧ���ô˷������� JUkagaka ��ע���Ա�������ʱ���ٱ���ȡ<br>
     * �����߿����� TODO ������ж��ǰ���û������.<br>
     * (!) �˷����ᱻ�ص�, ������ı䷽��ǩ��<br>
     * (*) �����Զ��巵��ֵ &lt;0=ʧ��, &gt;0=�ɹ�</p>
     * @return <ul>����ֵ<li>0=�ɹ�<li>1=����������б���<li>-1=(δ�������)
     * <li>-2=����ջ����<li>-3=��д����
     */
    public static int uninstallComponent()
    {
        // TODO �ڴ˴�����ж��ǰ�Ĵ���
        // ����ȷ�϶Ի���, �����û�����, ֪ͨ�������ʲô��.
        return(JUkaShellCtrl.uninstallComponent());
    }

  // Start-up | ������
    /**
     * �˷����� JUkagaka ����ʱ����������ɳ�ʼ��
     */
    public static void onLoad()
    {
        System.out.println("onLoad() was invoked successfully");
        return;
    }

  // Other | ����
    /**
     * @deprecated �˷���Ŀǰ�����ڵ���
     */
    public static void main(String[] args)
    {
        // ���ڲ��� Shell װ/ж�� �� ͼ�����ʱ��Ĵ���.
        System.out.println(SampleShell.installComponent());
        try
        {
            Class classOfSampleShell = Class.forName("jukagaka.shell.SampleShell");
            JUkaShellCtrl.prefetchImageResource(classOfSampleShell);
        }
        catch (Exception ex)
        {
            System.err.println(ex);
            return;
        }
        System.out.println(SampleShell.hashImages.size());
        System.out.println(SampleShell.hashMasks.size());
        System.out.println(SampleShell.uninstallComponent());

        // ���ڲ���ͼ���Ƿ���صĴ���
        JWindow tmpJDialog = new JWindow();
        tmpJDialog.setSize(320, 320);
        tmpJDialog.setVisible(true);
        tmpJDialog.getGraphics().drawImage(SampleShell.hashImages.get("shell/SampleShell/surface0000.png"),
                                           0, 0,
                                           JUkaStage.eventListener);

        return;
    }
}
