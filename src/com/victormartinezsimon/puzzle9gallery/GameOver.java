package com.victormartinezsimon.puzzle9gallery;

import java.util.ArrayList;
import java.util.List;


import BBDD.BBDD;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class GameOver extends ActionBarActivity implements OnClickListener
{

	float puntosAhora;
	boolean gameOver;
	
	TextView textoMensaje;
	TextView puntuacion;
	
	Button botonGuardar;
	
	EditText textoNombre;
	
	String level;
	
	//private Cursor elementosBBDD;
	List<nodoAdapter> elementosBBDD;
	
	ListView listaResultados;
	MyAdapter myAdapter;
	
	
	boolean fromMenu;
	
	LinearLayout ly;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		Bundle bundle = getIntent().getExtras();
		
		gameOver=bundle.getBoolean(Game.GAMEOVER);
		puntosAhora=bundle.getFloat(Game.PUNTOS);
		fromMenu= bundle.getBoolean(Game.FROMMENU);
		level=bundle.getString(Game.LEVEL);
		
		textoMensaje= (TextView) this.findViewById(R.id.MensajeGameOver);
		if(gameOver)
		{
			textoMensaje.setText("Game Over");
		}
		else
		{
			textoMensaje.setText("Enhorabuena!!");
		}
		
		puntuacion= (TextView) this.findViewById(R.id.puntosGameOver);
		puntuacion.setText(puntosAhora+" puntos");
		
		
		botonGuardar= (Button) this.findViewById(R.id.botonGuardarGameOver);
		botonGuardar.setOnClickListener(this);
		
	
		textoNombre= (EditText) this.findViewById(R.id.edicionTextoGameOver);
		
		construyeLista(BBDD.getInstance(
				this.getApplicationContext()).getFilterElements(null, null));
		
		listaResultados= (ListView) this.findViewById(R.id.listaResultadosGameOver);
		
		myAdapter=new MyAdapter(getApplicationContext());
		
		listaResultados.setAdapter(myAdapter);
		myAdapter.notifyDataSetChanged();
		
		ly= (LinearLayout) this.findViewById(R.id.linearLayoutGameOver);
		if(fromMenu)
		{
			textoMensaje.setText("HighScores");
			
			ly.setVisibility(View.GONE);
			
		}
	}

		
	
	private void construyeLista(Cursor filterElements) 
	{
		elementosBBDD=new ArrayList<GameOver.nodoAdapter>();
		
		
		while(filterElements.moveToNext())
		{
			
			nodoAdapter meter= new nodoAdapter();
			
			meter.id= filterElements.getInt(filterElements.getColumnIndex(BBDD.ID));
			meter.nombre= filterElements.getString(filterElements.getColumnIndex(BBDD.NOMBRE));
			meter.puntos= filterElements.getInt(filterElements.getColumnIndex(BBDD.PUNTOS));
			meter.level= filterElements.getString(filterElements.getColumnIndex(BBDD.LEVEL));
			
			elementosBBDD.add(meter);
			
		}

	}



	@Override
	public void onClick(View arg0) 
	{
		
		BBDD.getInstance(this.getApplicationContext()).addEntry(textoNombre.getText().toString(), (int)puntosAhora, level);
		botonGuardar.setEnabled(false);
		construyeLista(BBDD.getInstance(
				this.getApplicationContext()).getFilterElements(null, null));
		myAdapter.notifyDataSetChanged();
		ly.setVisibility(View.GONE);
		
	}

	
	class nodoAdapter
	{
		int id;
		String nombre;
		int puntos;
		String level;
		
	}
	
	class MyAdapter extends BaseAdapter
	{

		private LayoutInflater inflater = null;
		
		public MyAdapter(Context c)
		{
			inflater= LayoutInflater.from(c);
		}
		
		@Override
		public int getCount() 
		{
			return elementosBBDD.size();
		}

		@Override
		public Object getItem(int i) 
		{
			return elementosBBDD.get(i);
		}

		@Override
		public long getItemId(int arg0) 
		{
			return 0;//todos son iguales
		}

		
		class ViewHolder
		{
			TextView textoNombre;
			TextView textoPuntos;
			TextView textoNivel;
		}
		
		@Override
		public View getView(int i, View convertView, ViewGroup parent) 
		{
			ViewHolder holder;
			
			View view;
			
			if(convertView==null)
			{
				holder= new ViewHolder();
				
				view=inflater.inflate(R.layout.adapter_game_over, null);
				
				holder.textoNivel= (TextView) view.findViewById(R.id.textoDerecha);				
				holder.textoNombre= (TextView) view.findViewById(R.id.textoIzquierda);
				holder.textoPuntos=(TextView) view.findViewById(R.id.textoCentral);
				
				view.setTag(holder);
			}
			else
			{
				holder= (ViewHolder) convertView.getTag();
				view=convertView;
			}
			
			
			nodoAdapter na= (nodoAdapter) getItem(i);
			
			holder.textoNivel.setText("Nivel: "+na.level);
			holder.textoNombre.setText(na.nombre);
			holder.textoPuntos.setText(na.puntos+" puntos");
			
			return view;
		}
		
	}
	
}
