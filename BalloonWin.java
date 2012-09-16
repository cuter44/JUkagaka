/**
 * @version v120831
 * @author "Galin"<cuter44@qq.com>
 */

package jukagaka.shell;

import jukagaka.*;

import java.awt.Image;
import java.awt.geom.Area;
import java.util.Hashtable;
import javax.swing.JWindow;

public class BalloonWin extends JWindow
{
    private Hashtable<String, Image> htImages = null;
    private Hashtable<String, Area> htMasks = null;

    /**
     * <p>设定新的图像库引用</p>
     * <p>此方法在调用 createUkagaka() 时即自动被调用, 通常不需要手动地调用之</p>
     */
    public void setImageLib(Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        this.htImages = argHtImages;
        this.htMasks = argHtMasks;

        return;
    }

  // Create/Destroy | 构造/析构
    /**
     * <p>生成并返回一个空白的气球</p>
     * <p>生成的气球将被指定的 ini 文件中 balloon 段预初始化<br>
     * <ul>有效的字段有以下这些
     * <li>width=正整数 表示新气球的宽度(必须定义)
     * <li>height=正整数 表示新气球的高度(必须定义)
     * <li>background=字符串 取自拟加载图像节任一带有图层号的键, 表示新气球的背景画像
     * </ul></p>
     * @param argIni 表示记录有初始化信息的 ini 文件
     * @return 一个空白的气球引用
     */
    public static BalloonWin createBalloon(String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        BalloonWin newBalloon = new BalloonWin();
        Hashtable<String, String> htInitInfo = JUkaUtility.iniReadSector(argIniFile, "balloon");

        int h = 0,w = 0;

        // 初始化
        // 大小
        if (htInitInfo.containsKey("width"))
            w = Integer.parseInt(htInitInfo.get("width"));
        if (htInitInfo.containsKey("height"))
            h = Integer.parseInt(htInitInfo.get("height"));
        newBalloon.setSize(w,h);

        // 背景

        return(newBalloon);
    }

  // Other | 杂项
    /**
     * 私有构造函数
     */
    private BalloonWin()
    {
    }

    /**
     * @deprecated 此方法目前仅用于调试用途
     */
    //public static void main(String[] args)
    //{
        //return;
    //}
}
