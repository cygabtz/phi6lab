package Constants;

import processing.core.PApplet;
//Pendent posar static
public class Colors {
    int[] colors;
    public Colors(PApplet p5){
        this.setColors(p5);
    }

    public void setColors(PApplet p5){
        this.colors = new int[10];
        this.colors[0] = p5.color(0xFFffcc00);
        this.colors[1] = p5.color(0xFFddaf00);
        this.colors[2] = p5.color(0xFF916e00);
        this.colors[3] = p5.color(0xFF33ccff);
        this.colors[4] = p5.color(0xFF006d9b);
        this.colors[5] = p5.color(0xFFffffff);
        this.colors[6] = p5.color(0xFFe0e0e0);
        this.colors[7] = p5.color(0xFF1a1a1a);
        this.colors[8] = p5.color(0xFF292929);
        this.colors[9] = p5.color(0xFF404040);
    }

    public int getNumColors(){
        return this.colors.length;
    }

    public int primaryYellow(){
        return  this.colors[0];
    }

    public int primaryMustard(){
        return  this.colors[1];
    }

    public int primaryBrown(){
        return  this.colors[2];
    }

    public int accentSkyBlue(){
        return  this.colors[3];
    }

    public int accentDenimBlue(){
        return  this.colors[4];
    }

    public int textWhite(){
        return  this.colors[5];
    }

    public int textGrey(){
        return  this.colors[6];
    }

    public int bgBlack(){
        return  this.colors[7];
    }

    public int bgGrey(){
        return  this.colors[8];
    }

    public int bgLightGrey(){
        return  this.colors[9];
    }

    public int getColorAt(int i){
        return this.colors[i];
    }

    public void displayColors(PApplet p5, float x, float y, float w){
        p5.pushStyle();

        p5.noStroke();
        p5.fill(0); p5.textAlign(p5.LEFT); p5.textSize(36);
        p5.text("Constants.Colors:", x, y-10);


        float wc = w / getNumColors();
        for(int i=0; i<getNumColors(); i++){
            p5.fill(getColorAt(i));
            p5.rect(x + i*wc, y, wc, wc);
        }
        p5.popStyle();
    }
}
