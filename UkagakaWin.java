/**
 * @version v120831
 * @author "Galin"<cuter44@qq.com>
 */

package jukagaka.shell;

import javax.swing.JWindow;

public class UkagakaWin extends JWindow
{
  // Paint | ����
    /**
     * <p>���ĸ��������¼��ͼ�õ�ͼ��, �ɰ漰����</p>
     */
    private Image[] imageLayer = new Image[8];
    private Area[] maskLayer = new Area[8];
    /**
     * <p>�˶������򻺴� imageLayer �� maskLayer ǰ�Ĳ������</p>
     */
    private Image cacheImage = null;
    private Area cacheMask = null;

    /**
     * <p>���ƺ���</p>
     */
    @Override
    public void paintComponent()
    {
        Image =
        return();
    }

  // Create/Destroy | ����/����
    /**
     * <p>���ɲ�����һ���µĴ���(ָ���ڻ��ƴ��˵Ĵ���)</p>
     * <p>���ɵ��´��˽���ָ���� ini �ļ��� ukagaka ��Ԥ��ʼ��<br>
     * <ul>��Ч���ֶΰ���������Щ
     * <li>
     * <li>width=������ ָ���´��˵Ŀ��(���붨��)
     * <li>height=������ ָ���´��˵ĸ߶�(���붨��)
     * <li>layer4=�ַ��� ָ���´��˵Ļ���, ����4-��Ҫ��, (���붨��)
     * </ul></p>
     * @param argIni ��ʾ��¼�г�ʼ����Ϣ�� ini �ļ�
     * @return ���˴��������
     */
    public static BalloonWin createUkagaka(String argIni, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        UkagakaWin newUkaWin = new BalloonWin();
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
    private UkagakaWin()
    {
    }

    /**
     * @deprecated �˷���Ŀǰ�����ڵ���
     */
    //public static void main(String[] args)
    //{
        //return;
    //}
}
