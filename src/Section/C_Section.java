/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package Section;

import Data.C_Data;
import Layer.C_Layer;
import Screen.C_Screen;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import zMain.C_Main;

public final class C_Section{
    private C_Screen screenOwnerC;
    private String sName;
    private String sFolder;
    private C_Layer sLayer;

    private transient V_Section View;
    private final M_Section Model;
    
    private final C_Main mainC;
    private final C_Data dataC;

    public C_Section(C_Main mainC, C_Data dataC){
        View= new V_Section();
        Model= new M_Section();
        this.mainC= mainC;
        this.dataC= dataC;

        sName= "NONAME";
        sFolder= "";
        
        this.setBorderColor(C_Data.NO_SELECTED_BORDER_COLOR);
        
        addFunctionalityToMovementPanel(mainC);
        addFunctionalityToResizePanel(mainC);
        
        //TEST ONLY
        //View.setBackground(new Color(158, 213, 220));
        
    }
    
    public int getX() {
        return View.getX();
    }
    
    public int getY() {
        return View.getY();
    }
    
    public int getWidth() {
        return View.getWidth();
    }
    
    public int getHeight() {
        return View.getHeight();
    }
    
    public Point getTrueLocation() {
        return View.getTrueLocation();
    }
    
    public Dimension getTrueSize() {
        return View.getTrueSize();
    }

    public C_Layer getLayer() {
        return sLayer;
    }

    public String getFolder() {
        return sFolder;
    }
    
    public void setTrueSize(int width, int height, int iGridSize) {
        View.setTrueSize(width, height, iGridSize);
        mainC.refreshSectionInfo();
    }

    public void setTrueLocation(int x, int y, int iGridSize) {
        View.setTrueLocation(x, y, iGridSize);
        mainC.refreshSectionInfo();
    }

    public void setTrueLocation(int x, int y){
        View.setTrueLocation(x, y);
        mainC.refreshSectionInfo();
    }

    public void setTrueSize(int width, int height){
        View.setTrueSize(width, height);
        mainC.refreshSectionInfo();
    }

    public void setMaxTrueSize(Dimension dimMax){
        View.setMaxTrueSize(dimMax);
    }

    public Dimension getMaxTrueSize(){
        return View.getMaxTrueSize();
    }

    public void setMaxTrueLocationArea(Point pntMaxTrueLocationArea){
        View.setMaxTrueLocationArea(pntMaxTrueLocationArea);
    }

    public void setBackground(Color c) {
        View.getPnlMovement().setBackground(c);
        View.getPnlMargin().setBackground(c);
    }

    public void setName(String sName) {
        View.getLblName().setText(sName);
        this.sName = sName;
    }

    public void setBorderColor(Color c){
        Model.setBorderColor(View.getPnlMargin(), c);
    }
    
    public String getName() {
        return sName;
    }

    /**
     * Cambia la posesion de esta sección a la proporcionada
     * @param screenOwnerC La nueva propietaria de la sección
     */
    public void setOwner(C_Screen screenOwnerC){
        this.screenOwnerC= screenOwnerC;
    }

    /**
     * Cambia la capa a la que la seccion pertenece
     * @param sLayer 
     */
    public void setLayer(C_Layer sLayer) {
        this.sLayer = sLayer;
    }

    public void setFolder(String sFolder) {
        this.sFolder = sFolder;
    }

    /**
     * Devuelve la pantalla a la que esta sección pertenece
     * @return Pantalla propietaria
     */
    public C_Screen getOwner(){
        return screenOwnerC;
    }

    public V_Section getView() {
        return View;
    }

    //Functionality methods
    private void addFunctionalityToMovementPanel(C_Main mainC){
        Model.addFunctionalityToMovementPanel(View.getPnlMovement(), this, mainC, dataC);
    }

    private void addFunctionalityToResizePanel(C_Main mainC){
        Model.addFunctionalityToResizePanel(View.getPnlMargin(), this, mainC, dataC);
    }



}
