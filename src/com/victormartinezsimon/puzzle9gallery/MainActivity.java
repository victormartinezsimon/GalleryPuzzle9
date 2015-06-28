package com.victormartinezsimon.puzzle9gallery;


import BBDD.BBDD;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements OnClickListener
{	
	Button botonFacil;
	Button botonMedio;
	Button botonDificil;
	
	Button botonHighScore;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        botonFacil= (Button) this.findViewById(R.id.BotonFacil);
        botonFacil.setOnClickListener(this);
        
        botonMedio= (Button) this.findViewById(R.id.BotonMedio);
        botonMedio.setOnClickListener(this);
        
        botonDificil= (Button) this.findViewById(R.id.BotonDificil);
        botonDificil.setOnClickListener(this);
        
        botonHighScore= (Button) this.findViewById(R.id.BotonHighScore);
        botonHighScore.setOnClickListener(this);
      
    }

	@Override
	public void onClick(View v) 
	{
		Intent intencion= new Intent(this, SourceSelection.class);	
		if(v.getId()==R.id.BotonFacil)
		{
			intencion.putExtra(SourceSelection.MULTIPLICADOR, 2f);
			intencion.putExtra(SourceSelection.ANCHO, 3);
			intencion.putExtra(Game.LEVEL, BBDD.FACIL);
		}
		if(v.getId()==R.id.BotonMedio)
		{
			intencion.putExtra(SourceSelection.MULTIPLICADOR, 1.5f);
			intencion.putExtra(SourceSelection.ANCHO, 4);
			intencion.putExtra(Game.LEVEL, BBDD.MEDIO);
		}
		
		if(v.getId()==R.id.BotonDificil)
		{
			intencion.putExtra(SourceSelection.MULTIPLICADOR, 1f);
			intencion.putExtra(SourceSelection.ANCHO, 5);
			intencion.putExtra(Game.LEVEL, BBDD.DIFICIL);
			
		}
		
		if(v.getId()==R.id.BotonHighScore)
		{
			Intent intent= new Intent(this, GameOver.class);	
			intent.putExtra(Game.GAMEOVER, true);
			intent.putExtra(Game.PUNTOS, 0);			
			intent.putExtra(Game.FROMMENU, true);
			intent.putExtra(Game.LEVEL, BBDD.FACIL);
			this.startActivity(intent);
			return;
		}
		
		this.startActivity(intencion);
	}



	
}
