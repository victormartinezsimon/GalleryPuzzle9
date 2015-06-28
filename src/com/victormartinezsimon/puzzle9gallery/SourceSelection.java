package com.victormartinezsimon.puzzle9gallery;

import java.io.ByteArrayOutputStream;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.provider.MediaStore;

public class SourceSelection extends ActionBarActivity implements OnClickListener
{

	public enum Dificultad{FACIL,MEDIO,DIFICIL};
	
	int SELECT_GALLERY=1;
	int SELECT_PHOTO=2;
	
	public static String PATH="patH";
	public static String ANCHO="ANCHO";
	public static final String MULTIPLICADOR="MULTIPLICADOR";
	public static final String BITMAPSRC="BITMAPSRC";
	
	private float multiplicadorMovimientos;
	private int ancho;
	
	Button botonGaleria;
	Button botonFoto;
	
	private String level;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_source_selection);
		Bundle bundle = getIntent().getExtras();
		multiplicadorMovimientos= bundle.getFloat(MULTIPLICADOR);
		ancho=bundle.getInt(ANCHO);
		level=bundle.getString(Game.LEVEL);
		
		botonGaleria= (Button) this.findViewById(R.id.botonSourceGaleria);
		botonFoto= (Button) this.findViewById(R.id.botonSourceCamara);
		
		botonGaleria.setOnClickListener(this);
		botonFoto.setOnClickListener(this);
	}

	
	private void lanzarGallery() 
	{
			
		Intent intent = new Intent(
				Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_GALLERY);		
		
	}
	
	private void lanzarCamara() 
	{
			
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) 
	    {
	        startActivityForResult(takePictureIntent, SELECT_PHOTO);
	    }
		
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	   {
	        if (resultCode == RESULT_OK) 
	        {
	            if (requestCode == SELECT_GALLERY) 
	            {
	                Uri selectedImageUri = data.getData();
	                String selectedImagePath = getPath(selectedImageUri);
	                               
	                Intent intencion= new Intent(this, Game.class);
	                intencion.putExtra(PATH, selectedImagePath);
	                intencion.putExtra(ANCHO, ancho);
	                intencion.putExtra(MULTIPLICADOR, multiplicadorMovimientos);
	                intencion.putExtra(Game.LEVEL, level);
	                this.startActivity(intencion);
	                
	            }
	            if(requestCode==SELECT_PHOTO)
	            {
	            	Bundle extras = data.getExtras();
	                Bitmap imageBitmap = (Bitmap) extras.get("data");
	                
	                Intent intencion= new Intent(this, Game.class);

	                ByteArrayOutputStream stream = new ByteArrayOutputStream();
	                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	                byte[] byteArray = stream.toByteArray();
	                intencion.putExtra(BITMAPSRC, byteArray);
	                
	                intencion.putExtra(ANCHO, ancho);
	                intencion.putExtra(MULTIPLICADOR, multiplicadorMovimientos);
	                intencion.putExtra(Game.LEVEL, level);
	                this.startActivity(intencion);
	            }
	            this.finish();
	        }
	   }
		   
	   /**
	* helper to retrieve the path of an image URI
	*/
	public String getPath(Uri uri) 
   {
           // just some safety built in 
           if( uri == null ) {
              return null;
           }
           // try to retrieve the image from the media store first
           // this will only work for images selected from gallery
           String[] projection = { MediaStore.Images.Media.DATA };
           @SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
           if( cursor != null ){
               int column_index = cursor
               .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
               cursor.moveToFirst();
               return cursor.getString(column_index);
           }
           // this is our fallback here
           return uri.getPath();
   }


	@Override
	public void onClick(View v) 
	{
		if(v.getId()==R.id.botonSourceCamara)
		{
			lanzarCamara();
		}
		if(v.getId()==R.id.botonSourceGaleria)
		{
			lanzarGallery();
		}
		
		
	}
   
	
	
}
