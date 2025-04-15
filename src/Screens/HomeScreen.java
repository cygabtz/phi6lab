package Screens;

import Components.*;
import Components.Module;
import Constants.Colors;

import Constants.FinalColors;
import Main.DataBase;
import Main.GUI;
import Main.Phi6Lab;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

import static Constants.Layout.*;
import static processing.core.PConstants.*;

/**
 * Pantalla principal de la aplicación donde el usuario puede gestionar sus simulaciones.
 *
 * <p>Permite:
 * <ul>
 *   <li>Crear nuevas simulaciones</li>
 *   <li>Buscar simulaciones existentes</li>
 *   <li>Visualizar tarjetas con las simulaciones del usuario actual</li>
 *   <li>Acceder a la pantalla de configuración</li>
 *   <li>Cerrar sesión</li>
 * </ul>
 *
 * <p>La pantalla incluye una galería de tarjetas cargadas desde la base de datos y asociadas al usuario activo,
 * utilizando {@code GUI.currentUser} como filtro.
 *
 * @see Screens.SimulatorScreen
 * @see Gallery
 * @see DataBase
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
    PImage bannerImage;


    //Cards Gallery --------------------------
    public Gallery gallery;

    //DataBase -------------------------------
    private DataBase db;
    public DataBase appDataBase;

    // Cerrar sesión
    private Button logoutButton;

    /**
     * Constructor de HomeScreen. Inicializa componentes visuales y carga las simulaciones del usuario actual.
     *
     * @param p5 instancia principal de Processing
     * @param appColors esquema de colores de la aplicación
     * @param appFonts tipografías del sistema
     * @param db base de datos conectada a la aplicación
     */
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

        // Banner
        bannerImage = p5.loadImage("data/icons/banner.png");
        bannerImage.resize((int) bannerWidth, 0);

    }

    /**
     * Carga la lista de simulaciones asociadas al usuario activo desde la base de datos.
     *
     * @param db instancia de la base de datos
     */
    private void initializeDataBase(DataBase db) {
        this.appDataBase = db;
        try {
            simuList = db.getInfoSimuladores(Main.GUI.currentUser);
            System.out.println("Cargando simulaciones de :" + GUI.currentUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicializa el botón de creación de simulaciones con su icono.
     */
    private void initializeCreateButton() {
        newButtonIcon = new ButtonIconText(p5, hMargin, vMargin,
                hRect - vMargin * 2, vRect - hMargin * 2);
        newButtonIcon.setText("Crear");
        newButtonIcon.setIcon("data/icons/add.svg");
        newButtonIcon.setScale(20);
    }

    /**
     * Inicializa la barra de búsqueda y su botón asociado.
     */
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

    /**
     * Inicializa botones adicionales de la barra lateral (info, ajustes, cerrar sesión).
     */
    private void initializeExtraButtons() {
        float m = 10;
        float bHeight = (vRect - vMargin * 3) / 2;
        float bWidth = hRect - 2 * hMargin - bHeight - m;

        info = new Button(this.p5, hMargin + bHeight + m, (4 * vRect) + vMargin,
                bWidth, bHeight);
        settings = new Button(this.p5, hMargin + bHeight + m, 4 * vRect + vMargin * 2 + bHeight,
                bWidth, bHeight);

        float logoutWidth = hRect - 2 * hMargin;
        float logoutHeight = vRect / 2;

        logoutButton = new Button(p5, hMargin, 5 * vRect + vMargin, logoutWidth, logoutHeight);
        logoutButton.setText("Cerrar sesión");
        logoutButton.strokeColorOn = FinalColors.accentSkyBlue();
    }

    /**
     * Inicializa la galería de tarjetas que muestra las simulaciones del usuario.
     */
    private void initializeGallery() {
        gallery = new Gallery(p5, hRect + vMargin, (2 * vMargin) + bannerHeight,
                bannerWidth, 4 * vRect - vMargin, (int) vMargin);
        gallery.setNumCards(simuList.length);
        gallery.setMaxCardsInPage(6);
        gallery.setCards(p5, simuList);
    }

    /**
     * Dibuja toda la pantalla, incluyendo la barra lateral, galería y botones.
     */
    @Override
    public void display() {
        // SideBar Area
        displaySearchBar();
        //displayExtraButtons();
        logoutButton.display();

        // Gallery Arean
        displayBanner();
        displayGallery();
        displayCreateButton();
    }

    /**
     * Dibuja la barra lateral con la búsqueda y opciones.
     */
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

    /**
     * Dibuja el banner decorativo superior (puede contener texto o imagen).
     */
    private void displayBanner() {
        p5.push();

        p5.fill(this.appColors.bgGrey());
        p5.noStroke();
        p5.rect(hRect + vMargin, vMargin, bannerWidth, bannerHeight, corner);

        if (bannerImage != null) {
            p5.image(bannerImage, hRect + vMargin, vMargin - 50);
        }
        p5.pop();
    }

    /**
     * Dibuja la galería de tarjetas de simulaciones.
     */
    private void displayGallery() {
        p5.push();
        p5.fill(this.appColors.bgLightGrey());
        //p5.rect(hRect+ vMargin, (2* vMargin)+bannerHeight, bannerWidth, 4*vRect - vMargin, corner);
        gallery.display(p5);
        p5.pop();
    }

    /**
     * Dibuja el botón de creación de simulaciones.
     */
    private void displayCreateButton() {
        newButtonIcon.display();
    }

    /**
     * Actualiza el cursor si el mouse está sobre botones interactivos.
     *
     * @param p5 instancia de Processing
     */
    private void updateCursor(PApplet p5) { //NO FUNCIONA
        if (searchButton.mouseOverButton(p5) || searchBar.mouseOverButtons(p5) || searchBar.getTextField().mouseOverTextField(p5)) {
            p5.cursor(HAND);
        } else {
            p5.cursor(ARROW);
        }
    }

    /**
     * Maneja las interacciones del usuario con el ratón:
     * <ul>
     *     <li>Crear nueva simulación</li>
     *     <li>Abrir una simulación existente desde una tarjeta</li>
     *     <li>Buscar entre simulaciones</li>
     *     <li>Cerrar sesión</li>
     * </ul>
     *
     * @param gui instancia del sistema de pantallas
     */
    public void mousePressed(GUI gui) {
        //"Create" button
        if (newButtonIcon.mouseOverButton(p5)) {
            newButtonIcon.setEnabled(true);
            GUI.currentSimId = -1; //
            GUI.setCurrentScreen(GUI.SCREEN.SIMULATOR);
        }
        //Gallery buttons
        for (Card cardButton : gallery.getCards()) {
            if (cardButton.mouseOverButton(p5)) {
                cardButton.setEnabled(true);
                SimulatorScreen simScreen = (SimulatorScreen) GUI.screens[GUI.SCREEN.SIMULATOR.ordinal()];
                GUI.currentSimId = Integer.parseInt(cardButton.getId());
                simScreen.loadSimFromDB(Integer.parseInt(cardButton.getId()));
                GUI.setCurrentScreen(GUI.SCREEN.SIMULATOR);
            }
        }
        //TextList
        if (searchButton.mouseOverButton(p5) && searchButton.isEnabled()) {
            selectedText = searchBar.getSelectedValue();
        }
        searchBar.getTextField().mousePressed();
        searchBar.buttonPressed(p5);

        // Cerrar sesión
        if (logoutButton.mouseOverButton(p5)) {
            GUI.currentUser = "";
            GUI.setCurrentScreen(GUI.SCREEN.LOGIN);
            ((LoginScreen) gui.screens[GUI.SCREEN.LOGIN.ordinal()]).reset();
        }
    }

    /**
     * Recarga las simulaciones del usuario actual desde la base de datos y actualiza la galería.
     */
    public void refreshGallery() {
        try {
            System.out.println("Cargando datos de " + GUI.currentUser);
            simuList = appDataBase.getInfoSimuladores(Main.GUI.currentUser);
            gallery.setNumCards(simuList.length);
            gallery.setCards(p5, simuList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga por primera vez las simulaciones del usuario al entrar en HomeScreen.
     * Se recomienda llamarlo desde LoginScreen al hacer login.
     */
    public void loadSimuladores() {
        try {
            simuList = appDataBase.getInfoSimuladores(GUI.currentUser);
            gallery.setNumCards(simuList.length);
            gallery.setCards(p5, simuList);
            System.out.println("Cargando simulaciones de: " + GUI.currentUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
