package jukagaka.shell;

/* jukagaka */
import jukagaka.UkaDaemon;
import jukagaka.shell.renderer.BalloonRenderer;
/* ---- */

public class Balloon extends UkaWindow
{
  // Constuct & Destruct | ���켰����
    /**
     * ���췽��.
     *
     * @param ukagaka ����Ϊ������Ĵ���, �������游�������������
     */
    protected Balloon(Ukagaka ukagaka)
    {
        super((UkaWindow)ukagaka);
    }

    /**
     * ���췽��.
     *
     * @param ukagaka ����Ϊ�����������, �������游�������������
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

  // Renderer | ��Ⱦ
  // ����
    protected BalloonRenderer renderer = null;
  // ����
    /**
     * ��ȡ��ǰʹ�õ���Ⱦ��
     */
    public BalloonRenderer getRenderer()
    {
        return(this.renderer);
    }

    /**
     * ����ʹ�õ���Ⱦ��<br />
     * <br />
     * ����ӿ�������Ⱦ���������<br />
     */
    public void setRenderer(BalloonRenderer argRenderer)
    {
        this.renderer = argRenderer;

        return;
    }

  // Miscellaneous | ����
    //public static void main(String[] args)
    //{
        //return;
    //}
}
