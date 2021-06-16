package com.example.demo.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.model.ClientOrderModel;
@Component
public class ClientOrderJdbc {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//出荷予定日の更新
	public String shipmentDueDateUpdateJdbc(int client_order_no,String shipment_due_date) {
		try {
			this.jdbcTemplate.update("update clientorder set shipment_due_date= ? where client_order_no= ?",shipment_due_date,client_order_no);
		}catch(Exception ex) {
			return "エラーが発生しました。";
		}
		return "更新が完了しました。";
	}
	
	//出荷日の更新
		public String shipmentDateUpdateJdbc(int client_order_no) {
			try {
				this.jdbcTemplate.update("update clientorder set shipment_date = current_timestamp where client_order_no= ?",client_order_no);
			}catch(Exception ex) {
				return "エラーが発生しました。";
			}
			return "更新が完了しました。";
		}
	
	
	//商品名から受注履歴を取得
		public ArrayList<ClientOrderModel> getClientOrderLog(String searchWord){
			ArrayList<ClientOrderModel> returnList = new ArrayList<ClientOrderModel>();
			try {
				List<Map<String, Object>> itemDataList;
				if(searchWord.equals("出荷予定日未入力の商品")) {
					//出荷予定日未記入の情報を取得
					String sql = "select * from clientorder where shipment_due_date is null";
					itemDataList = jdbcTemplate.queryForList(sql);
				}else if(searchWord.equals("出荷前の商品")) {
					//出荷予定日記入済みかつ未出荷の情報を取得
					String sql = " select * from clientorder where shipment_due_date is not null && shipment_date is null";
					itemDataList = jdbcTemplate.queryForList(sql);
				}else if(searchWord.equals("出荷済みの商品")){
					//出荷日記入済み(出荷済み)の情報を取得
					String sql = "select * from clientorder where shipment_date is not null";
					itemDataList = jdbcTemplate.queryForList(sql);
				}else {
					String sql = "SELECT * FROM clientorder WHERE item_name LIKE ?";
					itemDataList = jdbcTemplate.queryForList(sql, '%'+searchWord+'%');
				}
				//格納する
				for(Map<String, Object> mapData : itemDataList) {
					ClientOrderModel returnData = new ClientOrderModel();
					returnData.setClient_order_no((int)mapData.get("client_order_no"));
					returnData.setItem_name((String)mapData.get("item_name"));
					returnData.setItem_product_no((String)mapData.get("item_product_no"));
					returnData.setItem_buy_count((int)mapData.get("item_buy_count"));
					returnData.setTotal_price((int)mapData.get("total_price"));
					returnData.setItem_buy_date((Date)mapData.get("item_buy_date"));
					returnData.setShipment_due_date((Date)mapData.get("shipment_due_date"));
					returnData.setShipment_date((Date)mapData.get("shipment_date"));
					returnList.add(returnData);
				}
			}catch(Exception ex) {
			
			}
			return returnList;
		}
		

		//受注情報を全件取得
		public  ArrayList<ClientOrderModel> getClientOrderDataList(){
			ArrayList<ClientOrderModel> returnList = new ArrayList<ClientOrderModel>();
			try {
			String sql = "SELECT * FROM clientorder";
			List <Map<String, Object>> clientOrderDataList = this.jdbcTemplate.queryForList(sql);
			//格納する
			for(Map<String, Object> clientOrderData : clientOrderDataList) {
				ClientOrderModel returnData = new ClientOrderModel();
				returnData.setClient_order_no((int)clientOrderData.get("client_order_no"));
				returnData.setItem_name((String)clientOrderData.get("item_name"));
				returnData.setItem_product_no((String)clientOrderData.get("item_product_no"));
				returnData.setItem_buy_count((int)clientOrderData.get("item_buy_count"));
				returnData.setTotal_price((int)clientOrderData.get("total_price"));
				returnData.setItem_buy_date((Date)clientOrderData.get("item_buy_date"));
				returnData.setShipment_due_date((Date)clientOrderData.get("shipment_due_date"));
				returnData.setShipment_date((Date)clientOrderData.get("shipment_date"));
				returnList.add(returnData);
			}
			}catch(Exception ex) {
				return null;
			}
			return returnList;
		}
		
		
		
		

}
