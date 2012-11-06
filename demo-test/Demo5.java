import java.util.LinkedList;
import java.lang.Integer;

public class Demo5
{
    private String oriString = "$em|cyau_st0,4,0,0|cyau_eye2,5,30,31|cyau_cloth1,6,0,0|cyau_cloth2,7,0,0;$p|2000;$em|,5,,;$em|cyau_st0,4,0,0;$p|100;$p300;";

    /**
     * <p>���ݸ�setEmotion�Ĳ�������</p>
     */
    private LinkedList<String> aArg = new LinkedList<String>();
    /**
     * <p>������ͣ�Ĳ�����</p>
     */
    private LinkedList<Integer> pArg = new LinkedList<Integer>();
    /**
     * <p>���ڼ�¼�����Ĳ�����</p>
     */
    private LinkedList<Character> cArg = new LinkedList<Character>();

    private int coConsumed;

    // �㷨�ܼ���, ��������
    private String doParse(String argMacro)
    {
        /* This block is for So, the beloved one.
         * For "Not every thing we can find its cause."
         * But there it lies, though I wish to deny.
         * Since there is so-called "parse"
         */

        // Դ��
        //StringBuffer macro= new StringBuffer(argMacro);
        // Ŀ�괮
        StringBuffer parsedStr = new StringBuffer();

        // Դ����� ��ȡλ��, ����λ��:�Ѵ����λ��
        int pos = 0, pastePos = 0;
        // ���ɴ���� д��λ��, ��λ��:����ʼ��Ҳ����$��λ��.
        int /*parsedStr.length() = 0,*/parsePos = 0;

        // ���ڲ���־
        boolean inMacro = false;

        while (pos < argMacro.length())
        {
            // if $
            if (argMacro.charAt(pos) == '$')
            {
                // �ſջ���
                if (pastePos != pos)
                    parsedStr.append(argMacro.substring(pastePos,pos));
                pastePos = pos;

                // Ƕ�׺�:�ݹ����doParse()
                if (inMacro)
                {
                    parsedStr.append(this.doParse(argMacro.substring(pos,argMacro.length())));
                    pos += this.coConsumed;
                    pastePos = pos;
                    continue;
                }
                // �����:��Ǻ����
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
                // ���յ�:�ſջ���,���������
                if (inMacro)
                {
                    inMacro = false;
                    pos++;

                    if (pastePos != pos)
                        parsedStr.append(argMacro.substring(pastePos,pos));
                    pastePos = pos;

                    // �ѳ������ĺ�
                    String macro = parsedStr.substring(parsePos);
                    boolean matched = false;
                    // ƥ�� $em|
                    if (!matched && macro.matches("$\\$em\\|"))
                    {
                        this.cArg.push('a');
                        this.aArg.push(macro.substring(4,macro.length()-1));
                        parsedStr.replace(parsePos,parsedStr.length(),"");
                        matched = true;
                    }
                    // ƥ�� $p|
                    if (!matched && macro.matches("$\\$p\\|"))
                    {
                        this.cArg.push('p');
                        this.pArg.push(Integer.parseInt(macro.substring(3,macro.length()-1)));
                        parsedStr.replace(parsePos,parsedStr.length(),"");
                        matched = true;
                    }
                    // else ����Utility����(δ����)

                    parsePos = parsedStr.length();
                    continue;
                }
                // ����:��������Ȩ����һ�����
                else
                    break;
            }
            // endif ;

            // else other character;
            pos++;
        }
        // endloop
        // д�뻺������
        if (pastePos < argMacro.length())
            parsedStr.append(argMacro.substring(pastePos,argMacro.length()));
        // д�뷵������
        this.coConsumed = pastePos;
        return(parsedStr.toString());
    }

    // ��demo���ڲ��Էָ�������
    public static void main(String args[])
    {
        Demo5 demo = new Demo5();
    }
}