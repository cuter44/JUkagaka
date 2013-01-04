// This file is created by Editplus

package jukagaka.shell;

import java.awt.Rectangle;
import javax.swing.JDialog;

public abstract class JUkaWindow extends JDialog
{
  // Abstract | 抽象函数列表
    abstract void clip();

  // Align & Position
    /**
     * 此组参数用以传递给 setBalloonPos
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
     * <p>使用简单的方法指定窗体相对于另一窗体的位置</p>
     * @param refWin指定被参照的窗体
     * @param hAlign指定水平的对齐方法, 适用从JUkaWindow.NO_MODIFY到JUkaWindow.ON_THE_BOTTOM
     * @param vAlign指定垂直的对齐方法, 域同上.
     */
    public void alignTo(JUkaWindow referWin, int hAlign, int vAlign)
    {
        Rectangle rRect = referWin.getBounds();
        Rectangle tRect = this.getBounds();

        // 计算水平位置
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

        // 计算垂直位置
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
