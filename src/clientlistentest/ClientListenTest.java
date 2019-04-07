package clientlistentest;

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


public class ClientListenTest extends JFrame implements KeyListener , ActionListener {
    JTextArea typingArea;
    static Socket ClientSocket;
    static DataOutputStream userData;
    static DataInputStream serverData;
    static final String newline = System.getProperty("line.separator");
    int leftarrowcount = 0;
    String text = "";
    StringBuilder shown = new StringBuilder(900*500);

    public static void main(String[] args) throws IOException {



        userGUI();
        createClientConnection();
        Thread t = new Thread(new serverHandler(ClientSocket,serverData));
        t.start();

    }

    private static void userGUI(){
        ClientListenTest frame = new ClientListenTest("ClientListenTest");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addComponentsToPane();
        frame.pack();
        frame.setVisible(true);
    }

    private static void createClientConnection() throws IOException
    {
        ClientSocket = new Socket("127.0.0.1",1337);
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
       /* System.out.println(e.getKeyChar());
        //char c = e.getKeyChar();
        int code = e.getKeyCode();
        try {
            //userData.writeChar(c);
            userData.writeInt(code);
        } catch (IOException ex) {
            Logger.getLogger(ClientListenTest.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println(e.getKeyChar());
        char c = e.getKeyChar();
        int code = e.getKeyCode();
        int id = e.getID();
        if(code == 37 && leftarrowcount < shown.length())
            leftarrowcount++;
        else if(code == 39 && leftarrowcount > 0)
            leftarrowcount--;
        else if(code == 27)
            try {
                userData.writeInt(code);
                System.out.println("Connection Closed!");
            } catch (IOException ex) {
                Logger.getLogger(ClientListenTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        else if(((code >= 32 && code <= 126) || code == 8 || code == 10) && code != 39 && code != 37)
        {
            try {
                if(leftarrowcount > 0)
                {
                    int one = 1;
                    userData.writeInt(one);
                    if(code == 8)
                    {
                        shown.deleteCharAt(shown.length() - leftarrowcount - 1);
                        System.out.println(shown.toString());
                        text = shown.toString();
                        userData.writeUTF(text);
                    }
                    else
                    {
                        shown.insert(shown.length() - leftarrowcount, c);
                        System.out.println(shown.toString());
                        text = shown.toString();
                        userData.writeUTF(text);
                    }


                }
                else if(code == 8)
                {
                    int zero = 0;
                    userData.writeInt(zero);
                    userData.writeChar(c);
                    System.out.println(c);
                    userData.writeInt(code);
                    shown.deleteCharAt(shown.length() - 1);
                }
                else
                {
                    int zero = 0;
                    userData.writeInt(zero);
                    userData.writeChar(c);
                    shown.append(c);
                    System.out.println(c);
                    userData.writeInt(code);
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientListenTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        typingArea.setText("");

        //Return the focus to the typing area.
        typingArea.requestFocusInWindow();
    }

    public ClientListenTest(String name) {
        super(name);
    }

}







class serverHandler extends Thread {

    final DataInputStream dis;
    final Socket s;

    public serverHandler(Socket s, DataInputStream dis)
    {
        System.out.println("New Connection");
        this.s = s;
        this.dis = dis;
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
            while (true)
            {   rec = dis.readChar();
                code = dis.readInt();
                //System.out.println(rec);
                //System.out.println(code);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }}