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

public class NodoBinario<Key, Value> {
    private Key clave;
    private Value valor;
    NodoBinario<Key, Value> hijoIzquierdo;
    NodoBinario<Key, Value> hijoDerecho;
    
    public NodoBinario(){ }
    
    public NodoBinario(Key clave, Value valor){
        this.clave = clave;
        this.valor = valor;
    }

    public Key getClave(){
        return clave;
    }
    
    public void setClave(Key clave){
        this.clave = clave;
    }

    public Value getValor(){
        return valor;
    }
    
    public void setValor(Value valor){
        this.valor = valor;
    }

    public NodoBinario<Key, Value> getHijoIzquierdo(){
        return hijoIzquierdo;
    }
    
    public void setHijoIzquierdo(NodoBinario<Key, Value> hijoIzquierdo){
        this.hijoIzquierdo = hijoIzquierdo;
    }

    public NodoBinario<Key, Value> getHijoDerecho(){
        return hijoDerecho;
    }
    
    public void setHijoDerecho(NodoBinario<Key, Value> hijoDerecho) {
        this.hijoDerecho = hijoDerecho;
    }
    
    public static NodoBinario nodoVacio(){
        return null;
    }

    public static boolean esNodoVacio(NodoBinario unNodo){
        return unNodo == nodoVacio();
    }

    public boolean esHijoIzquieroVacio() {
        return NodoBinario.esNodoVacio(this.getHijoIzquierdo());
    }
    
    public boolean esHijoDerechoVacio() {
        return NodoBinario.esNodoVacio(this.getHijoDerecho());
    }
    
    public boolean esHoja() {
        return this.esHijoDerechoVacio() && this.esHijoIzquieroVacio();
    }

    public int cantidadHijos(){
        if(NodoBinario.esNodoVacio(this)){
            return 0;
        }
        int cant = 0;
        if(!this.esHijoIzquieroVacio()){
            cant++;
        }
        if(!this.esHijoDerechoVacio()){
            cant++;
        }
        return cant;
    }  
}