package com.example.helper.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.helper.entity.Bill;
import com.example.helper.entity.BillDetail;
@Repository
public interface BillDetailRepo extends JpaRepository<BillDetail, Long>{
	public List<BillDetail> findByBill(Bill bill);
}
