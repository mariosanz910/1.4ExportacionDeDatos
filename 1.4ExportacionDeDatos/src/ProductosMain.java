import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProductosMain {
    public static void main(String[] args) {
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto(1, "Portátil", 850.0, 10));
        productos.add(new Producto(2, "Ratón", 25.5, 50));
        productos.add(new Producto(3, "Teclado", 45.9, 30));
        productos.add(new Producto(4, "Monitor", 210.3, 15));
        productos.add(new Producto(5, "Impresora", 130.0, 12));

        // ==================== EXPORTAR A CSV ====================
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("datos/productos.csv"))) {
            bw.write("ID;Nombre;Precio;Cantidad\n");
            double totalPrecio = 0;
            int totalCantidad = 0;

            for (Producto p : productos) {
                bw.write(p.id + ";" + p.nombre + ";" + p.precio + ";" + p.cantidad + "\n");
                totalPrecio += p.precio;
                totalCantidad += p.cantidad;
            }

            bw.write("# Precio medio;" + String.format("%.2f", totalPrecio / productos.size()) + "\n");
            bw.write("# Cantidad total;" + totalCantidad + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // ==================== EXPORTAR A XML ====================
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("datos/productos.xml"))) {
            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            double totalPrecio = 0;
            int totalCantidad = 0;

            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bw.write("<productos>\n");
            bw.write("  <metadata>\n");
            bw.write("    <fecha>" + fecha + "</fecha>\n");
            bw.write("    <totalProductos>" + productos.size() + "</totalProductos>\n");
            bw.write("  </metadata>\n");
            bw.write("  <lista>\n");

            for (Producto p : productos) {
                bw.write("    <producto id=\"" + p.id + "\">\n");
                bw.write("      <nombre>" + p.nombre + "</nombre>\n");
                bw.write("      <precio>" + p.precio + "</precio>\n");
                bw.write("      <cantidad>" + p.cantidad + "</cantidad>\n");
                bw.write("    </producto>\n");
                totalPrecio += p.precio;
                totalCantidad += p.cantidad;
            }

            bw.write("  </lista>\n");
            bw.write("  <resumen>\n");
            bw.write("    <precioMedio>" + String.format("%.2f", totalPrecio / productos.size()) + "</precioMedio>\n");
            bw.write("    <cantidadTotal>" + totalCantidad + "</cantidadTotal>\n");
            bw.write("  </resumen>\n");
            bw.write("</productos>\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // ==================== EXPORTAR A JSON ====================
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("datos/productos.json"))) {
            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            double totalPrecio = 0;
            int totalCantidad = 0;

            for (Producto p : productos) {
                totalPrecio += p.precio;
                totalCantidad += p.cantidad;
            }

            double precioMedio = totalPrecio / productos.size();

            bw.write("{\n  \"productos\": {\n");
            bw.write("    \"metadata\": {\"fecha\": \"" + fecha + "\", \"totalProductos\": " + productos.size() + "},\n");
            bw.write("    \"lista\": [\n");

            for (int i = 0; i < productos.size(); i++) {
                Producto p = productos.get(i);
                bw.write("      {\n");
                bw.write("        \"id\": " + p.id + ",\n");
                bw.write("        \"nombre\": \"" + p.nombre + "\",\n");
                bw.write("        \"precio\": " + p.precio + ",\n");
                bw.write("        \"cantidad\": " + p.cantidad + "\n");
                bw.write("      }" + (i < productos.size() - 1 ? "," : "") + "\n");
            }

            bw.write("    ],\n");
            bw.write("    \"estadisticas\": {\n");
            bw.write("      \"precioMedio\": " + String.format("%.2f", precioMedio) + ",\n");
            bw.write("      \"cantidadTotal\": " + totalCantidad + "\n");
            bw.write("    }\n");
            bw.write("  }\n}");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
