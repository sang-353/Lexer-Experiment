package com.lexer.dfa;

import com.lexer.model.Token;
import java.util.*;

/*
     简单的手写有限状态自动机 (DFA) 实现，用于将输入源代码分解成词法单元 (Token)
     设计要点：逐字符扫描输入，维护当前位置 `pos` 与当前行号 `line`，识别以下几类项：
      - 注释: 用 { ... } 包围，跳过内容并维护行号
      - 字符串: 以单引号 ' 开始和结束，按字面读取（未处理转义）
      - 标识符/关键字: 以字母或下划线开头，随后允许字母数字和下划线，关键字集合由外部提供
      - 无符号整数: 连续数字序列
      - 分界符/运算符: 包含单字符和双字符形式，如 ":="、".."
     返回的 Token 包含行号、词素和类型（例如 "关键字"、"标识符"、"字符串" 等）
     */
public class DFA {

    private final String source;
    private final Set<String> keywords;
    private int pos;
    private int line;

    public DFA(String source, Set<String> keywords) {
        this.source = source;
        this.keywords = keywords;
        this.pos = 0;
        this.line = 1;
    }

    public List<Token> tokenize() {
        // 主驱动方法：从头遍历 source，识别并收集 Token
        List<Token> tokens = new ArrayList<>();
        while (pos < source.length()) {
            char c = source.charAt(pos);

            if (c == '\n') {
                line++;
                pos++;
                continue;
            }
            if (Character.isWhitespace(c)) {
                pos++;
                continue;
            }

            // 注释: { ... }
            if (c == '{') {
                skipComment();
                continue;
            }

            // 字符串: ' ... '
            if (c == '\'') {
                // 读取直到下一个单引号（含），并将整个带引号的内容作为一个字符串 Token
                tokens.add(readString());
                continue;
            }

            // 标识符或关键字: 字母开头
            if (Character.isLetter(c) || c == '_') {
                // 标识符或关键字识别：readIdOrKeyword 会根据关键字集合决定类型
                tokens.add(readIdOrKeyword());
                continue;
            }

            // 无符号整数: 数字开头
            if (Character.isDigit(c)) {
                // 连续数字序列识别为无符号整数
                tokens.add(readNumber());
                continue;
            }

            // 冒号: 可能是 := 或单个 :
            if (c == ':') {
                pos++;
                if (pos < source.length() && source.charAt(pos) == '=') {
                    pos++;
                    tokens.add(new Token(line, ":=", "双字符分界符"));
                } else {
                    tokens.add(new Token(line, ":", "单字符分界符"));
                }
                continue;
            }

            // 句点: 可能是 .. 或单个 .
            if (c == '.') {
                pos++;
                if (pos < source.length() && source.charAt(pos) == '.') {
                    pos++;
                    tokens.add(new Token(line, "..", "数组下标界限符"));
                } else {
                    tokens.add(new Token(line, ".", "程序结束符"));
                }
                continue;
            }

            // 单字符分界符
            if ("+-*/=<>()[];,".indexOf(c) >= 0) {
                tokens.add(new Token(line, String.valueOf(c), "单字符分界符"));
                pos++;
                continue;
            }

            // 非法字符，跳过
            pos++;
        }
        return tokens;
    }

    private void skipComment() {
        // 从 '{' 开始，跳过直到匹配的 '}' 或文件末尾，同时维护行号
        pos++; // 跳过 开括号
        while (pos < source.length()) {
            char c = source.charAt(pos);
            if (c == '\n') line++;
            if (c == '}') { pos++; return; }
            pos++;
        }
    }

    private Token readString() {
        // 从开单引号开始读取直到闭合单引号（包含引号），简单处理，不处理转义序列
        int startLine = line;
        StringBuilder sb = new StringBuilder();
        sb.append('\'');
        pos++; // 跳过开引号
        while (pos < source.length()) {
            char c = source.charAt(pos);
            if (c == '\n') line++;
            sb.append(c);
            pos++;
            if (c == '\'') break; // 遇到闭合引号则结束
        }
        return new Token(startLine, sb.toString(), "字符串");
    }

    private Token readIdOrKeyword() {
        // 从字母或下划线开始，读取连续的字母数字或下划线组成标识符/关键字
        int startLine = line;
        StringBuilder sb = new StringBuilder();
        while (pos < source.length()) {
            char c = source.charAt(pos);
            if (Character.isLetterOrDigit(c) || c == '_') {
                sb.append(c);
                pos++;
            } else {
                break;
            }
        }
        String word = sb.toString();
        // 关键字比较使用小写形式，因此 keywords 应存放小写词汇
        String type = keywords.contains(word.toLowerCase()) ? "关键字" : "标识符";
        return new Token(startLine, word, type);
    }

    private Token readNumber() {
        // 读取连续数字序列作为无符号整数（未处理小数、负数或溢出）
        int startLine = line;
        StringBuilder sb = new StringBuilder();
        while (pos < source.length() && Character.isDigit(source.charAt(pos))) {
            sb.append(source.charAt(pos));
            pos++;
        }
        return new Token(startLine, sb.toString(), "无符号整数");
    }
}
