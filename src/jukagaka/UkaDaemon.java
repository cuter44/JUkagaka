/**
 * src/jukagaka/UkaDaemon.java
 * @author: "galin"<w617443801@gmail.com>
 *
 * {唯一}
 * 守护线程
 */

package jukagaka;

/* 输入输出 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
/* 标准数据结构 */
import java.util.HashSet;
import java.util.Properties;
import java.util.Iterator;
/* 字符串处理 */
import java.util.StringTokenizer;
/* 反射 */
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
/* jukagaka */
//import jukagaka.UkaComponent;
/* end_import */

public class UkaDaemon implements UkaComponent
{
  // Path | 路径
  // 获取用户配置文件夹和程序装载目录
  // 数据
    /**
     * 缓存程序的绝对路径, 由 getProgramDir() 生成和获取.
     */
    private static String programDir = null;
    /**
     * 缓存用户程序设定的绝对路径, 由 getUserConfDir() 生成和获取.
     */
    private static String userConfDir = null;
  // 代码
    /**
     * 此方法生成和返回程序的根目录(programDir)<br />
     * <br />
     * 如果...一定要给出一个例子的话: "/F:/project/v120826/jukagaka"<br />
     * @return 返回程序的Unix式绝对路径
     */
    public static String getProgramDir()
    {
        // 首次执行时生成属性, 再次执行时返回缓存的属性
        if (programDir == null)
            try
            {
                programDir = Class.forName("jukagaka.UkaDaemon").getClassLoader().getResource("").getPath();
                // 除去末尾的/
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
     * 获取用户设置目录(userConfDir)<br />
     *
     * 可以在这里创建关联到计算机用户文件存储, 以 File.mkdirs() 可以建立自己的
     * 存贮目录(通过命名域避免重名)<br />
     * 如果只是要存储简单的文本参数, 请使用标准参数池<br />
     * 给出的路径<strong>使用相应操作系统的习惯表示</strong><br />
     */
    public static String getUserConfDir()
    {
        // 首次执行时生成属性, 再次执行时返回缓存的属性
        if (userConfDir == null)
        {
            String osName = System.getProperty("os.name");
            float osVersion = Float.parseFloat(System.getProperty("os.version"));
            String userHome = System.getProperty("user.home");

            // OS 种类判定
            // Windows
            if (osName.startsWith("Windows"))
            {
                userConfDir = System.getenv("appdata") + "\\JUkagaka";

                return(userConfDir);
            }
            // Linux
            // TODO: 待补充

            // Unknown OS
            userConfDir = userHome + "/jukagaka";
            System.err.println("Warn:UkaDaemon.getUserConf():OS判定不能,使用failed-default设定:" + UkaDaemon.userConfDir);
        }

        return(userConfDir);
    }

  // ============================================
  // Configurations | 配置项
  // 为配置项提供 读, 写, 缓取 支持.
    // 该组函数为 Ukagaka 核心组件提供配置文件的存取支持, 非标准组件也可以
    // 利用这些函数进行将自己的配置托管到标准配置文件中(但要注意命名域).
    // 也可以仅利用封装好的文件读写功能(saveConf(), loadConf(),
    // 它们在标准的 load()/store() 之上增加自动创建文件的功能且隐藏了异常)
    // 关于命名域, 请以 组件名.配置名 的格式来命名配置项的键, Ukagaka 核心
    //的命名域为 Uka.
  // 数据
    /**
     * 全局/用户参数库文件名
     */
    private static final String globalConfFileName = getProgramDir() + "/UkaConf.properties";
    private static final String userConfFileName = getUserConfDir() + "/UkaConf.properties";
    /**
     * 全局/用户参数库
     */
    private static Properties globalConf = new Properties();
    private static Properties userConf = new Properties();
  // 代码
    /**
     * 从<strong>标准配置</strong>文件中提取配置值<br />
     * <br />
     * 对于具有相同键名的值, 用户配置优先于全局配置.<br />
     * @param strKey 请求的配置项的键名
     * @return 配置项的值, 如果没有相应的键, 则返回 null.
     */
    public static String getConf(String strKey)
    {
        String strValue;

        // 查询用户变量表
        strValue = userConf.getProperty(strKey);
        // 查询全局变量表
        if (strValue == null)
            strValue = globalConf.getProperty(strKey);

        return(strValue);
    }

    /**
     * 写入相应键值对到全局<strong>标准配置</strong>文件<br />
     * <br />
     * 写入的配置项会被缓冲在内存, 在 saveConf() 之前不会被写入文件.<br />
     * @param strKey 要增加的键
     * @param strValue 要增加的值
     * @return (覆盖)如果写入的键值对存在, 返回其旧的值; (新增)否则返回 null.
     */
    public static String setConfGlobal(String strKey, String strValue)
    {
        return((String)globalConf.setProperty(strKey, strValue));
    }

    /**
     * 写入相应键值对到用户<strong>标准配置</strong>文件<br />
     * <br />
     * 写入的配置项会被缓冲在内存, 在 saveConf() 之前不会被写入文件.<br />
     * 此处用户是指作业系统用户.<br />
     * @param strKey 要增加的键
     * @param strValue 要增加的值
     * @return (覆盖)如果写入的键值对存在, 返回其旧的值; (新增)否则返回 null.
     */
    public static String setConfUser(String strKey, String strValue)
    {
        return((String)userConf.setProperty(strKey, strValue));
    }

    /**
     * 从全局<strong>标准配置</strong>文件清除配置项<br />
     * <br />
     * 清除指定的键和值, 也就是使对配置表调用 getConf(strKey) 时返回 null
     * @param strKey 要删除的键
     * @return strKey 所存储的值, 如果指定的键名不存在则返回 null
     */
    public static String removeConfGlobal(String strKey)
    {
        return((String)globalConf.remove(strKey));
    }

    /**
     * 从用户<strong>标准配置</strong>文件清除配置项<br />
     * <br />
     * 清除指定的键和值, 也就是使对配置表调用 getConf(strKey) 时返回 null
     * @param strKey 要删除的键
     * @return strKey 所存储的值, 如果指定的键名不存在则返回 null
     */
    public static String removeConfUser(String strKey)
    {
        return((String)userConf.remove(strKey));
    }

    /**
     * 将某个配置表保存到文件<br />
     * <br />
     * 一个文件内只能容纳一个配置表<br />
     * @param confFileName 要写入的文件名, 如果目标路径不存在, 则自动创建.
     * @param prop 要写入的配置表
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
     * 从文件读入某个配置表<br />
     * <br />
     * 如果指定了某些奇怪格式的文件, 这个函数不负任何责任<br />
     * @param confFileName 要写入的文件名, 如果目标路径不存在, 则自动创建. 并返回一个空表.
     * @param prop 要写入的配置表
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
     * 保存标准配置
     */
    private static void saveConf()
    {
        saveConf(globalConfFileName, globalConf);
        saveConf(userConfFileName, userConf);
    }

    /**
     * 读取标准配置
     */
    private static void loadConf()
    {
        loadConf(globalConfFileName, globalConf);
        loadConf(userConfFileName, userConf);
    }

  // ============================================
  // Avoidance | 封印
  // 不可以使用的东东
    private UkaDaemon()
    {
        return;
    }

  // ============================================
  // Components | 组件
  // 监察和控制各个部件的运行状况
  // 数据
    private static int COMP_LEVELS = 5;
    /**
     * 当前加载到环境的组件名字表<br />
     * <br />
     * compList[0] 装载 Shell 的列表<br />
     */
    @SuppressWarnings("unchecked")
    private static HashSet<String> compList[] = new HashSet[COMP_LEVELS];
    /**
     * [字符串资源]用于读写组件列表
     */
    private static final String strCompListKey[] = {"Uka.Comp.SysList",
        "Uka.Comp.ShellList","Uka.Comp.GhostList","Uka.Comp.PluginList","Uka.Comp.Runtime"};
  // 代码
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

        // 展开加载项
        for (i=0; i<COMP_LEVELS; i++)
        {
            compList[i] = new HashSet<String>();

            // 析出字串
            String strCompList = getConf(strCompListKey[i]);
            if (strCompList == null)
                strCompList = "";

            // 分解字串
            StringTokenizer strSpilt = new StringTokenizer(strCompList, " [],");
            while (strSpilt.hasMoreTokens())
                compList[i].add(strSpilt.nextToken());
        }

        // 回调 onLoad()
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
                    // 析出组件名字
                    compClass = Class.forName(compName);
                    compMethod = compClass.getMethod("onLoad");
                    // 回调
                    rtValue = (Boolean)compMethod.invoke(null);
                }
                catch (Exception ex)
                {
                    System.err.println("error:UkaDaemon.onLoad():因为发生错误已中止加载组件:" + compName);
                    ex.printStackTrace();
                    rtValue = Boolean.FALSE;
                }

                if (rtValue.equals(Boolean.FALSE))
                {
                    System.err.println("note:UkaDaemon.onLoad():因为发生错误或组件积极拒绝, 已中止加载组件:" + compName);
                    iter.remove();
                }
            }
        }

        return(true);
    }

    protected static boolean onStart()
    {
        int i;

        // 回调 onStart()
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
                    // 析出组件名字
                    compClass = Class.forName(compName);
                    compMethod = compClass.getMethod("onStart");
                    // 回调
                    rtValue = (Boolean)compMethod.invoke(null);
                }
                catch (Exception ex)
                {
                    System.err.println("error:UkaDaemon.onStart():因为发生错误已中止加载组件:" + compName);
                    ex.printStackTrace();
                    rtValue = Boolean.FALSE;
                }

                if (rtValue.equals(Boolean.FALSE))
                {
                    System.err.println("note:UkaDaemon.onStart():因为发生错误或组件积极拒绝, 已中止加载组件:" + compName);
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

        // 回调 onExit()
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
                    // 析出组件名字
                    compClass = Class.forName(compName);
                    compMethod = compClass.getMethod("onExit");
                    // 回调
                    rtValue = (Boolean)compMethod.invoke(null);
                }
                catch (Exception ex)
                {
                    System.err.println("error:UkaDaemon.onExit():因为发生错误无法停止组件:" + compName);
                    ex.printStackTrace();
                    rtValue = Boolean.FALSE;
                }

                if (rtValue.equals(Boolean.FALSE))
                {
                    System.err.println("error:UkaDaemon.onExit():因为发生错误或组件积极拒绝, 无法停止组件:" + compName);
                }
                else
                {
                    iter.remove();
                }
            }
            // end_while
        }
        // end_for

        // 保存配置项
        saveConf();

        return(true);
    }

    /**
     * 要求加载一个组件<br />
     * <br />
     * 只是简单地执行一个组件的onLoad(), onStart()方法, 假如均返回true则将其加入
     * 正在运行的列表中<br />
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
            System.err.println("error:UkaDaemon.loadComp():因为发生错误或组件积极拒绝, 无法启动组件:" + compName);
            ex.printStackTrace();
        }

        return(rtValue);
    }

    /**
     * 要求停止一个组件<br />
     * <br />
     * 只是简单地执行一个组件的onExit(), 假如返回true则将其从
     * 正在运行的列表中去除<br />
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
            System.err.println("error:UkaDaemon.unloadComp():因为发生错误或组件积极拒绝, 无法停止组件:" + compName);
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
  // Dependence | 依赖关系
  // 组件可以查询其他组件是否已加载
  // 代码
    /**
     * 检查某组件是否已加载<br />
     * <br />
     * 仅仅是在已加载列表中检查指定的类名, 而不管其处于何种状态<br />
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
  // Debug | 调试
  // 用于开发时调试用的函式
  // 数据
  // 代码
    /**
     * 测试用 main()
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
     * 演示用
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
