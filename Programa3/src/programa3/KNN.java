package programa3;
//import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import programa3.Punto;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;
public class KNN extends AbstractAnalysis{
    ArrayList<Punto> puntos = new ArrayList<Punto>();
    ArrayList<Color> colores = new ArrayList<Color>();
    ArrayList<Punto> atractores = new ArrayList<Punto>();
    ArrayList<java.awt.Color> colorJva = new ArrayList<java.awt.Color>();
    Random random = new Random();
    int numPuntos,numClases,numVecinos,distancia,x,y,z,clas;
    Coord3d coordenadas[], coordenada[];
    Color colors[], color[];
    JLabel clasificado;
    JPanel c;
    int n;
    public KNN(int numPuntos,int numClases, int numVecinos,int escala, JLabel clasificado,JPanel c){
        chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
        this.numPuntos = numPuntos;
        this.numClases = numClases;
        this.numVecinos = numVecinos;
        this.coordenada = new Coord3d[1];
        this.color = new Color[1];
        this.n = escala;
        this.clasificado = clasificado;
        this.c = c;
        generaPuntos();
        asiganarColor();
        generarAtractores();
        clasificarPuntos();
        
        
        //grafica3D();
        
    }
    
    
    /*public static void main(String[] args) {
        try{
            KNN k = new KNN(1000,2,10,0,500);
            k.clasificarPunto(50, 80, 100);
            AnalysisLauncher.open(k);
           
        }
        catch(Exception e){{
            System.out.println(e);
            
            
        }}
    }*/
    
    public void clasificarPunto(int x,int y, int z,int distancia){
        this.distancia = distancia;
        coordenas3DPrimera();
        Scatter scatter = new Scatter(coordenadas, colors,4);
        grafica3D(scatter);
        
        System.out.print(puntos.size());
        ArrayList<Float> pDistancias = new ArrayList<Float>();
        ArrayList<Float> aux = new ArrayList<Float>();
        int punto[] = {x,y,z};
    
        for(int i=0;i<puntos.size();i++){
            switch(distancia){
                case 0:
                    pDistancias.add(distanciaEuclidiana(punto,puntos.get(i).getCoordena()));
                    break;
                case 1:
                    pDistancias.add(distanciaSemiverseno(punto,puntos.get(i).getCoordena()));

                    break;

                case 2:
                    pDistancias.add(distanciaManhattan(puntos.get(i).getCoordena(),punto));
                    break;

                case 3:
                    pDistancias.add(distanciaChebyshev(puntos.get(i).getCoordena(),punto));
                    break;
            }
            
            
            //System.out.println("Distancia con respecto al punto "+i+": "+pDistancias.get(i));
        }
        aux.addAll(pDistancias);
        Collections.sort(pDistancias);
        
        ArrayList<Integer> clases = new ArrayList<Integer>();
        for(int n=0;n<numVecinos;n++){
            //Optiene las clases de los vecinos que se le parecen más 
            int idx = aux.indexOf(pDistancias.get(n));
            clases.add(puntos.get(idx).getClase());
            //System.out.println("Clase: "+ clases.get(n));
        }
        ArrayList<Integer> frecuencia = new ArrayList<Integer>();
        for(int m=0;m<numVecinos;m++){
            //Se identifica la frecuencia de cada clase
            frecuencia.add(Collections.frequency(clases, m));
            //System.out.println("Frecuencia: "+ frecuencia.get(m));
        }
        //Se obtiene la clase con más frecuencia
        clas = frecuencia.indexOf(Collections.max(frecuencia));
        //Se crea un nuevo punto
        Punto nuevoPunto = new Punto();
        nuevoPunto.setCoordena(x, y, z);
        nuevoPunto.setClase(clas);
        nuevoPunto.setColor(colores.get(clas));
        //Se agrega el nuevo punto a los puntos 
        puntos.add(nuevoPunto);
        clasificado.setText("Clase: "+clas);
        c.setBackground(colorJva.get(clas));
        
        grafica3D(generarColorCoordenada(x,y,z,colores.get(clas)));
        

    }
    
    public void generaPuntos(){
        //Se generan los puntos de mane aleatoria y se crea un nuevo punto asignado las coordenadas
        //Se almacenan en una arrayList de puntos
        for(int i=0; i<numPuntos;i++){
            Punto pt = new Punto();
            //Se inicializa la coordenada
            pt.setCoordena(random.nextInt(n), random.nextInt(n), random.nextInt(n));
            //Se añade lala lista de puntos
            puntos.add(pt);
            //System.out.println(pt.getCoordena()[0]+","+pt.getCoordena()[1]+","+pt.getCoordena()[2]);
        }
    }
    public void asiganarColor(){
    //A cada clase le asigna un color 
    for(int i=0;i<numClases;i++){
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        colores.add(new Color(r,g,b));
        colorJva.add(new java.awt.Color(r,g,b));
        //System.out.println(colores.get(i));
    }
    
    }

    
    public void clasificarPuntos(){
        
        /*Se realiza una clasificación de puntos, por medio de un algoritmo paracido al KMeans,
        en definen atractores de forma aleatoria, y posteriormente se mediden las distancias con respecto 
        a eso puntos, se asigna la case de la distancia más pequeña*/
        
        for(int i=0;i<numPuntos;i++){
            int idx;
            ArrayList<Float> distancias = new ArrayList<Float>();
            for(int j=0;j<numClases;j++){
                //Se calculan todas las distancias para cada atractor
                distancias.add(distanciaEuclidiana(atractores.get(j).getCoordena(),puntos.get(i).getCoordena()));
                //System.out.println("Punto "+i+": "+"Distancia "+j+": "+distancias.get(j));
            }
            //Se obtiene el indice de la distancia minima
            idx = idxMin(distancias);
           // System.out.println("\n Indice de la distancia Minima de "+i+": "+idx);
            //Se asigna el valor de la clase al punto, en donde pertenece, así como el color de esa clase
            puntos.get(i).setClase(idx);
            puntos.get(i).setColor(colores.get(idx));
        }
        
        
    }
    public void generarAtractores(){
        //Se generan n atractores, deacuerdo con la clases
        for(int i=0; i<numClases;i++){
            atractores.add(new Punto());
            atractores.get(i).setCoordena(random.nextInt(n), random.nextInt(n), random.nextInt(n));
        }
    
    }
    
    //Distancias
    public float distanciaEuclidiana(int[] coordenada1, int[] coordenada2){
        float distancia, cuadrados;
        cuadrados = ((float) Math.pow((coordenada1[0]-coordenada2[0]), 2))+((float) Math.pow((coordenada1[1]-coordenada2[1]), 2))+((float)Math.pow((coordenada1[2]-coordenada2[2]), 2));
        distancia = (float) Math.sqrt(cuadrados);
        return distancia;
    }
    
    public float distanciaSemiverseno(int[] punto1, int[] punto2){
        double p1[] = new double[2];
        double p2[] = new double[2];
        float distancia;
        p1[0]= (double) Math.toRadians(punto1[0]);
        p1[1]= (double) Math.toRadians(punto1[1]);
        p2[0]= (double) Math.toRadians(punto2[0]);
        p2[1]= (double) Math.toRadians(punto2[1]);
        
        distancia = (float) Math.acos(Math.sin(p1[0])*Math.sin(p2[0])+ Math.cos(p1[0])*Math.cos(p2[0])*Math.cos(p1[1]-p2[1]));
        
        return distancia;
    
    }
    
    public float distanciaManhattan(int[] punto1, int[] punto2){
        float distancia;
        distancia = Math.abs(punto1[0]-punto2[0])+Math.abs(punto1[1]-punto2[1])+Math.abs(punto1[2]-punto2[2]);
        return distancia;
    }
    
    public float distanciaChebyshev(int[] punto1, int[] punto2){
        float distancia;
        distancia = Math.max(Math.abs(punto1[0]-punto2[0]),Math.abs(punto1[1]-punto2[1]));
        distancia = Math.max(distancia, Math.abs(punto1[2]-punto2[2]));
        return distancia;
    }
    
 
    
    
    public int idxMin(ArrayList distancias){
        //Permite encontrar la distancia más pequela y devuelve el valor del indice en donde se encuentra
        float min = (float) distancias.get(0);
        int idx;
        for(int i=0;i<distancias.size();i++){
            if((float)distancias.get(i)<min){
                min = (float) distancias.get(i);
            }
        }
        idx = distancias.indexOf(min);
        return idx;
    }
    
    public void coordenas3DPrimera(){
    //Se obtienes las coordenadas de los puntos y se obtiene los colores de cada punto
    int i;
    coordenadas = new Coord3d[puntos.size()];
    colors = new Color[puntos.size()];
    //System.out.println(puntos.size());
        for(i=0;i<puntos.size();i++){
            //System.out.println("Coordendas "+i+": "+puntos.get(i).getCoordena()[0]+","+puntos.get(i).getCoordena()[1]+","+puntos.get(i).getCoordena()[2]);
            coordenadas[i] = new Coord3d(puntos.get(i).getCoordena()[0],puntos.get(i).getCoordena()[1],puntos.get(i).getCoordena()[2]);
            colors[i] = puntos.get(i).getColor();
        }
    }
    
    public void grafica3D(Scatter scatter){
        //coordenadas, colores y tamaño
        //Scatter scatter = new Scatter(coordenadas, colors,4);
        chart.getScene().add(scatter);
        //chart.getScene().wait(.2);
        
        
    
    }
    public void eliminar3D(Scatter scatter){
        //Elimina un punto
        chart.getScene().remove(scatter);
        
    
    }
        
    public Scatter generarColorCoordenada(int x, int y, int z,Color c){
        //Se crea un scatter con los datos de un punto en especifico para graficarlo
        color[0] = c;
        coordenada[0] = new Coord3d(x,y,z);
        Scatter scatter = new Scatter(coordenada,color,15);
        
        return scatter;
    }

    @Override
    public void init() throws Exception {
        
    }
    
}
