package Components;

import Constants.FinalColors;
import Constants.Layout;
import processing.core.PApplet;

import static processing.core.PConstants.BACKSPACE;

/**
 * Componente gráfico personalizado que representa un campo de texto interactivo.
 *
 * <p>Permite al usuario introducir, editar o eliminar texto mediante el teclado.
 * También detecta si el ratón está sobre él y cambia su apariencia visual cuando está seleccionado.
 *
 * <p>Se puede personalizar en color, tamaño de texto, borde y texto de sugerencia. Incluye funcionalidad
 * de clonación para poder replicar campos con facilidad.
 *
 * <p>Utilizado en múltiples pantallas para entrada de nombres, valores numéricos, credenciales y más.
 */
public class TextField implements Cloneable {
    /** Instancia de Processing para dibujar y detectar interacción. */
    PApplet p5;

    /** Coordenada horizontal del campo de texto. */
    public float x;

    /** Coordenada vertical del campo de texto. */
    public float y;

    /** Altura visual del campo. */
    public float height;

    /** Ancho visual del campo. */
    public float width;

    /** Color de fondo del campo en estado normal. */
    int bgColor;

    /** Color del texto introducido. */
    int fgColor;

    /** Color de fondo cuando el campo está seleccionado. */
    int selectedColor;

    /** Color del borde del campo. */
    int borderColor;

    /** Grosor del borde. */
    int borderWeight = 1;

    /** Indica si el borde debe mostrarse siempre. */
    public boolean borderEnabled = false;

    /** Texto actual introducido por el usuario. */
    public String text = "";

    /** Texto de ayuda o sugerencia (placeholder). */
    private String emptyText = "";

    /** Tamaño del texto en píxeles. */
    int textSize = 24;

    /** Indica si el campo está actualmente seleccionado. */
    public boolean selected = false;

    /**
     * Crea un nuevo campo de texto con dimensiones y posición específicas.
     *
     * @param p5     instancia de Processing
     * @param x      posición horizontal
     * @param y      posición vertical
     * @param width  ancho del campo
     * @param height alto del campo
     */
    public TextField(PApplet p5, float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bgColor = FinalColors.bgGrey();
        this.fgColor = FinalColors.textWhite();
        this.selectedColor = FinalColors.bgLightGrey();
        this.borderColor = FinalColors.accentSkyBlue();
        this.p5 = p5;
    }

    /**
     * Dibuja el campo de texto en pantalla, aplicando estilos según si está seleccionado o no.
     * Muestra el texto introducido o el texto sugerido si el campo está vacío.
     */
    public void display() {
        p5.pushStyle();

        //p5.textFont(); --> Pendiente poner en default

        if (selected) {
            p5.fill(selectedColor);
            p5.strokeWeight(borderWeight);
            p5.stroke(borderColor);
        } else {
            p5.fill(bgColor);
            p5.noStroke();
        }

        if (borderEnabled) {
            p5.strokeWeight(borderWeight);
            p5.stroke(borderColor);
        }

        p5.rect(x, y, width, height, Layout.corner);

        p5.fill(fgColor);
        p5.textSize(textSize);
        p5.textAlign(p5.LEFT, p5.CENTER);

        p5.text(text, x + 10, y + height / 2);

        if (text.isEmpty()) p5.text(emptyText, x + 10, y + height / 2);

        p5.popStyle();
    }

    /**
     * Procesa las teclas presionadas por el usuario mientras el campo está seleccionado.
     * Soporta letras, números, espacio y retroceso.
     *
     * @param key     carácter presionado
     * @param keyCode código de la tecla
     */
    public void keyPressed(char key, int keyCode) {
        if (selected) {
            if (keyCode == (int) BACKSPACE) {
                removeText();
            } else if (keyCode == 32) {
                addText(' '); // SPACE
            } else {

                boolean isKeyCapitalLetter = (key >= 'A' && key <= 'Z');
                boolean isKeySmallLetter = (key >= 'a' && key <= 'z');
                boolean isKeyNumber = (key >= '0' && key <= '9');

                if (isKeyCapitalLetter || isKeySmallLetter || isKeyNumber) {
                    addText(key);
                }
            }
        }
    }

    /** Añade un carácter al final del texto si hay espacio suficiente. */
    public void addText(char c) {
        if (this.text.length() + 1 < width) {
            this.text += c;
        }
    }

    /** Elimina el último carácter del texto. */
    public void removeText() {
        if (text.length() > 0) {
            text = text.substring(0, text.length() - 1);
        }
    }

    /** Borra todo el contenido del campo. */
    public void removeAllText() {
        this.text = "";
    }

    /**
     * Devuelve el texto actual del campo.
     * @return texto escrito por el usuario
     */
    public String getText() {
        return this.text;
    }

    /**
     * Establece un nuevo texto en el campo.
     * @param t texto a mostrar
     */
    public void setText(String t) {
        this.text = t;
    }

    /**
     * Comprueba si el ratón está sobre el campo de texto.
     * @param p5 instancia de Processing
     * @return {@code true} si el cursor está sobre el campo
     */
    public boolean mouseOverTextField(PApplet p5) {
        return (p5.mouseX >= this.x && p5.mouseX <= this.x + this.width && p5.mouseY >= this.y && p5.mouseY <= this.y + this.height);
    }


    /**
     * Activa o desactiva la selección del campo dependiendo de la posición del ratón.
     */
    public void mousePressed() {
        selected = mouseOverTextField(p5);
    }

    //Setters
    public void setColors(int bgColor, int fgColor, int selectedColor, int borderColor) {
        this.bgColor = bgColor;
        this.fgColor = fgColor;
        this.selectedColor = selectedColor;
        this.borderColor = borderColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void setFgColor(int fgColor) {
        this.fgColor = fgColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public void setBorderWeight(int borderWeight) {
        this.borderWeight = borderWeight;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setEmptyText(String s) {
        this.emptyText = s;
    }

    /**
     * Crea una copia del campo de texto actual con todos sus atributos.
     *
     * @return nueva instancia de {@code TextField} clonada
     */
    @Override
    public TextField clone() {
        // Crear una nueva instancia y copiar todas las propiedades
        TextField clonedTextField = new TextField(this.p5, this.x, this.y, this.width, this.height);
        clonedTextField.setColors(this.bgColor, this.fgColor, this.selectedColor, this.borderColor);
        clonedTextField.setBorderWeight(this.borderWeight);
        clonedTextField.borderEnabled = this.borderEnabled;
        clonedTextField.setText(this.text);
        clonedTextField.setEmptyText(this.emptyText);
        clonedTextField.setTextSize(this.textSize);
        clonedTextField.selected = this.selected;
        return clonedTextField;
    }

}
