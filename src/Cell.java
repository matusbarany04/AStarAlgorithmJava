import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cell {
    Type type = Type.NORMAL;
    Cell owner;
    double global;
    int local;
    boolean visited = false;
    ArrayList<Cell> neigbours;
    Point pos;

    public Cell(Point pos) {
        neigbours = new ArrayList<>();
        this.pos = pos;
        global = Integer.MAX_VALUE - 50;
        local = Integer.MAX_VALUE - 50;

    }

    public boolean addNeigbour(Cell cell) {
        if (!neigbours.isEmpty()) {
            for (Cell neigbour : neigbours) {
                if (cell.equals(neigbour) || cell.equals(this)) {
                    return false;
                }
            }
        }
       // System.out.println("adding neigbour");
        neigbours.add(cell);
        return true;
    }

    public void addNeigbours(ArrayList<Cell> cell) {
        for(int i = 0; i < cell.size();i++){
            this.addNeigbour(cell.get(i));
        }
    }
}
