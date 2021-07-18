/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author AlexJPZ
 */
public class Categorias {
    private int IdCat;
    private String Categoria;
    private int IdDpto;

    public int getIdCat() {
        return IdCat;
    }

    public void setIdCat(int IdCat) {
        this.IdCat = IdCat;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String Categoria) {
        this.Categoria = Categoria;
    }

    public int getIdDpto() {
        return IdDpto;
    }

    public void setIdDpto(int IdDpto) {
        this.IdDpto = IdDpto;
    }

    @Override
    public String toString() {
        return Categoria;
    }
    
    
}
