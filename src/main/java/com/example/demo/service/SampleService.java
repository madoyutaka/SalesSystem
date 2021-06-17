package com.example.demo.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class SampleService {
	 @Autowired
	    protected JdbcTemplate jdbcTemplate;

	 @Autowired
	 HttpSession session;

	    public Map<String, Object> selectAddresses(String id,String pass, Model model) {
	    	System.out.println(jdbcTemplate);
	        return jdbcTemplate.queryForMap("SELECT user_no, user_id, password FROM user WHERE user_id = ? AND password = ?", id, pass);
	    }
}
