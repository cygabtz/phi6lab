package Main;

import Screens.*;

import processing.core.PApplet;
import Constants.Colors;

import static Constants.Layout.*;

/**
 * Clase principal de control de interfaz gráfica para la aplicación Phi6Lab.
 *
 * <p>Gestiona el ciclo de pantallas de la aplicación (login, home, simulador, configuración)
 * y mantiene el estado de sesión del usuario actual y simulador en uso.
 *
 * <p>Contiene referencias a:
 * <ul>
 *   <li>Instancia de {@link PApplet} de Processing</li>
 *   <li>Paleta de colores y fuentes del sistema</li>
 *   <li>Base de datos {@link DataBase}</li>
 *   <li>Pantallas activas y su estado actual</li>
 * </ul>
 *
 * <p>La clase permite cambiar entre pantallas mediante {@link #setCurrentScreen(SCREEN)},
 * renderiza la pantalla actual y mantiene las variables de sesión globales.
 *
 * @see Screens.LoginScreen
 * @see Screens.HomeScreen
 * @see Screens.SimulatorScreen
 * @see Screens.SettingsScreen
 * @see DataBase
 */
public class GUI {
    /** Instancia de Processing usada globalmente para dibujar. */
    public PApplet p5;

    /** Colores personalizados definidos en la aplicación. */
    public final Colors appColors;

    /** Tipografías definidas en el sistema. */
    public Constants.Fonts appFonts;

    /** Enumerador que representa cada pantalla del sistema. */
    public enum SCREEN {LOGIN, HOME, SIMULATOR, SETTINGS}

    /** Pantalla actual activa (en ejecución). */
    public static Screen currentScreen;

    /** Tipo de pantalla actual activa (LOGIN, HOME, etc.). */
    public static SCREEN currentSCREEN;

    /** Array de pantallas disponibles. */
    public static Screen[] screens;

    /** Referencia a la base de datos del sistema. */
    public DataBase appDataBase;

    /** ID de la simulación actualmente activa (-1 si es nueva). */
    public static int currentSimId = -1;

    /** Nombre del usuario actualmente autenticado. */
    public static String currentUser = "";

    /**
     * Constructor principal de la GUI.
     *
     * @param p5 instancia principal de Processing
     * @param appColors paleta de colores personalizada
     * @param appFonts conjunto de tipografías
     * @param db instancia de la base de datos
     */
    public GUI(PApplet p5, Colors appColors, Constants.Fonts appFonts, DataBase db){
        this.p5 = p5;
        this.appColors = appColors;
        this.appFonts = appFonts;
        this.appDataBase = db;

        initializeScreens(p5);
        currentScreen = new Screen(p5, appColors, appFonts);
    }

    /**
     * Cambia la pantalla activa según el tipo especificado.
     *
     * @param screenType tipo de pantalla a activar (LOGIN, HOME, etc.)
     */
    public static void setCurrentScreen(SCREEN screenType){
        currentSCREEN = screenType;
        currentScreen = screens[screenType.ordinal()];
    }

    /**
     * Inicializa todas las pantallas y las almacena en el array global {@code screens}.
     *
     * @param p5 instancia de Processing usada para crear cada pantalla
     */
    public void initializeScreens(PApplet p5){
        screens = new Screen[5];
        screens[0] = new LoginScreen(p5, appColors, appFonts);       // LOGIN
        screens[1] = new HomeScreen(p5, appColors, appFonts, appDataBase);
        screens[2] = new SimulatorScreen(p5, appColors, appFonts);
        screens[3] = new SettingsScreen(p5, appColors, appFonts);
    }

    /**
     * Dibuja la pantalla activa actualmente.
     *
     * @param p5 instancia global de Processing
     */
    public void displayActualScreen(PApplet p5){
        //displayGrid(p5);

        //Normal execution
        //displayGrid(p5);
        currentScreen.display();
    }

    /**
     * (Opcional) Dibuja una cuadrícula de depuración sobre la pantalla
     * dividida en rectángulos según {@code hRect} y {@code vRect}.
     *
     * @param p5 instancia de Processing
     */
    public void displayGrid(PApplet p5){
        p5.pushStyle();
        p5.stroke(255); p5.strokeWeight(1); p5.noFill();
        for (int i = 0; i<=5; i++){
            for (int j = 0; j<=6; j++){
                p5.rect(hRect*i, vRect*j, hRect * (i+1), vRect * (j+1));
            }
        }
        p5.popStyle();
    }
}
