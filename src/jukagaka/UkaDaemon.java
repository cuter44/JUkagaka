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
import java.util.HashSet;
import java.util.Properties;
import java.util.Iterator;
/* �ַ������� */
import java.util.StringTokenizer;
/* ���� */
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
/* jukagaka */
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
            strValue = globalConf.getProperty(strKey);

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
     * ��ȫ��<strong>��׼����</strong>�ļ����������<br />
     * <br />
     * ���ָ���ļ���ֵ, Ҳ����ʹ�����ñ���� getConf(strKey) ʱ���� null
     * @param strKey Ҫɾ���ļ�
     * @return strKey ���洢��ֵ, ���ָ���ļ����������򷵻� null
     */
    public static String removeConfGlobal(String strKey)
    {
        return((String)globalConf.remove(strKey));
    }

    /**
     * ���û�<strong>��׼����</strong>�ļ����������<br />
     * <br />
     * ���ָ���ļ���ֵ, Ҳ����ʹ�����ñ���� getConf(strKey) ʱ���� null
     * @param strKey Ҫɾ���ļ�
     * @return strKey ���洢��ֵ, ���ָ���ļ����������򷵻� null
     */
    public static String removeConfUser(String strKey)
    {
        return((String)userConf.remove(strKey));
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
  // ����
    private static int COMP_LEVELS = 5;
    /**
     * ��ǰ���ص�������������ֱ�<br />
     * <br />
     * compList[0] װ�� Shell ���б�<br />
     */
    @SuppressWarnings("unchecked")
    private static HashSet<String> compList[] = new HashSet[COMP_LEVELS];
    /**
     * [�ַ�����Դ]���ڶ�д����б�
     */
    private static final String strCompListKey[] = {"Uka.Comp.SysList",
        "Uka.Comp.ShellList","Uka.Comp.GhostList","Uka.Comp.PluginList","Uka.Comp.Runtime"};
  // ����
    //@SuppressWarnings("unchecked")
    protected static boolean onLoad()
    {
        int i;

        loadConf();

        File userConfFile = new File(userConfFileName);
        if (!userConfFile.exists())
        {
            // TODO: treat new user.
        }

        // չ��������
        for (i=0; i<COMP_LEVELS; i++)
        {
            compList[i] = new HashSet<String>();

            // �����ִ�
            String strCompList = getConf(strCompListKey[i]);
            if (strCompList == null)
                strCompList = "";

            // �ֽ��ִ�
            StringTokenizer strSpilt = new StringTokenizer(strCompList, " [],");
            while (strSpilt.hasMoreTokens())
                compList[i].add(strSpilt.nextToken());
        }

        // �ص� onLoad()
        for (i=0; i<COMP_LEVELS; i++)
        {
            Iterator<String> iter = compList[i].iterator();

            while (iter.hasNext())
            {
                String compName = iter.next();
                Class<?> compClass = null;
                Method compMethod = null;
                Boolean rtValue = Boolean.FALSE;

                try
                {
                    // �����������
                    compClass = Class.forName(compName);
                    compMethod = compClass.getMethod("onLoad");
                    // �ص�
                    rtValue = (Boolean)compMethod.invoke(null);
                }
                catch (Exception ex)
                {
                    System.err.println("error:UkaDaemon.onLoad():��Ϊ������������ֹ�������:" + compName);
                    ex.printStackTrace();
                    rtValue = Boolean.FALSE;
                }

                if (rtValue.equals(Boolean.FALSE))
                {
                    System.err.println("note:UkaDaemon.onLoad():��Ϊ�����������������ܾ�, ����ֹ�������:" + compName);
                    iter.remove();
                }
            }
        }

        return(true);
    }

    protected static boolean onStart()
    {
        int i;

        // �ص� onStart()
        for (i=0; i<=COMP_LEVELS; i++)
        {
            Iterator<String> iter = compList[i].iterator();

            while (iter.hasNext())
            {
                String compName = iter.next();
                Class<?> compClass = null;
                Method compMethod = null;
                Boolean rtValue = Boolean.FALSE;

                try
                {
                    // �����������
                    compClass = Class.forName(compName);
                    compMethod = compClass.getMethod("onStart");
                    // �ص�
                    rtValue = (Boolean)compMethod.invoke(null);
                }
                catch (Exception ex)
                {
                    System.err.println("error:UkaDaemon.onStart():��Ϊ������������ֹ�������:" + compName);
                    ex.printStackTrace();
                    rtValue = Boolean.FALSE;
                }

                if (rtValue.equals(Boolean.FALSE))
                {
                    System.err.println("note:UkaDaemon.onStart():��Ϊ�����������������ܾ�, ����ֹ�������:" + compName);
                    iter.remove();
                    continue;
                }
            }
            // end_while
        }
        // end_for

        return(true);
    }

    protected static boolean onExit()
    {
        int i;

        // �ص� onExit()
        for (i=COMP_LEVELS; i>=0; i++)
        {
            Iterator<String> iter = compList[i].iterator();

            while (iter.hasNext())
            {
                String compName = iter.next();
                Class<?> compClass = null;
                Method compMethod = null;
                Boolean rtValue = Boolean.FALSE;

                try
                {
                    // �����������
                    compClass = Class.forName(compName);
                    compMethod = compClass.getMethod("onExit");
                    // �ص�
                    rtValue = (Boolean)compMethod.invoke(null);
                }
                catch (Exception ex)
                {
                    System.err.println("error:UkaDaemon.onExit():��Ϊ���������޷�ֹͣ���:" + compName);
                    ex.printStackTrace();
                    rtValue = Boolean.FALSE;
                }

                if (rtValue.equals(Boolean.FALSE))
                {
                    System.err.println("error:UkaDaemon.onExit():��Ϊ�����������������ܾ�, �޷�ֹͣ���:" + compName);
                }
                else
                {
                    iter.remove();
                }
            }
            // end_while
        }
        // end_for

        // ����������
        saveConf();

        return(true);
    }

    /**
     * Ҫ�����һ�����<br />
     * <br />
     * ֻ�Ǽ򵥵�ִ��һ�������onLoad(), onStart()����, ���������true�������
     * �������е��б���<br />
     */
    public static boolean loadComp(String compName, int compLevel)
    {
        if ((compLevel < 0) || (compLevel >= COMP_LEVELS))
            return(false);

        if (isLoaded(compName))
            return(false);

        Class<?> compClass = null;
        Method compMethod = null;
        Boolean rtValue = Boolean.FALSE;

        try
        {
            compClass = Class.forName(compName);
            compMethod = compClass.getMethod("onLoad");
            rtValue = (Boolean)compMethod.invoke(null);
            if (rtValue.equals(Boolean.TRUE))
            {
                compMethod = compClass.getMethod("onStart");
                rtValue = (Boolean)compMethod.invoke(null);
            }
        }
        catch (Exception ex)
        {
            System.err.println("error:UkaDaemon.loadComp():��Ϊ�����������������ܾ�, �޷��������:" + compName);
            ex.printStackTrace();
        }

        return(rtValue);
    }

    /**
     * Ҫ��ֹͣһ�����<br />
     * <br />
     * ֻ�Ǽ򵥵�ִ��һ�������onExit(), ���緵��true�����
     * �������е��б���ȥ��<br />
     */
    public static boolean unloadComp(String compName)
    {
        if (!isLoaded(compName))
            return(false);

        Class<?> compClass = null;
        Method compMethod = null;
        Boolean rtValue = Boolean.FALSE;

        try
        {
            compClass = Class.forName(compName);
            compMethod = compClass.getMethod("onExit");
            rtValue = (Boolean)compMethod.invoke(null);
        }
        catch (Exception ex)
        {
            System.err.println("error:UkaDaemon.unloadComp():��Ϊ�����������������ܾ�, �޷�ֹͣ���:" + compName);
            ex.printStackTrace();
        }

        return(rtValue);
    }

    protected static boolean onInstall()
    {
        return(true);
    }

    protected static boolean onUnistall()
    {
        return(true);
    }

  // ============================================
  // Dependence | ������ϵ
  // ������Բ�ѯ��������Ƿ��Ѽ���
  // ����
    /**
     * ���ĳ����Ƿ��Ѽ���<br />
     * <br />
     * ���������Ѽ����б��м��ָ��������, �������䴦�ں���״̬<br />
     * @throws
     */
    public static boolean isLoaded(String compName)
    {
        int i;
        boolean rtValue = false;

        for (i=0; i<COMP_LEVELS; i++)
        {
            Iterator<String> iter = compList[i].iterator();
            while (iter.hasNext())
                if (compName.equals(iter.next()))
                    return(true);
        }
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

        onLoad();
        onStart();
        onExit();
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
