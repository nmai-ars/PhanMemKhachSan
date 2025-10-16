package DAO;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class KhachHangDAO {
    public String laymakh(String username) throws SQLException {
        String makh = null;
        try (Connection connection = connectdatabase.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("SELECT MAKH FROM customer WHERE USERNAME = ?");) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                makh = resultSet.getString("MAKH");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return makh;

    }

    public boolean CheckIn(String username, String pass) throws SQLException {
        boolean result = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSet rs = null;

        try {
            // Get a connection to the database
            connection = connectdatabase.getConnection();

            // SQL query to get the encoded password from the database
            String sql = "SELECT PASS FROM customer WHERE USERNAME = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Check if the result set has any rows
            if (resultSet.next()) {
                // Get the encoded password from the database
                String passwordFromDB = resultSet.getString("PASS");

                // Encode the input password with the same salt
                String salt = "asdfghjkl";
                String str = pass + salt;
                String encodedPass = Base64.getEncoder().encodeToString(str.getBytes());
                System.out.println(encodedPass);
                // Check if the encoded password matches the password from the database
                if (encodedPass.equals(passwordFromDB)) {

                    result = true;
                } else {
                    // Login failed

                    result = false;
                }
            } else {
                // Login failed
                result = false;
            }
        } finally {
            // Close resources in the reverse order of their creation to avoid resource
            // leaks
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return result;
    }
    public boolean kiemtraxacthuc(String tendangnhap, String email) throws SQLException {
        boolean isValid = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Get a connection to the database
            connection = connectdatabase.getConnection();

            // SQL query to check if the information is valid
            String sql = "SELECT * FROM customer WHERE USERNAME = ? AND EMAIL = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tendangnhap);
            preparedStatement.setString(2, email);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Check if the result set has any rows
            isValid = resultSet.next();
        } finally {
            // Close resources in the reverse order of their creation
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return isValid;

    }
    public boolean capNhatMatKhau(String tendangnhap, String newpass) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Get a connection to the database
            connection = connectdatabase.getConnection();

            // Encode the new password with salt
            String salt = "asdfghjkl";
            String str = newpass + salt;
            String passnew = Base64.getEncoder().encodeToString(str.getBytes());

            // SQL query to update the password
            String sql = "UPDATE customer SET PASS = ? WHERE USERNAME = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, passnew);
            preparedStatement.setString(2, tendangnhap);

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();



            if (rowsAffected > 0) {

                return true;
            } else {

                return false;
            }
        } finally {
            // Close resources in the reverse order of their creation
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void taikhoan(DefaultTableModel dftk) throws SQLException {
        if (dftk != null) { // Kiểm tra dftk trước khi sử dụng
            dftk.setRowCount(0);
        } else {
            System.err.println("Lỗi: DefaultTableModel dftk là null trong phương thức taikhoan.");
            return; // Hoặc xử lý lỗi theo cách khác
        }

        String sql = "SELECT MAKH, HOTEN, CCCD, SDT, EMAIL, PASS, USERNAME FROM customer";
        try (Connection connection = connectdatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String makh = resultSet.getString("MAKH");
                String hoten = resultSet.getString("HOTEN");
                String cccd = resultSet.getString("CCCD");
                String sdt = resultSet.getString("SDT");
                String email = resultSet.getString("EMAIL");
                String username = resultSet.getString("USERNAME");

                // Lấy mật khẩu đã mã hóa Base64 từ CSDL
                String passEncodedFromDB = "";
                Reader reader = resultSet.getCharacterStream("PASS");
                if (reader != null) {
                    StringBuilder sb = new StringBuilder();
                    char[] buffer = new char[1024];
                    int bytesRead;
                    try {
                        while ((bytesRead = reader.read(buffer)) != -1) {
                            sb.append(buffer, 0, bytesRead);
                        }
                        passEncodedFromDB = sb.toString();
                    } catch (IOException e_io) {
                        System.err.println("Lỗi khi đọc stream mật khẩu cho MAKH: " + makh);
                        e_io.printStackTrace();
                        // Có thể gán một giá trị mặc định hoặc bỏ qua nếu không đọc được
                    } finally {
                        try {
                            reader.close(); // Luôn đóng reader
                        } catch (IOException e_close) {
                            e_close.printStackTrace();
                        }
                    }
                }


                String originalPass = "[Không thể giải mã]"; // Giá trị mặc định nếu giải mã thất bại

                if (passEncodedFromDB != null && !passEncodedFromDB.isEmpty()) {
                    try {
                        // 1. Giải mã Base64
                        byte[] decodedBytes = Base64.getDecoder().decode(passEncodedFromDB);
                        String decodedString = new String(decodedBytes);

                        // 2. Loại bỏ salt cố định
                        String salt = "asdfghjkl"; // Salt đã dùng khi đăng ký
                        if (decodedString.endsWith(salt)) {
                            originalPass = decodedString.substring(0, decodedString.length() - salt.length());
                        } else {
                            // Trường hợp salt không khớp hoặc mật khẩu không được mã hóa theo cách này
                            originalPass = "[Lỗi giải mã - salt không khớp]";
                            System.err.println("Salt không khớp cho MAKH: " + makh + ". Chuỗi đã giải mã: " + decodedString);
                        }
                    } catch (IllegalArgumentException e_decode) {
                        // Lỗi nếu chuỗi từ DB không phải là Base64 hợp lệ
                        originalPass = "[Lỗi định dạng Base64]";
                        System.err.println("Lỗi giải mã Base64 cho MAKH: " + makh);
                        e_decode.printStackTrace();
                    }
                } else {
                    originalPass = "[Mật khẩu rỗng]";
                }
                // Đảm bảo dftk không null trước khi thêm hàng
                if (dftk != null) {
                    dftk.addRow(new Object[]{makh, hoten, cccd, sdt, email, originalPass, username});
                }
            }
        } catch (SQLException e_sql) {
            System.err.println("Lỗi SQL khi tải danh sách tài khoản: " + e_sql.getMessage());
            e_sql.printStackTrace();
            throw e_sql; // Ném lại ngoại lệ để lớp gọi có thể xử lý nếu cần
        }
    }

}
