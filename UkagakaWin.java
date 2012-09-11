/**
 * @version v120831
 * @author "Galin"<cuter44@qq.com>
 */

package jukagaka.shell;

import jukagaka.*;

import javax.swing.JWindow;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.geom.Area;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.util.Hashtable;
import com.sun.awt.AWTUtilities;

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
    private BufferedImage cacheImage = null;
    private Area cacheMask = null;

    /**
     * <p>此数据域指定默认的缓冲层数, 从 #0 到 #bufferLayer 层, 默认为 3.</p>
     * 请参见<a href="buildBackBuffer">buildBackBuffer()</a>
     */
    private int bufferLayer = 3;

    /**
     * <p><a id="buildBackBuffer">生成缓存</a></p>
     * <p>通过将不常改变的背景绘制到缓存中可以在实际绘制时提高性能.<br>
     * 如果指定的图层都为 null, 将自动绘制空白的缓存<br>
     * 如果不需要绘制缓冲, 请指定 bufferLayer = -1, 但即使如此也需要调用这个
     * 方法以保证正确执行.<br>
     * 大多数影响图层的方法都将自动重建缓存, 关于此条请参见各方法的注释.</p>
     */
    public void buildBackBuffer()
    {
        Dimension winSize = this.getSize();

        // 创建新的空白缓存
        this.cacheImage = new BufferedImage(winSize.width, winSize.height,BufferedImage.TYPE_INT_ARGB);
        this.cacheMask = new Area();
        Graphics g = this.cacheImage.getGraphics();

        // 检查非空图层
        int i;
        for (i=0; i<=this.bufferLayer; i++)
            if (this.imageLayer[i] != null)
            {
                g.drawImage(imageLayer[i], 0, 0, JUkaStage.eventListener);
                this.cacheMask.add(maskLayer[i]);
            }

        g.dispose();
        return;
    }

    /**
     * <p>绘制</p>
     * <p>此重载方法将根据缓存和图层重绘窗体</p>
     */
    @Override
    public void paint(Graphics g)
    {
        int i;

        super.paint(g);

        g.drawImage(this.cacheImage, 0, 0, JUkaStage.eventListener);
        for (i=this.bufferLayer+1; i<=7; i++)
            if (imageLayer[i]!=null)
                g.drawImage(this.imageLayer[i], 0, 0, JUkaStage.eventListener);

        return;
    }

    /**
     * <p>裁剪</p>
     * <p>此方法将根据缓存和图层裁剪窗体, 将透明的像素镂空或去除.</p>
     */
    public void clip()
    {
        int i;
        Area tmpMask = (Area)this.cacheMask.clone();

        for (i=this.bufferLayer+1; i<=7; i++)
            if (maskLayer[i]!=null)
                tmpMask.add(maskLayer[i]);

        AWTUtilities.setWindowShape(this, tmpMask);

        return;
    }

  // Create/Destroy | 构造/析构
    /**
     * <p>生成并返回一个新的春菜(指用于绘制春菜的窗体)</p>
     * <p>生成的新春菜将被指定的 ini 文件中 ukagaka 段预初始化<br>
     * <ul>有效的字段包括以下这些
     * <li>
     * <li>width=正整数 指定新春菜的宽度 (必须定义)
     * <li>height=正整数 指定新春菜的高度 (必须定义)
     * <li>layer?=字符串 取自 images 节任一键或值, 指定绘图的第?层, ? = 0-7 必须至少定义一个
     * </ul></p>
     * @param argIni 表示记录有初始化信息的 ini 文件
     * @return 春菜窗体的引用
     */
    public static UkagakaWin createUkagaka(String argIni, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        UkagakaWin newUkaWin = new UkagakaWin();
        Hashtable<String, String> htInitInfo = JUkaUtility.iniReadSector(argIni, "ukagaka");

        int h = 0,w = 0,i;

        // 初始化
        // 大小
        if (htInitInfo.containsKey("width"))
            w = Integer.parseInt(htInitInfo.get("width"));
        if (htInitInfo.containsKey("height"))
            h = Integer.parseInt(htInitInfo.get("height"));
        newUkaWin.setSize(w,h);

        // 图层
        for (i=0; i<=7; i++)
            if (htInitInfo.containsKey("layer"+i))
            {
                String tmpImageName = htInitInfo.get("layer"+i);
                // 通常都不应该有空值...不过总是会有人犯二的.
                if (tmpImageName.equals(""))
                    continue;
                newUkaWin.imageLayer[i] = argHtImages.get(tmpImageName);
                newUkaWin.maskLayer[i] = argHtMasks.get(tmpImageName);
            }
        newUkaWin.buildBackBuffer();

        return(newUkaWin);
    }

  // Dragable | 窗体可拖动化
  /**
   * 此二数据域用于记录鼠标相对坐标, 为拖动窗体提供支持.
   */
    private int relativeX,relativeY;

    /**
     * <p>此方法使窗体变得可拖动</p>
     * <p>调用一次即可. <br>
     * (!)此过程不可逆, 也就是说, 不可能使它重新变得不可拖动</p>
     */
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
     * <p>私有构造函数</p>
     * <p>从这里加入了拖动窗体的代码</p>
     */

    private UkagakaWin()
    {
        this.setDragable();
    }

  // Other | 杂项
    /**
     * @deprecated 此方法目前仅用于调试
     */
    public static void main(String[] args)
    {
        return;
    }
}
