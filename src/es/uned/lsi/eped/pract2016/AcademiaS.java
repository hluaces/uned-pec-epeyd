package es.uned.lsi.eped.pract2016;

import es.uned.lsi.eped.DataStructures.DoctorIF;
import es.uned.lsi.eped.DataStructures.AcademiaIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.Tree;
import es.uned.lsi.eped.DataStructures.TreeIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.CollectionIF;

/**
 * Academia sencilla basado en un árbol N-ario para implementar las relaciones
 * supervisor->estudiante.
 * 
 * Se apoya en el hecho de que cada doctor conoce directamente su supervisor.
 * 
 * @author Héctor Luaces NOvo
 */
public class AcademiaS implements AcademiaIF {
    private TreeIF<DoctorIF> doctores;
    
    public AcademiaS() {
        this.doctores = new Tree<>();
    }

    @Override
    public int size() {
        return this.doctores.size();
    }

    @Override
    public boolean isEmpty() {
        return this.doctores.isEmpty();
    }

    @Override
    public boolean contains(DoctorIF e) {
        return this.doctores.contains(e);
    }

    @Override
    public void clear() {
        this.doctores.clear();
    }

    @Override
    public IteratorIF<DoctorIF> iterator() {
        return this.doctores.iterator(TreeIF.PREORDER);
    }
            
    public TreeIF<DoctorIF> getDoctores() {
        return this.doctores;
    }
    
    /**
     * Dado el ID de un doctor y un árbol (que se asume es una rama de la
     * academia) devuelve la rama que coincide con el doctor.
     * 
     * Es un método recursivo auxiliar.
     * 
     * @param id Id del doctor a averiguar
     * @param arbol Árbol en el que estamos buscando actualmente
     * @return Rama del doctor o null si no existe
     */
    private TreeIF<DoctorIF> getRamaDoctor(int id, TreeIF<DoctorIF> arbol) {
        if ( arbol.isEmpty() )
            return null;
        
        if ( arbol.getRoot().getID() == id )
            return arbol;
        
        IteratorIF <TreeIF<DoctorIF>> it = arbol.getChildren().iterator();
        
        while(it.hasNext()) {
            TreeIF<DoctorIF> rama_hijo = it.getNext();
            rama_hijo = getRamaDoctor(id, rama_hijo);
            
            if ( rama_hijo != null )
                return rama_hijo;
        }
        
        return null;
    }
    
    /**
     * Método recursivo que, dado un ID de doctor, devuelve la rama del árbol
     * de la academia que lo tiene como raíz.
     * 
     * @param id ID del doctor a buscar
     * @return Rama del árbol de la academia que tiene como raíz a dicho doctor
     */
    private TreeIF<DoctorIF> getRamaDoctor(int id) {
        return this.getRamaDoctor(id, this.doctores);
    }
    
    @Override
    public DoctorIF getFounder() {
        return this.doctores.getRoot();
    }  
    
    @Override
    public DoctorIF getDoctor(int id) {
        IteratorIF<DoctorIF> it = this.iterator();
        
        while (it.hasNext()) {
            DoctorIF d = it.getNext();
            
            if ( d.getID() == id )
                return d;
        }
        
        return null;
    }

    
    @Override
    public void addDoctor(DoctorIF newDoctor, DoctorIF supervisor) {
        if ( getFounder() == null ) {
            this.doctores.setRoot(newDoctor);
            return;
        }
        TreeIF<DoctorIF> rama = getRamaDoctor(supervisor.getID());
        TreeIF<DoctorIF> hijo = new Tree<DoctorIF>(newDoctor);
        ((DoctorS) newDoctor).setSupervisor(supervisor);
        rama.addChild(1, hijo);
    }

    @Override
    public void addSupervision(DoctorIF student, DoctorIF supervisor) {
        TreeIF<DoctorIF> rama = this.getRamaDoctor(supervisor.getID());
        
        if ( rama.contains(student) )
            return;
        
        rama.addChild(1, new Tree<>(student));
        ((DoctorS) student).setSupervisor(supervisor);
    }
    
    /**
     * Busca en el árbol de la academia el doctor que pasamos como parámetro y 
     * devuelve la rama de su supervisor.
     * 
     * @param id ID del doctor para el que queremos conocer la rama de su super
     * visor
     * @return La rama del doctor supervisor o null
     */
    private TreeIF<DoctorIF> findRamaSupervisor(int id) {
        DoctorS doc = (DoctorS) getDoctor(id);
        
        if ( doc.getSupervisor() == null )
            return null;
        
        return getRamaDoctor(doc.getSupervisor().getID());
    }
    
    /**
     * Devuelve el doctor supervisor que corresponde con un ID de doctor.
     * 
     * @param id ID para el que queremos conocer el supervisor.
     * @return Doctor supervisor para el doctor pasado como parámetro.
     */
    public DoctorIF getSupervisor(int id) {
        DoctorS doc = (DoctorS) getDoctor(id);
        
        return doc.getSupervisor();
    }
    
    /**
     * Método recursivo auxiliar que desciende por los hijos de un doctor para
     * devolver un número de descendientes en función de las generaciones
     * pasadas como parámetro.
     * 
     * @param generations Generaciones de descendientes que queremos.
     * @param arbol Árbol (rama) por la que vamos a profundizar.
     * @param ret Lista con los doctores que hemos encontrado hasta ahora
     * @return La lista de los descendientes para las generaciones pasadas
     * como parámetro.
     */
    private CollectionIF<DoctorIF> getDescendants(
        int generations, TreeIF<DoctorIF> arbol, List<DoctorIF> ret
    ) {
        if ( arbol.isEmpty() || generations < 1 )
            return ret;
        
        IteratorIF<TreeIF<DoctorIF>> it = arbol.getChildren().iterator();
        
        while(it.hasNext()) {
            TreeIF<DoctorIF> arbol_hijo = it.getNext();
            
            getDescendants(generations - 1, arbol_hijo, ret);
            ret.insert(arbol_hijo.getRoot(), 1);
        }    

        return ret;
    }
    
    /**
     * Método recursivo que recorre todo el árbol para buscar las descendientes
     * de un doctor.
     * 
     * @param id_doctor ID del doctor para el que queremos conocer los descendientes.
     * @param generations Generaciones de descendientes que queremos conocer.
     * @return La lista con los descendientes académicos del doctor
     */
    public CollectionIF<DoctorIF> getDescendants(int id_doctor, int generations) {
        return this.getDescendants(generations, getRamaDoctor(id_doctor), new List<DoctorIF>());
    }
    
    /**
     * Devuelve una lista con los hermanos de un doctor pasado como parémtro.
     * 
     * @param id_doctor el ID del doctor para el que queremos conocer los hemanos
     * @return colección con los hermanos del doctor
     */
    public CollectionIF<DoctorIF> getSiblings(int id_doctor) {
        TreeIF<DoctorIF> rama_supervisor = this.findRamaSupervisor(id_doctor);
        List<DoctorIF> lista_hermanos = new List<DoctorIF>();
        
        if ( rama_supervisor == null )
            return lista_hermanos;

        
        IteratorIF<TreeIF<DoctorIF>> it = rama_supervisor.getChildren().iterator();
        
        while(it.hasNext()) {
            DoctorIF d = it.getNext().getRoot();
            
            if ( d.getID() == id_doctor )
                continue;
            
            lista_hermanos.insert(d, 1);
        }
        
        return lista_hermanos;
    }
    
    /**
     * Método recursivo auxiliar que asciende por los supervisores de un doctor para
     * devolver un número de ancestros en función de las generaciones
     * pasadas como parámetro.
     * 
     * @param doc Doctor para el que queremos conocer los ancestros
     * @param generations Generaciones de ancestros que queremos.
     * @return La colección de los ancestrospara las generaciones pasadas
     * como parámetro.
     */
    public CollectionIF<DoctorIF> getAncestors(DoctorS doc, int generations) {
        List<DoctorIF> ret = new List<>();

        doc = (DoctorS) doc.getSupervisor();
        
        while(generations-- > 0 && doc != null) {
            ret.insert(doc, 1);
            doc = (DoctorS) doc.getSupervisor();
        }
        
        return ret;
    }

    void setFounder(DoctorS founder) {
        this.doctores.setRoot(founder);
    }
}
