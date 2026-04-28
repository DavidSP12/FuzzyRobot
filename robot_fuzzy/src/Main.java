import java.nio.file.Path;
import java.util.*;

/**
 * Robot Movil - Logica Difusa (Mamdani)
 *
 * ENTRADAS:
 *   distancia_frente    = min(sensor_izq, sensor_der)   [0..300 cm]
 *   diferencia_sensores = sensor_der - sensor_izq       [-300..300 cm]
 *                          > 0 => mas espacio a la DERECHA
 *                          < 0 => mas espacio a la IZQUIERDA
 * SALIDA FUZZY:
 *   velocidad_giro                                      [-30..30 grados/s]
 *                          > 0 => girar DERECHA
 *                          < 0 => girar IZQUIERDA
 *
 * NOTA: La velocidad lineal maxima del robot (3 m/s) es un parametro
 *       fisico externo, NO es variable de salida de este sistema difuso.
 *
 * Compilar:
 *   javac -d out src/*.java
 *
 * Ejecutar (caso por defecto):
 *   java -cp out Main
 *
 * Ejecutar con valores propios:
 *   java -cp out Main data/variables_robot.txt data/rules_robot.txt distancia_frente=20 diferencia_sensores=120
 */
public class Main {
    public static void main(String[] args) {
        try {
            Path vp = args.length >= 1 ? Path.of(args[0]) : Path.of("data/variables_robot.txt");
            Path rp = args.length >= 2 ? Path.of(args[1]) : Path.of("data/rules_robot.txt");
            Map<String, Double> inputs = new LinkedHashMap<>();
            if (args.length >= 3) {
                for (int i = 2; i < args.length; i++) {
                    String[] p = args[i].split("=");
                    if (p.length != 2) throw new IllegalArgumentException("Use nombre=valor");
                    inputs.put(p[0].trim(), Double.parseDouble(p[1].trim()));
                }
            } else {
                // Prueba: obstaculo muy cerca (20 cm), mas espacio a la derecha (120 cm)
                inputs.put("distancia_frente", 20.0);
                inputs.put("diferencia_sensores", 120.0);
            }

            FuzzySystemLoader loader = new FuzzySystemLoader();
            FuzzySystem system = loader.loadSystem(vp, rp);

            System.out.println("========== ROBOT MOVIL - INFERENCIA DIFUSA ==========");
            System.out.println("Variables : " + vp.toAbsolutePath());
            System.out.println("Reglas    : " + rp.toAbsolutePath());
            System.out.println();
            System.out.println("--- Variables ---");
            System.out.println("  distancia_frente    [0..300 cm]       = min(sensor_izq, sensor_der)");
            System.out.println("  diferencia_sensores [-300..300 cm]    = sensor_der - sensor_izq");
            System.out.println("  velocidad_giro      [-30..30 deg/s]   << SALIDA FUZZY >>");
            System.out.println("  velocidad_robot     [0..3 m/s]        (parametro fisico externo)");
            System.out.println();
            System.out.println(system.describeVariables());
            System.out.println(system.getKnowledgeBase().describe());

            MamdaniInferenceEngine engine = new MamdaniInferenceEngine(system);
            InferenceResult result = engine.infer(inputs);

            System.out.println(result.getTrace());
            System.out.println("Grados agregados de salida:");
            for (Map.Entry<String, Double> e : result.getAggregatedOutputDegrees().entrySet())
                System.out.printf("  %-20s -> %.4f%n", e.getKey(), e.getValue());

            double v = result.getCrispOutput();
            System.out.printf("%n>>> velocidad_giro defuzzificada = %.4f grados/s%n", v);
            if (!Double.isNaN(v)) {
                if      (v >  0.5) System.out.printf(">>> Accion: GIRAR A LA DERECHA   (%.4f deg/s)%n", v);
                else if (v < -0.5) System.out.printf(">>> Accion: GIRAR A LA IZQUIERDA (%.4f deg/s)%n", Math.abs(v));
                else               System.out.println(">>> Accion: AVANZAR RECTO");
            }
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
