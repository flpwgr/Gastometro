package com.felipew.gastometro;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;




public class Util {
	public static final int REC_DATA_ATUAL = 0;
	
	// Tipos de Filtro
	public static final int FILTRO_DIA 	= 0; // Somente de Hoje
	public static final int FILTRO_SEM 	= 1; // Somente dos ultimos 7 dias?
	public static final int FILTRO_MES 	= 2; // Somente da mes corrente
	public static final int FILTRO_TIPO = 3; // Somente da mes corrente
	
	// #ID das Activity
	public static final int ACTIVITY_NOVADESPESA = 99;
	public static final int ACTIVITY_RELATORIO = 98;
	public static final int ACTIVITY_SETTINGS = 97;
	public static final int ACTIVITY_EDITDESPESA = 96;
	
	public static String recuperaData(int offset) {
		String saida;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH,offset);
		saida = dateFormat.format( cal.getTime() );
		return saida;
	}
}
