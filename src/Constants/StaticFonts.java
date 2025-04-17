package Constants;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * Clase de utilidad que gestiona una colección de fuentes predefinidas de forma estática.
 *
 * <p>Permite acceder a las fuentes desde cualquier parte del programa sin necesidad de instanciar objetos,
 * a diferencia de {@link Constants.Fonts}. Esto facilita el uso compartido de tipografías y mejora el rendimiento.
 *
 * <p>Las fuentes deben inicializarse una vez mediante el método {@link #initialize(PApplet)} al inicio de la aplicación.
 */
public class StaticFonts {

    /** Array estático que almacena las fuentes cargadas desde archivo. */
    private static PFont[] fonts;

    /**
     * Carga las fuentes desde los archivos en el directorio {@code data/fonts}
     * y las almacena en el array interno {@code fonts}.
     *
     * <p>Debe llamarse una vez al inicio de la aplicación (en {@code setup()}) antes de intentar acceder a cualquier fuente.
     *
     * @param p5 instancia de Processing necesaria para crear fuentes tipográficas
     */
    public static void initialize(PApplet p5) {
        fonts = new PFont[4];
        fonts[0] = p5.createFont("data/fonts/Rubik-Regular.ttf", Sizes.paragraph);
        fonts[1] = p5.createFont("data/fonts/Rubik-SemiBold.ttf", Sizes.widgetHeading);
        fonts[2] = p5.createFont("data/fonts/Rubik-Light.ttf", Sizes.details);
    }

    /**
     * Devuelve la fuente correspondiente al índice indicado.
     *
     * @param index índice de la fuente (0 = regular, 1 = semibold, 2 = light)
     * @return fuente {@code PFont} solicitada
     * @throws IllegalStateException si {@link #initialize(PApplet)} no ha sido llamado previamente
     */
    public static PFont getFont(int index) {
        if (fonts == null) {
            throw new IllegalStateException("Fonts no ha sido inicializado. Llama a initialize() primero.");
        }
        return fonts[index];
    }

}
