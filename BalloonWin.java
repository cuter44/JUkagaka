/**
 * @version v120831
 * @author "Galin"<cuter44@qq.com>
 */

package jukagaka.shell;

import jukagaka.*;
import javax.swing.JWindow;

public class BalloonWin extends JWindow
{
  // Create/Destroy | 构造/析构
    /**
     * <p>生成并返回一个空白的气球</p>
     * <p>生成的气球将被指定的 ini 文件中 balloon 段预初始化<br>
     * <ul>有效的字段有以下这些
     * <li>width=正整数 表示新气球的宽度(必须定义)
     * <li>height=正整数 表示新气球的高度(必须定义)
     * </ul></p>
     * @param argIni 表示记录有初始化信息的 ini 文件
     * @return 一个空白的气球引用
     */
    public static BalloonWin createBalloon(String argIni)
    {
        BalloonWin newBalloon = new BalloonWin();
        Hashtable htInitInfo = JUkaUtility.iniReadSector(argIni, "balloon");

        int h = 0,w = 0;

        // 初始化
        // 大小
        if (htInitInfo.containsKey("width"))
            w = Integer.parseInt(htInitInfo.get("width"));
        if (htInitInfo.containsKey("height"))
            h = Integer.parseInt(htInitInfo.get("height"));
        this.setSize(w,h);

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
