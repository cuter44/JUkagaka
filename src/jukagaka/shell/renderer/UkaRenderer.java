package jukagaka.shell.renderer;

/**
 * [改进]为了能简化依赖关系和绘制过程的接口, 用于替代原有的 UkagakaRenderer
 * 和 BalloonRenderer
 */
public abstract interface UkaRenderer
{
  // Renderer | 绘制回调
    public void injectRenderer()

  // Miscellaneous | 杂项
    //public static void main(String[] args)
    //{
        //return;
    //}
}
