/**
 * @version v120826
 * @author "Galin"<cuter44@qq.com>
 */

package jukagaka;

import jukagaka.ghost.*;

import java.io.File;
import java.util.ArrayList;

public class JUkaGhostCtrl extends JUkaComponentCtrl
{
  // Shell Management | Shell�������
    /**
     * ���������¼�����ļ�������
     */
    private static final File LIST_FILE = new File(JUkaUtility.getProgramPath() + "JUkaGhost.list");
    /**
     * <p>��װ Ghost ���, ���������</p>
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
        String classJUkaGhost = new JUkaGhostCtrl().getClass().getName();

        for (i=1; i<stack.length; i++)
            if (classJUkaGhost.equals(stack[i].getClassName()))
                break;
        try
        {
            return(JUkaComponentCtrl.installComponent(Class.forName(stack[i+1].getClassName()), JUkaGhostCtrl.LIST_FILE));
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
     * <p>ж�� Ghost ���, ���������</p>
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
        String classJUkaGhost = new JUkaGhostCtrl().getClass().getName();

        for (i=1; i<stack.length; i++)
            if (classJUkaGhost.equals(stack[i].getClassName()))
                break;
        try
        {
            return(JUkaComponentCtrl.uninstallComponent(Class.forName(stack[i+1].getClassName()), JUkaGhostCtrl.LIST_FILE));
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
     * <p>�����Ѱ�װ�� Ghost �б�</p>
     * <p>�˷����� JUkaStage �е�ͬ�������������ڴ˷������Ǵ��ļ���ȡ��.</p>
     * @rerurn �����Ѱ�װ�� Ghost �б�
     */
    public static ArrayList<Class> getRegisteredGhost()
    {
        return(JUkaComponentCtrl.readComponentListFile(JUkaGhostCtrl.LIST_FILE));
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
