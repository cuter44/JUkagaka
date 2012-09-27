// This demo is to test Java reflect system, ...and getPath().

class Demo2
{
    public static void main(String[] args)
    {
        try
        {
            String str = Class.forName("Demo2").getClassLoader().getResource("").getPath();
            str = str.substring(1,str.length()-1);
            str.replaceAll("/","\\");
            System.out.println(str);
        }
        catch(Exception ex)
        {
            System.out.println("Error");
        }
        return;
    }
}
