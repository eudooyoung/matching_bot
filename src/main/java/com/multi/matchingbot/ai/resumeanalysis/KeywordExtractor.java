package com.multi.matchingbot.ai.resumeanalysis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class KeywordExtractor {

    public static void main(String[] args) {
        try {
            // 1. HTML 파싱
            Document doc = Jsoup.connect("http://localhost:8080").get();

            // 2. 원문과 키워드 추출
            Elements paragraphs = doc.select("p");
            Elements keywordBlocks = doc.select("div:has(span.keyword)");

            for (int i = 0; i < Math.min(paragraphs.size(), keywordBlocks.size()); i++) {
                Element original = paragraphs.get(i);
                Elements keywords = keywordBlocks.get(i).select("span.keyword");

                System.out.println("✅ 원문: " + original.text());

                System.out.print("🔑 키워드: ");
                for (Element kw : keywords) {
                    System.out.print(kw.text() + " ");
                }
                System.out.println("\n--------------------------------------");
            }

        } catch (IOException e) {
            System.out.println("❌ 서버 연결 실패 또는 파싱 오류:");
            e.printStackTrace();
        }
    }
}
