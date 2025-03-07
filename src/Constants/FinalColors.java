package Constants;

public class FinalColors {
    private static final int[] colors;

    static {
        colors = new int[10];
        colors[0] = 0xFFffcc00;
        colors[1] = 0xFFddaf00;
        colors[2] = 0xFF916e00;
        colors[3] = 0xFF33ccff;
        colors[4] = 0xFF006d9b;
        colors[5] = 0xFFffffff;
        colors[6] = 0xFFe0e0e0;
        colors[7] = 0xFF1a1a1a;
        colors[8] = 0xFF292929;
        colors[9] = 0xFF404040;
    }

    public static int getNumColors() {
        return colors.length;
    }

    public static int primaryYellow() {
        return colors[0];
    }

    public static int primaryMustard() {
        return colors[1];
    }

    public static int primaryBrown() {
        return colors[2];
    }

    public static int accentSkyBlue() {
        return colors[3];
    }

    public static int accentDenimBlue() {
        return colors[4];
    }

    public static int textWhite() {
        return colors[5];
    }

    public static int textGrey() {
        return colors[6];
    }

    public static int bgBlack() {
        return colors[7];
    }

    public static int bgGrey() {
        return colors[8];
    }

    public static int bgLightGrey() {
        return colors[9];
    }

    public static int getColorAt(int i) {
        return colors[i];
    }


}
