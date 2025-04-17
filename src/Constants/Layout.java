package Constants;

import java.awt.Toolkit;

/**
 * Clase que define constantes de diseño y medidas proporcionales para la interfaz gráfica de la aplicación Phi6 Lab.
 *
 * <p>Contiene dimensiones relacionadas con la resolución de pantalla, márgenes, rejillas de diseño (hRect/vRect),
 * tamaños de marcos, esquinas redondeadas y componentes de navegación como la barra lateral.
 *
 * <p>Las constantes se utilizan de forma global para mantener coherencia visual entre pantallas.
 */
public class Layout {
    /**
     * Ancho de la pantalla en píxeles, detectado automáticamente por el sistema.
     */
    public static final float screenH = Toolkit.getDefaultToolkit().getScreenSize().width;

    /**
     * Altura de la pantalla en píxeles, detectada automáticamente.
     */
    public static final float screenV = Toolkit.getDefaultToolkit().getScreenSize().height;

    /**
     * Margen horizontal por defecto entre componentes.
     */
    public static float hMargin = 30;

    /**
     * Margen vertical por defecto entre componentes.
     */
    public static float vMargin = 30;

    /**
     * Margen pequeño utilizado para espaciado interno.
     */
    public static float margin = 6;

    /**
     * División horizontal de la pantalla en 5 rectángulos iguales.
     */
    public static float hRect = screenH / 5;

    /**
     * División vertical de la pantalla en 6 rectángulos iguales.
     */
    public static float vRect = screenV / 6;

    /**
     * Altura del marco superior e izquierdo, proporcional a la altura de los rectángulos.
     */
    public static float frame = (vRect / 8) * 2.5f;

    /**
     * Radio de esquina redondeada usado en tarjetas y botones.
     */
    public static float corner = 10;

    /**
     * Grosor de borde estándar en elementos destacados.
     */
    public static final int borderWeight = 2;

    // HOMESCREEN

    /**
     * Ancho total de la barra lateral (zona izquierda) de la pantalla principal.
     */
    public static float sidebarWidth = hRect - hMargin * 2;

    /**
     * Altura total de la barra lateral.
     */
    public static float sidebarHeight = 6 * vRect - 2 * vMargin;

    /**
     * Ancho disponible para una tarjeta dentro de la barra lateral.
     */
    public static float sideBarCardWidth = sidebarWidth - 2 * hMargin;
}
