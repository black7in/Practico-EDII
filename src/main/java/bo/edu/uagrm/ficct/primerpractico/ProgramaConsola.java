/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package bo.edu.uagrm.ficct.primerpractico;

/**
 *
 * @author HP
 */
public class ProgramaConsola {

    public static void main(String[] args) throws NullPointerException, ExcepcionClaveNoExiste {
        
        ArbolBinarioBusqueda<Integer, String> miArbol = new ArbolBinarioBusqueda<>();
        miArbol.insertar(50, "Rojo");
        miArbol.insertar(30, "Azul");
        miArbol.insertar(40, "Verde");
        miArbol.insertar(60, "Amarillo");
        miArbol.insertar(70, "Negro");
        
        ArbolBinarioBusqueda<Integer, String> miArbol2 = new ArbolBinarioBusqueda<>();
        miArbol2.insertar(50, "Rojo");
        miArbol2.insertar(30, "Azul");
        miArbol2.insertar(40, "Verde");
        miArbol2.insertar(60, "Amarillo");
        miArbol2.insertar(70, "Negro");
        
        ArbolMViasBusqueda<Integer, String> miArbolM = new ArbolMViasBusqueda<>();
        miArbolM.insertar(50, "Rojo");
        miArbolM.insertar(30, "Azul");
        miArbolM.insertar(40, "Verde");
        miArbolM.insertar(60, "Amarillo");
        miArbolM.insertar(70, "Negro");
        miArbolM.insertar(80, "Blnaco");
        
        ArbolMViasBusqueda<Integer, String> miArbolM2 = new ArbolMViasBusqueda<>();
        miArbolM2.insertar(50, "Rojo");
        miArbolM2.insertar(30, "Azul");
        miArbolM2.insertar(40, "Verde");
        miArbolM2.insertar(60, "Amarillo");
        miArbolM2.insertar(70, "Negro");
        miArbolM2.insertar(80, "Blnaco");
        
        AVL<Integer, String> miArbolAVL = new AVL<>();
        ArbolB<Integer, String> miArbolB = new ArbolB<>();
        
        System.out.println("Ejercicio 1: Implemente un metodo que reciba en listas de parametros las llaves......");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 2: Para un Arbol Binario de busqueda imple......");
        System.out.println("Recorrido Post Orden Iterativo: " + miArbol.recorridoEnPostOrdenIter());
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 3: Para un Arbol MVias implementar recorrido Postorden......");
        System.out.println("Recorrido Post Orden: " + miArbolM.recorridoEnPostOrden());
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 4: Para un Arbol MVias implementar recorrido PreOrden......");
        System.out.println("Recorrido Post Orden: " + miArbolM.recorridoEnPreOrden());
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 5: Para un Arbol MVias implementar recorrido InOrden......");
        System.out.println("Recorrido Post Orden: " + miArbolM.recorridoEnInOrden());
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 6: Para el Arbol AVL implemente metodo insertar......");
        miArbolAVL.insertar(10, "Rojo");
        miArbolAVL.insertar(20, "Amarillo");
        miArbolAVL.insertar(30, "Verde");
        miArbolAVL.insertar(40, "Rosado");
        System.out.println(miArbolAVL.toString());
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 7: Para el Arbol AVL implemente metodo eliminar......");
        miArbolAVL.eliminar(10);
        System.out.println(miArbolAVL.toString());
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 8: Para el Arbol B implemente metodo insertar......");
        miArbolB.insertar(50, "Rojo");
        miArbolB.insertar(30, "Azul");
        miArbolB.insertar(40, "Verde");
        miArbolB.insertar(60, "Amarillo");
        miArbolB.insertar(70, "Negro");
        miArbolB.insertar(80, "Blnaco");
        System.out.println(miArbolB.toString());
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 9: Para el Arbol B implemente metodo eliminar......");
        miArbolB.eliminar(50);
        System.out.println(miArbolB.toString());
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 10: Para el MVias implemente metodo insertar......");
        miArbolM.insertar(100, "Cafe");
        System.out.println(miArbolM.toString());
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 11: Para el MVias implemente metodo eliminar......");
        miArbolM.eliminar(100);
        System.out.println(miArbolM.toString());
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 12: Implemente un metodo recursivo que retorne......");
        
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 13: Implemente un metodo recursivo que retorne......");
        System.out.println("Numero de hijos Vacios: " + miArbol.numeroHijosVacioLogicaInOrden());
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 14: Implemente un metodo privado que reciba......");
        System.out.println("Predeceso InOrden: " + miArbol.predecesorInOrden());
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 15: Implemente un metodo que retorne ve......");
        System.out.println("Hay Nodos Completos en el nivel 2?: " + miArbolM.nodosCompletosEnElNivelN(2));
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 16: Implemente una clase......");   

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 17: ......");
        System.out.println("Son Similares los Arbol miArbolM y miArbolM2?: " + miArbolM.esSimilar(miArbolM2));
        
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Ejercicio 18: ......");
        System.out.println("Son Similares los Arbol miArbol y miArbol2?: " + miArbol.esSimilar(miArbol2));
        
        
    }
}
