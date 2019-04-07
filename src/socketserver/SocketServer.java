package socketserver;

import java.awt.event.KeyEvent;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SocketServer {
    public static int i=0;
    public static ArrayList<clientHandler> clients = new ArrayList<>(0);
    public static String FilePath = " ";

    public static void main(String[] args) throws IOException
    {
        ServerSocket server = new ServerSocket(1337);
        System.out.println("Server Booted Up.");
        while(true)
        {
            Socket clientSocket = null;
            //try
            //{
            clientSocket = server.accept();
            DataInputStream userData = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream serverData = new DataOutputStream(clientSocket.getOutputStream());

            clients.add(i, new clientHandler(clientSocket, userData, serverData,i));
            clients.get(i).start();
            i++;
        }
           /* catch(Exception e)
            {
                clientSocket.close();
                e.printStackTrace();
            }*/
    }

}



class clientHandler extends Thread
{
    final DataInputStream userData;
    final DataOutputStream serverData;
    final Socket s;
    final int num;


    public clientHandler(Socket s, DataInputStream dis, DataOutputStream dos, int num)
    {
        this.num = num;
        System.out.println("New Connection");
        this.s = s;
        this.userData = dis;
        this.serverData = dos;
    }

    @Override
    public void run()
    {
        char r;
        int code;
        int flag;
        String userText;
        int leaveLOOP = 1;
        while(true){
            try {
                flag = userData.readInt();
                switch(flag)
                {
                    case 0:
                        r = userData.readChar();
                        code = userData.readInt();
                        System.out.print(r);
                        System.out.println(code);
                        for(int j=0;j<SocketServer.i;j++)
                        {
                            if (this.num == j)
                                continue;
                            SocketServer.clients.get(j).serverData.writeInt(flag);
                            SocketServer.clients.get(j).serverData.writeChar(r);
                            SocketServer.clients.get(j).serverData.writeInt(code);

                        }
                        break;
                    case 1:
                        userText = userData.readUTF();
                        System.out.println(userText);
                        for(int j=0;j<SocketServer.i;j++)
                        {
                            if (this.num == j)
                                continue;
                            SocketServer.clients.get(j).serverData.writeInt(flag);
                            SocketServer.clients.get(j).serverData.writeUTF(userText);

                        }
                        break;
                    case 27:
                        this.s.close();
                        System.out.println("Client: " + this.num + ", Closed Connection");
                        leaveLOOP = 0;
                        break;

                }
                if(leaveLOOP == 0)
                    break;

            } catch (IOException ex) {
                Logger.getLogger(clientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try
        {
            // closing resources
            this.userData.close();
            this.serverData.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

}