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

  // Install/Uninstall | 安装/卸载
    /**
     * <p>(模板)安装 Shell</p>
     * <p>Shell 在可以被使用之前, 必须向 JUkaStage 通知其存在<br>
     * (!) 此类的扩展者应<b>复制</b>这个函数并在 TODO 处填充自撰的代码,
     *     JUkaShell.java 中原有的部分不能被删减. 否则可能导致运行失败.<br>
     * </p>
     * @return <ul>返回值, 大于等于0均表示执行成功, 反之表示失败
     *  <li>0=正常
     *  <li>1=该组件已安装
     *  <li>-1=(未定义错误)
     *  <li>-2=运行栈上溢
     *  <li>-3=读写错误
     * </ul>
     */
    public static int install()
    {
        // TODO 在此处加入安装前的代码
        // 比如 检查依赖/冲突关系, EULA, 使用说明, 确认对话框, 建立用户数据 等等.
        return(JUkaShellCtrl.installComponent());
    }

    /**
     * <p>(模板)卸载 Shell</p>
     * <p>要不再加载某个 Shell, 必须向 JUkaStage 通知注销<br>
     * (!) 此类的扩展者应<b>复制</b>这个函数并在 TODO 处填充自撰的代码,
     *     JUkaShell.java 中原有的部分不能被删减. 否则可能导致运行失败.<br>
     * </p>
     * @return <ul>返回值, 大于等于0均表示执行成功, 反之表示失败
     *  <li>0=正常
     *  <li>1=该组件不在列表中
     *  <li>-1=(未定义错误)
     *  <li>-2=运行栈上溢
     *  <li>-3=读写错误
     * </ul>
     */
    public static int uninstall()
    {
        // TODO 在此处加入卸载前的代码
        // 比如 确认对话框, 清理用户数据, 级联卸载等等.
        return(JUkaShellCtrl.uninstallComponent());
    }

  // Trigger | 启动器
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

  // Shell Constructor & Destructor | Shell 构造/析构
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
     * <p>默认的构造方法...什么也不干</p>
     */
    public CyauShell()
    {
    }

  // Balloon | 气球
    //@Override
    // 此方法不是Override
    public BalloonWin createBalloon()
    {
        return(super.createBalloon(CyauShell.DEFAULT_INI, CyauShell.hashImages, CyauShell.hashMasks));
    }

  // Other | 杂项
    /**
     * @deprecated 此方法目前仅用于调试
     */
    public static void main(String[] args)
    {
        // 用于测试 Shell 装/卸载 及 图像加载时间的代码.
        System.out.println(CyauShell.install());

        CyauShell.onLoad();

        // 用于测试创建 Shell / Ukagaka / Balloon
        CyauShell testShell = CyauShell.createShell();
        testShell.setWinVisible(0, true);
        testShell.fireClip(0);
        testShell.setWinVisible(1, true);
        testShell.fireClip(1);

        //System.out.println(SampleShell.hashImages.size());
        //System.out.println(SampleShell.hashMasks.size());
        //System.out.println(SampleShell.uninstallComponent());

        //// 用于测试图像是否加载的代码
        //JWindow tmpJDialog = new JWindow();
        //tmpJDialog.setSize(320, 320);
        //tmpJDialog.setVisible(true);
        //tmpJDialog.getGraphics().drawImage(SampleShell.hashImages.get("shell/SampleShell/surface0000.png"),
            //0, 0,
            //JUkaStage.eventListener
        //);

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
