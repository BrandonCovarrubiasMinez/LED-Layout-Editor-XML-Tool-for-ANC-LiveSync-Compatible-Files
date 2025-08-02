/*
 * Proyecto: LED Layout Editor (XML Tool for ANC LiveSync-Compatible Files) 
 * Autor: Brandon Iván Covarrubias Minez
 * Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * https://creativecommons.org/licenses/by-nc/4.0/
 */

package Heirs;

import zMain.C_Main;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JPanel;

public abstract class pnlZoomeable extends JPanel{

    private boolean boolTrueSize;  
    private static float floatZoom;  //Zoom interno. Se debe sincronizar con el zoom general
    private int intBoudsMode;
    private Dimension dimMaxTrueSize;
    private Dimension dimMinTrueSize;
    private Point pntMaxTrueLocationArea;
    private final Dimension dimTrueSize; //Tamaño verdadero de este componente
    private final Point pntTrueLocation;
    private final int INT_BOUNDS_BOTH= 0;
    private final int INT_BOUNDS_ONLY_LOCATION= 1;
    private final int INT_BOUNDS_ONLY_SIZE= 2;
    public static final int INT_KEEP_VALUE= -1;
    public static final int INT_KEEP_ASPECT_RATIO= -2;

    public pnlZoomeable(){
        dimTrueSize= new Dimension();
        pntTrueLocation= new Point();
        boolTrueSize= true;
        floatZoom= 1;
        intBoudsMode= INT_BOUNDS_BOTH;
        dimMaxTrueSize= new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        dimMinTrueSize= new Dimension(15, 15); //Para que de oportunidad de mostrarse a los margenes
        pntMaxTrueLocationArea= new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height){
        super.setBounds(x, y, width, height);
        actTrueBounds();
    }
    
    @Override
    public void setBounds(Rectangle r){
        super.setBounds(r);
        actTrueBounds();
    }
    //Estos metodos llaman a setBounds--
    @Override
    public void setSize(int width, int height){
        intBoudsMode= INT_BOUNDS_ONLY_SIZE;
        super.setSize(width, height);
    }
    
    @Override
    public void setSize(Dimension d){
        intBoudsMode= INT_BOUNDS_ONLY_SIZE;
        super.setSize(d);
    }
    
    @Override
    public void setLocation(int x, int y){
        intBoudsMode= INT_BOUNDS_ONLY_LOCATION;
        super.setLocation(x, y);
    }
    
    @Override
    public void setLocation(Point p){
        intBoudsMode= INT_BOUNDS_ONLY_LOCATION;
        super.setLocation(p);
    }
    
    public void zoom(float floatZoom){
        int intX= (int)Math.round((((float)pntTrueLocation.getX()) * floatZoom));
        int intY= (int)Math.round((((float)pntTrueLocation.getY()) * floatZoom));
        int intWidth= (int)Math.round((((float)dimTrueSize.getWidth()) * floatZoom));
        int intHeight= (int)Math.round((((float)dimTrueSize.getHeight()) * floatZoom));
       
        //Para que no se actualice el tamaño verdadero con el zoom
        boolTrueSize=false;
        setLocation(intX, intY);
        setSize(intWidth, intHeight);
        boolTrueSize=true;
       
        pnlZoomeable.floatZoom =floatZoom;
    }
    
    
    /**
     * Dependiendo de que metodo se llamó para cambiar el tamaño o locacion es la propiedad que se modifica
     * (Para que si se mueve no se cambie el tamaño verdadero al que se esta mostrando con zoom relativo.
     * Menos tamaño, menos exactitud al convertir el tamaño con zoom al verdadero asi que evito hacer esta conversion si solo se esta moviendo la seccion.)
     */
    public void actTrueBounds(){
        if(boolTrueSize)
            switch(intBoudsMode){
                case INT_BOUNDS_BOTH:
                    actTrueSize(this.getWidth(), this.getHeight());
                    actTrueLocation(this.getX(), this.getY());
                    break;
                case INT_BOUNDS_ONLY_LOCATION:
                    actTrueLocation(this.getX(), this.getY());
                    
                    //Vuelve a ambos por se se llama al metodo setBounds directamente
                    intBoudsMode= INT_BOUNDS_BOTH;
                    break;
                case INT_BOUNDS_ONLY_SIZE:
                    actTrueSize(this.getWidth(), this.getHeight());
                    
                    //Vuelve a ambos por se se llama al metodo setBounds directamente
                    intBoudsMode= INT_BOUNDS_BOTH;
                    break;
                default:
                    System.err.println("Valor no valido: "+intBoudsMode);
                    actTrueSize(this.getWidth(), this.getHeight());
                    actTrueLocation(this.getX(), this.getY());
                    break;
            }
    }
    
    //Flotantes para que los numeros se mantengan lo mas que se pueda
    private void actTrueSize(float floatWidth, float floatHeight){
        dimTrueSize.setSize(Math.round(floatWidth / floatZoom), Math.round(floatHeight / floatZoom));
    }
    
    private void actTrueLocation(float floatX, float floatY){
        pntTrueLocation.setLocation(Math.round(floatX / floatZoom), Math.round(floatY / floatZoom));
    }

    public static int getValueInZoom(int intValue){
        return Math.round(intValue / floatZoom);
    }
    
    public static int applyZoom(int intValue) {
        return Math.round(intValue * floatZoom);
    }

    public float getZoom(){
        return floatZoom;
    }

    public void setZoom(float floatZoom){
        pnlZoomeable.floatZoom = floatZoom;
    }
    
    /**
     * Cambia la locación real
     *
     * @param iX
     * @param iY
     * @param iGridSize
     */
    public void setTrueLocation(int iX, int iY, int iGridSize){
        Point pntNewLocationPlusArea;
        pntNewLocationPlusArea= new Point((Math.round((float) iX /iGridSize)*iGridSize)+this.getTrueWidth(), (Math.round((float) iY/iGridSize)*iGridSize)+this.getTrueHeight());

        if(iX != INT_KEEP_VALUE) {
            iX = Math.round((float) iX /iGridSize)*iGridSize;

            //Comprueba que el pnl se encuentre dentro del area maxima. Contempla su tamaño y ubicación
            while (pntNewLocationPlusArea.x > pntMaxTrueLocationArea.x) {
                iX -= iGridSize;
                pntNewLocationPlusArea = new Point(iX + this.getTrueWidth(), iY + this.getTrueHeight());
            }

            if (iX < 0) {
                iX = 0;
            }
        }else
            iX= pntTrueLocation.x;

        if(iY != INT_KEEP_VALUE) {
            iY= Math.round((float) iY/iGridSize)*iGridSize;

            while (pntNewLocationPlusArea.y > pntMaxTrueLocationArea.y) {
                iY -= iGridSize;
                pntNewLocationPlusArea = new Point(iX + this.getTrueWidth(), iY + this.getTrueHeight());
            }

            if (iY < 0)
                iY = 0;
        }else
            iY= pntTrueLocation.y;

        pntTrueLocation.setLocation(iX, iY);
        zoom(floatZoom);
    }

    /**
     * Ajusta el tamaño real del componente para alinearlo con el tamaño de grid especificado.
     * Las dimensiones reales se calculan redondeando el ancho y la altura proporcionados a los múltiplos más cercanos del tamaño de cuadrícula.
     * A continuación, se establece el tamaño real actualizado y se aplican los ajustes de nivel de zoom.
     *
     * @param iWidth: el ancho original que se ajustará.
     * @param iHeight: la altura original que se ajustará.
     * @param iGridSize: el tamaño de cuadrícula al que se deben alinear las dimensiones reales.
     */
    public void setTrueSize(int iWidth, int iHeight, int iGridSize) {

        if (iWidth != INT_KEEP_VALUE && iWidth != INT_KEEP_ASPECT_RATIO) {
            iWidth = (Math.round((float) iWidth / iGridSize) * iGridSize);

            //Comprueba que las dimenciones se encuentren dentro del tamaño minimo y máximo. Si no lo estan, se coloca el valor mas cercano respetando la cuadricula
            while (iWidth + this.getTrueX() > dimMaxTrueSize.width) //Se suma la coordenada en x para que siga dentro del area maxima
                iWidth -= iGridSize;     //Solo ocupa un paso atras pues la dimension de la pantalla solo permite pasar al siguiente tamaño

            if (iWidth < dimMinTrueSize.width)
                iWidth = dimMinTrueSize.width;

        }else if(iWidth != INT_KEEP_ASPECT_RATIO)
            iWidth = dimTrueSize.width;


        if (iHeight == INT_KEEP_ASPECT_RATIO){
            iHeight= (int)Math.round(( (float)iWidth) / C_Main.ASPECT_HORIZONTAL * C_Main.ASPECT_VERTICAL);

            //Este es el caso en el que al adaptar la altura del panel, esta rebase el limite asignado.
            //En ese caso la altura se fija al maximo permitido (Relativo a la locación) y el ancho se calcula a partir de esta.
            if( iHeight + pntTrueLocation.y > dimMaxTrueSize.height ) {
                iHeight = dimMaxTrueSize.height - pntTrueLocation.y;
                iWidth = (int)Math.round(( (float)iHeight) / C_Main.ASPECT_VERTICAL * C_Main.ASPECT_HORIZONTAL);
            }


        }else if(iHeight != INT_KEEP_VALUE){
            iHeight= (Math.round((float) iHeight/iGridSize)*iGridSize);

            while(iHeight + this.getTrueY() > dimMaxTrueSize.height)  //Se suma la coordenada en y para que siga dentro del area maxima
                iHeight-= iGridSize;    //Solo ocupa un paso atras pues la dimension de la pantalla solo permite pasar al siguiente tamaño

            if(iHeight < dimMinTrueSize.height)
                iHeight= dimMinTrueSize.height;

        }else
            iHeight= dimTrueSize.height;

        //Si el mantener ratio de aspecto esta en width, se deja que se calcule la altura y al final el ancho
        if(iWidth == INT_KEEP_ASPECT_RATIO) {
            iWidth = (int) Math.round(((float) iHeight) / C_Main.ASPECT_VERTICAL * C_Main.ASPECT_HORIZONTAL);

            //Este es el caso en el que al adaptar la altura del panel, esta rebase el limite asignado.
            //En ese caso la altura se fija al maximo permitido (Relativo a la locación) y el ancho se calcula a partir de esta.
            if( iWidth + pntTrueLocation.x > dimMaxTrueSize.width ) {
                iWidth = dimMaxTrueSize.width - pntTrueLocation.x;
                iHeight = (int)Math.round(( (float)iWidth) / C_Main.ASPECT_HORIZONTAL * C_Main.ASPECT_VERTICAL);
            }

        }

        dimTrueSize.setSize(iWidth, iHeight);
        zoom(floatZoom);
    }

    public void setTrueLocation(int x, int y){
        pntTrueLocation.setLocation( x == INT_KEEP_VALUE ? pntTrueLocation.x : x , y == INT_KEEP_VALUE ? pntTrueLocation.y : y);
        zoom(floatZoom);
    }

    public void setTrueSize(int width, int height){
        if(width == INT_KEEP_ASPECT_RATIO){
            width= (int)Math.round(((float) height) / C_Main.ASPECT_VERTICAL * C_Main.ASPECT_HORIZONTAL);

            //Este es el caso en el que al adaptar la altura del panel, esta rebase el limite asignado.
            //En ese caso la altura se fija al maximo permitido (Relativo a la locación) y el ancho se calcula a partir de esta.
            if( width + pntTrueLocation.x > dimMaxTrueSize.width ) {
                width = dimMaxTrueSize.width - pntTrueLocation.x;
                height = (int)Math.round(( (float)width) / C_Main.ASPECT_HORIZONTAL * C_Main.ASPECT_VERTICAL);
            }

            dimTrueSize.setSize(width, height);

        }else if(height == INT_KEEP_ASPECT_RATIO){
            height= (int)Math.round(((float) width) / C_Main.ASPECT_HORIZONTAL * C_Main.ASPECT_VERTICAL);

            //Este es el caso en el que al adaptar la altura del panel, esta rebase el limite asignado.
            //En ese caso la altura se fija al maximo permitido (Relativo a la locación) y el ancho se calcula a partir de esta.
            if( height + pntTrueLocation.y > dimMaxTrueSize.height ) {
                height = dimMaxTrueSize.height - pntTrueLocation.y;
                width = (int)Math.round(( (float)height) / C_Main.ASPECT_VERTICAL * C_Main.ASPECT_HORIZONTAL);
            }

            dimTrueSize.setSize(width, height);
        }else
            dimTrueSize.setSize(width == INT_KEEP_VALUE ? dimTrueSize.width : width, height == INT_KEEP_VALUE ? dimTrueSize.height : height);
        zoom(floatZoom);
    }

    public void setMaxTrueSize(Dimension dimMaxSize){
        this.dimMaxTrueSize= dimMaxSize;
    }

    public Dimension getMaxTrueSize(){
        return dimMaxTrueSize;
    }

    public void setTrueMinSize(Dimension dimMinSize){
        this.dimMinTrueSize = dimMinSize;
    }

    public void setMaxTrueLocationArea(Point pntMaxTrueLocationArea){
        this.pntMaxTrueLocationArea= pntMaxTrueLocationArea;
    }

    public void setTrueBounds(int intX, int intY, int intWidth, int intHeight){
        dimTrueSize.setSize(intWidth, intHeight);
        pntTrueLocation.setLocation(intX, intY);
        zoom(floatZoom);
    }

    public int getTrueWidth(){
        return dimTrueSize.width;
    }
    
    public int getTrueHeight(){
        return dimTrueSize.height;
    }
    
    public int getTrueX(){
        return pntTrueLocation.x;
    }
    
    public int getTrueY(){
        return pntTrueLocation.y;
    }
    
    public Point getTrueLocation(){
        return pntTrueLocation;
    }
    
    public Dimension getTrueSize(){
        return dimTrueSize;
    }
    
}
