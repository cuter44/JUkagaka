/**
 * @author "Galin"<cuter44@qq.com>
 * @version v120827
 */

package jukagaka.ghost;

import jukagaka.*;

public class JUkaGhost extends JUkaComponent
{
  // Trigger | 触发器
    /**
     * <p>此方法不包含任何代码, 仅仅作为 token 存在.</p>
     * <p>(*) 任何扩展此类的子类都必须以此方法签名重写该方法<br>
     * 子类的该方法会在 JUkagaka 启动时被调用, 以提供机会让 Ghost 完成初始化工作</p>
     */
    public static void onLoad()
    {
        return;
    }

    /**
     * <p>此方法不包含任何代码, 仅仅作为 token 存在.</p>
     * <p>(*) 任何扩展此类的子类都必须以此方法签名重写该方法<br>
     * 子类的该方法会在所有 JUkaComponentCtrl 初始化完成后被调用, 以引导 Ghost 开始工作</p>
     */
    public static void onStart()
    {
        return;
    }

  // Other | 杂项
    /**
     * @deprecated 此方法目前仅用于调试
     */
    public static void main(String[] args)
    {
        return;
    }
}
