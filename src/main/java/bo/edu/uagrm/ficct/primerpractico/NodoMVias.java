/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.uagrm.ficct.primerpractico;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author HP
 * @param <Key>
 * @param <Value>
 */

public class NodoMVias<Key, Value>{
    private List<Key> listaDeClaves;
    private List<Value> listaDeValores;
    private List<NodoMVias<Key, Value>> listaDeHijos;
    
    public NodoMVias (int orden){
        listaDeClaves = new LinkedList<> ();
        listaDeValores = new LinkedList<> ();
        listaDeHijos = new LinkedList<> ();
        
    for(int i = 0; i < orden - 1; i++){
        listaDeClaves.add((Key)NodoMVias.nodoVacio ());
        listaDeValores.add((Value)NodoMVias.datoVacio());
        listaDeHijos.add(NodoMVias.nodoVacio());
    }
        listaDeHijos.add(NodoMVias.nodoVacio());
    }
    
    public NodoMVias(int orden, Key primerClave, Value primerValor){
        this(orden);
        this.listaDeClaves.set(0, primerClave);
        this.listaDeValores.set(0, primerValor);
    }
    
    public static NodoMVias nodoVacio(){
        return null;
    }
    
    public static boolean esNodoVacio(NodoMVias elNodo){
        return elNodo == NodoMVias.nodoVacio();
    }
    
    public static Object datoVacio(){
        return null;
    }
    
    public Key getClave(int posicion){
        return this.listaDeClaves.get(posicion);
    }
    
    public void setClave(int posicion, Key clave){
        this.listaDeClaves.set(posicion, clave);
    }
    
    public Value getValor(int posicion) {
        return this.listaDeValores.get(posicion) ;
    }
    
    public void setValor(int posicion, Value valor){
        this.listaDeValores.set(posicion, valor);
    }
    
    public NodoMVias<Key, Value> getHijo(int posicion){
        return this.listaDeHijos.get(posicion);
    }
    
    public void setHijo(int posicion, NodoMVias<Key, Value> nodoHijo){
        this.listaDeHijos.set(posicion, nodoHijo);
    }
    
    public boolean esClaveVacia(int posicion){
        return this.listaDeClaves.get(posicion) == NodoMVias.datoVacio();
    }
    
    public boolean esHijoVacio(int posicion){
        return NodoMVias.esNodoVacio(this.listaDeHijos.get(posicion));
    }
    
    public boolean esHoja(){
        for(int i = 0; i < this.listaDeHijos.size(); i++){
            if(!this.esHijoVacio(i)){
                return false;
            }
        }
        return true;
    }
    
    public boolean estanclavesLlenas(){
        for(int i = 0; i < this.listaDeClaves.size(); i++){
            if(this.esClaveVacia(i)){
                return false;
            }
        }
        return true;
    }
    
    public int cantidadDeHijosNoVacios(){
        int cantidad = 0;
        for(int i = 0; i < this.listaDeHijos.size (); i++){
            if(!this.esHijoVacio (i)){
                cantidad++;
            }
        }
        return cantidad;
    }
    
    public int cantidadDeClavesNoVacias(){
        int cantidad = 0;
        for(int i = 0; i < this.listaDeClaves.size (); i++){
            if(!this.esClaveVacia(i)){
                cantidad++;
            }
        }
        return cantidad;
    }
    
    public int cantidadDeHijosVacios(){
        return this.listaDeHijos.size() - this.cantidadDeHijosNoVacios();
    }
    
    public boolean estanClavesLlenas(){
        for(int i = 0; i < this.listaDeClaves.size(); i++){
            if(this.esClaveVacia(i)){
                return false;
            }
        }
        return true;
    }
}
    