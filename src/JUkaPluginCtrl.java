/**
 * @version v120826
 * @author "Galin"<cuter44@qq.com>
 */
package jukagaka;

import jukagaka.plugin.*;

import java.io.File;
import java.util.ArrayList;

public class JUkaPluginCtrl extends JUkaComponentCtrl
{
  // Shell Management | Shell�������
    /**
     * ���������¼�����ļ�������
     */
    private static final File LIST_FILE = new File(JUkaUtility.getProgramDir() + "/plugin/JUkaPlugin.list");
    /**
     * <p>��װ Plugin ���, ���������</p>
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
        String classJUkaPlugin = new JUkaPluginCtrl().getClass().getName();

        for (i=1; i<stack.length; i++)
            if (classJUkaPlugin.equals(stack[i].getClassName()))
                break;
        try
        {
            return(JUkaComponentCtrl.installComponent(Class.forName(stack[i+1].getClassName()), JUkaPluginCtrl.LIST_FILE));
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
     * <p>ж�����, ���������</p>
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
        String classJUkaPlugin = new JUkaPluginCtrl().getClass().getName();

        for (i=1; i<stack.length; i++)
            if (classJUkaPlugin.equals(stack[i].getClassName()))
                break;
        try
        {
            return(JUkaComponentCtrl.uninstallComponent(Class.forName(stack[i+1].getClassName()), JUkaPluginCtrl.LIST_FILE));
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
     * <p>�����Ѱ�װ�� Plugin �б�</p>
     * <p>�˷����� JUkaStage �е�ͬ�������������ڴ˷������Ǵ��ļ���ȡ��.</p>
     * @return �����Ѱ�װ�� Plugin �б�
     */
    public static ArrayList<Class> getRegisteredPlugin()
    {
        return(JUkaComponentCtrl.readComponentListFile(JUkaPluginCtrl.LIST_FILE));
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
