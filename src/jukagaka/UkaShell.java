package jukagaka;

/* jukagaka */
//import jukagaka.UkaComponent;
import jukagaka.shell.UkaWindow;
import jukagaka.shell.Ukagaka;
import jukagaka.shell.Balloon;
/* ���� */
import java.util.LinkedList;
/* ---- */

public abstract class UkaShell implements UkaComponent
{
  // Launch | ��ͣ�ӿ�
    // ����
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

  // Frames | ����
  // ����
    /**
     *
     */
     private Ukagaka ukagaka = null;
  // ����
    /**
     * ��ô��˵�����
     * @return �� Shell �����Ĵ���
     */
    public Ukagaka getUkagaka()
    {
        return(this.ukagaka);
    }

  // Coordinate | ��λ

  // Constructor & Destructor | ����
    protected UkaShell(Ukagaka argUkagaka)
    {
        this.ukagaka = argUkagaka;
    }

  // Debug | ����
    //public static void main(String[] args)
    //{
        //return;
    //}
}
