/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package Screen;

import Data.C_Data;
import HdmiOutput.V_HdmiOutput;
import Section.V_Section;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import zMain.C_Main;

class M_Screen{

    void addSection(V_Screen View, V_Section sectionV) {
        View.add(sectionV);
    }

    /**
     * Cambia el color de fondo del panel
     * @param View
     * @param c 
     */
    void setBackground(V_Screen View, Color c) {
        View.setBackground(c);
    }
    
    void setBorderColor(V_Screen View, Color c) {
        View.setBorder(BorderFactory.createCompoundBorder(
            null,
            BorderFactory.createLineBorder(c, 1)
        ));
    }

    void setTrueSize(V_Screen View, int iTileWidth, int iTileHeight, float fTileColumnQuantity, float fTileRowQuantity) {
        //NUNCA debe llegar a dar un numero decimal en el calculo de las dimenciones pues aunque hay medios Tiles, no hay medios pixeles
        int iWidth= Math.round(iTileWidth * fTileColumnQuantity);
        int iHeight= Math.round(iTileHeight * fTileRowQuantity);
        
        View.setTrueSize(iWidth, iHeight);
    }

    public void addFunctionalityToScreen(V_Screen View, C_Screen Control, C_Main mainC, C_Data dataC) {
        
        
        View.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                //Verifica que el botón que lanzó el evento sea el click izquierdo
                if(evt.getModifiers() == MouseEvent.BUTTON1_MASK){
                    dataC.setCurrentScreeen(Control, mainC);
                    mainC.refreshItemsActionButtons();
                
                //Dispara el evento mousePressed del HDMI actual
                }else if(evt.getModifiers() == MouseEvent.BUTTON3_MASK){

                    V_HdmiOutput hdmiV = dataC.getCurrentHdmiOutput().getView();
                
                    if(hdmiV != null){
                    
                        Point pInferior = SwingUtilities.convertPoint(hdmiV, evt.getPoint(), hdmiV);


                        // Crear un nuevo MouseEvent para el panel inferior
                        MouseEvent nuevoEvento = new MouseEvent(
                            hdmiV,                          // Componente destino
                            evt.getID(),                    // ID del evento
                            evt.getWhen(),                  // Timestamp
                            evt.getModifiersEx(),           // Modificadores (Shift, Ctrl, etc)
                            pInferior.x,          // Coordenada X relativa al panelInferior
                            pInferior.y,          // Coordenada Y relativa al panelInferior
                            evt.getClickCount(),            // Número de clics
                            evt.isPopupTrigger(),           // ¿Es un popup (clic derecho)?
                            evt.getButton()                 // Botón presionado
                        );

                        // Enviar manualmente el evento a todos los MouseListener del panel inferior
                        for (MouseListener ml : hdmiV.getMouseListeners()) {
                            ml.mousePressed(nuevoEvento);
                        }
                    }
                }
            }
            
        });
        
        View.addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseDragged(MouseEvent evt) {
                
                //Dispara el evento mouseDragged del HDMI actual
                if(evt.getModifiers() == MouseEvent.BUTTON3_MASK){
                    V_HdmiOutput hdmiV = dataC.getCurrentHdmiOutput().getView();
                    
                    if(hdmiV != null){

                    Point pInferior = SwingUtilities.convertPoint(hdmiV, evt.getPoint(), hdmiV);

                        // Crear el nuevo evento
                        MouseEvent nuevoEvento = new MouseEvent(
                            hdmiV,
                            evt.getID(),
                            evt.getWhen(),
                            evt.getModifiersEx(),
                            pInferior.x,
                            pInferior.y,
                            evt.getClickCount(),
                            evt.isPopupTrigger(),
                            evt.getButton()
                        );

                        // Enviar el evento a los MouseMotionListeners del panel inferior
                        for (MouseMotionListener mml : hdmiV.getMouseMotionListeners()) {
                            mml.mouseDragged(nuevoEvento);
                        }
                    }
                }
            }
        });
        
    }

    
}
