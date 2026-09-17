package com.javaai.demo.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * Step 3：Function Calling 工具集
 *
 * 配套文章：篇3《Spring AI 2.0 GA 实战：从 0 到 1 搭建企业级 ChatClient》
 *
 * 关键认知：
 *   1. @Tool 的 description 必须写清楚 —— 这是模型判断"要不要调用"的唯一依据
 *   2. 工具方法要幂等，模型可能重复调用
 *   3. 危险操作（删除/转账）永远不要直接暴露成 Tool
 */
@Component
public class OrderTools {

    /** 演示用：真实场景应查数据库 */
    @Tool(description = "根据订单号查询订单状态，返回 PENDING/SHIPPED/DELIVERED 等状态")
    public String queryOrderStatus(
            @ToolParam(description = "订单号，例如 A12345") String orderNo) {
        return switch (orderNo) {
            case "A12345" -> "SHIPPED（已发货，预计 2 天送达）";
            case "B67890" -> "DELIVERED（已签收）";
            default -> "PENDING（处理中）";
        };
    }

    @Tool(description = "查询指定 SKU 的当前库存数量")
    public int queryStock(
            @ToolParam(description = "商品 SKU 编码") String sku) {
        // 演示用：真实场景查库存服务
        return 42;
    }
}
