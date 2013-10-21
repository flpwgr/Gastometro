package com.felipew.gastometro;

import java.util.ArrayList;

import com.felipew.gastometro.model.Despesa;
import com.felipew.gastrometro.dao.DbAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class EditDespesa extends Activity {
	DbAdapter db;
	Despesa despesa;
	ArrayList<String> tipos;
	ArrayList<String> subtipos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_despesa);
		long id = getIntent().getLongExtra("id", 0);
		if( id < 0 ) {
			Toast.makeText(getApplicationContext(), "Falha ao editar item", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			db = new DbAdapter(getApplicationContext());
			db.open();
			despesa = db.leDespesa(id);
			populaSpinner();
			
			Spinner spinner = (Spinner) findViewById(R.id.spinTipoEdit);
			int indexTipo = tipos.indexOf( despesa.getTipo() );
			spinner.setSelection(indexTipo);
			
			populaSpinnerSubtipo();
			
			int indexSubTipo = subtipos.indexOf( despesa.getSubtipo() );
			Spinner spinnerSubtipo = (Spinner) findViewById(R.id.spinSubTipoEdit);
			spinnerSubtipo.setSelection(indexSubTipo);
			
			EditText etDesc = (EditText) findViewById(R.id.txtDescricaoEdit);
			EditText etValor = (EditText) findViewById(R.id.txtValorEdit);
			
			etDesc.setText(despesa.getDescricao());
			etValor.setText(String.valueOf(despesa.getValor()));
			
			Toast.makeText(getApplicationContext(), "Id Parcela "+despesa.getIdParcela(), Toast.LENGTH_SHORT).show();
			if( despesa.getIdParcela() >= 1 ) {
				etValor.setEnabled(false);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_despesa, menu);
		return true;
	}
	
	public void populaSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.spinTipoEdit);
		
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
		Spinner spinner = (Spinner) findViewById(R.id.spinSubTipoEdit);
		Spinner spinnerTipo = (Spinner) findViewById(R.id.spinTipoEdit);
		
		int index = spinnerTipo.getSelectedItemPosition();
		String tipo = tipos.get(index);
		
		subtipos = db.listaSubTipoString(tipo);
		ArrayAdapter<String> adp = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,subtipos);
        spinner.setAdapter(adp);
	}
	
	public void updateDespesa(View v) {
		Spinner spinnerSubTipo = (Spinner) findViewById(R.id.spinSubTipoEdit);
		Spinner spinnerTipo = (Spinner) findViewById(R.id.spinTipoEdit);
		EditText etDesc = (EditText) findViewById(R.id.txtDescricaoEdit);
		EditText etValor = (EditText) findViewById(R.id.txtValorEdit); // se for parcela este valor nunca altera
		
		String tipo = tipos.get(spinnerTipo.getSelectedItemPosition());
		String subtipo = subtipos.get(spinnerSubTipo.getSelectedItemPosition());
		String desc = etDesc.getText().toString();
		String valor = etValor.getText().toString();
		
		db.alteraDespesa(despesa.getId(),tipo,subtipo,desc,valor);
		setResult(RESULT_OK);
		finish();
	}
}
