
package es.uned.lsi.eped.pract2016;

import es.uned.lsi.eped.DataStructures.DoctorIF;
import es.uned.lsi.eped.DataStructures.AcademiaIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;
    
/**
 * Academia completa basada en una ListaDoctores (lista) y una serie de nodos
 * (doctores) que conocen la información de sus antecesores y sucesores directos.
 * 
 * Esto último, unido a la ListaDoctores ayuda a mantener el crecimiento 
 * asintótico en un lugar asumible.
 *  
 * @author Héctor Luaces Novo
 */
public class AcademiaC implements AcademiaIF {
    /**
     * La academiaC usará una ListaDoctores para guardar una referencia de
     * todos los doctores y garantizar una eficiciencia de o(1) en el acceso.
     */
    private ListaDoctores<DoctorC> doctores;
    /**
     * El fundador de la academia
     */
    private DoctorC founder;
    
    /**
     * Constructores para satisfacer el juego de pruebas y empírico del equipo
     * docente.
     */
    public AcademiaC() {
        doctores = new ListaDoctores(20); 
    }
    
    public AcademiaC(DoctorC fundador) {
        this();
        
        this.founder = fundador;
    }
    
    @Override
    public DoctorIF getFounder() {
        return founder;
    }

    @Override
    public DoctorIF getDoctor(int id) {
        return this.doctores.get(id);
    }

    @Override
    public int size() {
        return this.doctores.size();
    }

    @Override
    public void addDoctor(DoctorIF newDoctor, DoctorIF supervisor) {
        this.doctores.insert((DoctorC) newDoctor, 0);
        
        // Tras añadir el doctor es necesario actualizar los objetos de las 
        // relaciones
        this.addSupervision(newDoctor, supervisor);
    }

    @Override
    public void addSupervision(DoctorIF student, DoctorIF supervisor) {
        if ( supervisor == null )
            return;
        
        // Añadimos estudiante y supervisor a sendos doctores
        ( (DoctorC) student).addSupervisor(supervisor);
        ( (DoctorC) supervisor).addStudent(student);
    }

    @Override
    public boolean isEmpty() {
        return this.doctores.isEmpty();
    }

    @Override
    public boolean contains(DoctorIF e) {
        return this.doctores.contains((DoctorC) e);
    }

    @Override
    public void clear() {
        this.doctores.clear();
    }

    @Override
    public IteratorIF<DoctorIF> iterator() {
        return this.doctores.iterator();
    }
    
    /**
     * Establece un nuevo fundador
     * @param doc Doctor a establecer como fundador
     */
    public void setFounder(DoctorIF doc) {
        founder = (DoctorC) doc;
        addDoctor(doc, null);
    }
}
