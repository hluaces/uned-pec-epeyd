package es.uned.lsi.eped.pract2016;

import es.uned.lsi.eped.DataStructures.DoctorIF;
import es.uned.lsi.eped.DataStructures.AcademiaIF;
import es.uned.lsi.eped.DataStructures.CollectionIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;

/**
 * Doctor para la academia completa.
 * 
 * Un doctor de esta academia conoce sus sucesores y antecesores directos 
 * para garantizar una eficiencia de O(1) a la hora de consultarlos.
 * 
 * @author Héctor Luaces Novo
 */
public class DoctorC implements DoctorIF {
    private int id;
    private AcademiaC academia;
    private CollectionIF<DoctorIF> estudiantes;
    private CollectionIF<DoctorIF> supervisores;
    
    /**
     * Constructores para satisfacer el juego de pruebas del equipo docente
     * y su implementación de IO
     */
    public DoctorC() {    
        this.estudiantes = new List<>();
        this.supervisores = new List<>();
    }
    
    public DoctorC(int id) {
        this();
        
        this.id = id;
    }
    
    public DoctorC(AcademiaIF academia) {
        this();
        
        this.academia = (AcademiaC) academia;
    }
    
    public DoctorC(int id, AcademiaIF academia) {
        this();
        
        this.id = id;
        this.academia = (AcademiaC) academia;
    }
    
    public DoctorC(int id, AcademiaIF academia, CollectionIF<DoctorIF> supervisores) {
        this();
        
        this.id = id;
        this.academia = (AcademiaC) academia;
        this.supervisores = supervisores;
    }

    @Override
    public CollectionIF<DoctorIF> getStudents() {
        return this.estudiantes;
    }
    
    @Override
    public int getID() {
        return this.id;
    }
    
        
    public CollectionIF<DoctorIF> getSupervisors() {
        return this.supervisores;
    }

    /**
     * Método privado recursivo usado para encontrar los antecesores de un doctor.
     * 
     * Usa una ListaDoctores con espacio suficiente para albergar a toda la 
     * academia y controlar así los duplicados al mismo tiempo que minifica
     * el acceso.
     * 
     * @param generations Número de generaciones que queremos obtener.
     * @param doc Doctor para el que queremos obtener las generaciones.
     * @param ret Lista de antecesores que lleva el método recursivo hasta ahora
     * @return La lista de los antecesores de dichas generaciones para el doctor
     * seleccionado.
     */
    private ListaDoctores<DoctorIF> getAncestors(int generations, DoctorC doc, ListaDoctores<DoctorIF> ret) {
        if ( generations < 1 || doc.getSupervisors().size() == 0)
            return ret;
        
        IteratorIF<DoctorIF> it = doc.getSupervisors().iterator();
        
        while(it.hasNext()) {
            DoctorC sup = (DoctorC) it.getNext();
            
            if ( ! ret.contains(sup) )
                ret.set(sup.getID() - 1, sup);
            
            getAncestors(generations -1, sup, ret);
        }
        
        return ret;
    }
    
    @Override
    public CollectionIF<DoctorIF> getAncestors(int generations) {
        return getAncestors(
            generations, this, new ListaDoctores<DoctorIF>(academia.size())
        ).toList();
    }

    
    /**
     * Método privado recursivo usado para encontrar los descendientes de un doctor.
     * 
     * Usa una ListaDoctores con espacio suficiente para albergar a toda la 
     * academia y controlar así los duplicados al mismo tiempo que minifica
     * el acceso.
     * 
     * @param generations Número de generaciones que queremos obtener.
     * @param doc Doctor para el que queremos obtener las generaciones.
     * @param ret Lista de descendientes que lleva el método recursivo hasta ahora
     * @return La lista de los descendientes de dichas generaciones para el doctor
     * seleccionado.
     */
    private ListaDoctores<DoctorIF> getDescendants(int generations, DoctorC doc, ListaDoctores<DoctorIF> ret) {
        if ( generations < 1 || doc.getStudents().size() == 0)
            return ret;
        
        IteratorIF<DoctorIF> it = doc.getStudents().iterator();
        
        while(it.hasNext()) {
            DoctorC sup = (DoctorC) it.getNext();
            
            if ( ! ret.contains(sup) )
                ret.set(sup.getID() - 1, sup);
            
            getDescendants(generations -1, sup, ret);
        }
        
        return ret;
    }
        
    @Override
    public CollectionIF<DoctorIF> getDescendants(int generations) {
        return getDescendants(
            generations, this, new ListaDoctores<DoctorIF>(academia.size())
        ).toList();
    }

    @Override
    public CollectionIF<DoctorIF> getSiblings() {
        ListaDoctores<DoctorIF> ret = new ListaDoctores<>(academia.size());
        
        if ( this.supervisores.isEmpty() )
            return ret;
        
        IteratorIF<DoctorIF> it = this.supervisores.iterator();
        
        while(it.hasNext()) {
            DoctorC doc = (DoctorC) it.getNext();
            
            if ( ret.contains(doc) )
                continue;
            
            IteratorIF<DoctorIF> st = doc.getStudents().iterator();
            
            while(st.hasNext()) {
                DoctorC student = (DoctorC) st.getNext();
                
                if ( student.getID() != id )
                    ret.set(student.getID() - 1, student);
            }
        }
        
        return ret.toList();
    }
        
    /**
     * EStablece la academia de éste doctor
     * @param a Academia del doctor
     */
    public void setAcademia(AcademiaIF a) {
        this.academia = (AcademiaC) a;
    }

    /**
     * Añade un supervisor a la lista del doctor
     * @param supervisor Supervisor a añadir a la lista del estudiante (se
     * comprueban duplicados)
     */
    public void addSupervisor(DoctorIF supervisor) {
        ListIF<DoctorIF> l = (ListIF<DoctorIF>) this.supervisores;

        if ( ! l.contains(supervisor) )
            l.insert(supervisor, 1);
    }

    /**
     * Añade un estudiante a la lista del doctor
     * @param student Estudiante a añadir. Se comprueban duplicados.
     */
    public void addStudent(DoctorIF student) {
        ListIF<DoctorIF> l = (ListIF<DoctorIF>) this.estudiantes;
            
        if ( ! l.contains(student) )
            l.insert(student, 1);
    }
}
