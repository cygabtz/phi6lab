package Constants;

import java.awt.Toolkit;

public class Layout {
    //-------------------------------GENERAL-------------------------------
    //Screen size
    public static final float screenH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final float screenV = Toolkit.getDefaultToolkit().getScreenSize().height;

    // Horizontal and vertical margins
    public static float hMargin = 30, vMargin = 30;

    //Small margin
    public static float margin = 6;

    //Guidelines rectangles (5:6)
    public static float hRect = screenH / 5, vRect = screenV / 6;

    //Top bar / left bar / frame -----------------------------------------
    public static float frame = (vRect / 8) * 2.5f;
    //Cards Corner
    public static float corner = 10;
    public static final int borderWeight = 2;

    //-------------------------------HOME-------------------------------
    // Side bar
    public static float sidebarWidth = hRect - hMargin * 2, sidebarHeight = 6 * vRect - 2 * vMargin;
    public static float sideBarCardWidth = sidebarWidth - 2 * hMargin;

}
