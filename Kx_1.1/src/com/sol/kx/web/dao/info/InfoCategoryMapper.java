package com.sol.kx.web.dao.info;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;
import org.sol.util.mybatis.BaseMapper;
import org.sol.util.mybatis.SelectTemplate;

import com.sol.kx.web.dao.pojo.InfoCategory;

public interface InfoCategoryMapper extends BaseMapper<InfoCategory>{
	
	@SelectProvider(type = SelectTemplate.class,method = "selectByPage")
	public List<InfoCategory> selectByPage(InfoCategory obj);
	
	/**
	 * 获取分类列表
	 * 添加分类时使用
	 * @param clevel
	 * @return
	 * @throws Exception
	 */
	public List<InfoCategory> findCategoryTypeByNew(InfoCategory obj);
	
	/**
	 * 获取分类列表
	 * 查询参数
	 * @param clevel
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	public List<InfoCategory> findCategoryTypeByQuery(InfoCategory obj);
	
	/**
	 * 生成产品名称
	 * 用于产生新产品时,对应图片名称
	 * @param parent
	 * @param code
	 * @return
	 */
	public String generatePname(InfoCategory obj);
	
	/**
	 * 增加产品时校验是否有重复
	 * @param code
	 * @param level
	 * @param parent
	 * @return
	 */
	public int checkExists(InfoCategory obj);
	
	/**
	 * 删除产品时的校验
	 * 是否有其他下级产品
	 * @param id
	 * @return
	 */
	public int checkDeleteExists(InfoCategory obj);
}
