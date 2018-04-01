package com.fdmgroup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

	@RequestMapping(value="/")
    public String index() {
        return "index";
    }
	
	@RequestMapping(value="/hello")
    public String sayHi(Model model) {
		model.addAttribute("number", 7);
        return "hi";
    }
	
}
