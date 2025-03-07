package Screens;

import Components.ButtonIcon;
import Components.Gallery;
import Components.TextList;
import Constants.Colors;

import Constants.FinalColors;
import processing.core.PApplet;
import static Constants.Layout.*;
import static processing.core.PConstants.*;

import Components.Button;


public class HomeScreen extends Screen {
    //Create Button ---------------------------
    public Button createButton;

    //TextList Searchbar ----------------------
    public TextList searchBar;
    public ButtonIcon searchButton;
    String[][] simuList = {
            {"0", "SimuA", "Descripción", "LastModifiedTime", "TimeCreation", "testImage.png"},
            {"1", "SimuA", "Descripción", "LastModifiedTime", "TimeCreation", "testImage.png"},
            {"2", "SimuA", "Descripción", "LastModifiedTime", "TimeCreation", "testImage.png"},
            {"3", "SimuA", "Descripción", "LastModifiedTime", "TimeCreation", "testImage.png"},
            {"4", "SimuA", "Descripción", "LastModifiedTime", "TimeCreation", "testImage.png"},
            {"5", "SimuA", "Descripción", "LastModifiedTime", "TimeCreation", "testImage.png"},
    };
    public String selectedText;

    //Logo buttons ---------------------------
    public Button info, settings;

    //Banner ---------------------------------
    float bannerWidth = (4*hRect)- vMargin *2;
    float bannerHeight = 2*vRect - 2* hMargin;

    //Cards Gallery --------------------------
    Gallery gallery;

    public HomeScreen(PApplet p5, Colors appColors, Constants.Fonts appFonts){
        super(p5, appColors, appFonts);

        //Create Button ------------------------------------------------------------------
        createButton = new Button(this.p5, hMargin, vMargin,
                hRect - vMargin *2, vRect - hMargin *2);
        createButton.setText("Create Simulator");
        createButton.setFont(this.appFonts.fonts[1]);

        //SearchBar -----------------------------------------------------------------------
        float m = 10;
        float searchBarWidth = hRect - margin*2 - frame*2;
        searchBar = new TextList(this.p5, simuList, vMargin, vRect + hMargin,
                searchBarWidth, ((vRect-2* hMargin)/2)-(hMargin)/2);
        searchBar.getTextField().setEmptyText("Buscar");

        searchButton = new ButtonIcon(this.p5, hRect-vMargin-frame, vRect + hMargin,
                frame-margin, frame-margin);
        searchButton.scale = 12;
        searchButton.setFont(this.appFonts.fonts[1]);
        searchButton.setIcon("data/icons/search.svg");

        //Extra Buttons---------------------------------------------------------------------

        float bHeight = (vRect - vMargin*3) / 2;
        float bWidth = hRect - 2*hMargin - bHeight - m;

        info = new Button(this.p5, hMargin+bHeight+m, (4*vRect) + vMargin,
                bWidth, bHeight);
        settings = new Button(this.p5, hMargin+bHeight+m, 4*vRect + vMargin*2 +bHeight,
                bWidth, bHeight);

        //Cards gallery ---------------------------------------------------------------------
        int numCardsInPage = 6;
        gallery = new Gallery(p5, numCardsInPage, (int)vMargin, hRect+ vMargin, (2* vMargin)+bannerHeight,
                bannerWidth, 4*vRect - vMargin);

        gallery.setCards(p5, simuList);
    }

    @Override
    public void display() {
        p5.push();
        //****************************************************************
        //*********************** SIDEBAR AREA ***************************
        //****************************************************************

        //Create simulator button -----------------------------------------
        createButton.display();

        //TextList searchbar ----------------------------------------------
        this.searchBar.display(p5);
        this.searchButton.display();
        this.updateCursor(p5);

        //Extra buttons ---------------------------------------------------
        info.display();
        settings.display();
        p5.fill(255, 0, 0); p5.stroke(255); //LOGO
        p5.rect(hMargin, info.getY(), info.getHeight(), info.getHeight(), corner);
        p5.rect(hMargin, settings.getY(), settings.getHeight(), info.getHeight(), corner);

        //****************************************************************
        //*********************** GALLERY AREA ***************************
        //****************************************************************

        //Banner Area -----------------------------------------------------
        p5.fill(this.appColors.bgGrey()); p5.noStroke();

        p5.rect(hRect+ vMargin, vMargin, bannerWidth, bannerHeight, corner);

        //Cards Area ------------------------------------------------------
        p5.fill(this.appColors.bgLightGrey());
        //p5.rect(hRect+ vMargin, (2* vMargin)+bannerHeight, bannerWidth, 4*vRect - vMargin, corner);

        gallery.display(p5);

        p5.pop();
    }
    private void updateCursor(PApplet p5){ //NO FUNCIONA
        if(searchButton.mouseOverButton(p5) || searchBar.mouseOverButtons(p5) || searchBar.getTextField().mouseOverTextField(p5)){
            p5.cursor(HAND);
        }
        else {
            p5.cursor(ARROW);
        }
    }
}
