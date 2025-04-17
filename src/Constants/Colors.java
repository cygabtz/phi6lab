package Constants;

import processing.core.PApplet;

/**
 * Clase que define una paleta de colores personalizada para la interfaz gráfica.
 *
 * <p>Los colores se generan a partir de valores hexadecimales utilizando la instancia de {@code PApplet},
 * por lo que esta clase requiere ser instanciada con Processing.
 *
 * <p><strong>Nota:</strong> se recomienda usar {@link FinalColors} en lugar de esta clase,
 * ya que {@code FinalColors} proporciona los mismos colores como constantes estáticas,
 * sin necesidad de instanciar un objeto ni pasar {@code PApplet}.
 */
public class Colors {

    /** Array que contiene los colores definidos por orden. */
    int[] colors;

    /**
     * Constructor que inicializa la paleta de colores con una instancia de {@code PApplet}.
     *
     * @param p5 instancia de Processing necesaria para generar los colores
     */
    public Colors(PApplet p5){
        this.setColors(p5);
    }

    /**
     * Define los colores personalizados cargándolos en el array {@code colors}.
     *
     * @param p5 instancia de Processing
     */
    public void setColors(PApplet p5){
        this.colors = new int[10];
        this.colors[0] = p5.color(0xFFffcc00); // primaryYellow
        this.colors[1] = p5.color(0xFFddaf00); // primaryMustard
        this.colors[2] = p5.color(0xFF916e00); // primaryBrown
        this.colors[3] = p5.color(0xFF33ccff); // accentSkyBlue
        this.colors[4] = p5.color(0xFF006d9b); // accentDenimBlue
        this.colors[5] = p5.color(0xFFffffff); // textWhite
        this.colors[6] = p5.color(0xFFe0e0e0); // textGrey
        this.colors[7] = p5.color(0xFF1a1a1a); // bgBlack
        this.colors[8] = p5.color(0xFF292929); // bgGrey
        this.colors[9] = p5.color(0xFF404040); // bgLightGrey
    }

    /**
     * Devuelve el número total de colores definidos.
     *
     * @return número de colores
     */
    public int getNumColors(){
        return this.colors.length;
    }

    /** @return color amarillo principal */
    public int primaryYellow(){ return this.colors[0]; }

    /** @return color mostaza */
    public int primaryMustard(){ return this.colors[1]; }

    /** @return color marrón oscuro */
    public int primaryBrown(){ return this.colors[2]; }

    /** @return color azul cielo */
    public int accentSkyBlue(){ return this.colors[3]; }

    /** @return color azul denim */
    public int accentDenimBlue(){ return this.colors[4]; }

    /** @return color blanco para textos */
    public int textWhite(){ return this.colors[5]; }

    /** @return color gris claro para textos */
    public int textGrey(){ return this.colors[6]; }

    /** @return color negro para fondo */
    public int bgBlack(){ return this.colors[7]; }

    /** @return color gris medio para fondo */
    public int bgGrey(){ return this.colors[8]; }

    /** @return color gris claro para fondo */
    public int bgLightGrey(){ return this.colors[9]; }

    /**
     * Devuelve el color en la posición {@code i} del array de colores.
     *
     * @param i índice del color
     * @return color correspondiente
     */
    public int getColorAt(int i){
        return this.colors[i];
    }

    /**
     * Dibuja una representación visual de todos los colores definidos.
     *
     * @param p5 instancia de Processing
     * @param x posición horizontal inicial
     * @param y posición vertical inicial
     * @param w ancho total del bloque visual
     */
    public void displayColors(PApplet p5, float x, float y, float w){
        p5.pushStyle();
        p5.noStroke();
        p5.fill(0);
        p5.textAlign(p5.LEFT);
        p5.textSize(36);
        p5.text("Constants.Colors:", x, y - 10);

        float wc = w / getNumColors();
        for(int i = 0; i < getNumColors(); i++){
            p5.fill(getColorAt(i));
            p5.rect(x + i * wc, y, wc, wc);
        }

        p5.popStyle();
    }
}

