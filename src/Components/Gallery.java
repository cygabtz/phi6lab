package Components;

import Main.GUI;
import processing.core.PApplet;

public class Gallery {
    Card[] cards;               // Productes
    int numCards;               // Número total de Productes
    int maxCardsInPage;         // Número de targetas en una página
    int numCardsRows = 3;

    int numPage;
    int numTotalPages;
    float margin;

    float x, y, w, h;
    float wCard, hCard;

    // Constructor
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


    public void nextPage() {
        if (this.numPage<this.numTotalPages) {
            this.numPage++;
        }
    }

    public void prevPage() {
        if (this.numPage>0) {
            this.numPage--;
        }
    }

    // Dibuixa taula
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

    public void setNumCards(int numCards) {
        this.numCards = numCards;
    }

    public void setMaxCardsInPage(int num){
        this.maxCardsInPage = num;
    }

    public Card[] getCards(){
        return this.cards;
    }
}
