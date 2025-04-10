package Components;

import processing.core.PApplet;
import processing.core.PShape;

import static Constants.Layout.corner;

/**
 * Un <code>ButtonIcon</code> es un {@link Button} cuadrado
 * con la posibilidad de poseer un icono en <code>.svg</code>.
 */
public class ButtonIcon extends Button{
    PShape icon;
    public int scale = 5;
    public ButtonIcon(PApplet p5, float x, float y, float width, float height){
        super(p5, x, y, width, height);
    }

    @Override
    public void display() {
        p5.pushStyle();
        p5.strokeWeight(strokeWeight);
        if(!enabled){
            p5.stroke(strokeColorOff);
            p5.fill(fillColorDisabled);
        }
        else if(mouseOverButton(p5)){
            p5.stroke(this.strokeColorOn);
            p5.fill(fillColorOver);
        }
        else{
            p5.stroke(strokeColorOff);
            p5.fill(fillColor);          // mouse is out of the button
        }

        p5.strokeWeight(strokeWeight);
        if(!enabled){
            p5.stroke(strokeColorOff);
            p5.fill(fillColorDisabled);
        }
        else if(mouseOverButton(p5)){
            p5.stroke(this.strokeColorOn);
            p5.fill(fillColorOver);
        }
        else{
            p5.stroke(strokeColorOff);
            p5.fill(fillColor);          // mouse is out of the button
        }

        p5.rect(this.x, this.y, this.width, this.height, corner);

        if (icon!=null){
            p5.stroke(255);
            icon.disableStyle();
            p5.fill(255);
            p5.shape(icon, x+scale, y+scale, width-2*scale, height-2*scale);
        }

        p5.popStyle();
    }

    /**
     * Almacena la ruta del icono en <code>.svg</code> a dibujar.
     * @param iconPath ruta al archivo vectorial
     */
    public void setIcon(String iconPath){
        this.icon = p5.loadShape(iconPath); //image.svg
    }

    /**
     * Escala el icono del botón respecto a sus márgenes.
     * @param scale factor de escala positivo
     */
    public void setScale(int scale){
        this.scale = scale;
    }

}
