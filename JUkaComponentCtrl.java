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
  // Static Data | 静态数据域

  // Install/Uninstall | 安装/卸载
    /**
     * 安装组件, 由直接子类调用
     * @param argComponent 指定待安装组件
     * @return <ul>返回值<li>0=成功<li>1=已注册<li>-1=读写错误</ul>
     */
    protected static int installComponent(Class argComponent, File argListFile)
    {
        ArrayList<Class> compList = JUkaComponentCtrl.readComponentListFile(argListFile);

        // 读取错误处理
        if (compList == null)
            return(-1);

        // 已注册组件处理
        if (compList.contains(argComponent))
            return(1);

        compList.add(argComponent);
        return(JUkaComponentCtrl.writeComponentListFile(compList, argListFile));
    }

    /**
     * 卸载组件, 由直接子类调用
     * @param argComponent 指定待卸载组件
     * @return <ul>返回值<li>0=成功<li>1=未注册<li>-11=读写错误</ul>
     */
    protected static int uninstallComponent(Class argComponent, File argListFile)
    {
        ArrayList<Class> compList = JUkaComponentCtrl.readComponentListFile(argListFile);

        // 读取错误处理
        if (compList == null)
            return(-1);

        // 未注册组件处理
        if (!compList.contains(argComponent))
            return(1);

        compList.remove(argComponent);
        return(JUkaComponentCtrl.writeComponentListFile(compList, argListFile));
    }

  // Basic I/O | 基础文件读写
    /**
     * <p>按指定的文件读取组件列表</p>
     * (*) 如果指定的文件不存在, 则创建此文件并返回一个空的 ArrayList<br></p>
     * @param argListFile 指定要反序列化的文件
     * @return 反序列化的组件列表
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
     * <p>按指定的文件写入组件列表</p>
     * @param argCompList 指定要写入的组件列表
     * @param argListFile 指定要序列化的文件
     * @return <ul>返回值<li>0=成功<li>-1=读写错误</ul>
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

  // Other | 杂项
    /**
     * @deprecated 此方法目前仅用于调试
     */
    public static void main(String[] args)
    {
        return;
    }
}
