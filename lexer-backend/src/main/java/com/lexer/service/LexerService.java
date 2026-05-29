package com.lexer.service;

import com.lexer.dfa.DFA;
import com.lexer.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LexerService {

    // 词法分析服务的外层封装：负责从控制器接收原始源代码字符串，
    // 使用 DFA（手写词法器）并返回 Token 列表。
    // 该类将关键字列表委托给 KeywordService 提供（以便 DFA 能够识别关键字）

    @Autowired
    private KeywordService keywordService;

    public List<Token> analyze(String sourceCode) {
        // 将当前关键字集传入 DFA，构建并触发词法分析后返回 Token 列表
        DFA dfa = new DFA(sourceCode, keywordService.getKeywords());
        return dfa.tokenize();
    }
}
