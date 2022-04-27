package com.atcdi.digital.service;

import com.atcdi.digital.dao.DairyDao;
import com.atcdi.digital.entity.StandardException;
import com.atcdi.digital.entity.User;
import com.atcdi.digital.entity.daliy.Dairy;
import com.atcdi.digital.handler.SessionHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DairyService {
    @Resource
    DairyDao dairyDao;
    @Resource
    UserService userService;
    @Resource
    ObjectMapper mapper;
    @Resource
    SessionHandler session;

    public List<JsonNode> getUserSelfDairyList() {
        return getUserDairyListByUser(userService.getCurrentUser());
    }

    public List<JsonNode> getUserDairyListByUsername(int userId) {
        User user = userService.getUserById(userId);
        return getUserDairyListByUser(user);
    }

    private List<JsonNode> getUserDairyListByUser(User user) {
        List<JsonNode> res = new ArrayList<>();
        dairyDao.getDairyByUser(user.getUserId()).stream().collect(Collectors.groupingBy(Dairy::getDate)).forEach((date, dairy)->{
            ObjectNode node = mapper.createObjectNode();
            node.set("date",mapper.valueToTree(date));
            List<JsonNode> children = new ArrayList<>();
            dairy.forEach(d->{
                ObjectNode child = mapper.createObjectNode();
                child.put("dairyId", d.getDairyId());
                child.put("username", user.getNickname());
                child.put("workItem", d.getWorkItem());
                if (d.getWorkDesc() != null) child.put("workDesc", d.getWorkDesc());
                child.put("spendTime", d.getSpendTime());
                children.add(child);
            });
            node.set("records", mapper.valueToTree(children));
            res.add(node);
        });
        return res;
    }

    public boolean updateDairy(Dairy dairy) {
        if (!dairyDao.updateDairy(dairy) && !checkUser(dairy.getDairyId())) {
            throw new StandardException(500, "日志更新错误，日志ID：" + dairy.getDairyId());
        } else return true;
    }

    public boolean deleteDairy(int dairyId) {
        if (!dairyDao.deleteDairy(dairyId) && !checkUser(dairyId)) {
            throw new StandardException(500, "日志删除错误，日志ID：" + dairyId);
        } else return true;
    }

    public int createDairy(Dairy dairy){
        dairy.setUserId(userService.getCurrentUser().getUserId());
        if (dairyDao.insertDairy(dairy)){
            return dairy.getDairyId();
        }else{
            throw new StandardException(500, "日志创建错误，日志ID：" + dairy.getDairyId());
        }
    }

    boolean checkUser(int checkId){
        if (checkId != userService.getCurrentUser().getUserId() || session.hasRole("ROLE_ADMIN")){
            throw new StandardException(403, "只允许操作本人日志");
        }else{
            return true;
        }
    }

    public Set<JsonNode> getUserTree(){
        Set<User> userSet = userService.getUserList();
        Set<JsonNode> res = new HashSet<>();
        // TODO 权限控制filter
        userSet.stream().filter((u)-> u.getGroup() != User.DepartmentGroup.ADMIN).collect(Collectors.groupingBy(User::getGroup)).forEach((group, user)->{
            ObjectNode node = mapper.createObjectNode();
            node.put("group" , group.toString());
            node.set("children", mapper.valueToTree(user));
            res.add(node);
        });
        return res;
    }



}
