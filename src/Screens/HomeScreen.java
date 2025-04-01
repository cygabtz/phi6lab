package Screens;

import Components.*;
import Components.Module;
import Constants.Colors;

import Constants.FinalColors;
import Main.DataBase;
import Main.Phi6Lab;
import processing.core.PApplet;

import java.util.ArrayList;

import static Constants.Layout.*;
import static processing.core.PConstants.*;


public class HomeScreen extends Screen {
    //Create Button ---------------------------
    public ButtonIconText newButtonIcon;

    //TextList Searchbar ----------------------
    public TextList searchBar;
    public ButtonIcon searchButton;
    String[][] simuList;

    public String selectedText;

    //Logo buttons ---------------------------
    public Button info, settings;

    //Banner ---------------------------------
    float bannerWidth = (4*hRect)- vMargin *2;
    float bannerHeight = 2*vRect - 2* hMargin;

    //Cards Gallery --------------------------
    public Gallery gallery;

    //DataBase -------------------------------
    public DataBase appDataBase;

    public HomeScreen(PApplet p5, Colors appColors, Constants.Fonts appFonts, DataBase db){
        super(p5, appColors, appFonts);
        this.appDataBase = db;
        simuList = db.getInfoSimuladores();

        //Create Button ------------------------------------------------------------------
        newButtonIcon = new ButtonIconText(p5, hMargin, vMargin,
                hRect - vMargin *2, vRect - hMargin *2);
        newButtonIcon.setText("Crear");
        newButtonIcon.setIcon("data/icons/add.svg");
        newButtonIcon.setScale(20);

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

        gallery = new Gallery(p5, hRect+ vMargin, (2* vMargin)+bannerHeight,
                bannerWidth, 4*vRect - vMargin, (int)vMargin);
        gallery.setNumCards(simuList.length);
        gallery.setMaxCardsInPage(6);
        gallery.setCards(p5, simuList);
    }

    @Override
    public void display() {
        p5.push();
        //****************************************************************
        //*********************** SIDEBAR AREA ***************************
        //****************************************************************


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


        //Create simulator button -----------------------------------------
        newButtonIcon.display();

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
