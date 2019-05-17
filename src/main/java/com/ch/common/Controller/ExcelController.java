package com.ch.common.Controller;

import com.ch.common.config.MyException;
import com.ch.module.achievement.dto.AchievementDto;
import com.ch.module.achievement.service.AchievementService;
import com.ch.module.user.domain.Registry;
import com.ch.utils.ExpPoiExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ch265357 2019-04-03 11:27
 */
@Slf4j
@RestController
@RequestMapping("/v0.1/excel")
public class ExcelController {
	@Autowired
	private AchievementService achievementService;

	@GetMapping
	public void export(HttpServletResponse response) throws UnsupportedEncodingException {
		response.setContentType("application/vnd.ms-excel; charset=utf-8");
		String fileName = "科研业绩报表";
		response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
		List<AchievementDto> excelDatas = achievementService.getExcelDatas();
		String title = "科研业绩报表";
		String pattern = "yyyy-MM-dd HH:mm:ss";
		ExpPoiExcel<AchievementDto> expPoiExcel = new ExpPoiExcel<>();
		try {
			String[] headers = { "类型","领域","标题","成果等级","所属用户","生成日期","业绩得分" };
			String[] fields = { "achTypeName", "domainTypeName", "title","levelName","createUserName","createTime","point"};
			expPoiExcel.excelExport(title, headers, fields, excelDatas, response, pattern);
		} catch (Exception e) {
			log.error("excel文件生成失败" + e);
			throw new MyException("400","error");
		}
	}
}
