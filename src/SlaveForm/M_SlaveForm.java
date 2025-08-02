/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package SlaveForm;

import Data.C_Data;
import Slave.C_Slave;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import zMain.C_Main;

public class M_SlaveForm {
    private boolean bSuccessful= false;
    
    /**
     * Inicializa un nuevo Slave y cambia el estado de la ventana a Sucessful
     * @param sName
     * @param dataC 
     */
    private void makeSlave(String sName, int iOffset, String sBackup, String sAddress, boolean bEditMode, C_Main mainC, C_Data dataC){
        C_Slave slaveC;
        
        if(!bEditMode){
            slaveC= new C_Slave();
            slaveC.setName(sName);
            slaveC.setOffset(iOffset);
            slaveC.setBackup(sBackup);
            slaveC.setAddress(sAddress);

            dataC.add(slaveC, mainC);
        }else{
            slaveC= dataC.getCurrentSlave();
            slaveC.setName(sName);
            slaveC.setOffset(iOffset);
            slaveC.setBackup(sBackup);
            slaveC.setAddress(sAddress);
        }
        
        
        bSuccessful=true;
        
    }
    

    void addFunctionalityToButtonSave(JButton btnSave, JCheckBox ckbBackup, JCheckBox ckbOffset, JSpinner sprOffset, JTextField txtNameSlave, JTextField txtNameBackup, JTextField txtAddress, boolean bEditMode, V_SlaveForm slaveFormV, C_Main mainC, C_Data dataC) {
        btnSave.addActionListener((ActionEvent evt) -> {
            if(!txtNameSlave.getText().trim().isEmpty() && !txtAddress.getText().trim().isEmpty()){
                
                int iOffset= ckbOffset.isSelected() ? (int)sprOffset.getValue() : 0;
                String sBackup= ckbBackup.isSelected() ? txtNameBackup.getText() : "";
                
                makeSlave(txtNameSlave.getText(), iOffset , sBackup, txtAddress.getText(), bEditMode, mainC, dataC);
                slaveFormV.dispose();
            }else
                JOptionPane.showMessageDialog(slaveFormV, "Please fill in all fields");
        });
    }

    void addFunctionalityToButtonCancel(JButton btnCancel, V_SlaveForm slaveFormV) {
        btnCancel.addActionListener((ActionEvent evt) -> {
            slaveFormV.dispose();
        });
    }

    void addFunctionalityToCheckBoxBackup(JCheckBox ckbBackup, JTextField txtNameBackup) {
        ckbBackup.addChangeListener((ChangeEvent evt) -> {
            if(ckbBackup.isSelected()){
                txtNameBackup.setEnabled(true);
                txtNameBackup.setBackground(Color.WHITE);
                ckbBackup.transferFocus();
            }else{
                txtNameBackup.setEnabled(false);
                txtNameBackup.setBackground(new Color(238,238,238));
            }
        });
    }

    void show(V_SlaveForm slaveFormV) {
        slaveFormV.setVisible(true);
    }

    boolean isSuccessful() {
        return bSuccessful;
    }

    void addFunctionalityToCheckBoxOffset(JCheckBox ckbOffset, JSpinner sprOffset) {
        ckbOffset.addChangeListener((ChangeEvent evt) -> {
            if(ckbOffset.isSelected()){
                sprOffset.setEnabled(true);
                sprOffset.setBackground(Color.WHITE);
                ckbOffset.transferFocus();
            }else{
                sprOffset.setEnabled(false);
                sprOffset.setBackground(new Color(238,238,238));
            }
        });
    };

    void loadSlaveData(JCheckBox ckbBackup, JCheckBox ckbOffset, JSpinner sprOffset, JTextField txtNameSlave, JTextField txtNameBackup, JTextField txtAddress, C_Data dataC) {
        C_Slave slaveC= dataC.getCurrentSlave();
        //CheckBox
        ckbBackup.setSelected(!slaveC.getBackup().equalsIgnoreCase(""));
        ckbOffset.setSelected(slaveC.getOffset() != 0);
            
        //Offset Spinner
        sprOffset.setValue(slaveC.getOffset());
        
        //Backup
        txtNameBackup.setText(slaveC.getBackup());
        
        //Name
        txtNameSlave.setText(slaveC.getName());
        
        //Address
        txtAddress.setText(slaveC.getAddress());
    }
    
    
}
