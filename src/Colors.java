import processing.core.PApplet;
public class Colors {
    int[] colors;
    public Colors(PApplet p5){
        this.setColors(p5);
    }

    void setColors(PApplet p5){
        this.colors = new int[5];
        this.colors[0] = p5.color(0xFF068D9D);
        this.colors[1] = p5.color(0xFF53599A);
        this.colors[2] = p5.color(0xFF6D9DC5);
        this.colors[3] = p5.color(0xFF80DED9);
        this.colors[4] = p5.color(0xFFAEECEF);
    }
}
