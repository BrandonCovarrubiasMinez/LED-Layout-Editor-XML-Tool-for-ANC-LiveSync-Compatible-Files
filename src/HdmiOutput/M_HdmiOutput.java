/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package HdmiOutput;

import Heirs.pnlZoomeable;
import Screen.V_Screen;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPanel;

class M_HdmiOutput{
    private final Point pntMouseHoldLocation;

    public M_HdmiOutput() {
        pntMouseHoldLocation= new Point();
    }
    

    void addFunctionalityToHdmiPanel(V_HdmiOutput View) {
        //Por si el movimiento se intenta dentro del pnlCanvas
        //♥ Esto se hacia con el panel Tabla 
        View.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent evt) {
                pntMouseHoldLocation.setLocation(evt.getX(), evt.getY());
            }
            
        });
        
        View.addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseDragged(MouseEvent evt) {
               
                //Click izquierdo
                if(evt.getModifiers() == MouseEvent.BUTTON3_MASK){
                    //Mueve el panel. Depende de la ubicacion del panel el movimiento en si y la ubicacion del mouse cuando se inicia.
                    View.setLocation( View.getX()+evt.getX() - pntMouseHoldLocation.x,
                            View.getY()+evt.getY() - pntMouseHoldLocation.y );
                    
                }
            }
        });
    }
    
    void refreshZoom(V_HdmiOutput hdmiCurrentV, float fZoom){
        Component[] arrpnlscrScreens= hdmiCurrentV.getComponents();
        
        //Zoom para el Hdmi 
        hdmiCurrentV.zoom(fZoom);
        
        for(Component pnlscrScreen : arrpnlscrScreens) {
            Component[] arrpnlsSections= ((JPanel)pnlscrScreen).getComponents();
            
            //Zoom para las pantallas
            ((pnlZoomeable)pnlscrScreen).zoom(fZoom);
            
            for(Component pnlsCurrent : arrpnlsSections){
                //Zoom para los componentes de la pantalla (secciones)
                ((pnlZoomeable)pnlsCurrent).zoom(fZoom);
            }
        }
        
    }

    /**
     * Devuelve la locación de la vista
     * @return Locación en x
     */
    int getX(V_HdmiOutput View) {
        return View.getX();
    }

    /**
     * Devuelve la locación de la vista
     * @return Locación en y
     */
    int getY(V_HdmiOutput View) {
        return View.getY();
    }

    /**
     * Cambia la ubicación de la vista
     * @param View
     * @param x
     * @param y 
     */
    void setLocation(V_HdmiOutput View, int x, int y) {
        View.setLocation(x, y);
    }
    
    void addScreen(V_HdmiOutput View, V_Screen screenV) {
        View.add(screenV);
    }

    int getWidth(V_HdmiOutput hdmiV) {
        return hdmiV.getWidth();
    }

    int getHeight(V_HdmiOutput hdmiV) {
        return hdmiV.getHeight();
    }
}
