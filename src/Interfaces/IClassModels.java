/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Graphics.ImagenBanner;
import ModelClass.*;
import javax.swing.JFileChooser;

/**
 *
 * @author AlexJPZ
 */
public interface IClassModels {

    public Caja caja = new Caja();
    public Compra compra = new Compra();
    public Cliente cliente = new Cliente();
    public ImagenBanner p = new ImagenBanner();
    public Proveedor proveedor = new Proveedor();
    public FormatDecimal formato = new FormatDecimal();
    public Departamento departamento = new Departamento();
    public TextFieldEvent evento = new TextFieldEvent();
    public Imprimir imprimir = new Imprimir();
    public Producto producto = new Producto();
    public Venta venta = new Venta();
    public Usuario ususrios = new Usuario();
    public JFileChooser abrirArchivo = new JFileChooser();
}
