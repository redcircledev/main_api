package com.rcdev.main_api.Controller;

import com.sun.org.apache.xml.internal.serializer.AttributesImplSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {

    Logger logger = LoggerFactory.getLogger(ApiController.class);

    @RequestMapping("/")
    public ModelAndView getHomeURL(){
        String projectUrl = "http://localhost:8080/swagger-ui/";
        logger.info("root called redirecting to swagger...");
        return new ModelAndView("redirect:" + projectUrl);
    }

}
