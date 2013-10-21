package com.felipew.gastometro;

import java.util.ArrayList;

import com.felipew.gastometro.model.Despesa;
import com.felipew.gastrometro.dao.DbAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Relatorio extends Activity {

	ArrayList<Despesa> lista;
	DbAdapter db;
	RelAdatper adapter;
	Despesa deletaDespesa;
	int filtroAtual = Util.FILTRO_MES;
	String tipoAtual = null;
	String subtipoAtual = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_relatorio);
		
		// Inicializa dados importantes
		lista = new ArrayList<Despesa>(); // Inicializa no onCreate
		db = new DbAdapter(getApplicationContext());
		db.open();
		
		ListView lv = (ListView) findViewById(R.id.listaRelatorio);
		if( lv != null ){
			inicializaAdapter(lv);
			lista = db.listaDespesasFiltroSimples(filtroAtual);
			atualizaTotal();
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.relatorio, menu);
		return true;
	}

	/**
	 * atualiza a listView cotnendo o relatorio baseado no tipo de filtro.
	 */
	public void atualizaRelatorio(){
		lista.clear();
		if( filtroAtual == Util.FILTRO_TIPO) {
			lista = db.listaDespesasFiltroTipos(tipoAtual,subtipoAtual);
		} else {
			lista = db.listaDespesasFiltroSimples(filtroAtual);
		}
		atualizaTotal();
		adapter.notifyDataSetChanged();
	}
	
	public void filtroDia(View v) {
		filtroAtual = Util.FILTRO_DIA;
		atualizaRelatorio();
	}
	public void filtroSem(View v) {
		filtroAtual = Util.FILTRO_SEM;
		atualizaRelatorio();
	}
	public void filtroMes(View v) {
		filtroAtual = Util.FILTRO_MES;
		atualizaRelatorio();
	}
	
	public void abreTelaFiltro(View v) {
		ArrayList<String> tipos = db.listaTiposString();
		SpinnerDialog mSpinnerDialog = new SpinnerDialog(this, tipos, new SpinnerDialog.DialogListener() {
			@Override
			public void ready(String tipo,String subtipo) {
				// TODO Auto-generated method stub
				String saida = "";
				if( subtipo != null ) {
					saida = subtipo;
				}
				filtroAtual	= Util.FILTRO_TIPO;
				tipoAtual = tipo;
				subtipoAtual = subtipo;
				
				atualizaRelatorio();
			}
			
			@Override
			public void cancelled() {
				// TODO Auto-generated method stub
				// Nao faz nada..
			}
		});
		mSpinnerDialog.setTitle("Selecione um filtro");
		mSpinnerDialog.show();
	}
	
	public void atualizaTotal() {
		float total = 0;
		for (Despesa d : lista) {
			total += d.getValor();
		}
		
		TextView txtTotal = (TextView) findViewById(R.id.txtTotal);
		txtTotal.setText("Total: R$ "+String.format("%.2f",total));
	}
	
	public void inicializaAdapter(ListView lv){
		adapter = new RelAdatper(getApplicationContext());
		lv.setAdapter(adapter); // pronto lista recebe as atualizacoes
		
		// Long Click - Quando o usuario deixa o botao pressionado
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int indice, long arg3) {
				deletaDespesa = lista.get(indice);
//				db.removeDespesa( d.getId(),d.getIdParcela() );
//				atualizaRelatorio();
				mostraDialogo();
				return true;
			}
		});
		
		// Click simples
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,	int indice, long arg3) {
				Despesa d = lista.get(indice);
				Intent in = new Intent(getApplicationContext(),EditDespesa.class);
				Toast.makeText(getApplicationContext(), "ID: "+d.getId(), Toast.LENGTH_SHORT).show();
				in.putExtra("id", d.getId() );
				startActivityForResult(in,Util.ACTIVITY_EDITDESPESA);
				// Editar a despesa
				// OQUE pode ser editado
				// Tipo
				// Subtipo
				// Descricao
				// Valor
				// Se for uma despesa parcelada, nao podera ser editado o valor.
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		atualizaRelatorio();
	}
	
	public void mostraDialogo(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Atencao").setMessage("Deseja remover a desepsa?");
		builder.setPositiveButton("SIM!", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Deletando item!", Toast.LENGTH_LONG).show();
				
				// Deleta a bagaca
				db.removeDespesa( deletaDespesa.getId(),deletaDespesa.getIdParcela() );
				atualizaRelatorio();
//				adapter.notifyDataSetChanged(); // avisa que mudou
			}
		});
		builder.setNegativeButton("NAO!", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Ufa... quase!", Toast.LENGTH_LONG).show();
			}
		});
		builder.create().show();
	}
	
	class RelAdatper extends BaseAdapter {
		Context context;
		LayoutInflater inflater;
		
		
		public RelAdatper(Context context) {
			this.context = context;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lista.size();
		}

		@Override
		public Object getItem(int indice) {
			// TODO Auto-generated method stub
			return lista.get(indice);
		}

		@Override
		public long getItemId(int indice) {
			// TODO Auto-generated method stub
			return indice;
		}

		@Override
		public View getView(int indice, View aux, ViewGroup arg2) {
			View linha = aux;
			if(linha == null) {
				linha = inflater.inflate(R.layout.linha_rel,arg2,false);
			}
			
			Despesa d = lista.get(indice);
			// Prepara as strings
			String cat = d.getTipo()+" / "+d.getSubtipo();
			String data = d.getData();
			String desc = d.getDescricao();
			String valor = "R$ "+String.format("%.2f",d.getValor());
			
			
			// Instancia os itens
			TextView txtCat 	= (TextView) linha.findViewById(R.id.txtLinhaCategoria);
			TextView txtData 	= (TextView) linha.findViewById(R.id.txtLinhaData);
			TextView txtDesc 	= (TextView) linha.findViewById(R.id.txtLinhaDesc);
			TextView txtValor 	= (TextView) linha.findViewById(R.id.txtLinhaValor);
			
			
			txtCat.setText(cat);
			txtData.setText(data);
			txtDesc.setText(desc);
			txtValor.setText(valor);

			return linha;
		}
		
	}
}
