package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the chat database table.
 * 
 */
@Embeddable
public class ChatPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false, unique=true, nullable=false, length=30)
	private String usuario1;

	@Column(insertable=false, updatable=false, unique=true, nullable=false, length=30)
	private String usuario2;

	public ChatPK() {
	}
	public String getUsuario1() {
		return this.usuario1;
	}
	public void setUsuario1(String usuario1) {
		this.usuario1 = usuario1;
	}
	public String getUsuario2() {
		return this.usuario2;
	}
	public void setUsuario2(String usuario2) {
		this.usuario2 = usuario2;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ChatPK)) {
			return false;
		}
		ChatPK castOther = (ChatPK)other;
		return 
			this.usuario1.equals(castOther.usuario1)
			&& this.usuario2.equals(castOther.usuario2);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.usuario1.hashCode();
		hash = hash * prime + this.usuario2.hashCode();
		
		return hash;
	}
}