package Components;

import Constants.FinalColors;
import Constants.Layout;
import processing.core.PApplet;

import static processing.core.PConstants.BACKSPACE;

public class TextField {
    PApplet p5;
    // Properties of the text field
    float x, y, h, w;

    // Colors
    int bgColor, fgColor, selectedColor, borderColor;
    int borderWeight = 1;

    public boolean borderEnabled = false;

    // Text of the field
    public String text = "";
    private String emptyText = "";
    int textSize = 24;

    boolean selected = false;

    public TextField(PApplet p5, float x, float y, float w, float h) {
        this.x = x; this.y = y; this.w = w; this.h = h;
        this.bgColor = FinalColors.bgGrey();
        this.fgColor = FinalColors.textWhite();
        this.selectedColor = FinalColors.bgLightGrey();
        this.borderColor = FinalColors.accentSkyBlue();
        this.p5 = p5;
    }

    public void display() {
        p5.pushStyle();


        if (selected){
            p5.fill(selectedColor);
            p5.strokeWeight(borderWeight);
            p5.stroke(borderColor);
        }
        else {
            p5.fill(bgColor); p5.noStroke();
        }

        if (borderEnabled){
            p5.strokeWeight(borderWeight);
            p5.stroke(borderColor);
        }

        p5.rect(x, y, w, h, Layout.corner);

        p5.fill(fgColor);
        p5.textSize(textSize); p5.textAlign(p5.LEFT, p5.CENTER);

        p5.text(text, x + 10, y + h/2);

        if (text.isEmpty()) p5.text(emptyText, x + 10, y + h/2);

        p5.popStyle();
    }

    // Añadir/eliminar texto
    public void keyPressed(char key, int keyCode) {
        if (selected) {
            if (keyCode == (int)BACKSPACE) {
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

    // Añade un char al final
    public void addText(char c) {
        if (this.text.length() + 1 < w) {
            this.text += c;
        }
    }

    // Elimina el último char
    public void removeText() {
        if (text.length() > 0) {
            text = text.substring(0, text.length() - 1);
        }
    }

    // Elimina todo
    public void removeAllText(){
        this.text = "";
    }


    public String getText(){
        return this.text;
    }


    public void setText(String t){
        this.text= t;
    }


    public boolean mouseOverTextField(PApplet p5) {
        return (p5.mouseX >= this.x && p5.mouseX <= this.x + this.w && p5.mouseY >= this.y && p5.mouseY <= this.y + this.h);
    }

    // Selected / unselected depending on if mouse is over the TextField
    public void mousePressed(PApplet p5) {
        if (mouseOverTextField(p5)) {
            selected = true;
        } else {
            selected = false;
        }
    }

    //Setters
    public void setColors(int bgColor, int fgColor, int selectedColor, int borderColor){
        this.bgColor = bgColor; this.fgColor = fgColor;
        this.selectedColor = selectedColor; this.borderColor = borderColor;
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

    public void setEmptyText(String s){
        this.emptyText = s;
    }


}
