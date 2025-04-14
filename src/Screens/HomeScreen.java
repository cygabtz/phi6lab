package Screens;

import Components.*;
import Components.Module;
import Constants.Colors;

import Constants.FinalColors;
import Main.DataBase;
import Main.GUI;
import Main.Phi6Lab;
import processing.core.PApplet;

import java.util.ArrayList;

import static Constants.Layout.*;
import static processing.core.PConstants.*;

/**
 * Pantalla principal donde se pueden añadir, abrir, eleminar simulaciones y acceder a los ajustes.
 */
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
    float bannerWidth = (4 * hRect) - vMargin * 2;
    float bannerHeight = 2 * vRect - 2 * hMargin;

    //Cards Gallery --------------------------
    public Gallery gallery;

    //DataBase -------------------------------
    private DataBase db;
    public DataBase appDataBase;

    public HomeScreen(PApplet p5, Colors appColors, Constants.Fonts appFonts, DataBase db) {
        super(p5, appColors, appFonts);
        // DataBase
        this.db = db;
        initializeDataBase(db);
        // Create Button
        initializeCreateButton();

        // SearchBar
        initializeSearchBar();

        // Extra Buttons
        initializeExtraButtons();

        // Cards gallery
        initializeGallery();

    }

    private void initializeDataBase(DataBase db) {
        this.appDataBase = db;
        try {
            simuList = db.getInfoSimuladores();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeCreateButton() {
        newButtonIcon = new ButtonIconText(p5, hMargin, vMargin,
                hRect - vMargin * 2, vRect - hMargin * 2);
        newButtonIcon.setText("Crear");
        newButtonIcon.setIcon("data/icons/add.svg");
        newButtonIcon.setScale(20);
    }

    private void initializeSearchBar() {

        float searchBarWidth = hRect - margin * 2 - frame * 2;
        searchBar = new TextList(this.p5, simuList, vMargin, vRect + hMargin,
                searchBarWidth, ((vRect - 2 * hMargin) / 2) - (hMargin) / 2);
        searchBar.getTextField().setEmptyText("Buscar");

        searchButton = new ButtonIcon(this.p5, hRect - vMargin - frame, vRect + hMargin,
                frame - margin, frame - margin);
        searchButton.scale = 12;
        searchButton.setFont(this.appFonts.fonts[1]);
        searchButton.setIcon("data/icons/search.svg");
    }

    private void initializeExtraButtons() {
        float m = 10;
        float bHeight = (vRect - vMargin * 3) / 2;
        float bWidth = hRect - 2 * hMargin - bHeight - m;

        info = new Button(this.p5, hMargin + bHeight + m, (4 * vRect) + vMargin,
                bWidth, bHeight);
        settings = new Button(this.p5, hMargin + bHeight + m, 4 * vRect + vMargin * 2 + bHeight,
                bWidth, bHeight);
    }

    private void initializeGallery() {
        gallery = new Gallery(p5, hRect + vMargin, (2 * vMargin) + bannerHeight,
                bannerWidth, 4 * vRect - vMargin, (int) vMargin);
        gallery.setNumCards(simuList.length);
        gallery.setMaxCardsInPage(6);
        gallery.setCards(p5, simuList);
    }

    @Override
    public void display() {
        // SideBar Area
        displaySearchBar();
        displayExtraButtons();

        // Gallery Arean
        displayBanner();
        displayGallery();
        displayCreateButton();
    }

    private void displaySearchBar() {
        this.searchBar.display(p5);
        this.searchButton.display();
        this.updateCursor(p5);
    }

    private void displayExtraButtons() {
        p5.push();
        info.display();
        settings.display();
        p5.fill(255, 0, 0);
        p5.stroke(255); //LOGO
        p5.rect(hMargin, info.getY(), info.getHeight(), info.getHeight(), corner);
        p5.rect(hMargin, settings.getY(), settings.getHeight(), info.getHeight(), corner);
        p5.pop();
    }

    private void displayBanner() {
        p5.push();
        p5.fill(this.appColors.bgGrey());
        p5.noStroke();

        p5.rect(hRect + vMargin, vMargin, bannerWidth, bannerHeight, corner);
        p5.pop();
    }

    private void displayGallery() {
        p5.push();
        p5.fill(this.appColors.bgLightGrey());
        //p5.rect(hRect+ vMargin, (2* vMargin)+bannerHeight, bannerWidth, 4*vRect - vMargin, corner);
        gallery.display(p5);
        p5.pop();
    }

    private void displayCreateButton() {
        newButtonIcon.display();
    }

    private void updateCursor(PApplet p5) { //NO FUNCIONA
        if (searchButton.mouseOverButton(p5) || searchBar.mouseOverButtons(p5) || searchBar.getTextField().mouseOverTextField(p5)) {
            p5.cursor(HAND);
        } else {
            p5.cursor(ARROW);
        }
    }

    public void mousePressed(GUI gui) {
        //"Create" button
        if(newButtonIcon.mouseOverButton(p5)){
            newButtonIcon.setEnabled(true);
            gui.setCurrentScreen(GUI.SCREEN.SIMULATOR);
        }
        //Gallery buttons
        for (Card cardButton : gallery.getCards()) {
            if(cardButton.mouseOverButton(p5)){
                cardButton.setEnabled(true);
                SimulatorScreen simScreen = (SimulatorScreen) gui.screens[GUI.SCREEN.SIMULATOR.ordinal()];
                GUI.currentSimId = Integer.parseInt(cardButton.getId());
                simScreen.loadSimFromDB(Integer.parseInt(cardButton.getId()));
                gui.setCurrentScreen(GUI.SCREEN.SIMULATOR);
            }
        }
        //TextList
        if(searchButton.mouseOverButton(p5) && searchButton.isEnabled()){
            selectedText = searchBar.getSelectedValue();
        }
        searchBar.getTextField().mousePressed();
        searchBar.buttonPressed(p5);
    }


}
