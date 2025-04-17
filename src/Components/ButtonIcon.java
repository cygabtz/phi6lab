package Components;

import processing.core.PApplet;
import processing.core.PShape;

import static Constants.Layout.corner;

/**
 * Botón cuadrado personalizado que permite mostrar un ícono vectorial en formato {@code .svg}.
 *
 * <p>Extiende {@link Button} e incluye un atributo {@link PShape} que representa el ícono.
 * Este tipo de botón es ideal para acciones visuales rápidas o iconográficas (cerrar, crear, ajustes...).
 *
 * <p>Los botones se renderizan con esquinas redondeadas y admiten personalización de escala y color.
 * El ícono se adapta al área interior del botón respetando un margen configurable.
 */
public class ButtonIcon extends Button implements Cloneable{
    /** Forma vectorial SVG que representa el ícono del botón. */
    PShape icon;

    /** Factor de escala que determina el margen interno del ícono. */
    public int scale = 5;

    /**
     * Crea un botón cuadrado con soporte para íconos vectoriales.
     *
     * @param p5     instancia de Processing
     * @param x      coordenada horizontal
     * @param y      coordenada vertical
     * @param width  ancho del botón
     * @param height alto del botón
     */
    public ButtonIcon(PApplet p5, float x, float y, float width, float height){
        super(p5, x, y, width, height);
    }

    /**
     * Dibuja el botón con su contorno, fondo e ícono en formato {@code .svg} si está disponible.
     *
     * <p>El color del botón se adapta al estado de interacción (activo, deshabilitado o resaltado).
     * El ícono se escala y centra dentro del botón respetando los márgenes definidos por {@code scale}.
     */
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
     * Asigna un ícono SVG al botón cargándolo desde la ruta proporcionada.
     *
     * @param iconPath ruta relativa al archivo {@code .svg}
     */
    public void setIcon(String iconPath){
        this.icon = p5.loadShape(iconPath); //image.svg
    }

    /**
     * Define el margen de escala del ícono dentro del botón.
     *
     * @param scale valor positivo que representa el margen interno
     */
    public void setScale(int scale){
        this.scale = scale;
    }

    /**
     * Crea una copia exacta del botón con el mismo ícono y escala.
     *
     * @return nueva instancia clonada de {@code ButtonIcon}
     */
    @Override
    public ButtonIcon clone() {
        ButtonIcon clone = new ButtonIcon(this.p5, this.x, this.y, this.width, this.height);
        clone.scale = this.scale;
        clone.icon = this.icon;
        return clone;
    }
}
