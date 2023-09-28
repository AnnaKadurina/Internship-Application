package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GenerateBookingFileUseCase;
import getawaygo_project.getawaygo_backend.business.exception.BookingNotFoundException;
import getawaygo_project.getawaygo_backend.business.exception.FileException;
import getawaygo_project.getawaygo_backend.persistance.BookingRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.BookingEntity;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GenerateBookingFileUseCaseImpl implements GenerateBookingFileUseCase {
    private BookingRepository bookingRepository;

    @Override
    public String generateFile(long id) {
        Optional<BookingEntity> bookingEntity = bookingRepository.findById(id);

        if (bookingEntity.isEmpty())
            throw new BookingNotFoundException();

        Instant startDateInstant = bookingEntity.get().getStartDate();
        LocalDateTime startDate = LocalDateTime.ofInstant(startDateInstant, ZoneId.systemDefault());

        DateTimeFormatter startDateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        String formattedStartDate = startDate.format(startDateFormatter);

        Instant endDateInstant = bookingEntity.get().getEndDate();
        LocalDateTime endDate = LocalDateTime.ofInstant(endDateInstant, ZoneId.systemDefault());

        DateTimeFormatter endDateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        String formattedEndDate = endDate.format(endDateFormatter);

        try (PDDocument document = new PDDocument()) {
//            in this part of the code we create new document and all of the data inside it is put on the X and Y.
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);

            float startY = 700;
            float lineHeight = 18;

            contentStream.beginText();

            contentStream.setFont(PDType1Font.TIMES_ROMAN, 22);
            float headingWidth = PDType1Font.TIMES_ROMAN.getStringWidth("Booking Confirmation") / 1000f * 16;
            float headingX = (page.getMediaBox().getWidth() - headingWidth) / 2 - 20;
            contentStream.newLineAtOffset(headingX, startY);
            contentStream.showText("Booking Confirmation");
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);

            contentStream.newLineAtOffset(-headingX + 20, 0);
            contentStream.showText("Booking ID: " + bookingEntity.get().getBookingId());
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("Start date: " + formattedStartDate);
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("End date: " + formattedEndDate);
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("Total price: " + bookingEntity.get().getPrice() + "€");

            contentStream.endText();

            contentStream.close();

            String filePath = "bookingFiles/booking_confirmation.pdf";

            document.save("public/" + filePath);

            String downloadLink = "http://localhost:8080/" + filePath;

            return downloadLink;
        } catch (Exception e) {
            throw new FileException();
        }
    }

}
