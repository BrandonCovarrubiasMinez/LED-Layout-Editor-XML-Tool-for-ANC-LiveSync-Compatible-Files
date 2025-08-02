/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package zMain;

import Data.C_Data;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

public final class C_Main{    
    //Data
    private final C_Data dataC; 
    //
    private final M_Main Model;
    private transient final V_Main View;
    
    public static final float ASPECT_HORIZONTAL= 16;
    public static final float ASPECT_VERTICAL= 9;
    
    public C_Main(){
        
        Model= new M_Main();
        View= new V_Main();
        dataC= new C_Data();
        
        //Para los atajos de teclado
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyDispatcher());
        
        addFunctionalityToButtonAddSlave();
        addFunctionalityToButtonAddSection();
        addFunctionalityToButtonAddScreen();
        addFunctionalityToButtonAddLayer();
        addFunctionalityToPnlTable();
        addFunctionalityToBoundsSpinners();
        addFunctionalityToButtonAddHdmiOutput();
        addFunctionalityToComboBoxHdmiOutputs();
        addFunctionalityToComboBoxSlaves();
        addFunctionalityToComboBoxLayers();
        //Botones delete
        addFunctionalityToButtonDeleteSlave();
        addFunctionalityToButtonDeleteHdmi();
        addFunctionalityToButtonDeleteScreen();
        addFunctionalityToButtonDeleteSection();
        //Botones Edit
        addFunctionalityToButtonEditSlave();
        addFunctionalityToButtonEditHdmi();
        addFunctionalityToButtonEditScreen();
        addFunctionalityToButtonEditSection();
        
        addFunctionalityToMenuItemExport();
        addFunctionalityToMenuItemImport();
        
        addFunctionalityToMenuItemAbout();
        addFunctionalityToMenuItemSave();
        addFunctionalityToMenuItemLoad();
                
        refreshItemsActionButtons();

        View.repaint();
        
        View.setVisible(true);
    }
    
    //Se llama cuando se esta cargando desde un archivo
    public C_Main(C_Data dataC){
        
        Model= new M_Main();
        View= new V_Main();
        this.dataC= dataC;
        
        //Para los atajos de teclado
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyDispatcher());
        
        addFunctionalityToButtonAddSlave();
        addFunctionalityToButtonAddSection();
        addFunctionalityToButtonAddScreen();
        addFunctionalityToButtonAddLayer();
        addFunctionalityToPnlTable();
        addFunctionalityToBoundsSpinners();
        addFunctionalityToButtonAddHdmiOutput();
        addFunctionalityToComboBoxHdmiOutputs();
        addFunctionalityToComboBoxSlaves();
        addFunctionalityToComboBoxLayers();
        //Botones delete
        addFunctionalityToButtonDeleteSlave();
        addFunctionalityToButtonDeleteHdmi();
        addFunctionalityToButtonDeleteScreen();
        addFunctionalityToButtonDeleteSection();
        //Botones Edit
        addFunctionalityToButtonEditSlave();
        addFunctionalityToButtonEditHdmi();
        addFunctionalityToButtonEditScreen();
        addFunctionalityToButtonEditSection();
        
        addFunctionalityToMenuItemExport();
        addFunctionalityToMenuItemImport();
        
        addFunctionalityToMenuItemAbout();
        addFunctionalityToMenuItemSave();
        addFunctionalityToMenuItemLoad();
                
        refreshItemsActionButtons();

        //View.getCmbHdmi().addItem("Nuevo 1");
        //View.getCmbHdmi().addItem("Nuevo 2");
        
        
        
        refreshTable();
        
        View.repaint();
        
        View.setVisible(true);
    }

    public void refreshHdmiInfo(){
        Model.refreshHdmiInfo(View.getLblWidthHdmi(), View.getLblHeightHdmi(), dataC.getCurrentHdmiOutput());
    }
    
    public void refreshScreenInfo(){
        Model.refreshScreenInfo(View.getLblWidthScreen(), View.getLblHeightScreen(), View.getLblXScreen(), View.getLblYScreen(), View.getLblNameScreen(), dataC.getCurrentScreen());
    }
    
    public void refreshSectionInfo(){
        Model.refreshSectionInfo(View.getArrSprSbnds(), View.getLblNameSection(), dataC.getCurrentSection());
    }
    
    public void refreshComboSlave(){
        Model.refreshComboSlave(View.getCmbSlave(), dataC);
    }
    
    void refreshComboHdmiOutput() {
        Model.refreshComboHdmiOutput(View.getCmbHdmi(), dataC);
    }
    
    public void refreshComboLayer(){
        Model.refreshComboLayer(View.getCmbLayer(), dataC);
    }
    
    public int getGridSize() {
        return Model.getGridSize(View.getCmbGridSize());
    }
    
    public void dispose(){
        View.dispose();
    }

    /**
     * Retorna si el boton de conservar ratio de aspecto esta seleccionado
     * @return 
     */
    public boolean isAspectRatioLock() {
        return Model.isAspectRatioLock(View.getTgbtnLock169());
    }
    
    //Métodos para las funciones básicas
    //Botones add
    private void addFunctionalityToButtonAddSlave(){
        Model.addFunctionalityToButtonAddSlave(View.getBtnAddSlave(), View.getCmbSlave(), this, dataC);
    }
    
    private void addFunctionalityToButtonAddHdmiOutput(){
        Model.addFunctionalityToButtonAddHdmiOutput(View.getBtnAddHdmi(), View.getCmbHdmi(), View.getPnlTable(), View, this, dataC);
    }
    
    private void addFunctionalityToButtonAddScreen(){
        Model.addFunctionalityToButtonAddScreen(View.getBtnAddScreen(), View, this, dataC);
    }
    
    private void addFunctionalityToButtonAddSection(){
        Model.addFunctionalityToButtonAddSection(View.getBtnAddSection(), this, dataC);
    }
    
    private void addFunctionalityToButtonAddLayer(){
        Model.addFunctionalityToButtonAddLayer(View.getBtnAddLayer(), View.getCmbLayer(), this, dataC);
    }
    
    //Botones delete
    private void addFunctionalityToButtonDeleteSlave(){
        Model.addFunctionalityToButtonDeleteSlave(View.getBtnDeleteSlave(), this, dataC);
    }
    
    private void addFunctionalityToButtonDeleteHdmi(){
        Model.addFunctionalityToButtonDeleteHdmi(View.getBtnDeleteHdmi(), this, dataC);
    }
    
    private void addFunctionalityToButtonDeleteScreen(){
        Model.addFunctionalityToButtonDeleteScreen(View.getBtnDeleteScreen(), this, dataC);
    }
    
    private void addFunctionalityToButtonDeleteSection(){
        Model.addFunctionalityToButtonDeleteSection(View.getBtnDeleteSection(), this, dataC);
    }
    
    //Botones edit
    private void addFunctionalityToButtonEditSlave(){
        Model.addFunctionalityToButtonEditSlave(View.getBtnEditSlave(), View.getCmbSlave(), this, dataC);
    }
    
    private void addFunctionalityToButtonEditHdmi(){
        Model.addFunctionalityToButtonEditHdmi(View.getBtnEditHdmi(),View.getCmbHdmi(), View.getPnlTable(), View, this, dataC);
    }
    
    private void addFunctionalityToButtonEditScreen(){
        Model.addFunctionalityToButtonEditScreen(View.getBtnEditScreen(), View, this, dataC);
    }
    
    private void addFunctionalityToButtonEditSection(){
        Model.addFunctionalityToButtonEditSection(View.getBtnEditSection(), View, this, dataC);
    }

    //Barra de menu de herramientas

    private void addFunctionalityToMenuItemSave(){
        Model.addFunctionalityToMenuItemSave(View.getMenuItemSave(), dataC);
    }

    private void addFunctionalityToMenuItemLoad(){
        Model.addFunctionalityToMenuItemLoad(View.getMenuItemLoad(), this, dataC);
    }

    private void addFunctionalityToMenuItemExport(){
        Model.addFunctionalityToMenuItemExport(View.getMenuItemExport(), dataC);
    }

    private void addFunctionalityToMenuItemImport(){
        Model.addFunctionalityToMenuItemImport(View.getMenuItemImport(), this, dataC);
    }
    
    private void addFunctionalityToMenuItemAbout(){
        Model.addFunctionalityToMenuItemAbout(View.getMenuItemAbout(), dataC);
    }
    
    //Miscelaneos
    private void addFunctionalityToPnlTable(){
        Model.addFunctionalityToPnlTable(View.getPnlTable(), dataC);
    }
    
    private void addFunctionalityToBoundsSpinners(){
        Model.addFunctionalityToBoundsSpinners(View.getArrSprSbnds(), this, dataC);
    }

    private void addFunctionalityToComboBoxHdmiOutputs(){
        Model.addFunctionalityToComboBoxHdmiOutputs(View.getCmbHdmi(), View.getPnlTable(), this, dataC);
    }
    
    private void addFunctionalityToComboBoxSlaves(){
        Model.addFunctionalityToComboBoxSlaves(View.getCmbSlave(), View.getCmbHdmi(), View.getCmbLayer(), View.getPnlTable(), this, dataC);
    }
    
    private void addFunctionalityToComboBoxLayers(){
        Model.addFunctionalityToComboBoxLayers(View.getCmbLayer(), this, dataC);
    }

    public void refreshTable() {
        Model.remplaceHdmiOutput(View.getPnlTable(), dataC.getCurrentHdmiOutput());
    }

    public void refreshItemsActionButtons(){
        Model.refreshItemsActionButtons(View.getCmbSlave(), View.getCmbHdmi(), View.getCmbLayer(),
                View.getBtnDeleteSlave(), View.getBtnDeleteHdmi(), View.getBtnDeleteScreen(),
                View.getBtnDeleteSection(), View.getBtnEditSlave(), View.getBtnEditHdmi(),
                View.getBtnEditScreen(), View.getBtnEditSection(),
                View.getArrSprSbnds(),
                View.getBtnAddSlave(), View.getBtnAddHdmi(), View.getBtnAddScreen(), View.getBtnAddSection(), View.getBtnAddLayer(),dataC);
    }
    
    public float getZoom(){
        return Model.getZoom();
    }
    
    //Atajos de teclado
    class KeyDispatcher implements KeyEventDispatcher {
        
        public boolean dispatchKeyEvent(KeyEvent evt) {
            if (evt.getID() == KeyEvent.KEY_PRESSED){
                switch (evt.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if(!View.getLblNameSection().getText().equals(M_Main.DEFAULT_INFO_TEXT)){
                            View.getArrSprSbnds()[V_Main.SPINNER_X].setValue( (int)View.getArrSprSbnds()[V_Main.SPINNER_X].getValue() -getGridSize());
                            return true;
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if(!View.getLblNameSection().getText().equals(M_Main.DEFAULT_INFO_TEXT)){
                            View.getArrSprSbnds()[V_Main.SPINNER_Y].setValue( (int)View.getArrSprSbnds()[V_Main.SPINNER_Y].getValue() -getGridSize());
                            return true;
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if(!View.getLblNameSection().getText().equals(M_Main.DEFAULT_INFO_TEXT)){
                            View.getArrSprSbnds()[V_Main.SPINNER_X].setValue( (int)View.getArrSprSbnds()[V_Main.SPINNER_X].getValue() +getGridSize());
                            return true;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if(!View.getLblNameSection().getText().equals(M_Main.DEFAULT_INFO_TEXT)){
                            View.getArrSprSbnds()[V_Main.SPINNER_Y].setValue( (int)View.getArrSprSbnds()[V_Main.SPINNER_Y].getValue() +getGridSize());
                            return true;
                        }
                        
                        break;
                }
            }
        return false;
        }
    }
    
}
