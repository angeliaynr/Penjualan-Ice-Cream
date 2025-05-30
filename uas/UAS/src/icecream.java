import javax.swing.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
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
import java.text.ParseException;

public class icecream extends javax.swing.JFrame {

    public icecream() {
    try {
        initComponents();
        load_table();
        kosong();
    } catch (SQLException e) {
        Logger.getLogger(icecream.class.getName()).log(Level.SEVERE, null, e);
    }

    cbpilihanrasa.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            setHargaPerPilihanRasa(); // Set harga per rasa saat pilihan rasa dipilih
            hitung(); // Panggil hitung() setelah rasa dipilih
        }
    });

    // Tambahkan key listener untuk text field txtpembayaran
    txtpembayaran.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                hitungKembalian(); 
            }
        }
    });

    txtjumlahbeli.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                hitung(); 
            }
        }
    });
}
    private void prosesData() {
    if (!validateInput()) {
        return;
    }
    setHargaPerPilihanRasa();
    hitung();
    hitungKembalian();
}

    private boolean validateInput() {
    if (cbpilihanrasa.getSelectedItem() == null || cbpilihanrasa.getSelectedItem().toString().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Pilih rasa terlebih dahulu.");
        return false;
    }
    if (txtjumlahbeli.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Jumlah beli tidak boleh kosong.");
        return false;
    }
    if (txtpembayaran.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Pembayaran tidak boleh kosong.");
        return false;
    }
    return true;
}


private void setHargaPerPilihanRasa() {
try {
        DecimalFormat df = new DecimalFormat("#,###");

        if (cbpilihanrasa.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Pilih rasa terlebih dahulu.");
            return;
        }

        String pilihanrasa = cbpilihanrasa.getSelectedItem().toString();
        int hargaPerRasa = 0;

        switch (pilihanrasa) {
            case "--PILIHAN--":
                txtharga.setText("");
                return;
            case "Coklat":
                hargaPerRasa = 10000;
                break;
            case "Vanilla":
                hargaPerRasa = 15000;
                break;
            case "Strawberry":
                hargaPerRasa = 17000;
                break;
            case "Oreo":
                hargaPerRasa = 18000;
                break;
        }

        System.out.println("Harga Per Rasa: " + hargaPerRasa); // Debugging
        txtharga.setText(df.format(hargaPerRasa));
        System.out.println("Formatted Harga: " + txtharga.getText()); // Debugging

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Data ini tidak valid. Pastikan semua data telah diisi dengan benar.");
    }
}


private void hitung() {
        try {
            DecimalFormat df = new DecimalFormat("#,###");

            int harga = Integer.parseInt(txtharga.getText().replaceAll(",", ""));
            int jumlahbeli = Integer.parseInt(txtjumlahbeli.getText());
            int totalharga = jumlahbeli * harga;

            txttotalharga.setText(df.format(totalharga));

            String pilihanrasa = cbpilihanrasa.getSelectedItem().toString();
            double diskon = 0;
            if ((pilihanrasa.equals("Coklat") || pilihanrasa.equals("Oreo")) && jumlahbeli >= 3) {
                diskon = totalharga * 0.15;
            }
            txtdiskon.setText(df.format(diskon));

            // Hitung PPN
            double ppn = 0.02 * totalharga;
            txtppn.setText(df.format(ppn));

            // Hitung total harga setelah diskon dan PPN
            double totalHarga = totalharga + ppn - diskon;
            txttotalbayar.setText(df.format(totalHarga));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Data ini tidak valid. Pastikan semua data telah diisi dengan benar.");
        }
    }

    private void hitungKembalian() {
        try {
            DecimalFormat df = new DecimalFormat("#,###");

        double totalHarga = Double.parseDouble(txttotalbayar.getText().replaceAll(",", ""));
        double jumlahPembayaran = Double.parseDouble(txtpembayaran.getText().replaceAll(",", ""));
        
        // Cek apakah pembayaran kurang dari total harga
        if (jumlahPembayaran < totalHarga) {
            JOptionPane.showMessageDialog(null, "Pembayaran kurang.");
            txtkembalian.setText(""); // Kosongkan field kembalian jika pembayaran kurang
            return;
        }

        double kembalian = jumlahPembayaran - totalHarga;

        txtkembalian.setText(df.format(kembalian));

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
    model.addColumn("Nama Pembeli");
    model.addColumn("Pilihan Rasa");
    model.addColumn("Harga");
    model.addColumn("Jumlah Beli");
    model.addColumn("Total Harga");
    model.addColumn("Diskon");
    model.addColumn("PPN");
    model.addColumn("Total Bayar");
    model.addColumn("Pembayaran");
    model.addColumn("Kembalian");

    String sql = "SELECT * FROM delight";
    Connection conn = config.configDB();
    java.sql.Statement stm = conn.createStatement();
    java.sql.ResultSet res = stm.executeQuery(sql);

    while (res.next()) {
        model.addRow(new Object[]{
            res.getString("id_transaksi"),
            res.getString("nama"),
            res.getString("rasa"),
            res.getDouble("harga"),
            res.getInt("jumlah_beli"),
            res.getDouble("total_harga"),
            res.getDouble("diskon"),
            res.getDouble("ppn"),
            res.getDouble("total_bayar"),
            res.getDouble("pembayaran"),
            res.getDouble("kembalian")
        });
    }
    jTable1.setModel(model);
}
    
    private void btambahActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            double harga = Double.parseDouble(txtharga.getText());
            int jumlahBeli = Integer.parseInt(txtjumlahbeli.getText());
            double totalHarga = harga * jumlahBeli;
            double diskon = totalHarga >= 100000 ? totalHarga * 0.2 : totalHarga * 0.1;
            double ppn = totalHarga * 0.1;
            double totalBayar = totalHarga - diskon + ppn;
            double pembayaran = Double.parseDouble(txtpembayaran.getText());
            double kembalian = pembayaran - totalBayar;
            
            String sql = "INSERT INTO delight (id_transaksi, nama, rasa, harga, jumlah_beli, total_harga, diskon, ppn, total_bayar, pembayaran, kembalian) VALUES ('" + 
                    txtnamapembeli.getText() + "','" + cbpilihanrasa.getSelectedIndex() + "'," + harga + "," + jumlahBeli + "," + totalHarga + "," + diskon + "," + ppn + "," + totalBayar + "," + pembayaran + "," + kembalian + ")";
            Connection conn = config.configDB();
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Berhasil Ditambahkan");
            
            load_table();
            kosong();
        } catch (SQLException | HeadlessException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
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

        txtharga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthargaActionPerformed(evt);
            }
        });

        txtjumlahbeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtjumlahbeliActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel12.setText("ID Transaksi");

        cbpilihanrasa.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        cbpilihanrasa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--PILIHAN--", "Coklat", "Vanilla", "Starwberry", "Oreo", " " }));
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
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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

        bbaru.setText("Baru");
        bbaru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bbaruActionPerformed(evt);
            }
        });

        bsimpan.setText("Simpan");
        bsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsimpanActionPerformed(evt);
            }
        });

        bedit.setText("Edit");
        bedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beditActionPerformed(evt);
            }
        });

        bhapus.setText("Hapus");
        bhapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bhapusActionPerformed(evt);
            }
        });

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
        jScrollPane1.setViewportView(jTable1);

        bcetak.setText("Cetak Nota");
        bcetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcetakActionPerformed(evt);
            }
        });

        bcetaklaporan.setText("Cetak Laporan");
        bcetaklaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcetaklaporanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
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
                            .addComponent(bbaru, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                            .addComponent(bsimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bedit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bhapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bkeluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(bcetaklaporan))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(197, 197, 197))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 695, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
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
                                .addComponent(bkeluar)))
                        .addGap(18, 18, 18)
                        .addComponent(bcetaklaporan))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bcetak)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    setHargaPerPilihanRasa();
    hitung(); 
    hitungKembalian(); 

    try {
        Connection conn = config.configDB();
        String sql = "INSERT INTO delight (id_transaksi, nama, rasa, harga, jumlah_beli, total_harga, diskon, ppn, total_bayar, pembayaran, kembalian) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setInt(1, Integer.parseInt(txtidtransaksi.getText()));
        pst.setString(2, txtnamapembeli.getText());
        pst.setString(3, cbpilihanrasa.getSelectedItem().toString());
        pst.setInt(4, Integer.parseInt(txtharga.getText().replace(",", "")));
        pst.setInt(5, Integer.parseInt(txtjumlahbeli.getText()));
        pst.setInt(6, Integer.parseInt(txttotalharga.getText().replace(",", "")));
        pst.setDouble(7, Double.parseDouble(txtdiskon.getText().replace(",", "")));
        pst.setDouble(8, Double.parseDouble(txtppn.getText().replace(",", "")));
        pst.setDouble(9, Double.parseDouble(txttotalbayar.getText().replace(",", "")));
        pst.setDouble(10, Double.parseDouble(txtpembayaran.getText().replace(",", "")));
        pst.setDouble(11, Double.parseDouble(txtkembalian.getText().replace(",", "")));

        pst.execute();
        JOptionPane.showMessageDialog(null, "Data berhasil disimpan");

        load_table();
        kosong();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + e.getMessage());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Format data tidak valid: " + e.getMessage());
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
                java.sql.Connection conn = (Connection) config.configDB();
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);

                pst.setString(1, txtidtransaksi.getText());

                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus");

                load_table();
                kosong();
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Menghapus data dibatalkan");
        }
    }//GEN-LAST:event_bhapusActionPerformed

    private void beditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beditActionPerformed
        // TODO add your handling code here:
       try {
        Connection conn = config.configDB();
        String sql = "UPDATE delight SET nama_pembeli = ?, pilihan_rasa = ?, harga = ?, jumlah_beli = ?, total_harga = ?, diskon = ?, ppn = ?, total_bayar = ?, jumlah_pembayaran = ?, kembalian = ? WHERE id_transaksi = ?";
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setString(1, txtnamapembeli.getText());
        pst.setString(2, cbpilihanrasa.getSelectedItem().toString());
        pst.setInt(3, Integer.parseInt(txtharga.getText().replace(",", "")));
        pst.setInt(4, Integer.parseInt(txtjumlahbeli.getText()));
        pst.setInt(5, Integer.parseInt(txttotalharga.getText().replace(",", "")));
        pst.setDouble(6, Double.parseDouble(txtdiskon.getText().replace(",", "")));
        pst.setDouble(7, Double.parseDouble(txtppn.getText().replace(",", "")));
        pst.setDouble(8, Double.parseDouble(txttotalbayar.getText().replace(",", "")));
        pst.setDouble(9, Double.parseDouble(txtpembayaran.getText().replace(",", "")));
        pst.setDouble(10, Double.parseDouble(txtkembalian.getText().replace(",", "")));
        pst.setInt(11, Integer.parseInt(txtidtransaksi.getText()));

        pst.execute();
        JOptionPane.showMessageDialog(null, "Data berhasil diperbarui");

        load_table();
        kosong();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal memperbarui data: " + e.getMessage());
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
            
            String idTransaksii = txtidtransaksi.getText();
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

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        int baris = jTable1.rowAtPoint(evt.getPoint());
        txtidtransaksi.setText(jTable1.getValueAt(baris, 0).toString());
        txtnamapembeli.setText(jTable1.getValueAt(baris, 1).toString());
        cbpilihanrasa.setSelectedItem(jTable1.getValueAt(baris, 2).toString());
        txtharga.setText(jTable1.getValueAt(baris, 3).toString());
        txtjumlahbeli.setText(jTable1.getValueAt(baris, 4).toString());
        txttotalharga.setText(jTable1.getValueAt(baris, 5).toString());
        txtdiskon.setText(jTable1.getValueAt(baris, 6).toString());
        txtppn.setText(jTable1.getValueAt(baris, 7).toString());
        txttotalbayar.setText(jTable1.getValueAt(baris, 8).toString());
        txtpembayaran.setText(jTable1.getValueAt(baris, 9).toString());
        txtkembalian.setText(jTable1.getValueAt(baris, 10).toString());
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(icecream.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(icecream.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(icecream.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(icecream.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new icecream().setVisible(true);
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
