package jukagaka.shell.util;

/* image */
import java.awt.Image;
import java.awt.Shape;
/* graphics */
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.MediaTracker;
import java.awt.image.PixelGrabber;
import java.awt.geom.Area;
/* jukagaka */
import jukagaka.shell.util.EdgeDetector;

public class LinealEdgeDetector implements EdgeDetector
{
  // Edge Detect | 边沿检测
  // 数据
    public PixelFilter filter;
  // 方法
    @Override
    public Shape shapeOf(Image image)
    {
        PixelGrabber grabber = new PixelGrabber(image, 0, 0, -1, -1, true);
        Area shape = new Area();
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
                    if (!filter.isOpaque(pixels[y * width + x]))
                    {
                        if (xs != -1)
                        {
                            shape.add(new Area(new Rectangle(xs,y,x-xs,1)));
                            xs = -1;
                        }
                    }
                    else
                        if (xs == -1)
                            xs = x;
                }
                // 行尾检查
                if (xs!=-1)
                    shape.add(new Area(new Rectangle(xs,y,width-xs,1)));
            }
        }
        catch (InterruptedException ex)
        {
            System.err.println("error:LinealEdgeDector.shapeOf():被打断了!?");
            ex.printStackTrace();
        }

        return(shape);
    }
  // 接口
    /**
     * 用于分辨像素是否透明的内部类
     */
    public interface PixelFilter
    {
        /**
         * 判断像素是否"遮光", 被认为"遮光"的像素在渲染时被显示, 否则会被透明化处理<br />
         * <br />
         * 在计算裁剪区域时会进行逐点判断, 当前像素(以ARGB格式)会被作为参数传入.
         * @return 认为"遮光"(绘制时显示)返回true, 否则(绘制时不显示)返回false
         */
        public abstract boolean isOpaque(int pixel);
    }
  // 预设
    /**
     * 严格的默认检边器, 认为 Alpha 为 255 的像素为不透明像素
     */
    public final static PixelFilter STRICT_DEFAULT_FILTER =
        new PixelFilter()
        {
            @Override
            public boolean isOpaque(int pixel)
            {
                if ((pixel & 0xff000000) == 0xff000000)
                    return(true);
                else
                    return(false);
            }
        };

    /**
     * 宽松的默认检边器, 认为 Alpha 不为 0 的像素为不透明像素
     */
    public final static PixelFilter LOOSE_DEFAULT_FILTER =
        new PixelFilter()
        {
            @Override
            public boolean isOpaque(int pixel)
            {
                if ((pixel & 0xff000000) != 0x00000000)
                    return(true);
                else
                    return(false);
            }
        };

  // Constructor | 构造器
  // 方法
    public LinealEdgeDetector()
    {
        this.filter = LOOSE_DEFAULT_FILTER;

        return;
    }

    public LinealEdgeDetector(PixelFilter argFilter)
    {
        this.filter = argFilter;

        return;
    }

    //public static void main(String[] args)
    //{
        //return;
    //}
}
