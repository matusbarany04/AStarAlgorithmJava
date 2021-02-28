import java.io.File;
import java.io.FileNotFoundException;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Main {
    Cell start;
    Cell finish;
    Point startPos;
    Point finsihPos;
    int size = 15;

    public static void main(String[] args) throws FileNotFoundException {
        new Main().start();
    }

    public void start() throws FileNotFoundException {
        ArrayList<ArrayList<Cell>> grid = new ArrayList<>();

        ArrayList<Cell> knownCells = new ArrayList<>();

        startPos =new Point(0,0);
        finsihPos =new Point(10,10);
        Scanner scanner = new Scanner(new File("grid.txt"));

        int lineNumber = 1;
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            System.out.println("line " + lineNumber + " :" + line);
            lineNumber++;
        }

        //make the grid
        for (int y = 0; y < size; y++) {
            grid.add(new ArrayList<>());
            for (int x = 0; x < size; x++) {
                Cell c = new Cell(new Point(x, y));
                grid.get(y).add(c);
                this.addStartAndFinish(x, y, c,startPos,finsihPos);
            }
        }


        start.type = Type.START;
        finish.type = Type.FINISH;

        //add neigbours
        grid = addNeigbours(grid);

        //do a a star algorithm
        // while ...
        knownCells.add(start);
        start.local = 0;
        start.global = Math.sqrt(Math.pow(Math.abs(finish.pos.x - start.pos.x), 2) + Math.pow(Math.abs(finish.pos.y - start.pos.y),2));


        whileLoop(knownCells,grid);

        trackThePath(grid,knownCells);
    }

    public boolean whileLoop(ArrayList<Cell> knownCells,  ArrayList<ArrayList<Cell>> grid){
        while (!knownCells.isEmpty()) {
            knownCells.get(0).visited = true;
            knownCells.get(0).type = Type.VISITED;
            Cell testingCell = knownCells.get(0);
            System.out.println("Hi im testing cell at " + testingCell.pos.x + " " + testingCell.pos.y);
            //to see how to goes
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }

            for (int i = 0; i < testingCell.neigbours.size(); i++) {

                Cell neigbour = testingCell.neigbours.get(i);

                if(!neigbour.type.equals(Type.WALL)){
                    if (testingCell.local + 1 < neigbour.local) {
                        neigbour.owner = testingCell;
                        neigbour.local = testingCell.local + 1;
                        neigbour.global = neigbour.local + Math.sqrt(Math.pow(Math.abs(finish.pos.x - neigbour.pos.x), 2) + Math.pow(Math.abs(finish.pos.y - neigbour.pos.y),2));
                        knownCells.add(neigbour);
                    }
                }


                if (neigbour.equals(finish)) {
                    System.out.println("hey it's me");
                    return true;
                }
            }


            knownCells.remove(0);

            sort(knownCells);


            render(grid);
        }

        return  false;
    }

    public void addStartAndFinish(int x, int y, Cell c, Point startPos, Point finishPos) {
        if (x == startPos.x && y == startPos.y) {
            start = c;
        } else if (x == size - finishPos.x && y == finishPos.y) {
            finish = c;
        }
    }

    public ArrayList<ArrayList<Cell>> addNeigbours(ArrayList<ArrayList<Cell>> grid) {
        for (ArrayList<Cell> lines : grid) {
            for (Cell cell : lines) {
                //up
                cell = searchNeigbour(grid, cell, -1, 0);
                // down
                cell = searchNeigbour(grid, cell, +1, 0);
                // right
                cell = searchNeigbour(grid, cell, 0, +1);
                // left
                cell = searchNeigbour(grid, cell, 0, -1);

                grid.get(cell.pos.y).set(cell.pos.x, cell);

            }
        }
        return grid;
    }

    public Cell searchNeigbour(ArrayList<ArrayList<Cell>> grid, Cell cell, int fromX, int fromY) {
        try {
            Cell neigbour = grid.get(cell.pos.y + fromY).get(cell.pos.x + fromX);
            if (neigbour.type != Type.WALL) {
                cell.addNeigbour(neigbour);
            }
        } catch (IndexOutOfBoundsException e) {
        }
        return cell;
    }

    public ArrayList<Cell> sort(ArrayList<Cell> arrayList) {
        Collections.sort(arrayList, new Comparator<Cell>() {
            @Override
            public int compare(Cell o1, Cell o2) {
                return Double.compare(o1.global, o2.global);
            }
        });
        return arrayList;
    }

    public void render(ArrayList<ArrayList<Cell>> grid) {
        for (ArrayList<Cell> arrayList : grid) {

            for (Cell cell : arrayList) {
                char a = ' ';
                switch (cell.type) {
                    case WALL:
                        a = '#';
                        break;
                    case START:
                        a = '0';
                        break;
                    case FINISH:
                        a = 'W';
                        break;
                    case VISITED:
                        a = '*';
                        break;
                    default:
                        a = '.';
                        break;
                }

                System.out.print(a + " ");
            }
            System.out.println();
        }

    }

    public void trackThePath(ArrayList<ArrayList<Cell>> grid, ArrayList<Cell> knownCells) {
        Cell cell = finish.owner;
        System.out.println(cell.pos.x + " "  + cell.pos.y);
        do {
            cell = cell.owner;
            System.out.println(cell.pos.x + " "  + cell.pos.y);
        }while(cell.owner != null);


        for (ArrayList<Cell> arrayList : grid) {

            for (Cell c : arrayList) {
                char a = ' ';
                switch (c.type) {
                    case WALL:
                        a = '#';
                        break;
                    case START:
                        a = '0';
                        break;
                    case FINISH:
                        a = 'W';
                        break;
                    case VISITED:
                        a = '*';
                        break;
                    default:
                        a = '.';
                        break;
                }

                System.out.print(a + " ");
            }
            System.out.println();
        }

    }

}


