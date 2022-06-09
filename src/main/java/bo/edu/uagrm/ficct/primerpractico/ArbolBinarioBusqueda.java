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

public class ArbolBinarioBusqueda<Key extends Comparable<Key>, Value> implements IArbolBusqueda<Key, Value> {
    protected NodoBinario<Key,Value> raiz;
    
    @Override
    public void insertar(Key clave, Value valor) throws NullPointerException {
        if(clave == null) {
            throw new NullPointerException("Clave a insertar no puede ser nula");
        }
        
        if(valor == null) {
            throw new NullPointerException("Valor a insertar no puede ser nula");
        }
        
        if(this.esArbolVacio()) {
            this.raiz = new NodoBinario<>(clave, valor);
            return;
        }
        
        NodoBinario<Key, Value> nodoAnterior = NodoBinario.nodoVacio();
        NodoBinario<Key, Value> nodoActual = this.raiz;
        
        while(!NodoBinario.esNodoVacio(nodoActual)) {
            Key claveDeNodoActual = nodoActual.getClave();
            nodoAnterior = nodoActual;
            if(clave.compareTo(claveDeNodoActual) < 0 ) {
                //aqui la clave a insertar es menor que la clave del nodo actual
                nodoActual = nodoActual.getHijoIzquierdo();
            } else if (clave.compareTo(claveDeNodoActual) > 0) {
                // aqui la clave a insertar es mayor que la clave del nodo actual
                nodoActual = nodoActual.getHijoDerecho();
            } else {
                // claves iguales, solo debemos actualizar el valor
                nodoActual.setValor(valor);
                return;   
            }
        }
        // si llego aqui creamos un nuevo nodo
        NodoBinario<Key, Value> nuevoNodo = new NodoBinario<>(clave, valor);
        Key claveDelNodoAnterior = nodoAnterior.getClave();
        if (clave.compareTo(claveDelNodoAnterior) < 0) {
            nodoAnterior.setHijoIzquierdo(nuevoNodo);
        }else {
            nodoAnterior.setHijoDerecho(nuevoNodo);
        }
    }

    @Override
    public Value eliminar(Key claveAEliminar) throws NullPointerException, ExcepcionClaveNoExiste {
        if(claveAEliminar == null) {
            throw new NullPointerException("Clave a eliminar no puede ser nula");
        }
        
        Value valorAEliminar = this.buscar(claveAEliminar);
        if(valorAEliminar == null) {
            throw new ExcepcionClaveNoExiste();
        }
        
        this.raiz = eliminar(this.raiz, claveAEliminar);
        
        return valorAEliminar; 
    }
    
    private NodoBinario<Key, Value> eliminar(NodoBinario<Key, Value> nodoActual, Key claveAEliminar) {
        Key claveActual = nodoActual.getClave();
        if(claveAEliminar.compareTo(claveActual) < 0) {
            // quiere decir que la calveAEliminar es menor que la claveActual
            NodoBinario<Key, Value> supuestoNuevoHijoIzq = eliminar(nodoActual.getHijoIzquierdo(), claveAEliminar);
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzq);
            return nodoActual;
        }
        
        if(claveAEliminar.compareTo(claveActual) > 0) {
            // quiere decir que la calveAEliminar es mayor que la claveActual
            NodoBinario<Key, Value> supuestoNuevoHijoDer = eliminar(nodoActual.getHijoDerecho(), claveAEliminar);
            nodoActual.setHijoDerecho(supuestoNuevoHijoDer);
            return nodoActual;
        }
        
        // en este punto se sabe que la claveAEmilimar = claveActual
        if(nodoActual.esHoja()) {
            return NodoBinario.nodoVacio();
        }
        
        // caso 2 opcion a
        if(nodoActual.esHijoIzquieroVacio()&& !nodoActual.esHijoDerechoVacio()) {
            return nodoActual.getHijoDerecho();
        }
        
        // caso 2 opcion b
        if(!nodoActual.esHijoIzquieroVacio() && nodoActual.esHijoDerechoVacio()) {
            return nodoActual.getHijoIzquierdo();
        }
        
        //caso 3
        NodoBinario<Key, Value> nodoDelSucesor = this.obtenerNodoSucesor(nodoActual.getHijoDerecho());
        NodoBinario<Key, Value> supuestoNuevoHijoDerecho = this.eliminar(nodoActual.getHijoDerecho(), nodoDelSucesor.getClave());
        nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
        nodoActual.setClave(nodoDelSucesor.getClave());
        nodoActual.setValor(nodoDelSucesor.getValor());
      
        return nodoActual;
    }
    
    protected NodoBinario<Key, Value> obtenerNodoSucesor(NodoBinario<Key, Value> nodoActual) {
        NodoBinario<Key, Value> nodoAnterior = NodoBinario.nodoVacio();
        while (!NodoBinario.esNodoVacio(nodoActual)) {            
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        return nodoAnterior;
    }
    
    /*
     * TAREA
     * Implementar un metodo que retorne la cantidad de nodos que tienen hijoDerecho diferente de vacio, 
     * tanto interativamente como recursivamente
    */
    public int cantidadNodosConHijoDerechoDistintoVacio(){
        return cantidadNodosConHijoDerechoDistintoVacio(this.raiz);
    }
    
    private int cantidadNodosConHijoDerechoDistintoVacio(NodoBinario<Key, Value> nodoActual){
        if(NodoBinario.esNodoVacio(nodoActual))
            return 0;
        
        int canIz = cantidadNodosConHijoDerechoDistintoVacio(nodoActual.getHijoIzquierdo());
        int canDe = cantidadNodosConHijoDerechoDistintoVacio(nodoActual.getHijoDerecho());
        
        if(!nodoActual.esHijoDerechoVacio()){
            return canIz + canDe + 1;
        }   
        return canIz + canDe;
    }
    
    public int cantidadNodosConHijoDerechoDistintoVacioIter(){
        if(this.esArbolVacio())
            return 0;
        
        int contador = 0;
        Queue<NodoBinario<Key, Value>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            NodoBinario<Key, Value> nodoActual = colaDeNodos.poll();
            if (!nodoActual.esHijoIzquieroVacio()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }
            if(!nodoActual.esHijoDerechoVacio()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
                contador++;
            }
        }
        return contador;
    }
    
    /* TAREA 2
     * El mismo problema Tarea 1: indicando a  contar apartir de un nivel n
    */
    
    public int cantidadNodosConHijoDerechoDistintoVacioNivel( int nivel ){
        if(nivel > this.altura() - 1)
            return 0;
        
        return cantidadNodosConHijoDerechoDistintoVacioNivel(this.raiz, nivel);
    }
    
    public int cantidadNodosConHijoDerechoDistintoVacioNivel( NodoBinario<Key, Value> nodoActual, int nivel ){
        if(NodoBinario.esNodoVacio(nodoActual))
            return 0;
        
        if(nivel == 0){
            int canIz = cantidadNodosConHijoDerechoDistintoVacioNivel(nodoActual.getHijoIzquierdo(), 0);
            int canDe = cantidadNodosConHijoDerechoDistintoVacioNivel(nodoActual.getHijoDerecho(), 0);
        
            if(!nodoActual.esHijoDerechoVacio()){
                return canIz + canDe + 1;
            }
            
            return canIz + canDe;
        }
        
        int canIz = cantidadNodosConHijoDerechoDistintoVacioNivel(nodoActual.getHijoIzquierdo(), nivel - 1);
        int canDe = cantidadNodosConHijoDerechoDistintoVacioNivel(nodoActual.getHijoDerecho(), nivel - 1);
        
        return canIz + canDe;  
    }
    
    public int cantidadNodosConHijoDerechoDistintoVacioNivelIter( int nivel ){
        return 0;
    }

    @Override
    public Value buscar(Key claveABuscar) throws NullPointerException {
        if(claveABuscar == null) {
            throw new NullPointerException("Clave a buscar no puede ser nula");
        }
        NodoBinario<Key, Value> nodoActual = this.raiz;
        
        while(!NodoBinario.esNodoVacio(nodoActual)) {
            Key claveDeNodoActual = nodoActual.getClave();
            if(claveABuscar.compareTo(claveDeNodoActual) < 0 ) { //aqui la clave a insertar es menor que la clave del nodo actual
                nodoActual = nodoActual.getHijoIzquierdo();
            } else if (claveABuscar.compareTo(claveDeNodoActual) > 0) { // aqui la clave a insertar es mayor que la clave del nodo actual
                nodoActual = nodoActual.getHijoDerecho();
            } else {
                return nodoActual.getValor();
            }
        }
        return null;
    }

    @Override
    public boolean contiene(Key claveABuscar) throws NullPointerException {
        return this.buscar(claveABuscar) != null;
    }

    @Override
    public int size() {
        int cantidad = 0;
        Queue<NodoBinario<Key, Value>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            NodoBinario<Key, Value> nodoActual = colaDeNodos.poll();
            cantidad++;
            if (!nodoActual.esHijoIzquieroVacio()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }
            
            if(!nodoActual.esHijoDerechoVacio()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }
        }
        return cantidad;
    }
    
    public int sizeRec() {
        return sizeRec(this.raiz);
    }
    
    private int sizeRec(NodoBinario<Key, Value> nodoActual) {
        if(NodoBinario.esNodoVacio(nodoActual)) { // caso base para simular n == 0;
            return 0;
        }
        
        int sizePorIzq = sizeRec(nodoActual.getHijoIzquierdo());
        int sizePorDer = sizeRec(nodoActual.getHijoDerecho());
        
        return sizePorIzq + sizePorDer + 1;
    }
    
    @Override
    public int altura() {
        return altura(this.raiz);
    }
    
    protected int altura(NodoBinario<Key, Value> nodoActual) {
        if(NodoBinario.esNodoVacio(nodoActual)) { // caso base para simular n == 0;
            return 0;
        }
        
        int alturaPorIzq = altura(nodoActual.getHijoIzquierdo());
        int alturaPorDer = altura(nodoActual.getHijoDerecho());
        
        if(alturaPorIzq > alturaPorDer) {
            return alturaPorIzq + 1;
        }
        
        return alturaPorDer + 1;
    }
    
    public int alturaIt() {
        int alturaTotal = 0;
        
        if (this.esArbolVacio()) {
            return alturaTotal;
        }
        Queue<NodoBinario<Key, Value>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            // .poll retorna y elimina
            int cantidadNodosNivel = colaDeNodos.size();
            //int posicion = 0;
            //while(posicion < cantidadNodosNivel) {
            while(cantidadNodosNivel > 0) {
                NodoBinario<Key, Value> nodoActual = colaDeNodos.poll();
                if (!nodoActual.esHijoIzquieroVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
            
                if(!nodoActual.esHijoDerechoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                cantidadNodosNivel--;
            }
            alturaTotal++;
        }
        return alturaTotal;
    }

    @Override
    public void vaciarArbol() {
        this.raiz = NodoBinario.nodoVacio();
    }
    
    @Override
    public boolean esArbolVacio() {
        return NodoBinario.esNodoVacio(this.raiz);
    }

    @Override
    public List<Key> recorridoPorNiveles() {
        List<Key> recorrido = new ArrayList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        
        Queue<NodoBinario<Key, Value>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            // .poll retorna y elimina
            NodoBinario<Key, Value> nodoActual = colaDeNodos.poll();
            recorrido.add(nodoActual.getClave());
            if (!nodoActual.esHijoIzquieroVacio()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }
            
            if(!nodoActual.esHijoDerechoVacio()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }
        }
        return recorrido;
    }

    // Hacer
    public List<Key> recorridoPorNivelesRec(){
        List<Key> recorrido = new ArrayList<>();
        return recorrido;
    }
    
    @Override
    public List<Key> recorridoEnPreOrden() {
        List<Key> recorrido = new ArrayList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        
        Stack<NodoBinario<Key, Value>> colaDeNodos = new Stack<>();
        colaDeNodos.push(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            // .pop retorna y elimina
            NodoBinario<Key, Value> nodoActual = colaDeNodos.pop();
            recorrido.add(nodoActual.getClave());
            
            if(!nodoActual.esHijoDerechoVacio()) {
                colaDeNodos.push(nodoActual.getHijoDerecho());
            }
                        
            if (!nodoActual.esHijoIzquieroVacio()) {
                colaDeNodos.push(nodoActual.getHijoIzquierdo());
            }
        }
        return recorrido;
    }
    
    public List<Key> recorridoEnPreOrdenRec(){
        List<Key> recorrido = new ArrayList<>();
        return recorrido;
    }

    @Override
    public List<Key> recorridoEnInOrden() {
        List<Key> recorrido = new ArrayList<>();
        recorridoEnInOrden(this.raiz, recorrido);
        return recorrido;
    }
    
    private void recorridoEnInOrden(NodoBinario<Key, Value> nodoActual, List<Key> recorrido) {
        if(NodoBinario.esNodoVacio(nodoActual)) { // caso base para simular n == 0;
            return;
        }
        recorridoEnInOrden(nodoActual.getHijoIzquierdo(), recorrido);
        recorrido.add(nodoActual.getClave());
        recorridoEnInOrden(nodoActual.getHijoDerecho(), recorrido);
    }
    
    public List<Key> recorridoEnInOrdenIter() {
        List<Key> recorrido = new ArrayList<>();
        return recorrido;
    }
    
    @Override
    public List<Key> recorridoEnPostOrden() {
        List<Key> recorrido = new ArrayList<>();
        recorridoEnPostOrden(this.raiz, recorrido);
        return recorrido;
    }
    
    private void recorridoEnPostOrden(NodoBinario<Key, Value> nodoActual, List<Key> recorrido) {
        if(NodoBinario.esNodoVacio(nodoActual)) { // caso base para simular n == 0;
            return;
        }
        recorridoEnPostOrden(nodoActual.getHijoIzquierdo(), recorrido);
        recorridoEnPostOrden(nodoActual.getHijoDerecho(), recorrido);
        recorrido.add(nodoActual.getClave());
    }
    
    /*
        2. Para un arbol binario de busqueda implementar el recorrido en port orden iterativo
    */
    public List<Key> recorridoEnPostOrdenIter() {
        List<Key> recorrido = new ArrayList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }

        Stack<NodoBinario<Key, Value>> pilaDeNodos = new Stack<>();
        NodoBinario<Key, Value> nodoActual = this.raiz;
        
        insertarEnPilaParaPostOrden(nodoActual, pilaDeNodos);
        
        while (!pilaDeNodos.isEmpty() ){
            nodoActual = pilaDeNodos.pop();
            recorrido.add(nodoActual.getClave());
            
            if (!pilaDeNodos.isEmpty()) {
                NodoBinario<Key, Value> nodoDelTope = pilaDeNodos.peek();
                if (!nodoDelTope.esHijoDerechoVacio()&& nodoDelTope.getHijoDerecho() != nodoActual) {
                    insertarEnPilaParaPostOrden(nodoDelTope.getHijoDerecho(), pilaDeNodos);
                }
            }
        }
        return recorrido;
    }
       
    private void insertarEnPilaParaPostOrden(NodoBinario<Key, Value> nodoActual, 
        Stack<NodoBinario<Key, Value>> pilaDeNodo){
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            pilaDeNodo.push(nodoActual);
            if (!nodoActual.esHijoIzquieroVacio()) {
                nodoActual = nodoActual.getHijoIzquierdo();
            }else{
                nodoActual = nodoActual.getHijoDerecho();
            }
        }
    }
    
    protected NodoBinario<Key, Value> buscarSucesor(NodoBinario<Key, Value> nodoActual) {
        NodoBinario<Key, Value> nodoAnterior = NodoBinario.nodoVacio();
        while (!NodoBinario.esNodoVacio(nodoActual)) {            
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        return nodoAnterior;
    }
    
    public int cantidadHojasDelArbol() {
        if (this.esArbolVacio()) {
            return 0;
        }
        
        int cantidadHojas = 0;
        Queue<NodoBinario<Key, Value>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        
        while (!colaDeNodos.isEmpty()) {            
            NodoBinario<Key, Value> nodoActual = colaDeNodos.poll();
            
            if (!nodoActual.esHijoIzquieroVacio()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            } else if (nodoActual.esHijoDerechoVacio()) {
                cantidadHojas++;
            }
            if (!nodoActual.esHijoDerechoVacio()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }
        }        
        return cantidadHojas;
    }
    
    @Override
    public int nivel() { 
        if(this.esArbolVacio()) {
            return -1;
        }
        
        int nivelDelArbol = -1;
        Queue<NodoBinario<Key, Value>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        
        while (!colaDeNodos.isEmpty()) {            
        int nroDeNodosDelNivel = colaDeNodos.size();
        int posicion = 0;
            while (posicion < nroDeNodosDelNivel) {                
                NodoBinario<Key, Value> nodoActual = colaDeNodos.poll();
                if (!nodoActual.esHijoIzquieroVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esHijoDerechoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                posicion++;
            }
            nivelDelArbol++;
        }
        return nivelDelArbol;
    }
    
    private NodoBinario<Key, Value> reconstruirConPreOrden(List<Key> clavesInOrden, List<Value> valoresInOrden, List<Key> clavesNoInOrden, List<Value> valoresNoInOrden) {
        if (clavesInOrden.isEmpty()) {
            return NodoBinario.nodoVacio();
        }

        Key clave = clavesNoInOrden.get(0);
        Value valor = valoresNoInOrden.get(0);
        NodoBinario<Key, Value> nodoRaiz = new NodoBinario<>(clave, valor);
        
        //posicion de la raiz en la lista inOrden
        int posicionRaiz = this.buscarPosicionRaiz(clavesInOrden, clave);

        //Hijo Izquierdo
        List<Key> subListaIzqClavesInOrden = clavesInOrden.subList(0, posicionRaiz);
        List<Value> subListaIzqValoresInOrden = valoresInOrden.subList(0, posicionRaiz);
        List<Key> subListaIzqClavesNoInOrden = clavesNoInOrden.subList(1, posicionRaiz + 1);
        List<Value> subListaIzqValoresNoInOrden = valoresNoInOrden.subList(1, posicionRaiz + 1);
        NodoBinario<Key, Value> hijoIzquierdo = this.reconstruirConPreOrden(subListaIzqClavesInOrden, subListaIzqValoresInOrden, subListaIzqClavesNoInOrden, subListaIzqValoresNoInOrden);

        //Hijo Derecho
        List<Key> subListaDerClavesInOrden = clavesInOrden.subList(posicionRaiz + 1, clavesInOrden.size());
        List<Value> subListaDerValoresInOrden = valoresInOrden.subList(posicionRaiz + 1, valoresInOrden.size());
        List<Key> subListaDerClavesNoInOrden = clavesNoInOrden.subList(posicionRaiz + 1, clavesNoInOrden.size());
        List<Value> subListaDerValoresNoInOrden = valoresNoInOrden.subList(posicionRaiz + 1, valoresNoInOrden.size());
        NodoBinario<Key, Value> hijoDerecho = this.reconstruirConPreOrden(subListaDerClavesInOrden, subListaDerValoresInOrden, subListaDerClavesNoInOrden, subListaDerValoresNoInOrden);

        nodoRaiz.setHijoIzquierdo(hijoIzquierdo);
        nodoRaiz.setHijoDerecho(hijoDerecho);
        return nodoRaiz;
    }
    
    private NodoBinario<Key, Value> reconstruirConPostOrden(List<Key> clavesInOrden, List<Value> valoresInOrden, List<Key> clavesNoInOrden, List<Value> valoresNoInOrden) {
        if (clavesInOrden.isEmpty()){
            return NodoBinario.nodoVacio();
        }

        Key clave = clavesNoInOrden.get(clavesNoInOrden.size() - 1);
        Value valor = valoresNoInOrden.get(valoresNoInOrden.size() - 1);
        NodoBinario<Key, Value> nodoRaiz = new NodoBinario<>(clave, valor);
        
        //posicion de la raiz en la lista inOrden
        int posicionRaiz = this.buscarPosicionRaiz(clavesInOrden, clave);

        //Hijo Izquierdo
        List<Key> subListaIzqClavesInOrden = clavesInOrden.subList(0, posicionRaiz);
        List<Value> subListaIzqValoresInOrden = valoresInOrden.subList(0, posicionRaiz);
        List<Key> subListaIzqClavesNoInOrden = clavesNoInOrden.subList(0, posicionRaiz);
        List<Value> subListaIzqValoresNoInOrden = valoresNoInOrden.subList(0, posicionRaiz);
        NodoBinario<Key, Value> hijoIzquierdo = this.reconstruirConPostOrden(subListaIzqClavesInOrden, subListaIzqValoresInOrden, subListaIzqClavesNoInOrden, subListaIzqValoresNoInOrden);

        //Hijo Derecho
        List<Key> subListaDerClavesInOrden = clavesInOrden.subList(posicionRaiz + 1, clavesInOrden.size());
        List<Value> subListaDerValoresInOrden = valoresInOrden.subList(posicionRaiz + 1, valoresInOrden.size());
        List<Key> subListaDerClavesNoInOrden = clavesNoInOrden.subList(posicionRaiz, clavesNoInOrden.size() - 1);
        List<Value> subListaDerValoresNoInOrden = valoresNoInOrden.subList(posicionRaiz, valoresNoInOrden.size() - 1);
        NodoBinario<Key, Value> hijoDerecho = this.reconstruirConPostOrden(subListaDerClavesInOrden, subListaDerValoresInOrden, subListaDerClavesNoInOrden, subListaDerValoresNoInOrden);

        nodoRaiz.setHijoIzquierdo(hijoIzquierdo);
        nodoRaiz.setHijoDerecho(hijoDerecho);
        return nodoRaiz;
    }
    
    private int buscarPosicionRaiz(List<Key> clavesInOrden, Key claveABuscar){
        for(int i = 0; i < clavesInOrden.size(); i++){
            if(claveABuscar.compareTo(clavesInOrden.get(i)) == 0){
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public String toString() {
        return generarCadenaDeArbol(raiz, "",  true); //raiz, prefijo "", ponerCodo true
    }
    
    private String generarCadenaDeArbol(NodoBinario<Key, Value> nodoActual, 
        String prefijo, boolean ponerCodo) {
        StringBuilder cadena = new StringBuilder();
        cadena.append(prefijo);
        if (prefijo.length() == 0) {
            cadena.append(ponerCodo ? "|__(R)" : "|--(R)");
            // + alt+192 +196 , + alt+195 +196
        } else {
            cadena.append(ponerCodo ? "|__(D)" : "|--(I)" );
            // + alt+192 +196 , + alt+195 +196
        }
        if (NodoBinario.esNodoVacio(nodoActual)) {
            cadena.append("-||\n");// ¦ alt+185
            return cadena.toString();
        }
        
        cadena.append(nodoActual.getClave().toString());
        cadena.append("\n");

        NodoBinario<Key, Value> nodoIzq = nodoActual.getHijoIzquierdo();
        String prefijoAux = prefijo + (ponerCodo ? "   " : "|   ");// ¦ alt+179
        cadena.append(generarCadenaDeArbol(nodoIzq, prefijoAux, false));
        
        NodoBinario<Key, Value> nodoDer = nodoActual.getHijoDerecho();
        cadena.append(generarCadenaDeArbol(nodoDer, prefijoAux, true));
        
        return cadena.toString();
    }
    
    /*
        12. 
    */
    public int cantidadNodoUnSoloHijo() {
        if (this.esArbolVacio()) {
            return 0;
        }
        return cantidadNodoUnSoloHijoR(this.raiz);
    }
    
    private int cantidadNodoUnSoloHijoR(NodoBinario<Key, Value> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        if (nodoActual.esHoja()) {
            return 0;
        }
        
        int cantPorIzq = cantidadNodoUnSoloHijoR(nodoActual.getHijoIzquierdo());
        int cantPorDer = cantidadNodoUnSoloHijoR(nodoActual.getHijoDerecho());
        if ((!nodoActual.esHijoIzquieroVacio() && nodoActual.esHijoDerechoVacio()) || (nodoActual.esHijoIzquieroVacio() && !nodoActual.esHijoDerechoVacio())) {
            return cantPorIzq + cantPorDer + 1;
        }
        return cantPorIzq + cantPorDer;
    }
    
    /*
        13.
    */
    
    public int numeroHijosVacioLogicaInOrden() {
        if (this.esArbolVacio()) {
            return 0;
        }
        
        int cant = 0;
        Stack<NodoBinario<Key, Value>> pilaDeNodos = new Stack<>();
        NodoBinario<Key, Value> nodoActual = this.raiz;
        this.insertarEnPilaParaInOrden(nodoActual, pilaDeNodos);
        
        while (!pilaDeNodos.isEmpty()) {            
            nodoActual = pilaDeNodos.pop();    
            if (nodoActual.esHijoIzquieroVacio()) {
                cant++;
            }
            if (!nodoActual.esHijoDerechoVacio()) {
                this.insertarEnPilaParaInOrden(nodoActual.getHijoDerecho(), pilaDeNodos);
            } else {
                cant++;
            }
        }
        
        return cant;
    }
    
    /*
        14.
    */
    public Key predecesorInOrden() {
        return predecesorInOrden(this.raiz.getHijoDerecho().getHijoDerecho());
    }
    
    private Key predecesorInOrden(NodoBinario<Key, Value> nodoActual) {
        Key predecesor = (Key)NodoBinario.nodoVacio();
        if(NodoBinario.esNodoVacio(nodoActual)) {
            return predecesor;
        }

        Key claveActual = nodoActual.getClave();
        NodoBinario<Key, Value> nodoPredecesor = this.raiz;
        Stack<NodoBinario<Key, Value>> pilaDeNodos = new Stack<>();

        if (claveActual.compareTo(nodoPredecesor.getClave()) > 0) {
            this.insertarEnPilaParaInOrden(nodoPredecesor.getHijoDerecho(), pilaDeNodos);
        } else if (claveActual.compareTo(nodoPredecesor.getClave()) < 0) {
            this.insertarEnPilaParaInOrden(nodoPredecesor.getHijoIzquierdo(), pilaDeNodos);
        } else {
            return predecesor;
        }

        NodoBinario<Key, Value> nodoAux;
        while (!pilaDeNodos.isEmpty()) {
            nodoAux = nodoPredecesor;
            nodoPredecesor = pilaDeNodos.pop();

            if (nodoPredecesor.getClave().compareTo(claveActual) == 0) {
                //si pilla la claveActual y la pila esta vacia quiere decir que su clave predecesora es la clave del nodoAux
                if (pilaDeNodos.isEmpty()) {
                    predecesor = nodoAux.getClave();
                } else {//si la pila no esta vacia su clave predecera sera la siguiente en la Pila
                    nodoPredecesor = pilaDeNodos.pop();
                    predecesor = nodoPredecesor.getClave();
                    return predecesor;
                }
            }
            if (!nodoPredecesor.esHijoDerechoVacio()) {
                this.insertarEnPilaParaInOrden(nodoPredecesor.getHijoDerecho(), pilaDeNodos);
            }
        }
        return predecesor;
    }
    
    private void insertarEnPilaParaInOrden(NodoBinario<Key, Value> nodoActual, Stack<NodoBinario<Key, Value>> pilaDeNodos) {
        while (!NodoBinario.esNodoVacio(nodoActual)) {           
            pilaDeNodos.push(nodoActual);
            nodoActual = nodoActual.getHijoIzquierdo();
        }  
    }
    
    /*
        18. Implemente un metodo que riciba un Arbol de parametro y retorne verdadero si ambos son similares
    */
    public boolean esSimilar(ArbolBinarioBusqueda<Key, Value> arbol2) {
        if (this.esArbolVacio() && arbol2.esArbolVacio()) {
            return true;
        }
        if ((!this.esArbolVacio() && arbol2.esArbolVacio())
            || (this.esArbolVacio() && !arbol2.esArbolVacio())) {
            return false;
        }

        Queue<NodoBinario<Key, Value>> colaDeNodos1 = new LinkedList<>();
        Queue<NodoBinario<Key, Value>> colaDeNodos2 = new LinkedList<>();
        colaDeNodos1.offer(this.raiz);
        colaDeNodos2.offer(arbol2.raiz);

        while (!colaDeNodos1.isEmpty() && !colaDeNodos2.isEmpty()) {
            NodoBinario<Key, Value> nodoActual1 = colaDeNodos1.poll();
            NodoBinario<Key, Value> nodoActual2 = colaDeNodos2.poll();

            if (!nodoActual1.esHijoIzquieroVacio()&& !nodoActual2.esHijoIzquieroVacio()) {
                colaDeNodos1.offer(nodoActual1.getHijoIzquierdo());
                colaDeNodos2.offer(nodoActual2.getHijoIzquierdo());
            } else {
                if ((!nodoActual1.esHijoIzquieroVacio() && nodoActual2.esHijoIzquieroVacio()) || (nodoActual1.esHijoIzquieroVacio() && !nodoActual2.esHijoIzquieroVacio())) {
                    return false;
                }
            }

            if (!nodoActual1.esHijoDerechoVacio()&& !nodoActual2.esHijoDerechoVacio()) {
                colaDeNodos1.offer(nodoActual1.getHijoDerecho());
                colaDeNodos2.offer(nodoActual2.getHijoDerecho());
            } else {
                if ((!nodoActual1.esHijoDerechoVacio() && nodoActual2.esHijoDerechoVacio()) || (nodoActual1.esHijoDerechoVacio() && !nodoActual2.esHijoDerechoVacio())) {
                    return false;
                }
            }
        }
        return true;
    }
}
