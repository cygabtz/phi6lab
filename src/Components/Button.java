package Components;
import processing.core.PApplet;
import static Constants.Layout.*;
public class Button {
    private float x, y;
    private float height, width;
    private int fillColor, strokeColor;
    private int fillColorOver, fillColorDisabled;
    private boolean enabled;
    private String buttonTittle;

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
        this.strokeColor = p5.color(0);

        //Text
        buttonTittle = "New Button";
    }
    //Setters
    public void setColors(int fillColor, int strokeColor,
                          int fillColorOver, int fillColorDisabled){
        this.fillColor = fillColor;
        this.fillColorOver = strokeColor;
        this.fillColorDisabled = fillColorOver;
        this.strokeColor = fillColorDisabled;
    }

    public void setEnabled(boolean b){
        this.enabled = b;
    }

    public void setTextBoto(String t){ this.buttonTittle = t; }

    //Getters
    public boolean isEnabled(){
        return  this.enabled;
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
        p5.text(buttonTittle, this.x + this.width/2, this.y + this.height/2 + 10);
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
