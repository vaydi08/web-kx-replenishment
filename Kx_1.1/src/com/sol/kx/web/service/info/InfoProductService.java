package com.sol.kx.web.service.info;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.sol.util.mybatis.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.common.Logger;
import com.sol.kx.web.common.PoiUtil;
import com.sol.kx.web.dao.info.InfoProductMapper;
import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.bean.PagerBean;

@Service
public class InfoProductService extends BaseService{
	@Autowired
	private InfoProductMapper infoProductMapper;

	public void printPreview(PagerBean<InfoProduct> bean,InfoProduct obj) {
		if(Constants.DEBUG)
			Logger.SERVICE.debug("查询[info_product]数据,打印预览");
		
		bean.setDataList(infoProductMapper.selectExport(obj));
	}
	
	// 导出
	@Transactional(readOnly = true)
	public InputStream createExcel(InfoProduct obj) {
		if(Constants.DEBUG)
			Logger.SERVICE.debug("查询[info_product]数据,导出为Excel表格数据");
		
		PoiUtil poi = new PoiUtil("产品数据");
		
		// 获取数据库
		List<InfoProduct> list = null;
		try {
			list = infoProductMapper.selectExport(obj);
		} catch (Exception e) {
			Logger.SERVICE.error("查询[info_product]数据,导出为Excel表格数据错误",e);
			return null;
		}
		
		ExportStructure stru = new ExportStructure();
		stru.loadFromProperties();
		
		poi.newRow();
		poi.setValue(stru.pname - 1, stru.pname_title);
		poi.setValue(stru.pcode - 1, stru.pcode_title);
		poi.setValue(stru.image -1 , stru.image_title);
		
		while((--stru.startrow-1) > 0) {
			poi.newRow();
		}
		
		// 输出到EXCEL表
		for(InfoProduct po : list) {
			poi.newRow();
			poi.setValue(stru.pname-1, po.getPname());
			poi.setValue(stru.pcode-1, po.getPcode());
			poi.setValue(stru.image-1, po.getImage());
		}
		
		return poi.getExcel();
	}
	
	@Override
	protected BaseMapper injectMapper() {
		return infoProductMapper;
	}

	private class ExportStructure {
		private int startrow;
		private int pname;
		private String pname_title;
		private int pcode;
		private String pcode_title;
		private int image;
		private String image_title;
		
		// 从配置文件加载
		private void loadFromProperties() {
			InputStream is = this.getClass().getResourceAsStream("/MyBatisConf/InfoProductExport.properties");
			Properties prop = new Properties();
			try {
				prop.load(is);
			} catch (IOException e) {
				exceptionHandler.onExcelException(e.getMessage(), e);
			} finally {
				try {
					if(is != null)
						is.close();
				} catch (IOException e) {
				}
			}
			
			this.startrow = Integer.parseInt(prop.getProperty("product.startrow","1"));
			this.pname = Integer.parseInt(prop.getProperty("product.pname","1"));
			this.pcode = Integer.parseInt(prop.getProperty("product.pcode","2"));
			this.image = Integer.parseInt(prop.getProperty("product.image","29"));
			this.pname_title = prop.getProperty("product.pname.title");
			this.pcode_title = prop.getProperty("product.pcode.title");
			this.image_title = prop.getProperty("product.image.title");
		}
	}

}
