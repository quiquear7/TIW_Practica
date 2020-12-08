package model;

import java.util.List;

public class Respuesta {
	
	private List<Producto> lista;
	private int cop;
	public List<Producto> getLista() {
		return lista;
	}
	public void setLista(List<Producto> lista) {
		this.lista = lista;
	}
	public int getCop() {
		return cop;
	}
	public void setCop(int cop) {
		this.cop = cop;
	}

}
