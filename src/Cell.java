import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cell {
    Cell owner;
    float global;
    float local;
    ArrayList<Cell> neigbours;
    Point pos;

    public Cell(Point pos) {
        neigbours = new ArrayList<>();
        this.pos = pos;
        global = -Float.MAX_VALUE;
        local = -Float.MAX_VALUE;
    }

    public boolean addNeigbour(Cell cell) {
        if (!neigbours.isEmpty()) {
            for (Cell neigbour : neigbours) {
                if (cell.equals(neigbour) || cell.equals(this)) {
                    return false;
                }
            }
        }
        neigbours.add(cell);
        return true;
    }

    public void addNeigbours(ArrayList<Cell> cell) {
        for (Cell newCell : cell) {
            this.addNeigbour(newCell);
        }
    }
}
