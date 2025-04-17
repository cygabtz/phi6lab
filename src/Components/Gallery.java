package Components;

import Main.GUI;
import processing.core.PApplet;

/**
 * Clase que representa una galería de tarjetas (Cards) dentro de la interfaz gráfica.
 *
 * <p>La galería permite mostrar un conjunto de tarjetas paginadas, cada una representando una
 * simulación (o producto) en forma visual. Controla la disposición, paginación y renderizado de
 * las tarjetas según la resolución y el número de elementos.
 *
 * <p>Esta clase es utilizada principalmente en {@code HomeScreen} para mostrar las simulaciones
 * asociadas a un usuario.
 */
public class Gallery {
    /** Array de objetos {@link Card} que representan cada simulación visualizada. */
    Card[] cards;

    /** Número total de tarjetas a mostrar en la galería. */
    int numCards;

    /** Número máximo de tarjetas visibles por página. */
    int maxCardsInPage;

    /** Número de tarjetas por fila (normalmente 3). */
    int numCardsRows = 3;

    /** Página actual que se está visualizando. */
    int numPage;

    /** Número total de páginas disponibles. */
    int numTotalPages;

    /** Margen entre las tarjetas. */
    float margin;

    /** Posición X del área de la galería. */
    float x;

    /** Posición Y del área de la galería. */
    float y;

    /** Ancho total de la galería. */
    float w;

    /** Alto total de la galería. */
    float h;

    /** Ancho individual de una tarjeta. */
    float wCard;

    /** Alto individual de una tarjeta. */
    float hCard;

    /**
     * Crea una nueva galería de tarjetas con posición, tamaño y espaciado definidos.
     *
     * @param p5     instancia de Processing
     * @param x      coordenada X de inicio
     * @param y      coordenada Y de inicio
     * @param w      ancho total de la galería
     * @param h      alto total de la galería
     * @param margin espacio entre tarjetas
     */
    public Gallery(PApplet p5, float x, float y, float w, float h, int margin) {

        this.numCards = 10;
        this.maxCardsInPage = 6;
        this.numPage = 0;

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.margin = margin;

        this.wCard = (w - margin *(numCardsRows -1)) / numCardsRows;
        this.hCard = (h - margin) / 2f;

    }

    // Setters

    /**
     * Carga los datos de las tarjetas a mostrar en la galería a partir de un array con la información.
     *
     * <p>Se crean instancias de {@link Card} en posiciones predefinidas con imágenes por defecto.
     *
     * @param p5        instancia de Processing
     * @param cardsInfo matriz con información de cada tarjeta (ID, título, fechas, etc.)
     */
    public void setCards(PApplet p5, String [][] cardsInfo) {
        cards = new Card[numCards];
        int k=0;

        for (int i=0; i<cards.length; i++) {

            float xc =  x + (i% numCardsRows)* (wCard + margin);
            float yc = (k% maxCardsInPage)<(maxCardsInPage /2)? y : y + hCard + margin;

            cards[i] = new Card(p5, xc, yc, wCard, hCard);
            cards[i].setInfo(cardsInfo[i]);
            cards[i].buildImage(p5, "data/testImage.png");
            k++;
        }
    }

    /** Avanza a la siguiente página si no se ha alcanzado la última. */
    public void nextPage() {
        if (this.numPage<this.numTotalPages) {
            this.numPage++;
        }
    }

    /** Retrocede a la página anterior si no se está en la primera. */
    public void prevPage() {
        if (this.numPage>0) {
            this.numPage--;
        }
    }

    /**
     * Dibuja las tarjetas correspondientes a la página actual y muestra información de paginación.
     *
     * @param p5 instancia de Processing utilizada para el renderizado
     */
    public void display(PApplet p5) {

        p5.pushStyle();

        // Dibuja Cards
        int firstCardPage = maxCardsInPage * numPage;
        int lastCardPage  = maxCardsInPage *(numPage+1) - 1;

        //Dibuja solo las targetas en la página indicada
        for (int i = 0; i <= numCards; i++) {
            //i targetas

            if (i<cards.length) {
                cards[i].display();
            }
        }

        // Dibuja la información de la página
        p5.fill(0); p5.textSize(18); p5.textAlign(p5.LEFT);
        p5.text("Pag: "+(this.numPage+1)+" / "+(this.numTotalPages+1), x + w + 60, y+30);

        p5.popStyle();
    }

    /**
     * Devuelve el índice de la tarjeta sobre la que está posicionado el cursor.
     *
     * @param p5 instancia de Processing
     * @return índice de la tarjeta sobre la que está el ratón, o {@code -1} si no hay ninguna
     */
    public int numCardOver(PApplet p5) {

        int firstCardPage = maxCardsInPage *numPage;
        int lastCardPage  = maxCardsInPage *(numPage+1) - 1;


        for (int i = firstCardPage; i <= lastCardPage; i++) {
            if (i<cards.length) {

                float xc =  x + (i% numCardsRows)* (wCard + margin);
                float yc = ((i)% maxCardsInPage)<(maxCardsInPage /2)? y : y + hCard + margin;

                if (p5.mouseX > xc && p5.mouseX < xc + wCard &&
                        p5.mouseY > yc && p5.mouseY < yc + hCard) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Establece el número total de tarjetas a mostrar.
     * @param numCards número total de elementos
     */
    public void setNumCards(int numCards) {
        this.numCards = numCards;
    }

    /**
     * Define el número máximo de tarjetas visibles por página.
     * @param num número de tarjetas por página
     */
    public void setMaxCardsInPage(int num){
        this.maxCardsInPage = num;
    }

    /**
     * Devuelve el array de tarjetas actualmente cargadas.
     * @return array de objetos {@link Card}
     */
    public Card[] getCards(){
        return this.cards;
    }
}
