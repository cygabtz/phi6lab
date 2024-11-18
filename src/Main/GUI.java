package Main;

import Screens.*;

import processing.core.PApplet;
import Constants.Colors;
import Constants.Fonts;
import static Constants.Layout.*;

public class GUI {
    //Processing Attributes
    public PApplet p5;
    public final Colors appColors;
    public Fonts fonts;

    //Screen switching control
    public enum SCREEN {HOME, SELECTION, SIMULATOR, SETTINGS, ABOUT}
    public SCREEN currentSCREEN;
    public Screen currentScreen;
    public Screen[] screens;


    public GUI(PApplet p5, Colors appColors, Fonts fonts){
        this.p5 = p5;
        this.appColors = appColors;
        this.fonts = fonts;

        initializeScreens(p5);
        currentScreen = new Screen(p5, appColors, fonts);
        setCurrentScreen(SCREEN.HOME);
    }

    public void setCurrentScreen(SCREEN screenType){
        currentSCREEN = screenType;
        currentScreen = screens[screenType.ordinal()];
    }

    public void initializeScreens(PApplet p5){
        screens = new Screen[5];

        screens[0] = new HomeScreen(p5, appColors, fonts);
        screens[1] = new SelectionScreen(p5, appColors, fonts);
        screens[2] = new SimulatorScreen(p5, appColors, fonts);
        screens[3] = new SettingsScreen(p5, appColors, fonts);
        screens[4] = new AboutScreen(p5, appColors, fonts);
    }

    public void displayActualScreen(PApplet p5){
        displayGrid(p5);

        //Normal execution
        currentScreen.display(p5);
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
