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
  // Ststic Data | 静态数据域
    /**
     * <p>此数据域登记已加载的</p>
     */
    private static ArrayList<Class> registeredShell, registeredGhost, registeredPlugin;
    //private static ArrayList<JUkaComponent> activeShell, activeGhost, activePlugin;
    /**
     * 此数据域记录程序的托盘图标引用
     */
    private static TrayIcon stageTray = null;
    /**
     * 此数据域记录 JUkaStage 的消息侦听器
     */
    public static JUkaStage eventListener = new JUkaStage();

  // Tray | 托盘
    /**
     * 此方法用于获取系统托盘图标的引用
     */
    public static TrayIcon getStageTray()
    {
        return(JUkaStage.stageTray);
    }
    /**
     * 此方法用于初始化系统托盘图标
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

        MenuItem exitItem = new MenuItem("退出");
        exitItem.addActionListener(JUkaStage.eventListener);
        exitItem.setActionCommand("exit");
        JUkaStage.getStageTray().getPopupMenu().add(exitItem);

        return(true);
    }

  // Execute | 执行
    /**
     * <p>一般运行时的入口</p>
     */
    public static void execute()
    {
        JUkaStage.registeredShell = JUkaShellCtrl.getRegisteredShell();
        JUkaStage.registeredGhost = JUkaGhostCtrl.getRegisteredGhost();
        JUkaStage.registeredPlugin = JUkaPluginCtrl.getRegisteredPlugin();

        // 加载 Shell->Ghost->Plugin
        // 在此处加入 进度指示器GUI 的代码
        for (Class compClass:JUkaStage.registeredShell)
        {
            try
            {
                Method onLoadMethod = compClass.getMethod("onLoad");
                onLoadMethod.invoke(compClass);
            }
            catch (Exception ex)
            {
                // TODO 在此加入 进度指示器GUI 在加载出错时 的代码
                System.err.println(ex);
                JUkaStage.registeredShell.remove(compClass);
            }
            // TODO 在此加入 进度指示器GUI 的代码
        }

        // 在此处加入 进度指示器GUI 的代码
        for (Class compClass:JUkaStage.registeredGhost)
        {
            try
            {
                Method onLoadMethod = compClass.getMethod("onLoad");
                onLoadMethod.invoke(compClass);
            }
            catch (Exception ex)
            {
                // TODO 在此加入 进度指示器GUI 在加载出错时 的代码
                System.err.println(ex);
                JUkaStage.registeredGhost.remove(compClass);
            }
            // TODO 在此加入 进度指示器GUI 的代码
        }

        // 在此处加入 进度指示器GUI 的代码
        for (Class compClass:JUkaStage.registeredPlugin)
        {
            try
            {
                Method onLoadMethod = compClass.getMethod("onLoad");
                onLoadMethod.invoke(compClass);
            }
            catch (Exception ex)
            {
                // TODO 在此加入 进度指示器GUI 在加载出错时 的代码
                System.err.println(ex);
                JUkaStage.registeredPlugin.remove(compClass);
            }
            // TODO 在此加入 进度指示器GUI 的代码
        }

        // 运行 Ghost->Plugin
        // 在此处加入 进度指示器GUI 的代码
        for (Class compClass:JUkaStage.registeredGhost)
        {
            try
            {
                Method onStartMethod = compClass.getMethod("onStart");
                onStartMethod.invoke(compClass);
            }
            catch (Exception ex)
            {
                // TODO 在此加入 进度指示器GUI 在加载出错时 的代码
                System.err.println(ex);
                JUkaStage.registeredGhost.remove(compClass);
            }
            // TODO 在此加入 进度指示器GUI 的代码
        }

        // 在此处加入 进度指示器GUI 的代码
        for (Class compClass:JUkaStage.registeredPlugin)
        {
            try
            {
                Method onStartMethod = compClass.getMethod("onStart");
                onStartMethod.invoke(compClass);
            }
            catch (Exception ex)
            {
                // TODO 在此加入 进度指示器GUI 在加载出错时 的代码
                System.err.println(ex);
                JUkaStage.registeredPlugin.remove(compClass);
            }
            // TODO 在此加入 进度指示器GUI 的代码
        }

        return;
    }

    /**
     * <p>此方法用于处理退出请求</p>
     * <p>此方法尚未实现</p>
     */
    public static void exit()
    {
        // TODO 加入 退出时 的代码.
        System.exit(0);
        return;
    }

    /**
     * <p>此方法用于向 JUkaStage 报告已准备好退出</p>
     * <p>在触发退出请求后, JUkaStage 会回调所有已加载的类的 onExit() 方法.
     * 组件类在完成退出前工作后应调用此方法以通知退出就绪.<br>
     * 只有全部组件报告其退出就绪 JUkagaka 才能自动退出, 否则将询问用户意见.
     * 此方法由具体组件子类调用, 方法会自动侦测其调用者并在</p>
     */
    public static void informExitStandby()
    {
        // TODO 在此加入 接收退出完成信息 的代码
    }

  // Event Process | 事件处理方法
    /**
     * <p>此方法实现 ActionListener 的 actionPerformed() 方法</p>
     * <p><ul>此方法目前处理的事件有:
     * <li>托盘图标菜单的退出指令</ul></p>
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
     * <p>此方法实现 ImageObserver 的 imageUpdate() 方法</p>
     * <p>此方法目前什么也不做, 仅仅是作为使用 Image.getHeight() 等方法的填坑物.</p>
     */
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height)
    {
        return(false);
    }

  // Other | 其他
    /**
     * @deprecated 此方法目前仅用于调试
     */
    public static void main(String[] args)
    {
        JUkaStage.initTrayIcon();
        return;
    }
}
