package minesweeper;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Table {

    private int row = 9;
    private int column = 9;
    private final char mine = 'X';
    private final char empty = '.';
    private final char mark = '*';
    private final char free = '/';
    protected char[][] map = new char[row][column];
    protected char[][] tempMap = new char[12][12];
    protected char[][] gridMap = new char[12][12];
    protected char[][] gridMap1 = new char[12][12];
    private int numberOfMines;
    private int countMines = 0;
    private int xcoordinate = 0;
    private int ycoordinate = 0;
    private String command;
    private boolean prviPotez = true;
    Random random = new Random();
    Scanner scanner = new Scanner(System.in);

    //start the game
    public void playmines () {
        inputNumberOfMines();
        initMap();
        initMines();
        countingTheNumberMines();
        mapGrid1();
        printemptyMap();
        setmarkgostart();
    }

    public void playminessecond () {
        initMap();
        initMines();
        countingTheNumberMines();
        mapGrid1();
        gridSetMap();
    }

    //Input for mines and control
    private void inputNumberOfMines() {
        System.out.println("How many mines do you want on the field? ");
        numberOfMines = scanner.nextInt();
        while (numberOfMines > row * column) {
            System.out.println("Number of mines exceeds the maximum table count, enter the number lower that " + row * column);
            System.out.println("How many mines do you want on the field? ");
            numberOfMines = scanner.nextInt();
        }
    }

    //Setting the table with 'empty' blocks
    private void initMap() {
        for (char[] chars : map) {
            Arrays.fill(chars, empty);
        }
    }

    //Setting the table with mines
    private void initMines() {
        countMines = 0;
        while (numberOfMines > countMines) {
            int rowInit = random.nextInt(row);
            int columnInit = random.nextInt(column);
            if (map[rowInit][columnInit] == empty) {
                map[rowInit][columnInit] = mine;
                countMines++;
            }
        }
    }
    //karta sa generiranim poljima za usporedbu i za kopiranje
    private void gridSetMap () {
        char number1 = 49;
        char number2 = 49;
        for (int i = 2; i < gridMap.length -1; i++) {
            for (int j = 2; j < gridMap[i].length -1; j++) {
                gridMap[i][j] = map[i-2][j-2];
            }
        }
        for (int i = 0; i < 11; i++) {
            gridMap[1][i] = '-';
            gridMap[11][i] = '-';

        }
        for (int i = 0; i < 12; i++) {
            gridMap[i][1] = '|';
            gridMap[i][11] = '|';
        }
        for (int i = 2; i < 11; i++) {
            gridMap[0][i] = number1++;
        }
        for (int i = 2; i < 11; i++) {
            gridMap[i][0] = number2++;
        }
        gridMap[0][0] = ' ';
    }

    public void printemptyMap() {
        gridSetMap();
        for (char[] chars : gridMap1) {
            for (char aChar : chars) {
                System.out.print(aChar);
            }
            System.out.println();
        }
    }

    //First part for calculating numbers in table
    public void countingTheNumberMines() {
        int count = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == empty) {
                    cellProcessing(map, i, j, count);
                }
            }
        }
    }

    //Second part for calculating numbers in table
    private void cellProcessing(char[][] map, int i, int j, int count) {
        for (int k = i - 1; k <= i + 1 && k < 9; k++) {
            for (int l = j - 1; l <= j + 1 && l < 9; l++) {
                if (k < 0 || l < 0) {
                    continue;
                } else if (map[k][l] == mine) {
                    count++;
                }
            }
        }
        if (count != 0) {
            map[i][j] = (char) (count + 48);
        }
    }

    public void mineMarks () {
        boolean test = true;
        while (test) {
            System.out.println("Set/unset mine marks or claim a cell as free:");
            this.ycoordinate = scanner.nextInt();
            this.xcoordinate = scanner.nextInt();
            this.command = scanner.next();
            if (xcoordinate <= 9 && xcoordinate >= 1 && ycoordinate <= 9 && ycoordinate >= 1 && (command.equals("mine") || command.equals("free")))
                test = false;
            this.xcoordinate += 1;
            this.ycoordinate += 1;
        }

        if(prviPotez && gridMap[xcoordinate][ycoordinate] == mine && command.equals("free")) {
            while(gridMap[xcoordinate][ycoordinate] == mine) {
                secondattemptmode();
                prviPotez = false;
            }
        }

    }

    //za print mine
    private void markedcellcheck () {
        boolean check = true;
        while (check) {
            if (gridMap1[xcoordinate][ycoordinate] == mark) {
                    gridMap1[xcoordinate][ycoordinate] = empty;
                check = false;
                if (gridMap[xcoordinate][ycoordinate] == mine)
                    countMines++;
                else
                    countMines--;
            } else if (gridMap[xcoordinate][ycoordinate] == mine) {
                gridMap1[xcoordinate][ycoordinate] = mark;
                check = false;
                countMines--;
            } else if (gridMap[xcoordinate][ycoordinate] == empty){
                gridMap1[xcoordinate][ycoordinate] = mark;
                check = false;
                countMines++;
            } else if (gridMap[xcoordinate][ycoordinate] >= 49 && gridMap[xcoordinate][ycoordinate] <= 57) {
                gridMap1[xcoordinate][ycoordinate] = mark;
                check = false;
            } else { //if (gridMap[xcoordinate][ycoordinate] == free || (gridMap[xcoordinate][ycoordinate] >= 49 && gridMap[xcoordinate][ycoordinate] <= 57)) {
                System.out.println("Cell is number or free, repeat");
            //}
        } }
        printemptyMap();
    }

    //za print free
    private void markedcellcheckfree() {
        boolean brojac1 = true;
        while (brojac1) {
            if (gridMap[xcoordinate][ycoordinate] == mine) {
                for (int i = 0; i < gridMap1.length; i++) {
                    for (int j = 0; j < gridMap1[i].length; j++) {
                        if (gridMap[i][j] == mine)
                            gridMap1[i][j] = gridMap[i][j];
                    }
                }
                printemptyMap();
                System.out.println("You stepped on a mine and failed!");
                brojac1 = false;
            } else if (gridMap[xcoordinate][ycoordinate] >= 49 && gridMap[xcoordinate][ycoordinate] <= 57) {
                //System.out.println("paint petlja");
                gridMap1[xcoordinate][ycoordinate] = gridMap[xcoordinate][ycoordinate];
                brojac1 = false;
                printemptyMap();
            } else if (gridMap[xcoordinate][ycoordinate] == empty) {
                System.out.println("paint petlja");
                floodFill(gridMap1,new boolean[gridMap1.length][gridMap1[0].length],xcoordinate,ycoordinate );
                brojac1 = false;
                printemptyMap();
            } else
                System.out.println("Not a valid enter");
        }

    }

    private void floodFill(char[][] grid, boolean[][] visited, int r, int c) {
        //quit if off the grid:
        if(r < 2 || r >= (grid.length-1) || c < 2 || c >= (grid[0].length-1)) return;

        //quit if visited:
        if(visited[r][c]) return;
        visited[r][c] = true;

        if(gridMap[r][c]>= 49 && gridMap[r][c]<= 57) grid[r][c] = gridMap[r][c];
        //we want to visit places with periods in them:
        if(gridMap1[r][c]=='.') grid[r][c] = '/';

        if((gridMap1[r+1][c-1] == '/' || gridMap1[r+1][c] == '/' || gridMap1[r+1][c+1] == '/' || gridMap1[r][c+1] == '/' || gridMap1[r-1][c] == '/' || gridMap1[r-1][c-1] == '/' || gridMap1[r][c-1] == '/' || gridMap1[r-1][c+1] == '/') && gridMap1[r][c] == mark)
            grid[r][c] = '/';

        //quit if hit wall:
        if(gridMap[r][c]=='-' || gridMap[r][c]=='/' || gridMap[r][c]== mark || (gridMap[r][c]>= 49 && gridMap[r][c]<= 57)) return;

        //recursively fill in all directions
        floodFill(grid,visited,r+1,c);
        floodFill(grid,visited,r-1,c);
        floodFill(grid,visited,r,c+1);
        floodFill(grid,visited,r,c-1);
        floodFill(grid,visited,r+1,c+1);
        floodFill(grid,visited,r-1,c-1);
        floodFill(grid,visited,r-1,c+1);
        floodFill(grid,visited,r+1,c-1);
    }

    private void setmarkgostart () {
        //temptable();
        while (countMines != 0) {
            mineMarks();
            if (this.command.equals("mine"))
                markedcellcheck();
            else
                markedcellcheckfree();
            if(checkUnmarkedMines())
                break;
        }
        System.out.println("Congratulations! You found all the mines!");
    }

    //novi pokušaj, prazna karta za pokazat
    private void mapGrid1 () {
        char number1 = 49;
        char number2 = 49;
        for (int i = 2; i < gridMap1.length -1; i++) {
            for (int j = 2; j < gridMap1[i].length -1; j++) {
                gridMap1[i][j] = empty;
            }
        }
        for (int i = 0; i < 11; i++) {
            gridMap1[1][i] = '-';
            gridMap1[11][i] = '-';

        }
        for (int i = 0; i < 12; i++) {
            gridMap1[i][1] = '|';
            gridMap1[i][11] = '|';
        }
        for (int i = 2; i < 11; i++) {
            gridMap1[0][i] = number1++;
        }
        for (int i = 2; i < 11; i++) {
            gridMap1[i][0] = number2++;
        }
        gridMap1[0][0] = ' ';
    }


    private boolean checkUnmarkedMines() {
        int brojacpraznina = 0;
        int brojacmarks = 0;
        for (char[] chars : gridMap1) {
            for (char aChar : chars) {
                if (aChar == mark)
                    brojacmarks++;
                if (aChar == empty)
                    brojacpraznina++;
            }
        }
        return brojacmarks == 0 && brojacpraznina == numberOfMines;
    }

    private void secondattemptmode () {
        for (int i = 0; i< gridMap1.length; i++) {
            for (int j = 0; j < gridMap1[i].length; j++) {
                if (gridMap1[i][j] == mark)
                    tempMap[i][j] = gridMap1[i][j];
            }
        }
        playminessecond();
        for (int i = 0; i< tempMap.length; i++) {
            for (int j = 0; j < tempMap[i].length; j++) {
                if ( gridMap1[i][j] == free && (gridMap1[xcoordinate][ycoordinate] >= 49 && gridMap1[xcoordinate][ycoordinate] <= 57) )
                    continue;
                else if (tempMap[i][j] == mark)
                    gridMap1[i][j] = mark;
            }
        }
    }
}