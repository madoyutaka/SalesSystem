package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.LoginModel;

/*見積管理関係のコントローラー*/
@Controller
public class QuotationManagementController  {
	String message;
	 @Autowired
	 HttpSession session;

	 /*見積管理画面へ遷移*/
	    @RequestMapping("QuotationManagement")
	    public String quotation(Model model) {
	    	if(session.getAttribute("data") == null) {
	    		message ="IDとパスワードを入力してください";
	    	    model.addAttribute("indexForm", new LoginModel());
	    	    model.addAttribute("message" , message);
	    	    return "Login";
	    	}

	        return "html/QuotationManagement";
	    }

    /*Jdbcテンプレート*/
	@Autowired
	JdbcTemplate jdbcTemplate;


}


