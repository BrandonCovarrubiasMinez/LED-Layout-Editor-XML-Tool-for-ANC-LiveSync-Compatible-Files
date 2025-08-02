/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files)
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package zMain;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;

public class V_Main extends javax.swing.JFrame {
    
    private javax.swing.JMenuItem menuItemNew;
    private javax.swing.JMenuItem menuItemSave;
    private javax.swing.JMenuItem menuItemLoad;
    private javax.swing.JMenuItem menuItemExport;
    private javax.swing.JMenuItem menuItemImport;
    private javax.swing.JMenuItem menuItemAbout;
    private javax.swing.JToggleButton menuItemSlave;

     //Almacena los JSpinners en un arreglo para hacer mas facil su consulta
    private final JSpinner[] arrSprSbnds;
    
    //Constantes estaticas para identificar los JSpinners
    static final int SPINNER_X= 0;
    static final int SPINNER_Y= 1;
    static final int SPINNER_WIDTH= 2;
    static final int SPINNER_HEIGHT= 3;
    
    public V_Main() {
        initComponents();
        setupMenuBar();  // Añade esta línea
        
        //Inicializando constantes
        arrSprSbnds= new JSpinner[4];
        arrSprSbnds[0]= sprSbndsX;
        arrSprSbnds[1]= sprSbndsY;
        arrSprSbnds[2]= sprSbndsWidth;
        arrSprSbnds[3]= sprSbndsHeight;

        pnlTable.setLayout(null);

        //Pantalla completa
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu menuAbout = new JMenu("About");
        JMenu menuOptions = new JMenu("Options");

        menuItemNew = new javax.swing.JMenuItem("New");
        menuItemSave = new javax.swing.JMenuItem("Save");
        menuItemLoad = new javax.swing.JMenuItem("Load");
        menuItemExport = new javax.swing.JMenuItem("Export");
        menuItemImport = new javax.swing.JMenuItem("Import");

        menuItemAbout= new javax.swing.JMenuItem("About");
        menuAbout.add(menuItemAbout);
        

        menuItemSlave= new javax.swing.JToggleButton("Slave");
        menuOptions.add(menuItemSlave);
        
        

        //menuFile.add(menuItemNew);
        //menuFile.add(menuItemSave);
        //menuFile.add(menuItemLoad);
        menuFile.addSeparator();
        menuFile.add(menuItemExport);
        menuFile.add(menuItemImport);

        menuBar.add(menuFile);
        menuBar.add(menuAbout);
        //menuBar.add(menuOptions);

        setJMenuBar(menuBar);
    }
    
    public JMenuItem getMenuItemNew() {
        return menuItemNew;
    }

    public JMenuItem getMenuItemSave() {
        return menuItemSave;
    }

    public JMenuItem getMenuItemLoad() {
        return menuItemLoad;
    }

    public JMenuItem getMenuItemExport() {
        return menuItemExport;
    }

    public JMenuItem getMenuItemImport() {
        return menuItemImport;
    }

    public JMenuItem getMenuItemAbout() {
        return menuItemAbout;
    }

    public JToggleButton getMenuItemSlave() {
        return menuItemSlave;
    }

    public JButton getBtnAddSlave() {
        return btnAddSlave;
    }
    
    public JButton getBtnAddHdmi() {
        return btnAddHdmi;
    }

    public JButton getBtnAddScreen() {
        return btnAddScreen;
    }
    
    JButton getBtnAddSection(){
        return btnAddSection;
    }

    

    public JButton getBtnDeleteSlave() {
        return btnDeleteSlave;
    }

    public JButton getBtnDeleteHdmi() {
        return btnDeleteHdmi;
    }

    public JButton getBtnDeleteScreen() {
        return btnDeleteScreen;
    }

    public JButton getBtnDeleteSection() {
        return btnDeleteSection;
    }

    public JButton getBtnEditSlave() {
        return btnEditSlave;
    }

    public JButton getBtnEditHdmi() {
        return btnEditHdmi;
    }

    public JButton getBtnEditScreen() {
        return btnEditScreen;
    }

    public JButton getBtnEditSection() {
        return btnEditSection;
    }

    public JButton getBtnAddLayer() {
        return btnAddLayer;
    }

    
    
    
    public JLabel getLblWidthHdmi() {
        return lblWidthHdmi;
    }

    public JLabel getLblHeightHdmi() {
        return lblHeightHdmi;
    }

    public JLabel getLblNameScreen() {
        return lblNameScreen;
    }

    public JLabel getLblWidthScreen() {
        return lblWidthScreen;
    }

    public JLabel getLblHeightScreen() {
        return lblHeightScreen;
    }

    public JLabel getLblXScreen() {
        return lblXScreen;
    }

    public JLabel getLblYScreen() {
        return lblYScreen;
    }

    public JLabel getLblNameSection() {
        return lblNameSection;
    }
    
    
    
    
    /**
     * @return JTuggleButton Boton de bloquear ratio de aspecto
     */
    JToggleButton getTgbtnLock169(){
        return tgbtnLock169;
    }
    
    /**
     * @return JPanel El panel que sirve como base para todo
     */
    JPanel getPnlTable(){
        return pnlTable;
    }
    
    /**
     * @return Arreglo con todos los JSpinner (x, y, Width, Height)
     */
    JSpinner[] getArrSprSbnds(){
        return arrSprSbnds;
    }
    
    /**
     * @return JComboBox con el tamaño de la grid seleccionado por el usuario
     */
    JComboBox getCmbGridSize(){
        return cmbGridSize;
    }

    public JComboBox<String> getCmbSlave() {
        return cmbSlave;
    }

    public JComboBox<String> getCmbHdmi() {
        return cmbHdmi;
    }

    public JComboBox<String> getCmbLayer() {
        return cmbLayer;
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
        pnlTable = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        btnAddSection = new javax.swing.JButton();
        btnAddScreen = new javax.swing.JButton();
        btnAddSlave = new javax.swing.JButton();
        btnAddHdmi = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        cmbSlave = new javax.swing.JComboBox<>();
        btnEditSlave = new javax.swing.JButton();
        btnDeleteSlave = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnEditHdmi = new javax.swing.JButton();
        cmbHdmi = new javax.swing.JComboBox<>();
        btnDeleteHdmi = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblHeightHdmi = new javax.swing.JLabel();
        lblWidthHdmi = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblNameScreen = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblWidthScreen = new javax.swing.JLabel();
        lblHeightScreen = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnEditScreen = new javax.swing.JButton();
        btnDeleteScreen = new javax.swing.JButton();
        lblXScreen = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblYScreen = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblNameSection = new javax.swing.JLabel();
        btnDeleteSection = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        sprSbndsX = new javax.swing.JSpinner();
        sprSbndsY = new javax.swing.JSpinner();
        sprSbndsHeight = new javax.swing.JSpinner();
        sprSbndsWidth = new javax.swing.JSpinner();
        btnEditSection = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        cmbGridSize = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        tgbtnLock169 = new javax.swing.JToggleButton();
        jLabel14 = new javax.swing.JLabel();
        cmbLayer = new javax.swing.JComboBox<>();
        btnAddLayer = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LED Layout Editor (XML Tool for LiveSync-Compatible Files)");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setLayout(new java.awt.BorderLayout());

        pnlTable.setBackground(new java.awt.Color(0, 102, 102));
        pnlTable.setMinimumSize(new java.awt.Dimension(500, 500));
        pnlTable.setName(""); // NOI18N
        pnlTable.setPreferredSize(new java.awt.Dimension(500, 500));
        pnlTable.setLayout(new java.awt.BorderLayout());
        jPanel1.add(pnlTable, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(255, 51, 102));
        jPanel2.setMinimumSize(new java.awt.Dimension(500, 500));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.setPreferredSize(new java.awt.Dimension(290, 500));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel5.setMaximumSize(new java.awt.Dimension(360, 510));
        jPanel5.setMinimumSize(new java.awt.Dimension(360, 510));
        jPanel5.setPreferredSize(new java.awt.Dimension(360, 510));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("ADD"));
        jPanel6.setMinimumSize(new java.awt.Dimension(270, 160));
        jPanel6.setPreferredSize(new java.awt.Dimension(12, 110));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAddSection.setForeground(new java.awt.Color(0, 0, 153));
        btnAddSection.setText("ADD SECTION");
        btnAddSection.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel6.add(btnAddSection, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 250, 40));

        btnAddScreen.setForeground(new java.awt.Color(0, 0, 153));
        btnAddScreen.setText("ADD SCREEN");
        jPanel6.add(btnAddScreen, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 65, 250, -1));

        btnAddSlave.setForeground(new java.awt.Color(0, 0, 153));
        btnAddSlave.setText("ADD SLAVE");
        jPanel6.add(btnAddSlave, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 95, 120, -1));

        btnAddHdmi.setForeground(new java.awt.Color(0, 0, 153));
        btnAddHdmi.setText("ADD HDMI");
        jPanel6.add(btnAddHdmi, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 95, 120, -1));

        jPanel5.add(jPanel6);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("SLAVE"));
        jPanel7.setMinimumSize(new java.awt.Dimension(270, 100));
        jPanel7.setPreferredSize(new java.awt.Dimension(12, 85));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.add(cmbSlave, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 250, -1));

        btnEditSlave.setForeground(new java.awt.Color(135, 99, 96));
        btnEditSlave.setText("EDIT");
        jPanel7.add(btnEditSlave, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 120, -1));

        btnDeleteSlave.setForeground(new java.awt.Color(204, 0, 0));
        btnDeleteSlave.setText("DELETE");
        jPanel7.add(btnDeleteSlave, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 120, -1));

        jPanel5.add(jPanel7);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("HDMI"));
        jPanel4.setPreferredSize(new java.awt.Dimension(12, 140));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEditHdmi.setForeground(new java.awt.Color(135, 99, 96));
        btnEditHdmi.setText("EDIT");
        jPanel4.add(btnEditHdmi, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 120, -1));

        jPanel4.add(cmbHdmi, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 250, -1));

        btnDeleteHdmi.setForeground(new java.awt.Color(204, 0, 0));
        btnDeleteHdmi.setText("DELETE");
        jPanel4.add(btnDeleteHdmi, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 120, -1));

        jLabel1.setText("HEIGHT:");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, -1, 20));

        jLabel2.setText("WIDTH:");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, 20));

        lblHeightHdmi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHeightHdmi.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel4.add(lblHeightHdmi, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 70, 20));

        lblWidthHdmi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWidthHdmi.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel4.add(lblWidthHdmi, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 70, 20));

        jPanel5.add(jPanel4);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("SCREEN"));
        jPanel8.setPreferredSize(new java.awt.Dimension(282, 180));
        jPanel8.setRequestFocusEnabled(false);
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setText("NAME:");
        jPanel8.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 20));

        lblNameScreen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameScreen.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.add(lblNameScreen, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 210, 20));

        jLabel7.setText("WIDTH:");
        jPanel8.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, 20));

        lblWidthScreen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWidthScreen.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.add(lblWidthScreen, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 70, 20));

        lblHeightScreen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHeightScreen.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.add(lblHeightScreen, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 70, 20));

        jLabel10.setText("HEIGHT:");
        jPanel8.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, -1, 20));

        btnEditScreen.setForeground(new java.awt.Color(135, 99, 96));
        btnEditScreen.setText("EDIT");
        jPanel8.add(btnEditScreen, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 120, -1));

        btnDeleteScreen.setForeground(new java.awt.Color(204, 0, 0));
        btnDeleteScreen.setText("DELETE");
        jPanel8.add(btnDeleteScreen, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, 120, -1));

        lblXScreen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblXScreen.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.add(lblXScreen, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 70, 20));

        jLabel8.setText("X:");
        jPanel8.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, 20));

        jLabel18.setText("Y:");
        jPanel8.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, -1, 20));

        lblYScreen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblYScreen.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.add(lblYScreen, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 70, 20));

        jPanel5.add(jPanel8);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("SECTION"));
        jPanel9.setPreferredSize(new java.awt.Dimension(282, 170));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setText("X:");
        jPanel9.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, 20));

        jLabel13.setText("NAME:");
        jPanel9.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 20));

        lblNameSection.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameSection.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel9.add(lblNameSection, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 210, 20));

        btnDeleteSection.setForeground(new java.awt.Color(204, 0, 0));
        btnDeleteSection.setText("DELETE");
        jPanel9.add(btnDeleteSection, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 120, -1));

        jLabel16.setText("Y:");
        jPanel9.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, -1, 20));

        jLabel17.setText("WIDTH:");
        jPanel9.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, 20));

        jLabel20.setText("HEIGHT:");
        jPanel9.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, -1, 20));

        sprSbndsX.setModel(new javax.swing.SpinnerNumberModel());
        jPanel9.add(sprSbndsX, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 70, -1));

        sprSbndsY.setModel(new javax.swing.SpinnerNumberModel());
        jPanel9.add(sprSbndsY, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 70, -1));

        sprSbndsHeight.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        jPanel9.add(sprSbndsHeight, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 70, -1));

        sprSbndsWidth.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        jPanel9.add(sprSbndsWidth, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 70, -1));

        btnEditSection.setForeground(new java.awt.Color(135, 99, 96));
        btnEditSection.setText("EDIT");
        jPanel9.add(btnEditSection, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 120, -1));

        jPanel5.add(jPanel9);

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("TOOLS"));
        jPanel10.setPreferredSize(new java.awt.Dimension(282, 150));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setText("LAYER:");
        jPanel10.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, 20));

        cmbGridSize.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "3", "5", "10", "50", "100", "500", "1000" }));
        jPanel10.add(cmbGridSize, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 170, -1));

        jLabel15.setText("GRID SIZE:");
        jPanel10.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, 20));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "16:9" }));
        jPanel10.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 60, -1));

        tgbtnLock169.setText("LOCK");
        jPanel10.add(tgbtnLock169, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 49, 100, -1));

        jLabel14.setText("ASPECT RATIO:");
        jPanel10.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, 20));

        jPanel10.add(cmbLayer, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 110, -1));

        btnAddLayer.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAddLayer.setText("+");
        jPanel10.add(btnAddLayer, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, 50, 25));

        jPanel5.add(jPanel10);

        jPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 286, 780));

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.LINE_END);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddHdmi;
    private javax.swing.JButton btnAddLayer;
    private javax.swing.JButton btnAddScreen;
    private javax.swing.JButton btnAddSection;
    private javax.swing.JButton btnAddSlave;
    private javax.swing.JButton btnDeleteHdmi;
    private javax.swing.JButton btnDeleteScreen;
    private javax.swing.JButton btnDeleteSection;
    private javax.swing.JButton btnDeleteSlave;
    private javax.swing.JButton btnEditHdmi;
    private javax.swing.JButton btnEditScreen;
    private javax.swing.JButton btnEditSection;
    private javax.swing.JButton btnEditSlave;
    private javax.swing.JComboBox<String> cmbGridSize;
    private javax.swing.JComboBox<String> cmbHdmi;
    private javax.swing.JComboBox<String> cmbLayer;
    private javax.swing.JComboBox<String> cmbSlave;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lblHeightHdmi;
    private javax.swing.JLabel lblHeightScreen;
    private javax.swing.JLabel lblNameScreen;
    private javax.swing.JLabel lblNameSection;
    private javax.swing.JLabel lblWidthHdmi;
    private javax.swing.JLabel lblWidthScreen;
    private javax.swing.JLabel lblXScreen;
    private javax.swing.JLabel lblYScreen;
    private javax.swing.JPanel pnlTable;
    private javax.swing.JSpinner sprSbndsHeight;
    private javax.swing.JSpinner sprSbndsWidth;
    private javax.swing.JSpinner sprSbndsX;
    private javax.swing.JSpinner sprSbndsY;
    private javax.swing.JToggleButton tgbtnLock169;
    // End of variables declaration//GEN-END:variables
}
