package com.boaz.adv_Backend.controller;

import com.boaz.adv_Backend.service.PrecedentService;
import com.boaz.adv_Backend.vo.Precedent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/precedent")
public class PrecedentController {

    @Autowired
    private PrecedentService precedentService;

    @RequestMapping("/precedents")
    public List getMembers() {
        List<Precedent> list = precedentService.getList();
        return list;
    }
}
