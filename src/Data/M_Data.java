/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package Data;

import HdmiOutput.C_HdmiOutput;
import Layer.C_Layer;
import Screen.C_Screen;
import Section.C_Section;
import Slave.C_Slave;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

class M_Data{
    

    void add(C_Slave slaveC, ArrayList<C_Slave> arltSlaves) {
        arltSlaves.add(slaveC);
    }
    
    void add(C_HdmiOutput hdmiC, ArrayList<C_HdmiOutput> arltHdmis) {
        arltHdmis.add(hdmiC);
    }

    void add(C_Screen screenC, ArrayList<C_Screen> arltScreens) {
        arltScreens.add(screenC);
    }

    void add(C_Section sectionC, ArrayList<C_Section> arltSections) {
        arltSections.add(sectionC);
    }
    
    void add(C_Layer layerC, ArrayList<C_Layer> arltLayers) {
        arltLayers.add(layerC);
    }

    /**
     * Retorna una lista con todos los nombres de las pantallas en el HdmiOutput actual
     * @param arltScreens
     * @return
     */
    ArrayList<String> getScreenNames(ArrayList<C_Screen> arltScreens, C_HdmiOutput currentHdmi) {
        ArrayList<String> arltScreenNames= new ArrayList<>();

        for(Iterator<C_Screen> iterator = arltScreens.iterator(); iterator.hasNext();) {
            C_Screen screenC = iterator.next();
            //Solo añade el nombre si la pantalla pertenece al HdmiOutput actual
            if(screenC.getOwner().equals(currentHdmi)){
                arltScreenNames.add(screenC.getName());
            }
        }

        return arltScreenNames;
    }

    ArrayList<String> getSlaveNames(ArrayList<C_Slave> arltSlaves) {
        ArrayList<String> arltSlaveNames= new ArrayList<>();
        for(Iterator<C_Slave> iterator = arltSlaves.iterator(); iterator.hasNext();) {
            C_Slave slaveC = iterator.next();
            arltSlaveNames.add(slaveC.getName());
        }

        return arltSlaveNames;
    }

    C_Slave getSlaveNames(String sName, ArrayList<C_Slave> arltSlaves) {
        for (Iterator<C_Slave> iterator = arltSlaves.iterator(); iterator.hasNext();) {
            C_Slave slaveC = iterator.next();
            if(slaveC.getName().equalsIgnoreCase(sName))
                return slaveC;
        }

        return null;
    }

    C_HdmiOutput getHdmiOutput(int iHdmiIndex, ArrayList<C_HdmiOutput> arltHdmis) {
        return arltHdmis.get(iHdmiIndex);
    }

    //Estos métodos se encargan de la estructura de las vistas (Las clases Vistas)-----------------------------------------------
    void refreshComponentsFromViews(boolean bManualScreensLayout, C_HdmiOutput hdmiC, C_Screen screenC, C_Section sectionC, C_Layer layerC, ArrayList<C_HdmiOutput> arltHdmis, ArrayList<C_Screen> arltScreens, ArrayList<C_Section> arltSections){
        this.removeComponentsFromViews(arltHdmis, arltScreens, arltSections);
        this.addComponentsFromViews(bManualScreensLayout, layerC, arltHdmis, arltScreens, arltSections);
        this.rainbow(screenC, sectionC, arltScreens, arltSections);

    }

    Dimension getSlaveDimension(C_Slave slaveC, ArrayList<C_HdmiOutput> arltHdmis) {
        Dimension d= new Dimension(0,0);

        for (C_HdmiOutput hdmiC : arltHdmis) {
            //Se asegura de que el HDMI pertenezca al slave
            if(hdmiC.getSlaveC() == slaveC){
                //Suma el ancho y el alto es el hdmi con la altura mas grande
                if(hdmiC.getTrueHeight() > d.getHeight())
                    d.setSize(d.getWidth() + hdmiC.getTrueWidth(), hdmiC.getTrueHeight());
                else
                    d.setSize(d.getWidth() + hdmiC.getTrueWidth(), d.getHeight());
            }
        }

        return d;
    }

    int getDisplacementOfHdmiOutput(C_HdmiOutput hdmiC, ArrayList<C_HdmiOutput> arltHdmis) {
        int iDisplacement= 0;

        for (C_HdmiOutput hdmiCTemp : arltHdmis) {
            //Para asegurar que pertenezcan al mismo slave
            if(hdmiC.getSlaveC() == hdmiCTemp.getSlaveC()){
                if(hdmiCTemp == hdmiC)
                    break;

                iDisplacement+= hdmiCTemp.getTrueWidth();
            }
        }

        return iDisplacement;
    }

    void save(C_Data dataC){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar base de datos local");
        int userSelection = fileChooser.showSaveDialog(null);
        ArrayList<Object> arltSave= new ArrayList<>();
        arltSave.add(dataC);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            
            File fileToSave = fileChooser.getSelectedFile();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileToSave))) {
                
                oos.writeObject(dataC);
                
                JOptionPane.showMessageDialog(null, "Datos guardados con éxito.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + e.getMessage());
            }
        }
    }
    
    C_Data load(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Cargar base de datos local");
        int userSelection = fileChooser.showOpenDialog(null);
        C_Data dataC = null;
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileToOpen))) {
                Object obj = ois.readObject();
                if (obj instanceof C_Data) {
                    dataC= (C_Data)obj;
                    //dataC.setCurrentHdmi(dataC.getHdmis().get(0));
                    //dataC.setCurrentScreeen(dataC.getScreens().get(0));
                    dataC.refreshComponentsFromViews();
                    
                }
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
            }
        }
        return dataC;
    }
    
    void generateLayers(ArrayList<C_Section> arltSections, ArrayList<C_Screen> arltScreens, ArrayList<C_HdmiOutput> arltHdmis, ArrayList<C_Layer> arltLayers){        
        ArrayList<C_Section> arltOrderSections= new ArrayList<>();
        ArrayList<C_Section> arltPlacedSections= new ArrayList<>();
        
        arltOrderSections.addAll(arltSections);
        
        arltOrderSections.sort((a, b) -> {
            double areaA = a.getTrueSize().width * a.getTrueSize().height;
            double areaB = b.getTrueSize().width * b.getTrueSize().height;
            
            return Double.compare(areaB, areaA); // orden descendente
        });
                
        int iLayerCount=-1;
        while(!arltOrderSections.isEmpty()){
            iLayerCount++;
            for (Iterator<C_HdmiOutput> iterator = arltHdmis.iterator(); iterator.hasNext();) {
                C_HdmiOutput hdmiC = iterator.next();

                C_Layer layerC= new C_Layer();
                layerC.setName(iLayerCount == 0 ? "FullScreen" : "Layer "+iLayerCount);
                
                layerC.setOwner(hdmiC);
                arltLayers.add(layerC);

                for (Iterator<C_Section> iterator1 = arltOrderSections.iterator(); iterator1.hasNext();) {
                    
                    //Quiero placear esta
                    C_Section orderSectionC = iterator1.next();
                    //String debug= orderSectionC.getName();
                    boolean bFlagPlaced=true;

                    for (Iterator<C_Section> iterator2 = arltPlacedSections.iterator(); iterator2.hasNext();) {
                        C_Section placedSectionC = iterator2.next();
                        //bFlagPlaced=false;

                        //Si la seccion que ya esta añadida pertenece al hdmi actual
                        if(orderSectionC.getOwner().getName().equalsIgnoreCase(placedSectionC.getOwner().getName())  && placedSectionC.getLayer().getName().equalsIgnoreCase(layerC.getName())){
                            bFlagPlaced= !areOverlapping(orderSectionC, placedSectionC);
                            if(!bFlagPlaced)
                                break;
                        }

                    }

                    if(bFlagPlaced){
                        orderSectionC.setLayer(layerC);
                        arltPlacedSections.add(orderSectionC);

                    }

                }

                for (Iterator<C_Section> iterator1 = arltPlacedSections.iterator(); iterator1.hasNext();) {
                    C_Section next = iterator1.next();
                    arltOrderSections.remove(next);
                }
            }
        
        }//While
        
    }
    
    private boolean areOverlapping(C_Section a, C_Section b) {
        return a.getTrueLocation().x < b.getTrueLocation().x + b.getTrueSize().width &&
               a.getTrueLocation().x + a.getTrueSize().width > b.getTrueLocation().x &&
               a.getTrueLocation().y < b.getTrueLocation().y + b.getTrueSize().height &&
               a.getTrueLocation().y + a.getTrueSize().height > b.getTrueLocation().y;
    }
    
    private void removeComponentsFromViews(ArrayList<C_HdmiOutput> arltHdmis, ArrayList<C_Screen> arltScreens, ArrayList<C_Section> arltSections){
        //Quita todos los componentes de los paneles
        for (Iterator<C_HdmiOutput> iterator = arltHdmis.iterator(); iterator.hasNext();) {
            C_HdmiOutput hdmiC = iterator.next();
            if(hdmiC.getView() != null)
                hdmiC.getView().removeAll();
            
        }

        for (Iterator<C_Screen> iterator = arltScreens.iterator(); iterator.hasNext();) {
            C_Screen screenC = iterator.next();
            if(screenC.getView() != null)
                screenC.getView().removeAll();
        }
    }

    private void addComponentsFromViews(boolean bManualScreensLayout, C_Layer layerC, ArrayList<C_HdmiOutput> arltHdmis, ArrayList<C_Screen> arltScreens, ArrayList<C_Section> arltSections){
        //Para acomodar las pantallas
        for (Iterator<C_HdmiOutput> it = arltHdmis.iterator(); it.hasNext();) {
            C_HdmiOutput hdmiC = it.next();
            ArrayList<C_Screen> separedScreensC= new ArrayList<>();

            for (Iterator<C_Screen> u = arltScreens.iterator(); u.hasNext();) {
                C_Screen screenC = u.next();
                if(screenC.getOwner() == hdmiC){
                    separedScreensC.add(screenC);
                }
            }
            //if(!bManualScreensLayout)
                //MaxRectsLayout.layoutPanels(separedScreensC, hdmiC.getTrueWidth(), hdmiC.getTrueHeight());
        }


        for (Iterator<C_Section> iterator = arltSections.iterator(); iterator.hasNext();) {
            C_Section sectionC = iterator.next();
            
            //Verifica que la seccion pertenezca a la layer actual
            if(layerC == sectionC.getLayer()){
                
                //Esto hace que se redimencionen las secciones cuando se cambia de capa para adaptarse al zoom
                sectionC.setTrueLocation(sectionC.getTrueLocation().x, sectionC.getTrueLocation().y);
                sectionC.setTrueSize(sectionC.getTrueSize().width, sectionC.getTrueSize().height);
                
                
                sectionC.getOwner().getView().add(sectionC.getView());
                sectionC.getOwner().getView().repaint();
                //sectionC.getOwner().getView().zoom(0);
            }
        }

        for (Iterator<C_Screen> iterator = arltScreens.iterator(); iterator.hasNext();) {
            C_Screen screenC = iterator.next();
            screenC.getOwner().getView().add(screenC.getView());
            screenC.getOwner().getView().repaint();
            //screenC.getOwner().getView().zoom(0);
        }
    }

    private void rainbow(C_Screen screenC, C_Section sectionC, ArrayList<C_Screen> arltScreens, ArrayList<C_Section> arltSections){
        for (C_Screen screenCTemp : arltScreens) {
            screenCTemp.setBackground(C_Data.NO_SELECTED_SCREEN_COLOR);
        }

        for (C_Section sectionCTemp : arltSections) {
            sectionCTemp.setBackground(C_Data.NO_SELECTED_SECTION_COLOR);
        }

        if(screenC!=null)
            screenC.setBackground(C_Data.SELECTED_SCREEN_COLOR);
        if(sectionC!=null)
            sectionC.setBackground(C_Data.SELECTED_SECTION_COLOR);

    }

    boolean isDuplicatedName(String sName, int iMode, ArrayList<C_Section> arltSections, ArrayList<C_Screen> arltScreens, ArrayList<C_Slave> arltSlaves,  ArrayList<C_Layer> arltLayers){
        ArrayList<Object> arltGeneric= new ArrayList<>();
        
        switch(iMode){
            case C_Data.SLAVE:
                arltGeneric.addAll(arltSlaves);
                break;
            case C_Data.SCREEN:
                arltGeneric.addAll(arltScreens);
                break;
            case C_Data.SECTION:
                arltGeneric.addAll(arltSections);
                break;
            case C_Data.LAYER:
                arltGeneric.addAll(arltLayers);
                break;
        }
        
        for(Object obj : arltGeneric) {
            String nombre = null;

            if (obj instanceof C_Slave) {
                nombre = ((C_Slave) obj).getName();
            } else if (obj instanceof C_Screen) {
                nombre = ((C_Screen) obj).getName();
            } else if (obj instanceof C_Section) {
                nombre = ((C_Section) obj).getName();
            } else if (obj instanceof C_Layer) {
                nombre = ((C_Layer) obj).getName();
            }

            if(nombre!= null && nombre.equalsIgnoreCase(sName)) {
                return false; //Ya existe ese nombre
            }
        }
        
        
        return true;
    }

    C_Layer gatLayer(String sName, ArrayList<C_Layer> arltLayers) {
        for (Iterator<C_Layer> iterator = arltLayers.iterator(); iterator.hasNext();) {
            C_Layer layerC = iterator.next();
            
            if(layerC.getName().equals(sName))
                return layerC;
        }

        return null;
    }
}
