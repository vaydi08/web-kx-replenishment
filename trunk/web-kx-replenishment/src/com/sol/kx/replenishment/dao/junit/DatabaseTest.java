package com.sol.kx.replenishment.dao.junit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sol.kx.replenishment.dao.InfoCategoryDao;
import com.sol.kx.replenishment.dao.InfoManufactoryDao;
import com.sol.kx.replenishment.dao.InfoProductDao;
import com.sol.kx.replenishment.dao.InfoTesseraDao;
import com.sol.kx.replenishment.dao.pojo.InfoManufactory;
import com.sol.kx.replenishment.dao.pojo.InfoProduct;
import com.sol.kx.replenishment.dao.pojo.InfoTessera;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class DatabaseTest {
	@Autowired
	private InfoProductDao dao;
	
	@Autowired
	private InfoTesseraDao tesseraDao;
	
	@Autowired
	private InfoManufactoryDao manufactoryDao;
	
//	@Test
//	public void test() {
//		List<InfoProduct> list = dao.find();
//		
//		List<InfoTessera> tlist = tesseraDao.findJoinProduct();
//		Map<String,List<InfoTessera>> tesseraMap = tesseraMap(tlist);
//		joinProduct(list,tesseraMap);
//		
//		for(InfoProduct po : list) {
//			System.out.println(po.getName());
//			for(InfoTessera s : po.getInfoTesseras())
//				System.out.println(s.getName());
//			for(InfoManufactory m : po.getInfoManufactories())
//				System.out.println(m.getName());
//			System.out.println();
//		}
//	}
//	
//	private Map<String,List<InfoTessera>> tesseraMap(List<InfoTessera> list) {
//		Map<String,List<InfoTessera>> tesseraMap = new HashMap<String, List<InfoTessera>>();
//		
//		for(InfoTessera t : list) {
////			List<InfoTessera> innerList = tesseraMap.get(t.getPname());
////			if(innerList != null)
////				innerList.add(t);
////			else {
////				innerList = new ArrayList<InfoTessera>();
////				innerList.add(t);
////				tesseraMap.put(t.getPname(), innerList);
//			}
//		}
//		
//		return tesseraMap;
//	}
//	private void joinProduct(List<InfoProduct> list,Map<String,List<InfoTessera>> tesseraMap) {
//		for(InfoProduct p : list) 
//			p.setInfoTesseras(tesseraMap.get(p.getName()));
//	}
	
	@Test
	public void test2() {
		DetachedCriteria criteria = DetachedCriteria.forClass(InfoTessera.class);
		DetachedCriteria pc = criteria.createCriteria("infoProduct");
		pc.add(Restrictions.like("name", "名称", MatchMode.ANYWHERE));
		List<InfoTessera> list = tesseraDao.findByCriteria(criteria);
		for(InfoTessera po : list)
			System.out.println(po.getName() + " " + po.getInfoProduct().getName());
	}
	
//	@Test
	public void save() {
//		InfoProduct po = new InfoProduct(0, 1, 2, 3, 4, "名称", "code", "quality", "image", 1.10, "stand", null, null);
//		dao.savePojo(po);
		InfoTessera po = new InfoTessera(0, 1, "镶嵌1");
		InfoTessera po2 = new InfoTessera(0, 1, "镶嵌2");
		InfoTessera po3 = new InfoTessera(0, 1, "镶嵌3");
		tesseraDao.savePojo(po);
		tesseraDao.savePojo(po2);
		tesseraDao.savePojo(po3);
		
		InfoManufactory mo = new InfoManufactory(0, 1, "制造厂1");
		InfoManufactory mo2 = new InfoManufactory(0, 1, "制造厂2");
		InfoManufactory mo3 = new InfoManufactory(0, 1, "制造厂3");
		manufactoryDao.savePojo(mo);
		manufactoryDao.savePojo(mo2);
		manufactoryDao.savePojo(mo3);
	}
}
