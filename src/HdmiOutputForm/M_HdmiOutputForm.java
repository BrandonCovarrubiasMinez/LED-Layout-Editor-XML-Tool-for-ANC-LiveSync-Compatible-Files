/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package HdmiOutputForm;

import Data.C_Data;
import HdmiOutput.C_HdmiOutput;
import java.awt.event.ActionEvent;
import javax.swing.*;
import zMain.C_Main;

class M_HdmiOutputForm{
    
    private boolean bSuccessful= false;
    
    void show(V_HdmiOutputForm View) {
        View.setVisible(true);
    }

    void addFunctionalityToButtonAddHdmiOutput( JButton btnSaveHdmiOutput, JTextField txtWidth, JTextField txtHeight, V_HdmiOutputForm View, boolean bEditMode, C_Main mainC, C_Data dataC) {
        btnSaveHdmiOutput.addActionListener((ActionEvent evt) -> {
            try{
                C_HdmiOutput hdmiC;
                if(!bEditMode){
                    hdmiC= new C_HdmiOutput();
                    hdmiC.setTrueSize(Integer.parseInt(txtWidth.getText().trim()), Integer.parseInt(txtHeight.getText().trim()));

                    dataC.add(hdmiC, mainC);
                }else{
                    hdmiC= dataC.getCurrentHdmiOutput();
                    hdmiC.setTrueSize(Integer.parseInt(txtWidth.getText().trim()), Integer.parseInt(txtHeight.getText().trim()));
                }
                
                bSuccessful= true;
                this.close(View);
            }catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(View, "Invalid dimensions");
            }
        });
    }

    void addFunctionalityToButtonCancel(JButton btnCancel, V_HdmiOutputForm View) {
        btnCancel.addActionListener((ActionEvent evt) -> {
            close(View);
        });
        
    }

    private void close(V_HdmiOutputForm View){
        View.dispose();
    }

    boolean isSuccessful() {
        return bSuccessful;
    }

    void loadSlaveData(JTextField txtWidth, JTextField txtHeight, C_Data dataC) {
        C_HdmiOutput hdmiC= dataC.getCurrentHdmiOutput();
        
        txtWidth.setText(hdmiC.getTrueWidth()+"");
        txtHeight.setText(hdmiC.getTrueHeight()+"");
    }

    

    
    
}
