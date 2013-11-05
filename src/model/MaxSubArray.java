package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MaxSubArray {
	static boolean invalidInput=false;
	//final index
	static int startRow,endRow,startCol,endCol;
	//column index in one turn
	static int startColumn,endColumn;

	public static String getMaxSum(String filename,char para) {
		int[][] array = getArrayInFile(filename);
		checkArray(array);
		
		if(invalidInput)
			return "Invalid input.";
		else{
			int max=0;
			switch(para){
				case 'r': 
					max=maxSubRec(array);
					break;
				case 'v': 
					max=maxSubRecVer(array);
					break;
				case 'h': 
					max=maxSubRecHor(array);
					int rowNum=array.length;
					int colNum = array[0].length;
					int newStartRow=rowNum-1-endCol;
					int newEndRow=rowNum-1-startCol;
					int newStartCol=startRow;
					int newEndCol=endRow;
					startRow=newStartRow;
					endRow=newEndRow;
					startCol=newStartCol;
					endCol=newEndCol;
					if(endCol>=colNum)
						endCol=endCol-colNum;
					break;
				case 'b':
					max=maxSubRecHorAndVer(array);
					break;
				default:
			}

			return Integer.toString(max);
		}
	}


	private static int maxSubRecHorAndVer(int[][] array) {
		int rowNum=array.length;
		int colNum=array[0].length;
		int[][] doubleArray=new int[rowNum*2][colNum*2];
		for(int i=0;i<rowNum;i++){
			System.arraycopy(array[i], 0, doubleArray[i], 0, colNum);
			System.arraycopy(array[i], 0, doubleArray[i], colNum, colNum);
			System.arraycopy(array[i], 0, doubleArray[i+rowNum], 0, colNum);
			System.arraycopy(array[i], 0, doubleArray[i+rowNum], colNum, colNum);
		}
		int max=0;
		for(int i=0;i<rowNum;i++){
			for(int j=i;j<i+rowNum;j++){
				int[] oneDimension=getOneDimensionArray(doubleArray,i,j);
				int maxOneDimension=maxSubSumWithWidthRestrict(oneDimension);
				if(maxOneDimension>max){
					max=maxOneDimension;
					startRow=i;
					endRow=j;
					if(endRow>=rowNum)
						endRow=endRow-rowNum;
					startCol=startColumn;
					endCol=endColumn;
					if(endCol>=colNum)
						endCol=endCol-colNum;
				}
					
			}
		}
		return max;
	}


	private static int maxSubSumWithWidthRestrict(int[] array) {
		//store max sum
		int maxSum=array[0];
		//store current sum in each group
		int currentSum=0;
		startColumn=0;
		int currentStart=0;
		int currentEnd=0;
		for(int i=0; i<array.length; i++){
			// current sum is negative, discard it, or it will decrease sum of following elements
			if(currentSum<0){
				currentSum=array[i];
				currentStart=i;
			}
			// start to end is larger than column num of original array
			// restart to add from start+1
			else if(i-currentStart==array.length/2){
				i=currentStart+1;
				currentSum=array[i];
				currentStart=i;
			}
			// just add new element
			else{
				currentSum+=array[i];
				currentEnd=i;
			}
			// current>max, replace
			if(currentSum>maxSum){
				maxSum=currentSum;
				startColumn=currentStart;
				endColumn=currentEnd;
			}
		}
		return maxSum;
	}


	private static int maxSubRecHor(int[][] array) {
		int rowNum = array.length;
		int colNum = array[0].length;
		int[][] transArray = new int[colNum][rowNum];
		for(int i=0;i<colNum;i++){
			for(int j=0;j<rowNum;j++){
				transArray[i][j]=array[rowNum-1-j][i];
			}
		}
		
		return maxSubRecVer(transArray);
	}


	private static int maxSubRecVer(int[][] array) {
		int rowNum = array.length;
		int colNum = array[0].length;
		int[][] doubleArrayInRow=new int[rowNum*2][colNum];
		for(int i=0;i<rowNum;i++){
			System.arraycopy(array[i], 0, doubleArrayInRow[i], 0, colNum);
			System.arraycopy(array[i], 0, doubleArrayInRow[i+rowNum], 0, colNum);
		}
		int max=0;
		for(int i=0;i<rowNum;i++){
			for(int j=i;j<i+rowNum;j++){
				int[] oneDimension=getOneDimensionArray(doubleArrayInRow,i,j);
				int maxOneDimension=maxSubSum(oneDimension);
				if(maxOneDimension>max){
					max=maxOneDimension;
					startRow=i;
					endRow=j;
					if(endRow>=rowNum)
						endRow=endRow-rowNum;
					startCol=startColumn;
					endCol=endColumn;
				}
			}
		}
		return max;
	}

	private static int maxSubRec(int[][] array) {
		int row = array.length;
		
		int max=0;
		for(int i=0;i<row;i++){
			for(int j=i;j<row;j++){
				int[] oneDimension=getOneDimensionArray(array,i,j);
				int maxOnedimension=maxSubSum(oneDimension);
				if(maxOnedimension>max){
					max=maxOnedimension;
					startRow=i;
					endRow=j;
					startCol=startColumn;
					endCol=endColumn;
				}
			}
		}
//		System.out.println("Start Row: "+startRow+", End Row: "+endRow);
//		System.out.println("Start Column: "+startCol+", End Column: "+endCol);
		return max;
	}

	private static int maxSubSum(int[] array) {
		//store max sum
		int maxSum=array[0];
		//store current sum in each group
		int currentSum=0;
		startColumn=0;
		int currentStart=0;
		int currentEnd=0;
		for(int i=0; i<array.length; i++){
			// current sum is negative, discard it, or it will decrease sum of following elements
			if(currentSum<0){
				currentSum=array[i];
				currentStart=i;
			}
			// just add new element
			else{
				currentSum+=array[i];
				currentEnd=i;
			}
			// current>max, replace
			if(currentSum>maxSum){
				maxSum=currentSum;
				startColumn=currentStart;
				endColumn=currentEnd;
			}
		}
		return maxSum;
	}

	private static int[] getOneDimensionArray(int[][] array, int start, int end) {
		int columnNum=array[0].length;
		int[] oneDimension = new int[columnNum];
		for(int i=0;i<columnNum;i++){
			for(int j=start;j<=end;j++){
				oneDimension[i]+=array[j][i];
			}
		}
		return oneDimension;
	}

	private static void checkArray(int[][] array) {
		if(array==null||array.length==0){
			invalidInput = true;
		}else
			invalidInput = false;
	}

	public static int[][] getArrayInFile(String filename) {
		int[][] array = null;
		File file = new File(filename);
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(file));
			String oneline = null;
			// get row
			int row = Integer.parseInt(reader.readLine().split(",")[0]);
			// get column
			int column = Integer.parseInt(reader.readLine().split(",")[0]);
			array = new int[row][column];
			int rowindex=0;
			while((oneline=reader.readLine())!=null){
				String[] rowStrs=oneline.split(",");
				//columnNum is not equal to column 
				if(rowStrs.length!=column){
					invalidInput=true;
					return null;
				}
				for(int j=0;j<rowStrs.length;j++){
					array[rowindex][j]=Integer.parseInt(rowStrs[j]);
				}
				rowindex++;
			}
			//rowNum is not equal to row
			if(rowindex!=row){
				invalidInput=true;
				return null;
			}
			invalidInput=false;
		}catch(Exception e){
			invalidInput = true;
			//e.printStackTrace();
		}finally{
			if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
		}
		return array;
	}

	public static int[] getIndexNote() {
		int[] indexes={startRow,endRow,startCol,endCol};
		return indexes;
	}

}
