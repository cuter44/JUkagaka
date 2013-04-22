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
  // Hierarchy | �̳й�ϵ�仯
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

  // Renderer | ��Ⱦ
    // (����û��...)
  // ����
    @Override
    public void paintComponent(Graphics g)
    {
        // JFXP �Ļ��ƻ��ƺ�һ��� JComponent ��ͬ����Ҫ���ø��෽��.
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

  // Web View | Web �ؼ�
    // ������ʾ HTML ����
  // ����
    private WebView webPanel = null;
  // ����
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

  // Constructor | ������
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
