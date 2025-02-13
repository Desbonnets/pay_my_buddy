package com.oc.pay_my_buddy.config;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Configuration
public class ThymeleafConfig {

    public SpringTemplateEngine thymeleafLayoutDialect() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.addDialect(new LayoutDialect());
        return engine;
    }
}
