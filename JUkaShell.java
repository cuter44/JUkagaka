/**
 * @author "Galin"<cuter44@qq.com>
 * @version v120827
 */

package jukagaka.shell;

import jukagaka.*;
import jukagaka.shell.*;

import java.io.File;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.ArrayList;
import java.awt.Image;
import java.awt.geom.Area;
//import javax.swing.JDialog;

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
    public static final String DEFAULT_INI = JUkaUtility.getProgramDir() + "/defaultShell.ini";

    /**
     * <p>hashImages �� hashShape ���������ڹ��ػ����ͼ����Դ</p>
     * <p>(!) static ���޷����̳�, ������ͬǩ������������д</p>
     * <p>�˶��������� JUkaShellCtrl.prefetchImageResource() ����д��.
     * ֮���Զ���Ϊ public static ��Ϊ�˸�Ч��д, ������Υ���˷�װԭ��,
     * ��Ϊ���ڱ���������Ƿ��޸ĵĿ�����, ���������κ��������µ��˶�������
     * ��Ϸ����:<br>
     * ������: ������<br>
     * ��ֵ����: ���Խ�ֹ<br>
     * clear()����: Σ��������<br>
     * put()/remove()����: ��ȷ�������ڴ�й©</p>
     */
    public static Hashtable<String, Image> hashImages = null;
    public static Hashtable<String, Area> hashMasks = null;

    ///**
     //* <p></p>
     //*/
    //public static String serialImageResourcePath = null;
    //public static String serialImageResourceSector = null;

  // Instance Data | ʵ��������
    /**
     * ���������¼��ǰ���õ��������
     */
    transient private ArrayList<JUkaWindow> winList = null;
    /**
     * ���������¼���ش��˵� Frame
     */
    private UkagakaWin ukagakaWin = null;
    /**
     * ���������¼���˵��� Frame
     */
    transient private BalloonWin mainBalloon = null;
    /**
     * ���������� Shell �Ƿ��Ѿ�����
     */
    transient private boolean discarded = false;

  // Install/Uninstall | ��װ/ж��
    // �밴��Ҫ����д��Щ����.
    /**
     * <p>(ģ��)��װ Shell</p>
     * <p>Shell �ڿ��Ա�ʹ��֮ǰ, ������ JUkaStage ֪ͨ�����<br>
     * (!) �������չ��Ӧ<b>����</b>����������� TODO �������׫�Ĵ���,
     *     JUkaShell.java ��ԭ�еĲ��ֲ��ܱ�ɾ��. ������ܵ�������ʧ��.<br>
     * </p>
     * @return <ul>����ֵ, ���ڵ���0����ʾִ�гɹ�, ��֮��ʾʧ��
     *  <li>0=����
     *  <li>1=������Ѱ�װ
     *  <li>-1=(δ�������)
     *  <li>-2=����ջ����
     *  <li>-3=��д����
     * </ul>
     */
    public static int install()
    {
        // TODO �ڴ˴����밲װǰ�Ĵ���
        // ���� �������/��ͻ��ϵ, EULA, ʹ��˵��, ȷ�϶Ի���, �����û����� �ȵ�.
        return(JUkaShellCtrl.installComponent());
    }

    /**
     * <p>(ģ��)ж�� Shell</p>
     * <p>Ҫ���ټ���ĳ�� Shell, ������ JUkaStage ֪ͨע��<br>
     * (!) �������չ��Ӧ<b>����</b>����������� TODO �������׫�Ĵ���,
     *     JUkaShell.java ��ԭ�еĲ��ֲ��ܱ�ɾ��. ������ܵ�������ʧ��.<br>
     * </p>
     * @return <ul>����ֵ, ���ڵ���0����ʾִ�гɹ�, ��֮��ʾʧ��
     *  <li>0=����
     *  <li>1=����������б���
     *  <li>-1=(δ�������)
     *  <li>-2=����ջ����
     *  <li>-3=��д����
     * </ul>
     */
    public static int uninstall()
    {
        // TODO �ڴ˴�����ж��ǰ�Ĵ���
        // ���� ȷ�϶Ի���, �����û�����, ����ж�صȵ�.
        return(JUkaShellCtrl.uninstallComponent());
    }

  // Shell Constructor & Destructor | Shell ����/����
    // �밴��Ҫ����д��Щ����.
    /**
     * <p>�˷������ڲ���һ���µ� Shell</p>
     * <p>����Ĳ�����������(���ǿ�¡)���洢�����ɵ� Wins ��, Ҳ����˵��ͼ���
     * �������޸Ļ��Զ���ӳ��������.<br>
     * (*) �������չ��Ӧ�� public ��д����, ������׫�Ĵ���<b>ǰ</b>���ø���ķ���.
     *     ����ʲôҲ������ֱ��ʹ��ȱʡ�ķ���</p>
     */
    public static JUkaShell createShell(String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        JUkaShell newShell = new JUkaShell();

        newShell.initalizeUkagaka(argIniFile, argHtImages, argHtMasks);
        newShell.initalizeMainBalloon(argIniFile, argHtImages, argHtMasks);
        newShell.initalizeWinList();

        return(newShell);
    }

    /**
     * ����չ
     */
    public void initalizeUkagaka(String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        this.ukagakaWin = UkagakaWin.createUkagaka(argIniFile, argHtImages, argHtMasks);

        return;
    }

    /**
     * ����չ
     */
    protected void initalizeMainBalloon(String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        this.mainBalloon = BalloonWin.createBalloon(argIniFile, argHtImages, argHtMasks);

        return;
    }

    /**
     * ����չ
     */
    public void initalizeWinList()
    {
        this.winList = new ArrayList<JUkaWindow>(16);
        this.winList.add(this.ukagakaWin);
        this.winList.add(this.mainBalloon);

        return;
    }

    // (!) ��API�ѱ�ȡ��
    ///**
     //* <p>���� Shell �Գ�ʼ��</p>
     //* <p>��ʵ�ϴ˷�������
     //* JUkaShell.initalizeInstance(this, argIni, argHtImages, argHtMasks)
     //* ִ��ʵ�ʵĳ�ʼ������</p>
     //*/
    //protected void initalize(String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    //{
        //JUkaShell.initalize(this. argIniFile, argHtImages, argHtMasks);
    //}

    // (!) ��API�ѱ�ȡ��
    ///**
     //* <p>�� Shell ������л�����ʼ��</p>
     //*/
    //protected static JUkaShell initalizeInstance(JUkaShell argShell, String argIniFile, Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    //{
        //// ��ʼ���������
        //argShell.winList = new ArrayList<JUkaWindow>(16);
        //// ��ʼ�� UkagakaWin
        //argShell.ukagakaWin = UkagakaWin.createUkagaka(argIniFile, argHtImages, argHtMasks);
        //argShell.winList.add(argShell.ukagakaWin);
        //// ��ʼ�� BalloonWin
        //argShell.mainBalloon = BalloonWin.createBalloon(argIniFile, argHtImages, argHtMasks);
        //argShell.winList.add(argShell.mainBalloon);

        //return(argShell);
    //}


    /**
     * <p>�˷�����������һ�� Shell ʹ֮������Ч</p>
     * <p>���� Shell ��ָ����ӵ�ǰ���л���������, <span color="red">������ָ
     * �����˴Ӽ�¼�в����������.</span><br>
     * �˲��������� Shell ռ�õ���ʾ��Դ, �������ʾΪ������(isDiscarded() ����
     * ���� false)<br>
     * �κζ������� Shell �Ĳ������󶼽������ػ���Ч.<br>
     * (*) �������չ��Ӧ���ո�ǩ����д����, ������׫�Ĵ���<b>��</b>���ø���ķ���.
     *     ����ʲôҲ������ֱ��ʹ��ȱʡ�ķ���</p>
     */
    protected static void discardShell(JUkaShell argShell)
    {

        argShell.discarded = true;
        return;
    }

    /**
     * <p>�˷������ڻ��� Shell ʹ֮������Ч</p>
     * <p>�˷���ֻ�ǵ����ص��� JUkaShell.destroyShell(this)<br>
     * (*) ����������չ��Ӧ�� public ��д����, ������׫�Ĵ���<b>��</b>���� super.discard()</p>
     */
    protected void discard()
    {
        JUkaShell.discardShell(this);
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

  // Ukagaka Manipulate | ���˲�������
    /* ���� JLS �淶�Լ��������ܵ���Ҫ�� ukagakaWin ��������,
     * ���˵�һ��������, ���������ת���� JUkaShell.ukagakaWin ��ͬ��������ִ��.
     * �������չ�߿�����д���� super.methodName() �ķ�ʽ�����������Լ��趨������.
     * ��ȻҲ���Է��Ų���д, ���� JLS �Ķ�̬���趨, ��Щ�������ɻᱻִ��.
     */
    /**
     * <p>��ȡ UkagakaWin ������</p>
     * <p>(!) ע��ͨ���˷��������ƹ���װֱ�Ӳٿش���, ����������ƻ��Ժ��,
     * ����벻Ҫ���ڵ��������Ŀ��ʹ�ô˷���.</p>
     */
    public UkagakaWin getUkagaka()
    {
        return(this.ukagakaWin);
    }

    public boolean setImageLayer(String argHashKey, int argLayer, int x, int y, int accessKey)
    {
        if (!this.checkAuthority(accessKey))
            return(false);

        return(this.ukagakaWin.setImageLayer(argHashKey, argLayer, x, y));
    }

    public boolean setImageLayer(String argHashKey, int accessKey)
    {
        if (!this.checkAuthority(accessKey))
            return(false);

        return(this.ukagakaWin.setImageLayer(argHashKey));
    }

    public boolean setImageBatch(String[] arrHashKeys, int accessKey)
    {
        if (!this.checkAuthority(accessKey))
            return(false);

        return(this.ukagakaWin.setImageBatch(arrHashKeys));
    }

    public boolean setImageBatch(String arrHashKeys, int accessKey)
    {
        if (!this.checkAuthority(accessKey))
            return(false);

        return(this.ukagakaWin.setImageBatch(arrHashKeys));
    }

    public int getBufferLayer()
    {
        return(this.ukagakaWin.getBufferLayer());
    }

    public int setBufferLayer(int newBufferLayer)
    {
        return(this.ukagakaWin.setBufferLayer(newBufferLayer));
    }

    public void buildBackBuffer()
    {
        this.ukagakaWin.buildBackBuffer();
        return;
    }

  // Ukagaka Monopoly | ��ռʹ��(�²ۿӵ��İ��ʰ�)
    /**
     * ���������¼Ŀǰʹ���е� Key
     */
    private int preservedKey = 0;

    /**
     * <p>���������Ի����ר��Ȩ</p>
     * <p>�������չ��Ӧ�� public ���Ǵ˺���, ������׫�Ĵ���<strong>��</strong>
     * ���ô˷���.</p>
     */
    protected int lockUkagaka()
    {
        // ������ʱ����
        if (this.preservedKey != 0)
            return(0xFFFFFFFF);

        // ������Կ
        do
            this.preservedKey = new Double(Math.random() * 0x7FFFFFFF).intValue();
        while (this.preservedKey <= 0);

        return(this.preservedKey);
    }

    /**
     * <p>����α������ȥ����ר�ñ���</p>
     * <p>���ڲ�������Կ����, ��������������ǳɹ����ҷ��� true ��, ����ζ��
     * ������ռ Shell, ���Ҳ�����ջ��Ϣ�Ի�ԭ��ռ... ԭռ����Ҳ���ᱻ֪ͨ<br>
     * �������չ��Ӧ���� public ���Ǵ˷���, ������׫�Ĵ���<strong>��</strong>
     * ���ô˷���</p>
     */
    protected boolean unlockUkagaka()
    {
        this.preservedKey = 0;

        return(true);
    }

    /**
     * <p>�˷����ж���������Կ�ܷ��ô��˵�ռ��Ȩ</p>
     */
    public boolean checkAuthority(int key)
    {
        if (this.preservedKey == 0)
            return(true);

        return((key ^ this.preservedKey) == 0);
    }

  // Balloon Manipulate | �������ָ��
    //   ��
    //  /��\  TODO: ������...
    // /_��_\

  // General for Ukagaka & Balloon | Win ͨ�ò�������
    /**
     * <p>Ҫ���ػ�ָ���Ĵ���</p>
     */
    public void fireRepaint(int argWinID)
    {
        try
        {
            this.fireRepaint(this.winList.get(argWinID));
        }
        catch (IndexOutOfBoundsException ex)
        {
            System.err.println(ex);
            System.err.println("Error: JUkaShell.fireReprint(): ��Ч�±�����, �������˳�");
            return;
        }

        return;
    }

    /**
     * <p>Ҫ���ػ�ָ���Ĵ���</p>
     */
    public boolean fireRepaint(JUkaWindow argWin)
    {
        if (argWin == null)
        {
            System.err.println("Error: JUkaShell.fireRepaint(): ��ָ�����, �������˳�");
            return(false);
        }

        if (!this.winList.contains(argWin))
        {
            System.err.println("Warn: JUkaShell.fireRepaint(): ָ������δ�������б���");
            return(false);
        }

        argWin.repaint();

        return(true);
    }

    /**
     * <p>Ҫ��ü�ָ���Ĵ���</p>
     */
    public void fireClip(int argWinID)
    {
        try
        {
            this.fireClip(this.winList.get(argWinID));
        }
        catch (IndexOutOfBoundsException ex)
        {
            System.err.println(ex);
            System.err.println("Error: JUkaShell.fireClip(): ��Ч�±�����, �������˳�");
            return;
        }

        return;
    }

    /**
     * <p>Ҫ��ü�ָ���Ĵ���</p>
     */
    public boolean fireClip(JUkaWindow argWin)
    {
        if (argWin == null)
        {
            System.err.println("Error: JUkaShell.fireClip(): ��ָ�����, �������˳�");
            return(false);
        }

        if (!this.winList.contains(argWin))
        {
            System.err.println("Warn: JUkaShell.fireClip(): ָ������δ�������б���");
            return(false);
        }

        argWin.clip();

        return(true);
    }

  // Trigger | ������
    /**
     * <p>�˷���Ŀǰ�������κδ���, ������Ϊ token ����.</p>
     * <p>����ĸ÷������� JUkagaka ����ʱ������, ������Գô���ɳ�ʼ������<br>
     * �������Ҫ��ʾ������������, �׳����� Exception ����.<br>
     * (*) �������չ��Ӧ�� public ��д����, ������׫�Ĵ���<strong>ǰ</strong>���ô˷���<br></p>
     */
    protected static void onLoad()
    {

        return;
    }

    /**
     * <p>�˷��������˳�ʱ����</p>
     * <p>����ĸ÷������� JUkagaka �˳�ǰ������, ������Գô�����˳�ǰ����<br>
     * �������Ҫ��ʾ������������, �׳����� Exception ����.<br>
     * (*) �������չ��Ӧ���ո�ǩ����д����, ������׫�Ĵ���<b>��</b>���ø���ķ���<br></p>
     */
    protected static void onExit()
    {
        return;
    }

  // Other | ����
    /**
     * ���캯��...��Ӧ�ñ���ʽ����
     */
    protected JUkaShell()
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

