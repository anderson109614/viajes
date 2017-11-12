/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gaby
 */
public class Viajes extends javax.swing.JFrame {

    DefaultTableModel model;

    /**
     * Creates new form Viajes
     */
    public Viajes() {
        initComponents();
        contadorViajes();
        cargarCombo();
        cargarPlacas();
        cargarTablaViajes("");
        txtBloqueo();
        BotonesInicio();
        cmbCiudadOrigen.insertItemAt("SELECCIONE", 0);
        cmbCiudadOrigen.setSelectedIndex(0);
        cmbCiudadDestino.insertItemAt("SELECCIONE", 0);
        cmbCiudadDestino.setSelectedIndex(0);
        cmbPlacas.insertItemAt("SELECCIONE", 0);
        cmbPlacas.setSelectedIndex(0);
         String formato= "YYYY/MM/dd";
         SimpleDateFormat sdf = new SimpleDateFormat(formato);
        fechaSalida.setDateFormat(sdf);
        fechaLLegada.setDateFormat(sdf);
        
        tblViajes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (tblViajes.getSelectedRow() != -1) {
                    int fila = tblViajes.getSelectedRow();
                    txtCodigo.setText(tblViajes.getValueAt(fila, 0).toString().trim());
                    cmbPlacas.setSelectedItem(tblViajes.getValueAt(fila, 1).toString().trim());
                    cmbCiudadOrigen.setSelectedItem(tblViajes.getValueAt(fila, 2).toString().trim());
                    cmbCiudadDestino.setSelectedItem(tblViajes.getValueAt(fila, 3).toString().trim());
                    
                    fechaSalida.setText(tblViajes.getValueAt(fila, 4).toString().trim());
                    fechaLLegada.setText(tblViajes.getValueAt(fila, 5).toString().trim());
//                    fechaSalida.setDateFormatString(tblViajes.getValueAt(fila, 4).toString().trim());
//                    fechaLLegada.setDateFormatString(tblViajes.getValueAt(fila, 5).toString().trim());
                    fmtHoraSalida.setText(tblViajes.getValueAt(fila, 6).toString().trim());
                    fmtHoraLlegada.setText(tblViajes.getValueAt(fila, 7).toString().trim());
                    txtCosto.setText(tblViajes.getValueAt(fila, 8).toString().trim());
                    txtDescripcion.setText(tblViajes.getValueAt(fila, 9).toString().trim());
                    txtDesBloqueo();
                    txtCodigo.setEnabled(false);
                    BotonesActualizar();
                }
            }
        });

    }

    public void cargarCombo() {
        conexionViaje cc = new conexionViaje();
        Connection cn = cc.conectar();
        String sql = "";
        sql = "SELECT * FROM CIUDAD";
        try {
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString("CIU_CODIGO");
                String origenDestino = rs.getString("CIU_NOMBRE");

                cmbCiudadOrigen.addItem(id + " " + origenDestino);
                cmbCiudadDestino.addItem(id + " " + origenDestino);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);

        }
    }

    public void cargarPlacas() {
        conexionViaje cc = new conexionViaje();
        Connection cn = cc.conectar();
        String sql = "";
        sql = "SELECT AUT_PLACA from AUTO WHERE AUT_EST='1'";
        try {
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                String placa = rs.getString("AUT_PLACA");
                cmbPlacas.addItem(placa);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void cargarTablaViajes(String Dato) {
        String[] titulos = {"CODIGO", "PLACA", "CIU ORIGEN", "CIU DESTINO", "FECHA SALIDA", "FECHA LLEGADA",
            "HORA SALIDA", "HORA LLEGADA", "COSTO", "DESCRIPCION"};
        model = new DefaultTableModel(null, titulos){
            public boolean isCellEditable(int row, int column) {
                // bloqueo de la primara columna
                if (column == 0) {
                    return false;
                }
                return true;
            }
        ;
        };
        String[] registro = new String[10];
        conexionViaje cc = new conexionViaje();
        Connection cn = cc.conectar();
        String sql = "";
        sql = "select *from viajes where via_codigo LIKE'%" + Dato + "%' order by via_codigo";

        try {
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {

                registro[0] = rs.getString("VIA_CODIGO");
                registro[1] = rs.getString("AUT_PLACA");
                registro[2] = rs.getString("CIU_CODIGO");
                registro[3] = rs.getString("CIU_CIU_CODIGO");
                registro[4] = rs.getDate("VIA_FECHASALIDA").toString();
                registro[5] = rs.getDate("VIA_FECHALLEGADA").toString();
                registro[6] = rs.getString("VIA_HORASALIDA");
                registro[7] = rs.getString("VIA_HORALLEGADA");
                registro[8] = rs.getString("VIA_COSTO");
                registro[9] = rs.getString("VIA_DESCRIPCION");
                model.addRow(registro);
            }
            tblViajes.setModel(model);
            txtCodigo.setEnabled(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }
    
    public void actualizar() {
        conexionViaje cc = new conexionViaje();
        Connection cn = cc.conectar();
        String sql = "";
        sql = "update viajes set via_codigo='" + txtCodigo.getText() + "'" 
                + ",AUT_PLACA='" + cmbPlacas.getSelectedItem()+ "'"
                + ",CIU_CODIGO='" + cmbCiudadOrigen.getSelectedItem().toString().charAt(0) + "'"
                + ",CIU_CIU_CODIGO='" + cmbCiudadDestino.getSelectedItem().toString().charAt(0) + "'"
                 + ",VIA_FECHASALIDA='" + fechaSalida.getText() + "'"
                + ",VIA_FECHALLEGADA='" + fechaLLegada.getText() + "'"
                + ",VIA_HORASALIDA='" +  fmtHoraSalida.getText() + "'"
                + ",VIA_HORALLEGADA='" + fmtHoraLlegada.getText() + "'"
                + ",VIA_COSTO=" + txtCosto.getText() + ""
                + ",VIA_DESCRIPCION='" + txtDescripcion.getText() + "'"
                + " Where VIA_CODIGO='" + txtCodigo.getText() + "'";
        try {
            PreparedStatement psd = cn.prepareStatement(sql);
            int n = psd.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Se actualizó el registro correctamente");
            }
            cargarTablaViajes("");
            txtLimpiar();
            txtBloqueo();
            BotonesInicio();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    public void guardar() {
        if (cmbPlacas.getSelectedItem().equals("SELECCIONE")) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una opción");
            cmbPlacas.requestFocus();
        } else if (cmbCiudadOrigen.getSelectedItem().equals("SELECCIONE")) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una opción");
        } else if (cmbCiudadDestino.getSelectedItem().equals("SELECCIONE")) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una opción");
        } else if (fechaSalida.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una Fecha");
        } else if (fechaLLegada.getText()
                .equals("")) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una Fecha");
        } else if (fmtHoraSalida.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar hora");
        } else if (fmtHoraLlegada.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar hora");
        } else if (txtCosto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar costo");
        } else {

            conexionViaje cc = new conexionViaje();
            Connection cn = cc.conectar();
            String VIA_CODIGO,AUT_PLACA,  VIA_FECHASALIDA, VIA_FECHALLEGADA, VIA_HORASALIDA,
                    VIA_HORALLEGADA,  VIA_DESCRIPCION;
            char CIU_CODIGO, CIU_CIU_CODIGO;
            float VIA_COSTO;
//            int viaje=generarNumeroViaje();
            VIA_CODIGO = txtCodigo.getText();
            AUT_PLACA=cmbPlacas.getSelectedItem().toString();
            CIU_CODIGO =cmbCiudadOrigen.getSelectedItem().toString().charAt(0);
            CIU_CIU_CODIGO = cmbCiudadDestino.getSelectedItem().toString().charAt(0);
            
            VIA_HORASALIDA = fmtHoraSalida.getText();
            VIA_HORALLEGADA = fmtHoraLlegada.getText();
            VIA_COSTO =Float.valueOf( txtCosto.getText());
            VIA_COSTO=(float) ((float)Math.round(VIA_COSTO * 100d) / 100d); 

            /// extraccion de las fechas del DataChooser
//            String formato= fechaSalida.getDateFormatString();
//            Date date = fechaSalida.getDate();
//            
//            SimpleDateFormat sdf = new SimpleDateFormat(formato);
//            VIA_FECHASALIDA =  String.valueOf(sdf.format(date));
//            date = fechaLLegada.getDate();
//            VIA_FECHALLEGADA = String.valueOf(sdf.format(date));
            
         VIA_FECHASALIDA=fechaSalida.getText();
         VIA_FECHALLEGADA=fechaLLegada.getText();
            // JOptionPane.showMessageDialog(null,VIA_FECHASALIDA);
            if (txtDescripcion.getText().isEmpty()) {
                VIA_DESCRIPCION = "Sin Informacion";
            } else {
                VIA_DESCRIPCION = txtDescripcion.getText();
            }

            String sql = "";
            sql = "INSERT INTO VIAJES(VIA_CODIGO,AUT_PLACA, CIU_CODIGO,CIU_CIU_CODIGO, VIA_FECHASALIDA,"
                    + " VIA_FECHALLEGADA, VIA_HORASALIDA, VIA_HORALLEGADA,VIA_COSTO,VIA_DESCRIPCION)"
                    + " VALUES(?,?,?,?,'"+VIA_FECHASALIDA+"','"+VIA_FECHALLEGADA+"',?,?,"+VIA_COSTO+",?)";

            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.setString(1, VIA_CODIGO);
                psd.setString(2, AUT_PLACA);
                psd.setString(3, String.valueOf(CIU_CODIGO));
                psd.setString(4, String.valueOf(CIU_CIU_CODIGO));
//                psd.setDate(5, VIA_FECHASALIDA);
//                psd.setDate(6, VIA_FECHALLEGADA);
                psd.setString(5, VIA_HORASALIDA);
                psd.setString(6, VIA_HORALLEGADA);
             
                psd.setString(7, VIA_DESCRIPCION);
                JOptionPane.showMessageDialog(null, psd.toString());
                int n = psd.executeUpdate();

                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Se insertó la información correctamente");
                    cargarTablaViajes("");
                    txtLimpiar();
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }

        }

    }

    public void txtLimpiar() {
        txtCodigo.setText("");
        cmbPlacas.setSelectedItem("");
        cmbCiudadOrigen.setSelectedItem("");
        cmbCiudadDestino.setSelectedItem("");
//        fechaSalida.setDateFormat("");
//        fechaLLegada.setDateFormat("");
        fmtHoraSalida.setText("");
        fmtHoraLlegada.setText("");
        txtCosto.setText("");
        txtDescripcion.setText("");
    }

    public void BotonesInicio() {
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnBorrar.setEnabled(false);
        btnSalir.setEnabled(true);
    }

    public void BotonesNuevo() {
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(true);
        btnBorrar.setEnabled(false);
        btnSalir.setEnabled(true);
    }
  

    public void BotonesActualizar() {
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnBorrar.setEnabled(true);
        btnSalir.setEnabled(true);
    }

    public void txtBloqueo() {
        txtCodigo.setEnabled(false);
        cmbPlacas.setEnabled(false);
        cmbCiudadOrigen.setEnabled(false);
        cmbCiudadDestino.setEnabled(false);
        fechaSalida.setEnabled(false);
        fechaLLegada.setEnabled(false);
        fmtHoraSalida.setEnabled(false);
        fmtHoraLlegada.setEnabled(false);
        txtCosto.setEnabled(false);
        txtDescripcion.setEnabled(false);

    }

    public void txtDesBloqueo() {
        txtCodigo.setEnabled(true);
        cmbPlacas.setEnabled(true);
        cmbCiudadOrigen.setEnabled(true);
        cmbCiudadDestino.setEnabled(true);
        fechaSalida.setEnabled(true);
        fechaLLegada.setEnabled(true);
        fmtHoraSalida.setEnabled(true);
        fmtHoraLlegada.setEnabled(true);
        txtCosto.setEnabled(true);
        txtDescripcion.setEnabled(true);
    }

    public int contadorViajes() {

        conexionViaje cc = new conexionViaje();
        Connection cn = cc.conectar();
        int i = 0;
        
            
            try {
                 
                String sql = "SELECT count(VIA_CODIGO) "
                        + "FROM VIAJES ";
                PreparedStatement psd = cn.prepareStatement(sql); 
                ResultSet rs = psd.executeQuery(sql);

                while (rs.next()) {
                    i = rs.getInt(1);
                    txtCodigo.setText(String.valueOf(i+1));
                }

            } catch (Exception e) {
            }
         
        return i;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbCiudadOrigen = new javax.swing.JComboBox<>();
        cmbCiudadDestino = new javax.swing.JComboBox<>();
        txtDescripcion = new javax.swing.JTextField();
        txtCosto = new javax.swing.JTextField();
        fmtHoraSalida = new javax.swing.JFormattedTextField();
        fmtHoraLlegada = new javax.swing.JFormattedTextField();
        cmbPlacas = new javax.swing.JComboBox<>();
        fechaSalida = new datechooser.beans.DateChooserCombo();
        fechaLLegada = new datechooser.beans.DateChooserCombo();
        jPanel2 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblViajes = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Código:");

        jLabel2.setText("Placa Auto:");

        jLabel3.setText("Ciudad Origen:");

        jLabel4.setText("Ciudad Destino:");

        jLabel5.setText("Fecha Salida:");

        jLabel6.setText("Fecha Llegada:");

        jLabel7.setText("Hora Salida:");

        jLabel8.setText("Hora Llegada:");

        jLabel9.setText("Costo:");

        jLabel10.setText("Descripción:");

        txtCosto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCostoActionPerformed(evt);
            }
        });

        try {
            fmtHoraSalida.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:## ")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            fmtHoraLlegada.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:## ")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fmtHoraLlegada, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fmtHoraSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(fechaLLegada, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fechaSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(cmbPlacas, javax.swing.GroupLayout.Alignment.LEADING, 0, 155, Short.MAX_VALUE)
                                .addComponent(cmbCiudadOrigen, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbCiudadDestino, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtCodigo, javax.swing.GroupLayout.Alignment.LEADING)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbPlacas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCiudadOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCiudadDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(fechaSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(fechaLLegada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(fmtHoraSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(fmtHoraLlegada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar.jpg"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/actualizar.jpg"))); // NOI18N
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar.jpg"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/borrawr.png"))); // NOI18N
        btnBorrar.setText("Borrar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBorrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblViajes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblViajes);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(194, 194, 194))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCostoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        txtDesBloqueo();
        BotonesNuevo();// TODO add your handling code here:
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardar();  
        BotonesInicio();
        txtBloqueo();// TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        actualizar(); 
        BotonesInicio();
        txtBloqueo();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        txtLimpiar();         // TODO add your handling code here:
        BotonesInicio();
        txtBloqueo();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBorrarActionPerformed

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
            java.util.logging.Logger.getLogger(Viajes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Viajes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Viajes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Viajes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Viajes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cmbCiudadDestino;
    private javax.swing.JComboBox<String> cmbCiudadOrigen;
    private javax.swing.JComboBox<String> cmbPlacas;
    private datechooser.beans.DateChooserCombo fechaLLegada;
    private datechooser.beans.DateChooserCombo fechaSalida;
    private javax.swing.JFormattedTextField fmtHoraLlegada;
    private javax.swing.JFormattedTextField fmtHoraSalida;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JTable tblViajes;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JTextField txtDescripcion;
    // End of variables declaration//GEN-END:variables
}
