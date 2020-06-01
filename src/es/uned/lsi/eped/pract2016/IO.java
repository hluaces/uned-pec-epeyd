package es.uned.lsi.eped.pract2016;

import es.uned.lsi.eped.DataStructures.DoctorIF;
import es.uned.lsi.eped.DataStructures.AcademiaIF;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.uned.lsi.eped.DataStructures.Collection;
import es.uned.lsi.eped.DataStructures.CollectionIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.List;

public class IO {

	//esta clase contiene métodos para gestionar la entrada y la salida
	// de la práctica.
	
	
	//métodos de lectura para los ficheros de datos en los dos escenarios
	//lectura del fichero de datos (academia y doctores): 
	//- La primera línea indica quién es el fundador de la academia
	//- Las siguientes líneas indican información sobre una tesis doctoral
	//    a. (doctor, supervisor) en el escenario S
	//    b. (doctor, listaSupervisores) en el escenario C
	
	
	public static AcademiaIF readDataFile(String fileData,String scenario) throws IOException{
		if(scenario.equalsIgnoreCase("S")){
			return readSimplifiedAcademia(fileData);
		}
		if(scenario.equalsIgnoreCase("C")){
			return readCompleteAcademia(fileData);
		}
		return null;
	}
	
	
	//método para leer el fichero de datos en el primer escenario
	public static AcademiaS readSimplifiedAcademia(String fileData) throws IOException{
		File FData = new File(fileData);
		BufferedReader brData = new BufferedReader(new FileReader(FData));
		String lineData;
		int cont = 1;
		AcademiaS academia = new AcademiaS();
		while((lineData = brData.readLine())!=null) {
			//primera línea: fundador de la academia
			if(cont==1){
				academia = IO.readAcademiaS(lineData);
			}
			//lineas posteriores: doctor y su supervisor
			else{
				DoctorS doctorS = IO.readDoctorS(academia, lineData);
				academia.addDoctor(doctorS, doctorS.getSupervisor());
			}
			cont++;
		}
		brData.close();
		return academia;
	}
	//método para leer el fichero de datos en el segundo escenario
		public static AcademiaC readCompleteAcademia(String fileData) throws IOException{
			File FData = new File(fileData);
			BufferedReader brData = new BufferedReader(new FileReader(FData));
			String lineData;
			int cont = 1;
			AcademiaC academia = new AcademiaC();
			while((lineData = brData.readLine())!=null) {
				//primera linea: fundador de la academia
				if(cont==1){
					academia = IO.readAcademiaC(lineData);
				}
				//líneas posteriores: doctor y sus supervisores
				else{
					DoctorC doctorC = IO.readDoctorC(academia, lineData);					
					List<DoctorIF> supervisors = (List<DoctorIF>) doctorC.getSupervisors();
					DoctorIF firstSupervisor = supervisors.get(1);
					//añadir el primer supervisor
					academia.addDoctor(doctorC,supervisors.get(1));
					IteratorIF<DoctorIF> iterator = supervisors.iterator();
					//añadir resto de supervisores
					while(iterator.hasNext()){
						DoctorIF supervisor = iterator.getNext();
						if(!supervisor.equals(firstSupervisor)){
							academia.addSupervision(doctorC, supervisor);
						}
					}	
				}
				cont++;
			}
			brData.close();
			return academia;
		}
	
	//métodos de creación de lectura de doctores
	// Los identificadores de los doctores son números entrecomillados
	// Para detectarlos se hace uso de expresiones regulares: 
		// - Se detecta el patrón "cadena de dígítos" entrecomillados ("[0-9]+"),
		//   donde 0-9 es cualquier digito, y + indica uno o más digitos.
		// - Se aplica el patrón a la línea de texto, detectando qué partes 
		//	 de la misma siguen dicho patrón.
		
	
	//crear un doctor en el primer escenario dada una línea del fichero de datos
	public static DoctorS readDoctorS(AcademiaIF a,String line){
		if(line.startsWith("Tesis")){
			String patron = "\"[0-9]+\"";
			Pattern p = Pattern.compile(patron);
			Matcher m = p.matcher(line);
			int cont = 1;
			int ID = -1;
			int IDSupervisor = -1;
			while(m.find()){
				String id = m.group();
				id = id.replace("\"", "");
				if(cont==1){
					ID = Integer.valueOf(id);
				}
				if(cont==2){
					IDSupervisor = Integer.valueOf(id);
				}
				cont++;
			}
			return new DoctorS(ID,a,a.getDoctor(IDSupervisor));
		}
		return null;
	}
	
	//crear un doctor en el segundo escenario dada una línea del fichero de datos	
	public static DoctorC readDoctorC(AcademiaIF a,String line){
		if(line.startsWith("Tesis")){
			String patron = "\"[0-9]+\"";
			Pattern p = Pattern.compile(patron);
			Matcher m = p.matcher(line);
			int cont = 1;
			int IDDoctor = -1;
			List<DoctorIF> supervisors = new List<DoctorIF>();
			while(m.find()){
				String id = m.group();
				id = id.replace("\"", "");
				if(cont==1){
					IDDoctor = Integer.valueOf(id);
				}
				if(cont>1){
					int IDSupervisor = Integer.valueOf(id);
					supervisors.insert(a.getDoctor(IDSupervisor), supervisors.size()+1);
				}
				cont++;
			}
			return new DoctorC(IDDoctor,a,supervisors);
		}
		return null;
	}
	
	//crear una academia en el primer escenario dada una línea del fichero de datos
	public static AcademiaS readAcademiaS(String line){
		if(line.startsWith("La Academia")){
			String patron = "\"[0-9]+\"";
			Pattern p = Pattern.compile(patron);
			Matcher m = p.matcher(line);
			int ID = -1;
			while(m.find()){
				String id = m.group();
				id = id.replace("\"", "");
				ID = Integer.valueOf(id);
			}
			if(ID!=-1){
				DoctorS founder = new DoctorS(ID);
				AcademiaS a = new AcademiaS();
				a.setFounder(founder);
				founder.setAcademia(a);
				return a;
			}
		}
		return null;
	}
	
	//crear una academia en el segundo escenario dada una línea del fichero de datos	
	public static AcademiaC readAcademiaC(String line){
		if(line.startsWith("La Academia")){
			String patron = "\"[0-9]+\"";
			Pattern p = Pattern.compile(patron);
			Matcher m = p.matcher(line);
			int ID = -1;
			while(m.find()){
				String id = m.group();
				id = id.replace("\"", "");
				ID = Integer.valueOf(id);
			}
			if(ID!=-1){
				DoctorC founder = new DoctorC(ID);
				AcademiaC a = new AcademiaC();
				a.setFounder(founder);
				founder.setAcademia(a);
				return a;
			}
		}
		return null;
	}
	
	
	
	//Método auxiliar que devuelve una cadena de caractéres con los IDs 
	//de los doctores incluidos en la colección dada por parámetro, en el 
	//formato del enunciado de la práctica.
	public static String doctorsToString(CollectionIF<DoctorIF> D){
		String salida="";
		IteratorIF<DoctorIF> iterator = D.iterator();
		int cont = 1;
		int size = D.size();
		while(iterator.hasNext()){
			DoctorIF doctor = iterator.getNext();
			int IDDoctor = doctor.getID();
			if(cont==1){
				salida = "\""+IDDoctor+"\"";
			}
			else{
				if(cont<size){
					salida = salida+", \""+IDDoctor+"\"";
				}
				else{
					salida = salida+" y \""+IDDoctor+"\"";								
				}
			}
			cont++;
		}
		return salida;
	}
	
	//lee lista de operaciones devolviendo por consola el resultado y la 
	// medición de tiempo de cada una de ellas.
	public static void applyOperations(String fileOperations, String scenario, AcademiaIF academia) throws NumberFormatException, IOException{
		//la primera línea de salida informa del nº de doctores de la academia.
		System.out.println("Doctores en la Academia: "+academia.size()+".");
		File FOperations = new File(fileOperations);
		BufferedReader brOperations = new BufferedReader(new FileReader(FOperations));
		String lineOperation;
		//número de iteraciones que se repite por cada operación
		//para la medición del tiempo.
		int iterations = 100000;
                 iterations = 1;
		while((lineOperation = brOperations.readLine())!=null) {
			//dividir la línea en tokens (palabras) separadas por espacios en blanco
			StringTokenizer st = new StringTokenizer(lineOperation);
			//se toma el primer token para distinguir la operación
			String operation = st.nextToken();
			//se aplica la operación correspondiente según el código de operación
			//consulta de supervisores
			if(operation.equalsIgnoreCase("SU")){
				String ID = st.nextToken();
				//eliminar comillas para obtener el entero
				ID = ID.replace("\"","");
				int IDDoctor = Integer.valueOf(ID);
				if(scenario.equalsIgnoreCase("S")){
					DoctorS doctor = (DoctorS) academia.getDoctor(IDDoctor);
					//se toma el tiempo antes de aplicar la operación
					long time_start = System.currentTimeMillis();
					//se aplica la operación 10000 veces para medir tiempo
					DoctorS supervisor = new DoctorS();
					for(int i=1;i<=iterations;i++){
						 supervisor = (DoctorS)doctor.getSupervisor();
					}
					//se toma el tiempo tras aplicar la operación
					long time_end = System.currentTimeMillis();
					//el tiempo consumido por la operación se obtiene restando el
					//tiempo antes de ejecutar la operación al tiempo tras ejecutar
					//la operación. La unidad de medida es milisegundos.
					long time = time_end - time_start;
					if(supervisor==null){
						System.out.println("Fundador de la Academia: no tiene supervisores");						
					}
					else{
						int IDSupervisor = supervisor.getID();
						System.out.println("Tesis de \""+IDDoctor+"\" dirigida por \""+IDSupervisor+"\"");
					}
					System.out.println("- Tiempo: "+time);			
				}
				if(scenario.equalsIgnoreCase("C")){
					DoctorC doctor = (DoctorC)academia.getDoctor(IDDoctor);
					long time_start = System.currentTimeMillis();
					CollectionIF<DoctorIF> supervisors = new Collection<DoctorIF>();
					for(int i=1;i<=iterations;i++){
						supervisors =  doctor.getSupervisors();
					}
					long time_end = System.currentTimeMillis();
					long time = time_end - time_start;
					String salida = IO.doctorsToString(supervisors);
					int size = supervisors.size();
					if(size==1){
						System.out.println("Tesis de \""+IDDoctor+"\" dirigida por "+salida);
					}
					else if(size>1){
						System.out.println("Tesis de \""+IDDoctor+"\" codirigida por "+salida);						
					}
					System.out.println("- Tiempo: "+time);			
				}
			}
			//consulta de ancestros
			if(operation.equalsIgnoreCase("AN")){
				String ID = st.nextToken();
				ID = ID.replace("\"","");
				int IDDoctor = Integer.valueOf(ID);
				int generations = Integer.valueOf(st.nextToken());
				DoctorIF doctor = academia.getDoctor(IDDoctor);
				long time_start = System.currentTimeMillis();
				CollectionIF<DoctorIF> ancestors = new Collection<DoctorIF>();
				for(int i=1;i<=iterations;i++){
					ancestors = doctor.getAncestors(generations);
				}
				long time_end = System.currentTimeMillis();
				long time = time_end - time_start;
				String salida = IO.doctorsToString(ancestors);
				System.out.println("Los ancestros de \""+IDDoctor+"\" hasta "+
					generations+" generaciones son "+salida);				
				System.out.println("- Tiempo: "+time);			
			}
			//consulta de estudiantes
			if(operation.equalsIgnoreCase("ST")){
				String ID = st.nextToken();
				ID = ID.replace("\"","");
				int IDDoctor = Integer.valueOf(ID);
				DoctorIF doctor = academia.getDoctor(IDDoctor);
				long time_start = System.currentTimeMillis();
				CollectionIF<DoctorIF> students = new Collection<DoctorIF>();
				for(int i=1;i<=iterations;i++){
					students = doctor.getStudents();
				}
				long time_end = System.currentTimeMillis();
				long time = time_end - time_start;
				String salida = IO.doctorsToString(students);
				System.out.println("\""+IDDoctor+"\" ha dirigido la tesis de "+salida);
				System.out.println("- Tiempo: "+time);			
			}
			//consulta de descendientes académicos
			if(operation.equalsIgnoreCase("DE")){
				String ID = st.nextToken();
				ID = ID.replace("\"","");
				int IDDoctor = Integer.valueOf(ID);
				int generations = Integer.valueOf(st.nextToken());
				DoctorIF doctor = academia.getDoctor(IDDoctor);
				long time_start = System.currentTimeMillis();
				CollectionIF<DoctorIF> descendants = new Collection<DoctorIF>();
				for(int i=1;i<=iterations;i++){
					descendants = doctor.getDescendants(generations);
				}
				long time_end = System.currentTimeMillis();
				long time = time_end - time_start;
				String salida = IO.doctorsToString(descendants);
				System.out.println("Los descendientes de \""+IDDoctor+"\" hasta "+
					generations+" generaciones son "+salida);	
				System.out.println("- Tiempo: "+time);			
			}
			//consulta de hermanos académicos
			if(operation.equalsIgnoreCase("SI")){
				String ID = st.nextToken();
				ID = ID.replace("\"","");
				int IDDoctor = Integer.valueOf(ID);
				DoctorIF doctor = academia.getDoctor(IDDoctor);
				long time_start = System.currentTimeMillis();
				CollectionIF<DoctorIF> siblings = doctor.getSiblings();
				for(int i=1;i<=iterations;i++){
					siblings = doctor.getSiblings();
				}
				long time_end = System.currentTimeMillis();
				long time = time_end - time_start;
				String salida = IO.doctorsToString(siblings);
				System.out.println("Los hermanos de \""+IDDoctor+"\" son "+salida);
				System.out.println("- Tiempo: "+time);			
			}
		}
		brOperations.close();
	}

	
	
	
}
