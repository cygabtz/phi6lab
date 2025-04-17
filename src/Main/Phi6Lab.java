package Main;

//Processing properties

import Components.Button;
import Components.FieldSlider;
import Constants.FinalColors;
import Constants.StaticFonts;
import Screens.SimulatorScreen;
import processing.core.PApplet;
import Constants.Colors;
import Constants.Fonts;

import static Constants.Layout.*;

/**
 * Clase principal de la aplicación Phi6 Lab.
 *
 * <p>Extiende {@link PApplet} y se encarga de inicializar el entorno gráfico, las fuentes, la base de datos
 * y el sistema de navegación entre pantallas ({@link GUI}). Gestiona también las interacciones globales del
 * teclado y ratón según la pantalla activa.
 *
 * <p>Esta clase es el punto de entrada del programa y contiene el ciclo principal de dibujo y eventos.
 */
public class Phi6Lab extends PApplet {
    /**
     * Método principal de arranque de la aplicación.
     *
     * @param args argumentos del sistema (no utilizados)
     */
    public static void main(String[] args) {
        PApplet.main("Main.Phi6Lab", args);
    }

    /**
     * Configura los parámetros iniciales del lienzo de Processing.
     * Se establece tamaño de pantalla completa y suavizado.
     */
    @Override
    public void settings() {
        size(parseInt(screenH), parseInt(screenV));
        smooth(10);
        fullScreen();
    }

    /** Esquema de colores personalizados utilizados en toda la interfaz. */
    Constants.Colors appColors;

    /** Gestor gráfico de pantallas e interfaz. */
    GUI gui;
    /** Tipografías utilizadas en la aplicación. */
    Constants.Fonts appFonts;

    /** Instancia estática de la base de datos MySQL. */
    public static DataBase db;

    /**
     * Método de inicialización de Processing.
     *
     * <p>Establece el frame rate, carga fuentes, colores, conecta con la base de datos
     * y lanza la pantalla inicial del sistema (pantalla de login).
     */
    public void setup() {
        frameRate(80);
        appColors = new Colors(this);
        appFonts = new Fonts(this);
        StaticFonts.initialize(this);

        db = new DataBase("admin", "12345", "simulaciones");


        try {
            db.connect();
        } catch (Exception e) {
            System.out.println("BASE NO CONECTADA");
        }


        gui = new GUI(this, appColors, appFonts, db);
        GUI.setCurrentScreen(GUI.SCREEN.LOGIN);

        System.out.println("H: " + screenH + "V: " + screenV);
    }

    /**
     * Ciclo principal de dibujo. Borra el fondo y renderiza la pantalla actual.
     */
    public void draw() {
        background(FinalColors.bgBlack());
        gui.displayActualScreen(this);
        //if (gui.currentScreen instanceof Screens.HomeScreen) text("Usuario: " + GUI.currentUser, 50, screenV-50);
    }

    /**
     * Lógica para captura de eventos de teclado.
     * Dependiendo de la pantalla activa, redirige la entrada al componente correspondiente.
     */
    public void keyPressed() {
        if (gui.currentScreen instanceof Screens.HomeScreen hs) {
            //TextList
            if (hs.searchBar.getTextField().mouseOverTextField(this)) {
                hs.searchBar.getTextField().keyPressed(key, keyCode);
                hs.searchBar.update(this, appFonts);
            }
        } else if (gui.currentScreen instanceof Screens.SimulatorScreen ss) {
            ss.keyPressed(key, keyCode);
        }

        if (gui.currentScreen instanceof Screens.LoginScreen ls) {
            ls.keyPressed(key, keyCode);
        }

    }

    /**
     * Lógica de procesamiento de clic del ratón.
     * Redirige el evento a la pantalla correspondiente.
     */
    public void mousePressed() {
        if (gui.currentScreen instanceof Screens.HomeScreen hs) {
            hs.mousePressed(gui);
        } else if (gui.currentScreen instanceof Screens.SimulatorScreen ss) {
            ss.mousePressed();
        }

        if (gui.currentScreen instanceof Screens.LoginScreen ls) {
            ls.mousePressed(gui);
        }


    }

    /**
     * Lógica para arrastre del ratón en la pantalla del simulador.
     */
    public void mouseDragged() {
        if (gui.currentScreen instanceof Screens.SimulatorScreen ss) {
            ss.mouseDragged();
        }
    }

    /**
     * Lógica cuando se suelta el botón del ratón.
     */
    public void mouseReleased() {
        if (gui.currentScreen instanceof SimulatorScreen ss) {
            ss.mouseReleased();
        }
    }

}