import java.util.ArrayList;
import java.util.List;

public class Heuristic {

    double[][] h1 = {
            {1  , 0.8, 0.6, 0.4},
            {0.8, 0.8, 0.6, 0.4},
            {0.6, 0.6, 0.6, 0.4},
            {0.4, 0.4, 0.4, 0.4}
    };

    double[][] h2 = {
            {0.4, 0.6, 0.8, 1},
            {0.4, 0.6, 0.8, 0.8},
            {0.4, 0.6, 0.6, 0.6},
            {0.4, 0.4, 0.4, 0.4}
    };

    double[][] h3 = {
            {1  , 0.8, 0.6, 0.4},
            {0.8, 0.8, 0.6, 0.4},
            {0.6, 0.6, 0.6, 0.4},
            {0.4, 0.4, 0.4, 0.4}
    };

    double[][] h4 = {
            {1  , 0.8, 0.6, 0.4},
            {0.8, 0.8, 0.6, 0.4},
            {0.6, 0.6, 0.6, 0.4},
            {0.4, 0.4, 0.4, 0.4}
    };

    public double evaluateWeigths(int heuristicCode, ArrayList<Double> currentTiles){

        return switch (heuristicCode) {
            case 1 -> calculatePossibilities(currentTiles, h1);
            case 2 -> calculatePossibilities(currentTiles, h2);
            case 3 -> calculatePossibilities(currentTiles, h3);
            case 4 -> calculatePossibilities(currentTiles, h4);
            default -> 0;
        };
    }

    public double calculatePossibilities(ArrayList<Double> currentTiles, double[][] heuristic){
        double result = 0;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                result += heuristic[i][j] * currentTiles.get(i*j + j);
            }
        }
        return result;
    }


}
