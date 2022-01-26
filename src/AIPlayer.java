import java.util.ArrayList;
import java.util.List;

enum NodeType {
    MAX,
    CHANCE
}

public class AIPlayer {

    public double evaluate(Game2048 game2048, int heuristic) {
        ArrayList<Double> currentTiles = new ArrayList<>();
        for(Game2048.Tile t: game2048.myTiles){
            currentTiles.add((double)t.value);
        }

        Heuristic h = new Heuristic();
        return h.evaluateWeigths(heuristic, currentTiles);

    }
    //we send a deep copy of the current game state
    public double Expectimax2(Game2048 currentStateOfTheGame, int depth, int heuristic, NodeType nodeType, ArrayList<String> sequence){
        int numofEmptyTiles=0;

        if (depth == 0){
            return evaluate(currentStateOfTheGame, heuristic);
        }

        if (nodeType == NodeType.CHANCE){
            double chances = 0;
            // getting the number of empty tiles
            for (int i = 0; i < 16; i++) {
                if(currentStateOfTheGame.myTiles[i].isEmpty()) {
                    numofEmptyTiles++;
                }
            }

            for (int i = 0; i < 16; i++){
                if (currentStateOfTheGame.myTiles[i].isEmpty()){
                    for (int j = 0; j < 2; j++){
                        if(j == 0){
                            Game2048 clone = clone(currentStateOfTheGame);
                            clone.myTiles[i].value = 2;
                            double chance = 0.9 / numofEmptyTiles;
                            chances += chance * Expectimax2(clone, depth - 1, heuristic, NodeType.MAX, sequence);
                        }else{
                            Game2048 clone = clone(currentStateOfTheGame);
                            clone.myTiles[i].value = 4;
                            double chance = 0.1 / numofEmptyTiles;
                            chances += chance * Expectimax2(clone, depth - 1, heuristic, NodeType.MAX, sequence);
                        }
                    }
                }
            }
            return chances;
        }

        if (nodeType == NodeType.MAX){
            double heuristicResultLeft = 0, heuristicResultRight = 0, heuristicResultUp = 0, heuristicResultDown = 0;
            boolean canDoLeft = true, canDoRight = true, canDoUp = true, canDoDown = true;
            //check if a move changes the game state
            for (int k = 0; k < 4; k++){
                if (k == 0){
                    Game2048 clone = clone(currentStateOfTheGame);
                    clone.left();
                    if (currentStateOfTheGame.myTiles == clone.myTiles){
                        canDoLeft = false;
                    }
                }
                if (k == 1){
                    Game2048 clone = clone(currentStateOfTheGame);
                    clone.right();
                    if (currentStateOfTheGame.myTiles == clone.myTiles){
                        canDoRight = false;
                    }
                }
                if (k == 2){
                    Game2048 clone = clone(currentStateOfTheGame);
                    clone.up();
                    if (currentStateOfTheGame.myTiles == clone.myTiles){
                        canDoUp = false;
                    }
                }
                if (k == 3){
                    Game2048 clone = clone(currentStateOfTheGame);
                    clone.down();
                    if (currentStateOfTheGame.myTiles == clone.myTiles){
                        canDoDown = false;
                    }
                }
            }


            for(int i = 0; i < 4; i ++){
                if(i == 0 && canDoLeft){
                    Game2048 clone = clone(currentStateOfTheGame);
                    clone.left();
                    heuristicResultLeft = Expectimax2(clone, depth - 1, heuristic, NodeType.CHANCE, sequence);
                }else if(i == 1 && canDoRight){
                    Game2048 clone = clone(currentStateOfTheGame);
                    clone.right();
                    heuristicResultRight = Expectimax2(clone, depth - 1, heuristic, NodeType.CHANCE, sequence);
                }else if(i == 2 && canDoUp){
                    Game2048 clone = clone(currentStateOfTheGame);
                    clone.up();
                    heuristicResultUp = Expectimax2(clone, depth - 1, heuristic, NodeType.CHANCE, sequence);
                }else if(i == 3 && canDoDown){
                    Game2048 clone = clone(currentStateOfTheGame);
                    clone.down();
                    heuristicResultDown = Expectimax2(clone, depth - 1, heuristic, NodeType.CHANCE, sequence);
                }
            }

            //find the max value
            double maximumHeuristicResult = Math.max(Math.max(heuristicResultLeft,heuristicResultRight),Math.max(heuristicResultUp,heuristicResultDown));
            //choosing left, right, up, down
            if (maximumHeuristicResult == heuristicResultLeft){
                String left = "left";
                sequence.add(left);
            }else if (maximumHeuristicResult == heuristicResultRight){
                String right = "right";
                sequence.add(right);
            }else if (maximumHeuristicResult == heuristicResultUp){
                String up = "up";
                sequence.add(up);
            }else{
                String down = "down";
                sequence.add(down);
            }
            return maximumHeuristicResult;
        }
        return 0;
    }
    public Game2048 clone(Game2048 realObject){
        Game2048 cloneObject = new Game2048();
        cloneObject.myLose = realObject.myLose;
        cloneObject.myWin = realObject.myWin;
        cloneObject.myTiles = realObject.myTiles;
        cloneObject.myScore = realObject.myScore;

        return cloneObject;
    }
}
