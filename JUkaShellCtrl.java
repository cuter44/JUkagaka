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
import javax.swing.JLabel;

public class JUkaShellCtrl extends JUkaComponentCtrl
{
  // Shell Management | Shell组件管理
    /**
     * 此数据域记录数据文件的名字
     */
    private static final File LIST_FILE = new File(JUkaUtility.getProgramPath() + "JUkaShell.list");

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
     *
     */
    public static int prefetchImageResource(Class argShell)
    {
        Field fieldCatalog = null;
        Field fieldHashImages = null;
        Field fieldHashMasks = null;
        File catalogFile = null;
        Scanner scnCatalog = null;
        Hashtable htImages = new Hashtable<String, Image>(48);
        Hashtable htMasks = new Hashtable<String, Area>(48);

        /* 试图获取指定 Shell 的字段, 对于过程中可能产生的全部 Exception
         * 将中止处理, 并返回0.
         */
        try
        {
            fieldCatalog = argShell.getField("IMAGE_CATALOG");
            fieldHashImages = argShell.getField("hashImages");
            fieldHashMasks = argShell.getField("hashMasks");
            catalogFile = (File)fieldCatalog.get(null);
            scnCatalog = new Scanner(catalogFile);
            fieldHashImages.set(null, htImages);
            fieldHashMasks.set(null, htMasks);
        }
        catch(Exception ex)
        {
            System.err.println(ex);
            return(0);
        }

        int declaredCount = scnCatalog.nextInt();
        int actualCount = 0;
        int i;
        String tmpString;
        Image tmpImage = null;
        Area tmpMask = null;

        // 跳过包含 declaredCount 的行
        scnCatalog.nextLine();
        // 循环读取及映射图像资源
        for (i=1; i<=declaredCount; i++)
        {
            try
            {
                tmpString = scnCatalog.nextLine();
                tmpImage = Toolkit.getDefaultToolkit().getImage(JUkaUtility.getProgramPath() + tmpString);
                if ((tmpMask = JUkaShellCtrl.calculateMask(tmpImage)) == null)
                    throw new NullPointerException("计算蒙版失败于 " + tmpString);
                htImages.put(tmpString, tmpImage);
                htMasks.put(tmpString, tmpMask);
                while (true)
                {
                    tmpString = scnCatalog.nextLine();
                    if (tmpString.equals("-"))
                        break;
                    htImages.put(tmpString, tmpImage);
                    htMasks.put(tmpString, tmpMask);
                }

                actualCount++;
            }
            catch (Exception ex)
            {
                System.err.println(ex);
            }
        }

        return(actualCount);
    }

    public static Area calculateMask(Image argImage)
    {
        int i,j,x=0,y=0;

        // 准备图像
        try
        {
            MediaTracker tmpMediaTracker=new MediaTracker(new JLabel());
            // note: Argument should be chenged to something else.
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
        try
        {
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
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            System.err.println(ex);
            System.err.println("OutOfIndex Error(x,y): " + x + "," + y);
            return(null);
        }

        return(rtMask);
    }

  // Other | 其他
    /**
     * @deprecated 此方法目前仅用于调试
     */
    public static void main(String[] args)
    {

        return;
    }
}
