package Screens;

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

    public HomeScreen(PApplet p5, Colors appColors, Constants.Fonts appFonts){
        super(p5, appColors, appFonts);

        //Create Button --------------------------------
        createButton = new Button(this.p5, marginH, marginV,
                hRect-marginV*2, vRect-marginH*2);
        createButton.setText("Create Simulator");
        createButton.setFont(this.appFonts.fonts[1]);

        //SearchBar -------------------------------------
        float searchBarWidth = hRect-marginV*5;
        searchBar = new TextList(this.p5, simuList, marginV, vRect+marginH,
                searchBarWidth, ((vRect-2*marginH)/2)-(marginH)/2);

        searchButton = new Button(this.p5, searchBarWidth+marginV+5, vRect+marginH,
                hRect-searchBarWidth-marginV*2, ((vRect-2*marginH)/2)-(marginH)/2);
        searchButton.setText("S"); searchButton.setFont(this.appFonts.fonts[1]);

        //Buttons with logo
        info = new Button(this.p5, marginV, marginH*2+vRect*2,
                searchBarWidth, (vRect-2*marginH)/2);
    }

    @Override
    public void display() {
        p5.push();

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

        //Banner Area -----------------------------------------------------
        p5.fill(this.appColors.bgGrey()); p5.noStroke();
        float bannerWidth = (4*hRect)-marginV*2;
        float bannerHeight = 2*vRect - 2*marginH;
        p5.rect(hRect+marginV, marginV, bannerWidth, bannerHeight, corner);

        //Cards Area ------------------------------------------------------
        p5.fill(this.appColors.bgGrey());
        p5.rect(hRect+marginV, (2*marginV)+bannerHeight,
                bannerWidth, 4*vRect - marginV, corner);

        //Create simulator button -----------------------------------------
        createButton.display(p5);

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
