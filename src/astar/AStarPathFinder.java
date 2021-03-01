package astar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class AStarPathFinder {
    PathCell start;
    PathCell finish;
    Point startPos;
    Point finishPos;
    //int size = 15;


    public void start(String relativePathToFile, int size)  {
        ArrayList<ArrayList<PathCell>> grid = new ArrayList<>();

        ArrayList<PathCell> knownPathCells = new ArrayList<>();

        startPos =new Point(1,0);
        finishPos =new Point((size *2 ) -1,size * 2 );


        try {
            Scanner scanner = new Scanner(new File(new File("").getAbsolutePath() +"\\src\\" + relativePathToFile));
            int y = 0;
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                int lineLength = line.length();

                    grid.add(new ArrayList<>());
                    for (int x = 0; x < lineLength; x++) {
                        char a = line.charAt(x);
                        if (a != ' ') {

                            PathCell c = new PathCell(new Point(x == 0 ? 0 : (x + 1) / 2, y));

                            if (a == '#')
                                c.type = Type.WALL;
                            else if (a == '.')
                                c.type = Type.NORMAL;
                            else if (a == 'S')
                                c.type = Type.START;

                            grid.get(y).add(c);
                            this.addStartAndFinish(x == 0 ? 0 : (x + 1) / 2, y , c, startPos, finishPos);
                        }
                    }
                    y++;
            }
        }catch (FileNotFoundException e){
            System.out.println("FUCK something bad happened ... ");
            //make the grid
            for (int y = 0; y < size; y++) {
                grid.add(new ArrayList<>());
                for (int x = 0; x < size; x++) {
                    PathCell c = new PathCell(new Point(x, y));
                    grid.get(y).add(c);
                    this.addStartAndFinish(x, y, c,startPos, finishPos);
                }
            }
        }

        start.type = Type.START;
        finish.type = Type.FINISH;

        //add neigbours
        grid = addNeigbours(grid);

        //do a a star algorithm
        // while ...
        knownPathCells.add(start);
        start.local = 0;
        start.global = Math.sqrt(Math.pow(Math.abs(finish.pos.x - start.pos.x), 2) + Math.pow(Math.abs(finish.pos.y - start.pos.y),2));


        whileLoop(knownPathCells,grid);

        trackThePath(grid, knownPathCells);

        render(grid);
    }

    public boolean whileLoop(ArrayList<PathCell> knownPathCells, ArrayList<ArrayList<PathCell>> grid){
        while (!knownPathCells.isEmpty()) {


            if (knownPathCells.get(0).equals(finish)) {
                return true;
            }
            knownPathCells.get(0).type = Type.VISITED;
            PathCell testingPathCell = knownPathCells.get(0);

            for (int i = 0; i < testingPathCell.neigbours.size(); i++) {

                PathCell neigbour = testingPathCell.neigbours.get(i);

                if(!neigbour.type.equals(Type.WALL)){
                    if (testingPathCell.local + 1 < neigbour.local) {
                        neigbour.owner = testingPathCell;
                        neigbour.local = testingPathCell.local + 1;
                        neigbour.global = neigbour.local + Math.sqrt(Math.pow(Math.abs(finish.pos.x - neigbour.pos.x), 2) + Math.pow(Math.abs(finish.pos.y - neigbour.pos.y),2));
                        knownPathCells.add(neigbour);
                    }
                }
            }

            knownPathCells.remove(0);

            sort(knownPathCells);

           // render(grid);
           // System.out.println();
        }

        return  false;
    }

    public void addStartAndFinish(int x, int y, PathCell c, Point startPos, Point finishPos) {
        if (x == startPos.x && y == startPos.y) {
            start = c;
        } else if (x == finishPos.x && y == finishPos.y) {
            finish = c;
        }
    }

    public ArrayList<ArrayList<PathCell>> addNeigbours(ArrayList<ArrayList<PathCell>> grid) {
        for (ArrayList<PathCell> lines : grid) {
            for (PathCell pathCell : lines) {
                //up
                pathCell = searchNeigbour(grid, pathCell, -1, 0);
                // down
                pathCell = searchNeigbour(grid, pathCell, +1, 0);
                // right
                pathCell = searchNeigbour(grid, pathCell, 0, +1);
                // left
                pathCell = searchNeigbour(grid, pathCell, 0, -1);

                grid.get(pathCell.pos.y).set(pathCell.pos.x, pathCell);

            }
        }
        return grid;
    }

    public PathCell searchNeigbour(ArrayList<ArrayList<PathCell>> grid, PathCell pathCell, int fromX, int fromY) {
        try {
            PathCell neigbour = grid.get(pathCell.pos.y + fromY).get(pathCell.pos.x + fromX);
            if (neigbour.type != Type.WALL) {
                pathCell.addNeigbour(neigbour);
            }
        } catch (IndexOutOfBoundsException e) {
        }
        return pathCell;
    }

    public ArrayList<PathCell> sort(ArrayList<PathCell> arrayList) {
        Collections.sort(arrayList, new Comparator<PathCell>() {
            @Override
            public int compare(PathCell o1, PathCell o2) {
                return Double.compare(o1.global, o2.global);
            }
        });
        return arrayList;
    }

    public void render(ArrayList<ArrayList<PathCell>> grid) {
        for (ArrayList<PathCell> arrayList : grid) {

            for (PathCell pathCell : arrayList) {
                char a;
                switch (pathCell.type) {
                    case WALL:
                        a = '#';
                        break;
                    case PATH:
                        a = '.';
                        break;
                    case START:
                        a = 'S';
                        break;
                    case FINISH:
                        a = 'W';
                        break;
                    default:
                        a = ' ';
                        break;
                }

                System.out.print(a + " ");
            }
            System.out.println();
        }

    }

    public void trackThePath(ArrayList<ArrayList<PathCell>> grid, ArrayList<PathCell> knownPathCells) {
        PathCell pathCell = finish.owner;
        //System.out.println(cell.pos.x + " "  + cell.pos.y);
        do {
            pathCell.type = Type.PATH;
            pathCell = pathCell.owner;
           // System.out.println(cell.pos.x + " "  + cell.pos.y);
        }while(pathCell.owner != null);
    }

}


