//package RSA;
//
//import java.io.*;
//import java.net.*;
//import RSA.Giaimaserver;
//
//public class Server {
//    public static void main(String[] args) {
//        int port = 8000;
//
//        try (ServerSocket serverSocket = new ServerSocket(port)) {
//            System.out.println("🔐 Server đang lắng nghe trên cổng " + port);
//
//            Socket clientSocket = serverSocket.accept();
//            System.out.println("✅ Đã kết nối với Client: " + clientSocket.getInetAddress());
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//
//            String duLieuJson;
//            while ((duLieuJson = in.readLine()) != null) {
//                try {
//                    String goc = Giaimaserver.giaiMaDuLieu(duLieuJson);
//                    System.out.println("📩 Nhận và giải mã thành công: " + goc);
//                    out.println("Đã nhận được: " + goc);
//                } catch (Exception e) {
//                    System.out.println("❌ Lỗi giải mã dữ liệu: " + e.getMessage());
//                    out.println("Lỗi giải mã.");
//                }
//            }
//
//            clientSocket.close();
//            System.out.println("🛑 Đã đóng kết nối.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
