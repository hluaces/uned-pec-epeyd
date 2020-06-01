package es.uned.lsi.eped.pract2016;

import es.uned.lsi.eped.DataStructures.DoctorIF;
import es.uned.lsi.eped.DataStructures.AcademiaIF;
import es.uned.lsi.eped.DataStructures.CollectionIF;

/**
 * Doctor para una academia sencilla. Guarda internamente una referencia
 * a su supervisor para garantizar un acceso O(1) al mismo.
 * 
 * @author Héctor Luaces Novo
 */
public class DoctorS implements DoctorIF {
    private int id;
    private AcademiaS academia;
    private DoctorIF supervisor;
    public DoctorS() {
    }
        
    public DoctorS(int id) {
        this.id = id;
    }
    
    public DoctorS(int id, AcademiaIF academia) {
        this.id = id;
        this.academia = (AcademiaS) academia;
    }

    public DoctorS(int id, AcademiaIF academia, DoctorIF supervisor) {
        this.id = id;
        this.academia = (AcademiaS) academia;
        this.supervisor = supervisor;
    }
    
    public DoctorIF getSupervisor() {
        return this.supervisor;
    }

    @Override
    public CollectionIF<DoctorIF> getAncestors(int generations) {
        return this.academia.getAncestors(this, generations);
    }

    @Override
    public CollectionIF<DoctorIF> getStudents() {
        return this.getDescendants(1);
    }

    @Override
    public CollectionIF<DoctorIF> getDescendants(int generations) {
        return this.academia.getDescendants(id, generations);
    }

    @Override
    public CollectionIF<DoctorIF> getSiblings() {
        return this.academia.getSiblings(id);
    }

    public AcademiaS getAcademia() {
        return academia;
    }

    @Override
    public int getID() {
        return this.id;
    }

    void setAcademia(AcademiaS a) {
        this.academia = (AcademiaS) a;
    }

    public void setSupervisor(DoctorIF supervisor) {
        this.supervisor = supervisor;
    }
}
