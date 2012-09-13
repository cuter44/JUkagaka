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

  // Paint | ����
    /**
     * <p>���ĸ��������¼��ͼ�õ�ͼ��, �ɰ漰����</p>
     */
    private Image[] imageLayer = new Image[8];
    private Area[] maskLayer = new Area[8];
    /**
     * <p>���������¼��ͼ����</p>
     */
    private int[][] coordinate = new int[8][2];
    /**
     * <p>���������¼ imageLayer �� maskLayer Ҫ�󻺳������</p>
     */
    private BufferedImage cacheImage = null;
    private Area cacheMask = null;
    /**
     * <p>�˶����������ڼ�¼ Shell ͼ��/�ɰ�������</p>
     */
    private Hashtable<String, Image> htImages = null;
    private Hashtable<String, Area> htMasks = null;

    /**
     * <p>��������ָ��Ĭ�ϵĻ������, �� #0 �� #bufferLayer ��, Ĭ��Ϊ 3.</p>
     * ��μ�<a href="buildBackBuffer">buildBackBuffer()</a>
     */
    private int bufferLayer = 3;

    /**
     * ���������¼�����Ƿ��ѹ���
     */
    private boolean bufferOutdated = false;

    /**
     * <p>���ͼ�㼰�ɰ�</p>
     * <p>�˷������� argHashKey �е�λ����Ϣ(Ҳ����˵, ֻ������Ϊͼ������ʹ��), ֻ���� argIndex, x, y ����ͼ��</p>
     */
    public boolean setImageLayer(String argHashKey, int argLayer, int x, int y)
    {
        // Խ����
        if ((argLayer < 0) || (argLayer > 7))
            return(false);

        // ͼԪ���ڼ��
        if (!htImages.containsKey(argHashKey))
            return(false);

        imageLayer[argLayer] = (Image)htImages.get(argHashKey);
        maskLayer[argLayer] = (Area)htMasks.get(argHashKey);
        this.coordinate[argLayer][0] = x;
        this.coordinate[argLayer][1] = y;

        if (argLayer <= this.bufferLayer)
            this.bufferOutdated = true;

        return(true);
    }


    /**
     * <p>���ͼ�㼰�ɰ�</p>
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
            System.err.println("argHashKey �����кϷ�ͼ���: " + argHashKey + " ,�Զ�����Ϊ: " + (this.bufferLayer + 1));
            layer = this.bufferLayer + 1;
        }

        try
        {
            x = Integer.parseInt(separatedKey[2]);
            y = Integer.parseInt(separatedKey[3]);
        }
        catch (Exception ex)
        {
            System.err.println("argHashKey �����кϷ�����: " + argHashKey + " ,�Զ�����Ϊ(0,0)");
            x = 0;
            y = 0;
        }

        return(this.setImageLayer(argHashKey, layer, x, y));
    }

    /**
     * <p><a id="buildBackBuffer">���ɻ���</a></p>
     * <p>ͨ���������ı�ı������Ƶ������п�����ʵ�ʻ���ʱ�������.<br>
     * ���ָ����ͼ�㶼Ϊ null, ���Զ����ƿհ׵Ļ���<br>
     * �������Ҫ���ƻ���, ��ָ�� bufferLayer = -1, ����ʹ���Ҳ��Ҫ�������
     * �����Ա�֤��ȷִ��.<br>
     * �����Ӱ��ͼ��ķ��������Զ��ؽ�����, ���ڴ�����μ���������ע��.</p>
     */
    public void buildBackBuffer()
    {
        Dimension winSize = this.getSize();

        // �����µĿհ׻���
        this.cacheImage = new BufferedImage(winSize.width, winSize.height,BufferedImage.TYPE_INT_ARGB);
        this.cacheMask = new Area();
        Graphics g = this.cacheImage.getGraphics();

        // ���ǿ�ͼ��
        int i;
        for (i=0; i<=this.bufferLayer; i++)
            if (this.imageLayer[i] != null)
            {
                g.drawImage(
                    this.imageLayer[i],
                    this.coordinate[i][0],
                    this.coordinate[i][1],
                    JUkaStage.eventListener
                );
                this.cacheMask.add(maskLayer[i]);
            }

        g.dispose();
        this.bufferOutdated = false;

        return;
    }

    /**
     * <p>����</p>
     * <p>�����ط��������ݻ����ͼ���ػ洰��</p>
     */
    @Override
    public void paint(Graphics g)
    {
        int i;

        if (this.bufferOutdated)
            this.buildBackBuffer();

        super.paint(g);

        g.drawImage(this.cacheImage, 0, 0, JUkaStage.eventListener);
        for (i=this.bufferLayer+1; i<=7; i++)
            if (this.imageLayer[i]!=null)
                g.drawImage(
                    this.imageLayer[i],
                    this.coordinate[i][0],
                    this.coordinate[i][1],
                    JUkaStage.eventListener
                );

        return;
    }

    /**
     * <p>�ü�</p>
     * <p>�˷��������ݻ����ͼ��ü�����, ��͸���������οջ�ȥ��.</p>
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

  // Create/Destroy | ����/����

    /**
     * <p>�趨�µ�ͼ�������</p>
     * <p>�˷����ڵ��� createUkagaka() ʱ���Զ�������, ͨ������Ҫ�ֶ��ص���֮</p>
     */
    public void setImageLib(Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        this.htImages = argHtImages;
        this.htMasks = argHtMasks;

        return;
    }

    /**
     * <p>���ɲ�����һ���µĴ���(ָ���ڻ��ƴ��˵Ĵ���)</p>
     * <p>���ɵ��´��˽���ָ���� ini �ļ��� ukagaka ��Ԥ��ʼ��<br>
     * <ul>��Ч���ֶΰ���������Щ
     * <li>
     * <li>width=������ ָ���´��˵Ŀ�� (���붨��)
     * <li>height=������ ָ���´��˵ĸ߶� (���붨��)
     * <li>image?=�ַ��� ȡ�������ͼ�����һ����ͼ��ŵļ�, ָ����ʼͼ������, ? = 0-7
     * </ul></p>
     * @param argIni ��ʾ��¼�г�ʼ����Ϣ�� ini �ļ�
     * @return ���˴��������
     */
    public static UkagakaWin createUkagaka(String argIni, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        UkagakaWin newUkaWin = new UkagakaWin();
        Hashtable<String, String> htInitInfo = JUkaUtility.iniReadSector(argIni, "ukagaka");
        newUkaWin.setImageLib(argHtImages, argHtMasks);

        int h = 0,w = 0,i;

        // ��ʼ��
        // ��С
        if (htInitInfo.containsKey("width"))
            w = Integer.parseInt(htInitInfo.get("width"));
        if (htInitInfo.containsKey("height"))
            h = Integer.parseInt(htInitInfo.get("height"));
        newUkaWin.setSize(w,h);

        // ͼ��
        for (i=0; i<=7; i++)
            if (htInitInfo.containsKey("image"+i))
            {
                String tmpImageName = htInitInfo.get("image"+i);
                // ͨ������Ӧ���п�ֵ...�������ǻ����˷�����.
                if (tmpImageName.equals(""))
                    continue;
                newUkaWin.setImageLayer(htInitInfo.get("image"+i));
            }
        newUkaWin.buildBackBuffer();

        return(newUkaWin);
    }

  // Dragable | ������϶���
  /**
   * �˶����������ڼ�¼����������, Ϊ�϶������ṩ֧��.
   */
    private int relativeX,relativeY;

    /**
     * <p>�˷���ʹ�����ÿ��϶�</p>
     * <p>����һ�μ���. <br>
     * (!)�˹��̲�����, Ҳ����˵, ������ʹ�����±�ò����϶�</p>
     */
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
     * <p>˽�й��캯��</p>
     * <p>������������϶�����Ĵ���</p>
     */

    private UkagakaWin()
    {
        this.setDragable();
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
