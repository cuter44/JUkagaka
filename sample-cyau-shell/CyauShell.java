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

    public static final String DEFAULT_INI = JUkaUtility.getProgramPath() + "shell/cyaushell/SampleShell.ini";
    public static Hashtable<String, Image> hashImages = null;
    public static Hashtable<String, Area> hashMasks = null;

  // Install/Uninstall | 安装/卸载
    /**
     * <p>安装 Shell 的方法</p>
     * <p>在使用之前, 应调用此方法以在 JUkagaka 中注册以便在启动时被读取<br>
     * 开发者可以在 TODO 处加入安装前的用户向代码.<br>
     * (!) 此方法会被回调, 故请勿改变方法签名<br>
     * (*) 可以自定义返回值 &lt;0=失败, &gt;0=成功</p>
     * @return <ul>返回值<li>0=成功<li>1=该组件已经安装<li>-1=(未定义错误)
     * <li>-2=运行栈上溢<li>-3=读写错误
     */
    public static int installComponent()
    {
        // TODO 在此处加入安装前的代码
        // 比如 EULA, 使用说明, 确认对话框什么的.
        return(JUkaShellCtrl.installComponent());
    }

    /**
     * <p>卸载 Shell 的方法</p>
     * <p>在删除之前, 应调用此方法以在 JUkagaka 中注册以便在启动时不再被读取<br>
     * 开发者可以在 TODO 处加入卸载前的用户向代码.<br>
     * (!) 此方法会被回调, 故请勿改变方法签名<br>
     * (*) 可以自定义返回值 &lt;0=失败, &gt;0=成功</p>
     * @return <ul>返回值<li>0=成功<li>1=该组件不在列表中<li>-1=(未定义错误)
     * <li>-2=运行栈上溢<li>-3=读写错误
     */
    public static int uninstallComponent()
    {
        // TODO 在此处加入卸载前的代码
        // 比如确认对话框, 清理用户数据, 通知其他组件什么的.
        return(JUkaShellCtrl.uninstallComponent());
    }

  // Start-up | 启动器
    /**
     * 此方法在 JUkagaka 启动时被调用以完成初始化
     */
    public static void onLoad()
    {
        JUkaShell.onLoad();

        // 加载图像资源
        try
        {
            Class classOfSampleShell = Class.forName("jukagaka.shell.cyaushell.CyauShell");
            JUkaShellCtrl.prefetchImageResource(classOfSampleShell, SampleShell.DEFAULT_INI, "images", true);
        }
        catch (Exception ex)
        {
            System.err.println(ex);
            return;
        }

        System.out.println("onLoad() was invoked successfully");
        return;
    }

  // Other | 杂项
    /**
     * @deprecated 此方法目前仅用于调试
     */
    public static void main(String[] args)
    {
        //// 用于测试 Shell 装/卸载 及 图像加载时间的代码.
        System.out.println(SampleShell.installComponent());
        try
        {
            Class classOfSampleShell = Class.forName("jukagaka.shell.cyaushell.CyauShell");
            JUkaShellCtrl.prefetchImageResource(classOfSampleShell, SampleShell.DEFAULT_INI, "images", true);
        }
        catch (Exception ex)
        {
            System.err.println(ex);
            return;
        }
        //System.out.println(SampleShell.hashImages.size());
        //System.out.println(SampleShell.hashMasks.size());
        //System.out.println(SampleShell.uninstallComponent());

        //// 用于测试图像是否加载的代码
        //JWindow tmpJDialog = new JWindow();
        //tmpJDialog.setSize(320, 320);
        //tmpJDialog.setVisible(true);
        //tmpJDialog.getGraphics().drawImage(SampleShell.hashImages.get("shell/SampleShell/surface0000.png"),
                                           //0, 0,
                                           //JUkaStage.eventListener);

        // 用于测试 UkagagkaWin 能否正确缓存的方法
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
