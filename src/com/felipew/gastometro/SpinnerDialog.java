package com.felipew.gastometro;

import java.util.ArrayList;

import com.felipew.gastrometro.dao.DbAdapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;


public class SpinnerDialog extends Dialog {
    private ArrayList<String> listaTipo;
    private ArrayList<String> listaSubTipo;
    private Context mContext;
    private Spinner spinTipo;
    private Spinner spinSubtipo;
    DbAdapter db;

   public interface DialogListener {
        public void ready(String tipo, String subtipo);
        public void cancelled();
    }

    private DialogListener mReadyListener;

    public SpinnerDialog(Context context, ArrayList<String> list, DialogListener readyListener) {
        super(context);
        mReadyListener = readyListener;
        mContext = context;
        listaTipo = new ArrayList<String>();
        listaTipo = list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        db = new DbAdapter(getContext());
        db.open();

        setContentView(R.layout.filtro_dialog);
        spinTipo = (Spinner) findViewById (R.id.dialogspin_tipo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (mContext, android.R.layout.simple_spinner_dropdown_item, listaTipo);
        spinTipo.setAdapter(adapter);
        spinTipo.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int index, long arg3) {
				populaSpinnerSubTipo(index);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				// nao faz nada...
			}
		});

        Button buttonOK = (Button) findViewById(R.id.dialogOK);
        Button buttonCancel = (Button) findViewById(R.id.dialogCancel);
        buttonOK.setOnClickListener(new android.view.View.OnClickListener(){
            public void onClick(View v) {
                int t = spinTipo.getSelectedItemPosition();
                int st = -1;
                String tipo,subtipo = null;
                
                tipo = listaTipo.get(t);
                if( spinSubtipo != null ){
                	st = spinSubtipo.getSelectedItemPosition();
                	subtipo = listaSubTipo.get(st);
                }
                
                
                mReadyListener.ready(tipo,subtipo);
                db.close();
                SpinnerDialog.this.dismiss();
            }
        });
        buttonCancel.setOnClickListener(new android.view.View.OnClickListener(){
            public void onClick(View v) {
                mReadyListener.cancelled();
                db.close();
                SpinnerDialog.this.dismiss();
            }
        });
    }
    
    /**
     * Poe os subtipos no spinner :)
     * @param index
     */
    public void populaSpinnerSubTipo(int index){
    	String strTipo = listaTipo.get(index);
    	listaSubTipo = db.listaSubTipoString(strTipo);
    	spinSubtipo = (Spinner) findViewById (R.id.dialogspin_subtipo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (mContext, android.R.layout.simple_spinner_dropdown_item, listaSubTipo);
        spinSubtipo.setAdapter(adapter);
    }
}
