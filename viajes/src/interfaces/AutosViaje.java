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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ander
 */
public class AutosViaje extends javax.swing.JInternalFrame {

    /**
     * Creates new form AutosViaje
     */
    public AutosViaje() {
        initComponents();
        BotonesInicio();
        txtBloqueo();
        cargarTablaAutos("");
        tblAutos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (tblAutos.getSelectedRow() != -1) {
                    int fila = tblAutos.getSelectedRow();
                    txtPlaca.setText(tblAutos.getValueAt(fila, 0).toString().trim());
                    txtMarca.setText(tblAutos.getValueAt(fila, 1).toString().trim());
                    txtModelo.setText(tblAutos.getValueAt(fila, 2).toString().trim());
                    txtColor.setText(tblAutos.getValueAt(fila, 3).toString().trim());
                    txtAño.setText(tblAutos.getValueAt(fila, 4).toString().trim());
                    txtObservacion.setText(tblAutos.getValueAt(fila, 5).toString().trim());
                    txtDesBloqueo();
                    txtPlaca.setEnabled(false);
                    BotonesActualizar();
                }
            }
        });
        
        
    }
    DefaultTableModel model;

    public void cargarTablaAutos(String Dato) {
        String[] titulos = {"PLACA", "MARCA", "MODELO", "COLOR", "AÑO", "DESCRIPCION"};
        model = new DefaultTableModel(null, titulos) {
            public boolean isCellEditable(int row, int column) {
                // bloque de la primara columna
                if (column == 0) {
                    return false;
                }
                return true;
            }
        ;
        };

        String[] registro = new String[6];
        conexionViaje cc = new conexionViaje();
        Connection cn = cc.conectar();
        String sql = "";
        sql = "SELECT * FROM auto WHERE AUT_PLACA LIKE '%" + Dato + "%' AND AUT_ESTADO='1'  ORDER BY AUT_PLACA";
        try {
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                registro[0] = rs.getString("AUT_PLACA");
                registro[1] = rs.getString("AUT_MARCA");
                registro[2] = rs.getString("AUT_MODELO");
                registro[3] = rs.getString("AUT_COLOR");
                registro[4] = rs.getString("AUT_ANIO");
                registro[5] = rs.getString("AUT_DESCRIPCION");
                model.addRow(registro);

            }
            tblAutos.setModel(model);

            model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {// guardado de la iformacion directo del jtable
                if(e.getType()==TableModelEvent.UPDATE){
                    int columna=e.getColumn();
                    int fila= e.getFirstRow();
                    if(Act_tabla(columna,fila)){
                        JOptionPane.showMessageDialog(null, "Actualizacion correcta");
                    }
                    
                    
                    
                }
            }
        });
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public boolean Act_tabla(int col,int fil){
            conexionViaje cc = new conexionViaje();
            Connection cn = cc.conectar();
            String sql = "";
        if(col==1){
           
            sql = "UPDATE AUTO SET AUT_MARCA='"+tblAutos.getValueAt(fil, col)+"' "
                    + "WHERE AUT_PLACA='" + tblAutos.getValueAt(fil, 0) + "'";
            
        }
        if(col==2){
           
            sql = "UPDATE AUTO SET AUT_MODELO='"+tblAutos.getValueAt(fil, col)+"' "
                    + "WHERE AUT_PLACA='" + tblAutos.getValueAt(fil, 0) + "'";
            
        }
        if(col==3){
           
            sql = "UPDATE AUTO SET AUT_COLOR='"+tblAutos.getValueAt(fil, col)+"' "
                    + "WHERE AUT_PLACA='" + tblAutos.getValueAt(fil, 0) + "'";
            
        }
        if(col==4){
           
            sql = "UPDATE AUTO SET AUT_ANIO='"+tblAutos.getValueAt(fil, col)+"' "
                    + "WHERE AUT_PLACA='" + tblAutos.getValueAt(fil, 0) + "'";
            
        }
        if(col==5){
           
            sql = "UPDATE AUTO SET AUT_DESCRIPCION='"+tblAutos.getValueAt(fil, col)+"' "
                    + "WHERE AUT_PLACA='" + tblAutos.getValueAt(fil, 0) + "'";
            
        }
        
        try {
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.executeUpdate();
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
                return false;
            }
        
        
    }

    public void txtLimpiar() {
        txtPlaca.setText("");
        txtModelo.setText("");
        txtMarca.setText("");
        txtColor.setText("");
        txtAño.setText("");
        txtObservacion.setText("");
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
        txtPlaca.setEnabled(false);
        txtModelo.setEnabled(false);
        txtMarca.setEnabled(false);
        txtColor.setEnabled(false);
        txtAño.setEnabled(false);
        txtObservacion.setEnabled(false);
    }

    public void txtDesBloqueo() {
        txtPlaca.setEnabled(true);
        txtModelo.setEnabled(true);
        txtMarca.setEnabled(true);
        txtColor.setEnabled(true);
        txtAño.setEnabled(true);
        txtObservacion.setEnabled(true);
        txtPlaca.requestFocus();
    }

    public void borrar() {
        int n = JOptionPane.showConfirmDialog(null, "Desea eliminar el registro ");

        if (n == 0) {
            conexionViaje cc = new conexionViaje();
            Connection cn = cc.conectar();
            String sql = "";
//           sql="DELETE FROM auto WHERE AUT_PLACA='"+txtPlaca.getText()+"'";
            sql = "UPDATE AUTO SET AUT_ESTADO='0' WHERE AUT_PLACA='" + txtPlaca.getText() + "'";
            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.executeUpdate();
                cargarTablaAutos("");
                txtBloqueo();
                txtLimpiar();
                BotonesInicio();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        } else {
            cargarTablaAutos("");
            txtBloqueo();
            txtLimpiar();
            BotonesInicio();
        }
    }

    public void guardar() {
        if (txtPlaca.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar la placa");
            txtPlaca.requestFocus();
        } else if (txtMarca.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar la Marca");
        } else if (txtModelo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar la Modelo");
        } else if (txtColor.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Color");
        } else if (txtAño.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Año");
        } else {

            conexionViaje cc = new conexionViaje();
            Connection cn = cc.conectar();
            String AUT_PLACA, AUT_MARCA, AUT_MODELO, AUT_COLOR, AUT_ANIO, AUT_DESCRIPCION;
            AUT_PLACA = txtPlaca.getText();
            AUT_MARCA = txtMarca.getText();
            AUT_MODELO = txtModelo.getText();
            AUT_COLOR = txtColor.getText();
            AUT_ANIO = txtAño.getText();
            if (txtObservacion.getText().isEmpty()) {
                AUT_DESCRIPCION = "Sin Informacion";
            } else {
                AUT_DESCRIPCION = txtObservacion.getText();
            }

            String sql = "";
            sql = "INSERT INTO AUTO(AUT_PLACA, AUT_MARCA,AUT_MODELO,AUT_COLOR,AUT_ANIO,AUT_DESCRIPCION,AUT_ESTADO)"
                    + " VALUES(?,?,?,?,?,?,'1')";
            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.setString(1, AUT_PLACA);
                psd.setString(2, AUT_MARCA);
                psd.setString(3, AUT_MODELO);
                psd.setString(4, AUT_COLOR);
                psd.setString(5, AUT_ANIO);
                psd.setString(6, AUT_DESCRIPCION);
                int n = psd.executeUpdate();

                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Se insertó la información correctamente");
                    cargarTablaAutos("");
                    txtLimpiar();
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }

        }

    }

    public void actualizar() {
        conexionViaje cc = new conexionViaje();
        Connection cn = cc.conectar();
        String sql = "";
        sql = "UPDATE auto SET AUT_MARCA='" + txtMarca.getText() + "'"
                + ",AUT_MODELO='" + txtModelo.getText() + "'"
                + ",AUT_COLOR='" + txtColor.getText() + "'"
                + ",AUT_ANIO=" + txtAño.getText() + ""
                + ",AUT_DESCRIPCION='" + txtObservacion.getText() + "'"
                + " WHERE AUT_PLACA='" + txtPlaca.getText() + "'";
        try {
            PreparedStatement psd = cn.prepareStatement(sql);
            int n = psd.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Se actualizo el registro correctamente ");
            }
            cargarTablaAutos("");
            txtLimpiar();
            txtBloqueo();
            BotonesInicio();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

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
        txtPlaca = new javax.swing.JTextField();
        txtMarca = new javax.swing.JTextField();
        txtModelo = new javax.swing.JTextField();
        txtColor = new javax.swing.JTextField();
        txtAño = new javax.swing.JTextField();
        txtObservacion = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtBuscarPlaca = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAutos = new javax.swing.JTable();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Placa");

        jLabel2.setText("Marca");

        jLabel3.setText("Modelo");

        jLabel4.setText("Color");

        jLabel5.setText("Año");

        jLabel6.setText("Observacion");

        jLabel7.setText("Buscar por Placa");

        txtBuscarPlaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarPlacaActionPerformed(evt);
            }
        });
        txtBuscarPlaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarPlacaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPlaca)
                            .addComponent(txtMarca)
                            .addComponent(txtModelo)
                            .addComponent(txtColor)
                            .addComponent(txtAño)
                            .addComponent(txtObservacion)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtBuscarPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtAño, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtObservacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtBuscarPlaca, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
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
        btnBorrar.setText("Borra");
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
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBorrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(btnNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnActualizar)
                .addGap(1, 1, 1)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBorrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSalir)
                .addGap(26, 26, 26))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblAutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblAutos);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(261, 261, 261))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        guardar();

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        txtDesBloqueo();
        BotonesNuevo();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        txtLimpiar();
        txtBloqueo();
        BotonesInicio();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        // System.exit(0);
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
        actualizar();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void txtBuscarPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarPlacaActionPerformed

    private void txtBuscarPlacaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarPlacaKeyReleased
        // TODO add your handling code here:
        cargarTablaAutos(txtBuscarPlaca.getText().trim());
    }//GEN-LAST:event_txtBuscarPlacaKeyReleased

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        // TODO add your handling code here:
        borrar();
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
            java.util.logging.Logger.getLogger(AutosViaje.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AutosViaje.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AutosViaje.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AutosViaje.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AutosViaje().setVisible(true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAutos;
    private javax.swing.JTextField txtAño;
    private javax.swing.JTextField txtBuscarPlaca;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtModelo;
    private javax.swing.JTextField txtObservacion;
    private javax.swing.JTextField txtPlaca;
    // End of variables declaration//GEN-END:variables

}
