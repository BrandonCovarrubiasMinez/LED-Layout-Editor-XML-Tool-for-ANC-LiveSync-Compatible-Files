/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package SectionForm;

import Data.C_Data;
import zMain.C_Main;

public class C_SectionForm {
    private final V_SectionForm View;
    private final M_SectionForm Model;

    public C_SectionForm(boolean bEditMode, C_Main mainC, C_Data dataC) {
        View= new V_SectionForm();
        Model= new M_SectionForm();
        
        addFunctionalityToButtonSave(bEditMode, dataC, mainC);
        addFunctionalityToButtonCancel();
        addFunctionalityToCheckBoxFolder();
        
        if(bEditMode)
            this.loadSectionData(dataC);
    }
    
    //Publicos
    public void show(){
        Model.show(View);
    }
    
    public boolean isSuccessful(){
        return Model.isSuccessful();
    }
    
    //Privados
    private void loadSectionData(C_Data dataC){
        Model.loadSectionData(View.getTxtName(), View.getTxtFolder(), View.getCkbFolder(), View.getSprWidth(), View.getSprHeight(), dataC);
    }
    
    //Funcionalidad
    private void addFunctionalityToButtonSave(boolean bEditMode, C_Data dataC, C_Main mainC){
        Model.addFunctionalityToButtonSave(bEditMode, View.getBtnSave(), View.getTxtName(), View.getTxtFolder(),
                View.getCkbFolder(), View.getSprWidth(), View.getSprHeight(), 
                View, mainC, dataC);
    }
    
    private void addFunctionalityToButtonCancel(){
        Model.addFunctionalityToButtonCancel(View.getBtnCancel(), View);
    }
    
    private void addFunctionalityToCheckBoxFolder(){
        Model.addFunctionalityToCheckBoxFolder(View.getCkbFolder(),  View.getTxtFolder());
    }
    
}
