package jukagaka.demo;

/* Ukagaka */
import jukagaka.UkaDaemon;
/* Dom4j */
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.dom4j.DocumentException;

public class TestDom4j
{
  // Launch | ÆôÍ£½Ó¿Ú
    public static boolean onLoad()
    {
        return(true);
    }

    public static boolean onStart()
    {
        return(true);
    }

    public static boolean onExit()
    {
        return(true);
    }

    public static boolean onInstall()
    {
        return(true);
    }

    public static boolean onUnistall()
    {
        return(true);
    }

  // ...
    public static Document parse(String strFileName)
    {
        SAXReader reader = new SAXReader();
        Document document = null;

        try
        {
            document = reader.read(strFileName);
        }
        catch (DocumentException ex)
        {
            ex.printStackTrace();
        }
        return document;
    }


  // Debug | µ÷ÊÔ
    public static void main(String[] args)
    {
        System.out.println(parse(UkaDaemon.getProgramDir() + "/../res/shell/StubShell/StubXML.xml"));
        return;
    }
}
