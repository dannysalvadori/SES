package com.fdmgroup.ses.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.model.Role;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.RoleRepository;
import com.fdmgroup.ses.repository.UserRepository;

@Controller
public class DemoController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;

	@RequestMapping(value="/")
    public ModelAndView index() {
		Authentication roles = SecurityContextHolder.getContext().getAuthentication();
		String auths = roles.getAuthorities().toString();
        return new ModelAndView("index");
    }
	
	@RequestMapping(value="/hello")
    public String sayHi(Model model) {
//		userRepo.save(new User());
		List<User> users = userRepo.findAll();
//		roleRepo.save(new Role());
		List<Role> roles = roleRepo.findAll();
		model.addAttribute("roleCount", roles.size());
		model.addAttribute("userCount", users.size());
        return "user/hi";
	}
	
	@RequestMapping(value="/exception")
    public ModelAndView throwException(ModelAndView modelAndView) {
		modelAndView.setViewName("hi");
		modelAndView.addObject("number", 7/0);
        return modelAndView;
	}
	
	@RequestMapping(value="/admin")
    public ModelAndView adminHome(ModelAndView modelAndView) {
		modelAndView.setViewName("admin/adminHome");
        return modelAndView;
	}
	
	@RequestMapping(value="/adminEdit")
    public ModelAndView adminEdit(ModelAndView modelAndView) {
		modelAndView.setViewName("hi");
        return modelAndView;
	}
	
}
