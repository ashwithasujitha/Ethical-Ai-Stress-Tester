package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Feedback;
import com.example.demo.Repository.FeedbackRepo;

@Service
public class FeedbackService {
        @Autowired
    
    FeedbackRepo feedrepo;
    public Feedback createfeed(Feedback u){
        return feedrepo.save(u);
    }

    public List<Feedback>getAllfeed(){
        return feedrepo.findAll();
    }

    public Optional<Feedback> getByIdfeed(Long id)
    {
        return feedrepo.findById(id);
    }

    public Feedback updatefeed(Long id,Feedback u)
    {
        u.setId(id);
        return feedrepo.save(u);
    }

    public String deleteByIdfeed(long id)
    {
        feedrepo.deleteById(id);
        return "Success";
    }
    public Page<Feedback> getFeedbacksByPage(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return feedrepo.findAll(pageable);
    }

    public List<Feedback> sortByFeedbackDate() {  
        return feedrepo.findAll(Sort.by(Sort.Direction.ASC, "orderDate"));
    }

    public List<Feedback> getByFeedbackId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }
        return feedrepo.findById(id).stream().toList();
    }


}
