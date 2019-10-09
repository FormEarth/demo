package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.example.demo.service.MongoDBService;

public class MongoDBServiceImpl<T> implements MongoDBService<T> {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void saveTest(T test) {
		mongoTemplate.save(test);
	}

	@Override
	public T findTestById(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		T mgt = mongoTemplate.findOne(query, null);
		return mgt;
	}

	@Override
	public void updateTest(T test) {
		Query query = new Query(Criteria.where("id").is(""));
		Update update = new Update().set("age", "").set("name", "");
		// 更新查询返回结果集的第一条
		mongoTemplate.updateFirst(query, update, test.getClass());
		// 更新查询返回结果集的所有
		// mongoTemplate.updateMulti(query,update,TestEntity.class);
	}

	@Override
	public void deleteTestById(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		mongoTemplate.remove(query);
	}

}
