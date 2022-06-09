/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.uagrm.ficct.primerpractico;

/**
 *
 * @author HP
 * @param <Key>
 * @param <Value>
 */
public class AVL <Key extends Comparable<Key>, Value> extends ArbolBinarioBusqueda<Key, Value> {
    private static final byte TOPE_DIFERENCIA = 1;
    
    /*
        6. Para un Arbol AVL implementar el metodo insertar.
    */
    @Override
    public void insertar (Key claveAInsertar, Value valorAInsertar) throws NullPointerException {
        if(valorAInsertar == null) {
            throw new NullPointerException ("No se permite insertar claves nulas en el árbol");
        }
        this.raiz = this.insertar (this.raiz, claveAInsertar, valorAInsertar);
    }
    
    private NodoBinario<Key, Value> insertar (NodoBinario<Key, Value> nodoActual, Key claveAInsertar, Value valorAInsertar){
        if(NodoBinario.esNodoVacio(nodoActual)) {
            NodoBinario<Key, Value> nuevoNodo = new NodoBinario<>(claveAInsertar, valorAInsertar);
            return nuevoNodo;
        }
        
        Key claveActual = nodoActual.getClave();
        if (claveAInsertar.compareTo(claveActual) < 0) {
            NodoBinario<Key, Value> nuevoSupuestoHijo = insertar(nodoActual.getHijoIzquierdo(), claveAInsertar, valorAInsertar);
            nodoActual.setHijoIzquierdo(nuevoSupuestoHijo);
            return balancear(nodoActual);
        }
        
        if (claveAInsertar.compareTo(claveActual) > 0) {
            NodoBinario<Key, Value> nuevoSupuestoHijo = insertar(nodoActual.getHijoDerecho(), claveAInsertar, valorAInsertar);
            nodoActual.setHijoDerecho(nuevoSupuestoHijo);
            return balancear(nodoActual);
        }
        
        nodoActual.setValor(valorAInsertar);
        return nodoActual;
    }
    /*
        7. Para el Arbol AVL impelementar el metodo eliminar.
    */
    @Override
     public Value eliminar(Key claveAEliminar) throws NullPointerException, ExcepcionClaveNoExiste {
        Value valorAEliminar = this.buscar(claveAEliminar);
        if (valorAEliminar == null) {
            throw new ExcepcionClaveNoExiste();
        }
        
        this.raiz = eliminar(this.raiz, claveAEliminar);
        return valorAEliminar;
    }

    private NodoBinario<Key, Value> eliminar(NodoBinario<Key, Value> nodoActual, Key claveAEliminar) {
        Key claveActual = nodoActual.getClave();
        
        if (claveAEliminar.compareTo(claveActual) < 0) {
            NodoBinario<Key, Value> supuestoNuevoHijoIzq = eliminar(nodoActual.getHijoIzquierdo(), claveAEliminar);
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzq);
            return balancear(nodoActual);
        }
        if (claveAEliminar.compareTo(claveActual) > 0) {
            NodoBinario<Key, Value> supuestoNuevoHijoDer = eliminar(nodoActual.getHijoDerecho(), claveAEliminar);
            nodoActual.setHijoDerecho(supuestoNuevoHijoDer);
            return balancear(nodoActual);
        }
        
        //Caso 1
        if (nodoActual.esHoja()) {
            return NodoBinario.nodoVacio();
        }
        
        //Caso 2 a
        if (!nodoActual.esHijoIzquieroVacio()&& nodoActual.esHijoDerechoVacio()) {
            return balancear(nodoActual.getHijoIzquierdo());
        }
        
        //Caso 2 b
        if (nodoActual.esHijoIzquieroVacio() && !nodoActual.esHijoDerechoVacio()) {
            return balancear(nodoActual.getHijoDerecho());
        }
        
        //Caso 3
        NodoBinario<Key, Value> nodoDelSucesor = buscarSucesor(nodoActual.getHijoDerecho());
        NodoBinario<Key, Value> supuestoNuevoHijo = eliminar(nodoActual.getHijoDerecho(), nodoDelSucesor.getClave());
        
        nodoActual.setHijoDerecho(supuestoNuevoHijo);
        nodoActual.setClave(nodoDelSucesor.getClave());
        nodoActual.setValor(nodoDelSucesor.getValor());
        return balancear(nodoActual);
    }
    
    private NodoBinario<Key, Value> balancear(NodoBinario<Key, Value> nodoActual) {
        int alturaPorIzquierda = altura(nodoActual.getHijoIzquierdo());
        int alturaPorDerecha = altura(nodoActual.getHijoDerecho());
        
        int diferenciaDeAltura = alturaPorIzquierda - alturaPorDerecha;
        if (diferenciaDeAltura > TOPE_DIFERENCIA) { //D_P = 1 , la altura de izquierda es mayor a la derecha
            //rotacion a derecha
            NodoBinario<Key, Value> hijoIzquierdoDelActual = nodoActual.getHijoIzquierdo();
            alturaPorIzquierda = altura(hijoIzquierdoDelActual.getHijoIzquierdo());
            alturaPorDerecha = altura(hijoIzquierdoDelActual.getHijoDerecho());
            if (alturaPorDerecha > alturaPorIzquierda) {
                return rotacionDobleADerecha(nodoActual);
            }
            return rotacionSimpleADerecha(nodoActual);
            
        } else if (diferenciaDeAltura < -TOPE_DIFERENCIA) { //D_P = -1, la altura derecha es mayor a la izquierd
            //rotancion a izquierda
            NodoBinario<Key, Value> hijoDerechoDelActual = nodoActual.getHijoDerecho();
            alturaPorIzquierda = altura(hijoDerechoDelActual.getHijoIzquierdo());
            alturaPorDerecha = altura(hijoDerechoDelActual.getHijoDerecho());
            if (alturaPorIzquierda > alturaPorDerecha) {// era esto el problema xd 
                return rotacionDobleAIzquierda(nodoActual);
            }
            return rotacionSimpleAIzquierda(nodoActual);
        }
        return nodoActual;
    }
    
    private NodoBinario<Key, Value> rotacionSimpleADerecha(NodoBinario<Key, Value> nodoActual) {
        NodoBinario<Key, Value> nodoQueRota = nodoActual.getHijoIzquierdo();//AQUI EL ERROR?
        nodoActual.setHijoIzquierdo(nodoQueRota.getHijoDerecho());
        nodoQueRota.setHijoDerecho(nodoActual);
        return nodoQueRota;
    }
    
    private NodoBinario<Key, Value> rotacionDobleADerecha(NodoBinario<Key, Value> nodoActual) {
        NodoBinario<Key, Value> nodoDePrimeraRotacion = rotacionSimpleAIzquierda(nodoActual.getHijoIzquierdo());
        nodoActual.setHijoIzquierdo(nodoDePrimeraRotacion);
        return rotacionSimpleADerecha(nodoActual);
    }
    
    private NodoBinario<Key, Value> rotacionSimpleAIzquierda(NodoBinario<Key, Value> nodoActual) {
        NodoBinario<Key, Value> nodoQueRota = nodoActual.getHijoDerecho();
        nodoActual.setHijoDerecho(nodoQueRota.getHijoIzquierdo());
        nodoQueRota.setHijoIzquierdo(nodoActual);
        return nodoQueRota;
    }
    
    private NodoBinario<Key, Value> rotacionDobleAIzquierda(NodoBinario<Key, Value> nodoActual) {
        NodoBinario<Key, Value> nodoDePrimeraRotacion = rotacionSimpleADerecha(nodoActual.getHijoDerecho());
        nodoActual.setHijoDerecho(nodoDePrimeraRotacion);
        return rotacionSimpleAIzquierda(nodoActual);  
    }
}
