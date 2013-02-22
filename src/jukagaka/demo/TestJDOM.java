package jukagaka.demo;

/* jukagaka */
import jukagaka.UkaDaemon;
/* jdom2 */
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.JDOMException;
/* Container */
import java.util.Iterator;

import java.io.IOException;

/**
 * �������� JDOM ��
 *
 * JDOM �� api @link http://www.jdom.org/docs/apidocs/
 * XPath ��˵�� @link http://www.w3school.com.cn/xpath/index.asp
 * DTD ��˵�� @link http://www.w3school.com.cn/dtd/dtd_intro.asp
 * ����Java��XML����ϸ˵�� @link http://www.ibm.com/developerworks/cn/xml/theme/x-java.html
 */
public class TestJDOM
{
    public static void dfs(Element el)
    {
        System.out.println(el);

        Iterator<Element> child = el.getChildren().iterator();
        while (child.hasNext())
            dfs(child.next());
        return;
    }

    public static void dump()
    {
        SAXBuilder builder = new SAXBuilder();
        Document document =null;
        try
        {
            document = builder.build(UkaDaemon.getProgramDir() + "/res/shell/StubShell/StubXML.xml");
        }
        catch (JDOMException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        Element root = document.getRootElement();
        dfs(root);

        return;
    }

    public static void main(String[] args)
    {
        dump();

        return;
    }
}
