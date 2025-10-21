import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EstudiantesMain {
    public static void main(String[] args) {
        List<Estudiante> estudiantes = new ArrayList<>();
        estudiantes.add(new Estudiante(1, "Juan", "García López", 20, 8.5));
        estudiantes.add(new Estudiante(2, "María", "Rodríguez", 19, 9.2));
        estudiantes.add(new Estudiante(3, "Pedro", "Martínez", 21, 7.8));
        estudiantes.add(new Estudiante(4, "Ana", "López", 20, 8.9));
        estudiantes.add(new Estudiante(5, "Carlos", "Sánchez", 22, 6.5));

        // ==================== EXPORTAR A CSV ====================
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("datos/estudiantes.csv"))) {
            bw.write("ID;Nombre;Apellidos;Edad;Nota\n");
            double sumaNotas = 0;
            for (Estudiante e : estudiantes) {
                bw.write(e.id + ";" + e.nombre + ";" + e.apellidos + ";" + e.edad + ";" + e.nota + "\n");
                sumaNotas += e.nota;
            }
            double notaMedia = sumaNotas / estudiantes.size();
            bw.write("# Nota media;" + String.format("%.2f", notaMedia) + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // ==================== EXPORTAR A XML ====================
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("datos/estudiantes.xml"))) {
            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            double sumaNotas = 0;
            double notaMax = Double.MIN_VALUE;
            double notaMin = Double.MAX_VALUE;

            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bw.write("<clase>\n");
            bw.write("  <metadata>\n");
            bw.write("    <fecha>" + fecha + "</fecha>\n");
            bw.write("    <totalEstudiantes>" + estudiantes.size() + "</totalEstudiantes>\n");
            bw.write("  </metadata>\n");
            bw.write("  <estudiantes>\n");

            for (Estudiante e : estudiantes) {
                bw.write("    <estudiante id=\"" + e.id + "\">\n");
                bw.write("      <nombre>" + e.nombre + "</nombre>\n");
                bw.write("      <apellidos>" + e.apellidos + "</apellidos>\n");
                bw.write("      <edad>" + e.edad + "</edad>\n");
                bw.write("      <nota>" + e.nota + "</nota>\n");
                bw.write("    </estudiante>\n");
                sumaNotas += e.nota;
                if (e.nota > notaMax) notaMax = e.nota;
                if (e.nota < notaMin) notaMin = e.nota;
            }

            double notaMedia = sumaNotas / estudiantes.size();

            bw.write("  </estudiantes>\n");
            bw.write("  <resumen>\n");
            bw.write("    <notaMedia>" + String.format("%.2f", notaMedia) + "</notaMedia>\n");
            bw.write("    <notaMaxima>" + notaMax + "</notaMaxima>\n");
            bw.write("    <notaMinima>" + notaMin + "</notaMinima>\n");
            bw.write("  </resumen>\n");
            bw.write("</clase>\n");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // ==================== EXPORTAR A JSON ====================
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("datos/estudiantes.json"))) {
            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            double sumaNotas = 0;
            int aprobados = 0;
            int suspensos = 0;
            double notaMax = Double.MIN_VALUE;
            double notaMin = Double.MAX_VALUE;

            for (Estudiante e : estudiantes) {
                sumaNotas += e.nota;
                if (e.nota >= 5) aprobados++;
                else suspensos++;
                if (e.nota > notaMax) notaMax = e.nota;
                if (e.nota < notaMin) notaMin = e.nota;
            }
            double notaMedia = sumaNotas / estudiantes.size();

            bw.write("{\n  \"clase\": {\n");
            bw.write("    \"metadata\": {\"fecha\": \"" + fecha + "\", \"totalEstudiantes\": " + estudiantes.size() + "},\n");
            bw.write("    \"estudiantes\": [\n");

            for (int i = 0; i < estudiantes.size(); i++) {
                Estudiante e = estudiantes.get(i);
                bw.write("      {\n");
                bw.write("        \"id\": " + e.id + ",\n");
                bw.write("        \"nombre\": \"" + e.nombre + "\",\n");
                bw.write("        \"apellidos\": \"" + e.apellidos + "\",\n");
                bw.write("        \"edad\": " + e.edad + ",\n");
                bw.write("        \"nota\": " + e.nota + "\n");
                bw.write("      }" + (i < estudiantes.size() - 1 ? "," : "") + "\n");
            }

            bw.write("    ],\n");
            bw.write("    \"estadisticas\": {\n");
            bw.write("      \"notaMedia\": " + String.format("%.2f", notaMedia) + ",\n");
            bw.write("      \"notaMaxima\": " + notaMax + ",\n");
            bw.write("      \"notaMinima\": " + notaMin + ",\n");
            bw.write("      \"aprobados\": " + aprobados + ",\n");
            bw.write("      \"suspensos\": " + suspensos + "\n");
            bw.write("    }\n");
            bw.write("  }\n}");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
