/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package Section;

import Data.C_Data;
import HdmiOutput.V_HdmiOutput;
import Heirs.pnlZoomeable;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import zMain.C_Main;

class M_Section{
    private final Point pntMouseHoldTruePosition;
    private Point pntEndSectionTruePoint;
    private int iResizeMode;
    static final int INT_MARGIN=5;
    static final int RESIZE_OFF= 0;
    static final int RESIZE_HORIZONTAL= 1;
    static final int RESIZE_HORIZONTAL_INV= 2;
    static final int RESIZE_VERTICAL= 3;
    static final int RESIZE_VERTICAL_INV= 4;
    static final int RESIZE_BOTH= 5;
    static final int RESIZE_LOCK_HORIZONTAL= 6;
    static final int RESIZE_LOCK_HORIZONTAL_INV= 7;
    static final int RESIZE_LOCK_VERTICAL= 8;
    static final int RESIZE_LOCK_VERTICAL_INV= 9;

    public M_Section(){
        pntMouseHoldTruePosition= new Point();
        pntEndSectionTruePoint= new Point();
        iResizeMode= RESIZE_OFF;
    }

    int getMarginSize(){
        return INT_MARGIN;
    }

    void setEndSectionPoint(Point pntEndSectionTruePoint){
        this.pntEndSectionTruePoint= pntEndSectionTruePoint;
    }

    Point getEndSectionPoint(){
        return pntEndSectionTruePoint;
    }

    void addFunctionalityToMovementPanel(JPanel pnlMovement, C_Section sectionC, C_Main mainC, C_Data dataC) {
        //Guarda las coordenadas del mouse
        pnlMovement.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                //Verifica que el botón que lanzó el evento sea el click izquierdo
                if(evt.getModifiers() == MouseEvent.BUTTON1_MASK && dataC.getCurrentSection() == sectionC){
                    pntMouseHoldTruePosition.setLocation(evt.getX(), evt.getY());
                    
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
            
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                //Verifica que el botón que lanzó el evento sea el click izquierdo
                if(evt.getModifiers() == MouseEvent.BUTTON1_MASK){
                    dataC.setCurrentSection(sectionC, mainC);
                    mainC.refreshItemsActionButtons();
                }
            }
        });

        pnlMovement.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                //Verifica que el botón que lanzó el evento sea el click izquierdo
                if(evt.getModifiers() == MouseEvent.BUTTON1_MASK && dataC.getCurrentSection() == sectionC){
                    
                    int newX = (sectionC.getTrueLocation().x + pnlZoomeable.getValueInZoom(evt.getX() - pntMouseHoldTruePosition.x));
                    int newY = (sectionC.getTrueLocation().y + pnlZoomeable.getValueInZoom(evt.getY()-pntMouseHoldTruePosition.y));
                    sectionC.setTrueLocation(newX, newY, mainC.getGridSize());
                    
                //Dispara el evento mouseDragged del HDMI actual
                } else if(evt.getModifiers() == MouseEvent.BUTTON3_MASK){
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

    void addFunctionalityToResizePanel(JPanel pnlMargin, C_Section sectionC,  C_Main mainC, C_Data dataC) {
        pnlMargin.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent evt) {
                //Si es la seccion actual
                if(dataC.getCurrentSection() == sectionC){

                    //Derecha
                    if(evt.getX() >= (pnlMargin.getWidth()-(INT_MARGIN*2)))
                        pnlMargin.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                        //Izquierda
                    else if(evt.getX() < INT_MARGIN)
                        pnlMargin.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                        //Abajo
                    else if(evt.getY() >= (pnlMargin.getHeight()-(INT_MARGIN*2)))
                        pnlMargin.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                        //Arriba
                    else if(evt.getY() < INT_MARGIN)
                        pnlMargin.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                }
                
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                //Si es la seccion actual
                if(dataC.getCurrentSection() == sectionC){
                    pnlMargin.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
                
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                
                //Verifica que el botón que lanzó el evento sea el click izquierdo
                if(evt.getModifiers() == MouseEvent.BUTTON1_MASK && dataC.getCurrentSection() == sectionC){
                    //Si es la seccion actual
                    if(dataC.getCurrentSection() == sectionC){

                        pntMouseHoldTruePosition.setLocation(pnlZoomeable.getValueInZoom(evt.getX()), pnlZoomeable.getValueInZoom(evt.getY()));
                        pntEndSectionTruePoint.setLocation(sectionC.getTrueLocation().x+sectionC.getTrueSize().width, sectionC.getTrueLocation().y +sectionC.getTrueSize().height);



                        //Lógica que checa si el boton Lock esta activo
                        dataC.setCurrentSection(sectionC, mainC);
                        if(mainC.isAspectRatioLock() && evt.getX() >= (pnlMargin.getWidth()-(INT_MARGIN*2)))
                            iResizeMode= RESIZE_LOCK_HORIZONTAL;
                        //Izquierda
                        else if(mainC.isAspectRatioLock() && evt.getX() < INT_MARGIN)
                            iResizeMode= RESIZE_LOCK_HORIZONTAL_INV;
                        //Abajo
                        else if(mainC.isAspectRatioLock() && evt.getY() >= (pnlMargin.getHeight()-(INT_MARGIN*2)))
                            iResizeMode= RESIZE_LOCK_VERTICAL;
                        //Arriba
                        else if(mainC.isAspectRatioLock() && evt.getY() < INT_MARGIN)
                            iResizeMode= RESIZE_LOCK_VERTICAL_INV;


                        //Derecha
                        else if(evt.getX() >= (pnlMargin.getWidth()-(INT_MARGIN*2)))
                            iResizeMode= RESIZE_HORIZONTAL;
                        //Izquierda
                        else if(evt.getX() < INT_MARGIN)
                            iResizeMode= RESIZE_HORIZONTAL_INV;
                        //Abajo
                        else if(evt.getY() >= (pnlMargin.getHeight()-(INT_MARGIN*2)))
                            iResizeMode= RESIZE_VERTICAL;
                        //Arriba
                        else if(evt.getY() < INT_MARGIN)
                            iResizeMode= RESIZE_VERTICAL_INV;
                    }
                    
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

            @Override
            public void mouseReleased(MouseEvent evt) {
                //Verifica que el botón que lanzó el evento sea el click izquierdo
                if(evt.getModifiers() == MouseEvent.BUTTON1_MASK){
                    //Si es la seccion actual
                    if(dataC.getCurrentSection() == sectionC){
                        iResizeMode= RESIZE_OFF;
                    }else{
                        //Por si hacen click al borde buscando seleccionar la seccion
                        dataC.setCurrentSection(sectionC, mainC);
                        mainC.refreshItemsActionButtons();
                    }
                }
            }


        });


        pnlMargin.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent evt) {
                //Verifica que el botón que lanzó el evento sea el click izquierdo
                if(evt.getModifiers() == MouseEvent.BUTTON1_MASK && dataC.getCurrentSection() == sectionC){
                    //Si es la seccion actual
                    if(dataC.getCurrentSection() == sectionC){
                        int iEvtTrueX= pnlZoomeable.getValueInZoom(evt.getX());
                        int iEvtTrueY= pnlZoomeable.getValueInZoom(evt.getY());
                        int x= 0;
                        int y= 0;
                        int width= 0;
                        int height= 0;
                        switch(iResizeMode) {
                            case RESIZE_HORIZONTAL:
                                width= iEvtTrueX  - pntMouseHoldTruePosition.x + pntEndSectionTruePoint.x - sectionC.getTrueLocation().x;
                                height= pnlZoomeable.INT_KEEP_VALUE;

                                sectionC.setTrueSize(width, height, mainC.getGridSize());
                                break;

                            case RESIZE_HORIZONTAL_INV:
                                //Al estar pegado al borde derecho se presenta un problema pues si quiero hacerlo mas chico necesito cambiar primero la
                                //Dimencion y luego la locacion, caso contrario a cuando quiero hacerlo mas grande donde debo cambiar primero la locacion
                                //Y luego la dimencion. Este if separa estos dos casos dependiendo si amplio la dimencion o la reduzco.
                                if(iEvtTrueX > 0){
                                    width= -iEvtTrueX  - pntMouseHoldTruePosition.x + pntEndSectionTruePoint.x - sectionC.getTrueLocation().x;
                                    height= pnlZoomeable.INT_KEEP_VALUE;


                                    int iScrrenW= sectionC.getOwner().getTrueSize().width;
                                    int iC= iScrrenW - sectionC.getTrueLocation().x - sectionC.getTrueSize().width;

                                    sectionC.setTrueSize(width, height, mainC.getGridSize());

                                    x= iScrrenW - iC -sectionC.getTrueSize().width;
                                    y= pnlZoomeable.INT_KEEP_VALUE;

                                    sectionC.setTrueLocation(x, y, mainC.getGridSize());
                                }else{
                                    x= sectionC.getTrueLocation().x + iEvtTrueX - pntMouseHoldTruePosition.x;
                                    y= pnlZoomeable.INT_KEEP_VALUE;
                                    width= sectionC.getTrueLocation().x;
                                    height= pnlZoomeable.INT_KEEP_VALUE;

                                    sectionC.setTrueLocation(x, y, mainC.getGridSize());

                                    //La differencia de la locacion anterior y la actual
                                    width-= sectionC.getTrueLocation().x; //No lo tomo del valor X ya que el metodo setTrueLocation cambia la posicion final dependiendo de la rejilla

                                    width+= sectionC.getTrueSize().width;


                                    //Evita que se siga crecientdo si se topa con el 0
                                    if(sectionC.getTrueLocation().x >= 0)
                                        sectionC.setTrueSize(width, height, mainC.getGridSize());

                                }
                                break;

                            case RESIZE_VERTICAL:
                                width= pnlZoomeable.INT_KEEP_VALUE;
                                height= iEvtTrueY  - pntMouseHoldTruePosition.y + pntEndSectionTruePoint.y - sectionC.getTrueLocation().y;


                                sectionC.setTrueSize(width, height, mainC.getGridSize());
                                break;

                            case RESIZE_VERTICAL_INV:
                                //Mismo caso que RESIZE_HORIZONTAL_INV. Leer nota para mas info
                                if(iEvtTrueY > 0){
                                    width= pnlZoomeable.INT_KEEP_VALUE;
                                    height= -iEvtTrueY  - pntMouseHoldTruePosition.y + pntEndSectionTruePoint.y - sectionC.getTrueLocation().y;


                                    int iScrrenH= sectionC.getOwner().getTrueSize().height;
                                    int iC= iScrrenH - sectionC.getTrueLocation().y - sectionC.getTrueSize().height;

                                    sectionC.setTrueSize(width, height, mainC.getGridSize());

                                    x= pnlZoomeable.INT_KEEP_VALUE;
                                    y= iScrrenH - iC -sectionC.getTrueSize().height;

                                    sectionC.setTrueLocation(x, y, mainC.getGridSize());
                                }else{
                                    x= pnlZoomeable.INT_KEEP_VALUE;
                                    y= sectionC.getTrueLocation().y + iEvtTrueY - pntMouseHoldTruePosition.y;
                                    width= pnlZoomeable.INT_KEEP_VALUE;
                                    height= sectionC.getTrueLocation().y;

                                    sectionC.setTrueLocation(x, y, mainC.getGridSize());
                                    //La differencia de la locacion anterior y la actual
                                    height-= sectionC.getTrueLocation().y; //No lo tomo del valor Y ya que el metodo setTrueLocation cambia la posicion final dependiendo de la rejilla
                                    height+= sectionC.getTrueSize().height;
                                    //Evita que se siga crecientdo si se topa con el 0
                                    if(sectionC.getTrueLocation().y >= 0)
                                        sectionC.setTrueSize(width, height);
                                }
                                break;


                            //Con el botón de conservar ratio de aspecto activo
                            case RESIZE_LOCK_HORIZONTAL:
                                width= iEvtTrueX - pntMouseHoldTruePosition.x + pntEndSectionTruePoint.x - sectionC.getTrueLocation().x;
                                height= pnlZoomeable.INT_KEEP_ASPECT_RATIO;

                                sectionC.setTrueSize(width, height, mainC.getGridSize());
                                break;

                            case RESIZE_LOCK_HORIZONTAL_INV:
                                x= sectionC.getTrueLocation().x + iEvtTrueX - pntMouseHoldTruePosition.x;
                                y= pnlZoomeable.INT_KEEP_VALUE;
                                width= sectionC.getTrueLocation().x;
                                height= pnlZoomeable.INT_KEEP_ASPECT_RATIO;

                                sectionC.setTrueLocation(x, y, mainC.getGridSize());

                                //La differencia de la locacion anterior y la actual
                                width-= sectionC.getTrueLocation().x; //No lo tomo del valor X ya que el metodo setTrueLocation cambia la posicion final dependiendo de la rejilla
                                width+= sectionC.getTrueSize().width;

                                //Evita que se siga crecientdo si se topa con el 0
                                if(sectionC.getTrueLocation().x >= 0)
                                    sectionC.setTrueSize(width, height);
                                break;

                            case RESIZE_LOCK_VERTICAL:
                                height= iEvtTrueY  - pntMouseHoldTruePosition.y + pntEndSectionTruePoint.y - sectionC.getTrueLocation().y;
                                width= pnlZoomeable.INT_KEEP_ASPECT_RATIO;

                                sectionC.setTrueSize(width, height, mainC.getGridSize());
                                break;

                            case RESIZE_LOCK_VERTICAL_INV:
                                x= pnlZoomeable.INT_KEEP_VALUE;
                                y= sectionC.getTrueLocation().y + iEvtTrueY - pntMouseHoldTruePosition.y;
                                height= sectionC.getTrueLocation().y;
                                width= pnlZoomeable.INT_KEEP_ASPECT_RATIO;

                                sectionC.setTrueLocation(x, y, mainC.getGridSize());
                                //La differencia de la locacion anterior y la actual
                                height-= sectionC.getTrueLocation().y; //No lo tomo del valor Y ya que el metodo setTrueLocation cambia la posicion final dependiendo de la rejilla
                                height+= sectionC.getTrueSize().height;
                                //Evita que se siga crecientdo si se topa con el 0
                                if(sectionC.getTrueLocation().y >= 0)
                                    sectionC.setTrueSize(width, height);

                                break;

                        }

                        mainC.refreshSectionInfo();
                    }
                    
                //Dispara el evento mouseDragged del HDMI actual
                } else if(evt.getModifiers() == MouseEvent.BUTTON3_MASK){
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

    void setBorderColor(JPanel pnlMargin, Color c) {
        pnlMargin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(c, 1),
            null
        ));
    }
}
