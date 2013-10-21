package com.felipew.gastrometro.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import com.felipew.gastometro.Util;
import com.felipew.gastometro.model.Despesa;
import com.felipew.gastometro.model.Tipo;

import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

/**
 * Aqui ira tudo relacionado a banco de dados
 * @author felipewagner
 *
 */
public class DbAdapter {
	// Estrutura do banco de dados
	//// TAB_TIPO_DESPESA
	public static final String TABELA_TIPO = "TAB_TIPO_DESPESA";
	public static final String TABELA_TIPO_ID = "_id";
	public static final String TABELA_TIPO_NOME = "tipo";
	
	public static final String CREATE_TABELA_TIPO = "CREATE TABLE "+TABELA_TIPO+" ("+
			TABELA_TIPO_ID	+"	INTEGER PRIMARY KEY, "+
			TABELA_TIPO_NOME+" 	TEXT NOT NULL);";
	
	//// TAB_SUB_TIPO
	public static final String TABELA_SUBTIPO = "TAB_SUB_TIPO";
	public static final String TABELA_SUBTIPO_ID = "_id";
	public static final String TABELA_SUBTIPO_IDTIPO = "_id_tipo";
	public static final String TABELA_SUBTIPO_SUBTIPO = "subtipo";
	
	public static final String CREATE_TABELA_SUBTIPO = "CREATE TABLE "+TABELA_SUBTIPO+" ("+
		TABELA_SUBTIPO_ID+"	INTEGER PRIMARY KEY, "+
		TABELA_SUBTIPO_IDTIPO+" INTEGER NOT NULL, "+
		TABELA_SUBTIPO_SUBTIPO+" TEXT NOT NULL, "+
		"FOREIGN KEY ("+TABELA_SUBTIPO_IDTIPO+") REFERENCES "+TABELA_TIPO+"("+TABELA_TIPO_ID+") );";
	
	//// TAB_PARCELAMENTO
	public static final String TABELA_PARCELAMENTO = "TAB_PARCELAMENTO";
	public static final String TABELA_PARCELAMENTO_ID = "_id";
	public static final String TABELA_PARCELAMENTO_PARCELAS = "parcelas";
	public static final String TABELA_PARCELAMENTO_TOTAL = "total";
	
	public static final String CREATE_TABELA_PARCELAMENTO = "CREATE TABLE "+TABELA_PARCELAMENTO+"( "+
		TABELA_PARCELAMENTO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
		TABELA_PARCELAMENTO_PARCELAS+" INTEGER NOT NULL, "+
		TABELA_PARCELAMENTO_TOTAL+"	INTEGER NOT NULL);";
	
	//// TAB_DESPESA
	public static final String TABELA_DESPESA = "TAB_DESPESA";
	public static final String TABELA_DESPESA_ID = "_id";
	public static final String TABELA_DESPESA_IDTIPO = "_id_tipo";
	public static final String TABELA_DESPESA_IDSUBTIPO = "_id_subtipo";
	public static final String TABELA_DESPESA_IDPARCELAMENTO = "_id_parcelamento";
	public static final String TABELA_DESPESA_DATA = "data";
	public static final String TABELA_DESPESA_DESC = "descricao";
	public static final String TABELA_DESPESA_FORMA = "forma_pagamento";
	public static final String TABELA_DESPESA_VALOR = "valor";
	
	public static final String CREATE_TABELA_DESPESA = "CREATE TABLE "+TABELA_DESPESA+" ( "+
			TABELA_DESPESA_ID+"	INTEGER PRIMARY KEY, "+
			TABELA_DESPESA_IDTIPO+"	INTEGER NOT NULL, "+
			TABELA_DESPESA_IDSUBTIPO+" INTEGER NOT NULL, "+
			TABELA_DESPESA_IDPARCELAMENTO+"	INTEGER, "+
			TABELA_DESPESA_DATA+" date NOT NULL, "+
			TABELA_DESPESA_DESC+" TEXT NOT NULL, "+
			TABELA_DESPESA_FORMA+" INTEGER NOT NULL, "+
			TABELA_DESPESA_VALOR+" INTEGER NOT NULL, "+
			"FOREIGN KEY ("+TABELA_DESPESA_IDTIPO+") REFERENCES "+TABELA_TIPO+" ("+TABELA_TIPO_ID+"),"+
			"FOREIGN KEY ("+TABELA_DESPESA_IDSUBTIPO+") REFERENCES "+TABELA_SUBTIPO+" ("+TABELA_SUBTIPO_ID+"),"+
			"FOREIGN KEY ("+TABELA_DESPESA_IDPARCELAMENTO+") REFERENCES "+TABELA_PARCELAMENTO+"("+TABELA_PARCELAMENTO_ID+") );";
	
	// FIM
	
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DB_NAME = "gastometro.db";
	private static final int DATABASE_VERSION = 1;

	private final Context mCtx;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d("Gastometro","Criando DB");
			// cria as tabelas do banco de dados
			db.execSQL(CREATE_TABELA_TIPO);
			db.execSQL(CREATE_TABELA_SUBTIPO);
			db.execSQL(CREATE_TABELA_PARCELAMENTO);
			db.execSQL(CREATE_TABELA_DESPESA);
			Log.d("Gastometro","Criou tabelas");
			
			// dados iniciais
			
			// tipo default
			db.execSQL("INSERT INTO "+TABELA_TIPO+" VALUES(null,'Alimentacao')");
			db.execSQL("INSERT INTO "+TABELA_TIPO+" VALUES(null,'Carro')");
			db.execSQL("INSERT INTO "+TABELA_TIPO+" VALUES(null,'Moradia')");
			db.execSQL("INSERT INTO "+TABELA_TIPO+" VALUES(null,'Outros')");
			
			// subtipo default
			db.execSQL("INSERT INTO "+TABELA_SUBTIPO+" VALUES(null,1,'Supermercado')"); // subtipo 1
			db.execSQL("INSERT INTO "+TABELA_SUBTIPO+" VALUES(null,2,'Gasolina')");		// subtipo 2
			db.execSQL("INSERT INTO "+TABELA_SUBTIPO+" VALUES(null,3,'Aluguel')");		// subtipo 3
			db.execSQL("INSERT INTO "+TABELA_SUBTIPO+" VALUES(null,4,'Apps')");			// subtipo 4
			db.execSQL("INSERT INTO "+TABELA_SUBTIPO+" VALUES(null,4,'Outros')");		// subtipo 5
			
			// despesa default
			db.execSQL("INSERT INTO "+TABELA_DESPESA+" VALUES(null,4,4,null,date('now'),'gastometro',0,0.99)");
			db.execSQL("INSERT INTO "+TABELA_DESPESA+" VALUES(null,4,5,null,date('now'),'outros',0,1.99)");
			
			Log.d("Gastometro","Inseriou dados");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			Log.d("Gastometro","Upgrade DB "+arg1+" >>>>> "+arg2);
			db.execSQL("DROP TABLE IF EXISTS " + TABELA_TIPO );
			db.execSQL("DROP TABLE IF EXISTS " + TABELA_SUBTIPO );
			db.execSQL("DROP TABLE IF EXISTS " + TABELA_PARCELAMENTO );
			db.execSQL("DROP TABLE IF EXISTS " + TABELA_DESPESA );
			onCreate(db);
			
		}
	}
	
	public DbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public DbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
		mDb.close();
	}
	
	// TIPO
	/**
	 * Cria uma nova categoria e retorna o ID, em caso de falha retorna -1;
	 * @param tipo String com o tipo a ser criado
	 * @return
	 */
	public long inserirTipo(String tipo) {
		ContentValues values = new ContentValues();
		values.put(TABELA_TIPO_NOME,tipo);
		
		return mDb.insert(TABELA_TIPO, null, values);
	}
	
	public long removeTipo(String tipo) {
		// TODO
		// Se alguem utiliza este tipo nao podemos remover
		// usa = Consulta TAB_DESPESA usar o metodo que sera criado mais a frente
		// usa = Consulta TAB_SUBTIPO usar o metodo que sera criado mais a frente
		// usa > 0:
		//	retorna 1;
		String args[] = new String[] { String.valueOf(retornaTipo2Id(tipo)) };
		
		if( mDb.delete(TABELA_TIPO, TABELA_TIPO_ID+"=?", args ) > 0 ){
			return 0; // OK
		} else {
			return -1;
		}
	}
	
	public ArrayList<Tipo> listaTipos(){
		// Seleciona todos os tipos
		Cursor mCursor = mDb.query(TABELA_TIPO,
				new String[] { TABELA_TIPO_ID, TABELA_TIPO_NOME }, 
				null, null, null, null, null);
		
		// Array que retorna a lista de tipos
		ArrayList<Tipo> lista = new ArrayList<Tipo>();
		if( mCursor != null ){
			mCursor.moveToFirst();
			while( !mCursor.isAfterLast() ){
				Tipo tipo = new Tipo();
				
				tipo.setId( mCursor.getLong( mCursor.getColumnIndex(TABELA_TIPO_ID) ) );
				tipo.setTipo( mCursor.getString( mCursor.getColumnIndex(TABELA_TIPO_NOME) ) );
				
				lista.add(tipo);
				
				mCursor.moveToNext();
			}
		}
		
		return lista;
	}
	
	public ArrayList<String> listaTiposString(){
		// Seleciona todos os tipos
		Cursor mCursor = mDb.query(TABELA_TIPO,
				new String[] { TABELA_TIPO_ID, TABELA_TIPO_NOME }, 
				null, null, null, null, null);
		
		// Array que retorna a lista de tipos
		ArrayList<String> lista = new ArrayList<String>();
		if( mCursor != null ){
			mCursor.moveToFirst();
			while( !mCursor.isAfterLast() ){
				String tipo = mCursor.getString( mCursor.getColumnIndex(TABELA_TIPO_NOME) );
				lista.add(tipo);
				mCursor.moveToNext();
			}
		}
		
		return lista;
	}
	
	/**
	 * Retorna o ID para um determinado tipo SE existir
	 * @param tipo
	 * @return
	 */
	public long retornaTipo2Id(String tipo){
		Cursor mCursor = mDb.query(	TABELA_TIPO, 
									new String[] { TABELA_TIPO_ID },
									TABELA_TIPO_NOME+"=?", 
									new String[] {tipo}, 
									null, null, null);
		long id = -1;
		
		if( mCursor != null ){
			mCursor.moveToFirst();
			id = mCursor.getLong( mCursor.getColumnIndex(TABELA_TIPO_ID) ); 
		}
		
		return id;
	}
	
	public String retornaId2Tipo(long id) {
		String tipo = null;
		Cursor mCursor = mDb.query(	TABELA_TIPO, 
				new String[] { TABELA_TIPO_NOME },
				TABELA_TIPO_ID+"=?", 
				new String[] {String.valueOf(id)}, 
				null, null, null);
		
		if( mCursor != null ){
			mCursor.moveToFirst();
			tipo = mCursor.getString( mCursor.getColumnIndex(TABELA_TIPO_NOME) ); 
		}
		
		return tipo;
	}
	
	// SUBTIPO
	/**
	 * Cria uma nova categoria e retorna o ID, em caso de falha retorna -1;
	 * @param subtipo String com o tipo a ser criado
	 * @return
	 */
	public long inserirSubTipo(String subtipo,String tipo) {
		ContentValues values = new ContentValues();
		values.put(TABELA_SUBTIPO_SUBTIPO,subtipo);
		values.put(TABELA_SUBTIPO_IDTIPO,retornaTipo2Id(tipo));
		
		return mDb.insert(TABELA_SUBTIPO, null, values);
	}
	
	public long removeSubTipo(String subtipo) {
		// TODO
		// Se alguem utiliza este tipo nao podemos remover
		// usa = Consulta TAB_DESPESA usar o metodo que sera criado mais a frente
		// usa > 0:
		//	retorna 1;
		String args[] = new String[] { String.valueOf(retornaSubTipo2Id(subtipo)) };
		
		if( mDb.delete(TABELA_SUBTIPO, TABELA_SUBTIPO_ID+"=?", args ) > 0 ){
			return 0; // OK
		} else {
			return -1;
		}
	}
	
	public ArrayList<String> listaSubTipoString(String tipo) {
		ArrayList<String> subtipos = new ArrayList<String>();
		long tipoId = retornaTipo2Id(tipo);
		String[] colunas = new String[] { TABELA_SUBTIPO_SUBTIPO };
		String where = TABELA_SUBTIPO_IDTIPO+"=?";
		String[] args = new String[] { tipoId+"" };
		
		Cursor mCursor = mDb.query(TABELA_SUBTIPO,
									colunas,
									where,
									args,
									null,null,null);
		
		// Array que retorna a lista de tipos
		if( mCursor != null ){
			mCursor.moveToFirst();
			while( !mCursor.isAfterLast() ){
				String subtipo = mCursor.getString( mCursor.getColumnIndex(TABELA_SUBTIPO_SUBTIPO) );
				subtipos.add(subtipo);
				mCursor.moveToNext();
			}
		}
		
		return subtipos;
	}
	
	/**
	 * Retorna o ID para um determinado subTipo SE existir
	 * @param tipo
	 * @return
	 */
	public long retornaSubTipo2Id(String subtipo){
		Cursor mCursor = mDb.query(	TABELA_SUBTIPO, 
									new String[] { TABELA_SUBTIPO_ID },
									TABELA_SUBTIPO_SUBTIPO+"=?", 
									new String[] {subtipo}, 
									null, null, null);
		long id = -1;
		
		if( mCursor != null ){
			mCursor.moveToFirst();
			id = mCursor.getLong( mCursor.getColumnIndex(TABELA_SUBTIPO_ID) ); 
		}
		
		return id;
	}
	
	public String retornaId2SubTipo(long id) {
		String subtipo = null;
		Cursor mCursor = mDb.query(	TABELA_SUBTIPO, 
				new String[] { TABELA_SUBTIPO_SUBTIPO },
				TABELA_SUBTIPO_ID+"=?", 
				new String[] {String.valueOf(id)}, 
				null, null, null);
		
		if( mCursor != null ){
			mCursor.moveToFirst();
			subtipo = mCursor.getString( mCursor.getColumnIndex(TABELA_SUBTIPO_SUBTIPO) ); 
		}
		
		return subtipo;
	}
	
	// PARCELAS
	/**
	 * Cria nova compra parcelada e retorna o id dela 
	 * @param nparcelas
	 * @param valorTotal
	 * @return
	 */
	public long insereNovaParecla(int nparcelas,float valorTotal) {
		long id = -1;
		ContentValues values = new ContentValues();
		values.put(TABELA_PARCELAMENTO_PARCELAS, nparcelas);
		values.put(TABELA_PARCELAMENTO_TOTAL, valorTotal);
		id = mDb.insert(TABELA_PARCELAMENTO, null, values);
		return id;
	}
	
	// DESPESA
	/**
	 * 
	 * @param tipo - id do tipo
	 * @param subtipo - id do subtipo 
	 * @param desc - descricao
	 * @param valor - valor da despesa
	 * @return id inserido
	 */
	public long insereNovaDespesa(long tipo,long subtipo,String desc,float valor) {
		ContentValues values = new ContentValues();
		values.put(TABELA_DESPESA_IDTIPO,tipo);
		values.put(TABELA_DESPESA_IDSUBTIPO,subtipo);
		values.put(TABELA_DESPESA_DESC, desc);
		values.put(TABELA_DESPESA_VALOR, valor);
		values.put(TABELA_DESPESA_DATA, Util.recuperaData(0) ); // queremos a data atual
		values.put(TABELA_DESPESA_FORMA,0); // Trocar por define
		
		// Insere a despesa...
		long id = mDb.insert(TABELA_DESPESA, null, values);
		
		return id;
	}
	
	/**
	 * 
	 * @param tipo
	 * @param subtipo
	 * @param desc
	 * @param valor Valor TOTAL da compra
	 * @param nparcelas
	 * @return
	 */
	public long insereNovaDespesaParcelada(long tipo, long subtipo, String desc, float valorTotal, int nparcelas) {
		long id = -1;
		// Antes de inserir os dados na tabela de despesa devemos recuperar o ID da compra parcelada
		long idParcela = insereNovaParecla(nparcelas,valorTotal);
		//////////////////////////////////////////////////////////////////////////////////		
		float valorParcela = valorTotal / nparcelas;
		ContentValues values = new ContentValues();
		values.put(TABELA_DESPESA_IDTIPO		, tipo);			// Valor fixos para todas as parcels
		values.put(TABELA_DESPESA_IDSUBTIPO		, subtipo);			// Valor fixos para todas as parcels
		values.put(TABELA_DESPESA_VALOR			, valorParcela);	// Valor fixos para todas as parcels
		values.put(TABELA_DESPESA_FORMA			, 0); 				// Valor fixos para todas as parcels
		values.put(TABELA_DESPESA_IDPARCELAMENTO, idParcela);		// Valor fixos para todas as parcels
		
		for(int i = 0 ; i < nparcelas ; i++ ) {
			// Limpa o values
			if(values.containsKey(TABELA_DESPESA_DATA)){
				values.remove(TABELA_DESPESA_DATA);
			}
			if(values.containsKey(TABELA_DESPESA_DESC)){
				values.remove(TABELA_DESPESA_DESC);
			}
			
			String strParcela = " "+(i+1)+"/"+nparcelas;
			values.put(TABELA_DESPESA_DATA		, Util.recuperaData(i) ); // Data
			values.put(TABELA_DESPESA_DESC		, desc+strParcela);
			id = mDb.insert(TABELA_DESPESA, null, values);
			if( id < 0){
				break;
			}
			// removeParcela
		}
		
		return id;
	}
	
	/**
	 * Remove a despesa com o id especificado
	 * @param id
	 */
	public int removeDespesa(long id,long idparcela){
		String where = "";
		String[] args;
		
		if( idparcela == 0 ){ // se nao eh parcela deleta normal
			where = TABELA_DESPESA_ID+"=?";
			args = new String[] { String.valueOf(id) };
		} else { // se eh parcela deleta todas as parcelas
			where = TABELA_DESPESA_IDPARCELAMENTO+"=?";
			args = new String[] { String.valueOf(idparcela) };
			// TODO: deletar da tab parcelamento
		}
		
		mDb.delete(TABELA_DESPESA, where, args);
		
		return 1;
	}
	
	public float somaDespesasMes(){
		float total = 0;
		Cursor mCursor = mDb.rawQuery("SELECT sum(valor) FROM "+TABELA_DESPESA+" WHERE "+TABELA_DESPESA_DATA+" BETWEEN date('now','start of month') AND date('now','start of month','+1 month','-1 day')",null);
		if( mCursor != null ){
			mCursor.moveToFirst();
			while( !mCursor.isAfterLast() ){
				total = mCursor.getFloat(0); 
				Log.d("Gastometro","Valor: "+mCursor.getFloat(0));
				mCursor.moveToNext();
			}
		}
		return total;
	}
	
	/**
	 * Retorna array com as despesas filtro padrao DIA,SEMANA, MES
	 * 
	 * @param filtro
	 * @return
	 */
	public ArrayList<Despesa> listaDespesasFiltroSimples(int filtro) {
		ArrayList<Despesa> lista = new ArrayList<Despesa>();
		String where;
		switch(filtro) {
			case Util.FILTRO_DIA:
				where = "data = date('now')";
				break;
			case Util.FILTRO_SEM:
				where = "data BETWEEN date('now','-7 days') AND date('now')";
				break;
			case Util.FILTRO_MES: // Default eh sempre filtrar por MES
			default:
				where = "data BETWEEN date('now','start of month') AND date('now','start of month','+1 month','-1 day')";
				break;
				
		}
		
		lista = consultaDespesaGenerica(where);
		return lista;
	}
	
	public ArrayList<Despesa> listaDespesasFiltroTipos(String tipo,String subtipo) {
		ArrayList<Despesa> lista = new ArrayList<Despesa>();
		String where = "data BETWEEN date('now','start of month') AND date('now','start of month','+1 month','-1 day')";
		String aux = "";
		long idTipo,idSubtipo = -1;
		
		
		idTipo = retornaTipo2Id(tipo);
		aux = TABELA_DESPESA_IDTIPO+" = "+idTipo+" AND ";
		if( subtipo != null) {
			idSubtipo = retornaSubTipo2Id(subtipo);
			aux = TABELA_DESPESA_IDSUBTIPO+" = "+idSubtipo+" AND ";
		}
		
		where = aux + where;
		
		lista = consultaDespesaGenerica(where);
		
		return lista;
	}
	
	public ArrayList<Despesa> consultaDespesaGenerica(String where) {
		ArrayList<Despesa> lista = new ArrayList<Despesa>();
		// Fazemos a consulta reversa, para o dado mais novo ficar no topo
		String query = "SELECT * FROM "+TABELA_DESPESA+" WHERE "+where+" ORDER BY "+TABELA_DESPESA_DATA+" DESC ,"+TABELA_DESPESA_ID+" DESC";
		Cursor mCursor = mDb.rawQuery(query, null);
		// Trabalhar neste cursor aqui rapa...
		if( mCursor != null ) {
			mCursor.moveToFirst();
			while( !mCursor.isAfterLast() ) {
				
				long id = mCursor.getLong( mCursor.getColumnIndex(TABELA_DESPESA_ID));
				long idparcela = mCursor.getLong( mCursor.getColumnIndex(TABELA_DESPESA_IDPARCELAMENTO));
				String tipo = retornaId2Tipo( mCursor.getLong( mCursor.getColumnIndex(TABELA_DESPESA_IDTIPO)) );
				String subtipo = retornaId2SubTipo( mCursor.getLong( mCursor.getColumnIndex(TABELA_DESPESA_IDSUBTIPO)) );
				String desc = mCursor.getString( mCursor.getColumnIndex(TABELA_DESPESA_DESC));
				float valor = mCursor.getFloat( mCursor.getColumnIndex(TABELA_DESPESA_VALOR));
				String data = mCursor.getString( mCursor.getColumnIndex(TABELA_DESPESA_DATA));
				
				Despesa d = new Despesa(id, idparcela, tipo, subtipo, desc, valor, data);
				
				lista.add(d);
				
				mCursor.moveToNext();
			}
		}
		return lista;
	}
	
	public Despesa leDespesa(long idBD) {
		Despesa d = null;
		String where = TABELA_DESPESA_ID+"=?";
		String[] args = new String[] { String.valueOf(idBD) };
		Cursor mCursor = mDb.query(TABELA_DESPESA, null, where, args, null, null, null);
		if( mCursor != null ){
			mCursor.moveToFirst();
			long id = mCursor.getLong( mCursor.getColumnIndex(TABELA_DESPESA_ID));
			long idparcela = mCursor.getLong( mCursor.getColumnIndex(TABELA_DESPESA_IDPARCELAMENTO));
			String tipo = retornaId2Tipo( mCursor.getLong( mCursor.getColumnIndex(TABELA_DESPESA_IDTIPO)) );
			String subtipo = retornaId2SubTipo( mCursor.getLong( mCursor.getColumnIndex(TABELA_DESPESA_IDSUBTIPO)) );
			String desc = mCursor.getString( mCursor.getColumnIndex(TABELA_DESPESA_DESC));
			float valor = mCursor.getFloat( mCursor.getColumnIndex(TABELA_DESPESA_VALOR));
			String data = mCursor.getString( mCursor.getColumnIndex(TABELA_DESPESA_DATA));
			
			d = new Despesa(id, idparcela, tipo, subtipo, desc, valor, data);
		}
		
		return d;
	}
	
	public void alteraDespesa(long id,String tipo,String subtipo,String desc,String valor) {
		ContentValues values = new ContentValues();
		values.put(TABELA_DESPESA_IDTIPO, retornaTipo2Id(tipo));
		values.put(TABELA_DESPESA_IDSUBTIPO, retornaSubTipo2Id(subtipo));
		values.put(TABELA_DESPESA_DESC, desc);
		values.put(TABELA_DESPESA_VALOR, valor);
		
		mDb.update(TABELA_DESPESA, values, TABELA_PARCELAMENTO_ID+"=?", new String[] { String.valueOf(id) });
	}
}
