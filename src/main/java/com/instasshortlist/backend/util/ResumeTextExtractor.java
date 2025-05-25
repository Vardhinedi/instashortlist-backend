package com.instashortlist.backend.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.io.File;

public class ResumeTextExtractor {
    public static Mono<String> extractTextFromPdf(FilePart filePart) {
        File tempFile = new File("temp_resume.pdf");
        return filePart.transferTo(tempFile)
                .then(Mono.fromCallable(() -> {
                    try (PDDocument doc = PDDocument.load(tempFile)) {
                        return new PDFTextStripper().getText(doc);
                    }
                }));
    }
}