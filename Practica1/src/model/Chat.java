package model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/**
 * The persistent class for the chat database table.
 * 
 */
@Entity
@NamedQuery(name="Chat.findAll", query="SELECT c FROM Chat c")
public class Chat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String emisor;

	private String mensaje;

	private String receptor;

	public Chat() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmisor() {
		return this.emisor;
	}

	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	public String getMensaje() {
		return this.mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getReceptor() {
		return this.receptor;
	}

	public void setReceptor(String receptor) {
		this.receptor = receptor;
	}

}
