package Screens;
import processing.core.PApplet;
import Main.GUI.SCREEN;
import Constants.Fonts;
import Constants.Colors;

public class Screen {
    private SCREEN screenType;

    //Processing properties
    public PApplet p5;
    public  Fonts appFonts;
    public Colors appColors;

    public Screen(PApplet p5, Colors colors, Fonts fonts){
        this.p5 = p5; this.appColors = colors; this.appFonts = fonts;
    }
    public void setScreenType(SCREEN screenType){
        this.screenType = screenType;
    }

    public void setAppColors(Colors appColors){
        this.appColors = appColors;
    }

    public void setAppFonts(Fonts appFonts){
        this.appFonts = appFonts;
    }

    public SCREEN getType(){
        return this.screenType;
    }

    public void display(PApplet p5){}
}
