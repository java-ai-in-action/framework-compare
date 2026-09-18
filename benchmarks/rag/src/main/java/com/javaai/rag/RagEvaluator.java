package com.javaai.rag;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * RAG 检索评估器：跑 100 题测试集，输出分层 Hit Rate@K
 *
 * 用法：
 *   mvn -q compile exec:java                          # 默认 K=5
 *   mvn -q compile exec:java -Dexec.args="10"         # K=10
 *   或打包后：java -jar rag-eval.jar 5
 *
 * 指标定义：
 *   HitRate@K = 前 K 条检索结果里，包含任一 expected_doc_id 的题目占比
 *   无答案题（answerable=false）不计入命中率，用于单独评估幻觉
 */
public class RagEvaluator {

    public static void main(String[] args) throws Exception {
        ObjectMapper om = new ObjectMapper();

        Models.Docs docsFile = om.readValue(new File("docs-sample.json"), Models.Docs.class);
        Models.TestSet ts = om.readValue(new File("testset-100.json"), Models.TestSet.class);

        int K = args.length > 0 ? Integer.parseInt(args[0]) : 5;

        HybridRetriever retriever = new HybridRetriever(docsFile.docs());

        // category -> [hit, total]
        Map<String, int[]> stat = new LinkedHashMap<>();
        int hitAll = 0, totalAns = 0;

        for (Models.Case c : ts.cases()) {
            if (!c.answerable()) continue;  // 无答案题不计入命中率

            List<String> hits = retriever.retrieve(c.question(), K);
            boolean hit = hits.stream().anyMatch(c.expectedDocIds()::contains);

            stat.computeIfAbsent(c.category(), x -> new int[2])[1]++;
            if (hit) {
                stat.get(c.category())[0]++;
                hitAll++;
            }
            totalAns++;
        }

        System.out.println();
        System.out.println("=== RAG 评估结果 · Hit Rate@" + K + " ===");
        System.out.printf("%-12s %6s %6s %10s%n", "类别", "命中", "总数", "命中率");
        System.out.println("-".repeat(38));
        for (Map.Entry<String, int[]> e : stat.entrySet()) {
            int h = e.getValue()[0], t = e.getValue()[1];
            System.out.printf("%-12s %6d %6d %9.1f%%%n", e.getKey(), h, t, 100.0 * h / t);
        }
        System.out.println("-".repeat(38));
        System.out.printf("%-12s %6d %6d %9.1f%%%n", "总计", hitAll, totalAns, 100.0 * hitAll / totalAns);
        System.out.println();
        System.out.println("💡 提示：把 HybridRetriever 换成你的真实检索实现（VectorStore + ES + Rerank），");
        System.out.println("   就能量化评估你的 RAG 系统，见证从 32% 到 89% 的每一步提升。");
    }
}
