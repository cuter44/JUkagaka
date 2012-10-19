/**
 * @version v120826
 * @author "Galin"<cuter44@qq.com>
 */
package jukagaka;

import jukagaka.shell.JUkaShell;
import jukagaka.ghost.JUkaGhost;
import jukagaka.plugin.JUkaPlugin;

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
     * <p>��������Ǽ��Ѽ��ص�����б�</p>
     */
    private static ArrayList<Class> registeredShell, registeredGhost, registeredPlugin;
    /**
     * <p>���������¼�������б�</p>
     * <p>�����û���˵�����ĵ�λ�� Shell, ����һ�� ghost ���ƶ�� shell,<br>
     * һ�� shell ֻ����һ������ ghost, ���е�ָ���������Ҿ�����������...</p>
     */
    private static ArrayList<JUkaShell> activeShell;
    private static ArrayList<JUkaGhost> activeGhost;
    /**
     * �������� Tray �˵������������
     */
    public static JUkaStage eventListener = new JUkaStage();

  // Tray | ����
    /**
     * ���������¼���������ͼ������
     */
    private static TrayIcon stageTray = null;

    /**
     * <p>��ȡϵͳ����ͼ�������</p>
     */
    public static TrayIcon getStageTray()
    {
        return(JUkaStage.stageTray);
    }

    /**
     * <p>��ȡϵͳ����ͼ�������</p>
     */
    public static PopupMenu getTrayMenu()
    {
        return(JUkaStage.stageTray.getPopupMenu());
    }

    /**
     * �˷������ڳ�ʼ��ϵͳ����ͼ��
     */
    private static boolean initTrayIcon()
    {
        try
        {
            Image imgTray = Toolkit.getDefaultToolkit().getImage(JUkaUtility.getProgramDir() + "/TrayIcon.jpg");
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

        // ����ͼ���Զ�����
        JUkaStage.stageTray.setImageAutoSize(true);
        // ��������ͼ��˵�
        PopupMenu trayMenu = new PopupMenu();
        JUkaStage.stageTray.setPopupMenu(trayMenu);

        MenuItem cmdHatch = new MenuItem("������...");
        cmdHatch.addActionListener(JUkaStage.eventListener);
        cmdHatch.setActionCommand("cmdHatch");
        trayMenu.add(cmdHatch);

        // �ָ���
        trayMenu.add(new MenuItem("-"));

        MenuItem cmdExit = new MenuItem("�˳�");
        cmdExit.addActionListener(JUkaStage.eventListener);
        cmdExit.setActionCommand("cmdExit");
        trayMenu.add(cmdExit);

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
    public static void fireExitCmd()
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
    @Override
    public void actionPerformed(ActionEvent ev)
    {
        String actionCommand = ev.getActionCommand();
        if (actionCommand.equals("cmdExit"))
        {
            JUkaStage.fireExitCmd();
            return;
        }
    }

    /**
     * <p>�˷���ʵ�� ImageObserver �� imageUpdate() ����</p>
     * <p>�˷���ĿǰʲôҲ����, ��������Ϊʹ�� Image.getHeight() �ȷ����������.</p>
     */
    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height)
    {
        return(false);
    }

  // Other | ����
    /**
     * ���캯��, ���ܱ�����.
     */
    private JUkaStage()
    {
    }

    /**
     * @deprecated �˷���Ŀǰ�����ڵ���
     */
    public static void main(String[] args)
    {
        JUkaStage.initTrayIcon();
        return;
    }
}
