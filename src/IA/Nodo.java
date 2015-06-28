package IA;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Nodo implements Comparable<Nodo>
{
	public enum Movimientos{ARRIBA,ABAJO,DERECHA,IZQUIERDA};
	private int movimientosReales=0;
	private int estimacion=Integer.MAX_VALUE;
	private  int[] estado;
	private int ancho;
	private  int posicionVacia;
	
	private int vacio=-1;
	
	private List<Movimientos> resultado;
	
	
	public Nodo(Nodo nodoOrigen,Movimientos movimiento)
	{
		this.estado= new  int[nodoOrigen.ancho*nodoOrigen.ancho];
		vacio=nodoOrigen.vacio;
		this.movimientosReales=nodoOrigen.getMovientosReales()+1;
		this.ancho=nodoOrigen.ancho;
		for(int i=0;i<ancho*ancho;i++)
		{
			this.estado[i]=nodoOrigen.estado[i];
			if(estado[i]==vacio)
			{
				posicionVacia=i;
			}
		}
		
		resultado= new ArrayList<Nodo.Movimientos>();
		List<Movimientos> resultadoaux=(List<Movimientos>) nodoOrigen.getResultado();
		for(Movimientos m: resultadoaux)
		{
			resultado.add(m);
		}
		resultado.add(movimiento);
		
		realizarMovimiento(movimiento);
		
		calcularEstimacion();
	}

	public Nodo(int[] puzzleOrigen,int posicionVAcia,int ancho) 
	{
		this.estado=puzzleOrigen;
		this.ancho=ancho;
		this.posicionVacia=posicionVAcia;
		resultado= new ArrayList<Nodo.Movimientos>();
		vacio= puzzleOrigen.length-1;
		
		
	}

	public List<Movimientos> getResultado() 
	{
	
		return this.resultado;
	}

	private int getMovientosReales()
	{
		return this.movimientosReales;
	}

	public void realizarMovimiento(Movimientos movimiento)
	{
		if(posicionVacia< ancho && movimiento==Movimientos.ARRIBA)
		{
			//es de la primera fila
			return;
		}
		
		if(posicionVacia% ancho ==0 && movimiento==Movimientos.IZQUIERDA)
		{
			//columna de la izquierda
			return;
		}
		
		if(posicionVacia% ancho ==(ancho-1) && movimiento==Movimientos.DERECHA)
		{
			//columna de la derecha
			return;
		}
		if(posicionVacia>= ancho*(ancho-1) && movimiento==Movimientos.ABAJO)
		{
			//fila de abajo
			return;
		}
		Log.d("MOVIMIENTO", escribirTablero());
		Log.d("MOVIMIENTO", movimiento.toString());
		
		//moviientos validos
		switch (movimiento) 
		{
			case ARRIBA: 
				intercambiar(posicionVacia,posicionVacia-ancho);
				posicionVacia=posicionVacia-ancho;				
				break;
			case ABAJO: 
				intercambiar(posicionVacia,posicionVacia+ancho);
				posicionVacia=posicionVacia+ancho;
				break;
			case DERECHA: 				
				intercambiar(posicionVacia,posicionVacia+1);
				posicionVacia=posicionVacia+1;				
				break;
			case IZQUIERDA: 
				intercambiar(posicionVacia,posicionVacia-1);
				posicionVacia=posicionVacia-1;				
				break;
		
		
		}
		Log.d("MOVIMIENTO", escribirTablero());
		
	}

	private void intercambiar(int pos1, int pos2) 
	{
		
		int aux= estado[pos1];
		estado[pos1]=estado[pos2];
		estado[pos2]=aux;
		
		
		
	}

	public boolean estadoFinal()
	{
		
		boolean devolver=true;
		int i=0;
		
		while(devolver && i< ancho*ancho)
		{
			if(estado[i]!=i )
			{
				devolver=false;
			}
			else
			{
				i++;
			}
		}
		
		return devolver;
		
	}

	
	public void calcularEstimacion()
	{
		
		int manhattan=calcularDistanciaManhattan();
		int flips=0;//calcularFlips();
		
		int ponderados= manhattan+ 4*flips;
		
		estimacion = movimientosReales+ponderados;
		
	}

	private int calcularDistanciaManhattan() 
	{
		int devolver=0;
		for(int i=0;i<estado.length;i++)
		{
			int valor= estado[i];
			
			if(valor!=vacio)
			{
				//miro cuantas filas he de subir/bajar
				int filaIdeal= valor/ancho;
				int filaActual= i/ancho;
				
				devolver+= Math.abs(filaIdeal-filaActual);
				
				//miramos la columnas
				int columnaIdeal= valor%ancho;
				int columnaActual= i%ancho;
				
				devolver+= Math.abs(columnaIdeal-columnaActual);		
				
			}
			
		}
		
		return devolver;
		
	}
	@Override
	public int compareTo(Nodo another) 
	{
		
		if(estimacion < another.estimacion)
		{
			return -1;
		}
		if(estimacion>another.estimacion)
		{
			return 1;
		}
				
		return 0;
	}
	
	public boolean iguales(Nodo another)
	{
		for(int i=0;i<ancho*ancho;i++)
		{
			if(another.estado[i]!= estado[i])
				return false;
		}
		
		
		return true;
	}
	

	public String escribirSolcion() 
	{
		String devolver="";
		
		for(Movimientos m: resultado)
		{
			devolver+= m+"|";
		}
		
		return devolver;
	}
	
	public String escribirTablero()
	{
		String devolver="";
		for(int i=0;i<estado.length;i++)
		{
			devolver+=estado[i]+"|";
			if(i%ancho== ancho-1)
			{
				devolver+="\n";
			}
		}
		return devolver;
	}

	public int getMovimientosReales() {
		return movimientosReales;
	}

	public int getEstimacion() {
		return estimacion;
	}

	public int[] getEstado() {
		return estado;
	}

	public int getAncho() {
		return ancho;
	}

	public int getPosicionVacia() {
		return posicionVacia;
	}

	public int getVacio() {
		return vacio;
	}
	
	
}
