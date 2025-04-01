package Components;

import Constants.FinalColors;
import Constants.StaticFonts;
import processing.core.PApplet;
import processing.core.PConstants;

import static Constants.Layout.*;

public class ButtonIconText extends ButtonIcon{

    public ButtonIconText(PApplet p5, float x, float y, float width, float height) {
        super(p5, x, y, width, height);
    }

    @Override
    public void display() {
        p5.pushStyle();
        p5.fill(this.fillColor);
        if(mouseOverButton(p5)) {
            p5.stroke(this.strokeColorOn);
            p5.strokeWeight(2);
        }
        else p5.noStroke();

        p5.rect(this.x, this.y, this.width, this.height, corner);

        if (icon!=null){
            p5.stroke(255);
            icon.disableStyle();
            p5.fill(255);

            float size = height-2*hMargin + scale;

            p5.shape(icon, x+vMargin - scale/2, y+hMargin - scale/2, size, size);
        }

        int marginBut = 2;

        p5.textFont(StaticFonts.getFont(1));
        p5.fill(FinalColors.textWhite());
        p5.textSize(50);
        p5.textAlign(PConstants.LEFT, PConstants.CENTER);
        p5.text(buttonText, this.x + 2*width/5, this.y + this.height/2);

        p5.popStyle();
    }

}
