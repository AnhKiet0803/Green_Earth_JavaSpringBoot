package com.example.demo.service;

import com.example.demo.dto.req.CelebrityReq;
import com.example.demo.dto.res.CelebrityRes;
import com.example.demo.entity.Celebrity;
import com.example.demo.repository.CelebrityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CelebrityService {
    private final CelebrityRepository celebrityRepository;

    public List<CelebrityRes> getAllCelebrities() {
        return celebrityRepository.findAll().stream().map(CelebrityRes::toJson).toList();
    }

    public CelebrityRes findById(Long id) {
        return CelebrityRes.toJson(celebrityRepository.findById(id).get());
    }

    public CelebrityRes create(CelebrityReq req) {
        try {
            Celebrity celebrity = new Celebrity();
            celebrity.setName(req.getName());
            celebrity.setDescription(req.getDescription());
            celebrity.setImage(req.getImage());
            celebrity.setSocialLink(req.getSocialLink());
            return CelebrityRes.toJson(celebrityRepository.save(celebrity));
        }catch (Exception e){
            return null;
        }
    }

    public CelebrityRes update(Long id, CelebrityReq req) {
        try {
            Celebrity celebrity = celebrityRepository.findById(id).get();
            celebrity.setName(req.getName());
            celebrity.setDescription(req.getDescription());
            celebrity.setImage(req.getImage());
            celebrity.setSocialLink(req.getSocialLink());
            return CelebrityRes.toJson(celebrityRepository.save(celebrity));
        }catch (Exception e){
            return null;
        }
    }

    public void delete(Long id) {
        celebrityRepository.deleteById(id);
    }
}