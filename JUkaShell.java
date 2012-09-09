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
     * <p>���������¼ Shell �ľ�̬�����洢, Ҳ���� ini �ļ���λ��.</p>
     * <p>Shell �Ĵ󲿷־�̬����(���紺�˵�����, ͼ����Դ�б��)���洢�ڴ� ini
     * �ļ���, ͨ�����ַ�ʽ�洢��Ϊ���ڱ����Ժ��������ȡ�þ���, ĳЩ API ��Ҫ
     * �������� Shell ������Ϊ�������, һ�����ǽ�ͨ�����������л�ȡ����, ��
     * �򻯲�����, �����͵����ߵı�̸���.<br>
     * (*) �������չ��Ӧ��д�����</p>
     */
    public static final String DEFAULT_INI = JUkaUtility.getProgramPath() + "defaultShell.ini";

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
     * <p>�˷�����������һ�� Shell ʹ֮������Ч</p>
     * <p>���� Shell ��ָ����ӵ�ǰ���л���������, <span color="red">������ָ
     * �����˴Ӽ�¼�в����������.</span><br>
     * �˲��������� Shell ռ�õ���ʾ��Դ, �������ʾΪ������(isDiscarded() ����
     * ���� false)<br>
     * �κζ������� Shell �Ĳ������󶼽������ػ���Ч.<br>
     * (*) �������չ��Ӧ���ո�ǩ����д����, ������׫�Ĵ���<b>��</b>���ø���ķ���</p>
     */
    public static void destroyShell(JUkaShell argShell)
    {

        this.discarded = true;
        return;
    }

    /**
     * <p>�˷������ڻ��� Shell ʹ֮������Ч</p>
     * <p>�˷���ֻ�ǵ����ص��� JUkaShell.destroyShell(this)<br>
     * (*) ����������չ�߰��ո�ǩ����д����, ������׫�Ĵ���<b>��</b>���� super.discard()</p>
     */
    public void discard()
    {
        JUkaShell.destroyShell(this);
        return;
    }

    /**
     * <p>�˷������� Shell �� discarded ��, ��ָʾ�� Shell �Ƿ��Ѿ�����</p>
     * <p>���õ� shell ��ָ�Ѿ�ͨ�� JUkaShell.destroyShell() ����� Shell.</p>
     * @return ��� Shell ������, �򷵻� true.
     */
    public boolean isDiscarded()
    {
        return(this.discarded);
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
     * �������Ҫ��ʾ������������, �׳����� Exception ����.<br>
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

