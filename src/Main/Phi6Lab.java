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
        gui = new GUI(this);
        gui.buildScreen(GUI.SCREEN.HOME, this);
    }

    public void draw(){
        background(appColors.bgBlack());
        gui.displayActualScreen(this);
    }


    //KEYBOARD INTERACTION ----------------------------------------------
    public void keyPressed(){
        if(key=='0'){
            gui.buildScreen(GUI.SCREEN.HOME, this);
        }
        else if(key=='1'){
            gui.buildScreen(GUI.SCREEN.SELECTION, this);
        }
        else if(key=='2'){
            gui.buildScreen(GUI.SCREEN.SIMULATOR, this);
        }
        else if(key=='3'){
            gui.buildScreen(GUI.SCREEN.ABOUT, this);
        }
        else if(key=='4'){
            gui.buildScreen(GUI.SCREEN.SETTINGS, this);
        }
    }

    public void mousePressed(){
        println("X: "+mouseX+", Y:"+mouseY);

        //Pendent
    }

    public void mouseDragged(){
        println("MOUSE DRAGGED");
    }

    public void mouseReleased() {
        println("MOUSE RELEASED");
    }
}