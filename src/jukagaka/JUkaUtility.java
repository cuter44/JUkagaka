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
  // Environment | 环境
    /**
     * 此数据域记录程序的绝对路径, 由 getProgramDir() 生成和获取.
     */
    private static String programDir = null;

  // Environment | 环境
    /**
     * <p>此方法生成和返回 programPath</p>
     * <p>此方法本来包含于 JUkaStage 中, 但为了能正确编译而被分离出来<br>
     * 此方法返回 Unix 规范的路径, 纵使它在 Windows 平台上运行<br>
     * 如果...一定要给出一个例子的话: "/F:/project/v120826/jukagaka"</p>
     * @return 返回程序的Unix式绝对路径
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
     * 此数据域记录用户相关设定的绝对路径, 由 getProgramDir() 生成和获取.
     */
    private static String userConfDir = null;

    /**
     * <p>此方法生成和返回 userConfDir</p>
     * <p>为了取得良好管理性, 建议在 userConfDir 下建子文件夹存放数据,
     * 相关内容请参见 JAPI 中 File.mkdirs() 描述.<br>
     * 注意 userConfPath 指定的目录不一定存在于实际文件系统上, 但 mkdirs()
     * 已经为此问题给出解决方案.</p>
     */
    public static String getUserConfDir()
    {
        if (JUkaUtility.userConfDir == null)
        {
            String osName = System.getProperty("os.name");
            float osVersion = Float.parseFloat(System.getProperty("os.version"));
            String userHome = System.getProperty("user.home");

            // OS 种类判定
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
            // TODO: 待补充

            // Unknown OS
            JUkaUtility.userConfDir = userHome + "/jukagaka";
            System.err.println("Warn:JUkaUtility.getUserConf():OS判定不能, 假定用户设定目录: " + JUkaUtility.userConfDir);
        }

        return(JUkaUtility.userConfDir);
    }


  // Ini File | ini 文件读取
  // 提供基本的 ini 文件读取功能
    /**
     * <p>从指定的 ini 文件中定位指定的节</p>
     * <p>主要是为了快速查找位置供其他 API 或第三方读取用的</p>
     * @return 指向请求节的下一行行首的 Scanner, 如果指定文件或节不存在则返回 null.
     */
    public static Scanner iniLocateSector(String argFileName, String argSector)
    {
        File iniFile = null;
        Scanner iniScanner = null;

        // 试图读取文件
        try
        {
            iniFile = new File(argFileName);
            iniScanner = new Scanner(new FileInputStream(iniFile));
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("Error: JUkaUtility.iniLocatSector():指定的文件不存在: " + argFileName);
            return(null);
        }
        catch (Exception ex)
        {
            System.err.println(ex);
            return(null);
        }

        // 逐行检索, 在找到所需 Sector 时返回
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
     * <p>从当前位置开始枚举解析并返回键-值对, 遇到节结束时终止</p>
     * <p>纯粹是因为 iniReadSector() 返回的数据是无序的所以才写的这个.<br>
     * (*) 每次调用该函数都将使 Scanner 的读取位置后移<br>
     * (*) 当函数返回 null 即读取节完成时, 将关闭作为参数的 Scanner</p>
     * @return 返回一个二元 String 数组, String[0] 表示键, String[1] 表示值, 如果已枚举到段末尾则返回 null.
     */
    public static String[] iniEnumSector(Scanner argIniScanner)
    {
        String[] rtString = null;
        String buffer = null;

        // 文件末尾时返回 null
        while (argIniScanner.hasNextLine())
        {
            buffer = argIniScanner.nextLine();

            // 显式跳过注释行
            if (buffer.matches("\\s*;.*"))
                continue;

            // 节末尾时返回 null
            if (buffer.matches("\\s*\\[.*\\]"))
                break;

            // 以 = 为基准匹配键-值对
            // 键首尾去空格, 值\n转换为回车符
            if (buffer.matches("\\s*(.+)=(.*)$"))
            {
                rtString = buffer.split("=",2);
                rtString[0] = rtString[0].trim();
                rtString[1] = rtString[1].replaceAll("\\n","\n");
                return(rtString);
            }

            //// 警告无法识别行, 但不打断执行
            //System.err.println("略过无法识别的ini内容: " + buffer);
        }

        argIniScanner.close();
        return(null);
    }

    /**
     * <p>从指定的 ini 文件中读取整个节</p>
     * @return 请求读取的节的哈希表, 如果指定文件或节不存在则返回 null,
     * 如果指定节为空则返回一个空的哈希表.
     */
    public static Hashtable<String, String> iniReadSector(String argFileName, String argSector)
    {
        Scanner iniScanner = JUkaUtility.iniLocateSector(argFileName, argSector);

        // 参数不正确时返回 null.
        if (iniScanner == null)
            return(null);

        Hashtable<String, String> rtHtSector = new Hashtable<String, String>();
        String[] buffer = null;

        // 迭代读取
        while ((buffer=iniEnumSector(iniScanner)) != null)
            rtHtSector.put(buffer[0],buffer[1]);

        return(rtHtSector);
    }

    /**
     * <p>从指定的 ini 文件中读取指定节和键的值</p>
     * <p>简单地说就是:<br>
     * [argSector]<br>
     * argKey=Value</p>
     * 总之, 返回的是 Value 就对了.</p>
     * @return 一个字符串表示所请求的值, 如果没有这样的值则返回 null;
     */
    public static String iniRead(String argFileName, String argSector, String argKey)
    {
        Scanner iniScanner = JUkaUtility.iniLocateSector(argFileName, argSector);

        // 参数不正确时返回 null.
        if (iniScanner == null)
            return(null);

        String[] buffer = null;

        // 迭代读取并判断
        while ((buffer=iniEnumSector(iniScanner)) != null)
            if (buffer[0].equals(argKey))
            {
                iniScanner.close();
                return(buffer[1]);
            }

        return(null);
    }

  // Other | 其他
    /**
     * @deprecated 此方法目前仅用于调试
     */
    public static void main(String[] args)
    {
        FileInputStream iniStream = null;
        Hashtable<String, String> ht = null;

        // 此代码段用于测试 获取程序路径
        System.out.println(JUkaUtility.getProgramDir());

        // 此代码段用于测试 读取ini
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
