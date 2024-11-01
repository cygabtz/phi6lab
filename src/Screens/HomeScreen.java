package Screens;


import processing.core.PApplet;
import static Constants.Layout.*;
import Constants.Colors;
import Components.Button;


public class HomeScreen extends Screen {
    Colors appColors;
    public Button b1;

    public HomeScreen(PApplet p5, Colors appColors){
        this.appColors = appColors;

        b1 = new Button(p5, marginH*2, marginV*2,
                sidebarWidth-marginH*2, 200);
    }

    @Override
    public void display(PApplet p5) {
        p5.push();

        //Sidebar    ----------------------------------------------------
        p5.fill(appColors.bgBlack());
        p5.strokeWeight(2); p5.stroke(0);
        p5.rect(marginH, marginV, sidebarWidth, sidebarHeight, corner);
        b1.display(p5);

        //Banner     ----------------------------------------------------
        p5.fill(appColors.bgGrey()); p5.noStroke();
        p5.rect((2*marginH)+sidebarWidth, marginV,
                bannerWidth, bannerHeight, corner);

        //Cards Zone ----------------------------------------------------
        p5.fill(appColors.bgGrey());
        p5.rect((2*marginH)+sidebarWidth, (2*marginV)+bannerHeight,
                cardsZoneWidth, cardsZoneHeight, corner);
        p5.pop();
    }
}
