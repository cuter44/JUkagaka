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
  // Edge Detect | ���ؼ��
  // ����
    public PixelFilter filter;
  // ����
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

            // ��ɨ��(����������ɨ�費͸������)
            for (y = 0; y < height; y++)
            {
                xs = -1;
                // ��ɨ��
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
                // ��β���
                if (xs!=-1)
                    shape.add(new Area(new Rectangle(xs,y,width-xs,1)));
            }
        }
        catch (InterruptedException ex)
        {
            System.err.println("error:LinealEdgeDector.shapeOf():�������!?");
            ex.printStackTrace();
        }

        return(shape);
    }
  // �ӿ�
    /**
     * ���ڷֱ������Ƿ�͸�����ڲ���
     */
    public interface PixelFilter
    {
        /**
         * �ж������Ƿ�"�ڹ�", ����Ϊ"�ڹ�"����������Ⱦʱ����ʾ, ����ᱻ͸��������<br />
         * <br />
         * �ڼ���ü�����ʱ���������ж�, ��ǰ����(��ARGB��ʽ)�ᱻ��Ϊ��������.
         * @return ��Ϊ"�ڹ�"(����ʱ��ʾ)����true, ����(����ʱ����ʾ)����false
         */
        public abstract boolean isOpaque(int pixel);
    }
  // Ԥ��
    /**
     * �ϸ��Ĭ�ϼ����, ��Ϊ Alpha Ϊ 255 ������Ϊ��͸������
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
     * ���ɵ�Ĭ�ϼ����, ��Ϊ Alpha ��Ϊ 0 ������Ϊ��͸������
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

  // Constructor | ������
  // ����
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
