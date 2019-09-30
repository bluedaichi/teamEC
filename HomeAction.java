package com.internousdev.olive.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.olive.dao.MCategoryDAO;
import com.internousdev.olive.dto.MCategoryDTO;
import com.internousdev.olive.util.CommonUtility;
import com.opensymphony.xwork2.ActionSupport;

public class HomeAction extends ActionSupport implements SessionAware{
	private Map<String,Object> session;

	public String execute() throws SQLException {

		//未ログイン:0/ログイン済:1
		if(!session.containsKey("logined")) {
			session.put("logined", 0);
		}

		//tempUserId生成理由:
		//未ログイン状態でもカード情報を取得するため
		if(!session.containsKey("tempUserId") && Integer.parseInt(session.get("logined").toString()) == 0){
			CommonUtility cU  = new CommonUtility();
			session.put("tempUserId",cU.getRamdomValue()) ;
		}

		//Mカテゴリー取得 保持理由:
		//Home画面上に常時カテゴリー情報を表示したいため
		if(!session.containsKey("mCategoryDTOList")) {
			List<MCategoryDTO> mCategoryDTOList = new ArrayList<MCategoryDTO>();
			MCategoryDAO mCategoryDAO = new MCategoryDAO();

			mCategoryDTOList = mCategoryDAO.getMcategoryList();
			session.put("mCategoryDTOList", mCategoryDTOList);
		}
		return SUCCESS;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
