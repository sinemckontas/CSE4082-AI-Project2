import java.util.ArrayList;
import java.util.List;

enum NodeType {
    MAX,
    CHANCE
}

public class AIPlayer {
    public static double expectimax(List<Game2048.Tile> myTiles, int depth,  NodeType nodeType) {
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
                        double luckyNode = expectimax(tilesCopy, depth-1, NodeType.MAX);
                        if(luckyNode!=-1) {
                            luckynode += (0.1*luckyNode);
                        }
                        else {
                        }
                        tilesCopy = new ArrayList<>(16);
                        tilesCopy.get(i).value = 2;
                        luckyNode = expectimax(tilesCopy, depth-1, NodeType.MAX);
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
            double evalL, evalR, evalU, evalD = 0;
            List<Game2048.Tile> tilesCopy = new ArrayList<>(16);
            Game2048.left();
        }

        return 0;
    }


    public static double evaluate() {
        return 0;
    }
}
