/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package HdmiOutput;

import Screen.C_Screen;
import Slave.C_Slave;
import java.awt.Color;

public class C_HdmiOutput{
    private C_Slave slaveC;
    
    
    private transient V_HdmiOutput View;
    private final M_HdmiOutput Model;

    public C_HdmiOutput() {
        this.View = new V_HdmiOutput();
        this.Model = new M_HdmiOutput();
        
        
        
        View.setVisible(true);

        addFunctionalityToHdmiPanel();
        
        //TEST ONLY
        View.setBackground(new Color(158, 213, 220));
        
    }
    
    /**
     * Devuelve la locación de la vista
     * @return Locación en x
     */
    public int getX(){
        return Model.getX(View);
    }
    
    /**
     * Devuelve la locación de la vista
     * @return Locación en y
     */
    public int getY(){
        return Model.getY(View);
    }
    
    public int getTrueWidth(){
        return View.getTrueWidth();
    }
    
    public int getTrueHeight(){
        return View.getTrueHeight();
    }
    
    /**
     * Cambia la ubicación de la vista
     * @param x
     * @param y 
     */
    public void setLocation(int x, int y){
        Model.setLocation(View, x, y);
    }
    
    /**
     * Este medoto devuelve la vista de la seccion. SOLO SE UTILIZA EN LA CLASE M_Data
     * @return 
     */
    public V_HdmiOutput getView() {
        return View;
    }
    
    /**
     * Llama a los metodos zoom de cada componente dentro de la vista
     * @param fZoom Factor de zoom (1: tamaño normal)
     */
    public void refreshGlobalZoom(float fZoom){
        Model.refreshZoom(View, fZoom);
    }
    
    public void addScreen(C_Screen screenC) {
        View.addScreen(View, screenC.getView());
    }
    
    public void setSize(int iWidth, int iHeight) {
        View.setSize(iWidth, iHeight);
    }
    
    public void setTrueSize(int iWidth, int iHeight){
        View.setTrueSize(iWidth, iHeight);
    }
    
    //Métodos para las funciones básicas
    private void addFunctionalityToHdmiPanel(){
        Model.addFunctionalityToHdmiPanel(View);
    }

    public void setSlave(C_Slave slaveC) {
        this.slaveC = slaveC;
    }

    public C_Slave getSlaveC() {
        return slaveC;
    }

    public int getWidth() {
        return Model.getWidth(View);
    }

    public int getHeight() {
        return Model.getHeight(View);
    }
}
