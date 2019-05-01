package listenkeytest;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTextArea;


public class ListenKeyTest extends JFrame implements KeyListener , ActionListener {
    public static JTextArea typingArea;

    static Socket ClientSocket;
    public  static  int code;
    static DataOutputStream userData;
    static DataInputStream serverData;
    public static String shown = "";
    public static Thread t = null;
    String textt = "";
    public static void main(String[] args) throws IOException {



        userGUI();
        createClientConnection();
        t = new Thread(new serverHandler(ClientSocket,serverData));
        t.start();

    }

    private static void userGUI(){
        ListenKeyTest frame = new ListenKeyTest("ListenKeyTest");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addComponentsToPane();
        frame.pack();
        frame.setVisible(true);
    }

    private static void createClientConnection() throws IOException
    {
        ClientSocket = new Socket("172.20.10.3",1337);
        userData = new DataOutputStream(ClientSocket.getOutputStream());
        serverData = new DataInputStream(ClientSocket.getInputStream());
    }

    private void addComponentsToPane(){
        typingArea = new JTextArea();
        typingArea.setPreferredSize(new Dimension(900,500));
        typingArea.addKeyListener(this);

        typingArea.setFocusTraversalKeysEnabled(false);

        getContentPane().add(typingArea, BorderLayout.PAGE_START);

    }

    @Override
    public void keyTyped(KeyEvent e) {

      /*  try {
            userData.writeChar(c);
            userData.writeInt(code);
        } catch (IOException ex) {

        }
  */  }

    @Override
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        code = e.getKeyCode();
        System.out.println(code);
        if(code == 27)
            try {
                userData.writeInt(code);
                System.out.println("Connection Closed!");
                this.ClientSocket.close();
                this.userData.close();
                this.serverData.close();
                System.exit(0);
            } catch (IOException ex) {

            }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if((((code >= 32 && code <= 126) || code == 8 || code == 10) && code != 39 && code != 37&& code != 38 && code != 40)){
            int one = 1;
            try {
                userData.writeInt(one);
                textt = typingArea.getText();
                Thread.sleep(50);
                userData.writeUTF(textt);
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }


        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        typingArea.setText("");

        //Return the focus to the typing area.
        typingArea.requestFocusInWindow();
    }

    public ListenKeyTest(String name) {
        super(name);
    }

}




class serverHandler extends Thread {

    final DataInputStream dis;
    final Socket s;
    //JTextArea typingArea;

    public serverHandler(Socket s, DataInputStream dis)
    {
        System.out.println("New Connection");
        this.s = s;
        this.dis = dis;
        //this.typingArea = ta;
    }

    @Override
    public  void run(){
        try
        {   //Scanner scn = new Scanner(System.in);
            //InetAddress ip = InetAddress.getByName("localhost");
            //Socket s = new Socket(ip, 1337);
            //DataInputStream dis = new DataInputStream(s.getInputStream());
            //DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            char rec;
            int code;
            String t;
            while (true)
            {   t = dis.readUTF();
                ListenKeyTest.typingArea.selectAll();
                Thread.sleep(5);
                ListenKeyTest.typingArea.replaceSelection(t);
                //rec = dis.readChar();
                //code = dis.readInt();
                //System.out.println(rec);
                //System.out.println(code);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }}
       /* try
        {
            //InetAddress ip = InetAddress.getByName("localhost");
            //Socket s = new Socket(ip, 1337);
            //DataInputStream dis = new DataInputStream(s.getInputStream());
            //DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            char rec;
            int code;
            int flag;
            int size;
            StringBuilder str = new StringBuilder(900*500);
            String send = "";
            while (true)
            {
                flag = dis.readInt();
                switch(flag)
                {
                    case 0:
                        rec = dis.readChar();
                        code = dis.readInt();
                        //System.out.println(rec);
                        System.out.println(code);
                        if(code == 8){
                            size = str.length();
                            if (size > 0) {
                                str.deleteCharAt(size - 1);
                                send = "";
                                send += str;
                                ListenKeyTest.typingArea.selectAll();
                                Thread.sleep(5);
                                ListenKeyTest.typingArea.replaceSelection(send);
                            }
                        }
                        else
                        {
                            str.append(rec);
                            send = "";
                            send += rec;
                            ListenKeyTest.typingArea.append(send);
                        }
                        break;
                    case 1:
                        ListenKeyTest.typingArea.selectAll();
                        Thread.sleep(5);
                        ListenKeyTest.typingArea.replaceSelection(dis.readUTF());
                        break;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}/*
  /*  try
            {   //Scanner scn = new Scanner(System.in);
            //InetAddress ip = InetAddress.getByName("localhost");
            //Socket s = new Socket(ip, 1337);
            //DataInputStream dis = new DataInputStream(s.getInputStream());
            //DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            char rec;
            int code;
            String t;
            while (true)
            {   t = dis.readUTF();
            ClientListenTest.typingArea.selectAll();
            Thread.sleep(5);
            ClientListenTest.typingArea.replaceSelection(t);
            //rec = dis.readChar();
            //code = dis.readInt();
            //System.out.println(rec);
            //System.out.println(code);
            }
            } catch(Exception e){
            e.printStackTrace();
            }
            }}*/