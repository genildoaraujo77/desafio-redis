package demo.spring.jedis.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true,  value = {"hibernateLazyInitializer", "handler"})
public class Gasto {
    
	@Id
	private Integer idgasto;
	private String descricao;
	private double valor;
	private String data;
	private Integer coduser;

	public Gasto(Integer idgasto, String descricao, double valor, String data, Integer coduser) {
		super();
		this.idgasto = idgasto;
		this.descricao = descricao;
		this.valor = valor;
		this.data = data;
		this.coduser = coduser;
	}
	public Gasto() {
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coduser == null) ? 0 : coduser.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((idgasto == null) ? 0 : idgasto.hashCode());
		long temp;
		temp = Double.doubleToLongBits(valor);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gasto other = (Gasto) obj;
		if (coduser == null) {
			if (other.coduser != null)
				return false;
		} else if (!coduser.equals(other.coduser))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (idgasto == null) {
			if (other.idgasto != null)
				return false;
		} else if (!idgasto.equals(other.idgasto))
			return false;
		if (Double.doubleToLongBits(valor) != Double.doubleToLongBits(other.valor))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Gastos [idgasto=" + idgasto + ", descricao=" + descricao + ", valor=" + valor + ", data=" + data
				+ ", coduser=" + coduser + "]";
	}

}
