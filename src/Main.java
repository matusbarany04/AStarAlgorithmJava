import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<ArrayList<Cell>> grid = new ArrayList<>();

        int size = 11;
        ArrayList<Cell> knownCells = new ArrayList<>();
        for(int y = 0; y < size; y++){
            grid.add(new ArrayList<>());
            for(int x = 0; x < size;x++) {
                grid.get(y).add(new Cell(new Point(x,y)));
            }
        }
    }

    public ArrayList<Cell> sort(ArrayList<Cell> arrayList){
        return new ArrayList<>();
    }
}
