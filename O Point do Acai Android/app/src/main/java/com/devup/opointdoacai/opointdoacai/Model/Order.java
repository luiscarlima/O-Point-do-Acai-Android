package com.devup.opointdoacai.opointdoacai.Model;

public class Order {

    private String ProductId;
    private String Quantidade;
    private String Complementos;
    private String Preco;

    public Order() {
    }

    public Order(String productId, String quantidade, String complementos, String preco) {
        ProductId = productId;
        Quantidade = quantidade;
        Complementos = complementos;
        Preco = preco;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(String quantidade) {
        Quantidade = quantidade;
    }

    public String getComplementos() {
        return Complementos;
    }

    public void setComplementos(String complementos) {
        Complementos = complementos;
    }

    public String getPreco() {
        return Preco;
    }

    public void setPreco(String preco) {
        Preco = preco;
    }
}
