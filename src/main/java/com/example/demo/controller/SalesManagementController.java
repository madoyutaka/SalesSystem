package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.LoginModel;

/*売上関係のコントローラー*/
@Controller
public class SalesManagementController  {
	String message;
	 @Autowired
	 HttpSession session;

	 /*売上集計画面へ遷移*/
	    @RequestMapping("SalesManagement")
	    public String sales(Model model) {
	    	if(session.getAttribute("data") == null) {
	    		message ="IDとパスワードを入力してください";
	    	    model.addAttribute("indexForm", new LoginModel());
	    	    model.addAttribute("message" , message);
	    	    return "Login";
	    	}

	        return "html/SalesManagement";
	    }

    /*Jdbcテンプレート*/
	@Autowired
	JdbcTemplate jdbcTemplate;


}


