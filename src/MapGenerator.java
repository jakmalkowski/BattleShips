import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
public class MapGenerator
{
    public char[][] generateEmptyMap()
    {
        char[][] emptymap = new char[10][10];
        for (int i = 0; i < 10; i++)
        {
            Arrays.fill(emptymap[i], '?');
        }
        return emptymap;
    }
    public char[][] generateFromFile(String input_file) throws FileNotFoundException
    {
        try
        {
            String str;
            int x=0;
            FileReader fr=new FileReader(input_file);
            BufferedReader br=new BufferedReader(fr);
            char[][] NewMap = new char[10][10];
            while ((str = br.readLine())!=null)
            {
                NewMap[x]=str.toCharArray();
                x++;
            }
            return NewMap;
        }
        catch (Exception e)
        {
            System.err.println("Error loading file,cannot generate map");
            return null;
        }
    }
}
