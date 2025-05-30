import java.util.ArrayList;

public class VectorMethods {
    public static Double[] getVectorFromRows(ArrayList<Double> point1, ArrayList<Double> point2) {
        Double[] vector = new Double[2];

        vector[0] = point2.get(0) - point1.get(0);
        vector[1] = point2.get(1) - point1.get(1);
        return vector;
    }

    public static double getMagnitude(Double[] vector) {
        return Math.sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2));
    }

    public static double dotProduct(Double[] vector1, Double[] vector2) {
        double total = 0;
        for (int i = 0; i < vector1.length; i++) {
            total += vector1[i] * vector2[i];
        }
        return total;
    }

    public static double getAngle(Double[] vector1, Double[] vector2) {
        return Math.acos((dotProduct(vector1, vector2))/(getMagnitude(vector1) * getMagnitude(vector2)));
    }


}
