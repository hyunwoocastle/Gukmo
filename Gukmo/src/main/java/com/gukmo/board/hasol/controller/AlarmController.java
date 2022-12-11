package com.gukmo.board.hasol.controller;

import java.util.List;
import java.util.Map;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gukmo.board.hasol.service.InterAlarmService;
import com.gukmo.board.model.AlarmVO;
import com.gukmo.board.model.MemberVO;

@Controller
public class AlarmController {

	@Autowired // Type 에 따라 알아서 Bean 을 주입해준다.
	private InterAlarmService service;

	@RequestMapping(value="/showAlarmCnt.do")
	public ModelAndView showAlarmCnt(ModelAndView mav, HttpServletRequest request) {
	
		HttpSession session = request.getSession();
		MemberVO user = (MemberVO)session.getAttribute("user");
		String userid = user.getUserid();
		
		int n = service.showAlarmCnt(userid);
		
	    mav.setViewName("board/alarm/alarm.tiles1");
	    return mav;
	}
	
	@ResponseBody
	@RequestMapping(value="/getNotAlarmList.do", method= {RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String getNotAlarmList(HttpServletRequest request, @RequestParam Map<String, String> paraMap) {
	
		HttpSession session = request.getSession();
		MemberVO user = (MemberVO)session.getAttribute("user");
		String userid = user.getUserid();
			
	    // 읽지 않은 알림 데이터 전체 가져오기
	    List<AlarmVO> notReadAlarmList = service.getNotReadAlarmList(userid);
	    
	    JSONObject jsonObj = new JSONObject();
	    jsonObj.put("notReadAlarmList" ,  notReadAlarmList);
		return jsonObj.toString();
	}
	
	@RequestMapping(value="/getAlarmList.do")
	public ModelAndView getAlarmList(ModelAndView mav, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		MemberVO user = (MemberVO)session.getAttribute("user");
		String userid = user.getUserid();
		
	    //1.해당 유저 알림데이터 전체 가져오기
	    List<AlarmVO> getAlarm = service.getAlarm(userid);
	    //for(InformDTO informDTO :getInform) {
	        //System.out.println("getInform : "+informDTO.toString());  
	    //}
	    
	    return mav;
	}
	
	@RequestMapping(value="/changeIsRead.do")
	public void changeIsread(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		MemberVO user = (MemberVO)session.getAttribute("user");
		String userid = user.getUserid();
		
		// 읽음 컬럼값 변경하기
		int n = service.changeIsRead(userid);
		
		
	}
}