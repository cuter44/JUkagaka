/**
 * @version v120831
 * @author "Galin"<cuter44@qq.com>
 */

package jukagaka.shell;

import jukagaka.*;

//import javax.swing.JDialog;
import java.awt.Color;
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

public class UkagakaWin extends JUkaWindow
{

  // Layer & Buffer | 变更图层及缓存
    /**
     * <p>此四个数据域记录绘图用的图层, 蒙版及缓存</p>
     */
    private Image[] imageLayer = new Image[8];
    private Area[] maskLayer = new Area[8];
    /**
     * <p>此数据域记录绘图坐标</p>
     */
    private int[][] coordinate = new int[8][2];
    /**
     * <p>此数据域记录 imageLayer 和 maskLayer 要求缓冲的内容</p>
     */
    private BufferedImage cacheImage = null;
    private Area cacheMask = null;
    /**
     * <p>此二数据域用于记录 Shell 图像/蒙版库的引用</p>
     * <p>数据域内容以引用形式获取自宿主Shell, 因此宿主Shell图像库的变更会自动
     * 反映到此数据域</p>
     */
    private Hashtable<String, Image> htImages = null;
    private Hashtable<String, Area> htMasks = null;

    /**
     * <p>此数据域指定默认的缓存层数, 从 #0 到 #bufferLayer 层, 默认为 3.</p>
     * 请参见<a href="buildBackBuffer">buildBackBuffer()</a>
     */
    private int bufferLayer = 3;

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

    /**
     * <p>此方法用于变更缓存层数</p>
     */
    public int setBufferLayer(int newBufferLayer)
    {
        if ((newBufferLayer<0) || (newBufferLayer>7))
            return(this.getBufferLayer());

        return(this.bufferLayer = newBufferLayer);
    }

    /**
     * <p>此方法返回当前缓存层数</p>
     */
    public int getBufferLayer()
    {
        return(this.bufferLayer);
    }

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

        // 生成缓存
        int i;
        for (i=0; i<=this.bufferLayer; i++)
            if (this.imageLayer[i] != null)
            {
                g.drawImage(
                    this.imageLayer[i],
                    this.coordinate[i][0],
                    this.coordinate[i][1],
                    this
                );
                this.cacheMask.add(maskLayer[i]);
            }

        g.dispose();
        this.bufferOutdated = false;

        return;
    }

    /**
     * <p>变更图层及蒙版</p>
     * <p>此方法无视 argHashKey 中的位置信息(也就是说, 只将其作为图像索引使用),
     * 只根据 argIndex, x, y 设置图层</p>
     */
    public boolean setImageLayer(String argHashKey, int argLayer, int x, int y)
    {
        if ((argHashKey == null) || (argHashKey.equals("")))
        // 清除图层
        {
            this.imageLayer[argLayer] = null;
            this.maskLayer[argLayer] = null;
        }
        else
        // 设置图层
        {
            // 越界检查
            if ((argLayer < 0) || (argLayer > 7))
                return(false);

            // 图元存在检查
            if (!this.htImages.containsKey(argHashKey))
                return(false);

            this.imageLayer[argLayer] = htImages.get(argHashKey);
            this.maskLayer[argLayer] = htMasks.get(argHashKey);
            this.coordinate[argLayer][0] = x;
            this.coordinate[argLayer][1] = y;
        }

        if (argLayer <= this.bufferLayer)
            this.bufferOutdated = true;

        return(true);
    }

    /**
     * <p>变更图层及蒙版</p>
     * <p>仅仅是带有图层和坐标标记的 HashKey 才能正确使用此方法</p>
     */
    public boolean setImageLayer(String argHashKey)
    {
        int layer, x, y;
        String[] separatedKey = argHashKey.split(",");

        try
        {
            layer = Integer.parseInt(separatedKey[1]);
        }
        catch (Exception ex)
        {
            System.err.println("argHashKey 不带有合法图层号: " + argHashKey + " ,自动假设为: " + (this.bufferLayer + 1));
            layer = this.bufferLayer + 1;
        }

        try
        {
            x = Integer.parseInt(separatedKey[2]);
            y = Integer.parseInt(separatedKey[3]);
        }
        catch (Exception ex)
        {
            System.err.println("argHashKey 不带有合法坐标: " + argHashKey + " ,自动假设为(0,0)");
            x = 0;
            y = 0;
        }

        //if (separatedKey[0].equals(""))
            //return(this.setImageLayer(null,layer,0,0));
        return(this.setImageLayer(argHashKey, layer, x, y));
    }

    /**
     * <p>成批地变更图层及蒙版</p>
     * <p>仅仅是带有图层和坐标标记的 HashKey 才能正确使用此方法</p>
     */
    public boolean setImageBatch(String[] arrHashKeys)
    {
        boolean rtSuccess = true;
        int i;

        for (i=1; i<=arrHashKeys.length; i++)
            rtSuccess = rtSuccess && this.setImageLayer(arrHashKeys[i]);

        return(rtSuccess);
    }

    public boolean setImageBatch(String arrHashKeys)
    {
        try
        {
            String[] splitedKeys = arrHashKeys.split("\\|");
            return(setImageBatch(splitedKeys));
        }
        catch (Exception ex)
        {
            System.err.println(ex);
            return(false);
        }
    }

  // Paint | 绘制
    /**
     * 此数据域记录缓存是否已过期
     */
    private boolean bufferOutdated = false;

    /**
     * <p>绘制</p>
     * <p>此重载方法将根据缓存和图层重绘窗体</p>
     */
    @Override
    public void paint(Graphics g)
    {
        int i;

        if (this.bufferOutdated)
            this.buildBackBuffer();

        super.paint(g);

        g.drawImage(this.cacheImage, 0, 0, this);
        for (i=this.bufferLayer+1; i<=7; i++)
            if (this.imageLayer[i]!=null)
                g.drawImage(
                    this.imageLayer[i],
                    this.coordinate[i][0],
                    this.coordinate[i][1],
                    this
                );

        return;
    }

    /**
     * <p>裁剪</p>
     * <p>此方法将根据缓存和图层裁剪窗体, 将透明的像素镂空或去除.</p>
     */
    @Override
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

     * 详细请参见 static initalizeInstance() 的注释</p>
     * @param argIniFile 表示记录有初始化信息的 ini 文件
     * @param argHtImages 欲使用的图像库
     * @param argHtMasks 对应图像库的蒙版库
     * @return 春菜窗体的引用
     */
    public static UkagakaWin createUkagaka(String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        UkagakaWin newUkaWin = new UkagakaWin();

        newUkaWin.initalize(argIniFile, argHtImages, argHtMasks);

        return(newUkaWin);
    }

    /**
     * <p>命令UkagakaWin执行自初始化</p>
     * <p>事实上此方法调用
     * UkagakaWin.initalizeInstance(this, argIni, argHtImages, argHtMasks)
     * 执行实际的初始化操作</p>
     */
    public void initalize(String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        UkagakaWin.initalizeInstance(this, argIniFile, argHtImages, argHtMasks);
    }

    /**
     * <p>初始化春菜(指用于绘制春菜的窗体)</p>
     * <p>使用指定的 ini 文件中 ukagaka 段进行初始化<br>
     * <ul>有效的字段包括以下这些
     * <li>
     * <li>width=正整数 指定新春菜的宽度 (必须定义)
     * <li>height=正整数 指定新春菜的高度 (必须定义)
     * <li>image?=字符串 取自拟加载图像节任一带有图层号的键, 指定初始图层序列, ? = 0-7
     * </ul></p>
     * @param argIni 表示记录有初始化信息的 ini 文件
     * @param argHtImages 欲使用的图像库
     * @param argHtMasks 对应图像库的蒙版库

     */
    public static void initalizeInstance(UkagakaWin argUkaWin, String argIni, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        Hashtable<String, String> htInitInfo = JUkaUtility.iniReadSector(argIni, "ukagaka");
        argUkaWin.setImageLib(argHtImages, argHtMasks);

        int h = 0,w = 0,i;

        // 初始化
        // 大小
        if (htInitInfo.containsKey("width"))
            w = Integer.parseInt(htInitInfo.get("width"));
        if (htInitInfo.containsKey("height"))
            h = Integer.parseInt(htInitInfo.get("height"));
        argUkaWin.setSize(w,h);

        // 图层
        for (i=0; i<=7; i++)
            if (htInitInfo.containsKey("image"+i))
            {
                String tmpImageName = htInitInfo.get("image"+i);
                argUkaWin.setImageLayer(tmpImageName);
            }
        argUkaWin.buildBackBuffer();

        // 可拖动化
        argUkaWin.setDragable(true);

        // 去除标题栏
        argUkaWin.setUndecorated(true);

        return;
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
     * @param argDragSwitch 指示拖动功能的开关状态

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

  // Other | 杂项
    /**
     * 构造函数...不应该被显式调用
     */
    protected UkagakaWin()
    {
    }

    /**
     * @deprecated 此方法目前仅用于调试
     */
    public static void main(String[] args)
    {
        return;
    }
}
