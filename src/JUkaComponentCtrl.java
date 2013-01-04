/**
 * @version v120826
 * @author "Galin"<cuter44@qq.com>
 */
package jukagaka;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class JUkaComponentCtrl
{
  // Static Data | ��̬������

  // Install/Uninstall | ��װ/ж��
    /**
     * ��װ���, ��ֱ���������
     * @param argComponent ָ������װ���
     * @return <ul>����ֵ<li>0=�ɹ�<li>1=��ע��<li>-1=��д����</ul>
     */
    protected static int installComponent(Class argComponent, File argListFile)
    {
        ArrayList<Class> compList = JUkaComponentCtrl.readComponentListFile(argListFile);

        // ��ȡ������
        if (compList == null)
            return(-1);

        // ��ע���������
        if (compList.contains(argComponent))
            return(1);

        compList.add(argComponent);
        return(JUkaComponentCtrl.writeComponentListFile(compList, argListFile));
    }

    /**
     * ж�����, ��ֱ���������
     * @param argComponent ָ����ж�����
     * @return <ul>����ֵ<li>0=�ɹ�<li>1=δע��<li>-11=��д����</ul>
     */
    protected static int uninstallComponent(Class argComponent, File argListFile)
    {
        ArrayList<Class> compList = JUkaComponentCtrl.readComponentListFile(argListFile);

        // ��ȡ������
        if (compList == null)
            return(-1);

        // δע���������
        if (!compList.contains(argComponent))
            return(1);

        compList.remove(argComponent);
        return(JUkaComponentCtrl.writeComponentListFile(compList, argListFile));
    }

  // Basic I/O | �����ļ���д
    /**
     * <p>��ָ�����ļ���ȡ����б�</p>
     * (*) ���ָ�����ļ�������, �򴴽����ļ�������һ���յ� ArrayList<br></p>
     * @param argListFile ָ��Ҫ�����л����ļ�
     * @return �����л�������б�
     */
    protected static ArrayList<Class> readComponentListFile(File argListFile)
    {
        ArrayList<Class> compList;
        try
        {
            if (argListFile.exists() == false)
            {
                compList = new ArrayList<Class>();
            }
            else
            {
                ObjectInputStream iStream = new ObjectInputStream(new FileInputStream(argListFile));
                compList = (ArrayList<Class>)iStream.readObject();
                iStream.close();
            }
            return(compList);
        }
        catch (Exception ex)
        {
            System.err.println(ex);
            return(null);
        }
    }

    /**
     * <p>��ָ�����ļ�д������б�</p>
     * @param argCompList ָ��Ҫд�������б�
     * @param argListFile ָ��Ҫ���л����ļ�
     * @return <ul>����ֵ<li>0=�ɹ�<li>-1=��д����</ul>
     */
    protected static int writeComponentListFile(ArrayList<Class> argCompList, File argListFile)
    {
        try
        {
            ObjectOutputStream oStream = new ObjectOutputStream(new FileOutputStream(argListFile));
            oStream.reset();
            oStream.writeObject(argCompList);
            oStream.close();
            return(0);
        }
        catch (Exception ex)
        {
            // Never cause this exception
            System.err.println(ex);
            return(-1);
        }
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
