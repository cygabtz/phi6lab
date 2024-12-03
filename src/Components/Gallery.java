package Components;

import processing.core.PApplet;
import processing.core.PImage;

public class Gallery {
    Card[] cards;               // Productes
    int numCards;               // Número total de Productes
    int numCardsPage;           // Número de Productes en 1 Pàgina
    int numCardsRows = 2;

    int numPage;
    int numTotalPages;
    float margin;

    float x, y, w, h;
    float wCard, hCard;

    // Constructor
    public Gallery(PApplet p5, int ncp, float x, float y, float w, float h) {

        this.numCards = 10;
        this.numCardsPage = ncp;
        this.numPage = 0;

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.wCard = (w - margin *(numCardsRows -1)) / numCardsRows;
        this.hCard = (h - margin) / 2f;

        this.margin = 20;
    }

    // Setters

    public void setCards(PApplet p5) {

        cards = new Card[numCards];

        int k=0;

        for (int i=0; i<cards.length; i++) {

            float xc =  x + (i% numCardsRows)* (wCard + margin);
            float yc = (k% numCardsPage)<(numCardsPage /2)? y : y + hCard + margin;

            cards[i] = new Card(p5, xc, yc, wCard, hCard);
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

        // Dibuixa Cards
        int firstCardPage = numCardsPage *numPage;
        int lastCardPage  = numCardsPage *(numPage+1) - 1;

        for (int i = firstCardPage; i <= lastCardPage; i++) {
            if (i<cards.length) {
                boolean mouseOver = (numCardOver(p5)==i);
                cards[i].display(p5);

            }
        }

        // Informació de la Pàgina
        p5.fill(0); p5.textSize(18); p5.textAlign(p5.LEFT);
        p5.text("Pag: "+(this.numPage+1)+" / "+(this.numTotalPages+1), x + w + 60, y+30);

        p5.popStyle();
    }

    public int numCardOver(PApplet p5) {

        int firstCardPage = numCardsPage *numPage;
        int lastCardPage  = numCardsPage *(numPage+1) - 1;


        for (int i = firstCardPage; i <= lastCardPage; i++) {
            if (i<cards.length) {

                float xc =  x + (i% numCardsRows)* (wCard + margin);
                float yc = ((i)% numCardsPage)<(numCardsPage /2)? y : y + hCard + margin;

                if (p5.mouseX > xc && p5.mouseX < xc + wCard &&
                        p5.mouseY > yc && p5.mouseY < yc + hCard) {
                    return i;
                }
            }
        }
        return -1;
    }
}
