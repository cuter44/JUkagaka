package jukagaka.demo;

import jukagaka.UkaDaemon;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestProp
{
    private static final String propFileName = "./test.prop";
    private static File propFile = new File(propFileName);

    public static void main(String[] args)
    {
        Properties prop = new Properties();
        FileInputStream ifs = null;
        FileOutputStream ofs = null;

        try
        {
            if (!propFile.exists())
                propFile.createNewFile();
                ifs = new FileInputStream(propFile);
                prop.load(ifs);

            ifs.close();

            prop.setProperty("key1","value1");
            prop.setProperty("key2","value2");
            prop.list(System.out);
            System.out.println("-----");
            System.out.println("prop.getProperty(\"key1\")=" + prop.getProperty("key1"));

            ofs = new FileOutputStream(propFile);
            prop.store(ofs,null);
            ofs.close();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return;
    }
}
