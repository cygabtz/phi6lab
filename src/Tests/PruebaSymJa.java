package Tests;

import org.matheclipse.core.eval.ExprEvaluator;

import java.util.ArrayList;
import java.util.Arrays;

public class PruebaSymJa {
    public static void main(String[] args) {
        testRollerPin();
    }

    private static void testRollerRoller(){
        ExprEvaluator engine = new ExprEvaluator();

        // Variables conocidas
        double posA = 0.0;
        double posB = 20.0;
        double sumFx = 0.0;
        double sumFy = -1440.0*9.8;
        double sumMoment = -1440.0*9.8*10;

        // Asignar valores conocidos al motor simbólico
        engine.eval("posA = " + posA);
        engine.eval("posB = " + posB);
        engine.eval("sumFx = " + sumFx);
        engine.eval("sumFy = " + sumFy);
        engine.eval("sumMoment = " + sumMoment);

        // Caso A=ROLLER y B=ROLLER
        String eq1 = "sumFy + RAy + RBy == 0";
        String eq2 = "sumMoment + posA*RAy + posB*RBy == 0";

        String solution = engine.eval(
                "Solve({" + eq1 + ", " + eq2 + "}, {RAy, RBy})"
        ).toString();
        System.out.println(solution);

        double [] reactions = reformat(solution);

        System.out.println("Soluciones formatadas: " + Arrays.toString(reactions));

        //Funciona
    }

    private static void testRollerPin() {
        double RBx, RAy, RBy;

        ExprEvaluator engine = new ExprEvaluator();

        // Variables conocidas
        double posA = 0;
        double posB = 6;
        double sumFx = 0.0;
        double sumFy = -10;
        double sumMoment = -4*10;

        // Asignar valores conocidos al motor simbólico
        engine.eval("posA = " + posA);
        engine.eval("posB = " + posB);
        engine.eval("sumFx = " + sumFx);
        engine.eval("sumFy = " + sumFy);
        engine.eval("sumMoment = " + sumMoment);

        // Caso A=ROLLER, B=PIN
        RBx = -sumFx;
        String eq1 = "sumFy + RAy + RBy == 0";
        String eq2 = "sumMoment + posB*RBy == 0";

        String solution = engine.eval(
                "Solve({" + eq1 + ", " + eq2 + "}, {RAy, RBy})"
        ).toString();

        double [] reactions = reformat(solution);
        RAy = reactions[0];
        RBy = reactions[1];

        System.out.println("RBx: " + RBx + " RAy: " + RAy + " RBy: " + RBy);
    }

    private static void testPinRoller() {
        double RAx, RAy, RBy;

        ExprEvaluator engine = new ExprEvaluator();

        // Variables conocidas
        double posA = 2.0;
        double posB = 7;
        double sumFx = 0.0;
        double sumFy = -5000 -10000;
        double sumMoment = 5000*2 - 10000*8;

        // Asignar valores conocidos al motor simbólico
        engine.eval("posA = " + posA);
        engine.eval("posB = " + posB);
        engine.eval("sumFx = " + sumFx);
        engine.eval("sumFy = " + sumFy);
        engine.eval("sumMoment = " + sumMoment);

        // Caso A=PIN, B=ROLLER
        RAx = -sumFx;
        String eq1 = "sumFy + RAy + RBy == 0";
        String eq2 = "sumMoment + posB*RBy == 0";

        String solution = engine.eval(
                "Solve({" + eq1 + ", " + eq2 + "}, {RAy, RBy})"
        ).toString();

        double [] reactions = reformat(solution);
        RAy = reactions[0];
        RBy = reactions[1];

        System.out.println("RAx: " + RAx + " RAy: " + RAy + " RBy: " + RBy);

        //Funciona
    }

    private static double [] reformat(String result) {
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
}
