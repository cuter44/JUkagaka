/**
 * @version v120831
 * @author "Galin"<cuter44@qq.com>
 */

package jukagaka.shell;

import jukagaka.*;

import java.awt.Image;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.AffineTransform;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.util.Hashtable;
//import javax.swing.JDialog;
import com.sun.awt.AWTUtilities;

public class BalloonWin extends JUkaWindow
{
  // Image APIs | 图像设定用API
    /**
     * 此二数据域记录原始(缩放前)的图形及蒙版引用
     */
    private Image originImage = null;
    private Area originMask = null;

    /**
     * <p>使用指定的背景图像及蒙版</p>
     * <p>修改将在窗体下一次重绘时生效</p>
     */
    public void setBkImage(Image argImage, Area argMask)
    {
        this.originImage = argImage;
        this.originMask = argMask;
        this.transOutdated = true;
        return;
    }

  // Paint | 绘制背景
    /**
     * 标记缓存有效性
     */
    private boolean transOutdated = false;

    /**
     * 此二字段记录缩放后的图像缓存
     */
    private Image transImage = null;
    private Area transMask = null;

    public void updateTrans()
    {
        Dimension winSize = this.getSize();
        //System.out.println(this.originImage);
        int imgWidth = this.originImage.getWidth(this);
        int imgHeight = this.originImage.getHeight(this);

        this.transImage = this.originImage.getScaledInstance(
            winSize.width,
            winSize.height,
            Image.SCALE_SMOOTH
        );

        /* 下面有两种策略获得新蒙版,
         * 两者在结果上的差别是几何变换时也许会有锯齿, 不过在2x以下放大倍数下肉眼很难看出来.
         * 不过从算法分析上说, 几何转换会比重新计算快至少一个级别
         * 重新计算 = O(n*m*x) 几何转换 = O(n+m) x 是放大倍数.
         */
        // 重新计算策略
        // this.transMask = JUkaShellCtrl.calculateMask(this.transImage);

        // 几何转换策略
        this.transMask = this.originMask.createTransformedArea(
            new AffineTransform(
                (double)winSize.width/imgWidth, 0,   // ScaleX, SheerY
                0, (double)winSize.height/imgHeight, // SheerX, ScaleY
                0, 0                                 // TransXY
            )
        );

        // 调试用, 输出路径的代码
        //PathIterator iter = this.transMask.getPathIterator(null);
        //float[] points = new float[6];

        //int i = 1;
        //while (!iter.isDone())
        //{

            //System.out.print(i++);

            //int status = iter.currentSegment(points);

            //if (status == PathIterator.SEG_MOVETO)
                //System.out.println("Move to " + points[0] + ", " + points[1]);

            //if (status == PathIterator.SEG_LINETO)
                //System.out.println("Line to " + points[0] + ", " + points[1]);

            //if (status == PathIterator.SEG_CLOSE)
                //System.out.println("End.");

            //iter.next();
        //}

        //iter = this.originMask.getPathIterator(null);

        //i = 1;
        //while (!iter.isDone())
        //{

            //System.out.print(i++);

            //int status = iter.currentSegment(points);

            //if (status == PathIterator.SEG_MOVETO)
                //System.out.println("Move to " + points[0] + ", " + points[1]);

            //if (status == PathIterator.SEG_LINETO)
                //System.out.println("Line to " + points[0] + ", " + points[1]);

            //if (status == PathIterator.SEG_CLOSE)
                //System.out.println("End.");

            //iter.next();
        //}

        this.transOutdated = false;
        return;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        if (this.transOutdated)
            this.updateTrans();
        g.drawImage(this.transImage, 0, 0, this);
        return;
    }

    @Override
    public void clip()
    {
        if (this.transOutdated)
            this.updateTrans();
        AWTUtilities.setWindowShape(this, this.transMask);
        return;
    }

  // Create/Destroy | 构造/析构
    /**
     * <p>生成并返回一个空白的气球</p>
     * <p>生成的气球将被指定的 ini 文件中 balloon 段预初始化<br>
     * <ul>有效的字段有以下这些
     * <li>width=正整数 表示新气球的宽度(建议定义)
     * <li>height=正整数 表示新气球的高度(建议定义)
     * <li>background=字符串 取自拟加载图像节任一带有图层号的键, 表示新气球的背景画像<br>
     *     背景画像会自动被缩放到整个窗体. 更多的排布方式正在开发中.
     * </ul></p>
     * @param argIni 表示记录有初始化信息的 ini 文件
     * @return 一个空白的气球引用
     */
    public static BalloonWin createBalloon(String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        BalloonWin newBalloon = new BalloonWin();
        newBalloon.initalize(argIniFile, argHtImages, argHtMasks);
        return(newBalloon);
    }

    /**
     * <p>命令BalloonWin执行自初始化</p>
     * <p>事实上此方法调用
     * BalloonWin.initalizeInstance(this, argIni, argHtImages, argHtMasks)
     * 执行实际的初始化操作</p>
     */
    public void initalize(String argIni, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        BalloonWin.initalizeInstance(this, argIni, argHtImages, argHtMasks);
    }

    /**
     * <p>初始化气球窗体</p>
     * <p>使用指定的 ini 文件中 balloon 段预初始化<br>
     * <ul>有效的字段有以下这些
     * <li>width=正整数 表示新气球的宽度(建议定义)
     * <li>height=正整数 表示新气球的高度(建议定义)
     * <li>background=字符串 取自拟加载图像节任一带有图层号的键, 表示新气球的背景画像<br>
     *     背景画像会自动被缩放到整个窗体. 更多的排布方式正在开发中.
     * </ul></p>
     * @param argIni 表示记录有初始化信息的 ini 文件
     * @return 一个空白的气球引用
     */
    public static BalloonWin initalizeInstance(BalloonWin argBalloon, String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        Hashtable<String, String> htInitInfo = JUkaUtility.iniReadSector(argIniFile, "balloon");

        int h = 0,w = 0;

        // 初始化
        // 大小
        if (htInitInfo.containsKey("width"))
            w = Integer.parseInt(htInitInfo.get("width"));
        if (htInitInfo.containsKey("height"))
            h = Integer.parseInt(htInitInfo.get("height"));
        argBalloon.setSize(w,h);

        // 背景
        if (htInitInfo.containsKey("background"))
            argBalloon.setBkImage(argHtImages.get(htInitInfo.get("background")), argHtMasks.get(htInitInfo.get("background")));

        // 去标题栏
        argBalloon.setUndecorated(true);

        // 可拖动(可选)
        //newBalloon.setDragable(true);

        return(argBalloon);
    }

    /**
     * 构造函数...但不应该被使用
     */
    protected BalloonWin()
    {
    }

  // Dragable | 窗体可拖动化
    /**
     * 此二数据域用于记录鼠标相对坐标, 为拖动窗体提供支持.
     */
    private int relativeX,relativeY;
    /**
     * 指示拖动可用性的开关, true = ON, false = OFF
     */
    private boolean dragSwitch = false;
    /**
     * 指示拖动侦听器已设定
     */
    private boolean dragSetup = false;

    /**
     * <p>此方法更改窗体的可拖动属性</p>
     * <p>此方法在首次被调用时将在窗体实例上安装拖动侦听器, 该侦听器在窗体销毁前
     * 一直有效, 但是否响应鼠标拖动由 dragSwitch 开关决定.<br>
     * </p>
     * @param dragSwitch 指示拖动功能的开关状态
     */
    public void setDragable(boolean argDragSwitch)
    {
        this.dragSwitch = argDragSwitch;

        if (this.dragSetup)
            return;
        this.dragSetup = true;

        // 侦听鼠标按下, 记录按下时的位置.
        this.addMouseListener(
            new MouseAdapter()
            {
                public void mousePressed(MouseEvent ev)
                {
                    if (dragSwitch)
                    {
                        relativeX = ev.getX();
                        relativeY = ev.getY();
                    }
                    return;
                }
            }
        );

        // 侦听鼠标拖动, 计算新的窗口位置.
        this.addMouseMotionListener(
            new MouseMotionAdapter()
            {
                public void mouseDragged(MouseEvent ev)
                {
                    if (!dragSwitch)
                        return;
                    // 调试用, 可以在标准输出查看对应按键的掩码
                    //System.out.println(ev.getModifers());

                    // 仅响应左键拖动
                    // 要响应其他按键修改掩码即可
                    if (ev.getModifiers() == 16)
                    {
                        int absoluteX = ev.getXOnScreen();
                        int absoluteY = ev.getYOnScreen();
                        setLocation(absoluteX - relativeX, absoluteY - relativeY);
                    }

                    return;
                }
            }
        );

        return;
    }

  // Resize(@Override) | 窗体大小变更(重写)
    /**
     * 变更大小时要求重绘
     */
    @Override
    public void setSize(int width, int height)
    {
        super.setSize(width, height);
        this.transOutdated = true;
        return;
    }

    @Override
    public void setSize(Dimension d)
    {
        this.setSize(d.width, d.height);
    }

  // Other | 杂项
    /**
     * @deprecated 此方法目前仅用于调试用途
     */
    public static void main(String[] args)
    {
        return;
    }
}
