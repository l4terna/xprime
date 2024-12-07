package com.laterna.xaxathonprime.event;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.laterna.xaxathonprime.discipline.Discipline;
import com.laterna.xaxathonprime.eventbase.EventBase;
import com.laterna.xaxathonprime.eventrequest.EventRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventPdfService {

    private static final String PDF_PATH = "events.pdf";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String FONT_PATH = "src/main/resources/pdf/times.ttf";

    private final EventRepository eventRepository;

    private PdfFont createFont() throws IOException {
        return PdfFontFactory.createFont(FONT_PATH, PdfEncodings.IDENTITY_H);
    }

    private List<EventBase> getConfirmedUpcomingEvents() {
        return eventRepository.findConfirmedUpcomingEvents(LocalDateTime.now())
                .stream()
                .map(Event::getBase)
                .collect(Collectors.toList());
    }

    public void generateEventsPdf() {
        List<EventBase> events = getConfirmedUpcomingEvents();
        if (events.isEmpty()) {
            log.info("No confirmed upcoming events found");
            return;
        }

        try {
            PdfWriter writer = new PdfWriter(PDF_PATH);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Initialize font with Russian support
            PdfFont font = createFont();
            document.setFont(font);

            Table table = new Table(UnitValue.createPercentArray(new float[]{10, 35, 15, 25, 15}));
            table.setWidth(UnitValue.createPercentValue(100));

            // Add headers
            addHeaderCell(table, "Номер\nмероприятия\nв ЕКП", font);
            addHeaderCell(table, "Наименование спортивного мероприятия\n(пол, возрастная группа)\n(дисциплина, программа)", font);
            addHeaderCell(table, "Сроки\nпроведения", font);
            addHeaderCell(table, "Место проведения\n(страна (+), регион, город)\n(спортивный объект, центр)", font);
            addHeaderCell(table, "Количество\nучастников\n(человек)", font);

            // Add event rows
            for (EventBase eventBase : events) {
                addEventRow(table, eventBase, font);
            }

            document.add(table);
            document.close();

            log.info("Successfully generated PDF with {} events", events.size());
        } catch (IOException e) {
            log.error("Error generating PDF: {}", e.getMessage());
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    private void addEventRow(Table table, EventBase eventBase, PdfFont font) {
        // Event ID
        addCell(table, eventBase.getId().toString(), TextAlignment.LEFT, font);

        // Event Name and Details
        String gender = eventBase.getGender() != null ? eventBase.getGender() : "мужчины, женщины";
        String disciplines = eventBase.getDisciplines().stream()
                .map(Discipline::getName)
                .collect(Collectors.joining(", "));

        String ageRange;
        if (eventBase.getMinAge() == null && eventBase.getMaxAge() == null) {
            ageRange = "без ограничений по возрасту";
        } else if (eventBase.getMinAge() != null && eventBase.getMaxAge() == null) {
            ageRange = "от " + eventBase.getMinAge() + " лет и старше";
        } else if (eventBase.getMinAge() == null && eventBase.getMaxAge() != null) {
            ageRange = "до " + eventBase.getMaxAge() + " лет";
        } else {
            ageRange = "от " + eventBase.getMinAge() + " до " + eventBase.getMaxAge() + " лет";
        }

        String eventDetails = gender + " " + ageRange + "\n" + disciplines;
        addCell(table, eventDetails, TextAlignment.LEFT, font);

        // Dates
        String dates = eventBase.getStartDate().format(DATE_FORMATTER) + "\n" +
                eventBase.getEndDate().format(DATE_FORMATTER);
        addCell(table, dates, TextAlignment.LEFT, font);

        // Location
        String location = (eventBase.getRegion() != null ?
                eventBase.getRegion().getName() + ", город " : "город ") +
                eventBase.getLocation();
        addCell(table, location, TextAlignment.LEFT, font);

        // Participants
        addCell(table, eventBase.getMaxParticipants() != null ?
                eventBase.getMaxParticipants().toString() : "", TextAlignment.RIGHT, font);
    }

    private void addHeaderCell(Table table, String text, PdfFont font) {
        Cell cell = new Cell()
                .add(new Paragraph(text).setFont(font))
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        table.addCell(cell);
    }

    private void addCell(Table table, String text, TextAlignment alignment, PdfFont font) {
        Cell cell = new Cell()
                .add(new Paragraph(text).setFont(font))
                .setTextAlignment(alignment);
        table.addCell(cell);
    }
}