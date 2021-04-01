import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class BtsServer
{
    private static ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    char[][] serverMap;
    char[][] losingMap;
    public static void main(String[] args){
        BtsServer server=new BtsServer();
        try
        {
            serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("Succesfully started server at port: "+args[0]);
            server.start(args[1]);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void displayMap(){
        for(int i=0;i<10;i++)
        {
            System.out.println(serverMap[i]);
        }
    }
    public void winProcedure() throws IOException
    {
        for(int i=0;i<10;i++) {
            System.out.println(in.readLine());
        }
        winShare();
    }
    public void winShare(){
        for(int i=0;i<10;i++)
        {
            out.println(serverMap[i]);
        }
    }
    public String acceptCommand(String command) throws IOException
    {
        if(command.equals("win"))
        {
            winProcedure();
            return "You Won!";
        }
        String result;
        int field=Integer.parseInt(String.valueOf(command.charAt(1)));
        int row=command.charAt(0);
        if(row<'A' || row>'J' || field<0 || field>9)
        {
            return "Incorrect Command";
        }
        row=row-65;
        Character sign=serverMap[row][field];
        if(sign.equals('.') || sign.equals('~'))
        {
            losingMap[row][field]='~';
            serverMap[row][field]='~';
            result="missed";
            return result;
        }
        if(sign.equals('#'))
        {
            serverMap[row][field]='@';
            losingMap[row][field]='@';
            result="hit";
            return result;
        }
        if(sign.equals('@'))
        {
            result="hit(again...)";
            return result;
        }
        return "Incorrect Command";
    }
    public void sendCommand(String command) throws IOException
    {
        out.write(command);
        out.println();
    }
    public void lossProcedure(){
        for(int i=0;i<10;i++)
        {
            out.println(serverMap[i]);
        }
    }
    public boolean checkLoss(){
        for(int i=0;i<10;i++)
        {
            for(int x=0;x<10;x++)
            {
                if(serverMap[i][x]=='#'){
                    return false;
                }
            }
        }
        return true;
    }
    public void receiveMap() throws IOException
    {
        String copy;
        for(int i=0;i<10;i++)
        {
            System.out.println(in.readLine());
        }
    }
    public void start(String mapfile) throws FileNotFoundException
    {
        Scanner s=new Scanner(System.in);
        MapGenerator mapGenerator=new MapGenerator();
        this.losingMap=mapGenerator.generateEmptyMap();
        this.serverMap=mapGenerator.generateFromFile(mapfile);
        displayMap();
        while (true){
            try {
                System.out.println("Waiting for player . . .");
                this.clientSocket=serverSocket.accept();
                this.out=new PrintWriter(clientSocket.getOutputStream(),true);
                this.in=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("Connected to player");
                String response;
                while (true)
                {
                    String[] received=in.readLine().split(";");
                    if(received[0].equals("start"))
                    {
                        System.out.println("The Game Begins!");
                        System.out.println(received[0]+";"+received[1]);
                        response=acceptCommand(received[1]);
                        while(true)
                        {
                            if(checkLoss())
                            {
                             System.out.println("You Lost");
                             sendCommand("Congratulations;win");
                             lossProcedure();
                             receiveMap();
                             break;
                            }
                            sendCommand(response+";"+s.next());
                            received=in.readLine().split(";");
                            System.out.println(received[0]+";"+received[1]);
                            response=(acceptCommand(received[1]));
                        }
                        break;
                    }
                    this.losingMap=mapGenerator.generateEmptyMap();
                    this.serverMap=mapGenerator.generateFromFile(mapfile);
                }
                System.out.println("Disconnected from player");
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
