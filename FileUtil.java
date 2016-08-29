import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileUtil{
	
    public static void createCSVFile(ArrayList<ArrayList<Double>> exportData,String filename) {  
  
        File csvFile = null;  
        BufferedWriter csvFileOutputStream = null;  
        try{  
            csvFile = new File(filename + ".csv");   
            csvFile.createNewFile();  
  
            csvFileOutputStream = new BufferedWriter( new OutputStreamWriter(  
                    new FileOutputStream(csvFile)),1024);  
            for(Iterator<ArrayList<Double>> itRow = exportData.iterator(); itRow.hasNext();) { 
            	ArrayList<Double> row = itRow.next();
            	for(Iterator<Double> itData = row.iterator();itData.hasNext();){
            		csvFileOutputStream.write(itData.next().toString());
                    if (itData.hasNext()) {  
                        csvFileOutputStream.write(",");  
                    }
            	}
                if(itRow.hasNext()){
                	csvFileOutputStream.newLine(); 
                }
            }   
            csvFileOutputStream.flush();    
        } catch (Exception e) {    
           e.printStackTrace();    
        } finally {    
           try {    
               csvFileOutputStream.close();    
           } catch (IOException e) {
        	   e.printStackTrace();  
           }
        }    
    }
    
    public static void createCSVFile(String filename, ArrayList<Integer> exportData){
        File csvFile = null;  
        BufferedWriter csvFileOutputStream = null;  
        try{  
            csvFile = new File(filename + ".csv");   
            csvFile.createNewFile();  
  
            csvFileOutputStream = new BufferedWriter( new OutputStreamWriter(  
                    new FileOutputStream(csvFile)),1024);  
           	for(Iterator<Integer> itData = exportData.iterator();itData.hasNext();){
        		csvFileOutputStream.write(itData.next().toString());
                if (itData.hasNext()) {  
                    csvFileOutputStream.write(",");  
                }
        	}
            csvFileOutputStream.flush();    
        } catch (Exception e) {    
           e.printStackTrace();    
        } finally {    
           try {    
               csvFileOutputStream.close();    
           } catch (IOException e) {
        	   e.printStackTrace();  
           }
        }        	
    }

}