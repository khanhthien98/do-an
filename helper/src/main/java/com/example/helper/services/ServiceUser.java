package com.example.helper.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.example.helper.entity.Bill;
import com.example.helper.entity.Brand;
import com.example.helper.entity.Category;
import com.example.helper.entity.Product;
import com.example.helper.entity.User;
import com.example.helper.params.BillParam;
import com.example.helper.params.ProductParam;
import com.example.helper.repository.BillRepo;
import com.example.helper.repository.ProductRepo;
import com.example.helper.repository.UserRepo;

@Component
public class ServiceUser {
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private BillRepo billRepo;
	
	@SuppressWarnings("serial")
	public Page<Product> findProduct(ProductParam productParam, Pageable pageable) {

		Page<Product> listPage = productRepo.findAll(new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Join<Product, Category> cateJoin = root.join("category",
						JoinType.LEFT);
				Join<Product, Brand> brandJoin = root.join("brand",
						JoinType.LEFT);
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				if (productParam.getCategoryID() > 0) {
					predicates.add(cb.and(cb.equal(root.get("category").get("categoryID"), productParam.getCategoryID())));
				}
				if (productParam.getBrandID() > 0) {
					predicates.add(cb.and(cb.equal(root.get("brand").get("brandID"), productParam.getBrandID())));
				}
				if (productParam.getProductName() != null) {
					predicates
							.add(cb.and(cb.like(cb.upper(root.<String>get("productName")), "%" + productParam.getProductName().trim().toUpperCase() + "%")));
				}
				if (productParam.getPriceFrom() > 0) {
					predicates.add(
							cb.and(cb.greaterThanOrEqualTo(root.get("productPrice"), productParam.getPriceFrom())));
				}
				if (productParam.getPriceTo() > 0) {
					predicates.add(cb.and(cb.lessThanOrEqualTo(root.get("productPrice"), productParam.getPriceTo())));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return listPage;
	}
	
	@SuppressWarnings("serial")
	public Page<Bill> findBill(BillParam billParam, Pageable pageable) {

		Page<Bill> listPage = billRepo.findAll(new Specification<Bill>() {
			@Override
			public Predicate toPredicate(Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Join<Bill, User> usJoin = root.join("user", JoinType.LEFT);

				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				if (billParam.getBillID() > 0) {
					predicates.add(cb.and(cb.equal(root.get("billID"), billParam.getBillID())));
				}
				if (billParam.getUserId() > 0) {
					predicates.add(cb.and(cb.equal(root.get("user").<String>get("userID"),
							billParam.getUserId())));
				}
				if (billParam.getUserName() != null) {
					predicates.add(cb.and(cb.like(cb.upper(root.get("user").<String>get("userName")),
							"%" + billParam.getUserName().trim().toUpperCase() + "%")));
				}
				if (billParam.getPriceFrom() > 0) {
					predicates.add(cb.and(cb.greaterThanOrEqualTo(root.get("total"), billParam.getPriceFrom())));
				}
				if (billParam.getPriceTo() > 0) {
					predicates.add(cb.and(cb.lessThanOrEqualTo(root.get("total"), billParam.getPriceTo())));
				}
				if (billParam.getFromDate() != null) {
					predicates.add(cb.and(cb.greaterThanOrEqualTo(root.get("date"), billParam.getFromDate())));
				}
				if (billParam.getToDate() != null) {
					predicates.add(cb.and(cb.lessThanOrEqualTo(root.get("date"), billParam.getToDate())));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return listPage;
	}
}
