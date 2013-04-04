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
  // Constructor & Destructor | ���켰����
  // ����
    /**
     * Ϊ Ukagaka ��׼���Ĺ��췽��
     */
    protected UkaWindow()
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
        this.layers = new LayerGroup();
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

  // Layers | ͼ��
  // �ڲ���
    /**
     * ����Ϊ UkaWindow �ṩ�ɻ���ͼ������
     */
    public class LayerGroup extends ArrayList<Object>
    {
        public Image cacheImage;
        public Shape cacheMask;
        public boolean modified = false;
    }
  // ����
    /**
     * ͼ��<br />
     * <br />
     * ģ�� Photoshop ͼ����ļ��е���Ƹ�ʽ, ������Ϊ Shell �� Renderer
     * �ṩ���ݽ����ĳ���. ����ΪList&lt;Object&gt;�ܹ�ʹ����ṹ�����ɵ�Ƕ��
     * . �����ǵ�Լ����, Renderer Ӧ��Ϊÿһ��"�ļ���"ά��һ������. ��ʹ���ڽ�
     * �ж�����ʱ�ܹ���Ч��Լʱ��<br />
     * ������ʵ����, �� UkaWindow�����ʼ��; �� Shell ����д��ͼ����Ϣ��֪ͨ���;
     * �� Renderer ������»����Լ��ػ�.<br />
     * ��Ȼ������Ƕ�ײ���������ֻǶ��һ��, �����Ƕ�������ӻ������ĵĿ�����.
     * (��Ȼ������Ϊ�����յ� Renderer ȷ��)
     */
    protected LayerGroup layers = null;
  // ����
    public void addLayerChangeListener(PropertyChangeListener l)
    {
        this.addPropertyChangeListener("layer",l);

        return;
    }

    public void removeLayerChangeListener(PropertyChangeListener l)
    {
        this.removePropertyChangeListener("layer",l);

        return;
    }

    /**
     * ����ͼ������<br />
     * <br />
     * ����ͼ��ֻ�ṩ��get������û���ṩset����.<br />
     * �������Ϊ�޸�ͼ���ṩ�����Ľӿڻ�˽�е�Լ��.<br />
     */
    public LayerGroup getLayers()
    {
        return this.layers;
    }

    public void fireLayerChange()
    {
        this.firePropertyChange("layer", null, this.layers);
    }

  // Miscellaneous | ����
    public static void main(String[] args)
    {
        UkaWindow window = new UkaWindow();

        return;
    }
}
