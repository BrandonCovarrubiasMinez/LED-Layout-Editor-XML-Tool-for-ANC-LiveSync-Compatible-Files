/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package XML;

import Data.C_Data;
import HdmiOutput.C_HdmiOutput;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import Screen.C_Screen;
import Section.C_Section;
import Slave.C_Slave;
import java.awt.Point;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import zMain.C_Main;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

class M_XMLGenerator{
    


    void createXMLDocument(C_Data dataC) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document xmlDoc = builder.newDocument();

        doIt(xmlDoc, dataC);

        exportXml(xmlDoc);
    }
    
    private static final Map<String, List<String>> attributeOrders = createAttributeOrders();

    private static Map<String, List<String>> createAttributeOrders() {
        Map<String, List<String>> map = new HashMap<>();

        map.put("slave", Arrays.asList(
            "name", "x", "y", "width", "height", "address", "backup",
            "useVSync", "antialiasing", "hinting", "refreshDelay", "executeDelay",
            "videoFrameBuffers", "loggingLevel", "timeout", "topMost"
        ));

        map.put("display", Arrays.asList(
            "name", "slave", "default_transition", "default_duration", "resolution", "audio_device"
        ));

        map.put("mapping", Arrays.asList("src", "dst"));
        map.put("preview", Arrays.asList("resolution", "framerate"));
        map.put("background", Arrays.asList("color"));
        map.put("section", Arrays.asList("name", "folder"));
        map.put("source", Arrays.asList("substratum", "player"));

        return map;
    }
    
    private void doIt(Document xmlDoc, C_Data dataC){

        Element displays = xmlDoc.createElement("displays");

        for (Iterator<C_Slave> iterator = dataC.getSlaves().iterator(); iterator.hasNext();) {
            C_Slave slaveC = iterator.next();
            
                Element slave = createElementWithAttribute(xmlDoc, "slave", new String[][]{
                    {"name", slaveC.getName()},
                    {"x", slaveC.getOffset()+""},
                    {"y", "0"},
                    {"width", String.valueOf(dataC.getSlaveDimension(slaveC).width)},
                    {"height", String.valueOf(dataC.getSlaveDimension(slaveC).height)},
                    {"address", slaveC.getAddress()},
                    {"backup", slaveC.getBackup()},
                    {"useVSync", "false"},
                    {"antialiasing", "antialias"},
                    {"hinting", "full"},
                    {"refreshDelay", "0.1"},
                    {"executeDelay", "0"},
                    {"videoFrameBuffers", "120"},
                    {"loggingLevel", "Information"},
                    {"timeout", "5"},
                    {"topMost", "true"}
            });

            displays.appendChild(slave);
            
        }
        

        for (C_Screen screenC : dataC.getScreens()) {
            Element mapping = createElementWithAttribute(xmlDoc, "mapping", new String[][]{
                    {"src", "0,0,"+screenC.getTrueSize().width+","+screenC.getTrueSize().height},
                    {"dst", (screenC.getTrueLocation().x + dataC.getDisplacementOfHdmiOutput(screenC.getOwner())) + ","+screenC.getTrueLocation().y}
            });

            Element preview = createElementWithAttribute(xmlDoc, "preview", new String[][]{
                    {"resolution", Math.round(screenC.getTrueSize().getWidth() / 2) + "," + Math.round(screenC.getTrueSize().getHeight() / 2)},
                    {"framerate", "5"}
            });

            Element background = createElementWithAttribute(xmlDoc, "background", new String[][]{
                    {"color", "0,0,0,0"}
            });

            Element display;
            if(screenC.getAudioDevice().isEmpty()){
                display= createElementWithAttribute(xmlDoc, "display", new String[][]{
                        {"name", screenC.getName()},
                        {"slave", screenC.getOwner().getSlaveC().getName()},
                        {"default_transition", "0.1"},
                        {"default_duration", screenC.getDefaultDuration()+""},
                        {"resolution", screenC.getTrueSize().width+","+screenC.getTrueSize().height}
                });
            }else{
                display= createElementWithAttribute(xmlDoc, "display", new String[][]{
                    {"name", screenC.getName()},
                    {"slave", screenC.getOwner().getSlaveC().getName()},
                    {"default_transition", "0.1"},
                    {"default_duration", screenC.getDefaultDuration()+""},
                    {"audio_device", screenC.getAudioDevice()},
                    {"resolution", screenC.getTrueSize().width+","+screenC.getTrueSize().height}
                });
            }
            

            Element sections = xmlDoc.createElement("sections");


            //Secciones del contenido
            for (C_Section sectionC : dataC.getSections()) {
                //Se asegura de que la seccion pertenezca a la pantalla
                if(screenC == sectionC.getOwner()) {
                    Element section;
                    if(sectionC.getFolder().isEmpty()){
                        section = createElementWithAttribute(xmlDoc, "section", new String[][]{
                            {"name", sectionC.getName()},
                        });
                    }else{
                        section = createElementWithAttribute(xmlDoc, "section", new String[][]{
                            {"name", sectionC.getName()},
                            {"folder", sectionC.getFolder()}
                        });
                    }
                    //Tamaño y posición de la sección
                    Element output = xmlDoc.createElement("output");
                    output.appendChild(xmlDoc.createTextNode(sectionC.getTrueLocation().x + "," + sectionC.getTrueLocation().y + "," + sectionC.getTrueSize().width + "," + sectionC.getTrueSize().height));

                    //Jerarquia de sección
                    section.appendChild(output);
                    sections.appendChild(section);
                }
            }

            //La ruta del contenido
            Element media = xmlDoc.createElement("media");
            Element source = createElementWithAttribute(xmlDoc, "source", new String[][]{
                    {"substratum", "C:\\LiveSync Media\\"+screenC.getName()},
                    {"player", "C:\\LiveSync Media\\"+screenC.getName()}
            });
            media.appendChild(source);

            display.appendChild(mapping);
            display.appendChild(preview);
            display.appendChild(background);
            display.appendChild(sections);
            display.appendChild(media);

            displays.appendChild(display);
        }

        xmlDoc.appendChild(displays);
    }
    
    private void save(Document xmlDoc, C_Data dataC){

        Element displays = xmlDoc.createElement("displays");

        for (Iterator<C_Slave> iterator = dataC.getSlaves().iterator(); iterator.hasNext();) {
            C_Slave slaveC = iterator.next();
            
                Element slave = createElementWithAttribute(xmlDoc, "slave", new String[][]{
                    {"name", slaveC.getName()},
                    {"x", slaveC.getOffset()+""},
                    {"y", "0"},
                    {"width", String.valueOf(dataC.getSlaveDimension(slaveC).width)},
                    {"height", String.valueOf(dataC.getSlaveDimension(slaveC).height)},
                    {"address", slaveC.getAddress()},
                    {"backup", slaveC.getBackup()},
                    {"useVSync", "false"},
                    {"antialiasing", "antialias"},
                    {"hinting", "full"},
                    {"refreshDelay", "0.1"},
                    {"executeDelay", "0"},
                    {"videoFrameBuffers", "120"},
                    {"loggingLevel", "Information"},
                    {"timeout", "5"},
                    {"topMost", "true"}
            });

            displays.appendChild(slave);
            
        }
        

        for (C_Screen screenC : dataC.getScreens()) {
            Element mapping = createElementWithAttribute(xmlDoc, "mapping", new String[][]{
                    {"src", "0,0,"+screenC.getTrueSize().width+","+screenC.getTrueSize().height},
                    {"dst", (screenC.getTrueLocation().x + dataC.getDisplacementOfHdmiOutput(screenC.getOwner())) + ","+screenC.getTrueLocation().y}
            });

            Element preview = createElementWithAttribute(xmlDoc, "preview", new String[][]{
                    {"resolution", Math.round(screenC.getTrueSize().getWidth() / 2) + "," + Math.round(screenC.getTrueSize().getHeight() / 2)},
                    {"framerate", "5"}
            });

            Element background = createElementWithAttribute(xmlDoc, "background", new String[][]{
                    {"color", "0,0,0,0"}
            });

            Element display;
            if(screenC.getAudioDevice().isEmpty()){
                display= createElementWithAttribute(xmlDoc, "display", new String[][]{
                        {"name", screenC.getName()},
                        {"slave", screenC.getOwner().getSlaveC().getName()},
                        {"default_transition", "0.1"},
                        {"default_duration", screenC.getDefaultDuration()+""},
                        {"resolution", screenC.getTrueSize().width+","+screenC.getTrueSize().height}
                });
            }else{
                display= createElementWithAttribute(xmlDoc, "display", new String[][]{
                    {"name", screenC.getName()},
                    {"slave", screenC.getOwner().getSlaveC().getName()},
                    {"default_transition", "0.1"},
                    {"default_duration", screenC.getDefaultDuration()+""},
                    {"audio_device", screenC.getAudioDevice()},
                    {"resolution", screenC.getTrueSize().width+","+screenC.getTrueSize().height}
                });
            }
            

            Element sections = xmlDoc.createElement("sections");


            //Secciones del contenido
            for (C_Section sectionC : dataC.getSections()) {
                //Se asegura de que la seccion pertenezca a la pantalla
                if(screenC == sectionC.getOwner()) {
                    Element section;
                    if(sectionC.getFolder().isEmpty()){
                        section = createElementWithAttribute(xmlDoc, "section", new String[][]{
                            {"name", sectionC.getName()},
                        });
                    }else{
                        section = createElementWithAttribute(xmlDoc, "section", new String[][]{
                            {"name", sectionC.getName()},
                            {"folder", sectionC.getFolder()}
                        });
                    }
                    //Tamaño y posición de la sección
                    Element output = xmlDoc.createElement("output");
                    output.appendChild(xmlDoc.createTextNode(sectionC.getTrueLocation().x + "," + sectionC.getTrueLocation().y + "," + sectionC.getTrueSize().width + "," + sectionC.getTrueSize().height));

                    //Jerarquia de sección
                    section.appendChild(output);
                    sections.appendChild(section);
                }
            }

            //La ruta del contenido
            Element media = xmlDoc.createElement("media");
            Element source = createElementWithAttribute(xmlDoc, "source", new String[][]{
                    {"substratum", "C:\\LiveSync Media\\"+screenC.getName()},
                    {"player", "C:\\LiveSync Media\\"+screenC.getName()}
            });
            media.appendChild(source);

            display.appendChild(mapping);
            display.appendChild(preview);
            display.appendChild(background);
            display.appendChild(sections);
            display.appendChild(media);

            displays.appendChild(display);
        }

        xmlDoc.appendChild(displays);
    }

    void exportXml(Document docDocument) throws IOException {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save XML File");
            fileChooser.setSelectedFile(new File("displays.xml"));
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("XML Files", "xml"));

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File outputFile = fileChooser.getSelectedFile();

                // Asegura que tenga extensión .xml
                if (!outputFile.getName().toLowerCase().endsWith(".xml")) {
                    outputFile = new File(outputFile.getAbsolutePath() + ".xml");
                }

                // Verifica si el archivo ya existe
                if (outputFile.exists()) {
                    int option = JOptionPane.showConfirmDialog(
                        null,
                        "The file \"" + outputFile.getName() + "\" already exists.\nDo you want to replace it?",
                        "Confirm replacement",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );

                    if (option != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                try (Writer writer = new FileWriter(outputFile)) {
                    writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
                    writeXmlWithAttributeOrder(docDocument.getDocumentElement(), writer, "");
                }

            }
        }



    
    private static void reorderElementAndChildren(Element element, Document doc) {
        String tag = element.getTagName();
        if (attributeOrders.containsKey(tag)) {
            reorderAttributes(doc, element, attributeOrders.get(tag));
        }

        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                reorderElementAndChildren((Element) node, doc);
            }
        }
    }

    private static void reorderAttributes(Document doc, Element element, List<String> desiredOrder) {
        NamedNodeMap originalAttrs = element.getAttributes();
        Map<String, String> attributes = new LinkedHashMap<>();

        for (int i = 0; i < originalAttrs.getLength(); i++) {
            Attr attr = (Attr) originalAttrs.item(i);
            attributes.put(attr.getName(), attr.getValue());
        }

        // Eliminar todos los atributos actuales
        while (element.getAttributes().getLength() > 0) {
            element.removeAttributeNode((Attr) element.getAttributes().item(0));
        }

        // Agregar primero los conocidos en orden
        for (String attrName : desiredOrder) {
            if (attributes.containsKey(attrName)) {
                element.setAttribute(attrName, attributes.remove(attrName));
            }
        }

        // Luego los no conocidos en su orden original
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            element.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    public C_Data importDisplayDoc(C_Main mainC) throws SAXException, ParserConfigurationException, IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
        
        
        
        
        // Mostrar el diálogo para seleccionar archivo
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            C_Data dataC= new C_Data();
            C_Main newMainC= new C_Main(dataC);

            //Cambia el modo de acomodo de pantallas a manual
            dataC.setManualScreensLayout(true);
        
            File selectedFile = fileChooser.getSelectedFile();

            // Crear el parser de XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xmlDoc = builder.parse(selectedFile);

            // Normalizar el documento
            xmlDoc.getDocumentElement().normalize();
            
            // Obtener el elemento raiz "displays"
            Element rootElement = xmlDoc.getDocumentElement();
            
            // Procesar los slaves
            NodeList slaveNodes = rootElement.getElementsByTagName("slave");
            for (int i = 0; i < slaveNodes.getLength(); i++) {
                
                //Slave
                
                Element slaveElement = (Element) slaveNodes.item(i);
                C_Slave slaveC = new C_Slave();
                
                slaveC.setName(slaveElement.getAttribute("name"));
                slaveC.setBackup(slaveElement.getAttribute("backup"));
                slaveC.setAddress(slaveElement.getAttribute("address"));
                slaveC.setOffset(Integer.parseInt(slaveElement.getAttribute("x")));
                
                // Agregar el slave a dataC
                dataC.add(slaveC);

                
                
                // Procesar los HdmiOutputs
                
                int iWidth, iHeight, iHdmiAmount;
                iHdmiAmount=1;
                //Pregunta por la cantidad de HDMI Por Slave pues es una cantidad que no se encuentra en el documento al importar
                //iHdmiAmount= Integer.parseInt( JOptionPane.showInputDialog("Enter the amount of HDMI outputs that seeks to simulate in the Slave:" + slaveC.getName() ));
                iWidth= Integer.parseInt(slaveElement.getAttribute("width")) /*/ iHdmiAmount*/;
                iHeight= Integer.parseInt(slaveElement.getAttribute("height"));
                
                for (int j = 0; j < iHdmiAmount; j++) {
                    C_HdmiOutput hdmiC = new C_HdmiOutput();

                    hdmiC.setSlave(slaveC);
                    hdmiC.setTrueSize(iWidth, iHeight);

                    // Agregar el HdmiOutput a dataC
                    dataC.add(hdmiC);
                }
            }
            
            
            
            
            
            // Procesar las pantallas (displays)
            NodeList displayNodes = rootElement.getElementsByTagName("display");
            for (int i = 0; i < displayNodes.getLength(); i++) {
                Element displayElement = (Element) displayNodes.item(i);
                //Crear nueva pantalla
                C_Screen screenC = new C_Screen(newMainC, dataC);
                
                //Nombre
                screenC.setName(displayElement.getAttribute("name"));

                //Obtener resolución
                String sResolution = displayElement.getAttribute("resolution");
                String[] dimensions = sResolution.split(",");
                screenC.setTrueSize(Integer.parseInt(dimensions[0].trim()), Integer.parseInt(dimensions[1].trim()));
                
                //Obtener Locación
                String sLocacion = ((Element)(displayElement.getElementsByTagName("mapping").item(0))).getAttribute("dst");
                String[] coords = sLocacion.split(",");
                screenC.setTrueLocation(Integer.parseInt(coords[0].trim()), Integer.parseInt(coords[1].trim()));

                //Busca el hdmi al que pertenece la pantalla
                
                screenC.setOwner( dataC.getOwnerOf( dataC.getSlave(displayElement.getAttribute("slave")) , screenC) );
                
                screenC.setTrueLocation(Integer.parseInt(coords[0].trim())- dataC.getDisplacementOfHdmiOutput(screenC.getOwner()), Integer.parseInt(coords[1].trim()));

                //Audio Device
                screenC.setAudioDevice(displayElement.getAttribute("audio_device"));
                
                //Default Duration
                screenC.setDefaultDuration(Integer.parseInt(displayElement.getAttribute("default_duration").trim()));
                
                // Agregar la pantalla a dataC
                dataC.add(screenC);

                
                
                
                
                
                
                // Procesar secciones
                NodeList sectionsNode = displayElement.getElementsByTagName("sections");
                NodeList sectionNodes = ((Element)(sectionsNode.item(0))).getElementsByTagName("section");
                for (int j = 0; j < sectionNodes.getLength(); j++) {
                    
                    Element sectionElement = (Element) sectionNodes.item(j);
                    C_Section sectionC = new C_Section(newMainC, dataC);
                                        
                    //Nombre
                    sectionC.setName(sectionElement.getAttribute("name"));

                    // Obtener output (posición y tamaño)
                    Element outputElement = (Element) sectionElement.getElementsByTagName("output").item(0);
                    String[] coordinates = outputElement.getTextContent().split(",");
                    sectionC.setTrueLocation( Integer.parseInt(coordinates[0].trim()), Integer.parseInt(coordinates[1].trim()));
                    sectionC.setTrueSize(Integer.parseInt(coordinates[2].trim()), Integer.parseInt(coordinates[3].trim()));

                    sectionC.setOwner(screenC);
                    
                    //Para que no se salgan de la screen
                    sectionC.setMaxTrueSize(screenC.getTrueSize());
                    sectionC.setMaxTrueLocationArea(new Point(screenC.getTrueSize().width, screenC.getTrueSize().height));
                    
                    //Folder
                    sectionC.setFolder(sectionElement.getAttribute("folder"));
                    
                    dataC.add(sectionC);
                    
                }


            }
            
            dataC.generateLayers();
            newMainC.refreshTable();
            newMainC.refreshComboSlave();
            //fuerza a actualizar la capa
            
            
            for (Iterator<C_HdmiOutput>  iterator= dataC.getHdmis().iterator(); iterator.hasNext();) {
                C_HdmiOutput hdmiC = iterator.next();
                hdmiC.refreshGlobalZoom(1);
            }
            
            dataC.setCurrentLayer(dataC.getLayers().get(0), newMainC);
            mainC.dispose();
            return dataC;
        }else
            return null;
    }
    
    public C_Data load(C_Main mainC) throws SAXException, ParserConfigurationException, IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
        
        
        
        
        // Mostrar el diálogo para seleccionar archivo
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            C_Data dataC= new C_Data();
            C_Main newMainC= new C_Main(dataC);

            //Cambia el modo de acomodo de pantallas a manual
            dataC.setManualScreensLayout(true);
        
            File selectedFile = fileChooser.getSelectedFile();

            // Crear el parser de XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xmlDoc = builder.parse(selectedFile);

            // Normalizar el documento
            xmlDoc.getDocumentElement().normalize();
            
            // Obtener el elemento raiz "displays"
            Element rootElement = xmlDoc.getDocumentElement();
            
            // Procesar los slaves
            NodeList slaveNodes = rootElement.getElementsByTagName("slave");
            for (int i = 0; i < slaveNodes.getLength(); i++) {
                
                //Slave
                
                Element slaveElement = (Element) slaveNodes.item(i);
                C_Slave slaveC = new C_Slave();
                
                slaveC.setName(slaveElement.getAttribute("name"));
                slaveC.setBackup(slaveElement.getAttribute("backup"));
                slaveC.setAddress(slaveElement.getAttribute("address"));
                slaveC.setOffset(Integer.parseInt(slaveElement.getAttribute("x")));
                
                // Agregar el slave a dataC
                dataC.add(slaveC);

                
                
                // Procesar los HdmiOutputs
                
                int iWidth, iHeight, iHdmiAmount;
                iHdmiAmount=1;
                //Pregunta por la cantidad de HDMI Por Slave pues es una cantidad que no se encuentra en el documento al importar
                //iHdmiAmount= Integer.parseInt( JOptionPane.showInputDialog("Enter the amount of HDMI outputs that seeks to simulate in the Slave:" + slaveC.getName() ));
                iWidth= Integer.parseInt(slaveElement.getAttribute("width")) /*/ iHdmiAmount*/;
                iHeight= Integer.parseInt(slaveElement.getAttribute("height"));
                
                for (int j = 0; j < iHdmiAmount; j++) {
                    C_HdmiOutput hdmiC = new C_HdmiOutput();

                    hdmiC.setSlave(slaveC);
                    hdmiC.setTrueSize(iWidth, iHeight);

                    // Agregar el HdmiOutput a dataC
                    dataC.add(hdmiC);
                }
            }
            
            
            
            
            
            // Procesar las pantallas (displays)
            NodeList displayNodes = rootElement.getElementsByTagName("display");
            for (int i = 0; i < displayNodes.getLength(); i++) {
                Element displayElement = (Element) displayNodes.item(i);
                //Crear nueva pantalla
                C_Screen screenC = new C_Screen(newMainC, dataC);
                
                //Nombre
                screenC.setName(displayElement.getAttribute("name"));

                //Obtener resolución
                String sResolution = displayElement.getAttribute("resolution");
                String[] dimensions = sResolution.split(",");
                screenC.setTrueSize(Integer.parseInt(dimensions[0].trim()), Integer.parseInt(dimensions[1].trim()));
                
                //Obtener Locación
                String sLocacion = ((Element)(displayElement.getElementsByTagName("mapping").item(0))).getAttribute("dst");
                String[] coords = sLocacion.split(",");
                screenC.setTrueLocation(Integer.parseInt(coords[0].trim()), Integer.parseInt(coords[1].trim()));

                //Busca el hdmi al que pertenece la pantalla
                
                screenC.setOwner( dataC.getOwnerOf( dataC.getSlave(displayElement.getAttribute("slave")) , screenC) );
                
                screenC.setTrueLocation(Integer.parseInt(coords[0].trim())- dataC.getDisplacementOfHdmiOutput(screenC.getOwner()), Integer.parseInt(coords[1].trim()));

                //Audio Device
                screenC.setAudioDevice(displayElement.getAttribute("audio_device"));
                
                //Default Duration
                screenC.setDefaultDuration(Integer.parseInt(displayElement.getAttribute("default_duration").trim()));
                
                // Agregar la pantalla a dataC
                dataC.add(screenC);

                
                
                
                
                
                
                // Procesar secciones
                NodeList sectionsNode = displayElement.getElementsByTagName("sections");
                NodeList sectionNodes = ((Element)(sectionsNode.item(0))).getElementsByTagName("section");
                for (int j = 0; j < sectionNodes.getLength(); j++) {
                    
                    Element sectionElement = (Element) sectionNodes.item(j);
                    C_Section sectionC = new C_Section(newMainC, dataC);
                                        
                    //Nombre
                    sectionC.setName(sectionElement.getAttribute("name"));

                    // Obtener output (posición y tamaño)
                    Element outputElement = (Element) sectionElement.getElementsByTagName("output").item(0);
                    String[] coordinates = outputElement.getTextContent().split(",");
                    sectionC.setTrueLocation( Integer.parseInt(coordinates[0].trim()), Integer.parseInt(coordinates[1].trim()));
                    sectionC.setTrueSize(Integer.parseInt(coordinates[2].trim()), Integer.parseInt(coordinates[3].trim()));

                    sectionC.setOwner(screenC);
                    
                    //Para que no se salgan de la screen
                    sectionC.setMaxTrueSize(screenC.getTrueSize());
                    sectionC.setMaxTrueLocationArea(new Point(screenC.getTrueSize().width, screenC.getTrueSize().height));
                    
                    //Folder
                    sectionC.setFolder(sectionElement.getAttribute("folder"));
                    
                    dataC.add(sectionC);
                    
                }


            }
            
            dataC.generateLayers();
            newMainC.refreshTable();
            newMainC.refreshComboSlave();
            //fuerza a actualizar la capa
            
            
            for (Iterator<C_HdmiOutput>  iterator= dataC.getHdmis().iterator(); iterator.hasNext();) {
                C_HdmiOutput hdmiC = iterator.next();
                hdmiC.refreshGlobalZoom(1);
            }
            
            dataC.setCurrentLayer(dataC.getLayers().get(0), newMainC);
            mainC.dispose();
            return dataC;
        }else
            return null;
    }

    private static Element createElementWithAttribute(Document docDoc, String strName, String[][] arrstrAttribute) {
        Element elemento = docDoc.createElement(strName);
        for (String[] strAttribute : arrstrAttribute) {
            elemento.setAttribute(strAttribute[0], strAttribute[1]);
        }
        return elemento;

    }
    
    private void writeXmlWithAttributeOrder(Element element, Writer writer, String indent) throws IOException {
        String tagName = element.getTagName();
        writer.write(indent + "<" + tagName);

        // Reordenar atributos
        NamedNodeMap attrMap = element.getAttributes();
        Map<String, String> attrValues = new LinkedHashMap<>();

        for (int i = 0; i < attrMap.getLength(); i++) {
            Attr attr = (Attr) attrMap.item(i);
            attrValues.put(attr.getName(), attr.getValue());
        }

        List<String> order = attributeOrders.getOrDefault(tagName, new ArrayList<>());
        for (String attrName : order) {
            if (attrValues.containsKey(attrName)) {
                writer.write(" " + attrName + "=\"" + escapeXml(attrValues.remove(attrName)) + "\"");
            }
        }
        for (Map.Entry<String, String> entry : attrValues.entrySet()) {
            writer.write(" " + entry.getKey() + "=\"" + escapeXml(entry.getValue()) + "\"");
        }

        NodeList children = element.getChildNodes();
        boolean hasElementChildren = false;
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                hasElementChildren = true;
                break;
            }
        }

        if (children.getLength() == 0) {
            writer.write("/>\n");
        } else {
            writer.write(">\n");

            for (int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    writeXmlWithAttributeOrder((Element) node, writer, indent + "\t");
                } else if (node.getNodeType() == Node.TEXT_NODE) {
                    String trimmed = node.getTextContent().trim();
                    if (!trimmed.isEmpty()) {
                        writer.write(indent + "\t" + escapeXml(trimmed) + "\n");
                    }
                }
            }

            writer.write(indent + "</" + tagName + ">\n");

            // Añadir línea en blanco después de ciertos elementos
            if (Arrays.asList("slave", "display", "section").contains(tagName)) {
                writer.write("\n");
            }
        }
    }



    private String escapeXml(String input) {
        return input.replace("&", "&amp;").replace("<", "&lt;")
                    .replace(">", "&gt;").replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }

    
}