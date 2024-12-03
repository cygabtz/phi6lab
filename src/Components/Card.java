package Components;

import Constants.Fonts;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

import static Constants.Layout.corner;

public class Card extends Button{
    //Card properties
    String id;
    String title;
    String category;
    String description;
    PImage image;
    PGraphics maskedImg;
    float imageHeight;

    public Card(PApplet p5, float x, float y, float width, float height){
        super(p5, x, y, width, height);
        imageHeight = height/3;
        title = "New Card";
    }

    //Setters
    public void buildImage(PApplet p5, String path){
        p5.noStroke();
        p5.fill(-1);
        p5.background(0);

        p5.rect(this.getX(), this.getY(), this.getWidth(), imageHeight+10,
                corner, corner, 0, 0);

        this.image = p5.loadImage(path);
        image.resize((int) p5.width, p5.height);
        image.mask(p5.get());
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

    public void setImageHeight(float height){
        this.imageHeight = height;
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
        p5.rect(this.getX(), this.getY()+imageHeight, this.getWidth(), this.getHeight()-imageHeight, corner);

        //Image upper zone
        p5.image(image, 0,0);
        p5.fill(0, 0);
        p5.stroke(getStrokeColor());
        p5.rect(this.getX(), this.getY(), this.getWidth(), imageHeight+10,
                corner, corner, 0, 0);

        // Text properties
        p5.fill(0); p5.textAlign(p5.CENTER); p5.textSize(20);
        p5.textFont(new Fonts(p5).fonts[1]); //Change Fonts to static
        //add more text and set positions
        p5.text(title, getX() + getWidth()/2, getY() + imageHeight + getHeight()/10);
        p5.popStyle();
    }

}
