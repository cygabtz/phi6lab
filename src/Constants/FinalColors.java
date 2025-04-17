package Constants;

/**
 * Clase de utilidad que proporciona una paleta de colores constantes predefinidos
 * para toda la interfaz gráfica de la aplicación.
 *
 * <p>Al estar basada en atributos {@code static final}, esta clase permite acceder
 * a los colores sin necesidad de instanciar ningún objeto, lo que la hace más eficiente
 * y recomendada frente a {@link Constants.Colors}, que requiere un {@code PApplet} para su uso.
 *
 * <p>Los colores están definidos en notación hexadecimal ARGB (alpha-red-green-blue).
 */
public class FinalColors {
    /**
     * Array de colores predefinidos utilizados en la aplicación.
     * Cada posición del array representa un color con una función específica.
     */
    private static final int[] colors;

    // Bloque estático que inicializa los colores una sola vez
    static {
        colors = new int[10];
        colors[0] = 0xFFffcc00;
        colors[1] = 0xFFddaf00;
        colors[2] = 0xFF916e00;
        colors[3] = 0xFF33ccff;
        colors[4] = 0xFF006d9b;
        colors[5] = 0xFFffffff;
        colors[6] = 0xFFe0e0e0;
        colors[7] = 0xFF1a1a1a;
        colors[8] = 0xFF292929;
        colors[9] = 0xFF404040;
    }

    /** @return número total de colores definidos en la paleta */
    public static int getNumColors() {
        return colors.length;
    }

    /** @return color amarillo principal de la interfaz */
    public static int primaryYellow() {
        return colors[0];
    }

    /** @return color mostaza */
    public static int primaryMustard() {
        return colors[1];
    }

    /** @return color marrón oscuro */
    public static int primaryBrown() {
        return colors[2];
    }

    /** @return color azul cielo (para acentos visuales) */
    public static int accentSkyBlue() {
        return colors[3];
    }

    /** @return color azul denim (para botones o elementos destacados) */
    public static int accentDenimBlue() {
        return colors[4];
    }

    /** @return color blanco para textos principales */
    public static int textWhite() {
        return colors[5];
    }

    /** @return color gris claro para textos secundarios */
    public static int textGrey() {
        return colors[6];
    }

    /** @return color negro para fondo principal */
    public static int bgBlack() {
        return colors[7];
    }

    /** @return gris oscuro como fondo alternativo */
    public static int bgGrey() {
        return colors[8];
    }

    /** @return gris medio como fondo secundario */
    public static int bgLightGrey() {
        return colors[9];
    }

    /**
     * Devuelve el color ubicado en el índice indicado del array interno.
     *
     * @param i índice del color (0 a 9)
     * @return valor ARGB del color
     */
    public static int getColorAt(int i) {
        return colors[i];
    }


}
