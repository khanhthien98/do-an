package com.example.helper.controller;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.helper.common.CommonUtils;
import com.example.helper.common.Constants;
import com.example.helper.controller.request.LoginRequest;
import com.example.helper.controller.response.LoginResponse;
import com.example.helper.controller.response.Response;
import com.example.helper.entity.Bill;
import com.example.helper.entity.BillDetail;
import com.example.helper.entity.Brand;
import com.example.helper.entity.Category;
import com.example.helper.entity.Contact;
import com.example.helper.entity.EmailJob;
import com.example.helper.entity.Product;
import com.example.helper.entity.User;
import com.example.helper.params.BillParam;
import com.example.helper.params.ProductParam;
import com.example.helper.repository.BillDetailRepo;
import com.example.helper.repository.BillRepo;
import com.example.helper.repository.BrandRepo;
import com.example.helper.repository.CategoryRepo;
import com.example.helper.repository.ConfigRepo;
import com.example.helper.repository.ContactRepo;
import com.example.helper.repository.EmailJobRepo;
import com.example.helper.repository.ProductRepo;
import com.example.helper.repository.UserRepo;
import com.example.helper.services.ServiceUser;
import com.google.gson.Gson;

@RestController
@RequestMapping(path = "/api/v1")
public class UserController {
	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private ServiceUser serviceUser;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private BrandRepo brandRepo;
	@Autowired
	private CommonUtils commonUtils;
	@Autowired
	private BillRepo billRepo;
	@Autowired
	private BillDetailRepo billDetailRepo;
	@Autowired
	private ConfigRepo configRepo;
	@Autowired
	private EmailJobRepo emailJobRepo;
	@Autowired
	private ContactRepo contactRepo;
	
	@GetMapping(value = "/product/{id}")
	public ResponseEntity<Response<Product>> getproduct(@PathVariable(name = "id") Long id) {
		Product product = productRepo.findById(id).orElse(null);
		return new ResponseEntity<Response<Product>>(new Response<Product>(product), HttpStatus.OK);
	}

	@GetMapping(value = "/products")
	public ResponseEntity<Response<Product>> getProducts(ProductParam productParam) {
		int page = productParam.getPageIndex() - 1;
		int size = productParam.getPageSize();
		Sort sortable = null;
		if (productParam.getSortField() != null && !productParam.getSortField().equalsIgnoreCase("null")) {
			if (productParam.getSortOrder().equals("ascend")) {
				sortable = Sort.by(productParam.getSortField()).ascending();
			}
			if (productParam.getSortOrder().equals("descend")) {
				sortable = Sort.by(productParam.getSortField()).descending();
			}
		} else {
			sortable = Sort.by("productID").descending();
		}
		Pageable pageable = PageRequest.of(page, size, sortable);
		Page<Product> pageBrandPage = serviceUser.findProduct(productParam, pageable);
		List<Product> lists = pageBrandPage.toList();
		Long count = (long) pageBrandPage.getTotalElements();
		return new ResponseEntity<Response<Product>>(new Response<Product>(count, lists), HttpStatus.OK);
	}

	// category service

	@GetMapping(value = "/brands/all")
	public ResponseEntity<Response<Brand>> getBrandAll() {
		List<Brand> lists = brandRepo.findAll();
		Long count = (long) lists.size();
		return new ResponseEntity<Response<Brand>>(new Response<Brand>(count, lists), HttpStatus.OK);
	}

	@GetMapping(value = "/categories/all")
	public ResponseEntity<Response<Category>> getCategoryAll() {
		List<Category> lists = categoryRepo.findAll();
		Long count = (long) lists.size();
		return new ResponseEntity<Response<Category>>(new Response<Category>(count, lists), HttpStatus.OK);
	}

	// user service

	@PostMapping(value = "/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		if (userRepo.loginUser(loginRequest) != Constants.LOGIN_FAIL) {
			String auth;
			try {
				auth = commonUtils.createToken(loginRequest.getUsername(), loginRequest.getPassword(), "0");
			} catch (Exception e) {
				return new ResponseEntity<LoginResponse>(new LoginResponse("false", ""), HttpStatus.OK);
			}
			return new ResponseEntity<LoginResponse>(
					new LoginResponse(auth, loginRequest.getUsername(), userRepo.loginUser(loginRequest)),
					HttpStatus.OK);
		}
		return new ResponseEntity<LoginResponse>(new LoginResponse("false", ""), HttpStatus.OK);

	}

	@PostMapping(value = "/user")
	public ResponseEntity<Response<User>> saveUser(@RequestBody User user) {
		if (user != null) {
			if (userRepo.checkUser(user.getUserName()) == Constants.LOGIN_SUCCESS) {
				return new ResponseEntity<Response<User>>(new Response<User>("Tài khoản đã tồn tại", "10002"),
						HttpStatus.OK);
			}
			User user2 = userRepo.save(user);
			if (user2 != null && user.getUserID() == null) {
				userRepo.updatePassword(user.getUserPass(), user2.getUserID());
			}
			return new ResponseEntity<Response<User>>(new Response<User>(user2), HttpStatus.OK);
		}
		return new ResponseEntity<Response<User>>(new Response<User>("loi", "10001"), HttpStatus.OK);
	}

	@GetMapping(value = "/user/{id}")
	public ResponseEntity<Response<User>> getUser(@PathVariable(name = "id") Long id) {
		User user = userRepo.findById(id).orElse(null);
		return new ResponseEntity<Response<User>>(new Response<User>(user), HttpStatus.OK);
	}

	// payment service
	@PostMapping(value = "/bill")
	public ResponseEntity<Response<Bill>> saveBill(@RequestBody Bill bill) {
		if (bill != null) {
			Gson g = new Gson();
			bill.setDate(new Date());
			bill.setStatus(Constants.PROCESS);
			Bill bill2 = billRepo.save(bill);
			StringBuilder content = new StringBuilder();

			Product[] products = g.fromJson(bill.getProducts(), Product[].class);

			for (Product product : products) {
				BillDetail billDetail = new BillDetail();
				billDetail.setBill(bill2);
				billDetail.setPrice(product.getProductPrice());
				billDetail.setProduct(product);
				billDetail.setQuantity(product.getQuanlityBuy());
				content.append("<p style=\"margin-left:40px\">");
				content.append(product.getProductName());
				content.append( " : ");
				content.append(product.getQuanlityBuy());
				content.append(" (sp)</p>");
				// save bill
				billDetailRepo.save(billDetail);
				// save product
				product.setProductQuantily(product.getProductQuantily() - product.getQuanlityBuy());
				productRepo.save(product);
			}

			// save to email job
//			EmailJob emailJob = new EmailJob();
//			String subject = configRepo.getByName("subject").getValue().replace("__idBill__",
//					String.valueOf(bill2.getBillID()));
//			emailJob.setSubject(subject);
//			emailJob.setUser(bill.getUser());
//			emailJob.setContent(
//					configRepo.getByName("content").getValue().replace("__name__", bill2.getName())
//							.replace("__total__",NumberFormat.getInstance().format(bill.getTotal())).replace("__content__", content.toString()));
//			emailJobRepo.save(emailJob);
			return new ResponseEntity<Response<Bill>>(new Response<Bill>(bill2), HttpStatus.OK);
		}
		return new ResponseEntity<Response<Bill>>(new Response<Bill>("loi", "10001"), HttpStatus.OK);
	}

	// bill service
	@GetMapping(value = "/bill-detail/{id}")
	public ResponseEntity<Response<BillDetail>> getBillDetail(@PathVariable(name = "id") Long id) {
		Bill bill = new Bill();
		bill.setBillID(id);
		List<BillDetail> billDetails = billDetailRepo.findByBill(bill);
		Long count = (long) billDetails.size();
		return new ResponseEntity<Response<BillDetail>>(new Response<BillDetail>(count, billDetails), HttpStatus.OK);
	}

	@GetMapping(value = "/bills")
	public ResponseEntity<Response<Bill>> getBills(BillParam billParam) {
		int page = billParam.getPageIndex() - 1;
		int size = billParam.getPageSize();
		Sort sortable = null;
		if (billParam.getSortField() != null && !billParam.getSortField().equalsIgnoreCase("null")) {
			if (billParam.getSortOrder().equals("ascend")) {
				sortable = Sort.by(billParam.getSortField()).ascending();
			}
			if (billParam.getSortOrder().equals("descend")) {
				sortable = Sort.by(billParam.getSortField()).descending();
			}
		} else {
			sortable = Sort.by("billID").descending();
		}
		Pageable pageable = PageRequest.of(page, size, sortable);
		Page<Bill> pageBrandPage = serviceUser.findBill(billParam, pageable);
		List<Bill> lists = pageBrandPage.toList();
		Long count = (long) pageBrandPage.getTotalElements();
		return new ResponseEntity<Response<Bill>>(new Response<Bill>(count, lists), HttpStatus.OK);
	}
	// contact service 
	@PostMapping(value = "/contact")
	public ResponseEntity<Response<Contact>> saveContact(@RequestBody Contact contact) {
		if (contact != null) {
			contact.setDate(new Date());
			Contact contact2 = contactRepo.save(contact);
			return new ResponseEntity<Response<Contact>>(new Response<Contact>(contact2), HttpStatus.OK);
		}
		return new ResponseEntity<Response<Contact>>(new Response<Contact>("loi", "10001"), HttpStatus.OK);
	}

}
