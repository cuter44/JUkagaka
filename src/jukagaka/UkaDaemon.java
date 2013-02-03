/**
 * src/jukagaka/UkaDaemon.java
 * @author: "galin"<w617443801@gmail.com>
 *
 * {Ψһ}
 * �ػ��߳�
 */

package jukagaka;

/* ������� */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
/* ��׼���ݽṹ */
import java.util.Properties;
/* �������� */
//import jukagaka.UkaComponent;
/* end_import */

public class UkaDaemon implements UkaComponent
{
  // Path | ·��
  // ��ȡ�û������ļ��кͳ���װ��Ŀ¼
  // ����
    /**
     * �������ľ���·��, �� getProgramDir() ���ɺͻ�ȡ.
     */
    private static String programDir = null;
    /**
     * �����û������趨�ľ���·��, �� getUserConfDir() ���ɺͻ�ȡ.
     */
    private static String userConfDir = null;

  // ����
    /**
     * �˷������ɺͷ��س���ĸ�Ŀ¼(programDir)<br />
     * <br />
     * ���...һ��Ҫ����һ�����ӵĻ�: "/F:/project/v120826/jukagaka"<br />
     * @return ���س����Unixʽ����·��
     */
    public static String getProgramDir()
    {
        // �״�ִ��ʱ��������, �ٴ�ִ��ʱ���ػ��������
        if (programDir == null)
            try
            {
                programDir = Class.forName("jukagaka.UkaDaemon").getClassLoader().getResource("").getPath();
                // ��ȥĩβ��/
                programDir = programDir.substring(0,programDir.length()-1);
            }
            catch (Exception ex)
            {
                System.err.println(ex);
                return(null);
            }
        return(programDir);
    }

    /**
     * ��ȡ�û�����Ŀ¼(userConfDir)<br />
     *
     * ���������ﴴ��������������û��ļ��洢, �� File.mkdirs() ���Խ����Լ���
     * ����Ŀ¼(ͨ���������������)<br />
     * ���ֻ��Ҫ�洢�򵥵��ı�����, ��ʹ�ñ�׼������<br />
     * ������·��<strong>ʹ����Ӧ����ϵͳ��ϰ�߱�ʾ</strong><br />
     */
    public static String getUserConfDir()
    {
        // �״�ִ��ʱ��������, �ٴ�ִ��ʱ���ػ��������
        if (userConfDir == null)
        {
            String osName = System.getProperty("os.name");
            float osVersion = Float.parseFloat(System.getProperty("os.version"));
            String userHome = System.getProperty("user.home");

            // OS �����ж�
            // Windows
            if (osName.startsWith("Windows"))
            {
                userConfDir = System.getenv("appdata") + "\\JUkagaka";
                // [Windows 2000, Windows 2003 R2]
                //if (osVersion < 5.9)
                    //userConfDir = userHome + "\\Application Data\\jukagaka";
                //// [Windows Vista, Windows 8]
                //if (osVersion < 6.21)
                    //userConfDir = userHome + "\\AppData\\Local\\jukagaka";

                return(userConfDir);
            }
            // Linux
            // TODO: ������

            // Unknown OS
            userConfDir = userHome + "/jukagaka";
            System.err.println("Warn:UkaDaemon.getUserConf():OS�ж�����,ʹ��failed-default�趨:" + UkaDaemon.userConfDir);
        }

        return(userConfDir);
    }

  // ============================================
  // Configurations | ������
  // Ϊ�������ṩ ��, д, ��ȡ ֧��.
    // ���麯��Ϊ Ukagaka ��������ṩ�����ļ��Ĵ�ȡ֧��, �Ǳ�׼���Ҳ����
    // ������Щ�������н��Լ��������йܵ���׼�����ļ���(��Ҫע��������).
    // Ҳ���Խ����÷�װ�õ��ļ���д����(saveConf(), loadConf(),
    // �����ڱ�׼�� load()/store() ֮�������Զ������ļ��Ĺ������������쳣)
    // ����������, ���� �����.������ �ĸ�ʽ������������ļ�, Ukagaka ����
    //��������Ϊ Uka.
  // ����
    /**
     * ȫ��/�û��������ļ���
     */
    private static final String globalConfFileName = getProgramDir() + "/UkaConf.properties";
    private static final String userConfFileName = getUserConfDir() + "/UkaConf.properties";
    /**
     * ȫ��/�û�������
     */
    private static Properties globalConf = new Properties();
    private static Properties userConf = new Properties();

  // ����
    /**
     * ��<strong>��׼����</strong>�ļ�����ȡ����ֵ<br />
     * <br />
     * ���ھ�����ͬ������ֵ, �û�����������ȫ������.<br />
     * @param strKey �����������ļ���
     * @return �������ֵ, ���û����Ӧ�ļ�, �򷵻� null.
     */
    public static String getConf(String strKey)
    {
        String strValue;

        // ��ѯ�û�������
        strValue = userConf.getProperty(strKey);
        // ��ѯȫ�ֱ�����
        if (strValue == null)
            strValue = userConf.getProperty(strKey);

        return(strValue);
    }

    /**
     * д����Ӧ��ֵ�Ե�ȫ��<strong>��׼����</strong>�ļ�<br />
     * <br />
     * д���������ᱻ�������ڴ�, �� saveConf() ֮ǰ���ᱻд���ļ�.<br />
     * @param strKey Ҫ���ӵļ�
     * @param strValue Ҫ���ӵ�ֵ
     * @return (����)���д��ļ�ֵ�Դ���, ������ɵ�ֵ; (����)���򷵻� null.
     */
    public static String setConfGlobal(String strKey, String strValue)
    {
        return((String)globalConf.setProperty(strKey, strValue));
    }

    /**
     * д����Ӧ��ֵ�Ե��û�<strong>��׼����</strong>�ļ�<br />
     * <br />
     * д���������ᱻ�������ڴ�, �� saveConf() ֮ǰ���ᱻд���ļ�.<br />
     * �˴��û���ָ��ҵϵͳ�û�.<br />
     * @param strKey Ҫ���ӵļ�
     * @param strValue Ҫ���ӵ�ֵ
     * @return (����)���д��ļ�ֵ�Դ���, ������ɵ�ֵ; (����)���򷵻� null.
     */
    public static String setConfUser(String strKey, String strValue)
    {
        return((String)userConf.setProperty(strKey, strValue));
    }

    /**
     * ��ĳ�����ñ��浽�ļ�<br />
     * <br />
     * һ���ļ���ֻ������һ�����ñ�<br />
     * @param confFileName Ҫд����ļ���, ���Ŀ��·��������, ���Զ�����.
     * @param prop Ҫд������ñ�
     */
    public static void saveConf(String confFileName, Properties prop)
    {
        try
        {
            File confFile = new File(confFileName);
            File confDir = new File(confFile.getParent());

            if (!confDir.exists())
                confDir.mkdirs();

            if (!confFile.exists())
                confFile.createNewFile();

            prop.store(new FileOutputStream(confFileName) ,null);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return;
    }

    /**
     * ���ļ�����ĳ�����ñ�<br />
     * <br />
     * ���ָ����ĳЩ��ָ�ʽ���ļ�, ������������κ�����<br />
     * @param confFileName Ҫд����ļ���, ���Ŀ��·��������, ���Զ�����. ������һ���ձ�.
     * @param prop Ҫд������ñ�
     */
    public static void loadConf(String confFileName, Properties prop)
    {
        try
        {
            File confFile = new File(confFileName);
            File confDir = new File(confFile.getParent());

            if (!confDir.exists())
                confDir.mkdirs();

            if (!confFile.exists())
                confFile.createNewFile();

            prop.load(new FileInputStream(confFileName));
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return;
    }

    /**
     * �����׼����
     */
    private static void saveConf()
    {
        saveConf(globalConfFileName, globalConf);
        saveConf(userConfFileName, userConf);
    }

    /**
     * ��ȡ��׼����
     */
    private static void loadConf()
    {
        loadConf(globalConfFileName, globalConf);
        loadConf(userConfFileName, userConf);
    }

  // ============================================
  // Avoidance | ��ӡ
  // ������ʹ�õĶ���
    private UkaDaemon()
    {
        return;
    }

  // ============================================
  // Components | ���
  // ���Ϳ��Ƹ�������������״��
    protected static boolean onLoad()
    {
        return(true);
    }

    protected static boolean onStart()
    {
        return(true);
    }

    protected static boolean onExit()
    {
        return(true);
    }

  // ============================================
  // Debug | ����
  // ���ڿ���ʱ�����õĺ�ʽ
  // ����

  // ����
    /**
     * ������ main()
     */
    public static void main(String[] args)
    {
        dumpDirs();
        dumpConf();
        return;
    }

    /**
     * ��ʾ��
     */
    private static void sample()
    {
        return;
    }

    private static void dumpConf()
    {
        loadConf();
        setConfGlobal("testConfiguration","global");
        setConfUser("testConfiguration","user");
        System.out.println("getConf(\"testConfiguration\")=" + getConf("testConfiguration"));
        saveConf();
    }

    private static void dumpDirs()
    {
        System.out.println("getProgramDir()=" + getProgramDir());
        System.out.println("getUserConfDir()=" + getUserConfDir());

        return;
    }

  // ============================================
}
// end_class_UkaDaemon
