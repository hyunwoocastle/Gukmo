package com.gukmo.board.hw.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

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

import com.gukmo.board.hw.repository.InterLoginDAO;
import com.gukmo.board.hw.service.InterLoginService;
import com.gukmo.board.model.MemberVO;

@Controller
public class LoginController {
	
	@Autowired
	InterLoginService service;
	@Autowired
	InterLoginDAO dao;
	
	
	
	/**
	 * 로그인 페이지 url매핑
	 */
	@RequestMapping(value="/login.do", method= {RequestMethod.GET})  // 오로지 GET 방식만 허락하는 것임. 
	public ModelAndView login(ModelAndView mav, HttpServletRequest request) {
	  String returnUrl = request.getParameter("returnUrl");
	  HttpSession session = request.getSession();
	  
	  if(session.getAttribute("user") != null) {	//로그인한 유저가 있다면
		  mav.setViewName("redirect:/index.do");
	  }
	  else {										//로그인한 유저가 없다면
		  mav.setViewName("login/login.tiles1");
	  }
	  
	  if(returnUrl != null && !returnUrl.trim().isEmpty()) {
		  try {
			  returnUrl = URLDecoder.decode(returnUrl, "UTF-8");
			  session.setAttribute("returnUrl", returnUrl);
		  } catch (UnsupportedEncodingException e) {
			  e.printStackTrace();
		  }
	  }
	  
//	  System.out.println(returnUrl);
      
      return mav;
 //   /WEB-INF/views/tiles1/login/login.jsp 파일을 생성한다.
     }
	
	
	
	/**
	 * 로그인할 유저가 존재하는지 검사
	 * @param 유저가 입력한 아이디, 유저가 입력한 비밀번호
	 * @return 유저가 존재하면 true, 유저가 존재하지않는다면 false를 반환한다.
	 */
	@ResponseBody
	@RequestMapping(value="/userExist.do", method= {RequestMethod.POST})
	public String loginCheck(HttpServletRequest request) {
		String userid = request.getParameter("userid");
		String passwd = request.getParameter("passwd");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("userid", userid);
		paraMap.put("passwd", passwd);
		
		boolean userExist = service.userExistCheck(paraMap);
			
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("userExist", userExist);
		
		return jsonObj.toString();
	}
	
	
	
	/**
	 * 휴면 풀어주기
	 */
	@ResponseBody
	@RequestMapping(value="/login/restRelease.do", method= {RequestMethod.POST})
	public boolean restRelease(HttpServletRequest request, @RequestParam String userid) {
		return dao.restRelease(userid);
	}
	
	
	
	
	/**
	 * 로그인되어질 회원의 상태 체크하기(정지,휴면,대기,비밀번호 변경시점 3개월)
	 * @param 유저가 입력한 아이디
	 * @return 활동중이라면 "활동" 정지회원이라면 "정지" 휴면회원이라면 "휴면" 승인대기라면 "대기"
	 * 		      비밀번호 변경시점 3개월 이상이라면 "비밀번호 변경 권장"
	 */
	@ResponseBody
	@RequestMapping(value="/statusCheck.do",method= {RequestMethod.POST},produces="text/plain;charset=UTF-8")
	public String status_check(HttpServletRequest request) {
		String userid = request.getParameter("userid");
		
		String status = service.statusCheck(userid);
		Map<String,String> penaltyReason = new HashMap<>();
		String detailReason = "";
		String refuseReason = "";
		HttpSession session = request.getSession();
		if("비밀번호 변경 권장".equals(status)) {
			session.setAttribute("changePwd", true);
		} else if("정지".equals(status)){	 					//정지상태라면
			penaltyReason = dao.getPenaltyReason(userid);	//사유 불러오기
			if("기타사유".equals(penaltyReason.get("SIMPLE_PENALTY_REASON"))) {	//사유가 기타사유 일경우
				detailReason = dao.getDetailReason(userid);	//상세사유 불러오기
				if(detailReason == null) {
					detailReason = "사유 없음";
				}
			}
		} else if("승인거부".equals(status)){	 					//승인거부상태라면
			refuseReason = dao.getRefuseReason(userid);			//사유불러오기
			session.setAttribute("changePwd", false);
		}else {
			session.setAttribute("changePwd", false);
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("refuseReason", refuseReason);
		jsonObj.put("status", status);
		jsonObj.put("penaltyReason", penaltyReason);
		jsonObj.put("detailReason", detailReason);
		
		return jsonObj.toString();
	}
	
	
	
	
	
	/**
	 * 로그인 완료처리하기(로그인기록테이블 insert해주기,MemberVO객체 세션에 저장)
	 * @param 유저아이디, 비밀번호
	 * @return 이전페이지로 이동(추후 구현예정),(추후구현예정) 현재는 index 페이지로 이동,
	 */
	@RequestMapping(value="/login.do",method= {RequestMethod.POST})
	public String login_complete(HttpServletRequest request) {
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("userid",request.getParameter("userid"));
		paraMap.put("client_ip",request.getRemoteAddr());
		
		MemberVO user = service.login_complete(paraMap);
		
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		
		String returnUrl = (String) session.getAttribute("returnUrl");
		boolean changePwd = (boolean)session.getAttribute("changePwd");
		session.removeAttribute("returnUrl");
		session.removeAttribute("changePwd");
		if(returnUrl != null && !changePwd) {
			String loc = request.getContextPath()+returnUrl;
			request.setAttribute("loc", loc);
			return "returnLogin";
		} else if(changePwd) {
			return "redirect:/member/myId.do";
		}
		else {
			return "redirect:/index.do";
		}
		
	}
	
	
	
	/**
	 * 로그아웃 처리하기
	 */
	@RequestMapping(value="/logout.do")	//GET이나 POST 둘다 처리하기
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String returnUrl = request.getParameter("returnUrl");
		session.invalidate();
		if(returnUrl != null && !returnUrl.trim().isEmpty()) {
			try {
				returnUrl = URLDecoder.decode(returnUrl, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String loc = request.getContextPath()+returnUrl;
			request.setAttribute("loc", loc);
			return "returnLogin";
		} else {
			return "redirect:/index.do";
		}
	}
	
	
	
	
	
	
	
	
	
	
}
