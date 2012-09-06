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
  //Static Data | 静态数据域
    /**
     * 此数据域记录程序的绝对路径, 由 getProgramPath() 生成和获取.
     */
    private static String programPath = null;

  // Environment | 环境
    /**
     * <p>此方法生成和返回 programPath</p>
     * <p>此方法本来包含于 JUkaStage 中, 但为了能正确编译而被分离出来<br>
     * 此方法返回 Unix 规范的路径, 纵使它在 Windows 平台上运行<br>
     * 如果...一定要给出一个例子的话: "/F:/project/v120826/jukagaka/"</p>
     * @return 返回程序的Unix式绝对路径
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

  // Ini File | ini 文件读取
  // 提供基本的 ini 文件读取功能
    /**
     * <p>从指定的 ini 文件中定位指定的节</p>
     * <p>主要是为了快速查找位置供其他 API 或第三方读取用的</p>
     * @return 已经指向指定位置的文件流, 如果指定文件或节不存在则返回 null.
     */
    public static FileInputStream iniLocate(String argFileName, String argSector)
    {
        File iniFile = null;
        FileInputStream iniStream = null;
        Scanner iniScanner = null;

        // 试图读取文件
        try
        {
            iniFile = new File(argFileName);
            iniStream = new FileInputStream(iniFile);
            iniScanner = new Scanner(new FileInputStream(iniFile));
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("指定的文件不存在: " + argFileName);
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
            {
                //iniScanner.close();
                return(iniStream);
            }
        }

        iniScanner.close();
        return(null);
    }

    /**
     * <p>从当前位置开始枚举解析并返回键-值对, 遇到节结束时终止</p>
     * <p>纯粹是因为 iniRead(Sector型) 返回的数据是无序的所以才写的这个.</p>
     * @return 返回一个二元 String 数组, String[0] 表示键, String[1] 表示值, 如果已枚举到段末尾则返回 null.
     */
    public static String[] iniEnumSector(FileInputStream iniStream)
    {
        Scanner iniScanner = new Scanner(iniStream);
        String[] rtString = null;
        String buffer = null;

        // 文件末尾时返回 null
        while (iniScanner.hasNextLine())
        {
            buffer = iniScanner.nextLine();

            // 跳过注释行
            if (buffer.matches("\\s;"))
                continue;

            // 节末尾时返回 null
            if (buffer.matches("\\s*\\[.*\\]"))
                return(null);

            // 以 = 为基准匹配键-值对
            // 键首尾去空格, 值\n转换为回车符
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
     * <p>从指定的 ini 文件中读取整个节</p>
     * @return 请求读取的节的哈希表, 如果指定文件或节不存在则返回 null,
     * 如果指定节为空则返回一个空的哈希表.
     */
    public static Hashtable<String, String> iniRead(String argFileName, String argSector)
    {
        FileInputStream iniStream = JUkaUtility.iniLocate(argFileName, argSector);

        // 参数不正确时返回 null.
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
     * <p>从指定的 ini 文件中读取指定节和键的值</p>
     * <p>简单地说就是:<br>
     * <p>[argSector]<br>
     * argKey=Value</p>
     * 总之, 返回的是 Value 就对了.</p>
     */
    public static String iniRead(String argFileName, String argSector, String argKey)
    {
        FileInputStream iniStream = JUkaUtility.iniLocate(argFileName, argSector);

        // 参数不正确时返回 null.
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

  // Other | 其他
    /**
     * @deprecated 此方法目前仅用于调试
     */
    public static void main(String[] args)
    {
        FileInputStream iniStream = null;
        Hashtable<String, String> ht = null;

        // 此代码段用于测试 获取程序路径
        System.out.println(JUkaUtility.getProgramPath());

        // 此代码段用于测试 读取ini
        JUkaUtility.iniLocate(JUkaUtility.getProgramPath() + "shell/SampleShell/SampleShell.ini", "images");
        ht = iniRead(JUkaUtility.getProgramPath() + "shell/SampleShell/SampleShell.ini", "images");

        return;
    }
}
