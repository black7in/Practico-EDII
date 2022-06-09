/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.uagrm.ficct.primerpractico;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author HP
 * @param <Key>
 * @param <Value>
 */
public class ArbolMViasBusqueda<Key extends Comparable<Key>, Value> implements IArbolBusqueda<Key, Value> {
    protected NodoMVias<Key, Value> raiz;
    protected int orden;
    protected static final int POSICION_NO_VALIDA = -1;
    protected static final int ORDEN_MINIMO = 3;

    public ArbolMViasBusqueda(){
        this.orden = ArbolMViasBusqueda.ORDEN_MINIMO;
    }

    public ArbolMViasBusqueda(int orden) throws ExcepcionOrdenInvalido{
        if(orden < ArbolMViasBusqueda.ORDEN_MINIMO){
            throw new ExcepcionOrdenInvalido();
        } 
        this.orden = orden;
    }
    
    /*
        10. Para el Arbol MVias de busqueda implemente el metodo insertar
    */
    @Override
    public void insertar(Key claveAInsertar, Value valorAInsertar) {
        if (valorAInsertar == null) {
            throw new RuntimeException("No se permite insertar valores nulos");
        }
        if (claveAInsertar == null) {
            throw new RuntimeException("No se permite insertar claves nulas");
        }
        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
            return;
        }

        NodoMVias<Key, Value> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            int posicionDeClave = this.obtenerPosicionDeClave(nodoActual, claveAInsertar);
            if (posicionDeClave != POSICION_NO_VALIDA) {
                nodoActual.setValor(posicionDeClave, valorAInsertar);//remplaza el valor de esa clave
                nodoActual = NodoMVias.nodoVacio();
            } else {
                if (nodoActual.esHoja()) {
                    if (nodoActual.estanClavesLlenas()) {
                        int posicionPorDondeBajar = this.obtenerPosicionPorDondeBajar(nodoActual, claveAInsertar);//?
                        NodoMVias<Key, Value> nuevoHijo = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
                        nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
                    } else {
                        this.insertarClaveYValorOrdenadoEnNodo(nodoActual, claveAInsertar, valorAInsertar);
                    }
                    nodoActual = NodoMVias.nodoVacio();
                } else {
                    int posicionPorDondeBajar = this.obtenerPosicionPorDondeBajar(nodoActual, claveAInsertar);
                    if (nodoActual.esHijoVacio(posicionPorDondeBajar)) {
                        NodoMVias<Key, Value> nuevoHijo = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
                        nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
                        nodoActual = NodoMVias.nodoVacio();
                    } else {
                        nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
                    }
                }
            }
        }
    }
    
    /*
        11. Para el Arbol Mvias de Busqueda implemente el metodo eliminar.
    */
    @Override
    public Value eliminar(Key claveAEliminar) throws ExcepcionClaveNoExiste {
        Value valorAEliminar = this.buscar(claveAEliminar);
        if (valorAEliminar == null) {
            throw new ExcepcionClaveNoExiste();
        }

        this.raiz = eliminar(this.raiz, claveAEliminar);
        return valorAEliminar;
    }
    
    private NodoMVias<Key, Value> eliminar(NodoMVias<Key, Value> nodoActual, Key claveAEliminar) {
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            Key claveActual = nodoActual.getClave(i);
            if (claveAEliminar.compareTo(claveActual) == 0) {
                if (nodoActual.esHoja()) {
                    this.eliminarClaveYValorDelNodo(nodoActual, i);
                    if (nodoActual.cantidadDeClavesNoVacias() == 0) {
                        return NodoMVias.nodoVacio();
                    }
                    return nodoActual;
                }

                Key claveDeReemplazo;
                if (this.hayHijosMasAdelante(nodoActual, i)) {
                    claveDeReemplazo = this.buscarClaveSucesoraInOrden(nodoActual, claveAEliminar);
                } else {
                    claveDeReemplazo = this.buscarClavePredecesoraInOrden(nodoActual, claveAEliminar);
                }
                Value valorDeReemplazo = buscar(claveDeReemplazo);
                nodoActual = eliminar(nodoActual, claveDeReemplazo);
                nodoActual.setClave(i, claveDeReemplazo);
                nodoActual.setValor(i, valorDeReemplazo);
                return nodoActual;
            }

            //no esta en la posicion i del nodoActual
            if (claveAEliminar.compareTo(claveActual) < 0) {
                NodoMVias<Key, Value> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(i), claveAEliminar);
                nodoActual.setHijo(i, supuestoNuevoHijo);
                return nodoActual;
            }
        }

        //si llego sin retornar, quiere decir que nunca baje por ningun lado, ni la encontre
        NodoMVias<Key, Value> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()), claveAEliminar);
        nodoActual.setHijo(nodoActual.cantidadDeClavesNoVacias(), supuestoNuevoHijo);
        return nodoActual;
    }
    
    @Override
    public Value buscar(Key claveABuscar) throws NullPointerException {
        if(claveABuscar == null)
            throw new NullPointerException("Clave a buscar no puede ser nula");
            
        NodoMVias<Key, Value> nodoActual = this.raiz;
        while(!NodoMVias.esNodoVacio(nodoActual)){
            boolean huboCambioDeNodoActual = false;
            for(int i = 0; i < nodoActual.cantidadDeClavesNoVacias() && !huboCambioDeNodoActual; i++){
                Key claveDeNodoActual = nodoActual.getClave(i);
                if(claveABuscar.compareTo(claveDeNodoActual) == 0){
                    return nodoActual.getValor(i);
                }
                
                if(claveABuscar.compareTo(claveDeNodoActual) < 0){
                    nodoActual = nodoActual.getHijo(i);
                    huboCambioDeNodoActual = true;
                }
            }
            if(!huboCambioDeNodoActual){
                nodoActual = nodoActual.getHijo(orden - 1);
            }
        
        }
        return null;
    }
    
    @Override
    public boolean contiene(Key claveABuscar) {
        return this.buscar(claveABuscar) != null;
    }
    
    @Override
    public int size() {
        if (this.esArbolVacio()) {
            return 0;
        }

        int cantidadDeNodos = 0;
        Queue<NodoMVias<Key, Value>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);

        while (!colaDeNodos.isEmpty()) {// recorrido por niveles
            NodoMVias<Key, Value> nodoActual = colaDeNodos.poll();
            for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
                cantidadDeNodos++;
                if (!nodoActual.esHijoVacio(i)) {
                    colaDeNodos.offer(nodoActual.getHijo(i));
                }
            }
            if (!nodoActual.esHijoVacio(nodoActual.cantidadDeClavesNoVacias())) {
                colaDeNodos.offer(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()));
            }
        }

        return cantidadDeNodos;
    }
    
    @Override
    public int altura() {
        return altura(this.raiz);
    }

    protected int altura(NodoMVias<Key, Value> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int alturaMayor = 0;
        for (int i = 0; i < this.orden; i++) {
            int alturaActual = altura(nodoActual.getHijo(i));
            if (alturaActual > alturaMayor) {
                alturaMayor = alturaActual;
            }
        }
        return alturaMayor + 1;
    }
    
    @Override
    public void vaciarArbol() {
        this.raiz = this.nuevoNodoVacio();
    }
    
    @Override
    public boolean esArbolVacio() {
        return NodoMVias.esNodoVacio(this.raiz);
    }
    
    protected int obtenerPosicionDeClave(NodoMVias<Key, Value> nodoActual, Key claveAInsertar) {
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            if (nodoActual.getClave(i).compareTo(claveAInsertar) == 0) {//si las claves son iguales
                return i;
            }
        }
        return -1;
    }
    
    protected int obtenerPosicionPorDondeBajar(NodoMVias<Key, Value> nodoActual, Key claveAInsertar) {
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            if (claveAInsertar.compareTo(nodoActual.getClave(i)) < 0) {//si la clave a insertar es menor 
                return i;
            }
        }
        
        //si llego hasta aqui quiere decir que la clave a insertar es mayor que la ultima clave del nodo
        return (nodoActual.cantidadDeClavesNoVacias()); 
    }
    
    protected void insertarClaveYValorOrdenadoEnNodo(NodoMVias<Key, Value> nodoActual, Key claveAInsertar, Value valorAInsertar) {
        int posicion = nodoActual.cantidadDeClavesNoVacias();
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            if (claveAInsertar.compareTo(nodoActual.getClave(i)) < 0) {
                posicion = i;//posicion donde insertar
                break;// para saltar las demas instrucciones del for y pasar a la sgt instruccion del metodo
            }
        }
        
        // si posicion no se altera quiere decir que la clave a insertar en mayor que las claves anteriores
        //inserta en la ultima posicion vacia
        if (posicion == nodoActual.cantidadDeClavesNoVacias()) {
            nodoActual.setClave(posicion, claveAInsertar);
            nodoActual.setValor(posicion, valorAInsertar);
            return;//return para que se salga del metodo y no realize las demas instrucciones del metodo
        }

        //recorremos los nodos una casilla a la derecha 
        for (int j = nodoActual.cantidadDeClavesNoVacias() ; j > posicion; j--) {
            nodoActual.setClave(j, nodoActual.getClave(j-1));
            nodoActual.setValor(j, nodoActual.getValor(j-1));
            nodoActual.setHijo(j+1, nodoActual.getHijo(j));//esto nodoActual.setHijo(j, nodoActual.getHijo(j-1));
        }
        //insertamos la clave y el valor en la posicion correspondiente
        nodoActual.setClave(posicion, claveAInsertar);
        nodoActual.setValor(posicion, valorAInsertar);
        nodoActual.setHijo(posicion, NodoMVias.nodoVacio());//esto
    }
    private boolean hayHijosMasAdelante(NodoMVias<Key, Value> nodoActual, int i) {
        for (int j = i+1; j < this.orden; j++) {
            if (!nodoActual.esHijoVacio(j)) {
                return true;
            }
        }
        return false;
    }
    protected void eliminarClaveYValorDelNodo(NodoMVias<Key, Value> nodoActual, int i) {
        //el nodo es hoja por lo tanto no tienen hijos y no hace falta moverlos
        int cantDeClavesNoVacias = nodoActual.cantidadDeClavesNoVacias();
        nodoActual.setClave(i, (Key)NodoMVias.datoVacio());
        nodoActual.setValor(i, (Value)NodoMVias.datoVacio());

        if (i+1 < cantDeClavesNoVacias) {//verifica que tenga mas claves adelante
            //traemos las claves y valores de derecha hacia izq
            for (int j = i; j < cantDeClavesNoVacias-1; j++) {
                nodoActual.setClave(j, nodoActual.getClave(j+1));
                nodoActual.setValor(j, nodoActual.getValor(j+1));
            }

            //eliminar la clave y valor de la ultima posicion
            if (cantDeClavesNoVacias > 0) {
                nodoActual.setClave(cantDeClavesNoVacias-1, (Key)NodoMVias.datoVacio());
                nodoActual.setValor(cantDeClavesNoVacias-1, (Value)NodoMVias.datoVacio());
            }
        }
    }
    private Key buscarClaveSucesoraInOrden(NodoMVias<Key, Value> nodoActual, Key claveAEliminar) {       
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return ((Key)NodoMVias.datoVacio());
        }

        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            if (nodoActual.getClave(i).compareTo(claveAEliminar) == 0) {//encuentro la posicion donde esta la claveAEliminar
                if (!nodoActual.esHijoVacio(i)) {//pregunto si tiene hijo esa clave
                    nodoActual = nodoActual.getHijo(i);
                    while (!NodoMVias.esNodoVacio(nodoActual)) {//recorro los nodos en InOrden
                        claveAEliminar = nodoActual.getClave(0);
                        nodoActual = nodoActual.getHijo(0);
                    }
                    return claveAEliminar;
                }

                if (i+1 < nodoActual.cantidadDeClavesNoVacias()) {//si no tiene hijo en la pos i, su sucesor seria la clave siguiente
                    if (!nodoActual.esHijoVacio(i+1)) {//si la clave siguiente tiene hijo se recorre en InOrden el hijo
                        nodoActual = nodoActual.getHijo(i+1);
                        while (!NodoMVias.esNodoVacio(nodoActual)) {//recorro los nodos en InOrden
                            claveAEliminar = nodoActual.getClave(0);
                            nodoActual = nodoActual.getHijo(0);
                        }
                    } else {//si no tiene hijo la clave siguiente, entonces el sucesor es la clave siguiente
                        claveAEliminar = nodoActual.getClave(i+1);
                    }
                    return  claveAEliminar;
                }
            }
        }//fin del for

        //si llego hasta aca quiere decir que su sucesor esta en el ultimo hijo
        nodoActual = nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias());
        while (!NodoMVias.esNodoVacio(nodoActual)) {//recorremos los nodos en InOrden
            claveAEliminar = nodoActual.getClave(0);
            nodoActual = nodoActual.getHijo(0);
        }

        return claveAEliminar;
    }
    private Key buscarClavePredecesoraInOrden(NodoMVias<Key, Value> nodoActual, Key claveAEliminar) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return (Key)NodoMVias.datoVacio();
        }

        List<Key> recorrido = new LinkedList<>();
        Stack<NodoMVias<Key, Value>> pilaDeNodos = new Stack<>();
        NodoMVias<Key, Value> nodoRecorrido = this.raiz;
        this.insertarEnPilaParaInOrden(nodoRecorrido, pilaDeNodos);//llenamos la pila con los nodos del arbol
        boolean huboCambioDeNodo = false;

        while (!pilaDeNodos.isEmpty()) {
            nodoRecorrido = pilaDeNodos.pop();
            for (int i = 0; i < nodoRecorrido.cantidadDeClavesNoVacias() && !huboCambioDeNodo; i++) {
                if (!recorrido.contains(nodoRecorrido.getClave(i))) {//pregunta si en el recorrido no esta esa clave(para no tener clavesrepetidas)

                    if (nodoRecorrido.getClave(i).compareTo(claveAEliminar) == 0) {//si la clave que voy a insertar es igual a la claveAEliminar
                        claveAEliminar = recorrido.get(recorrido.size()-1);//retorna el ultimo del recorrido(osea el predecesor)
                        return claveAEliminar;
                    }

                    recorrido.add(nodoRecorrido.getClave(i));
                    if (!nodoRecorrido.esHijoVacio(i+1)) {//pregunta si tiene hijo mas adelante
                        if (i+1 < nodoRecorrido.cantidadDeClavesNoVacias()) {//si el nodoActual aun tiene datos que no se insertaron al recorrido
                            //aqui volvemos a insertar el nodoActual
                            pilaDeNodos.push(nodoRecorrido);
                        }
                        this.insertarEnPilaParaInOrden(nodoRecorrido.getHijo(i+1), pilaDeNodos);
                        huboCambioDeNodo = true;//para que pare el for
                    }
                }
            }
            huboCambioDeNodo = false;
        }
        return claveAEliminar;
    }
    private void insertarEnPilaParaInOrden(NodoMVias<Key, Value> nodoActual, Stack<NodoMVias<Key, Value>> pilaDeNodos) {
        while (!NodoMVias.esNodoVacio(nodoActual)) {           
            pilaDeNodos.push(nodoActual);
            nodoActual = nodoActual.getHijo(0);
        }  
    }
    
    private void irAlNivelN(Queue<NodoMVias<Key, Value>> colaDeNodos, int nivelN) {
        while (!colaDeNodos.isEmpty() && nivelN > 0) {            
            int cantDeNodosDelNivel = colaDeNodos.size();
            int contador = 0;
            while (contador < cantDeNodosDelNivel) {
                NodoMVias<Key, Value> nodoActual = colaDeNodos.poll();
                if (!nodoActual.esHoja()) {
                    for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
                        if (!nodoActual.esHijoVacio(i)) {
                            colaDeNodos.offer(nodoActual.getHijo(i));
                        }
                    }
                    if (!nodoActual.esHijoVacio(nodoActual.cantidadDeClavesNoVacias())) {
                        colaDeNodos.offer(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()));
                    }
                }
                contador++;
            }
            nivelN--;
        }
    }

    
    protected NodoMVias<Key, Value> nuevoNodoVacio(){ // crea un nuevo nodo vacio
        return (NodoMVias<Key, Value>) NodoMVias.nodoVacio();
    }

    
  @Override
    public List<Key> recorridoPorNiveles() {
        List<Key> recorrido = new ArrayList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        
        Queue<NodoMVias<Key, Value>> coladeNodos = new LinkedList<>();
        coladeNodos.offer(this.raiz);
        while (!coladeNodos.isEmpty()) {
            NodoMVias<Key, Value> nodoActual = coladeNodos.poll();
            for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
                recorrido.add(nodoActual.getClave(i));
                if (!nodoActual.esHijoVacio(i)) {
                    coladeNodos.offer(nodoActual.getHijo(i));
                }
            }
            if (!nodoActual.esHijoVacio(nodoActual.cantidadDeClavesNoVacias())) {
                coladeNodos.offer(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()));
            }

        }
        return recorrido;
    }

    
    /*
        3. Para un Arbol MVias implementar recorrido en Post Orden 
    */

    @Override
    public List<Key> recorridoEnPostOrden() {
        List<Key> recorrido = new ArrayList<>();
        this.recorridoEnPostOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnPostOrden(NodoMVias<Key, Value> nodoActual, List<Key> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        recorridoEnPostOrden(nodoActual.getHijo(0), recorrido);
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            recorridoEnPostOrden(nodoActual.getHijo(i + 1), recorrido);
            recorrido.add(nodoActual.getClave(i));
        }
    }

    /*
        4. Para un Arbol MVias implementar recorrido Pre Orden
    */
    @Override
    public List<Key> recorridoEnPreOrden() {
        List<Key> recorrido = new ArrayList<>();
        this.recorridoPreOrden(this.raiz, recorrido);
        return recorrido;
    }
    private void recorridoPreOrden(NodoMVias<Key, Value> nodoActual, List<Key> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }

        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            recorrido.add(nodoActual.getClave(i));
            recorridoPreOrden(nodoActual.getHijo(i), recorrido);

        }
        recorridoPreOrden(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()), recorrido);
    }
    
    /*
        5. Para un Arbol Mvias implementar recorrido en In Orden
    */
    
    @Override
    public List<Key> recorridoEnInOrden() {
        List<Key> recorrido = new ArrayList<>();
        this.recorridoInOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoInOrden(NodoMVias<Key, Value> nodoActual, List<Key> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            recorridoInOrden(nodoActual.getHijo(i), recorrido);
            recorrido.add(nodoActual.getClave(i));

        }
        recorridoInOrden(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()), recorrido);

    }
    
    public int hojasAPartirDelNivel(int nivel) {
        return hojasAPartirDelNivel(this.raiz, nivel, 0);
    }

    private int hojasAPartirDelNivel(NodoMVias<Key, Value> nodoActual, int nivel, int nivelActual) {
        int nodosHoja=0;
        if(!NodoMVias.esNodoVacio(nodoActual)) {
            if(nivelActual>=nivel) {
                if(nodoActual.esHoja()) {
                    nodosHoja++;
                }
            }
            for(int i=0; i<this.orden; i++) {
                nodosHoja+=this.hojasAPartirDelNivel(nodoActual.getHijo(i), nivel, nivelActual+1);
            }
        }
        return nodosHoja;
    }
    
    
    @Override
    public String toString() {
        return generarCadenaDeArbol(this.raiz, "",  true, 0); //raiz, prefijo "", ponerCodo true
    }
    private String generarCadenaDeArbol(NodoMVias<Key, Value> nodoActual, String prefijo, boolean ponerCodo, int num) {
        StringBuilder cadena = new StringBuilder();
        cadena.append(prefijo);
        if (prefijo.length() == 0) {
            cadena.append("|__(R)");// + alt+192 +196 , + alt+195 +196
        } else {
            cadena.append(ponerCodo ? "|__(" : "|--(");
            cadena.append(num);
            cadena.append(")");
        }
        if (NodoMVias.esNodoVacio(nodoActual)) {
            cadena.append("-||\n");// ¦ alt+185
            return cadena.toString();
        }
        
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            cadena.append(nodoActual.getClave(i).toString());
            cadena.append(" ");
        }
        cadena.append("\n");
        
        for (int i = 0; i < this.orden - 1; i++) {
            NodoMVias<Key, Value> nodoHijo = nodoActual.getHijo(i);
            String prefijoAux = prefijo + (ponerCodo ? "   " : "|   ");// ¦ alt+179
            cadena.append(generarCadenaDeArbol(nodoHijo, prefijoAux, false, i+1));
        }
        
        NodoMVias<Key, Value> nodoHijo = nodoActual.getHijo(this.orden-1);
        String prefijoAux = prefijo + (ponerCodo ? "   " : "|   ");// ¦ alt+179
        cadena.append(generarCadenaDeArbol(nodoHijo, prefijoAux, true, this.orden));
        
        return cadena.toString();
    }
    
    /*
    15.
*/
    public boolean nodosCompletosEnElNivelN(int nivelN) {
        if ((nivelN < 0) || (nivelN > this.nivel())) {
            return false;
        }
        if (this.esArbolVacio()) {
            return false;
        }

        Queue<NodoMVias<Key, Value>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        this.irAlNivelN(colaDeNodos, nivelN);

        while (!colaDeNodos.isEmpty()) {
            NodoMVias<Key, Value> nodoActual = colaDeNodos.poll();
            if (nodoActual.cantidadDeClavesNoVacias() != this.orden - 1) {
                //if (nodoActual.cantidadDeHijosNoVacios() != this.orden) {
                    return false;
                //}
            }
        }

        return true;
    }
    
    @Override
    public int nivel() {
        return nivel(this.raiz);
    }
    protected int nivel(NodoMVias<Key, Value> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return -1;
        }
        int nivelMayor = -1;
        for (int i = 0; i < this.orden; i++) {
            int nivelActual = nivel(nodoActual.getHijo(i));
            if (nivelActual > nivelMayor) {
                nivelMayor = nivelActual;
            }
        }
        return nivelMayor + 1;
    }
    
    /*
    17.
    */
    public boolean esSimilar(ArbolMViasBusqueda<Key, Value> arbol2){
        return esSimilar1(raiz, arbol2.raiz);
    }
    public boolean esSimilar1(NodoMVias<Key, Value> arbol1,NodoMVias<Key, Value> arbol2){
        if (NodoMVias.esNodoVacio(arbol1) && NodoMVias.esNodoVacio(arbol2)) {
            return true;
        }
        
        if (NodoMVias.esNodoVacio(arbol1) || NodoMVias.esNodoVacio(arbol2)) {
            return false;
        }
        
        if (arbol1.esHoja() && arbol2.esHoja()) {
            if (arbol1.cantidadDeClavesNoVacias() == arbol2.cantidadDeClavesNoVacias()) {
                for (int i = 0; i < arbol1.cantidadDeClavesNoVacias(); i++) {
                    if (arbol1.getClave(i).compareTo(arbol2.getClave(i)) != 0) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        
        for (int i = 0; i < orden-1; i++) {
            if (arbol1.getClave(i).compareTo(arbol2.getClave(i)) != 0) {
                return false;
            }
        }
        
        if (arbol1.esHoja() || arbol2.esHoja()) {
            return false;
        }
        
        if (arbol1.cantidadDeHijosNoVacios()!= arbol2.cantidadDeHijosNoVacios()) {
            return false;
        }
       
        for (int i = 0; i < orden; i++) {
            if (esSimilar1(arbol1.getHijo(i), arbol2.getHijo(i)) == false) {
                return false;
            }
        }
        return true;
    }
}


