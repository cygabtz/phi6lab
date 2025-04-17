package Constants;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * Clase que gestiona una colección de fuentes personalizadas utilizadas en la interfaz de la aplicación.
 *
 * <p>Esta clase instancia un conjunto de fuentes desde archivos TTF ubicados en el directorio {@code data/fonts},
 * usando el motor de Processing. Cada fuente tiene un tamaño asociado (párrafos, encabezados, detalles...).
 *
 * <p><strong>Recomendación:</strong> se sugiere utilizar {@link StaticFonts}, que ofrece las mismas fuentes
 * de forma estática sin necesidad de crear una instancia de {@code Fonts}, lo que mejora la eficiencia y legibilidad del código.
 */
public class Fonts {
    /** Array que almacena las diferentes fuentes tipográficas cargadas desde archivo. */
    public PFont[] fonts;

    /**
     * Constructor que carga todas las fuentes en memoria utilizando una instancia de Processing.
     *
     * @param p5 instancia de Processing necesaria para crear las fuentes
     */
    public Fonts(PApplet p5){
        this.setFonts(p5);
    }

    /**
     * Carga y asigna las fuentes personalizadas al array {@code fonts}.
     *
     * @param p5 instancia de Processing para crear las fuentes
     */
    void setFonts(PApplet p5){
        this.fonts = new PFont[4];
        this.fonts[0] = p5.createFont("data/fonts/Rubik-Regular.ttf", Sizes.paragraph);
        this.fonts[1] = p5.createFont("data/fonts/Rubik-SemiBold.ttf", Sizes.widgetHeading);
        this.fonts[2] = p5.createFont("data/fonts/Rubik-Light.ttf", Sizes.details);
    }

}