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
//    }
//
//    public synchronized void broadcastRoomStatus() {
//        String jsonPhong = gson.toJson(phong);
//        String message = "UPDATE_ROOM_STATUS#" + jsonPhong;
//        for (ClientThread client : clients) {
//            client.sendMessage(message);
//        }
//        System.out.println("Broadcasted room status to " + clients.size() + " clients.");
//    }
//
//    public void removeClient(ClientThread client) {
//        clients.remove(client);
//        System.out.println("Client đã ngắt kết nối. Số client hiện tại: " + clients.size());
//    }
//
//    // --- TẤT CẢ LOGIC NGHIỆP VỤ NẰM Ở ĐÂY ---
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
//
//        danhsachDV[0] = new ModelDichVu(0, "Nước lọc", 15000);
//        danhsachDV[1] = new ModelDichVu(1, "Sting", 20000);
//        danhsachDV[2] = new ModelDichVu(2, "Bò húc", 25000);
//        danhsachDV[3] = new ModelDichVu(3, "Trà xanh", 20000);
//        danhsachDV[4] = new ModelDichVu(4, "7Up", 20000);
//        danhsachDV[5] = new ModelDichVu(5, "Pepsi", 20000);
//        danhsachDV[6] = new ModelDichVu(6, "CocaCola", 20000);
//        danhsachDV[7] = new ModelDichVu(7, "Snack tôm", 20000);
//        danhsachDV[8] = new ModelDichVu(8, "Snack cua", 20000);
//        danhsachDV[9] = new ModelDichVu(9, "Snack bò", 20000);
//        danhsachDV[10] = new ModelDichVu(10, "Snack cay", 20000);
//        danhsachDV[11] = new ModelDichVu(11, "Mỳ ly", 25000);
//        danhsachDV[12] = new ModelDichVu(12, "Giặt ủi", 100000);
//        danhsachDV[13] = new ModelDichVu(13, "Dọn phòng", 150000);
//        danhsachDV[14] = new ModelDichVu(14, "Xe máy", 200000);
//        danhsachDV[15] = new ModelDichVu(15, "Massage", 300000);
//        danhsachDV[16] = new ModelDichVu(16, "Fitness", 200000);
//
//        for (Phong p : phong) {
//            key_room.put(p.getId(), "");
//        }
//    }
//
//    public boolean CheckIn(String username, String pass) throws SQLException {
//        boolean result = false;
//        String sql = "SELECT PASS FROM customer WHERE USERNAME = ?";
//        try (Connection connection = connectdatabase.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setString(1, username);
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                if (resultSet.next()) {
//                    String passwordFromDB = resultSet.getString("PASS");
//                    String salt = "asdfghjkl";
//                    String str = pass + salt;
//                    String encodedPass = Base64.getEncoder().encodeToString(str.getBytes());
//                    if (encodedPass.equals(passwordFromDB)) {
//                        result = true;
//                    }
//                }
//            }
//        }
//        return result;
//    }
//
//    public boolean DangKy(String hoten, String cccd, String sdt, String email, String pass, String username) {
//        String sql = "INSERT INTO customer (MAKH, HOTEN, CCCD, SDT, EMAIL, PASS, USERNAME) VALUES (?, ?, ?, ?, ?, ?, ?)";
//        try (Connection connection = connectdatabase.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            String salt = "asdfghjkl";
//            String str = pass + salt;
//            String encodedPass = Base64.getEncoder().encodeToString(str.getBytes());
//            String lastThreeDigits = sdt.substring(sdt.length() - 3);
//            String makh = TaoMaKH(lastThreeDigits);
//            preparedStatement.setString(1, makh);
//            preparedStatement.setString(2, hoten);
//            preparedStatement.setString(3, cccd);
//            preparedStatement.setString(4, sdt);
//            preparedStatement.setString(5, email);
//            preparedStatement.setString(6, encodedPass);
//            preparedStatement.setString(7, username);
//            int rowsAffected = preparedStatement.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public String truyenthongtin(String username) {
//        String sql = "SELECT MAKH, HOTEN, CCCD, SDT, EMAIL FROM customer WHERE USERNAME = ?";
//        try (Connection connection = connectdatabase.getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, username);
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                ModelKhachHang khachHang = new ModelKhachHang(
//                        resultSet.getString("MAKH"),
//                        resultSet.getString("HOTEN"),
//                        resultSet.getString("CCCD"),
//                        resultSet.getString("SDT"),
//                        resultSet.getString("EMAIL")
//                );
//                return gson.toJson(khachHang);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public synchronized int booking(String chuoithongtin) {
//        Modelchuoi chuoi = gson.fromJson(chuoithongtin, Modelchuoi.class);
//        int madp = taomaDatphong();
//        key_room.put(chuoi.getMaphong(), String.valueOf(madp));
//        dulieudp.put(chuoi.getMaKhachHang(), madp);
//        for (Phong p : phong) {
//            if (p.getId() == chuoi.getMaphong()) {
//                p.setTrangThai(TrangThaiPhong.DA_DAT);
//                break;
//            }
//        }
//        broadcastRoomStatus();
//        return madp;
//    }
//
//    public String TaoMaKH(String soduoi) {
//        LocalDateTime now = LocalDateTime.now();
//        String dateTimeString = now.format(DATE_TIME_FORMATTER);
//        return soduoi + dateTimeString;
//    }
//
//    public int taomaDatphong() {
//        Random random = new Random();
//        return 10000 + random.nextInt(90000);
//    }
//
//    public synchronized void xacnhan(int maphong, int madp) {
//        for (Phong p : phong) {
//            if (p.getId() == maphong) {
//                p.setTrangThai(TrangThaiPhong.DANG_HOAT_DONG);
//                break;
//            }
//        }
//        broadcastRoomStatus();
//    }
//
//    public synchronized void thanhtoan(int maphong, int madp, int tongtien, String dichvu, String lydo) {
//        String sql = "INSERT INTO thanhtoan(MADP, MAPHONG, TONGTIEN, DICHVU, LYDO) VALUES (?, ?, ?, ?, ?)";
//        try (Connection connection = connectdatabase.getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql)){
//            statement.setInt(1, madp);
//            statement.setInt(2, maphong);
//            statement.setInt(3, tongtien);
//            statement.setString(4, dichvu);
//            statement.setString(5, lydo);
//            statement.executeUpdate();
//            for (Phong p : phong) {
//                if (p.getId() == maphong) {
//                    p.setTrangThai(TrangThaiPhong.TRONG);
//                    key_room.put(maphong, ""); // Reset key
//                    break;
//                }
//            }
//            broadcastRoomStatus();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public synchronized void huyPhong(int maphong) {
//        for (Phong p : phong) {
//            if (p.getId() == maphong) {
//                p.setTrangThai(TrangThaiPhong.TRONG);
//                key_room.put(maphong, ""); // Reset key
//                break;
//            }
//        }
//        broadcastRoomStatus();
//    }
//
//    public String thongkedoanhthu(String ngaybd, String ngaykt) {
//        ArrayList<ModelThanhToan> list = new ArrayList<>();
//        String sql = "SELECT MADP, MAPHONG, TONGTIEN, DICHVU, LYDO, THOIGIAN FROM thanhtoan WHERE THOIGIAN BETWEEN ? AND ?";
//        try (Connection connection = connectdatabase.getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql)){
//            statement.setString(1, ngaybd + " 00:00:00");
//            statement.setString(2, ngaykt + " 23:59:59");
//            ResultSet rs = statement.executeQuery();
//            while (rs.next()) {
//                list.add(new ModelThanhToan(
//                        rs.getInt("MADP"),
//                        rs.getInt("MAPHONG"),
//                        rs.getInt("TONGTIEN"),
//                        rs.getString("DICHVU"),
//                        rs.getString("LYDO"),
//                        rs.getTimestamp("THOIGIAN").toString()
//                ));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return gson.toJson(list);
//    }
//
//    public String thongkedichvu(String ngaybd, String ngaykt) {
//        HashMap<String, Integer> serviceCount = new HashMap<>();
//        String sql = "SELECT DICHVU FROM thanhtoan WHERE THOIGIAN BETWEEN ? AND ?";
//        try (Connection connection = connectdatabase.getConnection();