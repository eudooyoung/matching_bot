package com.multi.matchingbot.attachedItem.util;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportImageGenerator {

    private final TemplateEngine templateEngine;

    public BufferedImage convertReportToImage(Map<String, Object> reportData) throws Exception {
        // html 렌더링
        Context context = new Context();
        context.setVariables(reportData);
        String html = templateEngine.process("chatbot/evaluation-report", context);

        // html -> pdf
        ByteArrayOutputStream pdfOutPut = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        File fontFile = new File(getClass().getClassLoader().getResource("fonts/NanumGothic.ttf").toURI());
        System.out.println("✅ 폰트 파일 존재? " + fontFile.exists());
        System.out.println("✅ 경로: " + fontFile.getAbsolutePath());
        builder.useFont(fontFile, "NanumGothic");
        builder.useFastMode();
        builder.withHtmlContent(html, null);
        builder.toStream(pdfOutPut);
        builder.run();

        // pdf -> image
        BufferedImage image;
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfOutPut.toByteArray()))) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage fullImage = renderer.renderImageWithDPI(0, 200);  // 화질 제어
            log.info("PDF 페이지 높이: {}px", fullImage.getHeight());
            log.info("PDF 페이지 너비: {}px", fullImage.getWidth());

            // ✅ 위쪽, 아래쪽 잘라내기
            int cropTop = 60;
            int cropBottom = 100;
            int croppedHeight = fullImage.getHeight() - cropTop - cropBottom;

            image = fullImage.getSubimage(0, cropTop, fullImage.getWidth(), croppedHeight);
            //            image = fullImage;
        }

        log.info("리포트 이미지 생성 성공 (w={}px, h={}px)", image.getWidth(), image.getHeight());
        return image;
    }
}
