/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import RSA.Mahoaserver;

import java.io.*;
import java.net.Socket;

/**
 *
 * @author ASUS
 */
public class ServerThread implements Runnable {

    private Socket socketOfServer;
    private int clientNumber;
    private BufferedReader is;
    private BufferedWriter os;
    private boolean isClosed;
    private static Mahoaserver mahoaserver;
    private String username;

    public BufferedReader getIs() {
        return is;
    }

    public BufferedWriter getOs() {
        return os;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public ServerThread(Socket socketOfServer, int clientNumber) {
        this.socketOfServer = socketOfServer;
        this.clientNumber = clientNumber;
        System.out.println("Server thread number " + clientNumber + " Started");
        isClosed = false;
    }

    @Override
    public void run() {
        try {
            // Mở luồng vào ra trên Socket tại Server.
            is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
            System.out.println("Khời động luông mới thành công, ID là: " + clientNumber);
            write("get-id" + "," + this.clientNumber);
            Server.serverThreadBus.sendOnlineList();
            Server.serverThreadBus.mutilCastSend("global-message"+","+"---Client "+this.clientNumber+" đã đăng nhập---");
            String message;
            while (!isClosed) {
                message = is.readLine();
                if (message == null) {
                    break;
                }
//                String decryptedMessage = mahoaserver.giaiMaDuLieu(message);
//
//                String[] messageSplit = message.split(",");
//                if(messageSplit[0].equals("send-to-global")){
//                    Server.serverThreadBus.boardCast(this.getClientNumber(),"global-message"+","+"Client "+messageSplit[2]+": "+messageSplit[1]);
//                }
//                if(messageSplit[0].equals("send-to-person")){
//                    Server.serverThreadBus.sendMessageToPersion(Integer.parseInt(messageSplit[3]),"Client "+ messageSplit[2]+" (tới bạn): "+messageSplit[1]);
//                }
//            }
//        } catch (IOException e) {
//            isClosed = true;
//            Server.serverThreadBus.remove(clientNumber);
//            System.out.println(this.clientNumber+" đã thoát");
//            Server.serverThreadBus.sendOnlineList();
//            Server.serverThreadBus.mutilCastSend("global-message"+","+"---Client "+this.clientNumber+" đã thoát---");
//        }
//        try{
//            while (true){
//                String message = is.readLine();
//                if (message == null) {
//                    break;
//                }
                try {
                    String decryptedMessage = mahoaserver.giaiMaDuLieu(message);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                String[] messageSplit = message.split(",");
                switch (messageSplit[0]) {
                    case "login":
                        handleLogin(messageSplit);
                        break;
                    case "send-to-person":
                        handLePrivateMessage(messageSplit);
                        break;

                        case "send-to-global":
                            handleGlobalMessage(messageSplit);
                            break;   
                            
                    default:
                        System.out.println(".."+messageSplit[0]);



                }

            }
        }catch(IOException e ){
            e.printStackTrace();
        }finally{
            isClosed = true;
            server.ServerThreadBus.remove(clientNumber);
            System.out.println("Client : " +clientNumber +"..");
            Server.serverThreadBus.sendOnlineList();
            Server.serverThreadBus.mutilCastSend("global-message"+","+"---Client "+this.clientNumber+" đã thoát---");
        }
    }

    private void handleGlobalMessage(String[] messageSplit) {
        if (messageSplit.length < 3) {
            return;
        }
        String content = messageSplit[1];
        String senderId = messageSplit[2];
        Server.serverThreadBus.broadcastAllExcept("global-message," + senderId + "," + content, Integer.parseInt(senderId));
    }

    private void handLePrivateMessage(String[] messageSplit) {
        if (messageSplit.length < 4) {
            return;
        }
        String content = messageSplit[1];
        String senderId = messageSplit[2];
        String receiverId = messageSplit[3];
        Server.serverThreadBus.sendMessageToClient("private-message," + senderId + "," + content, Integer.parseInt(receiverId));
    }

    private void handleLogin(String[] messageSplit) {
        if (messageSplit.length < 2) {
            return;
        }
        try {
            this.username = messageSplit[1];
            write("login-success," + clientNumber);
            Server.serverThreadBus.updateOnlineList();
        }catch (IOException e ){
            e.printStackTrace();
        }
        Server.serverThreadBus.sendOnlineList();
    }

    public void write(String message) throws IOException{
        os.write(message);
        os.newLine();
        os.flush();
    }

}
