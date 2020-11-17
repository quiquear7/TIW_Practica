package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the chat database table.
 * 
 */
@Entity
@Table(name="chat")
@NamedQuery(name="Chat.findAll", query="SELECT c FROM Chat c")
public class Chat implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ChatPK id;

	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date fecha;

	@Lob
	@Column(nullable=false)
	private String mensajes;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="usuario1", nullable=false, insertable=false, updatable=false)
	private Usuario usuario1Bean;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="usuario2", nullable=false, insertable=false, updatable=false)
	private Usuario usuario2Bean;

	public Chat() {
	}

	public ChatPK getId() {
		return this.id;
	}

	public void setId(ChatPK id) {
		this.id = id;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getMensajes() {
		return this.mensajes;
	}

	public void setMensajes(String mensajes) {
		this.mensajes = mensajes;
	}

	public Usuario getUsuario1Bean() {
		return this.usuario1Bean;
	}

	public void setUsuario1Bean(Usuario usuario1Bean) {
		this.usuario1Bean = usuario1Bean;
	}

	public Usuario getUsuario2Bean() {
		return this.usuario2Bean;
	}

	public void setUsuario2Bean(Usuario usuario2Bean) {
		this.usuario2Bean = usuario2Bean;
	}

}