package jukagaka.shell.renderer;

/* javafx */
import javafx.application.Platform;
import javafx.scene.web.WebView;
import javafx.scene.Scene;
import javafx.embed.swing.JFXPanel;
/* graphics */
import java.awt.Graphics;
/* awt/swing */
import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.HierarchyEvent;
/* jukagaka */
import jukagaka.shell.UkaWindow;

public class WebViewRenderer extends JFXPanel implements PropertyChangeListener, HierarchyListener
{
  // Hierarchy | 继承关系变化
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

    @Override
    public void hierarchyChanged(HierarchyEvent ev)
    {
        if ((ev.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0)
        {
            if (this.belongWindow != null)
                this.belongWindow.removePropertyChangeListener("renderData", this);
            this.validateBelongWindow();
            if (this.belongWindow != null)
                this.belongWindow.addPropertyChangeListener("renderData", this);
        }

        return;
    }

  // Renderer | 渲染
    // (基本没用...)
  // 方法
    @Override
    public void paintComponent(Graphics g)
    {
        // JFXP 的绘制机制和一般的 JComponent 不同所以要调用父类方法.
        super.paintComponent(g);
    }

    @Override
    public void propertyChange(PropertyChangeEvent ev)
    {
        if (ev.getPropertyName() ==  "renderData")
        {
            try
            {
                final String url = (String)this.belongWindow.getRenderData();

                if (url == null)
                    return;

                Platform.runLater(
                    new Runnable()
                    {
                        public void run()
                        {
                            WebViewRenderer.this
                                .webPanel
                                .getEngine()
                                .load(url);
                        }
                    }
                );
            }
            catch (ClassCastException ex)
            {
                ex.printStackTrace();
                return;
            }
        }
        // end_if
        return;
    }

  // Web View | Web 控件
    // 用于显示 HTML 内容
  // 数据
    private WebView webPanel = null;
  // 方法
    private void initWebPanel()
    {
        Platform.runLater(
            new Runnable()
            {
                @Override
                public void run()
                {
                    WebViewRenderer.this.webPanel = new WebView();
                    WebViewRenderer.this.setScene(new Scene(WebViewRenderer.this.webPanel));
                }
            }
        );
    }

    public WebView getWebPanel()
    {
        return(this.webPanel);
    }

  // Constructor | 构造器
    public WebViewRenderer()
    {
        super();
        this.init();

        return;
    }

    public void init()
    {
        this.initWebPanel();
        this.addHierarchyListener(this);

        return;
    }

    //public static void main(String[] args)
    //{
        //return;
    //}
}
