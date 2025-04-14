package Components;

import Constants.FinalColors;
import Constants.StaticFonts;
import processing.core.PApplet;

import java.util.ArrayList;

import static Constants.Layout.*;

/**
 * Un <code>Module</code> es un contenedor de otros <code>Components</code> interactivos.
 * Se puede cerrar con el {@link ButtonIcon} de la cruz.
 * Los módulos están incialmente vacíos y se les incorpora componentes mediante añadirlos a la lista
 * <code>components</code>.
 * <p>
 * La interacción con el ratón debe manejarse individualmente para cada componente añadido.
 */
public class Module implements Cloneable{
    PApplet p5;
    public String title;
    public float x, y, width, height;
    public int bgColor, fontColor, selectedColor, borderColor;

    public boolean opened;

    //Base components
    public ButtonIcon closeButton;

    //Module management
    public enum STATE {ATTACHED, DEATACHED, HIDDEN}
    public STATE state;

    //Extra components
    public ArrayList<Object> components;

    /**
     * Constructor de un módulo vacío.
     * @param p5
     * @param x
     * @param y
     * @param width
     * @param height
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
