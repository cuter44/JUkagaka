package jukagaka.shell.renderer;

/* jukagaka */
import jukagaka.shell.UkaWindow;
/* awt */
import java.awt.Graphics;

public interface BalloonRenderer //extends UkaRenderer
{
  // Inject | ע��
    public abstract void inject(UkaWindow balloon);

  // Renderer Interface | ��Ⱦ�ӿ�
    public abstract void paint(Graphics g, UkaWindow balloon);

  // Miscellaneous | ����
    //public static void main(String[] args)
    //{
        //return;
    //}
}
