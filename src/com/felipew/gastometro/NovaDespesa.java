package com.felipew.gastometro;

import java.util.ArrayList;

import com.felipew.gastometro.model.Tipo;
import com.felipew.gastrometro.dao.DbAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NovaDespesa extends Activity {
	DbAdapter db;
	ArrayList<String> tipos;
	ArrayList<String> subtipos;
	CheckBox chkParcela;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nova_despesa);
		inicializaDb();
		populaSpinner();
		addListenerChk();
		EditText txtVezes = (EditText) findViewById(R.id.txtVezes);
		txtVezes.setEnabled(false);
		txtVezes.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nova_despesa, menu);
		return true;
	}
	
	
	public void inicializaDb() {
		db = new DbAdapter(getApplicationContext());
		db.open();
	}

	public void populaSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.spinTipo);
		
		tipos = db.listaTiposString();
		
		ArrayAdapter<String> adp = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,tipos);
        spinner.setAdapter(adp);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int index, long arg3) {
				populaSpinnerSubtipo();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				// nao faz nada...
			}
        	
		});
	}
	
	public void populaSpinnerSubtipo() {
		Spinner spinner = (Spinner) findViewById(R.id.spinSubTipo);
		Spinner spinnerTipo = (Spinner) findViewById(R.id.spinTipo);
		
		int index = spinnerTipo.getSelectedItemPosition();
		String tipo = tipos.get(index);
		
		subtipos = db.listaSubTipoString(tipo);
		ArrayAdapter<String> adp = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,subtipos);
        spinner.setAdapter(adp);
	}
	
	/**
	 * Valida os dados e salva no BD
	 * @param v
	 */
	public void salvaDespesa(View v) {
		// Recupera os botoes
		Spinner spinnerTipo = (Spinner) findViewById(R.id.spinTipo);
		Spinner spinnerSubtipo = (Spinner) findViewById(R.id.spinSubTipo);
		EditText txtDesc = (EditText) findViewById(R.id.txtDescricao);
		EditText txtValor = (EditText) findViewById(R.id.txtValor);
		EditText txtParcela = (EditText) findViewById(R.id.txtVezes);
		CheckBox chkParcela = (CheckBox) findViewById(R.id.chkParcela);
		
		// Recupera os valores dos campos
		String tipo = tipos.get((int) spinnerTipo.getSelectedItemId());
		String subtipo = subtipos.get((int) spinnerSubtipo.getSelectedItemId());
		
		
		// Valores para inserir no banco
		long idDesp = 0;
		String desc = txtDesc.getText().toString();
		float valor = Float.parseFloat(txtValor.getText().toString());
		long idTipo	= db.retornaTipo2Id(tipo);
		long idSubtipo = db.retornaSubTipo2Id(subtipo);
		
		if( chkParcela.isChecked() ){
			String nParcela = txtParcela.getText().toString();
			if( nParcela.isEmpty() ){
				Toast.makeText(getApplicationContext(), "Favor inserir a quantidade de parcelas", Toast.LENGTH_SHORT).show();
				return;
			}
			
			idDesp = db.insereNovaDespesaParcelada(idTipo, idSubtipo, desc, valor, Integer.parseInt(nParcela));
		} else {
			idDesp = db.insereNovaDespesa(idTipo, idSubtipo, desc, valor);
		}
		
		if( idDesp < 1 ) {
			Toast.makeText(getApplicationContext(), "Falha ao inserir despesa", Toast.LENGTH_SHORT).show();
		} else {
			Intent in = new Intent();
			setResult(RESULT_OK,in);
			finish(); // Cai fora :)
		}
	}
	
	// Controles de Comportamento da Janela /////////////////////////////////////////////////////////////////
	public void addListenerChk() {
		chkParcela = (CheckBox) findViewById(R.id.chkParcela);
		chkParcela.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText txtVezes = (EditText) findViewById(R.id.txtVezes);
				boolean checked = chkParcela.isChecked();
				txtVezes.setEnabled( checked );
				
				if( checked ) {
					txtVezes.setVisibility(View.VISIBLE);
				} else { 
					txtVezes.setVisibility(View.INVISIBLE);
				}
			}
		});
	}
}
