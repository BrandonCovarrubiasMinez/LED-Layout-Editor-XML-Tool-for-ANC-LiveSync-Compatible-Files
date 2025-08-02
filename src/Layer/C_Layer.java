/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package Layer;

import HdmiOutput.C_HdmiOutput;

public class C_Layer {
    private String sName;
    private C_HdmiOutput hdmiOwnerC;
    
    private final M_Layer Model;

    public C_Layer() {
        this.Model = new M_Layer();
    }

    public void setName(String sName) {
        this.sName = sName;
    }

    public void setOwner(C_HdmiOutput hdmiOwnerC) {
        this.hdmiOwnerC = hdmiOwnerC;
    }

    public String getName() {
        return sName;
    }

    public C_HdmiOutput getOwner() {
        return hdmiOwnerC;
    }
    
    
    
}
