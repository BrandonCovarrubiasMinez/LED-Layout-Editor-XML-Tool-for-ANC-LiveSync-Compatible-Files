/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package SectionForm;

import Data.C_Data;
import Section.C_Section;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import zMain.C_Main;

public class M_SectionForm {
    private boolean bSuccessful= false;

    void show(V_SectionForm View) {
        View.setVisible(true);
    }

    boolean isSuccessful() {
        return bSuccessful;
    }
    
    void loadSectionData(JTextField txtName, JTextField txtFolder, JCheckBox ckbFolder, JSpinner sprWidth, JSpinner sprHeight, C_Data dataC) {
        C_Section sectionC= dataC.getCurrentSection();
        
        txtName.setText(sectionC.getName());
        
        txtFolder.setText(sectionC.getFolder());
        ckbFolder.setSelected(!sectionC.getFolder().isEmpty());
        txtFolder.setEnabled(ckbFolder.isSelected());
        txtFolder.setBackground(ckbFolder.isSelected() ? Color.WHITE : new Color(231,231,231));
        
        sprWidth.setValue(sectionC.getTrueSize().width);
        sprHeight.setValue(sectionC.getTrueSize().height);
        
        
    }

    void addFunctionalityToButtonSave(boolean bEditMode, JButton btnSave, JTextField txtName, JTextField txtFolder, JCheckBox ckbFolder, JSpinner sprWidth, JSpinner sprHeight, V_SectionForm View, C_Main mainC, C_Data dataC) {
        btnSave.addActionListener((ActionEvent evt) -> {
            try {
                String sName= txtName.getText().trim();
                String sFolder= txtFolder.getText().trim();
                Dimension dSize= new Dimension((int)sprWidth.getValue(), (int)sprHeight.getValue());
                Point pLocation= new Point(0,0);    //Default. Si se cambia se deben añadir verificaciones (Que solo se permita dentro de la pantalla)
                boolean bFolder= ckbFolder.isSelected();
                
                //Checa que el nombre no se repita o sea nulo
                if(!dataC.isDuplicatedName(sName, C_Data.SECTION) && !bEditMode)
                    throw new Error("Invalid or duplicate name");
                
                if(sName.isEmpty())
                    throw new Error("Invalid name");
                
                if(bFolder && sFolder.isEmpty())
                    throw new Error("The folder field cannot be empty. Uncheck the \"Folder\" box for a section without a folder.");
                
                if(dSize.width < 1 || dSize.height < 1)
                    throw new Error("The dimensions cannot be less than 1");
                
                makeSection(bEditMode, bFolder, sName, sFolder, dSize, pLocation, mainC, dataC);
                
                View.dispose();
                
            }catch(Error e){
                JOptionPane.showMessageDialog(View, e.getMessage());
            }catch(Exception e){
                JOptionPane.showMessageDialog(View, "Unexpected Error: "+e.getMessage());
            }
        });
    }
    
    void addFunctionalityToButtonCancel(JButton btnCancel, V_SectionForm View) {
        btnCancel.addActionListener((ActionEvent evt) -> {
            View.dispose();
        });
    }
    
    void addFunctionalityToCheckBoxFolder(JCheckBox ckbFolder, JTextField txtFolder) {
        ckbFolder.addActionListener((ActionEvent evt) -> {
            txtFolder.setEnabled(ckbFolder.isSelected());
            txtFolder.setBackground(ckbFolder.isSelected() ? Color.WHITE : new Color(231,231,231));
        });
    }
    
    private void makeSection(boolean bEditMode, boolean bFolder, String sName, String sFolder, Dimension dSize, Point pLocation, C_Main mainC, C_Data dataC){
        C_Section sectionC;
        
        //Add mode
        if(!bEditMode){
            sectionC= new C_Section(mainC, dataC);
            
            sectionC.setName(sName);
            sectionC.setFolder(bFolder ? sFolder : "");
            
            sectionC.setTrueSize(dSize.width, dSize.height, mainC.getGridSize());
            sectionC.setTrueLocation(pLocation.x, pLocation.y, mainC.getGridSize());
            
            sectionC.setMaxTrueSize(dataC.getCurrentScreen().getTrueSize());
            sectionC.setMaxTrueLocationArea(new Point(dataC.getCurrentScreen().getTrueSize().width, dataC.getCurrentScreen().getTrueSize().height));

            dataC.add(sectionC, mainC);
            
        //Edit mode
        }else{
            sectionC= dataC.getCurrentSection();
            
            sectionC.setName(sName);
            sectionC.setFolder(bFolder ? sFolder : "");
            
            sectionC.setTrueSize(dSize.width, dSize.height, mainC.getGridSize());
            //sectionC.setTrueLocation(pLocation.x, pLocation.y, mainC.getGridSize()); //No se usa para que se evite la traslación a
             
            sectionC.setMaxTrueSize(dataC.getCurrentScreen().getTrueSize());
            sectionC.setMaxTrueLocationArea(new Point(dataC.getCurrentScreen().getTrueSize().width, dataC.getCurrentScreen().getTrueSize().height));
        }
        
        bSuccessful=true;
    }

    

    
}
