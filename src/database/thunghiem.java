package database;

import java.sql.SQLException;
import DAO.*;
import View.ManagerUI;

import javax.swing.table.DefaultTableModel;

public class thunghiem {

    public static void main(String[] args) {
        DatPhongDAO dp = new DatPhongDAO();
        try {
            dp.baocao();
            dp.thongtintraphong(786);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        KhachHangDAO kh = new KhachHangDAO();
        try {
            kh.laymakh("KH101");
            kh.CheckIn("KH101","12356");
            kh.kiemtraxacthuc("KH101","mail.@gmail.com");
            kh.capNhatMatKhau("KH101","12345634567");
            kh.taikhoan(new DefaultTableModel());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
