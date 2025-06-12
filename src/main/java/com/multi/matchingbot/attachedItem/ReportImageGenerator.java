package com.multi.matchingbot.attachedItem;

import com.multi.matchingbot.attachedItem.domain.ReportType;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportImageGenerator {

    private final TemplateEngine templateEngine;

    /**
     * @param reportData json형식으로 전처리된 AI 응답
     * @param reportType 보고서 형식: 전체 || 요약
     * @return 이미지
     * @throws Exception 예외처리
     */
    public BufferedImage convertReportToImage(Map<String, Object> reportData, ReportType reportType) throws Exception {
        // html 렌더링
        Context context = new Context();
        context.setVariables(reportData);
        String html = templateEngine.process(reportType.getTemplatePath(), context);

        // html -> pdf
        ByteArrayOutputStream pdfOutPut = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();

        // 로컬용
        /* File fontFile = new File(getClass().getClassLoader().getResource("fonts/NanumGothic.ttf").toURI());*/

        // jar 내부용
        InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/NanumGothic.ttf");
        if (fontStream == null) {
            throw new IllegalStateException("폰트 파일을 찾을 수 없습니다.");
        }

        File tempFontFile = File.createTempFile("NanumGothic", ".ttf");
        tempFontFile.deleteOnExit();

        try (OutputStream out = new FileOutputStream(tempFontFile)) {
            fontStream.transferTo(out);
        }

        File fontFile = tempFontFile;


        System.out.println("✅ 폰트 파일 존재? " + fontFile.exists());
        System.out.println("✅ 경로: " + fontFile.getAbsolutePath());
        builder.useFont(fontFile, "NanumGothic");
        builder.useFastMode();
        builder.withHtmlContent(html, null);
        builder.toStream(pdfOutPut);
        builder.run();

        // pdf -> image
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfOutPut.toByteArray()))) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage fullImage = renderer.renderImageWithDPI(0, ReportType.DPI);  // 화질 제어
            log.info("PDF 페이지 높이: {}px", fullImage.getHeight());
            log.info("PDF 페이지 너비: {}px", fullImage.getWidth());

            // ✅ 위쪽, 아래쪽 잘라내기
            int cropTop = ReportType.CROP_TOP;
            int cropBottom = reportType.getCropBottom();
            int cropLeft = ReportType.CROP_LEFT;
            int cropRight = ReportType.CROP_RIGHT;
            int croppedHeight = fullImage.getHeight() - (cropTop + cropBottom);
            int croppedWidth = fullImage.getWidth() - (cropLeft + cropRight);

            BufferedImage image = fullImage.getSubimage(
                    cropLeft, // x시작 위치
                    cropTop, // y 시작위치
                    croppedWidth, // 잘라낼 너비
                    croppedHeight // 잘라낼 높이
            );

            log.info("리포트 이미지 생성 성공 (w={}px, h={}px)", image.getWidth(), image.getHeight());
            return image;
        }
    }

    public InputStream bufferedImageToInputStream(BufferedImage image, String formatName) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, formatName, os);
        return new ByteArrayInputStream(os.toByteArray());
    }

}
