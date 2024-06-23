/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import RSA.Mahoaserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author ASUS
 */
public class Server {
    public static volatile ServerThreadBus serverThreadBus;
    public static Socket socketOfServer;
    private static Mahoaserver mahoaserver;


    public static void main(String[] args) {
        ServerSocket listener = null;
        serverThreadBus = new ServerThreadBus();
        System.out.println("Server is waiting to accept user...");
        int clientNumber = 0;

        // Mở một ServerSocket tại cổng 7777.
        // Chú ý bạn không thể chọn cổng nhỏ hơn 1023 nếu không là người dùng
        // đặc quyền (privileged users (root)).
        try {
            listener = new ServerSocket(9999);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10, // corePoolSize
                100, // maximumPoolSize
                10, // thread timeout
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(8) // queueCapacity
        );
        try {
            while (true) {
                // Chấp nhận một yêu cầu kết nối từ phía Client.
                // Đồng thời nhận được một đối tượng Socket tại server.
                socketOfServer = listener.accept();
                ServerThread serverThread = new ServerThread(socketOfServer, clientNumber++);
                serverThreadBus.add(serverThread);
                System.out.println("Số thread đang chạy là: " + serverThreadBus.getLength());
                executor.execute(serverThread);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                listener.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
//// Trong phương thức xử lý tin nhắn của Server
//        if (messageSplit[0].equals("login")) {
//            String username = messageSplit[1];
//            // Thêm logic xác thực nếu cần
//            // Ví dụ: kiểm tra username có tồn tại trong cơ sở dữ liệu không
//
//            // Nếu xác thực thành công
//            this.username = username;
//            write("login-success," + id);
//            // Cập nhật danh sách người dùng online
//            updateOnlineList();
//        } else if (messageSplit[0].equals("send-to-global")) {
//            // Xử lý tin nhắn toàn cầu
//        } else if (messageSplit[0].equals("send-to-person")) {
//            // Xử lý tin nhắn cá nhân
//        }


    }
}
