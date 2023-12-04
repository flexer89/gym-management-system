package utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;

public class ExportToPDF {
    public static void ExportTableToPDF(DefaultTableModel model, String filePath) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);

                int rowCount = model.getRowCount();
                int colCount = model.getColumnCount();

                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float yPosition = yStart;
                float tableHeight = 20f;
                float cellMargin = 2f;

                // Write the table headers
                contentStream.newLineAtOffset(margin, yPosition);
                for (int i = 0; i < colCount; i++) {
                    String header = model.getColumnName(i);
                    contentStream.showText(header);
                    contentStream.newLineAtOffset(tableWidth / colCount, 0);
                }
                
                // Write the table rows
                for (int i = 0; i < rowCount; i++) {
                    contentStream.newLineAtOffset(-tableWidth, -tableHeight);
                    yPosition -= tableHeight;
                    for (int j = 0; j < colCount; j++) {
                        String cell = model.getValueAt(i, j).toString();
                        // Fit the cell value to prevent overflow
                        float cellWidth = tableWidth / colCount - 2 * cellMargin;
                        String fittedCell = fitCellValue(cell, cellWidth, contentStream);
                        contentStream.showText(fittedCell);
                        contentStream.newLineAtOffset(tableWidth / colCount, 0);
                    }
                }

                contentStream.endText();
            }

            document.save(filePath);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String fitCellValue(String cellValue, float cellWidth, PDPageContentStream contentStream) throws IOException {
        String fittedValue = cellValue;
        float textWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(fittedValue) / 1000 * 8;
        while (textWidth > cellWidth) {
            fittedValue = fittedValue.substring(0, fittedValue.length() - 1);
            textWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(fittedValue) / 1000 * 8;
        }
        return fittedValue;
    }
}