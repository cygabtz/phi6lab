package Components;

import processing.core.PApplet;
import processing.core.PShape;

import static Constants.Layout.corner;

public class ButtonIcon extends Button{
    PShape icon;
    public int scale = 5;
    public ButtonIcon(PApplet p5, float x, float y, float width, float height){
        super(p5, x, y, width, height);
    }
    public void setIcon(String iconPath){
        this.icon = p5.loadShape(iconPath); //image.svg
    }
    @Override
    public void display() {
        p5.pushStyle();
        p5.fill(this.fillColor);
        if(mouseOverButton(p5)) {
            p5.stroke(this.strokeColorOn);
            p5.strokeWeight(2);
        }
        else p5.noStroke();

        p5.rect(this.x, this.y, this.width, this.height, corner);

        if (icon!=null){
            p5.stroke(255);
            icon.disableStyle();
            p5.fill(255);
            p5.shape(icon, x+scale, y+scale, width-2*scale, height-2*scale);
        }

        p5.popStyle();
    }

    public void mousePressed(){
        if (this.mouseOverButton(p5)){
            //Do action
        }
    }

    public void setScale(int scale){
        this.scale = scale;
    }

}
