//package DAO;
//
//import View.ManagerUI;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class DichVuDAO {
//    public void luuDichVuTruoc(Connection connect, String maKhachHang, int maDP, ManagerUI view) {
//        String maSuDungDichVu ="ghj";
//
//                // Tạo câu lệnh SQL INSERT
//                String sql = "INSERT INTO sudungthemdichvu (MASDDV, MADP, MADV, SLDV, CHIPHI) VALUES (?, ?, ?, ?, ?)";
//
//                // Gán giá trị cho các tham số
//                preparedStatement.setString(1, maSuDungDichVu);
//                preparedStatement.setInt(2, maDP);
//                preparedStatement.setInt(3, madv); // Lấy mã dịch vụ từ tên dịch vụ
//                preparedStatement.setInt(4, soLuong);
//                preparedStatement.setInt(5, thanhTien);
//
//                // Thực thi câu lệnh SQL
//                preparedStatement.executeUpdate();
//            }
//        }
