/**
 * @author "Galin"<cuter44@qq.com>
 * @version v120827
 */

package jukagaka.shell;

import jukagaka.*;
import jukagaka.shell.*;

import java.io.File;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.image.PixelGrabber;
import java.awt.MediaTracker;
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;
import java.lang.reflect.Field;
import javax.swing.JDialog;

public class JUkaShell extends JUkaComponent implements Serializable
{
  // Static Data (NEED REWRITE) | 待重写静态数据域
    // 请将这些数据域复制到你重写的子类中, 并按需要修改其中的字符串内容.
    // 非字符串值及数据域签名请勿改动.

    /**
     * <p>此数据域记录 Shell 的静态参数存储, 也就是 ini 文件的位置.</p>
     * <p>Shell 的大部分静态参数(例如春菜的名字, 图像资源列表等)均存储在此 ini
     * 文件中, 通过这种方式存储是为了在便利性和灵活性上取得均衡, 某些 API 会要
     * 求以整个 Shell 类来作为传入参数, 一般它们将通过此域来自行获取参数, 以
     * 简化参数列, 并降低调用者的编程负担.<br>
     * (*) 此类的扩展者应重写这个域</p>
     */
    public static final String DEFAULT_INI = JUkaUtility.getProgramDir() + "/defaultShell.ini";

    /**
     * <p>hashImages 和 hashShape 数据域用于挂载缓冲的图像资源</p>
     * <p>(!) static 域无法被继承, 请以相同签名在子类内重写</p>
     * <p>此二数据域由 JUkaShellCtrl.prefetchImageResource() 负责写入.
     * 之所以定义为 public static 是为了高效读写, 这样做违反了封装原则,
     * 因为存在被其它代码非法修改的可能性, 但我相信任何认真做事的人都会遵守
     * 游戏规则:<br>
     * 读操作: 请随意<br>
     * 赋值操作: 绝对禁止<br>
     * clear()操作: 危险请慎用<br>
     * put()/remove()操作: 请确保不会内存泄漏</p>
     */
    public static Hashtable<String, Image> hashImages = null;
    public static Hashtable<String, Area> hashMasks = null;

    ///**
     //* <p></p>
     //*/
    //public static String serialImageResourcePath = null;
    //public static String serialImageResourceSector = null;

  // Instance Data | 实例数据域
    /**
     * 此数据域记录当前可用的气球队列
     */
    transient private ArrayList<JUkaWindow> winList = null;
    /**
     * 此数据域记录承载春菜的 Frame
     */
    private UkagakaWin ukagakaWin = null;
    /**
     * 此数据域记录主菜单的 Frame
     */
    transient private BalloonWin mainBalloon = null;
    /**
     * 此数据域标记 Shell 是否已经废弃
     */
    transient private boolean discarded = false;

  // Install/Uninstall | 安装/卸载
    // 请按照要求重写这些方法.
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

  // Images Manipulate | 图像处理
    /**
     * <p>为 Shell 加载图像</p>
     * <p>此方法根据指定的列表将图像及其蒙版加载到指定类的哈希表中, 以备绘图使用.<br>
     * 方法从 argIniFile 中的 argSector 节中顺序读取整个节的键-值对, 并根据<br>
     * 其中包含的信息进行加载, 一个合法的键值对可以是一下列出的这些</p>
     * <p>alterName,[layer],[x],[y]=fileName</p>
     * <p>或</p>
     * <p>alterName=fileName</p>
     * <p>其中[]表示可省略的值, layer 表示图层号, x,y 分别表示相对坐标. fileName
     * 为文件路径, 以对于 JUkaUtility.getProgramDir() 的 UNIX 相对路径表示.
     * 这两种格式前者用于 UkagakaWin 的绘图, 后者用于 BalloonWin 的绘图.
     * 当然两者可以混用但有时需要遵守某些额外的约定<br>
     * 不建议使用前者的省略形式, 虽然解析器被设计成可以兼容这种模式但不一定会
     * 产生预期的结果. 省略的参数会被以默认值(具体多少是使用环境而定)代替.</p>
     * <p>方法首先会获取指定 Shell 类的 hashImages 和 hashMasks 域, 并根据
     * overwrite 的值决定是否排空这两个哈希表(注意是排空而不是新建一个哈希表).
     * 如果此二数据域的值为 null 则自动新建并赋值到所述数据域中, 此时 overwrite
     * 的值不起作用<br>
     * 虽然允许以 alterName1,[layer],x,y=anotherKey 的形式进行映射, 但除非你清楚
     * 自己要做什么, 否则建议不要这样做, 盖因anotherKey中蒙版的 x,y 平移量会被累计,
     * 而原始图像不会, 这可能会导致裁剪窗口时出现问题.<br>
     * 注意此方法将等号前的部分而不仅仅是 alterName 作为哈希映射的键(key).</p>
     * @param argShell 指定要为之缓冲图像的 Shell 类
     * @param argIniFile 指定要从中读取的ini文件, 以 UNIX 绝对路径表示.
     * @param argSector 指定要读取的节
     * @param overwrite 为 true 时在读取前清空哈希表, 否则跳过此步骤
     */
    public static void prefetchImageResource(Class argShell, String argIniFile, String argSector, boolean overwrite)
    {
        Field fieldHashImages = null;
        Field fieldHashMasks = null;
        String[] buffer = null;
        Hashtable<String, Image> htImages = null;
        Hashtable<String, Area> htMasks = null;

        /* 试图获取指定字段, 对于过程中可能产生的全部 Exception
         * 将中止处理, 并返回0.
         */
        try
        {
            fieldHashImages = argShell.getField("hashImages");
            fieldHashMasks = argShell.getField("hashMasks");
            if ( (htImages = (Hashtable<String,Image>)fieldHashImages.get(null)) == null)
                fieldHashImages.set(null, htImages = new Hashtable<String,Image>(48));
            else
                if (overwrite)
                    htImages.clear();
            if ( (htMasks = (Hashtable<String,Area>)fieldHashMasks.get(null)) == null)
                fieldHashMasks.set(null, htMasks = new Hashtable<String,Area>(48));
            else
                if (overwrite)
                    htMasks.clear();
        }
        catch(Exception ex)
        {
            System.err.println(ex);
            return;
        }

        Scanner iniScanner = JUkaUtility.iniLocateSector(argIniFile, argSector);

        Image tmpImage = null;
        Area tmpMask = null;

        while ((buffer=JUkaUtility.iniEnumSector(iniScanner)) != null)
        {
            // 判重并提取图像
            if (htImages.containsKey(buffer[1]))
            {
                tmpImage = htImages.get(buffer[1]);
                tmpMask = htMasks.get(buffer[1]);
            }
            else
            {
                tmpImage = Toolkit.getDefaultToolkit().getImage(JUkaUtility.getProgramDir() + buffer[1]);
                if ((tmpMask = JUkaShell.calculateMask(tmpImage)) == null)
                {
                    System.err.println("Error: JUkaShell.prefetchImageResource(): 计算蒙版失败, 图像可能不正确: " + buffer[1]);
                    continue;
                }
                htImages.put(buffer[1], tmpImage);
                htMasks.put(buffer[1], tmpMask);
            }

            float x,y;

            // 提取平移分量
            // 如 Key 的格式不正确则自动以无平移加载(兼容旧格式和气球背景)
            try
            {
                String[] separatedKey = buffer[0].split(",");
                x = Float.parseFloat(separatedKey[2]);
                y = Float.parseFloat(separatedKey[3]);
            }
            catch (Exception ex)
            {
                System.err.println(ex);
                x = 0;
                y = 0;
            }

            htImages.put(buffer[0], tmpImage);
            // 计算平移
            if ((x!=0) || (y!=0))
                tmpMask = tmpMask.createTransformedArea(
                    new AffineTransform(
                        1, 0, // ScaleX, SheerY
                        0, 1, // SheerX, ScaleY
                        x, y) // TransXY
                );
            htMasks.put(buffer[0], tmpMask);
        }

        return;
    }

    /**
     * <p>此方法以行扫描的形式计算参数图像的蒙版, 也就是图像中不透明像素的区域</p>
     */
    public static Area calculateMask(Image argImage)
    {
        int i,j,x=0,y=0;

        // 准备图像
        try
        {
            MediaTracker tmpMediaTracker=new MediaTracker(JUkaShell.reservedWin);
            tmpMediaTracker.addImage(argImage, 0);
            tmpMediaTracker.waitForAll();
        }
        catch (InterruptedException ex)
        {
            System.out.println(ex);
            return(null);
        }

        // 获取轮廓
        PixelGrabber tmpPixelGrabber = new PixelGrabber(argImage, 0, 0, -1, -1, true);
        Rectangle rctLineBlock;
        Area rtMask = new Area();
        int xStart,height,width;
        int tmpPixels[];

        // 抓取像素
        try
        {
            tmpPixelGrabber.grabPixels();
        }
        catch (InterruptedException ex)
        {
            System.err.println(ex);
            return(null);
        }

        // 导出像素
        // 按行扫描不透明的像素并将其加入到轮廓 Area 中
        tmpPixels = (int[]) tmpPixelGrabber.getPixels();

        // 用于输出全部像素的代码, 调试用
        //System.out.println(tmpPixels.length);
        //for (i=0; i<tmpPixels.length; i++)
            //System.out.print(tmpPixels[i] + " ");
        //System.out.println();

        height = argImage.getHeight(JUkaShell.reservedWin);
        width = argImage.getWidth(JUkaShell.reservedWin);

        // 不能加载图像时
        if (height == 0)
            return(null);

        for (y = 0; y < height; y++)
        {
            xStart = 0;
            for (x = 0; x < width; x++)
            {
                if ((tmpPixels[y * width + x] & 0xFF000000) == 0)
                {
                    if (xStart != 0)
                    {
                        rctLineBlock = new Rectangle(xStart, y, x - xStart, 1);
                        rtMask.add(new Area(rctLineBlock));
                        xStart = 0;
                    }
                }
                else if (xStart == 0)
                    xStart = x;
            }
            if (((tmpPixels[(y+1) * width - 1] & 0xFF000000) != 0)
                && (xStart!=0))
            {
                rctLineBlock = new Rectangle(xStart, y, width - xStart, 1);
                rtMask.add(new Area(rctLineBlock));
                xStart = 0;
            }
        }

        return(rtMask);
    }

  // Shell Constructor & Destructor | Shell 构造/析构
    // 请按照要求重写这些方法.
    /**
     * <p>此方法用于产生一个新的 Shell</p>
     * <p>传入的参数将以引用(而非克隆)被存储于生成的 Wins 中, 也就是说对图像库
     * 所作的修改会自动反映到窗体中.<br>
     * (*) 此类的扩展者应以 public 重写方法, 并在自撰的代码<b>前</b>调用父类的方法.
     *     或者什么也不做以直接使用缺省的方法</p>
     * @deprecated 由于Java只支持cast而不支持expand特性, 导致不可能原封地将父类对象
     *             转换为子类, 因此只能更改策略, 使用initalizeShell()更换之.
     */
    public static JUkaShell createShell(String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        return(null);
    }

    public static JUkaShell initalizeShell(JUkaShell argShell, String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        argShell.initalizeUkagaka(argIniFile, argHtImages, argHtMasks);
        argShell.initalizeMainBalloon(argIniFile, argHtImages, argHtMasks);
        argShell.initalizeWinList();

        return(argShell);
    }

    /**
     * 待扩展
     */
    public void initalizeUkagaka(String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        this.ukagakaWin = UkagakaWin.createUkagaka(argIniFile, argHtImages, argHtMasks);

        return;
    }

    /**
     * 待扩展
     */
    protected void initalizeMainBalloon(String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        this.mainBalloon = BalloonWin.createBalloon(argIniFile, argHtImages, argHtMasks);

        return;
    }

    /**
     * 待扩展
     */
    public void initalizeWinList()
    {
        this.winList = new ArrayList<JUkaWindow>(16);
        this.winList.add(this.ukagakaWin);
        this.winList.add(this.mainBalloon);

        return;
    }

    // (!) 此API已被取消
    ///**
     //* <p>命令 Shell 自初始化</p>
     //* <p>事实上此方法调用
     //* JUkaShell.initalizeInstance(this, argIni, argHtImages, argHtMasks)
     //* 执行实际的初始化操作</p>
     //*/
    //protected void initalize(String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    //{
        //JUkaShell.initalize(this. argIniFile, argHtImages, argHtMasks);
    //}

    // (!) 此API已被取消
    ///**
     //* <p>对 Shell 对象进行基础初始化</p>
     //*/
    //protected static JUkaShell initalizeInstance(JUkaShell argShell, String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    //{
        //// 初始化窗体队列
        //argShell.winList = new ArrayList<JUkaWindow>(16);
        //// 初始化 UkagakaWin
        //argShell.ukagakaWin = UkagakaWin.createUkagaka(argIniFile, argHtImages, argHtMasks);
        //argShell.winList.add(argShell.ukagakaWin);
        //// 初始化 BalloonWin
        //argShell.mainBalloon = BalloonWin.createBalloon(argIniFile, argHtImages, argHtMasks);
        //argShell.winList.add(argShell.mainBalloon);

        //return(argShell);
    //}


    /**
     * <p>此方法用于弃置一个 Shell 使之不再有效</p>
     * <p>弃置 Shell 是指将其从当前运行环境中销毁, <span color="red">而不是指
     * 将春菜从记录中不可逆地消除.</span><br>
     * 此操作将销毁 Shell 占用的显示资源, 并将其标示为不可用(isDiscarded() 方法
     * 返回 false)<br>
     * 任何对已弃置 Shell 的操作请求都将被驳回或无效.<br>
     * (*) 此类的扩展者应按照该签名重写方法, 并在自撰的代码<b>后</b>调用父类的方法.
     *     或者什么也不做以直接使用缺省的方法</p>
     */
    protected static void discardShell(JUkaShell argShell)
    {

        argShell.discarded = true;
        return;
    }

    /**
     * <p>此方法用于回收 Shell 使之不再有效</p>
     * <p>此方法只是单纯地调用 JUkaShell.destroyShell(this)<br>
     * (*) 建议此类的扩展者应以 public 重写方法, 并在自撰的代码<b>后</b>调用 super.discard()</p>
     */
    protected void discard()
    {
        JUkaShell.discardShell(this);
        return;
    }

    /**
     * <p>此方法返回 Shell 的 discarded 域, 以指示该 Shell 是否已经弃置</p>
     * <p>弃置的 shell 是指已经通过 JUkaShell.destroyShell() 处理的 Shell.</p>
     * @return 如果 Shell 已弃置, 则返回 true.
     */
    public boolean isDiscarded()
    {
        return(this.discarded);
    }

  // Ukagaka Manipulate | 春菜操纵命令
    /* 基于 JLS 规范以及锁定机能的需要对 ukagakaWin 进行屏蔽,
     * 除了第一个命令外, 其余命令将被转发到 JUkaShell.ukagakaWin 的同名方法上执行.
     * 此类的扩展者可以重写并以 super.methodName() 的方式调用以增加自己设定的限制.
     * 当然也可以放着不重写, 根据 JLS 的动态绑定设定, 这些方法依旧会被执行.
     */
    /**
     * <p>获取 UkagakaWin 的引用</p>
     * <p>(!) 注意通过此方法可以绕过封装直接操控春菜, 这可能做成破坏性后果,
     * 因此请不要处于调试以外的目的使用此方法.</p>
     */
    public UkagakaWin getUkagaka()
    {
        return(this.ukagakaWin);
    }

    public boolean setImageLayer(String argHashKey, int argLayer, int x, int y, int accessKey)
    {
        if (!this.checkAuthority(accessKey))
            return(false);

        return(this.ukagakaWin.setImageLayer(argHashKey, argLayer, x, y));
    }

    public boolean setImageLayer(String argHashKey, int accessKey)
    {
        if (!this.checkAuthority(accessKey))
            return(false);

        return(this.ukagakaWin.setImageLayer(argHashKey));
    }

    public boolean setImageBatch(String[] arrHashKeys, int accessKey)
    {
        if (!this.checkAuthority(accessKey))
            return(false);

        return(this.ukagakaWin.setImageBatch(arrHashKeys));
    }

    public boolean setImageBatch(String arrHashKeys, int accessKey)
    {
        if (!this.checkAuthority(accessKey))
            return(false);

        return(this.ukagakaWin.setImageBatch(arrHashKeys));
    }

    public int getBufferLayer()
    {
        return(this.ukagakaWin.getBufferLayer());
    }

    public int setBufferLayer(int newBufferLayer)
    {
        return(this.ukagakaWin.setBufferLayer(newBufferLayer));
    }

    public void buildBackBuffer()
    {
        this.ukagakaWin.buildBackBuffer();
        return;
    }

  // Ukagaka Monopoly | 独占使用(吐槽坑爹的爱词霸)
    /**
     * 此数据域记录目前使用中的 Key
     */
    private int preservedKey = 0;

    /**
     * <p>锁定春菜以获得其专用权</p>
     * <p>此类的扩展者应以 public 覆盖此函数, 并在自撰的代码<strong>后</strong>
     * 调用此方法.</p>
     */
    protected int lockUkagaka()
    {
        // 已锁定时报错
        if (this.preservedKey != 0)
            return(0xFFFFFFFF);

        // 生成密钥
        do
            this.preservedKey = new Double(Math.random() * 0x7FFFFFFF).intValue();
        while (this.preservedKey <= 0);

        return(this.preservedKey);
    }

    /**
     * <p>解锁伪春菜以去除其专用保护</p>
     * <p>由于不依赖密钥解锁, 所以这个方法总是成功并且返回 true 的, 这意味着
     * 可以抢占 Shell, 而且不会有栈信息以还原抢占... 原占有者也不会被通知<br>
     * 此类的扩展者应该以 public 覆盖此方法, 并在自撰的代码<strong>后</strong>
     * 调用此方法</p>
     */
    protected boolean unlockUkagaka()
    {
        this.preservedKey = 0;

        return(true);
    }

    /**
     * <p>此方法判定给定的密钥能否获得春菜的占用权</p>
     */
    public boolean checkAuthority(int key)
    {
        if (this.preservedKey == 0)
            return(true);

        return((key ^ this.preservedKey) == 0);
    }

    // Balloon Manipulate | 气球操纵指令
    //   ∧
    //  /｜\  TODO: 工事中...
    // /_＾_\

  // General for Ukagaka & Balloon | Win 通用操纵命令
    /**
     * <p>要求重绘指定的窗体</p>
     */
    public void fireRepaint(int argWinID)
    {
        try
        {
            this.fireRepaint(this.winList.get(argWinID));
        }
        catch (IndexOutOfBoundsException ex)
        {
            System.err.println(ex);
            System.err.println("Error: JUkaShell.fireReprint(): 无效下标索引, 函数已退出");
            return;
        }

        return;
    }

    /**
     * <p>要求重绘指定的窗体</p>
     */
    public boolean fireRepaint(JUkaWindow argWin)
    {
        if (argWin == null)
        {
            System.err.println("Error: JUkaShell.fireRepaint(): 空指针参数, 函数已退出");
            return(false);
        }

        if (!this.winList.contains(argWin))
        {
            System.err.println("Warn: JUkaShell.fireRepaint(): 指定窗体未包含在列表中");
            return(false);
        }

        argWin.repaint();

        return(true);
    }

    /**
     * <p>要求裁剪指定的窗体</p>
     */
    public void fireClip(int argWinID)
    {
        try
        {
            this.fireClip(this.winList.get(argWinID));
        }
        catch (IndexOutOfBoundsException ex)
        {
            System.err.println(ex);
            System.err.println("Error: JUkaShell.fireClip(): 无效下标索引, 函数已退出");
            return;
        }

        return;
    }

    /**
     * <p>要求裁剪指定的窗体</p>
     */
    public boolean fireClip(JUkaWindow argWin)
    {
        if (argWin == null)
        {
            System.err.println("Error: JUkaShell.fireClip(): 空指针参数, 函数已退出");
            return(false);
        }

        if (!this.winList.contains(argWin))
        {
            System.err.println("Warn: JUkaShell.fireClip(): 指定窗体未包含在列表中");
            return(false);
        }

        argWin.clip();

        return(true);
    }

  // Trigger | 触发器
    /**
     * <p>此方法目前不包含任何代码, 仅仅作为 token 存在.</p>
     * <p>子类的该方法会在 JUkagaka 启动时被调用, 组件可以趁此完成初始化工作<br>
     * 如果子类要表示不能正常加载, 抛出任意 Exception 即可.<br>
     * (*) 此类的扩展这应以 public 重写方法, 并在自撰的代码<strong>前</strong>调用此方法<br></p>
     */
    protected static void onLoad()
    {

        return;
    }

    /**
     * <p>此方法用于退出时调用</p>
     * <p>子类的该方法会在 JUkagaka 退出前被调用, 组件可以趁此完成退出前工作<br>
     * 如果子类要表示不能正常加载, 抛出任意 Exception 即可.<br>
     * (*) 此类的扩展这应按照该签名重写方法, 并在自撰的代码<b>后</b>调用父类的方法<br></p>
     */
    protected static void onExit()
    {
        return;
    }

  // Other | 杂项
    /**
     * <p>目前这个窗体还没有用途, 仅用于消除模块依赖</p>
     */
    static JDialog reservedWin = new JDialog();

    /**
     * 构造函数...不应该被显式调用
     */
    protected JUkaShell()
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

