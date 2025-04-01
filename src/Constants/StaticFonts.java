package Constants;
import processing.core.PApplet;
import processing.core.PFont;
public class StaticFonts {
        private static PFont[] fonts;

        public static void initialize(PApplet p5) {
            fonts = new PFont[4];
            fonts[0] = p5.createFont("data/fonts/Rubik-Regular.ttf", Sizes.paragraph);
            fonts[1] = p5.createFont("data/fonts/Rubik-SemiBold.ttf", Sizes.widgetHeading);
            fonts[2] = p5.createFont("data/fonts/Rubik-Light.ttf", Sizes.details);
        }

        public static PFont getFont(int index) {
            if (fonts == null) {
                throw new IllegalStateException("Fonts no ha sido inicializado. Llama a initialize() primero.");
            }
            return fonts[index];
        }

}
