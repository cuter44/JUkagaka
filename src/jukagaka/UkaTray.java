package jukagaka;

/* 图像 */
import java.awt.Toolkit;
import java.awt.Image;
/* 系统托盘 */
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.PopupMenu;
import java.awt.AWTException;
/* ---- */

public class UkaTray implements UkaComponent
{
  // 数据
    private static TrayIcon trayIcon = null;
  // 代码
    public static boolean onLoad()
    {
        if (!SystemTray.isSupported())
        {
            System.err.println("note:UkaTray.onLoad():运行环境不支持托盘图标,UkaTray已停止");
            return(false);
        }

        // 析出图标
        String imgFileName = UkaDaemon.getConf("Uka.Tray.IconImage");
        if (imgFileName == null)
        {
            System.err.println("error:UkaTray.onLoad():配置文件没有指定托盘图标:Uka.Tray.IconImage");
            // 试图生成一个空图标, 但这种做法无法通过文件存在检查
            imgFileName = "";
        }
        else
        {
            // 不检查文件存在因为没有必要, 假如指定的文件不存在 getImage() 将返回空 Image 对象
            //File imgFile = new File(imgFileName);
            //if (!imgFile.isFile())
                //System.err.println("warning:UkaTray.onLoad():找不到要求的文件");
        }

        Image trayIconImage = Toolkit.getDefaultToolkit().getImage(imgFileName);
        trayIcon = new TrayIcon(trayIconImage);

        try
        {
            SystemTray.getSystemTray().add(trayIcon);
        }
        catch (AWTException ex)
        {
            System.err.println("fatal:UkaTray.onLoad():似乎不能加入托盘图标...UkaTray已停止");
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