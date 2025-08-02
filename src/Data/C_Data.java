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
import zMain.C_Main;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class C_Data{
    //Modelo
    private final M_Data Model;
    //Local Database
    private final ArrayList<C_Slave> arltSlaves;
    private final ArrayList<C_HdmiOutput> arltHdmis;
    private final ArrayList<C_Screen> arltScreens;
    private final ArrayList<C_Section> arltSections;
    
    private final ArrayList<C_Layer> arltLayers;
    
    //Elementos actuales
    private int iCurrentSlave;
    private int iCurrentHdmi;
    private int iCurrentScreeen;
    private int iCurrentSection;
    
    private int iCurrentLayer;
    
    private boolean bManualScreensLayout;

    
    //Constantes
    static final Color SELECTED_SCREEN_COLOR= new Color(24, 82, 88);
    static final Color SELECTED_SECTION_COLOR= new Color(61, 111, 86);
    static final Color NO_SELECTED_SCREEN_COLOR = new Color(24, 60, 68);
    static final Color NO_SELECTED_SECTION_COLOR= new Color(61, 91, 86);
    public static final Color SELECTED_BORDER_COLOR= Color.WHITE; 
    public static final Color NO_SELECTED_BORDER_COLOR= Color.BLACK;
    //Para metodos comprobativos (Checar nombre dupolicado)
    public static final int SLAVE= 0;
    public static final int SCREEN= 1;
    public static final int SECTION= 2;
    public static final int LAYER= 3;


    public C_Data() {
        this.Model = new M_Data();
        arltSlaves= new ArrayList<>();
        arltHdmis= new ArrayList<>();
        arltScreens= new ArrayList<>();
        arltSections= new ArrayList<>();
        
        arltLayers= new ArrayList<>();
        
        
        iCurrentSlave=-1;
        iCurrentHdmi=-1;
        iCurrentScreeen=-1;
        iCurrentSection=-1;
        iCurrentLayer= -1;
        
        bManualScreensLayout= true;
        
    }
    
    
    
    public void add(C_Slave slaveC, C_Main mainC) {
        Model.add(slaveC, arltSlaves);
        setCurrentSlave(slaveC, mainC);
    }
    
    /**
     * Añade y actualiza el focus al argumento
     * @param hdmiC
     * @param mainC
     */
    public void add(C_HdmiOutput hdmiC, C_Main mainC){
        hdmiC.setSlave(this.getCurrentSlave());
        Model.add(hdmiC, arltHdmis);
        setCurrentHdmi(hdmiC, mainC);
        
        C_Layer layerC= new C_Layer();
        layerC.setName("Default");
        add(layerC, mainC);
        
        refreshComponentsFromViews();
    }
    
    /**
     * Añade y actualiza el focus al argumento
     * @param screenC 
     * @param mainC 
     */
    public void add(C_Screen screenC, C_Main mainC){
        screenC.setOwner(this.getCurrentHdmiOutput());
        Model.add(screenC, arltScreens);
        setCurrentScreeen(screenC, mainC);
        refreshComponentsFromViews();
    }
    
    /**
     * Añade y actualiza el focus al argumento
     * @param sectionC 
     */
    public void add(C_Section sectionC, C_Main mainC){
        Model.add(sectionC, arltSections);
        sectionC.setOwner(this.getCurrentScreen());
        sectionC.setLayer(arltLayers.get(iCurrentLayer));
        setCurrentSection(sectionC, mainC);
        refreshComponentsFromViews();
    }
    
    /**
     * Añade y actualiza el focus al argumento
     * @param layerC
     * @param mainC 
     */
    public void add(C_Layer layerC, C_Main mainC){
        Model.add(layerC, arltLayers);
        layerC.setOwner(this.getCurrentHdmiOutput());
        setCurrentLayer(layerC, mainC);
        
        refreshComponentsFromViews();
    }
    
    //Load add
    
    public void add(C_Slave slaveC) {
        Model.add(slaveC, arltSlaves);
    }
    
    public void add(C_HdmiOutput hdmiC){
        Model.add(hdmiC, arltHdmis);
    }
    
    public void add(C_Screen screenC){
        Model.add(screenC, arltScreens);
    }
    
    public void add(C_Section sectionC){
        Model.add(sectionC, arltSections);
    }
    
    
    
    //Oportunidad de mejora cambiando el codigo pra que haga un cambio tipo cascada (Implementado en setCurrentSlave)
    
    public void setCurrentSlave(C_Slave slaveC, C_Main mainC) {
        //En En caso de ser null, se guarda como -1
        iCurrentSlave= slaveC != null ? arltSlaves.indexOf(slaveC) : -1;
        
        //Trata de utilizar el primer Hdmi como actual. Si no existe por lo menos uno, es null
        for (Iterator<C_HdmiOutput> iterator = arltHdmis.iterator(); iterator.hasNext();) {
            C_HdmiOutput hdmiC = iterator.next();
            
            if(hdmiC.getSlaveC() == slaveC){
                setCurrentHdmi(hdmiC , mainC);
                break;
            }else if(!iterator.hasNext()){
                setCurrentHdmi(null , mainC);
            }
        }
        
        
    }

    public void setCurrentHdmi(C_HdmiOutput hdmiC, C_Main mainC) {
        //En caso de ser null, se guarda como -1
        iCurrentHdmi = hdmiC != null ? arltHdmis.indexOf(hdmiC) : -1;

        iCurrentScreeen = -1;
        iCurrentSection = -1;

        //Actualiza la info que se despliega en los elementos afectados
        mainC.refreshHdmiInfo();
        mainC.refreshScreenInfo();
        mainC.refreshSectionInfo();
        
        refreshComponentsFromViews();
    }

    public void setCurrentScreeen(C_Screen screenC, C_Main mainC) {
        //En caso de ser null, se guarda como -1
        if(screenC != null){
            //Cambia el color de los bordes. Se hace antes de el cambio de screen para tener tambien la anterior current screen
            if(this.getCurrentScreen() != null)
                this.getCurrentScreen().setBorderColor(NO_SELECTED_BORDER_COLOR);
            screenC.setBorderColor(SELECTED_BORDER_COLOR);
            
            iCurrentScreeen = arltScreens.indexOf(screenC);
            
        }else
            iCurrentScreeen = -1;

        //Comprueba que la pantalla sea la misma, si lo es , la seccion seleccionada no cambia.
        if(screenC != this.getCurrentScreen())
            iCurrentSection = -1;

        //Actualiza la info que se despliega en los elementos afectados
        mainC.refreshScreenInfo();
        mainC.refreshSectionInfo();
        
        refreshComponentsFromViews();
    }

    public void setCurrentSection(C_Section sectionC, C_Main mainC) {
        if(sectionC != null){
            //Cambia el color de los bordes. Se hace antes de el cambio de seccion para tener tambien la anterior current section
            if(this.getCurrentSection()!= null)
                this.getCurrentSection().setBorderColor(NO_SELECTED_BORDER_COLOR);
            sectionC.setBorderColor(SELECTED_BORDER_COLOR);
            
            iCurrentSection = arltSections.indexOf(sectionC);
            
            //iCurrentScreeen = arltScreens.indexOf(sectionC.getOwner());
            this.setCurrentScreeen(sectionC.getOwner(), mainC);
            
            //Se hace el refresh primero por que se cambia el z order para que la seccion actual este en top
            refreshComponentsFromViews();
            //Probablemente deberia cambiar esto de lugar.. Pone la seccion en top para mas facilidad al trabajarla
            sectionC.getOwner().getView().setComponentZOrder(sectionC.getView(), 0);
            //-----
        }else{
            iCurrentSection = -1;
        }
        
        
        //Actualiza la info que se despliega en los elementos afectados
        //mainC.refreshScreenInfo();
        mainC.refreshSectionInfo();
        
    }
    
    public void setCurrentLayer(C_Layer layerC, C_Main mainC) {
        //En caso de ser null, se guarda como -1
        if(layerC != null){
            iCurrentLayer= arltLayers.indexOf(layerC);
            //this.getCurrentHdmiOutput().refreshGlobalZoom(mainC.getZoom());
            
        }else
            iCurrentLayer= -1;
        
        
        setCurrentSection(null, mainC);
        mainC.refreshHdmiInfo();
        mainC.refreshScreenInfo();
        mainC.refreshSectionInfo();
        
        refreshComponentsFromViews();
        
    }
    
    public C_Slave getCurrentSlave() {
        if(iCurrentSlave != -1)
            return arltSlaves.get(iCurrentSlave);
        else
            return null;
    }
    
    public C_HdmiOutput getCurrentHdmiOutput(){
        if(iCurrentHdmi != -1)
            return arltHdmis.get(iCurrentHdmi);
        else
            return null;
    }
    
    public C_Screen getCurrentScreen(){
        if(iCurrentScreeen != -1)
            return arltScreens.get(iCurrentScreeen);
        else
            return null;
    }
    
    public C_Section getCurrentSection(){
        if(iCurrentSection != -1)
            return arltSections.get(iCurrentSection);
        else
            return null;
    }
    
    public C_Layer getCurrentLayer(){
        if(iCurrentLayer != -1)
            return arltLayers.get(iCurrentLayer);
        else
            return null;
    }
    
    /**
     * Confirma que el nombre sea valido (No vacio) y que no este repetido.
     * @param sName
     * @param iMode Constantes en C_Data
     * @return 
     */
    public boolean isDuplicatedName(String sName, int iMode){
        return Model.isDuplicatedName(sName, iMode, arltSections, arltScreens, arltSlaves, arltLayers);
    }
    
    public void save(){
        Model.save(this);
    }
    
    public C_Data load(){
        return Model.load();
    }
    
    
    /**
     * Retorna una lista con todos los nombres de las pantallas del actual HdmiOutput seleccionado
     * @return Nombres de pantallas en HdmiOutput seleccionado
     */
    public ArrayList<String> getScreenNames(){
        return Model.getScreenNames(arltScreens, this.getCurrentHdmiOutput());
    }
    
    /**
     * Retorna una lista con todos los nombres de las pantallas del actual HdmiOutput seleccionado
     * @return Nombres de pantallas en HdmiOutput seleccionado
     */
    public ArrayList<String> getSlaveNames(){
        return Model.getSlaveNames(arltSlaves);
    }
    
    public C_Slave getSlave(String sName){
        return Model.getSlaveNames(sName, arltSlaves);
    }
    
    public C_Layer getLayer(String sName){
        return Model.gatLayer(sName, arltLayers);
    }
    
    /**
     * Refresca todas las vistas y sus componentes quitandolos y añadiendolos de nuevo.
     */
    public void refreshComponentsFromViews(){
        Model.refreshComponentsFromViews(bManualScreensLayout, this.getCurrentHdmiOutput(),this.getCurrentScreen(), this.getCurrentSection(), this.getCurrentLayer(), arltHdmis, arltScreens, arltSections);
    }

    public C_HdmiOutput getHdmiOutput(int iHdmiIndex) {
        return Model.getHdmiOutput(iHdmiIndex, arltHdmis);
    }

    public Dimension getSlaveDimension(C_Slave slaveC){
        return Model.getSlaveDimension(slaveC, arltHdmis);
    }

    public ArrayList<C_Slave> getSlaves() {
        return arltSlaves;
    }

    public ArrayList<C_HdmiOutput> getHdmis() {
        return arltHdmis;
    }

    public ArrayList<C_Screen> getScreens() {
        return arltScreens;
    }

    public ArrayList<C_Section> getSections() {
        return arltSections;
    }
    
    public ArrayList<C_Layer> getLayers() {
        return arltLayers;
    }
    
    /**
     * Encuentra el HDMI al que la pantalla pertenece. Se basa en su posicion para encontrar en que area de HDMI se encuentra
     * @param slaveC
     * @param screenC
     * @return 
     */
    public C_HdmiOutput getOwnerOf(C_Slave slaveC, C_Screen screenC){
        int iAcumulator=0;
        
        for (Iterator<C_HdmiOutput> iterator = arltHdmis.iterator(); iterator.hasNext();) {
            C_HdmiOutput hdmiC = iterator.next();
            
            if(hdmiC.getSlaveC() == slaveC){
                iAcumulator+= hdmiC.getTrueWidth();

                if(screenC.getTrueLocation().x < iAcumulator){
                    return hdmiC;
                }
            }
            
        }
        
        return null;
    }
    
    public void delete(C_Slave slaveC, C_Main mainC){
        
        ArrayList<C_HdmiOutput> arltTempSections= new ArrayList<>();
        arltTempSections.addAll(arltHdmis);
        for (Iterator<C_HdmiOutput> iterator = arltTempSections.iterator(); iterator.hasNext();) {
            C_HdmiOutput hdmiC = iterator.next();
            
            if(hdmiC.getSlaveC() == slaveC){
                delete(hdmiC, mainC);
            }
        }
        
        arltSlaves.remove(slaveC);
    }
    
    public void delete(C_HdmiOutput hdmiC, C_Main mainC){
        arltHdmis.remove(hdmiC);
        
        if(!arltHdmis.isEmpty()){
            setCurrentHdmi(arltHdmis.get(arltHdmis.size()-1), mainC);
        }else{
            setCurrentHdmi(null, mainC);
        }
        
        ArrayList<C_Screen> arltTempSections= new ArrayList<>();
        arltTempSections.addAll(arltScreens);
        for (Iterator<C_Screen> iterator = arltTempSections.iterator(); iterator.hasNext();) {
            C_Screen screenC = iterator.next();
            
            if(screenC.getOwner() == hdmiC){
                delete(screenC, mainC);
            }
        }
        
        this.refreshComponentsFromViews();
        mainC.refreshHdmiInfo();
    }
    
    public void delete(C_Screen screenC, C_Main mainC){
        arltScreens.remove(screenC);
        setCurrentScreeen(null, mainC);
        
        ArrayList<C_Section> arltTempSections= new ArrayList<>();
        arltTempSections.addAll(arltSections);
        for (Iterator<C_Section> iterator = arltTempSections.iterator(); iterator.hasNext();) {
            C_Section sectionC = iterator.next();
            
            if(sectionC.getOwner() == screenC){
                delete(sectionC, mainC);
            }
        }
        
        this.refreshComponentsFromViews();
        mainC.refreshScreenInfo();
        
    }

    public void delete(C_Section sectionC, C_Main mainC){
        arltSections.remove(sectionC);
        setCurrentSection(null, mainC);
        
        this.refreshComponentsFromViews();
        mainC.refreshSectionInfo();
    }

    /**
     * Calcula el desplazamiento horizontal (ancho acumulado) de una salida HDMI especificada
     * desde el principio de la lista de salidas HDMI hasta la salida HDMI indicada.
     *
     * @param hdmiC la salida HDMI de destino cuyo desplazamiento se calculará
     * @return el desplazamiento de la salida HDMI especificada, calculado como la suma de los anchos reales de las salidas HDMI anteriores, o 0 si la salida HDMI de destino es la primera de la lista
     */
    public int getDisplacementOfHdmiOutput(C_HdmiOutput hdmiC) {
        return Model.getDisplacementOfHdmiOutput(hdmiC, arltHdmis);
    }

    public void setManualScreensLayout(boolean bManualScreensLayout) {
        this.bManualScreensLayout = bManualScreensLayout;
    }
    
    public void generateLayers(){
        Model.generateLayers(arltSections, arltScreens, arltHdmis, arltLayers);
        this.refreshComponentsFromViews();
    }
    
}
