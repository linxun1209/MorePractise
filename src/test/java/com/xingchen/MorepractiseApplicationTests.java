package com.xingchen;


import com.xingchen.dao.NoteMapper;
import com.xingchen.dao.TopicMapper;


import com.xingchen.service.*;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class MorepractiseApplicationTests {

    @Autowired
    private TopicMapper topicDao;
    @Autowired
    private ITopicService iTopicService;
    @Autowired
    private INoticeService noticeService;
    @Autowired
    private IProfessionalService iProfessionalService;
    @Autowired
    private NoteMapper noteMapper;
    @Autowired
    private IConclusionService iConclusionService;
    @Autowired
    private IAnswerService iAnswerService;




    @Test
    void TestInsert() {
        iTopicService.removeById(1);
    }

    @Test
    void TestInsert1() {
        List<List<Integer>> res=new ArrayList<>();
        res.get(1).add(1,1);

    }

    @RestController
    public class TestController {
        @GetMapping("/test")
        public String test(){
            return "SpringSecurity 基本配置成功!";
        }
    }












}
