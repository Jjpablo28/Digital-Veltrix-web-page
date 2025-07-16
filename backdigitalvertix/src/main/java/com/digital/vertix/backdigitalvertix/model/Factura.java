package com.digital.vertix.backdigitalvertix.model;


import java.util.List;
import java.time.LocalDateTime;

public class Factura {
    private String cliente;
    private LocalDateTime fecha;
    private List<Item> items;
	

    public double getTotal() {
        return items.stream()
                    .mapToDouble(i -> i.getCantidad() * i.getPrecio())
                    .sum();
    }

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

    
    // Getters y setters
}
