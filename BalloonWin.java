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
  // Image APIs | ͼ���趨��API
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

  // Paint | ���Ʊ���
    /**
     * ��ǻ�����Ч��
     */
    private boolean transOutdated = false;

    /**
     * �˶��ֶμ�¼���ź��ͼ�񻺴�
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

        /* ���������ֲ��Ի�����ɰ�,
         * �����ڽ���ϵĲ���Ǽ��α任ʱҲ����о��, ������2x���·Ŵ��������ۺ��ѿ�����.
         * �������㷨������˵, ����ת��������¼��������һ������
         * ���¼��� = O(n*m*x) ����ת�� = O(n+m) x �ǷŴ���.
         */
        // ���¼������
        // this.transMask = JUkaShellCtrl.calculateMask(this.transImage);

        // ����ת������
        this.transMask = this.originMask.createTransformedArea(
            new AffineTransform(
                (double)winSize.width/imgWidth, 0,   // ScaleX, SheerY
                0, (double)winSize.height/imgHeight, // SheerX, ScaleY
                0, 0                                 // TransXY
            )
        );

        // ������, ���·���Ĵ���
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
        newBalloon.initalize(argIniFile, argHtImages, argHtMasks);
        return(newBalloon);
    }

    /**
     * <p>����BalloonWinִ���Գ�ʼ��</p>
     * <p>��ʵ�ϴ˷�������
     * BalloonWin.initalizeInstance(this, argIni, argHtImages, argHtMasks)
     * ִ��ʵ�ʵĳ�ʼ������</p>
     */
    public void initalize(String argIni, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        BalloonWin.initalizeInstance(this, argIni, argHtImages, argHtMasks);
    }

    /**
     * <p>��ʼ��������</p>
     * <p>ʹ��ָ���� ini �ļ��� balloon ��Ԥ��ʼ��<br>
     * <ul>��Ч���ֶ���������Щ
     * <li>width=������ ��ʾ������Ŀ��(���鶨��)
     * <li>height=������ ��ʾ������ĸ߶�(���鶨��)
     * <li>background=�ַ��� ȡ�������ͼ�����һ����ͼ��ŵļ�, ��ʾ������ı�������<br>
     *     ����������Զ������ŵ���������. ������Ų���ʽ���ڿ�����.
     * </ul></p>
     * @param argIni ��ʾ��¼�г�ʼ����Ϣ�� ini �ļ�
     * @return һ���հ׵���������
     */
    public static BalloonWin initalizeInstance(BalloonWin argBalloon, String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        Hashtable<String, String> htInitInfo = JUkaUtility.iniReadSector(argIniFile, "balloon");

        int h = 0,w = 0;

        // ��ʼ��
        // ��С
        if (htInitInfo.containsKey("width"))
            w = Integer.parseInt(htInitInfo.get("width"));
        if (htInitInfo.containsKey("height"))
            h = Integer.parseInt(htInitInfo.get("height"));
        argBalloon.setSize(w,h);

        // ����
        if (htInitInfo.containsKey("background"))
            argBalloon.setBkImage(argHtImages.get(htInitInfo.get("background")), argHtMasks.get(htInitInfo.get("background")));

        // ȥ������
        argBalloon.setUndecorated(true);

        // ���϶�(��ѡ)
        //newBalloon.setDragable(true);

        return(argBalloon);
    }

    /**
     * ���캯��...����Ӧ�ñ�ʹ��
     */
    protected BalloonWin()
    {
    }

  // Dragable | ������϶���
    /**
     * �˶����������ڼ�¼����������, Ϊ�϶������ṩ֧��.
     */
    private int relativeX,relativeY;
    /**
     * ָʾ�϶������ԵĿ���, true = ON, false = OFF
     */
    private boolean dragSwitch = false;
    /**
     * ָʾ�϶����������趨
     */
    private boolean dragSetup = false;

    /**
     * <p>�˷������Ĵ���Ŀ��϶�����</p>
     * <p>�˷������״α�����ʱ���ڴ���ʵ���ϰ�װ�϶�������, ���������ڴ�������ǰ
     * һֱ��Ч, ���Ƿ���Ӧ����϶��� dragSwitch ���ؾ���.<br>
     * </p>
     * @param dragSwitch ָʾ�϶����ܵĿ���״̬
     */
    public void setDragable(boolean argDragSwitch)
    {
        this.dragSwitch = argDragSwitch;

        if (this.dragSetup)
            return;
        this.dragSetup = true;

        // ������갴��, ��¼����ʱ��λ��.
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

        // ��������϶�, �����µĴ���λ��.
        this.addMouseMotionListener(
            new MouseMotionAdapter()
            {
                public void mouseDragged(MouseEvent ev)
                {
                    if (!dragSwitch)
                        return;
                    // ������, �����ڱ�׼����鿴��Ӧ����������
                    //System.out.println(ev.getModifers());

                    // ����Ӧ����϶�
                    // Ҫ��Ӧ���������޸����뼴��
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

  // Resize(@Override) | �����С���(��д)
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

    @Override
    public void setSize(Dimension d)
    {
        this.setSize(d.width, d.height);
    }

  // Other | ����
    /**
     * @deprecated �˷���Ŀǰ�����ڵ�����;
     */
    public static void main(String[] args)
    {
        return;
    }
}
