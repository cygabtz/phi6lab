package Main;

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

    public void setup(){
        frameRate(80);
        appColors = new Colors(this);
        gui = new GUI(this, appColors);

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

    }

    public void mouseDragged(){
        println("MOUSE DRAGGED");
    }

    public void mouseReleased() {
        println("MOUSE RELEASED");
    }
}