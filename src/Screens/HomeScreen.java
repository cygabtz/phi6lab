package Screens;

import Components.TextField;
import Components.TextList;
import Constants.Colors;
import Constants.Fonts;

import processing.core.PApplet;
import static Constants.Layout.*;
import static processing.core.PConstants.*;

import Components.Button;
import processing.core.PFont;


public class HomeScreen extends Screen {
    public Button b1;
    public TextField searchBar;

    //TextList
    public TextList tList;
    public Button b;
    String[][] listValues = {{"0", "Alemania"},{"1", "Angola"},{"2", "Canada"}, {"3", "Brasil"}};
    public String selectedText;
    float tListW = 600, tListH = 60; float buttonW = 120, buttonH = 60;

    public HomeScreen(PApplet p5, Colors appColors, Constants.Fonts appFonts){
        super(p5, appColors, appFonts);

        //Buttons --------------------------------
        b1 = new Button(this.p5, marginH, marginV, hRect-marginV*2, vRect-marginH*2);
        b1.setButtonText("Create Simulator");
        b1.setFont(this.appFonts.fonts[1]);

        //SearchBar ------------------------------
        searchBar = new TextField(this.p5, marginH, vRect+marginH, hRect-marginV*2, vRect/2);
        float width = screenH; float height = screenV;

        tList = new TextList(this.p5, listValues, width/8, height/12, tListW, tListH);

        b = new Button(this.p5, 3*width/4, height/12, buttonW, buttonH);
        b.setButtonText("TRIA");
        b.setFont(this.appFonts.fonts[1]);
    }

    @Override
    public void display() {
        p5.push();

        //Sidebar Area ----------------------------------------------------
//        p5.fill(this.appColors.bgBlack());
//        p5.strokeWeight(2); p5.stroke(0);
//        p5.rect(marginH, marginV, sidebarWidth, sidebarHeight, corner);
        //SearchBar
//        searchBar.display(p5);

        this.tList.display(p5);
        this.b.display(p5);
//        this.updateCursor(p5);

        if(selectedText!=null){
            p5.pushStyle();
            p5.textAlign(CENTER); p5.fill(0);
            p5.text(selectedText, screenH/2, screenV/2);
            p5.popStyle();
        }

        //Create simulator button
//        b1.display(p5);

//        //Banner     ----------------------------------------------------
//        p5.fill(this.appColors.bgGrey()); p5.noStroke();
//        p5.rect((2*marginH)+sidebarWidth, marginV,
//                bannerWidth, bannerHeight, corner);
//
//        //Cards Zone ----------------------------------------------------
//        p5.fill(this.appColors.bgGrey());
//        p5.rect((2*marginH)+sidebarWidth, (2*marginV)+bannerHeight,
//                cardsZoneWidth, cardsZoneHeight, corner);
        p5.pop();
    }
    private void updateCursor(PApplet p5){
        if( b.mouseOverButton(p5) || tList.mouseOverButtons(p5)){
            p5.cursor(HAND);
        }
        else {
            p5.cursor(ARROW);
        }
    }
}
