package Screens;

import Components.Card;
import Components.TextList;
import Constants.Colors;

import processing.core.PApplet;
import static Constants.Layout.*;
import static processing.core.PConstants.*;

import Components.Button;


public class HomeScreen extends Screen {
    //Create Button ---------------------------
    public Button createButton;

    //TextList Searchbar ----------------------
    public TextList searchBar;
    public Button searchButton;
    String[][] simuList = {{"0", "Alemania"},{"1", "Angola"},{"2", "Canada"}, {"3", "Brasil"}};
    public String selectedText;

    //Logo buttons ---------------------------
    public Button info, settings;

    //Cards Gallery --------------------------
    Card card;

    public HomeScreen(PApplet p5, Colors appColors, Constants.Fonts appFonts){
        super(p5, appColors, appFonts);

        //Create Button --------------------------------
        createButton = new Button(this.p5, hMargin, vMargin,
                hRect - vMargin *2, vRect - hMargin *2);
        createButton.setText("Create Simulator");
        createButton.setFont(this.appFonts.fonts[1]);

        //SearchBar -------------------------------------
        float m = 10;
        float searchBarWidth = hRect - vMargin *5 - m;
        searchBar = new TextList(this.p5, simuList, vMargin, vRect+ hMargin,
                searchBarWidth, ((vRect-2* hMargin)/2)-(hMargin)/2);

        searchButton = new Button(this.p5, searchBarWidth + vMargin + m, vRect+ hMargin,
                hRect-searchBarWidth- vMargin *2, ((vRect-2* hMargin)/2)-(hMargin)/2);
        searchButton.setText("S"); searchButton.setFont(this.appFonts.fonts[1]);

        //Extra Buttons

        float bHeight = (vRect - vMargin*3) / 2;
        float bWidth = hRect - 2*hMargin - bHeight - m;

        info = new Button(this.p5, hMargin+bHeight+m, (4*vRect) + vMargin,
                bWidth, bHeight);
        settings = new Button(this.p5, hMargin+bHeight+m, 4*vRect + vMargin*2 +bHeight,
                bWidth, bHeight);

        //Cards gallery ------------------------------
        card = new Card(p5, 500, 500, 250, 500);
        card.setTitle("Card Example");

    }

    @Override
    public void display() {
        p5.push();
        //****************** SIDEBAR AREA ********************************

        //Create simulator button -----------------------------------------
        createButton.display(p5);

        //TextList searchbar ----------------------------------------------
        this.searchBar.display(p5);
        this.searchButton.display(p5);
        this.updateCursor(p5);
        if(selectedText!=null){
            p5.pushStyle();
            p5.textAlign(CENTER); p5.fill(0);
            p5.text(selectedText, screenH/2, screenV/2);
            p5.popStyle();
        }

        //Sidebar Area ----------------------------------------------------
//        p5.fill(this.appColors.bgBlack());
//        p5.strokeWeight(2); p5.stroke(0);
//        p5.rect(marginH, marginV, sidebarWidth, sidebarHeight, corner);

        //Extra buttons ---------------------------------------------------
        info.display(p5);
        settings.display(p5);
        p5.fill(255, 0, 0); p5.stroke(255); //LOGO
        p5.rect(hMargin, info.getY(), info.getHeight(), info.getHeight(), corner);
        p5.rect(hMargin, settings.getY(), settings.getHeight(), info.getHeight(), corner);

        //Banner Area -----------------------------------------------------
        p5.fill(this.appColors.bgGrey()); p5.noStroke();
        float bannerWidth = (4*hRect)- vMargin *2;
        float bannerHeight = 2*vRect - 2* hMargin;
        p5.rect(hRect+ vMargin, vMargin, bannerWidth, bannerHeight, corner);

        //Cards Area ------------------------------------------------------
        p5.fill(this.appColors.bgLightGrey());
        p5.rect(hRect+ vMargin, (2* vMargin)+bannerHeight,
                bannerWidth, 4*vRect - vMargin, corner);
        card.display(p5);

        p5.pop();
    }
    private void updateCursor(PApplet p5){
        if(searchButton.mouseOverButton(p5) || searchBar.mouseOverButtons(p5) || searchBar.getTextField().mouseOverTextField(p5)){
            p5.cursor(HAND);
        }
        else {
            p5.cursor(ARROW);
        }
    }
}
