package com.internousdev.olive.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.olive.dao.MCategoryDAO;
import com.internousdev.olive.dao.ProductInfoDAO;
import com.internousdev.olive.dto.MCategoryDTO;
import com.internousdev.olive.dto.ProductInfoDTO;
import com.internousdev.olive.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;

public class SearchItemAction extends ActionSupport implements SessionAware{
	private String keywords;
	private int categoryId;
	private List<ProductInfoDTO> productInfoDTOList;
	private List<String> keywordsErrorMessageList;
	private Map<String, Object> session;

	public String execute() {

		//カテゴリー選択なし: 1 カテゴリー選択あり: 選択したcategoryId
		if(categoryId == 0) {
			categoryId = 1;
		}

		//処理用の変数に値を入れる キーワードが null,""," ","　"の時に空文字に設定
		if(StringUtils.isBlank(keywords)) {
			keywords = "";
			//キーワードを"　"を" "に変換 " "が２つ以上ある場合" "を１つにする
		}else {
			keywords = keywords.replaceAll("　", " ").replaceAll("\\s{2,}", " ").trim();
		}

		//エラーが該当すれば、エラーメッセージを返す
		if(!keywords.equals("")) {
			InputChecker inputChecker = new InputChecker();
			keywordsErrorMessageList = inputChecker.doCheck("検索ワード", keywords, 0, 50, true, true, true, true, true, true);

			if(keywordsErrorMessageList.size() > 0) {
				return SUCCESS;
			}
		}

		ProductInfoDAO productInfoDAO = new ProductInfoDAO();
		if(categoryId == 1) {
			productInfoDTOList = productInfoDAO.getProductInfoListByKeyword(keywords.split(" "));
		}else {
			productInfoDTOList = productInfoDAO.getProductInfoListByCategoryAndKeyword(categoryId,keywords.split(" "));
		}

		//カテゴリーリストがsessionに保持されていない場合、画面上部にカテゴリーが表示されない為
		//カテゴリーリストをsessionに保持する。
		if(!session.containsKey("mCategoryDTOList")){
			List<MCategoryDTO> mCategoryDTOList = new ArrayList<MCategoryDTO>();
			MCategoryDAO mCategoryDAO = new MCategoryDAO();

			mCategoryDTOList = mCategoryDAO.getMcategoryList();

			session.put("mCategoryDTOList", mCategoryDTOList);
		}

		return SUCCESS;
	}

	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public List<ProductInfoDTO> getProductInfoDTOList() {
		return productInfoDTOList;
	}
	public void setProductInfoDTOList(List<ProductInfoDTO> productInfoDTOList) {
		this.productInfoDTOList = productInfoDTOList;
	}
	public List<String> getKeywordsErrorMessageList() {
		return keywordsErrorMessageList;
	}
	public void setKeywordsErrorMessageList(List<String> keywordsErrorMessageList) {
		this.keywordsErrorMessageList = keywordsErrorMessageList;
	}
	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}