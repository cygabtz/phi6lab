package Components;

import Constants.FinalColors;
import Constants.StaticFonts;
import processing.core.PApplet;

import java.util.ArrayList;

import static Constants.Layout.*;

/**
 * Clase que representa un módulo gráfico flotante o acoplado dentro de la interfaz de usuario.
 *
 * <p>Un módulo actúa como una ventana o panel que puede contener diferentes componentes
 * interactivos (como botones, sliders, campos de texto, etc.), y puede mostrar u ocultar
 * su contenido dinámicamente. Está diseñado para usarse en pantallas como el simulador.
 *
 * <p>Cada módulo tiene un título, una zona de cabecera con un botón de cierre, y un
 * sistema de estados ({@code ATTACHED}, {@code DEATACHED}, {@code HIDDEN}) que permite
 * controlar su comportamiento en pantalla.
 *
 * <p>Admite personalización de colores, tipografía, bordes y admite clonación si se requiere
 * replicar su estructura. También mantiene una lista de componentes internos que son renderizados
 * automáticamente cuando se llama al método {@link #display()}.
 */
public class Module implements Cloneable{
    /** Instancia de Processing utilizada para dibujar el módulo. */
    PApplet p5;

    /** Título visible del módulo, normalmente mostrado en la cabecera. */
    public String title;

    /** Coordenada horizontal (X) del módulo. */
    public float x;

    /** Coordenada vertical (Y) del módulo. */
    public float y;

    /** Ancho del módulo en píxeles. */
    public float width;

    /** Altura del módulo en píxeles. */
    public float height;

    /** Color de fondo del módulo. */
    public int bgColor;

    /** Color del texto mostrado dentro del módulo. */
    public int fontColor;

    /** Color que se usa para resaltar si el módulo está activo o seleccionado. */
    public int selectedColor;

    /** Color del borde del módulo. */
    public int borderColor;

    /** Indica si el módulo está actualmente abierto ({@code true}) o cerrado/oculto ({@code false}). */
    public boolean opened;

    /** Botón para cerrar o minimizar el módulo (usualmente en la esquina superior derecha). */
    public ButtonIcon closeButton;

    /**
     * Enum que representa el estado del módulo:
     * <ul>
     *   <li>{@code ATTACHED} – acoplado a una zona fija de la interfaz</li>
     *   <li>{@code DEATACHED} – flotante, arrastrable</li>
     *   <li>{@code HIDDEN} – no visible</li>
     * </ul>
     */
    public enum STATE {ATTACHED, DEATACHED, HIDDEN}

    /** Estado actual del módulo dentro de la interfaz. */
    public STATE state;

    /**
     * Lista de componentes visuales y funcionales añadidos al módulo.
     * Puede contener botones, sliders u otros elementos gráficos.
     */
    public ArrayList<Object> components;

    /**
     * Constructor que crea un nuevo módulo interactivo con posición, dimensiones
     * y estilo visual predeterminados.
     *
     * <p>El módulo se inicia en estado {@code ATTACHED} y se muestra como abierto.
     * Incluye un botón de cierre con ícono SVG y se configura con los colores por defecto
     * definidos en {@link FinalColors}. El título se inicializa como "Nuevo módulo...".
     *
     * @param p5     instancia de Processing necesaria para el renderizado
     * @param x      posición horizontal del módulo (coordenada X)
     * @param y      posición vertical del módulo (coordenada Y)
     * @param width  ancho del módulo en píxeles
     * @param height alto del módulo en píxeles
     */
    public Module(PApplet p5, float x, float y, float width, float height){
        this.p5 = p5;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.state = STATE.ATTACHED;
        this.opened = true;

        this.title = "Nuevo módulo...";
        this.closeButton = new ButtonIcon(p5, x+width - frame + 2*margin, y+margin,
                frame -3*margin, frame -3*margin);
        closeButton.setIcon("data/icons/close.svg");

        this.bgColor = FinalColors.bgGrey();
        this.borderColor = FinalColors.accentSkyBlue();
        this.selectedColor = FinalColors.bgLightGrey();
        this.fontColor = FinalColors.textWhite();

        this.components = new ArrayList<>();
    }

    /**
     * Dibuja visualmente el módulo en pantalla si está abierto.
     *
     * <p>El módulo se representa como un rectángulo con cabecera y borde. Si el cursor del ratón
     * está sobre el área del módulo, se usa un color de borde destacado y se dibuja una línea
     * divisoria entre la cabecera y el cuerpo. Si no hay interacción, se usa un borde más neutro.
     *
     * <p>Se muestra el título en la parte superior y el botón de cierre. Además, se recorren
     * todos los componentes añadidos al módulo para dibujarlos en su respectiva posición.
     * Los componentes soportados actualmente incluyen:
     * <ul>
     *   <li>{@link Button}</li>
     *   <li>{@link ButtonIcon}</li>
     *   <li>{@link FieldSlider} (mostrando tanto el campo de texto como el slider)</li>
     *   <li>{@link TextField}</li>
     *   <li>{@link Slider}</li>
     * </ul>
     *
     * <p>Este método debe llamarse en el bucle principal de dibujo de la aplicación para mantener
     * actualizada la visualización del módulo.
     */
    public void display() {
        if (opened) {
            p5.push();
            if (isMouseOver()){
                p5.stroke(borderColor);
                p5.fill(bgColor);
                p5.rect(x, y, width, height, corner);

                p5.strokeWeight(borderWeight);
                p5.line(x, y+ frame -margin, x+width, y+ frame -margin);
            }
            else {
                p5.noStroke();
                p5.fill(bgColor);
                p5.rect(x, y, width, height, corner);

                p5.stroke(FinalColors.bgBlack()) ;p5.strokeWeight(borderWeight);
                p5.line(x, y+ frame -margin, x+width, y+ frame -margin);
            }


            //Upper area
            p5.fill(fontColor);
            p5.textAlign(p5.LEFT, p5.CENTER);
            p5.text(title, x + 10, y + (frame -margin)/2);
            closeButton.display();

            p5.pop();

            for (Object component : components){
                if (component instanceof Button b) {
                    b.display();
                }
                else if (component instanceof ButtonIcon b){
                    System.out.println("Displaying");
                    b.display();
                }
                else if (component instanceof FieldSlider) {
                    ((FieldSlider) component).textField.display();
                    ((FieldSlider) component).slider.display();
                }
                else if (component instanceof TextField t){
                    t.display();
                }
                else if (component instanceof Slider s){
                    s.display();
                    s.update();
                }
            }
        }
    }

    /**
     * Verifica si el cursor del ratón se encuentra actualmente sobre el área visible del módulo.
     *
     * <p>Esta comprobación se utiliza para aplicar estilos visuales especiales
     * (como resaltar el borde) cuando el módulo está siendo apuntado por el cursor.
     *
     * @return {@code true} si el ratón está dentro de los límites del módulo, {@code false} en caso contrario
     */
    boolean isMouseOver() {
        return p5.mouseX > x && p5.mouseX < x + width && p5.mouseY > y && p5.mouseY < y + height;
    }

    //Setters
    public void setP5(PApplet p5) {
        this.p5 = p5;
    }

    public void setX(float x) {
        this.x = x;
        this.closeButton.setX(x+width- frame);
    }

    public void setY(float y) {
        this.y = y;
        this.closeButton.setY(y+margin);
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public void setColors(int bgColor, int fontColor, int selectedColor, int borderColor){
        this.bgColor = bgColor;
        this.fontColor = fontColor;
        this.selectedColor = selectedColor;
        this.borderColor = borderColor;
    }

    //Getters

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void mousePressed(){
        if (this.closeButton.mouseOverButton(p5)) this.opened = false;
    }

}
