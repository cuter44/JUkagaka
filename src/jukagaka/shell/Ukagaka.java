package jukagaka.shell;

import jukagaka.shell.renderer.UkagakaRenderer;

public class Ukagaka extends UkaWindow
{

  // Constuct & Destruct | ���켰����
    public Ukagaka()
    {
        super();
    }

    //protected Ukagaka(UkagakaRenderer argRenderer)
    //{
        //super();

        //this.setRenderer(this);
    //}


  // Renderer | ��Ⱦ
  // ����
    protected UkagakaRenderer renderer = null;
  // ����
    /**
     * ��ȡ��ǰʹ�õ���Ⱦ��
     */
    public UkagakaRenderer getRenderer()
    {
        return(this.renderer);
    }

    /**
     * ����ʹ�õ���Ⱦ��<br />
     * <br />
     * ����ӿ�������Ⱦ���������<br />
     */
    public void setRenderer(UkagakaRenderer argRenderer)
    {
        this.renderer = argRenderer;
        this.renderer.inject(this);

        return;
    }

  // Miscellaneous | ����
    //public static void main(String[] args)
    //{
        //return;
    //}
}
