package jukagaka.shell.renderer;

/* container */
import java.util.List;
import java.util.Map;
import java.util.Hashtable;
import java.util.Iterator;
/* graphics */
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.geom.Area;
import java.awt.Shape;
import java.awt.Component;
/* jukagaka */
import jukagaka.UkaDaemon;
import jukagaka.shell.UkaWindow;
import jukagaka.shell.renderer.FakeComponent;
import jukagaka.shell.util.EdgeDetector;
import jukagaka.shell.util.LinealEdgeDetector;
/* jdom2 */
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.filter.ElementFilter;
import org.jdom2.JDOMException;
/* awt/swing */
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.HierarchyListener;
import java.awt.event.HierarchyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import com.sun.awt.AWTUtilities;
/* debug */
import javax.swing.JDialog;
import java.io.IOException;
/* ---- */

public class SwingRenderer extends JPanel implements HierarchyListener, PropertyChangeListener
{
  // Edge Detect | ���ؼ��
    private EdgeDetector edgeDetector = null;

  // Image Prefetch | ͼ��ȡ
    // �Ӵ��̶�ȡ��Ԥ����ͼ�����, ׼��Ϊ��������ʹ��.
  // ����
    /**
     * ͼ���XML��Ϣ����
     */
    private List<Element> imgMeta = null;
    /**
     * ͼ������ػ���
     */
    private Map<String,Image> imgCont = null;
    /**
     * ͼ�����������
     */
    private Map<String,Shape> imgShape = null;
  // ����
    /**
     * ���ش洢��XML�е�ͼ���б�
     *
     * @param xmlFileName Ҫ���ص��ļ���
     * @param xPath ���ڴ�XML��ɸѡ��XPath���ʽ
     * ����XPath���ʽ��ο�wiki
     * @return �ڽ���ʧ��ʱ����false(�����ȡ����, XPath��ʽ����ȷ֮��), ���򷵻�true.
     */
    private boolean loadImageMeta(String xmlFileName, String xPath)
    {
        try
        {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(xmlFileName);

            XPathExpression<Element> xpath = XPathFactory.instance().compile(xPath, new ElementFilter());
            this.imgMeta = xpath.evaluate(document);
        }
        catch (Exception ex)
        {
            System.err.println("error:SwingRenderer.loadImageGroup():����XMLʱ��������:");
            ex.printStackTrace();
            return(false);
        }

        return(true);
    }

    /**
     * ����ͼ������, �����Ѽ��ص�XMLԪ�ر�
     *
     * @return ���������Ƿ���false
     */
    private boolean loadImageContent()
    {
        try
        {
            Iterator<Element> iter = imgMeta.iterator();
            Hashtable<String,Image> map = new Hashtable<String,Image>();
            MediaTracker tracker = new MediaTracker(new FakeComponent());

            while (iter.hasNext())
            {
                Element elem = iter.next();
                String id = elem.getAttributeValue("id");
                String src = elem.getAttributeValue("value");

                try
                {
                    Image img = Toolkit.getDefaultToolkit().getImage(UkaDaemon.getProgramDir() + src);
                    // TODO ����ƽ�Ƶ�
                    map.put(id, img);
                    tracker.addImage(img, 0);
                }
                catch (NullPointerException ex)
                {
                    XMLOutputter outputter = new XMLOutputter();
                    System.out.println("error:SwingRenderer.loadImageContent():XMLԪ��û��id��/��value����:"+outputter.outputString(elem));
                    ex.printStackTrace();
                    continue;
                }
            }
            tracker.waitForAll();
            this.imgCont = map;
        }
        catch (InterruptedException ex)
        {
            // ��̫���ܱ���ϰ�...?
            ex.printStackTrace();
        }

        return(true);
    }

    /**
     * Ϊ�Ѽ��ص�ͼ������ɰ�
     *
     * @return Ŀǰ�����Ƿ���true
     */
    private boolean loadImageShape()
    {
        Iterator<Map.Entry<String,Image>> iter = this.imgCont.entrySet().iterator();
        Hashtable<String,Shape> map = new Hashtable<String,Shape>();

        while (iter.hasNext())
        {
            Map.Entry<String,Image> entry = iter.next();
            map.put(entry.getKey(), edgeDetector.shapeOf(entry.getValue()));
        }

        this.imgShape = map;

        return(true);
    }

    /**
     * ����һ���ģʽ����ͼ��<br />
     * <br />
     * �����εص���loadImageMeta(), loadImageContent(), loadImageShape()�Բ���
     * ��������ı�Ҫ��Ϣ<br />
     * @param xmlFileName ׼����ȡ�� XML �ļ���, ���ļ�Ӧ�÷���<a href="">һ���Ĺ���</a>
     * @param xPath ������xml�ĵ���ɸѡԪ��(Element)��<a href="">XPath���ʽ</a>
     * @return Ŀǰ�����Ƿ���true.
     */
    public boolean loadImage(String xmlFileName, String xPath)
    {
        this.loadImageMeta(xmlFileName, xPath);
        this.loadImageContent();
        this.loadImageShape();

        return(true);
    }
  // ����
    /**
     * ���¼���get/set������������˼����, �����⼸������ֻ��Ϊ�˵�����;,
     * ����������Լ�Ҫ��ʲô, ����Ҫ����ʹ����Щ����
     * �� SwingRenderer �������޸�.
     */
    public List<Element> getImageMeta()
    {
        return(this.imgMeta);
    }

    public void setImageMeta(List<Element> argImgMeta)
    {
        this.imgMeta = argImgMeta;
        return;
    }

    public Map<String,Image> getImageContent()
    {
        return(this.imgCont);
    }

    public void setImageContent(Map<String, Image> argImgCont)
    {
        this.imgCont = argImgCont;
        return;
    }

    public Map<String,Shape> getImageShape()
    {
        return(this.imgShape);
    }

    public void setImageShape(Map<String,Shape> argImgShape)
    {
        this.imgShape = argImgShape;
        return;
    }

    /**
     * ������;
     */

  // Buffer | ��ͼ�㻺��
    // ԭͼ����Ƶ��滻����, ��ԭͼ����Ƶ�ģ��ʵ��.
    // ���ĵ�˼���ǵ���һ�ν���ͼ����ӻ���ʱ���㲢����ͼ��, ���ٴ�ʹ����ͬ��ͼ�����ʱ�����Ӧ�ٶ�
    // �Ե�ͼ������ٶȼ����������Ӱ��
  // ����
    /**
     * Ŀǰ����������ֻ�ܼ򵥵ذ���������Ϣ��ȡ���е�ͼ��, �޷�����ͼ�����
     */
    public Image getImage(String id)
    {
        return(this.imgCont.get(id));
    }

    public Shape getShape(String id)
    {
        return(this.imgShape.get(id));
    }

  // Renderer | ��Ⱦ
  // ����
    private UkaWindow belongWindow = null;
  // ����
    public void validateBelongWindow()
    {
        Component ancestor = this;

        do
        {
            ancestor = ancestor.getParent();
        }
        while ((ancestor != null) && !(ancestor instanceof UkaWindow));

        this.belongWindow = (UkaWindow)ancestor;
        return;
    }
    /**
     * �̳��� HierarchyListener �ķ���<br />
     * <br />
     * ��Ҫ��Ϊ����Ӧ setContentPane() ����
     * Ŀǰ���ӵ��߼���:<br />
     * <ul>
     * <li>��ȡ��������</li>
     * </ul>
     */
    @Override
    public void hierarchyChanged(HierarchyEvent ev)
    {
        if ((ev.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0)
            this.validateBelongWindow();

        return;
    }

    /**
     * �̳��� PropertyChangeListener �ķ���<br />
     * <br />
     * ��Ҫ��Ϊ����Ӧ ͼ��仯�¼�
     * Ŀǰ���ӵ��߼���:<br />
     * <ul>
     * <li>(ʲôҲû��...)</li>
     * </ul>
     */
    @Override
    public void propertyChange(PropertyChangeEvent ev)
    {
        if (ev.getPropertyName() == "renderData")
        {
            try
            {
                String oldRenderData = (String)ev.getOldValue();
                String newRenderData = (String)ev.getNewValue();
            }
            catch (ClassCastException ex)
            {
                ex.printStackTrace();
                return;
            }
        }

        return;
    }

    /**
     * �ػ�ķ���
     */
    @Override
    public void paintComponent(Graphics g)
    {
        try
        {
            String renderData = (String)this.belongWindow.getRenderData();
            Image renderImage = this.getImage(renderData);
            Shape renderShape = this.getShape(renderData);

            // ���Ʊ���
            g.drawImage(
                renderImage,
                0, 0,
                this.belongWindow
            );
            // ���ô����С
            this.belongWindow.setSize(
                renderImage.getWidth(this.belongWindow),
                renderImage.getHeight(this.belongWindow)
            );
            // �ü�����
            AWTUtilities.setWindowShape(
                this.belongWindow,
                this.getShape(renderData)
            );
        }
        catch (NullPointerException ex)
        {
            ex.printStackTrace();
            return;
        }
        catch (ClassCastException ex)
        {
            ex.printStackTrace();
            return;
        }

        return;
    }

  // Constructor & Initalization | ������
    public SwingRenderer()
    {
        this.edgeDetector = new LinealEdgeDetector();
        // TODO ����ͼ����Ϣ
        this.init();

        return;
    }

    public SwingRenderer(EdgeDetector argEdgeDetector)
    {
        this.edgeDetector = argEdgeDetector;
        this.init();

        return;
    }

    private void init()
    {
        this.addHierarchyListener(this);
    }

  // Miscellaneous | ����
    public static void main(String[] args)
    {
        SwingRenderer renderer = new SwingRenderer();

        return;
    }
}
