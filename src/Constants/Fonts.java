package Constants;
import processing.core.PApplet;
import processing.core.PFont;

public class Fonts {
    public PFont[] fonts;

    public Fonts(PApplet p5){
        this.setFonts(p5);
    }

    void setFonts(PApplet p5){
        this.fonts = new PFont[4];
        this.fonts[0] = p5.createFont("data/fonts/Rubik-Regular.ttf", Sizes.paragraph);
        this.fonts[1] = p5.createFont("data/fonts/Rubik-SemiBold.ttf", Sizes.widgetHeading);
        this.fonts[2] = p5.createFont("data/fonts/Rubik-Light.ttf", Sizes.details);
    }

}