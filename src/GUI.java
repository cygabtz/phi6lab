import Components.sidebarCard;
import Constants.Colors;
import Constants.Fonts;
import processing.core.PApplet;

import static Constants.Layout.*; 

public class GUI {

    public enum SCREEN {HOME, SELECTION, SIMULATOR, SETTINGS, ABOUT};

    private SCREEN actualSecreen;
    private final Colors appColors;
    private Fonts fonts;
    Components.sidebarCard c1;
    //GUI builds Screen classes as needed
    public GUI(PApplet p5){
        actualSecreen = SCREEN.HOME;
        appColors = new Colors(p5);
        c1 = new sidebarCard(100, "A", "d");
    }
    public void setActualSecreen(SCREEN screen){
        actualSecreen = screen;
    }
    public SCREEN getActualSecreen(){
        return actualSecreen;
    }
    public void displayActualScreen(PApplet p5){
        switch(actualSecreen){
            case HOME:      displayHomeScreen(p5);          break;
            case SELECTION: displaySelectionScreen(p5);     break;
            case SIMULATOR: displaySimulatorScreen(p5);     break;
            case SETTINGS:  displaySettingsScreen(p5);      break;
            case ABOUT:     displayAboutScreen(p5);         break;
        }
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

    private void displayHomeScreen(PApplet p5){
        p5.pushStyle();
        //Grid       ----------------------------------------------------
        //drawGrid(p5);

        //Sidebar    ----------------------------------------------------
        p5.noStroke();
        p5.fill(appColors.bgGrey());
        p5.rect(marginH, marginV, sidebarWidth, sidebarHeight, corner);
        c1.displaySideBarCard(p5, appColors);

        //Banner     ----------------------------------------------------
        p5.fill(appColors.bgGrey());
        p5.rect((2*marginH)+sidebarWidth, marginV,
                bannerWidth, bannerHeight, corner);

        //Cards Zone ----------------------------------------------------
        p5.fill(appColors.bgGrey());
        p5.rect((2*marginH)+sidebarWidth, (2*marginV)+bannerHeight,
                cardsZoneWidth, cardsZoneHeight, corner);
    }

    private void displaySelectionScreen(PApplet p5){}
    private void displaySimulatorScreen(PApplet p5){}
    private void displaySettingsScreen(PApplet p5){}
    private void displayAboutScreen(PApplet p5){}
}
