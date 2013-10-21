package com.felipew.gastometro;

import java.util.ArrayList;

import com.felipew.gastrometro.dao.DbAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class Settings extends Activity {
	
	DbAdapter db;
	ArrayList<String> tipos;
	ArrayList<String> subtipos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		inicializa();
		populaSpinner();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	public void inicializa() {
		db = new DbAdapter(getApplicationContext());
		db.open();
		
		tipos = new ArrayList<String>();
		subtipos = new ArrayList<String>();
	}
	

	public void populaSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.spintipo_Settings);
		tipos.clear();
		tipos = db.listaTiposString();
		
		ArrayAdapter<String> adp = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,tipos);
        spinner.setAdapter(adp);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,	int index, long arg3) {
				populaSpinnerSubtipo();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				// nao faz nada...
			}
		});
//        adp.notifyDataSetChanged();
	}
	
	public void populaSpinnerSubtipo() {
		Spinner spinner = (Spinner) findViewById(R.id.spinsubtipo_Settings);
		Spinner spinnerTipo = (Spinner) findViewById(R.id.spintipo_Settings);
		
		int index = spinnerTipo.getSelectedItemPosition();
		String tipo = tipos.get(index);
		
		subtipos.clear();
		subtipos = db.listaSubTipoString(tipo);
		ArrayAdapter<String> adp = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,subtipos);
        spinner.setAdapter(adp);
//        adp.notifyDataSetChanged();
	}
	
	// Funcoes para tratar as acoes nos botoes
	public void btnInserirTipo(View v) {
		EditText et = (EditText) findViewById(R.id.txtInserirTipo);
		String tipo = et.getText().toString();
		if( tipo.isEmpty() ) {
			Toast.makeText(getApplicationContext(), "Tipo nao pode ser vazio!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if( tipos.contains(tipo) ) {
			Toast.makeText(getApplicationContext(), "Tipo ja existe", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Se chegou aqui tudo OK podemos inserir
		Spinner sTipo = (Spinner) findViewById(R.id.spintipo_Settings);
		if( db.inserirTipo(tipo) > 0 ) {
			populaSpinner();
			sTipo.setSelection( tipos.size()-1 );
		}
		et.setText("");
	}
	
	public void btnInserirSubTipo(View v){
		EditText et = (EditText) findViewById(R.id.txtInserirSubTipo);
		String subtipo = et.getText().toString();
		if( subtipo.isEmpty() ) {
			Toast.makeText(getApplicationContext(), "SubTipo nao pode ser vazio!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if( subtipos.contains(subtipo) ) {
			Toast.makeText(getApplicationContext(), "SubTipo ja existe", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Spinner sTipo = (Spinner) findViewById(R.id.spintipo_Settings);
		Spinner sSubTipo = (Spinner) findViewById(R.id.spinsubtipo_Settings);
		String tipo = tipos.get( sTipo.getSelectedItemPosition() );
		// Se chegou aqui tudo OK podemos inserir
		if( db.inserirSubTipo(subtipo,tipo) > 0 ) {
			populaSpinnerSubtipo();
			sSubTipo.setSelection( subtipos.size()-1 );
		}
		et.setText("");
	}
	
	public void deletarTipo(View v){
		Spinner sTipo = (Spinner) findViewById(R.id.spintipo_Settings);
		int pos = sTipo.getSelectedItemPosition();
		if( pos >= 0 ) {
			String tipo = tipos.get(pos);
			db.removeTipo(tipo);
			populaSpinner();
		} else {
			Toast.makeText(getApplicationContext(), "Nada para deletar", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void deletarSubTipo(View v){
		Spinner sSubTipo = (Spinner) findViewById(R.id.spinsubtipo_Settings);
		int pos = sSubTipo.getSelectedItemPosition();
		if( pos >= 0 ) {
			String subtipo = subtipos.get(pos);
			db.removeSubTipo(subtipo);
			populaSpinnerSubtipo();
		} else {
			Toast.makeText(getApplicationContext(), "Nada para deletar", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void fecharJanela(View v){
		Intent in = new Intent();
		setResult(RESULT_OK,in);
		finish(); // Cai fora :)
	}
	

}
