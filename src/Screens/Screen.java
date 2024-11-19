package Screens;
import processing.core.PApplet;
import Main.GUI.SCREEN;
import Constants.Fonts;
import Constants.Colors;


public class Screen {
    private SCREEN screenType;

    //Processing properties
    public PApplet p5;
    public  Constants.Fonts appFonts;
    public Colors appColors;

    public Screen(PApplet p5, Colors colors, Fonts fonts){
        this.p5 = p5; this.appColors = colors; this.appFonts = fonts;
    }

    public SCREEN getType(){
        return this.screenType;
    }

    public void display(){}
}
