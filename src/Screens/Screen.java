package Screens;
import processing.core.PApplet;
import Main.GUI.SCREEN;
import Constants.Fonts;
import Constants.Colors;


/**
 * Clase base para todas las pantallas de la aplicación Phi6 Lab.
 *
 * <p>Proporciona una estructura común para gestionar la visualización de interfaces
 * y acceso a propiedades compartidas como los colores, fuentes y el contexto de Processing.
 *
 * <p>Las clases derivadas deben sobrescribir el método {@link #display()} para dibujar
 * su contenido específico.
 */
public class Screen {

    /**
     * Enumerador que indica el tipo de pantalla que representa la instancia.
     * Útil para gestión de navegación en el sistema de pantallas (GUI).
     */
    private SCREEN screenType;

    /**
     * Instancia principal de Processing usada para el renderizado.
     */
    public PApplet p5;

    /**
     * Conjunto de fuentes de texto utilizadas por la aplicación.
     */
    public Constants.Fonts appFonts;

    /**
     * Paleta de colores definida para la aplicación.
     */
    public Colors appColors;

    /**
     * Constructor base para una pantalla.
     *
     * @param p5       instancia de Processing
     * @param colors   colores del sistema
     * @param fonts    fuentes del sistema
     */
    public Screen(PApplet p5, Colors colors, Fonts fonts) {
        this.p5 = p5;
        this.appColors = colors;
        this.appFonts = fonts;
    }

    /**
     * Devuelve el tipo de pantalla representado por esta instancia.
     *
     * @return enumerador {@code SCREEN} correspondiente a esta pantalla
     */
    public SCREEN getType() {
        return this.screenType;
    }

    /**
     * Método abstracto que debe ser sobrescrito por cada pantalla hija
     * para dibujar su interfaz gráfica correspondiente.
     */
    public void display() {
        // Implementación vacía por defecto
    }
}
