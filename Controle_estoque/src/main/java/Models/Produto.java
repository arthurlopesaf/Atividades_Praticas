package Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Produto {
    private Integer id;
    private String codigo;
    private String nome;
    private String descricao;
    private Integer categoriaId;
    private Categoria categoria;
    private BigDecimal precoCompra;
    private BigDecimal precoVenda;
    private Integer estoqueMinimo;
    private Integer saldoAtual;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Produto() {
        this.precoCompra = BigDecimal.ZERO;
        this.precoVenda = BigDecimal.ZERO;
        this.estoqueMinimo = 0;
        this.saldoAtual = 0;
        this.ativo = true;
    }

    public Produto(String codigo, String nome, Integer categoriaId, BigDecimal precoCompra, BigDecimal precoVenda) {
        this();
        setCodigo(codigo);
        setNome(nome);
        setCategoriaId(categoriaId);
        setPrecoCompra(precoCompra);
        setPrecoVenda(precoVenda);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código do produto não pode ser vazio");
        }
        this.codigo = codigo.trim().toUpperCase();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio");
        }
        this.nome = nome.trim();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        if (categoriaId == null || categoriaId <= 0) {
            throw new IllegalArgumentException("Categoria do produto é obrigatória");
        }
        this.categoriaId = categoriaId;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(BigDecimal precoCompra) {
        if (precoCompra == null || precoCompra.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço de compra não pode ser negativo");
        }
        this.precoCompra = precoCompra;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        if (precoVenda == null || precoVenda.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço de venda deve ser maior que zero");
        }
        this.precoVenda = precoVenda;
    }

    public Integer getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(Integer estoqueMinimo) {
        if (estoqueMinimo == null || estoqueMinimo < 0) {
            estoqueMinimo = 0;
        }
        this.estoqueMinimo = estoqueMinimo;
    }

    public Integer getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(Integer saldoAtual) {
        if (saldoAtual == null) {
            saldoAtual = 0;
        }
        this.saldoAtual = saldoAtual;
    }

    public Boolean isAtivo() {
        return ativo != null && ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo != null && ativo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public BigDecimal getValorEstimado() {
        return precoVenda.multiply(BigDecimal.valueOf(saldoAtual));
    }

    public boolean isEstoqueBaixo() {
        return saldoAtual <= estoqueMinimo;
    }

    @Override
    public String toString() {
        return codigo + " - " + nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produto)) return false;
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
