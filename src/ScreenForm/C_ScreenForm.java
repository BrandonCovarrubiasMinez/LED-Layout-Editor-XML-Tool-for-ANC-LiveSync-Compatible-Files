/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package ScreenForm;

import Data.C_Data;
import javax.swing.JFrame;
import zMain.C_Main;

public class C_ScreenForm{
    private transient V_ScreenForm View;
    private final M_ScreenForm Model;
    
    public C_ScreenForm(boolean bEditMode, JFrame frmParent, C_Main mainC, C_Data dataC) {
        View = new V_ScreenForm(frmParent);
        Model = new M_ScreenForm();      
        
        addFunctionalityToButtonSave(bEditMode, dataC, mainC);
        addFunctionalityToButtonCancel();
        addFunctionalityToCheckBoxAudio();
        
        if(bEditMode)
            loadScreenData(dataC);
    }
    //Metodos publicos
    public void show(){
        Model.show(View);
    }
    
    public boolean isSuccessful(){
        return Model.isSuccessful();
    }
    
    //Metodos internos
    
    //Metodo para cargar la información del slave actual en el modo edición
    private void loadScreenData(C_Data dataC){
        Model.loadScreenData(View.getCkbAudio(), View.getTxtName(),
                View.getSprXLocation(), View.getSprYLocation(), View.getTxtTileWidth(), View.getTxtTileHeight(),
                View.getSprTileColumnQuantity(), View.getSprTileRowQuantity(), View.getSprDefDuration(), View.getCmbAudio(), dataC);
    }
    
    private void addFunctionalityToButtonSave(boolean bEditMode, C_Data dataC, C_Main mainC){
        Model.addFunctionalityToButtonSave(bEditMode, View.getBtnSave(), View.getCkbAudio(), View.getTxtName(),
                View.getSprXLocation(), View.getSprYLocation(), View.getTxtTileWidth(), View.getTxtTileHeight(), 
                View.getSprTileColumnQuantity(), View.getSprTileRowQuantity(), View.getSprDefDuration(), View.getCmbAudio(),
                View, mainC, dataC);
    }
    
    private void addFunctionalityToButtonCancel(){
        Model.addFunctionalityToButtonCancel(View.getBtnCancel(), View);
    }
    
    private void addFunctionalityToCheckBoxAudio(){
        Model.addFunctionalityToCheckBoxAudio(View.getCkbAudio(), View.getCmbAudio());
    }
    
}
