package com.yl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yl.config.JsonLongSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class BaseEntity implements Serializable {

//	@TableId(value = "id", type = IdType.AUTO)
	@JsonSerialize(using = JsonLongSerializer.class )		//Long类型长度过长，导致精度丢失方案
	private Long id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private LocalDateTime createTime;				//创建时间

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private LocalDateTime updateTime;				//更新时间

	private Integer status;				//状态

	//分页参数
	@TableField(exist = false)
	private int current;			//页码

	@TableField(exist = false)
	private int size;				//每页展示条数

	@TableField(exist = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private LocalTime startTime;		//开始时间yyyy-mm-dd

	@TableField(exist = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private LocalTime endTime;			//结束时间yyyy-mm-dd

}
