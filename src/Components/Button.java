package Components;
import Constants.FinalColors;
import Constants.Sizes;
import Constants.StaticFonts;
import processing.core.PApplet;
import processing.core.PFont;

import static Constants.Layout.*;
public class Button implements Cloneable{
    PApplet p5;
    /** Posición horizontal del botón en la pantalla (coordenada X). */
    public float x;

    /** Posición vertical del botón en la pantalla (coordenada Y). */
    public float y;

    /** Altura total del botón. */
    public float height;

    /** Ancho total del botón. */
    public float width;

    /** Color de relleno del botón en estado normal. */
    public int fillColor;

    /** Color del borde cuando el botón está activo o seleccionado. */
    public int strokeColorOn;

    /** Color del borde cuando el botón no está activo. */
    public int strokeColorOff;

    /** Grosor del borde del botón (en píxeles). */
    public int strokeWeight;

    /** Color de relleno cuando el cursor del ratón pasa sobre el botón. */
    public int fillColorOver;

    /** Color de relleno cuando el botón está deshabilitado. */
    public int fillColorDisabled;

    /** Indica si el botón está habilitado para interacción ({@code true}) o no ({@code false}). */
    public boolean enabled;

    /** Texto que se muestra dentro del botón. */
    public String buttonText;

    /** Fuente utilizada para renderizar el texto del botón. */
    public PFont textFont;

    /** Alineación del texto dentro del botón (e.g., {@code CENTER}, {@code LEFT}). */
    public int textAlign = 3;

    /** Tamaño del texto del botón (en puntos). */
    public float textSize;

    /**
     * Constructor que crea un botón con dimensiones, posición y estilo por defecto.
     *
     * <p>Inicializa los colores de relleno, bordes, fuente de texto y estado habilitado.
     * Los valores de estilo se asignan a partir de clases auxiliares como {@code FinalColors}, {@code Sizes}
     * y {@code StaticFonts}, proporcionando una apariencia coherente con la interfaz de Phi6 Lab.
     *
     * @param p5     instancia de Processing necesaria para dibujar el botón
     * @param x      coordenada X del botón (posición horizontal)
     * @param y      coordenada Y del botón (posición vertical)
     * @param width  ancho del botón (en píxeles)
     * @param height altura del botón (en píxeles)
     */
    public Button(PApplet p5, float x, float y, float width, float height){
        this.p5 = p5;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.enabled = true;

        //Color
        this.fillColor = FinalColors.bgGrey();
        this.fillColorOver = FinalColors.bgLightGrey();
        this.fillColorDisabled = p5.color(150);
        this.strokeColorOn = FinalColors.accentSkyBlue();
        this.strokeColorOff = fillColor;
        this.strokeWeight = borderWeight;

        //Text
        this.buttonText = "New Button";
        this.textFont = StaticFonts.getFont(0);
        this.textSize = Sizes.buttonText;
    }

    //Setters

    /**
     * Establece los colores visuales del botón en sus distintos estados.
     *
     * @param fillColor         color de fondo por defecto
     * @param strokeColor       color del borde activo
     * @param fillColorOver     color de fondo cuando el ratón pasa por encima
     * @param fillColorDisabled color de fondo cuando el botón está deshabilitado
     */
    public void setColors(int fillColor, int strokeColor,
                          int fillColorOver, int fillColorDisabled){
        this.fillColor = fillColor;
        this.strokeColorOn = strokeColor;
        this.fillColorOver = fillColorOver;
        this.fillColorDisabled = fillColorDisabled;

    }

    /**
     * Asigna la fuente de texto que se utilizará en el botón.
     *
     * @param font objeto {@code PFont} con la fuente deseada
     */
    public void setFont(PFont font){
        this.textFont = font;
    }

    /**
     * Activa o desactiva el botón para interacción.
     *
     * @param b {@code true} si debe estar habilitado, {@code false} si debe estar deshabilitado
     */
    public void setEnabled(boolean b){
        this.enabled = b;
    }

    /**
     * Define el texto que se mostrará dentro del botón.
     *
     * @param t cadena de texto a visualizar
     */
    public void setText(String t){ this.buttonText = t; }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    //Getters

    public boolean isEnabled(){
        return  this.enabled;
    }

    public String getButtonText(){
        return buttonText;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public PFont getTextFont() {
        return textFont;
    }

    //Color getters
    public int getFillColor() {
        return fillColor;
    }

    public int getStrokeColorOn() {
        return strokeColorOn;
    }

    public int getFillColorOver() {
        return fillColorOver;
    }

    public int getFillColorDisabled() {
        return fillColorDisabled;
    }

    /**
     * Dibuja el botón en pantalla con su apariencia correspondiente según el estado actual.
     *
     * <p>El método establece los estilos gráficos temporales mediante {@code pushStyle()} y
     * dibuja un rectángulo con esquinas redondeadas, además del texto centrado o alineado a la izquierda.
     *
     * <p>El botón adapta sus colores dependiendo del contexto:
     * <ul>
     *   <li>Si está deshabilitado, se usa {@code fillColorDisabled}.</li>
     *   <li>Si el cursor está sobre el botón, se usa {@code fillColorOver} y {@code strokeColorOn}.</li>
     *   <li>En estado normal, se usa {@code fillColor} y {@code strokeColorOff}.</li>
     * </ul>
     *
     * <p>El texto se muestra con la fuente, alineación y tamaño configurados. El color del texto es blanco por defecto.
     */
    public void display(){
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

        //Button properties

        p5.rect(this.x, this.y, this.width, this.height, corner);

        // Text properties
        p5.fill(FinalColors.textWhite());
        p5.textFont(textFont);
        p5.textSize(textSize);

        p5.textAlign(textAlign);
        if (textAlign==p5.CENTER){
            p5.text(buttonText, this.x + this.width/2, this.y + this.height/2 + 8);
        }
        else if (textAlign==p5.LEFT){
            p5.text(buttonText, this.x + margin, this.y + this.height/2 + 8);
        }

        p5.popStyle();
    }

    /**
     * Verifica si el cursor del ratón se encuentra actualmente dentro del área del botón.
     *
     * <p>Esta comprobación se basa en la posición del ratón ({@code mouseX} y {@code mouseY})
     * y las dimensiones del botón ({@code x, y, width, height}).
     *
     * @param p5 instancia activa de Processing necesaria para acceder a las coordenadas del ratón
     * @return {@code true} si el cursor está dentro del área del botón, {@code false} en caso contrario
     */
    public boolean mouseOverButton(PApplet p5){
        return (p5.mouseX >= this.x) && (p5.mouseX <= this.x + this.width) &&
                (p5.mouseY >= this.y) && (p5.mouseY <= this.y + this.height);
    }

    /**
     * Crea una copia exacta del botón actual, replicando todas sus propiedades visuales y de comportamiento.
     *
     * <p>El botón clonado mantiene:
     * <ul>
     *   <li>Posición y tamaño</li>
     *   <li>Colores de fondo, borde, y estados (over, deshabilitado)</li>
     *   <li>Texto, fuente, tamaño y alineación</li>
     * </ul>
     *
     * <p>No se copia el estado {@code enabled} explícitamente (queda en {@code true} por defecto),
     * ni se asigna una nueva acción o comportamiento.
     *
     * @return una nueva instancia de {@code Button} con las mismas características visuales
     */
    public Button clone() {
        Button b = new Button(this.p5, this.x, this.y, this.width, this.height);

        b.strokeColorOn = this.strokeColorOn;
        b.strokeColorOff = this.strokeColorOff;
        b.fillColorOver = this.fillColorOver;
        b.fillColorDisabled = this.fillColorDisabled;
        b.strokeWeight = this.strokeWeight;
        b.buttonText = this.buttonText;
        b.textSize = this.textSize;
        b.textFont = this.textFont;
        b.textAlign = this.textAlign;

        return b;
    }
}
