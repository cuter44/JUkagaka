package jukagaka.shell.renderer;

/* jukagaka */
import jukagaka.shell.UkaWindow;
/* awt */
import java.awt.Graphics;

public interface BalloonRenderer //extends UkaRenderer
{
  // Inject | 注入
    public abstract void inject(UkaWindow balloon);

  // Renderer Interface | 渲染接口
    public abstract void paint(Graphics g, UkaWindow balloon);

  // Miscellaneous | 杂项
    //public static void main(String[] args)
    //{
        //return;
    //}
}
