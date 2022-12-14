package com.gukmo.board.hasol.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gukmo.board.hasol.service.InterBoardService;
import com.gukmo.board.model.AdVO;
import com.gukmo.board.model.BoardVO;
import com.gukmo.board.model.HashtagVO;

@Controller
public class BoardController {
	
	@Autowired
	private InterBoardService service;
	
	
	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping(value="/open_banner.do", method = {RequestMethod.GET},
	 * produces="text/plain;charset=UTF-8") public String
	 * open_banner(HttpServletRequest request, AdVO advo) {
	 * 
	 * List<AdVO> mainBannerList = service.getMainBannerList(advo);
	 * 
	 * JSONArray jsonArr = new JSONArray();
	 * 
	 * if(mainBannerList != null) { for(AdVO advo : mainBannerList) { JSONObject
	 * jsonobj = new JSONObject(); jsonobj.put("ad_num",
	 * advo.getAdvertisement_num()); jsonobj.put("url", advo.getUrl());
	 * jsonobj.put("period", advo.getPeriod());
	 * 
	 * } }
	 * 
	 * return mav; }
	 */
	
	@RequestMapping(value="/main_search.do")
	public ModelAndView main_search(Map<String,String> paraMap, ModelAndView mav, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		session.setAttribute("readCountPermission", "yes");	
		
		String searchWord = request.getParameter("searchWord");
		String hashtag = request.getParameter("hashtag");

		// System.out.println("searchWord="+searchWord);
		String str_currentPageNo = request.getParameter("currentPageNo");
				
				
		if(hashtag == null || "".equals(hashtag) || hashtag.trim().isEmpty()) {
			hashtag = "";
		}
		
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		// System.out.println("hashtag:" +hashtag);
		
		
		paraMap = new HashMap<>();
		paraMap.put("searchWord", searchWord);
		paraMap.put("hashtag", hashtag);

		// System.out.println("??? ???????????? ??????!!!!:" + searchWord);
		
		int totalCnt = 0; // ??? ????????? ??? ???
		int sizePerpage = 10; // ??? ????????? ??? ????????? ????????? ??? ???
		int currentPageNo = 0; // ?????? ?????? ?????? ????????? ??????
		int totalPage = 0; // ??? ????????? ???(== ????????? ???)
		
		int startRno = 0; // ?????? ??? ??????
		int endRno = 0; // ??? ??? ??????
		
		
		try {			
			// ??? ????????? ??? ???(totalCnt)
			totalCnt = service.getTotalCnt(paraMap);
			//System.out.println("totalCnt:" + totalCnt);
						
			
			// ??? ????????? ???(== ????????? ???)
			totalPage= (int)Math.ceil(((double)totalCnt/sizePerpage));		
			//System.out.println("totalPage:" + totalPage);
			
			// ?????? ?????? ?????? ????????? 1??? ?????????
			if(str_currentPageNo == null) { currentPageNo = 1; } // ????????????
			else {
				
				try {
					currentPageNo = Integer.parseInt(str_currentPageNo);
					if(currentPageNo < 1 || currentPageNo > totalPage) {
						currentPageNo = 1;
					}
				} catch (NumberFormatException e) {
					currentPageNo = 1;
				}
			}
			
			startRno = ((currentPageNo - 1 ) * sizePerpage ) + 1 ;
			endRno = startRno + sizePerpage - 1;
			
			paraMap.put("startRno", String.valueOf(startRno));
			paraMap.put("endRno", String.valueOf(endRno));

		
			
			List<BoardVO> searchList = service.getSearchList(paraMap);
			// System.out.println("searchList:" + searchList.toString() );		

			// ????????? ??? ?????????
			// ????????? ??? ?????? ???
			int blockSize = 10;
			
			// ????????? ??? ?????? loop ??????
			int loop = 1;
			
			// ????????? ??? ?????? ?????? ?????? ???
			int blockStart = ((currentPageNo - 1) / blockSize ) * blockSize + 1;
			
			String pageBar = "<ul class='pagination'>";
			String url = "main_search.do";
			
			// ????????? ?????? ??? ?????????
			if(blockStart != 1) {
				// ??????
				pageBar += "<li class='page-item'>" +
						   "	<a class='page-link' href='" +url+ "?searchWord=" +searchWord+ "&currentPageNo=1' aria-label='super_previous'>" +
						   "		<i class='fa-solid fa-angles-left'></i>"+
						   "	</a>" + 
						   "</li>";
				// ??????
				pageBar += "<li class='page-item'>" +
						   "	<a class='page-link' href='" +url+ "?searchWord=" +searchWord+ "&currentPageNo=" +(blockStart-1)+ "' aria-label='previous'>" +
						   "		<i class='fa-solid fa-angle-left'></i>"+
						   "	</a>" + 
						   "</li>";
			}
			
			while (!(loop>blockSize || blockStart > totalPage)) {
				
				if(blockStart == currentPageNo) {
					// ?????? ?????? ???????????? ?????? ?????? ???????????? ?????? ?????? (????????? ??????)
					pageBar += "<li class='page-item disabled'>" +
							   "	<a class='page-link'>" +blockStart+ "</a>"+
							   "</li>";
				}
				else {
					// ?????? ?????? ???????????? ?????? ?????? ???????????? ????????????(???????????? ?????? ??????)
					pageBar += "<li class='page-item'>" +
							   "	<a class='page-link' href='" +url+ "?searchWord=" +searchWord+ "&currentPageNo=" +blockStart+ "'>" +blockStart+ "</a>"+
							   "</li>";
				}
				
				loop++;
				blockStart++;
				
				// ??????
				// blockstart - 1 / current - 1 / [1] / loop-2 / blockstart-2
				// blockstart - 2 / current - 1 / [1] [2(??????)] / loop-3 / blockstart-3
				// blockstart - 3 / current - 1 / [1] [2(??????)] / loop-4 / blockstart-4
				// blockstart - 4 / current - 1 / [1] [2(??????)] / loop-5 / blockstart-5
				// blockstart - 5 / current - 1 / [1] [2(??????)] / loop-6 / blockstart-6
				// blockstart - 6 / current - 1 / [1] [2(??????)] / loop-7 / blockstart-7
				// blockstart - 7 / current - 1 / [1] [2(??????)] / loop-8 / blockstart-8
				// blockstart - 8 / current - 1 / [1] [2(??????)] / loop-9 / blockstart-9
				// blockstart - 9 / current - 1 / [1] [2(??????)] / loop-10 / blockstart-10
				// ?????? ????????? ??????
			
				// 11~
				// ?????? ?????? ??????
				// blockstart - 11 / current - 11 / [1] [2(??????)] / loop-2 / blockstart-12
				// blockstart - 12 / current - 12 / [1] [2(??????)] / loop-3 / blockstart-13
				// blockstart - 13 / current - 13 / [1] [2(??????)] / loop-4 / blockstart-14
				// blockstart - 14 / current - 14 / [1] [2(??????)] / loop-5 / blockstart-15
				
				// 14 
				// blockstart- 1 / current -3 / [1(??????)] / loop-2 / blockstart-2
				// blockStart - 2 / currnet -3 / [2(??????)] / loop-3 / blockstart-3
				
			}
			
			// ????????? ????????? ?????????		
			if(blockStart <= totalPage) {
				pageBar += "<li class='page-item'>" +
						   "	<a class='page-link' href='" +url+ "?searchWord=" +searchWord+ "&currentPageNo=" +blockStart+ "'aria-label='next'>" +
						   "		<i class='fa-solid fa-angle-right'></i>"+
						   "	</a>" + 
						   "</li>";
				
				pageBar += "<li class='page-item'>" +
						   "	<a class='page-link' href='" +url+ "?searchWord=" +searchWord+ "&currentPageNo=" +totalPage+ "' aria-label='super-next'>" +
						   "		<i class='fa-solid fa-angles-right'></i>"+
						   "	</a>" + 
						   "</li>";
			}

			pageBar +="</ul>";
	
			mav.addObject("pageBar", pageBar);
			mav.addObject("searchList", searchList);
			
			//System.out.println(searchList.toString());
			
		} catch (NullPointerException e) {
			String message = "????????? ?????? ????????? ????????????.";
			mav.addObject("message", message);
		}		
			
		// ????????? ?????????
		if(!"".equals(hashtag)) {
			searchWord = hashtag;
			paraMap.put("searchWord", searchWord);
		}
		if(!"".equals(searchWord)) { mav.addObject("paraMap", paraMap); }
		
		// ?????? ???????????? ????????? ???????????? ?????????
		List<HashtagVO> topHashList = service.getTopHashList();
		mav.addObject("topHashList", topHashList);
		System.out.println(topHashList);
		
		mav.addObject("totalCnt", totalCnt);
		mav.setViewName("board/main_search/searchPage.tiles1");
		
		return mav;
	}
	
	
}
