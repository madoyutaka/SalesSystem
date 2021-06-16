package com.example.demo.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.jdbc.ClientOrderJdbc;
import com.example.demo.jdbc.ItemJdbc;
import com.example.demo.logic.ItemLogic;
import com.example.demo.logic.VenderOrderLogic;
import com.example.demo.model.ClientOrderModel;
import com.example.demo.model.InventoryModel;
import com.example.demo.model.LoginModel;
import com.example.demo.model.VenderOrderModel;

/*在庫管理関係のコントローラー*/
@Controller
public class InventoryManagementController {
	String message;
	@Autowired
	HttpSession session;

	@Autowired
	private ItemJdbc itemJdbc;

	@Autowired
	private ClientOrderJdbc clientOrderJdbc;
	
	@Autowired
	ItemLogic itemLogic;
	
	@Autowired
	VenderOrderLogic venderOrderLogic;



	 /*入荷管理画面へ遷移*/
   	@RequestMapping("ArrivalManagement")
    	public String arrival(Model model) {
    		if(session.getAttribute("data") == null) {
    			message ="IDとパスワードを入力してください";
    	    		model.addAttribute("indexForm", new LoginModel());
    	    		model.addAttribute("message" , message);
    	    		return "Login";
    		}

        	return "html/ArrivalManagement";
    	}

	    /*出荷管理画面へ遷移*/
	    @RequestMapping("ShipmentManagement")
	    public String shipment(Model model) {
	    	if(session.getAttribute("data") == null) {
	    		message ="IDとパスワードを入力してください";
	    	    model.addAttribute("indexForm", new LoginModel());
	    	    model.addAttribute("message" , message);
	    	    return "Login";
	    	}

	        return "html/ShipmentManagement";
	    }

	    /*在庫管理画面へ遷移*/
	    @RequestMapping("InventoryManagement")
	    public String inventory(Model model) {
		if(session.getAttribute("data") == null) {
			message ="IDとパスワードを入力してください";
		    model.addAttribute("indexForm", new LoginModel());
		    model.addAttribute("message" , message);
		    return "Login";
		}
	        return "html/InventoryManagement";
	    }

	    /*棚卸・在庫調整画面へ遷移*/
	    @RequestMapping("InventoryAdjustment")
	    public String  	inventoryadjustment(Model model) {
		if(session.getAttribute("data") == null) {
			message ="IDとパスワードを入力してください";
		    model.addAttribute("indexForm", new LoginModel());
		    model.addAttribute("message" , message);
		    return "Login";
		}
	        return "html/InventoryAdjustment";
	    }

	    @Autowired
		private ItemLogic ItemLogic;

	    @RequestMapping("InventrySearch")
		public String inventoryadjustmentsearch(@RequestParam("item_name") String searchWord, Model model) {
			ArrayList<InventoryModel> returnList = ItemLogic.getInventoryLog(searchWord);
			//検索結果を渡す
			if(returnList.size()==0){
				model.addAttribute("searchList", null);
			}else{
				model.addAttribute("searchList", returnList);
			}
			model.addAttribute("resultText", "検索結果"+returnList.size()+"件");
			return "html/InventoryAdjustment";
		}
	


//出荷管理↓
	//データベースと照合して受注情報を表示(ボタン)
	@RequestMapping("ClientOrderSearchButton")
		public String orderSearchButton(@RequestParam("searchWord") String searchWord,Model model){
		ArrayList<ClientOrderModel> list = clientOrderJdbc.getClientOrderLog(searchWord);
		model.addAttribute("clientOrderList",list);
		return "html/ShipmentManagement";
	}
			    
		
	//商品名をデータベースと照合して受注情報を表示(フォーム)
	@RequestMapping("ClientOrderSearchForm")
	public String orderSearchForm(@RequestParam("searchWord") String searchWord, Model model){
		ArrayList<ClientOrderModel> list = clientOrderJdbc.getClientOrderLog(searchWord);
		model.addAttribute("clientOrderList",list);
		model.addAttribute("item_name", searchWord);
	  	return "html/ShipmentManagement";
	}

	//出荷予定日の更新
	@RequestMapping("ShipmentDueDateUpdate")
	public String shipmentDueDateUpdate(@RequestParam("client_order_no") String client_order_no,@RequestParam("shipment_due_date") String shipment_due_date ,@RequestParam("searchWord") String searchWord, Model model){
	 	int totalItemNo = clientOrderJdbc.getClientOrderDataList().size();
	    String resultText = null;
	   	int no = -1;
		try {
			no = Integer.parseInt(client_order_no);
		}catch(Exception ex){
			model.addAttribute("resultText","数値を入力してください。");
	        return "html/ShipmentManagement";
		}
		if(totalItemNo<no) {
			resultText = "入力された番号は存在しません。";
		}else{
			resultText = clientOrderJdbc.shipmentDueDateUpdateJdbc(no,shipment_due_date);
		}
		ArrayList<ClientOrderModel> list = clientOrderJdbc.getClientOrderLog(searchWord);
		model.addAttribute("clientOrderList",list);
		model.addAttribute("item_name", searchWord);
		model.addAttribute("resultText",resultText);
		return "html/ShipmentManagement";
	}
		 	
	
	//出荷日の更新(出荷確定処理)
	@RequestMapping("ShipmentDateUpdate")
	public String shipmentDateUpdate(@RequestParam("client_order_no") String client_order_no,@RequestParam("searchWord") String searchWord, Model model){
		int totalItemNo = clientOrderJdbc.getClientOrderDataList().size();
		String resultText = null;
		int no = -1;
		try {
			no = Integer.parseInt(client_order_no);
		}catch(Exception ex){
			model.addAttribute("resultText","数値を入力してください。");
			return "html/ShipmentManagement";
		}
		if(totalItemNo<no) {
			resultText = "入力された番号は存在しません。";
		}else{
			resultText = clientOrderJdbc.shipmentDateUpdateJdbc(no);
		}
		ArrayList<ClientOrderModel> list = clientOrderJdbc.getClientOrderLog(searchWord);
		model.addAttribute("clientOrderList",list);
		model.addAttribute("item_name", searchWord);
		model.addAttribute("resultText",resultText);
		return "html/ShipmentManagement";
	}
//出荷管理↑
	
		
  //在庫の更新
    @RequestMapping("InventryUpdate")
  		public String inventoryadjustmentupdate(@RequestParam("item_no") String searchWord, @RequestParam("item_stock") String updateStock,Model model)
    {
  			ArrayList<InventoryModel> returnList = ItemLogic.getInventoryUpdate(searchWord,updateStock);

  			//更新結果を渡す
  			if(returnList.size()==0){
  				model.addAttribute("searchList", null);
  				model.addAttribute("resultText", "更新できませんでした。");
  				return "html/InventoryAdjustment";
  			}else{
  				model.addAttribute("searchList", returnList);
  			}
  			model.addAttribute("resultText", "更新しました！");
  			return "html/InventoryAdjustment";
  		}

	
    //入荷確定処理
    @RequestMapping("ArrivalDateUpdate")
    public String arrivalDueDateUpdate(@RequestParam("itemNo") String itemNo, @RequestParam("searchItemName") String searchItemName, @RequestParam("selectBtn") String selectBtn, @RequestParam("search") String search, Model model) {
    	System.out.println(search+"で検索。itemName："+searchItemName+"　btnName："+selectBtn);
    	
    	//検索方法によって取得情報を変更する
    	ArrayList<VenderOrderModel> searchList = new ArrayList<VenderOrderModel>();
    	//キーワード検索
    	if(selectBtn.equals("")&& search.equals("word")){
    		searchList = venderOrderLogic.getVenderOrderLog(searchItemName);
    		model.addAttribute("search", "word");
    		model.addAttribute("searchItemName", searchItemName);
    	}
    	//button検索
    	if(searchItemName.equals("") && search.equals("button")){
    		searchList = venderOrderLogic.getVenderOrderLog(selectBtn, "button");
    		model.addAttribute("search", "button");
        	model.addAttribute("selectBtn", selectBtn);
    	}
    	if(searchList.size()>0) {
    		model.addAttribute("searchList", searchList);
    	}
    	
    	//intに変換可能か確認する
    	String noConfirmationStr = itemLogic.inputConfirmation(itemNo);
    	if(noConfirmationStr.equals("true")){
    	}else{
    		model.addAttribute("resultText", noConfirmationStr);
    		return "html/ArrivalManagement";
    	}
    	
    	//入力された番号が存在するか、既に確定されているか確認し、入荷確定処理を行う。
    	String resultText = itemLogic.checkItemNoLogic(Integer.parseInt(itemNo));
    	model.addAttribute("resultText", resultText);
    	
        return "html/ArrivalManagement";
    }
    
    //検索機能
    @RequestMapping("ArrivalDataKeywordSearch")
    public String arrivalDateSearch(@RequestParam("searchItemName") String searchItemName, Model model) {
    	ArrayList<VenderOrderModel> searchList = venderOrderLogic.getVenderOrderLog(searchItemName);
    	if(searchList.size()>0) {
    		model.addAttribute("searchList", searchList);
    	}
    	model.addAttribute("resultText", "検索結果："+searchList.size()+"件");
    	model.addAttribute("search", "word");
    	model.addAttribute("searchItemName", searchItemName);
    	return "html/ArrivalManagement";
    }
    
    //入荷前、入荷済みボタン用。検索機能
    @RequestMapping("ArrivalDataSearch")
    public String arrivalDataSearchBtn(@RequestParam("selectBtn") String selectBtn, Model model) {
    	String selectBtnText = venderOrderLogic.arrivalStateBtnStr(selectBtn);
    	if(selectBtnText.equals("エラー")) {
    		model.addAttribute("resultText", selectBtnText+"が発生しました。");
    		return "html/ArrivalManagement";
    	}
    	ArrayList<VenderOrderModel> searchList = venderOrderLogic.getVenderOrderLog(selectBtn, "button");
    	if(searchList.size()>0) {
    		model.addAttribute("searchList", searchList);
    	}
    	model.addAttribute("search", "button");
    	model.addAttribute("selectBtn", selectBtn);
    	model.addAttribute("resultText", selectBtnText+"："+searchList.size()+"件");
    	return "html/ArrivalManagement";
    }

}
