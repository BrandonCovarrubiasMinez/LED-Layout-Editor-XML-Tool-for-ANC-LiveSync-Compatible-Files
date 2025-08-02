/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package XML;

import Data.C_Data;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import zMain.C_Main;

public class C_XMLGenerator{
    private final M_XMLGenerator Model;

    public C_XMLGenerator() {
        Model= new M_XMLGenerator();
    }

    public void generateXML(C_Data dataC){
        try {
            Model.createXMLDocument(dataC);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public C_Data importDisplayDoc(C_Main mainC){
        try {
            return Model.importDisplayDoc(mainC);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            return null;
        }
    }
}
