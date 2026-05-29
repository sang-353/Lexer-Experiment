package com.lexer.controller;

import com.lexer.service.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/keywords")
public class KeywordController {

    // 提供关键字管理相关的 HTTP 接口：列出、添加、更新、删除关键字
    @Autowired
    private KeywordService keywordService;

    @GetMapping
    public List<String> list() {
        // 返回当前关键字列表的快照（排序/顺序由内部 LinkedHashSet 保持）
        return keywordService.listAll();
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody Map<String, String> body) {
        boolean added = keywordService.add(body.get("keyword"));
        if (added) {
            return ResponseEntity.ok("添加成功");
        } else {
            return ResponseEntity.status(409).body("关键字已存在");
        }
    }

    @PutMapping
    public void update(@RequestBody Map<String, String> body) {
        // 调用服务层 update（先移除后添加），可用于修改关键字拼写或大小写
        keywordService.update(body.get("oldKeyword"), body.get("newKeyword"));
    }

    @DeleteMapping
    public void remove(@RequestBody Map<String, String> body) {
        // 从请求体读取 keyword 并移除，若关键字不存在则无副作用
        keywordService.remove(body.get("keyword"));
    }
}
