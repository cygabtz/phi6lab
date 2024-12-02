package Constants;

import java.awt.Toolkit;

public class Layout {
    //-------------------------------GENERAL-------------------------------
    //Screen size
    public final static float screenH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public final static float screenV = Toolkit.getDefaultToolkit().getScreenSize().height;

    // Horizontal and vertical margins
    public static float hMargin = 30, vMargin = 30;

    //Guidelines rectangles (5:6)
    public static float hRect = screenH/5, vRect = screenV/6;

    //Cards Corner
    public static float corner = 10;

    //-------------------------------HOME-------------------------------
    // Side bar
    public static float sidebarWidth  = hRect - hMargin *2, sidebarHeight = 6*vRect - 2* vMargin;
    public static float sideBarCardWidth = sidebarWidth - 2* hMargin;

}
