package com.zhailr.caipiao.model.bean;

import java.io.Serializable;

public class PayType implements Serializable {
  
  private static final long serialVersionUID = -5686226594415665388L;  
  public String id;
  public String name;
  public String desc;
  public Integer flag;
  
  public PayType(String id, String name, String desc, Integer flag) {
	super();
	this.id = id;
	this.name = name;
	this.desc = desc;
	this.flag = flag;
}
  
  
  
  
}
