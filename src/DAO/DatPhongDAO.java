package DAO;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatPhongDAO {
    public void baocao() throws SQLException {
        try (Connection connection = connectdatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT MADP, MAKH, MAPHONG, NGAYGIOVP, NGAYGIOTP FROM datphong");) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String madp = resultSet.getString("MADP");
                String makh = resultSet.getString("MAKH");
                String maphong = resultSet.getString("MAPHONG");
                String ngayvao = resultSet.getString("NGAYGIOVP");
                String ngayra = resultSet.getString("NGAYGIOTP");

                Object data[][] = null;
                String cl[] = { "MÃ ĐẶT PHÒNG", "MÃ KHÁCH HÀNG", "MÃ PHÒNG", "NGÀY GIỜ VÀO PHÒNG", "NGÀY GIỜ TRẢ PHÒNG" };
                DefaultTableModel df = new DefaultTableModel(data, cl);
                df.addRow(new Object[]{madp, makh, maphong, ngayvao, ngayra});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean thongtintraphong(int maDp) {
        Connection connect = null;
        PreparedStatement preparedStatement = null;

        try {
            // Kết nối đến database
            connect = connectdatabase.getConnection();

            // Bắt đầu Transaction
            connect.setAutoCommit(false);

            String maKhachHang = "makh";
            String maPhong = "maPhong";
            String ngayVao = "ngayVao";
            String ngayRa = "ngayra";
            String madatphong = "madatphong";
            int tienphong =7;
            String ngaygiott = "ngaygiott";
            int thue =8;
            int tongtien = 45678;


            // Tạo câu lệnh SQL INSERT cho bảng datphong
            String sql1 = "INSERT INTO datphong (MADP, MAKH, MAPHONG, NGAYGIOVP, NGAYGIOTP, CHIPHI) VALUES (?, ?, ?, ?, ?, ?)";
            preparedStatement = connect.prepareStatement(sql1);
            preparedStatement.setString(1, madatphong); // Tạo mã đặt phòng
            preparedStatement.setString(2, maKhachHang);
            preparedStatement.setString(3, maPhong);
            preparedStatement.setString(4, ngayVao);
            preparedStatement.setString(5, ngayRa);
            preparedStatement.setInt(6, tienphong); // Giả sử chi phí đặt phòng là tổng tiền
            preparedStatement.executeUpdate();

            // Tạo câu lệnh SQL INSERT cho bảng thanhtoan
            String sql2 = "INSERT INTO thanhtoan (MATHANHTOAN, MADP, NGAYGIOTT, THUE, TONGCHIPHI) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connect.prepareStatement(sql2);
            preparedStatement.setString(2, madatphong); // Sử dụng mã đặt phòng từ bước trước
            preparedStatement.setString(3, ngaygiott); // Ngày giờ thanh toán là ngày giờ trả phòng
            preparedStatement.setInt(4, thue);
            preparedStatement.setInt(5, tongtien);
            preparedStatement.executeUpdate();

            // Commit Transaction
            connect.commit();
            return true; // Lưu thông tin thành công

        } catch (SQLException e) {
            e.printStackTrace();

            try {
                // Rollback Transaction nếu có lỗi
                if (connect != null) {
                    connect.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } finally {
            // Đóng các tài nguyên
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false; // Lưu thông tin thất bại
    }
}
