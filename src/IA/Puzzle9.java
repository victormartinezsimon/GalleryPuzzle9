package IA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import IA.Nodo.Movimientos;

public class Puzzle9 
{

	List<Nodo> listaAEvaluar;
	Nodo nodoFinal;
	List<Movimientos> movimientosRealizados;
	
	public boolean paradaForzada=false;

	
	public Puzzle9(int ancho, int numeroMovIniciales)
	{
		Random r= new Random(System.currentTimeMillis());
		
		movimientosRealizados= new ArrayList<Nodo.Movimientos>();
		
		int[] puzzle=new int[ancho*ancho];
		
		for(int i=0;i<ancho*ancho;i++)
		{
			puzzle[i]=i;
		}
		
		int vacio=ancho*ancho-1;		
		Nodo n= new Nodo(puzzle,vacio,ancho);
		
		int movimiento=r.nextInt(Movimientos.values().length);
		
		n.realizarMovimiento(Movimientos.values()[movimiento]);		
		movimientosRealizados.add(Movimientos.values()[movimiento]);
		
		int ultimoMovimiento=movimiento;
		
		int i=0;
		
		while(i<numeroMovIniciales-1)
		{
			movimiento=r.nextInt(Movimientos.values().length);
			
			if(!movimientoInverso(movimiento,ultimoMovimiento))
			{
				i++;
				n.realizarMovimiento(Movimientos.values()[movimiento]);	
				movimientosRealizados.add(Movimientos.values()[movimiento]);
				ultimoMovimiento=movimiento;
			}
			
			
			
		}

		listaAEvaluar= new ArrayList<Nodo>();
		listaAEvaluar.add(n);	
		
		
		
	}
	
	private boolean movimientoInverso(int movimiento, int ultimoMovimiento) 
	{
		if(movimiento==0 && ultimoMovimiento==1)
		{
			return true;
		}
		if(movimiento==1 && ultimoMovimiento==0)
		{
			return true;
		}
		
		if(movimiento==2 && ultimoMovimiento==3)
		{
			return true;
		}
		if(movimiento==3 && ultimoMovimiento==2)
		{
			return true;
		}
		
		return false;
	}

	public int calcularSolucion()
	{
		
		boolean seguirBuscando=true;
	
		while(seguirBuscando && !listaAEvaluar.isEmpty() && !paradaForzada)
		{
			//cojo nodo primero
			
			Nodo origen= listaAEvaluar.get(0);
			listaAEvaluar.remove(origen);
			
			if(origen.estadoFinal())
			{
				seguirBuscando=false;
				nodoFinal=origen;
			}
			
			
			
			Nodo arriba= new Nodo(origen, Movimientos.ARRIBA);
			Nodo abajo= new Nodo(origen, Movimientos.ABAJO);
			Nodo derecha= new Nodo(origen, Movimientos.DERECHA);
			Nodo izquierda= new Nodo(origen, Movimientos.IZQUIERDA);
			
			if(!arriba.iguales(origen))
			{
				listaAEvaluar.add(arriba);
			}
			if(!abajo.iguales(origen))
			{
				listaAEvaluar.add(abajo);
			}
			if(!derecha.iguales(origen))
			{
				listaAEvaluar.add(derecha);
			}
			if(!izquierda.iguales(origen))
			{
				listaAEvaluar.add(izquierda);
			}
			
			
			Collections.sort(listaAEvaluar);
			
		}
		
		if(paradaForzada)
		{
			return movimientosRealizados.size();
		}
		
		return nodoFinal.getResultado().size();
	}

	public Nodo getNodoFinal() 
	{
		return nodoFinal;
	}
	
	public List<Movimientos> getListaMovimientos()
	{
			return movimientosRealizados;
	}
}

