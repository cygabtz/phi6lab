package Main;

import Screens.*;
import Constants.Colors;
import Constants.Fonts;
import processing.core.PApplet;

import static Constants.Layout.*; 

public class GUI {

    public enum SCREEN {HOME, SELECTION, SIMULATOR, SETTINGS, ABOUT};

    private SCREEN actualSecreen;
    private Iscreen actualIscreen;
    private final Colors appColors;
    private Fonts fonts;


    public GUI(PApplet p5){
        actualSecreen = SCREEN.HOME;
        appColors = new Colors(p5);
    }

    public void buildScreen(SCREEN screen, PApplet p5){
        switch(screen){
            case HOME:      actualIscreen = new HomeScreen(p5);       break;
            case SELECTION: actualIscreen = new SelectionScreen(p5);  break;
            case SIMULATOR: actualIscreen = new SimulatorScreen(p5);  break;
            case ABOUT:     actualIscreen = new AboutScreen(p5);      break;
            case SETTINGS:  actualIscreen = new SettingsScreen(p5);   break;
        }
    }
    public boolean isScreenBuilt(SCREEN screen){
        return false;//pendent
    }

    public void displayActualScreen(PApplet p5){
        displayGrid(p5);
        actualIscreen.display(p5);
    }

    public Iscreen getActualIScreen(){
        return actualIscreen;
    }

    public void displayGrid(PApplet p5){
        p5.pushStyle();
        p5.stroke(255); p5.strokeWeight(1); p5.noFill();
        for (int i = 0; i<=5; i++){
            for (int j = 0; j<=6; j++){
                p5.rect(hRect*i, vRect*j, hRect * (i+1), vRect * (j+1));
            }
        }
        p5.popStyle();
    }
}
