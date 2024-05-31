package com.javacode.entity;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 *新闻
 */
@Table(name = "news")
public class News {
	/*
	 * 主键
	 * */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)   //自增
	private Long id;

	private String time;

	private String title;

	public Long getClick() {
		return click;
	}

	public void setClick(Long click) {
		this.click = click;
	}

	private Long click;

	private String content;

//	private Long views;

	private  Long likes;
	private  String type;
	private  String keyword;

	public String getKeywords() {
		return keyword;
	}

	public void setKeywords(String kesyword) {
		this.keyword = kesyword;
	}

//	// 获取新闻类型
//	public String getType() {
//		try {
//			// 将type字段解析为JSON对象
//			JSONObject json = new JSONObject(type);
//			// 获取type字段的值
//			return json.getString("type");
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return ""; // 返回一个空字符串作为默认值
//		}
//	}

//	// 获取新闻类型
	public String getType() {
		return type; // 直接返回type字段即可
	}



	public void setType(String type) {
		this.type = type;
	}

//	public Long getViews() {
//		return views;
//	}
//
//	public void setViews(Long views) {
//		this.views = views;
//	}

	public Long getLikes() {
		return likes;
	}

	public void setLikes(Long likes) {
		this.likes = likes;
	}

	/**
	 * 图片
	 */
	private String picture;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	@Transient
	private List<Long> fileList;

	public void setFileList(List<Long> fileList) {
		this.fileList = fileList;
	}

	public List<Long> getFileList() {
		return fileList;
	}
}