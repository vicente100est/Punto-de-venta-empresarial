/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Interfaces.IClassModels;
import ModelClass.*;
import Models.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author AlexJPZ
 */
public class Sistema extends javax.swing.JFrame implements IClassModels {

    private List<Usuarios> listUsuario = new ArrayList<>();
    private List<Cajas> listCaja = new ArrayList<>();
    private List<Proveedores> dataProveedor = new ArrayList<>();
    private List<JLabel> labels = new ArrayList<>();
    private Object[] textFieldObject, labelsObject;
    private DefaultTableModel tablaModelReportCliente, tablaModelReportPd;
    private String accion = "insert", pago, deudaActual, role, proveedores, usuario, saldoProveedor;
    private int pageSize = 10, tab = 0, idProveedorCp, idProveedor, idCat = 0, idUsuario;
    private int num_registro = 0, numPagi = 0, idCliente, idRegistro, idDpto = 0;
    private int idCompra = 0, cantidad, funcion, idProducto, cajaUser;
    private String precioCompra;
    private Departamentos dpt;
    private Categorias cat;
    private Caja objectCaja;

    /**
     * Creates new form Sistema
     */
    public Sistema(List<Usuarios> listUsuario, List<Cajas> listCaja) {

        initComponents();

        if (null != listUsuario) {
            role = listUsuario.get(0).getRole();
            idUsuario = listUsuario.get(0).getIdUsuario();
            usuario = listUsuario.get(0).getUsuario();
            if (role.equals("Admin")) {
                Label_Usuario.setText(listUsuario.get(0).getNombre());
                Label_Caja.setText("0");
                this.listUsuario = listUsuario;
                cajaUser = 0;
            } else {
                Label_Usuario.setText(listUsuario.get(0).getNombre());
                Label_Caja.setText(String.valueOf(listCaja.get(0).getCaja()));
                this.listUsuario = listUsuario;
                this.listCaja = listCaja;
                cajaUser = listCaja.get(0).getCaja();
            }
        }

        //Agregar la imagen al jPanel
        PanelBanner.add(p);
        timer1.start();

        //<editor-fold defaultstate="collapsed" desc="CODIGO CLIENTE">
        if (!role.equalsIgnoreCase("Admin")) {
            RadioButton_PagosCliente.setEnabled(false);
        }
        RadioButton_IngresarCliente.setSelected(true);
        RadioButton_IngresarCliente.setForeground(new Color(0, 153, 51));
        TextField_PagosCliente.setEnabled(false);
        cliente.reportesCliente(Table_ReportesCLT, idCliente);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="CODIGO DEL RPOVEEEDOR">
        if (!role.equalsIgnoreCase("Admin")) {
            RadioButtonPd_Pagos.setEnabled(false);
        }
        RadioButtonPd_Ingresar.setSelected(true);
        RadioButtonPd_Ingresar.setForeground(new Color(0, 153, 51));
        TextFieldPd_Pagos.setEnabled(false);
        proveedor.reportesProveeedor(TablePd_Reportes, idProveedor);
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="CODIGO DEPARTAMENTO">
        TextField_Categoria.setEnabled(false);
        RadioButton_Dpt.setSelected(true);
        RadioButton_Dpt.setForeground(new Color(0, 153, 51));
        departamento.searchDepartamentos(Table_Dpt, "");
        departamento.getCategorias(Table_Cat, idDpto);
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="CODIGO COMPRAS">
        compra.searchProveedores(TableCP_Proveedor, "");
        if (!role.equalsIgnoreCase("Admin")) {
            Button_Compras.setEnabled(false);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="CODIGO DE VENTAS">
        venta.start(cajaUser, idUsuario);
        labels.add(label_Deuda);
        labels.add(label_ReciboDeuda);
        labels.add(label_ReciboDeudaTotal);
        labels.add(label_ReciboNombre);
        labels.add(label_ReciboDeudaAnterior);
        labels.add(label_ReciboUltimoPago);
        labels.add(label_ReciboFecha);

        labels.add(label_Pago);
        labels.add(label_MensajeCliente);
        labels.add(label_SuCambio);
        Button_Ventas.setEnabled(false);
        restablecerVentas();
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="CODIGO DE COFIGURACIONES">
        if (role.equals("Admin")) {
            Button_Configuracion.setEnabled(true);

        } else {

            Button_Configuracion.setEnabled(false);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="CODIGO DE USUARIOS">
        Object[] TextFieldObject = {
            TextFieldUser_Nombre, TextFieldUser_Apellido, TextFieldUser_Telefono, TextFieldUser_Direccion,
            TextFieldUser_Email, TextFieldUser_Password, TextFieldUser_Usuario
        };
        Object[] LabelsObject = {
            LabelUser_Nombre, LabelUser_Apellido, LabelUser_Telefono, LabelUser_Direccion,
            LabelUser_Email, LabelUser_Password, LabelUser_Usuario, LabelUser_Imagen, LabelUser_Paginas};
        this.textFieldObject = TextFieldObject;
        this.labelsObject = LabelsObject;
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="CODIGO DE CAJAS">
        Object[] objectCajas = {TableCajas_Ingresos, dateChooserCombo_Cajas, idUsuario, Table_Cajas, Spinner_Caja};
        labelCajas = new ArrayList<JLabel>();
        labelCajas.add(LabelCaja_Ingreso);
        labelCajas.add(LabelCaja_Retirar);
        labelCajas.add(LabelCaja_Ingresos);
        labelCajas.add(LabelNum_Caja);
        labelCajas.add(LabelCaja_Numero);

        textField = new ArrayList<JTextField>();
        textField.add(TextFieldCaja_Retirar);
        textField.add(TextFieldCaja_IngresoInicial);
        objectCaja = new Caja(objectCajas, labelCajas, textField);
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="CODIGO DE INVENTARIO">
        Object[] objectBodega = {
            Table_Bodega,
            SpinnerBodega_Existencia,
            CheckBoxBodega_Existencia,
            TabbedPane_Inv,
            TableInv_Productos,
            dateChooserCombo_Inicio,
            dateChooserCombo_Final,
            CheckBox_MaxVentas,
            TableInv_Ventas
        };
        textFieldBodega = new ArrayList<JTextField>();
        textFieldBodega.add(TextFieldBodega_Existencia);
        textFieldBodega.add(TextFieldInv_Precio);
        textFieldBodega.add(TextFieldInv_Descuento);

        labelBodega = new ArrayList<JLabel>();
        labelBodega.add(LabelBodega_Existencia);
        labelBodega.add(LabelInvBodega_Paginas);
        labelBodega.add(LabelInv_Productos);
        labelBodega.add(LabelInvProductos_Paginas);
        labelBodega.add(LabelInv_Precio);
        labelBodega.add(LabelInv_Descuento);
        labelBodega.add(LabelInvVentas_Paginas);

        inventario = new Inventario(objectBodega, labelBodega, textFieldBodega);
        ListClass.inventario = inventario;
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="CODIGO DE REPORTES">
        Object[] objectReport = {
            ComboBox_ReportDtop,
            ComboBox_ReportCat,
            TableReport_Productos,
            LabelGrafica_Producto,
            TableReport_InformeProducto,
            TableReport_produtoBodega
        };
        _report = new Reportes(objectReport);
        //</editor-fold>
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooserDialog1 = new datechooser.beans.DateChooserDialog();
        dateChooserDialog2 = new datechooser.beans.DateChooserDialog();
        PanelBanner = new javax.swing.JPanel();
        Button_Configuracion = new javax.swing.JButton();
        Button_Compras = new javax.swing.JButton();
        Button_Cat_Dpt = new javax.swing.JButton();
        Button_Poductos = new javax.swing.JButton();
        Button_Cliente = new javax.swing.JButton();
        Button_Ventas = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        TextFieldVT_BuscarProductos = new javax.swing.JTextField();
        Button_BuscarProducto = new javax.swing.JButton();
        Label_MensajeVenta = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        Table_VentaTempo =   Table_Clientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        ButtonVT_Anterior = new javax.swing.JButton();
        ButtonVT_Siguiente = new javax.swing.JButton();
        ButtonVT_Primero = new javax.swing.JButton();
        ButtonTV_Ultimo = new javax.swing.JButton();
        LabelVT_Paginas = new javax.swing.JLabel();
        Button_ReciboVenta = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        button_Cobrar = new javax.swing.JButton();
        ButtonVT_Cancelar = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel36 = new javax.swing.JPanel();
        CheckBox_Creditos = new javax.swing.JCheckBox();
        label_Pago = new javax.swing.JLabel();
        TextField_Pagos = new javax.swing.JTextField();
        Label_ApellidoCliente1 = new javax.swing.JLabel();
        label_ImportesVentas = new javax.swing.JLabel();
        label_SuCambio = new javax.swing.JLabel();
        label_Cambio = new javax.swing.JLabel();
        Label_TelefonoCliente2 = new javax.swing.JLabel();
        label_Deuda = new javax.swing.JLabel();
        jPanel37 = new javax.swing.JPanel();
        Label_ApellidoCliente2 = new javax.swing.JLabel();
        labelVT_IngresoIni = new javax.swing.JLabel();
        label_SuCambio1 = new javax.swing.JLabel();
        labelVT_IngresosVt = new javax.swing.JLabel();
        Label_TelefonoCliente3 = new javax.swing.JLabel();
        labelVT_IngresoTotal = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        TableVT_Cliente = TableVT_Cliente = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        TextFieldVT_BuscarCliente = new javax.swing.JTextField();
        label_MensajeCliente = new javax.swing.JLabel();
        Panel_ReciboVenta = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        label_ReciboDeudaAnterior = new javax.swing.JLabel();
        label_ReciboNombre = new javax.swing.JLabel();
        label_ReciboDeuda = new javax.swing.JLabel();
        label_ReciboDeudaTotal = new javax.swing.JLabel();
        label_ReciboUltimoPago = new javax.swing.JLabel();
        label_ReciboFecha = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        TextField_BuscarCliente = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        RadioButton_IngresarCliente = new javax.swing.JRadioButton();
        RadioButton_PagosCliente = new javax.swing.JRadioButton();
        Label_NombreCliente = new javax.swing.JLabel();
        TextField_NombreCliente = new javax.swing.JTextField();
        Label_ApellidoCliente = new javax.swing.JLabel();
        TextField_ApellidioCliente = new javax.swing.JTextField();
        Label_DireccionCliente = new javax.swing.JLabel();
        TextField_DireccioCliente = new javax.swing.JTextField();
        Label_TelefonoCliente = new javax.swing.JLabel();
        TextField_TelefonoCliente = new javax.swing.JTextField();
        Label_PagoCliente = new javax.swing.JLabel();
        TextField_PagosCliente = new javax.swing.JTextField();
        Button_GuardarCliente = new javax.swing.JButton();
        Button_EliminarCLT = new javax.swing.JButton();
        Button_CancelarCLT = new javax.swing.JButton();
        Label_IdCliente = new javax.swing.JLabel();
        TextField_IdCliente = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Table_Clientes =   Table_Clientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        Button_AnteriorCLT = new javax.swing.JButton();
        Button_SiguienteCLT = new javax.swing.JButton();
        Button_PrimeroCLT = new javax.swing.JButton();
        Button_UltimoCLT = new javax.swing.JButton();
        Label_PaginasClientes = new javax.swing.JLabel();
        Button_FacturaCliente = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Table_ReportesCLT = Table_ReportesCLT = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        Panel_ReciboCliente = new javax.swing.JPanel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        Label_SaldoActualCLT = new javax.swing.JLabel();
        Label_NombreCLT = new javax.swing.JLabel();
        Label_ApellidoCLT = new javax.swing.JLabel();
        Label_UltimoPagoCLT = new javax.swing.JLabel();
        Label_FechaPagoCLT = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        TextFieldPd_Buscar = new javax.swing.JTextField();
        jPanel30 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        Table_Proveedor =   Table_Proveedor = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        ButtonPd_Anterior = new javax.swing.JButton();
        ButtonPd_Siguiente = new javax.swing.JButton();
        ButtonPd_Primero = new javax.swing.JButton();
        ButtonPd_Ultimo = new javax.swing.JButton();
        LabelPd_Paginas = new javax.swing.JLabel();
        ButtonPd_Factura = new javax.swing.JButton();
        jPanel31 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        RadioButtonPd_Ingresar = new javax.swing.JRadioButton();
        RadioButtonPd_Pagos = new javax.swing.JRadioButton();
        LabelPd_Proveedor = new javax.swing.JLabel();
        TextFieldPd_Proveedor = new javax.swing.JTextField();
        LabelPd_Email = new javax.swing.JLabel();
        TextFieldPd_Email = new javax.swing.JTextField();
        LabelPd_Telefono = new javax.swing.JLabel();
        TextFieldPd_Telefono = new javax.swing.JTextField();
        LabelPd_Pagos = new javax.swing.JLabel();
        TextFieldPd_Pagos = new javax.swing.JTextField();
        Button_GuardarProveedor = new javax.swing.JButton();
        ButtonPd_Eliminar = new javax.swing.JButton();
        ButtonPd_Cancelar = new javax.swing.JButton();
        jPanel32 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        TablePd_Reportes = TablePd_Reportes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        PanelPd_Recibo = new javax.swing.JPanel();
        jLabel82 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        LabelPd_SaldoActual = new javax.swing.JLabel();
        Label_ProveedorRB = new javax.swing.JLabel();
        LabelPd_UltimoPago = new javax.swing.JLabel();
        LabelPd_FechaPago = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        LabelPT_Descripcion = new javax.swing.JLabel();
        TextFieldPT_Descripcion = new javax.swing.JTextField();
        LabelPT_PrecioVenta = new javax.swing.JLabel();
        TextFieldPT_PrecioVenta = new javax.swing.JTextField();
        Label_DepartamentoPDT = new javax.swing.JLabel();
        ButtonPT_Guardar = new javax.swing.JButton();
        ButtonPT_Cancelar = new javax.swing.JButton();
        Label_CategoriaPDT = new javax.swing.JLabel();
        ComboBox_Departamento = new javax.swing.JComboBox();
        ComboBox_Categoria = new javax.swing.JComboBox();
        PanelPT_CodigoBarra = new javax.swing.JPanel();
        LabelPT_CodigoBarra = new javax.swing.JLabel();
        LabelPT_Producto = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TablePT_Compras =   TablePT_Compras = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jPanel13 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TablePT_Productos = TablePT_Productos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jLabel14 = new javax.swing.JLabel();
        Label = new javax.swing.JLabel();
        ButtonPT_Primero = new javax.swing.JButton();
        ButtonPT_Anterior = new javax.swing.JButton();
        ButtonPT_Siguiente = new javax.swing.JButton();
        ButtonPT_Ultimo = new javax.swing.JButton();
        LabelPT_Paginas = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        Label1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        TextField_ComprasProductos = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        RadioButton_Dpt = new javax.swing.JRadioButton();
        RadioButton_Cat = new javax.swing.JRadioButton();
        Button_GuardarCatDpt = new javax.swing.JButton();
        Button_EliminarCatDpt = new javax.swing.JButton();
        Button_CancelarCatDpt = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        Label_Cat = new javax.swing.JLabel();
        TextField_Categoria = new javax.swing.JTextField();
        jPanel23 = new javax.swing.JPanel();
        Label_Dpt = new javax.swing.JLabel();
        TextField_Departamento = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        Table_Dpt =   Table_Dpt = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jPanel20 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        TextField_BuscarDpt = new javax.swing.JTextField();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        Table_Cat =   Table_Cat = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jPanel5 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        Button_GuardarCliente1 = new javax.swing.JButton();
        ButtonCP_Eliminar = new javax.swing.JButton();
        Button_CancelarCompras = new javax.swing.JButton();
        TabbedPane_Compras = new javax.swing.JTabbedPane();
        jPanel33 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        LabelCP_Descripcion = new javax.swing.JLabel();
        TextFieldCP_Descripcion = new javax.swing.JTextField();
        LabelCP_Cantidad = new javax.swing.JLabel();
        TextFieldCP_Cantidad = new javax.swing.JTextField();
        LabelCP_Precio = new javax.swing.JLabel();
        TextFieldCP_Precio = new javax.swing.JTextField();
        LabelCP_ImporteCompra = new javax.swing.JLabel();
        LabelCP_Importe1 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        LabelCP_Encaja = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        LabelCP_MontoPagar = new javax.swing.JLabel();
        LabelCP_Pago = new javax.swing.JLabel();
        TextFieldCP_Pagos = new javax.swing.JTextField();
        CheckBoxCP_Deuda = new javax.swing.JCheckBox();
        jLabel53 = new javax.swing.JLabel();
        LabelCP_Deudas = new javax.swing.JLabel();
        PanelCP_Recibo = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        LabelCP_ProveedorR = new javax.swing.JLabel();
        LabelCP_TotalPagar = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        LabelCP_Deuda = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        LabelCP_Saldo = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        LabelCP_Fecha = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        Table_Compras =   Table_Compras = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        ButtonCP_Anterior = new javax.swing.JButton();
        ButtonCP_Siguiente = new javax.swing.JButton();
        ButtonCP_Primero = new javax.swing.JButton();
        ButtonCP_Ultimo = new javax.swing.JButton();
        LabelCP_Paginas = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        TextFieldCP_Buscar = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        LabelCP_Proveedor = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        TableCP_Proveedor = TableCP_Proveedor = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        TextFieldCP_Proveedor = new javax.swing.JTextField();
        LabelCP_Importe2 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        Button_Usuarios = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        Button_Cajas = new javax.swing.JButton();
        Button_Inventario = new javax.swing.JButton();
        jLabel60 = new javax.swing.JLabel();
        Button_Reportes = new javax.swing.JButton();
        jLabel75 = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        TextFieldUser_Buscar = new javax.swing.JTextField();
        jPanel41 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel43 = new javax.swing.JPanel();
        LabelUser_Nombre = new javax.swing.JLabel();
        TextFieldUser_Nombre = new javax.swing.JTextField();
        LabelUser_Apellido = new javax.swing.JLabel();
        TextFieldUser_Apellido = new javax.swing.JTextField();
        LabelUser_Telefono = new javax.swing.JLabel();
        TextFieldUser_Telefono = new javax.swing.JTextField();
        LabelUser_Direccion = new javax.swing.JLabel();
        TextFieldUser_Direccion = new javax.swing.JTextField();
        LabelUser_Email = new javax.swing.JLabel();
        TextFieldUser_Email = new javax.swing.JTextField();
        LabelUser_Password = new javax.swing.JLabel();
        TextFieldUser_Password = new javax.swing.JTextField();
        LabelUser_Usuario = new javax.swing.JLabel();
        TextFieldUser_Usuario = new javax.swing.JTextField();
        jPanel44 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        LabelUser_Imagen = new javax.swing.JLabel();
        ButtonUser_Imagen = new javax.swing.JButton();
        ButtonUser_Guardar = new javax.swing.JButton();
        Button_CancelarCompras1 = new javax.swing.JButton();
        jPanel42 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table_Usuario =     Table_Usuario = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        ComboBoxUser_Roles = new javax.swing.JComboBox<>();
        ButtonUser_Primero = new javax.swing.JButton();
        ButtonUser_Anterior = new javax.swing.JButton();
        ButtonUser_Siguiente = new javax.swing.JButton();
        ButtonUser_Ultimo = new javax.swing.JButton();
        LabelUser_Paginas = new javax.swing.JLabel();
        jPanel46 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        dateChooserCombo_Cajas = new datechooser.beans.DateChooserCombo();
        jPanel48 = new javax.swing.JPanel();
        TabbedPane_Caja2 = new javax.swing.JTabbedPane();
        jPanel53 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        TableCajas_Ingresos =     TableCajas_Ingresos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jPanel54 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        Table_Cajas =     Table_Cajas = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jPanel49 = new javax.swing.JPanel();
        TabbedPane_Caja1 = new javax.swing.JTabbedPane();
        jPanel50 = new javax.swing.JPanel();
        LabelUser_Usuario1 = new javax.swing.JLabel();
        TextFieldCaja_Retirar = new javax.swing.JTextField();
        LabelCaja_Ingresos = new javax.swing.JLabel();
        LabelCaja_Retirar = new javax.swing.JLabel();
        LabelUser_Usuario3 = new javax.swing.JLabel();
        LabelCaja_Ingreso = new javax.swing.JLabel();
        jPanel51 = new javax.swing.JPanel();
        LabelUser_Usuario4 = new javax.swing.JLabel();
        LabelNum_Caja = new javax.swing.JLabel();
        Spinner_Caja = new javax.swing.JSpinner();
        CheckBoxCaja_Ingresos = new javax.swing.JCheckBox();
        LabelUser_Usuario5 = new javax.swing.JLabel();
        LabelCaja_Numero = new javax.swing.JLabel();
        TextFieldCaja_IngresoInicial = new javax.swing.JTextField();
        LabelCaja_IngresoInicial = new javax.swing.JLabel();
        ButtonCaja_Guardar = new javax.swing.JButton();
        Button_CancelarBodega = new javax.swing.JButton();
        jPanel52 = new javax.swing.JPanel();
        TabbedPane_Inv = new javax.swing.JTabbedPane();
        jPanel55 = new javax.swing.JPanel();
        jPanel57 = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        SpinnerBodega_Existencia = new javax.swing.JSpinner();
        CheckBoxBodega_Existencia = new javax.swing.JCheckBox();
        LabelBodega_Existencia = new javax.swing.JLabel();
        TextFieldBodega_Existencia = new javax.swing.JTextField();
        ButtonBodega_Cancelar = new javax.swing.JButton();
        ButtonInvr_GuardarBodega = new javax.swing.JButton();
        jLabel65 = new javax.swing.JLabel();
        ButtonBodega_Excel = new javax.swing.JButton();
        ButtonBodega_PDF = new javax.swing.JButton();
        jPanel58 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        Table_Bodega =     Table_Bodega = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        ButtonInvBodega_Primero = new javax.swing.JButton();
        ButtonInvBodega_Anterior = new javax.swing.JButton();
        ButtonInvBodega_Siguiente = new javax.swing.JButton();
        ButtonInvBodega_Ultimo = new javax.swing.JButton();
        LabelInvBodega_Paginas = new javax.swing.JLabel();
        jPanel56 = new javax.swing.JPanel();
        jPanel60 = new javax.swing.JPanel();
        LabelInv_Productos = new javax.swing.JLabel();
        LabelInv_Precio = new javax.swing.JLabel();
        TextFieldInv_Precio = new javax.swing.JTextField();
        ButtonInvProductos_Cancelar = new javax.swing.JButton();
        ButtonInv_GuardarProductos = new javax.swing.JButton();
        jLabel68 = new javax.swing.JLabel();
        ButtonInvProducto_Excel = new javax.swing.JButton();
        ButtonInvProducto_PDF = new javax.swing.JButton();
        LabelInv_Descuento = new javax.swing.JLabel();
        TextFieldInv_Descuento = new javax.swing.JTextField();
        jPanel61 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        TableInv_Productos =     TableInv_Productos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        ButtonInvProducto_Primero = new javax.swing.JButton();
        ButtonInvProducto_Anterior = new javax.swing.JButton();
        ButtonInvProducto_Siguiente = new javax.swing.JButton();
        ButtonInvProducto_Ultimo = new javax.swing.JButton();
        LabelInvProductos_Paginas = new javax.swing.JLabel();
        jPanel62 = new javax.swing.JPanel();
        jPanel63 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        TableInv_Ventas =     TableInv_Ventas = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        ButtonInvVenta_Primero = new javax.swing.JButton();
        ButtonInvVenta_Anterior = new javax.swing.JButton();
        ButtonInvVenta_Siguiente = new javax.swing.JButton();
        ButtonInvVenta_Ultimo = new javax.swing.JButton();
        LabelInvVentas_Paginas = new javax.swing.JLabel();
        jPanel64 = new javax.swing.JPanel();
        LabelInv_Productos1 = new javax.swing.JLabel();
        LabelInv_Precio1 = new javax.swing.JLabel();
        ButtonInvProductos_Cancelar1 = new javax.swing.JButton();
        jLabel69 = new javax.swing.JLabel();
        ButtonInvProducto_Excel1 = new javax.swing.JButton();
        ButtonInvProducto_PDF1 = new javax.swing.JButton();
        LabelInv_Descuento1 = new javax.swing.JLabel();
        dateChooserCombo_Inicio = new datechooser.beans.DateChooserCombo();
        dateChooserCombo_Final = new datechooser.beans.DateChooserCombo();
        CheckBox_MaxVentas = new javax.swing.JCheckBox();
        jPanel59 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        TextFieldInv_Buscar = new javax.swing.JTextField();
        jPanel65 = new javax.swing.JPanel();
        jPanel66 = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        TabbedPane_Inv1 = new javax.swing.JTabbedPane();
        jPanel67 = new javax.swing.JPanel();
        jPanel68 = new javax.swing.JPanel();
        jLabel70 = new javax.swing.JLabel();
        Label_DepartamentoPDT1 = new javax.swing.JLabel();
        ComboBox_ReportDtop = new javax.swing.JComboBox();
        Label_CategoriaPDT1 = new javax.swing.JLabel();
        ComboBox_ReportCat = new javax.swing.JComboBox();
        jScrollPane19 = new javax.swing.JScrollPane();
        TableReport_Productos =     TableReport_Productos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jLabel67 = new javax.swing.JLabel();
        TextFieldInv_Buscar1 = new javax.swing.JTextField();
        jPanel69 = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        jPanel70 = new javax.swing.JPanel();
        LabelGrafica_Producto = new javax.swing.JLabel();
        jPanel71 = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        TableReport_InformeProducto =     TableReport_InformeProducto = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jLabel72 = new javax.swing.JLabel();
        jPanel72 = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        jScrollPane21 = new javax.swing.JScrollPane();
        TableReport_produtoBodega =     TableReport_produtoBodega = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        Button_Proveedor = new javax.swing.JButton();
        jLabel43 = new javax.swing.JLabel();
        Label_Usuario = new javax.swing.JLabel();
        Label_Caja = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema punto de ventas");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        PanelBanner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        PanelBanner.setForeground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout PanelBannerLayout = new javax.swing.GroupLayout(PanelBanner);
        PanelBanner.setLayout(PanelBannerLayout);
        PanelBannerLayout.setHorizontalGroup(
            PanelBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        PanelBannerLayout.setVerticalGroup(
            PanelBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 66, Short.MAX_VALUE)
        );

        Button_Configuracion.setBackground(new java.awt.Color(0, 153, 153));
        Button_Configuracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Settings.png"))); // NOI18N
        Button_Configuracion.setToolTipText("");
        Button_Configuracion.setBorder(null);
        Button_Configuracion.setBorderPainted(false);
        Button_Configuracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_ConfiguracionActionPerformed(evt);
            }
        });

        Button_Compras.setBackground(new java.awt.Color(0, 153, 153));
        Button_Compras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/shopping_cart.png"))); // NOI18N
        Button_Compras.setToolTipText("");
        Button_Compras.setBorder(null);
        Button_Compras.setBorderPainted(false);
        Button_Compras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_Compras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_ComprasActionPerformed(evt);
            }
        });

        Button_Cat_Dpt.setBackground(new java.awt.Color(0, 153, 153));
        Button_Cat_Dpt.setForeground(new java.awt.Color(255, 255, 255));
        Button_Cat_Dpt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Etiqueta.png"))); // NOI18N
        Button_Cat_Dpt.setToolTipText("");
        Button_Cat_Dpt.setBorder(null);
        Button_Cat_Dpt.setBorderPainted(false);
        Button_Cat_Dpt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_Cat_Dpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_Cat_DptActionPerformed(evt);
            }
        });

        Button_Poductos.setBackground(new java.awt.Color(0, 153, 153));
        Button_Poductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/productos.png"))); // NOI18N
        Button_Poductos.setToolTipText("");
        Button_Poductos.setBorder(null);
        Button_Poductos.setBorderPainted(false);
        Button_Poductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_PoductosActionPerformed(evt);
            }
        });

        Button_Cliente.setBackground(new java.awt.Color(0, 153, 153));
        Button_Cliente.setForeground(new java.awt.Color(255, 255, 255));
        Button_Cliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/people.png"))); // NOI18N
        Button_Cliente.setToolTipText("");
        Button_Cliente.setBorder(null);
        Button_Cliente.setBorderPainted(false);
        Button_Cliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_Cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_ClienteActionPerformed(evt);
            }
        });

        Button_Ventas.setBackground(new java.awt.Color(0, 153, 153));
        Button_Ventas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/shopping.png"))); // NOI18N
        Button_Ventas.setToolTipText("");
        Button_Ventas.setBorder(null);
        Button_Ventas.setBorderPainted(false);
        Button_Ventas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_VentasActionPerformed(evt);
            }
        });

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(70, 106, 124));
        jLabel12.setText("Ventas");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(70, 106, 124));
        jLabel15.setText("Buscar");

        Button_BuscarProducto.setBackground(new java.awt.Color(0, 153, 153));
        Button_BuscarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search.png"))); // NOI18N
        Button_BuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_BuscarProductoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addGap(205, 205, 205)
                .addComponent(jLabel15)
                .addGap(18, 18, 18)
                .addComponent(TextFieldVT_BuscarProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Button_BuscarProducto)
                .addGap(18, 18, 18)
                .addComponent(Label_MensajeVenta)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Label_MensajeVenta)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel15)
                            .addComponent(TextFieldVT_BuscarProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(Button_BuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_VentaTempo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Table_VentaTempo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_VentaTempo.setRowHeight(20);
        Table_VentaTempo.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_VentaTempo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_VentaTempoMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(Table_VentaTempo);

        ButtonVT_Anterior.setBackground(new java.awt.Color(0, 153, 153));
        ButtonVT_Anterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
        ButtonVT_Anterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonVT_AnteriorActionPerformed(evt);
            }
        });

        ButtonVT_Siguiente.setBackground(new java.awt.Color(0, 153, 153));
        ButtonVT_Siguiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
        ButtonVT_Siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonVT_SiguienteActionPerformed(evt);
            }
        });

        ButtonVT_Primero.setBackground(new java.awt.Color(0, 153, 153));
        ButtonVT_Primero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
        ButtonVT_Primero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonVT_PrimeroActionPerformed(evt);
            }
        });

        ButtonTV_Ultimo.setBackground(new java.awt.Color(0, 153, 153));
        ButtonTV_Ultimo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
        ButtonTV_Ultimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonTV_UltimoActionPerformed(evt);
            }
        });

        LabelVT_Paginas.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        LabelVT_Paginas.setForeground(new java.awt.Color(70, 106, 124));
        LabelVT_Paginas.setText("Page");

        Button_ReciboVenta.setBackground(new java.awt.Color(0, 153, 153));
        Button_ReciboVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Imprimir.png"))); // NOI18N
        Button_ReciboVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_ReciboVentaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(302, 302, 302)
                        .addComponent(LabelVT_Paginas))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(ButtonVT_Primero, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonVT_Anterior, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonVT_Siguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonTV_Ultimo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 552, Short.MAX_VALUE)
                        .addComponent(Button_ReciboVenta)))
                .addGap(28, 28, 28))
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7)
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelVT_Paginas)
                .addGap(4, 4, 4)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ButtonTV_Ultimo)
                        .addComponent(Button_ReciboVenta))
                    .addComponent(ButtonVT_Siguiente, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ButtonVT_Anterior, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ButtonVT_Primero, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("")));
        jPanel25.setForeground(new java.awt.Color(70, 106, 124));

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(70, 106, 124));
        jLabel16.setText("Configuración de venta");
        jLabel16.setToolTipText("");

        button_Cobrar.setBackground(new java.awt.Color(0, 153, 153));
        button_Cobrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
        button_Cobrar.setBorder(null);
        button_Cobrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_CobrarActionPerformed(evt);
            }
        });

        ButtonVT_Cancelar.setBackground(new java.awt.Color(0, 153, 153));
        ButtonVT_Cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
        ButtonVT_Cancelar.setBorder(null);
        ButtonVT_Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonVT_CancelarActionPerformed(evt);
            }
        });

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));
        jPanel36.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        CheckBox_Creditos.setForeground(new java.awt.Color(70, 106, 124));
        CheckBox_Creditos.setText("Credito");
        CheckBox_Creditos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBox_CreditosActionPerformed(evt);
            }
        });

        label_Pago.setForeground(new java.awt.Color(70, 106, 124));
        label_Pago.setText("Pagó con");
        label_Pago.setToolTipText("");

        TextField_Pagos.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextField_Pagos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_PagosKeyReleased(evt);
            }
        });

        Label_ApellidoCliente1.setForeground(new java.awt.Color(70, 106, 124));
        Label_ApellidoCliente1.setText("Monto a pagar");

        label_ImportesVentas.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        label_ImportesVentas.setForeground(new java.awt.Color(70, 106, 124));
        label_ImportesVentas.setText("$0.00");
        label_ImportesVentas.setToolTipText("");

        label_SuCambio.setForeground(new java.awt.Color(70, 106, 124));
        label_SuCambio.setText("Su cambio");

        label_Cambio.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        label_Cambio.setForeground(new java.awt.Color(70, 106, 124));
        label_Cambio.setText("$0.00");

        Label_TelefonoCliente2.setForeground(new java.awt.Color(70, 106, 124));
        Label_TelefonoCliente2.setText("Deudada total");

        label_Deuda.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        label_Deuda.setForeground(new java.awt.Color(70, 106, 124));
        label_Deuda.setText("$0.00");

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(label_SuCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextField_Pagos, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Label_ApellidoCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_ImportesVentas, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                    .addComponent(CheckBox_Creditos)
                    .addComponent(label_Pago)
                    .addComponent(label_Cambio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Label_TelefonoCliente2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_Deuda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CheckBox_Creditos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_Pago)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_Pagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_ApellidoCliente1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_ImportesVentas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_SuCambio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_Cambio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_TelefonoCliente2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_Deuda)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Ventas", jPanel36);

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));
        jPanel37.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Label_ApellidoCliente2.setForeground(new java.awt.Color(70, 106, 124));
        Label_ApellidoCliente2.setText("Ingreso inicial");

        labelVT_IngresoIni.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        labelVT_IngresoIni.setForeground(new java.awt.Color(70, 106, 124));
        labelVT_IngresoIni.setText("$0.00");
        labelVT_IngresoIni.setToolTipText("");

        label_SuCambio1.setForeground(new java.awt.Color(70, 106, 124));
        label_SuCambio1.setText("Ingreso de ventas");

        labelVT_IngresosVt.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        labelVT_IngresosVt.setForeground(new java.awt.Color(70, 106, 124));
        labelVT_IngresosVt.setText("$0.00");

        Label_TelefonoCliente3.setForeground(new java.awt.Color(70, 106, 124));
        Label_TelefonoCliente3.setText("Ingreso total");

        labelVT_IngresoTotal.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        labelVT_IngresoTotal.setForeground(new java.awt.Color(70, 106, 124));
        labelVT_IngresoTotal.setText("$0.00");

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(label_SuCambio1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Label_ApellidoCliente2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelVT_IngresoIni, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelVT_IngresosVt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Label_TelefonoCliente3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelVT_IngresoTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label_ApellidoCliente2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelVT_IngresoIni)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_SuCambio1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelVT_IngresosVt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_TelefonoCliente3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelVT_IngresoTotal)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Ingresos", jPanel37);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addContainerGap(150, Short.MAX_VALUE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(button_Cobrar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(ButtonVT_Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2)
                .addGap(18, 18, 18)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_Cobrar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonVT_Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        TableVT_Cliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        TableVT_Cliente.setRowHeight(20);
        TableVT_Cliente.setSelectionBackground(new java.awt.Color(102, 204, 255));
        TableVT_Cliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableVT_ClienteMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(TableVT_Cliente);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(70, 106, 124));
        jLabel20.setText("Cliente");

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Clientes.png"))); // NOI18N

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel24.setText("©PDHN");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(70, 106, 124));
        jLabel25.setText("Buscar");

        TextFieldVT_BuscarCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldVT_BuscarClienteKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 956, Short.MAX_VALUE)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addGap(18, 18, 18)
                        .addComponent(TextFieldVT_BuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_MensajeCliente)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(261, 261, 261)
                .addComponent(jLabel24)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel25)
                        .addComponent(TextFieldVT_BuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(label_MensajeCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addGap(5, 5, 5))
        );

        Panel_ReciboVenta.setBackground(new java.awt.Color(255, 255, 255));
        Panel_ReciboVenta.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel17.setText("Abarrotes punto de venta");

        jLabel26.setText("Nombre:");

        jLabel27.setText("Deuda:");

        jLabel28.setText("Deuda anterior:");

        jLabel29.setText("Deuda actual:");

        jLabel30.setText("Ultimo pago:");

        jLabel31.setText("Fecha:");

        label_ReciboDeudaAnterior.setText("$0.00");

        label_ReciboNombre.setText("Nombre");

        label_ReciboDeuda.setText("$0.00");

        label_ReciboDeudaTotal.setText("$0.00");

        label_ReciboUltimoPago.setText("$0.00");

        label_ReciboFecha.setText("Fecha");

        javax.swing.GroupLayout Panel_ReciboVentaLayout = new javax.swing.GroupLayout(Panel_ReciboVenta);
        Panel_ReciboVenta.setLayout(Panel_ReciboVentaLayout);
        Panel_ReciboVentaLayout.setHorizontalGroup(
            Panel_ReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ReciboVentaLayout.createSequentialGroup()
                .addGroup(Panel_ReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_ReciboVentaLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel17))
                    .addGroup(Panel_ReciboVentaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(Panel_ReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(Panel_ReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_ReciboFecha)
                            .addComponent(label_ReciboUltimoPago)
                            .addComponent(label_ReciboDeudaTotal)
                            .addComponent(label_ReciboDeuda)
                            .addComponent(label_ReciboNombre)
                            .addComponent(label_ReciboDeudaAnterior))))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        Panel_ReciboVentaLayout.setVerticalGroup(
            Panel_ReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ReciboVentaLayout.createSequentialGroup()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_ReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(label_ReciboNombre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_ReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(label_ReciboDeuda))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_ReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(label_ReciboDeudaAnterior))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_ReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(label_ReciboDeudaTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_ReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(label_ReciboUltimoPago))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(Panel_ReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(label_ReciboFecha)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Panel_ReciboVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Panel_ReciboVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, 151, Short.MAX_VALUE)))
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab1", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(70, 106, 124));
        jLabel1.setText("Clientes");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(70, 106, 124));
        jLabel5.setText("Buscar");

        TextField_BuscarCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_BuscarClienteKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(204, 204, 204)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(TextField_BuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5)
                    .addComponent(TextField_BuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("")));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(70, 106, 124));
        jLabel2.setText("Configuració del cliente");
        jLabel2.setToolTipText("");

        RadioButton_IngresarCliente.setForeground(new java.awt.Color(0, 153, 51));
        RadioButton_IngresarCliente.setText("Ingresar cliente");
        RadioButton_IngresarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioButton_IngresarClienteActionPerformed(evt);
            }
        });

        RadioButton_PagosCliente.setText("Pagos de cliente");
        RadioButton_PagosCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioButton_PagosClienteActionPerformed(evt);
            }
        });

        Label_NombreCliente.setForeground(new java.awt.Color(102, 102, 102));
        Label_NombreCliente.setText("Nombre completo");
        Label_NombreCliente.setToolTipText("");

        TextField_NombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_NombreClienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_NombreClienteKeyTyped(evt);
            }
        });

        Label_ApellidoCliente.setForeground(new java.awt.Color(102, 102, 102));
        Label_ApellidoCliente.setText("Apellido");

        TextField_ApellidioCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_ApellidioClienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_ApellidioClienteKeyTyped(evt);
            }
        });

        Label_DireccionCliente.setForeground(new java.awt.Color(102, 102, 102));
        Label_DireccionCliente.setText("Dirección");
        Label_DireccionCliente.setToolTipText("");

        TextField_DireccioCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_DireccioClienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_DireccioClienteKeyTyped(evt);
            }
        });

        Label_TelefonoCliente.setForeground(new java.awt.Color(102, 102, 102));
        Label_TelefonoCliente.setText("Telefono");

        TextField_TelefonoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_TelefonoClienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_TelefonoClienteKeyTyped(evt);
            }
        });

        Label_PagoCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Label_PagoCliente.setForeground(new java.awt.Color(102, 102, 102));
        Label_PagoCliente.setText("Pagos de deudas");

        TextField_PagosCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_PagosClienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_PagosClienteKeyTyped(evt);
            }
        });

        Button_GuardarCliente.setBackground(new java.awt.Color(0, 153, 153));
        Button_GuardarCliente.setForeground(new java.awt.Color(255, 255, 255));
        Button_GuardarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
        Button_GuardarCliente.setBorder(null);
        Button_GuardarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_GuardarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_GuardarClienteActionPerformed(evt);
            }
        });

        Button_EliminarCLT.setBackground(new java.awt.Color(0, 153, 153));
        Button_EliminarCLT.setForeground(new java.awt.Color(255, 255, 255));
        Button_EliminarCLT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/eliminar.png"))); // NOI18N
        Button_EliminarCLT.setBorder(null);
        Button_EliminarCLT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_EliminarCLT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_EliminarCLTActionPerformed(evt);
            }
        });

        Button_CancelarCLT.setBackground(new java.awt.Color(0, 153, 153));
        Button_CancelarCLT.setForeground(new java.awt.Color(255, 255, 255));
        Button_CancelarCLT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
        Button_CancelarCLT.setBorder(null);
        Button_CancelarCLT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_CancelarCLT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_CancelarCLTActionPerformed(evt);
            }
        });

        Label_IdCliente.setText("ID");
        Label_IdCliente.setToolTipText("");

        TextField_IdCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_IdClienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_IdClienteKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(RadioButton_IngresarCliente)
                                .addGap(18, 18, 18)
                                .addComponent(RadioButton_PagosCliente))
                            .addComponent(jLabel2))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(Button_GuardarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Button_EliminarCLT, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Button_CancelarCLT, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(Label_NombreCliente)
                            .addComponent(TextField_NombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_ApellidoCliente)
                            .addComponent(Label_DireccionCliente)
                            .addComponent(TextField_DireccioCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_TelefonoCliente)
                            .addComponent(TextField_TelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_PagoCliente)
                            .addComponent(TextField_PagosCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TextField_ApellidioCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_IdCliente)
                            .addComponent(TextField_IdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RadioButton_IngresarCliente)
                    .addComponent(RadioButton_PagosCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Label_IdCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_IdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_NombreCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_NombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_ApellidoCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_ApellidioCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_DireccionCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_DireccioCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_TelefonoCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_TelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_PagoCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_PagosCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Button_GuardarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_EliminarCLT, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_CancelarCLT, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_Clientes.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Table_Clientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_Clientes.setRowHeight(20);
        Table_Clientes.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_Clientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_ClientesMouseClicked(evt);
            }
        });
        Table_Clientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table_ClientesKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(Table_Clientes);

        Button_AnteriorCLT.setBackground(new java.awt.Color(0, 153, 153));
        Button_AnteriorCLT.setForeground(new java.awt.Color(255, 255, 255));
        Button_AnteriorCLT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
        Button_AnteriorCLT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_AnteriorCLT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_AnteriorCLTActionPerformed(evt);
            }
        });

        Button_SiguienteCLT.setBackground(new java.awt.Color(0, 153, 153));
        Button_SiguienteCLT.setForeground(new java.awt.Color(255, 255, 255));
        Button_SiguienteCLT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
        Button_SiguienteCLT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_SiguienteCLT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_SiguienteCLTActionPerformed(evt);
            }
        });

        Button_PrimeroCLT.setBackground(new java.awt.Color(0, 153, 153));
        Button_PrimeroCLT.setForeground(new java.awt.Color(255, 255, 255));
        Button_PrimeroCLT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
        Button_PrimeroCLT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_PrimeroCLT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_PrimeroCLTActionPerformed(evt);
            }
        });

        Button_UltimoCLT.setBackground(new java.awt.Color(0, 153, 153));
        Button_UltimoCLT.setForeground(new java.awt.Color(255, 255, 255));
        Button_UltimoCLT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
        Button_UltimoCLT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_UltimoCLT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_UltimoCLTActionPerformed(evt);
            }
        });

        Label_PaginasClientes.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_PaginasClientes.setForeground(new java.awt.Color(70, 106, 124));
        Label_PaginasClientes.setText("Page");

        Button_FacturaCliente.setBackground(new java.awt.Color(0, 153, 153));
        Button_FacturaCliente.setForeground(new java.awt.Color(255, 255, 255));
        Button_FacturaCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Imprimir.png"))); // NOI18N
        Button_FacturaCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_FacturaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_FacturaClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2)
                .addGap(10, 10, 10))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(302, 302, 302)
                .addComponent(Label_PaginasClientes)
                .addContainerGap(859, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(Button_PrimeroCLT, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Button_AnteriorCLT, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Button_SiguienteCLT, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Button_UltimoCLT, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Button_FacturaCliente)
                .addGap(56, 56, 56))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_PaginasClientes)
                .addGap(4, 4, 4)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Button_FacturaCliente)
                        .addComponent(Button_UltimoCLT))
                    .addComponent(Button_SiguienteCLT, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Button_AnteriorCLT, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Button_PrimeroCLT, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_ReportesCLT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_ReportesCLT.setRowHeight(20);
        Table_ReportesCLT.setSelectionBackground(new java.awt.Color(102, 204, 255));
        jScrollPane3.setViewportView(Table_ReportesCLT);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(70, 106, 124));
        jLabel7.setText("Reportes");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Clientes.png"))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("©PDHN");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(263, 263, 263)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(261, 261, 261)
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(5, 5, 5))
        );

        Panel_ReciboCliente.setBackground(new java.awt.Color(255, 255, 255));
        Panel_ReciboCliente.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel77.setText("Recibo");

        jLabel78.setText("Nombre:");

        jLabel79.setText("Apellido:");

        jLabel80.setText("Deuda actual:");

        jLabel81.setText("Ultimo pago:");

        jLabel83.setText("Fecha:");

        Label_SaldoActualCLT.setText("$0.00");

        Label_NombreCLT.setText("Nombre");

        Label_ApellidoCLT.setText("Apellido");

        Label_UltimoPagoCLT.setText("$0.00");

        Label_FechaPagoCLT.setText("Fecha");

        javax.swing.GroupLayout Panel_ReciboClienteLayout = new javax.swing.GroupLayout(Panel_ReciboCliente);
        Panel_ReciboCliente.setLayout(Panel_ReciboClienteLayout);
        Panel_ReciboClienteLayout.setHorizontalGroup(
            Panel_ReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ReciboClienteLayout.createSequentialGroup()
                .addGroup(Panel_ReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_ReciboClienteLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(Panel_ReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel78)
                            .addComponent(jLabel79)
                            .addComponent(jLabel80)
                            .addComponent(jLabel81)
                            .addComponent(jLabel83))
                        .addGap(15, 15, 15)
                        .addGroup(Panel_ReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Label_NombreCLT)
                            .addComponent(Label_ApellidoCLT, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_SaldoActualCLT)
                            .addComponent(Label_UltimoPagoCLT)
                            .addComponent(Label_FechaPagoCLT)))
                    .addGroup(Panel_ReciboClienteLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(jLabel77)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Panel_ReciboClienteLayout.setVerticalGroup(
            Panel_ReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ReciboClienteLayout.createSequentialGroup()
                .addComponent(jLabel77)
                .addGap(18, 18, 18)
                .addGroup(Panel_ReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel78)
                    .addComponent(Label_NombreCLT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_ReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel79)
                    .addComponent(Label_ApellidoCLT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Panel_ReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(Label_SaldoActualCLT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Panel_ReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81)
                    .addComponent(Label_UltimoPagoCLT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Panel_ReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel83)
                    .addComponent(Label_FechaPagoCLT)))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Panel_ReciboCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Panel_ReciboCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab2", jPanel2);

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel28.setForeground(new java.awt.Color(255, 255, 255));

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(70, 106, 124));
        jLabel3.setText("Proveedores");

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(70, 106, 124));
        jLabel38.setText("Buscar");

        TextFieldPd_Buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldPd_BuscarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(204, 204, 204)
                .addComponent(jLabel38)
                .addGap(18, 18, 18)
                .addComponent(TextFieldPd_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel38)
                    .addComponent(TextFieldPd_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));
        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_Proveedor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Table_Proveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_Proveedor.setRowHeight(20);
        Table_Proveedor.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_Proveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_ProveedorMouseClicked(evt);
            }
        });
        Table_Proveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table_ProveedorKeyReleased(evt);
            }
        });
        jScrollPane11.setViewportView(Table_Proveedor);

        ButtonPd_Anterior.setBackground(new java.awt.Color(0, 153, 153));
        ButtonPd_Anterior.setForeground(new java.awt.Color(255, 255, 255));
        ButtonPd_Anterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
        ButtonPd_Anterior.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonPd_Anterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPd_AnteriorActionPerformed(evt);
            }
        });

        ButtonPd_Siguiente.setBackground(new java.awt.Color(0, 153, 153));
        ButtonPd_Siguiente.setForeground(new java.awt.Color(255, 255, 255));
        ButtonPd_Siguiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
        ButtonPd_Siguiente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonPd_Siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPd_SiguienteActionPerformed(evt);
            }
        });

        ButtonPd_Primero.setBackground(new java.awt.Color(0, 153, 153));
        ButtonPd_Primero.setForeground(new java.awt.Color(255, 255, 255));
        ButtonPd_Primero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
        ButtonPd_Primero.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonPd_Primero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPd_PrimeroActionPerformed(evt);
            }
        });

        ButtonPd_Ultimo.setBackground(new java.awt.Color(0, 153, 153));
        ButtonPd_Ultimo.setForeground(new java.awt.Color(255, 255, 255));
        ButtonPd_Ultimo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
        ButtonPd_Ultimo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonPd_Ultimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPd_UltimoActionPerformed(evt);
            }
        });

        LabelPd_Paginas.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        LabelPd_Paginas.setForeground(new java.awt.Color(70, 106, 124));
        LabelPd_Paginas.setText("Page");

        ButtonPd_Factura.setBackground(new java.awt.Color(0, 153, 153));
        ButtonPd_Factura.setForeground(new java.awt.Color(255, 255, 255));
        ButtonPd_Factura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Imprimir.png"))); // NOI18N
        ButtonPd_Factura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonPd_Factura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPd_FacturaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane11)
                .addGap(10, 10, 10))
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(302, 302, 302)
                .addComponent(LabelPd_Paginas)
                .addContainerGap(812, Short.MAX_VALUE))
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(ButtonPd_Primero, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButtonPd_Anterior, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButtonPd_Siguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButtonPd_Ultimo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ButtonPd_Factura)
                .addGap(49, 49, 49))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelPd_Paginas)
                .addGap(4, 4, 4)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(ButtonPd_Factura)
                        .addComponent(ButtonPd_Ultimo))
                    .addComponent(ButtonPd_Siguiente, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ButtonPd_Anterior, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ButtonPd_Primero, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));
        jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("")));

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(70, 106, 124));
        jLabel39.setText("Configuració del proveedores");
        jLabel39.setToolTipText("");

        RadioButtonPd_Ingresar.setForeground(new java.awt.Color(0, 153, 51));
        RadioButtonPd_Ingresar.setText("Ingresar proveeedor");
        RadioButtonPd_Ingresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioButtonPd_IngresarActionPerformed(evt);
            }
        });

        RadioButtonPd_Pagos.setText("Pagos de proveedor");
        RadioButtonPd_Pagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioButtonPd_PagosActionPerformed(evt);
            }
        });

        LabelPd_Proveedor.setForeground(new java.awt.Color(102, 102, 102));
        LabelPd_Proveedor.setText("Proveedor");
        LabelPd_Proveedor.setToolTipText("");

        TextFieldPd_Proveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldPd_ProveedorKeyReleased(evt);
            }
        });

        LabelPd_Email.setForeground(new java.awt.Color(102, 102, 102));
        LabelPd_Email.setText("Email");
        LabelPd_Email.setToolTipText("");

        TextFieldPd_Email.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldPd_EmailKeyReleased(evt);
            }
        });

        LabelPd_Telefono.setForeground(new java.awt.Color(102, 102, 102));
        LabelPd_Telefono.setText("Telefono");

        TextFieldPd_Telefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldPd_TelefonoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextFieldPd_TelefonoKeyTyped(evt);
            }
        });

        LabelPd_Pagos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        LabelPd_Pagos.setForeground(new java.awt.Color(102, 102, 102));
        LabelPd_Pagos.setText("Pagos de deudas");

        TextFieldPd_Pagos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldPd_PagosKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextFieldPd_PagosKeyTyped(evt);
            }
        });

        Button_GuardarProveedor.setBackground(new java.awt.Color(0, 153, 153));
        Button_GuardarProveedor.setForeground(new java.awt.Color(255, 255, 255));
        Button_GuardarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
        Button_GuardarProveedor.setBorder(null);
        Button_GuardarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_GuardarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_GuardarProveedorActionPerformed(evt);
            }
        });

        ButtonPd_Eliminar.setBackground(new java.awt.Color(0, 153, 153));
        ButtonPd_Eliminar.setForeground(new java.awt.Color(255, 255, 255));
        ButtonPd_Eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/eliminar.png"))); // NOI18N
        ButtonPd_Eliminar.setBorder(null);
        ButtonPd_Eliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonPd_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPd_EliminarActionPerformed(evt);
            }
        });

        ButtonPd_Cancelar.setBackground(new java.awt.Color(0, 153, 153));
        ButtonPd_Cancelar.setForeground(new java.awt.Color(255, 255, 255));
        ButtonPd_Cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
        ButtonPd_Cancelar.setBorder(null);
        ButtonPd_Cancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonPd_Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPd_CancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addComponent(RadioButtonPd_Ingresar)
                                .addGap(18, 18, 18)
                                .addComponent(RadioButtonPd_Pagos))
                            .addComponent(jLabel39))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addComponent(Button_GuardarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonPd_Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonPd_Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(LabelPd_Proveedor)
                            .addComponent(TextFieldPd_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelPd_Email)
                            .addComponent(TextFieldPd_Email, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelPd_Telefono)
                            .addComponent(TextFieldPd_Telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelPd_Pagos)
                            .addComponent(TextFieldPd_Pagos, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RadioButtonPd_Ingresar)
                    .addComponent(RadioButtonPd_Pagos))
                .addGap(18, 18, 18)
                .addComponent(LabelPd_Proveedor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextFieldPd_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelPd_Email)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextFieldPd_Email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelPd_Telefono)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextFieldPd_Telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelPd_Pagos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextFieldPd_Pagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 169, Short.MAX_VALUE)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Button_GuardarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonPd_Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonPd_Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));
        jPanel32.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        TablePd_Reportes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        TablePd_Reportes.setRowHeight(20);
        TablePd_Reportes.setSelectionBackground(new java.awt.Color(102, 204, 255));
        jScrollPane12.setViewportView(TablePd_Reportes);

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(70, 106, 124));
        jLabel40.setText("Reportes");

        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Clientes.png"))); // NOI18N

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel42.setText("©PDHN");

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane12))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(263, 263, 263)
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel41)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGap(261, 261, 261)
                .addComponent(jLabel42)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel40)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel42)
                .addGap(5, 5, 5))
        );

        PanelPd_Recibo.setBackground(new java.awt.Color(255, 255, 255));
        PanelPd_Recibo.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel82.setText("Recibo");

        jLabel84.setText("Nombre:");

        jLabel86.setText("Deuda actual:");

        jLabel87.setText("Ultimo pago:");

        jLabel88.setText("Fecha:");

        LabelPd_SaldoActual.setText("$0.00");

        Label_ProveedorRB.setText("Nombre");

        LabelPd_UltimoPago.setText("$0.00");

        LabelPd_FechaPago.setText("Fecha");

        javax.swing.GroupLayout PanelPd_ReciboLayout = new javax.swing.GroupLayout(PanelPd_Recibo);
        PanelPd_Recibo.setLayout(PanelPd_ReciboLayout);
        PanelPd_ReciboLayout.setHorizontalGroup(
            PanelPd_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPd_ReciboLayout.createSequentialGroup()
                .addGroup(PanelPd_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPd_ReciboLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(PanelPd_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel84)
                            .addComponent(jLabel86)
                            .addComponent(jLabel87)
                            .addComponent(jLabel88))
                        .addGap(15, 15, 15)
                        .addGroup(PanelPd_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Label_ProveedorRB)
                            .addComponent(LabelPd_SaldoActual)
                            .addComponent(LabelPd_UltimoPago)
                            .addComponent(LabelPd_FechaPago)))
                    .addGroup(PanelPd_ReciboLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(jLabel82)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        PanelPd_ReciboLayout.setVerticalGroup(
            PanelPd_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPd_ReciboLayout.createSequentialGroup()
                .addComponent(jLabel82)
                .addGap(18, 18, 18)
                .addGroup(PanelPd_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel84)
                    .addComponent(Label_ProveedorRB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelPd_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel86)
                    .addComponent(LabelPd_SaldoActual))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelPd_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87)
                    .addComponent(LabelPd_UltimoPago))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelPd_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel88)
                    .addComponent(LabelPd_FechaPago))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(PanelPd_Recibo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PanelPd_Recibo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab3", jPanel28);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("")));
        jPanel11.setPreferredSize(new java.awt.Dimension(275, 498));
        jPanel11.setRequestFocusEnabled(false);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(70, 106, 124));
        jLabel11.setText("Llene la información del nuevo producto");
        jLabel11.setToolTipText("");

        LabelPT_Descripcion.setForeground(new java.awt.Color(70, 106, 124));
        LabelPT_Descripcion.setText("Descripción");

        TextFieldPT_Descripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldPT_DescripcionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextFieldPT_DescripcionKeyTyped(evt);
            }
        });

        LabelPT_PrecioVenta.setForeground(new java.awt.Color(70, 106, 124));
        LabelPT_PrecioVenta.setText("Precio venta");
        LabelPT_PrecioVenta.setToolTipText("");

        TextFieldPT_PrecioVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldPT_PrecioVentaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextFieldPT_PrecioVentaKeyTyped(evt);
            }
        });

        Label_DepartamentoPDT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Label_DepartamentoPDT.setForeground(new java.awt.Color(70, 106, 124));
        Label_DepartamentoPDT.setText("Departamento");

        ButtonPT_Guardar.setBackground(new java.awt.Color(0, 153, 153));
        ButtonPT_Guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
        ButtonPT_Guardar.setBorder(null);
        ButtonPT_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPT_GuardarActionPerformed(evt);
            }
        });

        ButtonPT_Cancelar.setBackground(new java.awt.Color(0, 153, 153));
        ButtonPT_Cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
        ButtonPT_Cancelar.setBorder(null);
        ButtonPT_Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPT_CancelarActionPerformed(evt);
            }
        });

        Label_CategoriaPDT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Label_CategoriaPDT.setForeground(new java.awt.Color(70, 106, 124));
        Label_CategoriaPDT.setText("Categoria");

        ComboBox_Departamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBox_DepartamentoActionPerformed(evt);
            }
        });

        PanelPT_CodigoBarra.setBackground(new java.awt.Color(255, 255, 255));
        PanelPT_CodigoBarra.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        PanelPT_CodigoBarra.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        PanelPT_CodigoBarra.add(LabelPT_CodigoBarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 200, 90));

        LabelPT_Producto.setForeground(new java.awt.Color(70, 106, 124));
        LabelPT_Producto.setText("Producto");
        PanelPT_CodigoBarra.add(LabelPT_Producto, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, -1));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelPT_Descripcion)
                    .addComponent(TextFieldPT_Descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelPT_PrecioVenta)
                    .addComponent(Label_DepartamentoPDT)
                    .addComponent(Label_CategoriaPDT)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(ComboBox_Categoria, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ComboBox_Departamento, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TextFieldPT_PrecioVenta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(PanelPT_CodigoBarra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(54, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(ButtonPT_Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ButtonPT_Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(4, 4, 4)
                .addComponent(PanelPT_CodigoBarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelPT_Descripcion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextFieldPT_Descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelPT_PrecioVenta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextFieldPT_PrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_DepartamentoPDT)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ComboBox_Departamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_CategoriaPDT)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ComboBox_Categoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ButtonPT_Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonPT_Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel12.setPreferredSize(new java.awt.Dimension(475, 208));

        TablePT_Compras.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        TablePT_Compras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        TablePT_Compras.setRowHeight(20);
        TablePT_Compras.setSelectionBackground(new java.awt.Color(102, 204, 255));
        TablePT_Compras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablePT_ComprasMouseClicked(evt);
            }
        });
        TablePT_Compras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TablePT_ComprasKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(TablePT_Compras);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        TablePT_Productos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        TablePT_Productos.setRowHeight(20);
        TablePT_Productos.setSelectionBackground(new java.awt.Color(102, 204, 255));
        TablePT_Productos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablePT_ProductosMouseClicked(evt);
            }
        });
        TablePT_Productos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TablePT_ProductosKeyReleased(evt);
            }
        });
        jScrollPane5.setViewportView(TablePT_Productos);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("©PDHN");

        Label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label.setForeground(new java.awt.Color(70, 106, 124));
        Label.setText("Productos ");

        ButtonPT_Primero.setBackground(new java.awt.Color(0, 153, 153));
        ButtonPT_Primero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
        ButtonPT_Primero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPT_PrimeroActionPerformed(evt);
            }
        });

        ButtonPT_Anterior.setBackground(new java.awt.Color(0, 153, 153));
        ButtonPT_Anterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
        ButtonPT_Anterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPT_AnteriorActionPerformed(evt);
            }
        });

        ButtonPT_Siguiente.setBackground(new java.awt.Color(0, 153, 153));
        ButtonPT_Siguiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
        ButtonPT_Siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPT_SiguienteActionPerformed(evt);
            }
        });

        ButtonPT_Ultimo.setBackground(new java.awt.Color(0, 153, 153));
        ButtonPT_Ultimo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
        ButtonPT_Ultimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPT_UltimoActionPerformed(evt);
            }
        });

        LabelPT_Paginas.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        LabelPT_Paginas.setForeground(new java.awt.Color(70, 106, 124));
        LabelPT_Paginas.setText("Page");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(175, 175, 175)
                                .addComponent(jLabel14))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(ButtonPT_Primero, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonPT_Anterior, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonPT_Siguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonPT_Ultimo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(208, 208, 208)
                                .addComponent(LabelPT_Paginas)))
                        .addGap(0, 637, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(327, 327, 327)
                .addComponent(Label)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelPT_Paginas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonPT_Anterior)
                    .addComponent(ButtonPT_Primero)
                    .addComponent(ButtonPT_Siguiente)
                    .addComponent(ButtonPT_Ultimo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addContainerGap())
        );

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(70, 106, 124));
        jLabel4.setText("Productos");

        Label1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label1.setForeground(new java.awt.Color(70, 106, 124));
        Label1.setText("Productos comprados");

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(70, 106, 124));
        jLabel6.setText("Buscar");

        TextField_ComprasProductos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_ComprasProductosKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(200, 200, 200)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(TextField_ComprasProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(Label1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(12, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(Label1)
                    .addComponent(TextField_ComprasProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 1195, Short.MAX_VALUE)))
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab4", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("")));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(70, 106, 124));
        jLabel13.setText("Configuració del cliente");
        jLabel13.setToolTipText("");

        RadioButton_Dpt.setForeground(new java.awt.Color(0, 153, 51));
        RadioButton_Dpt.setText("Departamento");
        RadioButton_Dpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioButton_DptActionPerformed(evt);
            }
        });

        RadioButton_Cat.setText("Categoria");
        RadioButton_Cat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioButton_CatActionPerformed(evt);
            }
        });

        Button_GuardarCatDpt.setBackground(new java.awt.Color(0, 153, 153));
        Button_GuardarCatDpt.setForeground(new java.awt.Color(255, 255, 255));
        Button_GuardarCatDpt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
        Button_GuardarCatDpt.setBorder(null);
        Button_GuardarCatDpt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_GuardarCatDpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_GuardarCatDptActionPerformed(evt);
            }
        });

        Button_EliminarCatDpt.setBackground(new java.awt.Color(0, 153, 153));
        Button_EliminarCatDpt.setForeground(new java.awt.Color(255, 255, 255));
        Button_EliminarCatDpt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/eliminar.png"))); // NOI18N
        Button_EliminarCatDpt.setBorder(null);
        Button_EliminarCatDpt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_EliminarCatDpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_EliminarCatDptActionPerformed(evt);
            }
        });

        Button_CancelarCatDpt.setBackground(new java.awt.Color(0, 153, 153));
        Button_CancelarCatDpt.setForeground(new java.awt.Color(255, 255, 255));
        Button_CancelarCatDpt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
        Button_CancelarCatDpt.setAutoscrolls(true);
        Button_CancelarCatDpt.setBorder(null);
        Button_CancelarCatDpt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_CancelarCatDpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_CancelarCatDptActionPerformed(evt);
            }
        });

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Label_Cat.setText("Categoria");
        Label_Cat.setToolTipText("");

        TextField_Categoria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_CategoriaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_CategoriaKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Label_Cat)
                    .addComponent(TextField_Categoria, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label_Cat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_Categoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Label_Dpt.setText("Departamento");
        Label_Dpt.setToolTipText("");

        TextField_Departamento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_DepartamentoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_DepartamentoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Label_Dpt)
                    .addComponent(TextField_Departamento, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label_Dpt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_Departamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(Button_GuardarCatDpt, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Button_EliminarCatDpt, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Button_CancelarCatDpt, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(RadioButton_Dpt)
                                .addGap(18, 18, 18)
                                .addComponent(RadioButton_Cat))
                            .addComponent(jLabel13))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RadioButton_Dpt)
                    .addComponent(RadioButton_Cat))
                .addGap(18, 18, 18)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Button_GuardarCatDpt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_EliminarCatDpt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_CancelarCatDpt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_Dpt.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Table_Dpt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_Dpt.setRowHeight(20);
        Table_Dpt.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_Dpt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_DptMouseClicked(evt);
            }
        });
        Table_Dpt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table_DptKeyReleased(evt);
            }
        });
        jScrollPane6.setViewportView(Table_Dpt);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6)
                .addContainerGap())
        );

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(70, 106, 124));
        jLabel22.setText("Dpt and Cat");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(70, 106, 124));
        jLabel23.setText("Buscar");

        TextField_BuscarDpt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_BuscarDptKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addGap(204, 204, 204)
                .addComponent(jLabel23)
                .addGap(18, 18, 18)
                .addComponent(TextField_BuscarDpt, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(876, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23)
                    .addComponent(TextField_BuscarDpt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_Cat.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Table_Cat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_Cat.setRowHeight(20);
        Table_Cat.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_Cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_CatMouseClicked(evt);
            }
        });
        Table_Cat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table_CatKeyReleased(evt);
            }
        });
        jScrollPane8.setViewportView(Table_Cat);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        jTabbedPane1.addTab("tab5", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("")));

        Button_GuardarCliente1.setBackground(new java.awt.Color(0, 153, 153));
        Button_GuardarCliente1.setForeground(new java.awt.Color(255, 255, 255));
        Button_GuardarCliente1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
        Button_GuardarCliente1.setBorder(null);
        Button_GuardarCliente1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_GuardarCliente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_GuardarCliente1ActionPerformed(evt);
            }
        });

        ButtonCP_Eliminar.setBackground(new java.awt.Color(0, 153, 153));
        ButtonCP_Eliminar.setForeground(new java.awt.Color(255, 255, 255));
        ButtonCP_Eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/eliminar.png"))); // NOI18N
        ButtonCP_Eliminar.setBorder(null);
        ButtonCP_Eliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonCP_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCP_EliminarActionPerformed(evt);
            }
        });

        Button_CancelarCompras.setBackground(new java.awt.Color(0, 153, 153));
        Button_CancelarCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
        Button_CancelarCompras.setBorder(null);
        Button_CancelarCompras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        TabbedPane_Compras.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        TabbedPane_Compras.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabbedPane_ComprasStateChanged(evt);
            }
        });

        jPanel33.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(70, 106, 124));
        jLabel10.setText("Llene la información del producto");
        jLabel10.setToolTipText("");

        LabelCP_Descripcion.setText("Descripción");
        LabelCP_Descripcion.setToolTipText("");

        TextFieldCP_Descripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldCP_DescripcionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextFieldCP_DescripcionKeyTyped(evt);
            }
        });

        LabelCP_Cantidad.setText("Cantidad");
        LabelCP_Cantidad.setToolTipText("");

        TextFieldCP_Cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldCP_CantidadKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextFieldCP_CantidadKeyTyped(evt);
            }
        });

        LabelCP_Precio.setText("Precio de compra");

        TextFieldCP_Precio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldCP_PrecioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextFieldCP_PrecioKeyTyped(evt);
            }
        });

        LabelCP_ImporteCompra.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        LabelCP_ImporteCompra.setText("Importe");

        LabelCP_Importe1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        LabelCP_Importe1.setForeground(new java.awt.Color(70, 106, 124));
        LabelCP_Importe1.setText("$0.00");

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(LabelCP_Descripcion)
                    .addComponent(TextFieldCP_Descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelCP_Cantidad)
                    .addComponent(TextFieldCP_Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextFieldCP_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelCP_ImporteCompra)
                    .addComponent(LabelCP_Precio)
                    .addComponent(LabelCP_Importe1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(LabelCP_Descripcion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextFieldCP_Descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelCP_Cantidad)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextFieldCP_Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelCP_Precio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextFieldCP_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LabelCP_ImporteCompra)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelCP_Importe1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TabbedPane_Compras.addTab("Productos", jPanel33);

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));

        jLabel50.setBackground(new java.awt.Color(70, 106, 124));
        jLabel50.setForeground(new java.awt.Color(70, 106, 124));
        jLabel50.setText("En caja");

        LabelCP_Encaja.setBackground(new java.awt.Color(70, 106, 124));
        LabelCP_Encaja.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        LabelCP_Encaja.setForeground(new java.awt.Color(70, 106, 124));
        LabelCP_Encaja.setText("$0.00");

        jLabel52.setBackground(new java.awt.Color(70, 106, 124));
        jLabel52.setForeground(new java.awt.Color(70, 106, 124));
        jLabel52.setText("Monto a pagar");

        LabelCP_MontoPagar.setBackground(new java.awt.Color(70, 106, 124));
        LabelCP_MontoPagar.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        LabelCP_MontoPagar.setForeground(new java.awt.Color(70, 106, 124));
        LabelCP_MontoPagar.setText("$0.00");

        LabelCP_Pago.setBackground(new java.awt.Color(70, 106, 124));
        LabelCP_Pago.setForeground(new java.awt.Color(70, 106, 124));
        LabelCP_Pago.setText("Monto a pagar");

        TextFieldCP_Pagos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldCP_PagosKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextFieldCP_PagosKeyTyped(evt);
            }
        });

        CheckBoxCP_Deuda.setBackground(new java.awt.Color(70, 106, 124));
        CheckBoxCP_Deuda.setForeground(new java.awt.Color(70, 106, 124));
        CheckBoxCP_Deuda.setText("Credito");
        CheckBoxCP_Deuda.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                CheckBoxCP_DeudaStateChanged(evt);
            }
        });

        jLabel53.setBackground(new java.awt.Color(70, 106, 124));
        jLabel53.setForeground(new java.awt.Color(70, 106, 124));
        jLabel53.setText("Deuda");

        LabelCP_Deudas.setBackground(new java.awt.Color(70, 106, 124));
        LabelCP_Deudas.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        LabelCP_Deudas.setForeground(new java.awt.Color(70, 106, 124));
        LabelCP_Deudas.setText("$0.00");

        PanelCP_Recibo.setBackground(new java.awt.Color(255, 255, 255));
        PanelCP_Recibo.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel51.setText("Sistema punto de venta");

        jLabel54.setText("Proveedor:");

        LabelCP_ProveedorR.setText("Nombre");

        LabelCP_TotalPagar.setText("$0.00");

        jLabel55.setText("Total a pagar:");

        LabelCP_Deuda.setText("$0.00");

        jLabel56.setText("Deuda:");

        LabelCP_Saldo.setText("$0.00");

        jLabel57.setText("Saldo");

        jLabel58.setText("Fecha:");

        LabelCP_Fecha.setText("--.--.--");

        javax.swing.GroupLayout PanelCP_ReciboLayout = new javax.swing.GroupLayout(PanelCP_Recibo);
        PanelCP_Recibo.setLayout(PanelCP_ReciboLayout);
        PanelCP_ReciboLayout.setHorizontalGroup(
            PanelCP_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCP_ReciboLayout.createSequentialGroup()
                .addGroup(PanelCP_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelCP_ReciboLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel51))
                    .addGroup(PanelCP_ReciboLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(PanelCP_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel55)
                            .addComponent(jLabel54)
                            .addComponent(jLabel56)
                            .addComponent(jLabel57)
                            .addComponent(jLabel58))
                        .addGap(22, 22, 22)
                        .addGroup(PanelCP_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelCP_Deuda)
                            .addComponent(LabelCP_ProveedorR)
                            .addComponent(LabelCP_TotalPagar)
                            .addComponent(LabelCP_Saldo)
                            .addComponent(LabelCP_Fecha))))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        PanelCP_ReciboLayout.setVerticalGroup(
            PanelCP_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCP_ReciboLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCP_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(LabelCP_ProveedorR))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCP_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(LabelCP_TotalPagar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCP_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(LabelCP_Deuda))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCP_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(LabelCP_Saldo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCP_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(LabelCP_Fecha))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(TextFieldCP_Pagos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(CheckBoxCP_Deuda))
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelCP_Encaja)
                            .addComponent(jLabel52)
                            .addComponent(LabelCP_MontoPagar)
                            .addComponent(LabelCP_Pago)
                            .addComponent(jLabel53)
                            .addComponent(LabelCP_Deudas))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel34Layout.createSequentialGroup()
                                .addComponent(jLabel50)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(PanelCP_Recibo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel50)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelCP_Encaja, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel52)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelCP_MontoPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelCP_Pago)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TextFieldCP_Pagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CheckBoxCP_Deuda))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel53)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelCP_Deudas, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelCP_Recibo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(36, 36, 36))
        );

        TabbedPane_Compras.addTab("Pagos", jPanel34);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(Button_GuardarCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonCP_Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Button_CancelarCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(TabbedPane_Compras)
                        .addContainerGap())))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TabbedPane_Compras, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Button_GuardarCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonCP_Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_CancelarCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_Compras.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Table_Compras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_Compras.setRowHeight(20);
        Table_Compras.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_Compras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_ComprasMouseClicked(evt);
            }
        });
        Table_Compras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table_ComprasKeyReleased(evt);
            }
        });
        jScrollPane9.setViewportView(Table_Compras);

        ButtonCP_Anterior.setBackground(new java.awt.Color(0, 153, 153));
        ButtonCP_Anterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
        ButtonCP_Anterior.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonCP_Anterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCP_AnteriorActionPerformed(evt);
            }
        });

        ButtonCP_Siguiente.setBackground(new java.awt.Color(0, 153, 153));
        ButtonCP_Siguiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
        ButtonCP_Siguiente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonCP_Siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCP_SiguienteActionPerformed(evt);
            }
        });

        ButtonCP_Primero.setBackground(new java.awt.Color(0, 153, 153));
        ButtonCP_Primero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
        ButtonCP_Primero.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonCP_Primero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCP_PrimeroActionPerformed(evt);
            }
        });

        ButtonCP_Ultimo.setBackground(new java.awt.Color(0, 153, 153));
        ButtonCP_Ultimo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
        ButtonCP_Ultimo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonCP_Ultimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCP_UltimoActionPerformed(evt);
            }
        });

        LabelCP_Paginas.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        LabelCP_Paginas.setForeground(new java.awt.Color(70, 106, 124));
        LabelCP_Paginas.setText("Page");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(ButtonCP_Primero, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonCP_Anterior, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonCP_Siguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonCP_Ultimo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(302, 302, 302)
                        .addComponent(LabelCP_Paginas)))
                .addContainerGap(647, Short.MAX_VALUE))
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9)
                .addGap(10, 10, 10))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelCP_Paginas)
                .addGap(4, 4, 4)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ButtonCP_Ultimo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ButtonCP_Siguiente, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ButtonCP_Anterior, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ButtonCP_Primero, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(70, 106, 124));
        jLabel18.setText("Compras");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(70, 106, 124));
        jLabel19.setText("Buscar");

        TextFieldCP_Buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldCP_BuscarKeyReleased(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(70, 106, 124));
        jLabel45.setText("Proveedor:");

        LabelCP_Proveedor.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        LabelCP_Proveedor.setForeground(new java.awt.Color(70, 106, 124));
        LabelCP_Proveedor.setText("proveedor");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addGap(204, 204, 204)
                .addComponent(jLabel19)
                .addGap(18, 18, 18)
                .addComponent(TextFieldCP_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110)
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelCP_Proveedor)
                .addContainerGap(542, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(TextFieldCP_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45)
                    .addComponent(LabelCP_Proveedor))
                .addContainerGap())
        );

        jPanel35.setBackground(new java.awt.Color(255, 255, 255));
        jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        TableCP_Proveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        TableCP_Proveedor.setRowHeight(20);
        TableCP_Proveedor.setSelectionBackground(new java.awt.Color(102, 204, 255));
        TableCP_Proveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableCP_ProveedorMouseClicked(evt);
            }
        });
        TableCP_Proveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TableCP_ProveedorKeyReleased(evt);
            }
        });
        jScrollPane13.setViewportView(TableCP_Proveedor);

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(70, 106, 124));
        jLabel46.setText("Porveedor");

        jLabel47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/shippingBlack.png"))); // NOI18N

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel48.setText("©PDHN");

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(70, 106, 124));
        jLabel49.setText("Buscar");

        TextFieldCP_Proveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldCP_ProveedorKeyReleased(evt);
            }
        });

        LabelCP_Importe2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        LabelCP_Importe2.setForeground(new java.awt.Color(70, 106, 124));
        LabelCP_Importe2.setText("$0.00");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addComponent(jLabel49)
                        .addGap(18, 18, 18)
                        .addComponent(TextFieldCP_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel47)
                        .addGap(64, 64, 64)
                        .addComponent(LabelCP_Importe2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGap(261, 261, 261)
                .addComponent(jLabel48)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel46)
                        .addComponent(jLabel49)
                        .addComponent(TextFieldCP_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelCP_Importe2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel48)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab6", jPanel5);

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Button_Usuarios.setBackground(new java.awt.Color(255, 153, 51));
        Button_Usuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Person.png"))); // NOI18N
        Button_Usuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_UsuariosActionPerformed(evt);
            }
        });

        jLabel33.setForeground(new java.awt.Color(70, 106, 124));
        jLabel33.setText("Usuarios");

        jPanel39.setBackground(new java.awt.Color(255, 255, 255));
        jPanel39.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(70, 106, 124));
        jLabel34.setText("Opciones");

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel39Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel34)
                .addContainerGap())
        );

        jLabel36.setForeground(new java.awt.Color(70, 106, 124));
        jLabel36.setText("Cajas");

        Button_Cajas.setBackground(new java.awt.Color(255, 153, 51));
        Button_Cajas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cajas.png"))); // NOI18N
        Button_Cajas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_CajasActionPerformed(evt);
            }
        });

        Button_Inventario.setBackground(new java.awt.Color(255, 153, 51));
        Button_Inventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Inventario.png"))); // NOI18N
        Button_Inventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_InventarioActionPerformed(evt);
            }
        });

        jLabel60.setForeground(new java.awt.Color(70, 106, 124));
        jLabel60.setText("Inventario");

        Button_Reportes.setBackground(new java.awt.Color(255, 153, 51));
        Button_Reportes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Reporte.png"))); // NOI18N
        Button_Reportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_ReportesActionPerformed(evt);
            }
        });

        jLabel75.setForeground(new java.awt.Color(70, 106, 124));
        jLabel75.setText("Reportes");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33)
                    .addComponent(Button_Usuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Button_Cajas, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addGap(26, 26, 26)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel60)
                    .addComponent(Button_Inventario, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel75)
                    .addComponent(Button_Reportes, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(1211, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(Button_Usuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel33))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Button_Cajas, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Button_Inventario, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(jLabel60)))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(Button_Reportes, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel75)))
                .addContainerGap(418, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab7", jPanel27);

        jPanel38.setBackground(new java.awt.Color(255, 255, 255));
        jPanel38.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel40.setBackground(new java.awt.Color(255, 255, 255));
        jPanel40.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(70, 106, 124));
        jLabel32.setText("Usuarios");

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(70, 106, 124));
        jLabel35.setText("Buscar");

        TextFieldUser_Buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldUser_BuscarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32)
                .addGap(204, 204, 204)
                .addComponent(jLabel35)
                .addGap(18, 18, 18)
                .addComponent(TextFieldUser_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel40Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel35)
                    .addComponent(TextFieldUser_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel41.setBackground(new java.awt.Color(255, 255, 255));
        jPanel41.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jTabbedPane3.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel43.setBackground(new java.awt.Color(255, 255, 255));
        jPanel43.setLayout(null);

        LabelUser_Nombre.setForeground(new java.awt.Color(70, 106, 124));
        LabelUser_Nombre.setText("Nombre");
        jPanel43.add(LabelUser_Nombre);
        LabelUser_Nombre.setBounds(10, 0, 160, 16);

        TextFieldUser_Nombre.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextFieldUser_Nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldUser_NombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextFieldUser_NombreKeyTyped(evt);
            }
        });
        jPanel43.add(TextFieldUser_Nombre);
        TextFieldUser_Nombre.setBounds(10, 20, 213, 30);

        LabelUser_Apellido.setForeground(new java.awt.Color(70, 106, 124));
        LabelUser_Apellido.setText("Apellido");
        jPanel43.add(LabelUser_Apellido);
        LabelUser_Apellido.setBounds(10, 50, 150, 16);

        TextFieldUser_Apellido.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextFieldUser_Apellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldUser_ApellidoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextFieldUser_ApellidoKeyTyped(evt);
            }
        });
        jPanel43.add(TextFieldUser_Apellido);
        TextFieldUser_Apellido.setBounds(10, 70, 213, 30);

        LabelUser_Telefono.setForeground(new java.awt.Color(70, 106, 124));
        LabelUser_Telefono.setText("Telefono");
        jPanel43.add(LabelUser_Telefono);
        LabelUser_Telefono.setBounds(10, 100, 160, 16);

        TextFieldUser_Telefono.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextFieldUser_Telefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldUser_TelefonoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextFieldUser_TelefonoKeyTyped(evt);
            }
        });
        jPanel43.add(TextFieldUser_Telefono);
        TextFieldUser_Telefono.setBounds(10, 120, 213, 30);

        LabelUser_Direccion.setForeground(new java.awt.Color(70, 106, 124));
        LabelUser_Direccion.setText("Direccion");
        jPanel43.add(LabelUser_Direccion);
        LabelUser_Direccion.setBounds(10, 150, 150, 16);

        TextFieldUser_Direccion.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextFieldUser_Direccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldUser_DireccionKeyReleased(evt);
            }
        });
        jPanel43.add(TextFieldUser_Direccion);
        TextFieldUser_Direccion.setBounds(10, 170, 213, 30);

        LabelUser_Email.setForeground(new java.awt.Color(70, 106, 124));
        LabelUser_Email.setText("Email");
        jPanel43.add(LabelUser_Email);
        LabelUser_Email.setBounds(10, 200, 170, 16);

        TextFieldUser_Email.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextFieldUser_Email.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldUser_EmailKeyReleased(evt);
            }
        });
        jPanel43.add(TextFieldUser_Email);
        TextFieldUser_Email.setBounds(10, 220, 213, 30);

        LabelUser_Password.setForeground(new java.awt.Color(70, 106, 124));
        LabelUser_Password.setText("Cotraseña");
        jPanel43.add(LabelUser_Password);
        LabelUser_Password.setBounds(10, 250, 160, 16);

        TextFieldUser_Password.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextFieldUser_Password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldUser_PasswordKeyReleased(evt);
            }
        });
        jPanel43.add(TextFieldUser_Password);
        TextFieldUser_Password.setBounds(10, 270, 213, 30);

        LabelUser_Usuario.setForeground(new java.awt.Color(70, 106, 124));
        LabelUser_Usuario.setText("Usuario");
        jPanel43.add(LabelUser_Usuario);
        LabelUser_Usuario.setBounds(10, 300, 170, 16);

        TextFieldUser_Usuario.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextFieldUser_Usuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldUser_UsuarioKeyReleased(evt);
            }
        });
        jPanel43.add(TextFieldUser_Usuario);
        TextFieldUser_Usuario.setBounds(10, 320, 213, 30);

        jTabbedPane3.addTab("Infoemacion", jPanel43);

        jPanel44.setBackground(new java.awt.Color(255, 255, 255));

        jPanel45.setBackground(new java.awt.Color(255, 255, 255));
        jPanel45.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel45Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelUser_Imagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelUser_Imagen, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        ButtonUser_Imagen.setBackground(new java.awt.Color(255, 102, 51));
        ButtonUser_Imagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/image.png"))); // NOI18N
        ButtonUser_Imagen.setToolTipText("");
        ButtonUser_Imagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonUser_ImagenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel44Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(ButtonUser_Imagen, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ButtonUser_Imagen)
                .addContainerGap(106, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Foto o imagen", jPanel44);

        ButtonUser_Guardar.setBackground(new java.awt.Color(0, 153, 153));
        ButtonUser_Guardar.setForeground(new java.awt.Color(255, 255, 255));
        ButtonUser_Guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
        ButtonUser_Guardar.setBorder(null);
        ButtonUser_Guardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonUser_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonUser_GuardarActionPerformed(evt);
            }
        });

        Button_CancelarCompras1.setBackground(new java.awt.Color(0, 153, 153));
        Button_CancelarCompras1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
        Button_CancelarCompras1.setBorder(null);
        Button_CancelarCompras1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_CancelarCompras1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_CancelarCompras1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addContainerGap())
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(ButtonUser_Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Button_CancelarCompras1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ButtonUser_Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_CancelarCompras1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel42.setBackground(new java.awt.Color(255, 255, 255));
        jPanel42.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_Usuario.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        Table_Usuario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_Usuario.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_Usuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_UsuarioMouseClicked(evt);
            }
        });
        Table_Usuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table_UsuarioKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(Table_Usuario);

        ButtonUser_Primero.setBackground(new java.awt.Color(0, 153, 153));
        ButtonUser_Primero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
        ButtonUser_Primero.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonUser_Primero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonUser_PrimeroActionPerformed(evt);
            }
        });

        ButtonUser_Anterior.setBackground(new java.awt.Color(0, 153, 153));
        ButtonUser_Anterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
        ButtonUser_Anterior.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonUser_Anterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonUser_AnteriorActionPerformed(evt);
            }
        });

        ButtonUser_Siguiente.setBackground(new java.awt.Color(0, 153, 153));
        ButtonUser_Siguiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
        ButtonUser_Siguiente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonUser_Siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonUser_SiguienteActionPerformed(evt);
            }
        });

        ButtonUser_Ultimo.setBackground(new java.awt.Color(0, 153, 153));
        ButtonUser_Ultimo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
        ButtonUser_Ultimo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonUser_Ultimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonUser_UltimoActionPerformed(evt);
            }
        });

        LabelUser_Paginas.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        LabelUser_Paginas.setForeground(new java.awt.Color(70, 106, 124));
        LabelUser_Paginas.setText("Page");

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1183, Short.MAX_VALUE)
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addComponent(ComboBoxUser_Roles, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(120, 120, 120)
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel42Layout.createSequentialGroup()
                                .addComponent(ButtonUser_Primero, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonUser_Anterior, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonUser_Siguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonUser_Ultimo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel42Layout.createSequentialGroup()
                                .addGap(212, 212, 212)
                                .addComponent(LabelUser_Paginas)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(ComboBoxUser_Roles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LabelUser_Paginas)
                        .addGap(4, 4, 4)
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ButtonUser_Ultimo, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ButtonUser_Siguiente, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ButtonUser_Anterior, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ButtonUser_Primero, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addComponent(jPanel41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab8", jPanel38);

        jPanel46.setBackground(new java.awt.Color(255, 255, 255));
        jPanel46.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel47.setBackground(new java.awt.Color(255, 255, 255));
        jPanel47.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(70, 106, 124));
        jLabel37.setText("Cajas");

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(70, 106, 124));
        jLabel59.setText("Buscar");

        dateChooserCombo_Cajas.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 15),
                    new java.awt.Color(187, 187, 187),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 15),
                    new java.awt.Color(187, 187, 187),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 15),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 15),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 15),
                    new java.awt.Color(187, 187, 187),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 15),
                    new java.awt.Color(187, 187, 187),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    dateChooserCombo_Cajas.setFormat(2);
    try {
        dateChooserCombo_Cajas.setDefaultPeriods(new datechooser.model.multiple.PeriodSet(new datechooser.model.multiple.Period(new java.util.GregorianCalendar(2018, 6, 25),
            new java.util.GregorianCalendar(2018, 6, 25))));
} catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
    e1.printStackTrace();
    }
    dateChooserCombo_Cajas.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
        public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
            dateChooserCombo_CajasOnSelectionChange(evt);
        }
    });

    javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
    jPanel47.setLayout(jPanel47Layout);
    jPanel47Layout.setHorizontalGroup(
        jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel47Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel37)
            .addGap(204, 204, 204)
            .addComponent(jLabel59)
            .addGap(18, 18, 18)
            .addComponent(dateChooserCombo_Cajas, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel47Layout.setVerticalGroup(
        jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel47Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(jLabel59))
                .addComponent(dateChooserCombo_Cajas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    jPanel48.setBackground(new java.awt.Color(255, 255, 255));
    jPanel48.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    TabbedPane_Caja2.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            TabbedPane_Caja2StateChanged(evt);
        }
    });

    jPanel53.setBackground(new java.awt.Color(255, 255, 255));
    jPanel53.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    TableCajas_Ingresos.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
    TableCajas_Ingresos.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    TableCajas_Ingresos.setSelectionBackground(new java.awt.Color(102, 204, 255));
    TableCajas_Ingresos.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            TableCajas_IngresosMouseClicked(evt);
        }
    });
    TableCajas_Ingresos.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TableCajas_IngresosKeyReleased(evt);
        }
    });
    jScrollPane14.setViewportView(TableCajas_Ingresos);

    javax.swing.GroupLayout jPanel53Layout = new javax.swing.GroupLayout(jPanel53);
    jPanel53.setLayout(jPanel53Layout);
    jPanel53Layout.setHorizontalGroup(
        jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel53Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 1167, Short.MAX_VALUE)
            .addContainerGap())
    );
    jPanel53Layout.setVerticalGroup(
        jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel53Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
            .addContainerGap())
    );

    TabbedPane_Caja2.addTab("Ingresos", jPanel53);

    jPanel54.setBackground(new java.awt.Color(255, 255, 255));
    jPanel54.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    Table_Cajas.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
    Table_Cajas.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    Table_Cajas.setSelectionBackground(new java.awt.Color(102, 204, 255));
    Table_Cajas.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            Table_CajasMouseClicked(evt);
        }
    });
    Table_Cajas.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            Table_CajasKeyReleased(evt);
        }
    });
    jScrollPane15.setViewportView(Table_Cajas);

    javax.swing.GroupLayout jPanel54Layout = new javax.swing.GroupLayout(jPanel54);
    jPanel54.setLayout(jPanel54Layout);
    jPanel54Layout.setHorizontalGroup(
        jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel54Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 1167, Short.MAX_VALUE)
            .addContainerGap())
    );
    jPanel54Layout.setVerticalGroup(
        jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel54Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
            .addContainerGap())
    );

    TabbedPane_Caja2.addTab("Cajas", jPanel54);

    javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
    jPanel48.setLayout(jPanel48Layout);
    jPanel48Layout.setHorizontalGroup(
        jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel48Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(TabbedPane_Caja2)
            .addContainerGap())
    );
    jPanel48Layout.setVerticalGroup(
        jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel48Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(TabbedPane_Caja2)
            .addContainerGap())
    );

    jPanel49.setBackground(new java.awt.Color(255, 255, 255));
    jPanel49.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    TabbedPane_Caja1.setBackground(new java.awt.Color(255, 255, 255));
    TabbedPane_Caja1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    TabbedPane_Caja1.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            TabbedPane_Caja1StateChanged(evt);
        }
    });

    jPanel50.setBackground(new java.awt.Color(255, 255, 255));
    jPanel50.setLayout(null);

    LabelUser_Usuario1.setForeground(new java.awt.Color(70, 106, 124));
    LabelUser_Usuario1.setText("Ingresos");
    jPanel50.add(LabelUser_Usuario1);
    LabelUser_Usuario1.setBounds(13, 130, 170, 16);

    TextFieldCaja_Retirar.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
    TextFieldCaja_Retirar.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TextFieldCaja_RetirarKeyReleased(evt);
        }
        public void keyTyped(java.awt.event.KeyEvent evt) {
            TextFieldCaja_RetirarKeyTyped(evt);
        }
    });
    jPanel50.add(TextFieldCaja_Retirar);
    TextFieldCaja_Retirar.setBounds(13, 91, 213, 30);

    LabelCaja_Ingresos.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
    LabelCaja_Ingresos.setForeground(new java.awt.Color(70, 106, 124));
    LabelCaja_Ingresos.setText("$0.00");
    jPanel50.add(LabelCaja_Ingresos);
    LabelCaja_Ingresos.setBounds(13, 143, 208, 29);

    LabelCaja_Retirar.setForeground(new java.awt.Color(70, 106, 124));
    LabelCaja_Retirar.setText("Retirar Ingresos");
    jPanel50.add(LabelCaja_Retirar);
    LabelCaja_Retirar.setBounds(13, 65, 170, 16);

    LabelUser_Usuario3.setForeground(new java.awt.Color(70, 106, 124));
    LabelUser_Usuario3.setText("Ingresos");
    jPanel50.add(LabelUser_Usuario3);
    LabelUser_Usuario3.setBounds(13, 13, 170, 16);

    LabelCaja_Ingreso.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
    LabelCaja_Ingreso.setForeground(new java.awt.Color(70, 106, 124));
    LabelCaja_Ingreso.setText("$0.00");
    jPanel50.add(LabelCaja_Ingreso);
    LabelCaja_Ingreso.setBounds(13, 26, 208, 29);

    TabbedPane_Caja1.addTab("Ingresos", jPanel50);

    jPanel51.setBackground(new java.awt.Color(255, 255, 255));

    LabelUser_Usuario4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    LabelUser_Usuario4.setForeground(new java.awt.Color(70, 106, 124));
    LabelUser_Usuario4.setText("Numero de caja");

    LabelNum_Caja.setForeground(new java.awt.Color(70, 106, 124));
    LabelNum_Caja.setText("Numero de cajas");

    Spinner_Caja.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            Spinner_CajaStateChanged(evt);
        }
    });

    CheckBoxCaja_Ingresos.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    CheckBoxCaja_Ingresos.setText("Asignar ingresos");
    CheckBoxCaja_Ingresos.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            CheckBoxCaja_IngresosStateChanged(evt);
        }
    });

    LabelUser_Usuario5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    LabelUser_Usuario5.setForeground(new java.awt.Color(70, 106, 124));
    LabelUser_Usuario5.setText("Registrar caja");

    LabelCaja_Numero.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
    LabelCaja_Numero.setForeground(new java.awt.Color(70, 106, 124));
    LabelCaja_Numero.setText("#0");

    TextFieldCaja_IngresoInicial.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
    TextFieldCaja_IngresoInicial.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TextFieldCaja_IngresoInicialKeyReleased(evt);
        }
        public void keyTyped(java.awt.event.KeyEvent evt) {
            TextFieldCaja_IngresoInicialKeyTyped(evt);
        }
    });

    LabelCaja_IngresoInicial.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    LabelCaja_IngresoInicial.setForeground(new java.awt.Color(70, 106, 124));
    LabelCaja_IngresoInicial.setText("Ingreso inicial");

    javax.swing.GroupLayout jPanel51Layout = new javax.swing.GroupLayout(jPanel51);
    jPanel51.setLayout(jPanel51Layout);
    jPanel51Layout.setHorizontalGroup(
        jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel51Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel51Layout.createSequentialGroup()
                    .addGroup(jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(LabelUser_Usuario4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelNum_Caja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Spinner_Caja, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CheckBoxCaja_Ingresos, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                        .addComponent(LabelCaja_Numero, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(LabelCaja_IngresoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(TextFieldCaja_IngresoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE))
                .addComponent(LabelUser_Usuario5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    jPanel51Layout.setVerticalGroup(
        jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel51Layout.createSequentialGroup()
            .addGap(12, 12, 12)
            .addComponent(LabelUser_Usuario5)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(LabelNum_Caja)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(Spinner_Caja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(CheckBoxCaja_Ingresos)
            .addGap(18, 18, 18)
            .addComponent(LabelUser_Usuario4)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(LabelCaja_Numero)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(LabelCaja_IngresoInicial)
            .addGap(10, 10, 10)
            .addComponent(TextFieldCaja_IngresoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(79, Short.MAX_VALUE))
    );

    TabbedPane_Caja1.addTab("Cajas", jPanel51);

    ButtonCaja_Guardar.setBackground(new java.awt.Color(0, 153, 153));
    ButtonCaja_Guardar.setForeground(new java.awt.Color(255, 255, 255));
    ButtonCaja_Guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
    ButtonCaja_Guardar.setBorder(null);
    ButtonCaja_Guardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonCaja_Guardar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonCaja_GuardarActionPerformed(evt);
        }
    });

    Button_CancelarBodega.setBackground(new java.awt.Color(0, 153, 153));
    Button_CancelarBodega.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
    Button_CancelarBodega.setBorder(null);
    Button_CancelarBodega.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    Button_CancelarBodega.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_CancelarBodegaActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
    jPanel49.setLayout(jPanel49Layout);
    jPanel49Layout.setHorizontalGroup(
        jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel49Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(TabbedPane_Caja1)
            .addContainerGap())
        .addGroup(jPanel49Layout.createSequentialGroup()
            .addGap(32, 32, 32)
            .addComponent(ButtonCaja_Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
            .addComponent(Button_CancelarBodega, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(38, 38, 38))
    );
    jPanel49Layout.setVerticalGroup(
        jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel49Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(TabbedPane_Caja1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ButtonCaja_Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Button_CancelarBodega, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
    jPanel46.setLayout(jPanel46Layout);
    jPanel46Layout.setHorizontalGroup(
        jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel46Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel46Layout.createSequentialGroup()
                    .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel46Layout.setVerticalGroup(
        jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel46Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );

    jTabbedPane1.addTab("tab9", jPanel46);

    jPanel52.setBackground(new java.awt.Color(255, 255, 255));
    jPanel52.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    TabbedPane_Inv.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            TabbedPane_InvStateChanged(evt);
        }
    });

    jPanel55.setBackground(new java.awt.Color(255, 255, 255));
    jPanel55.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    jPanel57.setBackground(new java.awt.Color(255, 255, 255));
    jPanel57.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    jLabel63.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
    jLabel63.setForeground(new java.awt.Color(70, 106, 124));
    jLabel63.setText("Producto en bodega");

    jLabel64.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    jLabel64.setForeground(new java.awt.Color(70, 106, 124));
    jLabel64.setText("Limite de existencia");
    jLabel64.setToolTipText("");

    SpinnerBodega_Existencia.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

    CheckBoxBodega_Existencia.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    CheckBoxBodega_Existencia.setText("Productos apunto de agotarse");

    LabelBodega_Existencia.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    LabelBodega_Existencia.setForeground(new java.awt.Color(70, 106, 124));
    LabelBodega_Existencia.setText("Existencia");
    LabelBodega_Existencia.setToolTipText("");

    TextFieldBodega_Existencia.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TextFieldBodega_ExistenciaKeyReleased(evt);
        }
        public void keyTyped(java.awt.event.KeyEvent evt) {
            TextFieldBodega_ExistenciaKeyTyped(evt);
        }
    });

    ButtonBodega_Cancelar.setBackground(new java.awt.Color(0, 153, 153));
    ButtonBodega_Cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
    ButtonBodega_Cancelar.setBorder(null);
    ButtonBodega_Cancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonBodega_Cancelar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonBodega_CancelarActionPerformed(evt);
        }
    });

    ButtonInvr_GuardarBodega.setBackground(new java.awt.Color(0, 153, 153));
    ButtonInvr_GuardarBodega.setForeground(new java.awt.Color(255, 255, 255));
    ButtonInvr_GuardarBodega.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
    ButtonInvr_GuardarBodega.setBorder(null);
    ButtonInvr_GuardarBodega.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonInvr_GuardarBodega.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvr_GuardarBodegaActionPerformed(evt);
        }
    });

    jLabel65.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
    jLabel65.setForeground(new java.awt.Color(70, 106, 124));
    jLabel65.setText("Opciones para exportar");

    ButtonBodega_Excel.setBackground(new java.awt.Color(0, 102, 153));
    ButtonBodega_Excel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    ButtonBodega_Excel.setForeground(new java.awt.Color(255, 255, 255));
    ButtonBodega_Excel.setText("Excel");
    ButtonBodega_Excel.setToolTipText("");
    ButtonBodega_Excel.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonBodega_ExcelActionPerformed(evt);
        }
    });

    ButtonBodega_PDF.setBackground(new java.awt.Color(0, 102, 153));
    ButtonBodega_PDF.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    ButtonBodega_PDF.setForeground(new java.awt.Color(255, 255, 255));
    ButtonBodega_PDF.setText("PDF");
    ButtonBodega_PDF.setToolTipText("");
    ButtonBodega_PDF.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonBodega_PDFActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel57Layout = new javax.swing.GroupLayout(jPanel57);
    jPanel57.setLayout(jPanel57Layout);
    jPanel57Layout.setHorizontalGroup(
        jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel57Layout.createSequentialGroup()
            .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel57Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(SpinnerBodega_Existencia, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(CheckBoxBodega_Existencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TextFieldBodega_Existencia, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(LabelBodega_Existencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel57Layout.createSequentialGroup()
                    .addGap(65, 65, 65)
                    .addComponent(ButtonInvr_GuardarBodega, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(52, 52, 52)
                    .addComponent(ButtonBodega_Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel57Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel57Layout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(ButtonBodega_Excel)
                    .addGap(62, 62, 62)
                    .addComponent(ButtonBodega_PDF)))
            .addContainerGap(109, Short.MAX_VALUE))
    );
    jPanel57Layout.setVerticalGroup(
        jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel57Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel63)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jLabel64)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(SpinnerBodega_Existencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(CheckBoxBodega_Existencia)
            .addGap(18, 18, 18)
            .addComponent(LabelBodega_Existencia)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(TextFieldBodega_Existencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jLabel65)
            .addGap(18, 18, 18)
            .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ButtonBodega_Excel)
                .addComponent(ButtonBodega_PDF))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
            .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ButtonInvr_GuardarBodega, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ButtonBodega_Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    jPanel58.setBackground(new java.awt.Color(255, 255, 255));
    jPanel58.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    Table_Bodega.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
    Table_Bodega.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    Table_Bodega.setSelectionBackground(new java.awt.Color(102, 204, 255));
    Table_Bodega.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            Table_BodegaMouseClicked(evt);
        }
    });
    Table_Bodega.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            Table_BodegaKeyReleased(evt);
        }
    });
    jScrollPane16.setViewportView(Table_Bodega);

    ButtonInvBodega_Primero.setBackground(new java.awt.Color(0, 153, 153));
    ButtonInvBodega_Primero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
    ButtonInvBodega_Primero.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonInvBodega_Primero.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvBodega_PrimeroActionPerformed(evt);
        }
    });

    ButtonInvBodega_Anterior.setBackground(new java.awt.Color(0, 153, 153));
    ButtonInvBodega_Anterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
    ButtonInvBodega_Anterior.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonInvBodega_Anterior.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvBodega_AnteriorActionPerformed(evt);
        }
    });

    ButtonInvBodega_Siguiente.setBackground(new java.awt.Color(0, 153, 153));
    ButtonInvBodega_Siguiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
    ButtonInvBodega_Siguiente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonInvBodega_Siguiente.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvBodega_SiguienteActionPerformed(evt);
        }
    });

    ButtonInvBodega_Ultimo.setBackground(new java.awt.Color(0, 153, 153));
    ButtonInvBodega_Ultimo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
    ButtonInvBodega_Ultimo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonInvBodega_Ultimo.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvBodega_UltimoActionPerformed(evt);
        }
    });

    LabelInvBodega_Paginas.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    LabelInvBodega_Paginas.setForeground(new java.awt.Color(70, 106, 124));
    LabelInvBodega_Paginas.setText("Page");

    javax.swing.GroupLayout jPanel58Layout = new javax.swing.GroupLayout(jPanel58);
    jPanel58.setLayout(jPanel58Layout);
    jPanel58Layout.setHorizontalGroup(
        jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel58Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 1048, Short.MAX_VALUE)
            .addContainerGap())
        .addGroup(jPanel58Layout.createSequentialGroup()
            .addGap(275, 275, 275)
            .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel58Layout.createSequentialGroup()
                    .addComponent(ButtonInvBodega_Primero, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ButtonInvBodega_Anterior, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ButtonInvBodega_Siguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ButtonInvBodega_Ultimo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel58Layout.createSequentialGroup()
                    .addGap(212, 212, 212)
                    .addComponent(LabelInvBodega_Paginas)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel58Layout.setVerticalGroup(
        jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel58Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(LabelInvBodega_Paginas)
            .addGap(4, 4, 4)
            .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ButtonInvBodega_Ultimo, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(ButtonInvBodega_Siguiente, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(ButtonInvBodega_Anterior, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(ButtonInvBodega_Primero, javax.swing.GroupLayout.Alignment.TRAILING))
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel55Layout = new javax.swing.GroupLayout(jPanel55);
    jPanel55.setLayout(jPanel55Layout);
    jPanel55Layout.setHorizontalGroup(
        jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel55Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel58, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );
    jPanel55Layout.setVerticalGroup(
        jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel55Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel58, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );

    TabbedPane_Inv.addTab("Bodega", jPanel55);

    jPanel56.setBackground(new java.awt.Color(255, 255, 255));

    jPanel60.setBackground(new java.awt.Color(255, 255, 255));
    jPanel60.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    LabelInv_Productos.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
    LabelInv_Productos.setForeground(new java.awt.Color(70, 106, 124));
    LabelInv_Productos.setText("Lista de productos");

    LabelInv_Precio.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    LabelInv_Precio.setForeground(new java.awt.Color(70, 106, 124));
    LabelInv_Precio.setText("Precio");
    LabelInv_Precio.setToolTipText("");

    TextFieldInv_Precio.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TextFieldInv_PrecioKeyReleased(evt);
        }
        public void keyTyped(java.awt.event.KeyEvent evt) {
            TextFieldInv_PrecioKeyTyped(evt);
        }
    });

    ButtonInvProductos_Cancelar.setBackground(new java.awt.Color(0, 153, 153));
    ButtonInvProductos_Cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
    ButtonInvProductos_Cancelar.setBorder(null);
    ButtonInvProductos_Cancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonInvProductos_Cancelar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvProductos_CancelarActionPerformed(evt);
        }
    });

    ButtonInv_GuardarProductos.setBackground(new java.awt.Color(0, 153, 153));
    ButtonInv_GuardarProductos.setForeground(new java.awt.Color(255, 255, 255));
    ButtonInv_GuardarProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
    ButtonInv_GuardarProductos.setBorder(null);
    ButtonInv_GuardarProductos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonInv_GuardarProductos.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInv_GuardarProductosActionPerformed(evt);
        }
    });

    jLabel68.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
    jLabel68.setForeground(new java.awt.Color(70, 106, 124));
    jLabel68.setText("Opciones para exportar");

    ButtonInvProducto_Excel.setBackground(new java.awt.Color(0, 102, 153));
    ButtonInvProducto_Excel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    ButtonInvProducto_Excel.setForeground(new java.awt.Color(255, 255, 255));
    ButtonInvProducto_Excel.setText("Excel");
    ButtonInvProducto_Excel.setToolTipText("");
    ButtonInvProducto_Excel.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvProducto_ExcelActionPerformed(evt);
        }
    });

    ButtonInvProducto_PDF.setBackground(new java.awt.Color(0, 102, 153));
    ButtonInvProducto_PDF.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    ButtonInvProducto_PDF.setForeground(new java.awt.Color(255, 255, 255));
    ButtonInvProducto_PDF.setText("PDF");
    ButtonInvProducto_PDF.setToolTipText("");
    ButtonInvProducto_PDF.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvProducto_PDFActionPerformed(evt);
        }
    });

    LabelInv_Descuento.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    LabelInv_Descuento.setForeground(new java.awt.Color(70, 106, 124));
    LabelInv_Descuento.setText("Descuento");
    LabelInv_Descuento.setToolTipText("");

    TextFieldInv_Descuento.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TextFieldInv_DescuentoKeyReleased(evt);
        }
        public void keyTyped(java.awt.event.KeyEvent evt) {
            TextFieldInv_DescuentoKeyTyped(evt);
        }
    });

    javax.swing.GroupLayout jPanel60Layout = new javax.swing.GroupLayout(jPanel60);
    jPanel60.setLayout(jPanel60Layout);
    jPanel60Layout.setHorizontalGroup(
        jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel60Layout.createSequentialGroup()
            .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(jPanel60Layout.createSequentialGroup()
                    .addGap(65, 65, 65)
                    .addComponent(ButtonInv_GuardarProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(52, 52, 52)
                    .addComponent(ButtonInvProductos_Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel60Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel60Layout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(ButtonInvProducto_Excel)
                    .addGap(62, 62, 62)
                    .addComponent(ButtonInvProducto_PDF))
                .addComponent(TextFieldInv_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(LabelInv_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(TextFieldInv_Descuento, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(LabelInv_Descuento, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel60Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(LabelInv_Productos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addContainerGap(109, Short.MAX_VALUE))
    );
    jPanel60Layout.setVerticalGroup(
        jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel60Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(LabelInv_Productos)
            .addGap(18, 18, 18)
            .addComponent(LabelInv_Precio)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(TextFieldInv_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(LabelInv_Descuento)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(TextFieldInv_Descuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(66, 66, 66)
            .addComponent(jLabel68)
            .addGap(18, 18, 18)
            .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ButtonInvProducto_Excel)
                .addComponent(ButtonInvProducto_PDF))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
            .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ButtonInv_GuardarProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ButtonInvProductos_Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    jPanel61.setBackground(new java.awt.Color(255, 255, 255));
    jPanel61.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    TableInv_Productos.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
    TableInv_Productos.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    TableInv_Productos.setSelectionBackground(new java.awt.Color(102, 204, 255));
    TableInv_Productos.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            TableInv_ProductosMouseClicked(evt);
        }
    });
    TableInv_Productos.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TableInv_ProductosKeyReleased(evt);
        }
    });
    jScrollPane17.setViewportView(TableInv_Productos);

    ButtonInvProducto_Primero.setBackground(new java.awt.Color(0, 153, 153));
    ButtonInvProducto_Primero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
    ButtonInvProducto_Primero.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonInvProducto_Primero.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvProducto_PrimeroActionPerformed(evt);
        }
    });

    ButtonInvProducto_Anterior.setBackground(new java.awt.Color(0, 153, 153));
    ButtonInvProducto_Anterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
    ButtonInvProducto_Anterior.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonInvProducto_Anterior.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvProducto_AnteriorActionPerformed(evt);
        }
    });

    ButtonInvProducto_Siguiente.setBackground(new java.awt.Color(0, 153, 153));
    ButtonInvProducto_Siguiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
    ButtonInvProducto_Siguiente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonInvProducto_Siguiente.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvProducto_SiguienteActionPerformed(evt);
        }
    });

    ButtonInvProducto_Ultimo.setBackground(new java.awt.Color(0, 153, 153));
    ButtonInvProducto_Ultimo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
    ButtonInvProducto_Ultimo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonInvProducto_Ultimo.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvProducto_UltimoActionPerformed(evt);
        }
    });

    LabelInvProductos_Paginas.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    LabelInvProductos_Paginas.setForeground(new java.awt.Color(70, 106, 124));
    LabelInvProductos_Paginas.setText("Page");

    javax.swing.GroupLayout jPanel61Layout = new javax.swing.GroupLayout(jPanel61);
    jPanel61.setLayout(jPanel61Layout);
    jPanel61Layout.setHorizontalGroup(
        jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel61Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 1050, Short.MAX_VALUE)
            .addContainerGap())
        .addGroup(jPanel61Layout.createSequentialGroup()
            .addGap(275, 275, 275)
            .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel61Layout.createSequentialGroup()
                    .addComponent(ButtonInvProducto_Primero, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ButtonInvProducto_Anterior, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ButtonInvProducto_Siguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ButtonInvProducto_Ultimo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel61Layout.createSequentialGroup()
                    .addGap(212, 212, 212)
                    .addComponent(LabelInvProductos_Paginas)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel61Layout.setVerticalGroup(
        jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel61Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(LabelInvProductos_Paginas)
            .addGap(4, 4, 4)
            .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ButtonInvProducto_Ultimo, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(ButtonInvProducto_Siguiente, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(ButtonInvProducto_Anterior, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(ButtonInvProducto_Primero, javax.swing.GroupLayout.Alignment.TRAILING))
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel56Layout = new javax.swing.GroupLayout(jPanel56);
    jPanel56.setLayout(jPanel56Layout);
    jPanel56Layout.setHorizontalGroup(
        jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel56Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel60, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );
    jPanel56Layout.setVerticalGroup(
        jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel56Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel60, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );

    TabbedPane_Inv.addTab("Productos", jPanel56);

    jPanel62.setBackground(new java.awt.Color(255, 255, 255));

    jPanel63.setBackground(new java.awt.Color(255, 255, 255));
    jPanel63.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    TableInv_Ventas.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
    TableInv_Ventas.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    TableInv_Ventas.setSelectionBackground(new java.awt.Color(102, 204, 255));
    jScrollPane18.setViewportView(TableInv_Ventas);

    ButtonInvVenta_Primero.setBackground(new java.awt.Color(0, 153, 153));
    ButtonInvVenta_Primero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
    ButtonInvVenta_Primero.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonInvVenta_Primero.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvVenta_PrimeroActionPerformed(evt);
        }
    });

    ButtonInvVenta_Anterior.setBackground(new java.awt.Color(0, 153, 153));
    ButtonInvVenta_Anterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
    ButtonInvVenta_Anterior.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonInvVenta_Anterior.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvVenta_AnteriorActionPerformed(evt);
        }
    });

    ButtonInvVenta_Siguiente.setBackground(new java.awt.Color(0, 153, 153));
    ButtonInvVenta_Siguiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
    ButtonInvVenta_Siguiente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonInvVenta_Siguiente.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvVenta_SiguienteActionPerformed(evt);
        }
    });

    ButtonInvVenta_Ultimo.setBackground(new java.awt.Color(0, 153, 153));
    ButtonInvVenta_Ultimo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
    ButtonInvVenta_Ultimo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonInvVenta_Ultimo.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvVenta_UltimoActionPerformed(evt);
        }
    });

    LabelInvVentas_Paginas.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    LabelInvVentas_Paginas.setForeground(new java.awt.Color(70, 106, 124));
    LabelInvVentas_Paginas.setText("Page");

    javax.swing.GroupLayout jPanel63Layout = new javax.swing.GroupLayout(jPanel63);
    jPanel63.setLayout(jPanel63Layout);
    jPanel63Layout.setHorizontalGroup(
        jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel63Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 1050, Short.MAX_VALUE)
            .addContainerGap())
        .addGroup(jPanel63Layout.createSequentialGroup()
            .addGap(275, 275, 275)
            .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel63Layout.createSequentialGroup()
                    .addComponent(ButtonInvVenta_Primero, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ButtonInvVenta_Anterior, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ButtonInvVenta_Siguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ButtonInvVenta_Ultimo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel63Layout.createSequentialGroup()
                    .addGap(212, 212, 212)
                    .addComponent(LabelInvVentas_Paginas)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel63Layout.setVerticalGroup(
        jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel63Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(LabelInvVentas_Paginas)
            .addGap(4, 4, 4)
            .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ButtonInvVenta_Ultimo, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(ButtonInvVenta_Siguiente, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(ButtonInvVenta_Anterior, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(ButtonInvVenta_Primero, javax.swing.GroupLayout.Alignment.TRAILING))
            .addContainerGap())
    );

    jPanel64.setBackground(new java.awt.Color(255, 255, 255));
    jPanel64.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    LabelInv_Productos1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
    LabelInv_Productos1.setForeground(new java.awt.Color(70, 106, 124));
    LabelInv_Productos1.setText("Historial de ventas");

    LabelInv_Precio1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    LabelInv_Precio1.setForeground(new java.awt.Color(70, 106, 124));
    LabelInv_Precio1.setText("Inicio");
    LabelInv_Precio1.setToolTipText("");

    ButtonInvProductos_Cancelar1.setBackground(new java.awt.Color(0, 153, 153));
    ButtonInvProductos_Cancelar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
    ButtonInvProductos_Cancelar1.setBorder(null);
    ButtonInvProductos_Cancelar1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    ButtonInvProductos_Cancelar1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvProductos_Cancelar1ActionPerformed(evt);
        }
    });

    jLabel69.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
    jLabel69.setForeground(new java.awt.Color(70, 106, 124));
    jLabel69.setText("Opciones para exportar");

    ButtonInvProducto_Excel1.setBackground(new java.awt.Color(0, 102, 153));
    ButtonInvProducto_Excel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    ButtonInvProducto_Excel1.setForeground(new java.awt.Color(255, 255, 255));
    ButtonInvProducto_Excel1.setText("Excel");
    ButtonInvProducto_Excel1.setToolTipText("");
    ButtonInvProducto_Excel1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvProducto_Excel1ActionPerformed(evt);
        }
    });

    ButtonInvProducto_PDF1.setBackground(new java.awt.Color(0, 102, 153));
    ButtonInvProducto_PDF1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    ButtonInvProducto_PDF1.setForeground(new java.awt.Color(255, 255, 255));
    ButtonInvProducto_PDF1.setText("PDF");
    ButtonInvProducto_PDF1.setToolTipText("");
    ButtonInvProducto_PDF1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInvProducto_PDF1ActionPerformed(evt);
        }
    });

    LabelInv_Descuento1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    LabelInv_Descuento1.setForeground(new java.awt.Color(70, 106, 124));
    LabelInv_Descuento1.setText("Final");
    LabelInv_Descuento1.setToolTipText("");

    dateChooserCombo_Inicio.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 16),
                new java.awt.Color(187, 187, 187),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 16),
                new java.awt.Color(187, 187, 187),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 16),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 16),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 16),
                new java.awt.Color(187, 187, 187),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 16),
                new java.awt.Color(187, 187, 187),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dateChooserCombo_Inicio.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
    public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
        dateChooserCombo_InicioOnSelectionChange(evt);
    }
    });

    dateChooserCombo_Final.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 16),
                new java.awt.Color(187, 187, 187),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 16),
                new java.awt.Color(187, 187, 187),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 16),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 16),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 16),
                new java.awt.Color(187, 187, 187),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 16),
                new java.awt.Color(187, 187, 187),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dateChooserCombo_Final.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
    public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
        dateChooserCombo_FinalOnSelectionChange(evt);
    }
    });

    CheckBox_MaxVentas.setText("Productos mas vendidos");
    CheckBox_MaxVentas.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            CheckBox_MaxVentasStateChanged(evt);
        }
    });

    javax.swing.GroupLayout jPanel64Layout = new javax.swing.GroupLayout(jPanel64);
    jPanel64.setLayout(jPanel64Layout);
    jPanel64Layout.setHorizontalGroup(
        jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel64Layout.createSequentialGroup()
            .addGroup(jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel64Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel64Layout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(ButtonInvProducto_Excel1)
                    .addGap(62, 62, 62)
                    .addComponent(ButtonInvProducto_PDF1))
                .addComponent(LabelInv_Precio1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(LabelInv_Descuento1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel64Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(LabelInv_Productos1, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(dateChooserCombo_Inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(CheckBox_MaxVentas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                    .addComponent(dateChooserCombo_Final, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel64Layout.createSequentialGroup()
                    .addGap(97, 97, 97)
                    .addComponent(ButtonInvProductos_Cancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(109, Short.MAX_VALUE))
    );
    jPanel64Layout.setVerticalGroup(
        jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel64Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(LabelInv_Productos1)
            .addGap(18, 18, 18)
            .addComponent(LabelInv_Precio1)
            .addGap(10, 10, 10)
            .addComponent(dateChooserCombo_Inicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(LabelInv_Descuento1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(dateChooserCombo_Final, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(CheckBox_MaxVentas)
            .addGap(22, 22, 22)
            .addComponent(jLabel69)
            .addGap(18, 18, 18)
            .addGroup(jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ButtonInvProducto_Excel1)
                .addComponent(ButtonInvProducto_PDF1))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
            .addComponent(ButtonInvProductos_Cancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel62Layout = new javax.swing.GroupLayout(jPanel62);
    jPanel62.setLayout(jPanel62Layout);
    jPanel62Layout.setHorizontalGroup(
        jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel62Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel64, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );
    jPanel62Layout.setVerticalGroup(
        jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel62Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );

    TabbedPane_Inv.addTab("Ventas", jPanel62);

    jPanel59.setBackground(new java.awt.Color(255, 255, 255));
    jPanel59.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    jLabel61.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
    jLabel61.setForeground(new java.awt.Color(70, 106, 124));
    jLabel61.setText("Inventario");

    jLabel62.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel62.setForeground(new java.awt.Color(70, 106, 124));
    jLabel62.setText("Buscar");

    TextFieldInv_Buscar.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TextFieldInv_BuscarKeyReleased(evt);
        }
    });

    javax.swing.GroupLayout jPanel59Layout = new javax.swing.GroupLayout(jPanel59);
    jPanel59.setLayout(jPanel59Layout);
    jPanel59Layout.setHorizontalGroup(
        jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel59Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel61)
            .addGap(204, 204, 204)
            .addComponent(jLabel62)
            .addGap(18, 18, 18)
            .addComponent(TextFieldInv_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel59Layout.setVerticalGroup(
        jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel59Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel61)
                .addComponent(jLabel62)
                .addComponent(TextFieldInv_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel52Layout = new javax.swing.GroupLayout(jPanel52);
    jPanel52.setLayout(jPanel52Layout);
    jPanel52Layout.setHorizontalGroup(
        jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel52Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(TabbedPane_Inv, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jPanel59, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    jPanel52Layout.setVerticalGroup(
        jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel59, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(TabbedPane_Inv)
            .addContainerGap())
    );

    jTabbedPane1.addTab("tab10", jPanel52);

    jPanel65.setBackground(new java.awt.Color(255, 255, 255));
    jPanel65.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    jPanel66.setBackground(new java.awt.Color(255, 255, 255));
    jPanel66.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    jLabel66.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
    jLabel66.setForeground(new java.awt.Color(70, 106, 124));
    jLabel66.setText("Reportes");

    javax.swing.GroupLayout jPanel66Layout = new javax.swing.GroupLayout(jPanel66);
    jPanel66.setLayout(jPanel66Layout);
    jPanel66Layout.setHorizontalGroup(
        jPanel66Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel66Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel66)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel66Layout.setVerticalGroup(
        jPanel66Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel66Layout.createSequentialGroup()
            .addContainerGap(8, Short.MAX_VALUE)
            .addComponent(jLabel66)
            .addContainerGap())
    );

    TabbedPane_Inv1.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            TabbedPane_Inv1StateChanged(evt);
        }
    });

    jPanel67.setBackground(new java.awt.Color(255, 255, 255));
    jPanel67.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    jPanel68.setBackground(new java.awt.Color(255, 255, 255));
    jPanel68.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    jLabel70.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
    jLabel70.setForeground(new java.awt.Color(70, 106, 124));
    jLabel70.setText("Productos");

    Label_DepartamentoPDT1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    Label_DepartamentoPDT1.setForeground(new java.awt.Color(70, 106, 124));
    Label_DepartamentoPDT1.setText("Departamento");

    ComboBox_ReportDtop.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ComboBox_ReportDtopActionPerformed(evt);
        }
    });

    Label_CategoriaPDT1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    Label_CategoriaPDT1.setForeground(new java.awt.Color(70, 106, 124));
    Label_CategoriaPDT1.setText("Categoria");

    ComboBox_ReportCat.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ComboBox_ReportCatActionPerformed(evt);
        }
    });

    TableReport_Productos.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
    TableReport_Productos.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    TableReport_Productos.setSelectionBackground(new java.awt.Color(102, 204, 255));
    TableReport_Productos.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            TableReport_ProductosMouseClicked(evt);
        }
    });
    TableReport_Productos.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TableReport_ProductosKeyReleased(evt);
        }
    });
    jScrollPane19.setViewportView(TableReport_Productos);

    jLabel67.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel67.setForeground(new java.awt.Color(70, 106, 124));
    jLabel67.setText("Buscar");

    TextFieldInv_Buscar1.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TextFieldInv_Buscar1KeyReleased(evt);
        }
    });

    javax.swing.GroupLayout jPanel68Layout = new javax.swing.GroupLayout(jPanel68);
    jPanel68.setLayout(jPanel68Layout);
    jPanel68Layout.setHorizontalGroup(
        jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel68Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGroup(jPanel68Layout.createSequentialGroup()
                    .addGroup(jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Label_DepartamentoPDT1)
                        .addComponent(Label_CategoriaPDT1)
                        .addGroup(jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(ComboBox_ReportCat, javax.swing.GroupLayout.Alignment.LEADING, 0, 177, Short.MAX_VALUE)
                            .addComponent(ComboBox_ReportDtop, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jLabel67)
                        .addComponent(TextFieldInv_Buscar1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 70, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel68Layout.setVerticalGroup(
        jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel68Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel70)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(Label_DepartamentoPDT1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(ComboBox_ReportDtop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(Label_CategoriaPDT1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(ComboBox_ReportCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jLabel67)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(TextFieldInv_Buscar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(23, Short.MAX_VALUE))
    );

    jPanel69.setBackground(new java.awt.Color(255, 255, 255));
    jPanel69.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    jLabel71.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
    jLabel71.setForeground(new java.awt.Color(70, 106, 124));
    jLabel71.setText("Reportes de productos");

    jPanel70.setBackground(new java.awt.Color(255, 255, 255));
    jPanel70.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    javax.swing.GroupLayout jPanel70Layout = new javax.swing.GroupLayout(jPanel70);
    jPanel70.setLayout(jPanel70Layout);
    jPanel70Layout.setHorizontalGroup(
        jPanel70Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel70Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(LabelGrafica_Producto, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel70Layout.setVerticalGroup(
        jPanel70Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel70Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(LabelGrafica_Producto, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
            .addContainerGap())
    );

    jPanel71.setBackground(new java.awt.Color(255, 255, 255));
    jPanel71.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jPanel71.setForeground(new java.awt.Color(255, 255, 255));

    TableReport_InformeProducto.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
    TableReport_InformeProducto.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    TableReport_InformeProducto.setSelectionBackground(new java.awt.Color(102, 204, 255));
    TableReport_InformeProducto.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            TableReport_InformeProductoMouseClicked(evt);
        }
    });
    TableReport_InformeProducto.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TableReport_InformeProductoKeyReleased(evt);
        }
    });
    jScrollPane20.setViewportView(TableReport_InformeProducto);

    jLabel72.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel72.setForeground(new java.awt.Color(70, 106, 124));
    jLabel72.setText("Informe de productos");

    javax.swing.GroupLayout jPanel71Layout = new javax.swing.GroupLayout(jPanel71);
    jPanel71.setLayout(jPanel71Layout);
    jPanel71Layout.setHorizontalGroup(
        jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel71Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane20)
            .addContainerGap())
        .addGroup(jPanel71Layout.createSequentialGroup()
            .addGap(496, 496, 496)
            .addComponent(jLabel72)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel71Layout.setVerticalGroup(
        jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel71Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel72)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(17, 17, 17))
    );

    jPanel72.setBackground(new java.awt.Color(255, 255, 255));
    jPanel72.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    jLabel73.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel73.setForeground(new java.awt.Color(70, 106, 124));
    jLabel73.setText("Inventario de productos");

    TableReport_produtoBodega.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
    TableReport_produtoBodega.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    TableReport_produtoBodega.setSelectionBackground(new java.awt.Color(102, 204, 255));
    TableReport_produtoBodega.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            TableReport_produtoBodegaMouseClicked(evt);
        }
    });
    TableReport_produtoBodega.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TableReport_produtoBodegaKeyReleased(evt);
        }
    });
    jScrollPane21.setViewportView(TableReport_produtoBodega);

    javax.swing.GroupLayout jPanel72Layout = new javax.swing.GroupLayout(jPanel72);
    jPanel72.setLayout(jPanel72Layout);
    jPanel72Layout.setHorizontalGroup(
        jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel72Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane21, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
            .addContainerGap())
        .addGroup(jPanel72Layout.createSequentialGroup()
            .addGap(218, 218, 218)
            .addComponent(jLabel73)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel72Layout.setVerticalGroup(
        jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel72Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel73)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel69Layout = new javax.swing.GroupLayout(jPanel69);
    jPanel69.setLayout(jPanel69Layout);
    jPanel69Layout.setHorizontalGroup(
        jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel69Layout.createSequentialGroup()
            .addGroup(jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel71, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel69Layout.createSequentialGroup()
                    .addGap(469, 469, 469)
                    .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(jPanel69Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel70, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel69Layout.setVerticalGroup(
        jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel69Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel71)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel70, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jPanel71, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel67Layout = new javax.swing.GroupLayout(jPanel67);
    jPanel67.setLayout(jPanel67Layout);
    jPanel67Layout.setHorizontalGroup(
        jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel67Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel68, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );
    jPanel67Layout.setVerticalGroup(
        jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel67Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel68, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );

    TabbedPane_Inv1.addTab("Productos", jPanel67);

    javax.swing.GroupLayout jPanel65Layout = new javax.swing.GroupLayout(jPanel65);
    jPanel65.setLayout(jPanel65Layout);
    jPanel65Layout.setHorizontalGroup(
        jPanel65Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel65Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel65Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(TabbedPane_Inv1, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jPanel66, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    jPanel65Layout.setVerticalGroup(
        jPanel65Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel65Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel66, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(TabbedPane_Inv1)
            .addContainerGap())
    );

    jTabbedPane1.addTab("tab11", jPanel65);

    Button_Proveedor.setBackground(new java.awt.Color(0, 153, 153));
    Button_Proveedor.setForeground(new java.awt.Color(255, 255, 255));
    Button_Proveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/shipping.png"))); // NOI18N
    Button_Proveedor.setToolTipText("");
    Button_Proveedor.setBorder(null);
    Button_Proveedor.setBorderPainted(false);
    Button_Proveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    Button_Proveedor.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_ProveedorActionPerformed(evt);
        }
    });

    jLabel43.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    jLabel43.setForeground(new java.awt.Color(70, 106, 124));
    jLabel43.setText("Bienvenido: ");

    Label_Usuario.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    Label_Usuario.setForeground(new java.awt.Color(70, 106, 124));
    Label_Usuario.setText("Usuario");

    Label_Caja.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
    Label_Caja.setForeground(new java.awt.Color(0, 204, 153));
    Label_Caja.setText("0");

    jLabel44.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    jLabel44.setForeground(new java.awt.Color(70, 106, 124));
    jLabel44.setText("Caja  Nº ");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(PanelBanner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(Button_Ventas, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(Button_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(Button_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(Button_Poductos, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(Button_Cat_Dpt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(Button_Compras, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(Button_Configuracion, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel43)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(Label_Usuario)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel44)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(Label_Caja))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTabbedPane1)))
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(PanelBanner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(Button_Ventas, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Button_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Button_Poductos, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Button_Compras, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Button_Cat_Dpt, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Button_Configuracion, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Button_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel43)
                .addComponent(Label_Usuario)
                .addComponent(Label_Caja)
                .addComponent(jLabel44))
            .addGap(29, 29, 29)
            .addComponent(jTabbedPane1)
            .addContainerGap())
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="CODIGO CLIENTE">
    private void Button_ClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_ClienteActionPerformed
        jTabbedPane1.setSelectedIndex(1);
        Button_Ventas.setEnabled(true);
        Button_Cliente.setEnabled(false);
        Button_Poductos.setEnabled(true);
        Button_Cat_Dpt.setEnabled(true);
        Button_Proveedor.setEnabled(true);
        restablecerCliente();
        if (role.equals("Admin")) {
            Button_Compras.setEnabled(true);
            Button_Configuracion.setEnabled(true);
        } else {
            Button_Compras.setEnabled(false);
            Button_Configuracion.setEnabled(false);
        }
    }//GEN-LAST:event_Button_ClienteActionPerformed

    private void RadioButton_IngresarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioButton_IngresarClienteActionPerformed
        RadioButton_IngresarCliente.setForeground(new Color(0, 153, 51));
        RadioButton_PagosCliente.setForeground(Color.black);
        TextField_NombreCliente.setEnabled(true);
        TextField_ApellidioCliente.setEnabled(true);
        TextField_DireccioCliente.setEnabled(true);
        TextField_TelefonoCliente.setEnabled(true);
        TextField_PagosCliente.setEnabled(false);
        RadioButton_PagosCliente.setSelected(false);
        TextField_IdCliente.setEnabled(true);
    }//GEN-LAST:event_RadioButton_IngresarClienteActionPerformed

    private void RadioButton_PagosClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioButton_PagosClienteActionPerformed
        RadioButton_PagosCliente.setForeground(new Color(0, 153, 51));
        RadioButton_IngresarCliente.setForeground(Color.black);
        TextField_PagosCliente.setEnabled(true);
        TextField_NombreCliente.setEnabled(false);
        TextField_ApellidioCliente.setEnabled(false);
        TextField_DireccioCliente.setEnabled(false);
        TextField_TelefonoCliente.setEnabled(false);
        TextField_IdCliente.setEnabled(false);
        RadioButton_IngresarCliente.setSelected(false);
    }//GEN-LAST:event_RadioButton_PagosClienteActionPerformed

    private void TextField_IdClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_IdClienteKeyReleased
        if (TextField_IdCliente.getText().equals("")) {
            Label_IdCliente.setForeground(new Color(102, 102, 102));
        } else {
            Label_IdCliente.setText("ID");
            Label_IdCliente.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_IdClienteKeyReleased

    private void TextField_IdClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_IdClienteKeyTyped
        evento.numberKeyPres(evt);
    }//GEN-LAST:event_TextField_IdClienteKeyTyped

    private void TextField_NombreClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_NombreClienteKeyReleased
        if (TextField_NombreCliente.getText().equals("")) {
            Label_NombreCliente.setForeground(new Color(102, 102, 102));
        } else {
            Label_NombreCliente.setText("Nombre completo");
            Label_NombreCliente.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_NombreClienteKeyReleased

    private void TextField_NombreClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_NombreClienteKeyTyped
        evento.textKeyPress(evt);
    }//GEN-LAST:event_TextField_NombreClienteKeyTyped

    private void TextField_ApellidioClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_ApellidioClienteKeyReleased
        if (TextField_ApellidioCliente.getText().equals("")) {
            Label_ApellidoCliente.setForeground(new Color(102, 102, 102));
        } else {
            Label_ApellidoCliente.setText("Apellido");
            Label_ApellidoCliente.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_ApellidioClienteKeyReleased

    private void TextField_ApellidioClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_ApellidioClienteKeyTyped
        evento.textKeyPress(evt);
    }//GEN-LAST:event_TextField_ApellidioClienteKeyTyped

    private void TextField_DireccioClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_DireccioClienteKeyReleased
        if (TextField_DireccioCliente.getText().equals("")) {
            Label_DireccionCliente.setForeground(new Color(102, 102, 102));
        } else {
            Label_DireccionCliente.setText("Direccoòn");
            Label_DireccionCliente.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_DireccioClienteKeyReleased

    private void TextField_DireccioClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_DireccioClienteKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_TextField_DireccioClienteKeyTyped

    private void TextField_TelefonoClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_TelefonoClienteKeyReleased
        if (TextField_TelefonoCliente.getText().equals("")) {
            Label_TelefonoCliente.setForeground(new Color(102, 102, 102));
        } else {
            Label_TelefonoCliente.setText("Telefono");
            Label_TelefonoCliente.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_TelefonoClienteKeyReleased

    private void TextField_TelefonoClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_TelefonoClienteKeyTyped
        evento.numberKeyPres(evt);
    }//GEN-LAST:event_TextField_TelefonoClienteKeyTyped

    private void TextField_PagosClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_PagosClienteKeyReleased
        if (Table_ReportesCLT.getRowCount() == 0) {
            Label_PagoCliente.setText("Seleccione el cliente");
            Label_PagoCliente.setForeground(Color.RED);
        } else {
            if (!TextField_PagosCliente.getText().equalsIgnoreCase("")) {
                Label_PagoCliente.setText("Pagos de deudas");
                Label_PagoCliente.setForeground(new Color(0, 153, 51));
                String deuda1;
                double deuda2, deuda3, deudaTotal;
                deuda1 = (String) tablaModelReportCliente.getValueAt(0, 3);
                deuda2 = formato.reconstruir(deuda1.replace("$", ""));
                deuda3 = Double.parseDouble(TextField_PagosCliente.getText());
                pago = formato.decimal(deuda3);
                deudaTotal = deuda2 - deuda3;
                deudaActual = formato.decimal(deudaTotal);
            }
        }
    }//GEN-LAST:event_TextField_PagosClienteKeyReleased

    private void TextField_PagosClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_PagosClienteKeyTyped
        evento.numberDecimalKeyPress(evt, TextField_PagosCliente);
    }//GEN-LAST:event_TextField_PagosClienteKeyTyped

    private void Button_GuardarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_GuardarClienteActionPerformed
        if (RadioButton_IngresarCliente.isSelected()) {
            guardarCliente();
        } else {
            guardarReporte();
        }
    }//GEN-LAST:event_Button_GuardarClienteActionPerformed
    private void guardarCliente() {
        if (TextField_IdCliente.getText().equals("")) {
            Label_IdCliente.setText("Ingrese la ID");
            Label_IdCliente.setForeground(Color.RED);
            TextField_IdCliente.requestFocus();
        } else {
            if (TextField_NombreCliente.getText().equals("")) {
                Label_NombreCliente.setText("Ingrese el nombre completo");
                Label_NombreCliente.setForeground(Color.RED);
                TextField_NombreCliente.requestFocus();
            } else {
                if (TextField_ApellidioCliente.getText().equals("")) {
                    Label_ApellidoCliente.setText("Ingrese el Apellido");
                    Label_ApellidoCliente.setForeground(Color.RED);
                    TextField_ApellidioCliente.requestFocus();
                } else {
                    if (TextField_DireccioCliente.getText().equals("")) {
                        Label_DireccionCliente.setText("Ingrese la dirección");
                        Label_DireccionCliente.setForeground(Color.RED);
                        TextField_DireccioCliente.requestFocus();
                    } else {
                        if (TextField_TelefonoCliente.getText().equals("")) {
                            Label_TelefonoCliente.setText("Ingrese el telefono");
                            Label_TelefonoCliente.setForeground(Color.RED);
                            TextField_TelefonoCliente.requestFocus();
                        } else {
                            String ID = TextField_IdCliente.getText();
                            String Nombre = TextField_NombreCliente.getText();
                            String Apellido = TextField_ApellidioCliente.getText();
                            String Direccion = TextField_DireccioCliente.getText();
                            String Telefono = TextField_TelefonoCliente.getText();
                            boolean valor;
                            if (accion.equals("insert")) {

                                valor = cliente.insertCliente(ID, Nombre, Apellido, Direccion, Telefono);
                                if (valor) {
                                    restablecerCliente();
                                } else {
                                    Label_IdCliente.setText("La id ya esta registrada");
                                    Label_IdCliente.setForeground(Color.RED);
                                    TextField_IdCliente.requestFocus();
                                }
                            }
                            if (role.equals("Admin")) {
                                if (accion.equals("update")) {

                                    valor = cliente.updateCliente(ID, Nombre, Apellido, Direccion, Telefono, idCliente);
                                    if (valor) {
                                        restablecerCliente();
                                    } else {
                                        Label_IdCliente.setText("La id ya esta registrada");
                                        Label_IdCliente.setForeground(Color.RED);
                                        TextField_IdCliente.requestFocus();
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No cuenta con el permiso requerido");
                            }
                        }
                    }
                }
            }
        }
    }

    private void guardarReporte() {
        if (TextField_PagosCliente.getText().equals("")) {
            Label_PagoCliente.setText("Ingrese el pago");
            Label_PagoCliente.setForeground(Color.RED);
            TextField_PagosCliente.requestFocus();
        } else {
            cliente.updateRepostes(deudaActual, new Calendario().getFecha(), pago, idCliente);
            restablecerCliente();
        }
    }
    private void Button_PrimeroCLTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_PrimeroCLTActionPerformed
        new Paginador(tab, Table_Clientes, Label_PaginasClientes, 1).primero();
    }//GEN-LAST:event_Button_PrimeroCLTActionPerformed

    private void Button_AnteriorCLTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_AnteriorCLTActionPerformed
        new Paginador(tab, Table_Clientes, Label_PaginasClientes, 0).anterior();
    }//GEN-LAST:event_Button_AnteriorCLTActionPerformed

    private void Button_SiguienteCLTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SiguienteCLTActionPerformed
        new Paginador(tab, Table_Clientes, Label_PaginasClientes, 0).siguiente();
    }//GEN-LAST:event_Button_SiguienteCLTActionPerformed

    private void Button_UltimoCLTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_UltimoCLTActionPerformed
        new Paginador(tab, Table_Clientes, Label_PaginasClientes, 0).ultimo();
    }//GEN-LAST:event_Button_UltimoCLTActionPerformed

    private void Button_CancelarCLTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_CancelarCLTActionPerformed
        restablecerCliente();
    }//GEN-LAST:event_Button_CancelarCLTActionPerformed
    private void restablecerCliente() {
        tab = 1;
        accion = "insert";
        idCliente = 0;
        idRegistro = 0;
        TextField_IdCliente.setText("");
        TextField_NombreCliente.setText("");
        TextField_ApellidioCliente.setText("");
        TextField_DireccioCliente.setText("");
        TextField_TelefonoCliente.setText("");
        TextField_PagosCliente.setText("");
        TextField_IdCliente.setEnabled(true);
        TextField_NombreCliente.setEnabled(true);
        TextField_ApellidioCliente.setEnabled(true);
        TextField_DireccioCliente.setEnabled(true);
        TextField_TelefonoCliente.setEnabled(true);
        TextField_PagosCliente.setEnabled(false);
        Label_IdCliente.setForeground(new Color(102, 102, 102));
        Label_IdCliente.setText("ID");
        Label_NombreCliente.setForeground(new Color(102, 102, 102));
        Label_NombreCliente.setText("Nombre completo");
        Label_ApellidoCliente.setForeground(new Color(102, 102, 102));
        Label_ApellidoCliente.setText("Apellido");
        Label_DireccionCliente.setForeground(new Color(102, 102, 102));
        Label_DireccionCliente.setText("Direccoòn");
        Label_TelefonoCliente.setForeground(new Color(102, 102, 102));
        Label_TelefonoCliente.setText("Telefono");
        Label_PagoCliente.setForeground(new Color(102, 102, 102));
        Label_PagoCliente.setText("Pagos de deudas");
        RadioButton_IngresarCliente.setSelected(true);
        RadioButton_PagosCliente.setSelected(false);
        RadioButton_IngresarCliente.setForeground(new Color(0, 153, 51));
        RadioButton_PagosCliente.setForeground(Color.black);
        new Paginador(tab, Table_Clientes, Label_PaginasClientes, 1);
        cliente.reportesCliente(Table_ReportesCLT, idCliente);
        Label_NombreCLT.setText("Nombre");
        Label_ApellidoCLT.setText("Apellido");
        Label_SaldoActualCLT.setText("$0.00");
        Label_UltimoPagoCLT.setText("$0.00");
        Label_FechaPagoCLT.setText("Fecha");
    }
    private void Table_ClientesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_ClientesKeyReleased
        if (Table_Clientes.getSelectedRows().length > 0) {
            datosClientes();
        }
    }//GEN-LAST:event_Table_ClientesKeyReleased

    private void Table_ClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_ClientesMouseClicked
        if (Table_Clientes.getSelectedRows().length > 0) {
            datosClientes();
        }
    }//GEN-LAST:event_Table_ClientesMouseClicked

    private void Button_EliminarCLTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_EliminarCLTActionPerformed
        if (idCliente == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un cliente para eliminar");
        } else {
            if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null,
                    "Desea eliminar los registros " + "'", "Eliminar registros ",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                cliente.deleteCliente(idCliente, idRegistro);
                restablecerCliente();
            }
        }
    }//GEN-LAST:event_Button_EliminarCLTActionPerformed

    private void Button_FacturaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_FacturaClienteActionPerformed
        imprimir.imprimirRecibo(Panel_ReciboCliente);
    }//GEN-LAST:event_Button_FacturaClienteActionPerformed

    private void TextField_BuscarClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_BuscarClienteKeyReleased
        cliente.searchCliente(Table_Clientes, TextField_BuscarCliente.getText(), numPagi, pageSize);
    }//GEN-LAST:event_TextField_BuscarClienteKeyReleased
    private void datosClientes() {
        accion = "update";
        int filas = Table_Clientes.getSelectedRow();
        idCliente = Integer.valueOf((String) cliente.getModelo().getValueAt(filas, 0));
        TextField_IdCliente.setText((String) cliente.getModelo().getValueAt(filas, 1));
        TextField_NombreCliente.setText((String) cliente.getModelo().getValueAt(filas, 2));
        TextField_ApellidioCliente.setText((String) cliente.getModelo().getValueAt(filas, 3));
        TextField_DireccioCliente.setText((String) cliente.getModelo().getValueAt(filas, 4));
        TextField_TelefonoCliente.setText((String) cliente.getModelo().getValueAt(filas, 5));
        tablaModelReportCliente = cliente.reportesCliente(Table_ReportesCLT, idCliente);
        idRegistro = Integer.valueOf((String) tablaModelReportCliente.getValueAt(0, 0));
        Label_NombreCLT.setText((String) tablaModelReportCliente.getValueAt(0, 1));
        Label_ApellidoCLT.setText((String) tablaModelReportCliente.getValueAt(0, 2));
        Label_SaldoActualCLT.setText((String) tablaModelReportCliente.getValueAt(0, 3));
        Label_UltimoPagoCLT.setText((String) tablaModelReportCliente.getValueAt(0, 5));
        Label_FechaPagoCLT.setText((String) tablaModelReportCliente.getValueAt(0, 6));
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="CODIGO PROVEEDOR">
    private void Button_ProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_ProveedorActionPerformed
        jTabbedPane1.setSelectedIndex(2);
        Button_Ventas.setEnabled(true);
        Button_Cliente.setEnabled(true);
        Button_Poductos.setEnabled(true);
        Button_Cat_Dpt.setEnabled(true);
        Button_Proveedor.setEnabled(false);
        if (role.equals("Admin")) {
            Button_Compras.setEnabled(true);
            Button_Configuracion.setEnabled(true);
        } else {
            Button_Compras.setEnabled(false);
            Button_Configuracion.setEnabled(false);
        }
        restablecerProveedor();
    }//GEN-LAST:event_Button_ProveedorActionPerformed

    private void TextFieldPd_ProveedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldPd_ProveedorKeyReleased
        if (TextFieldPd_Proveedor.getText().equals("")) {
            LabelPd_Proveedor.setForeground(new Color(102, 102, 102));
        } else {
            LabelPd_Proveedor.setText("Proveedor");
            LabelPd_Proveedor.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldPd_ProveedorKeyReleased

    private void TextFieldPd_EmailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldPd_EmailKeyReleased
        if (TextFieldPd_Email.getText().equals("")) {
            LabelPd_Email.setForeground(new Color(102, 102, 102));
        } else {
            LabelPd_Email.setText("Email");
            LabelPd_Email.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldPd_EmailKeyReleased

    private void TextFieldPd_TelefonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldPd_TelefonoKeyReleased
        if (TextFieldPd_Telefono.getText().equals("")) {
            LabelPd_Telefono.setForeground(new Color(102, 102, 102));
        } else {
            LabelPd_Telefono.setText("Telefono");
            LabelPd_Telefono.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldPd_TelefonoKeyReleased

    private void TextFieldPd_PagosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldPd_PagosKeyReleased
        if (TablePd_Reportes.getRowCount() == 0) {
            LabelPd_Pagos.setText("Seleccione el proveedor");
            LabelPd_Pagos.setForeground(Color.RED);
        } else {
            if (!TextFieldPd_Pagos.getText().equalsIgnoreCase("")) {
                LabelPd_Pagos.setText("Pagos de deudas");
                LabelPd_Pagos.setForeground(new Color(0, 153, 51));
                String deuda1;
                double deuda2, deuda3, deudaTotal;
                deuda1 = (String) tablaModelReportPd.getValueAt(0, 2);
                deuda2 = formato.reconstruir(deuda1.replace("$", ""));
                deuda3 = Double.parseDouble(TextFieldPd_Pagos.getText());
                pago = formato.decimal(deuda3);
                deudaTotal = deuda2 - deuda3;
                deudaActual = formato.decimal(deudaTotal);
            }
        }
    }//GEN-LAST:event_TextFieldPd_PagosKeyReleased

    private void TextFieldPd_TelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldPd_TelefonoKeyTyped
        evento.numberKeyPres(evt);
    }//GEN-LAST:event_TextFieldPd_TelefonoKeyTyped

    private void TextFieldPd_PagosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldPd_PagosKeyTyped
        evento.numberDecimalKeyPress(evt, TextFieldPd_Pagos);
    }//GEN-LAST:event_TextFieldPd_PagosKeyTyped

    private void Button_GuardarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_GuardarProveedorActionPerformed

        if (RadioButtonPd_Ingresar.isSelected()) {
            guardarProveedor();
        } else {
            guardarReportePd();
        }

    }//GEN-LAST:event_Button_GuardarProveedorActionPerformed

    private void RadioButtonPd_IngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioButtonPd_IngresarActionPerformed
        RadioButtonPd_Ingresar.setForeground(new Color(0, 153, 51));
        RadioButtonPd_Pagos.setForeground(Color.black);
        TextFieldPd_Proveedor.setEnabled(true);
        TextFieldPd_Email.setEnabled(true);
        TextFieldPd_Telefono.setEnabled(true);
        TextFieldPd_Pagos.setEnabled(false);
        RadioButtonPd_Pagos.setSelected(false);
    }//GEN-LAST:event_RadioButtonPd_IngresarActionPerformed

    private void RadioButtonPd_PagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioButtonPd_PagosActionPerformed
        RadioButtonPd_Pagos.setForeground(new Color(0, 153, 51));
        RadioButtonPd_Ingresar.setForeground(Color.black);
        TextFieldPd_Proveedor.setEnabled(false);
        TextFieldPd_Email.setEnabled(false);
        TextFieldPd_Telefono.setEnabled(false);
        TextFieldPd_Pagos.setEnabled(true);
        RadioButtonPd_Ingresar.setSelected(false);
    }//GEN-LAST:event_RadioButtonPd_PagosActionPerformed
    private void guardarProveedor() {
        if (TextFieldPd_Proveedor.getText().equals("")) {
            LabelPd_Proveedor.setText("Ingrese el proveedor");
            LabelPd_Proveedor.setForeground(Color.RED);
            TextFieldPd_Proveedor.requestFocus();
        } else {
            if (TextFieldPd_Email.getText().equals("")) {
                LabelPd_Email.setText("Ingrese el email");
                LabelPd_Email.setForeground(Color.RED);
                TextFieldPd_Email.requestFocus();
            } else {
                if (TextFieldPd_Telefono.getText().equals("")) {
                    LabelPd_Telefono.setText("Ingrese el telefono");
                    LabelPd_Telefono.setForeground(Color.RED);
                    TextFieldPd_Telefono.requestFocus();
                } else {
                    if (evento.isEmail(TextFieldPd_Email.getText())) {
                        String Proveedor = TextFieldPd_Proveedor.getText();
                        String Email = TextFieldPd_Email.getText();
                        String Telefono = TextFieldPd_Telefono.getText();
                        boolean valor;

                        switch (accion) {
                            case "insert":
                                dataProveedor = proveedor.insertProveedor(Proveedor, Email, Telefono);
                                if (0 == dataProveedor.size()) {
                                    restablecerProveedor();
                                } else {
                                    if (dataProveedor.get(0).getTelefono().equals(Telefono)) {
                                        LabelPd_Telefono.setText("El telefono ya esta registrado");
                                        LabelPd_Telefono.setForeground(Color.RED);
                                        TextFieldPd_Telefono.requestFocus();
                                    }
                                    if (dataProveedor.get(0).getEmail().equals(Email)) {
                                        LabelPd_Email.setText("El email ya esta registrado");
                                        LabelPd_Email.setForeground(Color.RED);
                                        TextFieldPd_Email.requestFocus();
                                    }
                                }
                                break;

                            case "update":
                                if (role.equals("Admin")) {
                                    dataProveedor = proveedor.updateProveedor(idProveedor, Proveedor, Email, Telefono);
                                    if (0 == dataProveedor.size()) {
                                        restablecerProveedor();
                                    } else {
                                        if (idProveedor != dataProveedor.get(0).getIdProveedor()) {
                                            LabelPd_Telefono.setText("El telefono ya esta registrado");
                                            LabelPd_Telefono.setForeground(Color.RED);
                                            TextFieldPd_Telefono.requestFocus();
                                        }
                                        if (2 == dataProveedor.size() && idProveedor != dataProveedor.get(1).getIdProveedor()) {
                                            LabelPd_Email.setText("El email ya esta registrado");
                                            LabelPd_Email.setForeground(Color.RED);
                                            TextFieldPd_Email.requestFocus();
                                        }
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "No cuenta con el permiso requerido");
                                }
                                break;
                        }

                    } else {
                        LabelPd_Email.setText("Email incorrecto");
                        LabelPd_Email.setForeground(Color.RED);
                        TextFieldPd_Email.requestFocus();
                    }
                }
            }
        }
    }

    private void guardarReportePd() {
        if (TextFieldPd_Pagos.getText().equals("")) {
            LabelPd_Pagos.setText("Ingrese el pago");
            LabelPd_Pagos.setForeground(Color.RED);
            TextFieldPd_Pagos.requestFocus();
        } else {
            proveedor.updateRepostes(deudaActual, new Calendario().getFecha(), pago, idProveedor);
            restablecerProveedor();
        }
    }
    private void Table_ProveedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_ProveedorKeyReleased
        if (Table_Proveedor.getSelectedRows().length > 0) {
            datosProveedor();
        }
    }//GEN-LAST:event_Table_ProveedorKeyReleased

    private void Table_ProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_ProveedorMouseClicked
        if (Table_Proveedor.getSelectedRows().length > 0) {
            datosProveedor();
        }
    }//GEN-LAST:event_Table_ProveedorMouseClicked
    private void datosProveedor() {
        accion = "update";
        int filas = Table_Proveedor.getSelectedRow();
        idProveedor = Integer.valueOf((String) proveedor.getModelo().getValueAt(filas, 0));
        TextFieldPd_Proveedor.setText((String) proveedor.getModelo().getValueAt(filas, 1));
        TextFieldPd_Email.setText((String) proveedor.getModelo().getValueAt(filas, 2));
        TextFieldPd_Telefono.setText((String) proveedor.getModelo().getValueAt(filas, 3));
        tablaModelReportPd = proveedor.reportesProveeedor(TablePd_Reportes, idProveedor);
        idRegistro = Integer.valueOf((String) tablaModelReportPd.getValueAt(0, 0));
        Label_ProveedorRB.setText((String) tablaModelReportPd.getValueAt(0, 1));
        LabelPd_SaldoActual.setText((String) tablaModelReportPd.getValueAt(0, 2));
        LabelPd_UltimoPago.setText((String) tablaModelReportPd.getValueAt(0, 4));
        LabelPd_FechaPago.setText((String) tablaModelReportPd.getValueAt(0, 3));

        LabelPd_Proveedor.setForeground(new Color(0, 153, 51));
        LabelPd_Telefono.setForeground(new Color(0, 153, 51));
        LabelPd_Email.setForeground(new Color(0, 153, 51));
    }

    private void restablecerProveedor() {
        tab = 2;
        accion = "insert";
        idProveedor = 0;
        idRegistro = 0;
        TextFieldPd_Proveedor.setText("");
        TextFieldPd_Email.setText("");
        TextFieldPd_Telefono.setText("");
        TextFieldPd_Pagos.setText("");
        TextFieldPd_Proveedor.requestFocus();
        LabelPd_Pagos.setText("Pagos de deudas");
        LabelPd_Pagos.setForeground(new Color(102, 102, 102));
        LabelPd_Proveedor.setText("Proveedor");
        LabelPd_Proveedor.setForeground(new Color(102, 102, 102));
        LabelPd_Email.setText("Email");
        LabelPd_Email.setForeground(new Color(102, 102, 102));
        LabelPd_Telefono.setText("Telefono");
        LabelPd_Telefono.setForeground(new Color(102, 102, 102));
        RadioButtonPd_Ingresar.setSelected(true);
        RadioButtonPd_Ingresar.setForeground(new Color(0, 153, 51));
        TextFieldPd_Proveedor.setEnabled(true);
        TextFieldPd_Email.setEnabled(true);
        TextFieldPd_Telefono.setEnabled(true);
        TextFieldPd_Pagos.setEnabled(false);
        RadioButtonPd_Pagos.setSelected(false);
        RadioButtonPd_Pagos.setForeground(Color.black);
        Label_ProveedorRB.setText("Proveedor");
        LabelPd_SaldoActual.setText("$0.00");
        LabelPd_UltimoPago.setText("$0.00");
        LabelPd_FechaPago.setText("Fecha");
        new Paginador(tab, Table_Proveedor, LabelPd_Paginas, 1);
        proveedor.reportesProveeedor(TablePd_Reportes, idProveedor);
    }
    private void ButtonPd_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPd_CancelarActionPerformed
        restablecerProveedor();
    }//GEN-LAST:event_ButtonPd_CancelarActionPerformed

    private void ButtonPd_PrimeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPd_PrimeroActionPerformed
        new Paginador(tab, Table_Proveedor, LabelPd_Paginas, 1).primero();
    }//GEN-LAST:event_ButtonPd_PrimeroActionPerformed

    private void ButtonPd_AnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPd_AnteriorActionPerformed
        new Paginador(tab, Table_Proveedor, LabelPd_Paginas, 0).anterior();
    }//GEN-LAST:event_ButtonPd_AnteriorActionPerformed

    private void ButtonPd_SiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPd_SiguienteActionPerformed
        new Paginador(tab, Table_Proveedor, LabelPd_Paginas, 0).siguiente();
    }//GEN-LAST:event_ButtonPd_SiguienteActionPerformed

    private void ButtonPd_UltimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPd_UltimoActionPerformed
        new Paginador(tab, Table_Proveedor, LabelPd_Paginas, 0).ultimo();
    }//GEN-LAST:event_ButtonPd_UltimoActionPerformed

    private void ButtonPd_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPd_EliminarActionPerformed
        if (idProveedor == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un proveedor para eliminar");
        } else {
            if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null,
                    "Desea eliminar los registros " + "'", "Eliminar registros ",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                proveedor.deleteProveedor(idProveedor, idRegistro);

                restablecerProveedor();
            }
        }
    }//GEN-LAST:event_ButtonPd_EliminarActionPerformed

    private void TextFieldPd_BuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldPd_BuscarKeyReleased
        proveedor.searchProveedores(Table_Proveedor, TextFieldPd_Buscar.getText(), num_registro, pageSize);
    }//GEN-LAST:event_TextFieldPd_BuscarKeyReleased

    private void ButtonPd_FacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPd_FacturaActionPerformed
        imprimir.imprimirRecibo(PanelPd_Recibo);
    }//GEN-LAST:event_ButtonPd_FacturaActionPerformed
    // </editor-fold>

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null,
                "Estas seguro de salir del sistema ? " + "'", "Cerrar sesión ",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            new Login().setVisible(true);
            if (0 < listUsuario.size()) {

                int idUsuario = listUsuario.get(0).getIdUsuario();
                String nombre = listUsuario.get(0).getNombre();
                String apellido = listUsuario.get(0).getApellido();
                String user = listUsuario.get(0).getUsuario();

                if (role.equals("Admin")) {
                    caja.insertCajarEgistro(idUsuario, nombre, apellido, user, role, 0, 0, false, new Calendario().getHora(), new Calendario().getFecha());
                } else {
                    int idCaja = listCaja.get(0).getIdCaja();
                    int cajas = listCaja.get(0).getCaja();
                    caja.update(idCaja, true);
                    caja.insertCajarEgistro(idUsuario, nombre, apellido, user, role, idCaja, cajas, false, new Calendario().getHora(), new Calendario().getFecha());
                }
            }
            dispose();
        } else {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }

    }//GEN-LAST:event_formWindowClosing
    Timer timer1 = new Timer(3000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (tab) {
                case 0:
                    venta.ingresosCajas(labelVT_IngresoIni, labelVT_IngresosVt, labelVT_IngresoTotal,
                            cajaUser, idUsuario);
                    break;
                case 8:
                    objectCaja.getCajas();
                    break;
                case 9:
                    switch (TabbedPane_Inv.getSelectedIndex()) {
                        case 0:
                            inventario.getBodega("", num_registro, pageSize);
                            break;
                        case 1:
                            inventario.getProductos("", num_registro, pageSize);
                            break;

                    }

                    break;
            }
        }
    });

    // <editor-fold defaultstate="collapsed" desc="CODIGO DEPARTAMENTO">
    private void Button_Cat_DptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_Cat_DptActionPerformed
        jTabbedPane1.setSelectedIndex(4);
        Button_Ventas.setEnabled(true);
        Button_Cliente.setEnabled(true);
        Button_Poductos.setEnabled(true);
        Button_Cat_Dpt.setEnabled(true);
        Button_Proveedor.setEnabled(true);
        Button_Cat_Dpt.setEnabled(false);
        if (role.equals("Admin")) {
            Button_Compras.setEnabled(true);
            Button_Configuracion.setEnabled(true);
        } else {
            Button_Compras.setEnabled(false);
            Button_Configuracion.setEnabled(false);
        }
    }//GEN-LAST:event_Button_Cat_DptActionPerformed

    private void RadioButton_DptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioButton_DptActionPerformed
        TextField_Categoria.setEnabled(false);
        TextField_Departamento.setEnabled(true);
        RadioButton_Dpt.setForeground(new Color(0, 153, 51));
        RadioButton_Cat.setForeground(Color.black);
        RadioButton_Cat.setSelected(false);
        TextField_Categoria.setText("");
    }//GEN-LAST:event_RadioButton_DptActionPerformed

    private void RadioButton_CatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioButton_CatActionPerformed
        TextField_Categoria.setEnabled(true);
        TextField_Departamento.setEnabled(false);
        RadioButton_Dpt.setForeground(Color.black);
        RadioButton_Dpt.setSelected(false);
        RadioButton_Cat.setForeground(new Color(0, 153, 51));
        TextField_Departamento.setText("");
    }//GEN-LAST:event_RadioButton_CatActionPerformed

    private void TextField_DepartamentoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_DepartamentoKeyReleased
        if (TextField_Departamento.getText().equals("")) {
            Label_Dpt.setText("Departamento");
            Label_Dpt.setForeground(new Color(102, 102, 102));
        } else {
            Label_Dpt.setText("Departamento");
            Label_Dpt.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_DepartamentoKeyReleased

    private void TextField_DepartamentoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_DepartamentoKeyTyped
        evento.textKeyPress(evt);
    }//GEN-LAST:event_TextField_DepartamentoKeyTyped

    private void TextField_CategoriaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_CategoriaKeyReleased
        if (TextField_Categoria.getText().equals("")) {
            Label_Cat.setText("Categoria");
            Label_Cat.setForeground(new Color(102, 102, 102));
        } else {
            Label_Cat.setText("Categoria");
            Label_Cat.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_CategoriaKeyReleased

    private void TextField_CategoriaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_CategoriaKeyTyped
        evento.textKeyPress(evt);
    }//GEN-LAST:event_TextField_CategoriaKeyTyped

    private void Button_GuardarCatDptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_GuardarCatDptActionPerformed
        if (role.equals("Admin")) {
            guardarCatDpt();
        } else {
            JOptionPane.showMessageDialog(null, "No cuenta con el permiso requerido");
        }

    }//GEN-LAST:event_Button_GuardarCatDptActionPerformed
    private void guardarCatDpt() {
        boolean valor = true;
        if (RadioButton_Dpt.isSelected()) {
            if (TextField_Departamento.getText().equals("")) {
                Label_Dpt.setText("Ingrese el departamento");
                Label_Dpt.setForeground(Color.RED);
                TextField_Departamento.requestFocus();
            } else {
                switch (accion) {
                    case "insert":
                        valor = departamento.insertDptoCat(TextField_Departamento.getText(), 0, "dpto");
                        break;
                    case "update":
                        valor = departamento.updateDptoCat(TextField_Departamento.getText(), idDpto, 0, "dpto");
                        break;
                }
                if (valor == false) {
                    Label_Dpt.setText("El departamento ya esta registrado");
                    Label_Dpt.setForeground(Color.RED);
                } else {
                    restablecerDoptoCat();
                }
            }
        }
        if (RadioButton_Cat.isSelected()) {
            if (TextField_Categoria.getText().equals("")) {
                Label_Cat.setText("Ingrese la categoria");
                Label_Cat.setForeground(Color.RED);
                TextField_Categoria.requestFocus();
            } else {
                if (idDpto != 0) {
                    switch (accion) {
                        case "insert":
                            valor = departamento.insertDptoCat(TextField_Categoria.getText(), idDpto, "cat");
                            break;
                        case "update":
                            valor = departamento.updateDptoCat(TextField_Categoria.getText(), idDpto, idCat, "cat");
                            break;
                    }

                    if (valor == false) {
                        Label_Cat.setText("La categoria ya esta registrada");
                        Label_Cat.setForeground(Color.RED);
                    } else {
                        restablecerDoptoCat();
                    }
                } else {
                    Label_Dpt.setText("Seleccione el departamento");
                    Label_Dpt.setForeground(Color.RED);
                }
            }
        }
    }

    private void restablecerDoptoCat() {
        idCat = 0;
        idDpto = 0;
        accion = "insert";
        TextField_Departamento.setEnabled(true);
        RadioButton_Dpt.setForeground(new Color(0, 153, 51));
        TextField_Departamento.setText("");
        TextField_BuscarDpt.setText("");
        TextField_Categoria.setEnabled(false);//cambiar
        RadioButton_Dpt.setForeground(Color.black);
        RadioButton_Dpt.setSelected(true);
        RadioButton_Cat.setSelected(false);
        RadioButton_Cat.setForeground(Color.black);
        TextField_Categoria.setText("");
        Label_Dpt.setForeground(Color.black);
        Label_Cat.setForeground(Color.black);
        departamento.searchDepartamentos(Table_Dpt, "");
        departamento.getCategorias(Table_Cat, idDpto);

    }
    private void Table_DptKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_DptKeyReleased
        if (Table_Dpt.getSelectedRows().length > 0) {
            datosDpto();
        }
    }//GEN-LAST:event_Table_DptKeyReleased

    private void Table_DptMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_DptMouseClicked
        if (Table_Dpt.getSelectedRows().length > 0) {
            datosDpto();
        }
    }//GEN-LAST:event_Table_DptMouseClicked
    private void datosDpto() {
        if (RadioButton_Dpt.isSelected()) {
            accion = "update";
        }
        int filas = Table_Dpt.getSelectedRow();
        idDpto = Integer.valueOf((String) departamento.getModelo().getValueAt(filas, 0));
        TextField_Departamento.setText((String) departamento.getModelo().getValueAt(filas, 1));
        if (RadioButton_Cat.isSelected()) {
            accion = "insert";
            Label_Dpt.setText("Departamento");
            Label_Dpt.setForeground(new Color(0, 153, 51));
        }
        Label_Dpt.setText("Departamento");
        Label_Dpt.setForeground(new Color(0, 153, 51));
        departamento.getCategorias(Table_Cat, idDpto);
    }
    private void Table_CatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_CatKeyReleased
        if (Table_Cat.getSelectedRows().length > 0) {
            datosCat();
        }
    }//GEN-LAST:event_Table_CatKeyReleased

    private void Table_CatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_CatMouseClicked
        if (Table_Cat.getSelectedRows().length > 0) {
            datosCat();
        }
    }//GEN-LAST:event_Table_CatMouseClicked
    private void datosCat() {
        accion = "update";
        int filas = Table_Cat.getSelectedRow();
        idCat = Integer.valueOf((String) departamento.getModeloCat().getValueAt(filas, 0));
        TextField_Categoria.setText((String) departamento.getModeloCat().getValueAt(filas, 1));
        Label_Cat.setText("Categoria");
        Label_Cat.setForeground(new Color(0, 153, 51));
    }
    private void Button_EliminarCatDptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_EliminarCatDptActionPerformed
        if (role.equals("Admin")) {
            eliminarDptoCat();
        } else {
            JOptionPane.showMessageDialog(null, "No cuenta con el permiso requerido");
        }
    }//GEN-LAST:event_Button_EliminarCatDptActionPerformed
    private void eliminarDptoCat() {
        if (RadioButton_Dpt.isSelected()) {
            if (idDpto > 0) {
                if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null,
                        "Estas seguro de eliminar este departamento?" + "'", "Eliminar departamento ",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                    departamento.deleteDptoCat(idDpto, idCat, "dpto");
                    restablecerDoptoCat();
                }
            }
        } else {
            if (idCat > 0) {
                if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null,
                        "Estas seguro de eliminar esta categoria??" + "'", "Eliminar categoria ",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                    departamento.deleteDptoCat(idDpto, idCat, "cat");
                    restablecerDoptoCat();
                }
            }
        }
    }
    private void Button_CancelarCatDptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_CancelarCatDptActionPerformed
        restablecerDoptoCat();
    }//GEN-LAST:event_Button_CancelarCatDptActionPerformed

    private void TextField_BuscarDptKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_BuscarDptKeyReleased
        departamento.searchDepartamentos(Table_Dpt, TextField_BuscarDpt.getText());
    }//GEN-LAST:event_TextField_BuscarDptKeyReleased
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE COMPRAS">
    private void Button_ComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_ComprasActionPerformed
        jTabbedPane1.setSelectedIndex(5);
        Button_Ventas.setEnabled(true);
        Button_Cliente.setEnabled(true);
        Button_Poductos.setEnabled(true);
        Button_Cat_Dpt.setEnabled(true);
        Button_Proveedor.setEnabled(true);
        if (role.equals("Admin")) {
            Button_Compras.setEnabled(false);
            Button_Configuracion.setEnabled(true);
        } else {
            Button_Compras.setEnabled(false);
            Button_Configuracion.setEnabled(false);
        }
        restablecerCompras();
    }//GEN-LAST:event_Button_ComprasActionPerformed

    private void TextFieldCP_DescripcionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldCP_DescripcionKeyReleased
        if (TextFieldCP_Descripcion.getText().equals("")) {
            LabelCP_Descripcion.setForeground(new Color(102, 102, 102));
        } else {
            LabelCP_Descripcion.setText("Descripción");
            LabelCP_Descripcion.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldCP_DescripcionKeyReleased

    private void TextFieldCP_DescripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldCP_DescripcionKeyTyped
        evento.textKeyPress(evt);
    }//GEN-LAST:event_TextFieldCP_DescripcionKeyTyped

    private void TextFieldCP_CantidadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldCP_CantidadKeyReleased
        if (TextFieldCP_Cantidad.getText().equals("")) {
            LabelCP_Cantidad.setForeground(new Color(102, 102, 102));
        } else {
            LabelCP_Cantidad.setText("Cantidad");
            LabelCP_Cantidad.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldCP_CantidadKeyReleased

    private void TextFieldCP_CantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldCP_CantidadKeyTyped
        evento.numberKeyPres(evt);
    }//GEN-LAST:event_TextFieldCP_CantidadKeyTyped

    private void TextFieldCP_PrecioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldCP_PrecioKeyReleased
        if (TextFieldCP_Precio.getText().equals("")) {
            LabelCP_Precio.setForeground(new Color(102, 102, 102));
        } else {
            LabelCP_Precio.setText("Precio de compra");
            LabelCP_Precio.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldCP_PrecioKeyReleased

    private void TextFieldCP_PrecioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldCP_PrecioKeyTyped
        evento.numberDecimalKeyPress(evt, TextFieldCP_Precio);
    }//GEN-LAST:event_TextFieldCP_PrecioKeyTyped

    private void Button_GuardarCliente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_GuardarCliente1ActionPerformed
        if (TabbedPane_Compras.getSelectedIndex() == 0) {
            guardarTempoCompras();
        } else {
            guardarCompras();
        }
    }//GEN-LAST:event_Button_GuardarCliente1ActionPerformed
    private void guardarTempoCompras() {
        if (TextFieldCP_Descripcion.getText().equals("")) {
            LabelCP_Descripcion.setText("Ingrese la descripción");
            LabelCP_Descripcion.setForeground(Color.RED);
            TextFieldCP_Descripcion.requestFocus();
        } else {
            if (TextFieldCP_Cantidad.getText().equals("")) {
                LabelCP_Cantidad.setText("Ingrese la cantidad");
                LabelCP_Cantidad.setForeground(Color.RED);
                TextFieldCP_Cantidad.requestFocus();
            } else {
                if (TextFieldCP_Precio.getText().equals("")) {
                    LabelCP_Precio.setText("Ingrese el precio de compra");
                    LabelCP_Precio.setForeground(Color.RED);
                    TextFieldCP_Precio.requestFocus();
                } else {
                    String des = TextFieldCP_Descripcion.getText();
                    int cant = Integer.valueOf(TextFieldCP_Cantidad.getText());
                    String precio = TextFieldCP_Precio.getText();
                    switch (accion) {
                        case "insert":
                            if (TabbedPane_Compras.getSelectedIndex() == 0) {
                                if (idProveedorCp != 0) {
                                    compra.guardarTempoCompra(des, cant, precio);
                                    restablecerCompras();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Seleccione un proveedor : ");
                                }
                            }
                            break;
                        case "update":
                            if (TabbedPane_Compras.getSelectedIndex() == 0) {
                                if (idProveedorCp != 0) {
                                    compra.updateTempoCompra(idCompra, des, cant, precio);
                                    restablecerCompras();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Seleccione un proveedor : ");
                                }
                            }
                            break;
                    }
                }
            }
        }
    }

    private void restablecerCompras() {
        tab = 5;
        TabbedPane_Compras.setSelectedIndex(0);
        TextFieldCP_Descripcion.setText("");
        TextFieldCP_Cantidad.setText("");
        TextFieldCP_Precio.setText("");
        TextFieldCP_Buscar.setText("");
        TextFieldCP_Proveedor.setText("");
        TextFieldCP_Descripcion.requestFocus();
        LabelCP_Descripcion.setText("Descripción");
        LabelCP_Descripcion.setForeground(new Color(102, 102, 102));
        LabelCP_Cantidad.setText("Cantidad");
        LabelCP_Cantidad.setForeground(new Color(102, 102, 102));
        LabelCP_Precio.setText("Precio de compra");
        LabelCP_Precio.setForeground(new Color(102, 102, 102));

        new Paginador(tab, Table_Compras, LabelCP_Paginas, 1);
        compra.searchCompras(Table_Compras, "", num_registro, pageSize);
        compra.importesTempo(LabelCP_Importe1, LabelCP_Importe2, LabelCP_TotalPagar);

        TextFieldCP_Buscar.setText("");
        TextFieldCP_Pagos.setText("");
        LabelCP_Pago.setText("Monto a pagar");
        LabelCP_Pago.setForeground(new Color(70, 106, 124));
        CheckBoxCP_Deuda.setSelected(false);
        LabelCP_ProveedorR.setText("Nombre");
        LabelCP_TotalPagar.setText("$0.00");
        LabelCP_Deuda.setText("$0.00");
        LabelCP_Saldo.setText("$0.00");
        LabelCP_Fecha.setText("--.--.--");
        LabelCP_Deudas.setText("$0.00");
        LabelCP_Proveedor.setText("Proveedor");

        idCompra = 0;
    }

    private void datosCompras() {
        accion = "update";
        int filas = Table_Compras.getSelectedRow();
        idCompra = Integer.valueOf((String) compra.getModelo().getValueAt(filas, 0));
        TextFieldCP_Descripcion.setText((String) compra.getModelo().getValueAt(filas, 1));
        TextFieldCP_Cantidad.setText((String) compra.getModelo().getValueAt(filas, 2));
        String precios = (String) compra.getModelo().getValueAt(filas, 3);
        TextFieldCP_Precio.setText(precios.replace("$", ""));
        LabelCP_Descripcion.setText("Descripción");
        LabelCP_Descripcion.setForeground(new Color(0, 153, 51));
        LabelCP_Cantidad.setText("Cantidad");
        LabelCP_Cantidad.setForeground(new Color(0, 153, 51));
        LabelCP_Precio.setText("Precio de compra");
        LabelCP_Precio.setForeground(new Color(0, 153, 51));
    }

    private void Table_ComprasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_ComprasKeyReleased
        if (Table_Compras.getSelectedRows().length > 0) {
            datosCompras();
        }
    }//GEN-LAST:event_Table_ComprasKeyReleased

    private void Table_ComprasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_ComprasMouseClicked
        if (Table_Compras.getSelectedRows().length > 0) {
            datosCompras();
        }
    }//GEN-LAST:event_Table_ComprasMouseClicked

    private void ButtonCP_PrimeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCP_PrimeroActionPerformed
        new Paginador(tab, Table_Compras, LabelCP_Paginas, 1).primero();
    }//GEN-LAST:event_ButtonCP_PrimeroActionPerformed

    private void ButtonCP_AnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCP_AnteriorActionPerformed
        new Paginador(tab, Table_Compras, LabelCP_Paginas, 0).anterior();
    }//GEN-LAST:event_ButtonCP_AnteriorActionPerformed

    private void ButtonCP_SiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCP_SiguienteActionPerformed
        new Paginador(tab, Table_Compras, LabelCP_Paginas, 0).siguiente();
    }//GEN-LAST:event_ButtonCP_SiguienteActionPerformed

    private void ButtonCP_UltimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCP_UltimoActionPerformed
        new Paginador(tab, Table_Compras, LabelCP_Paginas, 0).ultimo();
    }//GEN-LAST:event_ButtonCP_UltimoActionPerformed

    private void TextFieldCP_BuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldCP_BuscarKeyReleased
        compra.searchCompras(Table_Compras, TextFieldCP_Buscar.getText(), num_registro, pageSize);
    }//GEN-LAST:event_TextFieldCP_BuscarKeyReleased

    private void TextFieldCP_ProveedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldCP_ProveedorKeyReleased
        compra.searchProveedores(TableCP_Proveedor, TextFieldCP_Proveedor.getText());
    }//GEN-LAST:event_TextFieldCP_ProveedorKeyReleased

    private void TableCP_ProveedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableCP_ProveedorKeyReleased
        if (TableCP_Proveedor.getSelectedRows().length > 0) {
            datosCPproveedor();
        }
    }//GEN-LAST:event_TableCP_ProveedorKeyReleased

    private void TableCP_ProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableCP_ProveedorMouseClicked
        if (TableCP_Proveedor.getSelectedRows().length > 0) {
            datosCPproveedor();
        }
    }//GEN-LAST:event_TableCP_ProveedorMouseClicked
    private void datosCPproveedor() {
        int fila = TableCP_Proveedor.getSelectedRow();
        idProveedorCp = Integer.valueOf((String) compra.getModelo2().getValueAt(fila, 0));
        proveedores = (String) compra.getModelo2().getValueAt(fila, 1);
        saldoProveedor = (String) compra.getModelo2().getValueAt(fila, 4);
        LabelCP_Proveedor.setText(proveedores);
        LabelCP_ProveedorR.setText(proveedores);
        LabelCP_Fecha.setText(new Calendario().getFecha());
    }
    private void ButtonCP_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCP_EliminarActionPerformed
        if (0 < idCompra) {
            if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null,
                    "Estas seguro de eliminar esta compra?" + "'", "Eliminar compra",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                compra.deleteCompras(idCompra);
                restablecerCompras();
            }
        } else {
            if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null,
                    "Estas seguro de eliminar estas compras?" + "'", "Eliminar compras",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                compra.deleteCompras(0);
                restablecerProveedorCompras(0);
            }
        }
    }//GEN-LAST:event_ButtonCP_EliminarActionPerformed

    private void TabbedPane_ComprasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabbedPane_ComprasStateChanged
        compra.getIngresos(LabelCP_Encaja);
        compra.importesTempo(LabelCP_MontoPagar, LabelCP_Importe2, LabelCP_TotalPagar);
    }//GEN-LAST:event_TabbedPane_ComprasStateChanged

    private void TextFieldCP_PagosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldCP_PagosKeyTyped
        evento.numberDecimalKeyPress(evt, TextFieldCP_Pagos);
    }//GEN-LAST:event_TextFieldCP_PagosKeyTyped

    private void TextFieldCP_PagosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldCP_PagosKeyReleased
        compra.verificarPago(TextFieldCP_Pagos, LabelCP_Pago, CheckBoxCP_Deuda, LabelCP_Deudas,
                LabelCP_Deuda, LabelCP_Saldo, idProveedorCp);
    }//GEN-LAST:event_TextFieldCP_PagosKeyReleased

    private void CheckBoxCP_DeudaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_CheckBoxCP_DeudaStateChanged
        compra.verificarPago(TextFieldCP_Pagos, LabelCP_Pago, CheckBoxCP_Deuda, LabelCP_Deudas,
                LabelCP_Deuda, LabelCP_Saldo, idProveedorCp);
    }//GEN-LAST:event_CheckBoxCP_DeudaStateChanged
    private void guardarCompras() {
        if (TextFieldCP_Pagos.getText().equals("")) {
            LabelCP_Pago.setText("Ingrese el pago");
            LabelCP_Pago.setForeground(Color.RED);
            TextFieldCP_Pagos.requestFocus();
        } else {
            if (idProveedorCp != 0) {
                boolean valor = compra.verificarPago(TextFieldCP_Pagos, LabelCP_Pago, CheckBoxCP_Deuda,
                        LabelCP_Deudas, LabelCP_Deuda, LabelCP_Saldo, idProveedorCp);
                if (valor) {
                    compra.saveCompras(proveedores, idProveedorCp, usuario, idUsuario, role);
                    compra.deleteCompras(0);
                    restablecerProveedorCompras(1);
                }
            } else {
                LabelCP_Pago.setText("Seleccione un proveedor");
                LabelCP_Pago.setForeground(Color.RED);
            }
        }
    }

    private void restablecerProveedorCompras(int compras) {
        if (0 < compras) {
            imprimir.imprimirRecibo(PanelCP_Recibo);
        }
        idProveedorCp = 0;
        TextFieldCP_Proveedor.setText("");
        compra.searchProveedores(TableCP_Proveedor, "");
        restablecerCompras();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="CODIGO DE PRODUCTOS">
    private String codeCompra;
    private void Button_PoductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_PoductosActionPerformed
        jTabbedPane1.setSelectedIndex(3);
        Button_Ventas.setEnabled(true);
        Button_Cliente.setEnabled(true);
        Button_Poductos.setEnabled(false);
        Button_Cat_Dpt.setEnabled(true);
        Button_Proveedor.setEnabled(true);
        Button_Cat_Dpt.setEnabled(true);
        if (role.equals("Admin")) {
            Button_Compras.setEnabled(true);
            Button_Configuracion.setEnabled(true);
        } else {
            Button_Compras.setEnabled(false);
            Button_Configuracion.setEnabled(false);
        }
        restablecerProductos();
    }//GEN-LAST:event_Button_PoductosActionPerformed

    private void TablePT_ComprasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablePT_ComprasKeyReleased
        if (TablePT_Compras.getSelectedRows().length > 0) {
            datosTempoProductos();
        }
    }//GEN-LAST:event_TablePT_ComprasKeyReleased

    private void TablePT_ComprasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablePT_ComprasMouseClicked
        if (TablePT_Compras.getSelectedRows().length > 0) {
            datosTempoProductos();
        }
    }//GEN-LAST:event_TablePT_ComprasMouseClicked
    private void restablecerProductos() {
        tab = 4;
        idCompra = 0;
        funcion = 0;
        idProducto = 0;
        accion = "insert";
        producto.getProductos(TablePT_Compras, "");
        producto.getDepartamentos(ComboBox_Departamento, "");
        dpt = (Departamentos) ComboBox_Departamento.getSelectedItem();
        producto.getCategorias(ComboBox_Departamento, ComboBox_Categoria, dpt.getIdDpto(), "");
        TextFieldPT_Descripcion.setText("");
        TextFieldPT_PrecioVenta.setText("");
        LabelPT_Descripcion.setForeground(new Color(102, 102, 102));
        LabelPT_PrecioVenta.setForeground(new Color(102, 102, 102));
        producto.codeBarra(LabelPT_CodigoBarra, "000000000000", TextFieldPT_Descripcion.getText(),
                TextFieldPT_PrecioVenta.getText());
        new Paginador(tab, TablePT_Productos, LabelPT_Paginas, 1);
        producto.searchProductos(TablePT_Productos, TextFieldPT_Descripcion.getText(), num_registro, pageSize);
    }

    private void datosTempoProductos() {
        String product;
        funcion = 1;
        accion = "insert";
        int filas = TablePT_Compras.getSelectedRow();
        idCompra = Integer.valueOf((String) producto.getModelo().getValueAt(filas, 0));
        product = (String) producto.getModelo().getValueAt(filas, 1);
        TextFieldPT_Descripcion.setText(product);
        LabelPT_Producto.setText(product);
        cantidad = Integer.valueOf((String) producto.getModelo().getValueAt(filas, 2));
        LabelPT_Descripcion.setText("Descripción");
        precioCompra = (String) producto.getModelo().getValueAt(filas, 3);
        codeCompra = (String) producto.getModelo().getValueAt(filas, 7);
        LabelPT_Descripcion.setForeground(new Color(0, 153, 51));
        producto.codeBarra(LabelPT_CodigoBarra, "0", product, TextFieldPT_PrecioVenta.getText());
        producto.searchProductos(TablePT_Productos, product, num_registro, pageSize);
    }
    private void TextFieldPT_DescripcionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldPT_DescripcionKeyReleased
        if (TextFieldPT_Descripcion.getText().equals("")) {
            LabelPT_Descripcion.setForeground(new Color(102, 102, 102));
        } else {
            LabelPT_Descripcion.setText("Descripción");
            LabelPT_Descripcion.setForeground(new Color(0, 153, 51));
            if (funcion == 1) {
                producto.codeBarra(LabelPT_CodigoBarra, "0", TextFieldPT_Descripcion.getText(),
                        TextFieldPT_PrecioVenta.getText());
            }
        }
        producto.searchProductos(TablePT_Productos, TextFieldPT_Descripcion.getText(), num_registro, pageSize);
    }//GEN-LAST:event_TextFieldPT_DescripcionKeyReleased

    private void TextFieldPT_DescripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldPT_DescripcionKeyTyped
        evento.textKeyPress(evt);
    }//GEN-LAST:event_TextFieldPT_DescripcionKeyTyped

    private void TextFieldPT_PrecioVentaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldPT_PrecioVentaKeyReleased
        if (TextFieldPT_PrecioVenta.getText().equals("")) {
            LabelPT_PrecioVenta.setText("Precio venta");
            LabelPT_PrecioVenta.setForeground(new Color(102, 102, 102));
        } else {
            LabelPT_PrecioVenta.setText("Precio venta");
            LabelPT_PrecioVenta.setForeground(new Color(0, 153, 51));
            if (funcion == 1 && precioCompra != null) {
                producto.codeBarra(LabelPT_CodigoBarra, "0", TextFieldPT_Descripcion.getText(),
                        TextFieldPT_PrecioVenta.getText());
                producto.verificarPrecioVenta(LabelPT_PrecioVenta, TextFieldPT_PrecioVenta.getText(),
                        precioCompra, funcion);
            }
        }
    }//GEN-LAST:event_TextFieldPT_PrecioVentaKeyReleased

    private void TextFieldPT_PrecioVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldPT_PrecioVentaKeyTyped
        evento.numberDecimalKeyPress(evt, TextFieldPT_PrecioVenta);
    }//GEN-LAST:event_TextFieldPT_PrecioVentaKeyTyped

    private void ComboBox_DepartamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBox_DepartamentoActionPerformed
        dpt = (Departamentos) ComboBox_Departamento.getSelectedItem();
        producto.getCategorias(ComboBox_Departamento, ComboBox_Categoria, dpt.getIdDpto(), "");
    }//GEN-LAST:event_ComboBox_DepartamentoActionPerformed

    private void ButtonPT_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPT_GuardarActionPerformed
        guardarProductos();
    }//GEN-LAST:event_ButtonPT_GuardarActionPerformed
    private void guardarProductos() {
        if (TextFieldPT_Descripcion.getText().equals("")) {
            LabelPT_Descripcion.setText("Ingrese la descripción");
            LabelPT_Descripcion.setForeground(Color.RED);
        } else {
            if (TextFieldPT_PrecioVenta.getText().equals("")) {
                LabelPT_PrecioVenta.setText("Ingrese el precio de venta");
                LabelPT_PrecioVenta.setForeground(Color.RED);
            } else {
                String product = TextFieldPT_Descripcion.getText();
                String precio = TextFieldPT_PrecioVenta.getText();
                dpt = (Departamentos) ComboBox_Departamento.getSelectedItem();
                String departamento = dpt.getDepartamento();
                cat = (Categorias) ComboBox_Categoria.getSelectedItem();
                String categoria = cat.getCategoria();
                boolean verificar = producto.verificarPrecioVenta(LabelPT_PrecioVenta,
                        TextFieldPT_PrecioVenta.getText(), precioCompra, funcion);
                switch (accion) {
                    case "insert":
                        if (funcion == 1) {
                            if (verificar) {
                                producto.saveProducto(product, cantidad, precio, departamento,
                                        categoria, accion, idCompra,codeCompra);
                                imprimir.imprimirRecibo(PanelPT_CodigoBarra);
                                restablecerProductos();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Seleccione un producto");
                        }
                        break;
                    case "update":
                        if (funcion == 2) {
                            producto.saveProducto(product, cantidad, precio, departamento,
                                    categoria, accion, idProducto,codeCompra);
                            restablecerProductos();
                        } else {
                            JOptionPane.showMessageDialog(null, "Seleccione un producto");
                        }
                        break;
                }
            }
        }
    }
    private void TextField_ComprasProductosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_ComprasProductosKeyReleased
        producto.getProductos(TablePT_Compras, TextField_ComprasProductos.getText());
    }//GEN-LAST:event_TextField_ComprasProductosKeyReleased

    private void TablePT_ProductosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablePT_ProductosKeyReleased
        if (TablePT_Productos.getSelectedRows().length > 0) {
            datosProductos();
        }
    }//GEN-LAST:event_TablePT_ProductosKeyReleased

    private void TablePT_ProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablePT_ProductosMouseClicked
        if (TablePT_Productos.getSelectedRows().length > 0) {
            datosProductos();
        }
    }//GEN-LAST:event_TablePT_ProductosMouseClicked
    private void datosProductos() {
        String product, codigo, precio, departamento, categoria;
        funcion = 2;
        accion = "update";
        int filas = TablePT_Productos.getSelectedRow();
        idProducto = Integer.valueOf((String) producto.getModelo1().getValueAt(filas, 0));
        codigo = (String) producto.getModelo1().getValueAt(filas, 1);
        product = (String) producto.getModelo1().getValueAt(filas, 2);
        precio = (String) producto.getModelo1().getValueAt(filas, 3);
        TextFieldPT_Descripcion.setText(product);
        TextFieldPT_PrecioVenta.setText(precio.replace("$", ""));
        LabelPT_Descripcion.setForeground(new Color(0, 153, 51));
        LabelPT_PrecioVenta.setForeground(new Color(0, 153, 51));
        departamento = (String) producto.getModelo1().getValueAt(filas, 5);
        dpt = producto.getDepartamentos(ComboBox_Departamento, departamento);
        ComboBox_Departamento.setSelectedItem(dpt);
        categoria = (String) producto.getModelo1().getValueAt(filas, 6);
        cat = producto.getCategorias(ComboBox_Departamento, ComboBox_Categoria, dpt.getIdDpto(), categoria);
        ComboBox_Categoria.setSelectedItem(cat);
        producto.codeBarra(LabelPT_CodigoBarra, codigo, product, TextFieldPT_PrecioVenta.getText());
    }
    private void ButtonPT_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPT_CancelarActionPerformed
        restablecerProductos();
    }//GEN-LAST:event_ButtonPT_CancelarActionPerformed

    private void ButtonPT_PrimeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPT_PrimeroActionPerformed
        new Paginador(tab, TablePT_Productos, LabelPT_Paginas, 0).primero();
    }//GEN-LAST:event_ButtonPT_PrimeroActionPerformed

    private void ButtonPT_AnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPT_AnteriorActionPerformed
        new Paginador(tab, TablePT_Productos, LabelPT_Paginas, 0).anterior();
    }//GEN-LAST:event_ButtonPT_AnteriorActionPerformed

    private void ButtonPT_SiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPT_SiguienteActionPerformed
        new Paginador(tab, TablePT_Productos, LabelPT_Paginas, 0).siguiente();
    }//GEN-LAST:event_ButtonPT_SiguienteActionPerformed

    private void ButtonPT_UltimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPT_UltimoActionPerformed
        new Paginador(tab, TablePT_Productos, LabelPT_Paginas, 0).ultimo();
    }//GEN-LAST:event_ButtonPT_UltimoActionPerformed
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE VENTAS">
    private void Button_VentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_VentasActionPerformed
        jTabbedPane1.setSelectedIndex(0);
        Button_Ventas.setEnabled(false);
        Button_Cliente.setEnabled(true);
        Button_Poductos.setEnabled(true);
        Button_Cat_Dpt.setEnabled(true);
        Button_Proveedor.setEnabled(true);
        Button_Cat_Dpt.setEnabled(true);
        if (role.equals("Admin")) {
            Button_Compras.setEnabled(true);
            Button_Configuracion.setEnabled(true);
        } else {
            Button_Compras.setEnabled(false);
            Button_Configuracion.setEnabled(false);
        }
        restablecerVentas();
    }//GEN-LAST:event_Button_VentasActionPerformed

    private void Button_BuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_BuscarProductoActionPerformed
        if (TextFieldVT_BuscarProductos.getText().equals("")) {
            Label_MensajeVenta.setText("Ingrese el código del producto ");
            Label_MensajeVenta.setForeground(Color.RED);
            TextFieldVT_BuscarProductos.requestFocus();
        } else {
            List<Bodega> bodega = venta.searchBodega(TextFieldVT_BuscarProductos.getText());
            if (0 < bodega.size()) {

                Label_MensajeVenta.setText("");
                venta.saveVentasTempo(TextFieldVT_BuscarProductos.getText(), 0, cajaUser, idUsuario);
                venta.searchVentatemp(Table_VentaTempo, num_registro, pageSize);
                venta.importes(label_ImportesVentas, cajaUser, idUsuario);

            } else {
                Label_MensajeVenta.setText("El código del producto no existe ");
                Label_MensajeVenta.setForeground(Color.RED);
            }
        }
    }//GEN-LAST:event_Button_BuscarProductoActionPerformed
    private void restablecerVentas() {
        tab = 0;
        accion = "insert";
        TextField_Pagos.setText("");
        TextFieldVT_BuscarCliente.setText("");
        label_Deuda.setText("$0.00");
        label_Cambio.setText("$0.00");
        label_SuCambio.setText("Su cambio");
        label_SuCambio.setForeground(new Color(102, 102, 102));
        label_Pago.setForeground(new Color(102, 102, 102));
        if (CheckBox_Creditos.isSelected() == false) {
            label_ReciboDeuda.setText("$0.00");
            label_ReciboDeudaTotal.setText("$0.00");
            label_ReciboNombre.setText("Nombre");
            label_ReciboDeudaAnterior.setText("$0.00");
            label_ReciboUltimoPago.setText("$0.00");
            label_ReciboFecha.setText("--/--/--");
        }
        label_MensajeCliente.setText("");
        CheckBox_Creditos.setSelected(false);
        venta.searchVentatemp(Table_VentaTempo, num_registro, pageSize);
        venta.importes(label_ImportesVentas, cajaUser, idUsuario);
        venta.reportesCliente(TableVT_Cliente, TextFieldVT_BuscarCliente.getText());
        new Paginador(tab, Table_VentaTempo, LabelVT_Paginas, 1);
    }
    private void Table_VentaTempoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_VentaTempoMouseClicked
        if (Table_VentaTempo.getSelectedRows().length > 0) {
            if (evt.getClickCount() == 2) {
                int filas = Table_VentaTempo.getSelectedRow();
                String codigo = (String) venta.getModelo().getValueAt(filas, 1);
                int cantidad = Integer.valueOf((String) venta.getModelo().getValueAt(filas, 4));
                venta.deleteVenta(codigo, cantidad, cajaUser, idUsuario);
                venta.searchVentatemp(Table_VentaTempo, num_registro, pageSize);
                venta.importes(label_ImportesVentas, cajaUser, idUsuario);
            }
        }
    }//GEN-LAST:event_Table_VentaTempoMouseClicked

    private void TextField_PagosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_PagosKeyReleased
        venta.pagosCliente(TextField_Pagos, label_SuCambio, label_Cambio, label_Pago, CheckBox_Creditos);
    }//GEN-LAST:event_TextField_PagosKeyReleased

    private void TextFieldVT_BuscarClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldVT_BuscarClienteKeyReleased
        venta.reportesCliente(TableVT_Cliente, TextFieldVT_BuscarCliente.getText());
    }//GEN-LAST:event_TextFieldVT_BuscarClienteKeyReleased

    private void CheckBox_CreditosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBox_CreditosActionPerformed
        venta.dataCliente(CheckBox_Creditos, TextField_Pagos, TextFieldVT_BuscarCliente, TableVT_Cliente, labels);
        venta.reportesCliente(TableVT_Cliente, TextFieldVT_BuscarCliente.getText());
        venta.cobrar(CheckBox_Creditos, TextField_Pagos, TableVT_Cliente, labels);
        venta.pagosCliente(TextField_Pagos, label_SuCambio, label_Cambio, label_Pago, CheckBox_Creditos);
        label_MensajeCliente.setText("");
    }//GEN-LAST:event_CheckBox_CreditosActionPerformed

    private void TableVT_ClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableVT_ClienteMouseClicked
        if (TableVT_Cliente.getSelectedRows().length > 0) {
            label_MensajeCliente.setText("");
            if (CheckBox_Creditos.isSelected()) {
                if (!TextField_Pagos.getText().equalsIgnoreCase("")) {
                    venta.dataCliente(CheckBox_Creditos, TextField_Pagos, TextFieldVT_BuscarCliente,
                            TableVT_Cliente, labels);
                } else {
                    label_MensajeCliente.setText("Ingrese el pago");
                    label_MensajeCliente.setForeground(Color.RED);
                    TextField_Pagos.requestFocus();
                }
            } else {
                label_MensajeCliente.setText("Seleccione la opción de crédito");
                label_MensajeCliente.setForeground(Color.RED);
            }
        }
    }//GEN-LAST:event_TableVT_ClienteMouseClicked

    private void button_CobrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_CobrarActionPerformed
        boolean valor = venta.cobrar(CheckBox_Creditos, TextField_Pagos, TableVT_Cliente,
                labels);
        if (valor) {
            imprimir.imprimirRecibo(null);
            restablecerVentas();
        }
    }//GEN-LAST:event_button_CobrarActionPerformed

    private void ButtonVT_PrimeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonVT_PrimeroActionPerformed
        new Paginador(tab, Table_VentaTempo, LabelVT_Paginas, 1).primero();
    }//GEN-LAST:event_ButtonVT_PrimeroActionPerformed

    private void ButtonVT_AnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonVT_AnteriorActionPerformed
        new Paginador(tab, Table_VentaTempo, LabelVT_Paginas, 0).anterior();
    }//GEN-LAST:event_ButtonVT_AnteriorActionPerformed

    private void ButtonVT_SiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonVT_SiguienteActionPerformed
        new Paginador(tab, Table_VentaTempo, LabelVT_Paginas, 0).siguiente();
    }//GEN-LAST:event_ButtonVT_SiguienteActionPerformed

    private void ButtonTV_UltimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonTV_UltimoActionPerformed
        new Paginador(tab, Table_VentaTempo, LabelVT_Paginas, 0).ultimo();
    }//GEN-LAST:event_ButtonTV_UltimoActionPerformed

    private void Button_ReciboVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_ReciboVentaActionPerformed
        imprimir.imprimirRecibo(Panel_ReciboVenta);
        CheckBox_Creditos.setSelected(false);
        restablecerVentas();
    }//GEN-LAST:event_Button_ReciboVentaActionPerformed

    private void ButtonVT_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonVT_CancelarActionPerformed
        restablecerVentas();
    }//GEN-LAST:event_ButtonVT_CancelarActionPerformed

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="CODIGO DE CONFIGURACION">
    private void Button_ConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_ConfiguracionActionPerformed
        jTabbedPane1.setSelectedIndex(6);
        Button_Ventas.setEnabled(true);
        Button_Cliente.setEnabled(true);
        Button_Poductos.setEnabled(true);
        Button_Cat_Dpt.setEnabled(true);
        Button_Proveedor.setEnabled(true);
        Button_Cat_Dpt.setEnabled(true);
        if (role.equals("Admin")) {
            Button_Compras.setEnabled(true);
            Button_Configuracion.setEnabled(false);
        } else {
            Button_Compras.setEnabled(false);
            Button_Configuracion.setEnabled(false);
        }
    }//GEN-LAST:event_Button_ConfiguracionActionPerformed
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE USUARIOS">
    private Usuario objectUsusrios;
    private void Button_UsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_UsuariosActionPerformed
        tab = 8;
        jTabbedPane1.setSelectedIndex(7);
        Button_Configuracion.setEnabled(true);
        objectUsusrios = new Usuario(textFieldObject, labelsObject, ComboBoxUser_Roles, Table_Usuario);
        objectUsusrios.restablecerUsuarios();
    }//GEN-LAST:event_Button_UsuariosActionPerformed

    private void TextFieldUser_NombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldUser_NombreKeyReleased
        if (TextFieldUser_Nombre.getText().equals("")) {
            LabelUser_Nombre.setForeground(new Color(102, 102, 102));
        } else {
            LabelUser_Nombre.setText("Nombre");
            LabelUser_Nombre.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldUser_NombreKeyReleased

    private void TextFieldUser_NombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldUser_NombreKeyTyped
        evento.textKeyPress(evt);
    }//GEN-LAST:event_TextFieldUser_NombreKeyTyped

    private void TextFieldUser_ApellidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldUser_ApellidoKeyReleased
        if (TextFieldUser_Apellido.getText().equals("")) {
            LabelUser_Apellido.setForeground(new Color(102, 102, 102));
        } else {
            LabelUser_Apellido.setText("Apellido");
            LabelUser_Apellido.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldUser_ApellidoKeyReleased

    private void TextFieldUser_ApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldUser_ApellidoKeyTyped
        evento.textKeyPress(evt);
    }//GEN-LAST:event_TextFieldUser_ApellidoKeyTyped

    private void TextFieldUser_TelefonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldUser_TelefonoKeyReleased
        if (TextFieldUser_Telefono.getText().equals("")) {
            LabelUser_Telefono.setForeground(new Color(102, 102, 102));
        } else {
            LabelUser_Telefono.setText("Telefono");
            LabelUser_Telefono.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldUser_TelefonoKeyReleased

    private void TextFieldUser_TelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldUser_TelefonoKeyTyped
        evento.numberKeyPres(evt);
    }//GEN-LAST:event_TextFieldUser_TelefonoKeyTyped

    private void TextFieldUser_DireccionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldUser_DireccionKeyReleased
        if (TextFieldUser_Direccion.getText().equals("")) {
            LabelUser_Direccion.setForeground(new Color(102, 102, 102));
        } else {
            LabelUser_Direccion.setText("Direccion");
            LabelUser_Direccion.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldUser_DireccionKeyReleased

    private void TextFieldUser_EmailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldUser_EmailKeyReleased
        if (TextFieldUser_Email.getText().equals("")) {
            LabelUser_Email.setForeground(new Color(102, 102, 102));
        } else {
            LabelUser_Email.setText("Email");
            LabelUser_Email.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldUser_EmailKeyReleased

    private void TextFieldUser_PasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldUser_PasswordKeyReleased
        if (TextFieldUser_Password.getText().equals("")) {
            LabelUser_Password.setForeground(new Color(102, 102, 102));
        } else {
            LabelUser_Password.setText("Cotraseña");
            LabelUser_Password.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldUser_PasswordKeyReleased

    private void TextFieldUser_UsuarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldUser_UsuarioKeyReleased
        if (TextFieldUser_Usuario.getText().equals("")) {
            LabelUser_Usuario.setForeground(new Color(102, 102, 102));
        } else {
            LabelUser_Usuario.setText("Usuario");
            LabelUser_Usuario.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldUser_UsuarioKeyReleased

    private void ButtonUser_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonUser_GuardarActionPerformed
        if (objectUsusrios.registrarUsuario()) {
            objectUsusrios.restablecerUsuarios();
        }

    }//GEN-LAST:event_ButtonUser_GuardarActionPerformed

    private void Table_UsuarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_UsuarioKeyReleased
        if (Table_Usuario.getSelectedRows().length > 0) {
            objectUsusrios.dataTableUsuarios();
        }
    }//GEN-LAST:event_Table_UsuarioKeyReleased

    private void Table_UsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_UsuarioMouseClicked
        if (Table_Usuario.getSelectedRows().length > 0) {
            objectUsusrios.dataTableUsuarios();
        }
    }//GEN-LAST:event_Table_UsuarioMouseClicked

    private void ButtonUser_ImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonUser_ImagenActionPerformed
        ususrios.CargarImagen(LabelUser_Imagen, true, null);
    }//GEN-LAST:event_ButtonUser_ImagenActionPerformed

    private void TextFieldUser_BuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldUser_BuscarKeyReleased
        objectUsusrios.searchUsuarios(Table_Usuario, TextFieldUser_Buscar.getText(), num_registro, pageSize);
    }//GEN-LAST:event_TextFieldUser_BuscarKeyReleased

    private void ButtonUser_PrimeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonUser_PrimeroActionPerformed
        new Paginador(tab, Table_Usuario, LabelUser_Paginas, 1).primero();
    }//GEN-LAST:event_ButtonUser_PrimeroActionPerformed

    private void ButtonUser_AnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonUser_AnteriorActionPerformed
        new Paginador(tab, Table_Usuario, LabelUser_Paginas, 0).anterior();
    }//GEN-LAST:event_ButtonUser_AnteriorActionPerformed

    private void ButtonUser_SiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonUser_SiguienteActionPerformed
        new Paginador(tab, Table_Usuario, LabelUser_Paginas, 0).siguiente();
    }//GEN-LAST:event_ButtonUser_SiguienteActionPerformed

    private void ButtonUser_UltimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonUser_UltimoActionPerformed
        new Paginador(tab, Table_Usuario, LabelUser_Paginas, 0).ultimo();
    }//GEN-LAST:event_ButtonUser_UltimoActionPerformed

    private void Button_CancelarCompras1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_CancelarCompras1ActionPerformed
        objectUsusrios.restablecerUsuarios();
    }//GEN-LAST:event_Button_CancelarCompras1ActionPerformed
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE CAJAS">
    private List<JLabel> labelCajas;
    private List<JTextField> textField;
    private void Button_CajasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_CajasActionPerformed
        tab = 8;
        jTabbedPane1.setSelectedIndex(8);
        Button_Configuracion.setEnabled(true);
        objectCaja.restablecerCajas();
    }//GEN-LAST:event_Button_CajasActionPerformed

    private void ButtonCaja_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCaja_GuardarActionPerformed
        if (TabbedPane_Caja1.getSelectedIndex() == 0) {
            if (objectCaja.cajaIngresos()) {
                objectCaja.guardarIngresos();
                objectCaja.restablecerCajas();
            }
        } else {
            if (CheckBoxCaja_Ingresos.isSelected()) {
                if (TextFieldCaja_IngresoInicial.getText().equals("")) {
                    LabelCaja_IngresoInicial.setText("Ingrese el ingrso inicial");
                    LabelCaja_IngresoInicial.setForeground(new Color(102, 102, 102));
                } else {
                    objectCaja.insetarIngresoInicial();
                    CheckBoxCaja_Ingresos.setSelected(false);
                }
            } else {
                objectCaja.registrarCajas();
            }
        }
    }//GEN-LAST:event_ButtonCaja_GuardarActionPerformed

    private void Button_CancelarBodegaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_CancelarBodegaActionPerformed
        objectCaja.restablecerCajas();
        CheckBoxCaja_Ingresos.setSelected(true);
    }//GEN-LAST:event_Button_CancelarBodegaActionPerformed

    private void dateChooserCombo_CajasOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dateChooserCombo_CajasOnSelectionChange
        objectCaja.getIngresos();
    }//GEN-LAST:event_dateChooserCombo_CajasOnSelectionChange

    private void TableCajas_IngresosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableCajas_IngresosKeyReleased
        if (TableCajas_Ingresos.getSelectedRows().length > 0) {
            objectCaja.dataTableCaja();
        }
    }//GEN-LAST:event_TableCajas_IngresosKeyReleased

    private void TableCajas_IngresosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableCajas_IngresosMouseClicked
        if (TableCajas_Ingresos.getSelectedRows().length > 0) {
            objectCaja.dataTableCaja();
        }
    }//GEN-LAST:event_TableCajas_IngresosMouseClicked

    private void TextFieldCaja_RetirarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldCaja_RetirarKeyTyped
        evento.numberDecimalKeyPress(evt, TextFieldCaja_Retirar);
    }//GEN-LAST:event_TextFieldCaja_RetirarKeyTyped

    private void TextFieldCaja_RetirarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldCaja_RetirarKeyReleased
        objectCaja.cajaIngresos();
    }//GEN-LAST:event_TextFieldCaja_RetirarKeyReleased

    private void TabbedPane_Caja1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabbedPane_Caja1StateChanged
        if (TabbedPane_Caja1.getSelectedIndex() == 0) {
            TabbedPane_Caja2.setSelectedIndex(0);
        } else {
            TabbedPane_Caja2.setSelectedIndex(1);
        }
    }//GEN-LAST:event_TabbedPane_Caja1StateChanged

    private void TabbedPane_Caja2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabbedPane_Caja2StateChanged
        if (TabbedPane_Caja2.getSelectedIndex() == 0) {
            if (TabbedPane_Caja1.getSelectedIndex() > 0) {
                TabbedPane_Caja1.setSelectedIndex(0);
            }
        } else {
            TabbedPane_Caja1.setSelectedIndex(1);
        }
    }//GEN-LAST:event_TabbedPane_Caja2StateChanged

    private void TextFieldCaja_IngresoInicialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldCaja_IngresoInicialKeyReleased
        if (TextFieldCaja_IngresoInicial.getText().equals("")) {
            LabelCaja_IngresoInicial.setText("Ingreso inicial");
            LabelCaja_IngresoInicial.setForeground(new Color(102, 102, 102));
        } else {
            LabelCaja_IngresoInicial.setText("Ingreso inicial");
            LabelCaja_IngresoInicial.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldCaja_IngresoInicialKeyReleased

    private void TextFieldCaja_IngresoInicialKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldCaja_IngresoInicialKeyTyped
        evento.numberDecimalKeyPress(evt, TextFieldCaja_IngresoInicial);
    }//GEN-LAST:event_TextFieldCaja_IngresoInicialKeyTyped

    private void Spinner_CajaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_Spinner_CajaStateChanged
        LabelNum_Caja.setText("Numero de cajas");
        LabelNum_Caja.setForeground(new Color(102, 102, 102));
    }//GEN-LAST:event_Spinner_CajaStateChanged

    private void Table_CajasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_CajasKeyReleased
        if (Table_Cajas.getSelectedRows().length > 0) {
            objectCaja.dataCajaIngresos();
            CheckBoxCaja_Ingresos.setSelected(true);
        }
    }//GEN-LAST:event_Table_CajasKeyReleased

    private void Table_CajasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_CajasMouseClicked
        if (Table_Cajas.getSelectedRows().length > 0) {
            objectCaja.dataCajaIngresos();
            CheckBoxCaja_Ingresos.setSelected(true);
        }
    }//GEN-LAST:event_Table_CajasMouseClicked

    private void CheckBoxCaja_IngresosStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_CheckBoxCaja_IngresosStateChanged
        if (CheckBoxCaja_Ingresos.isSelected()) {
            timer1.stop();
        } else {
            timer1.start();
            objectCaja.restablecerCajas();
        }
        LabelCaja_IngresoInicial.setText("Ingreso inicial");
        LabelCaja_IngresoInicial.setForeground(new Color(102, 102, 102));
    }//GEN-LAST:event_CheckBoxCaja_IngresosStateChanged

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="CODIGO DE INVENTARIO">
    private boolean value = false;
    Inventario inventario;
    private List<JLabel> labelBodega;
    private List<JTextField> textFieldBodega;
    private void Button_InventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_InventarioActionPerformed
        tab = 9;
        value = false;
        jTabbedPane1.setSelectedIndex(9);
        Button_Configuracion.setEnabled(true);
        inventario.restablecerBodega();

    }//GEN-LAST:event_Button_InventarioActionPerformed

    private void TextFieldInv_BuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldInv_BuscarKeyReleased
        if (TextFieldInv_Buscar.getText().equals("")) {
            timer1.start();
        } else {
            timer1.stop();
        }
        switch (TabbedPane_Inv.getSelectedIndex()) {
            case 0:
                inventario.getBodega(TextFieldInv_Buscar.getText(), num_registro, pageSize);
                break;
            case 1:
                inventario.getProductos(TextFieldInv_Buscar.getText(), num_registro, pageSize);
                break;
            case 2:
                inventario.searchVentas(TextFieldInv_Buscar.getText(), num_registro, pageSize);
                break;
        }
    }//GEN-LAST:event_TextFieldInv_BuscarKeyReleased

    private void TextFieldBodega_ExistenciaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldBodega_ExistenciaKeyReleased
        if (TextFieldBodega_Existencia.getText().equals("")) {
            LabelBodega_Existencia.setForeground(new Color(102, 102, 102));
        } else {
            LabelBodega_Existencia.setText("Existencia");
            LabelBodega_Existencia.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldBodega_ExistenciaKeyReleased

    private void Table_BodegaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_BodegaMouseClicked
        if (Table_Bodega.getSelectedRows().length > 0) {
            timer1.stop();
            inventario.dataTableBodega();
        }
    }//GEN-LAST:event_Table_BodegaMouseClicked

    private void Table_BodegaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_BodegaKeyReleased
        if (Table_Bodega.getSelectedRows().length > 0) {
            timer1.stop();
            inventario.dataTableBodega();
        }
    }//GEN-LAST:event_Table_BodegaKeyReleased

    private void ButtonBodega_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonBodega_CancelarActionPerformed
        timer1.start();
        inventario.restablecerBodega();
    }//GEN-LAST:event_ButtonBodega_CancelarActionPerformed

    private void ButtonInvr_GuardarBodegaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvr_GuardarBodegaActionPerformed
        timer1.start();
        inventario.updateExistencia();
        inventario.restablecerBodega();
    }//GEN-LAST:event_ButtonInvr_GuardarBodegaActionPerformed

    private void TextFieldBodega_ExistenciaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldBodega_ExistenciaKeyTyped
        evento.numberKeyPres(evt);
    }//GEN-LAST:event_TextFieldBodega_ExistenciaKeyTyped

    private void ButtonBodega_ExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonBodega_ExcelActionPerformed
        String[] title = {"Codigo", "Producto", "Existencia"};
        int[] colum = {0, 4};
        new ExportData().exportarDataExcel(Table_Bodega, title, colum, "Bodega");
    }//GEN-LAST:event_ButtonBodega_ExcelActionPerformed

    private void ButtonBodega_PDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonBodega_PDFActionPerformed
        String[] title = {"Codigo", "Producto", "Existencia"};
        int[] colum = {0, 4};
        new ExportData().exportarDataPdf(Table_Bodega, title, colum, "Productos en inventario");
    }//GEN-LAST:event_ButtonBodega_PDFActionPerformed

    private void ButtonInvBodega_PrimeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvBodega_PrimeroActionPerformed
        timer1.start();
        new Paginador(tab, Table_Bodega, LabelInvBodega_Paginas, 1).primero();
    }//GEN-LAST:event_ButtonInvBodega_PrimeroActionPerformed

    private void ButtonInvBodega_AnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvBodega_AnteriorActionPerformed
        timer1.stop();
        new Paginador(tab, Table_Bodega, LabelInvBodega_Paginas, 0).anterior();
    }//GEN-LAST:event_ButtonInvBodega_AnteriorActionPerformed

    private void ButtonInvBodega_SiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvBodega_SiguienteActionPerformed
        timer1.stop();
        new Paginador(tab, Table_Bodega, LabelInvBodega_Paginas, 0).siguiente();
    }//GEN-LAST:event_ButtonInvBodega_SiguienteActionPerformed

    private void ButtonInvBodega_UltimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvBodega_UltimoActionPerformed
        timer1.stop();
        new Paginador(tab, Table_Bodega, LabelInvBodega_Paginas, 0).ultimo();
    }//GEN-LAST:event_ButtonInvBodega_UltimoActionPerformed

    private void TextFieldInv_PrecioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldInv_PrecioKeyReleased
        if (TextFieldInv_Precio.getText().equals("")) {
            LabelInv_Precio.setForeground(new Color(102, 102, 102));
        } else {
            LabelInv_Precio.setText("Precio");
            LabelInv_Precio.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldInv_PrecioKeyReleased

    private void TextFieldInv_PrecioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldInv_PrecioKeyTyped
        evento.numberDecimalKeyPress(evt, TextFieldInv_Precio);
    }//GEN-LAST:event_TextFieldInv_PrecioKeyTyped

    private void ButtonInvProductos_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvProductos_CancelarActionPerformed
        timer1.start();
        inventario.restablecerBodega();
    }//GEN-LAST:event_ButtonInvProductos_CancelarActionPerformed

    private void ButtonInv_GuardarProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInv_GuardarProductosActionPerformed
        inventario.updateProductos();
        timer1.start();
    }//GEN-LAST:event_ButtonInv_GuardarProductosActionPerformed

    private void ButtonInvProducto_ExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvProducto_ExcelActionPerformed
        if (TableInv_Productos.getSelectedRows().length > 0) {
            timer1.stop();
            String[] title = {"Codigo", "Producto", "Precio", "Descuento", "Departamento", "Categoria"};
            int[] colum = {0};
            new ExportData().exportarDataExcel(TableInv_Productos, title, colum, "Lista de roductos");
        }
    }//GEN-LAST:event_ButtonInvProducto_ExcelActionPerformed

    private void ButtonInvProducto_PDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvProducto_PDFActionPerformed
        if (TableInv_Productos.getSelectedRows().length > 0) {
            timer1.stop();
            String[] title = {"Codigo", "Producto", "Precio", "Descuento", "Departamento", "Categoria"};
            int[] colum = {0};
            new ExportData().exportarDataPdf(TableInv_Productos, title, colum, "Lista de roductos");
        }
    }//GEN-LAST:event_ButtonInvProducto_PDFActionPerformed

    private void TableInv_ProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableInv_ProductosMouseClicked
        if (TableInv_Productos.getSelectedRows().length > 0) {
            timer1.stop();
            inventario.dataTableProductos();
        }
    }//GEN-LAST:event_TableInv_ProductosMouseClicked

    private void TableInv_ProductosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableInv_ProductosKeyReleased
        if (TableInv_Productos.getSelectedRows().length > 0) {
            timer1.stop();
            inventario.dataTableProductos();
        }
    }//GEN-LAST:event_TableInv_ProductosKeyReleased

    private void ButtonInvProducto_PrimeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvProducto_PrimeroActionPerformed
        timer1.start();
        new Paginador(10, TableInv_Productos, LabelInvProductos_Paginas, 1).primero();
    }//GEN-LAST:event_ButtonInvProducto_PrimeroActionPerformed

    private void ButtonInvProducto_AnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvProducto_AnteriorActionPerformed
        timer1.stop();
        new Paginador(10, TableInv_Productos, LabelInvProductos_Paginas, 0).anterior();
    }//GEN-LAST:event_ButtonInvProducto_AnteriorActionPerformed

    private void ButtonInvProducto_SiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvProducto_SiguienteActionPerformed
        timer1.stop();
        new Paginador(10, TableInv_Productos, LabelInvProductos_Paginas, 0).siguiente();
    }//GEN-LAST:event_ButtonInvProducto_SiguienteActionPerformed

    private void ButtonInvProducto_UltimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvProducto_UltimoActionPerformed
        timer1.stop();
        new Paginador(10, TableInv_Productos, LabelInvProductos_Paginas, 0).ultimo();
    }//GEN-LAST:event_ButtonInvProducto_UltimoActionPerformed

    private void TextFieldInv_DescuentoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldInv_DescuentoKeyReleased
        if (TextFieldInv_Descuento.getText().equals("")) {
            LabelInv_Descuento.setForeground(new Color(102, 102, 102));
        } else {
            LabelInv_Descuento.setText("Descuento");
            LabelInv_Descuento.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextFieldInv_DescuentoKeyReleased

    private void TextFieldInv_DescuentoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldInv_DescuentoKeyTyped
        evento.numberDecimalKeyPress(evt, TextFieldInv_Precio);
    }//GEN-LAST:event_TextFieldInv_DescuentoKeyTyped

    private void TabbedPane_InvStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabbedPane_InvStateChanged
        if (TabbedPane_Inv.getSelectedIndex() == 0) {
            if (value) {
                inventario.restablecerBodega();
            }
        } else {
            inventario.restablecerBodega();
            value = true;
        }
    }//GEN-LAST:event_TabbedPane_InvStateChanged

    private void ButtonInvVenta_PrimeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvVenta_PrimeroActionPerformed
        new Paginador(11, TableInv_Ventas, LabelInvVentas_Paginas, 1).primero();
    }//GEN-LAST:event_ButtonInvVenta_PrimeroActionPerformed

    private void ButtonInvVenta_AnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvVenta_AnteriorActionPerformed
        new Paginador(11, TableInv_Ventas, LabelInvVentas_Paginas, 1).anterior();
    }//GEN-LAST:event_ButtonInvVenta_AnteriorActionPerformed

    private void ButtonInvVenta_SiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvVenta_SiguienteActionPerformed
        new Paginador(11, TableInv_Ventas, LabelInvVentas_Paginas, 1).siguiente();
    }//GEN-LAST:event_ButtonInvVenta_SiguienteActionPerformed

    private void ButtonInvVenta_UltimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvVenta_UltimoActionPerformed
        new Paginador(11, TableInv_Ventas, LabelInvVentas_Paginas, 1).ultimo();
    }//GEN-LAST:event_ButtonInvVenta_UltimoActionPerformed

    private void ButtonInvProductos_Cancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvProductos_Cancelar1ActionPerformed
        inventario.restablecerBodega();
    }//GEN-LAST:event_ButtonInvProductos_Cancelar1ActionPerformed

    private void ButtonInvProducto_Excel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvProducto_Excel1ActionPerformed
        if (TableInv_Ventas.getSelectedRows().length > 0) {
            String[] title = {"Codigo", "Descripcion", "Precio", "Cantidad", "Importe", "Año"};
            int[] colum = {0, 6, 7, 9, 10, 11, 12};
            new ExportData().exportarDataExcel(TableInv_Ventas, title, colum, "Historial de ventas");
        }
    }//GEN-LAST:event_ButtonInvProducto_Excel1ActionPerformed

    private void ButtonInvProducto_PDF1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInvProducto_PDF1ActionPerformed
        if (TableInv_Ventas.getSelectedRows().length > 0) {
            String[] title = {"Codigo", "Descripcion", "Precio", "Cantidad", "Importe", "Año"};
            int[] colum = {0, 6, 7, 9, 10, 11, 12};
            new ExportData().exportarDataPdf(TableInv_Ventas, title, colum, "Historial de ventas");
        }
    }//GEN-LAST:event_ButtonInvProducto_PDF1ActionPerformed

    private void dateChooserCombo_InicioOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dateChooserCombo_InicioOnSelectionChange
        inventario.searchVentas("", 0, pageSize);
    }//GEN-LAST:event_dateChooserCombo_InicioOnSelectionChange

    private void dateChooserCombo_FinalOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dateChooserCombo_FinalOnSelectionChange
        inventario.searchVentas("", 0, pageSize);
    }//GEN-LAST:event_dateChooserCombo_FinalOnSelectionChange

    private void CheckBox_MaxVentasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_CheckBox_MaxVentasStateChanged
        inventario.searchVentas("", 0, pageSize);
    }//GEN-LAST:event_CheckBox_MaxVentasStateChanged
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE REPORTES">
    Reportes _report;
    private Departamentos _dptReport;
    private void TextFieldInv_Buscar1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldInv_Buscar1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_TextFieldInv_Buscar1KeyReleased

    private void TabbedPane_Inv1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabbedPane_Inv1StateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_TabbedPane_Inv1StateChanged

    private void Button_ReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_ReportesActionPerformed
        jTabbedPane1.setSelectedIndex(10);
        Button_Configuracion.setEnabled(true);
        _report.restablecer();
    }//GEN-LAST:event_Button_ReportesActionPerformed

    private void ComboBox_ReportDtopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBox_ReportDtopActionPerformed
        _dptReport = (Departamentos) ComboBox_ReportDtop.getSelectedItem();
         _report.getCategorias(_dptReport.getIdDpto());
        _report.getProductos("");
    }//GEN-LAST:event_ComboBox_ReportDtopActionPerformed

    private void TableReport_ProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableReport_ProductosMouseClicked
         if (TableReport_Productos.getSelectedRows().length > 0) {
            _report.getCodigo();
        }
    }//GEN-LAST:event_TableReport_ProductosMouseClicked

    private void TableReport_ProductosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableReport_ProductosKeyReleased
         if (TableReport_Productos.getSelectedRows().length > 0) {
            _report.getCodigo();
        }
    }//GEN-LAST:event_TableReport_ProductosKeyReleased

    private void ComboBox_ReportCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBox_ReportCatActionPerformed
        _report.getProductos("");
    }//GEN-LAST:event_ComboBox_ReportCatActionPerformed

    private void TableReport_InformeProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableReport_InformeProductoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TableReport_InformeProductoMouseClicked

    private void TableReport_InformeProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableReport_InformeProductoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_TableReport_InformeProductoKeyReleased

    private void TableReport_produtoBodegaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableReport_produtoBodegaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TableReport_produtoBodegaMouseClicked

    private void TableReport_produtoBodegaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableReport_produtoBodegaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_TableReport_produtoBodegaKeyReleased

    // </editor-fold>
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sistema(null, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonBodega_Cancelar;
    private javax.swing.JButton ButtonBodega_Excel;
    private javax.swing.JButton ButtonBodega_PDF;
    private javax.swing.JButton ButtonCP_Anterior;
    private javax.swing.JButton ButtonCP_Eliminar;
    private javax.swing.JButton ButtonCP_Primero;
    private javax.swing.JButton ButtonCP_Siguiente;
    private javax.swing.JButton ButtonCP_Ultimo;
    private javax.swing.JButton ButtonCaja_Guardar;
    private javax.swing.JButton ButtonInvBodega_Anterior;
    private javax.swing.JButton ButtonInvBodega_Primero;
    private javax.swing.JButton ButtonInvBodega_Siguiente;
    private javax.swing.JButton ButtonInvBodega_Ultimo;
    private javax.swing.JButton ButtonInvProducto_Anterior;
    private javax.swing.JButton ButtonInvProducto_Excel;
    private javax.swing.JButton ButtonInvProducto_Excel1;
    private javax.swing.JButton ButtonInvProducto_PDF;
    private javax.swing.JButton ButtonInvProducto_PDF1;
    private javax.swing.JButton ButtonInvProducto_Primero;
    private javax.swing.JButton ButtonInvProducto_Siguiente;
    private javax.swing.JButton ButtonInvProducto_Ultimo;
    private javax.swing.JButton ButtonInvProductos_Cancelar;
    private javax.swing.JButton ButtonInvProductos_Cancelar1;
    private javax.swing.JButton ButtonInvVenta_Anterior;
    private javax.swing.JButton ButtonInvVenta_Primero;
    private javax.swing.JButton ButtonInvVenta_Siguiente;
    private javax.swing.JButton ButtonInvVenta_Ultimo;
    private javax.swing.JButton ButtonInv_GuardarProductos;
    private javax.swing.JButton ButtonInvr_GuardarBodega;
    private javax.swing.JButton ButtonPT_Anterior;
    private javax.swing.JButton ButtonPT_Cancelar;
    private javax.swing.JButton ButtonPT_Guardar;
    private javax.swing.JButton ButtonPT_Primero;
    private javax.swing.JButton ButtonPT_Siguiente;
    private javax.swing.JButton ButtonPT_Ultimo;
    private javax.swing.JButton ButtonPd_Anterior;
    private javax.swing.JButton ButtonPd_Cancelar;
    private javax.swing.JButton ButtonPd_Eliminar;
    private javax.swing.JButton ButtonPd_Factura;
    private javax.swing.JButton ButtonPd_Primero;
    private javax.swing.JButton ButtonPd_Siguiente;
    private javax.swing.JButton ButtonPd_Ultimo;
    private javax.swing.JButton ButtonTV_Ultimo;
    private javax.swing.JButton ButtonUser_Anterior;
    private javax.swing.JButton ButtonUser_Guardar;
    private javax.swing.JButton ButtonUser_Imagen;
    private javax.swing.JButton ButtonUser_Primero;
    private javax.swing.JButton ButtonUser_Siguiente;
    private javax.swing.JButton ButtonUser_Ultimo;
    private javax.swing.JButton ButtonVT_Anterior;
    private javax.swing.JButton ButtonVT_Cancelar;
    private javax.swing.JButton ButtonVT_Primero;
    private javax.swing.JButton ButtonVT_Siguiente;
    private javax.swing.JButton Button_AnteriorCLT;
    private javax.swing.JButton Button_BuscarProducto;
    private javax.swing.JButton Button_Cajas;
    private javax.swing.JButton Button_CancelarBodega;
    private javax.swing.JButton Button_CancelarCLT;
    private javax.swing.JButton Button_CancelarCatDpt;
    private javax.swing.JButton Button_CancelarCompras;
    private javax.swing.JButton Button_CancelarCompras1;
    private javax.swing.JButton Button_Cat_Dpt;
    private javax.swing.JButton Button_Cliente;
    private javax.swing.JButton Button_Compras;
    private javax.swing.JButton Button_Configuracion;
    private javax.swing.JButton Button_EliminarCLT;
    private javax.swing.JButton Button_EliminarCatDpt;
    private javax.swing.JButton Button_FacturaCliente;
    private javax.swing.JButton Button_GuardarCatDpt;
    private javax.swing.JButton Button_GuardarCliente;
    private javax.swing.JButton Button_GuardarCliente1;
    private javax.swing.JButton Button_GuardarProveedor;
    private javax.swing.JButton Button_Inventario;
    private javax.swing.JButton Button_Poductos;
    private javax.swing.JButton Button_PrimeroCLT;
    private javax.swing.JButton Button_Proveedor;
    private javax.swing.JButton Button_ReciboVenta;
    private javax.swing.JButton Button_Reportes;
    private javax.swing.JButton Button_SiguienteCLT;
    private javax.swing.JButton Button_UltimoCLT;
    private javax.swing.JButton Button_Usuarios;
    private javax.swing.JButton Button_Ventas;
    private javax.swing.JCheckBox CheckBoxBodega_Existencia;
    private javax.swing.JCheckBox CheckBoxCP_Deuda;
    private javax.swing.JCheckBox CheckBoxCaja_Ingresos;
    private javax.swing.JCheckBox CheckBox_Creditos;
    private javax.swing.JCheckBox CheckBox_MaxVentas;
    private javax.swing.JComboBox<String> ComboBoxUser_Roles;
    private javax.swing.JComboBox ComboBox_Categoria;
    private javax.swing.JComboBox ComboBox_Departamento;
    private javax.swing.JComboBox ComboBox_ReportCat;
    private javax.swing.JComboBox ComboBox_ReportDtop;
    private javax.swing.JLabel Label;
    private javax.swing.JLabel Label1;
    private javax.swing.JLabel LabelBodega_Existencia;
    private javax.swing.JLabel LabelCP_Cantidad;
    private javax.swing.JLabel LabelCP_Descripcion;
    private javax.swing.JLabel LabelCP_Deuda;
    private javax.swing.JLabel LabelCP_Deudas;
    private javax.swing.JLabel LabelCP_Encaja;
    private javax.swing.JLabel LabelCP_Fecha;
    private javax.swing.JLabel LabelCP_Importe1;
    private javax.swing.JLabel LabelCP_Importe2;
    private javax.swing.JLabel LabelCP_ImporteCompra;
    private javax.swing.JLabel LabelCP_MontoPagar;
    private javax.swing.JLabel LabelCP_Paginas;
    private javax.swing.JLabel LabelCP_Pago;
    private javax.swing.JLabel LabelCP_Precio;
    private javax.swing.JLabel LabelCP_Proveedor;
    private javax.swing.JLabel LabelCP_ProveedorR;
    private javax.swing.JLabel LabelCP_Saldo;
    private javax.swing.JLabel LabelCP_TotalPagar;
    private javax.swing.JLabel LabelCaja_Ingreso;
    private javax.swing.JLabel LabelCaja_IngresoInicial;
    private javax.swing.JLabel LabelCaja_Ingresos;
    private javax.swing.JLabel LabelCaja_Numero;
    private javax.swing.JLabel LabelCaja_Retirar;
    private javax.swing.JLabel LabelGrafica_Producto;
    private javax.swing.JLabel LabelInvBodega_Paginas;
    private javax.swing.JLabel LabelInvProductos_Paginas;
    private javax.swing.JLabel LabelInvVentas_Paginas;
    private javax.swing.JLabel LabelInv_Descuento;
    private javax.swing.JLabel LabelInv_Descuento1;
    private javax.swing.JLabel LabelInv_Precio;
    private javax.swing.JLabel LabelInv_Precio1;
    private javax.swing.JLabel LabelInv_Productos;
    private javax.swing.JLabel LabelInv_Productos1;
    private javax.swing.JLabel LabelNum_Caja;
    private javax.swing.JLabel LabelPT_CodigoBarra;
    private javax.swing.JLabel LabelPT_Descripcion;
    private javax.swing.JLabel LabelPT_Paginas;
    private javax.swing.JLabel LabelPT_PrecioVenta;
    private javax.swing.JLabel LabelPT_Producto;
    private javax.swing.JLabel LabelPd_Email;
    private javax.swing.JLabel LabelPd_FechaPago;
    private javax.swing.JLabel LabelPd_Paginas;
    private javax.swing.JLabel LabelPd_Pagos;
    private javax.swing.JLabel LabelPd_Proveedor;
    private javax.swing.JLabel LabelPd_SaldoActual;
    private javax.swing.JLabel LabelPd_Telefono;
    private javax.swing.JLabel LabelPd_UltimoPago;
    private javax.swing.JLabel LabelUser_Apellido;
    private javax.swing.JLabel LabelUser_Direccion;
    private javax.swing.JLabel LabelUser_Email;
    private javax.swing.JLabel LabelUser_Imagen;
    private javax.swing.JLabel LabelUser_Nombre;
    private javax.swing.JLabel LabelUser_Paginas;
    private javax.swing.JLabel LabelUser_Password;
    private javax.swing.JLabel LabelUser_Telefono;
    private javax.swing.JLabel LabelUser_Usuario;
    private javax.swing.JLabel LabelUser_Usuario1;
    private javax.swing.JLabel LabelUser_Usuario3;
    private javax.swing.JLabel LabelUser_Usuario4;
    private javax.swing.JLabel LabelUser_Usuario5;
    private javax.swing.JLabel LabelVT_Paginas;
    private javax.swing.JLabel Label_ApellidoCLT;
    private javax.swing.JLabel Label_ApellidoCliente;
    private javax.swing.JLabel Label_ApellidoCliente1;
    private javax.swing.JLabel Label_ApellidoCliente2;
    private javax.swing.JLabel Label_Caja;
    private javax.swing.JLabel Label_Cat;
    private javax.swing.JLabel Label_CategoriaPDT;
    private javax.swing.JLabel Label_CategoriaPDT1;
    private javax.swing.JLabel Label_DepartamentoPDT;
    private javax.swing.JLabel Label_DepartamentoPDT1;
    private javax.swing.JLabel Label_DireccionCliente;
    private javax.swing.JLabel Label_Dpt;
    private javax.swing.JLabel Label_FechaPagoCLT;
    private javax.swing.JLabel Label_IdCliente;
    private javax.swing.JLabel Label_MensajeVenta;
    private javax.swing.JLabel Label_NombreCLT;
    private javax.swing.JLabel Label_NombreCliente;
    private javax.swing.JLabel Label_PaginasClientes;
    private javax.swing.JLabel Label_PagoCliente;
    private javax.swing.JLabel Label_ProveedorRB;
    private javax.swing.JLabel Label_SaldoActualCLT;
    private javax.swing.JLabel Label_TelefonoCliente;
    private javax.swing.JLabel Label_TelefonoCliente2;
    private javax.swing.JLabel Label_TelefonoCliente3;
    private javax.swing.JLabel Label_UltimoPagoCLT;
    private javax.swing.JLabel Label_Usuario;
    private javax.swing.JPanel PanelBanner;
    private javax.swing.JPanel PanelCP_Recibo;
    private javax.swing.JPanel PanelPT_CodigoBarra;
    private javax.swing.JPanel PanelPd_Recibo;
    private javax.swing.JPanel Panel_ReciboCliente;
    private javax.swing.JPanel Panel_ReciboVenta;
    private javax.swing.JRadioButton RadioButtonPd_Ingresar;
    private javax.swing.JRadioButton RadioButtonPd_Pagos;
    private javax.swing.JRadioButton RadioButton_Cat;
    private javax.swing.JRadioButton RadioButton_Dpt;
    private javax.swing.JRadioButton RadioButton_IngresarCliente;
    private javax.swing.JRadioButton RadioButton_PagosCliente;
    private javax.swing.JSpinner SpinnerBodega_Existencia;
    private javax.swing.JSpinner Spinner_Caja;
    private javax.swing.JTabbedPane TabbedPane_Caja1;
    private javax.swing.JTabbedPane TabbedPane_Caja2;
    private javax.swing.JTabbedPane TabbedPane_Compras;
    private javax.swing.JTabbedPane TabbedPane_Inv;
    private javax.swing.JTabbedPane TabbedPane_Inv1;
    private javax.swing.JTable TableCP_Proveedor;
    private javax.swing.JTable TableCajas_Ingresos;
    private javax.swing.JTable TableInv_Productos;
    private javax.swing.JTable TableInv_Ventas;
    private javax.swing.JTable TablePT_Compras;
    private javax.swing.JTable TablePT_Productos;
    private javax.swing.JTable TablePd_Reportes;
    private javax.swing.JTable TableReport_InformeProducto;
    private javax.swing.JTable TableReport_Productos;
    private javax.swing.JTable TableReport_produtoBodega;
    private javax.swing.JTable TableVT_Cliente;
    private javax.swing.JTable Table_Bodega;
    private javax.swing.JTable Table_Cajas;
    private javax.swing.JTable Table_Cat;
    private javax.swing.JTable Table_Clientes;
    private javax.swing.JTable Table_Compras;
    private javax.swing.JTable Table_Dpt;
    private javax.swing.JTable Table_Proveedor;
    private javax.swing.JTable Table_ReportesCLT;
    private javax.swing.JTable Table_Usuario;
    private javax.swing.JTable Table_VentaTempo;
    private javax.swing.JTextField TextFieldBodega_Existencia;
    private javax.swing.JTextField TextFieldCP_Buscar;
    private javax.swing.JTextField TextFieldCP_Cantidad;
    private javax.swing.JTextField TextFieldCP_Descripcion;
    private javax.swing.JTextField TextFieldCP_Pagos;
    private javax.swing.JTextField TextFieldCP_Precio;
    private javax.swing.JTextField TextFieldCP_Proveedor;
    private javax.swing.JTextField TextFieldCaja_IngresoInicial;
    private javax.swing.JTextField TextFieldCaja_Retirar;
    private javax.swing.JTextField TextFieldInv_Buscar;
    private javax.swing.JTextField TextFieldInv_Buscar1;
    private javax.swing.JTextField TextFieldInv_Descuento;
    private javax.swing.JTextField TextFieldInv_Precio;
    private javax.swing.JTextField TextFieldPT_Descripcion;
    private javax.swing.JTextField TextFieldPT_PrecioVenta;
    private javax.swing.JTextField TextFieldPd_Buscar;
    private javax.swing.JTextField TextFieldPd_Email;
    private javax.swing.JTextField TextFieldPd_Pagos;
    private javax.swing.JTextField TextFieldPd_Proveedor;
    private javax.swing.JTextField TextFieldPd_Telefono;
    private javax.swing.JTextField TextFieldUser_Apellido;
    private javax.swing.JTextField TextFieldUser_Buscar;
    private javax.swing.JTextField TextFieldUser_Direccion;
    private javax.swing.JTextField TextFieldUser_Email;
    private javax.swing.JTextField TextFieldUser_Nombre;
    private javax.swing.JTextField TextFieldUser_Password;
    private javax.swing.JTextField TextFieldUser_Telefono;
    private javax.swing.JTextField TextFieldUser_Usuario;
    private javax.swing.JTextField TextFieldVT_BuscarCliente;
    private javax.swing.JTextField TextFieldVT_BuscarProductos;
    private javax.swing.JTextField TextField_ApellidioCliente;
    private javax.swing.JTextField TextField_BuscarCliente;
    private javax.swing.JTextField TextField_BuscarDpt;
    private javax.swing.JTextField TextField_Categoria;
    private javax.swing.JTextField TextField_ComprasProductos;
    private javax.swing.JTextField TextField_Departamento;
    private javax.swing.JTextField TextField_DireccioCliente;
    private javax.swing.JTextField TextField_IdCliente;
    private javax.swing.JTextField TextField_NombreCliente;
    private javax.swing.JTextField TextField_Pagos;
    private javax.swing.JTextField TextField_PagosCliente;
    private javax.swing.JTextField TextField_TelefonoCliente;
    private javax.swing.JButton button_Cobrar;
    private datechooser.beans.DateChooserCombo dateChooserCombo_Cajas;
    private datechooser.beans.DateChooserCombo dateChooserCombo_Final;
    private datechooser.beans.DateChooserCombo dateChooserCombo_Inicio;
    private datechooser.beans.DateChooserDialog dateChooserDialog1;
    private datechooser.beans.DateChooserDialog dateChooserDialog2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel64;
    private javax.swing.JPanel jPanel65;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel70;
    private javax.swing.JPanel jPanel71;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JLabel labelVT_IngresoIni;
    private javax.swing.JLabel labelVT_IngresoTotal;
    private javax.swing.JLabel labelVT_IngresosVt;
    private javax.swing.JLabel label_Cambio;
    private javax.swing.JLabel label_Deuda;
    private javax.swing.JLabel label_ImportesVentas;
    private javax.swing.JLabel label_MensajeCliente;
    private javax.swing.JLabel label_Pago;
    private javax.swing.JLabel label_ReciboDeuda;
    private javax.swing.JLabel label_ReciboDeudaAnterior;
    private javax.swing.JLabel label_ReciboDeudaTotal;
    private javax.swing.JLabel label_ReciboFecha;
    private javax.swing.JLabel label_ReciboNombre;
    private javax.swing.JLabel label_ReciboUltimoPago;
    private javax.swing.JLabel label_SuCambio;
    private javax.swing.JLabel label_SuCambio1;
    // End of variables declaration//GEN-END:variables
}
