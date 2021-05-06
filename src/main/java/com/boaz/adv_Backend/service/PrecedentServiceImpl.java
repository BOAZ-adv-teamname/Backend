package com.boaz.adv_Backend.service;

import com.boaz.adv_Backend.repository.PrecedentRepository;
import com.boaz.adv_Backend.vo.Precedent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PrecedentServiceImpl implements PrecedentService{
    @Autowired
    private PrecedentRepository precedentRepository;

    @Override
    public List<Precedent> getList() {
        return precedentRepository.findAll();
    }
}
