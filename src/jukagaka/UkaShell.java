package jukagaka;

/* �ڲ����� */
//import jukagaka.UkaComponent;
/* ---- */

public abstract class UkaShell implements UkaComponent
{
  // Launch | ��ͣ�ӿ�
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

  // Image | ͼ��
    //public static Image loadImage(???)
    //{
    //}

    //public static Shape getMask(Image img)
    //{
    //}

  // Debug | ����
    //public static void main(String[] args)
    //{
        //return;
    //}
}
