import java.util.ArrayList;
import java.util.List;

enum NodeType {
    MAX,
    CHANCE
}

public class AIPlayer {
    Game2048 realGame;
    public AIPlayer(Game2048 game){
        realGame = game;
    }

    public double expectimax(Game2048 currentStateOfTheGame, List<Game2048.Tile> myTiles, int depth, NodeType nodeType, int heuristic) {
        int numofEmptyTiles=0;
        if (depth == 0) {
            return evaluate();
        }

        for (int y=0; y<16; y++) {
            if(myTiles.get(y).isEmpty()) {
                numofEmptyTiles++;
            }
        }

        if(nodeType == NodeType.CHANCE) {
            double luckynode = 0;
            for(int i=0; i<16; i++){
                if (!myTiles.get(i).isEmpty()) {
                    continue;
                }
                else {
                    List<Game2048.Tile> tilesCopy = new ArrayList<>(16);
                    for (Game2048.Tile t: myTiles) {
                        tilesCopy.add(new Game2048.Tile(t.value));
                        tilesCopy.get(i).value = 4;
                        double luckyNode = expectimax(this.realGame, tilesCopy, depth-1, NodeType.MAX, heuristic);
                        if(luckyNode!=-1) {
                            luckynode += (0.1*luckyNode);
                        }
                        else {
                        }
                        tilesCopy = new ArrayList<>(16);
                        tilesCopy.get(i).value = 2;
                        luckyNode = expectimax(this.realGame, tilesCopy, depth-1, NodeType.MAX, heuristic);
                        if(luckyNode!=-1) {
                            luckynode += (0.9*luckyNode);
                        }
                        else {
                        }
                    }
                    luckynode/= numofEmptyTiles;
                    return luckynode;
                }
            }

            return evaluate();
        }

        if(nodeType == NodeType.MAX) {
            double evalL = 0, evalR = 0, evalU = 0, evalD = 0, luckynode = 0;

            for(int i = 0; i < 4; i ++){
                Game2048 game2048Copy = currentStateOfTheGame;
                switch (i){
                    case 0:
                        game2048Copy.left();
                        evalL = evaluate(game2048Copy, heuristic);
                    case 1:
                        game2048Copy.right();
                        evalR = evaluate(game2048Copy, heuristic);
                    case 2:
                        game2048Copy.up();
                        evalU = evaluate(game2048Copy, heuristic);
                    case 3:
                        game2048Copy.down();
                        evalD = evaluate(game2048Copy, heuristic);
                }
            }
            // find
            double choice1 = Math.max(evalL, evalR);
            double choice2 = Math.max(evalU, evalD);
            double probableChoice = Math.max(choice1, choice2);

            if (probableChoice == evalL){
                currentStateOfTheGame.left();
            }else if (probableChoice == evalR){
                currentStateOfTheGame.right();
            }else if(probableChoice == evalU){
                currentStateOfTheGame.up();
            }else if(probableChoice == evalD){
                currentStateOfTheGame.down();
            }

            double luckyNode = expectimax(currentStateOfTheGame, myTiles, depth-1, nodeType.CHANCE, heuristic);

            if(luckyNode>luckynode) {
                luckynode = luckyNode;
            }
            return luckynode;
        }
        //
        return 0;
    }


    public double evaluate(Game2048 game2048, int heuristic) {
        ArrayList<Double> currentTiles = new ArrayList<>();
        for(Game2048.Tile t: game2048.myTiles){
            currentTiles.add((double)t.value);
        }

        Heuristic h = new Heuristic();
        return h.evaluateWeigths(heuristic, currentTiles);

    }
}
