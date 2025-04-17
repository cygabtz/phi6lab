package Components;

import Constants.FinalColors;
import processing.core.PApplet;

import static Constants.Layout.corner;
import static Constants.Layout.margin;

/**
 * Clase que representa un componente deslizante (slider) interactivo personalizado.
 *
 * <p>El slider permite seleccionar un valor numérico dentro de un rango definido mediante el arrastre de un controlador.
 * Se renderiza visualmente como una línea horizontal con un "handle" circular que puede moverse con el ratón.
 *
 * <p>Este componente es completamente independiente y puede ser clonado. Se utiliza ampliamente en {@code SimulatorScreen}
 * junto con otros elementos como {@code FieldSlider}.
 */
public class Slider implements Cloneable{
    /** Instancia de Processing utilizada para dibujar el slider. */
    private PApplet p5;

    /** Posición horizontal del slider. */
    public float x;

    /** Posición vertical del slider. */
    public float y;

    /** Ancho del slider. */
    public float width;

    /** Alto del slider. */
    public float height;

    /** Posición X actual del "handle" del slider. */
    public float handleX;

    /** Color del círculo del slider. */
    public int boxColor;

    /** Color cuando el slider está seleccionado. */
    public int selectedColor;

    /** Color de la línea del slider. */
    public int lineColor;

    /** Color del texto o indicadores del slider. */
    public int fontColor;

    /** Grosor de la línea del slider. */
    public int lineWeight;

    /** Valor mínimo del slider. */
    public float minValue;

    /** Valor máximo del slider. */
    public float maxValue;

    /** Valor actual representado por la posición del handle. */
    public float value;

    /** Indica si el usuario está arrastrando el slider actualmente. */
    public boolean dragging = false;

    /** Radio del círculo que actúa como "handle". */
    private float handleRadius = 12;

    /** Margen calculado a partir del radio del handle. */
    private float mg = handleRadius + margin;

    /**
     * Crea un nuevo slider con los valores y estilos visuales especificados.
     *
     * @param p5           instancia de Processing
     * @param x            posición X
     * @param y            posición Y
     * @param width        ancho del slider
     * @param height       alto del slider
     * @param minValue     valor mínimo del rango
     * @param maxValue     valor máximo del rango
     * @param initialValue valor inicial del slider
     */
    public Slider(PApplet p5, float x, float y, float width, float height, float minValue, float maxValue, float initialValue) {
        this.p5 = p5;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = initialValue;
        this.handleX = PApplet.map(initialValue, minValue, maxValue, x + mg, x + width - mg);

        this.boxColor = FinalColors.accentSkyBlue();
        this.lineColor = FinalColors.primaryYellow();


        this.selectedColor = FinalColors.bgLightGrey();
        this.fontColor = FinalColors.textWhite();
    }

    /**
     * Dibuja visualmente el slider, incluyendo su línea base y el controlador circular.
     */
    public void display() {
        //Hit box
        p5.noFill();
        p5.strokeWeight(1);
        p5.stroke(FinalColors.primaryYellow());
        p5.rect(x, y, width, height, corner);

        // Draw the slider line
        p5.stroke(lineColor);
        p5.strokeWeight(5);
        p5.line(x + mg, y + height/2, x + width - mg, y + height/2);

        // Draw the slider handle

        p5.fill(boxColor);
        p5.stroke(FinalColors.bgBlack()); p5.strokeWeight(2);
        p5.ellipse(handleX, y + height/2, handleRadius*2, handleRadius*2);
    }

    /**
     * Actualiza el valor del slider si está siendo arrastrado por el usuario.
     */
    public void update() {
        if (dragging) {
            float newValue = PApplet.map(p5.mouseX, x + mg, x + width - 2* mg, minValue, maxValue);
            value = PApplet.constrain(newValue, minValue, maxValue);
            handleX = PApplet.map(value, minValue, maxValue, x + mg, x + width - mg);
        }
    }


    /**
     * Activa el estado de arrastre si el ratón hace clic sobre el controlador.
     */
    public void mousePressed() {
        float handleRadius = 10;
        if (PApplet.dist(p5.mouseX, p5.mouseY, handleX, y + height/2) < handleRadius) {
            dragging = true;
        }
    }

    /**
     * Comprueba si el ratón está posicionado sobre el controlador del slider.
     *
     * @return {@code true} si el cursor está dentro del área circular del handle
     */
    public boolean isMouseOverSlider(){
        float handleRadius = 10;
        return PApplet.dist(p5.mouseX, p5.mouseY, handleX, y + height/2) < handleRadius;
    }

    /**
     * Finaliza el estado de arrastre del slider.
     */
    public void mouseReleased() {
        dragging = false;
    }

    /**
     * Establece manualmente un nuevo valor para el slider y actualiza la posición del controlador.
     *
     * @param value nuevo valor a asignar
     */
    public void setValueAt(float value){
        this.value = value;
        this.handleX = PApplet.map(value, minValue, maxValue, x + mg, x + width - mg);
    }

    /**
     * Crea una copia idéntica del slider actual, incluyendo valores y propiedades visuales.
     *
     * @return nueva instancia clonada de {@code Slider}
     */
    @Override
    public Slider clone() {
        // Crear una nueva instancia de Slider con las mismas propiedades iniciales
        Slider clonedSlider = new Slider(this.p5, this.x, this.y, this.width, this.height, this.minValue, this.maxValue, this.value);

        // Clonar propiedades adicionales
        clonedSlider.handleX = this.handleX;
        clonedSlider.boxColor = this.boxColor;
        clonedSlider.selectedColor = this.selectedColor;
        clonedSlider.lineColor = this.lineColor;
        clonedSlider.fontColor = this.fontColor;
        clonedSlider.lineWeight = this.lineWeight;
        clonedSlider.dragging = this.dragging;
        clonedSlider.handleRadius = this.handleRadius;

        return clonedSlider;
    }

}
