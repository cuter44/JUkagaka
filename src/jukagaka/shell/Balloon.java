package jukagaka.shell;

/* jukagaka */
import jukagaka.UkaDaemon;
import jukagaka.shell.renderer.BalloonRenderer;
/* ---- */

public class Balloon extends UkaWindow
{
  // Constuct & Destruct | 构造及析构
    /**
     * 构造方法.
     *
     * @param ukagaka 是作为父窗体的春菜, 气球会跟随父窗体的显隐动作
     */
    protected Balloon(Ukagaka ukagaka)
    {
        super((UkaWindow)ukagaka);
    }

    /**
     * 构造方法.
     *
     * @param ukagaka 是作为父窗体的气球, 气球会跟随父窗体的显隐动作
     */
    protected Balloon(Balloon balloon)
    {
        super((UkaWindow)balloon);
    }

    /**
     * @see java.util.Window.dispose();
     */
    public void dispose()
    {
        super.dispose();
    }

  // Renderer | 渲染
  // 数据
    protected BalloonRenderer renderer = null;
  // 代码
    /**
     * 获取当前使用的渲染器
     */
    public BalloonRenderer getRenderer()
    {
        return(this.renderer);
    }

    /**
     * 设置使用的渲染器<br />
     * <br />
     * 这个接口留给渲染器本身调用<br />
     */
    public void setRenderer(BalloonRenderer argRenderer)
    {
        this.renderer = argRenderer;

        return;
    }

  // Miscellaneous | 杂项
    //public static void main(String[] args)
    //{
        //return;
    //}
}
