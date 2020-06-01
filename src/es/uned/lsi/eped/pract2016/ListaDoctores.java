package es.uned.lsi.eped.pract2016;

import es.uned.lsi.eped.DataStructures.DoctorIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;

/**
 * Lista de doctores: una ListaIF que usa internamente un array para garantizar
 * un acceso O(1) al elemento N de la lista. El array crecerá en un 50% cada vez
 * que no haya espacio para almacenar un nuevo espacio, lo que significa que
 * una inserción costará O(1) o O(N) en función de si hay o no espacio. Con esto
 * garantizamos que el coste es de O(1) amortizado, puesto que para cada K veces 
 * que hemos tenido que redimensionar el array los habremos usado eficientemente
 * por lo menos K/2 veces.
 * 
 * Actualmente la lista asume que las posiciones de los doctores en el array 
 * interno se corresponden por su ID.
 * 
 * Puesto que el ejercicio añade los doctores en forma secuencia, no se hace
 * ningún esfuerzo en ordenar los datos y se asume que al insertar éstos
 * se ordenarán solos.
 * 
 * La lista garantiza un acceso O(1) al doctor de un ID dado, asumiendo que
 * nada haya alterado el orden.
 *
 * Incluye también un iterador eficiente para recorrer la lista de doctores.
 * 
 * 
 * @author Héctor Luaces Novo
 * @param <T> Tipo de doctor que guardará la lista 
 */
public class ListaDoctores<T extends DoctorIF> implements ListIF<T> {
    private Object[] doctores;
    private int size;
    
    private class IteratorDoctores implements IteratorIF<T> {
        int posicion = 0;
        @Override
        
        public T getNext() {
            posicion++;
            
            return (T) doctores[posicion - 1];
        }

        @Override
        public boolean hasNext() {
            return size > posicion;
        }

        @Override
        public void reset() {
            posicion = 0;
        }
    }
    
    public ListaDoctores(int capacidad) {
        this.doctores = new Object[capacidad];
    }

    private boolean isFull() {
        return size == doctores.length;
    }
    
    private void grow() {
        Object[] aux = this.doctores;
        
        this.doctores = new Object[(int) (size * 1.5f)];
        
        for(int i = 0; i < aux.length; i++) {
            doctores[i] = aux[i];
        }
    }
    
    private void desplazarDerecha(int desde) {
        for(int i = size; i > desde; i--) {
            doctores[i] = doctores[i-1] ;
        }
        
        doctores[desde] = null;
    }
    
    private void desplazarIzquierda(int desde) {
        for(int i = desde; i < size; i++) {
            doctores[i] = doctores[i+1] ;
        }
        
        doctores[size - 1] = null;
    }
        
    @Override
    public T get(int id) {
        if ( size < id - 1 )
            return null;
        
        return (T) this.doctores[id -1];
    }

    @Override
    public void set(int pos, T e) {
        if ( pos >= doctores.length )
            return;
        
        if ( doctores[pos] == null )
            size++;
        
        doctores[pos] = e;
    }

    @Override
    public void insert(T elem, int pos) {
        if ( this.isFull() )
            this.grow();
 
        if ( pos == 0 )
            pos = size;
        else if ( doctores[pos] != null )
            desplazarDerecha(pos);
            
        doctores[pos] = elem;
        size++;
    }

    @Override
    public void remove(int pos) {
        if ( pos > size - 1 )
            return;
        
        if ( doctores[pos] == null )
            return;
        
        desplazarIzquierda(pos);
        size--;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(T e) {
        if ( size >= e.getID() )
            return false;
        
        return doctores[e.getID() - 1] != null;
    }

    @Override
    public void clear() {
        size = 0;
        this.doctores = new Object[doctores.length];
    }

    @Override
    public IteratorIF iterator() {
        return new IteratorDoctores();
    }
    
    public ListIF<T> toList() {
        ListIF<T> ret = new List<>();
        
        for(int i = 0; i < doctores.length; i++ ) {
            if ( doctores[i] == null )
                continue;
            
            ret.insert((T) doctores[i], 1);
        }
        
        return ret;
    }
    
    private void debug() {
        for(int i = 0; i < doctores.length; i++) {
            T doc = (T) doctores[i];
            
            if ( doc == null )
                continue;
            
            System.out.println(" - " + i + ": " + doc.getID());
        }
    }
    
    public static void main(String args[]) {
        ListaDoctores<DoctorS> l = new ListaDoctores(5);
        
        l.insert(new DoctorS(1), 0);
        l.insert(new DoctorS(2), 0);
        l.insert(new DoctorS(3), 0);
        l.insert(new DoctorS(4), 0);
        
        l.debug();
        l.insert(new DoctorS(5), 0);
        l.insert(new DoctorS(6), 0);
        l.insert(new DoctorS(7), 0);
        
        l.debug();
        l.insert(new DoctorS(8), 1);
        l.debug();
        l.remove(7);
        l.debug();
        l.remove(0);
        l.debug();
        l.remove(1);
        l.debug();
        
        IteratorIF<DoctorS> it = l.iterator();
        
        System.out.println("Iterador");
        while(it.hasNext()) {
            DoctorS doc = it.getNext();
            
            System.out.println(doc.getID());
        }
    }

}
