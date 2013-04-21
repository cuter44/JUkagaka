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
  // Render Data | ��Ⱦ����
    // Ϊ��Ⱦ���ṩ�����ŵ�, �Ա��������������Ⱦ����Ҫ��ʾʲô����.
    // ͨ����ʹ�� String, �� String ���� SwingRenderer ��ζ��ͼ����Ϣ, �� WebViewRenderer ��ζ��һ�� URL
  // ����
    private Object renderData = null;
  // ����
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

  // Constructor & Destructor | ���켰����
  // ����
    /**
     * Ϊ Ukagaka ��׼���Ĺ��췽��
     */
    public UkaWindow()
    {
        super();
        this.init();
    }

    /**
     * Ϊ Balloon ��׼���Ĺ��췽��
     */
    protected UkaWindow(UkaWindow owner)
    {
        super(owner);
        this.init();
    }

    /**
     * ����һЩʣ��ĳ�ʼ������(e.g. ȥ��������)
     */
    private void init()
    {
        this.setUndecorated(true);
    }

  // Canvas | ����
    /* Ŀǰ��˵��Ϊ�ɹ�����Ľ���Ѿ�ֱ���ô���� ContentPane ��Ϊ����
     * Renderer Ӧ��׫д�������µĴ���������ڳ�ʼ��ʱ��������
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
     * ���ϵĲ�����α���������ʵ���������
     * * render �����������ඨ��
     * * ʹ�� GLPanel �Ĵ�������в�ͬ��д��
     */


  // Miscellaneous | ����
    public static void main(String[] args)
    {
        UkaWindow window = new UkaWindow();

        return;
    }
}
