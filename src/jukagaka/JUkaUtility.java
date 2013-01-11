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
  // Environment | ����
    /**
     * ���������¼����ľ���·��, �� getProgramDir() ���ɺͻ�ȡ.
     */
    private static String programDir = null;

  // Environment | ����
    /**
     * <p>�˷������ɺͷ��� programPath</p>
     * <p>�˷������������� JUkaStage ��, ��Ϊ������ȷ��������������<br>
     * �˷������� Unix �淶��·��, ��ʹ���� Windows ƽ̨������<br>
     * ���...һ��Ҫ����һ�����ӵĻ�: "/F:/project/v120826/jukagaka"</p>
     * @return ���س����Unixʽ����·��
     */
    public static String getProgramDir()
    {
        if (JUkaUtility.programDir == null)
            try
            {
                JUkaUtility.programDir = Class.forName("jukagaka.JUkaUtility").getClassLoader().getResource("").getPath() + "jukagaka";
            }
            catch (Exception ex)
            {
                System.err.println(ex);
                return(null);
            }
        return(JUkaUtility.programDir);
    }

    /**
     * ���������¼�û�����趨�ľ���·��, �� getProgramDir() ���ɺͻ�ȡ.
     */
    private static String userConfDir = null;

    /**
     * <p>�˷������ɺͷ��� userConfDir</p>
     * <p>Ϊ��ȡ�����ù�����, ������ userConfDir �½����ļ��д������,
     * ���������μ� JAPI �� File.mkdirs() ����.<br>
     * ע�� userConfPath ָ����Ŀ¼��һ��������ʵ���ļ�ϵͳ��, �� mkdirs()
     * �Ѿ�Ϊ����������������.</p>
     */
    public static String getUserConfDir()
    {
        if (JUkaUtility.userConfDir == null)
        {
            String osName = System.getProperty("os.name");
            float osVersion = Float.parseFloat(System.getProperty("os.version"));
            String userHome = System.getProperty("user.home");

            // OS �����ж�
            // Windows 2003
            if (osName.startsWith("Windows"))
            {
                // [Windows 2000, Windows 2003 R2]
                if (osVersion < 5.9)
                    return(JUkaUtility.userConfDir = userHome + "/Application Data/jukagaka");
                // [Windows Vista, Windows 8]
                if (osVersion < 6.21)
                    return(JUkaUtility.userConfDir = userHome + "/AppData/jukagaka");
            }
            // Linux
            // TODO: ������

            // Unknown OS
            JUkaUtility.userConfDir = userHome + "/jukagaka";
            System.err.println("Warn:JUkaUtility.getUserConf():OS�ж�����, �ٶ��û��趨Ŀ¼: " + JUkaUtility.userConfDir);
        }

        return(JUkaUtility.userConfDir);
    }


  // Ini File | ini �ļ���ȡ
  // �ṩ������ ini �ļ���ȡ����
    /**
     * <p>��ָ���� ini �ļ��ж�λָ���Ľ�</p>
     * <p>��Ҫ��Ϊ�˿��ٲ���λ�ù����� API ���������ȡ�õ�</p>
     * @return ָ������ڵ���һ�����׵� Scanner, ���ָ���ļ���ڲ������򷵻� null.
     */
    public static Scanner iniLocateSector(String argFileName, String argSector)
    {
        File iniFile = null;
        Scanner iniScanner = null;

        // ��ͼ��ȡ�ļ�
        try
        {
            iniFile = new File(argFileName);
            iniScanner = new Scanner(new FileInputStream(iniFile));
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("Error: JUkaUtility.iniLocatSector():ָ�����ļ�������: " + argFileName);
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
                return(iniScanner);
        }

        iniScanner.close();
        return(null);
    }

    /**
     * <p>�ӵ�ǰλ�ÿ�ʼö�ٽ��������ؼ�-ֵ��, �����ڽ���ʱ��ֹ</p>
     * <p>��������Ϊ iniReadSector() ���ص���������������Բ�д�����.<br>
     * (*) ÿ�ε��øú�������ʹ Scanner �Ķ�ȡλ�ú���<br>
     * (*) ���������� null ����ȡ�����ʱ, ���ر���Ϊ������ Scanner</p>
     * @return ����һ����Ԫ String ����, String[0] ��ʾ��, String[1] ��ʾֵ, �����ö�ٵ���ĩβ�򷵻� null.
     */
    public static String[] iniEnumSector(Scanner argIniScanner)
    {
        String[] rtString = null;
        String buffer = null;

        // �ļ�ĩβʱ���� null
        while (argIniScanner.hasNextLine())
        {
            buffer = argIniScanner.nextLine();

            // ��ʽ����ע����
            if (buffer.matches("\\s*;.*"))
                continue;

            // ��ĩβʱ���� null
            if (buffer.matches("\\s*\\[.*\\]"))
                break;

            // �� = Ϊ��׼ƥ���-ֵ��
            // ����βȥ�ո�, ֵ\nת��Ϊ�س���
            if (buffer.matches("\\s*(.+)=(.*)$"))
            {
                rtString = buffer.split("=",2);
                rtString[0] = rtString[0].trim();
                rtString[1] = rtString[1].replaceAll("\\n","\n");
                return(rtString);
            }

            //// �����޷�ʶ����, �������ִ��
            //System.err.println("�Թ��޷�ʶ���ini����: " + buffer);
        }

        argIniScanner.close();
        return(null);
    }

    /**
     * <p>��ָ���� ini �ļ��ж�ȡ������</p>
     * @return �����ȡ�ĽڵĹ�ϣ��, ���ָ���ļ���ڲ������򷵻� null,
     * ���ָ����Ϊ���򷵻�һ���յĹ�ϣ��.
     */
    public static Hashtable<String, String> iniReadSector(String argFileName, String argSector)
    {
        Scanner iniScanner = JUkaUtility.iniLocateSector(argFileName, argSector);

        // ��������ȷʱ���� null.
        if (iniScanner == null)
            return(null);

        Hashtable<String, String> rtHtSector = new Hashtable<String, String>();
        String[] buffer = null;

        // ������ȡ
        while ((buffer=iniEnumSector(iniScanner)) != null)
            rtHtSector.put(buffer[0],buffer[1]);

        return(rtHtSector);
    }

    /**
     * <p>��ָ���� ini �ļ��ж�ȡָ���ںͼ���ֵ</p>
     * <p>�򵥵�˵����:<br>
     * [argSector]<br>
     * argKey=Value</p>
     * ��֮, ���ص��� Value �Ͷ���.</p>
     * @return һ���ַ�����ʾ�������ֵ, ���û��������ֵ�򷵻� null;
     */
    public static String iniRead(String argFileName, String argSector, String argKey)
    {
        Scanner iniScanner = JUkaUtility.iniLocateSector(argFileName, argSector);

        // ��������ȷʱ���� null.
        if (iniScanner == null)
            return(null);

        String[] buffer = null;

        // ������ȡ���ж�
        while ((buffer=iniEnumSector(iniScanner)) != null)
            if (buffer[0].equals(argKey))
            {
                iniScanner.close();
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
        System.out.println(JUkaUtility.getProgramDir());

        // �˴�������ڲ��� ��ȡini
        //JUkaUtility.iniLocateSector(JUkaUtility.getProgramDir() + "/shell/SampleShell/SampleShell.ini", "images");
        ht = JUkaUtility.iniReadSector(
            JUkaUtility.getProgramDir() + "/shell/cyaushell/SampleShell.ini",
            "images"
        );
        System.out.println(
            JUkaUtility.iniRead(
                JUkaUtility.getProgramDir() + "/shell/cyaushell/SampleShell.ini",
                "images",
                "balloonbk,0,0,0"
            )
        );

        return;
    }
}
