/**
 * @author "Galin"<cuter44@qq.com>
 * @version v120827
 */

package jukagak.shell;

import jukagaka.*;

import java.io.File;
import java.util.Hashtable;
import java.util.ArrayList;
import java.awt.Image;
import java.awt.geom.Area;
import javax.swing.JWindow;

public class JUkaShell extends JUkaComponent implements Serializable
{
  // Static Data (NEED REWRITE) | 待重写静态数据域
    // 请将这些数据域复制到你重写的子类中, 并按需要修改其中的字符串内容.
    // 非字符串值及数据域签名请勿改动.
    /**
     * <p>此数据域记录此 Shell 的编程访问名字, 没有多大用...</p>
     * <p>(*) 此类的扩展者应重写这个域</p>
     */
    public static final String shellName = "jukagaka.shell.JUkaShell";

    /**
     * <p>此数据域记录此 Shell 对应的默认 Ghost 名字</p>
     * <p>名字由完整的包名和类名组成, 并且大小写敏感<br>
     * JUkagaka 将在创建新的 Shell 时查找并提供这个默认的 Ghost.<br>
     * (*) 此类的扩展者应重写这个域</p>
     */
    public static final String defaultGhost = "jukagaka.ghost.JUkaGhost";

    /**
     * <p>此数据域记录此春菜的称谓</p>
     * <p>(*) 此类的扩展者应重写这个域</p>
     */
    public static final String ukagakaName = "JUkaShell";

    /**
     * <p>此数据域记录此春菜的自称</p>
     * <p>(*) 此类的扩展者应重写这个域</p>
     */
    public static final String ukagakaSelfCall = "JUkaShell";

    /**
     * <p>此数据域记录人格图像资源文件的引用</p>
     * <p>(*) 此类的扩展者应重写这个域<br>
     * .images是一个plain-text文件, 其中:<br>
     * &nbsp;第一行是一个整数n表示共有n个关联对<br>
     * &nbsp;此后包含n个组, 其中每组中的:
     * &nbsp;&nbsp;第1行是一个图像文件名, 以对于JUkaUtility.getProgramPath()
     * 的相对路径表示<br>
     * &nbsp;&nbsp;此后包含 0 到若干行, 表示要注册的图像别名<br>
     * &nbsp;&nbsp;单独一行 "-" 表示此组结束<br>
     * &nbsp;表示将这些别名映射到图像资源上<br>
     * 这些字串将用于嵌入宏/setBackground()等功能上以唯一地标识一个图像资源<br>
     * <del>(!) 暂时还无法解决将多于两个名字绑定到同一图像时的预读重复问题(其实是因为懒)</del><br>
     * 在同一个shell中定义的别名不允许重复(废话)<br>
     * 大小写敏感</p>
     */
    public static final File IMAGE_CATALOG = new File(JUkaUtility.getProgramPath() + "default.images");

    /**
     * <p>hashImages 和 hashShape 数据域用于挂载缓冲的图像资源</p>
     * <p>此二数据域由 JUkaShellCtrl.prefetchImageResource() 负责写入</p>
     */
    public static Hashtable<String, Image> hashImages = null;
    public static Hashtable<String, Area> hashMasks = null;

  // Instance Data | 实例数据域
    /**
     * 此数据域记录当前可用的气球队列
     */
    private ArrayList<JWindow> frameList = null;
    /**
     * 此数据域记录承载春菜的 Frame
     */
    private UkagakaFrame ukagakaFrame = null;
    /**
     * 此数据域记录主菜单的 Frame
     */
    private BalloonFrame mainBalloonFrame = null;
    /**
     * 此数据域标记 Shell 是否已经废弃
     */
    private boolean discarded = false;

  // Shell Instance | Shell 实例管理
  // 请按照要求重写这些方法.
    /**
     * <p>此方法用于产生一个新的 Shell</p>
     * <p><br>
     * (*) 此类的扩展者应按照该签名重写方法, 并在自撰的代码<b>前</b>调用父类的方法</p>
     */
    public static JUkaShell createShell()
    {
        JUkaShell newShell = new JUkaShell();
        newShell.ukagakaFrame = UkagakaFrame.createFrame();
        newShell.mainBalloonFrame = balloonFrame.createFrame();
        return(null);
    }

    /**
     * <p>此方法用于回收一个 Shell 使之不再有效</p>
     * <p>此方法将销毁春菜占用的资源<br>
     * (*) 此类的扩展者应按照该签名重写方法, 并在自撰的代码<b>后</b>调用父类的方法</p>
     */
    public static void destroyShell()
    {

        return;
    }

  // Start-up | 启动器
    /**
     * <p>此方法目前不包含任何代码, 仅仅作为 token 存在.</p>
     * <p>子类的该方法会在 JUkagaka 启动时被调用, 组件可以趁此完成初始化工作<br>
     * 如果子类要表示不能正常加载, 抛出任意 Exception 即可.<br>
     * (*) 此类的扩展这应按照该签名重写方法<br></p>
     */
    public static void onLoad()
    {
        return;
    }

    /**
     * <p>此方法用于退出时调用</p>
     * <p>子类的该方法会在 JUkagaka 退出前被调用, 组件可以趁此完成退出前工作<br>
     * (*) 此类的扩展这应按照该签名重写方法, 并在自撰的代码<b>后</b>调用父类的方法<br></p>
     */
    public static void onExit()
    {
        return;
    }

  // Other | 其他
    /**
     * 不允许访问的构造函数
     */
    private JUkaShell()
    {
    }
    /**
     * @deprecated 此方法目前仅用于调试
     */
    public static void main(String[] args)
    {
        return;
    }
}

