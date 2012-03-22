package com.sol.kx.web.dao.compare;

import java.util.List;

import org.sol.util.mybatis.BaseMapper;

import com.sol.kx.web.dao.pojo.CompareExcelProperty;

public interface CompareExcelPropertyMapper extends BaseMapper<CompareExcelProperty>{
	public List<CompareExcelProperty> loadProperty(String category);
}
