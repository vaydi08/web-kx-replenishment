package com.megajoy.thirdpart.report.thread;

import org.sol.util.thread.BaseThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.megajoy.thirdpart.report.service.ThirdpartService;

@Controller
public class ResumeUnsendThread extends BaseThread{

	public ResumeUnsendThread() {
		super(5, 600, "Resume unprocessed working data thread");
	}

	@Autowired
	private ThirdpartService thirdpartService;
	
	@Override
	public void dowhileAction() {
		thirdpartService.resumeUnsendData();
	}

}
