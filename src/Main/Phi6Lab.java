package Main;


import Constants.Fonts;
import Screens.HomeScreen;
import processing.core.PApplet;
import Constants.Colors;
import static Constants.Layout.*;

public class Phi6Lab extends PApplet {
    public static void main(String[] args) {
        PApplet.main("Main.Phi6Lab", args);
    }

    @Override
    public void settings() {
        size(parseInt(screenH), parseInt(screenV));
        smooth(10);
        fullScreen();
    }

    Colors appColors;
    GUI gui;
    Fonts fonts;

    public void setup(){
        frameRate(80);
        appColors = new Colors(this);
        fonts = new Fonts(this);
        gui = new GUI(this, appColors, fonts);
    }

    public void draw(){
        background(appColors.bgBlack());
        gui.displayActualScreen(this);
    }


    //KEYBOARD INTERACTION ----------------------------------------------
    public void keyPressed(){
        int keyNum = Character.getNumericValue(key);
        if(keyNum>=0 && keyNum<=5){
            println("Key reference: " + Character.getNumericValue(key));
            gui.setActualScreen(GUI.SCREEN.values()[keyNum]);
        }
    }

    public void mousePressed(){
        println("X: "+mouseX+", Y:"+mouseY);

        HomeScreen hs = (HomeScreen) gui.screens[0];
        if(hs.b1.mouseOverButton(this)){
            hs.b1.setEnabled(true);
            hs.b1.setButtonText("ENABLED");
        }
    }

    public void mouseDragged(){
        println("MOUSE DRAGGED");
    }

    public void mouseReleased() {
        println("MOUSE RELEASED");
    }
}