package com.sol.kx.web.service.bean;

public class ResultBean {
	// 提交请求执行结果
	private boolean resultSuccess;
	// 提交请求错误信息
	private String resultErrMsg;
	
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
	
	/**
	 * 提交请求 成功
	 * @return
	 */
	public static ResultBean RESULT_SUCCESS() {
		ResultBean bean = new ResultBean();
		bean.setResultSuccess(true);
		
		return bean;
	}
	
	/**
	 * 提交请求 失败
	 * @param msg
	 * @return
	 */
	public static ResultBean RESULT_ERR(String msg) {
		ResultBean bean = new ResultBean();
		bean.setResultSuccess(false);
		bean.setResultErrMsg(msg);
		return bean;
	}
	public Object getReserve() {
		return reserve;
	}
	public void setReserve(Object reserve) {
		this.reserve = reserve;
	}
}
