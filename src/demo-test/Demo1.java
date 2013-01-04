// This demo is to test damned

import jukagaka.JUkaShellCtrl;
import jukagaka.shell.*;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.AffineTransform;

class Demo1
{
    public static void main(String[] args)
    {
        Image img = Toolkit.getDefaultToolkit().getImage("surface1001.png");
        //Area mask = JUkaShellCtrl.calculateMask(img);
        Area mask = new Area(new Rectangle(10, 10, 100, 100));
        PathIterator iter = mask.getPathIterator(null);
        float[] points = new float[6];

        int i = 1;
        while (!iter.isDone())
        {

            System.out.print(i++);

            int status = iter.currentSegment(points);

            if (status == PathIterator.SEG_MOVETO)
                System.out.println("Move to " + points[0] + ", " + points[1]);

            if (status == PathIterator.SEG_LINETO)
                System.out.println("Line to " + points[0] + ", " + points[1]);

            if (status == PathIterator.SEG_CLOSE)
                System.out.println("End.");

            iter.next();
        }

        System.out.println(iter.getWindingRule() == PathIterator.WIND_EVEN_ODD);
        System.out.println("---- Transformed ----");
        AffineTransform tx = new AffineTransform(1.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        double[] txArgs = new double[6];
        tx.getMatrix(txArgs);
        for (i=0; i<=5; i++)
            System.out.println(txArgs[i]);

        Area transMask = mask.createTransformedArea(tx);

        System.out.println(transMask.isEmpty());

        while (!iter.isDone())
        {
            int status = iter.currentSegment(points);

            if (status == PathIterator.SEG_MOVETO)
                System.out.println("Move to " + points[0] + ", " + points[1]);

            if (status == PathIterator.SEG_LINETO)
                System.out.println("Line to " + points[0] + ", " + points[1]);

            if (status == PathIterator.SEG_CLOSE)
                System.out.println("End.");

            iter.next();
        }

        //Area rect = new Area(new Rectangle(10, 10, 100, 100));
        //rect.transform(new AffineTransform(1.0, 0.0, 0.0, 0.0, 1.0, 0.0));
        //System.out.println(rect.isEmpty());


        //System.out.println(System.getProperty("java.class.path"));
        return;
    }
}
