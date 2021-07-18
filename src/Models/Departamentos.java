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
public class Departamentos {

    private int IdDpto;
    private String Departamento;

    public int getIdDpto() {
        return IdDpto;
    }

    public void setIdDpto(int IdDpto) {
        this.IdDpto = IdDpto;
    }

    public String getDepartamento() {
        return Departamento;
    }

    public void setDepartamento(String Departamento) {
        this.Departamento = Departamento;
    }

    @Override
    public String toString() {
        return Departamento;
    }

}
