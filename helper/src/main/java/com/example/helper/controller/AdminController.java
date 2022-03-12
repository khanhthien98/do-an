package com.example.helper.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.helper.common.CommonUtils;
import com.example.helper.common.Constants;
import com.example.helper.controller.request.LoginRequest;
import com.example.helper.controller.response.LoginResponse;
import com.example.helper.controller.response.Response;
import com.example.helper.entity.Bill;
import com.example.helper.entity.BillDetail;
import com.example.helper.entity.Brand;
import com.example.helper.entity.Category;
import com.example.helper.entity.Product;
import com.example.helper.entity.User;
import com.example.helper.entity.UserAdmin;
import com.example.helper.params.BaseParam;
import com.example.helper.params.BillParam;
import com.example.helper.params.BrandParam;
import com.example.helper.params.CategoryParam;
import com.example.helper.params.ProductParam;
import com.example.helper.params.UserParam;
import com.example.helper.repository.BillDetailRepo;
import com.example.helper.repository.BillRepo;
import com.example.helper.repository.BrandRepo;
import com.example.helper.repository.CategoryRepo;
import com.example.helper.repository.ProductRepo;
import com.example.helper.repository.UserAdminRepo;
import com.example.helper.repository.UserRepo;
import com.example.helper.services.ServiceAdmin;

@RestController
@RequestMapping(path = "/api/admin/v1")
public class AdminController {
	@Autowired
	private UserAdminRepo userAdminRepo;
	@Autowired
	private CommonUtils commonUtils;
	@Autowired
	private BillDetailRepo billDetailRepo;
	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private BrandRepo brandRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private ServiceAdmin serviceAdmin;
	@Autowired
	private BillRepo billRepo;
	@Autowired
	private UserRepo userRepo;

	@GetMapping(value = "/useradmin/list")
	public ResponseEntity<Response<UserAdmin>> getUserAdminTest() {
		List<UserAdmin> listUserAdmins = userAdminRepo.findAll();
		Long count = Long.valueOf(listUserAdmins.size());
		return new ResponseEntity<Response<UserAdmin>>(new Response<UserAdmin>(count, listUserAdmins), HttpStatus.OK);
	}

	@PostMapping(value = "/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		if (userAdminRepo.loginAdmin(loginRequest) == Constants.LOGIN_SUCCESS) {
			String auth;
			try {
				auth = commonUtils.createToken(loginRequest.getUsername(), loginRequest.getPassword(), "1");
			} catch (Exception e) {
				return new ResponseEntity<LoginResponse>(new LoginResponse("false", ""), HttpStatus.OK);
			}
			return new ResponseEntity<LoginResponse>(new LoginResponse(auth, loginRequest.getUsername()),
					HttpStatus.OK);
		}
		return new ResponseEntity<LoginResponse>(new LoginResponse("false", ""), HttpStatus.OK);

	}

	// bill service
	@GetMapping(value = "/export-bills")
	public ResponseEntity<InputStreamResource> exportBill(BillParam billParam) {
		ByteArrayInputStream in;
		try {
			in = serviceAdmin.exportExcel(billParam);
			HttpHeaders headers = new HttpHeaders();
			String date = CommonUtils.StringFormatDate(new Date(), "dd/MM/yyyy");
			headers.add("Content-Disposition", "attachment; filename=BaoCao" + date + ".xlsx");
			return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping(value = "/bill-detail/{id}")
	public ResponseEntity<Response<BillDetail>> getBillDetail(@PathVariable(name = "id") Long id) {
		Bill bill = new Bill();
		bill.setBillID(id);
		List<BillDetail> billDetails = billDetailRepo.findByBill(bill);
		Long count = (long) billDetails.size();
		return new ResponseEntity<Response<BillDetail>>(new Response<BillDetail>(count, billDetails), HttpStatus.OK);
	}
	
	@PostMapping(value = "/cancel-bill")
	public ResponseEntity<Response<BillDetail>> cancelBill(@RequestBody Bill bill) {
		if(bill!=null) {
			// save bill	
			bill.setStatus(Constants.CANCEL);
			billRepo.save(bill);
			// refund product
			List<BillDetail> billDetails = billDetailRepo.findByBill(bill);
			for (BillDetail billDetail : billDetails) {
				Product product = productRepo.findByProductID(billDetail.getProduct().getProductID());
				product.setProductQuantily(product.getProductQuantily() + billDetail.getQuantity());
				productRepo.save(product);
			}
			return new ResponseEntity<Response<BillDetail>>(new Response<BillDetail>("1002","oke"), HttpStatus.OK);
		}
		return new ResponseEntity<Response<BillDetail>>(new Response<BillDetail>("loi", "10001"), HttpStatus.OK);
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
		Page<Bill> pageBrandPage = serviceAdmin.findBill(billParam, pageable);
		List<Bill> lists = pageBrandPage.toList();
		Long count = (long) pageBrandPage.getTotalElements();
		return new ResponseEntity<Response<Bill>>(new Response<Bill>(count, lists), HttpStatus.OK);
	}

	@DeleteMapping(value = "/bill/{id}")
	public ResponseEntity<Response<Bill>> deleteBill(@PathVariable(name = "id") Long id) {
		billRepo.deleteById(id);
		return new ResponseEntity<Response<Bill>>(new Response<Bill>("xoa thanh cong", "200"), HttpStatus.OK);
	}

	@PostMapping(value = "/bill")
	public ResponseEntity<Response<Bill>> saveBill(@RequestBody Bill bill) {
		if (bill != null) {
			Bill bill2 = billRepo.save(bill);
			return new ResponseEntity<Response<Bill>>(new Response<Bill>(bill2), HttpStatus.OK);
		}
		return new ResponseEntity<Response<Bill>>(new Response<Bill>("loi", "10001"), HttpStatus.OK);
	}

	@GetMapping(value = "/statistic-bill")
	public ResponseEntity<Response<List<String>>> getSatisticBill() {
		List<Long> countList = serviceAdmin.statisticBillByWeek();
		List<String> staticList = new ArrayList<String>();
		for (Long long1 : countList) {
			staticList.add(NumberFormat.getInstance().format(long1));
		}
		return new ResponseEntity<Response<List<String>>>(new Response<List<String>>(staticList), HttpStatus.OK);
	}

	// product service
	@GetMapping(value = "/product/{id}")
	public ResponseEntity<Response<Product>> getProduct(@PathVariable(name = "id") Long id) {
		Product product = productRepo.findById(id).orElse(null);
		return new ResponseEntity<Response<Product>>(new Response<Product>(product), HttpStatus.OK);
	}

	@GetMapping(value = "/export-products")
	public ResponseEntity<InputStreamResource> exportProduct() {
		ByteArrayInputStream in;
		try {
			ProductParam productParam = new ProductParam();
			in = serviceAdmin.exportExcelProduct(productParam);
			HttpHeaders headers = new HttpHeaders();
			String date = CommonUtils.StringFormatDate(new Date(), "dd/MM/yyyy");
			headers.add("Content-Disposition", "attachment; filename=BaoCao" + date + ".xlsx");
			return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping(value = "/product/statistic-brand")
	public ResponseEntity<Response<List<Long>>> getSatisticBrand() {
		List<Long> statics = new ArrayList<Long>();
		List<Brand> lists = brandRepo.findAll();
		for (Brand brand : lists) {
			if ((long) productRepo.findByBrand(brand).size() > 0) {
				statics.add((long) productRepo.findByBrand(brand).size());
			} else {
				statics.add(0L);
			}
		}
		return new ResponseEntity<Response<List<Long>>>(new Response<List<Long>>(statics), HttpStatus.OK);
	}

	@GetMapping(value = "/product/statistic-category")
	public ResponseEntity<Response<List<Long>>> getSatisticCategory() {
		List<Long> statics = new ArrayList<Long>();
		List<Category> lists = categoryRepo.findAll();
		Long count = (long) productRepo.findAll().size();
		for (Category category : lists) {
			if ((long) productRepo.findByCategory(category).size() > 0) {
				statics.add((long) ((productRepo.findByCategory(category).size() / (double) count) * 100));
			} else {
				statics.add(0L);
			}
		}
		return new ResponseEntity<Response<List<Long>>>(new Response<List<Long>>(statics), HttpStatus.OK);
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
		Page<Product> pageBrandPage = serviceAdmin.findProduct(productParam, pageable);
		List<Product> lists = pageBrandPage.toList();
		Long count = (long) pageBrandPage.getTotalElements();
		return new ResponseEntity<Response<Product>>(new Response<Product>(count, lists), HttpStatus.OK);
	}

	@PostMapping(value = "/product")
	public ResponseEntity<Response<Product>> saveProduct(@RequestBody Product product) {
		if (product != null) {
			Product product2 = productRepo.save(product);
			return new ResponseEntity<Response<Product>>(new Response<Product>(product2), HttpStatus.OK);
		}
		return new ResponseEntity<Response<Product>>(new Response<Product>("loi", "10001"), HttpStatus.OK);
	}

	@DeleteMapping(value = "/product/{id}")
	public ResponseEntity<Response<Product>> deleteProduct(@PathVariable(name = "id") Long id) {
		productRepo.deleteById(id);
		return new ResponseEntity<Response<Product>>(new Response<Product>("xoa thanh cong", "200"), HttpStatus.OK);
	}

	// brand service
	@GetMapping(value = "/brands")
	public ResponseEntity<Response<Brand>> getBrands(BrandParam brandParam) {
		int page = brandParam.getPageIndex() - 1;
		int size = brandParam.getPageSize();
		Sort sortable = null;
		if (brandParam.getSortField() != null && !brandParam.getSortField().equalsIgnoreCase("null")) {
			if (brandParam.getSortOrder().equals("ascend")) {
				sortable = Sort.by(brandParam.getSortField()).ascending();
			}
			if (brandParam.getSortOrder().equals("descend")) {
				sortable = Sort.by(brandParam.getSortField()).descending();
			}
		} else {
			sortable = Sort.by("brandID").descending();
		}
		Pageable pageable = PageRequest.of(page, size, sortable);
		Page<Brand> pageBrandPage = serviceAdmin.findBrand(null, pageable);
		List<Brand> lists = pageBrandPage.toList();
		Long count = (long) pageBrandPage.getTotalElements();
		return new ResponseEntity<Response<Brand>>(new Response<Brand>(count, lists), HttpStatus.OK);
	}

	@GetMapping(value = "/brands/all")
	public ResponseEntity<Response<Brand>> getBrandAll() {
		List<Brand> lists = brandRepo.findAll();
		Long count = (long) lists.size();
		return new ResponseEntity<Response<Brand>>(new Response<Brand>(count, lists), HttpStatus.OK);
	}

	@PostMapping(value = "/brand")
	public ResponseEntity<Response<Brand>> saveBrand(@RequestBody Brand brand) {
		if (brand != null) {
			Brand brand2 = brandRepo.save(brand);
			return new ResponseEntity<Response<Brand>>(new Response<Brand>(brand2), HttpStatus.OK);
		}
		return new ResponseEntity<Response<Brand>>(new Response<Brand>("loi", "10001"), HttpStatus.OK);
	}

	@DeleteMapping(value = "/brand/{id}")
	public ResponseEntity<Response<Brand>> deleteBrand(@PathVariable(name = "id") Long id) {
		brandRepo.deleteById(id);
		return new ResponseEntity<Response<Brand>>(new Response<Brand>("xoa thanh cong", "200"), HttpStatus.OK);
	}

	// category service
	@GetMapping(value = "/categories")
	public ResponseEntity<Response<Category>> getCategories(CategoryParam categoryParam) {
		int page = categoryParam.getPageIndex() - 1;
		int size = categoryParam.getPageSize();
		Sort sortable = null;
		if (categoryParam.getSortField() != null && !categoryParam.getSortField().equalsIgnoreCase("null")) {
			if (categoryParam.getSortOrder().equals("ascend")) {
				sortable = Sort.by(categoryParam.getSortField()).ascending();
			}
			if (categoryParam.getSortOrder().equals("descend")) {
				sortable = Sort.by(categoryParam.getSortField()).descending();
			}
		} else {
			sortable = Sort.by("categoryID").descending();
		}
		Pageable pageable = PageRequest.of(page, size, sortable);
		Page<Category> pageBrandPage = serviceAdmin.findCategory(null, pageable);
		List<Category> lists = pageBrandPage.toList();
		Long count = (long) pageBrandPage.getTotalElements();
		return new ResponseEntity<Response<Category>>(new Response<Category>(count, lists), HttpStatus.OK);
	}

	@GetMapping(value = "/categories/all")
	public ResponseEntity<Response<Category>> getCategoryAll() {
		List<Category> lists = categoryRepo.findAll();
		Long count = (long) lists.size();
		return new ResponseEntity<Response<Category>>(new Response<Category>(count, lists), HttpStatus.OK);
	}

	@PostMapping(value = "/category")
	public ResponseEntity<Response<Category>> saveCategory(@RequestBody Category category) {
		if (category != null) {
			Category category2 = categoryRepo.save(category);
			return new ResponseEntity<Response<Category>>(new Response<Category>(category2), HttpStatus.OK);
		}
		return new ResponseEntity<Response<Category>>(new Response<Category>("loi", "10001"), HttpStatus.OK);
	}

	@DeleteMapping(value = "/category/{id}")
	public ResponseEntity<Response<Category>> deleteCategory(@PathVariable(name = "id") Long id) {
		categoryRepo.deleteById(id);
		return new ResponseEntity<Response<Category>>(new Response<Category>("xoa thanh cong", "200"), HttpStatus.OK);
	}

	// update image
	@RequestMapping(path = "/cms_anh/{id}", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity<Response<String>> fileUpload(@PathVariable(name = "id") Long id,
			@RequestParam("file") MultipartFile multipartFile) {
		String rootFileUpload = Constants.ROOT_IMAGES_BACKEND;
		String rootFileUpload1 = Constants.ROOT_IMAGES_FRONTEND;
//		String rootFileUpload = "/home/app/bidv_run";
		Product product = productRepo.findById(id).orElse(null);

		if (product != null) {
			String originalFilename = multipartFile.getOriginalFilename();
			product.setProductImage("images/" + originalFilename);
			File file = new File(rootFileUpload + originalFilename);
			if (file.getParentFile().exists() == false) {
				file.getParentFile().mkdirs();
			}
			File file1 = new File(rootFileUpload1 + originalFilename);
			if (file1.getParentFile().exists() == false) {
				file1.getParentFile().mkdirs();
			}
			try {
				try (InputStream is = multipartFile.getInputStream()) {
					try (OutputStream os = new FileOutputStream(file)) {
						byte[] b = new byte[10240];
						int size = 0;
						while ((size = is.read(b)) != -1) {
							os.write(b, 0, size);
						}
					}

					is.close();
				}
				try (InputStream is1 = multipartFile.getInputStream()) {
					try (OutputStream os = new FileOutputStream(file1)) {
						byte[] b = new byte[10240];
						int size = 0;
						while ((size = is1.read(b)) != -1) {
							os.write(b, 0, size);
						}
					}
					is1.close();
				}
				// save product
				productRepo.save(product);
				return new ResponseEntity<Response<String>>(new Response<String>("Update thành công"), HttpStatus.OK);
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<Response<String>>(new Response<String>("Có lỗi"), HttpStatus.BAD_REQUEST);
			}
		}
		return ResponseEntity.notFound().build();
	}

	// user service
	@GetMapping(value = "/users")
	public ResponseEntity<Response<User>> getUsers(UserParam userParam) {
		int page = userParam.getPageIndex() - 1;
		int size = userParam.getPageSize();
		Sort sortable = null;
		if (userParam.getSortField() != null && !userParam.getSortField().equalsIgnoreCase("null")) {
			if (userParam.getSortOrder().equals("ascend")) {
				sortable = Sort.by(userParam.getSortField()).ascending();
			}
			if (userParam.getSortOrder().equals("descend")) {
				sortable = Sort.by(userParam.getSortField()).descending();
			}
		} else {
			sortable = Sort.by("userID").descending();
		}
		Pageable pageable = PageRequest.of(page, size, sortable);
		Page<User> pageBrandPage = serviceAdmin.findUser(userParam, pageable);
		List<User> lists = pageBrandPage.toList();
		Long count = (long) pageBrandPage.getTotalElements();
		return new ResponseEntity<Response<User>>(new Response<User>(count, lists), HttpStatus.OK);
	}

	@PostMapping(value = "/user")
	public ResponseEntity<Response<User>> saveUser(@RequestBody User user) {
		if (user != null) {
			User product2 = userRepo.save(user);
			return new ResponseEntity<Response<User>>(new Response<User>(product2), HttpStatus.OK);
		}
		return new ResponseEntity<Response<User>>(new Response<User>("loi", "10001"), HttpStatus.OK);
	}

	@DeleteMapping(value = "/user/{id}")
	public ResponseEntity<Response<User>> deleteUser(@PathVariable(name = "id") Long id) {
		userRepo.deleteById(id);
		return new ResponseEntity<Response<User>>(new Response<User>("xoa thanh cong", "200"), HttpStatus.OK);
	}

}
