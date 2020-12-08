package com.beans.roaststars.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.beans.roaststars.model.service.CafeService;
import com.beans.roaststars.model.vo.CafeOperatingTimeVO;
import com.beans.roaststars.model.vo.CafeVO;
import com.beans.roaststars.model.vo.UserVO;
@Controller
public class ManagerController {
	@Resource
	private CafeService cafeService;
	private String uploadPath; // 업로드 경로

	// 카페등록폼으로 이동
	@Secured("ROLE_MANAGER")
	@RequestMapping("register-cafeform.do")
	public String registerCafeForm() {
		return "cafe/registerCafeForm.tiles";
	}

	// 카페등록하기
	@Transactional
	@Secured("ROLE_MANAGER")
	@PostMapping("register-cafe.do")
	public ModelAndView registerCafe(CafeVO cafeVO, CafeOperatingTimeVO cafeOperVO,MultipartHttpServletRequest request) {
		// 로그인한 유저정보 불러오기
		UserVO uvo = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		cafeVO.setUserVO(uvo);
		cafeOperVO.setCafeVO(cafeVO);
		// System.out.println("회원가입 시 패스워드 확인:"+vo.getPassword()+"----"+vo.getPassword().length());
				//이미지 파일 업로드용
				//System.out.println(cafeVO.getUploadFile());
				
				if (cafeVO.getUploadFile() != null) {
					uploadPath = request.getSession().getServletContext().getRealPath("/resources/upload/");
					File uploadDir = new File(uploadPath);
					if (uploadDir.exists() == false)
						uploadDir.mkdirs();
					MultipartFile file = cafeVO.getUploadFile();
					if (file != null && file.isEmpty() == false) {
						File uploadFile = new File(uploadPath + file.getOriginalFilename());
						try {
							file.transferTo(uploadFile);
							// System.out.println(uploadPath + file.getOriginalFilename());
							cafeVO.setCafePic((file.getOriginalFilename()));
							String localPath = "C:\\kosta203\\Final-project\\FinalPj_RoastStars\\roaststars\\src\\main\\webapp\\resources\\upload";
							File localPathDir = new File(localPath);
							if (localPathDir.exists() == false)
								localPathDir.mkdirs();
							FileCopyUtils.copy(file.getBytes(),
									new File(localPath + File.separator + file.getOriginalFilename()));
						} catch (IllegalStateException | IOException e) {
							e.printStackTrace();
						}
					}
				}
				 
		// 카페정보 등록
		cafeService.registerCafe(cafeVO, cafeOperVO);
		// cafeNo 보내주기
		cafeVO = cafeService.findcafeByNoNotJoin(cafeVO.getCafeNo());
		cafeOperVO.setCafeVO(cafeVO);
		cafeOperVO = cafeService.findCafeByCafeNo(cafeVO.getCafeNo());
		return new ModelAndView("cafe/registerCafeResult.tiles", "cafeOperVO", cafeOperVO);
	}

	// 카페정보수정폼으로 이동하기 전에 자신의 카페 리스트 불러오기
	@Secured("ROLE_MANAGER")
	@RequestMapping("update-cafelist.do")
	public String updateCafeList(Model model) {
		UserVO userVO = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<CafeVO> list = cafeService.getCafeList(userVO.getId());
		model.addAttribute("list", list);
		return "cafe/updateCafeList.tiles";
	}
	//카페정보 수정전 자신의 카페목록 불러오기.
	@Secured("ROLE_MANAGER")
	@RequestMapping("update-cafeform.do")
	public ModelAndView updateCafeForm(String cafeNo, Model model) {
		model.addAttribute(cafeNo);
		CafeVO cafeVO = new CafeVO();
		cafeVO = cafeService.findcafeByNoNotJoin(cafeNo);
		return new ModelAndView("cafe/updateCafeForm.tiles", "cafeVO", cafeVO);
	}

	// 카페정보수정하기
	@Transactional
	@Secured("ROLE_MANAGER")
	@PostMapping("update-cafe.do")
	public ModelAndView updateCafe(CafeVO cafeVO, CafeOperatingTimeVO cafeOperVO,MultipartHttpServletRequest request) { // 로그인한 유저정보 불러오기
		UserVO uvo = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		cafeVO.setUserVO(uvo);
		cafeOperVO.setCafeVO(cafeVO);
		if (cafeVO.getUploadFile() != null) {
			uploadPath = request.getSession().getServletContext().getRealPath("/resources/upload/");
			File uploadDir = new File(uploadPath);
			if (uploadDir.exists() == false)
				uploadDir.mkdirs();
			MultipartFile file = cafeVO.getUploadFile();
			if (file != null && file.isEmpty() == false) {
				File uploadFile = new File(uploadPath + file.getOriginalFilename());
				try {
					file.transferTo(uploadFile);
					// System.out.println(uploadPath + file.getOriginalFilename());
					cafeVO.setCafePic((file.getOriginalFilename()));
					String localPath = "C:\\kosta203\\Final-project\\FinalPj_RoastStars\\roaststars\\src\\main\\webapp\\resources\\upload";
					File localPathDir = new File(localPath);
					if (localPathDir.exists() == false)
						localPathDir.mkdirs();
					FileCopyUtils.copy(file.getBytes(),
							new File(localPath + File.separator + file.getOriginalFilename()));
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
			}
		}
		cafeService.updateCafe(cafeVO, cafeOperVO);
		cafeOperVO = cafeService.findCafeByCafeNo(cafeVO.getCafeNo());
		return new ModelAndView("cafe/updateCafeResult.tiles", "cafeOperVO", cafeOperVO);
	}
	
	@Secured({"ROLE_MANAGER","ROLE_ADMIN"})
	@PostMapping("deleteCafe-Ajax.do")
	@ResponseBody
	public String deleteCafe(String cafeNo) {
		return cafeService.deleteCafe(cafeNo);
	}
}
