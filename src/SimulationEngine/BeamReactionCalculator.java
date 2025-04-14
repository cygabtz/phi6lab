package SimulationEngine;

import org.matheclipse.core.eval.ExprEvaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static SimulationEngine.Elements.*;
import static SimulationEngine.Elements.SUPPORT_TYPE.*;

/**
 * Clase encargada de calcular las reacciones estáticas de una viga en equilibrio.
 *
 * <p>Utiliza un motor simbólico basado en Symja ({@link org.matheclipse.core.eval.ExprEvaluator})
 * para resolver ecuaciones simbólicas de equilibrio estático.
 *
 * <p>Las reacciones se calculan en función de las fuerzas externas, momentos y apoyos definidos,
 * devolviendo un arreglo con las componentes de reacción en los extremos A y B.
 *
 * <p>Esta clase admite configuraciones con:
 * <ul>
 *   <li>Un solo apoyo (tipo FIXED).</li>
 *   <li>Dos apoyos (combinaciones válidas de PIN y ROLLER).</li>
 * </ul>
 *
 * <p>No soporta, por ahora, más de dos apoyos ni otras combinaciones con grados de indeterminación.
 */
public class BeamReactionCalculator {

    private static ExprEvaluator engine = new ExprEvaluator();

    /**
     * Calcula las reacciones estáticas de la viga a partir de fuerzas, momentos y apoyos definidos.
     *
     * <p>Si se detecta solo un apoyo, se asume que es de tipo FIXED y se calculan las reacciones directamente.
     * Si hay dos apoyos, se generan ecuaciones de equilibrio para resolver simbólicamente las incógnitas.
     *
     * <p>Las componentes devueltas están en el orden:
     * {@code [RAx, RAy, MA, RBx, RBy, MB]}.
     *
     * @param forces   lista de fuerzas externas aplicadas sobre la viga.
     * @param moments  lista de momentos aplicados.
     * @param supports lista de apoyos con su tipo y posición.
     * @return arreglo con las reacciones estáticas calculadas.
     */
    public static double[] calculateReactions(List<Force> forces, List<Moment> moments, List<Support> supports) {
        System.out.println("Calculando reacciones...");

        // Inicializar variables de sumatorias
        double sumFx = 0.0;
        double sumFy = 0.0;
        double sumMoment = 0.0;

        // Calcular las sumatorias de fuerzas y momentos
        for (Force force : forces) {
            sumFx += force.getHorizontalComponent();
            sumFy += force.getVerticalComponent();
            sumMoment += force.getVerticalComponent() * force.position; // Momento respecto al origen
            System.out.println("Calculando sumatorias por valor de: ");
            System.out.println("\t\tsumFx: " + sumFx);
            System.out.println("\t\tsumFx: " + sumFx);
            System.out.println("\t\tsumMoment: " + sumMoment);
        }
        for (Moment moment : moments) sumMoment += moment.magnitude;

        // Array de resultados. Siempre es [RAx, RAy, MA, RBx, RBy, MB]
        double RAx = 0, RAy = 0, MA = 0, RBx = 0, RBy = 0, MB = 0;

        if (supports.size() == 1) {
            //Casos sencillos que no requieren computar ecuaciones
            Support support = supports.get(0);
            SUPPORT_TYPE type = support.type;
            double pos = support.position;

            if (type == FIXED) {
                // Caso A=FIXED
                RAx = -sumFx;
                RAy = -sumFy;
                MA = -sumMoment - pos * RAy;
            } else {
                System.out.println("Combinación no válida: si hay un apoyo este debe ser únicamente empotrado");
            }
        } else if (supports.size() == 2) {
            Support supportA = supports.get(0);
            Support supportB = supports.get(1);
            SUPPORT_TYPE typeA = supportA.type;
            SUPPORT_TYPE typeB = supportB.type;

            // Variables conocidas
            double posA = supportA.position;
            double posB = supportB.position;

            // Asignar valores conocidos al motor simbólico
            engine.eval("posA = " + posA);
            engine.eval("posB = " + posB);
            engine.eval("sumFx = " + sumFx);
            engine.eval("sumFy = " + sumFy);
            engine.eval("sumMoment = " + sumMoment);

            //Casos donde se requiere computar ecuaciones
            //Se podría extender a más casos en un futuro
            if (typeA == PIN && typeB == ROLLER) {
                RAx = -sumFx;

                String eq1 = "sumFy + RAy + RBy == 0";
                String eq2 = "sumMoment + posB*RBy == 0";

                String solution = engine.eval(
                        "Solve({" + eq1 + ", " + eq2 + "}, {RAy, RBy})"
                ).toString();

                double[] reactions = reformat(solution);
                RAy = reactions[0];
                RBy = reactions[1];

                //Funciona

            } else if (typeA == ROLLER && typeB == PIN) {
                RBx = -sumFx;

                String eq1 = "sumFy + RAy + RBy == 0";
                String eq2 = "sumMoment + posB*RBy == 0";

                String solution = engine.eval(
                        "Solve({" + eq1 + ", " + eq2 + "}, {RAy, RBy})"
                ).toString();

                double[] reactions = reformat(solution);
                RAy = reactions[0];
                RBy = reactions[1];

                //Funciona

            } else if (typeA == ROLLER && typeB == ROLLER) {

                String eq1 = "sumFy + RAy + RBy == 0";
                String eq2 = "sumMoment + posA*RAy + posB*RBy == 0";

                String solution = engine.eval(
                        "Solve({" + eq1 + ", " + eq2 + "}, {RAy, RBy})"
                ).toString();
                System.out.println(solution);

                double[] reactions = reformat(solution);
                RAy = reactions[0];
                RBy = reactions[1];

                //Funciona
            } else {
                System.out.println("Combinación no válida");
            }
        } else System.out.println("No se pueden calcular vigas con más de dos soportes");

        System.out.println("Imprimiendo resultados");
        return new double[]{RAx, RAy, MA, RBx, RBy, MB};
    }

    /**
     * Reformatea el resultado simbólico devuelto por Symja en un arreglo de {@code double}.
     *
     * <p>Extrae los valores numéricos de una cadena con el formato:
     * {@code {RAy -> 40.0, RBy -> 60.0}} y los convierte en un arreglo: {@code [40.0, 60.0]}.
     *
     * @param result cadena con el resultado simbólico.
     * @return arreglo de valores numéricos extraídos.
     */
    private static double[] reformat(String result) {
        result = result.replaceAll("[{}\\s]", ""); // Eliminar {}, espacios y tabulaciones

        // Dividir el String en pares variable->valor
        String[] pares = result.split(",");

        // Lista para almacenar los resultados
        ArrayList<Double> valores = new ArrayList<>();

        for (String par : pares) {
            // Dividir la pareja en variable y valor
            String[] partes = par.split("->");
            if (partes.length == 2) { // Asegurarse de que la pareja esté bien formada
                try {
                    // Convertir el valor a double y agregarlo a la lista
                    valores.add(Double.parseDouble(partes[1]));
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir el valor: " + partes[1]);
                }
            }
        }

        // Convertir la lista a un array de doubles
        double[] resultados = new double[valores.size()];
        for (int i = 0; i < valores.size(); i++) {
            resultados[i] = valores.get(i);
        }

        return resultados;
    }

    public static void main(String[] args) {
        engine = new ExprEvaluator();

        Force f1 = new Force(200, 1.5, true);
        Force f2 = new Force(-200, 4.5, false);

        Support sA = new Support("A", PIN, 0);
        Support sB = new Support("B", ROLLER, 3.0);

        List<Force> forces = new ArrayList<>();
        forces.add(f1);
        forces.add(f2);

        List<Support> supports;
        supports = new ArrayList<>();
        supports.add(sA);
        supports.add(sB);

        List<Moment> moments;
        moments = new ArrayList<>();

        System.out.println(Arrays.toString(calculateReactions(forces, moments, supports)));
    }


}