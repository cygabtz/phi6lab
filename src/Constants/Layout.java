package Constants;

import java.awt.Toolkit;

public class Layout {
    //-------------------------------GENERAL-------------------------------
    //Screen size
    public final static float screenH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public final static float screenV = Toolkit.getDefaultToolkit().getScreenSize().height;

    // Horizontal and vertical margins
    public static float marginH = 30, marginV = 30;

    //Guidelines rectangles (5:6)
    public static float hRect = screenH/5, vRect = screenV/6;

    //Cards Corner
    public static float corner = 10;

    //-------------------------------HOME-------------------------------
    // Side bar
    public static float sidebarWidth  = hRect - marginH*2, sidebarHeight = 6*vRect - 2*marginV;
    public static float sideBarCardWidh = sidebarWidth - 2*marginH;

    // Banner
    public static float bannerWidth  = (4*hRect) - marginH, bannerHeight = 2*vRect - 2*marginH;

    // Cards Zone
    public static float cardsZoneWidth  = 4*hRect - marginH, cardsZoneHeight = 4*vRect - marginV;

}
