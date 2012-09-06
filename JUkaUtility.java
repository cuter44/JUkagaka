/**
 * @version v120831
 * @author "Galin"<cuter44@qq.com>
 */

package jukagaka;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Hashtable;

public class JUkaUtility
{
  //Static Data | ��̬������
    /**
     * ���������¼����ľ���·��, �� getProgramPath() ���ɺͻ�ȡ.
     */
    private static String programPath = null;

  // Environment | ����
    /**
     * <p>�˷������ɺͷ��� programPath</p>
     * <p>�˷������������� JUkaStage ��, ��Ϊ������ȷ��������������<br>
     * �˷������� Unix �淶��·��, ��ʹ���� Windows ƽ̨������<br>
     * ���...һ��Ҫ����һ�����ӵĻ�: "/F:/project/v120826/jukagaka/"</p>
     * @return ���س����Unixʽ����·��
     */
    public static String getProgramPath()
    {
        if (JUkaUtility.programPath == null)
            try
            {
                JUkaUtility.programPath = Class.forName("jukagaka.JUkaUtility").getClassLoader().getResource("").getPath() + "jukagaka/";
            }
            catch (Exception ex)
            {
                System.err.println(ex);
                return(null);
            }
        return(JUkaUtility.programPath);
    }

  // Ini File | ini �ļ���ȡ
  // �ṩ������ ini �ļ���ȡ����
    /**
     * <p>��ָ���� ini �ļ��ж�λָ���Ľ�</p>
     * <p>��Ҫ��Ϊ�˿��ٲ���λ�ù����� API ���������ȡ�õ�</p>
     * @return �Ѿ�ָ��ָ��λ�õ��ļ���, ���ָ���ļ���ڲ������򷵻� null.
     */
    public static FileInputStream iniLocate(String argFileName, String argSector)
    {
        File iniFile = null;
        FileInputStream iniStream = null;
        Scanner iniScanner = null;

        // ��ͼ��ȡ�ļ�
        try
        {
            iniFile = new File(argFileName);
            iniStream = new FileInputStream(iniFile);
            iniScanner = new Scanner(new FileInputStream(iniFile));
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("ָ�����ļ�������: " + argFileName);
            return(null);
        }
        catch (Exception ex)
        {
            System.err.println(ex);
            return(null);
        }

        // ���м���, ���ҵ����� Sector ʱ����
        while(iniScanner.hasNextLine())
        {
            String buffer = iniScanner.nextLine();
            if (buffer.matches("\\s*\\[" + argSector + "\\]"))
            {
                //iniScanner.close();
                return(iniStream);
            }
        }

        iniScanner.close();
        return(null);
    }

    /**
     * <p>�ӵ�ǰλ�ÿ�ʼö�ٽ��������ؼ�-ֵ��, �����ڽ���ʱ��ֹ</p>
     * <p>��������Ϊ iniRead(Sector��) ���ص���������������Բ�д�����.</p>
     * @return ����һ����Ԫ String ����, String[0] ��ʾ��, String[1] ��ʾֵ, �����ö�ٵ���ĩβ�򷵻� null.
     */
    public static String[] iniEnumSector(FileInputStream iniStream)
    {
        Scanner iniScanner = new Scanner(iniStream);
        String[] rtString = null;
        String buffer = null;

        // �ļ�ĩβʱ���� null
        while (iniScanner.hasNextLine())
        {
            buffer = iniScanner.nextLine();

            // ����ע����
            if (buffer.matches("\\s;"))
                continue;

            // ��ĩβʱ���� null
            if (buffer.matches("\\s*\\[.*\\]"))
                return(null);

            // �� = Ϊ��׼ƥ���-ֵ��
            // ����βȥ�ո�, ֵ\nת��Ϊ�س���
            if (buffer.matches("\\s*(.+)=(.*)$"))
            {
                rtString = buffer.split("=",2);
                rtString[0] = rtString[0].trim();
                rtString[1] = rtString[1].replaceAll("\\n","\n");
                iniScanner.close();
                return(rtString);
            }
        }

        return(null);
    }

    /**
     * <p>��ָ���� ini �ļ��ж�ȡ������</p>
     * @return �����ȡ�ĽڵĹ�ϣ��, ���ָ���ļ���ڲ������򷵻� null,
     * ���ָ����Ϊ���򷵻�һ���յĹ�ϣ��.
     */
    public static Hashtable<String, String> iniRead(String argFileName, String argSector)
    {
        FileInputStream iniStream = JUkaUtility.iniLocate(argFileName, argSector);

        // ��������ȷʱ���� null.
        if (iniStream == null)
            return(null);

        Hashtable<String, String> rtHtSector = new Hashtable<String, String>();
        String[] buffer = null;

        while ((buffer=iniEnumSector(iniStream)) != null)
            rtHtSector.put(buffer[0],buffer[1]);

        try
        {
            iniStream.close();
        }
        catch (IOException ex)
        {
            // This exception merely occures
            System.err.println(ex);
        }
        return(rtHtSector);
    }

    /**
     * <p>��ָ���� ini �ļ��ж�ȡָ���ںͼ���ֵ</p>
     * <p>�򵥵�˵����:<br>
     * <p>[argSector]<br>
     * argKey=Value</p>
     * ��֮, ���ص��� Value �Ͷ���.</p>
     */
    public static String iniRead(String argFileName, String argSector, String argKey)
    {
        FileInputStream iniStream = JUkaUtility.iniLocate(argFileName, argSector);

        // ��������ȷʱ���� null.
        if (iniStream == null)
            return(null);

        String[] buffer = null;

        while ((buffer=iniEnumSector(iniStream)) != null)
            if (buffer[0].equals(argKey))
            {
                try
                {
                    iniStream.close();
                }
                catch (IOException ex)
                {
                    // This exception merely occured.
                    System.err.println(ex);
                }
                return(buffer[1]);
            }

        return(null);
    }

  // Other | ����
    /**
     * @deprecated �˷���Ŀǰ�����ڵ���
     */
    public static void main(String[] args)
    {
        FileInputStream iniStream = null;
        Hashtable<String, String> ht = null;

        // �˴�������ڲ��� ��ȡ����·��
        System.out.println(JUkaUtility.getProgramPath());

        // �˴�������ڲ��� ��ȡini
        JUkaUtility.iniLocate(JUkaUtility.getProgramPath() + "shell/SampleShell/SampleShell.ini", "images");
        ht = iniRead(JUkaUtility.getProgramPath() + "shell/SampleShell/SampleShell.ini", "images");

        return;
    }
}
