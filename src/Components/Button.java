package Components;
import Constants.FinalColors;
import Constants.Fonts;
import Constants.Sizes;
import processing.core.PApplet;
import processing.core.PFont;

import static Constants.Layout.*;
public class Button {
    PApplet p5;
    public float x, y;
    public float height, width;
    public int fillColor, strokeColor, strokeWeight;
    public int fillColorOver, fillColorDisabled;
    public boolean enabled;
    public String buttonText;
    public PFont textFont;
    public int textAlign = 3;

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
        this.strokeColor = FinalColors.accentSkyBlue();
        this.strokeWeight = borderWeight;

        //Text
        buttonText = "New Button";
        setFont(new Fonts(p5).fonts[0]);
    }

    //Setters
    public void setColors(int fillColor, int strokeColor,
                          int fillColorOver, int fillColorDisabled){
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.fillColorOver = fillColorOver;
        this.fillColorDisabled = fillColorDisabled;

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

    public void display(){
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
        p5.noStroke();
        p5.rect(this.x, this.y, this.width, this.height, corner);

        // Text properties
        p5.fill(FinalColors.textWhite());
        p5.textFont(textFont);
        p5.textSize(Sizes.buttonText);

        p5.textAlign(textAlign);
        if (textAlign==p5.CENTER){
            p5.text(buttonText, this.x + this.width/2, this.y + this.height/2 + 10);
        }
        else if (textAlign==p5.LEFT){
            p5.text(buttonText, this.x + margin, this.y + this.height/2 + 8);
        }

        p5.popStyle();
    }

    public boolean mouseOverButton(PApplet p5){
        return (p5.mouseX >= this.x) && (p5.mouseX <= this.x + this.width) &&
                (p5.mouseY >= this.y) && (p5.mouseY <= this.y + this.height);
    }

    public boolean updateHandCursor(PApplet p5){
        return mouseOverButton(p5) && enabled;
    }


    //Setters
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
}
