package Models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Movimentacao {
    public enum TipoMovimentacao {
        ENTRADA, SAIDA
    }

    private Integer id;
    private Integer produtoId;
    private Produto produto;
    private TipoMovimentacao tipo;
    private Integer quantidade;
    private Integer saldoAnterior;
    private Integer saldoNovo;
    private String observacao;
    private LocalDateTime criadoEm;

    public Movimentacao() {
    }

    public Movimentacao(Integer produtoId, TipoMovimentacao tipo, Integer quantidade, String observacao) {
        setProdutoId(produtoId);
        setTipo(tipo);
        setQuantidade(quantidade);
        this.observacao = observacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Integer produtoId) {
        if (produtoId == null || produtoId <= 0) {
            throw new IllegalArgumentException("Produto é obrigatório");
        }
        this.produtoId = produtoId;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Models.Produto produto) {
        this.produto = produto;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de movimentação é obrigatório");
        }
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        this.quantidade = quantidade;
    }

    public Integer getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(Integer saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public Integer getSaldoNovo() {
        return saldoNovo;
    }

    public void setSaldoNovo(Integer saldoNovo) {
        this.saldoNovo = saldoNovo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    @Override
    public String toString() {
        return tipo + " - " + quantidade + " un. - " + criadoEm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movimentacao)) return false;
        Movimentacao that = (Movimentacao) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
