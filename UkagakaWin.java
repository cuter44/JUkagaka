/**
 * @version v120831
 * @author "Galin"<cuter44@qq.com>
 */

package jukagaka.shell;

import javax.swing.JWindow;

public class UkagakaWin extends JWindow
{
  // Paint | 绘制
    /**
     * <p>此四个数据域记录绘图用的图层, 蒙版及缓存</p>
     */
    private Image[] imageLayer = new Image[8];
    private Area[] maskLayer = new Area[8];
    /**
     * <p>此二数据域缓存 imageLayer 和 maskLayer 前四层的内容</p>
     */
    private Image cacheImage = null;
    private Area cacheMask = null;

    /**
     * <p>绘制函数</p>
     */
    @Override
    public void paintComponent()
    {
        Image =
        return();
    }

  // Create/Destroy | 构造/析构
    /**
     * <p>生成并返回一个新的春菜(指用于绘制春菜的窗体)</p>
     * <p>生成的新春菜将被指定的 ini 文件中 ukagaka 段预初始化<br>
     * <ul>有效的字段包括以下这些
     * <li>
     * <li>width=正整数 指定新春菜的宽度(必须定义)
     * <li>height=正整数 指定新春菜的高度(必须定义)
     * <li>layer4=字符串 指定新春菜的画像, 用于4-主要层, (必须定义)
     * </ul></p>
     * @param argIni 表示记录有初始化信息的 ini 文件
     * @return 春菜窗体的引用
     */
    public static BalloonWin createUkagaka(String argIni, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        UkagakaWin newUkaWin = new BalloonWin();
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
    private UkagakaWin()
    {
    }

    /**
     * @deprecated 此方法目前仅用于调试
     */
    //public static void main(String[] args)
    //{
        //return;
    //}
}
