package com.victormartinezsimon.puzzle9gallery;

import IA.Nodo;
import IA.Puzzle9;
import IA.Nodo.Movimientos;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class Game extends ActionBarActivity
{

	private String srcPath;
	private Bitmap bitmapFoto;
	private int movimientos;
	private int ancho;
	
	private Nodo tablero;
	
	private MyCanvas myCanvas;
	private TextView textoInformacion;
	
	public static String GAMEOVER="GAMEOVER";
	public static String PUNTOS="PUNTOS";
	public static String FROMMENU = "FROMMENU";
	public static final String LEVEL = "LEVEL";
	
	
	float puntos;
	float multiplicador;
	
	boolean threadAcabado=false;
	
	public int MOVIMIENTOSDESORDEN=25;
	
	Puzzle9 puzzle9;
	
	private String level;
	
	protected long tiempoMaximoThread=1000*15;//30 segundos
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.activity_game);

	
		Bundle bundle = getIntent().getExtras();
		srcPath=bundle.getString(SourceSelection.PATH);
		multiplicador=bundle.getFloat(SourceSelection.MULTIPLICADOR);
		level= bundle.getString(Game.LEVEL);
		
		ancho=bundle.getInt(SourceSelection.ANCHO);
		
		if(getIntent().hasExtra(SourceSelection.BITMAPSRC))
		{
			byte[] byteArray = getIntent().getByteArrayExtra(SourceSelection.BITMAPSRC);
			bitmapFoto= BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		}
		
		//inicializacion del thread y de variables para que no pete

		int[] puzzle=new int[ancho*ancho];
		
		for(int i=0;i<ancho*ancho;i++)
		{
			puzzle[i]=i;
		}
		
		int vacio=ancho*ancho-1;		
	    tablero= new Nodo(puzzle,vacio,ancho);
	    
		textoInformacion= (TextView) Game.this.findViewById(R.id.textoInformacionGame);
	    updateTexto();
	    
	    new Thread(runnableAEstrella).start();
	    new Thread(runnableTimeOut).start();
			
		myCanvas=  (MyCanvas) this.findViewById(R.id.myCanvasGame);
		myCanvas.build(this,srcPath,bitmapFoto);
		myCanvas.setThreadAcabado(threadAcabado);
		
		//setContentView(myCanvas);
		myCanvas.requestFocus();	
		
		
		
	}
	
	public void realizarMovimiento(Movimientos movimiento)
	{
		tablero.realizarMovimiento(movimiento);
		movimientos--;
		updateTexto();
		int check=checkFinJuego();
		if(check!=-1)
		{
			Intent intent= new Intent(this, GameOver.class);	
			
			//hacemos algo
			if(check==1)
			{
				//lo hemos completado
				intent.putExtra(GAMEOVER, false);
				intent.putExtra(PUNTOS, puntos*2);			
				
				
			}
			else
			{
				intent.putExtra(GAMEOVER, true);
				intent.putExtra(PUNTOS, 0);
			}
			intent.putExtra(LEVEL, level);
			intent.putExtra(FROMMENU, false);
			this.startActivity(intent);
			this.finish();
		}
		
	}
	
	public int checkFinJuego()
	{
		if(movimientos<=0 && myCanvas.desordenado())
		{
			return 0;
		}
		
		if(tablero.estadoFinal()&&myCanvas.desordenado())
		{
			return 1;
		}
		
			
		return -1;
	}
		
	public void updateTexto()
	{
		if(!threadAcabado)
		{
			textoInformacion.setText("Desordenando...");
		}
		else
		{
			updatePuntos();
			textoInformacion.setText("Movimientos Restantes: " + movimientos+"\n Puntos totales: "+puntos);
		}
		
	}
	
	private void updatePuntos()
	{
		puntos= movimientos* (3-multiplicador);
	}

	public String getSrcPath() 
	{
		return srcPath;
	}

	public int getMovimientos() 
	{
		return movimientos;
	}

	public int getAncho() 
	{
		return ancho;
	}

	public Nodo getTablero()
	{
		return tablero;
	}

	public void onBackPressed()
	{
		super.onBackPressed();
		if(!threadAcabado)
		{
			puzzle9.paradaForzada=true;
		}
		
	}
	
	private Runnable runnableAEstrella= new Runnable() 
	{
		
		@Override
		public void run() 
		{
			puzzle9= new Puzzle9(ancho,MOVIMIENTOSDESORDEN);
	        movimientos= puzzle9.calcularSolucion();
	        //Log.d("SOLUCION",puzzle9.getNodoFinal().escribirSolcion());
	        
	        movimientos*=multiplicador;
	        			
			threadAcabado=true;
			myCanvas.setThreadAcabado(threadAcabado);
			myCanvas.setListaMovimientos(puzzle9.getListaMovimientos());			
			
			Message msg= new Message();
			msg.arg2=1;
			HandlerTexto.sendMessage(msg);
			
		}
	};
		
	private Runnable runnableTimeOut= new Runnable()
	{
		long espera=10;//10 ms;
		long timeAcumulado=0;
		@Override
		public void run() 
		{
			boolean pararThread=false;
			
			while(!threadAcabado && !pararThread)
			{
				try {
					Thread.sleep(espera);
					timeAcumulado+=espera;
					
					if(timeAcumulado>= tiempoMaximoThread)
					{
						puzzle9.paradaForzada=true;
						Log.d("TimeOut", "time out");
						pararThread=true;
						
					}
					
					Message msg= new Message();
					msg.arg1= (int) ((tiempoMaximoThread-timeAcumulado)/1000);
					msg.arg2=0;
					HandlerTexto.sendMessage(msg);					
					
				} 
				catch (InterruptedException e) 
				{					
					e.printStackTrace();
				}				
			}	
			Log.d("TimeOut", "Fin thread");
		}

	};
	
	@SuppressLint("HandlerLeak")
	private Handler HandlerTexto = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			if(msg.arg2==0 && !threadAcabado )
			   textoInformacion.setText("Desordenando... Tiempo maximo:" +msg.arg1+" segundos");
			else
			   updateTexto();
		}
	};
	
}
