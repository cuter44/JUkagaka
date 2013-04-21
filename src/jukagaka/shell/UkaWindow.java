package jukagaka.shell;

/* awt/swing */
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Shape;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
/* container */
import java.util.ArrayList;
/* jukagaka */
//import jukagaka.*;
/* ---- */

public class UkaWindow extends JDialog
{
  // Render Data | 渲染数据
    // 为渲染器提供数据锱点, 以便其他组件告诉渲染器需要显示什么内容.
    // 通常会使用 String, 该 String 对于 SwingRenderer 意味着图层信息, 对 WebViewRenderer 意味着一个 URL
  // 数据
    private Object renderData = null;
  // 方法
    public void fireRenderDataChange(Object oldRenderData, Object newRenderData)
    {
        this.firePropertyChange("renderData", oldRenderData, newRenderData);

        return;
    }

    public void setRenderData(Object newRenderData)
    {
        Object oldRenderData = this.renderData;
        this.renderData = newRenderData;
        fireRenderDataChange(oldRenderData, newRenderData);
    }

    public Object getRenderData()
    {
        return(this.renderData);
    }

  // Constructor & Destructor | 构造及析构
  // 方法
    /**
     * 为 Ukagaka 类准备的构造方法
     */
    public UkaWindow()
    {
        super();
        this.init();
    }

    /**
     * 为 Balloon 类准备的构造方法
     */
    protected UkaWindow(UkaWindow owner)
    {
        super(owner);
        this.init();
    }

    /**
     * 处理一些剩余的初始化杂项(e.g. 去掉标题栏)
     */
    private void init()
    {
        this.setUndecorated(true);
    }

  // Canvas | 画布
    /* 目前来说因为成功试验的结果已经直接拿窗体的 ContentPane 作为画布
     * Renderer 应该撰写类似以下的代码以完成在初始化时进行切入
     * <code>
       window.setContentPane
       {
           new JPanel()
           {
               @Override
               public void paint(Graphics g)
               {
                   super.paint(g);
                   window.renderer.paint(g, window);

                   return;
               }
           {
       }
     * </code>
     * 以上的部分是伪代码请根据实际情况调整
     * * render 数据域将在子类定义
     * * 使用 GLPanel 的代码可能有不同的写法
     */


  // Miscellaneous | 杂项
    public static void main(String[] args)
    {
        UkaWindow window = new UkaWindow();

        return;
    }
}
