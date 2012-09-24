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
  // Shell Management | Shell�������
    /**
     * ���������¼�����ļ�������
     */
    private static final File LIST_FILE = new File(JUkaUtility.getProgramPath() + "shell/JUkaShell.list");

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
     * <p>Ϊ Shell ����ͼ��</p>
     * <p>�˷�������ָ�����б�ͼ�����ɰ���ص�ָ����Ĺ�ϣ����, �Ա���ͼʹ��.<br>
     * ������ argIniFile �е� argSector ����˳���ȡ�����ڵļ�-ֵ��, ������<br>
     * ���а�������Ϣ���м���, һ���Ϸ��ļ�ֵ�Կ�����һ���г�����Щ</p>
     * <p>alterName,[layer],[x],[y]=fileName</p>
     * <p>��</p>
     * <p>alterName=fileName</p>
     * <p>����[]��ʾ��ʡ�Ե�ֵ, layer ��ʾͼ���, x,y �ֱ��ʾ�������. fileName
     * Ϊ�ļ�·��, �Զ��� JUkaUtility.getProgramPath() �� UNIX ���·����ʾ.
     * �����ָ�ʽǰ������ UkagakaWin �Ļ�ͼ, �������� BalloonWin �Ļ�ͼ.
     * ��Ȼ���߿��Ի��õ���ʱ��Ҫ����ĳЩ�����Լ��<br>
     * ������ʹ��ǰ�ߵ�ʡ����ʽ, ��Ȼ����������Ƴɿ��Լ�������ģʽ����һ����
     * ����Ԥ�ڵĽ��. ʡ�ԵĲ����ᱻ��Ĭ��ֵ(���������ʹ�û�������)����.</p>
     * <p>�������Ȼ��ȡָ�� Shell ��� hashImages �� hashMasks ��, ������
     * overwrite ��ֵ�����Ƿ��ſ���������ϣ��(ע�����ſն������½�һ����ϣ��).
     * ����˶��������ֵΪ null ���Զ��½�����ֵ��������������, ��ʱ overwrite
     * ��ֵ��������<br>
     * ��Ȼ������ alterName1,[layer],x,y=alterName2 ����ʽ����ӳ��, ��������֪��
     * �Լ�Ҫ��ʲô, �����鲻Ҫ������, �����ɰ�� x,y λ�ƻᱻ�ۼ�(�����ԭ
     * ͼ��), ����ܻᵼ�²ü�����ʱ��������.<br>
     * ע��˷������Ⱥ�ǰ�Ĳ��ֶ��������� alterName ��Ϊ��ϣӳ��ļ�.</p>
     * @param argShell ָ��ҪΪ֮����ͼ��� Shell ��
     * @param argIniFile ָ��Ҫ���ж�ȡ��ini�ļ�, �Զ��� JUkaUtility.getProgramPath() �� UNIX ���·����ʾ.
     * @param argSector ָ��Ҫ��ȡ�Ľ�
     * @param overwrite Ϊ true ʱ�ڶ�ȡǰ��չ�ϣ��, ���������˲���
     */
    public static void prefetchImageResource(Class argShell, String argIniFile, String argSector, boolean overwrite)
    {
        Field fieldHashImages = null;
        Field fieldHashMasks = null;
        String[] buffer = null;
        Hashtable<String, Image> htImages = null;
        Hashtable<String, Area> htMasks = null;

        /* ��ͼ��ȡָ���ֶ�, ���ڹ����п��ܲ�����ȫ�� Exception
         * ����ֹ����, ������0.
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
            // ���ز���ȡͼ��
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
                    System.err.println("�����ɰ�ʧ��, ͼ����ܲ���ȷ: " + buffer[1]);
                    continue;
                }
                htImages.put(buffer[1], tmpImage);
                htMasks.put(buffer[1], tmpMask);
            }

            float x,y;

            // ��ȡƽ�Ʒ���
            // �� Key �ĸ�ʽ����ȷ���Զ�����ƽ�Ƽ���(���ݾɸ�ʽ�����򱳾�)
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
            // ����ƽ��
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

        // ׼��ͼ��
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

        // ���ܼ���ͼ��ʱ
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

  // Other | ����
    /**
     * @deprecated �˷���Ŀǰ�����ڵ���
     */
    public static void main(String[] args)
    {

        return;
    }
}
