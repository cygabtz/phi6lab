package Components;
import processing.core.PApplet;
import java.util.ArrayList;

public class TextList {
    float x, y, w, h;          // Posición y dimensiones
    String[][] texts;          // Valores que toma

    TextField textField;       // Campo de texto

    int selectedIndex;         // fila seleccionada
    String selectedId;         // identificador seleccionado
    String selectedValue;      // valor seleccionado

    boolean enabled;           // activado / desactivado

    int numMatchs = 0;         // número de coincidencias
    ArrayList<Button> buttons; // lista de botones

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

    public void display(PApplet p5) {
        p5.pushStyle();
        textField.display(p5);

        if(textField.selected){
            for(Button b : buttons){
                b.display(p5);
            }
        }

        p5.popStyle();
    }

    public String getSelectedValue(){
        return this.selectedValue;
    }

    public TextField getTextField(){
        return  this.textField;
    }

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
                    buttons.add(b);
                    this.numMatchs++;
                    if (this.numMatchs==5) {
                        break;
                    }
                }
            }
        }
    }

    public boolean mouseOverButtons(PApplet p5){
        for(Button b : buttons){
            if(b.mouseOverButton(p5)){
                return true;
            }
        }
        return false;
    }

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
