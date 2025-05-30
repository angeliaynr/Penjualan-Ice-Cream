import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import java.util.HashMap;
import java.io.File;
import java.sql.DriverManager;
import java.awt.event.*;
import java.text.DecimalFormat;


public class icecream extends javax.swing.JFrame {

    public icecream() {
        try {
            initComponents();
            load_table();
            kosong();
            
            txtjumlahbeli.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        hitung();
                    }
                }
            });

            txtpembayaran.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        hitungKembalian();
                    }
                }
            });
            
            cbpilihanrasa.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setharga();
                }
            });
            
        } catch (SQLException e) {
            Logger.getLogger(icecream.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void setharga() {
        try {
            DecimalFormat df = new DecimalFormat("#,###");
            String rasa = cbpilihanrasa.getSelectedItem().toString();

            int harga = 0;
            switch (rasa) {
                case "--PILIHAN RASA--":
                    txtharga.setText("");
                    return;
                case "Coklat":
                    harga = 12000;
                    break;
                case "Vanilla":
                    harga = 15000;
                    break;
                case "Strawberry":
                    harga = 17000;
                    break;
                case "Oreo":
                    harga = 18000;
                    break; 
                case "Caramel":
                    harga = 20000;
                    break; 
                case "Red Velvet":
                    harga = 22000;
                    break; 
                default:
                    JOptionPane.showMessageDialog(null, "Pilihan rasa tidak valid.");
                    return;
            }

            txtharga.setText(df.format(harga));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Data ini tidak valid. Pastikan semua data telah diisi dengan benar.");
        }
    }
    
   private void kosong() {
    txtidtransaksi.setText(null);
    txtnamapembeli.setText(null);
    cbpilihanrasa.setSelectedIndex(0);
    txtharga.setText(null);
    txtjumlahbeli.setText(null);
    txttotalharga.setText(null);
    txtdiskon.setText(null);
    txtppn.setText(null);
    txttotalbayar.setText(null);
    txtpembayaran.setText(null);
    txtkembalian.setText(null);
}

    private void load_table() throws SQLException {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Transaksi");
        model.addColumn("Nama");
        model.addColumn("Rasa");
        model.addColumn("Harga");
        model.addColumn("Jumlah Beli");
        model.addColumn("Total Harga");
        model.addColumn("Diskon");
        model.addColumn("PPN");
        model.addColumn("Total Bayar");
        model.addColumn("Pembayaran");
        model.addColumn("Kembalian");
        
        try {
            String sql = "SELECT * FROM delight";
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbicecream", "root", "");
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);
            
            while (res.next()) {
                model.addRow(new Object[]{
                    res.getString("id_transaksi"),
                    res.getString("nama"),
                    res.getString("rasa"),
                    res.getString("harga"),
                    res.getString("jumlah_beli"),
                    res.getString("total_harga"),
                    res.getString("diskon"),
                    res.getString("ppn"),
                    res.getString("total_bayar"),
                    res.getString("pembayaran"),
                    res.getString("kembalian")
                });
            }
            jTable1.setModel(model);
        } catch (SQLException e) {
            Logger.getLogger(icecream.class.getName()).log(Level.SEVERE, null, e);
        }
    }



private void hitung() {
        try {
            String hargaText = txtharga.getText().replace(",", "");
            double harga = Double.parseDouble(hargaText);
            int jumlahbeli = Integer.parseInt(txtjumlahbeli.getText());
            double totalharga = harga * jumlahbeli;
            double diskon = 0;
            double ppn = 0.1 * totalharga;

            if (totalharga >= 50000) {
                diskon = totalharga * 0.1;
            }

            double totalbayar = totalharga - diskon + ppn;

            txttotalharga.setText(String.valueOf(totalharga));
            txtdiskon.setText(String.valueOf(diskon));
            txtppn.setText(String.valueOf(ppn));
            txttotalbayar.setText(String.valueOf(totalbayar));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input tidak valid: " + e.getMessage());
        }
    }

    private void hitungKembalian() {
        try {
            double totalbayar = Double.parseDouble(txttotalbayar.getText());
            double pembayaran = Double.parseDouble(txtpembayaran.getText());
            double kembalian = pembayaran - totalbayar;

            txtkembalian.setText(String.valueOf(kembalian));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Pembayaran tidak valid. Pastikan jumlah pembayaran sudah diisi dengan benar.");
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtidtransaksi = new javax.swing.JTextField();
        txtharga = new javax.swing.JTextField();
        txtjumlahbeli = new javax.swing.JTextField();
        txttotalharga = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtnamapembeli = new javax.swing.JTextField();
        cbpilihanrasa = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtdiskon = new javax.swing.JTextField();
        txtppn = new javax.swing.JTextField();
        txttotalbayar = new javax.swing.JTextField();
        txtpembayaran = new javax.swing.JTextField();
        txtkembalian = new javax.swing.JTextField();
        bbaru = new javax.swing.JButton();
        bsimpan = new javax.swing.JButton();
        bedit = new javax.swing.JButton();
        bhapus = new javax.swing.JButton();
        bkeluar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        bcetak = new javax.swing.JButton();
        jTextField10 = new javax.swing.JTextField();
        bcetaklaporan = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));
        jPanel1.setForeground(new java.awt.Color(255, 153, 153));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Pemesanan Ice Cream Delight");

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel2.setText("Nama Pembeli");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel3.setText("Pilihan Rasa");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel4.setText("Harga");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel5.setText("Jumlah Beli");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel6.setText("Total Harga");

        txtidtransaksi.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtidtransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidtransaksiActionPerformed(evt);
            }
        });

        txtharga.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtharga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthargaActionPerformed(evt);
            }
        });

        txtjumlahbeli.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtjumlahbeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtjumlahbeliActionPerformed(evt);
            }
        });

        txttotalharga.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txttotalharga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotalhargaActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel12.setText("ID Transaksi");

        txtnamapembeli.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        cbpilihanrasa.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        cbpilihanrasa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--PILIHAN RASA--", "Coklat", "Vanilla", "Strawberry", "Oreo", "Caramel", "Red Velvet", " " }));
        cbpilihanrasa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbpilihanrasaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)))
                    .addComponent(jLabel12)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txttotalharga)
                    .addComponent(txtidtransaksi)
                    .addComponent(txtharga)
                    .addComponent(txtnamapembeli)
                    .addComponent(cbpilihanrasa, 0, 150, Short.MAX_VALUE)
                    .addComponent(txtjumlahbeli))
                .addGap(15, 15, 15))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtidtransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtnamapembeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbpilihanrasa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtharga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtjumlahbeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txttotalharga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(23, Short.MAX_VALUE))))
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel7.setText("Diskon");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel8.setText("PPN");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel9.setText("Total Bayar");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel10.setText("Pembayaran");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel11.setText("Kembalian");

        txtdiskon.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        txtppn.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtppn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtppnActionPerformed(evt);
            }
        });

        txtpembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpembayaranActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtppn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtdiskon, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtpembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txttotalbayar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtkembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtdiskon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtppn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txttotalbayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtpembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtkembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        bbaru.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/new-document.png"))); // NOI18N
        bbaru.setText("Baru");
        bbaru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bbaruActionPerformed(evt);
            }
        });

        bsimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/diskette.png"))); // NOI18N
        bsimpan.setText("Simpan");
        bsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsimpanActionPerformed(evt);
            }
        });

        bedit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/editing.png"))); // NOI18N
        bedit.setText("Edit");
        bedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beditActionPerformed(evt);
            }
        });

        bhapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete.png"))); // NOI18N
        bhapus.setText("Hapus");
        bhapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bhapusActionPerformed(evt);
            }
        });

        bkeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logout.png"))); // NOI18N
        bkeluar.setText("Keluar");
        bkeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bkeluarActionPerformed(evt);
            }
        });

        jTable1.setBackground(new java.awt.Color(204, 204, 255));
        jTable1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        bcetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/notes.png"))); // NOI18N
        bcetak.setText("Cetak Nota");
        bcetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcetakActionPerformed(evt);
            }
        });

        bcetaklaporan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/report.png"))); // NOI18N
        bcetaklaporan.setText("Cetak Laporan");
        bcetaklaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcetaklaporanActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ice-cream.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(bcetak)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(bbaru, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(bsimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(bedit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(bhapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(bkeluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(bcetaklaporan))))
                        .addGap(0, 8, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jButton1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel1)))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(bbaru)
                        .addGap(20, 20, 20)
                        .addComponent(bsimpan)
                        .addGap(18, 18, 18)
                        .addComponent(bedit)
                        .addGap(18, 18, 18)
                        .addComponent(bhapus)
                        .addGap(18, 18, 18)
                        .addComponent(bkeluar))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bcetak)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bcetaklaporan))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtidtransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidtransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidtransaksiActionPerformed

    private void txthargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txthargaActionPerformed

    private void txtjumlahbeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtjumlahbeliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtjumlahbeliActionPerformed

    private void txtppnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtppnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtppnActionPerformed

    private void txtpembayaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpembayaranActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpembayaranActionPerformed

    private void bbaruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bbaruActionPerformed
        // TODO add your handling code here:
          kosong();
    }//GEN-LAST:event_bbaruActionPerformed

    private void bsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bsimpanActionPerformed
try {
            String hargaStr = txtharga.getText().replace(",", "");
            double harga = Double.parseDouble(hargaStr);

            String sql = "INSERT INTO delight (id_transaksi, nama, rasa, harga, jumlah_beli, total_harga, diskon, ppn, total_bayar, pembayaran, kembalian) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbicecream", "root", "");
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtidtransaksi.getText());
            pst.setString(2, txtnamapembeli.getText());
            pst.setString(3, cbpilihanrasa.getSelectedItem().toString());
            pst.setDouble(4, harga);
            pst.setString(5, txtjumlahbeli.getText());
            pst.setString(6, txttotalharga.getText());
            pst.setString(7, txtdiskon.getText());
            pst.setString(8, txtppn.getText());
            pst.setString(9, txttotalbayar.getText());
            pst.setString(10, txtpembayaran.getText());
            pst.setString(11, txtkembalian.getText());

            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Berhasil Ditambahkan");
            load_table();
            kosong();
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + e.getMessage());
        }   
    }//GEN-LAST:event_bsimpanActionPerformed

    private void bhapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bhapusActionPerformed
        // TODO add your handling code here:
       int confirmed = JOptionPane.showConfirmDialog(null, 
        "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi Hapus",
        JOptionPane.YES_NO_OPTION);
        if (confirmed == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM delight WHERE id_transaksi=?";
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbicecream", "root", "");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, txtidtransaksi.getText());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
                load_table();
                kosong();
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
    }//GEN-LAST:event_bhapusActionPerformed
    }
    private void beditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beditActionPerformed
        // TODO add your handling code here:
       try {
        String sql = "UPDATE delight SET " 
                        + "nama=?, " 
                        + "rasa=?, " 
                        + "harga=?, " 
                        + "jumlah_beli=?, "
                        + "total_harga=?, " 
                        + "diskon=?, " 
                        + "ppn=?, " 
                        + "total_bayar=?, " 
                        + "pembayaran=?, " 
                        + "kembalian=? " 
                        + "WHERE id_transaksi=?";
            
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbicecream", "root", "");
            PreparedStatement pst = conn.prepareStatement(sql);
            
            pst.setString(1, txtnamapembeli.getText());
            pst.setString(2, cbpilihanrasa.getSelectedItem().toString());
            pst.setDouble(3, Double.parseDouble(txtharga.getText().replace(",", "")));
            pst.setInt(4, Integer.parseInt(txtjumlahbeli.getText()));
            pst.setDouble(5, Double.parseDouble(txttotalharga.getText()));
            pst.setDouble(6, Double.parseDouble(txtdiskon.getText()));
            pst.setDouble(7, Double.parseDouble(txtppn.getText()));
            pst.setDouble(8, Double.parseDouble(txttotalbayar.getText()));
            pst.setDouble(9, Double.parseDouble(txtpembayaran.getText()));
            pst.setDouble(10, Double.parseDouble(txtkembalian.getText()));
            pst.setString(11, txtidtransaksi.getText());

            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
            load_table();
            kosong();
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengubah data: " + e.getMessage());
        }
    }//GEN-LAST:event_beditActionPerformed

    private void bkeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bkeluarActionPerformed
        // TODO add your handling code here:
         int response = JOptionPane.showConfirmDialog(null, "Apakah anda ingin keluar dari program?", "Konfirmasi Keluar",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
        
    }//GEN-LAST:event_bkeluarActionPerformed

    private void bcetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcetakActionPerformed
        // TODO add your handling code here:
        try {
            
            String idtransaksii = txtidtransaksi.getText();
            HashMap<String, Object> parameter = new HashMap<>();
            parameter.put("idtransaksi", txtidtransaksi);
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/dbicecream", "root", "");

            File file = new File("D:\\apau\\NetBeansProjects\\UAS\\src\\laporan\\Laporan_Data_Mhs.jasper");
            JasperReport jp = (JasperReport) JRLoader.loadObject(file);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jp, parameter, cn);
            JasperViewer.viewReport(jasperPrint, false);
            JasperViewer.setDefaultLookAndFeelDecorated(true);

        } catch (ClassNotFoundException | SQLException | JRException e) {
            JOptionPane.showMessageDialog(null, "Dokumen Tidak Ada " + e.getMessage());
        }
    }//GEN-LAST:event_bcetakActionPerformed

    private void bcetaklaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcetaklaporanActionPerformed
        // TODO add your handling code here:
        try {
            HashMap<String, Object> parameter = new HashMap<>();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/dbpemesanan", "root", "");

            File file = new File("D:\\apau\\NetBeansProjects\\JavaKampus1\\src\\laporan\\Laporan_Data_Mhs.jasper");
            JasperReport jp = (JasperReport) JRLoader.loadObject(file);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jp, parameter, cn);
            JasperViewer.viewReport(jasperPrint, false);
            JasperViewer.setDefaultLookAndFeelDecorated(true);

        } catch (ClassNotFoundException | SQLException | JRException e) {
            JOptionPane.showMessageDialog(null, "Data Tidak Dapat Ditampilkan: " + e.getMessage());
        }
    }//GEN-LAST:event_bcetaklaporanActionPerformed

    private void cbpilihanrasaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbpilihanrasaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbpilihanrasaActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
         int baris = jTable1.rowAtPoint(evt.getPoint());
        String idtransaksi = jTable1.getValueAt(baris, 0).toString();
        txtidtransaksi.setText(idtransaksi);
        String nama = jTable1.getValueAt(baris, 1).toString();
        txtnamapembeli.setText(nama);
        String rasa = jTable1.getValueAt(baris, 2).toString();
        cbpilihanrasa.setSelectedItem(rasa);
        String harga = jTable1.getValueAt(baris, 3).toString();
        txtharga.setText(harga);
        String jumlahBeli = jTable1.getValueAt(baris, 4).toString();
        txtjumlahbeli.setText(jumlahBeli);
        String totalHarga = jTable1.getValueAt(baris, 5).toString();
        txttotalharga.setText(totalHarga);
        String diskon = jTable1.getValueAt(baris, 6).toString();
        txtdiskon.setText(diskon);
        String ppn = jTable1.getValueAt(baris, 7).toString();
        txtppn.setText(ppn);
        String totalBayar = jTable1.getValueAt(baris, 8).toString();
        txttotalbayar.setText(totalBayar);
        String pembayaran = jTable1.getValueAt(baris, 9).toString();
        txtpembayaran.setText(pembayaran);
        String kembalian = jTable1.getValueAt(baris, 10).toString();
        txtkembalian.setText(kembalian);
    }//GEN-LAST:event_jTable1MouseClicked

    private void txttotalhargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotalhargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotalhargaActionPerformed

    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new icecream().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bbaru;
    private javax.swing.JButton bcetak;
    private javax.swing.JButton bcetaklaporan;
    private javax.swing.JButton bedit;
    private javax.swing.JButton bhapus;
    private javax.swing.JButton bkeluar;
    private javax.swing.JButton bsimpan;
    private javax.swing.JComboBox<String> cbpilihanrasa;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField txtdiskon;
    private javax.swing.JTextField txtharga;
    private javax.swing.JTextField txtidtransaksi;
    private javax.swing.JTextField txtjumlahbeli;
    private javax.swing.JTextField txtkembalian;
    private javax.swing.JTextField txtnamapembeli;
    private javax.swing.JTextField txtpembayaran;
    private javax.swing.JTextField txtppn;
    private javax.swing.JTextField txttotalbayar;
    private javax.swing.JTextField txttotalharga;
    // End of variables declaration//GEN-END:variables
}
