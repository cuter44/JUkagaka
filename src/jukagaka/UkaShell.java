package jukagaka;

/* jukagaka */
//import jukagaka.UkaComponent;
import jukagaka.shell.UkaWindow;
import jukagaka.shell.Ukagaka;
import jukagaka.shell.Balloon;
/* 容器 */
import java.util.LinkedList;
/* ---- */

public abstract class UkaShell implements UkaComponent
{
  // Launch | 启停接口
    // 代码
    public static boolean onLoad()
    {
        return(true);
    }

    public static boolean onStart()
    {
        return(true);
    }

    public static boolean onExit()
    {
        return(true);
    }

    public static boolean onInstall()
    {
        return(true);
    }

    public static boolean onUnistall()
    {
        return(true);
    }

  // Frames | 窗体
  // 数据
    /**
     *
     */
     private Ukagaka ukagaka = null;
  // 代码
    /**
     * 获得春菜的引用
     * @return 该 Shell 下属的春菜
     */
    public Ukagaka getUkagaka()
    {
        return(this.ukagaka);
    }

  // Coordinate | 定位

  // Constructor & Destructor | 构造
    protected UkaShell(Ukagaka argUkagaka)
    {
        this.ukagaka = argUkagaka;
    }

  // Debug | 调试
    //public static void main(String[] args)
    //{
        //return;
    //}
}
