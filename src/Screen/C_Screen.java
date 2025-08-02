/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package Screen;

import Data.C_Data;
import HdmiOutput.C_HdmiOutput;

import java.awt.*;
import zMain.C_Main;

public final class C_Screen{
    private C_HdmiOutput hdmiOwnerC;
    private String strScreenName;
    private String sAudioDevice;
    private int iDefaultDuration;
    
    private transient V_Screen View;
    private final M_Screen Model;

    public C_Screen(C_Main mainC, C_Data dataC) {
        this.View = new V_Screen();
        this.Model = new M_Screen();
        
        strScreenName= "NONAME";
        sAudioDevice= "";
        iDefaultDuration= 15;

        this.addFunctionalityToView(dataC, mainC);

        this.setBorderColor(C_Data.NO_SELECTED_BORDER_COLOR);
        
    }
    
    /**
     * Este medoto devuelve la vista de la seccion. SOLO SE UTILIZA EN LA CLASE M_Data
     * @return 
     */
    public V_Screen getView() {
        return View;
    }

    /**
     * Cambia la posesion de esta pantalla a la proporcionada
     * @param hdmiOwnerC La nueva propietaria de la pantalla
     */
    public void setOwner(C_HdmiOutput hdmiOwnerC) {
        this.hdmiOwnerC = hdmiOwnerC;
    }
    
    public void setBackground(Color c) {
        Model.setBackground(View, c);
    }

    public void setName(String strScreenName){
        this.strScreenName= strScreenName;
    }
    
    public void setTrueSize(int iTileWidth, int iTileHeight, float fTileColumnQuantity, float fTileRowQuantity){
        Model.setTrueSize(View, iTileWidth, iTileHeight, fTileColumnQuantity, fTileRowQuantity);
    }
    
    public Dimension getSize(){
        return View.getSize();
    }
    
    public void setLocation(int x, int y) {
        View.setLocation(x, y);
    }
    
    public Dimension getTrueSize() {
        return View.getTrueSize();
    }

    public Point getTrueLocation() {
        return View.getTrueLocation();
    }

    public void setTrueLocation(int x, int y) {
        View.setTrueLocation(x, y, 1);
    }

    public void setTrueSize(int width, int height) {
        View.setTrueSize(width, height, 1);
    }

    public void setDefaultDuration(int iDefaultDuration) {
        this.iDefaultDuration = iDefaultDuration;
    }

    public void setAudioDevice(String sAudioDevice) {
        this.sAudioDevice = sAudioDevice;
    }

    public void setBorderColor(Color c){
        Model.setBorderColor(View, c);
    }
    
    public int getDefaultDuration() {
        return iDefaultDuration;
    }

    public String getAudioDevice() {
        return sAudioDevice;
    }
    
    /**
     * Devuelve la HdmiOutput a la que esta pantalla pertenece
     * @return HdmiOutput propietaria
     */
    public C_HdmiOutput getOwner() {
        return hdmiOwnerC;
    }
    
    public String getName() {
        return strScreenName;
    }

    private void addFunctionalityToView(C_Data dataC, C_Main mainC){
        Model.addFunctionalityToScreen(View, this, mainC, dataC);
    }


}
