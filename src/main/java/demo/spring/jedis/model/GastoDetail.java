package demo.spring.jedis.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true, value = { "hibernateLazyInitializer", "handler" })
public class GastoDetail {

	@Id
	private Integer idgasto;
	private String descricao;
	private double valor;
	private String data;
	private Integer coduser;
	private Integer codcategoria;
	private String nomecategoria;

	public GastoDetail(Integer idgasto, String descricao, double valor, String data, Integer coduser,
			Integer codcategoria, String nomecategoria) {
		super();
		this.idgasto = idgasto;
		this.descricao = descricao;
		this.valor = valor;
		this.data = data;
		this.coduser = coduser;
		this.codcategoria = codcategoria;
		this.nomecategoria = nomecategoria;
	}

	public GastoDetail() {
		super();
	}

	public Integer getIdgasto() {
		return idgasto;
	}

	public void setIdgasto(Integer idgasto) {
		this.idgasto = idgasto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getCoduser() {
		return coduser;
	}

	public void setCoduser(Integer coduser) {
		this.coduser = coduser;
	}

	public Integer getCodcategoria() {
		return codcategoria;
	}

	public void setCodcategoria(Integer codcategoria) {
		this.codcategoria = codcategoria;
	}

	public String getNomecategoria() {
		return nomecategoria;
	}

	public void setNomecategoria(String nomecategoria) {
		this.nomecategoria = nomecategoria;
	}

	@Override
	public String toString() {
		return "GastoDetail [idgasto=" + idgasto + ", descricao=" + descricao + ", valor=" + valor + ", data=" + data
				+ ", coduser=" + coduser + ", codcategoria=" + codcategoria + ", nomecategoria=" + nomecategoria + "]";
	}

}
