package Components;
import Constants.Fonts;
import processing.core.PApplet;
import processing.core.PFont;

import static Constants.Layout.*;
public class Button {
    private float x, y;
    private float height, width;
    private int fillColor, strokeColor;
    private int fillColorOver, fillColorDisabled;
    private boolean enabled;
    private String buttonText;
    private PFont textFont;

    public Button(PApplet p5, float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.enabled = true;

        //Color
        this.fillColor = p5.color(155, 55, 155);
        this.fillColorOver = p5.color(255, 55, 155);
        this.fillColorDisabled = p5.color(150);
        this.strokeColor = p5.color(255);

        //Text
        buttonText = "New Button";
        PFont defaultFont = new Fonts(p5).fonts[0];
        setFont(defaultFont);
    }

    //Setters
    public void setColors(int fillColor, int strokeColor,
                          int fillColorOver, int fillColorDisabled){
        this.fillColor = fillColor;
        this.fillColorOver = strokeColor;
        this.fillColorDisabled = fillColorOver;
        this.strokeColor = fillColorDisabled;
    }

    public void setFont(PFont font){
        this.textFont = font;
    }

    public void setEnabled(boolean b){
        this.enabled = b;
    }

    public void setText(String t){ this.buttonText = t; }

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

    public int getStrokeColor() {
        return strokeColor;
    }

    public int getFillColorOver() {
        return fillColorOver;
    }

    public int getFillColorDisabled() {
        return fillColorDisabled;
    }

    public void display(PApplet p5){
        p5.pushStyle();
        if(!enabled){
            p5.fill(fillColorDisabled);
        }
        else if(mouseOverButton(p5)){
            p5.fill(fillColorOver);
        }
        else{
            p5.fill(fillColor);          // mouse is out of the button
        }

        //Button properties
        p5.stroke(strokeColor); p5.strokeWeight(2);
        p5.rect(this.x, this.y, this.width, this.height, corner);

        // Text properties
        p5.fill(0); p5.textAlign(p5.CENTER); p5.textSize(20);
        p5.textFont(textFont);
        p5.text(buttonText, this.x + this.width/2, this.y + this.height/2 + 10);
        p5.popStyle();
    }

    public boolean mouseOverButton(PApplet p5){
        return (p5.mouseX >= this.x) && (p5.mouseX <= this.x + this.width) &&
                (p5.mouseY >= this.y) && (p5.mouseY <= this.y + this.height);
    }

    public boolean updateHandCursor(PApplet p5){
        return mouseOverButton(p5) && enabled;
    }

}
