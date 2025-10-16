package View;

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.sql.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.google.gson.Gson;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.time.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;

import Model.ModelDichVu;
import Model.ModelKhachHang;
import Model.Modelchuoi;
import Model.Phong;
import Model.Phong.LoaiPhong;
import Model.Phong.TrangThaiPhong;

import controller.PhongManagerQL;
import DAO.connectdatabase;

import java.awt.geom.RoundRectangle2D;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManagerUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private final Connection conn;
	public Phong[] phong;
	JPanel pn_trangchu;
	JPanel pn_sodophong;
	JPanel pn_hoatdong;
	private static List<ClientThread> clients = new ArrayList<>();
	private static Socket clientSocket;
	private static ServerSocket serverSocket;
	Color colordat = new Color(205, 180, 219);
	Color colorchoxacnhan = new Color(255, 200, 221);
	public CardLayout cardhd;
	public HashMap<Integer, String> key_room = new HashMap<Integer, String>();
	public HashMap<ClientThread, String> dulieukhach = new HashMap<ClientThread, String>();
	public HashMap<String, Integer> dulieudp = new HashMap<String, Integer>();
	private DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	public ArrayList<PhongManagerQL> quanLyPhong;
	ModelDichVu[] danhsachDV = new ModelDichVu[17];
	private JTable table;
	private JTable tableDanhSachHoaDonTrongBaoCao; // Đổi tên để rõ ràng hơn, đây là JTable trong pn_baocao
	private DefaultTableModel modelTableBaoCao;

	private JButton btLocPhongTrong;
	private JButton btLocPhongDaDat;
	private JButton btLocPhongChoXacNhan;
	private JButton btHienThiTatCa;
	private JLabel lblNewLabel_17, lblNewLabel_17_1, lblNewLabel_17_1_1, lblNewLabel_17_1_2,
			lblNewLabel_17_1_3, lblNewLabel_17_1_4, lblNewLabel_17_1_5, lblNewLabel_17_1_6,
			lblNewLabel_17_1_4_1, lblNewLabel_17_1_4_2, lblNewLabel_17_1_4_3,
			lblNewLabel_17_1_4_4, lblNewLabel_17_1_4_5, lblNewLabel_17_1_4_6,
			lblNewLabel_17_1_4_7, lblNewLabel_17_1_4_8;
	public DefaultTableModel df;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			ManagerUI manager = new ManagerUI();
			try {
				serverSocket = new ServerSocket(8000);
				System.out.println("Server đang lắng nghe trên cổng " + 8000);

				while (true) {
					clientSocket = serverSocket.accept();
					System.out.println("Đã kết nối với Client: " + clientSocket.getInetAddress().getHostAddress());

					ClientThread clientThread = new ClientThread(clientSocket, manager);
					clients.add(clientThread);
					clientThread.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Create the frame.
	 */
	public ManagerUI() {
		setTitle("Hệ thống quản lý HomeStay - Khách Sạn");
		getContentPane().setBackground(new Color(113, 223, 214));
		Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ManagerUI.class.getResource("/fileanh/hotel.png")));
		border = BorderFactory.createCompoundBorder(new RoundedBorder(20, 20, Color.GRAY), border);
		Font font = new Font("Roboto", Font.BOLD, 22);
		Font font2 = new Font("Roboto", Font.CENTER_BASELINE, 18);

		Connection conn = connectdatabase.getConnection();
		this.conn = connectdatabase.getConnection();
		quanLyPhong = new ArrayList<PhongManagerQL>();
		phong = new Phong[] { new Phong(101, "Phòng 101", TrangThaiPhong.TRONG, 400000, LoaiPhong.THUONG),
				new Phong(102, "Phòng 102", TrangThaiPhong.TRONG, 400000, LoaiPhong.THUONG),
				new Phong(103, "Phòng 103", TrangThaiPhong.TRONG, 400000, LoaiPhong.THUONG),
				new Phong(104, "Phòng 104", TrangThaiPhong.TRONG, 400000, LoaiPhong.THUONG),
				new Phong(201, "Phòng 201", TrangThaiPhong.TRONG, 600000, LoaiPhong.TRUNG),
				new Phong(202, "Phòng 202", TrangThaiPhong.TRONG, 600000, LoaiPhong.TRUNG),
				new Phong(203, "Phòng 203", TrangThaiPhong.TRONG, 600000, LoaiPhong.TRUNG),
				new Phong(204, "Phòng 204", TrangThaiPhong.TRONG, 600000, LoaiPhong.TRUNG),
				new Phong(301, "Phòng 301", TrangThaiPhong.TRONG, 800000, LoaiPhong.VIP),
				new Phong(302, "Phòng 302", TrangThaiPhong.TRONG, 800000, LoaiPhong.VIP),
				new Phong(303, "Phòng 303", TrangThaiPhong.TRONG, 800000, LoaiPhong.VIP),
				new Phong(304, "Phòng 304", TrangThaiPhong.TRONG, 800000, LoaiPhong.VIP), };
		danhsachDV[0] = new ModelDichVu(0, "Nước lọc", 15000);
		danhsachDV[1] = new ModelDichVu(1, "Snack khoai tây", 20000);
		danhsachDV[2] = new ModelDichVu(2, "Coca/Pepsi", 20000);
		danhsachDV[3] = new ModelDichVu(3, "Rượu Vodka SMIRNOFF 700ML", 400000);
		danhsachDV[4] = new ModelDichVu(4, "Bánh KitKat", 25000);
		danhsachDV[5] = new ModelDichVu(5, "Nước Smartwater 500ML", 60000);
		danhsachDV[6] = new ModelDichVu(6, "Bia Heineken 250ML", 20000);
		danhsachDV[7] = new ModelDichVu(7, "Vang Ý Mango Tropical 750ML", 500000);
		danhsachDV[8] = new ModelDichVu(8, "Chivas Regal 18 Gold Signature 700ML", 1400000);
		danhsachDV[9] = new ModelDichVu(9, "Cho thuê xe tự lái", 700000);
		danhsachDV[10] = new ModelDichVu(10, "Dùng điểm tâm", 500000);
		danhsachDV[11] = new ModelDichVu(11, "Đưa đón sân bay", 200000);
		danhsachDV[12] = new ModelDichVu(12, "Trông trẻ", 300000);
		danhsachDV[13] = new ModelDichVu(13, "Tuần trăng mật", 3000000);
		danhsachDV[14] = new ModelDichVu(14, "Giặt ủi", 100000);
		danhsachDV[15] = new ModelDichVu(15, "Spa", 1500000);
		danhsachDV[16] = new ModelDichVu(16, "Fitness", 200000);

		key_room.put(101, "");
		key_room.put(102, "");
		key_room.put(103, "");
		key_room.put(104, "");
		key_room.put(201, "");
		key_room.put(202, "");
		key_room.put(203, "");
		key_room.put(204, "");
		key_room.put(301, "");
		key_room.put(302, "");
		key_room.put(303, "");
		key_room.put(304, "");


		this.setVisible(true);
		this.setSize(1200, 800);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JPanel pn_menu = new JPanel();
		pn_menu.setBackground(new Color(255, 255, 255));
		pn_menu.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 0, 0), null, null, null));
		pn_menu.setBounds(10, 10, 211, 742);
		getContentPane().add(pn_menu);

		pn_hoatdong = new JPanel();
		pn_hoatdong.setBounds(231, 10, 947, 742);
		getContentPane().add(pn_hoatdong);
		cardhd = new CardLayout();
		pn_hoatdong.setLayout(cardhd);

		JButton bt_trangchu = new JButton("Trang chủ");
		bt_trangchu.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/trangchu.png")));
		bt_trangchu.setBackground(new Color(214, 255, 249));
		bt_trangchu.setOpaque(true);                         // Bắt buộc vẽ nền
		bt_trangchu.setContentAreaFilled(true);              // Cho phép tô nền
		bt_trangchu.setBorderPainted(false);
		bt_trangchu.setBounds(2, 2, 207, 67);
		bt_trangchu.setFont(font2);
		bt_trangchu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cardhd.show(pn_hoatdong, "anhTrangchu");
			}
		});
		pn_menu.setLayout(null);
		pn_menu.add(bt_trangchu);

		JButton bt_sodophong = new JButton("Sơ đồ phòng");
		bt_sodophong.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/diagram.png")));
		bt_sodophong.setBackground(new Color(214, 255, 249));
		bt_sodophong.setOpaque(true);                         // Bắt buộc vẽ nền
		bt_sodophong.setContentAreaFilled(true);              // Cho phép tô nền
		bt_sodophong.setBorderPainted(false);
		bt_sodophong.setBounds(2, 74, 207, 67);
		bt_sodophong.setFont(font2);
		pn_menu.add(bt_sodophong);

		JButton bt_Thongke = new JButton("Thống kê");
		bt_Thongke.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/pie-chart.png")));
		bt_Thongke.setBackground(new Color(214, 255, 249));
		bt_Thongke.setOpaque(true);                         // Bắt buộc vẽ nền
		bt_Thongke.setContentAreaFilled(true);              // Cho phép tô nền
		bt_Thongke.setBorderPainted(false);
		bt_Thongke.setBounds(2, 146, 207, 67);
		bt_Thongke.setFont(font2);
		pn_menu.add(bt_Thongke);

		JButton bt_baocao = new JButton("Báo Cáo");
		bt_baocao.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/report.png")));
		bt_baocao.setBackground(new Color(214, 255, 249));
		bt_baocao.setOpaque(true);                         // Bắt buộc vẽ nền
		bt_baocao.setContentAreaFilled(true);              // Cho phép tô nền
		bt_baocao.setBorderPainted(false);
		bt_baocao.setBounds(2, 218, 207, 67);
		bt_baocao.setFont(font2);
		pn_menu.add(bt_baocao);

//		JButton bt_dangxuat = new JButton("Đăng Xuất");
//		bt_dangxuat.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/logout (1).png")));
//		bt_dangxuat.setBackground(new Color(214, 255, 249));
//		bt_dangxuat.setOpaque(true);
//		bt_dangxuat.setContentAreaFilled(true);
//		bt_dangxuat.setBorderPainted(false);
//		bt_dangxuat.setBounds(2,218,207,67);
//		bt_dangxuat.setFont(font2);
//		pn_menu.add(bt_dangxuat);
//		pn_menu.setLayout(null); // Đảm bảo có dòng này trước khi add nút

		JButton bt_taikhoan = new JButton("Tài Khoản");
		bt_taikhoan.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/taikhoan.png")));
		bt_taikhoan.setBackground(new Color(214, 255, 249));
		bt_taikhoan.setOpaque(true);                         // Bắt buộc vẽ nền
		bt_taikhoan.setContentAreaFilled(true);              // Cho phép tô nền
		bt_taikhoan.setBorderPainted(false);
		bt_taikhoan.setBounds(2, 290, 207, 67);
		bt_taikhoan.setFont(font2);
		pn_menu.add(bt_taikhoan);

		// Sửa lại để gán cho biến thành viên của lớp
		this.pn_trangchu = new JPanel(); // Hoặc chỉ cần: pn_trangchu = new JPanel();
		this.pn_hoatdong.add(this.pn_trangchu, "anhTrangchu");
		this.pn_trangchu.setLayout(new BorderLayout(0, 0));

// Sửa lại để gán cho biến thành viên của lớp
		this.pn_sodophong = new JPanel(); // Hoặc chỉ cần: pn_sodophong = new JPanel();
		this.pn_sodophong.setBackground(new Color(255, 255, 255));
		this.pn_hoatdong.add(this.pn_sodophong, "sơ đồ phòng");
		this.pn_sodophong.setLayout(null);
		bt_sodophong.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardhd.show(pn_hoatdong, "sơ đồ phòng");
			}
		});

		JPanel pn_bar = new JPanel();
		pn_bar.setBounds(10, 10, 927, 40);
		this.pn_sodophong.add(pn_bar);
		pn_bar.setLayout(new GridLayout(1, 0, 10, 10));

		JPanel pn_luachon = new JPanel();
		pn_luachon.setBackground(new Color(255, 255, 255));
		pn_bar.add(pn_luachon);
		pn_luachon.setLayout(new GridLayout(1, 4, 10, 10)); // Giả sử có 4 nút

// Nút Lọc Phòng Trống
		btLocPhongTrong = new JButton("Trống");
		btLocPhongTrong.setFont(font); // Sử dụng font đã định nghĩa
		btLocPhongTrong.setBackground(Color.getHSBColor(0, 0, (float) 0.94));
		btLocPhongTrong.setOpaque(true);
		btLocPhongTrong.setBorderPainted(false); // Tùy chọn: có thể giữ border để trông giống nút hơn
		pn_luachon.add(btLocPhongTrong);

// Nút Lọc Phòng Đã Đặt
		btLocPhongDaDat = new JButton("Đã Đặt");
		btLocPhongDaDat.setFont(font);
		btLocPhongDaDat.setBackground(colordat); // colordat đã được định nghĩa
		btLocPhongDaDat.setOpaque(true);
		btLocPhongDaDat.setBorderPainted(false);
		pn_luachon.add(btLocPhongDaDat);

// Nút Lọc Phòng Chờ Xác Nhận
		btLocPhongChoXacNhan = new JButton("Chờ Xác Nhận");
		btLocPhongChoXacNhan.setFont(font);
		btLocPhongChoXacNhan.setBackground(colorchoxacnhan); // colorchoxacnhan đã được định nghĩa
		btLocPhongChoXacNhan.setOpaque(true);
		btLocPhongChoXacNhan.setBorderPainted(false);
		pn_luachon.add(btLocPhongChoXacNhan);

// Nút Hiển Thị Tất Cả
		btHienThiTatCa = new JButton("Tất Cả");
		btHienThiTatCa.setFont(font);
// btHienThiTatCa.setBackground(new Color(220, 220, 220)); // Một màu trung tính
		btHienThiTatCa.setOpaque(true);
		btHienThiTatCa.setBorderPainted(false);
		pn_luachon.add(btHienThiTatCa);

		btLocPhongTrong.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filterPhongTheoTrangThai(TrangThaiPhong.TRONG);
			}
		});

		btLocPhongDaDat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// "Đã Đặt" trong ngữ cảnh này có thể tương ứng với DANG_HOAT_DONG
				filterPhongTheoTrangThai(TrangThaiPhong.DANG_HOAT_DONG);
			}
		});

		btLocPhongChoXacNhan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filterPhongTheoTrangThai(TrangThaiPhong.CHO_XAC_NHAN);
			}
		});

		btHienThiTatCa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hienThiTatCaPhong();
			}
		});

		// Tạo các panel để cho thấy thông tin phòng
		JPanel pn_p101 = new JPanel();
		JPanel pn_p102 = new JPanel();
		JPanel pn_p103 = new JPanel();
		JPanel pn_p104 = new JPanel();
		JPanel pn_p201 = new JPanel();
		JPanel pn_p202 = new JPanel();
		JPanel pn_p203 = new JPanel();
		JPanel pn_p204 = new JPanel();
		JPanel pn_p301 = new JPanel();
		JPanel pn_p302 = new JPanel();
		JPanel pn_p303 = new JPanel();
		JPanel pn_p304 = new JPanel();

		JPanel panel_phong1 = new JPanel();
		panel_phong1.setBorder(border);
		panel_phong1.setBounds(10, 79, 200, 200);
		this.pn_sodophong.add(panel_phong1);
		panel_phong1.setLayout(null);
		JLabel lblNewLabel_3 = new JLabel("P_101");
		lblNewLabel_3.setBounds(10, 10, 70, 30);
		lblNewLabel_3.setBackground(new Color(240, 240, 240));
		lblNewLabel_3.setFont(font);
		panel_phong1.add(lblNewLabel_3);
		JLabel photo1 = new JLabel("");
		photo1.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/website.png")));
		photo1.setBounds(36, 50, 128, 128);
		panel_phong1.add(photo1);
		pn_DanghoatdongQL hoatdong101 = new pn_DanghoatdongQL(phong[0], this);
		pn_ChoxacnhanQL xacnhan101 = new pn_ChoxacnhanQL(phong[0], hoatdong101, this);
		pn_DatphongQL datphong101 = new pn_DatphongQL(phong[0], xacnhan101, hoatdong101, this);
		CardLayout cardP1 = new CardLayout();
		pn_p101.setLayout(cardP1);
		pn_p101.add(datphong101, "datohong101");
		pn_p101.add(xacnhan101, "xacnhan101");
		pn_p101.add(hoatdong101, "hoatdong101");
		PhongManagerQL manager1 = new PhongManagerQL(phong[0], panel_phong1, cardP1, datphong101, xacnhan101,
				hoatdong101, pn_p101);
		manager1.start();
		quanLyPhong.add(manager1);
		panel_phong1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardhd.show(pn_hoatdong, "phong 101");
			}
		});

		JPanel panel_phong2 = new JPanel();
		panel_phong2.setBorder(border);
		panel_phong2.setBounds(252, 79, 200, 200);
		this.pn_sodophong.add(panel_phong2);
		panel_phong2.setLayout(null);
		JLabel lblNewLabel_4 = new JLabel("P_102");
		lblNewLabel_4.setBounds(10, 10, 70, 30);
		lblNewLabel_4.setFont(font);
		panel_phong2.add(lblNewLabel_4);
		JLabel photo2 = new JLabel("");
		photo2.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/website.png")));
		photo2.setBounds(36, 50, 128, 128);
		panel_phong2.add(photo2);
		pn_DanghoatdongQL hoatdong102 = new pn_DanghoatdongQL(phong[1], this);
		pn_ChoxacnhanQL xacnhan102 = new pn_ChoxacnhanQL(phong[1], hoatdong102, this);
		pn_DatphongQL datphong102 = new pn_DatphongQL(phong[1], xacnhan102, hoatdong102, this);
		CardLayout cardP2 = new CardLayout();
		pn_p102.setLayout(cardP2);
		pn_p102.add(datphong102, "datohong102");
		pn_p102.add(xacnhan102, "xacnhan102");
		pn_p102.add(hoatdong102, "hoatdong102");
		PhongManagerQL manager2 = new PhongManagerQL(phong[1], panel_phong2, cardP2, datphong102, xacnhan102,
				hoatdong102, pn_p102);
		manager2.start();
		quanLyPhong.add(manager2);
		panel_phong2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardhd.show(pn_hoatdong, "phong 102");
			}
		});

		JPanel panel_phong3 = new JPanel();
		panel_phong3.setBorder(border);
		panel_phong3.setBounds(498, 79, 200, 200);
		this.pn_sodophong.add(panel_phong3);
		panel_phong3.setLayout(null);
		JLabel lblNewLabel_5 = new JLabel("P_103");
		lblNewLabel_5.setBounds(10, 10, 70, 30);
		lblNewLabel_5.setFont(font);
		panel_phong3.add(lblNewLabel_5);
		JLabel photo3 = new JLabel("");
		photo3.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/website.png")));
		photo3.setBounds(36, 50, 128, 128);
		panel_phong3.add(photo3);
		pn_DanghoatdongQL hoatdong103 = new pn_DanghoatdongQL(phong[2], this);
		pn_ChoxacnhanQL xacnhan103 = new pn_ChoxacnhanQL(phong[2], hoatdong103, this);
		pn_DatphongQL datphong103 = new pn_DatphongQL(phong[2], xacnhan103, hoatdong103, this);
		CardLayout cardP3 = new CardLayout();
		pn_p103.setLayout(cardP3);
		pn_p103.add(datphong103, "datohong103");
		pn_p103.add(xacnhan103, "xacnhan103");
		pn_p103.add(hoatdong103, "hoatdong103");
		PhongManagerQL manager3 = new PhongManagerQL(phong[2], panel_phong3, cardP3, datphong103, xacnhan103,
				hoatdong103, pn_p103);
		manager3.start();
		quanLyPhong.add(manager3);
		panel_phong3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardhd.show(pn_hoatdong, "phong 103");
			}
		});

		JPanel panel_phong4 = new JPanel();
		panel_phong4.setBorder(border);
		panel_phong4.setBounds(737, 79, 200, 200);
		this.pn_sodophong.add(panel_phong4);
		panel_phong4.setLayout(null);
		JLabel lblNewLabel_6 = new JLabel("P_104");
		lblNewLabel_6.setBounds(10, 10, 70, 30);
		lblNewLabel_6.setFont(font);
		panel_phong4.add(lblNewLabel_6);
		JLabel photo4 = new JLabel("");
		photo4.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/website.png")));
		photo4.setBounds(36, 50, 128, 128);
		panel_phong4.add(photo4);
		pn_DanghoatdongQL hoatdong104 = new pn_DanghoatdongQL(phong[3], this);
		pn_ChoxacnhanQL xacnhan104 = new pn_ChoxacnhanQL(phong[3], hoatdong104, this);
		pn_DatphongQL datphong104 = new pn_DatphongQL(phong[3], xacnhan104, hoatdong104, this);
		CardLayout cardP4 = new CardLayout();
		pn_p104.setLayout(cardP4);
		pn_p104.add(datphong104, "datohong104");
		pn_p104.add(xacnhan104, "xacnhan104");
		pn_p104.add(hoatdong104, "hoatdong104");
		PhongManagerQL manager4 = new PhongManagerQL(phong[3], panel_phong4, cardP4, datphong104, xacnhan104,
				hoatdong104, pn_p104);
		manager4.start();
		quanLyPhong.add(manager4);
		panel_phong4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardhd.show(pn_hoatdong, "phong 104");
			}
		});

		JPanel panel_phong5 = new JPanel();
		panel_phong5.setBorder(border);
		panel_phong5.setBounds(10, 306, 200, 200);
		this.pn_sodophong.add(panel_phong5);
		panel_phong5.setLayout(null);
		JLabel lblNewLabel_7 = new JLabel("P_201");
		lblNewLabel_7.setBounds(10, 10, 70, 30);
		lblNewLabel_7.setFont(font);
		panel_phong5.add(lblNewLabel_7);
		JLabel photo5 = new JLabel("");
		photo5.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/website.png")));
		photo5.setBounds(36, 50, 128, 128);
		panel_phong5.add(photo5);
		pn_DanghoatdongQL hoatdong201 = new pn_DanghoatdongQL(phong[4], this);
		pn_ChoxacnhanQL xacnhan201 = new pn_ChoxacnhanQL(phong[4], hoatdong201, this);
		pn_DatphongQL datphong201 = new pn_DatphongQL(phong[4], xacnhan201, hoatdong201, this);
		CardLayout cardP5 = new CardLayout();
		pn_p201.setLayout(cardP5);
		pn_p201.add(datphong201, "datohong201");
		pn_p201.add(xacnhan201, "xacnhan201");
		pn_p201.add(hoatdong201, "hoatdong201");
		PhongManagerQL manager5 = new PhongManagerQL(phong[4], panel_phong5, cardP5, datphong201, xacnhan201,
				hoatdong201, pn_p201);
		manager5.start();
		quanLyPhong.add(manager5);
		panel_phong5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardhd.show(pn_hoatdong, "phong 201");
			}
		});

		JPanel panel_phong6 = new JPanel();
		panel_phong6.setBorder(border);
		panel_phong6.setBounds(252, 306, 200, 200);
		this.pn_sodophong.add(panel_phong6);
		panel_phong6.setLayout(null);
		JLabel lblNewLabel_11 = new JLabel("P_202");
		lblNewLabel_11.setBounds(10, 10, 70, 30);
		lblNewLabel_11.setFont(font);
		panel_phong6.add(lblNewLabel_11);
		JLabel photo6 = new JLabel("");
		photo6.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/website.png")));
		photo6.setBounds(36, 50, 128, 128);
		panel_phong6.add(photo6);
		pn_DanghoatdongQL hoatdong202 = new pn_DanghoatdongQL(phong[5], this);
		pn_ChoxacnhanQL xacnhan202 = new pn_ChoxacnhanQL(phong[5], hoatdong202, this);
		pn_DatphongQL datphong202 = new pn_DatphongQL(phong[5], xacnhan202, hoatdong202, this);
		CardLayout cardP6 = new CardLayout();
		pn_p202.setLayout(cardP6);
		pn_p202.add(datphong202, "datohong202");
		pn_p202.add(xacnhan202, "xacnhan202");
		pn_p202.add(hoatdong202, "hoatdong202");
		PhongManagerQL manager6 = new PhongManagerQL(phong[5], panel_phong6, cardP6, datphong202, xacnhan202,
				hoatdong202, pn_p202);
		manager6.start();
		quanLyPhong.add(manager6);
		panel_phong6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardhd.show(pn_hoatdong, "phong 202");
			}
		});

		JPanel panel_phong7 = new JPanel();
		panel_phong7.setBorder(border);
		panel_phong7.setBounds(498, 306, 200, 200);
		this.pn_sodophong.add(panel_phong7);
		panel_phong7.setLayout(null);
		JLabel lblNewLabel_12 = new JLabel("P_203");
		lblNewLabel_12.setBounds(10, 10, 70, 30);
		lblNewLabel_12.setFont(font);
		panel_phong7.add(lblNewLabel_12);
		JLabel photo7 = new JLabel("");
		photo7.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/website.png")));
		photo7.setBounds(36, 50, 128, 128);
		panel_phong7.add(photo7);
		pn_DanghoatdongQL hoatdong203 = new pn_DanghoatdongQL(phong[6], this);
		pn_ChoxacnhanQL xacnhan203 = new pn_ChoxacnhanQL(phong[6], hoatdong203, this);
		pn_DatphongQL datphong203 = new pn_DatphongQL(phong[6], xacnhan203, hoatdong203, this);

		CardLayout cardP7 = new CardLayout();
		pn_p203.setLayout(cardP7);
		pn_p203.add(datphong203, "datohong203");
		pn_p203.add(xacnhan203, "xacnhan203");
		pn_p203.add(hoatdong203, "hoatdong203");
		PhongManagerQL manager7 = new PhongManagerQL(phong[6], panel_phong7, cardP7, datphong203, xacnhan203,
				hoatdong203, pn_p203);
		manager7.start();
		quanLyPhong.add(manager7);
		panel_phong7.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardhd.show(pn_hoatdong, "phong 203");
			}
		});

		JPanel panel_phong8 = new JPanel();
		panel_phong8.setBorder(border);
		panel_phong8.setBounds(737, 306, 200, 200);
		this.pn_sodophong.add(panel_phong8);
		panel_phong8.setLayout(null);
		JLabel lblNewLabel_13 = new JLabel("P_204");
		lblNewLabel_13.setBounds(10, 10, 70, 30);
		lblNewLabel_13.setFont(font);
		panel_phong8.add(lblNewLabel_13);
		JLabel photo8 = new JLabel("");
		photo8.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/website.png")));
		photo8.setBounds(36, 50, 128, 128);
		panel_phong8.add(photo8);
		pn_DanghoatdongQL hoatdong204 = new pn_DanghoatdongQL(phong[7], this);
		pn_ChoxacnhanQL xacnhan204 = new pn_ChoxacnhanQL(phong[7], hoatdong204, this);
		pn_DatphongQL datphong204 = new pn_DatphongQL(phong[7], xacnhan204, hoatdong204, this);
		CardLayout cardP8 = new CardLayout();
		pn_p204.setLayout(cardP8);
		pn_p204.add(datphong204, "datohong204");
		pn_p204.add(xacnhan204, "xacnhan204");
		pn_p204.add(hoatdong204, "hoatdong204");
		PhongManagerQL manager8 = new PhongManagerQL(phong[7], panel_phong8, cardP8, datphong204, xacnhan204,
				hoatdong204, pn_p204);
		manager8.start();
		quanLyPhong.add(manager8);
		panel_phong8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardhd.show(pn_hoatdong, "phong 204");
			}
		});
		pn_DanghoatdongQL hoatdong301 = new pn_DanghoatdongQL(phong[8], this);
		pn_ChoxacnhanQL xacnhan301 = new pn_ChoxacnhanQL(phong[8], hoatdong301, this);
		pn_DatphongQL datphong301 = new pn_DatphongQL(phong[8], xacnhan301, hoatdong301, this);
		CardLayout cardP9 = new CardLayout();
		pn_p301.setLayout(cardP9);
		pn_p301.add(datphong301, "datohong301");
		pn_p301.add(xacnhan301, "xacnhan301");
		pn_p301.add(hoatdong301, "hoatdong301");
		JPanel panel_phong9 = new JPanel();
		panel_phong9.setBounds(10, 532, 200, 200);
		this.pn_sodophong.add(panel_phong9);
		panel_phong9.setBorder(border);
		panel_phong9.setLayout(null);
		JLabel lblNewLabel_14 = new JLabel("P_301");
		lblNewLabel_14.setBounds(10, 10, 70, 30);
		lblNewLabel_14.setFont(font);
		panel_phong9.add(lblNewLabel_14);
		JLabel photo9 = new JLabel("");
		photo9.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/website.png")));
		photo9.setBounds(36, 50, 128, 128);
		panel_phong9.add(photo9);
		PhongManagerQL manager9 = new PhongManagerQL(phong[8], panel_phong9, cardP9, datphong301, xacnhan301,
				hoatdong301, pn_p301);
		panel_phong9.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardhd.show(pn_hoatdong, "phong 301");
			}
		});
		manager9.start();
		quanLyPhong.add(manager9);

		JPanel panel_phong10 = new JPanel();
		panel_phong10.setBorder(border);
		panel_phong10.setBounds(252, 532, 200, 200);
		this.pn_sodophong.add(panel_phong10);
		panel_phong10.setLayout(null);
		JLabel lblNewLabel_10 = new JLabel("P_302");
		lblNewLabel_10.setBounds(10, 10, 70, 30);
		lblNewLabel_10.setFont(font);
		panel_phong10.add(lblNewLabel_10);
		JLabel photo10 = new JLabel("");
		photo10.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/website.png")));
		photo10.setBounds(36, 50, 128, 128);
		panel_phong10.add(photo10);
		pn_DanghoatdongQL hoatdong302 = new pn_DanghoatdongQL(phong[9], this);
		pn_ChoxacnhanQL xacnhan302 = new pn_ChoxacnhanQL(phong[9], hoatdong302, this);
		pn_DatphongQL datphong302 = new pn_DatphongQL(phong[9], xacnhan302, hoatdong302, this);
		CardLayout cardP10 = new CardLayout();
		pn_p302.setLayout(cardP10);
		pn_p302.add(datphong302, "datohong302");
		pn_p302.add(xacnhan302, "xacnhan302");
		pn_p302.add(hoatdong302, "hoatdong302");
		PhongManagerQL manager10 = new PhongManagerQL(phong[9], panel_phong10, cardP10, datphong302, xacnhan302,
				hoatdong302, pn_p302);
		manager10.start();
		quanLyPhong.add(manager10);
		panel_phong10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardhd.show(pn_hoatdong, "phong 302");
			}
		});

		JPanel panel_phong11 = new JPanel();
		panel_phong11.setBorder(border);
		panel_phong11.setBounds(498, 532, 200, 200);
		this.pn_sodophong.add(panel_phong11);
		panel_phong11.setLayout(null);
		JLabel lblNewLabel_8 = new JLabel("P_303");
		lblNewLabel_8.setBounds(10, 10, 70, 30);
		lblNewLabel_8.setFont(font);
		panel_phong11.add(lblNewLabel_8);
		JLabel photo11 = new JLabel("");
		photo11.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/website.png")));
		photo11.setBounds(36, 50, 128, 128);
		panel_phong11.add(photo11);
		pn_DanghoatdongQL hoatdong303 = new pn_DanghoatdongQL(phong[10], this);
		pn_ChoxacnhanQL xacnhan303 = new pn_ChoxacnhanQL(phong[10], hoatdong303, this);
		pn_DatphongQL datphong303 = new pn_DatphongQL(phong[10], xacnhan303, hoatdong303, this);
		CardLayout cardP11 = new CardLayout();
		pn_p303.setLayout(cardP11);
		pn_p303.add(datphong303, "datohong303");
		pn_p303.add(xacnhan303, "xacnhan303");
		pn_p303.add(hoatdong303, "hoatdong303");
		PhongManagerQL manager11 = new PhongManagerQL(phong[10], panel_phong11, cardP11, datphong303, xacnhan303,
				hoatdong303, pn_p303);
		manager11.start();
		quanLyPhong.add(manager11);
		panel_phong11.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardhd.show(pn_hoatdong, "phong 303");
			}
		});

		JPanel panel_phong12 = new JPanel();
		panel_phong12.setBorder(border);
		panel_phong12.setBounds(737, 532, 200, 200);
		this.pn_sodophong.add(panel_phong12);
		panel_phong12.setLayout(null);
		JLabel lblNewLabel_9 = new JLabel("P_304");
		lblNewLabel_9.setBounds(10, 10, 70, 30);
		lblNewLabel_9.setFont(font);
		panel_phong12.add(lblNewLabel_9);
		JLabel photo12 = new JLabel("");
		photo12.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/website.png")));
		photo12.setBounds(36, 50, 128, 128);
		panel_phong12.add(photo12);
		pn_DanghoatdongQL hoatdong304 = new pn_DanghoatdongQL(phong[11], this);
		pn_ChoxacnhanQL xacnhan304 = new pn_ChoxacnhanQL(phong[11], hoatdong304, this);
		pn_DatphongQL datphong304 = new pn_DatphongQL(phong[11], xacnhan304, hoatdong304, this);
		CardLayout cardP12 = new CardLayout();
		pn_p304.setLayout(cardP12);
		pn_p304.add(datphong304, "datohong304");
		pn_p304.add(xacnhan304, "xacnhan304");
		pn_p304.add(hoatdong304, "hoatdong304");
		PhongManagerQL manager12 = new PhongManagerQL(phong[11], panel_phong12, cardP12, datphong304, xacnhan304,
				hoatdong304, pn_p304);


		manager12.start();
		quanLyPhong.add(manager12);
		panel_phong12.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardhd.show(pn_hoatdong, "phong 304");
			}
		});

		JLabel lbanhtrangchu = new JLabel("");
		lbanhtrangchu.setIcon(new ImageIcon(ManagerUI.class.getResource("/fileanh/anhksan.png")));

//		ImageIcon originalIcon = new ImageIcon(ManagerUI.class.getResource("/fileanh/anhksan.png"));
//		Image scaledImage = originalIcon.getImage().getScaledInstance(1366,768, Image.SCALE_SMOOTH);
//		lbanhtrangchu.setIcon(new ImageIcon(scaledImage));

		JPanel pn_thongke = new JPanel();
		pn_hoatdong.add(pn_thongke, "thống kê");
		pn_thongke.setLayout(null);
		bt_Thongke.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cardhd.show(pn_hoatdong, "thống kê");
			}
		});

		Thongkedoanhthu thongkedoanhthu = new Thongkedoanhthu();
		final JFreeChart[] bieudodoanhthu = {thongkedoanhthu.createRevenueChart(conn)};
		Thongkedichvu thongkedichvu = new Thongkedichvu();
		final JFreeChart[] bieudodichvu = {thongkedichvu.createServiceChart(conn)};
		//
		JPanel pn_tkdoanhthu = new JPanel();
		pn_tkdoanhthu.setBounds(20, 40, 910, 320);
		pn_thongke.add(pn_tkdoanhthu);
		ChartPanel bddoanhthu = new ChartPanel(bieudodoanhthu[0]);
		bddoanhthu.setPreferredSize(new Dimension(900, 300));
		pn_tkdoanhthu.add(bddoanhthu);
		bddoanhthu.setLayout(null);

		//
		JPanel pn_tkusedevice = new JPanel();
		pn_tkusedevice.setBounds(20, 380, 350, 350);
		pn_thongke.add(pn_tkusedevice);
		ChartPanel bddichvu = new ChartPanel(bieudodichvu[0]);
		bddichvu.setPreferredSize(new Dimension(350, 350));

		pn_tkusedevice.add(bddichvu);

		JLabel lblNewLabel_15 = new JLabel("Thống kế doanh thu");
		lblNewLabel_15.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 16));
		lblNewLabel_15.setBounds(30, 10, 300, 30);
		pn_thongke.add(lblNewLabel_15);

		JLabel lblNewLabel_15_1 = new JLabel("Thống kế tỷ lệ sử dụng dịch vụ");
		lblNewLabel_15_1.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 16));
		lblNewLabel_15_1.setBounds(30, 350, 320, 30);
		pn_thongke.add(lblNewLabel_15_1);

		HashMap<String, Integer> serviceUsageMap = getServiceUsageCounts(conn);

// ... (các JLabel tiêu đề như lblNewLabel_15, lblNewLabel_15_1) ...
		JLabel lblNewLabel_16 = new JLabel("Tổng lần sử dụng:");
		lblNewLabel_16.setFont(new Font("Monospaced", Font.BOLD, 14));
		lblNewLabel_16.setBounds(540, 355, 320, 30); // Tăng chiều rộng của label
		pn_thongke.add(lblNewLabel_16);


// Nước lọc (danhsachDV[0])
		String tenDV0 = danhsachDV[0].getTenDichvu(); // "Nước lọc"
		int count0 = serviceUsageMap.getOrDefault(tenDV0, 0);
		this.lblNewLabel_17 = new JLabel(tenDV0 + ": " + count0);
		this.lblNewLabel_17.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.lblNewLabel_17.setBounds(663, 379 + 24*0, 250, 20); // Điều chỉnh tọa độ Y và chiều rộng
		pn_thongke.add(lblNewLabel_17);

// Snack khoai tây (danhsachDV[1])
		String tenDV1 = danhsachDV[1].getTenDichvu(); // "Snack khoai tây"
		int count1 = serviceUsageMap.getOrDefault(tenDV1, 0);
		this.lblNewLabel_17_1 = new JLabel(tenDV1 + ": " + count1);
		this.lblNewLabel_17_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.lblNewLabel_17_1.setBounds(663, 379 + 24*1, 250, 20);
		pn_thongke.add(lblNewLabel_17_1);

// Coca/Pepsi (danhsachDV[2])
		String tenDV2 = danhsachDV[2].getTenDichvu(); // "Coca/Pepsi"
		int count2 = serviceUsageMap.getOrDefault(tenDV2, 0);
		this.lblNewLabel_17_1_1 = new JLabel(tenDV2 + ": " + count2);
		this.lblNewLabel_17_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.lblNewLabel_17_1_1.setBounds(663, 379 + 24*2, 250, 20);
		pn_thongke.add(lblNewLabel_17_1_1);

// Bánh KitKat (danhsachDV[4]) - Lưu ý chỉ số mảng và tên
		String tenDV4 = danhsachDV[4].getTenDichvu(); // "Bánh KitKat"
		int count4 = serviceUsageMap.getOrDefault(tenDV4, 0);
		this.lblNewLabel_17_1_2 = new JLabel(tenDV4 + ": " + count4); // Tên biến JLabel là ...1_2
		this.lblNewLabel_17_1_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.lblNewLabel_17_1_2.setBounds(663, 379 + 24*3, 250, 20);
		pn_thongke.add(lblNewLabel_17_1_2);

// Rượu Vodka SMIRNOFF 700ML (danhsachDV[3])
		String tenDV3 = danhsachDV[3].getTenDichvu(); // "Rượu Vodka SMIRNOFF 700ML"
		int count3 = serviceUsageMap.getOrDefault(tenDV3, 0);
		this.lblNewLabel_17_1_3 = new JLabel(tenDV3 + ": " + count3);
		this.lblNewLabel_17_1_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.lblNewLabel_17_1_3.setBounds(663, 379 + 24*4, 250, 20); // Tăng chiều rộng label nếu tên dài
		pn_thongke.add(lblNewLabel_17_1_3);

// Bia Heineken 250ML (danhsachDV[6])
		String tenDV6 = danhsachDV[6].getTenDichvu(); // "Bia Heineken 250ML"
		int count6 = serviceUsageMap.getOrDefault(tenDV6, 0);
		this.lblNewLabel_17_1_4 = new JLabel(tenDV6 + ": " + count6);
		this.lblNewLabel_17_1_4.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.lblNewLabel_17_1_4.setBounds(663, 379 + 24*5, 250, 20);
		pn_thongke.add(lblNewLabel_17_1_4);

// Vang Ý Mango Tropical 750ML (danhsachDV[7])
		String tenDV7 = danhsachDV[7].getTenDichvu(); // "Vang Ý Mango Tropical 750ML"
		int count7 = serviceUsageMap.getOrDefault(tenDV7, 0);
		this.lblNewLabel_17_1_5 = new JLabel(tenDV7 + ": " + count7);
		this.lblNewLabel_17_1_5.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.lblNewLabel_17_1_5.setBounds(663, 379 + 24*6, 250, 20);
		pn_thongke.add(lblNewLabel_17_1_5);

// Chivas Regal 18 Gold Signature 700ML (danhsachDV[8])
		String tenDV8 = danhsachDV[8].getTenDichvu(); // "Chivas Regal 18 Gold Signature 700ML"
		int count8 = serviceUsageMap.getOrDefault(tenDV8, 0);
		this.lblNewLabel_17_1_6 = new JLabel(tenDV8 + ": " + count8);
		this.lblNewLabel_17_1_6.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.lblNewLabel_17_1_6.setBounds(663, 379 + 24*7, 280, 20); // Tăng chiều rộng
		pn_thongke.add(lblNewLabel_17_1_6);

// Các dịch vụ tiện ích (từ danhsachDV[9] đến danhsachDV[16])
// Cho thuê xe tự lái (danhsachDV[9])
		String tenDV9 = danhsachDV[9].getTenDichvu();
		int count9 = serviceUsageMap.getOrDefault(tenDV9, 0);
		this.lblNewLabel_17_1_4_1 = new JLabel(tenDV9 + ": " + count9);
		this.lblNewLabel_17_1_4_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.lblNewLabel_17_1_4_1.setBounds(663, 379 + 24*8, 250, 20);
		pn_thongke.add(lblNewLabel_17_1_4_1);

// Dùng điểm tâm (danhsachDV[10])
		String tenDV10 = danhsachDV[10].getTenDichvu();
		int count10 = serviceUsageMap.getOrDefault(tenDV10, 0);
		this.lblNewLabel_17_1_4_2 = new JLabel(tenDV10 + ": " + count10);
		this.lblNewLabel_17_1_4_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.lblNewLabel_17_1_4_2.setBounds(663, 379 + 24*9, 250, 20);
		pn_thongke.add(lblNewLabel_17_1_4_2);

// Đưa đón sân bay (danhsachDV[11])
		String tenDV11 = danhsachDV[11].getTenDichvu();
		int count11 = serviceUsageMap.getOrDefault(tenDV11, 0);
		this.lblNewLabel_17_1_4_3 = new JLabel(tenDV11 + ": " + count11);
		this.lblNewLabel_17_1_4_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.lblNewLabel_17_1_4_3.setBounds(663, 379 + 24*10, 250, 20);
		pn_thongke.add(lblNewLabel_17_1_4_3);

// Trông trẻ (danhsachDV[12])
		String tenDV12 = danhsachDV[12].getTenDichvu();
		int count12 = serviceUsageMap.getOrDefault(tenDV12, 0);
		this.lblNewLabel_17_1_4_4 = new JLabel(tenDV12 + ": " + count12);
		this.lblNewLabel_17_1_4_4.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.lblNewLabel_17_1_4_4.setBounds(663, 379 + 24*11, 250, 20);
		pn_thongke.add(lblNewLabel_17_1_4_4);

// Giặt ủi (danhsachDV[14])
		String tenDV14 = danhsachDV[14].getTenDichvu();
		int count14 = serviceUsageMap.getOrDefault(tenDV14, 0);
		this.lblNewLabel_17_1_4_5 = new JLabel(tenDV14 + ": " + count14);
		this.lblNewLabel_17_1_4_5.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.lblNewLabel_17_1_4_5.setBounds(663, 379 + 24*12, 250, 20);
		pn_thongke.add(lblNewLabel_17_1_4_5);

// Tuần trăng mật (danhsachDV[13])
		String tenDV13 = danhsachDV[13].getTenDichvu();
		int count13 = serviceUsageMap.getOrDefault(tenDV13, 0);
		this.lblNewLabel_17_1_4_6 = new JLabel(tenDV13 + ": " + count13);
		this.lblNewLabel_17_1_4_6.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.lblNewLabel_17_1_4_6.setBounds(663, 379 + 24*13, 250, 20);
		pn_thongke.add(lblNewLabel_17_1_4_6);

// Spa (danhsachDV[15])
		String tenDV15 = danhsachDV[15].getTenDichvu();
		int count15 = serviceUsageMap.getOrDefault(tenDV15, 0);
		this.lblNewLabel_17_1_4_7 = new JLabel(tenDV15 + ": " + count15);
		this.lblNewLabel_17_1_4_7.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.lblNewLabel_17_1_4_7.setBounds(663, 379 + 24*14, 250, 20);
		pn_thongke.add(lblNewLabel_17_1_4_7);

// Fitness (danhsachDV[16])
		String tenDV16 = danhsachDV[16].getTenDichvu();
		int count16 = serviceUsageMap.getOrDefault(tenDV16, 0);
		this.lblNewLabel_17_1_4_8 = new JLabel(tenDV16 + ": " + count16);
		this.lblNewLabel_17_1_4_8.setFont(new Font("Tahoma", Font.PLAIN, 13));
// Điều chỉnh Y cho label cuối cùng này, đảm bảo nó không chồng chéo hoặc ra ngoài panel
		this.lblNewLabel_17_1_4_8.setBounds(663, 379 + 24*15, 250, 20);
		pn_thongke.add(lblNewLabel_17_1_4_8);
		JButton btcapnhatsolieu = new JButton("Cập Nhật Số Liệu");
		btcapnhatsolieu.setFont(new Font("Tahoma", Font.BOLD, 14));
		btcapnhatsolieu.setBounds(400, 680, 200, 50);
		pn_thongke.add(btcapnhatsolieu);
		// Trong ActionListener của btcapnhatsolieu
		btcapnhatsolieu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btcapnhatsolieu.setText("Đang tải dữ liệu...");
				btcapnhatsolieu.setEnabled(false);

				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						// Giả lập thời gian tải
						Thread.sleep(1000); // Giảm thời gian giả lập nếu muốn

						// Tạo lại biểu đồ trong nền
						bieudodoanhthu[0] = thongkedoanhthu.createRevenueChart(conn); // Sử dụng this.conn
						bieudodichvu[0] = thongkedichvu.createServiceChart(conn);   // Sử dụng this.conn

						return null;
					}

					@Override
					protected void done() {
						// Cập nhật UI sau khi dữ liệu xong
						pn_tkdoanhthu.removeAll();
						ChartPanel bddoanhthu = new ChartPanel(bieudodoanhthu[0]);
						bddoanhthu.setPreferredSize(new Dimension(900, 300));
						pn_tkdoanhthu.add(bddoanhthu);

						pn_tkusedevice.removeAll();
						ChartPanel bddichvu = new ChartPanel(bieudodichvu[0]);
						bddichvu.setPreferredSize(new Dimension(350, 340)); // Kích thước cũ là 350, 350
						pn_tkusedevice.add(bddichvu);

						// GỌI PHƯƠNG THỨC CẬP NHẬT CHÚ THÍCH DỊCH VỤ
						capNhatChuThichDichVu(conn); // Sử dụng this.conn

						pn_thongke.revalidate();
						pn_thongke.repaint();

						btcapnhatsolieu.setText("Cập Nhật Số Liệu");
						btcapnhatsolieu.setEnabled(true);
					}
				};
				worker.execute();
			}
		});

		pn_trangchu.add(lbanhtrangchu);

		JPanel pn_baocao = new JPanel();
		pn_hoatdong.add(pn_baocao, "báo cáo");
		bt_baocao.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cardhd.show(pn_hoatdong, "báo cáo");
			}
		});
		pn_baocao.setLayout(null);

		JPanel pn_xuatbaocao = new JPanel();
		pn_xuatbaocao.setBounds(10, 10, 927, 408);
		pn_baocao.add(pn_xuatbaocao);
		pn_xuatbaocao.setLayout(new GridLayout(1, 0, 0, 0));

		table = new JTable();
		Object data[][] = null;
		String cl[] = { "MÃ ĐẶT PHÒNG", "MÃ KHÁCH HÀNG", "MÃ PHÒNG", "NGÀY GIỜ VÀO PHÒNG", "NGÀY GIỜ TRẢ PHÒNG" };
		df = new DefaultTableModel(data, cl);
		table.setModel(df);
		JScrollPane sc = new JScrollPane(table);
		try {
			baocao();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		pn_xuatbaocao.add(sc);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 428, 927, 84);
		pn_baocao.add(panel_1);
		panel_1.setLayout(null);

		JButton btnNewButton = new JButton("Xuất file xml");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton.setBounds(384, 10, 145, 64);
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					try {
						// Tạo timestamp
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
						LocalDateTime now = LocalDateTime.now();
						String timestamp = dtf.format(now);

						// Tạo tên file với timestamp và thư mục đích
						File directory = new File("D:\\KyHai2024.2025\\DoAnCoSo1\\DACS1\\HomeStayXML");

						// Tạo thư mục nếu chưa tồn tại
						if (!directory.exists()) {
							directory.mkdir();
						}

						String fileName = "row_" + timestamp + ".xml";
						String filePath = "D:\\KyHai2024.2025\\DoAnCoSo1\\DACS1\\HomeStayXML" + File.separator + fileName;

						XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
						XMLStreamWriter writer = outputFactory.createXMLStreamWriter(new FileWriter(filePath));

						writer.writeStartDocument();
						writer.writeStartElement("row");

						for (int column = 0; column < table.getColumnCount(); column++) {
							Object value = table.getValueAt(selectedRow, column);
							writer.writeStartElement(table.getColumnName(column));
							writer.writeCharacters(value.toString());
							writer.writeEndElement();
						}

						writer.writeEndElement();
						writer.writeEndDocument();

						writer.flush();
						writer.close();
						JOptionPane.showMessageDialog(null, "Xuất file thành công");
					} catch (XMLStreamException | IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		JButton btnXemHoaDon = new JButton("Xem Hoá Đơn");
		btnXemHoaDon.setFont(new Font("Tahoma", Font.PLAIN, 14));
		// Position it next to the "Xuất file xml" button.
		// btnNewButton is at (384, 10, 145, 64)
		btnXemHoaDon.setBounds(btnNewButton.getX() + btnNewButton.getWidth() + 10, 10, 145, 64); // Adjust X coordinate and width as needed
		panel_1.add(btnXemHoaDon);

		// Bên trong constructor ManagerUI(), trong phần khởi tạo pn_baocao:

// ... (code khởi tạo table và df của bạn) ...

		btnXemHoaDon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow(); // Sử dụng 'table' là JTable trong pn_baocao

				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(ManagerUI.this,
							"Vui lòng chọn một đặt phòng từ bảng để xem hoá đơn.",
							"Chưa Chọn Đặt Phòng",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				// Giả định chỉ số cột:
				// Cột 0: MÃ ĐẶT PHÒNG (madp)
				// Cột 4: NGÀY GIỜ TRẢ PHÒNG (chuỗi cần chuyển thành timestamp)
				final int COLUMN_INDEX_MADP = 0;
				final int COLUMN_INDEX_NGAYGIOTP = 3;

				Object madpObj = table.getValueAt(selectedRow, COLUMN_INDEX_MADP);
				Object ngayGioVaoPhongObj = table.getValueAt(selectedRow, COLUMN_INDEX_NGAYGIOTP);

				// Kiểm tra null trước khi gọi toString()
				if (madpObj == null) {
					JOptionPane.showMessageDialog(ManagerUI.this,
							"Dữ liệu Mã Đặt Phòng không hợp lệ ở hàng đã chọn (rỗng).",
							"Lỗi dữ liệu",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				String madp = madpObj.toString(); // madp này hiện tại không được dùng để tạo tên file
				// trong displayInvoiceDialog(String billTimestamp)
				// nhưng vẫn có thể hữu ích nếu bạn muốn truyền nó vào hàm đó
				// để hiển thị trên tiêu đề dialog (cần sửa lại hàm displayInvoiceDialog)


				if (ngayGioVaoPhongObj == null) {
					JOptionPane.showMessageDialog(ManagerUI.this,
							"Dữ liệu Ngày Giờ Trả Phòng không hợp lệ ở hàng đã chọn (rỗng).",
							"Lỗi dữ liệu",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				String ngayGioVaoPhongStr = ngayGioVaoPhongObj.toString();

				// --- Phần kiểm tra thanh toán (giữ lại từ code gốc của bạn nếu cần) ---
				boolean paymentExists = false;
				try (Connection tempConn = connectdatabase.getConnection();
					 PreparedStatement checkStmt = tempConn.prepareStatement("SELECT COUNT(*) FROM thanhtoan WHERE MADP = ?")) {
					checkStmt.setString(1, madp); // Sử dụng madp đã lấy được
					ResultSet rsCheck = checkStmt.executeQuery();
					if (rsCheck.next() && rsCheck.getInt(1) > 0) {
						paymentExists = true;
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(ManagerUI.this,
							"Lỗi khi kiểm tra thông tin thanh toán: " + ex.getMessage(),
							"Lỗi Database",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!paymentExists) {
					JOptionPane.showMessageDialog(ManagerUI.this,
							"Chưa có hoá đơn thanh toán cho Mã Đặt Phòng: " + madp,
							"Thông Báo",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				// --- Kết thúc phần kiểm tra thanh toán ---


				String billTimestampForFile;
				try {
					// Định dạng của dữ liệu NGÀY GIỜ TRẢ PHÒNG trong JTable
					DateTimeFormatter tableDateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
					LocalDateTime localDateTime = LocalDateTime.parse(ngayGioVaoPhongStr, tableDateTimeFormatter);

					// Định dạng của timestamp trong tên file hóa đơn
					DateTimeFormatter fileTimestampFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
					billTimestampForFile = localDateTime.format(fileTimestampFormatter);

				} catch (DateTimeParseException ex) {
					JOptionPane.showMessageDialog(ManagerUI.this,
							"Lỗi định dạng ngày giờ từ bảng: '" + ngayGioVaoPhongStr + "'.\n" +
									"Định dạng kỳ vọng: HH:mm:ss dd/MM/yyyy\n" +
									"Chi tiết lỗi: " + ex.getMessage(),
							"Lỗi Định Dạng Ngày Giờ", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace(); // In lỗi ra console để debug
					return;
				}

				// Gọi hàm displayInvoiceDialog (phiên bản hiện có ở cuối file ManagerUI.java)
				// với timestamp đã được định dạng từ "NGÀY GIỜ TRẢ PHÒNG"
				displayInvoiceDialog(billTimestampForFile);
			}
		});


		// Giả sử bạn đã có object khách hàng:
		pn_trangchu.add(lbanhtrangchu);

		JPanel pn_taikhoan = new JPanel();
		pn_taikhoan.setLayout(null);
		pn_hoatdong.add(pn_taikhoan, "tài khoản");

// Xử lý chuyển card
		bt_taikhoan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardhd.show(pn_hoatdong, "tài khoản");
			}
		});

		JPanel pn_dstk = new JPanel();
		pn_dstk.setBounds(10, 10, 927, 408);
		pn_dstk.setLayout(new GridLayout(1, 0, 0, 0));
		pn_taikhoan.add(pn_dstk);

		JTable tableTK = new JTable();
		String cltk[] = {"MÃ KH", "HỌ TÊN", "CCCD", "SĐT", "EMAIL", "MẬT KHẨU", "USERNAME"};
		DefaultTableModel dftk = new DefaultTableModel(null, cltk);
		tableTK.setModel(dftk);
		JScrollPane scTK = new JScrollPane(tableTK);
		pn_dstk.add(scTK);

// Gọi hàm nạp dữ liệu tài khoản
		try {
			taikhoan(dftk);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

// Label tiêu đề
//		JTable table1 = new JTable();
//		Object data1[][] = null;
//		String cl1[] = {"MÃ KHÁCH HÀNG", "HỌ TÊN", "CCCD", "SDT","EMAIL", "PASSWORD", "USERNAME" };
//		df = new DefaultTableModel(data1, cl1);
//		table.setModel(df);
//		JScrollPane sc1 = new JScrollPane(table);
//		try {
//			taikhoan();
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

// Giờ bạn chỉ cần add pn_taikhoan vào card layout hoặc frame như bình thường
		pn_hoatdong.add(pn_taikhoan, "tài khoản");

		panel_1.add(btnNewButton);

		// add vào panel hoạt động và đặt tên
		pn_hoatdong.add(pn_p101, "phong 101");
		pn_hoatdong.add(pn_p102, "phong 102");
		pn_hoatdong.add(pn_p103, "phong 103");
		pn_hoatdong.add(pn_p104, "phong 104");
		pn_hoatdong.add(pn_p201, "phong 201");
		pn_hoatdong.add(pn_p202, "phong 202");
		pn_hoatdong.add(pn_p203, "phong 203");
		pn_hoatdong.add(pn_p204, "phong 204");
		pn_hoatdong.add(pn_p301, "phong 301");
		pn_hoatdong.add(pn_p302, "phong 302");
		pn_hoatdong.add(pn_p303, "phong 303");
		pn_hoatdong.add(pn_p304, "phong 304");

	}


	public JPanel getPn_sodophong() {
		return this.pn_sodophong;
	}

	public void setPn_sodophong(JPanel pn_sodophong) {
		this.pn_sodophong = pn_sodophong;
	}

	private static class RoundedBorder implements Border {
		private final int arcWidth;
		private final int arcHeight;
		private final Color color;

		public RoundedBorder(int arcWidth, int arcHeight, Color color) {
			this.arcWidth = arcWidth;
			this.arcHeight = arcHeight;
			this.color = color;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(color);
			g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, arcWidth, arcHeight));
			g2.dispose();
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(arcHeight / 2, arcWidth / 2, arcHeight / 2, arcWidth / 2);
		}

		@Override
		public boolean isBorderOpaque() {
			return true;
		}
	}
	// phương thức kết nối database

	public boolean CheckDPKH(String MaDP, int maphong) {
		if (MaDP.equals(key_room.get(maphong))) {
			return true;
		} else
			return false;
	}

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

	public boolean DangKy(String hoten, String cccd, String sdt, String email, String pass, String username) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// Mã hóa mật khẩu
			String salt = "asdfghjkl";
			String str = pass + salt;
			String encodedPass = Base64.getEncoder().encodeToString(str.getBytes());
			System.out.println(encodedPass);

			// Tạo kết nối đến cơ sở dữ liệu
			connection = connectdatabase.getConnection();
			String lastThreeDigits = sdt.substring(sdt.length() - 3);
			String makh = TaoMaKH(lastThreeDigits);
			// Chuẩn bị câu lệnh SQL để thêm người dùng mới
//insert ma khách hàng
			String sql = "INSERT INTO customer (MAKH, HOTEN, CCCD, SDT, EMAIL, PASS, USERNAME) VALUES (?, ?, ?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sql);

			// Gán giá trị cho các tham số
			preparedStatement.setString(1, makh);
			preparedStatement.setString(2, hoten);
			preparedStatement.setString(3, cccd);
			preparedStatement.setString(4, sdt);
			preparedStatement.setString(5, email);
			preparedStatement.setString(6, encodedPass);
			preparedStatement.setString(7, username);

			// Thực thi câu lệnh SQL
			int rowsAffected = preparedStatement.executeUpdate();

			if (rowsAffected > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;

		} finally {
			// Đóng các tài nguyên
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean KiemTraTonTai(String username, String cccd) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean isDuplicated = false;

		try {
			// Tạo kết nối đến cơ sở dữ liệu
			connection = connectdatabase.getConnection();

			// Chuẩn bị câu lệnh SQL để kiểm tra username và CCCD
			String sql = "SELECT COUNT(*) FROM customer WHERE USERNAME = ? OR CCCD = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, cccd);

			// Thực thi câu lệnh SQL
			resultSet = preparedStatement.executeQuery();

			// Kiểm tra kết quả
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				isDuplicated = count > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Đóng các tài nguyên
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return isDuplicated;
	}

	// Truy vấn dữ liệu để đăng nhập
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

	public String phantichgia(int gia) {
		String s = new String();
		int dong = 0, trieu = 0, nghin = 0, copy = gia;
		String dongs = null, trieus = null, nghins = null;
		if (copy > 0) {
			dong = copy % 1000;
			copy /= 1000;
			if (copy > 0) {
				nghin = copy % 1000;
				copy /= 1000;
				if (copy > 0) {
					trieu = copy % 1000;
				}
			}
		}
		if (trieu > 0 && trieu <= 99) {
			trieus = "0" + dong;
		} else if (trieu == 0) {
			trieus = "000";
		} else if (trieu > 99) {
			trieus = dong + "";
		}
		if (dong > 0 && dong <= 99) {
			dongs = "0" + dong;
		} else if (dong == 0) {
			dongs = "000";
		} else if (dong > 99) {
			dongs = dong + "";
		}

		if (nghin > 0 && nghin <= 99) {
			nghins = "0" + nghin;
		} else if (nghin == 0) {
			nghins = "000";
		} else if (nghin > 99) {
			nghins = nghin + "";
		}

		if (gia >= 1000000) {
			s = trieu + "." + nghins + "." + dongs + " VND";
		} else
			s = nghin + "." + dongs + " VND";
		return s;
	}

	// phương thức tạo mã khách hàng khi tạo tài khoản
	public String TaoMaKH(String soduoi) {
		LocalDateTime now = LocalDateTime.now();
		String dateTimeString = now.format(DATE_TIME_FORMATTER);
		return soduoi + dateTimeString;
	}

	public int taomaDatphong() {
		Random random = new Random();
		int randomNumber = random.nextInt(89999) + 10000;
		return randomNumber;
	}

	public void Order(int maphong, int madv, int soluong) {
		for (PhongManagerQL phongql : quanLyPhong) {
			if (phongql.phong.getId() == maphong) {
				phongql.hoatdong.dbsau.addRow(
						new Object[] { danhsachDV[madv].getTenDichvu(), phantichgia(danhsachDV[madv].getGiaca()),
								soluong, phantichgia(danhsachDV[madv].getGiaca() * soluong) });
			}
		}
	}

	public int booking(String chuoithongtin) {
		Gson tt = new Gson();
		Modelchuoi chuoi = tt.fromJson(chuoithongtin, Modelchuoi.class);
		// Xử lý đặt phòng
		int madp = taomaDatphong();
		key_room.put(chuoi.getMaphong(), madp + "");
		dulieudp.put(chuoi.getMaKhachHang(), madp);
		if (chuoi.getTrangthai() == TrangThaiPhong.CHO_XAC_NHAN) {
			for (PhongManagerQL phongql : quanLyPhong) {
				if (phongql.phong.getId() == chuoi.getMaphong()) {
					phongql.phong.setTrangThai(TrangThaiPhong.CHO_XAC_NHAN);
					phongql.xacnhan.TMadatphong.setText(madp + "");
					phongql.xacnhan.TMaKH.setText(chuoi.getMaKhachHang());
					phongql.xacnhan.THovaten.setText(chuoi.getHoVaTen());
					phongql.xacnhan.TCCCD.setText(chuoi.getCccd());
					phongql.xacnhan.TSdth.setText(chuoi.getSdt());
					if (chuoi.isChoThueXe())
						phongql.xacnhan.db
								.addRow(new Object[] { "Cho thuê xe tự lái", phantichgia(danhsachDV[9].getGiaca()) });
					if (chuoi.isDuaDonSanBay())
						phongql.xacnhan.db
								.addRow(new Object[] { "Đưa đón sân bay", phantichgia(danhsachDV[11].getGiaca()) });
					if (chuoi.isDungDiemTam())
						phongql.xacnhan.db
								.addRow(new Object[] { "Dùng điểm tâm", phantichgia(danhsachDV[10].getGiaca()) });
					if (chuoi.isTrongTre())
						phongql.xacnhan.db.addRow(new Object[] { "Trông trẻ", phantichgia(danhsachDV[12].getGiaca()) });
					if (chuoi.isTuanTrangMat())
						phongql.xacnhan.db
								.addRow(new Object[] { "Tuần trăng mật", phantichgia(danhsachDV[13].getGiaca()) });
					if (chuoi.isGiatui())
						phongql.xacnhan.db.addRow(new Object[] { "Giặt ủi", phantichgia(danhsachDV[14].getGiaca()) });
					if (chuoi.isSpa())
						phongql.xacnhan.db.addRow(new Object[] { "Spa", phantichgia(danhsachDV[15].getGiaca()) });
					if (chuoi.isFitness())
						phongql.xacnhan.db.addRow(new Object[] { "Fitness", phantichgia(danhsachDV[16].getGiaca()) });
				}
			}
		} else if (chuoi.getTrangthai() == TrangThaiPhong.DANG_HOAT_DONG) {
			for (PhongManagerQL phongql : quanLyPhong) {
				if (phongql.phong.getId() == chuoi.getMaphong()) {
					phongql.phong.setTrangThai(TrangThaiPhong.DANG_HOAT_DONG);
					phongql.hoatdong.TMadatphong.setText(madp + "");
					phongql.hoatdong.TMaKH.setText(chuoi.getMaKhachHang());
					phongql.hoatdong.THovaten.setText(chuoi.getHoVaTen());
					phongql.hoatdong.TCCCD.setText(chuoi.getCccd());
					phongql.hoatdong.TSDTH.setText(chuoi.getSdt());
					phongql.hoatdong.TNgayGioNHanPhong.setText(chuoi.getngaygiovaophong());
					if (chuoi.isChoThueXe())
						phongql.hoatdong.db
								.addRow(new Object[] { "Cho thuê xe tự lái", phantichgia(danhsachDV[9].getGiaca()) });
					if (chuoi.isDuaDonSanBay())
						phongql.hoatdong.db
								.addRow(new Object[] { "Đưa đón sân bay", phantichgia(danhsachDV[11].getGiaca()) });
					if (chuoi.isDungDiemTam())
						phongql.hoatdong.db
								.addRow(new Object[] { "Dùng điểm tâm", phantichgia(danhsachDV[10].getGiaca()) });
					if (chuoi.isTrongTre())
						phongql.hoatdong.db
								.addRow(new Object[] { "Trông trẻ", phantichgia(danhsachDV[12].getGiaca()) });
					if (chuoi.isTuanTrangMat())
						phongql.hoatdong.db
								.addRow(new Object[] { "Tuần trăng mật", phantichgia(danhsachDV[13].getGiaca()) });
					if (chuoi.isGiatui())
						phongql.hoatdong.db.addRow(new Object[] { "Giặt ủi", phantichgia(danhsachDV[14].getGiaca()) });
					if (chuoi.isSpa())
						phongql.hoatdong.db.addRow(new Object[] { "Spa", phantichgia(danhsachDV[15].getGiaca()) });
					if (chuoi.isFitness())
						phongql.hoatdong.db.addRow(new Object[] { "Fitness", phantichgia(danhsachDV[16].getGiaca()) });
				}
			}
		}

		return madp;
	}

	public void xacnhan(int maphong, String tgian) {
		for (PhongManagerQL phongql : quanLyPhong) {
			if (phongql.phong.getId() == maphong) {
				phongql.xacnhan.datphong(phongql.hoatdong, phongql.phong, tgian);
			}
		}
	}

	public void cancel(int maphong) {
		for (PhongManagerQL phongql : quanLyPhong) {
			if (phongql.phong.getId() == maphong) {
				phongql.xacnhan.huyphong(phongql.phong, this);
			}
		}
	}

	// tao modelchuoi để lưu

	public static int convert(String input) {
		// Loại bỏ ký tự "VND" và dấu chấm phẩy
		String cleanedInput = input.replaceAll("[^0-9]", "");

		// Chuyển đổi chuỗi thành số nguyên kiểu int
		int value = Integer.parseInt(cleanedInput);
		return value;

	}

	public String truyenthongtin(String username) {
		try (Connection connection = connectdatabase.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT MAKH, HOTEN, CCCD, SDT, EMAIL FROM customer WHERE USERNAME = ?");) {

			statement.setString(1, username);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				String makh = resultSet.getString("MAKH");
				String hoten = resultSet.getString("HOTEN");
				String cccd = resultSet.getString("CCCD");
				String sdt = resultSet.getString("SDT");
				String email = resultSet.getString("EMAIL");

				// Tạo đối tượng ModelKhachHang và đặt các thuộc tính
				ModelKhachHang khachHang = new ModelKhachHang();
				khachHang.setMakhachhang(makh);
				khachHang.setHoten(hoten);
				khachHang.setCccd(cccd);
				khachHang.setSdt(sdt);
				khachHang.setEmail(email);

				// Sử dụng Gson để chuyển đối tượng thành chuỗi JSON
				Gson gson = new Gson();
				String thongtin = gson.toJson(khachHang);

				// In chuỗi JSON ra console hoặc xử lý theo yêu cầu

				return thongtin;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// Lớp xử lý biểu đồ doanh thu
	// Lớp xử lý biểu đồ doanh thu
	public static class Thongkedoanhthu {

		public JFreeChart createRevenueChart(Connection conn) {
			TimeSeries series = new TimeSeries("Doanh thu");

			try {
				// Truy vấn dữ liệu từ cơ sở dữ liệu
				String query = "SELECT NGAYGIOTT AS NGAY, SUM(TONGCHIPHI) AS DOANHTHU " + "FROM thanhtoan "
						+ "INNER JOIN datphong ON thanhtoan.MADP = datphong.MADP " + "GROUP BY NGAYGIOTT "
						+ "ORDER BY NGAYGIOTT";
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);

				// Thêm dữ liệu vào series
				SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
				while (rs.next()) {
					String ngay = rs.getString("NGAY");
					double doanhthu = rs.getDouble("DOANHTHU");

					try {
						java.util.Date date = dateFormat.parse(ngay);
						series.addOrUpdate(new Day(date), doanhthu);
					} catch (java.text.ParseException e) {
						e.printStackTrace();
					}
				}

				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Tạo dataset từ series
			TimeSeriesCollection dataset = new TimeSeriesCollection();
			dataset.addSeries(series);

			// Tạo biểu đồ doanh thu
			JFreeChart chart = ChartFactory.createTimeSeriesChart("Doanh thu theo ngày", "Ngày", "Tiền", dataset, true,
					true, false);

			// Lấy trục x của biểu đồ
			XYPlot plot = (XYPlot) chart.getPlot();
			DateAxis axis = (DateAxis) plot.getDomainAxis();

			// Đặt định dạng ngày cho trục x
			axis.setDateFormatOverride(new SimpleDateFormat("dd/MM/yyyy"));
			// Lấy trục y của biểu đồ

			NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();

			// Đặt tiêu đề cho trục y
			yAxis.setLabel("Doanh thu (VND)");

			// Đặt định dạng số cho trục y
			DecimalFormat format = new DecimalFormat("#,###");
			yAxis.setNumberFormatOverride(format);

			// Đặt khoảng cách giữa các giá trị trên trục y
			yAxis.setTickUnit(new NumberTickUnit(100000000)); // Khoảng cách 500,000 VND

			// Đặt giá trị tối thiểu và tối đa cho trục y (nếu cần)
			yAxis.setRange(0, 1000000000); // Từ 0 đến 100,000,000 VND

			return chart;
		}
	}

	// Lớp xử lý biểu đồ sử dụng dịch vụ
	public static class Thongkedichvu {
		public static JFreeChart createServiceChart(Connection conn) {
			DefaultPieDataset dataset = new DefaultPieDataset();

			try {
				// Truy vấn dữ liệu từ cơ sở dữ liệu
				String query = "SELECT dv.TENDV, SUM(sdv.SLDV) AS SDDICHVU " + "FROM sudungthemdichvu sdv "
						+ "INNER JOIN dichvu dv ON sdv.MADV = dv.MADV " + "GROUP BY dv.TENDV";
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery(query);

				// Tính tổng số lượng dịch vụ
				int totalServices = 0;
				while (rs.next()) {
					int sddv = rs.getInt("SDDICHVU");
					totalServices += sddv;
				}

				// Quay lại đầu kết quả và thêm dữ liệu vào dataset
				rs.beforeFirst();
				while (rs.next()) {
					String tendv = rs.getString("TENDV");
					int sddv = rs.getInt("SDDICHVU");
					double percentage = (double) sddv / totalServices * 100;
					dataset.setValue(tendv + " (" + String.format("%.2f", percentage) + "%)", sddv);
				}

				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Tạo biểu đồ sử dụng dịch vụ
			JFreeChart chart = ChartFactory.createPieChart("Tỷ lệ sử dụng dịch vụ", dataset, true, true, false);

			return chart;
		}
	}

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

		            df.addRow(new Object[]{madp, makh, maphong, ngayvao, ngayra});
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
	}

	private void filterPhongTheoTrangThai(TrangThaiPhong trangThaiCanLoc) {
		if (quanLyPhong == null) return; // Kiểm tra null
		for (PhongManagerQL phongManager : quanLyPhong) {
			if (phongManager.phong.getTrangThai() == trangThaiCanLoc) {
				phongManager.panelPhong.setVisible(true); // phongPanel là JPanel của phòng trong PhongManagerQL
			} else {
				phongManager.panelPhong.setVisible(false);
			}
		}
		this.pn_sodophong.revalidate();
		this.pn_sodophong.repaint();
	}

	private void hienThiTatCaPhong() {
		if (quanLyPhong == null) return; // Kiểm tra null
		for (PhongManagerQL phongManager : quanLyPhong) {
			phongManager.panelPhong.setVisible(true);
		}
		this.pn_sodophong.revalidate();
		this.pn_sodophong.repaint();
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

	private HashMap<String, Integer> getServiceUsageCounts(Connection conn) {
		HashMap<String, Integer> serviceCounts = new HashMap<>();
		// Câu truy vấn này giống với câu truy vấn trong Thongkedichvu.createServiceChart
		// Chỉ lấy TENDV và tổng SLDV
		String query = "SELECT dv.TENDV, SUM(sdv.SLDV) AS TOTAL_USAGE " +
				"FROM sudungthemdichvu sdv " +
				"INNER JOIN dichvu dv ON sdv.MADV = dv.MADV " +
				"GROUP BY dv.TENDV";
		try (Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				serviceCounts.put(rs.getString("TENDV"), rs.getInt("TOTAL_USAGE"));
			}
		} catch (SQLException e) {
			System.err.println("Lỗi khi lấy số lượt sử dụng dịch vụ: " + e.getMessage());
			// Trong trường hợp lỗi, serviceCounts sẽ trống hoặc không đầy đủ
		}
		return serviceCounts;
	}

	private void capNhatChuThichDichVu(Connection conn) {
		HashMap<String, Integer> serviceUsageMap = getServiceUsageCounts(conn);

		// Nước lọc (danhsachDV[0])
		String tenDV0 = danhsachDV[0].getTenDichvu();
		int count0 = serviceUsageMap.getOrDefault(tenDV0, 0);
		if (this.lblNewLabel_17 != null) this.lblNewLabel_17.setText(tenDV0 + ": " + count0);

		// Snack khoai tây (danhsachDV[1])
		String tenDV1 = danhsachDV[1].getTenDichvu();
		int count1 = serviceUsageMap.getOrDefault(tenDV1, 0);
		if (this.lblNewLabel_17_1 != null) this.lblNewLabel_17_1.setText(tenDV1 + ": " + count1);

		// Coca/Pepsi (danhsachDV[2])
		String tenDV2 = danhsachDV[2].getTenDichvu();
		int count2 = serviceUsageMap.getOrDefault(tenDV2, 0);
		if (this.lblNewLabel_17_1_1 != null) this.lblNewLabel_17_1_1.setText(tenDV2 + ": " + count2);

		// Bánh KitKat (danhsachDV[4]) - Kiểm tra lại chỉ số nếu cần
		String tenDV4 = danhsachDV[4].getTenDichvu();
		int count4 = serviceUsageMap.getOrDefault(tenDV4, 0);
		if (this.lblNewLabel_17_1_2 != null) this.lblNewLabel_17_1_2.setText(tenDV4 + ": " + count4);

		// Rượu Vodka SMIRNOFF 700ML (danhsachDV[3])
		String tenDV3 = danhsachDV[3].getTenDichvu();
		int count3 = serviceUsageMap.getOrDefault(tenDV3, 0);
		if (this.lblNewLabel_17_1_3 != null) this.lblNewLabel_17_1_3.setText(tenDV3 + ": " + count3);

		// Bia Heineken 250ML (danhsachDV[6])
		String tenDV6 = danhsachDV[6].getTenDichvu();
		int count6 = serviceUsageMap.getOrDefault(tenDV6, 0);
		if (this.lblNewLabel_17_1_4 != null) this.lblNewLabel_17_1_4.setText(tenDV6 + ": " + count6);

		// Vang Ý Mango Tropical 750ML (danhsachDV[7])
		String tenDV7 = danhsachDV[7].getTenDichvu();
		int count7 = serviceUsageMap.getOrDefault(tenDV7, 0);
		if (this.lblNewLabel_17_1_5 != null) this.lblNewLabel_17_1_5.setText(tenDV7 + ": " + count7);

		// Chivas Regal 18 Gold Signature 700ML (danhsachDV[8])
		String tenDV8 = danhsachDV[8].getTenDichvu();
		int count8 = serviceUsageMap.getOrDefault(tenDV8, 0);
		if (this.lblNewLabel_17_1_6 != null) this.lblNewLabel_17_1_6.setText(tenDV8 + ": " + count8);

		// Cho thuê xe tự lái (danhsachDV[9])
		String tenDV9 = danhsachDV[9].getTenDichvu();
		int count9 = serviceUsageMap.getOrDefault(tenDV9, 0);
		if (this.lblNewLabel_17_1_4_1 != null) this.lblNewLabel_17_1_4_1.setText(tenDV9 + ": " + count9);

		// Dùng điểm tâm (danhsachDV[10])
		String tenDV10 = danhsachDV[10].getTenDichvu();
		int count10 = serviceUsageMap.getOrDefault(tenDV10, 0);
		if (this.lblNewLabel_17_1_4_2 != null) this.lblNewLabel_17_1_4_2.setText(tenDV10 + ": " + count10);

		// Đưa đón sân bay (danhsachDV[11])
		String tenDV11 = danhsachDV[11].getTenDichvu();
		int count11 = serviceUsageMap.getOrDefault(tenDV11, 0);
		if (this.lblNewLabel_17_1_4_3 != null) this.lblNewLabel_17_1_4_3.setText(tenDV11 + ": " + count11);

		// Trông trẻ (danhsachDV[12])
		String tenDV12 = danhsachDV[12].getTenDichvu();
		int count12 = serviceUsageMap.getOrDefault(tenDV12, 0);
		if (this.lblNewLabel_17_1_4_4 != null) this.lblNewLabel_17_1_4_4.setText(tenDV12 + ": " + count12);

		// Giặt ủi (danhsachDV[14])
		String tenDV14 = danhsachDV[14].getTenDichvu();
		int count14 = serviceUsageMap.getOrDefault(tenDV14, 0);
		if (this.lblNewLabel_17_1_4_5 != null) this.lblNewLabel_17_1_4_5.setText(tenDV14 + ": " + count14);

		// Tuần trăng mật (danhsachDV[13])
		String tenDV13 = danhsachDV[13].getTenDichvu();
		int count13 = serviceUsageMap.getOrDefault(tenDV13, 0);
		if (this.lblNewLabel_17_1_4_6 != null) this.lblNewLabel_17_1_4_6.setText(tenDV13 + ": " + count13);

		// Spa (danhsachDV[15])
		String tenDV15 = danhsachDV[15].getTenDichvu();
		int count15 = serviceUsageMap.getOrDefault(tenDV15, 0);
		if (this.lblNewLabel_17_1_4_7 != null) this.lblNewLabel_17_1_4_7.setText(tenDV15 + ": " + count15);

		// Fitness (danhsachDV[16])
		String tenDV16 = danhsachDV[16].getTenDichvu();
		int count16 = serviceUsageMap.getOrDefault(tenDV16, 0);
		if (this.lblNewLabel_17_1_4_8 != null) this.lblNewLabel_17_1_4_8.setText(tenDV16 + ": " + count16);

		// if (pn_thongke != null) { // pn_thongke đã được revalidate và repaint trong SwingWorker
		//     pn_thongke.revalidate();
		//     pn_thongke.repaint();
		// }
	}
	public void capNhatSauKhiTraPhong(int maPhongDaTra) {
		for (PhongManagerQL phongManager : quanLyPhong) {
			if (phongManager.phong.getId() == maPhongDaTra) {
				if (phongManager.hoatdong != null) { // 'hoatdong' ở đây là instance của pn_DanghoatdongQL
					phongManager.hoatdong.xoaform(); // << GỌI xoaform() TẠI ĐÂY
				}
				// Thread PhongManagerQL sẽ tự động cập nhật màu sắc của phòng trên sơ đồ
				// dựa vào trạng thái mới của đối tượng phong.
				break;
			}
		}
		// Cập nhật lại bảng báo cáo nếu cần
		try {
			if (df != null) { // df là DefaultTableModel của bảng báo cáo
				df.setRowCount(0); // Xóa dữ liệu cũ
				baocao();          // Nạp lại dữ liệu báo cáo mới
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}


	private void displayInvoiceDialog(String timestamp) {
		// Tạo một JDialog mới để hiển thị hoá đơn.
		// Tiêu đề vẫn có thể bao gồm madp để dễ nhận biết
		JDialog invoiceDialog = new JDialog(this, "Hoá Đơn - Bill: " + timestamp );
		invoiceDialog.setSize(700, 800); // Kích thước của dialog
		invoiceDialog.setLocationRelativeTo(this); // Hiển thị dialog ở giữa cửa sổ cha
		invoiceDialog.setLayout(new BorderLayout(10, 10));

		// Panel chính chứa nội dung hoá đơn
		JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		// JTextArea để hiển thị văn bản hoá đơn, không cho phép chỉnh sửa
		JTextArea invoiceTextArea = new JTextArea();
		invoiceTextArea.setEditable(false);
		invoiceTextArea.setFont(new Font("Monospaced", Font.PLAIN, 13)); // Font đơn cách cho hoá đơn

		// StringBuilder để đọc nội dung từ file
		StringBuilder fileContentBuilder = new StringBuilder();

		// --- Logic đọc file hoá đơn dựa trên timestamp ---
		String directoryPath = "D:\\KyHai2024.2025\\DoAnCoSo1\\DACS1\\Bill_Homestay"; // Đường dẫn thư mục của bạn
		// Tên file được xây dựng CHỈ từ billTimestamp theo yêu cầu
		String fileName = "Bill_" + timestamp + ".txt";
		File invoiceFile = new File(directoryPath, fileName);

		if (invoiceFile.exists() && invoiceFile.isFile() && invoiceFile.canRead()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(invoiceFile))) {
				String line;
				while ((line = reader.readLine()) != null) {
					fileContentBuilder.append(line).append("\n");
				}
				invoiceTextArea.setText(fileContentBuilder.toString());
			} catch (IOException e_io) {
				String errorMessage = "Lỗi khi đọc file hoá đơn: " + invoiceFile.getName() + "\n" +
						"Chi tiết: " + e_io.getMessage();
				invoiceTextArea.setText(errorMessage);
				e_io.printStackTrace(); // In lỗi ra console để debug
				JOptionPane.showMessageDialog(invoiceDialog,
						"Không thể đọc được nội dung file hoá đơn.\n" + e_io.getMessage(),
						"Lỗi Đọc File", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			String errorMessage = "Không tìm thấy file hoá đơn hoặc không thể truy cập:\n" +
					invoiceFile.getAbsolutePath() + "\n" +
					"(Đảm bảo file được lưu với tên " + fileName + ")";
			invoiceTextArea.setText(errorMessage);
			JOptionPane.showMessageDialog(invoiceDialog,
					errorMessage,
					"Lỗi File Không Tồn Tại", JOptionPane.ERROR_MESSAGE);
		}
		// Đặt con trỏ về đầu văn bản
		invoiceTextArea.setCaretPosition(0);

		// Thêm JTextArea vào JScrollPane để có thể cuộn nếu nội dung dài
		JScrollPane scrollPane = new JScrollPane(invoiceTextArea);
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		// Tạo nút "Đóng"
		JButton btnOk = new JButton("Đóng");
		btnOk.addActionListener(e -> invoiceDialog.dispose()); // Khi nhấn nút, đóng dialog

		// Panel chứa nút "Đóng", căn phải
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.add(btnOk);

		// Thêm panel nội dung và panel nút vào dialog
		invoiceDialog.add(contentPanel, BorderLayout.CENTER);
		invoiceDialog.add(southPanel, BorderLayout.SOUTH);

		// Hiển thị dialog hoá đơn
		invoiceDialog.setVisible(true);
	}
}