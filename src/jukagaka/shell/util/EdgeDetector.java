package jukagaka.shell.util;

/* image */
import java.awt.Shape;
import java.awt.Image;

/**
 * 边沿检测器的接口
 *
 * 使用者通过实现 shapeOf() 方法, 对所给图像返回其不透光区域
 *
 */

public interface EdgeDetector
{
  // Edge Detecting | 边沿检测
    public abstract Shape shapeOf(Image image);

    //public static void main(String[] args)
    //{
        //return;
    //}
}
