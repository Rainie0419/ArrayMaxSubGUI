package utils;

public class Utils {

	public static Object[][] toObjectArray(int[][] dataArray) {
		Object[][] array=new Object[dataArray.length][dataArray[0].length];
		for(int i=0;i<dataArray.length;i++){
			for(int j=0;j<dataArray[0].length;j++){
				array[i][j]=dataArray[i][j];
			}
		}
		return array;
	}

}
