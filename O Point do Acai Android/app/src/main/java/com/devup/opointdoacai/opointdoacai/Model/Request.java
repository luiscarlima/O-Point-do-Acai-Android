package com.devup.opointdoacai.opointdoacai.Model;

import java.util.List;

public class Request {

    private String id;
    private String telefone;
    private String email;
    private String nome;
    private String endereco;
    private String total;
    private String paymentMethod;
    private String troco;
    private String status;
    private String shipping;
    private List<Order> pedidos;
    private String isGetting;
    private String observacao;

    public Request() {
    }

    public Request(String id, String telefone, String email, String nome, String endereco, String total, String paymentMethod, String troco, String status, String shipping, List<Order> pedidos, String isGetting, String observacao) {
        this.id = id;
        this.telefone = telefone;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.troco = troco;
        this.status = status;
        this.shipping = shipping;
        this.pedidos = pedidos;
        this.isGetting = isGetting;
        this.observacao = observacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTroco() {
        return troco;
    }

    public void setTroco(String troco) {
        this.troco = troco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public List<Order> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Order> pedidos) {
        this.pedidos = pedidos;
    }

    public String getIsGetting() {
        return isGetting;
    }

    public void setIsGetting(String isGetting) {
        this.isGetting = isGetting;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
