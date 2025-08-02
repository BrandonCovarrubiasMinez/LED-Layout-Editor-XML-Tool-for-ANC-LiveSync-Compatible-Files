/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package zMain;

import Data.C_Data;
import HdmiOutput.C_HdmiOutput;
import HdmiOutputForm.C_HdmiOutputForm;
import Layer.C_Layer;
import Screen.C_Screen;
import Screen.V_Screen;
import ScreenForm.C_ScreenForm;
import Section.C_Section;
import SectionForm.C_SectionForm;
import Slave.C_Slave;
import SlaveForm.C_SlaveForm;
import XML.C_XMLGenerator;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

class M_Main{
    
    private float floatZoom;
    private boolean boolFlagHumanChange;
    
    private final float ZOOM_DELTA= 0.1f;  //El tamaño del cambio del zoom
    private final Point pntMouseTableHoldPoint;
    private final Point pntTableHoldPoint;
    private static final int DEFAULT_SIZE_WIDTH= 250;
    private static final int DEFAULT_SIZE_HEIGHT= 250;
    private static final int DEFAULT_LOCATION_X= 0;
    private static final int DEFAULT_LOCATION_Y= 0;
    private static final int DEFAULT_INFO_VALUE= 0;
    static final String DEFAULT_INFO_TEXT= "";
    
    public M_Main(){
        floatZoom= 1.0f;
        pntMouseTableHoldPoint= new Point(0,0);
        pntTableHoldPoint= new Point(0,0);
        //
        boolFlagHumanChange=true;
        
        


    }
    
    /**
     * Se crea una nueva pantalla con ayuda de la clase ScreenForm. Se pasa la vista de Main como argumento solo para parametro de JDialog del formulario
     * @param dataC clase de almacenamiento de datos
     */
    void newScreen(V_Main mainV, C_Main mainC, C_Data dataC){
        C_ScreenForm screenFormC= new C_ScreenForm(false, mainV, mainC, dataC);
        screenFormC.show();

        dataC.getCurrentHdmiOutput().refreshGlobalZoom(floatZoom);
        
        mainC.refreshItemsActionButtons();
                /*
                //Lo que se hace dependiendo del resultado de la ventana de nueva pantalla
                switch(screenFormC.getIntResult()){
                    case C_ScreenForm.RES_EXSUCCESSFUL:
                        switchScreen(arrpnlscrScreens.size()-1, arrpnlscrScreens, mView.getPnlCanvas());
                        updateCmbScreens(cmbScreens, screenFormC.getScreenName());
                        break;
                }*/
    }



    void addFunctionalityToButtonAddSection(JButton btnSecAdd, C_Main mainC, C_Data dataC){
        btnSecAdd.addActionListener((evt) -> {
            C_SectionForm sectionFormC= new C_SectionForm(false, mainC, dataC);
            sectionFormC.show();

            dataC.getCurrentHdmiOutput().refreshGlobalZoom(floatZoom);
            mainC.refreshItemsActionButtons();
            
        });
    }

    void addFunctionalityToButtonAddScreen(JButton btnScrnAdd, V_Main mainV, C_Main mainC, C_Data dataC){
        btnScrnAdd.addActionListener(evt -> newScreen(mainV, mainC, dataC));
    }
    
    void addFunctionalityToButtonAddLayer(JButton btnAddLayer,JComboBox<String> cmbLayer, C_Main mainC, C_Data dataC) {
        btnAddLayer.addActionListener((ActionEvent evt) -> {
            boolFlagHumanChange= false;
            C_Layer layerC= new C_Layer();
            
            layerC.setName(JOptionPane.showInputDialog("Enter the layer name"));
            
            dataC.add(layerC, mainC);
            mainC.refreshComboLayer();
            cmbLayer.setSelectedIndex(cmbLayer.getItemCount()-1);
            
            boolFlagHumanChange= true;
            
        });
    }

    void addFunctionalityToPnlTable(JPanel pnlTble, C_Data dataC){
        //Función de mover el canvas por la pantalla con el click izquierdo--


        //Por si el movimiento se intenta fuera del pnlCanvas
        pnlTble.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent evt) {
                if(dataC.getCurrentHdmiOutput()!= null){
                    setMouseTableHoldPoint(evt.getX(), evt.getY());
                    setTableHoldPoint(dataC.getCurrentHdmiOutput().getX(), dataC.getCurrentHdmiOutput().getY());
                }
            }

        });

        pnlTble.addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseDragged(MouseEvent evt) {
                if(dataC.getCurrentHdmiOutput()!= null){
                    if(evt.getModifiers() == MouseEvent.BUTTON3_MASK){
                        //Mueve el panel. Depende de la ubicacion inicial del panel el movimiento en si y la ubicacion del mouse cuando se inicia.
                        dataC.getCurrentHdmiOutput().setLocation( pntTableHoldPoint.x+evt.getX() - pntMouseTableHoldPoint.x, pntTableHoldPoint.y+evt.getY() - pntMouseTableHoldPoint.y);
                    }
                }
            }
        });
        //--

        //Centra el canvas al mouse
        pnlTble.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent evt) {
                if(dataC.getCurrentHdmiOutput() != null){
                    int mouseX = evt.getX();
                    int mouseY = evt.getY();

                    //Coordenadas relativas al canvas antes de zoom
                    int relMouseX = mouseX - dataC.getCurrentHdmiOutput().getX();
                    int relMouseY = mouseY - dataC.getCurrentHdmiOutput().getY();

                    float previousZoom = floatZoom;

                    //Calculamos un factor suave de zoom
                    double zoomAmountPerNotch = 1.1; //10% de aumento por "notch" del scroll
                    int wheelRotation = evt.getWheelRotation(); //Positivo si baja, negativo si sube
                    if (wheelRotation < 0) {
                        floatZoom *= Math.pow(zoomAmountPerNotch, -wheelRotation); //Scroll arriba (acercar)
                    } else {
                        floatZoom /= Math.pow(zoomAmountPerNotch, wheelRotation); //Scroll abajo (alejar)
                    }

                    //Clampear el zoom para que no explote
                    floatZoom = Math.max(0.1f, Math.min(10.0f, floatZoom)); //Entre 10% y 1000%

                    float scaleFactor = floatZoom / previousZoom;

                    dataC.getCurrentHdmiOutput().refreshGlobalZoom(floatZoom); //Actualiza zoom

                    int newCanvasX = (int) (mouseX - relMouseX * scaleFactor);
                    int newCanvasY = (int) (mouseY - relMouseY * scaleFactor);

                    dataC.getCurrentHdmiOutput().setLocation(newCanvasX, newCanvasY);
                }
            }
        });


    }

    private void setMouseTableHoldPoint(int intX, int intY){
        pntMouseTableHoldPoint.setLocation(intX, intY);
    }

    private void setTableHoldPoint(int intX, int intY){
        pntTableHoldPoint.setLocation(intX, intY);
    }

    private void zooomOut(){
        floatZoom= Math.min(floatZoom + ZOOM_DELTA, 5);
        //floatZoom= Math.round(floatZoom;
    }

    private void zoomIn(){
        floatZoom= (float)Math.max(floatZoom - ZOOM_DELTA, 0.1);
        //floatZoom= Math.round(floatZoom);
    }


    //Añade el elemento a un combo box

    private void updateCmbScreens(JComboBox cmbScreens, String strScreenName){
        cmbScreens.addItem(strScreenName);
        cmbScreens.setSelectedItem(strScreenName);
        cmbScreens.repaint();
    }
    
    void switchScreen(int intID, ArrayList arrpnlscrScreens, JPanel pnlCanvas){
        //Oculta todos los elementos actuales de la vista utilizando un iterador para evitar problemas de concurrencia.
        Iterator<V_Screen> iterator = arrpnlscrScreens.iterator();
        while (iterator.hasNext()) {
            iterator.next().setVisible(false);  //Oculta cada elemento de la vista.
        }

        //Deja visible unicamente el que se quiere mostrar
        ((V_Screen)(arrpnlscrScreens.get(intID))).setVisible(true);

        pnlCanvas.repaint();

    }
    
    void refreshHdmiInfo(JLabel lblWidthHdmi, JLabel lblHeightHdmi, C_HdmiOutput hdmiC) {
        if(hdmiC != null){
            lblWidthHdmi.setText(""+hdmiC.getTrueWidth());
            lblHeightHdmi.setText(""+hdmiC.getTrueHeight());
            
        }else{
            lblWidthHdmi.setText(""+DEFAULT_INFO_VALUE);
            lblHeightHdmi.setText(""+DEFAULT_INFO_VALUE);
        }
    }

    void refreshScreenInfo(JLabel lblWidthScreen, JLabel lblHeightScreen, JLabel lblXScreen, JLabel lblYScreen, JLabel lblNameScreen, C_Screen screenC) {
        if(screenC != null){
            lblWidthScreen.setText(""+screenC.getTrueSize().width);
            lblHeightScreen.setText(""+screenC.getTrueSize().height);
            lblXScreen.setText(""+screenC.getTrueLocation().x);
            lblYScreen.setText(""+screenC.getTrueLocation().y);
            lblNameScreen.setText(screenC.getName());
        }else{
            lblWidthScreen.setText(""+DEFAULT_INFO_VALUE);
            lblHeightScreen.setText(""+DEFAULT_INFO_VALUE);
            lblXScreen.setText(""+DEFAULT_INFO_VALUE);
            lblYScreen.setText(""+DEFAULT_INFO_VALUE);
            lblNameScreen.setText(DEFAULT_INFO_TEXT);
        }
    }
    
    /**
     * Actualiza el valor de los JSpinners (x, y, width, height) asi como el nombre mostrado en el apartado de informacion de la seccion
     * @param arrSprSbnds Arreglo que contiene los componentes
     * @param lblName Etiqueta de nombre
     * @param sectionC La seccion actual
     */
    void refreshSectionInfo(JSpinner[] arrSprSbnds, JLabel lblName, C_Section sectionC){
        if(sectionC != null){
            
            setJSpinnerValue(arrSprSbnds[V_Main.SPINNER_X], sectionC.getTrueLocation().x);
            setJSpinnerValue(arrSprSbnds[V_Main.SPINNER_Y], sectionC.getTrueLocation().y);
            setJSpinnerValue(arrSprSbnds[V_Main.SPINNER_WIDTH], sectionC.getTrueSize().width);
            setJSpinnerValue(arrSprSbnds[V_Main.SPINNER_HEIGHT], sectionC.getTrueSize().height);
            lblName.setText(sectionC.getName());
        }else{
            setJSpinnerValue(arrSprSbnds[V_Main.SPINNER_X], DEFAULT_INFO_VALUE);
            setJSpinnerValue(arrSprSbnds[V_Main.SPINNER_Y], DEFAULT_INFO_VALUE);
            setJSpinnerValue(arrSprSbnds[V_Main.SPINNER_WIDTH], DEFAULT_INFO_VALUE);
            setJSpinnerValue(arrSprSbnds[V_Main.SPINNER_HEIGHT], DEFAULT_INFO_VALUE);
            
            lblName.setText(DEFAULT_INFO_TEXT);
        }
    }
    
    /**
     * Cambia el valor de un JSpinner sin activar el evento de stateChange ya que sis se activa se genera un problema con el tamaño Verdadero y el tamaño relativo del zoom en los paneles de secciones
     * @param sprS
     * @param intNewValue
     */
    private void setJSpinnerValue(JSpinner sprS, int intNewValue){
        boolFlagHumanChange= false;
        sprS.setValue(intNewValue);
        boolFlagHumanChange= true;
    }


     void remplaceHdmiOutput(JPanel pnlTable, C_HdmiOutput hdmiC){
        pnlTable.removeAll();
        if(hdmiC!= null)
            pnlTable.add(hdmiC.getView());
        pnlTable.repaint();

        if(hdmiC!= null){
            //floatZoom= 0.2f;
            hdmiC.refreshGlobalZoom(floatZoom);
            hdmiC.setLocation(pnlTable.getWidth()/2 - hdmiC.getWidth()/2, pnlTable.getHeight()/2 - hdmiC.getHeight()/2);
        }
    }

    //void setCurrentHdmiLocationWithCursor(int x, int y) {
    //}

    /**
     * Devuelve el valor de el combo del tamalño de la grid como un entero
     * @param cmbGridSize
     * @return Tamaño de la grid
     */
    int getGridSize(JComboBox cmbGridSize) {
        return Integer.valueOf(String.valueOf(cmbGridSize.getSelectedItem()));
    }

    boolean isAspectRatioLock(JToggleButton tgbtnLock169) {
        return tgbtnLock169.isSelected();
    }

    void addFunctionalityToBoundsSpinners(JSpinner[] arrSprSbnds, C_Main mainC, C_Data dataC) {

        arrSprSbnds[V_Main.SPINNER_X].addChangeListener((ChangeEvent evt) -> {
            if(boolFlagHumanChange)
                dataC.getCurrentSection().setTrueLocation((int) arrSprSbnds[V_Main.SPINNER_X].getValue(), dataC.getCurrentSection().getTrueLocation().y, mainC.getGridSize());
        });

        arrSprSbnds[V_Main.SPINNER_Y].addChangeListener((ChangeEvent evt) -> {
            if(boolFlagHumanChange)
                dataC.getCurrentSection().setTrueLocation(dataC.getCurrentSection().getTrueLocation().x,  (int) arrSprSbnds[V_Main.SPINNER_Y].getValue(), mainC.getGridSize());
        });

        arrSprSbnds[V_Main.SPINNER_WIDTH].addChangeListener((ChangeEvent evt) -> {
            if(boolFlagHumanChange)
                if(mainC.isAspectRatioLock()){
                    //Respeta la relacion de aspecto
                    float floatTrueWidth= (int) arrSprSbnds[V_Main.SPINNER_WIDTH].getValue();
                    float floatTrueHeight= (floatTrueWidth/C_Main.ASPECT_HORIZONTAL)*C_Main.ASPECT_VERTICAL;

                    dataC.getCurrentSection().setTrueSize(Math.round(floatTrueWidth) , Math.round(floatTrueHeight), mainC.getGridSize());

                    //Actualiza el JSpinner pues se modifico el valor de altura por codigo
                    setJSpinnerValue(arrSprSbnds[V_Main.SPINNER_HEIGHT], dataC.getCurrentSection().getTrueSize().height);
                }else
                    dataC.getCurrentSection().setTrueSize((int) arrSprSbnds[V_Main.SPINNER_WIDTH].getValue() , dataC.getCurrentSection().getTrueSize().height, mainC.getGridSize());
        });

        arrSprSbnds[V_Main.SPINNER_HEIGHT].addChangeListener((ChangeEvent evt) -> {
            if(boolFlagHumanChange)
                if(mainC.isAspectRatioLock()){
                    //Respeta la relacion de aspecto
                    float floatTrueHeight= (int) arrSprSbnds[V_Main.SPINNER_HEIGHT].getValue();
                    float floatTrueWidth= (floatTrueHeight/C_Main.ASPECT_VERTICAL)*C_Main.ASPECT_HORIZONTAL;

                    dataC.getCurrentSection().setTrueSize(Math.round(floatTrueWidth) , Math.round(floatTrueHeight), mainC.getGridSize());

                    //Actualiza el JSpinner pues se modifico el valor de anchura por codigo
                    setJSpinnerValue(arrSprSbnds[V_Main.SPINNER_WIDTH], dataC.getCurrentSection().getTrueSize().width);

                }else
                    dataC.getCurrentSection().setTrueSize(dataC.getCurrentSection().getTrueSize().width,  (int) arrSprSbnds[V_Main.SPINNER_HEIGHT].getValue(), mainC.getGridSize());
        });
    }

    void addFunctionalityToButtonAddHdmiOutput(JButton btnHdmiAdd, JComboBox<String> cmbHdmi, JPanel pnlTable, V_Main View, C_Main mainC, C_Data dataC) {
        btnHdmiAdd.addActionListener((ActionEvent evt) -> {
            //Al terminar el formulario añade y actualiza iCurrentHdmi
            C_HdmiOutputForm hdmiForm= new C_HdmiOutputForm(false, View, mainC, dataC);
            hdmiForm.show();

            if(hdmiForm.isSuccessful()){
                boolFlagHumanChange= false;
                remplaceHdmiOutput(pnlTable, dataC.getCurrentHdmiOutput());
                refreshComboHdmiOutput(cmbHdmi, dataC);
                cmbHdmi.setSelectedIndex(cmbHdmi.getItemCount()-1);
                mainC.refreshComboLayer();
                boolFlagHumanChange= true;
                
                mainC.refreshItemsActionButtons();
            }
        });
    }
    
    

    void addFunctionalityToComboBoxSlaves(JComboBox<String> cmbSlave, JComboBox<String> cmbHdmi,JComboBox<String> cmbLayer, JPanel pnlTable, C_Main mainC, C_Data dataC) {
        cmbSlave.addActionListener((ActionEvent evt) -> {
            if(boolFlagHumanChange){
                //El cmbSlave esta ordenado igual que el arreglo de slaves
                dataC.setCurrentSlave(cmbSlave.getItemCount() !=0 ? dataC.getSlaves().get(cmbSlave.getSelectedIndex()) : null , mainC);
                remplaceHdmiOutput(pnlTable, dataC.getCurrentHdmiOutput());
                boolFlagHumanChange= false;
                refreshComboHdmiOutput(cmbHdmi, dataC);
                refreshComboLayer(cmbLayer, dataC);
                dataC.setCurrentLayer(dataC.getLayer(cmbLayer.getSelectedItem().toString()), mainC);
                
                
                //if(dataC.getCurrentLayer().getName()!= null)
                //    cmbLayer.setSelectedItem(dataC.getCurrentLayer().getName());
                boolFlagHumanChange= true;
                
                mainC.refreshItemsActionButtons();
            }
        });
    }
    
    void addFunctionalityToComboBoxHdmiOutputs(JComboBox<String> cmbHdmiOutput, JPanel pnlTable, C_Main mainC, C_Data dataC) {
        cmbHdmiOutput.addActionListener((ActionEvent evt) -> {
            if(boolFlagHumanChange){
                dataC.setCurrentHdmi(dataC.getHdmiOutput(cmbHdmiOutput.getSelectedIndex()), mainC);
                remplaceHdmiOutput(pnlTable, dataC.getCurrentHdmiOutput());
                //dataC.getCurrentHdmi().refreshGlobalZoom(floatZoom);
                
                boolFlagHumanChange=false;
                mainC.refreshComboLayer();
                boolFlagHumanChange=true;
                
                mainC.refreshItemsActionButtons();
            }
        });
    }
    
    void addFunctionalityToComboBoxLayers(JComboBox<String> cmbLayer, C_Main mainC, C_Data dataC) {
        cmbLayer.addActionListener((ActionEvent evt) -> {
            if(boolFlagHumanChange){
                dataC.setCurrentLayer(dataC.getLayer(cmbLayer.getSelectedItem().toString()), mainC);
            }
        });
    }

    void addFunctionalityToMenuItemExport(JMenuItem menuItemExport, C_Data dataC) {
        menuItemExport.addActionListener((ActionEvent evt) -> {
            C_XMLGenerator xmlGenerator= new C_XMLGenerator();
            xmlGenerator.generateXML(dataC);
        });
    }
    
    void addFunctionalityToMenuItemImport(JMenuItem menuItemImport, C_Main mainC, C_Data dataC) {
        menuItemImport.addActionListener((ActionEvent evt) -> {
            
            int iAnswer= JOptionPane.showConfirmDialog(null, "All progress will be lost. Do you wish to continue?.", "Warning", JOptionPane.YES_NO_OPTION);
            
            if(iAnswer == JOptionPane.YES_OPTION){
                C_XMLGenerator xmlGenerator= new C_XMLGenerator();
                xmlGenerator.importDisplayDoc(mainC);
            }
            
        });
    }

    public void addFunctionalityToMenuItemAbout(JMenuItem menuItemAbout, C_Data dataC) {
        menuItemAbout.addActionListener((ActionEvent evt) -> {
            JOptionPane.showMessageDialog(null, "LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) – Beta v1.00\n"
                    + "© 2025 Brandon Iván Covarrubias Minez. Some rights reserved.\n" +
                      "Not affiliated with ANC or the LiveSync software.\n" +
                      "Licensed under Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0).\n"
                    + "Visit https://creativecommons.org/licenses/by-nc/4.0/ for license terms.", "About", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public void addFunctionalityToMenuItemSave(JMenuItem menuItemSave, C_Data dataC) {
        menuItemSave.addActionListener((ActionEvent evt) -> {
            dataC.save();
        });
        
    }

    public void addFunctionalityToMenuItemLoad(JMenuItem menuItemLoad, C_Main mainC, C_Data dataC) {
        menuItemLoad.addActionListener((ActionEvent evt) -> {
            C_Data newDataC= null;
            
            newDataC=dataC.load();
            
            //Remplaza la data con la cargada si es valida
            if(newDataC != null){
                
                C_Main newMainC= new C_Main(newDataC);
                mainC.dispose();
                
            }
        });
    }

    void addFunctionalityToButtonDeleteSlave(JButton btnDeleteSlave, C_Main mainC, C_Data dataC) {
        btnDeleteSlave.addActionListener((ActionEvent evt) -> {
            dataC.delete(dataC.getCurrentSlave(), mainC);
            mainC.refreshComboSlave();
            mainC.refreshComboHdmiOutput();
            mainC.refreshItemsActionButtons();
        });
    }

    void addFunctionalityToButtonDeleteHdmi(JButton btnDeleteHdmi, C_Main mainC, C_Data dataC) {
        btnDeleteHdmi.addActionListener((ActionEvent evt) -> {
            dataC.delete(dataC.getCurrentHdmiOutput(), mainC);
            mainC.refreshItemsActionButtons();
                
        });
    }

    void addFunctionalityToButtonDeleteScreen(JButton btnDeleteScreen, C_Main mainC, C_Data dataC) {
        btnDeleteScreen.addActionListener((ActionEvent evt) -> {
            dataC.delete(dataC.getCurrentScreen(), mainC);
            mainC.refreshItemsActionButtons();
            
        });
    }

    void addFunctionalityToButtonDeleteSection(JButton btnDeleteSection, C_Main mainC, C_Data dataC) {
        btnDeleteSection.addActionListener((ActionEvent evt) -> {
            dataC.delete(dataC.getCurrentSection(), mainC);
            mainC.refreshItemsActionButtons();
        });
    }

    void addFunctionalityToButtonEditSlave(JButton btnEditSlave, JComboBox<String> cmbSlave, C_Main mainC, C_Data dataC) {
        btnEditSlave.addActionListener((ActionEvent evt) -> {
            C_SlaveForm slaveFormC= new C_SlaveForm(true, mainC, dataC);
            slaveFormC.show();
            
            if(slaveFormC.isSuccessful()){
                boolFlagHumanChange= false;
                mainC.refreshComboSlave();
                cmbSlave.setSelectedItem(dataC.getCurrentSlave().getName());
                boolFlagHumanChange=true;
                
                mainC.refreshItemsActionButtons();
            }
            
        });
    }

    void addFunctionalityToButtonEditHdmi(JButton btnEditHdmi, JComboBox<String> cmbHdmi, JPanel pnlTable, V_Main View, C_Main mainC, C_Data dataC) {
        btnEditHdmi.addActionListener((ActionEvent evt) -> {
            //Al terminar el formulario añade y actualiza iCurrentHdmi
            C_HdmiOutputForm hdmiForm= new C_HdmiOutputForm(true, View, mainC, dataC);
            hdmiForm.show();

            if(hdmiForm.isSuccessful()){
                mainC.refreshHdmiInfo();
                dataC.getCurrentHdmiOutput().refreshGlobalZoom(floatZoom);
                //mainC.refreshTable();
                //mainC.refreshItemsActionButtons();
            }
        });
    }

    void addFunctionalityToButtonEditScreen(JButton btnEditScreen, V_Main mainV, C_Main mainC, C_Data dataC) {
        btnEditScreen.addActionListener((ActionEvent evt) -> {
            C_ScreenForm screenFormC= new C_ScreenForm(true, mainV, mainC, dataC);
            screenFormC.show();
            
            dataC.getCurrentHdmiOutput().refreshGlobalZoom(floatZoom);

            mainC.refreshItemsActionButtons();
        });
    }
    
    void addFunctionalityToButtonEditSection(JButton btnEditSection, V_Main View, C_Main mainC, C_Data dataC) {
        btnEditSection.addActionListener((ActionEvent evt) -> {
            C_SectionForm sectionFormC= new C_SectionForm(true, mainC, dataC);
            sectionFormC.show();

            dataC.getCurrentHdmiOutput().refreshGlobalZoom(floatZoom);
            mainC.refreshItemsActionButtons();
        });
    }

    void addFunctionalityToButtonAddSlave(JButton btnAddSlave, JComboBox<String> cmbSlave, C_Main mainC, C_Data dataC) {
        btnAddSlave.addActionListener((ActionEvent evt) -> {
            C_SlaveForm slaveFormC= new C_SlaveForm(false, mainC, dataC);
            slaveFormC.show();
            
            if(slaveFormC.isSuccessful()){
                boolFlagHumanChange= false;
                mainC.refreshComboSlave();
                //Cambia el slave acual al ultimo agragado
                dataC.setCurrentSlave(dataC.getSlaves().get(dataC.getSlaves().size()-1) , mainC);
                cmbSlave.setSelectedItem(dataC.getCurrentSlave().getName());
                boolFlagHumanChange=true;
                
                mainC.refreshItemsActionButtons();
            }
            
        });
    }

    void refreshComboSlave(JComboBox<String> cmbSlave, C_Data dataC) {
        cmbSlave.removeAllItems();
        
        for(Iterator<C_Slave> iterator = dataC.getSlaves().iterator(); iterator.hasNext();) {
            C_Slave slaveC = iterator.next();
            cmbSlave.addItem(slaveC.getName());
        }
        
        //cmbSlave.setSelectedItem(dataC.getCurrentSlave().getName());
    }
    
    void refreshComboHdmiOutput(JComboBox<String> cmbHdmi, C_Data dataC){
        cmbHdmi.removeAllItems();
        int c=0;
        for(Iterator<C_HdmiOutput> iterator = dataC.getHdmis().iterator(); iterator.hasNext();) {
            C_HdmiOutput hdmiC = iterator.next();
            
            
            if(dataC.getCurrentSlave() == hdmiC.getSlaveC()){
                c++;
                cmbHdmi.addItem("HDMI #"+c+" ("+hdmiC.getTrueWidth()+","+hdmiC.getTrueHeight()+")");
            }
            
        }
    }
    
    void refreshComboLayer(JComboBox<String> cmbLayer, C_Data dataC){
        boolFlagHumanChange=false;
        cmbLayer.removeAllItems();
        
        for(Iterator<C_Layer> iterator = dataC.getLayers().iterator(); iterator.hasNext();) {
            C_Layer layerC = iterator.next();
            
            if(layerC.getOwner() == dataC.getCurrentHdmiOutput() )
                cmbLayer.addItem(layerC.getName());
        }
        boolFlagHumanChange=true;
    }

    /**
     * Actualiza la disponibilidad de los botones dependiendo si la funcion tiene un target.
     * @param cmbSlave
     * @param cmbHdmi
     * @param btnDeleteSlave
     * @param btnDeleteHdmi
     * @param btnDeleteScreen
     * @param btnDeleteSection
     * @param btnEditSlave
     * @param btnEditHdmi
     * @param btnEditScreen
     * @param dataC 
     */
    void refreshItemsActionButtons(JComboBox<String> cmbSlave, JComboBox<String> cmbHdmi, JComboBox<String> cmbLayer,
            JButton btnDeleteSlave, JButton btnDeleteHdmi, JButton btnDeleteScreen, 
            JButton btnDeleteSection, JButton btnEditSlave, JButton btnEditHdmi, 
            JButton btnEditScreen, JButton btnEditSection,
            JSpinner[] arrSprSbnds,
            JButton btnAddSlave, JButton btnAddHdmi, JButton btnAddScreen, JButton btnAddSection,JButton btnAddLayer, C_Data dataC) {
        
        //Combos
        cmbSlave.setEnabled(dataC.getCurrentSlave() != null);
        cmbHdmi.setEnabled(dataC.getCurrentHdmiOutput() != null);
        cmbLayer.setEnabled(dataC.getCurrentHdmiOutput() != null);
        
        //Botones add
        btnAddHdmi.setEnabled(dataC.getCurrentSlave() != null);
        btnAddScreen.setEnabled(dataC.getCurrentHdmiOutput() != null);
        btnAddSection.setEnabled(dataC.getCurrentScreen() != null);
        btnAddLayer.setEnabled(dataC.getCurrentHdmiOutput() != null);
        
        //Botones delete
        btnDeleteSlave.setEnabled(dataC.getCurrentSlave() != null);
        btnDeleteHdmi.setEnabled(dataC.getCurrentHdmiOutput() != null);
        btnDeleteScreen.setEnabled(dataC.getCurrentScreen() != null);
        btnDeleteSection.setEnabled(dataC.getCurrentSection() != null);
        
        //Botones edit
        btnEditSlave.setEnabled(dataC.getCurrentSlave() != null);
        btnEditHdmi.setEnabled(dataC.getCurrentHdmiOutput() != null);
        btnEditScreen.setEnabled(dataC.getCurrentScreen() != null);
        btnEditSection.setEnabled(dataC.getCurrentSection() != null);
        
        //Bounds de Section
        arrSprSbnds[V_Main.SPINNER_X].setEnabled(dataC.getCurrentSection() != null);
        arrSprSbnds[V_Main.SPINNER_Y].setEnabled(dataC.getCurrentSection() != null);
        arrSprSbnds[V_Main.SPINNER_WIDTH].setEnabled(dataC.getCurrentSection() != null);
        arrSprSbnds[V_Main.SPINNER_HEIGHT].setEnabled(dataC.getCurrentSection() != null);
        
    }

    float getZoom(){
        return floatZoom;
    }

    
    
}

