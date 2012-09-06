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
  // Shell Management | Shell�������
    /**
     * ���������¼�����ļ�������
     */
    private static final File LIST_FILE = new File(JUkaUtility.getProgramPath() + "JUkaShell.list");

    /**
     * <p>��װ Shell ���, ���������</p>
     * <p>(!) �˷��������������������ǿ��ȫ��.<br>
     * �˷����Զ�������ջ����һ����Ϊ��ע�����, �ʱ����Դ�ע�����ֱ�ӵ��ø÷�
     * ��.</p>
     * @return <ul>����ֵ<li>0=�ɹ�<li>1=������Ѿ���װ<li>-1=��дʧ��
     * <li>-2=����ջ����<li>-3=�಻����</ul>
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
     * <p>ж�� Shell ���, ���������</p>
     * <p>(!) �˷��������������������ǿ��ȫ��.<br>
     * �˷����Զ�������ջ����һ����Ϊ��ж�����, �ʱ����Դ�ж�����ֱ�ӵ��ø÷�
     * ��.</p>
     * @return <ul>����ֵ<li>0=�ɹ�<li>1=����������б���<li>-1=��дʧ��
     * <li>-2=����ջ����<li>-3=�಻����</ul>
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
     * <p>�����Ѱ�װ�� Shell �б�</p>
     * <p>�˷����� JUkaStage �е�ͬ�������������ڴ˷������Ǵ��ļ���ȡ��.</p>
     * @rerurn �����Ѱ�װ�� Shell �б�
     */
    public static ArrayList<Class> getRegisteredShell()
    {
        return(JUkaComponentCtrl.readComponentListFile(JUkaShellCtrl.LIST_FILE));
    }

  // Images | ͼ����
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

        /* ��ͼ��ȡָ�� Shell ���ֶ�, ���ڹ����п��ܲ�����ȫ�� Exception
         * ����ֹ����, ������0.
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

        // �������� declaredCount ����
        scnCatalog.nextLine();
        // ѭ����ȡ��ӳ��ͼ����Դ
        for (i=1; i<=declaredCount; i++)
        {
            try
            {
                tmpString = scnCatalog.nextLine();
                tmpImage = Toolkit.getDefaultToolkit().getImage(JUkaUtility.getProgramPath() + tmpString);
                if ((tmpMask = JUkaShellCtrl.calculateMask(tmpImage)) == null)
                    throw new NullPointerException("�����ɰ�ʧ���� " + tmpString);
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

        // ׼��ͼ��
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

        // ��ȡ����
        PixelGrabber tmpPixelGrabber = new PixelGrabber(argImage, 0, 0, -1, -1, true);
        Rectangle rctLineBlock;
        Area rtMask = new Area();
        int xStart,height,width;
        int tmpPixels[];
        // ץȡ����
        try
        {
            tmpPixelGrabber.grabPixels();
        }
        catch (InterruptedException ex)
        {
            System.err.println(ex);
            return(null);
        }

        // ��������
        // ����ɨ�費͸�������ز�������뵽���� Area ��
        tmpPixels = (int[]) tmpPixelGrabber.getPixels();

        // �������ȫ�����صĴ���, ������
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

  // Other | ����
    /**
     * @deprecated �˷���Ŀǰ�����ڵ���
     */
    public static void main(String[] args)
    {

        return;
    }
}
