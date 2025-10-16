//package RSA;
//
//import java.io.*;
//import java.net.*;
//import RSA.Mahoaclient;
//
//public class Client {
//    public static void main(String[] args) {
//        String serverAddress = "localhost";
//        int serverPort = 8000;
//
//        try (
//                Socket socket = new Socket(serverAddress, serverPort);
//                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
//                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
//        ) {
//            System.out.println("✅ Đã kết nối đến server!");
//
//            String message;
//            while (true) {
//                System.out.print("Nhập tin nhắn (exit để thoát): ");
//                message = userInput.readLine();
//                if ("exit".equalsIgnoreCase(message)) break;
//
//                String duLieuDaMaHoa = Mahoaclient.maHoaDuLieu(message);
//                out.println(duLieuDaMaHoa);
//
//                String phanHoi = in.readLine();
//                System.out.println("📬 Server phản hồi: " + phanHoi);
//            }
//
//            System.out.println("🔒 Đã ngắt kết nối.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
