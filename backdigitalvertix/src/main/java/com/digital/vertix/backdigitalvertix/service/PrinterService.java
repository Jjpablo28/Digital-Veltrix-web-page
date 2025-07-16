package com.digital.vertix.backdigitalvertix.service;

import org.springframework.stereotype.Service;

import javax.print.*;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

@Service
public class PrinterService {

    private static final String PRINTER_NAME = "CITIZEN CT-S310II";
    private static final String PRINTER_ENCODING = "CP437"; // Verifica que la impresora use esto

    public static void printReceipt(String body) throws Exception {
        PrintService printer = buscarImpresora();

        if (printer == null) {
            throw new RuntimeException("Impresora '" + PRINTER_NAME + "' no encontrada.");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Inicializar impresora
        out.write(new byte[]{0x1B, 0x40}); // ESC @

        // Comandos ESC/POS
        byte[] doubleSize = new byte[]{0x1D, 0x21, 0x11}; // Doble ancho y alto
        byte[] normalSize = new byte[]{0x1D, 0x21, 0x01}; // Normal

        // Separar por líneas
        String[] lines = body.split("\n");

        for (String line : lines) {
            if (line.startsWith("[BIG]")) {
                out.write(doubleSize);
                out.write(line.replace("[BIG]", "").getBytes(PRINTER_ENCODING));
                out.write(normalSize);
            } else {
                out.write(line.getBytes(PRINTER_ENCODING));
            }
            out.write("\n".getBytes());
        }

        // Alimentar papel y cortar
        out.write(new byte[]{0x1B, 0x64, 0x05}); // ESC d 5 (avanzar 5 líneas)
        out.write(new byte[]{0x1D, 0x56, 0x41, 0x00}); // GS V A n (corte parcial)
        out.write(new byte[]{0x1B, 0x70, 0x00, 0x32, 0x32});//abrir la caja

        // Enviar a impresora
        byte[] data = out.toByteArray();
        DocPrintJob job = printer.createPrintJob();
        Doc doc = new SimpleDoc(data, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
        job.print(doc, null);
    }

    private static PrintService buscarImpresora() {
        for (PrintService ps : PrintServiceLookup.lookupPrintServices(null, null)) {
            System.out.println("Detectada: " + ps.getName());
            if (ps.getName().equalsIgnoreCase(PRINTER_NAME)) {
                return ps;
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
   buscarImpresora();
    }
}
