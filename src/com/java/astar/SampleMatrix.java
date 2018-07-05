package com.java.astar;

import java.util.Comparator;
import java.util.PriorityQueue;


public class SampleMatrix {
	
	//input matrix
	 int[][] givenMatrix ;
	
	//Blocked cells are just null Cell values in grid
     Cell [][] grid;
    
     //stores the cell in order to calculate the heuristic cost
     PriorityQueue<Cell> open;
     
     //
     boolean closed[][];
     
     int startI, startJ;
     
     int endI, endJ;
 
	
	 class Cell{  
        int heuristicCost = 0; //Heuristic cost
        int finalCost = 0; //G+H
        int i, j;
        Cell parent; 
        
        Cell(int i, int j){
            this.i = i;
            this.j = j; 
        }
        
        @Override
        public String toString(){
            return "["+this.i+", "+this.j+"]";
        }
    }
 
	/*Method to update the grid as null for the given coordinates*/       
    public  void setBlockedPath(int i, int j){
        grid[i][j] = null;
    }
    
    /*Method to update the starting coordinates*/
    public  void setStartCell(int i, int j){
        startI = i;
        startJ = j;
    }
    
    /*Method to update the destination coordinates*/
    private  void setEndCell(int i, int j){
        endI = i;
        endJ = j; 
    }
    /*Method to update the final cost of each cell
     * */
     private void checkAndUpdateCost(Cell current, Cell t, int cost){
        if(t == null || closed[t.i][t.j])return;
        int t_final_cost = t.heuristicCost+cost;
        
        boolean inOpen = open.contains(t);
        if(!inOpen || t_final_cost<t.finalCost){
            t.finalCost = t_final_cost;
            t.parent = current;
            if(!inOpen)open.add(t);
        }
    }
	
	/*method to search the shortest path
	 * @param startPosX is the starting initial x Coordinates
	 * @param startPosy is the starting initial y Coordinates
	 *  @param endPosX is the destination goal x Coordinates
	 *  @param endPosY is the destination goal y Coordinates
	 *  @param rowCount is the number of rows for the given matrix
	 *  @param colCount is the number of columns for the given matrix
	 *  @param blockedPath containing the array of x/y coordinates whose contains water or no path
	 * */
    private void discoverShortestPath(int statPosX,int startPosY,int endPosX,int endPosY,int rowCount,int colCount,int[][] blockedPath)
    {
    	 
    	setStartCell(statPosX, startPosY);
    	
    	setEndCell(endPosX, endPosY);
    	
    	grid = new Cell[rowCount][colCount];
    	
    	closed = new boolean[rowCount][colCount];
    	
    	open = new PriorityQueue<Cell>(1, new Comparator<Object>() {

			@Override
			public int compare(Object o1, Object o2) {
				
				 Cell c1 = (Cell)o1;
	                Cell c2 = (Cell)o2;

	                return c1.finalCost<c2.finalCost?-1:
	                        c1.finalCost>c2.finalCost?1:0;
			}
        	   
		});

    	for(int i=0;i < rowCount;++i){
            for(int j=0;j < colCount;++j){
                grid[i][j] = new Cell(i, j);
                grid[i][j].heuristicCost = Math.abs(i-endI)+Math.abs(j-endJ);
            }
         }
         grid[startI][startJ].finalCost = 0;
         
         /*
           Set the cell values to null
           for blocked cells.
         */
         for(int i=0;i<blockedPath.length;++i){
        	 setBlockedPath(blockedPath[i][0], blockedPath[i][1]);
         }
         
         computePath();
         
         updateMatrixPathCost(); 
         
    }
     
     /*update the shortest path cell values as 6,
      *so that it is easy to show # for those cells */
     private void updateMatrixPathCost()
     {
    	
    	   	if(closed[endI][endJ]){
            //Trace back the path 
             System.out.println("Shortest Path: ");
             Cell current = grid[endI][endJ];
             
             givenMatrix[current.i][current.j] = 6;
             
             System.out.print(current);
             
             while(current.parent!=null){
                 System.out.print(" -> "+current.parent);
                 current = current.parent;
                 givenMatrix[current.i][current.j] = 6;
             } 
             System.out.println("\n");
        }else System.out.println("No possible path");
   	
     }
     
     /*show the shortest path of the matrix with # symbol
 	 * used constants for symbols as explained below-
 	 * 0 -> @(i.e.Start)
 	 * 1 -> .(i.e. Empty land)
 	 * 2 -> *(i.e.jungle)
 	 * 3 -> ^(i.e. mountain) 
 	 * 4 -> X(i.e. Destination or Goal)
 	 * 5 -> ~(i.e. Water)
 	 * 6 -> #(i.e. path)
 	 * */
	private void showAStarPath()
	{
		
		int rowSize = givenMatrix.length;
		int colSize = givenMatrix[1].length;
		
		System.out.println("Output Matrix with Path:");
		System.out.println("\n");
		for(int i = 0; i< rowSize; i++)
		{
			for(int j = 0; j< colSize; j++)
			{
				if (givenMatrix[i][j] == 0)
				{
					System.out.print("@   ");
				}
				else if(givenMatrix[i][j] == 1)
				{
					System.out.print(".   ");
				}
				else if(givenMatrix[i][j] == 2)
				{
					System.out.print("*   ");
				}
				else if(givenMatrix[i][j] == 3)
				{
					System.out.print("^   ");
				}
				else if(givenMatrix[i][j] == 4)
				{
					System.out.print("X   ");
				}
				else if(givenMatrix[i][j] == 5)
				{
					System.out.print("~   ");
				}
				else if(givenMatrix[i][j] == 6)
				{
					System.out.print("#   ");
				}
				
			}
			System.out.println("\n");
		}
	}
	
	/*Create a matrix with proper symbol according to the given input
	 * @param inputData given to show the matrix with actual Data.
	 * used constants for symbols as explained below-
	 * 0 -> @(i.e.Start)
	 * 1 -> .(i.e. Empty land)
	 * 2 -> *(i.e.jungle)
	 * 3 -> ^(i.e. mountain) 
	 * 4 -> X(i.e. Destination or Goal)
	 * 5 -> ~(i.e. Water)
	 * */
  private void createAndShowInputMatrix(int[][] inputData)
	{
		givenMatrix = inputData;
		
		int rowSize = inputData.length;
		int colSize = inputData[0].length;
		
		System.out.println("Input Matrix:");
		System.out.println("\n");
		for(int i = 0; i< rowSize; i++)
		{
			for(int j = 0; j< colSize; j++)
			{
				if (inputData[i][j] == 0)
				{
					System.out.print("@   ");
				}
				else if(inputData[i][j] == 1)
				{
					System.out.print(".   ");
				}
				else if(inputData[i][j] == 2)
				{
					System.out.print("*   ");
				}
				else if(inputData[i][j] == 3)
				{
					System.out.print("^   ");
				}
				else if(inputData[i][j] == 4)
				{
					System.out.print("X   ");
				}
				else if(inputData[i][j] == 5)
				{
					System.out.print("~   ");
				}
				
			}
			System.out.println("\n");
		}
		
	}
	
	
	public  void computePath(){ 
        
        //add the start location to open list.
        open.add(grid[startI][startJ]);
        
        Cell current;
        
        while(true){ 
            current = open.poll();
            if(current==null)break;
            closed[current.i][current.j]=true; 

            if(current.equals(grid[endI][endJ])){
                return; 
            } 

            Cell t;  
            if(current.i-1>=0){
                t = grid[current.i-1][current.j];
                checkAndUpdateCost(current, t, current.finalCost); 

                if(current.j-1>=0){                      
                    t = grid[current.i-1][current.j-1];
                    checkAndUpdateCost(current, t, current.finalCost); 
                }

                if(current.j+1<grid[0].length){
                    t = grid[current.i-1][current.j+1];
                    checkAndUpdateCost(current, t, current.finalCost); 
                }
            } 

            if(current.j-1>=0){
                t = grid[current.i][current.j-1];
                checkAndUpdateCost(current, t, current.finalCost); 
            }

            if(current.j+1<grid[0].length){
                t = grid[current.i][current.j+1];
                checkAndUpdateCost(current, t, current.finalCost); 
            }

            if(current.i+1<grid.length){
                t = grid[current.i+1][current.j];
                checkAndUpdateCost(current, t, current.finalCost); 

                if(current.j-1>=0){
                    t = grid[current.i+1][current.j-1];
                    checkAndUpdateCost(current, t, current.finalCost); 
                }
                
                if(current.j+1<grid[0].length){
                   t = grid[current.i+1][current.j+1];
                    checkAndUpdateCost(current, t, current.finalCost); 
                }  
            }
        } 
    }

	

		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SampleMatrix sm = new SampleMatrix();
		int[][] inputMatrix = {{0,2,3,3,3},{5,5,2,5,1},{2,2,1,1,1},{3,1,1,2,5},{5,5,2,5,4}}; 
		int[][] blockedPath = {{1,0},{1,1},{1,3},{3,4},{4,0},{4,1},{4,3}};
		
		sm.createAndShowInputMatrix(inputMatrix);
		
		sm.discoverShortestPath(0, 0, 4, 4, 5, 5, blockedPath);
		
		sm.showAStarPath();
	}

}
