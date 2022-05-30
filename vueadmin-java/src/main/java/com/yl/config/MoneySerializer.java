package com.yl.config;



import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * @author ：jerry
 * @date ：Created in 2022/4/13 10:47
 * @description：Long类型的金额字段序列化
 * @version: V1.1
 * 使用方式：加注解到对应金额字段：@JsonSerialize(using = MoneySerializer.class)
 *   //金额：存需要乘100，显示会自动序列化除以100，无需处理
 */

@EnableWebMvc
@Configuration
public class MoneySerializer extends JsonSerializer<Long> {

    /**
     * 缩放倍数，调整需要慎重，目前会影响到的有两处：
     * 1、金额
     * 2、库存
     */
    public static final BigDecimal MULTIPLE = BigDecimal.valueOf(100);

    @Override
    public void serialize(Long num, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (num != null) {
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMinimumFractionDigits(2);
            gen.writeString(numberFormat.format(this.reduce(num)));
        }
    }

    /**
     * 缩小（四舍五入，保留两位小数）
     *
     * @param num 需要缩小的数值
     * @return
     */
    public Double reduce(Long num) {
        return reduce(num, 2);
    }

    /**
     * 缩小(四舍五入)
     *
     * @param num   需要缩小的数值
     * @param scale 小数点后位数
     * @return
     */
    public Double reduce(Long num, int scale) {
        return num == null ? null : BigDecimal.valueOf(num).divide(MULTIPLE, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
