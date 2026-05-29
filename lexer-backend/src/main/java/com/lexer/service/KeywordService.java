package com.lexer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class KeywordService {
    // 管理关键字集合的服务类：
    // - 启动时从配置的 JSON 文件加载关键字（默认 keywords.json），若不存在则使用内置默认关键字并将其保存到文件
    // - 提供对关键字的查询、增删改操作，所有写操作会持久化回文件
    // 注意：关键字在内部以小写形式保存以便忽略大小写比较

    @Value("${lexer.keyword-file:keywords.json}")
    private String keywordFile;

    // 返回内部关键字集合（直接返回引用，调用方不应修改）
    @Getter
    private Set<String> keywords = new LinkedHashSet<>();
    private final ObjectMapper mapper = new ObjectMapper();

    private static final List<String> DEFAULT_KEYWORDS = Arrays.asList(
        "program", "var", "integer", "array", "of", "begin", "end",
        "if", "then", "else", "while", "do", "for", "to", "downto",
        "string", "real", "char", "boolean", "function", "procedure",
        "const", "type", "record", "and", "or", "not", "div", "mod"
    );

    @PostConstruct
    public void init() {
        // 程序初始化时尝试从外部文件加载关键字列表；
        // - 若文件存在且可解析，则使用文件数据；
        // - 否则回退到 DEFAULT_KEYWORDS 并在首次运行时将其写入文件以便后续修改持久化
        File file = new File(keywordFile);
        if (file.exists()) {
            try {
                keywords = new LinkedHashSet<>(mapper.readValue(file, new TypeReference<List<String>>() {}));
            } catch (IOException e) {
                // 如果读取失败（例如 JSON 格式错误），退回到内置默认集合
                keywords = new LinkedHashSet<>(DEFAULT_KEYWORDS);
            }
        } else {
            keywords = new LinkedHashSet<>(DEFAULT_KEYWORDS);
            // 首次启动时将默认关键字写入文件，方便用户后续编辑
            saveToFile();
        }
    }

    public boolean contains(String word) {
        return keywords.contains(word.toLowerCase());
    }

    public List<String> listAll() {
        return new ArrayList<>(keywords);
    }

    public boolean add(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return false;
        }
        boolean added = keywords.add(keyword.trim().toLowerCase());
        if (added) {
            saveToFile();
        }
        return added;
    }

    public void remove(String keyword) {
        // 从集合中移除关键字并持久化
        if (keyword != null) {
            keywords.remove(keyword.trim().toLowerCase());
            saveToFile();
        }
    }

    public void update(String oldKeyword, String newKeyword) {
        // update 操作通过先移除旧关键字再添加新关键字实现（会各自触发持久化）
        remove(oldKeyword);
        add(newKeyword);
    }

    private void saveToFile() {
        // 将当前关键字集合写回到配置文件（覆盖式写入），写失败时打印栈跟踪
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(keywordFile), new ArrayList<>(keywords));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
