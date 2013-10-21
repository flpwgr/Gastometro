package com.felipew.gastometro.model;

public class Despesa {
	private long id;
	private long idparcela;
	private String tipo;
	private String subtipo;
	private String descricao;
	private float valor;
	private String data;
	
	
	public Despesa() {
	}
	
	public Despesa(long id, long idparcela, String tipo, String subtipo, String descricao, float valor,String data) {
		this.id = id;
		this.idparcela = idparcela;
		this.tipo = tipo;
		this.subtipo = subtipo;
		this.descricao = descricao;
		this.valor = valor;
		this.data = data;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getSubtipo() {
		return subtipo;
	}
	public void setSubtipo(String subtipo) {
		this.subtipo = subtipo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}

	public long getIdParcela() {
		return idparcela;
	}

	public void setIdParcela(long idparcela) {
		this.idparcela = idparcela;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
