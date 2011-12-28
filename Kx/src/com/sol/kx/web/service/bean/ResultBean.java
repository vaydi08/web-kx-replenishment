package com.sol.kx.web.service.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultBean {
	// 提交请求执行结果
	private boolean resultSuccess;
	// 提交请求错误信息
	private String resultErrMsg;
	
	private Object input;
	
	private Object reserve;
	
	public boolean isResultSuccess() {
		return resultSuccess;
	}
	public void setResultSuccess(boolean resultSuccess) {
		this.resultSuccess = resultSuccess;
	}
	public String getResultErrMsg() {
		return resultErrMsg;
	}
	public void setResultErrMsg(String resultErrMsg) {
		this.resultErrMsg = resultErrMsg;
	}
	
	public String getInputJson() {
		JSONObject json = new JSONObject(input);
		try {
			json.put("success", this.resultSuccess);
			if(!this.resultSuccess)
				json.put("msg", this.resultErrMsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json.toString();
	}
	
	/**
	 * 提交请求 成功
	 * @return
	 */
	public static ResultBean RESULT_SUCCESS(Object input) {
		ResultBean bean = new ResultBean();
		bean.setResultSuccess(true);
		bean.setInput(input);
		
		return bean;
	}
	
	/**
	 * 提交请求 失败
	 * @param msg
	 * @return
	 */
	public static ResultBean RESULT_ERR(String msg,Object input) {
		ResultBean bean = new ResultBean();
		bean.setResultSuccess(false);
		bean.setResultErrMsg(msg);
		bean.setInput(input);
		return bean;
	}
	public Object getReserve() {
		return reserve;
	}
	public void setReserve(Object reserve) {
		this.reserve = reserve;
	}
	public void setInput(Object input) {
		this.input = input;
	}
}
