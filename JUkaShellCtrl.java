/**
 * @version v120826
 * @author "Galin"<cuter44@qq.com>
 */

package jukagaka;

import jukagaka.shell.*;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Hashtable;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.image.PixelGrabber;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;
import javax.swing.JLabel;

public class JUkaShellCtrl extends JUkaComponentCtrl
{
  // Shell Management | Shell组件管理
    /**
     * 此数据域记录数据文件的名字
     */
    private static final File LIST_FILE = new File(JUkaUtility.getProgramPath() + "shell/JUkaShell.list");

    /**
     * <p>安装 Shell 组件, 由子类调用</p>
     * <p>(!) 此方法不包含传入参数以增强安全性.<br>
     * 此方法自动将运行栈的上一层作为待注册组件, 故必须以待注册组件直接调用该方
     * 法.</p>
     * @return <ul>返回值<li>0=成功<li>1=该组件已经安装<li>-1=读写失败
     * <li>-2=运行栈上溢<li>-3=类不存在</ul>
     */
    public static int installComponent()
    {
        int i;
        StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        String classJUkaShell = new JUkaShellCtrl().getClass().getName();

        for (i=1; i<stack.length; i++)
            if (classJUkaShell.equals(stack[i].getClassName()))
                break;
        try
        {
            return(JUkaComponentCtrl.installComponent(Class.forName(stack[i+1].getClassName()), JUkaShellCtrl.LIST_FILE));
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            System.err.println(ex);
            return(-2);
        }
        catch (ClassNotFoundException ex)
        {
            System.err.println(ex);
            return(-3);
        }
    }

    /**
     * <p>卸载 Shell 组件, 由子类调用</p>
     * <p>(!) 此方法不包含传入参数以增强安全性.<br>
     * 此方法自动将运行栈的上一层作为待卸载组件, 故必须以待卸载组件直接调用该方
     * 法.</p>
     * @return <ul>返回值<li>0=成功<li>1=该组件不在列表中<li>-1=读写失败
     * <li>-2=运行栈上溢<li>-3=类不存在</ul>
     */
    public static int uninstallComponent()
    {
        int i;
        StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        String classJUkaShell = new JUkaShellCtrl().getClass().getName();

        for (i=1; i<stack.length; i++)
            if (classJUkaShell.equals(stack[i].getClassName()))
                break;
        try
        {
            return(JUkaComponentCtrl.uninstallComponent(Class.forName(stack[i+1].getClassName()), JUkaShellCtrl.LIST_FILE));
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            System.err.println(ex);
            return(-2);
        }
        catch (ClassNotFoundException ex)
        {
            System.err.println(ex);
            return(-3);
        }
    }

    /**
     * <p>返回已安装的 Shell 列表</p>
     * <p>此方法与 JUkaStage 中的同名方法区别在于此方法总是从文件读取的.</p>
     * @rerurn 返回已安装的 Shell 列表
     */
    public static ArrayList<Class> getRegisteredShell()
    {
        return(JUkaComponentCtrl.readComponentListFile(JUkaShellCtrl.LIST_FILE));
    }

  // Images | 图像处理
    /**
     * <p>为 Shell 加载图像</p>
     * <p>此方法根据指定的列表将图像及其蒙版加载到指定类的哈希表中, 以备绘图使用.<br>
     * 方法从 argIniFile 中的 argSector 节中顺序读取整个节的键-值对, 并根据<br>
     * 其中包含的信息进行加载, 一个合法的键值对可以是一下列出的这些</p>
     * <p>alterName,[layer],[x],[y]=fileName</p>
     * <p>或</p>
     * <p>alterName=fileName</p>
     * <p>其中[]表示可省略的值, layer 表示图层号, x,y 分别表示相对坐标. fileName
     * 为文件路径, 以对于 JUkaUtility.getProgramPath() 的 UNIX 相对路径表示.
     * 这两种格式前者用于 UkagakaWin 的绘图, 后者用于 BalloonWin 的绘图.
     * 当然两者可以混用但有时需要遵守某些额外的约定<br>
     * 不建议使用前者的省略形式, 虽然解析器被设计成可以兼容这种模式但不一定会
     * 产生预期的结果. 省略的参数会被以默认值(具体多少是使用环境而定)代替.</p>
     * <p>方法首先会获取指定 Shell 类的 hashImages 和 hashMasks 域, 并根据
     * overwrite 的值决定是否排空这两个哈希表(注意是排空而不是新建一个哈希表).
     * 如果此二数据域的值为 null 则自动新建并赋值到所述数据域中, 此时 overwrite
     * 的值不起作用<br>
     * 虽然允许以 alterName1,[layer],x,y=alterName2 的形式进行映射, 但除非你知道
     * 自己要做什么, 否则建议不要这样做, 盖因蒙版的 x,y 位移会被累计(相对于原
     * 图像), 这可能会导致裁剪窗口时出现问题.<br>
     * 注意此方法将等号前的部分而不仅仅是 alterName 作为哈希映射的键.</p>
     * @param argShell 指定要为之缓冲图像的 Shell 类
     * @param argIniFile 指定要从中读取的ini文件, 以对于 JUkaUtility.getProgramPath() 的 UNIX 相对路径表示.
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
                tmpImage = Toolkit.getDefaultToolkit().getImage(JUkaUtility.getProgramPath() + buffer[1]);
                if ((tmpMask = JUkaShellCtrl.calculateMask(tmpImage)) == null)
                {
                    System.err.println("计算蒙版失败, 图像可能不正确: " + buffer[1]);
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
            System.out.println(tmpMask.isEmpty());
            htMasks.put(buffer[0], tmpMask);
        }

        return;
    }

    public static Area calculateMask(Image argImage)
    {
        int i,j,x=0,y=0;

        // 准备图像
        try
        {
            MediaTracker tmpMediaTracker=new MediaTracker(new JLabel());
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

        height = argImage.getHeight(JUkaStage.eventListener);
        width = argImage.getWidth(JUkaStage.eventListener);

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

  // Other | 杂项
    /**
     * @deprecated 此方法目前仅用于调试
     */
    public static void main(String[] args)
    {

        return;
    }
}
