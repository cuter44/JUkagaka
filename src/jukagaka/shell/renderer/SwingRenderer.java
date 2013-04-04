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

  // Image Toolkit | 工具箱
    // 为处理图像及生成附加信息而编写的工具函数
  // 代码
    /**
     * 从图片产生轮廓<br />
     * <br />
     * @param image 传入的图像
     * @param filter 决定像素是否舍弃的过滤器
     * @return 图像的轮廓区域
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

            // 行扫描(行先序线性扫描不透明像素)
            for (y = 0; y < height; y++)
            {
                xs = -1;
                // 列扫描
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
                // 行尾检查
                if (xs!=-1)
                    mask.add(new Area(new Rectangle(xs,y,width-xs,1)));
            }
        }
        catch (InterruptedException ex)
        {
            System.err.println("error:SwingRenderer.cutOut():被打断了!?");
            ex.printStackTrace();
        }

        return(mask);
    }
  // 接口
    /**
     * 用于分辨像素是否透明的内部类
     */
    public interface PixelFilter
    {
        /**
         * 判断像素是否"透明", 被认为"透明"的像素在渲染时会被裁剪<br />
         * <br />
         * 在计算裁剪区域时会进行逐点判断, 当前像素(以ARGB格式)会被作为参数传入.
         * 由接口的实现者决定该像素是否会裁剪.
         * @return 如果要裁剪此像素应返回true, 否则返回false
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

  // Image Prefetch | 图像缓取
    // 从磁盘读取及预处理图像过程, 准备为其他部分使用.
  // 数据
    /**
     * 图像的XML信息缓存
     */
    private List<Element> imgMeta = null;
    /**
     * 图像的像素缓存
     */
    private Map<String,Image> imgCont = null;
    /**
     * 图像的蒙版缓存
     */
    private Map<String,Area> imgMask = null;
  // 代码
    /**
     * 加载存储在XML中的图像列表
     *
     * @param xmlFileName 要加载的文件名
     * @param xPath 用于从XML中筛选的XPath表达式
     * 关于XPath表达式请参考wiki
     * @return 在解析失败时返回false(比如读取错误, XPath格式不正确之类), 否则返回true.
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
            System.err.println("error:SwingRenderer.loadImageGroup():解析XML时发生错误:");
            ex.printStackTrace();
            return(false);
        }

        return(true);
    }

    /**
     * 加载图像内容, 根据已加载的XML元素表
     *
     * @return 现在它总是返回false
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
                    // TODO 缩放平移等
                    map.put(id, img);
                    tracker.addImage(img, 0);
                }
                catch (NullPointerException ex)
                {
                    XMLOutputter outputter = new XMLOutputter();
                    System.out.println("error:SwingRenderer.loadImageContent():XML元素没有id和/或value属性:"+outputter.outputString(elem));
                    ex.printStackTrace();
                    continue;
                }
            }
            tracker.waitForAll();
            this.imgCont = map;
        }
        catch (InterruptedException ex)
        {
            // 不太可能被打断吧...?
            ex.printStackTrace();
        }

        return(true);
    }

    /**
     * 为已加载的图像计算蒙版
     *
     * @return 目前它总是返回true
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
     * 按照一般的模式载入图像<br />
     * <br />
     * 会依次地调用loadImageMeta(), loadImageContent(), loadImageMask()以产生
     * 绘制所需的必要信息<br />
     * @param xmlFileName 准备读取的 XML 文件名, 该文件应该符合<a href="">一定的规则</a>
     * @param xPath 用来从xml文档中筛选元素(Element)的<a href="">XPath表达式</a>
     * @param filter 提供给loadImageMask用于判断像素是否透明的条件
     * @see jukagaka.shell.renderer.SwingRenderer.PixelFilter
     * @return 目前它总是返回true.
     */
    public boolean loadImage(String xmlFileName, String xPath, PixelFilter filter)
    {
        this.loadImageMeta(xmlFileName, xPath);
        this.loadImageContent();
        this.loadImageMask(filter);

        return(true);
    }

    /**
     * 和 loadImage(String,String,PixelFilter) 的默认实现,
     * 该方法将图像中的完全透明像素认作透明像素.
     */
    public boolean loadImage(String xmlFileName, String xPath)
    {
        return(
            this.loadImage(xmlFileName, xPath, DEFAULT_FILTER)
        );
    }
  // 调试
    /**
     * 以下几个get/set按照其字面意思工作, 设置这几个函数只是为了调试用途,
     * 除非你清楚自己要做什么, 否则不要轻易使用这些函数
     * 对 SwingRenderer 的作出修改.
     */
    public List<Element> getImageMeta()
    {
        return(this.imgMeta);
    }

    /**
     * 以下几个get/set按照其字面意思工作, 设置这几个函数只是为了调试用途,
     * 除非你清楚自己要做什么, 否则不要轻易使用这些函数
     * 对 SwingRenderer 的作出修改.
     */
    public void setImageMeta(List<Element> argImgMeta)
    {
        this.imgMeta = argImgMeta;
        return;
    }

    /**
     * 以下几个get/set按照其字面意思工作, 设置这几个函数只是为了调试用途,
     * 除非你清楚自己要做什么, 否则不要轻易使用这些函数
     * 对 SwingRenderer 的作出修改.
     */
    public Map<String,Image> getImageContent()
    {
        return(this.imgCont);
    }

    /**
     * 以下几个get/set按照其字面意思工作, 设置这几个函数只是为了调试用途,
     * 除非你清楚自己要做什么, 否则不要轻易使用这些函数
     * 对 SwingRenderer 的作出修改.
     */
    public void setImageContent(Map<String, Image> argImgCont)
    {
        this.imgCont = argImgCont;
        return;
    }

    /**
     * 以下几个get/set按照其字面意思工作, 设置这几个函数只是为了调试用途,
     * 除非你清楚自己要做什么, 否则不要轻易使用这些函数
     * 对 SwingRenderer 的作出修改.
     */
    public Map<String,Area> getImageMask()
    {
        return(this.imgMask);
    }

    /**
     * 以下几个get/set按照其字面意思工作, 设置这几个函数只是为了调试用途,
     * 除非你清楚自己要做什么, 否则不要轻易使用这些函数
     * 对 SwingRenderer 的作出修改.
     */
    public void setImageMask(Map<String,Area> argImgMask)
    {
        this.imgMask = argImgMask;
        return;
    }

    /**
     * 调试用途
     */
    public Image getImage(String id)
    {
        return(this.imgCont.get(id));
    }

    public Area getMask(String id)
    {
        return(this.imgMask.get(id));
    }

  // Renderer | 渲染回调接口
    @Override
    public void inject(UkaWindow argWindow)
    {
        // Ukagaka
        if (argWindow instanceof Ukagaka)
        {
            final Ukagaka window = (Ukagaka)argWindow;

            // 更换画布
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

            // 更换画布
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

        // 注册图层侦听器
        argWindow.addLayerChangeListener(this);

        // TODO Something else?

        return;
    }

    /**
     * 完成实际重绘和雕刻窗体工作
     */
    @Override
    public void paint(Graphics g, UkaWindow window)
    {
        g.drawImage(window.getLayers().cacheImage, 0, 0, window);
        AWTUtilities.setWindowShape(window, window.getLayers().cacheMask);

        return;
    }

    /**
     * 更新绘制图层缓存的接口
     */
    public void propertyChange(PropertyChangeEvent ev)
    {
        System.out.println("Caught Layer Change");

        return;
    }

  // Miscellaneous | 杂项

    public static void main(String[] args)
    {
        SwingRenderer renderer = new SwingRenderer();

        // 读取 XML 机能测试
        System.out.println(
            renderer.loadImageMeta(
                UkaDaemon.getProgramDir()+"/res/shell/StubShell/StubXML.xml",
                "/root/image/img"
            )
        );

        // 读取图像测试
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
