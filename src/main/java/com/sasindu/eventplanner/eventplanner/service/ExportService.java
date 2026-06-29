package com.sasindu.eventplanner.eventplanner.service;

import com.sasindu.eventplanner.eventplanner.model.Guest;
import com.sasindu.eventplanner.eventplanner.service.GuestService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportService {

    @Autowired
    private GuestService guestService;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ISO_LOCAL_DATE;

    public void exportGuestsCsv(Integer eventId, HttpServletResponse response) throws IOException {
        List<Guest> guests = guestService.getGuestsByEventId(eventId);

        response.setContentType("text/csv");
        String filename = "guests-event-" + eventId + ".csv";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        String header = "GuestID,FirstName,LastName,ContactDetails,Status\n";
        StringBuilder sb = new StringBuilder();
        sb.append(header);

        for (Guest g : guests) {
            // simple CSV escaping for commas and quotes
            sb.append(csvEscape(g.getGuestID() != null ? g.getGuestID().toString() : ""));
            sb.append(",");
            sb.append(csvEscape(g.getFirstName()));
            sb.append(",");
            sb.append(csvEscape(g.getLastName()));
            sb.append(",");
            sb.append(csvEscape(g.getContactDetails()));
            sb.append(",");
            // If you later add status field, replace with g.getStatus()
            sb.append(csvEscape(""));
            sb.append("\n");
        }

        response.getWriter().write(sb.toString());
        response.getWriter().flush();
    }

    private String csvEscape(String s) {
        if (s == null) return "";
        String out = s.replace("\"", "\"\"");
        if (out.contains(",") || out.contains("\"") || out.contains("\n")) {
            return "\"" + out + "\"";
        }
        return out;
    }

    public void exportGuestsExcel(Integer eventId, HttpServletResponse response) throws IOException {
        List<Guest> guests = guestService.getGuestsByEventId(eventId);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String filename = "guests-event-" + eventId + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Guests");

            int rowIdx = 0;
            var headerRow = sheet.createRow(rowIdx++);
            headerRow.createCell(0).setCellValue("GuestID");
            headerRow.createCell(1).setCellValue("FirstName");
            headerRow.createCell(2).setCellValue("LastName");
            headerRow.createCell(3).setCellValue("ContactDetails");
            headerRow.createCell(4).setCellValue("Status");

            for (Guest g : guests) {
                var r = sheet.createRow(rowIdx++);
                r.createCell(0).setCellValue(g.getGuestID() != null ? g.getGuestID() : 0);
                r.createCell(1).setCellValue(g.getFirstName() != null ? g.getFirstName() : "");
                r.createCell(2).setCellValue(g.getLastName() != null ? g.getLastName() : "");
                r.createCell(3).setCellValue(g.getContactDetails() != null ? g.getContactDetails() : "");
                r.createCell(4).setCellValue(""); // status placeholder
            }

            // auto-size columns
            for (int i = 0; i <= 4; i++) sheet.autoSizeColumn(i);

            try (OutputStream out = response.getOutputStream()) {
                workbook.write(out);
            }
        }
    }

    public void exportGuestsPdf(Integer eventId, HttpServletResponse response) throws IOException {
        List<Guest> guests = guestService.getGuestsByEventId(eventId);

        response.setContentType("application/pdf");
        String filename = "guests-event-" + eventId + ".pdf";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        try {
            OutputStream out = response.getOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            document.add(new Paragraph("Guest List for Event " + eventId, titleFont));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100f);
            table.addCell("GuestID");
            table.addCell("FirstName");
            table.addCell("LastName");
            table.addCell("ContactDetails");
            table.addCell("Status");

            for (Guest g : guests) {
                table.addCell(g.getGuestID() != null ? g.getGuestID().toString() : "");
                table.addCell(g.getFirstName() != null ? g.getFirstName() : "");
                table.addCell(g.getLastName() != null ? g.getLastName() : "");
                table.addCell(g.getContactDetails() != null ? g.getContactDetails() : "");
                table.addCell(""); // status placeholder
            }

            document.add(table);
            document.close();
            out.flush();
        } catch (DocumentException de) {
            throw new IOException("Error creating PDF: " + de.getMessage(), de);
        }
    }
}