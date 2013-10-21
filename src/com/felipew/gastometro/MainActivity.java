package com.felipew.gastometro;

import com.felipew.gastrometro.dao.DbAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private DbAdapter db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		carregaDados();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch( item.getItemId() ) {
			case R.id.action_add:
				callNovaDespesa();
				break;
			case R.id.action_relatorio:
				callRelatorio(); //
				break;
			case R.id.action_settings:
				callSettings();
				break;
				
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	public void callNovaDespesa() {
		// Chama Intent de cadastro de despesa
		Intent in = new Intent(getApplicationContext(),NovaDespesa.class);
		startActivityForResult(in,Util.ACTIVITY_NOVADESPESA);
	}
	
	
	public void callRelatorio() {
		Intent in = new Intent(getApplicationContext(),Relatorio.class);
		startActivityForResult(in, Util.ACTIVITY_RELATORIO);
	}
	
	public void callSettings() {
		Intent in = new Intent(getApplicationContext(),Settings.class);
		startActivityForResult(in, Util.ACTIVITY_SETTINGS);
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( requestCode == Util.ACTIVITY_NOVADESPESA && resultCode == RESULT_OK ){
			Toast.makeText(getApplicationContext(), "Despesa inserida com sucesso!", Toast.LENGTH_SHORT).show();
		}
		
		atualizaDados();
	}
	
	public void carregaDados() {
		db = new DbAdapter(getApplicationContext());
		db.open();
		
		atualizaDados();
	}
	
	public void atualizaDados() {
		float total = db.somaDespesasMes();
		TextView txt = (TextView) findViewById(R.id.txtTotal);
		txt.setText("R$ "+total);
	}
}