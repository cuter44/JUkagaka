package jukagaka.shell.renderer;

/* jukagaka */
import jukagaka.shell.UkaWindow;
/* awt */
import java.awt.Graphics;

public interface UkagakaRenderer //extends UkaRenderer
{
  // Inject | ע��
    public abstract void inject(UkaWindow ukagaka);

  // Renderer Interface | ��Ⱦ�ӿ�
    public abstract void paint(Graphics g, UkaWindow ukagaka);

  // Miscellaneous | ����
    //public static void main(String[] args)
    //{
        //return;
    //}
}
