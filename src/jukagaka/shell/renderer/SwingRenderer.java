package jukagaka.shell.renderer;

/* container */
import java.util.List;
import java.util.Map;
import java.util.Hashtable;
import java.util.Iterator;
/* graphics */
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.MediaTracker;
import java.awt.image.PixelGrabber;
import java.awt.image.ImageObserver;
import java.awt.geom.Area;
/* jukagaka */
import jukagaka.UkaDaemon;
import jukagaka.shell.UkaWindow;
import jukagaka.shell.Ukagaka;
import jukagaka.shell.Balloon;
import jukagaka.shell.renderer.FakeComponent;
/* jdom2 */
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.JDOMException;
/* awt/swing */
import javax.swing.JPanel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import com.sun.awt.AWTUtilities;
/* debug */
import javax.swing.JDialog;
import java.awt.Graphics;
import java.io.IOException;
/* ---- */

public class SwingRenderer implements UkagakaRenderer, BalloonRenderer, PropertyChangeListener
{

  // Image Toolkit | ������
    // Ϊ����ͼ�����ɸ�����Ϣ����д�Ĺ��ߺ���
  // ����
    /**
     * ��ͼƬ��������<br />
     * <br />
     * @param image �����ͼ��
     * @param filter ���������Ƿ������Ĺ�����
     * @return ͼ�����������
     */
    public static Area cutOut(Image image, PixelFilter filter)
    {
        PixelGrabber grabber = new PixelGrabber(image, 0, 0, -1, -1, true);
        Area mask = new Area();
        int pixels[];
        int height,width,x,y,xs;

        try
        {
            if (!grabber.grabPixels())
            {
                // TODO:do something for fail-to-load
                System.out.println("fail-to-load");
            }

            // get image info
            pixels = (int[])grabber.getPixels();
            height = grabber.getHeight();
            width = grabber.getWidth();

            // ��ɨ��(����������ɨ�費͸������)
            for (y = 0; y < height; y++)
            {
                xs = -1;
                // ��ɨ��
                for (x = 0; x < width; x++)
                {
                    if (filter.isTransparent(pixels[y * width + x]))
                    {
                        if (xs != -1)
                        {
                            mask.add(new Area(new Rectangle(xs,y,x-xs,1)));
                            xs = -1;
                        }
                    }
                    else
                        if (xs == -1)
                            xs = x;
                }
                // ��β���
                if (xs!=-1)
                    mask.add(new Area(new Rectangle(xs,y,width-xs,1)));
            }
        }
        catch (InterruptedException ex)
        {
            System.err.println("error:SwingRenderer.cutOut():�������!?");
            ex.printStackTrace();
        }

        return(mask);
    }
  // �ӿ�
    /**
     * ���ڷֱ������Ƿ�͸�����ڲ���
     */
    public interface PixelFilter
    {
        /**
         * �ж������Ƿ�"͸��", ����Ϊ"͸��"����������Ⱦʱ�ᱻ�ü�<br />
         * <br />
         * �ڼ���ü�����ʱ���������ж�, ��ǰ����(��ARGB��ʽ)�ᱻ��Ϊ��������.
         * �ɽӿڵ�ʵ���߾����������Ƿ��ü�.
         * @return ���Ҫ�ü�������Ӧ����true, ���򷵻�false
         */
        public abstract boolean isTransparent(int pixel);
    }

    public static final PixelFilter DEFAULT_FILTER = new PixelFilter()
    {
        @Override
        public boolean isTransparent(int pixel)
        {
            if ((pixel & 0xff000000) == 0)
                return(true);
            else
                return(false);
        }
    };

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
     * ͼ����ɰ滺��
     */
    private Map<String,Area> imgMask = null;
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
    private boolean loadImageMask(PixelFilter filter)
    {
        Iterator<Map.Entry<String,Image>> iter = this.imgCont.entrySet().iterator();
        Hashtable<String,Area> map = new Hashtable<String,Area>();

        while (iter.hasNext())
        {
            Map.Entry<String,Image> entry = iter.next();
            map.put(entry.getKey(), cutOut(entry.getValue(),filter));
        }

        this.imgMask = map;

        return(true);
    }

    /**
     * ����һ���ģʽ����ͼ��<br />
     * <br />
     * �����εص���loadImageMeta(), loadImageContent(), loadImageMask()�Բ���
     * ��������ı�Ҫ��Ϣ<br />
     * @param xmlFileName ׼����ȡ�� XML �ļ���, ���ļ�Ӧ�÷���<a href="">һ���Ĺ���</a>
     * @param xPath ������xml�ĵ���ɸѡԪ��(Element)��<a href="">XPath���ʽ</a>
     * @param filter �ṩ��loadImageMask�����ж������Ƿ�͸��������
     * @see jukagaka.shell.renderer.SwingRenderer.PixelFilter
     * @return Ŀǰ�����Ƿ���true.
     */
    public boolean loadImage(String xmlFileName, String xPath, PixelFilter filter)
    {
        this.loadImageMeta(xmlFileName, xPath);
        this.loadImageContent();
        this.loadImageMask(filter);

        return(true);
    }

    /**
     * �� loadImage(String,String,PixelFilter) ��Ĭ��ʵ��,
     * �÷�����ͼ���е���ȫ͸����������͸������.
     */
    public boolean loadImage(String xmlFileName, String xPath)
    {
        return(
            this.loadImage(xmlFileName, xPath, DEFAULT_FILTER)
        );
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

    /**
     * ���¼���get/set������������˼����, �����⼸������ֻ��Ϊ�˵�����;,
     * ����������Լ�Ҫ��ʲô, ����Ҫ����ʹ����Щ����
     * �� SwingRenderer �������޸�.
     */
    public void setImageMeta(List<Element> argImgMeta)
    {
        this.imgMeta = argImgMeta;
        return;
    }

    /**
     * ���¼���get/set������������˼����, �����⼸������ֻ��Ϊ�˵�����;,
     * ����������Լ�Ҫ��ʲô, ����Ҫ����ʹ����Щ����
     * �� SwingRenderer �������޸�.
     */
    public Map<String,Image> getImageContent()
    {
        return(this.imgCont);
    }

    /**
     * ���¼���get/set������������˼����, �����⼸������ֻ��Ϊ�˵�����;,
     * ����������Լ�Ҫ��ʲô, ����Ҫ����ʹ����Щ����
     * �� SwingRenderer �������޸�.
     */
    public void setImageContent(Map<String, Image> argImgCont)
    {
        this.imgCont = argImgCont;
        return;
    }

    /**
     * ���¼���get/set������������˼����, �����⼸������ֻ��Ϊ�˵�����;,
     * ����������Լ�Ҫ��ʲô, ����Ҫ����ʹ����Щ����
     * �� SwingRenderer �������޸�.
     */
    public Map<String,Area> getImageMask()
    {
        return(this.imgMask);
    }

    /**
     * ���¼���get/set������������˼����, �����⼸������ֻ��Ϊ�˵�����;,
     * ����������Լ�Ҫ��ʲô, ����Ҫ����ʹ����Щ����
     * �� SwingRenderer �������޸�.
     */
    public void setImageMask(Map<String,Area> argImgMask)
    {
        this.imgMask = argImgMask;
        return;
    }

    /**
     * ������;
     */
    public Image getImage(String id)
    {
        return(this.imgCont.get(id));
    }

    public Area getMask(String id)
    {
        return(this.imgMask.get(id));
    }

  // Renderer | ��Ⱦ�ص��ӿ�
    @Override
    public void inject(UkaWindow argWindow)
    {
        // Ukagaka
        if (argWindow instanceof Ukagaka)
        {
            final Ukagaka window = (Ukagaka)argWindow;

            // ��������
            window.setContentPane(
                new JPanel()
                {
                    @Override
                    protected void paintComponent(Graphics g)
                    {
                        window.getRenderer().paint(g, window);

                        return;
                    }
                }
            );
        }

        // Balloon
        if (argWindow instanceof Balloon)
        {
            final Balloon window = (Balloon)argWindow;

            // ��������
            window.setContentPane(
                new JPanel()
                {
                    @Override
                    public void paintComponent(Graphics g)
                    {
                        window.getRenderer().paint(g, window);

                        return;
                    }
                }
            );
        }

        // ע��ͼ��������
        argWindow.addLayerChangeListener(this);

        // TODO Something else?

        return;
    }

    /**
     * ���ʵ���ػ�͵�̴��幤��
     */
    @Override
    public void paint(Graphics g, UkaWindow window)
    {
        g.drawImage(window.getLayers().cacheImage, 0, 0, window);
        AWTUtilities.setWindowShape(window, window.getLayers().cacheMask);

        return;
    }

    /**
     * ���»���ͼ�㻺��Ľӿ�
     */
    public void propertyChange(PropertyChangeEvent ev)
    {
        System.out.println("Caught Layer Change");

        return;
    }

  // Miscellaneous | ����

    public static void main(String[] args)
    {
        SwingRenderer renderer = new SwingRenderer();

        // ��ȡ XML ���ܲ���
        System.out.println(
            renderer.loadImageMeta(
                UkaDaemon.getProgramDir()+"/res/shell/StubShell/StubXML.xml",
                "/root/image/img"
            )
        );

        // ��ȡͼ�����
        renderer.loadImage(
            UkaDaemon.getProgramDir()+"/res/shell/StubShell/StubXML.xml",
            "/root/image/img",
            DEFAULT_FILTER
        );

        System.out.println(renderer.imgCont.values());
        System.out.println(renderer.imgMask.values());

        return;
    }
}
