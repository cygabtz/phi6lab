import processing.core.PApplet;
import Constants.Colors;
import static Constants.Layout.*;

public class Phi6Lab extends PApplet {
    public static void main(String[] args) {
        PApplet.main("Phi6Lab", args);
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
    }

    public void draw(){
        background(appColors.bgBlack());
        gui.displayActualScreen(this);
    }


    //KEYBOARD INTERACTION ----------------------------------------------
    public void keyPressed(){
        if(key=='0'){
            gui.setActualSecreen(GUI.SCREEN.HOME);
        }
        else if(key=='1'){
            gui.setActualSecreen(GUI.SCREEN.SELECTION);
        }
        else if(key=='2'){
            gui.setActualSecreen(GUI.SCREEN.ABOUT);
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