package jukagaka;

/* ͼ�� */
import java.awt.Toolkit;
import java.awt.Image;
/* ϵͳ���� */
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.PopupMenu;
import java.awt.AWTException;
/* ---- */

public class UkaTray implements UkaComponent
{
  // ����
    private static TrayIcon trayIcon = null;
  // ����
    public static boolean onLoad()
    {
        if (!SystemTray.isSupported())
        {
            System.err.println("note:UkaTray.onLoad():���л�����֧������ͼ��,UkaTray��ֹͣ");
            return(false);
        }

        // ����ͼ��
        String imgFileName = UkaDaemon.getConf("Uka.Tray.IconImage");
        if (imgFileName == null)
        {
            System.err.println("error:UkaTray.onLoad():�����ļ�û��ָ������ͼ��:Uka.Tray.IconImage");
            // ��ͼ����һ����ͼ��, �����������޷�ͨ���ļ����ڼ��
            imgFileName = "";
        }
        else
        {
            // ������ļ�������Ϊû�б�Ҫ, ����ָ�����ļ������� getImage() �����ؿ� Image ����
            //File imgFile = new File(imgFileName);
            //if (!imgFile.isFile())
                //System.err.println("warning:UkaTray.onLoad():�Ҳ���Ҫ����ļ�");
        }

        Image trayIconImage = Toolkit.getDefaultToolkit().getImage(imgFileName);
        trayIcon = new TrayIcon(trayIconImage);

        try
        {
            SystemTray.getSystemTray().add(trayIcon);
        }
        catch (AWTException ex)
        {
            System.err.println("fatal:UkaTray.onLoad():�ƺ����ܼ�������ͼ��...UkaTray��ֹͣ");
            ex.printStackTrace();

            trayIcon = null;
            return(false);
        }

        PopupMenu trayMenu = new PopupMenu();
        trayIcon.setPopupMenu(trayMenu);
        System.err.println(trayIcon.getPopupMenu());

        return(true);
    }

    public static boolean onStart()
    {
        return(true);
    }

    public static boolean onExit()
    {
        SystemTray.getSystemTray().remove(trayIcon);

        return(true);
    }

    public static boolean onInstall()
    {
        return(true);
    }

    public static boolean onUnistall()
    {
        return(true);
    }

    public static void main(String args[])
    {
        return;
    }
}