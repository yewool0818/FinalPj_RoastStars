package com.beans.roaststars.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.beans.roaststars.model.service.PagingBean;
import com.beans.roaststars.model.vo.BeansPickVO;

@Mapper
public interface BeansPickMapper {

	BeansPickVO getOneBeansPick(String beansNo);

	void registerBeansPick(BeansPickVO beansPickVO);
	
	void deleteBeansPick(String	beansNo);

	void updateBeansPick(BeansPickVO beansPickVO);
	
	int getTotalCountBeansPick();

	List<BeansPickVO> getAllBeansPickList(PagingBean pagingBean);
	
}
