package controller;
import java.awt.*;

import javax.swing.JPanel;

import Model.Phong;
import Model.Phong.TrangThaiPhong; // Thêm import này
import View.pn_ChoxacnhanQL;
import View.pn_DanghoatdongQL;
import View.pn_DatphongQL;

public class PhongManagerQL extends Thread {
    public Phong phong;
    public JPanel panelPhong;
    public pn_DatphongQL datphong;
    public pn_ChoxacnhanQL xacnhan;
    public pn_DanghoatdongQL hoatdong;
    private JPanel panelhienthi;
    private CardLayout layout;

    public PhongManagerQL(Phong phong, JPanel panelPhongTrenSoDo, CardLayout layoutCard, pn_DatphongQL datphongCard, pn_ChoxacnhanQL xacnhanCard,
                          pn_DanghoatdongQL hoatdongCard, JPanel panelChuaCardLayout) {
        this.phong = phong;
        this.panelPhong = panelPhongTrenSoDo;
        this.layout = layoutCard;
        this.datphong = datphongCard;
        this.xacnhan = xacnhanCard;
        this.hoatdong = hoatdongCard;
        this.panelhienthi = panelChuaCardLayout;
    }

    public Color colordat = new Color(205, 180, 219);
    public Color colorchoxacnhan = new Color(255, 200, 221);

    @Override
    public void run() {
        while (!isInterrupted()) {
            if (this.panelPhong == null || this.layout == null || this.panelhienthi == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    interrupt();
                }
                continue;
            }
            switch (phong.getTrangThai()) {
                case TRONG:
                    this.panelPhong.setBackground(Color.getHSBColor(0, 0, (float) 0.94));
                    this.layout.show(this.panelhienthi, "datohong" + phong.getId());
                    break;
                case DANG_HOAT_DONG:
                    this.panelPhong.setBackground(colordat);
                    this.layout.show(this.panelhienthi, "hoatdong" + phong.getId());
                    break;
                case CHO_XAC_NHAN:
                    this.panelPhong.setBackground(colorchoxacnhan);
                    this.layout.show(this.panelhienthi, "xacnhan" + phong.getId());
                    break;
                default:
                    this.panelPhong.setBackground(Color.LIGHT_GRAY);
                    break;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}