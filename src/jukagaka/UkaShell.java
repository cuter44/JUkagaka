package jukagaka;

/* 内部依赖 */
//import jukagaka.UkaComponent;
/* ---- */

public abstract class UkaShell implements UkaComponent
{
  // Launch | 启停接口
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

  // Image | 图像
    //public static Image loadImage(???)
    //{
    //}

    //public static Shape getMask(Image img)
    //{
    //}

  // Debug | 调试
    //public static void main(String[] args)
    //{
        //return;
    //}
}
