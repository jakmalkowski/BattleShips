import java.io.*;
import java.net.Socket;
import java.util.Scanner;
public class BtsClient
{
    private PrintWriter out;
    private BufferedReader in;
    private  final String host;
    private  final int port;
    char[][] clientMap;
    char[][] victoryMap;
    BtsClient(String host,int port,String mapfile) throws IOException{
        this.host=host;
        this.port=port;
        MapGenerator mG=new MapGenerator();
        this.victoryMap=mG.generateEmptyMap();
        this.clientMap=mG.generateFromFile(mapfile);
        try{
            Socket s = new Socket(host,port);
            this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.out = new PrintWriter(s.getOutputStream(),true);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    private void displayMap(){
        for(int i=0;i<10;i++)
        {
            System.out.println(clientMap[i]);
        }
    }
    private void winProcedure() throws IOException
    {
        for(int i=0;i<10;i++) {
            System.out.println(in.readLine());
        }
        winShare();
    }
    private void winShare(){
        for(int i=0;i<10;i++)
        {
            out.println(victoryMap[i]);
        }
    }
    private String acceptCommand(String command) throws IOException
    {
        String result;
        if(command.equals("win"))
        {
            winProcedure();
            return "You Won!";
        }
        int field=Integer.parseInt(String.valueOf(command.charAt(1)));
        int row=command.charAt(0);
        if(row<'A' || row>'J' || field<0 || field>9)
        {
            return "Incorrect Command";
        }
        row=row-65;
        Character sign=clientMap[row][field];
        if(sign.equals('.') || sign.equals('~'))
        {
            clientMap[row][field]='~';
            clientMap[row][field]='~';
            result="missed";
            return result;
        }
        if(sign.equals('#'))
        {
            clientMap[row][field]='@';
            clientMap[row][field]='@';
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
    private void sendCommand(String cmd) throws IOException {
        out.println(cmd);
    }
    private boolean checkLoss(){
        for(int i=0;i<10;i++)
        {
            for(int x=0;x<10;x++)
            {
                if(clientMap[i][x]=='#'){
                    return false;
                }
            }
        }
        return true;
    }
    private void lossProcedure(){
        for(int i=0;i<10;i++)
        {
            out.println(victoryMap[i]);
        }
    }
    private void receiveMap() throws IOException
    {
        String copy;
        for(int i=0;i<10;i++)
        {
            System.out.println(in.readLine());
        }
        System.exit(0);
    }
    private void run() throws IOException
    {
        Scanner s=new Scanner(System.in);
        String result;
        String dump;
        displayMap();
        System.out.println("Input the start command to begin!");
        sendCommand(s.nextLine());
        while (true){
            String[] received=in.readLine().split(";");
            System.out.println(received[0]+";"+received[1]);
            result=acceptCommand(received[1]);
            if(checkLoss())
            {
                System.out.println("You lost!");
                sendCommand("Congratulations;win");
                lossProcedure();
                receiveMap();
               break;
            }
            dump=result+";"+s.next();
            sendCommand(dump);
        }
    }
    public static void main(String[] args) throws IOException
    {
        BtsClient client=new BtsClient("127.0.0.1",Integer.parseInt(args[0]),args[1]);
        client.run();
    }
}
