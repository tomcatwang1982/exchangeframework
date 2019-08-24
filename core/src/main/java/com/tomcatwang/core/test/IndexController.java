package com.tomcatwang.core.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Random;

@Controller
@RequestMapping("/test")
public class IndexController {
    @GetMapping("/index")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("socket");
        Random random = new Random();
        mav.addObject("uid", random.nextInt(50));
        return mav;
    }
}
