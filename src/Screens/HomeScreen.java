package Screens;

import Constants.Colors;
import Constants.Fonts;

import processing.core.PApplet;
import static Constants.Layout.*;
import Components.Button;


public class HomeScreen extends Screen {
    public Button b1;

    public HomeScreen(PApplet p5, Colors appColors, Fonts fonts){
        super(p5, appColors, fonts);

        //Buttons --------------------------------
        b1 = new Button(this.p5, marginH*2, marginV*2, sidebarWidth-marginH*2, 200);
        b1.setButtonText("Create Simulator");
        b1.setFont(this.appFonts.fonts[1]);
    }

    @Override
    public void display(PApplet p5) {
        p5.push();

        //Sidebar    ----------------------------------------------------
        p5.fill(this.appColors.bgBlack());
        p5.strokeWeight(2); p5.stroke(0);
        p5.rect(marginH, marginV, sidebarWidth, sidebarHeight, corner);

        b1.display(p5);

        //Banner     ----------------------------------------------------
        p5.fill(this.appColors.bgGrey()); p5.noStroke();
        p5.rect((2*marginH)+sidebarWidth, marginV,
                bannerWidth, bannerHeight, corner);

        //Cards Zone ----------------------------------------------------
        p5.fill(this.appColors.bgGrey());
        p5.rect((2*marginH)+sidebarWidth, (2*marginV)+bannerHeight,
                cardsZoneWidth, cardsZoneHeight, corner);
        p5.pop();
    }
}
