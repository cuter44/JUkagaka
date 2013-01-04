import java.util.LinkedList;
import java.lang.Integer;

public class Demo5
{
    private String oriString = "$em|cyau_st0,4,0,0|cyau_eye2,5,30,31|cyau_cloth1,6,0,0|cyau_cloth2,7,0,0;$p|2000;$em|,5,,;$em|cyau_st0,4,0,0;$p|100;$p300;";

    /**
     * <p>传递给setEmotion的参数序列</p>
     */
    private LinkedList<String> aArg = new LinkedList<String>();
    /**
     * <p>用于暂停的参数列</p>
     */
    private LinkedList<Integer> pArg = new LinkedList<Integer>();
    /**
     * <p>用于记录动作的参数列</p>
     */
    private LinkedList<Character> cArg = new LinkedList<Character>();

    private int coConsumed;

    // 算法密集区, 慎入慎改
    private String doParse(String argMacro)
    {
        /* This block is for So, the beloved one.
         * For "Not every thing we can find its cause."
         * But there it lies, though I wish to deny.
         * Since there is so-called "parse"
         */

        // 源串
        //StringBuffer macro= new StringBuffer(argMacro);
        // 目标串
        StringBuffer parsedStr = new StringBuffer();

        // 源串标记 读取位置, 复制位置:已处理的位置
        int pos = 0, pastePos = 0;
        // 生成串标记 写入位置, 宏位置:宏起始点也就是$的位置.
        int /*parsedStr.length() = 0,*/parsePos = 0;

        // 宏内部标志
        boolean inMacro = false;

        while (pos < argMacro.length())
        {
            // if $
            if (argMacro.charAt(pos) == '$')
            {
                // 排空缓冲
                if (pastePos != pos)
                    parsedStr.append(argMacro.substring(pastePos,pos));
                pastePos = pos;

                // 嵌套宏:递归调用doParse()
                if (inMacro)
                {
                    parsedStr.append(this.doParse(argMacro.substring(pos,argMacro.length())));
                    pos += this.coConsumed;
                    pastePos = pos;
                    continue;
                }
                // 宏起点:标记宏起点
                else
                {
                    parsePos = parsedStr.length();
                    inMacro = true;
                    pos++;
                    continue;
                }
            }
            // endif $

            // if ;
            if (argMacro.charAt(pos) == ';')
            {
                // 宏终点:排空缓冲,启动宏解释
                if (inMacro)
                {
                    inMacro = false;
                    pos++;

                    if (pastePos != pos)
                        parsedStr.append(argMacro.substring(pastePos,pos));
                    pastePos = pos;

                    // 脱出完整的宏
                    String macro = parsedStr.substring(parsePos);
                    boolean matched = false;
                    // 匹配 $em|
                    if (!matched && macro.matches("$\\$em\\|"))
                    {
                        this.cArg.push('a');
                        this.aArg.push(macro.substring(4,macro.length()-1));
                        parsedStr.replace(parsePos,parsedStr.length(),"");
                        matched = true;
                    }
                    // 匹配 $p|
                    if (!matched && macro.matches("$\\$p\\|"))
                    {
                        this.cArg.push('p');
                        this.pArg.push(Integer.parseInt(macro.substring(3,macro.length()-1)));
                        parsedStr.replace(parsePos,parsedStr.length(),"");
                        matched = true;
                    }
                    // else 交由Utility解释(未作成)

                    parsePos = parsedStr.length();
                    continue;
                }
                // 外侧宏:交还控制权由上一层解释
                else
                    break;
            }
            // endif ;

            // else other character;
            pos++;
        }
        // endloop
        // 写入缓冲数据
        if (pastePos < argMacro.length())
            parsedStr.append(argMacro.substring(pastePos,argMacro.length()));
        // 写入返回数据
        this.coConsumed = pastePos;
        return(parsedStr.toString());
    }

    // 此demo用于测试分割器代码
    public static void main(String args[])
    {
        Demo5 demo = new Demo5();
    }
}