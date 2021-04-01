import java.io.IOException;

public class Battleships
{
    public static void main(String[] args) throws IOException
    {
        String[] newargs={args[1],args[2]};
        if(args[0].equals("server")){

            BtsServer.main(newargs);
        }
        if(args[0].equals("client")){
            BtsClient.main(newargs);
        }
    }
}
