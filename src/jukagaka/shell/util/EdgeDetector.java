package jukagaka.shell.util;

/* image */
import java.awt.Shape;
import java.awt.Image;

/**
 * ���ؼ�����Ľӿ�
 *
 * ʹ����ͨ��ʵ�� shapeOf() ����, ������ͼ�񷵻��䲻͸������
 *
 */

public interface EdgeDetector
{
  // Edge Detecting | ���ؼ��
    public abstract Shape shapeOf(Image image);

    //public static void main(String[] args)
    //{
        //return;
    //}
}
