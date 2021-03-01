package astar;

import java.util.ArrayList;

public class PathCell {
    Type type = Type.NORMAL;
    PathCell owner;
    double global;
    int local;
    boolean visited = false;
    ArrayList<PathCell> neigbours;
    Point pos;

    public PathCell(Point pos) {
        neigbours = new ArrayList<>();
        this.pos = pos;
        global = Integer.MAX_VALUE - 50;
        local = Integer.MAX_VALUE - 50;

    }

    public boolean addNeigbour(PathCell pathCell) {
        if (!neigbours.isEmpty()) {
            for (PathCell neigbour : neigbours) {
                if (pathCell.equals(neigbour) || pathCell.equals(this)) {
                    return false;
                }
            }
        }
       // System.out.println("adding neigbour");
        neigbours.add(pathCell);
        return true;
    }

    public void addNeigbours(ArrayList<PathCell> pathCell) {
        for(int i = 0; i < pathCell.size(); i++){
            this.addNeigbour(pathCell.get(i));
        }
    }
}
