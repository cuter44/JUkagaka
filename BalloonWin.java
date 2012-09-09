/**
 * @version v120831
 * @author "Galin"<cuter44@qq.com>
 */

package jukagaka.shell;

import jukagaka.*;
import javax.swing.JWindow;

public class BalloonWin extends JWindow
{
  // Create/Destroy | ����/����
    /**
     * <p>���ɲ�����һ���հ׵�����</p>
     * <p>���ɵ����򽫱�ָ���� ini �ļ��� balloon ��Ԥ��ʼ��<br>
     * <ul>��Ч���ֶ���������Щ
     * <li>width=������ ��ʾ������Ŀ��(���붨��)
     * <li>height=������ ��ʾ������ĸ߶�(���붨��)
     * </ul></p>
     * @param argIni ��ʾ��¼�г�ʼ����Ϣ�� ini �ļ�
     * @return һ���հ׵���������
     */
    public static BalloonWin createBalloon(String argIni)
    {
        BalloonWin newBalloon = new BalloonWin();
        Hashtable htInitInfo = JUkaUtility.iniReadSector(argIni, "balloon");

        int h = 0,w = 0;

        // ��ʼ��
        // ��С
        if (htInitInfo.containsKey("width"))
            w = Integer.parseInt(htInitInfo.get("width"));
        if (htInitInfo.containsKey("height"))
            h = Integer.parseInt(htInitInfo.get("height"));
        this.setSize(w,h);

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
