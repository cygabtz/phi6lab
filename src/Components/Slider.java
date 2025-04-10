package Components;

import Constants.FinalColors;
import processing.core.PApplet;

import static Constants.Layout.corner;
import static Constants.Layout.margin;

public class Slider {
    private PApplet p5;
    public float x, y, width, height, handleX;
    public int boxColor, selectedColor, lineColor, fontColor, lineWeight;
    public float minValue, maxValue, value;
    public boolean dragging = false;
    private float handleRadius = 12;
    private float mg = handleRadius + margin;

    public Slider(PApplet p5, float x, float y, float width, float height, float minValue, float maxValue, float initialValue) {
        this.p5 = p5;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = initialValue;
        this.handleX = PApplet.map(initialValue, minValue, maxValue, x + mg, x + width - mg);

        this.boxColor = FinalColors.accentSkyBlue();
        this.lineColor = FinalColors.primaryYellow();


        this.selectedColor = FinalColors.bgLightGrey();
        this.fontColor = FinalColors.textWhite();
    }

    public void display() {
        //Hit box
        p5.noFill();
        p5.strokeWeight(1);
        p5.stroke(FinalColors.primaryYellow());
        p5.rect(x, y, width, height, corner);

        // Draw the slider line
        p5.stroke(lineColor);
        p5.strokeWeight(5);
        p5.line(x + mg, y + height/2, x + width - mg, y + height/2);

        // Draw the slider handle

        p5.fill(boxColor);
        p5.stroke(FinalColors.bgBlack()); p5.strokeWeight(2);
        p5.ellipse(handleX, y + height/2, handleRadius*2, handleRadius*2);

        // Draw the value label (Deprecated)
//        p5.fill(boxColor);
//        float labelWidth = 100;
//        float labelHeight = 30;
//        p5.rect(handleX - labelWidth/2, y + height - height/4, labelWidth, labelHeight);
//        p5.fill(fontColor);
//        p5.textAlign(PApplet.CENTER, PApplet.CENTER);
//        String unit = "m";
//        p5.text(String.format("%.2f %s", value, unit), handleX, y + height + 4);
    }

    public void update() {
        if (dragging) {
            float newValue = PApplet.map(p5.mouseX, x + mg, x + width - 2* mg, minValue, maxValue);
            value = PApplet.constrain(newValue, minValue, maxValue);
            handleX = PApplet.map(value, minValue, maxValue, x + mg, x + width - mg);
        }
    }

    public void mousePressed() {
        float handleRadius = 10;
        if (PApplet.dist(p5.mouseX, p5.mouseY, handleX, y + height/2) < handleRadius) {
            dragging = true;
        }
    }

    public boolean isMouseOverSlider(){
        float handleRadius = 10;
        return PApplet.dist(p5.mouseX, p5.mouseY, handleX, y + height/2) < handleRadius;
    }

    public void mouseReleased() {
        dragging = false;
    }

    public void setValueAt(int value){
        this.value = value;
        this.handleX = PApplet.map(value, minValue, maxValue, x + mg, x + width - mg);
    }

    @Override
    public Slider clone() {
        // Crear una nueva instancia de Slider con las mismas propiedades iniciales
        Slider clonedSlider = new Slider(this.p5, this.x, this.y, this.width, this.height, this.minValue, this.maxValue, this.value);

        // Clonar propiedades adicionales
        clonedSlider.handleX = this.handleX;
        clonedSlider.boxColor = this.boxColor;
        clonedSlider.selectedColor = this.selectedColor;
        clonedSlider.lineColor = this.lineColor;
        clonedSlider.fontColor = this.fontColor;
        clonedSlider.lineWeight = this.lineWeight;
        clonedSlider.dragging = this.dragging;
        clonedSlider.handleRadius = this.handleRadius;

        return clonedSlider;
    }

}
