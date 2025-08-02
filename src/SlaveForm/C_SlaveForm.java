/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package SlaveForm;

import Data.C_Data;
import zMain.C_Main;

public class C_SlaveForm {
    private final M_SlaveForm Model;
    private final V_SlaveForm View;
    
    /**
     * Constructor
     * @param bEditMode Si es true la ventana editará el slave actual
     * @param mainC
     * @param dataC 
     */
    public C_SlaveForm(boolean bEditMode, C_Main mainC, C_Data dataC) {
        this.Model = new M_SlaveForm();
        this.View = new V_SlaveForm();
                
        addFunctionalityToButtonSave(bEditMode, mainC, dataC);
        addFunctionalityToButtonCancel();
        addFunctionalityToCheckBoxBackup();
        addFunctionalityToCheckBoxOffset();
        
        if(bEditMode)
            this.loadSlaveData(dataC);
    }
    
    //Metodos publicos
    public boolean isSuccessful(){
        return Model.isSuccessful();
    }
    
    public void show() {
        Model.show(View);
    }
    
    //Métodos para las funciones básicas
    //Botones aceptar y cancelar
    private void addFunctionalityToButtonSave(boolean bEditMode, C_Main mainC, C_Data dataC){
        Model.addFunctionalityToButtonSave(View.getBtnSave(), View.getCkbBackup(), View.getCkbOffset(), View.getSprOffset(),
                View.getTxtNameSlave(), View.getTxtNameBackup(), View.getTxtAddress(), bEditMode, View, mainC, dataC);
    }
    
    private void addFunctionalityToButtonCancel(){
        Model.addFunctionalityToButtonCancel(View.getBtnCancel(), View);
    }
    
    //Checkbox para habilitar Backup
    private void addFunctionalityToCheckBoxBackup(){
        Model.addFunctionalityToCheckBoxBackup(View.getCkbBackup(), View.getTxtNameBackup());
    }

    //Checkbox para habilitar Offset
    private void addFunctionalityToCheckBoxOffset(){
        Model.addFunctionalityToCheckBoxOffset(View.getCkbOffset(), View.getSprOffset());
    }
    
    //Metodo para cargar la información del slave actual en el modo edición
    private void loadSlaveData(C_Data dataC){
        Model.loadSlaveData(View.getCkbBackup(), View.getCkbOffset(), View.getSprOffset(),
                View.getTxtNameSlave(), View.getTxtNameBackup(), View.getTxtAddress(), dataC);
    }
    
}
