import java.util.ArrayList;
import java.util.List;

enum NodeType {
    MAX,
    CHANCE
}

public class AIPlayer {

    public static double expectimax(List<Game2048.Tile> myTiles, int depth,  NodeType nodeType) {
        if (depth == 0) {
            return evaluate();
        }

        if(nodeType == NodeType.CHANCE) {
            double luckynode;
            for(int i=0; i<16; i++){
                if (!myTiles.get(i).isEmpty()) {
                    continue;
                }
                else {
                    List<Game2048.Tile> tilesCopy = new ArrayList<>(16);
                    for (Game2048.Tile t: myTiles) {
                        tilesCopy.add(new Game2048.Tile(t.value));
                    }
                    tilesCopy.get(i).value = 4;
                    expectimax(tilesCopy, depth-1, NodeType.MAX);
                    

                }
            }

            return evaluate();
        }

        return 0;
    }

    public static double evaluate() {
        return 0;
    }
}
