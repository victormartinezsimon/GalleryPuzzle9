package com.victormartinezsimon.puzzle9gallery;

import java.util.List;


import IA.Nodo.Movimientos;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



public class MyCanvas extends View 
{

	
	
	private Game myGame;
	
	Bitmap[] arrayDibujos;
	int widthPantalla=-1;
	int heighPantalla=-1;
	
	String srcPath=null;
	Bitmap bitmapFoto=null;
	
	private boolean threadAcabado;
	
	List<Movimientos> listaMovimientos=null;
	int indiceMovimiento=0;
	
	public MyCanvas(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		setFocusableInTouchMode(true);
		setFocusable(true);
	
	}
	public MyCanvas(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		setFocusableInTouchMode(true);
		setFocusable(true);
	
	}
	public MyCanvas(Context context) 
	{
		super(context);
		setFocusableInTouchMode(true);
		setFocusable(true);
		
		
	}

	public void build(Game myGame, String srcPath, Bitmap bitmapFoto)
	{
		this.myGame= myGame;
		arrayDibujos= new Bitmap[myGame.getAncho()*myGame.getAncho()];	
		this.srcPath=srcPath;
		this.bitmapFoto=bitmapFoto;
	}
	
	public Bitmap[] getArrayDibujos() 
	{
		return arrayDibujos;
	}

	public Game getMyGame() 
	{
		return myGame;
	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas)
	{
		Paint paint= new Paint();
		paint.setColor(getResources().getColor(R.color.abc_search_url_text_holo));//7f070003
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
		
		leerImagenes();
		
		if(threadAcabado && listaMovimientos!= null && listaMovimientos.size()!= indiceMovimiento)
		{
			if(indiceMovimiento==0)
				myGame.updateTexto();
			myGame.getTablero().realizarMovimiento(listaMovimientos.get(indiceMovimiento));
			indiceMovimiento++;
			invalidate();
			
		}
		
		
		for(int i=0;i<arrayDibujos.length;i++)
		{
			int columna= i % myGame.getAncho();
			int fila= i/myGame.getAncho();
			
			int numeroTablero= myGame.getTablero().getEstado()[i];
			
			if(numeroTablero!= myGame.getTablero().getVacio())
			{
				int indice;
				if(!threadAcabado)
				{
					indice=i;
				}
				else
				{
					indice=numeroTablero;
				}
				canvas.drawBitmap(arrayDibujos[indice], columna*widthPantalla,fila*heighPantalla,paint);
			}
				
		}
		
		
		
		int anchoBorde=10;
		int anchoSeparacion=5;
		dibujarSeparaciones(canvas, anchoSeparacion);
		dibujarBordes(canvas,anchoBorde);
		
	}
	private void dibujarSeparaciones(Canvas canvas, int ancho) 
	{
		Paint paint= new Paint();
		paint.setColor(0xff000ff0);
		//columnas
		for(int i=0;i<myGame.getAncho();i++)
		{
			canvas.drawRect(i*widthPantalla, 0, i*widthPantalla+ancho, getHeight(), paint);		
		}
		
		//filas
		for(int j=0;j<myGame.getAncho();j++)
		{
			canvas.drawRect(0, j*heighPantalla, getWidth(),j*heighPantalla+ancho, paint);
		}
	}
	private void dibujarBordes(Canvas canvas,int ancho)
	{
		
		Paint paint= new Paint();
		paint.setColor(0xff000ff0);
		
		//borde de izquierda
		canvas.drawRect(0, 0, ancho, getHeight(), paint);
		//borde de derecha
		canvas.drawRect(getWidth()-ancho, 0, getWidth(), getHeight(), paint);
		//borde arriba
		canvas.drawRect(0, 0, getWidth(),ancho, paint);
		//borde abajo
		canvas.drawRect(0, getHeight()-ancho, getWidth(),getHeight(), paint);
		
	}

	private void leerImagenes()
	{		
		if(widthPantalla==-1 && heighPantalla==-1)
		{
		
			widthPantalla= getWidth()/myGame.getAncho();
			heighPantalla= getHeight()/myGame.getAncho();
			
			Bitmap bitmapOriginal=null;
			
			if(srcPath!= null && srcPath.length()>0)
			{
				bitmapOriginal= BitmapFactory.decodeFile(myGame.getSrcPath());
			}
			else
			{
				if(bitmapFoto!= null)
				{
					bitmapOriginal=bitmapFoto;
				}
			}
			
			
			//calculo de los tamaños
			int widthBitmap= bitmapOriginal.getWidth()/myGame.getAncho();
			int heighBitmap = bitmapOriginal.getHeight()/myGame.getAncho();
			
			for(int i=0;i<arrayDibujos.length;i++)
			{
				int columna= i % myGame.getAncho();
				int fila= i/myGame.getAncho();
				
				Bitmap porcion= Bitmap.createBitmap(bitmapOriginal, columna*widthBitmap, fila*heighBitmap, widthBitmap, heighBitmap);
				arrayDibujos[i]= Bitmap.createScaledBitmap(porcion, widthPantalla, heighPantalla, false);
				
			}
			
			
			
			
			
		}		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(threadAcabado && listaMovimientos.size()== indiceMovimiento)
		{
			float eventX = event.getX();
		    float eventY = event.getY();

		    switch (event.getAction())
		    {
			    case MotionEvent.ACTION_DOWN:
			     
			    	if(tratarPulsacion(eventX,eventY))
			    	{
			    		invalidate();
			    		return true;
			    	}

			   
		    }
		}    

	    return false;
	  }
	private boolean  tratarPulsacion(float eventX, float eventY) 
	{
		int filaPulsacion=(int) (eventY/heighPantalla);
		int columnaPulsacion= (int) (eventX/widthPantalla);
		
		int filaVacio= myGame.getTablero().getPosicionVacia()/myGame.getAncho();
		int columnaVacio=myGame.getTablero().getPosicionVacia()%myGame.getAncho();
		
		
		if(filaPulsacion== filaVacio)
		{
			int resta= columnaPulsacion-columnaVacio;
			
			if(resta==1)
			{
				myGame.realizarMovimiento(Movimientos.DERECHA);
				return true;
			}
			if(resta==-1)
			{
				myGame.realizarMovimiento(Movimientos.IZQUIERDA);
				return true;
			}
			
		}
		
		if(columnaPulsacion==columnaVacio)
		{
			int resta= filaPulsacion-filaVacio;
			
			if(resta==1)
			{
				myGame.realizarMovimiento(Movimientos.ABAJO);
				return true;
			}
			if(resta==-1)
			{
				myGame.realizarMovimiento(Movimientos.ARRIBA);
				return true;
			}
			
		}
		
		
		return false;
	}
	
	public void setThreadAcabado(boolean val)
	{
		threadAcabado=val;
		
	}
	public void setListaMovimientos(List<Movimientos> listaMovimientos) 
	{
		this.listaMovimientos=listaMovimientos;
		indiceMovimiento=0;
	}
	public boolean desordenado() 
	{
		return threadAcabado && listaMovimientos!= null && listaMovimientos.size()== indiceMovimiento;
	}
	
}
