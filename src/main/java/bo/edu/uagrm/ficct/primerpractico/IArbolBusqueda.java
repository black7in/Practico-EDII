/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package bo.edu.uagrm.ficct.primerpractico;

import java.util.List;

/**
 *
 * @author HP
 * @param <Key>
 * @param <Value>
 */
public interface IArbolBusqueda<Key extends Comparable<Key>, Value>{
    void insertar(Key claveAInsertar, Value valorAInsertar) throws NullPointerException;
    Value eliminar(Key claveAEliminar) throws ExcepcionClaveNoExiste;
    Value buscar(Key claveABuscar);
    boolean contiene(Key claveABuscar);
    int size();
    int nivel();
    int altura();
    void vaciarArbol();
    boolean esArbolVacio();  
    List<Key> recorridoPorNiveles();
    List<Key> recorridoEnPreOrden();
    List<Key> recorridoEnInOrden();
    List<Key> recorridoEnPostOrden();
    
}
