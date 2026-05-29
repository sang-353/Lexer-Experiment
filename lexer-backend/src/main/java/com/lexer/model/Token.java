package com.lexer.model;

import lombok.Data;

@Data
public class Token {
    // 表示词法分析产生的一个词法单元（Token）
    // - line: 该 token 在源代码中的行号（从 1 开始）
    // - lexeme: 词素文本（例如标识符名称、字符串字面量、运算符等）
    // - type: 词法类别描述（例如 "关键字"、"标识符"、"字符串"、"无符号整数"）
    private int line;
    private String lexeme;
    private String type;

    public Token() {}

    public Token(int line, String lexeme, String type) {
        this.line = line;
        this.lexeme = lexeme;
        this.type = type;
    }

//    public int getLine() { return line; }
//    public void setLine(int line) { this.line = line; }
//    public String getLexeme() { return lexeme; }
//    public void setLexeme(String lexeme) { this.lexeme = lexeme; }
//    public String getType() { return type; }
//    public void setType(String type) { this.type = type; }
}
