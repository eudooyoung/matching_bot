package com.multi.matchingbot;

import com.multi.matchingbot.common.config.RoleAccessProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableConfigurationProperties(RoleAccessProperties.class)
public class MatchingBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchingBotApplication.class, args);

    }
//    @Bean
//    public CommandLineRunner printAllRequestMappings(ApplicationContext ctx) {
//        return args -> {
//            System.out.println("===== 등록된 RequestMapping 경로 목록 =====");
//            Map<String, Object> beans = ctx.getBeansWithAnnotation(RestController.class);
//            for (Object bean : beans.values()) {
//                RequestMapping rm = bean.getClass().getAnnotation(RequestMapping.class);
//                if (rm != null) {
//                    System.out.println("클래스 레벨 매핑: " + Arrays.toString(rm.value()));
//                }
//            }
//
//            Map<RequestMappingInfo, HandlerMethod> handlerMethods = ctx.getBean("requestMappingHandlerMapping", org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping.class).getHandlerMethods();
//            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
//                System.out.println(entry.getKey());
//            }
//            System.out.println("=======================================");
//        };
//    }
//    @Bean
//    public ChatbotRestController forceRegisterChatbotController(ChatbotReportService a, ChatbotJobReviewService b, ChatbotFreeTalkService c) {
//        System.out.println("✅ ChatbotRestController 강제 등록됨");
//        return new ChatbotRestController(a, b, c);
//    }


}
