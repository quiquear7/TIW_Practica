package model;


import java.io.Serializable;
import controller.BytesSerializerDeserializer;
import javax.json.bind.annotation.JsonbTypeDeserializer;
import javax.json.bind.annotation.JsonbTypeSerializer;
import javax.persistence.*;


/**
 * The persistent class for the producto database table.
 * 
 */
@Entity
@NamedQuery(name="Producto.findAll", query="SELECT p FROM Producto p")
public class Producto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int referencia;

	private String categoria;

	private String descripcion;

	private byte estado;

	@Lob
	@JsonbTypeDeserializer(BytesSerializerDeserializer.class)
	@JsonbTypeSerializer(BytesSerializerDeserializer.class)
	private byte[] imagen;

	private String nombre;

	private float precio;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="vendedor")
	private Usuario usuario;

	public Producto() {
	}

	public int getReferencia() {
		return this.referencia;
	}

	public void setReferencia(int referencia) {
		this.referencia = referencia;
	}

	public String getCategoria() {
		return this.categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public byte getEstado() {
		return this.estado;
	}

	public void setEstado(byte estado) {
		this.estado = estado;
	}

	public byte[] getImagen() {
		return this.imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public float getPrecio() {
		return this.precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}