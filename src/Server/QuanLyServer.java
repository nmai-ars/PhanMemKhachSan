//package Server;
//
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import Model.ModelDichVu;
//import Model.ModelKhachHang;
//import Model.ModelThanhToan;
//import Model.Modelchuoi;
//import Model.Phong;
//import Model.Phong.LoaiPhong;
//import Model.Phong.TrangThaiPhong;
//import View.ClientThread;
//import controller.PhongManagerQL;
//import controller.connectdatabase;
//import java.lang.reflect.Type;
//
//public class QuanLyServer {
//
//    private final Connection conn;
//    public Phong[] phong;
//    private static List<ClientThread> clients = new CopyOnWriteArrayList<>();
//    public HashMap<Integer, String> key_room = new HashMap<>();
//    public HashMap<String, Integer> dulieudp = new HashMap<>();
//    private DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//    public ModelDichVu[] danhsachDV = new ModelDichVu[17];
//    private final Gson gson = new Gson();
//
//    public QuanLyServer() {
//        this.conn = connectdatabase.getConnection();
//        initializeData();
//        System.out.println("Server đã khởi tạo dữ liệu thành công.");
//    }
//
//    public static void main(String[] args) {
//        QuanLyServer server = new QuanLyServer();
//        try (ServerSocket serverSocket = new ServerSocket(8000)) {
//            System.out.println("Server đang lắng nghe trên cổng " + 8000);
//            while (true) {
//                Socket clientSocket = serverSocket.accept();
//                System.out.println("Đã kết nối với Client: " + clientSocket.getInetAddress().getHostAddress());
//                ClientThread clientThread = new ClientThread(clientSocket, server);
//                clients.add(clientThread);
//                clientThread.start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Connection conn = connectdatabase.getConnection();
//        this.conn = connectdatabase.getConnection();
//        quanLyPhong = new ArrayList<PhongManagerQL>();
//        phong = new Phong[] { new Phong(101, "Phòng 101", TrangThaiPhong.TRONG, 400000, LoaiPhong.THUONG),
//                new Phong(102, "Phòng 102", TrangThaiPhong.TRONG, 400000, LoaiPhong.THUONG),
//                new Phong(103, "Phòng 103", TrangThaiPhong.TRONG, 400000, LoaiPhong.THUONG),
//                new Phong(104, "Phòng 104", TrangThaiPhong.TRONG, 400000, LoaiPhong.THUONG),
//                new Phong(201, "Phòng 201", TrangThaiPhong.TRONG, 600000, LoaiPhong.TRUNG),
//                new Phong(202, "Phòng 202", TrangThaiPhong.TRONG, 600000, LoaiPhong.TRUNG),
//                new Phong(203, "Phòng 203", TrangThaiPhong.TRONG, 600000, LoaiPhong.TRUNG),
//                new Phong(204, "Phòng 204", TrangThaiPhong.TRONG, 600000, LoaiPhong.TRUNG),
//                new Phong(301, "Phòng 301", TrangThaiPhong.TRONG, 800000, LoaiPhong.VIP),
//                new Phong(302, "Phòng 302", TrangThaiPhong.TRONG, 800000, LoaiPhong.VIP),
//                new Phong(303, "Phòng 303", TrangThaiPhong.TRONG, 800000, LoaiPhong.VIP),
//                new Phong(304, "Phòng 304", TrangThaiPhong.TRONG, 800000, LoaiPhong.VIP), };
//        danhsachDV[0] = new ModelDichVu(0, "Nước lọc", 15000);
//        danhsachDV[1] = new ModelDichVu(1, "Snack khoai tây", 20000);
//        danhsachDV[2] = new ModelDichVu(2, "Coca/Pepsi", 20000);
//        danhsachDV[3] = new ModelDichVu(3, "Rượu Vodka SMIRNOFF 700ML", 400000);
//        danhsachDV[4] = new ModelDichVu(4, "Bánh KitKat", 25000);
//        danhsachDV[5] = new ModelDichVu(5, "Nước Smartwater 500ML", 60000);
//        danhsachDV[6] = new ModelDichVu(6, "Bia Heineken 250ML", 20000);
//        danhsachDV[7] = new ModelDichVu(7, "Vang Ý Mango Tropical 750ML", 500000);
//        danhsachDV[8] = new ModelDichVu(8, "Chivas Regal 18 Gold Signature 700ML", 1400000);
//        danhsachDV[9] = new ModelDichVu(9, "Cho thuê xe tự lái", 700000);
//        danhsachDV[10] = new ModelDichVu(10, "Dùng điểm tâm", 500000);
//        danhsachDV[11] = new ModelDichVu(11, "Đưa đón sân bay", 200000);
//        danhsachDV[12] = new ModelDichVu(12, "Trông trẻ", 300000);
//        danhsachDV[13] = new ModelDichVu(13, "Tuần trăng mật", 3000000);
//        danhsachDV[14] = new ModelDichVu(14, "Giặt ủi", 100000);
//        danhsachDV[15] = new ModelDichVu(15, "Spa", 1500000);
//        danhsachDV[16] = new ModelDichVu(16, "Fitness", 200000);
//
//        key_room.put(101, "");
//        key_room.put(102, "");
//        key_room.put(103, "");
//        key_room.put(104, "");
//        key_room.put(201, "");
//        key_room.put(202, "");
//        key_room.put(203, "");
//        key_room.put(204, "");
//        key_room.put(301, "");
//        key_room.put(302, "");
//        key_room.put(303, "");
//        key_room.put(304, "");
//    }
//
//    private void initializeData() {
//        phong = new Phong[] {
//                new Phong(101, "Phòng 101", TrangThaiPhong.TRONG, 400000, LoaiPhong.THUONG),
//                new Phong(102, "Phòng 102", TrangThaiPhong.TRONG, 400000, LoaiPhong.THUONG),
//                new Phong(103, "Phòng 103", TrangThaiPhong.TRONG, 400000, LoaiPhong.THUONG),
//                new Phong(104, "Phòng 104", TrangThaiPhong.TRONG, 400000, LoaiPhong.THUONG),
//                new Phong(201, "Phòng 201", TrangThaiPhong.TRONG, 600000, LoaiPhong.VIP),
//                new Phong(202, "Phòng 202", TrangThaiPhong.TRONG, 600000, LoaiPhong.VIP),
//                new Phong(203, "Phòng 203", TrangThaiPhong.TRONG, 600000, LoaiPhong.VIP),
//                new Phong(204, "Phòng 204", TrangThaiPhong.TRONG, 600000, LoaiPhong.VIP),
//                new Phong(301, "Phòng 301", TrangThaiPhong.TRONG, 800000, LoaiPhong.VIP),
//                new Phong(302, "Phòng 302", TrangThaiPhong.TRONG, 800000, LoaiPhong.VIP),
//                new Phong(303, "Phòng 303", TrangThaiPhong.TRONG, 800000, LoaiPhong.VIP),
//                new Phong(304, "Phòng 304", TrangThaiPhong.TRONG, 800000, LoaiPhong.VIP),
//        };
//    }
//    }