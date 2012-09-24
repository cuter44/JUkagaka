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
  // Images & painting | ͼ�񼰻��ƹ���
    // ���������򼰷����ݲ��ṩ
    //private Hashtable<String, Image> htImages = null;
    //private Hashtable<String, Area> htMasks = null;

    ///**
     //* <p>�趨�µ�ͼ�������</p>
     //* <p>�˷����ڵ��� createUkagaka() ʱ���Զ�������, ͨ������Ҫ�ֶ��ص���֮</p>
     //*/
    //public void setImageLib(Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    //{
        //this.htImages = argHtImages;
        //this.htMasks = argHtMasks;

        //return;
    //}

    /**
     * �˶��������¼ԭʼ(����ǰ)��ͼ�μ��ɰ�����
     */
    private Image originImage = null;
    private Area originMask = null;

    /**
     * <p>ʹ��ָ���ı���ͼ���ɰ�</p>
     * <p>�޸Ľ��ڴ�����һ���ػ�ʱ��Ч</p>
     */
    public void setBkImage(Image argImage, Area argMask)
    {
        this.originImage = argImage;
        this.originMask = argMask;
        this.transOutdated = true;
        return;
    }

    ///**
     //* <p>�ӿ��г�ȡ��Ӧ argHashKey �ı���ͼ���ɰ�</p>
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

  // paint | ����
    /**
     * ��ǻ�����Ч��
     */
    private boolean transOutdated = false;

    /**
     * �˶��ֶμ�¼���ź��ͼ�񻺴�
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

        // ���·���Ĵ���, ������
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

  // Create/Destroy | ����/����
    /**
     * <p>���ɲ�����һ���հ׵�����</p>
     * <p>���ɵ����򽫱�ָ���� ini �ļ��� balloon ��Ԥ��ʼ��<br>
     * <ul>��Ч���ֶ���������Щ
     * <li>width=������ ��ʾ������Ŀ��(���鶨��)
     * <li>height=������ ��ʾ������ĸ߶�(���鶨��)
     * <li>background=�ַ��� ȡ�������ͼ�����һ����ͼ��ŵļ�, ��ʾ������ı�������<br>
     *     ����������Զ������ŵ���������. ������Ų���ʽ���ڿ�����.
     * </ul></p>
     * @param argIni ��ʾ��¼�г�ʼ����Ϣ�� ini �ļ�
     * @return һ���հ׵���������
     */
    public static BalloonWin createBalloon(String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        BalloonWin newBalloon = new BalloonWin();
        Hashtable<String, String> htInitInfo = JUkaUtility.iniReadSector(argIniFile, "balloon");

        int h = 0,w = 0;

        // ��ʼ��
        // ��С
        if (htInitInfo.containsKey("width"))
            w = Integer.parseInt(htInitInfo.get("width"));
        if (htInitInfo.containsKey("height"))
            h = Integer.parseInt(htInitInfo.get("height"));
        newBalloon.setSize(w,h);

        // ����
        if (htInitInfo.containsKey("background"))
            newBalloon.setBkImage(argHtImages.get(htInitInfo.get("background")), argHtMasks.get(htInitInfo.get("background")));

        // ȥ������
        newBalloon.setUndecorated(true);

        // ���϶�
        newBalloon.setDragable();

        return(newBalloon);
    }

    /**
     * <p>�˷���ʹ�����ÿ��϶�</p>
     * <p>����һ�μ���. <br>
     * (!)�˹��̲�����, Ҳ����˵, ������ʹ�����±�ò����϶�</p>
     */
  // Other | ����
    /**
     * �����СʱҪ���ػ�
     */
    @Override
    public void setSize(int width, int height)
    {
        super.setSize(width, height);
        this.transOutdated = true;
        return;
    }

    /**
     * �˶����������ڼ�¼����������, Ϊ�϶������ṩ֧��.
     */
    private int relativeX, relativeY;

    @Override
    public void setSize(Dimension d)
    {
        this.setSize(d.width, d.height);
    }

    private void setDragable()
    {
        // ������갴��, ��¼����ʱ��λ��.
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

        // ��������϶�, �����µĴ���λ��.
        this.addMouseMotionListener(
            new MouseMotionAdapter()
            {
                public void mouseDragged(MouseEvent ev)
                {
                    // ����Ӧ����϶�
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
     * ���캯��...����Ӧ�ñ�ʹ��
     */
    protected BalloonWin()
    {
    }

    /**
     * @deprecated �˷���Ŀǰ�����ڵ�����;
     */
    public static void main(String[] args)
    {
        return;
    }
}
