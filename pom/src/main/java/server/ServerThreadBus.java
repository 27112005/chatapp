/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
public class ServerThreadBus {
    private static final Logger LOGGER = Logger.getLogger(ServerThreadBus.class.getName());
    private final List<ServerThread> listServerThreads;

    public List<ServerThread> getListServerThreads() {
        return listServerThreads;
    }

    public ServerThreadBus() {
        listServerThreads = new ArrayList<>();

    }

    


    public void add(ServerThread serverThread) {
        listServerThreads.add(serverThread);
    }

    public void mutilCastSend(String message) { //like sockets.emit in socket.io
        for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
            try {
                serverThread.write(message);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Error sending multicast message", ex);
                ex.printStackTrace();
            }
        }
    }

    public void boardCast(int id, String message) {
        for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
            if (serverThread.getClientNumber() == id) {
                continue;
            } else {
                try {
                    serverThread.write(message);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public int getLength() {
        return listServerThreads.size();
    }

    public void sendOnlineList() {
        String res = "";
        List<ServerThread> threadbus = Server.serverThreadBus.getListServerThreads();
        for (ServerThread serverThread : threadbus) {
            res += serverThread.getClientNumber() + "-";
        }
        Server.serverThreadBus.mutilCastSend("update-online-list" + "," + res);
    }

    public void sendMessageToPersion(int id, String message) {
        for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
            if (serverThread.getClientNumber() == id) {
                try {
                    serverThread.write("global-message" + "," + message);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    //    private void processClienMessage(String message , Socket clientSocket){
//        String[] parts = message.split(",");
//        String command = parts[0];
//
//        switch(command){
//            case "update-online-list":
//                String username = parts[1];
//                handleLogin(username, clientSocket);
//                break;
//
//        }
//    }
//    private void handleLogin(String username, Socket clientSocket){
//        clientSocketMap.put(clientSocket, username);
//
//
//
//    }
    public static void remove(int id) {
        for (int i = 0; i < Server.serverThreadBus.getLength(); i++) {
            if (Server.serverThreadBus.getListServerThreads().get(i).getClientNumber() == id) {
                Server.serverThreadBus.listServerThreads.remove(i);
            }
        }
    }

    public void sendMessageToClient(String message, int clientId) {
        for (ServerThread serverThread : listServerThreads) {
            if (serverThread.getClientNumber() == clientId) {
                try {
                    serverThread.write(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }

    }

    public void updateOnlineList() {
        String onlineList = getOnlineList();
        broadcastAll("update-online-list," + onlineList);
    }

    private void broadcastAll(String message) {
        for (ServerThread serverThread : listServerThreads) {
            try {
                serverThread.write(message);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error broadcasting message to all", e);
            }
        }
    }

    private String getOnlineList() {
        StringBuilder list = new StringBuilder();
        for (ServerThread serverThread : listServerThreads) {
            list.append(serverThread.getClientNumber()).append("-");
        }
        return list.toString();
    }


    public void broadcastAllExcept(String message, int exceptId) {
        for (ServerThread serverThread : listServerThreads) {
            if (serverThread.getClientNumber() != exceptId) {
                try {
                    serverThread.write(message
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}


