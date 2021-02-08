package demo.spring.jedis.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Categoria {
    
	@Id
	private Integer idcategoria;
	private String nome;
	
	public Categoria() {
	}

	public Integer getIdcategoria() {
		return idcategoria;
	}

	public void setIdcategoria(Integer idcategoria) {
		this.idcategoria = idcategoria;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "Categoria [idcategoria=" + idcategoria + ", nome=" + nome + "]";
	}
	

}