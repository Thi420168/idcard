package com.rithi.idcard.service;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rithi.idcard.model.BarcodeType;
import com.rithi.idcard.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfExportService {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    private static final Font LABEL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font BODY_FONT = new Font(Font.FontFamily.HELVETICA, 10);

    private final ProfileService profileService;

    public byte[] generateProfilePdf(Long id) {
        return render(List.of(profileService.getProfileById(id)), "ID Card");
    }

    public byte[] generateBatchPdf() {
        return render(profileService.getAllProfiles(), "Batch ID Cards");
    }

    private byte[] render(List<Profile> profiles, String title) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph(title, TITLE_FONT));
            document.add(Chunk.NEWLINE);

            for (int index = 0; index < profiles.size(); index++) {
                addProfileCard(document, writer, profiles.get(index));
                if (index < profiles.size() - 1) {
                    document.newPage();
                }
            }

            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException exception) {
            throw new IllegalStateException("Unable to generate PDF", exception);
        }
    }

    private void addProfileCard(Document document, PdfWriter writer, Profile profile) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2.2f, 1f});
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell detailsCell = new PdfPCell();
        detailsCell.setPadding(14);
        detailsCell.addElement(new Paragraph(value(profile.getRegistrationNumber()), TITLE_FONT));
        detailsCell.addElement(row("Name", profile.getFullName()));
        detailsCell.addElement(row("Type", profile.getType() == null ? null : profile.getType().name()));
        detailsCell.addElement(row("Department", profile.getDepartment()));
        detailsCell.addElement(row("Email", profile.getEmail()));
        detailsCell.addElement(row("Phone", profile.getPhone()));
        detailsCell.addElement(row("QR text", qrText(profile)));
        detailsCell.addElement(row("Barcode text", barcodeText(profile)));
        table.addCell(detailsCell);

        PdfPCell codeCell = new PdfPCell();
        codeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        codeCell.setPadding(14);
        codeCell.addElement(qrImage(qrText(profile)));
        codeCell.addElement(new Paragraph(" "));
        codeCell.addElement(barcodeImage(writer, profile));
        table.addCell(codeCell);

        document.add(table);
    }

    private Paragraph row(String label, String value) {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Phrase(label + ": ", LABEL_FONT));
        paragraph.add(new Phrase(value(value), BODY_FONT));
        return paragraph;
    }

    private Image qrImage(String text) throws BadElementException {
        BarcodeQRCode qrCode = new BarcodeQRCode(text, 120, 120, null);
        Image image = qrCode.getImage();
        image.setAlignment(Element.ALIGN_CENTER);
        return image;
    }

    private Image barcodeImage(PdfWriter writer, Profile profile) throws BadElementException {
        PdfContentByte contentByte = writer.getDirectContent();
        String text = barcodeText(profile);
        if (profile.getBarcodeType() == BarcodeType.EAN_13 && text.matches("\\d{12,13}")) {
            BarcodeEAN barcode = new BarcodeEAN();
            barcode.setCodeType(BarcodeEAN.EAN13);
            barcode.setCode(text.length() == 12 ? text : text.substring(0, 12));
            return barcode.createImageWithBarcode(contentByte, null, null);
        }

        Barcode128 barcode = new Barcode128();
        barcode.setCode(text);
        barcode.setCodeType(Barcode128.CODE128);
        return barcode.createImageWithBarcode(contentByte, null, null);
    }

    private String qrText(Profile profile) {
        return "IDCARD|" + value(profile.getUuid()) + "|" + value(profile.getRegistrationNumber());
    }

    private String barcodeText(Profile profile) {
        return value(profile.getRegistrationNumber());
    }

    private String value(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}
