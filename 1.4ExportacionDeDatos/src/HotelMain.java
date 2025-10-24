import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class Cliente {
    int id;
    String nombre;
    String email;
    String telefono;

    public Cliente(int id, String nombre, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }
}

class Habitacion {
    int numero;
    String tipo;
    double precioPorNoche;
    boolean disponible;

    public Habitacion(int numero, String tipo, double precioPorNoche, boolean disponible) {
        this.numero = numero;
        this.tipo = tipo;
        this.precioPorNoche = precioPorNoche;
        this.disponible = disponible;
    }
}

class Reserva {
    int id;
    Cliente cliente;
    Habitacion habitacion;
    LocalDate fechaEntrada;
    LocalDate fechaSalida;
    int noches;
    double precioTotal;
    String estado;

    public Reserva(int id, Cliente cliente, Habitacion habitacion, LocalDate fechaEntrada,
                   LocalDate fechaSalida, int noches, double precioTotal, String estado) {
        this.id = id;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.noches = noches;
        this.precioTotal = precioTotal;
        this.estado = estado;
    }
}

public class HotelMain {
    public static void main(String[] args) {
        Cliente c1 = new Cliente(1, "Juan García", "juan@email.com", "666111222");
        Cliente c2 = new Cliente(2, "María López", "maria@email.com", "666333444");

        Habitacion h1 = new Habitacion(101, "Doble", 90.0, false);
        Habitacion h2 = new Habitacion(205, "Suite", 200.0, false);

        List<Reserva> reservas = new ArrayList<>();
        reservas.add(new Reserva(1, c1, h1, LocalDate.of(2025,10,20), LocalDate.of(2025,10,23), 3, 270.0, "Confirmada"));
        reservas.add(new Reserva(2, c2, h2, LocalDate.of(2025,10,21), LocalDate.of(2025,10,25), 4, 800.0, "Confirmada"));

        // ==================== EXPORTAR A CSV ====================
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("datos/reservas.csv"))) {
            bw.write("ID;ClienteNombre;ClienteEmail;HabitacionNum;TipoHabitacion;FechaEntrada;FechaSalida;Noches;PrecioTotal;Estado\n");
            for (Reserva r : reservas) {
                bw.write(r.id + ";" + r.cliente.nombre + ";" + r.cliente.email + ";" + r.habitacion.numero + ";" +
                        r.habitacion.tipo + ";" + r.fechaEntrada + ";" + r.fechaSalida + ";" + r.noches + ";" +
                        r.precioTotal + ";" + r.estado + "\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // ==================== EXPORTAR A XML ====================
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("datos/reservas.xml"))) {
            String fechaHoy = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            int totalReservas = reservas.size();
            double ingresosTotal = 0;
            int nochesTotal = 0;

            int reservasIndividual = 0, reservasDoble = 0, reservasSuite = 0;
            double ingresosIndividual = 0, ingresosDoble = 0, ingresosSuite = 0;
            int confirmadas = 0, canceladas = 0, completadas = 0;

            for (Reserva r : reservas) {
                ingresosTotal += r.precioTotal;
                nochesTotal += r.noches;
                switch (r.habitacion.tipo) {
                    case "Individual": reservasIndividual++; ingresosIndividual += r.precioTotal; break;
                    case "Doble": reservasDoble++; ingresosDoble += r.precioTotal; break;
                    case "Suite": reservasSuite++; ingresosSuite += r.precioTotal; break;
                }
                switch (r.estado) {
                    case "Confirmada": confirmadas++; break;
                    case "Cancelada": canceladas++; break;
                    case "Completada": completadas++; break;
                }
            }

            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bw.write("<hotel>\n");
            bw.write("  <informacion>\n");
            bw.write("    <nombre>Hotel Paradise</nombre>\n");
            bw.write("    <fecha>" + fechaHoy + "</fecha>\n");
            bw.write("  </informacion>\n");
            bw.write("  <reservas totalReservas=\"" + totalReservas + "\">\n");

            for (Reserva r : reservas) {
                bw.write("    <reserva id=\"" + r.id + "\" estado=\"" + r.estado + "\">\n");
                bw.write("      <cliente>\n");
                bw.write("        <id>" + r.cliente.id + "</id>\n");
                bw.write("        <nombre>" + r.cliente.nombre + "</nombre>\n");
                bw.write("        <email>" + r.cliente.email + "</email>\n");
                bw.write("        <telefono>" + r.cliente.telefono + "</telefono>\n");
                bw.write("      </cliente>\n");
                bw.write("      <habitacion numero=\"" + r.habitacion.numero + "\" tipo=\"" + r.habitacion.tipo + "\">\n");
                bw.write("        <precioPorNoche>" + r.habitacion.precioPorNoche + "</precioPorNoche>\n");
                bw.write("        <disponible>" + r.habitacion.disponible + "</disponible>\n");
                bw.write("      </habitacion>\n");
                bw.write("      <fechas>\n");
                bw.write("        <entrada>" + r.fechaEntrada + "</entrada>\n");
                bw.write("        <salida>" + r.fechaSalida + "</salida>\n");
                bw.write("        <noches>" + r.noches + "</noches>\n");
                bw.write("      </fechas>\n");
                bw.write("      <precio>\n");
                bw.write("        <total>" + r.precioTotal + "</total>\n");
                bw.write("        <porNoche>" + r.habitacion.precioPorNoche + "</porNoche>\n");
                bw.write("      </precio>\n");
                bw.write("    </reserva>\n");
            }

            bw.write("  </reservas>\n");
            bw.write("  <estadisticas>\n");
            bw.write("    <porTipoHabitacion>\n");
            bw.write("      <Individual><totalReservas>" + reservasIndividual + "</totalReservas><ingresos>" + ingresosIndividual + "</ingresos></Individual>\n");
            bw.write("      <Doble><totalReservas>" + reservasDoble + "</totalReservas><ingresos>" + ingresosDoble + "</ingresos></Doble>\n");
            bw.write("      <Suite><totalReservas>" + reservasSuite + "</totalReservas><ingresos>" + ingresosSuite + "</ingresos></Suite>\n");
            bw.write("    </porTipoHabitacion>\n");
            bw.write("    <porEstado>\n");
            bw.write("      <Confirmada>" + confirmadas + "</Confirmada>\n");
            bw.write("      <Cancelada>" + canceladas + "</Cancelada>\n");
            bw.write("      <Completada>" + completadas + "</Completada>\n");
            bw.write("    </porEstado>\n");
            bw.write("    <resumen>\n");
            bw.write("      <totalReservas>" + totalReservas + "</totalReservas>\n");
            bw.write("      <ingresosTotal>" + ingresosTotal + "</ingresosTotal>\n");
            bw.write("      <nochesReservadas>" + nochesTotal + "</nochesReservadas>\n");
            bw.write("    </resumen>\n");
            bw.write("  </estadisticas>\n");
            bw.write("</hotel>\n");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // ==================== EXPORTAR A JSON ====================
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("datos/reservas.json"))) {
            String fechaHoy = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            bw.write("{\n  \"hotel\": {\n");
            bw.write("    \"informacion\": {\"nombre\": \"Hotel Paradise\", \"fecha\": \"" + fechaHoy + "\"},\n");

            bw.write("    \"clientes\": {\n");
            for (int i = 0; i < reservas.size(); i++) {
                Cliente c = reservas.get(i).cliente;
                bw.write("      \"" + c.id + "\": {\"nombre\": \"" + c.nombre + "\", \"email\": \"" + c.email + "\", \"telefono\": \"" + c.telefono + "\"}" + (i < reservas.size() -1 ? "," : "") + "\n");
            }
            bw.write("    },\n");

            bw.write("    \"habitaciones\": {\n");
            for (int i = 0; i < reservas.size(); i++) {
                Habitacion h = reservas.get(i).habitacion;
                bw.write("      \"" + h.numero + "\": {\"tipo\": \"" + h.tipo + "\", \"precioPorNoche\": " + h.precioPorNoche + ", \"disponible\": " + h.disponible + "}" + (i < reservas.size() -1 ? "," : "") + "\n");
            }
            bw.write("    },\n");

            bw.write("    \"reservas\": [\n");
            for (int i = 0; i < reservas.size(); i++) {
                Reserva r = reservas.get(i);
                bw.write("      {\n");
                bw.write("        \"id\": " + r.id + ",\n");
                bw.write("        \"clienteId\": " + r.cliente.id + ",\n");
                bw.write("        \"habitacionNumero\": " + r.habitacion.numero + ",\n");
                bw.write("        \"fechaEntrada\": \"" + r.fechaEntrada + "\",\n");
                bw.write("        \"fechaSalida\": \"" + r.fechaSalida + "\",\n");
                bw.write("        \"noches\": " + r.noches + ",\n");
                bw.write("        \"precioTotal\": " + r.precioTotal + ",\n");
                bw.write("        \"estado\": \"" + r.estado + "\"\n");
                bw.write("      }" + (i < reservas.size()-1 ? "," : "") + "\n");
            }
            bw.write("    ],\n");

            // Estadísticas simples
            double ingresosTotal = reservas.stream().mapToDouble(r -> r.precioTotal).sum();
            int nochesTotal = reservas.stream().mapToInt(r -> r.noches).sum();
            bw.write("    \"estadisticas\": {\n");
            bw.write("      \"totalReservas\": " + reservas.size() + ",\n");
            bw.write("      \"ingresosTotal\": " + ingresosTotal + ",\n");
            bw.write("      \"nochesReservadas\": " + nochesTotal + "\n");
            bw.write("    }\n");

            bw.write("  }\n}");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
