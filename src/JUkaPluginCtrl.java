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
  // Shell Management | Shell组件管理
    /**
     * 此数据域记录数据文件的名字
     */
    private static final File LIST_FILE = new File(JUkaUtility.getProgramDir() + "/plugin/JUkaPlugin.list");
    /**
     * <p>安装 Plugin 组件, 由子类调用</p>
     * <p>(!) 此方法不包含传入参数以增强安全性.<br>
     * 此方法自动将运行栈的上一层作为待注册组件, 故必须以待注册组件直接调用该方
     * 法.</p>
     * @return <ul>返回值<li>0=成功<li>1=该组件已经安装<li>-1=读写失败
     * <li>-2=运行栈上溢<li>-3=类不存在</ul>
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
     * <p>卸载组件, 由子类调用</p>
     * <p>(!) 此方法不包含传入参数以增强安全性.<br>
     * 此方法自动将运行栈的上一层作为待卸载组件, 故必须以待卸载组件直接调用该方
     * 法.</p>
     * @return <ul>返回值<li>0=成功<li>1=该组件不在列表中<li>-1=读写失败
     * <li>-2=运行栈上溢<li>-3=类不存在</ul>
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
     * <p>返回已安装的 Plugin 列表</p>
     * <p>此方法与 JUkaStage 中的同名方法区别在于此方法总是从文件读取的.</p>
     * @return 返回已安装的 Plugin 列表
     */
    public static ArrayList<Class> getRegisteredPlugin()
    {
        return(JUkaComponentCtrl.readComponentListFile(JUkaPluginCtrl.LIST_FILE));
    }

  // Other | 杂项
    /**
     * @deprecated 此方法目前仅用于调试
     */
    public static void main(String[] args)
    {

        return;
    }
}
