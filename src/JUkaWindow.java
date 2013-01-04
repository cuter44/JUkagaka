// This file is created by Editplus

package jukagaka.shell;

import java.awt.Rectangle;
import javax.swing.JDialog;

public abstract class JUkaWindow extends JDialog
{
  // Abstract | �������б�
    abstract void clip();

  // Align & Position
    /**
     * ����������Դ��ݸ� setBalloonPos
     */
    public static final int NO_MODIFY     = 0;
    public static final int ON_THE_LEFT   = 1;
    public static final int ON_THE_TOP    = 1;
    public static final int LEFT_ALIGN    = 2;
    public static final int TOP_ALIGN     = 2;
    public static final int CENTER_ALIGN  = 3;
    public static final int RIGHT_ALIGN   = 4;
    public static final int BOTTOM_ALIGN  = 4;
    public static final int ON_THE_RIGHT  = 5;
    public static final int ON_THE_BOTTOM = 5;

    /**
     * <p>ʹ�ü򵥵ķ���ָ�������������һ�����λ��</p>
     * @param refWinָ�������յĴ���
     * @param hAlignָ��ˮƽ�Ķ��뷽��, ���ô�JUkaWindow.NO_MODIFY��JUkaWindow.ON_THE_BOTTOM
     * @param vAlignָ����ֱ�Ķ��뷽��, ��ͬ��.
     */
    public void alignTo(JUkaWindow referWin, int hAlign, int vAlign)
    {
        Rectangle rRect = referWin.getBounds();
        Rectangle tRect = this.getBounds();

        // ����ˮƽλ��
        switch (hAlign)
        {
            case ON_THE_LEFT:
                tRect.x = rRect.x - tRect.width;
                break;
            case ON_THE_RIGHT:
                tRect.x = rRect.x + rRect.width;
                break;
            case LEFT_ALIGN:
                tRect.x = rRect.x;
                break;
            case RIGHT_ALIGN:
                tRect.x = rRect.x + rRect.width - tRect.width;
                break;
            case CENTER_ALIGN:
                tRect.x = rRect.x + (rRect.width>>1) - (tRect.width>>1);
                break;
            case NO_MODIFY:
            default:
        }

        // ���㴹ֱλ��
        switch (vAlign)
        {
            case ON_THE_TOP:
                tRect.y = rRect.y - tRect.height;
                break;
            case ON_THE_BOTTOM:
                tRect.y = rRect.y + rRect.height;
                break;
            case TOP_ALIGN:
                tRect.y = rRect.y;
                break;
            case BOTTOM_ALIGN:
                tRect.y = rRect.y + rRect.height - tRect.height;
                break;
            case CENTER_ALIGN:
                tRect.y = rRect.y + (rRect.height>>1) - (tRect.height>>1);
                break;
            case NO_MODIFY:
            default:
        }

        this.setBounds(tRect);

        return;
    }

    //public static void main(String[] args)
    //{
        //return;
    //}
}
