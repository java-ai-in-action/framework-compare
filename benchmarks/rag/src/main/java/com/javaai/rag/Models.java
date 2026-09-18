package com.javaai.rag;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/** 数据模型（对应 docs-sample.json / testset-100.json） */
public class Models {

    public record Doc(String id, String title, String content, List<String> keywords, Boolean authoritative) {}

    public record Docs(List<Doc> docs) {}

    public record Case(
            String id,
            String category,
            String question,
            @JsonProperty("expected_doc_ids") List<String> expectedDocIds,
            boolean answerable) {}

    public record Meta(String name, int size, Object categories, String metric, String note) {}

    public record TestSet(Meta meta, List<Case> cases) {}
}
