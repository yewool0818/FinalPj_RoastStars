package com.beans.roaststars.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beans.roaststars.model.service.AdminService;
import com.beans.roaststars.model.vo.AdminListVO;

@Controller
public class AdminController {

	@Resource
	private AdminService adminService;

	// 성호 : 페이지이동(adminDetailForm.jsp)

	@RequestMapping("admin-detail-form.do")
	public String adminDetailForm(String authority, String pageNo, Model model) {
		//등급대기 중인 회원 리스트
		model.addAttribute("list", adminService.getAllWaitingForUpgradeUserList());
		//권한 종류
		model.addAttribute("uplist", adminService.getUserAuthorityList());
		//등급대기 총인원
		model.addAttribute("waitingMemberTotalCount", adminService.getTotalCountByWaitingMember());
		//회원 정보 넘기기
		AdminListVO lvo=adminService.findMemberByAuthority(pageNo);
		model.addAttribute("lvo",lvo);
		return "admin/adminDetailForm.tiles";

	}

	// 성호 : 페이지이동(beanspickDetailForm.jsp)
	@RequestMapping("beanspick-detail-form.do")
	public String beanspickDetailForm() {
		return "admin/beanspickDetailForm.tiles";
	}

}
