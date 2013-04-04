package jukagaka.shell;

import jukagaka.shell.renderer.UkagakaRenderer;

public class Ukagaka extends UkaWindow
{

  // Constuct & Destruct | 构造及析构
    public Ukagaka()
    {
        super();
    }

    //protected Ukagaka(UkagakaRenderer argRenderer)
    //{
        //super();

        //this.setRenderer(this);
    //}


  // Renderer | 渲染
  // 数据
    protected UkagakaRenderer renderer = null;
  // 代码
    /**
     * 获取当前使用的渲染器
     */
    public UkagakaRenderer getRenderer()
    {
        return(this.renderer);
    }

    /**
     * 设置使用的渲染器<br />
     * <br />
     * 这个接口留给渲染器本身调用<br />
     */
    public void setRenderer(UkagakaRenderer argRenderer)
    {
        this.renderer = argRenderer;
        this.renderer.inject(this);

        return;
    }

  // Miscellaneous | 杂项
    //public static void main(String[] args)
    //{
        //return;
    //}
}
