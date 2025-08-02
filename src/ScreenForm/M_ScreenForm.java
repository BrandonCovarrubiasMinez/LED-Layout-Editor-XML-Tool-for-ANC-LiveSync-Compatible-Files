/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package ScreenForm;

import Data.C_Data;
import Screen.C_Screen;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import zMain.C_Main;

class M_ScreenForm{
    
    private boolean bSuccessful= false;
    
    void dispose(V_ScreenForm nwscnView){
        nwscnView.dispose();
    }
    
    void show(V_ScreenForm screenV){
        screenV.setVisible(true);
    }
    
    boolean isSuccessful() {
        return bSuccessful;
    }
    
    void loadScreenData(JCheckBox ckbAudio, JTextField txtName, JSpinner sprXLocation, JSpinner sprYLocation, JTextField txtTileWidth, JTextField txtTileHeight, JSpinner sprTileColumnQuantity, JSpinner sprTileRowQuantity, JSpinner sprDefDuration, JComboBox<String> cmbAudio, C_Data dataC) {
        C_Screen screenC= dataC.getCurrentScreen();
        
        ckbAudio.setSelected(!screenC.getAudioDevice().isEmpty());
        txtName.setText(screenC.getName());
        txtTileWidth.setText(screenC.getTrueSize().width+"");
        txtTileHeight.setText(screenC.getTrueSize().height+"");
        sprXLocation.setValue(screenC.getTrueLocation().x);
        sprYLocation.setValue(screenC.getTrueLocation().y);
        //Editar luego
        sprTileColumnQuantity.setValue(1);
        sprTileRowQuantity.setValue(1);
        sprDefDuration.setValue(screenC.getDefaultDuration());
        
        //Audio device
        if(!screenC.getAudioDevice().isEmpty()){
            cmbAudio.setEnabled(true);
            cmbAudio.addItem(screenC.getAudioDevice());
            cmbAudio.setSelectedItem(screenC.getAudioDevice());
        }
    }
    
    
    void addFunctionalityToButtonSave(boolean bEditMode, JButton btnSave, JCheckBox ckbAudio, JTextField txtName,
                                  JSpinner sprXLocation, JSpinner sprYLocation, JTextField txtTileWidth, JTextField txtTileHeight,
                                  JSpinner sprTileColumnQuantity, JSpinner sprTileRowQuantity, JSpinner sprDefaultDuration, JComboBox<String> cmbAudio,
                                  V_ScreenForm View, C_Main mainC, C_Data dataC) {
        
        btnSave.addActionListener((ActionEvent evt) -> {
            try{
                int iTileWidth = Integer.parseInt(txtTileWidth.getText());
                int iTileHeight = Integer.parseInt(txtTileHeight.getText());
                int iXLocation = Integer.parseInt(sprXLocation.getValue().toString());
                int iYLocation = Integer.parseInt(sprYLocation.getValue().toString());
                int iDefaultDuration= Integer.parseInt( sprDefaultDuration.getValue().toString() );
                float fTileColumnQuantity = Float.parseFloat( sprTileColumnQuantity.getValue().toString() );
                float fTileRowQuantity = Float.parseFloat( sprTileRowQuantity.getValue().toString() );
                String sAudioDevice= ckbAudio.isSelected() ? cmbAudio.getSelectedItem().toString() : ""; //Solo se le asigna valor si su casilla esta encendida
                String sName= txtName.getText().trim();

                //Verifica que las dimenciones sean positivas y no cero
                if(iTileWidth < 1 || iTileHeight < 1)
                    throw new Error("The dimensions cannot be less than 1");
                
                if(ckbAudio.isSelected() && cmbAudio.getSelectedItem().toString().trim().isEmpty())
                    throw new Error("Audio device cannot be empty. Uncheck the \"Audio\" box for a display without audio.");
                
                if(!dataC.isDuplicatedName(sName, C_Data.SCREEN) && !bEditMode)
                    throw new Error("Duplicate name");
                
                if(sName.isEmpty())
                    throw new Error("Invalid name");
                
                //Se Crea/Edita pantalla
                makeScreen(sName, iXLocation, iYLocation, iTileWidth, iTileHeight, iDefaultDuration,  fTileColumnQuantity, fTileRowQuantity, sAudioDevice, bEditMode,  View, mainC, dataC);
            
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(View, "Invalid dimensions");
            }catch(Error e){
                JOptionPane.showMessageDialog(View, e.getMessage());
            }
            
        });
    }
    
    void addFunctionalityToButtonCancel(JButton btnCancel, V_ScreenForm screenFormV) {
        btnCancel.addActionListener((ActionEvent evt) -> {
            screenFormV.dispose();
        });
    }

    private void makeScreen(String strScreenName, int iXLocation, int iYLocation, int iTileWidth, int iTileHeight, int iDefaultDuration, float fTileColumnQuantity,  float fTileRowQuantity, String sAudioDevice, boolean bEditMode, V_ScreenForm View, C_Main mainC, C_Data dataC){
        C_Screen screenC;
        
        if(!bEditMode){
            screenC= new C_Screen(mainC, dataC);
            
            screenC.setName(strScreenName);
            screenC.setTrueSize(iTileWidth, iTileHeight, fTileColumnQuantity, fTileRowQuantity);
            screenC.setTrueLocation(iXLocation, iYLocation);
            screenC.setAudioDevice(sAudioDevice);
            screenC.setDefaultDuration(iDefaultDuration);

            dataC.add(screenC, mainC);
            
        }else{
            screenC= dataC.getCurrentScreen();
            
            screenC.setName(strScreenName);
            screenC.setTrueSize(iTileWidth, iTileHeight, fTileColumnQuantity, fTileRowQuantity);
            screenC.setTrueLocation(iXLocation, iYLocation);
            screenC.setAudioDevice(sAudioDevice);
            screenC.setDefaultDuration(iDefaultDuration);
        }
        bSuccessful= true;
        dispose(View);
    }

    /**
     * Activa/Desactiva el cmbAudio dependiendo del checkbox
     * @param ckbAudio
     * @param cmbAudio 
     */
    void addFunctionalityToCheckBoxAudio(JCheckBox ckbAudio, JComboBox<String> cmbAudio) {
        ckbAudio.addActionListener((ActionEvent evt) -> {
            cmbAudio.setEnabled(ckbAudio.isSelected());
        });
    }
}