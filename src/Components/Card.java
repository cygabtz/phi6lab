package Components;

import Constants.FinalColors;
import Constants.Fonts;
import processing.core.PApplet;
import processing.core.PImage;

import static Constants.Layout.corner;

public class Card extends Button{
    //Card properties
    String id;
    String title;
    String category;
    String lastModified, creation; //Tiempos
    PImage image;
    String imgPath;
    float imageHeight;

    Constants.Fonts appFonts;

    public Card(PApplet p5, float x, float y, float width, float height){
        super(p5, x, y, width, height);
        imageHeight = height/1.5f;
        title = "New Card";
        appFonts = new Fonts(p5);
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

    public void setInfo(String [][] simuList){
        for (String[] strings : simuList) {
            this.id = strings[0];
            this.title = strings[1];
            //No se visualiza la descripción
            this.lastModified = strings[3];
            this.creation = strings[4];
            this.imgPath = strings[5];
        }
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

    public void setImageHeight(float height){
        this.imageHeight = height;
    }

    @Override
    public void display() {
        p5.pushStyle();
        if(!isEnabled()){
            p5.fill(fillColorDisabled);
            p5.noStroke();
        }
        else if(mouseOverButton(p5)){
            p5.fill(fillColorOver);
            p5.stroke(strokeWeight); p5.stroke(strokeColor);
        }
        else{
            p5.fill(fillColor);          // El mouse está fuera del botón
            p5.noStroke();
        }

        //Propiedades Button

        p5.rect(this.getX(), this.getY()+imageHeight, this.getWidth(), this.getHeight()-imageHeight, corner);

        //Imagen superior
        //p5.image(image, 0,0);
        p5.fill(0, 0);
        p5.stroke(getStrokeColor());
        p5.rect(this.getX(), this.getY(), this.getWidth(), imageHeight+10,
                corner, corner, 0, 0);

        //Texto inferior
        p5.fill(FinalColors.textWhite()); p5.textAlign(p5.CENTER); p5.textSize(20);
        p5.textFont(appFonts.fonts[1]); //Change Fonts to static
            //Título
            p5.text(title, getX() + getWidth()/2, getY() + imageHeight + getHeight()/10);
            //Descripción
            p5.textFont(appFonts.fonts[0]); p5.textAlign(p5.LEFT);
            float marginH = 15,  marginV = 10, height = this.getY()+imageHeight + 60;
            //Tiempos
            p5.text("Última modificación: "+this.lastModified,
                    this.getX()+marginH, height+appFonts.fonts[0].getSize()+marginV, this.getWidth(), height);
            p5.text("Creado: "+this.creation,
                    this.getX()+marginH, height, this.getWidth(), height);

        p5.popStyle();
    }

}
