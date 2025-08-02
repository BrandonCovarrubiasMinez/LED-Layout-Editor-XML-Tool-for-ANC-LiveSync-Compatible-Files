/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package Slave;


public class C_Slave{
    private String sName;
    private String sBackup;
    private String sAddress;
    private int iOffset;
    
    
    private final M_Slave Model;

    public C_Slave() {
        Model= new M_Slave();
        sName= "DEFAULT";
        sBackup= "";
        sAddress="localhost";
        iOffset= 1920;
    }

    public void setName(String sName) {
        this.sName= sName;
    }

    public void setOffset(int iOffset) {
        this.iOffset = iOffset;
    }

    public void setBackup(String sBackup) {
        this.sBackup = sBackup;
    }

    public void setAddress(String sAddress) {
        this.sAddress = sAddress;
    }
    
    
    

    public String getName() {
        return sName;
    }

    public int getOffset() {
        return iOffset;
    }

    public String getBackup() {
        return sBackup;
    }

    public String getAddress() {
        return sAddress;
    }
    
    
    
    
    
}
