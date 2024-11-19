package Components;

import processing.core.PApplet;

import static processing.core.PConstants.BACKSPACE;

public class TextField {
    // Properties of the text field
    float x, y, h, w;

    // Colors
    int bgColor, fgColor, selectedColor, borderColor;
    int borderWeight = 1;

    // Text of the field
    public String text = "";
    int textSize = 24;

    boolean selected = false;

    public TextField(PApplet p5, float x, float y, float w, float h) {
        this.x = x; this.y = y; this.w = w; this.h = h;
        this.bgColor = p5.color(140, 140, 140);
        this.fgColor = p5.color(0, 0, 0);
        this.selectedColor = p5.color(190, 190, 60);
        this.borderColor = p5.color(30, 30, 30);
        this.borderWeight = 1;
    }

    public void display(PApplet p5) {
        p5.pushStyle();
        if (selected) {
            p5.fill(selectedColor);
        } else {
            p5.fill(bgColor);

        }

        p5.strokeWeight(borderWeight);
        p5.stroke(borderColor);
        p5.rect(x, y, w, h, 5);

        p5.fill(fgColor);
        p5.textSize(textSize); p5.textAlign(p5.LEFT, p5.CENTER);
        p5.text(text, x + 5, y + h - textSize);
        p5.popStyle();
    }

    // Add/delete text keyed
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

    // Adds the character c at the end of the text
    public void addText(char c) {
        if (this.text.length() + 1 < w) {
            this.text += c;
        }
    }

    // Deletes the last character of the text
    public void removeText() {
        if (text.length() > 0) {
            text = text.substring(0, text.length() - 1);
        }
    }

    // Deletes the whole text
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
    public void isPressed(PApplet p5) {
        if (mouseOverTextField(p5)) {
            selected = true;
        } else {
            selected = false;
        }
    }
}
