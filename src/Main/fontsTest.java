package Main;

import Components.Slider;
import Constants.FinalColors;
import Constants.Fonts;
import Constants.Sizes;
import processing.core.PApplet;

import static Constants.Layout.screenH;
import static Constants.Layout.screenV;

public class fontsTest extends PApplet {
    public static void main(String[] args) {
        PApplet.main("Main.fontsTest");
    }

    public void settings() {
        size(800, 800);
    }
    private Fonts appFonts;
    private Slider slider;

    public void setup(){
        appFonts = new Fonts(this);

        slider = new Slider(this, 100,100, 100, 600, 0, 2000, 1200);

    }

    public void draw(){
        background(FinalColors.bgBlack());
        textSize(40);
        textFont(appFonts.fonts[1]);
        text("Esto es una prueba", 20, height/2);

        slider.display();
        slider.update();
    }

    public void mousePressed(){
        slider.mousePressed();
    }

    public void mouseReleased(){
        slider.mouseReleased();
    }
}
