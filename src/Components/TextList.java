package Components;
import processing.core.PApplet;
import java.util.ArrayList;

/**
 * Componente visual que permite buscar y seleccionar elementos desde una lista de texto mediante autocompletado.
 *
 * <p>Combina un campo de entrada de texto ({@link TextField}) con una lista desplegable de botones
 * que se actualiza dinámicamente según las coincidencias encontradas en un array bidimensional de texto.
 *
 * <p>Utilizado principalmente en pantallas como {@code HomeScreen} para facilitar la selección de simuladores.
 */
public class TextList {
    /** Posición horizontal del componente. */
    float x;

    /** Posición vertical del componente. */
    float y;

    /** Ancho del campo y la lista de botones. */
    float w;

    /** Alto del campo y de cada botón. */
    float h;

    /** Conjunto de entradas disponibles en formato [id, valor]. */
    String[][] texts;

    /** Campo de texto donde el usuario escribe su búsqueda. */
    TextField textField;

    /** Índice actual de selección dentro de la lista. */
    int selectedIndex;

    /** Identificador único del elemento seleccionado. */
    String selectedId;

    /** Valor textual seleccionado por el usuario. */
    String selectedValue;

    /** Indica si el componente está activado. */
    boolean enabled;

    /** Número de coincidencias encontradas en la búsqueda. */
    int numMatchs = 0;

    /** Botones que representan las coincidencias actuales. */
    ArrayList<Button> buttons;

    /**
     * Crea una nueva instancia de {@code TextList} con los datos a buscar y el tamaño visual deseado.
     *
     * @param p5    instancia de Processing
     * @param texts matriz con los datos a mostrar (formato: [id, texto])
     * @param x     coordenada horizontal
     * @param y     coordenada vertical
     * @param w     ancho del campo
     * @param h     alto del campo y botones
     */
    public TextList(PApplet p5, String[][] texts, float x, float y, float w, float h) {
        this.texts = texts;
        this.selectedId = "";
        this.selectedValue = "";
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.enabled = true;

        this.textField = new TextField(p5, x, y, w, h);
        this.buttons = new ArrayList<Button>();
    }

    /**
     * Dibuja el campo de texto y, si está seleccionado, muestra la lista de botones sugeridos.
     *
     * @param p5 instancia de Processing para renderizado
     */
    public void display(PApplet p5) {
        p5.pushStyle();
        textField.display();

        if(textField.selected){
            for(Button b : buttons){
                b.display();
            }
        }

        p5.popStyle();
    }

    /**
     * Devuelve el valor textual actualmente seleccionado.
     *
     * @return valor de la selección actual
     */
    public String getSelectedValue(){
        return this.selectedValue;
    }

    /**
     * Devuelve el campo de texto asociado a este componente.
     *
     * @return objeto {@link TextField}
     */
    public TextField getTextField(){
        return  this.textField;
    }

    /**
     * Actualiza la lista de botones sugeridos según el texto introducido por el usuario.
     * Solo se muestran como máximo 5 coincidencias cuyo texto comience con lo escrito.
     *
     * @param p5        instancia de Processing
     * @param appFonts  conjunto de fuentes para aplicar estilo a los botones
     */
    public void update(PApplet p5, Constants.Fonts appFonts) {

        String searchFor = this.textField.text;
        System.out.println("SEARCH FOR: "+searchFor);

        this.numMatchs = 0;
        this.buttons = new ArrayList<Button>();

        if (searchFor.length() > 0) {
            for (int i=0; i<texts.length; i++) {
                if (texts[i][1].startsWith(searchFor)) {
                    int s = 10; //Space between buttons
                    Button b = new Button(p5, x, y + h + s + (h + s)*numMatchs, w, h);
                    b.setText(texts[i][1]);
                    b.setFont(appFonts.fonts[1]);
                    b.textAlign = p5.LEFT;
                    buttons.add(b);
                    this.numMatchs++;
                    if (this.numMatchs==5) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Indica si el cursor del ratón está sobre alguno de los botones sugeridos.
     *
     * @param p5 instancia de Processing
     * @return {@code true} si el ratón está sobre al menos un botón
     */
    public boolean mouseOverButtons(PApplet p5){
        for(Button b : buttons){
            if(b.mouseOverButton(p5)){
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si se ha presionado uno de los botones sugeridos y actualiza el campo de texto.
     *
     * <p>Si se detecta una selección, actualiza el valor elegido y limpia la lista de botones.
     *
     * @param p5 instancia de Processing
     */
    public void buttonPressed(PApplet p5){
        boolean pressed = false;
        for(Button b : buttons){
            if(b.mouseOverButton(p5)){
                textField.text = b.getButtonText();
                this.selectedValue = b.getButtonText();
                pressed = true;
            }
        }
        if(pressed){
            buttons.clear();
        }
    }
}
