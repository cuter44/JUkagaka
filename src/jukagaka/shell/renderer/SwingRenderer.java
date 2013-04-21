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
  // Edge Detect | 边沿检测
    private EdgeDetector edgeDetector = null;

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
     * 图像的轮廓缓存
     */
    private Map<String,Shape> imgShape = null;
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
     * 按照一般的模式载入图像<br />
     * <br />
     * 会依次地调用loadImageMeta(), loadImageContent(), loadImageShape()以产生
     * 绘制所需的必要信息<br />
     * @param xmlFileName 准备读取的 XML 文件名, 该文件应该符合<a href="">一定的规则</a>
     * @param xPath 用来从xml文档中筛选元素(Element)的<a href="">XPath表达式</a>
     * @return 目前它总是返回true.
     */
    public boolean loadImage(String xmlFileName, String xPath)
    {
        this.loadImageMeta(xmlFileName, xPath);
        this.loadImageContent();
        this.loadImageShape();

        return(true);
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
     * 调试用途
     */

  // Buffer | 多图层缓冲
    // 原图层机制的替换机制, 即原图层机制的模拟实现.
    // 核心的思想是当第一次进行图层叠加绘制时计算并储存图像, 在再次使用相同的图层叠加时提高响应速度
    // 对单图层绘制速度几乎不会造成影响
  // 方法
    /**
     * 目前这两个方法只能简单地按给出的信息抽取已有的图像, 无法进行图层叠加
     */
    public Image getImage(String id)
    {
        return(this.imgCont.get(id));
    }

    public Shape getShape(String id)
    {
        return(this.imgShape.get(id));
    }

  // Renderer | 渲染
  // 数据
    private UkaWindow belongWindow = null;
  // 方法
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
     * 继承自 HierarchyListener 的方法<br />
     * <br />
     * 主要是为了响应 setContentPane() 调用
     * 目前附加的逻辑有:<br />
     * <ul>
     * <li>获取所属窗体</li>
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
     * 继承自 PropertyChangeListener 的方法<br />
     * <br />
     * 主要是为了响应 图层变化事件
     * 目前附加的逻辑有:<br />
     * <ul>
     * <li>(什么也没有...)</li>
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
     * 重绘的方法
     */
    @Override
    public void paintComponent(Graphics g)
    {
        try
        {
            String renderData = (String)this.belongWindow.getRenderData();
            Image renderImage = this.getImage(renderData);
            Shape renderShape = this.getShape(renderData);

            // 绘制背景
            g.drawImage(
                renderImage,
                0, 0,
                this.belongWindow
            );
            // 设置窗体大小
            this.belongWindow.setSize(
                renderImage.getWidth(this.belongWindow),
                renderImage.getHeight(this.belongWindow)
            );
            // 裁剪窗体
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

  // Constructor & Initalization | 构造器
    public SwingRenderer()
    {
        this.edgeDetector = new LinealEdgeDetector();
        // TODO 加载图像信息
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

  // Miscellaneous | 杂项
    public static void main(String[] args)
    {
        SwingRenderer renderer = new SwingRenderer();

        return;
    }
}
