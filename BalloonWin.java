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
import javax.swing.JDialog;
import com.sun.awt.AWTUtilities;

public class BalloonWin extends JDialog
{
  // Images & painting | 图像及绘制功能
    // 以下数据域及方法暂不提供
    //private Hashtable<String, Image> htImages = null;
    //private Hashtable<String, Area> htMasks = null;

    ///**
     //* <p>设定新的图像库引用</p>
     //* <p>此方法在调用 createUkagaka() 时即自动被调用, 通常不需要手动地调用之</p>
     //*/
    //public void setImageLib(Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    //{
        //this.htImages = argHtImages;
        //this.htMasks = argHtMasks;

        //return;
    //}

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

    ///**
     //* <p>从库中抽取对应 argHashKey 的背景图像及蒙版</p>
     //* <p></p>
     //*/
    //public boolean setBkImage(String argHashKey, Hashtable<String, Image> htImages, Hashtable<String, Area> htMasks)
    //{
        //try
        //{
            //this.setBkImage(htImages.get(argHashKey), htMasks.get(argHashKey));
        //}
        //catch (Exception ex)
        //{
            //System.err.println(ex);
            //return(false);
        //}

        //return(true);
    //}

  // paint | 绘制
    /**
     * 标记缓存有效性
     */
    private boolean transOutdated = false;

    /**
     * 此二字段记录缩放后的图像缓存
     */
    private Image transImage = null;
    private Area transMask = null;

    public void computeTrans()
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

        this.transMask = JUkaShellCtrl.calculateMask(this.transImage);

        //this.transMask = this.originMask.createTransformedArea(
            //new AffineTransform(
                //(double)winSize.width/imgWidth, 0,   // ScaleX, SheerY
                //0, (double)winSize.height/imgHeight, // SheerX, ScaleY
                //0, 0                                 // TransXY
            //)
        //);

        // 输出路径的代码, 调试用
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
            this.computeTrans();
        g.drawImage(this.transImage, 0, 0, this);
        return;
    }

    public void clip()
    {
        if (this.transOutdated)
            this.computeTrans();
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
        if (htInitInfo.containsKey("background"))
            newBalloon.setBkImage(argHtImages.get(htInitInfo.get("background")), argHtMasks.get(htInitInfo.get("background")));

        // 去标题栏
        newBalloon.setUndecorated(true);

        // 可拖动
        newBalloon.setDragable();

        return(newBalloon);
    }

    /**
     * <p>此方法使窗体变得可拖动</p>
     * <p>调用一次即可. <br>
     * (!)此过程不可逆, 也就是说, 不可能使它重新变得不可拖动</p>
     */
  // Other | 杂项
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

    /**
     * 此二数据域用于记录鼠标相对坐标, 为拖动窗体提供支持.
     */
    private int relativeX, relativeY;

    @Override
    public void setSize(Dimension d)
    {
        this.setSize(d.width, d.height);
    }

    private void setDragable()
    {
        // 侦听鼠标按下, 记录按下时的位置.
        this.addMouseListener(
            new MouseAdapter()
            {
                public void mousePressed(MouseEvent ev)
                {
                    relativeX = ev.getX();
                    relativeY = ev.getY();
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
                    // 仅响应左键拖动
                    if (ev.getModifiers()!=16)
                        return;

                    int absoluteX = ev.getXOnScreen();
                    int absoluteY = ev.getYOnScreen();
                    setLocation(absoluteX - relativeX, absoluteY - relativeY);
                    return;
                }
            }
        );
    }

    /**
     * 构造函数...但不应该被使用
     */
    protected BalloonWin()
    {
    }

    /**
     * @deprecated 此方法目前仅用于调试用途
     */
    public static void main(String[] args)
    {
        return;
    }
}
