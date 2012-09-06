/**
 * @author "Galin"<cuter44@qq.com>
 * @version v120827
 */

package jukagak.shell;

import jukagaka.*;

import java.io.File;
import java.util.Hashtable;
import java.util.ArrayList;
import java.awt.Image;
import java.awt.geom.Area;
import javax.swing.JWindow;

public class JUkaShell extends JUkaComponent implements Serializable
{
  // Static Data (NEED REWRITE) | ����д��̬������
    // �뽫��Щ�������Ƶ�����д��������, ������Ҫ�޸����е��ַ�������.
    // ���ַ���ֵ��������ǩ������Ķ�.
    /**
     * <p>���������¼�� Shell �ı�̷�������, û�ж����...</p>
     * <p>(*) �������չ��Ӧ��д�����</p>
     */
    public static final String shellName = "jukagaka.shell.JUkaShell";

    /**
     * <p>���������¼�� Shell ��Ӧ��Ĭ�� Ghost ����</p>
     * <p>�����������İ������������, ���Ҵ�Сд����<br>
     * JUkagaka ���ڴ����µ� Shell ʱ���Ҳ��ṩ���Ĭ�ϵ� Ghost.<br>
     * (*) �������չ��Ӧ��д�����</p>
     */
    public static final String defaultGhost = "jukagaka.ghost.JUkaGhost";

    /**
     * <p>���������¼�˴��˵ĳ�ν</p>
     * <p>(*) �������չ��Ӧ��д�����</p>
     */
    public static final String ukagakaName = "JUkaShell";

    /**
     * <p>���������¼�˴��˵��Գ�</p>
     * <p>(*) �������չ��Ӧ��д�����</p>
     */
    public static final String ukagakaSelfCall = "JUkaShell";

    /**
     * <p>���������¼�˸�ͼ����Դ�ļ�������</p>
     * <p>(*) �������չ��Ӧ��д�����<br>
     * .images��һ��plain-text�ļ�, ����:<br>
     * &nbsp;��һ����һ������n��ʾ����n��������<br>
     * &nbsp;�˺����n����, ����ÿ���е�:
     * &nbsp;&nbsp;��1����һ��ͼ���ļ���, �Զ���JUkaUtility.getProgramPath()
     * �����·����ʾ<br>
     * &nbsp;&nbsp;�˺���� 0 ��������, ��ʾҪע���ͼ�����<br>
     * &nbsp;&nbsp;����һ�� "-" ��ʾ�������<br>
     * &nbsp;��ʾ����Щ����ӳ�䵽ͼ����Դ��<br>
     * ��Щ�ִ�������Ƕ���/setBackground()�ȹ�������Ψһ�ر�ʶһ��ͼ����Դ<br>
     * <del>(!) ��ʱ���޷�����������������ְ󶨵�ͬһͼ��ʱ��Ԥ���ظ�����(��ʵ����Ϊ��)</del><br>
     * ��ͬһ��shell�ж���ı����������ظ�(�ϻ�)<br>
     * ��Сд����</p>
     */
    public static final File IMAGE_CATALOG = new File(JUkaUtility.getProgramPath() + "default.images");

    /**
     * <p>hashImages �� hashShape ���������ڹ��ػ����ͼ����Դ</p>
     * <p>�˶��������� JUkaShellCtrl.prefetchImageResource() ����д��</p>
     */
    public static Hashtable<String, Image> hashImages = null;
    public static Hashtable<String, Area> hashMasks = null;

  // Instance Data | ʵ��������
    /**
     * ���������¼��ǰ���õ��������
     */
    private ArrayList<JWindow> frameList = null;
    /**
     * ���������¼���ش��˵� Frame
     */
    private UkagakaFrame ukagakaFrame = null;
    /**
     * ���������¼���˵��� Frame
     */
    private BalloonFrame mainBalloonFrame = null;
    /**
     * ���������� Shell �Ƿ��Ѿ�����
     */
    private boolean discarded = false;

  // Shell Instance | Shell ʵ������
  // �밴��Ҫ����д��Щ����.
    /**
     * <p>�˷������ڲ���һ���µ� Shell</p>
     * <p><br>
     * (*) �������չ��Ӧ���ո�ǩ����д����, ������׫�Ĵ���<b>ǰ</b>���ø���ķ���</p>
     */
    public static JUkaShell createShell()
    {
        JUkaShell newShell = new JUkaShell();
        newShell.ukagakaFrame = UkagakaFrame.createFrame();
        newShell.mainBalloonFrame = balloonFrame.createFrame();
        return(null);
    }

    /**
     * <p>�˷������ڻ���һ�� Shell ʹ֮������Ч</p>
     * <p>�˷��������ٴ���ռ�õ���Դ<br>
     * (*) �������չ��Ӧ���ո�ǩ����д����, ������׫�Ĵ���<b>��</b>���ø���ķ���</p>
     */
    public static void destroyShell()
    {

        return;
    }

  // Start-up | ������
    /**
     * <p>�˷���Ŀǰ�������κδ���, ������Ϊ token ����.</p>
     * <p>����ĸ÷������� JUkagaka ����ʱ������, ������Գô���ɳ�ʼ������<br>
     * �������Ҫ��ʾ������������, �׳����� Exception ����.<br>
     * (*) �������չ��Ӧ���ո�ǩ����д����<br></p>
     */
    public static void onLoad()
    {
        return;
    }

    /**
     * <p>�˷��������˳�ʱ����</p>
     * <p>����ĸ÷������� JUkagaka �˳�ǰ������, ������Գô�����˳�ǰ����<br>
     * (*) �������չ��Ӧ���ո�ǩ����д����, ������׫�Ĵ���<b>��</b>���ø���ķ���<br></p>
     */
    public static void onExit()
    {
        return;
    }

  // Other | ����
    /**
     * ��������ʵĹ��캯��
     */
    private JUkaShell()
    {
    }
    /**
     * @deprecated �˷���Ŀǰ�����ڵ���
     */
    public static void main(String[] args)
    {
        return;
    }
}

