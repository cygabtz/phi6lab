package Components;

import Constants.FinalColors;
import Constants.StaticFonts;
import processing.core.PApplet;
import processing.core.PConstants;

import static Constants.Layout.*;

/**
 * Botón personalizado que combina un ícono SVG con un texto lateral.
 *
 * <p>Extiende la clase {@link ButtonIcon} y añade soporte para mostrar una etiqueta textual
 * alineada horizontalmente junto al ícono, útil en interfaces donde se requiere una acción visual clara
 * (como el botón "Crear" en {@code HomeScreen}).
 *
 * <p>Se apoya en {@link StaticFonts} y {@link FinalColors} para mantener coherencia visual.
 */
public class ButtonIconText extends ButtonIcon{

    /**
     * Crea un botón con ícono y texto con las dimensiones especificadas.
     *
     * @param p5     instancia de Processing para renderizado
     * @param x      posición horizontal del botón
     * @param y      posición vertical del botón
     * @param width  ancho del botón
     * @param height alto del botón
     */
    public ButtonIconText(PApplet p5, float x, float y, float width, float height) {
        super(p5, x, y, width, height);
    }

    /**
     * Dibuja el botón con su fondo, ícono SVG y texto alineado a la izquierda.
     *
     * <p>El botón cambia su contorno cuando el ratón pasa por encima. El ícono se coloca a la izquierda
     * y el texto a la derecha, usando las fuentes y colores definidos en la interfaz.
     *
     * <p>Se utiliza un margen y escalado personalizado para adaptar el diseño visual.
     */
    @Override
    public void display() {
        p5.pushStyle();
        p5.fill(this.fillColor);
        if(mouseOverButton(p5)) {
            p5.stroke(this.strokeColorOn);
            p5.strokeWeight(2);
        }
        else p5.noStroke();

        p5.rect(this.x, this.y, this.width, this.height, corner);

        if (icon!=null){
            p5.stroke(255);
            icon.disableStyle();
            p5.fill(255);

            float size = height-2*hMargin + scale;

            p5.shape(icon, x+vMargin - scale/2, y+hMargin - scale/2, size, size);
        }

        int marginBut = 2;

        p5.textFont(StaticFonts.getFont(1));
        p5.fill(FinalColors.textWhite());
        p5.textSize(50);
        p5.textAlign(PConstants.LEFT, PConstants.CENTER);
        p5.text(buttonText, this.x + 2*width/5, this.y + this.height/2);

        p5.popStyle();
    }

}
