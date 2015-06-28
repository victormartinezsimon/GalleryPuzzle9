package BBDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BBDD extends SQLiteOpenHelper
{
	

	private static String BBDD_NAME= "HighScore.db"; 
	private static int VERSION=2;
	private static String TABLE_NAME="HighScore";
	
	public static String ID="ID";
	public static String NOMBRE="Nombre";
	public static String PUNTOS="PUNTOS";
	public static String LEVEL="LEVEL";
	
	public static String FACIL="FACIL";
	public static String MEDIO="MEDIO";
	public static String DIFICIL="DIFICIL";
	
	private String sqlTabla="Create table "+ TABLE_NAME+ " ( "
			+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
			+NOMBRE+" TEXT NOT NULL,"
			+PUNTOS+" INTEGER NOT NULL,"
			+LEVEL+" TEXT NOT NULL"
			+")";

	private static BBDD m_instance=null;
	
	public static BBDD getInstance(Context context)
	{
		if(m_instance==null)
		{
			m_instance= new BBDD(context);
		}
		return m_instance;
		
	}
	
	public static BBDD getInstance()
	{
		return m_instance;
	}

	private BBDD(Context context) 
	{
		super(context, BBDD_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(sqlTabla);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
		onCreate(db);
		
	}

	public void addEntry(String name,int points, String level)
	{
		SQLiteDatabase db= this.getWritableDatabase();
		
		ContentValues cv= new ContentValues();
		cv.put(NOMBRE, name);
		cv.put(PUNTOS, points);
		cv.put(LEVEL, level);
		
		db.insert(TABLE_NAME, null, cv);
		
	}
	
	/**
	 * Realiza la busqueda
	 * @param level-> si es null, devolvera todos
	 * @param limit-> si es null, no hya limite
	 * @return
	 */
	public Cursor getFilterElements(String level, String limit)
	{
		SQLiteDatabase db= this.getReadableDatabase();
		
		Cursor c = null;
				
		//si el level es <=0, es que queremos todos
		if(level==null)
		{
			
			c=db.query(TABLE_NAME, null, null, null, null, null, PUNTOS+" desc", limit);
		}
		else
		{
			c=db.query(TABLE_NAME, null, LEVEL+"=?", new String[]{level}, LEVEL+" desc", null, PUNTOS, limit);
		}
		
		
		return c;
	}
}
