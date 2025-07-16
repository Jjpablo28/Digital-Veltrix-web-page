package com.digital.vertix.backdigitalvertix.service;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.digital.vertix.backdigitalvertix.model.Factura;
import com.digital.vertix.backdigitalvertix.model.Item;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;


@Service
public class FacturaService {

    private static final int LINE_WIDTH = 45;

   ;

    /**
     * Genera la factura en formato de texto y la envía a la impresora.
     * @param factura Los datos de la factura.
     * @throws Exception Si ocurre un error durante la impresión.
     */
    public void imprimirFacturaTexto(Factura factura) throws Exception {
        String texto = generarTextoFactura(factura);
        // El método adaptarTextoParaEscPos se llama dentro de generarTextoFactura o antes de printReceipt si es necesario.
        // Asumo que tu PrinterService ya maneja la conversión a bytes de ESC/POS.
        PrinterService.printReceipt(texto);
        System.out.println("Factura enviada como texto ESC/POS al PrinterService.");
    }
    
    /**
     * Nuevo método para imprimir una línea de texto en PDF con control de negrita, tamaño y alineación.
     * @param cb        El contenido del PDF.
     * @param bf        La fuente base (normal).
     * @param bfBold    La fuente base (negrita).
     * @param text      El texto a imprimir.
     * @param center    Si debe centrarse horizontalmente.
     * @param y         La coordenada vertical donde se imprimirá.
     * @param fontSize  El tamaño de la fuente.
     * @param isBold    Si el texto debe ser negrita.
     * @return La nueva coordenada Y después de imprimir la línea.
     */
    private float printLinePdf(PdfContentByte cb, BaseFont bf, BaseFont bfBold, String text, boolean center, float y, float fontSize, boolean isBold) {
        cb.beginText();
        if (isBold) {
            cb.setFontAndSize(bfBold, fontSize);
        } else {
            cb.setFontAndSize(bf, fontSize);
        }

        float x;
        float textWidth = (isBold ? bfBold : bf).getWidthPoint(text, fontSize);

        if (center) {
            x = (226 - textWidth) / 2f;
        } else {
            x = 10f; // Margen izquierdo
        }

        cb.setTextMatrix(x, y);
        cb.showText(text);
        cb.endText();

        return y - (fontSize + 4); // Ajusta el espaciado vertical dinámicamente
    }

    /**
     * Nuevo método para imprimir una línea con texto a la izquierda y otro texto alineado a la derecha en PDF.
     * Permite controlar la negrita y el tamaño de la fuente para ambos textos.
     * @param cb        El contenido del PDF.
     * @param bf        La fuente base (normal).
     * @param bfBold    La fuente base (negrita).
     * @param leftText  El texto alineado a la izquierda.
     * @param rightText El texto alineado a la derecha.
     * @param y         La coordenada vertical.
     * @param fontSize  El tamaño de la fuente para ambos textos.
     * @param isBoldLeft Si el texto izquierdo debe ser negrita.
     * @param isBoldRight Si el texto derecho debe ser negrita.
     * @return La nueva coordenada Y después de imprimir la línea.
     */
    private float printAlignedLinePdf(PdfContentByte cb, BaseFont bf, BaseFont bfBold, String leftText, String rightText, float y, float fontSize, boolean isBoldLeft, boolean isBoldRight) {
        cb.beginText();
        
        float leftX = 10f; // Margen izquierdo fijo
        float rightXLimit = 226 - 10f; // Límite derecho de la página (ancho de página - margen)

        // Imprimir texto izquierdo
        if (isBoldLeft) {
            cb.setFontAndSize(bfBold, fontSize);
        } else {
            cb.setFontAndSize(bf, fontSize);
        }
        cb.setTextMatrix(leftX, y);
        cb.showText(leftText);
        
        // Imprimir texto derecho
        if (isBoldRight) {
            cb.setFontAndSize(bfBold, fontSize);
        } else {
            cb.setFontAndSize(bf, fontSize);
        }
        // Calcular la posición del texto derecho para que quede alineado a la derecha
        float rightTextWidth = (isBoldRight ? bfBold : bf).getWidthPoint(rightText, fontSize);
        cb.setTextMatrix(rightXLimit - rightTextWidth, y);
        cb.showText(rightText);

        cb.endText();
        return y - (fontSize + 4); // Ajusta el espaciado vertical
    }

    
    /**
     * Genera los bytes de un documento PDF de factura con un estilo de recibo térmico.
     * Este método solo se encarga de la creación del PDF en memoria y devuelve los bytes,
     * sin guardarlo ni imprimirlo.
     * @param factura Los datos de la factura.
     * @return Un array de bytes que representa el documento PDF.
     * @throws DocumentException Si hay un error al generar el PDF.
     * @throws IOException Si hay un error de E/S.
     */
    public byte[] generarPdfFacturaEstiloRecibo(Factura factura) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Dimensiones del recibo (ancho fijo, alto dinámico para adaptarse al contenido)
        Document document = new Document(new Rectangle(226, 1000), 10, 10, 10, 10);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        PdfContentByte cb = writer.getDirectContent();
        // Fuentes base para PDF: Courier es monoespaciada como las impresoras térmicas
        BaseFont bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        BaseFont bfBold = BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED); // Fuente en negrita

        float y = 980; // Posición inicial Y (parte superior del recibo)

        // Información de la empresa (sin negrita, centrado)
        y = printLinePdf(cb, bf, bfBold, "TU EMPRESA S.A.S.", true, y, 8, false);
        y = printLinePdf(cb, bf, bfBold, "NIT: 900.XXX.XXX-X", true, y, 8, false);
        y = printLinePdf(cb, bf, bfBold, "Calle 123 #45-67, Bogotá", true, y, 8, false);
        y = printLinePdf(cb, bf, bfBold, "Tel: (1) 1234567", true, y, 8, false);
        y -= 12; // Espacio extra entre la información de la empresa y la factura

        // "F A C T U R A" en negrita y centrado (con mayor tamaño de fuente)
        y = printLinePdf(cb, bf, bfBold, "F A C T U R A", true, y, 10, true); 
        y = printLinePdf(cb, bf, bfBold, repeatChar('=', 43), true, y, 8, false); // Línea decorativa (igual que en texto)

        // Datos de la factura y cliente (alineados a la izquierda)
        y = printLinePdf(cb, bf, bfBold, "Factura No: 50", false, y, 8, false);
        y = printLinePdf(cb, bf, bfBold, "Fecha      : " + factura.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), false, y, 8, false);
        y = printLinePdf(cb, bf, bfBold, "Cliente    : " + factura.getCliente(), false, y, 8, false);
        y = printLinePdf(cb, bf, bfBold, repeatChar('-', 43), false, y, 8, false); // Línea decorativa

        // Encabezados de ítems (negrita, alineado simulando columnas)
        String headerDesc = "DESCRIPCION";
        String headerCant = "CANT";
        String headerValor = "VALOR";
        
        // Estos anchos son para la lógica de alineación de texto monoespaciado en PDF
        int pdfHeaderDescWidth = 24; 
        int pdfHeaderCantWidth = 5;  
        int pdfHeaderValorWidth = 8; 

        // Dibuja los encabezados con negrita y posicionamiento manual para alineación columnar
        cb.beginText();
        cb.setFontAndSize(bfBold, 8); // Negrita para encabezados
        cb.setTextMatrix(10f, y); // Posición inicial para DESCRIPCION
        cb.showText(String.format("%-"+pdfHeaderDescWidth+"s", headerDesc));
        
        // Ajuste de X para CANT y VALOR. Los valores 4.8f y 15f son aproximaciones para Courier.
        // Podrías necesitar ajustarlos para que las columnas se alineen perfectamente.
        cb.setTextMatrix(10f + (pdfHeaderDescWidth * 4.8f), y); 
        cb.showText(String.format("%"+pdfHeaderCantWidth+"s", headerCant));
        
        cb.setTextMatrix(10f + (pdfHeaderDescWidth * 4.8f) + (pdfHeaderCantWidth * 4.8f) + 15f, y); 
        cb.showText(String.format("%"+pdfHeaderValorWidth+"s", headerValor));
        cb.endText();
        y -= (8 + 4); // Espaciado vertical después de los encabezados

        y = printLinePdf(cb, bf, bfBold, repeatChar('-', 43), false, y, 8, false); // Línea decorativa

        double total = 0.0;
        int cantidadTotal = 0; // Para el total de ítems
        @SuppressWarnings("deprecation")
		Locale locale = new Locale("es", "CO"); // Asegurar el locale para formato de números

        for (Item item : factura.getItems()) {
            double subtotal = item.getCantidad() * item.getPrecio();
            total += subtotal;
            cantidadTotal += item.getCantidad(); 

            NumberFormat nf = NumberFormat.getNumberInstance(locale);
            nf.setMinimumFractionDigits(0); 
            nf.setMaximumFractionDigits(0);
            String precioFormateado = nf.format(subtotal);

            String nombre = item.getNombre();
            String cantidad = String.format("%4d", item.getCantidad()); // Formato fijo para cantidad
            String valor = String.format("%7s", precioFormateado); // Formato fijo para valor

            // Dividir el nombre del item si es muy largo
            List<String> lineas = splitText(nombre, pdfHeaderDescWidth); 
            
            for (int i = 0; i < lineas.size(); i++) {
                cb.beginText();
                cb.setFontAndSize(bf, 8); // Fuente normal para ítems
                if (i == 0) {
                    // Primera línea del ítem: descripción, cantidad, valor
                    cb.setTextMatrix(10f, y); 
                    cb.showText(String.format("%-"+pdfHeaderDescWidth+"s", "· " + lineas.get(i))); 

                    cb.setTextMatrix(10f + (pdfHeaderDescWidth * 4.8f), y); 
                    cb.showText(String.format("%"+pdfHeaderCantWidth+"s", cantidad));
                    
                    cb.setTextMatrix(10f + (pdfHeaderDescWidth * 4.8f) + (pdfHeaderCantWidth * 4.8f) + 15f, y); 
                    cb.showText(String.format("%"+pdfHeaderValorWidth+"s", valor));
                } else {
                    // Líneas adicionales de la descripción del ítem (indentadas)
                    cb.setTextMatrix(10f + 5f, y); 
                    cb.showText(lineas.get(i));
                }
                cb.endText();
                y -= (8 + 4); // Espaciado vertical para cada línea del ítem
            }
        }

        y = printLinePdf(cb, bf, bfBold, repeatChar('-', 43), false, y, 8, false); 
        
        // "TOTAL DE ITEMS" y su valor (izquierda y derecha)
        String totalItemsText = "TOTAL DE ITEMS:";
        String cantidadTotalFormatted = String.valueOf(cantidadTotal);
        y = printAlignedLinePdf(cb, bf, bfBold, totalItemsText, cantidadTotalFormatted, y, 8, false, false); 

        // "TOTAL A PAGAR" en negrita (izquierda y derecha, con mayor tamaño)
        NumberFormat nfTotal = NumberFormat.getNumberInstance(locale);
        nfTotal.setMinimumFractionDigits(0);
        nfTotal.setMaximumFractionDigits(0);
        String totalFormateado = nfTotal.format(total);
        y = printAlignedLinePdf(cb, bf, bfBold, "TOTAL A PAGAR:", totalFormateado, y, 10, true, true); 

        y = printLinePdf(cb, bf, bfBold, repeatChar('=', 43), true, y, 8, false); 

        // Mensajes finales (centrado)
        y = printLinePdf(cb, bf, bfBold, "¡GRACIAS POR SU COMPRA!", true, y, 8, false);
        y = printLinePdf(cb, bf, bfBold, "Vuelva Pronto", true, y, 8, false);
        y = printLinePdf(cb, bf, bfBold, "Sitio Web: www.tuempresa.com", true, y, 8, false);
        y = printLinePdf(cb, bf, bfBold, "Atendido por: [Nombre Cajero]", true, y, 8, false);
        y = printLinePdf(cb, bf, bfBold, "Desarrollado por Digital-Veltrix", true, y, 8, false);


        document.close();
        return baos.toByteArray();
    }


    /**
     * Divide un texto en varias líneas basándose en una longitud máxima,
     * intentando cortar por espacios para evitar cortar palabras.
     * @param text El texto a dividir.
     * @param maxLength La longitud máxima de cada línea.
     * @return Una lista de cadenas, cada una representando una línea.
     */
    private List<String> splitText(String text, int maxLength) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            lines.add("");
            return lines;
        }

        while (text.length() > maxLength) {
            int splitAt = text.lastIndexOf(" ", maxLength);
            if (splitAt <= 0) { 
                splitAt = maxLength; 
            }
            lines.add(text.substring(0, splitAt));
            text = text.substring(splitAt).trim(); 
        }
        lines.add(text); 
        return lines;
    }

   

    /**
     * Genera la factura en PDF, la guarda directamente en la carpeta de Descargas del usuario
     * y luego intenta abrirla con el visor predeterminado del sistema operativo.
     * Incluye mensajes de depuración para facilitar el diagnóstico.
     *
     * IMPORTANTE: Esta funcionalidad (abrir el PDF en un visor) SÓLO funcionará si la aplicación
     * se ejecuta en un entorno con una interfaz gráfica de usuario (GUI) activa y disponible para la JVM.
     * Si tu aplicación Spring Boot se ejecuta en un servidor sin GUI (la situación más común para un backend),
     * la parte de "abrir" el PDF fallará, aunque el archivo PDF sí se guardará en la carpeta de
     * Descargas de la cuenta de usuario que ejecuta el servidor.
     *
     * @param factura Los datos de la factura.
     * @throws DocumentException Si hay un error al generar el PDF.
     * @throws IOException Si hay un error de E/S al guardar o abrir el archivo.
     */
    public void abrirFacturaPdfEnVisor(Factura factura) throws DocumentException, IOException {
        byte[] pdfBytes = null;
        try {
           
            pdfBytes = generarPdfFacturaEstiloRecibo(factura);
            if (pdfBytes == null || pdfBytes.length == 0) {
                System.err.println("ERROR: generarPdfFacturaEstiloRecibo devolvió un array de bytes vacío o nulo. No se puede guardar ni abrir el PDF.");
                return; 
            }
        } catch (DocumentException | IOException e) {
            System.err.println("ERROR al generar los bytes del PDF: " + e.getMessage());
            e.printStackTrace();
            throw e; 
        }

        Path downloadsPath;
        String userHome = System.getProperty("user.home");
        
        System.out.println("Ruta de inicio del usuario (user.home): " + userHome);

        try {
            // Construye la ruta completa a la carpeta de Descargas del usuario
            // Puedes descomentar y usar una ruta fija para depuración si la de Descargas da problemas
            // downloadsPath = Paths.get("C:", "temp", "DigitalVeltrixPDFs"); // Ejemplo para Windows
            // downloadsPath = Paths.get("/tmp", "DigitalVeltrixPDFs"); // Ejemplo para Linux/macOS
            
            downloadsPath = Paths.get(userHome, "Downloads"); 
            
            Files.createDirectories(downloadsPath); 
            System.out.println("Directorio de Descargas/destino final: " + downloadsPath.toAbsolutePath());

        } catch (SecurityException e) {
            System.err.println("Error de seguridad al acceder/crear el directorio de Descargas: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("No se pudo acceder al directorio de Descargas debido a permisos.", e);
        }

        String sanitizedClientName = factura.getCliente().replaceAll("[^a-zA-Z0-9_.-]", "_");
        String fileName = "factura_" +
                          sanitizedClientName +
                          "_" +
                          LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                          ".pdf";

        Path filePathInDownloads = downloadsPath.resolve(fileName);
        
        System.out.println("Nombre del archivo a guardar: " + fileName);
        System.out.println("Ruta completa del archivo a guardar: " + filePathInDownloads.toAbsolutePath());

        try {
           
            Files.write(filePathInDownloads, pdfBytes); 
            System.out.println("PDF guardado con éxito en: " + filePathInDownloads.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("ERROR al guardar el PDF en el disco: " + e.getMessage());
            e.printStackTrace();
            throw e; 
        }

       
        if (Desktop.isDesktopSupported()) { 
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                try {
                    desktop.open(filePathInDownloads.toFile()); 
                    System.out.println("PDF abierto en el visor predeterminado del sistema.");
                } catch (IOException e) {
                   
                    System.err.println("Error al intentar abrir el PDF desde Descargas con el visor: " + e.getMessage());
                    System.err.println("Este error no impide que el archivo se haya guardado correctamente.");
                    e.printStackTrace();
                }
            } else {
                System.err.println("La acción 'OPEN' no es soportada por el entorno de escritorio actual. El PDF se guardó, pero no se abrió automáticamente.");
            }
        } else {
            System.err.println("El entorno de escritorio no es soportado en este sistema operativo para abrir archivos directamente.");
            System.err.println("Por favor, asegúrate de que tu aplicación se ejecute en un entorno con GUI si esperas que el PDF se abra automáticamente.");
            System.err.println("El PDF se guardó, pero no se abrió automáticamente.");
        }
    }


    /**
     * Genera el texto plano de la factura para impresión en una impresora térmica.
     * Este método NO DEBE SER MODIFICADO según las instrucciones.
     * @param factura Los datos de la factura.
     * @return La cadena de texto formateada para la impresora.
     */
    private String generarTextoFactura(Factura factura) {
        StringBuilder sb = new StringBuilder();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        @SuppressWarnings("deprecation")
		Locale locale = new Locale("es", "CO");

        sb.append(centerText("TU EMPRESA S.A.S.", LINE_WIDTH)).append("\n");
        sb.append(centerText("NIT: 900.XXX.XXX-X", LINE_WIDTH)).append("\n");
        sb.append(centerText("Calle 123 #45-67, Bogotá", LINE_WIDTH)).append("\n");
        sb.append(centerText("Tel: (1) 1234567", LINE_WIDTH)).append("\n");
        sb.append(centerText(" ", LINE_WIDTH)).append("\n");

        sb.append(centerTextForBigText("F A C T U R A", LINE_WIDTH)).append("\n");
        sb.append(repeatChar('=', LINE_WIDTH)).append("\n");

        String facturaNumero = "50"; 
        sb.append(String.format("Factura No: %s\n", facturaNumero));
        sb.append(String.format("Fecha      : %s\n", factura.getFecha().format(dateFormatter)));
        sb.append(String.format("Cliente    : %s\n", factura.getCliente()));
        sb.append(repeatChar('-', LINE_WIDTH)).append("\n");

        int descWidth = 26; 
        int cantHeaderWidth = 5; 
        int valorHeaderWidth = 10; 

        sb.append(String.format(locale, "%-"+descWidth+"s %"+cantHeaderWidth+"s %"+valorHeaderWidth+"s\n", "DESCRIPCION", "CANT", "VALOR"));
        sb.append(repeatChar('-', LINE_WIDTH)).append("\n");
        int cantidadTotalItems = 0; // Renombrada para evitar confusión con el total numérico
        for (Item item : factura.getItems()) {
            String nombreItem = item.getNombre();
            
            String cantidadFormateada = String.format("%5d", item.getCantidad()); 
            
            NumberFormat nfItem = NumberFormat.getNumberInstance(locale);
            nfItem.setMinimumFractionDigits(0); 
            nfItem.setMaximumFractionDigits(0); 
            
            double subtotal = item.getPrecio() * item.getCantidad();
            String precioTotalItemFormateado = nfItem.format(subtotal);
            
            int cantItemWidth = 1;  
            int valorItemWidth = 12; 

            String productLabel = "· "; 
            
            String[] truncatedLines = splitTextIntoLines(nombreItem, descWidth - productLabel.length());
            String firstLineDesc = truncatedLines.length > 0 ? productLabel + truncatedLines[0] : productLabel;

            for (int i = 0; i < truncatedLines.length; i++) {
                if (i == 0) {
                    sb.append(String.format(locale, "%-"+descWidth+"s %"+cantItemWidth+"s %"+valorItemWidth+"s\n",
                                            firstLineDesc, cantidadFormateada, precioTotalItemFormateado));
                    cantidadTotalItems=cantidadTotalItems+item.getCantidad();// Incrementar por cada item principal
                } else {
                    sb.append(String.format(locale, "%-"+(LINE_WIDTH)+"s\n", "  " + truncatedLines[i]));
                }
            }
        }

        sb.append(repeatChar('-', LINE_WIDTH)).append("\n");
        
       
        NumberFormat nfTotal = NumberFormat.getNumberInstance(locale);
        // Establecer 0 decimales para el formato del total
        nfTotal.setMinimumFractionDigits(0);
        nfTotal.setMaximumFractionDigits(0);
        String totalFormateado = nfTotal.format(factura.getTotal());
        
        
        String totalItemsText = "TOTAL DE ITEMS: ";
        String cantidadTotalFormatted = String.valueOf(cantidadTotalItems); // Usar la nueva variable
        int paddingItems = LINE_WIDTH - totalItemsText.length() - cantidadTotalFormatted.length();
        sb.append(String.format(locale, "%s%"+paddingItems+"s%s\n", totalItemsText, "", cantidadTotalFormatted));

        
        String totalPagarText = "TOTAL A PAGAR: ";
        int paddingTotal = LINE_WIDTH - totalPagarText.length() - totalFormateado.length();
        sb.append(String.format(locale, "%s%"+paddingTotal+"s%s\n", totalPagarText, "", totalFormateado));

        sb.append(repeatChar('=', LINE_WIDTH)).append("\n");
        sb.append(centerText(" ", LINE_WIDTH)).append("\n");
        sb.append(centerText("¡GRACIAS POR SU COMPRA!", LINE_WIDTH)).append("\n");
        sb.append(centerText("Vuelva Pronto", LINE_WIDTH)).append("\n");
        sb.append(centerText(" ", LINE_WIDTH)).append("\n");
        sb.append(centerText("Sitio Web: www.tuempresa.com", LINE_WIDTH)).append("\n");
        sb.append(centerText("Atendido por: [Nombre Cajero]", LINE_WIDTH)).append("\n");
        sb.append(centerText("Desarrollado por Digital-Veltrix", LINE_WIDTH)).append("\n");

        
        // Espacios finales para corte del recibo
        sb.append("\n\n\n\n\n\n");
        sb.append("\n\n\n\n\n\n");

        return sb.toString();
    }

    /**
     * Adapta el texto plano (extraído de PDF o generado) para incluir comandos de impresora ESC/POS.
     * Actualmente solo detecta "FACTURA" para aplicar un comando [BIG] (tamaño grande).
     * @param texto El texto a adaptar.
     * @return El texto con los comandos ESC/POS incrustados.
     */
    private String adaptarTextoParaEscPos(String texto) {
        StringBuilder sb = new StringBuilder();
        // Nota: Las secuencias de escape "\u001B" representan el carácter de escape ESC (0x1B).
        // Se usan para que PrinterService las convierta a bytes reales de la impresora.
        // Asegúrate de que PrinterService maneje estas secuencias de forma adecuada.
        String bigOn  = "\u001B\u0021\u0030"; // ESC ! 30h (Activa doble ancho y alto)
        String bigOff = "\u001B\u0021\u0000"; // ESC ! 00h (Desactiva doble ancho y alto)
        for (String linea : texto.split("\n")) {
            // Si la línea contiene "F A C T U R A", la pone en tamaño grande.
            // Esto es un ejemplo, podrías querer aplicar negrita a más elementos aquí.
            if (linea.toUpperCase().contains("FACTURA") || linea.toUpperCase().contains("F A C T U R A")) {
                // Aquí podrías envolverlo en comandos de negrita también si quieres:
                // sb.append(bigOn).append(boldOn).append(linea.trim()).append(boldOff).append(bigOff).append("\n");
                sb.append(bigOn).append(linea.trim()).append(bigOff).append("\n");
            } else {
                sb.append(linea.trim()).append("\n");
            }
        }
        return sb.toString();
    }


    /**
     * Imprime una factura extrayendo el texto de un PDF.
     * Esto es útil si el PDF ya contiene el formato visual deseado y solo necesitas el texto para impresoras térmicas.
     * @param pdfBytes Los bytes del documento PDF.
     * @throws Exception Si ocurre un error al procesar el PDF o al imprimir.
     */
    public void imprimirFacturaDesdePdfComoTexto(byte[] pdfBytes) throws Exception {
        String textoPlano;
        try (PDDocument document = PDDocument.load(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            textoPlano = stripper.getText(document);
        }

        // Aquí se adapta el texto extraído del PDF para los comandos de la impresora térmica.
        // Podría ser necesario un procesamiento más avanzado si el diseño del PDF es muy complejo
        // y quieres mapearlo a comandos ESC/POS específicos (ej. columnas, etc.).
        textoPlano = adaptarTextoParaEscPos(textoPlano); // Aplica los comandos BIG si encuentra "FACTURA"

        PrinterService.printReceipt(textoPlano);
        System.out.println("Se está imprimiendo la factura desde PDF como texto.");
    }


    /**
     * Centra un texto considerando que algunos caracteres (como los de agrandar) pueden ocupar más espacio.
     * Este método es para la lógica de `generarTextoFactura`.
     * @param text El texto a centrar.
     * @param normalWidth El ancho de línea "normal" de la impresora.
     * @return El texto centrado.
     */
    private String centerTextForBigText(String text, int normalWidth) {
        // Estas etiquetas son para la lógica interna de centrado en texto plano, no para ESC/POS directamente
        String bigTextOnTag = "[BIG_TEXT_ON]";
        String bigTextOffTag = "[BIG_TEXT_OFF]";

        // Si el texto a centrar contiene las etiquetas de negrita, las ignoramos para el cálculo del ancho
        String textWithoutBoldTags = text.replace("[B]", "").replace("[/B]", "");

        // Si el texto a centrar contiene las etiquetas BIG, asumimos que el contenido dentro de ellas es el doble de ancho
        int bigTextStartIndex = textWithoutBoldTags.indexOf(bigTextOnTag);
        int bigTextEndIndex = textWithoutBoldTags.indexOf(bigTextOffTag);

        if (bigTextStartIndex == -1 || bigTextEndIndex == -1 || bigTextStartIndex > bigTextEndIndex) {
            // Si no hay etiquetas BIG, usa el centrado normal, pero sigue ignorando etiquetas de negrita.
            return centerText(textWithoutBoldTags, normalWidth);
        }

        String prefix = textWithoutBoldTags.substring(0, bigTextStartIndex);
        String bigTextContent = textWithoutBoldTags.substring(bigTextStartIndex + bigTextOnTag.length(), bigTextEndIndex);
        String suffix = textWithoutBoldTags.substring(bigTextEndIndex + bigTextOffTag.length());

        // Calcula la longitud efectiva: prefijo + (contenido BIG * 2) + sufijo
        int effectiveLength = prefix.length() + (bigTextContent.length() * 2) + suffix.length();

        int paddingNeeded = normalWidth - effectiveLength;
        int paddingLeft = Math.max(0, paddingNeeded / 2);
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paddingLeft; i++) {
            sb.append(" ");
        }
        sb.append(text); // Devuelve el texto original con las etiquetas (que luego serán reemplazadas por ESC/POS)
        
        return sb.toString();
    }

    /**
     * Centra una línea de texto dado un ancho total.
     * Este método es para la lógica de `generarTextoFactura`.
     * @param text El texto a centrar.
     * @param width El ancho total disponible.
     * @return El texto con el padding necesario para estar centrado.
     */
    private String centerText(String text, int width) {
        // Asegúrate de que las etiquetas [B] y [/B] no afecten el cálculo del centrado
        String textWithoutBoldTags = text.replace("[B]", "").replace("[/B]", "");
        if (textWithoutBoldTags == null || textWithoutBoldTags.length() >= width) return text;
        int padding = (width - textWithoutBoldTags.length()) / 2;
        return " ".repeat(padding) + text; // Devuelve el texto original (con etiquetas si las tiene)
    }

    /**
     * Repite un carácter un número específico de veces.
     * @param character El carácter a repetir.
     * @param count El número de veces que se repite el carácter.
     * @return Una cadena con el carácter repetido.
     */
    private String repeatChar(char character, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(character);
        }
        return sb.toString();
    }

    /**
     * Divide un texto en líneas, similar a splitText, pero diseñado para el formato de recibo.
     * Este método es para la lógica de `generarTextoFactura`.
     * @param text El texto a dividir.
     * @param maxLength La longitud máxima de cada línea.
     * @return Un array de cadenas, cada una representando una línea.
     */
    private String[] splitTextIntoLines(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return new String[]{text};
        }

        java.util.List<String> lines = new java.util.ArrayList<>();
        int currentPos = 0;
        while (currentPos < text.length()) {
            int end = Math.min(currentPos + maxLength, text.length());
            String line = text.substring(currentPos, end);
            
            // Intenta cortar por el último espacio si no estamos al final del texto y el corte cayó a mitad de palabra
            if (end < text.length() && line.lastIndexOf(" ") != -1 && !text.substring(end-1, end).equals(" ")) {
                int lastSpace = line.lastIndexOf(" ");
                if (lastSpace != -1 && currentPos + lastSpace > currentPos) {
                    line = line.substring(0, lastSpace);
                    end = currentPos + lastSpace;
                }
            }
            
            lines.add(line.trim()); // Añadir línea y recortar espacios
            currentPos = end;
            // Si el corte fue forzado por maxLength, avanzar al siguiente caracter para evitar bucles infinitos
            if (currentPos < text.length() && text.charAt(currentPos-1) != ' ' && lines.get(lines.size()-1).length() == maxLength) {
                 // Si la última línea se cortó por la fuerza y no termina en espacio, avanzamos 1 para evitar bucle
                 // Esto es un parche para palabras muy largas. Idealmente, manejar esto sería más robusto.
                 if (currentPos < text.length() && text.charAt(currentPos) != ' ') { // Si el siguiente no es un espacio
                     // Intenta avanzar hasta el siguiente espacio o el fin de la palabra para no cortar por la mitad
                     int nextSpace = text.indexOf(' ', currentPos);
                     if (nextSpace != -1) {
                         currentPos = nextSpace + 1;
                     } else {
                         currentPos = text.length(); // Si no hay más espacios, ir al final
                     }
                 } else {
                     currentPos++; // Si ya estamos en un espacio o fin de línea, simplemente avanza
                 }
            }
             // Asegurarse de que el siguiente segmento no empiece con espacios excesivos
            while (currentPos < text.length() && text.charAt(currentPos) == ' ') {
                currentPos++;
            }
        }
        return lines.toArray(String[]::new);
    }
}