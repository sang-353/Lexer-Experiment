package com.lexer.controller;

import com.lexer.model.Token;
import com.lexer.service.LexerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lexer")
public class LexerController {

    @Autowired
    private LexerService lexerService;

    @PostMapping("/analyze")
    public List<Token> analyze(@RequestBody Map<String, String> body) {
        // 取 JSON body 中的 sourceCode 字段，若为空则返回空列表，避免传入 null
        String sourceCode = body.get("sourceCode");
        if (sourceCode == null || sourceCode.isEmpty()) {
            return List.of();
        }
        return lexerService.analyze(sourceCode);
    }
}
