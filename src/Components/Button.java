package Components;
import Constants.FinalColors;
import Constants.Sizes;
import Constants.StaticFonts;
import processing.core.PApplet;
import processing.core.PFont;

import static Constants.Layout.*;
public class Button {
    PApplet p5;
    public float x, y;
    public float height, width;
    public int fillColor, strokeColorOn, strokeColorOff, strokeWeight;
    public int fillColorOver, fillColorDisabled;
    public boolean enabled;
    public String buttonText;
    public PFont textFont;
    public int textAlign = 3;
    public float textSize;

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
    public void setColors(int fillColor, int strokeColor,
                          int fillColorOver, int fillColorDisabled){
        this.fillColor = fillColor;
        this.strokeColorOn = strokeColor;
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

    public int getStrokeColorOn() {
        return strokeColorOn;
    }

    public int getFillColorOver() {
        return fillColorOver;
    }

    public int getFillColorDisabled() {
        return fillColorDisabled;
    }

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
