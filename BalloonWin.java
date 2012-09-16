/**
 * @version v120831
 * @author "Galin"<cuter44@qq.com>
 */

package jukagaka.shell;

import jukagaka.*;

import java.awt.Image;
import java.awt.geom.Area;
import java.util.Hashtable;
import javax.swing.JWindow;

public class BalloonWin extends JWindow
{
    private Hashtable<String, Image> htImages = null;
    private Hashtable<String, Area> htMasks = null;

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

  // Create/Destroy | ����/����
    /**
     * <p>���ɲ�����һ���հ׵�����</p>
     * <p>���ɵ����򽫱�ָ���� ini �ļ��� balloon ��Ԥ��ʼ��<br>
     * <ul>��Ч���ֶ���������Щ
     * <li>width=������ ��ʾ������Ŀ��(���붨��)
     * <li>height=������ ��ʾ������ĸ߶�(���붨��)
     * <li>background=�ַ��� ȡ�������ͼ�����һ����ͼ��ŵļ�, ��ʾ������ı�������
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

        return(newBalloon);
    }

  // Other | ����
    /**
     * ˽�й��캯��
     */
    private BalloonWin()
    {
    }

    /**
     * @deprecated �˷���Ŀǰ�����ڵ�����;
     */
    //public static void main(String[] args)
    //{
        //return;
    //}
}
