package com.ihelin.car.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ihelin.car.db.entity.ServiceMenu;
import com.ihelin.car.utils.ResponseUtil;

@Controller
public class AdminMenuController extends AdminBaseController {

	@RequestMapping("menu_admin")
	public String menuAdmin(Model model) {
		List<ServiceMenu> menus = serviceMenuMannger.getAllMenus();
		model.addAttribute("menus", menus);
		return "admin/menu_admin";
	}

	@RequestMapping(value = "service_menu_sync")
	public String syncMenu() {
		try {
			serviceMenuMannger.syncServiceMenuToWeiXin();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/admin/menu_admin";
	}

	@RequestMapping(value = "/service_menu_update")
	public String updateServiceMenu(String menuName, String content, String url, Integer articleId, Integer menuType,
			Integer parentId, Integer sort) {
		ServiceMenu menu = new ServiceMenu();
		menu.setName(menuName);
		if (menuType == ServiceMenu.TEXT_MENU) {
			menu.setContent(content);
		} else if (menuType == ServiceMenu.LINK_MENU) {
			if (url != null && !url.startsWith("http")) {
				url = "http://" + url;
			}
			menu.setContent(url);
		} else if (menuType == ServiceMenu.PIC_MENU) {
			menu.setContent(String.valueOf(articleId));
		}
		menu.setContentType(menuType);
		menu.setParentId(parentId);
		if (sort == null)
			sort = 100;
		menu.setSort(sort);
		serviceMenuMannger.insertMenu(menu);
		return "redirect:/admin/menu_admin";
	}

	@RequestMapping(value = "delete_menu")
	public void deleteMenu(Integer id, HttpServletResponse response) {
		ServiceMenu menu = serviceMenuMannger.getMenuById(id);
		if (menu.getParentId() == null) {
			List<ServiceMenu> subMenus = serviceMenuMannger.getMenusByParentId(id);
			for (ServiceMenu serviceMenu : subMenus) {
				serviceMenuMannger.deleteMenu(serviceMenu.getId());
			}
		}
		serviceMenuMannger.deleteMenu(id);
		ResponseUtil.writeSuccessJSON(response);
	}

}