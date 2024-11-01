package Main;

import Components.Button;
import Screens.*;
import Constants.Colors;
import Constants.Fonts;
import processing.core.PApplet;

import static Constants.Layout.*; 

public class GUI {

    public enum SCREEN {HOME, SELECTION, SIMULATOR, SETTINGS, ABOUT};
    private SCREEN actualSecreenType;
    private Screen actualScreen;
    public Screen[] screens;
    public HomeScreen hs;
    public SelectionScreen ss;

    private final Colors appColors;
    private Fonts fonts;


    public GUI(PApplet p5, Colors appColors){
        // Make p5 an attribute??
        this.appColors = appColors;


        ss = new SelectionScreen(p5);
        hs = new HomeScreen(p5, appColors);

        initializeScreens(p5);

        setActualScreen(SCREEN.HOME);
    }

    public void initializeScreens(PApplet p5){
        screens = new Screen[5];

        screens[0] = hs;
        screens[1] = ss;
//        screens[2] = new SimulatorScreen(p5);
//        screens[3] = new SettingsScreen(p5);
//        screens[4] = new AboutScreen(p5);
    }

    public void setActualScreen(SCREEN screenType){
        actualSecreenType = screenType;
        actualScreen = screens[screenType.ordinal()];
    }

    public void displayActualScreen(PApplet p5){
        displayGrid(p5);
        actualScreen.display(p5);
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
