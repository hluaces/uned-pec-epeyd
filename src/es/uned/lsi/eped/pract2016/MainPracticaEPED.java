package es.uned.lsi.eped.pract2016;

import es.uned.lsi.eped.DataStructures.AcademiaIF;

public class MainPracticaEPED {
    public static void main(String [] args) throws Exception{
            if ( args.length == 0 ) {
                args = new String[3];
                args[0] = "S";
                args[1] = "C:\\Users\\mordisko\\Desktop\\JdP-Estudiantes\\academiaS.txt";
                args[2] = "C:\\Users\\mordisko\\Desktop\\JdP-Estudiantes\\operationsS.txt";
                
                args[0] = "C";
                args[1] = "C:\\Users\\mordisko\\Desktop\\JdP-Estudiantes\\academiaC.txt";
                args[2] = "C:\\Users\\mordisko\\Desktop\\JdP-Estudiantes\\operationsC.txt";
            }
            
            String scenario = args[0];
            String fileData = args[1];
            String fileOperations=args[2];
            //lectura del fichero de operaciones según el escenario
            AcademiaIF academia = IO.readDataFile(fileData,scenario);
            //salida por consola de las operaciones contenidas en el 
            //fichero de operaciones
            IO.applyOperations(fileOperations,scenario,academia);
    }	
}