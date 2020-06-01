package es.uned.lsi.eped.DataStructures;

import es.uned.lsi.eped.DataStructures.CollectionIF;

/**
 * Representación de un doctor perteneciente a una academia
 */
public interface DoctorIF {
    /**
     * Consulta lso acnestros académicos del doctor, limitándose al número 
     * de generaciones indicado por el parámetro.
     * 
     * @param generations Número de generaciones a considerar
     * @pre generations > 0 
     * @return 
     */
    public CollectionIF<DoctorIF> getAncestors(int generations);
    
    /**
     * Consulta los doctores a quienes el doctor ha dirigido sus Tesis.
     * 
     * @return La colección de doctores cuyo director de tesis es el doctor
     */
    public CollectionIF<DoctorIF> getStudents();   
    
    /**
     * Consulta los descendientes académicos del doctor, limitándose
     * al número de generaciones indicado por el parámetro.
     * 
     * @pre generations > 0 
     * @param generations número de generaciones a considerar
     * @return La lista de descendientes para las generaciones especificadas
     */
    public CollectionIF<DoctorIF> getDescendants(int generations);
    
    /**
     * Consulta los doctores que comparten director de tesis con el doctor.
     * 
     * @return La colección de hermamos académicos del doctor. NO deberá
     * contener repeticiones ni al doctor llamante.
     */
    public CollectionIF<DoctorIF> getSiblings();
    
    /**
     * Obtiene el identificador del doctor
     * @return El identificador del doctor, único en la academia.
     */
    public int getID();    
}
