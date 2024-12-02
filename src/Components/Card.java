package Components;

import processing.core.PApplet;
import processing.core.PImage;

import static Constants.Layout.corner;

public class Card extends Button{
    //Card properties
    String id;
    String title;
    String category;
    String description;
    PImage image;

    public Card(PApplet p5, float x, float y, float width, float height){
        super(p5, x, y, width, height);
    }

    //Setters
    public void setImgage(PApplet p5, String path){
        this.image = p5.loadImage(path);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void display(PApplet p5) {
        p5.pushStyle();
        if(!isEnabled()){
            p5.fill(getFillColorDisabled());
        }
        else if(mouseOverButton(p5)){
            p5.fill(getFillColorOver());
        }
        else{
            p5.fill(getFillColor());          // mouse is out of the button
        }

        //Button properties
        p5.stroke(getStrokeColor()); p5.strokeWeight(2);
        p5.rect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), corner);

        // Text properties
        p5.fill(0); p5.textAlign(p5.CENTER); p5.textSize(20);
        p5.textFont(getTextFont());
        p5.text(title, getX() + getWidth()/2, getY() + getHeight()/2 + 10);
        p5.popStyle();
    }


}
