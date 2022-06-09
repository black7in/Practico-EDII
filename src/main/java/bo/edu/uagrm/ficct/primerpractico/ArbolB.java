/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.uagrm.ficct.primerpractico;

import java.util.Stack;

/**
 *
 * @author HP
 * @param <Key>
 * @param <Value>
 */
public class ArbolB <Key extends Comparable<Key>, Value> extends ArbolMViasBusqueda<Key, Value>{
    private final int nroMaximoDeDatos;
    private final int nroMinimoDeDatos;
    private final int nroMinimoDeHijos;
    
    public ArbolB() {
        super();
        this.nroMaximoDeDatos = 2;
        this.nroMinimoDeDatos = 1;
        this.nroMinimoDeHijos = 2;
    }
      
    public ArbolB(int orden) throws ExcepcionOrdenInvalido {
        super(orden);
        this.nroMaximoDeDatos = this.orden - 1;
        this.nroMinimoDeDatos = this.nroMaximoDeDatos / 2;
        this.nroMinimoDeHijos = this.nroMinimoDeDatos + 1;
    }
    
    /*
        8. Para el ArbolB implemente el metodo insertar.
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
            this.raiz = new NodoMVias<>(this.orden + 1, claveAInsertar, valorAInsertar);//creamos un nodo con un campo adicional (orden+1)
            return;
        }
        
        Stack<NodoMVias<Key, Value>> pilaDeAncestros = new Stack<>();
        NodoMVias<Key, Value> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {            
            int posicionDeClave = super.obtenerPosicionDeClave(nodoActual, claveAInsertar);
            if (posicionDeClave != POSICION_NO_VALIDA) {
                nodoActual.setValor(posicionDeClave, valorAInsertar);
                nodoActual = NodoMVias.nodoVacio();
            } else {
                if (nodoActual.esHoja()) {
                    this.insertarClaveYValorOrdenadoEnNodo(nodoActual, claveAInsertar, valorAInsertar);
                    if (nodoActual.cantidadDeClavesNoVacias() > this.nroMaximoDeDatos) {
                        this.dividir(nodoActual, pilaDeAncestros);
                    }
                    nodoActual = NodoMVias.nodoVacio();
                } else {
                    int posicionPorDondeBajar = this.obtenerPosicionPorDondeBajar(nodoActual, claveAInsertar);
                    pilaDeAncestros.push(nodoActual);
                    nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
                }
            }
        }
    }
    
    private void dividir(NodoMVias<Key, Value> nodoActual, Stack<NodoMVias<Key, Value>> pilaDeAncentros) {
        NodoMVias<Key, Value> nuevoNodoHijo = new NodoMVias<>(this.orden + 1);//Primer Hijo
        for (int i = 0; i < nroMinimoDeDatos; i++) {
            nuevoNodoHijo.setClave(i, nodoActual.getClave(i));
            nuevoNodoHijo.setValor(i, nodoActual.getValor(i));
            nuevoNodoHijo.setHijo(i, nodoActual.getHijo(i));
        }
        nuevoNodoHijo.setHijo(nroMinimoDeDatos, nodoActual.getHijo(nroMinimoDeDatos));

        int j = 0;
        NodoMVias<Key, Value> nuevoNodoHijo2 = new NodoMVias<>(this.orden + 1);//Segundo Hijo
        for (int i = nroMinimoDeDatos + 1; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            nuevoNodoHijo2.setClave(j, nodoActual.getClave(i));
            nuevoNodoHijo2.setValor(j, nodoActual.getValor(i));
            nuevoNodoHijo2.setHijo(j, nodoActual.getHijo(i));
            j++;
        }
        nuevoNodoHijo2.setHijo(j, nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()));

        if (!pilaDeAncentros.isEmpty()) {
            NodoMVias<Key, Value> nodoAncestro = pilaDeAncentros.pop();
            if (!nodoAncestro.estanClavesLlenas()) {
                Key claveInsertada = nodoActual.getClave(nroMinimoDeDatos);
                //con este metodo se inserta el dato en el lugar correcto
                this.insertarClaveYValorOrdenadoEnNodo(nodoAncestro, nodoActual.getClave(nroMinimoDeDatos), nodoActual.getValor(nroMinimoDeDatos));
                int posicion = obtenerPosicionDeClave(nodoAncestro, claveInsertada);
                nodoAncestro.setHijo(posicion, nuevoNodoHijo);
                nodoAncestro.setHijo(posicion + 1, nuevoNodoHijo2);
                if (nodoAncestro.cantidadDeClavesNoVacias() > this.nroMaximoDeDatos) {
                    this.dividir(nodoAncestro, pilaDeAncentros);
                }
            } else {
                nodoActual.setClave(0, nodoActual.getClave(nroMinimoDeDatos));
                nodoActual.setValor(0, nodoActual.getValor(nroMinimoDeDatos));
                int cantDeClavesNoVacias = nodoActual.cantidadDeClavesNoVacias();
                for (int i = 1; i < cantDeClavesNoVacias; i++) {
                    nodoActual.setClave(i, (Key)NodoMVias.datoVacio());
                    nodoActual.setValor(i, (Value)NodoMVias.datoVacio());
                    nodoActual.setHijo(i, NodoMVias.nodoVacio());
                }
                nodoActual.setHijo(0, nuevoNodoHijo);
                nodoActual.setHijo(1, nuevoNodoHijo2);
            }
        } else {//si la pila esta vacia quiere decir que no tiene ancestro el nodoActual, osea el nodoActual es la raiz
            nodoActual.setClave(0, nodoActual.getClave(nroMinimoDeDatos));
            nodoActual.setValor(0, nodoActual.getValor(nroMinimoDeDatos));
            int cantDeClavesNoVacias = nodoActual.cantidadDeClavesNoVacias();
            for (int i = 1; i < cantDeClavesNoVacias; i++) {
                nodoActual.setValor(i, (Value)NodoMVias.datoVacio());
                nodoActual.setClave(i, (Key)NodoMVias.datoVacio());
                nodoActual.setHijo(i, NodoMVias.nodoVacio());
            }
            nodoActual.setHijo(0, nuevoNodoHijo);
            nodoActual.setHijo(1, nuevoNodoHijo2);
        }
    }

    /*
        9. Para el ArbolB implemente el metodo eliminar.
    */
    @Override
    public Value eliminar(Key claveAEliminar) throws ExcepcionClaveNoExiste {
        if (claveAEliminar == NodoMVias.datoVacio()) {
            throw new IllegalArgumentException("Clave a buscar no puede ser nula");
        }

        Stack<NodoMVias<Key, Value>> pilaDePadres = new Stack<>();
        NodoMVias<Key, Value> nodoActual = this.buscarNodoDeDato(claveAEliminar,pilaDePadres);

        if (NodoMVias.esNodoVacio(nodoActual)) {
            throw new ExcepcionClaveNoExiste();
        }

        int posicionDeClaveAEliminar = super.obtenerPosicionPorDondeBajar(nodoActual, claveAEliminar) - 1;
        Value valorARetornar = nodoActual.getValor(posicionDeClaveAEliminar);

        if (nodoActual.esHoja()) {
            super.eliminarClaveYValorDelNodo(nodoActual, posicionDeClaveAEliminar);
            if (nodoActual.cantidadDeClavesNoVacias() < this.nroMinimoDeDatos) {
                // si llego aca hay que ajustar
                if (pilaDePadres.isEmpty()) {
                    if (nodoActual.cantidadDeClavesNoVacias() == 0) {
                        super.vaciarArbol();
                    }
                }else {
                    NodoMVias<Key, Value> nodoPadre = pilaDePadres.peek();
                    //Posicion del nodo hijo en problema en el nodoPadre
                    int posicionNodoEnProblema = super.obtenerPosicionPorDondeBajar(nodoPadre, claveAEliminar);

                    this.prestarseOFusionarse(nodoActual, pilaDePadres,posicionNodoEnProblema);
                }

            }
        }else {
            pilaDePadres.push(nodoActual);
            NodoMVias<Key, Value> nodoDelPredecesor = this.obtenerNodoDeClavePredecesora(pilaDePadres, nodoActual.getHijo(posicionDeClaveAEliminar));
            int posicionDelPredecesor = nodoDelPredecesor.cantidadDeClavesNoVacias() - 1;
            Key clavePredecesora = nodoDelPredecesor.getClave(posicionDelPredecesor);
            Value datoDelPredecesor = nodoDelPredecesor.getValor(posicionDelPredecesor);
            super.eliminarClaveYValorDelNodo(nodoDelPredecesor, posicionDelPredecesor);
            NodoMVias<Key, Value> padreDelNodoPredecesoor = pilaDePadres.peek();
            //Posicion del nodo hijo en problema en el nodoPadreDelPredecesor
            int posicionNodoEnProblema = super.obtenerPosicionPorDondeBajar(padreDelNodoPredecesoor, clavePredecesora);

            nodoActual.setClave(posicionDeClaveAEliminar, clavePredecesora);
            nodoActual.setValor(posicionDeClaveAEliminar, datoDelPredecesor);

            if (nodoDelPredecesor.cantidadDeClavesNoVacias() < this.nroMinimoDeDatos) {
                this.prestarseOFusionarse(nodoDelPredecesor, pilaDePadres, posicionNodoEnProblema );
            }

        }

        return valorARetornar;
    }

    private NodoMVias<Key, Value> obtenerNodoDeClavePredecesora(Stack<NodoMVias<Key, Value>> pilaDePadres, NodoMVias<Key, Value> nodoActual) {
        while (!nodoActual.esHoja()) {
            pilaDePadres.push(nodoActual);
            nodoActual = nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias());
        }
        return nodoActual;
    }

    private void prestarseOFusionarse(NodoMVias<Key, Value> nodoEnProblema,Stack<NodoMVias<Key, Value>> pilaDePadres, int posicionNodoEnProblema ) {


        while ((nodoEnProblema.cantidadDeClavesNoVacias() < this.nroMinimoDeDatos) && (!pilaDePadres.isEmpty())) {

            NodoMVias<Key, Value> nodoPadre = pilaDePadres.pop();

            NodoMVias<Key, Value> nodoHermanoSig = nodoPadre.getHijo(posicionNodoEnProblema + 1);
            NodoMVias<Key, Value> nodoHermanoAnt;
            if (posicionNodoEnProblema == 0) {
                nodoHermanoAnt = NodoMVias.nodoVacio();
            } else {
                nodoHermanoAnt = nodoPadre.getHijo(posicionNodoEnProblema - 1);
            }

            //El nodoEnProblema se presta del hermano Siguiente,  si este es mayor al minimo de datos
            if (!NodoMVias.esNodoVacio(nodoHermanoSig) && nodoHermanoSig.cantidadDeClavesNoVacias() > this.nroMinimoDeDatos) {
                this.prestarseDelHermanoSig(nodoEnProblema, nodoPadre, nodoHermanoSig, posicionNodoEnProblema);
            }
            //caso contrario  se presta del hermano Anterior, si este es mayor al minimo de datos
            else if (!NodoMVias.esNodoVacio(nodoHermanoAnt) && nodoHermanoAnt.cantidadDeClavesNoVacias() > this.nroMinimoDeDatos) {
                this.prestarseDelHermanoAnt(nodoEnProblema, nodoPadre, nodoHermanoAnt, posicionNodoEnProblema);
            }
            //caso contrario se fuciona con el hermano siguiente si este no es vacio
            else if(!NodoMVias.esNodoVacio(nodoHermanoSig)) {
                posicionNodoEnProblema = fusionarseConHermanoSig(nodoPadre, posicionNodoEnProblema, nodoEnProblema, pilaDePadres, nodoHermanoSig);
                //caso contrario se fuciona con el hermano anterior
            }else {
                posicionNodoEnProblema = fusionarseConHermanoAnt(nodoPadre, posicionNodoEnProblema,nodoEnProblema , pilaDePadres, nodoHermanoAnt);

            }

            nodoEnProblema = nodoPadre;

        }
    }
  
    private int fusionarseConHermanoSig(NodoMVias<Key, Value> nodoPadre, int posicionNodoEnProblema, NodoMVias<Key, Value> nodoEnProblema, Stack<NodoMVias<Key, Value>> pilaDePadres, NodoMVias<Key, Value> nodoHermanoSig) {
        Key claveDelNodoPadre = nodoPadre.getClave(posicionNodoEnProblema);
        Value valorDelNodoPadre = nodoPadre.getValor(posicionNodoEnProblema);
        nodoPadre.setHijo(posicionNodoEnProblema + 1, NodoMVias.nodoVacio());
        super.eliminarClaveYValorDelNodo(nodoPadre, posicionNodoEnProblema);
        this.insertarClaveYValorOrdenadoEnNodo(nodoEnProblema, claveDelNodoPadre, valorDelNodoPadre);

        for (int i = 0; i < nodoHermanoSig.cantidadDeClavesNoVacias(); i++) {
            Key claveHermanoSig = nodoHermanoSig.getClave(i);
            Value valorHermanoSig = nodoHermanoSig.getValor(i);
            NodoMVias<Key, Value> hijosDelHermanoSig = nodoHermanoSig.getHijo(i);
            nodoEnProblema.setHijo(nodoEnProblema.cantidadDeHijosNoVacios(), hijosDelHermanoSig);
            this.insertarClaveYValorOrdenadoEnNodo(nodoEnProblema, claveHermanoSig, valorHermanoSig);
        }

        NodoMVias<Key, Value> hijosDelHermanoSig = nodoHermanoSig.getHijo(nodoHermanoSig.cantidadDeClavesNoVacias());
        nodoEnProblema.setHijo(nodoEnProblema.cantidadDeHijosNoVacios(), hijosDelHermanoSig);

        if (!pilaDePadres.isEmpty()) {
            NodoMVias<Key, Value> nodoAbuelo = pilaDePadres.peek();
            posicionNodoEnProblema = super.obtenerPosicionPorDondeBajar(nodoAbuelo,claveDelNodoPadre);
        }

        if (super.raiz.cantidadDeClavesNoVacias() == 0) {
            super.raiz = nodoEnProblema;
        }
        return posicionNodoEnProblema;
    }

    private int fusionarseConHermanoAnt(NodoMVias<Key, Value> nodoPadre, int posicionNodoEnProblema, NodoMVias<Key, Value>nodoEnProblema , Stack<NodoMVias<Key, Value>> pilaDePadres, NodoMVias<Key, Value> nodoHermanoAnt) {
        Key claveDelNodoPadre = nodoPadre.getClave(posicionNodoEnProblema - 1);
        Value valorDelNodoPadre = nodoPadre.getValor(posicionNodoEnProblema - 1);
        nodoPadre.setHijo(posicionNodoEnProblema, NodoMVias.nodoVacio());
        super.eliminarClaveYValorDelNodo(nodoPadre, posicionNodoEnProblema - 1);
        this.insertarClaveYValorOrdenadoEnNodo(nodoHermanoAnt, claveDelNodoPadre, valorDelNodoPadre);

        for (int i = 0; i < nodoEnProblema.cantidadDeClavesNoVacias(); i++) {
            Key claveNodoEnProblema = nodoEnProblema.getClave(i);
            Value valorNodoEnProblema = nodoEnProblema.getValor(i);
            NodoMVias<Key, Value> hijosDelNodoEnProblema = nodoEnProblema.getHijo(i);
            nodoHermanoAnt.setHijo(nodoHermanoAnt.cantidadDeHijosNoVacios(), hijosDelNodoEnProblema);
            this.insertarClaveYValorOrdenadoEnNodo(nodoHermanoAnt, claveNodoEnProblema, valorNodoEnProblema);
        }
        NodoMVias<Key, Value> hijosDelNodoEnProblema = nodoEnProblema.getHijo(nodoEnProblema.cantidadDeClavesNoVacias());
        nodoHermanoAnt.setHijo(nodoHermanoAnt.cantidadDeHijosNoVacios(), hijosDelNodoEnProblema);

        if (!pilaDePadres.isEmpty()) {
            NodoMVias<Key, Value> nodoAbuelo = pilaDePadres.peek();
            posicionNodoEnProblema = super.obtenerPosicionPorDondeBajar(nodoAbuelo,claveDelNodoPadre);
        }

        if (super.raiz.cantidadDeClavesNoVacias() == 0) {
            super.raiz = nodoHermanoAnt;
        }
        return posicionNodoEnProblema;
    }

    private void prestarseDelHermanoSig(NodoMVias<Key, Value> nodoEnProblema, NodoMVias<Key, Value> nodoPadre , NodoMVias<Key, Value> nodoHermanoSig, int posicionNodoEnProblema) {
        Key claveHermanoSig = nodoHermanoSig.getClave(0);
        Value valorHermanoSig = nodoHermanoSig.getValor(0);
        Key claveDelNodoPadre = nodoPadre.getClave(posicionNodoEnProblema);
        Value valorDelNodoPadre = nodoPadre.getValor(posicionNodoEnProblema);

        super.insertarClaveYValorOrdenadoEnNodo(nodoEnProblema, claveDelNodoPadre, valorDelNodoPadre);
        super.eliminarClaveYValorDelNodo(nodoPadre, posicionNodoEnProblema);
        super.insertarClaveYValorOrdenadoEnNodo(nodoPadre, claveHermanoSig, valorHermanoSig);
        super.eliminarClaveYValorDelNodo(nodoHermanoSig, 0);
    }

    private void prestarseDelHermanoAnt(NodoMVias<Key, Value> nodoEnProblema, NodoMVias<Key, Value> nodoPadre , NodoMVias<Key, Value> nodoHermanoAnt, int posicionNodoEnProblema) {
        Key claveHermanoAnt = nodoHermanoAnt.getClave(nodoHermanoAnt.cantidadDeClavesNoVacias() - 1);
        Value valorHermanoAnt = nodoHermanoAnt.getValor(nodoHermanoAnt.cantidadDeClavesNoVacias() - 1);
        Key claveDelNodoPadre = nodoPadre.getClave(posicionNodoEnProblema - 1);
        Value valorDelNodoPadre = nodoPadre.getValor(posicionNodoEnProblema - 1);

        super.insertarClaveYValorOrdenadoEnNodo(nodoEnProblema, claveDelNodoPadre, valorDelNodoPadre);
        super.eliminarClaveYValorDelNodo(nodoPadre, posicionNodoEnProblema - 1);
        super.insertarClaveYValorOrdenadoEnNodo(nodoPadre, claveHermanoAnt, valorHermanoAnt);
        super.eliminarClaveYValorDelNodo(nodoHermanoAnt, nodoHermanoAnt.cantidadDeClavesNoVacias() - 1);

    }
    
       private NodoMVias<Key, Value> buscarNodoDeDato(Key claveAEliminar, Stack<NodoMVias<Key, Value>> pilaDePadres) {
        NodoMVias<Key, Value> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            boolean huboCambioDeNodo = false;
            for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias() && !huboCambioDeNodo; i++) {
                Key claveActual = nodoActual.getClave(i);
                if (claveAEliminar.compareTo(claveActual) == 0) {
                    return nodoActual;
                }

                if (claveAEliminar.compareTo(claveActual) < 0) {
                    pilaDePadres.push(nodoActual);
                    nodoActual = nodoActual.getHijo(i);
                    huboCambioDeNodo = true;
                }

            }
            if (!huboCambioDeNodo) {
                pilaDePadres.push(nodoActual);
                nodoActual = nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias());
            }
        }
        return NodoMVias.nodoVacio();
    }
}