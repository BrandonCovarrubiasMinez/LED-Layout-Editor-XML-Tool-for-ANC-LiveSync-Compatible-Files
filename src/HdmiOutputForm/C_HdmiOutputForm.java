/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package HdmiOutputForm;

import Data.C_Data;
import javax.swing.JFrame;
import zMain.C_Main;

public class C_HdmiOutputForm{
    private final M_HdmiOutputForm Model;
    private transient V_HdmiOutputForm View;
    private final JFrame frmParent;
    
    public C_HdmiOutputForm(boolean bEditMode, JFrame frmParent, C_Main mainC, C_Data dataC){
        Model= new M_HdmiOutputForm();
        View= new V_HdmiOutputForm(frmParent);
        this.frmParent= frmParent;
        
        //addFunctionalityToButtonAddSlave(dataC);
        addFunctionalityToButtonAddHdmiOutput(bEditMode, mainC, dataC);
        addFunctionalityToButtonCancel();
        
        if(bEditMode)
            loadSlaveData(dataC);
    }
    
    //Metodos publicos
    public boolean isSuccessful(){
        return Model.isSuccessful();
    }
    
    public void show(){
        Model.show(View);
    }
    
    private void addFunctionalityToButtonAddHdmiOutput(boolean bEditMode, C_Main mainC, C_Data dataC){
        Model.addFunctionalityToButtonAddHdmiOutput(View.getBtnSaveHdmiOutput(), View.getTxtWidth(), View.getTxtHeight(), View, bEditMode, mainC, dataC);
    }
    
    private void addFunctionalityToButtonCancel(){
        Model.addFunctionalityToButtonCancel(View.getBtnCancel(), View);
    }
    
    //Metodo para cargar la información del HDMI actual en el modo edición
    private void loadSlaveData(C_Data dataC){
        Model.loadSlaveData(View.getTxtWidth(), View.getTxtHeight(), dataC);
    }
}
