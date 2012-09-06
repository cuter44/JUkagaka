/**
 * @version v120826
 * @author "Galin"<cuter44@qq.com>
 */
package jukagaka;

import jukagaka.shell.*;
import jukagaka.ghost.*;
import jukagaka.plugin.*;

import java.util.ArrayList;
import java.lang.reflect.Method;
import java.awt.TrayIcon;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.Toolkit;
import java.awt.MediaTracker;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.PopupMenu;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class JUkaStage implements ActionListener,ImageObserver
{
  // Ststic Data | ��̬������
    /**
     * <p>��������Ǽ��Ѽ��ص�</p>
     */
    private static ArrayList<Class> registeredShell, registeredGhost, registeredPlugin;
    //private static ArrayList<JUkaComponent> activeShell, activeGhost, activePlugin;
    /**
     * ���������¼���������ͼ������
     */
    private static TrayIcon stageTray = null;
    /**
     * ���������¼ JUkaStage ����Ϣ������
     */
    public static JUkaStage eventListener = new JUkaStage();

  // Tray | ����
    /**
     * �˷������ڻ�ȡϵͳ����ͼ�������
     */
    public static TrayIcon getStageTray()
    {
        return(JUkaStage.stageTray);
    }
    /**
     * �˷������ڳ�ʼ��ϵͳ����ͼ��
     */
    private static boolean initTrayIcon()
    {
        try
        {
            Image imgTray = Toolkit.getDefaultToolkit().getImage(JUkaUtility.getProgramPath() + "TrayIcon.jpg");
            MediaTracker tmpMediaTracker = new MediaTracker(new JLabel());
            tmpMediaTracker.addImage(imgTray, 0);
            tmpMediaTracker.waitForAll();
            SystemTray sysTray = SystemTray.getSystemTray();
            JUkaStage.stageTray = new TrayIcon(imgTray);
            sysTray.add(JUkaStage.stageTray);
        }
        catch (Exception ex)
        {
            System.err.println(ex);
            return(false);
        }

        JUkaStage.stageTray.setImageAutoSize(true);
        JUkaStage.stageTray.setPopupMenu(new PopupMenu());

        MenuItem exitItem = new MenuItem("�˳�");
        exitItem.addActionListener(JUkaStage.eventListener);
        exitItem.setActionCommand("exit");
        JUkaStage.getStageTray().getPopupMenu().add(exitItem);

        return(true);
    }

  // Execute | ִ��
    /**
     * <p>һ������ʱ�����</p>
     */
    public static void execute()
    {
        JUkaStage.registeredShell = JUkaShellCtrl.getRegisteredShell();
        JUkaStage.registeredGhost = JUkaGhostCtrl.getRegisteredGhost();
        JUkaStage.registeredPlugin = JUkaPluginCtrl.getRegisteredPlugin();

        // ���� Shell->Ghost->Plugin
        // �ڴ˴����� ����ָʾ��GUI �Ĵ���
        for (Class compClass:JUkaStage.registeredShell)
        {
            try
            {
                Method onLoadMethod = compClass.getMethod("onLoad");
                onLoadMethod.invoke(compClass);
            }
            catch (Exception ex)
            {
                // TODO �ڴ˼��� ����ָʾ��GUI �ڼ��س���ʱ �Ĵ���
                System.err.println(ex);
                JUkaStage.registeredShell.remove(compClass);
            }
            // TODO �ڴ˼��� ����ָʾ��GUI �Ĵ���
        }

        // �ڴ˴����� ����ָʾ��GUI �Ĵ���
        for (Class compClass:JUkaStage.registeredGhost)
        {
            try
            {
                Method onLoadMethod = compClass.getMethod("onLoad");
                onLoadMethod.invoke(compClass);
            }
            catch (Exception ex)
            {
                // TODO �ڴ˼��� ����ָʾ��GUI �ڼ��س���ʱ �Ĵ���
                System.err.println(ex);
                JUkaStage.registeredGhost.remove(compClass);
            }
            // TODO �ڴ˼��� ����ָʾ��GUI �Ĵ���
        }

        // �ڴ˴����� ����ָʾ��GUI �Ĵ���
        for (Class compClass:JUkaStage.registeredPlugin)
        {
            try
            {
                Method onLoadMethod = compClass.getMethod("onLoad");
                onLoadMethod.invoke(compClass);
            }
            catch (Exception ex)
            {
                // TODO �ڴ˼��� ����ָʾ��GUI �ڼ��س���ʱ �Ĵ���
                System.err.println(ex);
                JUkaStage.registeredPlugin.remove(compClass);
            }
            // TODO �ڴ˼��� ����ָʾ��GUI �Ĵ���
        }

        // ���� Ghost->Plugin
        // �ڴ˴����� ����ָʾ��GUI �Ĵ���
        for (Class compClass:JUkaStage.registeredGhost)
        {
            try
            {
                Method onStartMethod = compClass.getMethod("onStart");
                onStartMethod.invoke(compClass);
            }
            catch (Exception ex)
            {
                // TODO �ڴ˼��� ����ָʾ��GUI �ڼ��س���ʱ �Ĵ���
                System.err.println(ex);
                JUkaStage.registeredGhost.remove(compClass);
            }
            // TODO �ڴ˼��� ����ָʾ��GUI �Ĵ���
        }

        // �ڴ˴����� ����ָʾ��GUI �Ĵ���
        for (Class compClass:JUkaStage.registeredPlugin)
        {
            try
            {
                Method onStartMethod = compClass.getMethod("onStart");
                onStartMethod.invoke(compClass);
            }
            catch (Exception ex)
            {
                // TODO �ڴ˼��� ����ָʾ��GUI �ڼ��س���ʱ �Ĵ���
                System.err.println(ex);
                JUkaStage.registeredPlugin.remove(compClass);
            }
            // TODO �ڴ˼��� ����ָʾ��GUI �Ĵ���
        }

        return;
    }

    /**
     * <p>�˷������ڴ����˳�����</p>
     * <p>�˷�����δʵ��</p>
     */
    public static void exit()
    {
        // TODO ���� �˳�ʱ �Ĵ���.
        System.exit(0);
        return;
    }

    /**
     * <p>�˷��������� JUkaStage ������׼�����˳�</p>
     * <p>�ڴ����˳������, JUkaStage ��ص������Ѽ��ص���� onExit() ����.
     * �����������˳�ǰ������Ӧ���ô˷�����֪ͨ�˳�����.<br>
     * ֻ��ȫ������������˳����� JUkagaka �����Զ��˳�, ����ѯ���û����.
     * �˷����ɾ�������������, �������Զ����������߲���</p>
     */
    public static void informExitStandby()
    {
        // TODO �ڴ˼��� �����˳������Ϣ �Ĵ���
    }

  // Event Process | �¼�������
    /**
     * <p>�˷���ʵ�� ActionListener �� actionPerformed() ����</p>
     * <p><ul>�˷���Ŀǰ������¼���:
     * <li>����ͼ��˵����˳�ָ��</ul></p>
     */
    public void actionPerformed(ActionEvent ev)
    {
        String actionCommand = ev.getActionCommand();
        if (actionCommand.equals("exit"));
        {
            JUkaStage.exit();
            return;
        }
    }

    /**
     * <p>�˷���ʵ�� ImageObserver �� imageUpdate() ����</p>
     * <p>�˷���ĿǰʲôҲ����, ��������Ϊʹ�� Image.getHeight() �ȷ����������.</p>
     */
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height)
    {
        return(false);
    }

  // Other | ����
    /**
     * @deprecated �˷���Ŀǰ�����ڵ���
     */
    public static void main(String[] args)
    {
        JUkaStage.initTrayIcon();
        return;
    }
}
