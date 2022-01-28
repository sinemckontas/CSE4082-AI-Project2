import java.util.ArrayList;

public class AIPlayer2 {


    enum NodeType {
        MAX,
        CHANCE
    }

    // Initializing Nodes to null
     public Node newNode(Game2048 gameCopy)
    {
        Node temp = new Node();
        temp.value = 0;
        temp.childrens = new ArrayList<>();
        temp.sequence = new ArrayList<>();
        temp.gameCopy = gameCopy;
        return temp;
    }

    // Getting expectimax
    public double Expectimax(Node node, int heuristic, int depth, NodeType nodeType)
    {
        // Condition for Terminal node
        if (depth == 0) {
            node.value = evaluate(node.gameCopy, heuristic);
            return node.value;
        }

        // Maximizer node. Chooses the max from the
        // left and right sub-trees
        if (nodeType == NodeType.MAX) {
            double heuristicResultLeft = 0, heuristicResultRight = 0, heuristicResultUp = 0, heuristicResultDown = 0, maxValue = 0;
            boolean canDoLeft = true, canDoRight = true, canDoUp = true, canDoDown = true;

            for (int k = 0; k < 4; k++){
                if (k == 0){
                    Game2048 clone = clone2(node.gameCopy);
                    clone.left();
                    for(int z = 0; z < node.gameCopy.myTiles.length; z++){
                        if (node.gameCopy.myTiles[z].value != clone.myTiles[z].value){
                            canDoLeft = true;
                            break;
                        }else {
                            canDoLeft = false;
                        }
                    }
                }
                if (k == 1){
                    Game2048 clone = clone2(node.gameCopy);
                    clone.right();
                    for(int z = 0; z < node.gameCopy.myTiles.length; z++){
                        if (node.gameCopy.myTiles[z].value != clone.myTiles[z].value){
                            canDoRight = true;
                            break;
                        }else {
                            canDoRight = false;
                        }
                    }
                }
                if (k == 2){
                    Game2048 clone = clone2(node.gameCopy);
                    clone.up();
                    for(int z = 0; z < node.gameCopy.myTiles.length; z++){
                        if (node.gameCopy.myTiles[z].value != clone.myTiles[z].value){
                            canDoUp = true;
                            break;
                        }else {
                            canDoUp = false;
                        }
                    }

                }
                if (k == 3){
                    Game2048 clone = clone2(node.gameCopy);
                    clone.down();
                    for(int z = 0; z < node.gameCopy.myTiles.length; z++){
                        if (node.gameCopy.myTiles[z].value != clone.myTiles[z].value){
                            canDoDown = true;
                            break;
                        }else {
                            canDoDown = false;
                        }
                    }
                }
            }

            for(int i = 0; i < 4; i ++){
                if(i == 0 && canDoLeft){
                    Game2048 clone = clone2(node.gameCopy);
                    clone.left();

                    Node childNode = newNode(clone);
                    node.childrens.add(childNode);
                    //childNode.sequence.add("left");
                    heuristicResultLeft = Expectimax(childNode, heuristic, depth - 1, NodeType.CHANCE);


                }else if(i == 1 && canDoRight){
                    Game2048 clone = clone2(node.gameCopy);
                    clone.right();

                    Node childNode = newNode(clone);
                    //childNode.sequence.add("right");
                    node.childrens.add(childNode);
                    heuristicResultRight = Expectimax(childNode, heuristic, depth - 1, NodeType.CHANCE);


                }else if(i == 2 && canDoUp){
                    Game2048 clone = clone2(node.gameCopy);
                    clone.up();
                    Node childNode = newNode(clone);
                    //childNode.sequence.add("up");
                    node.childrens.add(childNode);
                    heuristicResultUp = Expectimax(childNode,  heuristic, depth - 1, NodeType.CHANCE);


                }else if(i == 3 && canDoDown){
                    Game2048 clone = clone2(node.gameCopy);
                    clone.down();
                    Node childNode = newNode(clone);
                    //childNode.sequence.add("down");
                    node.childrens.add(childNode);
                    heuristicResultDown = Expectimax(childNode, heuristic, depth - 1, NodeType.CHANCE);

                }

            }

            //choosing left, right, up, down
            double maximumHeuristicResult = Math.max(Math.max(heuristicResultLeft,heuristicResultRight), Math.max(heuristicResultUp,heuristicResultDown));
            if (maximumHeuristicResult == heuristicResultLeft && canDoLeft){
                String left = "left";
                node.sequence.add(left);
                node.value = heuristicResultLeft;
            }else if (maximumHeuristicResult == heuristicResultRight && canDoRight){
                String right = "right";
                node.sequence.add(right);
                node.value = heuristicResultRight;
            }else if (maximumHeuristicResult == heuristicResultUp && canDoUp){
                String up = "up";
                node.sequence.add(up);
                node.value = heuristicResultUp;
            }else if(maximumHeuristicResult == heuristicResultDown && canDoDown){
                String down = "down";
                node.sequence.add(down);
                node.value = heuristicResultDown;
            }

            for (Node child:node.childrens) {
                if (child.value == node.value){
                    node.sequence.addAll(child.sequence);
                }
            }

            return maximumHeuristicResult;
            // Chance node. Returns the average of
            // the left and right sub-trees
        }else if (nodeType == NodeType.CHANCE){
            int numofEmptyTiles=0;
            double chances = 0;
            for (int i = 0; i < 16; i++) {
                if(node.gameCopy.myTiles[i].isEmpty()) {
                    numofEmptyTiles++;
                }
            }
            if (numofEmptyTiles != 0){
                for (int i = 0; i < 16; i++){
                    if (node.gameCopy.myTiles[i].isEmpty()){
                        for (int j = 0; j < 2; j++){
                            if(j == 0){
                                Game2048 clone = clone2(node.gameCopy);
                                clone.myTiles[i].value = 2;
                                Node childNode = newNode(clone);
                                node.childrens.add(childNode);

                                double chance = 0.9 / numofEmptyTiles;
                                double cost = Expectimax(childNode, heuristic, depth - 1, NodeType.MAX);
                                chances += chance * cost;

                            }else{
                                Game2048 clone = clone2(node.gameCopy);
                                clone.myTiles[i].value = 4;
                                Node childNode = newNode(clone);
                                node.childrens.add(childNode);

                                double chance = 0.1 / numofEmptyTiles;
                                double cost = Expectimax(childNode, heuristic, depth - 1, NodeType.MAX);
                                chances += chance * cost;

                            }
                        }
                    }
                }
                double maxCost = 0;
                ArrayList<String> sequence = new ArrayList<>();
                for (int k = 0; k < node.childrens.size(); k++){
                    if (node.childrens.get(k).value > maxCost){
                        maxCost = node.childrens.get(k).value;
                        sequence = node.childrens.get(k).sequence;
                    }
                }
                node.sequence.addAll(sequence);
                node.value = chances;
                return chances;
            }else{
                node.value = evaluate(node.gameCopy, heuristic);
                return node.value;
            }

        }
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

    public Game2048 clone2(Game2048 realObject){
        Game2048 cloneObject = new Game2048();
        cloneObject.myLose = realObject.myLose;
        cloneObject.myWin = realObject.myWin;
        cloneObject.myTiles = realObject.myTiles;
        cloneObject.myScore = realObject.myScore;

        return cloneObject;
    }
}
