/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uned.lsi.eped.pract2016;

import es.uned.lsi.eped.DataStructures.CollectionIF;

public interface AcademiaIF extends CollectionIF<DoctorIF> {
    /**
     * Consulta el doctor que fundó la academia
     */
     public DoctorIF getFounder();
     
     /**
      * Busca un doctor dentro de la Academia a partir de su identificador.
      * 
      * @pre El doctor pertenence a la cademia && id > 0
      * @param id el identificador del doctor a buscar
      * @return el Doctor buscado
      */
     public DoctorIF getDoctor(int id);     
     
     /**
      * Consulta el número de doctores que pertenence a la academia.
      * @return el número de doctores perteneciente a la academia
      */
     public int size();
     
     /**
      * Añade un nuevo doctor a la academia a partir de la lectura de su tesis
      * @pre newDoctor no debe existir en la academia y el supervisor si debe
      * @param newDoctor El nuevo doctor 
      * @param supervisor Su primer director d etesis
      */
     public void addDoctor(DoctorIF newDoctor, DoctorIF supervisor);
     
     /**
      * Añade una relación de dirección al árbol genealógico de la academia
      * 
      * @pre ambos doctores deben existir y no tener una relación previa entre ellos
      * @param student El nuevo doctor para el que queremos añadir una supervisión
      * @param supervisor El codirector de tesis del doctor
      */
     public void addSupervision(DoctorIF student, DoctorIF supervisor);
    

}