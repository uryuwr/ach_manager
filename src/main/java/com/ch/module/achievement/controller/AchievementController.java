package com.ch.module.achievement.controller;

import com.alibaba.fastjson.JSONObject;
import com.ch.common.config.MyException;
import com.ch.common.config.MyWebSocket;
import com.ch.common.entity.Items;
import com.ch.common.entity.PageCondition;
import com.ch.common.entity.Select;
import com.ch.module.achievement.domain.AchDomain;
import com.ch.module.achievement.domain.AchInfo;
import com.ch.module.achievement.domain.AchLevel;
import com.ch.module.achievement.domain.Achievement;
import com.ch.module.achievement.dto.AchievementDto;
import com.ch.module.achievement.repository.AchDomainRepository;
import com.ch.module.achievement.repository.AchLevelRepository;
import com.ch.module.achievement.repository.AchievementRepository;
import com.ch.module.achievement.service.AchDomainService;
import com.ch.module.achievement.service.AchInfoService;
import com.ch.module.achievement.service.AchLevelService;
import com.ch.module.achievement.service.AchievementService;
import com.ch.module.user.domain.Registry;
import com.ch.module.user.service.RegistryService;
import com.ch.utils.FileUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen on 2019-05-11-11:17
 */
@RestController
@RequestMapping("/v0.1/achievement")
public class AchievementController {
    @Autowired
    private AchievementService achievementService;

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private RegistryService registryService;

    @Autowired
    private AchDomainService achDomainService;

    @Autowired
    private AchLevelService achLevelService;

    @Autowired
    private AchInfoService achInfoService;

    @Autowired
    private AchDomainRepository achDomainRepository;

    @Autowired
    private AchLevelRepository achLevelRepository;

    @GetMapping("/domains")
    public List<AchDomain> getDomains() {
        return achDomainService.findAll();
    }

    @GetMapping("/domains/{id}")
    public String getDomainById(@PathVariable Integer id) {
        return JSONObject.toJSONString(achDomainService.findStrictOne(id));
    }

    @PutMapping("/domains")
    public void saveDomain(@RequestBody AchDomain achDomain) {
        achDomainRepository.save(achDomain);
    }

    @PutMapping("/levels")
    public void saveLevel(@RequestBody AchLevel achLevel) {
        achLevelRepository.save(achLevel);
    }

    @GetMapping("/levels")
    public List<AchLevel> getLevels() {
        return achLevelService.findAll();
    }

    @GetMapping("/levels/{id}")
    public String getLevelById(@PathVariable Integer id) {
        return JSONObject.toJSONString(achLevelService.findStrictOne(id));
    }

    @PostMapping
    public void commitAch(@RequestBody Achievement achievement, HttpServletRequest request) throws IOException {
        if (StringUtils.isEmpty(achievement.getTitle())) {
            throw new MyException("400","标题不能为空！");
        }

        if (StringUtils.isEmpty(achievement.getAuthor())) {
            throw new MyException("400","作者不能为空！");
        }

        Registry userInfo = registryService.getSessionUserInfo(request);
        achievement.setAuditState(0);
        achievement.setCreateUser(userInfo.getUserName());
        achievement.setAuditUser("无");
        achievementService.add(achievement);
        MyWebSocket.sendInfo("您有新的审核消息！", "admin", "系统管理员");
    }

    @PostMapping("/upload")
    public void uploadFile(@RequestParam( value="files",required=false)MultipartFile file) throws IOException {
        if (file != null) {
            String fileName = file.getOriginalFilename();
            String path =  ResourceUtils.getURL("classpath:static/upload").getPath();
            File upload = new File(path + "\\" + fileName);
            if (upload.exists()) {
                throw new MyException("400","文件重名！");
            }
            file.transferTo(upload);
        }
    }

    @GetMapping("download/{id}")
    public void downloadAch(@PathVariable Integer id, HttpServletResponse response) throws FileNotFoundException, UnsupportedEncodingException {
        Achievement achievement = achievementService.findStrictOne(id);
        String attachment = achievement.getAttachment();

        if (StringUtils.isEmpty(achievement)) {
            throw new MyException("400","没有可下载的附件！");
        }

        FileUtils.dowanload(attachment,response);
    }

    @PutMapping("/{id}/action/audit")
    public void audit(@PathVariable Integer id) {
        Achievement achievement = achievementService.findStrictOne(id);
        achievement.setAuditState(1);
        achievementRepository.save(achievement);
        if (!achInfoService.hadAch(achievement.getId())) {
            AchInfo achInfo = new AchInfo();
            achInfo.setAchId(achievement.getId());
            achInfo.setAchType(achievement.getAchType());
            achInfo.setAuthors(achievement.getAuthor());
            achInfo.setTitle(achievement.getTitle());
            achInfo.setPoint(achLevelService.getPoint(achievement.getLevelId(), achievement.getProportion()));
            achInfo.setCreateUser(achievement.getCreateUser());
            achInfoService.add(achInfo);
        }
    }

    @GetMapping("/infos")
    public Items<AchievementDto> getAchInfoPage( @RequestParam(required = false) Integer offset,
                                          @RequestParam(required = false) Integer limit,
                                          @RequestParam(required = false) Boolean count,
                                          @RequestParam(required = false) String selects) {
        Select select = new Select();
        if (selects != null) {
            select = Select.getSelect(selects,false);
        }
        PageCondition pageCondition = new PageCondition(offset, limit, count, select);
        AchInfo ach = new AchInfo();
        Items<AchInfo> items = achInfoService.getAchInfos(pageCondition, ach);
        List<AchInfo> achInfoList = items.getList();
        List<AchievementDto> ret = new ArrayList<>();
        for (AchInfo achInfo : achInfoList) {
            Integer achId =  achInfo.getAchId();
            Achievement achievement = achievementService.findStrictOne(achId);
            AchievementDto achievementDto = new AchievementDto();
            achievementDto.setId(achId);
            achievementDto.setAchTypeName(achievementService.getAchType(achInfo.getAchType()));
            achievementDto.setTitle(achInfo.getTitle());
            achievementDto.setPoint(achInfo.getPoint());
            achievementDto.setAuthor(achInfo.getAuthors());
            achievementDto.setCreateTime(achInfo.getCreateTime());
            achievementDto.setCreateUserName(registryService.findByUserName(achInfo.getCreateUser()).getRealName());
            achievementDto.setDomainTypeName(achDomainService.getDomainName(achievement.getDomainType()));
            achievementDto.setLevelName(achLevelService.getlevelName(achievement.getLevelId()));
            ret.add(achievementDto);
        }
        return Items.of(ret,items.getCount());
    }

    @GetMapping("/page")
    public Items<AchievementDto> getPage(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Boolean count,
            @RequestParam(required = false) String selects,
            @RequestParam(required = false) boolean state) throws InvocationTargetException, IllegalAccessException {
        Select select = new Select();
        if (selects != null) {
            select = Select.getSelect(selects,state);
        }
        PageCondition pageCondition = new PageCondition(offset, limit, count, select);
        Achievement ach = new Achievement();
        Items<Achievement> items = achievementService.getAchievements(pageCondition, ach);
        List<Achievement> achievementList = items.getList();
        List<AchievementDto> ret = new ArrayList<>();
        for (Achievement achievement : achievementList) {
            AchievementDto achievementDto = new AchievementDto();
            BeanUtils.copyProperties(achievementDto, achievement);
            achievementDto.setAchTypeName(achievementService.getAchType(achievement.getAchType()));
            achievementDto.setDomainTypeName(achDomainService.getDomainName(achievement.getDomainType()));
            achievementDto.setAuditStateName(getAuditStateName(achievement.getAuditState()));
            achievementDto.setLevelName(achLevelService.getlevelName(achievement.getLevelId()));
            ret.add(achievementDto);
        }
        return Items.of(ret, items.getCount());
    }

    @GetMapping("/{id}")
    public AchievementDto getAchById(@PathVariable Integer id) throws InvocationTargetException, IllegalAccessException {
        Achievement achievement = achievementService.findStrictOne(id);
        AchievementDto achievementDto = new AchievementDto();
        BeanUtils.copyProperties(achievementDto, achievement);
        achievementDto.setAchTypeName(achievementService.getAchType(achievement.getAchType()));
        achievementDto.setDomainTypeName(achDomainService.getDomainName(achievement.getDomainType()));
        achievementDto.setAuditStateName(getAuditStateName(achievement.getAuditState()));
        achievementDto.setLevelName(achLevelService.getlevelName(achievement.getLevelId()));
        achievementDto.setCreateUserName(registryService.findByUserName(achievement.getCreateUser()).getRealName());
        return achievementDto;
    }

    private String getAuditStateName(Integer auditState) {
        switch (auditState) {
            case 0:
                return "未审核";
            case 1:
                return "已通过";
        }
        return "未审核";
    }

}
